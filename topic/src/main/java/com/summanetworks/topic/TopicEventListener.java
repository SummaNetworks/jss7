package com.summanetworks.topic;

/**
 * @author ajimenez, created on 26/3/20.
 */
public interface TopicEventListener {

    void onUnableToConnect(String ip);

    void onConnected(String ip, int peerId);

    void onDisconnected(String ip, int peerId);

    void onClosedByHeartbeat(String host, int peerId);

    void onMessagesFromNotRegisteredPeer(int peerId);

}
