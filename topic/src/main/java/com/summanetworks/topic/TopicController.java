package com.summanetworks.topic;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.summanetworks.topic.exception.TopicException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mobicents.protocols.ss7.sccp.message.SccpDataMessage;

/**
 * @author ajimenez, created on 15/3/20.
 */
public class TopicController {

    private static final Logger logger = LogManager.getLogger(TopicController.class);

    private boolean started = false;

    private TopicConfig topicConfig;
    private TopicClient client;
    private TopicServer server;
    private HashMap<Integer, TopicListener> listenerMap = new HashMap<>();
    private TopicEventListener eventListener;
    private HashMap<String, TopicHandler> hostHandlerMap = new HashMap<>();
    private HashMap<Integer, TopicHandler> handlerRegisterMap = new HashMap<>();
    private ConcurrentHashMap<Integer, AtomicInteger> lostMessagesMap = new ConcurrentHashMap<>();

    ScheduledExecutorService executor;

    private TopicController(){
    }


    /**
     * Existen mas de un TCAP provider, uno por cada ssn.
     */
    private static TopicController instance;
    public static synchronized TopicController getInstance(){
        if(instance == null){
            instance = new TopicController();
        }
        return instance;
    }

    public void setTopicConfig(TopicConfig topicConfig){
        this.topicConfig = topicConfig;
    }

    public boolean isStarted(){
        return started;
    }

    public synchronized void init() throws TopicException {
        if (!started) {
            if (topicConfig == null) {
                throw new TopicException("TopicConfig was not set.");
            }
            if(!topicConfig.isValid()){
                throw new TopicException("TopicConfig not valid.");
            }
            try {
                started = true;
                server = new TopicServer();
                server.start(this);
                Thread.sleep(2000);
                connectToPeers();
                executor  = Executors.newScheduledThreadPool(2);
                startMetricsPrinter();
            }catch(Exception e){
                throw new TopicException("Unexpected error", e);
            }
        }
    }

    public TopicConfig getTopicConfig(){
        return topicConfig;
    }

    public synchronized void stop(){
        logger.info("Stop called. Stopping and cleaning resources...");
        if(started) {
            started = false;
            client.stop();
            server.stop();
            handlerRegisterMap.clear();
            hostHandlerMap.clear();
            lostMessagesMap.clear();
            if(executor != null && !executor.isShutdown())
                executor.shutdown();
            executor = null;
        }
    }

    private void connectToPeers(){
        if(topicConfig.getPeerAddresses() != null) {
            client = new TopicClient();
            if(topicConfig.getPeerAddresses() != null && !topicConfig.getPeerAddresses().isEmpty()) {
                for (String ip : topicConfig.getPeerAddresses()) {
                    if (!topicConfig.getLocalIp().equals(ip) && !"127.0.0.1".equals(ip) && !"0.0.0.0".equals(ip)) {
                        if (!hostHandlerMap.containsKey(ip)) {
                            client.initConnection(ip, topicConfig.getLocalPort(), this);
                        }
                    } else {
                        logger.debug("connectToPeer(): Ignoring " + ip);
                    }
                }
            } else {
                logger.warn("No peers address configured.");
            }
        }
    }

    /**
     * Connected but not yet registered.
     * @param handler
     * @return
     */
    protected TopicHandler connected(TopicHandler handler){
        logger.info(String.format("Remote host %s connected.", handler.getRemoteAddress()));
        //Validate host.
        //Validate that is configured.
        //Validate that is not yet register (by client 4 example).
        return hostHandlerMap.put(handler.getRemoteAddress(), handler);
    }

    protected void disconnected(TopicHandler handler){
        if(hostHandlerMap.get(handler.getRemoteAddress()) == handler) {
            logger.info(String.format("Remote host %s disconnected.", handler.getRemoteAddress()));
            hostHandlerMap.remove(handler.getRemoteAddress());
        }else{
            logger.debug("disconnected(): Current handler is not connected.");
        }
    }

    protected boolean isConnected(String host){
        return hostHandlerMap.containsKey(host);
    }

    protected TopicHandler getHandlerRegistered(int peerId){
        return handlerRegisterMap.get(peerId);
    }

    protected synchronized TopicHandler registerHandler(Integer peerId, TopicHandler handler){
        logger.info(String.format("Host %s with peerId %d registered.",handler.getRemoteAddress(), peerId));
        lostMessagesMap.remove(peerId);
        onRegister(handler.getRemoteAddress(), peerId);
        return handlerRegisterMap.put(peerId, handler);
    }

    protected void unregisterHandler(TopicHandler handler){
        if(handlerRegisterMap.get(handler.getRemotePeerId()) == handler ) {
            logger.info(String.format("Unregistering handler with peerId %d...", handler.getRemotePeerId()));
            handlerRegisterMap.remove(handler.getRemotePeerId());
            onDeregister(handler.getRemoteAddress(), handler.getRemotePeerId());
        } else if(handlerRegisterMap.get(handler.getRemotePeerId()) != null) {
            logger.debug(String.format("unregisterHandler(): Other handler is register with peerId %d.", handler.getRemotePeerId()));
        }else {
            logger.debug(String.format("unregisterHandler(): Handler with peerId %d not found in register.", handler.getRemotePeerId()));
        }
    }

