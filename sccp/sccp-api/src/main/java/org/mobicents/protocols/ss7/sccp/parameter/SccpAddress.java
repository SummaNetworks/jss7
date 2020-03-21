/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.protocols.ss7.sccp.parameter;

import org.mobicents.protocols.ss7.indicator.AddressIndicator;

/**
 * The interface Sccp address.
 *
 * @author baranowb
 */
public interface SccpAddress extends Parameter {
    /**
     * The constant CGA_PARAMETER_CODE.
     */
// calling party address
    int CGA_PARAMETER_CODE = 0x4;
    /**
     * The constant CDA_PARAMETER_CODE.
     */
// called party address
    int CDA_PARAMETER_CODE = 0x3;

    /**
     * Is translated boolean.
     *
     * @return the boolean
     */
    boolean isTranslated();

    /**
     * Sets translated.
     *
     * @param translated the translated
     */
    void setTranslated(boolean translated);

    /**
     * Gets address indicator.
     *
     * @return the address indicator
     */
    AddressIndicator getAddressIndicator();

    /**
     * Gets signaling point code.
     *
     * @return the signaling point code
     */
    int getSignalingPointCode();


    void setSubsystemNumber(int ssn);

    /**
     * Gets subsystem number.
     *
     * @return the subsystem number
     */
    int getSubsystemNumber();

    /**
     * Gets global title.
     *
     * @return the global title
     */
    GlobalTitle getGlobalTitle();


    /**
     * Gets incoming opc. Used to ORIGINATED routing type.
     *
     * @return the incoming opc
     */
    int getIncomingOpc();


    /**
     * Sets incoming pc. Used to ORIGINATED routing type.
     *
     * @param pc the pc
     */
    void setIncomingOpc(int pc);
}
