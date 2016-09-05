/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.AbstractMAPAsnPrimitive;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.ExtCallBarringInfoForCSE;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.ExtForwardingInfoForCSE;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.ExtSSInfoForCSE;

/**
 *
 <code>
 Ext-SS-InfoFor-CSE ::= CHOICE {
 forwardingInfoFor-CSE   [0] Ext-ForwardingInfoFor-CSE,
 callBarringInfoFor-CSE  [1] Ext-CallBarringInfoFor-CSE
 }
 </code>
 * @author eva ogallar
 *
 */
public class ExtSSInfoForCSEImpl extends AbstractMAPAsnPrimitive implements ExtSSInfoForCSE {

    public static final int _TAG_forwardingInfoFor_CSE = 0;
    public static final int _TAG_callBarringInfoFor_CSE = 1;

    private static final String DEFAULT_STRING_VALUE = null;

    public static final String _PrimitiveName = "ExtSSInfoForCSE";

    private ExtForwardingInfoForCSE forwardingInfoForCSE;
    private ExtCallBarringInfoForCSE callBarringInfoForCSE;

    public ExtSSInfoForCSEImpl() {
    }

    public ExtSSInfoForCSEImpl(ExtForwardingInfoForCSE forwardingInfoForCSE) {
        this.forwardingInfoForCSE = forwardingInfoForCSE;
    }

    public ExtSSInfoForCSEImpl(ExtCallBarringInfoForCSE callBarringInfoForCSE) {
        this.callBarringInfoForCSE = callBarringInfoForCSE;
    }

    public ExtForwardingInfoForCSE getForwardingInfoForCSE() {
        return forwardingInfoForCSE;
    }

    public ExtCallBarringInfoForCSE getCallBarringInfoForCSE() {
        return callBarringInfoForCSE;
    }

    public int getTag() throws MAPException {
        if (this.callBarringInfoForCSE != null) {
            return _TAG_callBarringInfoFor_CSE;
        } else {
            return _TAG_forwardingInfoFor_CSE;
        }
    }

    public int getTagClass() {
        return Tag.CLASS_CONTEXT_SPECIFIC;
    }

    public boolean getIsPrimitive() {
        return false;
    }


    public void _decode(AsnInputStream ais, int length) throws MAPParsingComponentException, IOException, AsnException {

        this.forwardingInfoForCSE = null;
        this.callBarringInfoForCSE = null;

        int tag = ais.getTag();

/*
        if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || ais.isTagPrimitive())
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": Primitive has bad tag class or is primitive", MAPParsingComponentExceptionReason.MistypedParameter);
*/

        if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
            switch (tag) {
                case _TAG_forwardingInfoFor_CSE:
                    this.forwardingInfoForCSE = new ExtForwardingInfoForCSEImpl();
                    ((ExtForwardingInfoForCSEImpl) this.forwardingInfoForCSE).decodeData(ais, length);
                    break;
                case _TAG_callBarringInfoFor_CSE:
                    this.callBarringInfoForCSE = new ExtCallBarringInfoForCSEImpl();
                    ((ExtCallBarringInfoForCSEImpl) this.callBarringInfoForCSE).decodeData(ais, length);
                    break;
                default:
                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ": bad choice tag",
                            MAPParsingComponentExceptionReason.MistypedParameter);
            }
        } else {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ": bad choice tagClass",
                    MAPParsingComponentExceptionReason.MistypedParameter);

        }

    }

    public void encodeData(AsnOutputStream asnOs) throws MAPException {

        if (this.forwardingInfoForCSE == null && this.callBarringInfoForCSE == null ||
                this.forwardingInfoForCSE != null && this.callBarringInfoForCSE != null) {
            throw new MAPException("Error while decoding " + _PrimitiveName + ": One and only one choice must be selected");
        }

        if (this.forwardingInfoForCSE != null) {
            ((ExtForwardingInfoForCSEImpl) this.forwardingInfoForCSE).encodeData(asnOs);
        } else {
            ((ExtCallBarringInfoForCSEImpl) this.callBarringInfoForCSE).encodeData(asnOs);
        }
    }

    @Override
    protected String getPrimitiveName() {
        return _PrimitiveName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ExtSSInfoForCSE [");

        if (this.forwardingInfoForCSE != null) {
            sb.append("forwardingInfoForCSE=");
            sb.append(this.forwardingInfoForCSE);
        }
        if (this.callBarringInfoForCSE != null) {
            sb.append(", callBarringInfoForCSE=");
            sb.append(this.callBarringInfoForCSE);
        }

        sb.append("]");

        return sb.toString();
    }

}
