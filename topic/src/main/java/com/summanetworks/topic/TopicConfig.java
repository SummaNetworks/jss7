package com.summanetworks.topic;

import java.util.Arrays;
import java.util.List;

import com.summanetworks.topic.exception.TopicException;

/**
 * @author ajimenez, created on 15/3/20.
 */
public class TopicConfig {

    private int peerId;
    private String localIp;
    private int localPort = 5500;

    private List<String> ips;

    // FIXME: 21/3/20 by Ajimenez - Determinar el tamaño usado para parsear el mensaje por el handler.
    private int maxTCPFrameSize = 2048;  //min 1024 , max 2048

    public void loadData() throws TopicException{
        // FIXME: 22/3/20 by Ajimenez - Validation.
        try {
            this.peerId = Integer.valueOf(System.getProperty("topic.id"));

            this.localIp = System.getProperty("topic.local.ip", "0.0.0.0");

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
