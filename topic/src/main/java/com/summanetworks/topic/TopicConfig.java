package com.summanetworks.topic;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;


import com.summanetworks.topic.exception.TopicException;
import org.apache.log4j.Logger;

/**
 * @author ajimenez, created on 15/3/20.
 */
public class TopicConfig {

    private static final Logger logger = Logger.getLogger(TopicConfig.class);

    private int peerId;
    private String localIp;
    private int localPort = 5500;

    private List<String> ips;

    // FIXME: 21/3/20 by Ajimenez - Determinar el tamaño usado para parsear el mensaje por el handler.
    private int maxTCPFrameSize = 2048;  //min 1024 , max 2048

    public void loadData() throws TopicException{
        // FIXME: 22/3/20 by Ajimenez - Validation.
        try {
            String peerIdString = System.getProperty("jboss.messaging.ServerPeerID");
            this.peerId = Integer.valueOf(peerIdString);

            this.localIp = System.getProperty("topic.local.ip", "0.0.0.0");
            try {
                InetAddress.getByName(localIp);
            } catch (UnknownHostException e) {
                logger.warn("topic.local.ip "+localIp+" system value invalid. Using 0.0.0.0");
                this.localIp = "0.0.0.0";
            }

            //Topic peers hosts separated by comma.
            String topicPeers = System.getProperty("topic.peers.host");
            ips = Arrays.asList(topicPeers.split(","));

        }catch(Exception e){
            throw new TopicException("Error getting values.",e);
        }
    }


    public int getPeerId() {
        return peerId;
    }

    public String getLocalIp(){
        return localIp;
    }
    public int getLocalPort(){
        return localPort;
    }

    public List<String> getIps() {
        return ips;
    }

    public int getMaxTCPFrameSize(){
        return maxTCPFrameSize;
    }
}
