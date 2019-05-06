package org.mobicents.protocols.ss7.m3ua.listener;

public interface AspStateChangeListener {
    void handleAspUp(String aspName);
    void handleAspUpAck(String aspName);
    void handleAspDown(String aspName);
    void handleAspDownAck(String aspName);
    void handleAspActive(String aspName);
    void handleAspActiveAck(String aspName);
    void handleAspInactive(String aspName);
    void handleAspInactiveAck(String aspName);
}