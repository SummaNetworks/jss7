/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
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
package org.mobicents.protocols.ss7.m3ua;

import java.util.List;

import org.mobicents.protocols.api.Association;
import org.mobicents.protocols.ss7.m3ua.parameter.ASPIdentifier;

/**
 *
 * @author amit bhayani
 * @author ajimenez
 *
 */
public interface AspFactory {

    String getName();

    Association getAssociation();

    List<Asp> getAspList();

    boolean getStatus();

    Functionality getFunctionality();

    IPSPType getIpspType();

    ASPIdentifier getAspid();

    boolean isHeartBeatEnabled();

    int getMaxOutboundStreams();

    int getMaxInboundStreams();
}
