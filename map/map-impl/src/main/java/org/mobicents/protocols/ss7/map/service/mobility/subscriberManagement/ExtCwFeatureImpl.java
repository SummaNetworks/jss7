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

package org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.ExtCwFeature;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSStatus;
import org.mobicents.protocols.ss7.map.datacoding.ObjectEncoderFacility;
import org.mobicents.protocols.ss7.map.primitives.SequenceBase;

/**
 *
 <code>
 Ext-CwFeature ::= SEQUENCE {
     basicService     [1] Ext-BasicServiceCode,
     ss-Status        [2] Ext-SS-Status,
     ...
 }
 </code>
 * @author eva ogallar
 *
 */
public class ExtCwFeatureImpl extends SequenceBase implements ExtCwFeature {

    private static final int TAG_BASIC_SERVICE = 1;
    private static final int TAG_SS_STATUS = 2;

    private ExtBasicServiceCode basicService = null;
    private ExtSSStatus ssStatus = null;

    public ExtCwFeatureImpl() {
        super("ExtCwFeature");
    }

    /**
     *
     */
    public ExtCwFeatureImpl(ExtBasicServiceCode basicService, ExtSSStatus ssStatus) {
        super("ExtCwFeature");

        this.basicService = basicService;
        this.ssStatus = ssStatus;
    }

    public ExtBasicServiceCode getBasicService() {
        return this.basicService;
    }

    public ExtSSStatus getSsStatus() {
        return this.ssStatus;
    }

    protected void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException, AsnException {
        this.basicService = null;
        this.ssStatus = null;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);

        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            switch (ais.getTagClass()) {
                case Tag.CLASS_CONTEXT_SPECIFIC:
                    switch (tag) {
                        case TAG_BASIC_SERVICE:
                            this.basicService = (ExtBasicServiceCode) ObjectEncoderFacility.
                                    decodeObject(ais, new ExtBasicServiceCodeImpl(), "basicService", _PrimitiveName);
                            break;
                        case TAG_SS_STATUS:
                            this.ssStatus = (ExtSSStatus) ObjectEncoderFacility.
                                    decodeObject(ais, new ExtSSStatusImpl(), "ssStatus", _PrimitiveName);
                            break;
                        default:
                            ais.advanceElement();
                            break;

                    }
                    break;

                default:
                    ais.advanceElement();
                    break;
            }
        }

        if (this.ssStatus == null) {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ": ssStatus required.",
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }

        if (this.basicService == null) {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ": basicService required.",
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#encodeData (org.mobicents.protocols.asn.AsnOutputStream)
     */
    public void encodeData(AsnOutputStream asnOs) throws MAPException {

        if (this.ssStatus == null) {
            throw new MAPException("Error while encoding " + _PrimitiveName + ": ssStatus required.");
        }

        if (this.basicService == null) {
            throw new MAPException("Error while encoding " + _PrimitiveName + ": basicService required.");
        }

        ((ExtBasicServiceCodeImpl) this.basicService).encodeAll(asnOs);

        ((ExtSSStatusImpl) this.ssStatus).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, TAG_SS_STATUS);

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName + " [");

        if (this.basicService != null) {
            sb.append("basicService=");
            sb.append(this.basicService.toString());
            sb.append(", ");
        }

        if (this.ssStatus != null) {
            sb.append("ssStatus=");
            sb.append(this.ssStatus.toString());
            sb.append(", ");
        }

        sb.append("]");

        return sb.toString();
    }

}
