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
import java.util.ArrayList;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.AbstractMAPAsnPrimitive;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.ExtForwardingInfoForCSE;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtForwFeature;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SSCode;
import org.mobicents.protocols.ss7.map.datacoding.NullEncoderFacility;
import org.mobicents.protocols.ss7.map.datacoding.ObjectEncoderFacility;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtForwFeatureImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.SSCodeImpl;

/**
 *
 <code>
 Ext-ForwardingInfoFor-CSE ::= SEQUENCE {
     ss-Code                [0] SS-Code,
     forwardingFeatureList  [1] Ext-ForwFeatureList,
     notificationToCSE      [2] NULL OPTIONAL,
     extensionContainer     [3] ExtensionContainer OPTIONAL,
     ...
 }

 Ext-ForwFeatureList ::= SEQUENCE SIZE (1..32) OF Ext-ForwFeature
 </code>
 * @author eva ogallar
 *
 */
public class ExtForwardingInfoForCSEImpl extends AbstractMAPAsnPrimitive implements ExtForwardingInfoForCSE {

    public static final String _PrimitiveName = "ExtForwardingInfoForCSE";

    private SSCode ssCode;
    private ArrayList<ExtForwFeature> forwardingFeatureList;
    private boolean notificationToCSE;
    private MAPExtensionContainer extensionContainer;

    private static final int TAG_SS_CODE = 0;
    private static final int TAG_FORWARDING_FEATURE_LIST = 1;
    private static final int TAG_NOTIFICATION_TO_CSE = 2;
    private static final int TAG_EXTENSION_CONTAINER = 3;

    public ExtForwardingInfoForCSEImpl() {
    }

    public ExtForwardingInfoForCSEImpl(SSCode ssCode, ArrayList<ExtForwFeature> forwardingFeatureList,
                                       boolean notificationToCSE, MAPExtensionContainer extensionContainer) {
        this.ssCode = ssCode;
        this.forwardingFeatureList = forwardingFeatureList;
        this.notificationToCSE = notificationToCSE;
        this.extensionContainer = extensionContainer;
    }

    @Override
    protected void _decode(AsnInputStream ansIS, int length) throws IOException, AsnException, MAPParsingComponentException {
        this.ssCode = null;
        this.forwardingFeatureList = null;
        this.notificationToCSE = false;
        this.extensionContainer = null;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);

        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag(); //
            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                    case TAG_SS_CODE:
                        ssCode = (SSCode) ObjectEncoderFacility.decodePrimitiveObject(ais, new SSCodeImpl(), "ssCode", getPrimitiveName());
                        break;
                    case TAG_FORWARDING_FEATURE_LIST:
                        if (ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".forwardingFeatureList: is primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        AsnInputStream ais2 = ais.readSequenceStream();
                        this.forwardingFeatureList = new ArrayList<ExtForwFeature>();
                        while (true) {
                            if (ais2.available() == 0)
                                break;

                            int tag2 = ais2.readTag();
                            if (tag2 != Tag.SEQUENCE || ais2.getTagClass() != Tag.CLASS_UNIVERSAL || ais2.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ": bad forwardingFeatureList tag or tagClass or is primitive ",
                                        MAPParsingComponentExceptionReason.MistypedParameter);

                            ExtForwFeature elem = new ExtForwFeatureImpl();
                            ((ExtForwFeatureImpl) elem).decodeAll(ais2);
                            this.forwardingFeatureList.add(elem);
                        }
                        break;
                    case TAG_NOTIFICATION_TO_CSE:
                        this.notificationToCSE = NullEncoderFacility.decode(ais, "notificationToCSE", _PrimitiveName);
                        break;
                    case TAG_EXTENSION_CONTAINER:
                        extensionContainer = (MAPExtensionContainer) ObjectEncoderFacility.
                                decodePrimitiveObject(ais, new MAPExtensionContainerImpl(), "extensionContainer", getPrimitiveName());
                        break;
                    default:
                        ais.advanceElement();
                        break;
                }
            } else {
                ais.advanceElement();
            }
        }
        validate();
    }

    private void validate() throws MAPParsingComponentException {
        if (this.ssCode == null) {
            throw new MAPParsingComponentException(
                    "Error while decoding " + getPrimitiveName() + ": ssCode is mandatory but not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
        if (this.forwardingFeatureList == null ||
                this.forwardingFeatureList.size() > 32 || this.forwardingFeatureList.size() < 1 ){
            throw new MAPParsingComponentException(
                    "Error while decoding " + getPrimitiveName() + ": forwardingFeatureList size must be between 1 and 32 ",
                    MAPParsingComponentExceptionReason.MistypedParameter);

        }
    }

    @Override
    public int getTag() throws MAPException {
        return Tag.SEQUENCE;
    }

    @Override
    public int getTagClass() {
        return Tag.CLASS_UNIVERSAL;
    }

    @Override
    public void encodeData(AsnOutputStream asnOs) throws MAPException {

        try {
            validate();
        } catch (MAPParsingComponentException e) {
            throw new MAPException(e.getMessage());
        }

        ((SSCodeImpl) this.ssCode).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, TAG_SS_CODE);

        try {
            asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, TAG_FORWARDING_FEATURE_LIST);
            int pos = asnOs.StartContentDefiniteLength();
            for (ExtForwFeature elem : this.forwardingFeatureList) {
                ((ExtForwFeatureImpl) elem).encodeAll(asnOs);
            }
            asnOs.FinalizeContent(pos);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ".forwardingFeatureList: "
                    + e.getMessage(), e);
        }

        NullEncoderFacility.encode(asnOs, this.notificationToCSE, Tag.CLASS_CONTEXT_SPECIFIC, TAG_NOTIFICATION_TO_CSE, "longFtnSupported");

        if (this.extensionContainer != null) {
            ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, TAG_EXTENSION_CONTAINER);
        }

    }

    @Override
    protected String getPrimitiveName() {
        return _PrimitiveName;
    }

    @Override
    public boolean getIsPrimitive() {
        return false;
    }

    public SSCode getSsCode() {
        return ssCode;
    }

    public ArrayList<ExtForwFeature> getForwardingFeatureList() {
        return forwardingFeatureList;
    }

    public boolean getNotificationToCSE() {
        return notificationToCSE;
    }

    public MAPExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }
}
