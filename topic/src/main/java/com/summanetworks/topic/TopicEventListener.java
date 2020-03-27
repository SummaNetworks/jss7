package com.summanetworks.topic;

/**
 * @author ajimenez, created on 26/3/20.
 */
public interface TopicEventListener {

    void onPeerRegistered(int peerId);

    void onMessagesFromNotRegisteredPeer(int peerId);

}
