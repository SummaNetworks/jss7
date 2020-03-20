package com.summanetworks.topic;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.sccp.impl.message.SccpDataMessageImpl;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 * @author ajimenez, created on 15/3/20.
 */
public class TopicController {

    private static final Logger logger = Logger.getLogger(TopicController.class);

    long localId = 100;
    TopicListener listener;
    TopicClient client;
    TopicServer server;
    //PeerId as KEY.
    Map<String, TopicHandler> handlerRegister = new HashMap<>();
    Map<String, TopicHandler> hostHandlerMap = new HashMap<>();

    public void init(){
        try {
            // 1 - Cuando sea instanciado leer la configuracion usando al TopicConfig

            // 2 - Iniciar el TopicServer
            server = new TopicServer();
            server.start(this);

            // 3 - Iniciar el TopicClient e intentar conectar los que no están conectados aun y están configurados.
            connectToPeers();

            // 3.1 Iniciar proceso de reconexion.
        }catch(Exception e){
            logger.error("Unexpected error starting TOPIC",e);
        }
    }

    private void connectToPeers(){
        String topicPeer = System.getProperty("topicPeer");
        if(topicPeer != null){
            client = new TopicClient();
            client.initConnection(topicPeer, 5500, this);
        }
    }


    protected void channelActive(TopicHandler handler){
        //Validate host.
        //Validate that is configured.
        //Validate that is not yet register (by client 4 example).
        hostHandlerMap.put(handler.getRemoteAddress(), handler);
    }


    protected TopicHandler registerHandler(long peerId, TopicHandler handler){
        hostHandlerMap.put(String.valueOf(peerId), handler);
        return handler;
    }
    protected void channelInvalid(TopicHandler handler){
        hostHandlerMap.remove(handler.getRemoteAddress());
    }

    //Custom message, should be in user part.
    public boolean sendMessage(int dialogId, SccpDataMessageImpl sccpDataMessage){
        TopicSccpMessage tm = new TopicSccpMessage(dialogId, sccpDataMessage);
        String peerId = String.valueOf(dialogId).substring(0,1);
        return sendMessage(peerId, tm);
    }

    //Generic for library.
    public boolean sendMessage(String peerId, TopicSccpMessage message){
        try {
            TopicHandler th = handlerRegister.get(peerId);
            if(th == null) {
                return false;
            }
            ByteBuffer bb = ByteBuffer.wrap(message.toByte());
            th.write(bb);
            return true;
        }catch(Exception e){
            return false;
        }
    }

    public void onMessage(long peerId, TopicSccpMessage message){
        //Validate message?

        //Call listener.
        if(listener != null){
            listener.onMessage(peerId, message.id, message.localAddress, message.remoteAddress, message.data);
        }else{
            // FIXME: 20/3/20 by Ajimenez - Return error? Ignore?
        }
    }

    public void registerListener(TopicListener listener){
        this.listener = listener;
    }

    public interface TopicListener {
        void onMessage(long peerId, long dialogId, SccpAddress localAddress, SccpAddress remoteAddress, byte [] data);
    }



}
