package org.mobicents.protocols.ss7.m3ua.listener;

public interface AsStateChangeListener {
    void asDownToInactive(String asName);
    void asInactiveToActive(String asName);
    void asInactiveToInactive(String asName);
    void asPendingToActive(String asName);
    void asActiveToActiveNotifyAltAspAct(String asName);
    void asActiveToActiveNotifyInsAsp(String asName);
    void asActiveToActiveRemAspAct(String asName);
    void asActiveToActiveRemAspUp(String asName);
    void asActiveToPendingRemAspDown(String asName);
    void asActiveToPendingRemAspInactive(String asName);
}