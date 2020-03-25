package com.summanetworks.topic;

import java.util.ArrayList;
import java.util.List;

import com.summanetworks.topic.exception.TopicException;
import org.apache.commons.validator.routines.InetAddressValidator;
import org.apache.log4j.Logger;

/**
 * @author ajimenez, created on 15/3/20.
 */
public class TopicConfig {

    private static final Logger logger = Logger.getLogger(TopicConfig.class);

    private static final int LOCAL_PEER_ID_MIN = 10;
    private static final int LOCAL_PEER_ID_MAX = 99;
    static final int PEER_ID_LENGTH = 2;

    private int localPeerId = 0;
    private String localIp = null;
    private int localPort = 5500;
    private List<String> peerAddresses;


    // FIXME: 21/3/20 by Ajimenez - Determinar el tamaño usado para parsear el mensaje por el handler.
    private int maxTCPFrameSize = 2048;  //min 1024 , max 2048

//    @Deprecated
//    public void loadData() throws TopicException{
//        try {
//            String peerIdString = System.getProperty("jboss.messaging.ServerPeerID");
//            this.localPeerId = Integer.valueOf(peerIdString);
//
//            this.localIp = System.getProperty("topic.local.ip", "0.0.0.0");
//
//            if(!InetAddressValidator.getInstance().isValidInet4Address(localIp) &&
//                    !InetAddressValidator.getInstance().isValidInet6Address(localIp)){
//                logger.warn("topic.local.ip "+localIp+" system value invalid. Using 0.0.0.0");
//                this.localIp = "0.0.0.0";
//            }
//            //Topic peers hosts separated by comma.
//            String topicPeers = System.getProperty("topic.peers.host");
//            String [] ipArray = topicPeers.split(",");
//            peerAddresses = new ArrayList<>();
//            for(String ip : ipArray){
//                if(InetAddressValidator.getInstance().isValidInet4Address(localIp) ||
//                        InetAddressValidator.getInstance().isValidInet6Address(localIp)){
//                    peerAddresses.add(ip);
//                } else {
//                  logger.warn(String.format("Invalid ip %s found in topic.peers.host", ip));
//                }
//            }
//        }catch(Exception e){
//            throw new TopicException("Error getting values.",e);
//        }
//    }

    public boolean isValid(){
        if(localPeerId == 0){
            return false;
        }
        if(localIp == null){
            localIp = "0.0.0.0";
            logger.warn("Local IP not given. Using "+localIp);
        }
        if(peerAddresses.isEmpty()){
            logger.warn("No peers IP found. Acting as server.");
        }
        return true;
    }

    public void setLocalPeerId(int localPeerId) throws TopicException {
        if(localPeerId < LOCAL_PEER_ID_MIN || localPeerId > LOCAL_PEER_ID_MAX ){
            throw new TopicException("Invalid peerId. Must be between 100 and 999");
        }
        this.localPeerId = localPeerId;
    }

    public void setLocalIp(String localIp) throws TopicException {
        if(localIp == null){
            throw new TopicException("Invalid ip given null");
        }
        if (!InetAddressValidator.getInstance().isValidInet4Address(localIp) &&
                !InetAddressValidator.getInstance().isValidInet6Address(localIp)) {
            throw new TopicException("Invalid ip given " + localIp);
        } else {
            this.localIp = localIp;
        }
    }

    public void setLocalPort(int port){
        this.localPort = port;
    }

    public void setPeerAddresses(List<String> peerAddresses) throws TopicException {
        this.peerAddresses = new ArrayList<>();
        StringBuilder errors = new StringBuilder(0);
        for(String ip : peerAddresses){
            if(InetAddressValidator.getInstance().isValidInet4Address(localIp) ||
                    InetAddressValidator.getInstance().isValidInet6Address(localIp)){
                this.peerAddresses.add(ip);
            } else {
                if(errors.length() == 0)
                    errors.append("Invalid peers given: ");
                errors.append(ip).append(",");
            }
        }
        if(errors.length() > 0){
            throw new TopicException(errors.toString());
        }
    }


    public int getLocalPeerId() {
        return localPeerId;
    }

    public String getLocalIp(){
        return localIp;
    }
    public int getLocalPort(){
        return localPort;
    }

    public List<String> getPeerAddresses() {
        return peerAddresses;
    }

    public int getMaxTCPFrameSize(){
        return maxTCPFrameSize;
    }
}
