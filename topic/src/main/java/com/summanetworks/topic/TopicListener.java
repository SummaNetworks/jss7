package com.summanetworks.topic;

import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 * @author ajimenez, created on 22/3/20.
 */
public interface TopicListener {

    void onMessage(long peerId, long dialogId, SccpAddress localAddress, SccpAddress remoteAddress, byte [] data);
}
