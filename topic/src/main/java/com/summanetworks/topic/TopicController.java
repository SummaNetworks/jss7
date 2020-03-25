package com.summanetworks.topic;

import java.util.HashMap;
import java.util.Map;

import com.summanetworks.topic.exception.TopicException;
import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.sccp.message.SccpDataMessage;

/**
 * @author ajimenez, created on 15/3/20.
 */
public class TopicController {

    private static final Logger logger = Logger.getLogger(TopicController.class);

    private boolean started = false;

    private Map<Integer, TopicListener> listenerMap = new HashMap<>();
    private TopicClient client;
    private TopicServer server;

    private Map<String, TopicHandler> handlerRegister = new HashMap<>();
    private Map<String, TopicHandler> hostHandlerMap = new HashMap<>();
    private TopicConfig topicConfig;

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
            handlerRegister.clear();
            hostHandlerMap.clear();
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

    protected void connected(TopicHandler handler){
        logger.info(String.format("Remote host %s connected", handler.getRemoteAddress()));
        //Validate host.
        //Validate that is configured.
        //Validate that is not yet register (by client 4 example).
        hostHandlerMap.put(handler.getRemoteAddress(), handler);
    }
    protected void disconnected(TopicHandler handler){
        logger.info(String.format("Remote host %s disconnected", handler.getRemoteAddress()));
        hostHandlerMap.remove(handler.getRemoteAddress());
    }

    protected boolean isConnected(String host){
        return hostHandlerMap.containsKey(host);
    }


    protected TopicHandler registerHandler(long peerId, TopicHandler handler){
        logger.info(String.format("Host %s registered with peerId %d registered ",handler.getRemoteAddress(), peerId));
        handlerRegister.put(String.valueOf(peerId), handler);
        return handler;
    }
    protected void channelInvalid(TopicHandler handler){
        hostHandlerMap.remove(handler.getRemoteAddress());
    }

    //Custom message, should be in user part.
    public boolean sendMessage(long dialogId, SccpDataMessage sccpDataMessage){
        TopicSccpMessage tm = new TopicSccpMessage(dialogId, sccpDataMessage);
        String peerId = String.valueOf(dialogId).substring(0,TopicConfig.PEER_ID_LENGTH);
        return sendMessage(peerId, tm);
    }

    //Generic for library.
    protected boolean sendMessage(String peerId, TopicSccpMessage message){
        try {
            TopicHandler th = handlerRegister.get(peerId);
            if(th == null) {
                logger.debug("No peer found for peer "+peerId);
                return false;
            }
            th.sendMessage(message);
            return true;
        }catch(Exception e){
            return false;
        }
    }

    public void onMessage(final long peerId, final TopicSccpMessage message){
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

    public void unRegisterListener(int ssn){
        this.listenerMap.remove(ssn);
    }

}