    //Custom message, should be in user part.
    public boolean sendMessage(long dialogId, SccpDataMessage sccpDataMessage){
        if(this.isStarted()) {
            TopicSccpMessage tm = new TopicSccpMessage(dialogId, sccpDataMessage);
            if(dialogId >= TopicConfig.LOCAL_PEER_ID_MIN) {  //At least, bigger than prefix.
                String peerId = String.valueOf(dialogId).substring(0, TopicConfig.PEER_ID_LENGTH);
                return sendMessage(Integer.valueOf(peerId), tm);
            } else {
                return false;
            }
        }else{
            return false;
        }
    }

    //Generic for library.
    protected boolean sendMessage(Integer peerId, TopicSccpMessage message){
        try {
            TopicHandler th = handlerRegisterMap.get(peerId);
            if(th == null) {
                logger.debug("No peer found for id "+peerId+".");
                handleMessageForPeerNotRegistered(peerId);
                return false;
            }
            th.sendMessage(message);
            return true;
        }catch(Exception e){
            return false;
        }
    }

    public void onMessage(final int peerId, final TopicSccpMessage message){
        // TODO: 24/3/20 by Ajimenez -Validate message?

        final TopicListener listener = listenerMap.get(message.ssn);
        //Call listener.
        if(listener != null){
            //Remote and local addresses are expected interchanged.
            listener.onMessage(peerId, message.id, message.remoteAddress, message.localAddress, message.data);
        }else{
            logger.warn("TopicListener not registered to process message for SSN: "+message.ssn+". Ignoring message.");
        }
    }

    public void registerListener(TopicListener listener, int ssn){
        this.listenerMap.put(ssn, listener);
    }

    public void unregisterListener(int ssn){
        this.listenerMap.remove(ssn);
    }

    public void registerEventListener(TopicEventListener listener){
        eventListener = listener;
    }

    //Handle messages from an unknow peer, if number of messages are bigger than a limited, call listener.
    private synchronized void handleMessageForPeerNotRegistered(final Integer peerId) {
        AtomicInteger lostCounter = lostMessagesMap.get(peerId);
        if (lostCounter != null) {
            lostCounter.incrementAndGet();
        } else {
            lostCounter = new AtomicInteger(1);
            lostMessagesMap.put(peerId, lostCounter);
            //Schedule check in some seconds...
            executor.schedule(new Runnable() {
                @Override
                public void run() {
                    AtomicInteger lostCounter = lostMessagesMap.get(peerId);
                    if (lostCounter != null) {
                        int lostMessagesInt = lostCounter.get();
                        if (lostMessagesInt > topicConfig.getMaxPeerMsgLostByWindows()) {
                            logger.warn(String.format("Detected several messages lost [%d], addresses to peerId %d",
                                    lostCounter.get(), peerId));
                            if(eventListener != null){
                                //¿Usando el executor necesito otro hilo?
                                new Thread("UnknownPeer-"+peerId){
                                    @Override
                                    public void run() {
                                        eventListener.onMessagesFromNotRegisteredPeer(peerId);
                                    }
                                }.start();
                            }
                        } else {
                            logger.debug(String.format("Several message [%d] can not be sent to peerId %d but not reach the limit of %d," +
                                            " reset counter without notify.", lostMessagesInt,
                                    peerId, topicConfig.getMaxPeerMsgLostByWindows()));
                        }
                        lostMessagesMap.remove(peerId);
                    }
                }
            }, topicConfig.getPeerMsgLostWindowSeconds(), TimeUnit.SECONDS);
        }
    }

    private void startMetricsPrinter(){
        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                for(Map.Entry<Integer, TopicHandler> es : handlerRegisterMap.entrySet()){
                    try {
                        logger.info("Messages amount with peerId {}: {}; while the last minute.", es.getKey(), es.getValue().messagesReceivedCountAndReset());
                    }catch(Exception ignored){}
                }
            }
        }, 60, 60, TimeUnit.SECONDS);
    }

    //Event methods

    // FIXME: podria ser un onClose genérico?
    public void onClosedByHeartbeat(String ip, int peerId){
        if(eventListener != null)
            eventListener.onClosedByHeartbeat(ip, peerId);
    }

    //As client.
    public void onUnableToConnect(String ip){
        if(eventListener != null)
            eventListener.onUnableToConnect(ip);
    }

    public void onRegister(String ip, int peerId){
        if(eventListener != null)
            eventListener.onConnected(ip, peerId);
    }

    public void onDeregister(String ip, int peerId){
        if(eventListener != null)
            eventListener.onDisconnected(ip, peerId);
    }

}
