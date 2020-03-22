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

    Map<Integer, TopicListener> listenerMap = new HashMap<>();
    TopicClient client;
    TopicServer server;
    //PeerId as KEY.
    Map<String, TopicHandler> handlerRegister = new HashMap<>();
    Map<String, TopicHandler> hostHandlerMap = new HashMap<>();
    TopicConfig topicConfig;
    private int peerLength = 0;

    private TopicController(){
        topicConfig = new TopicConfig();
    }


    /**
     * Existen mas de un TCAP provider, uno por cada ssn.
     */
    private static TopicController instance;
    public static TopicController getInstance(){
        if(instance == null){
            instance = new TopicController();
            instance.init();
        }
        return instance;
    }

    public synchronized void init(){
        try {
            if(!started) {
                started = true;
                // 1 - Cuando sea instanciado leer la configuracion usando al TopicConfig
                topicConfig.loadData();
                peerLength = String.valueOf(topicConfig.getPeerId()).length();

                // 2 - Iniciar el TopicServer
                server = new TopicServer();
                server.start(this);

                // 3 - Iniciar el TopicClient e intentar conectar los que no están conectados aun y están configurados.
                connectToPeers();
            }
        }catch(TopicException e){
            logger.warn("Error starting Topic.",e);
        }catch(Exception e){
            logger.error("Unexpected error starting TOPIC",e);
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
        if(topicConfig.getIps() != null) {
            client = new TopicClient();
            for (String ip : topicConfig.getIps()) {
                if(!hostHandlerMap.containsKey(ip)) {
                    client.initConnection(ip, 5500, this);
                }
            }
        }
    }

    protected void channelActive(TopicHandler handler){
        logger.debug(String.format("Remote host %s active", handler.getRemoteAddress()));
        //Validate host.
        //Validate that is configured.
        //Validate that is not yet register (by client 4 example).
        hostHandlerMap.put(handler.getRemoteAddress(), handler);
    }


    protected TopicHandler registerHandler(long peerId, TopicHandler handler){
        logger.debug(String.format("Host %s registered with peerId %d registered ",handler.getRemoteAddress(), peerId));
        handlerRegister.put(String.valueOf(peerId), handler);
        return handler;
    }
    protected void channelInvalid(TopicHandler handler){
        hostHandlerMap.remove(handler.getRemoteAddress());
    }

    //Custom message, should be in user part.
    public boolean sendMessage(long dialogId, SccpDataMessage sccpDataMessage){
        TopicSccpMessage tm = new TopicSccpMessage(dialogId, sccpDataMessage);
        String peerId = String.valueOf(dialogId).substring(0,peerLength);
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

    public void onMessage(long peerId, TopicSccpMessage message){
        //Validate message?


        TopicListener listener = listenerMap.get(message.ssn);
        //Call listener.
        if(listener != null){
            listener.onMessage(peerId, message.id, message.localAddress, message.remoteAddress, message.data);
        }else{
            // FIXME: 20/3/20 by Ajimenez - Return error? Ignore?
            logger.warn("TopicListener not registered to process message for SSN: "+message.ssn);
        }
    }

    public void registerListener(TopicListener listener, int ssn){
        this.listenerMap.put(ssn, listener);
    }

    public void unRegisterListener(int ssn){
        this.listenerMap.remove(ssn);
    }

}
