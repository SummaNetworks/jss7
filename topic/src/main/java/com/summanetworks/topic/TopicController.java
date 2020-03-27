package com.summanetworks.topic;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.summanetworks.topic.exception.TopicException;
import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.sccp.message.SccpDataMessage;

/**
 * @author ajimenez, created on 15/3/20.
 */
public class TopicController {

    private static final Logger logger = Logger.getLogger(TopicController.class);

    private boolean started = false;

    private TopicConfig topicConfig;
    private TopicClient client;
    private TopicServer server;
    private HashMap<Integer, TopicListener> listenerMap = new HashMap<>();
    private TopicEventListener eventListener;
    private HashMap<String, TopicHandler> hostHandlerMap = new HashMap<>();
    private HashMap<Integer, TopicHandler> handlerRegisterMap = new HashMap<>();
    private ConcurrentHashMap<Integer, AtomicInteger> lostMessagesMap = new ConcurrentHashMap<>();

    private TopicController(){
    }


    /**
     * Existen mas de un TCAP provider, uno por cada ssn.
     */
    private static TopicController instance;
    public static TopicController getInstance(){
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
            }catch(Exception e){
                throw new TopicException("Unexpected error", e);
            }
        }
    }

    public TopicConfig getTopicConfig(){
        return topicConfig;
    }

    public synchronized void stop(){
        if(started) {
            started = false;
            client.stop();
            server.stop();
            handlerRegisterMap.clear();
            hostHandlerMap.clear();
            lostMessagesMap.clear();
        }
    }

    private void connectToPeers(){
        if(topicConfig.getPeerAddresses() != null) {
            client = new TopicClient();
            for (String ip : topicConfig.getPeerAddresses()) {
                if(!hostHandlerMap.containsKey(ip)) {
                    client.initConnection(ip, topicConfig.getLocalPort(), this);
                }
            }
        }
    }

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
        return handlerRegisterMap.put(peerId, handler);
    }
    protected void unregisterHandler(TopicHandler handler){
        if(handlerRegisterMap.get(handler.getRemotePeerId()) == handler ) {
            logger.info(String.format("Unregistering handler with peerId %d...", handler.getRemotePeerId()));
            handlerRegisterMap.remove(handler.getRemotePeerId());
        } else if(handlerRegisterMap.get(handler.getRemotePeerId()) != null) {
            logger.debug(String.format("unregisterHandler(): Other handler is register with peerId %d.", handler.getRemotePeerId()));
        }else {
            logger.debug(String.format("unregisterHandler(): Handler with peerId %d not found in register.", handler.getRemotePeerId()));
        }
    }

    protected void channelInvalid(TopicHandler handler){
        hostHandlerMap.remove(handler.getRemoteAddress());
    }

    //Custom message, should be in user part.
    public boolean sendMessage(long dialogId, SccpDataMessage sccpDataMessage){
        TopicSccpMessage tm = new TopicSccpMessage(dialogId, sccpDataMessage);
        String peerId = String.valueOf(dialogId).substring(0,TopicConfig.PEER_ID_LENGTH);
        return sendMessage(Integer.valueOf(peerId), tm);
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
            listener.onMessage(peerId, message.id, message.localAddress, message.remoteAddress, message.data);
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
    private synchronized void handleMessageForPeerNotRegistered(final Integer peerId){
        // Si recibo X mensajes mando 1 alerta, que se anota. Si se reinicia la conexión,
        // o finalmente se conecta el peer, se resetea la alerta.
        AtomicInteger ai = lostMessagesMap.get(peerId);
        if(ai == null){
            ai = new AtomicInteger(1);
            lostMessagesMap.put(peerId, ai);
        }
        int current = ai.get();
        if(current > topicConfig.getUnknownByPeerLimit()){
            logger.warn("Received several message from unregistered peer.");
            if(eventListener != null){
                new Thread("UnknownPeer-"+peerId){
                    @Override
                    public void run() {
                        eventListener.onMessagesFromNotRegisteredPeer(peerId);
                    }
                }.start();
            }
            //Message called. Set to 0 to avoid more calls.
            ai.set(0);
        }else if(current > 0){
            ai.incrementAndGet();
        }
    }

}
