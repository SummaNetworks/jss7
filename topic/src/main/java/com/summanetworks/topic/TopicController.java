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

    // FIXME: 15/3/20 by Ajimenez -
    // 1 - Cuando sea instanciado leer la configuracion usando al TopicConfig
    // 2 - Iniciar el TopicServer
    // 3 - Iniciar el TopicClient e intentar conectar.


    //Ver como gestionar la conexion cruzada.
    //Conectar el client al recibir conexiones en el server? ¿Qué pasa si no se puede conectar en el otro sentido?
    //¿Enviar por el Client y escuhar por el server?
    //¿Usar una sola conexion?


    TopicClient client;
    TopicServer server;



    //PeerId as KEY.
    Map<String, TopicHandler> handlerRegister = new HashMap<>();

    public void init(){
        // 1 - Cuando sea instanciado leer la configuracion usando al TopicConfig
        // 2 - Iniciar el TopicServer
        // 3 - Iniciar el TopicClient e intentar conectar los que no están conectados aun y están configurados.
        // 3.1 Iniciar proceso de reconexion.

    }


    protected TopicHandler registerHandler(int peerId, TopicHandler handler){

        return handler;
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

    public void registerListener(int peerId){

    }

    public interface TopicListener {

        void onMessage(int dialogId, SccpAddress localAddress, SccpAddress remoteAddress);

    }



}
