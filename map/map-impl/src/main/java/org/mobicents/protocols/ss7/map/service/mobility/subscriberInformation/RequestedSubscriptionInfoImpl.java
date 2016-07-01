/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and/or its affiliates, and individual
 * contributors as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
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
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.AdditionalRequestedCAMELSubscriptionInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedCAMELSubscriptionInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedSubscriptionInfo;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SSForBSCode;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.SSForBSCodeImpl;

/**
 * 
 * <code>
 RequestedSubscriptionInfo ::= SEQUENCE {
         requestedSS-Info                           [1] SS-ForBS-Code OPTIONAL,
         odb                                        [2] NULL OPTIONAL,
         requestedCAMEL-SubscriptionInfo            [3] RequestedCAMEL-SubscriptionInfo OPTIONAL,
         supportedVLR-CAMEL-Phases                  [4] NULL OPTIONAL,
         supportedSGSN-CAMEL-Phases                 [5] NULL OPTIONAL,
         extensionContainer                         [6] ExtensionContainer OPTIONAL, ...,
         additionalRequestedCAMEL-SubscriptionInfo  [7] AdditionalRequestedCAMEL-SubscriptionInfo OPTIONAL,
         msisdn-BS-List                             [8] NULL OPTIONAL,
         csg-SubscriptionDataRequested              [9] NULL OPTIONAL,
         cw-Info                                    [10] NULL OPTIONAL,
         clip-Info                                  [11] NULL OPTIONAL,
         clir-Info                                  [12] NULL OPTIONAL,
         hold-Info                                  [13] NULL OPTIONAL,
         ect-Info                                   [14] NULL OPTIONAL
 }
 </code>
 * @author eva ogallar
 *
 */
public class RequestedSubscriptionInfoImpl implements RequestedSubscriptionInfo, MAPAsnPrimitive {

    public static final int _ID_requestedSS_Info = 1;
    public static final int _ID_odb = 2;
    public static final int _ID_requestedCAMEL_SubscriptionInfo = 3;
    public static final int _ID_supportedVLR_CAMEL_Phases = 4;
    public static final int _ID_supportedSGSN_CAMEL_Phases = 5;
    public static final int _ID_extensionContainer = 6;
    public static final int _ID_additionalRequestedCAMEL_SubscriptionInfo = 7;
    public static final int _ID_msisdn_BS_List = 8;
    public static final int _ID_csg_SubscriptionDataRequested = 9;
    public static final int _ID_cw_Info = 10;
    public static final int _ID_clip_Info = 11;
    public static final int _ID_clir_Info = 12;
    public static final int _ID_hold_Info = 13;
    public static final int _ID_ect_Info = 14;

    public static final String _PrimitiveName = "RequestedSubscriptionInfo";

    private SSForBSCode requestedSSInfo;
    private boolean odb;
    private RequestedCAMELSubscriptionInfo requestedCAMELSubscriptionInfo;
    private boolean supportedVlrCamelPhases;
    private boolean supportedSgsnCamelPhases;
    private MAPExtensionContainer extensionContainer ;
    private AdditionalRequestedCAMELSubscriptionInfo additionalRequestedCamelSubscriptionInfo;
    private boolean msisdnBsList;
    private boolean csgSubscriptionDataRequested;
    private boolean cwInfo;
    private boolean clipInfo;
    private boolean clirInfo;
    private boolean holdInfo;
    private boolean ectInfo;


    /**
     *
     */
    public RequestedSubscriptionInfoImpl() {
        super();
    }


    public RequestedSubscriptionInfoImpl(SSForBSCode requestedSSInfo, boolean odb,
                                         RequestedCAMELSubscriptionInfo requestedCAMELSubscriptionInfo,
                                         boolean supportedVlrCamelPhases, boolean supportedSgsnCamelPhases,
                                         MAPExtensionContainer extensionContainer,
                                         AdditionalRequestedCAMELSubscriptionInfo additionalRequestedCamelSubscriptionInfo,
                                         boolean msisdnBsList, boolean csgSubscriptionDataRequested, boolean cwInfo,
                                         boolean clipInfo, boolean clirInfo, boolean holdInfo, boolean ectInfo) {
        this.requestedSSInfo = requestedSSInfo;
        this.odb = odb;
        this.requestedCAMELSubscriptionInfo = requestedCAMELSubscriptionInfo;
        this.supportedVlrCamelPhases = supportedVlrCamelPhases;
        this.supportedSgsnCamelPhases = supportedSgsnCamelPhases;
        this.extensionContainer = extensionContainer;
        this.additionalRequestedCamelSubscriptionInfo = additionalRequestedCamelSubscriptionInfo;
        this.msisdnBsList = msisdnBsList;
        this.csgSubscriptionDataRequested = csgSubscriptionDataRequested;
        this.cwInfo = cwInfo;
        this.clipInfo = clipInfo;
        this.clirInfo = clirInfo;
        this.holdInfo = holdInfo;
        this.ectInfo = ectInfo;
    }

    /*
         * (non-Javadoc)
         *
         * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#getTag()
         */
    public int getTag() throws MAPException {
        return Tag.SEQUENCE;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#getTagClass()
     */
    public int getTagClass() {
        return Tag.CLASS_UNIVERSAL;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#getIsPrimitive ()
     */
    public boolean getIsPrimitive() {
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#decodeAll( org.mobicents.protocols.asn.AsnInputStream)
     */
    public void decodeAll(AsnInputStream ansIS) throws MAPParsingComponentException {
        try {
            int length = ansIS.readLength();
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new MAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new MAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#decodeData (org.mobicents.protocols.asn.AsnInputStream,
     * int)
     */
    public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {
        try {
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new MAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new MAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    private void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException, AsnException {
        AsnInputStream ais = ansIS.readSequenceStreamData(length);

        requestedSSInfo = null;
        odb = false;
        requestedCAMELSubscriptionInfo = null;
        supportedVlrCamelPhases = false;
        supportedSgsnCamelPhases = false;
        extensionContainer = null;
        additionalRequestedCamelSubscriptionInfo = null;
        msisdnBsList = false;
        csgSubscriptionDataRequested = false;
        cwInfo = false;
        clipInfo = false;
        clirInfo = false;
        holdInfo = false;
        ectInfo = false;

        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                    case _ID_requestedSS_Info:
                        if (ais.isTagPrimitive())
                            throw new MAPParsingComponentException(
                                    "Error while decoding RequestedSubscriptionInfo: Parameter requestedSSInfo is primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        requestedSSInfo = new SSForBSCodeImpl();
                        ((SSForBSCodeImpl) requestedSSInfo).decodeAll(ais);
                        break;
                    case _ID_odb:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException(
                                    "Error while decoding RequestedSubscriptionInfo: Parameter odb is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        ais.readNull();
                        this.odb = Boolean.TRUE;
                        break;
                    case _ID_requestedCAMEL_SubscriptionInfo:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException(
                                    "Error while decoding RequestedSubscriptionInfo: Parameter requestedCAMELSubscriptionInfo is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        int i1 = (int) ais.readInteger();
                        this.requestedCAMELSubscriptionInfo = RequestedCAMELSubscriptionInfo.getInstance(i1);
                        break;
                    case _ID_supportedVLR_CAMEL_Phases:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException(
                                    "Error while decoding RequestedSubscriptionInfo: Parameter supportedVlrCamelPhases is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        ais.readNull();
                        this.supportedVlrCamelPhases = Boolean.TRUE;
                        break;
                    case _ID_supportedSGSN_CAMEL_Phases:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException(
                                    "Error while decoding RequestedSubscriptionInfo: Parameter supportedSgsnCamelPhases is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        ais.readNull();
                        this.supportedSgsnCamelPhases = Boolean.TRUE;
                        break;
                    case _ID_extensionContainer:
                        if (ais.isTagPrimitive())
                            throw new MAPParsingComponentException(
                                    "Error while decoding RequestedSubscriptionInfo: Parameter is primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        extensionContainer = new MAPExtensionContainerImpl();
                        ((MAPExtensionContainerImpl) extensionContainer).decodeAll(ais);
                        break;
                    case _ID_additionalRequestedCAMEL_SubscriptionInfo:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException(
                                    "Error while decoding RequestedSubscriptionInfo: Parameter additionalRequestedCamelSubscriptionInfo is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        int i2 = (int) ais.readInteger();
                        this.additionalRequestedCamelSubscriptionInfo = AdditionalRequestedCAMELSubscriptionInfo.getInstance(i2);
                        break;
                    case _ID_msisdn_BS_List:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException(
                                    "Error while decoding RequestedSubscriptionInfo: Parameter msisdnBsList is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        ais.readNull();
                        this.msisdnBsList = Boolean.TRUE;
                        break;
                    case _ID_csg_SubscriptionDataRequested:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException(
                                    "Error while decoding RequestedSubscriptionInfo: Parameter csgSubscriptionDataRequested is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        ais.readNull();
                        this.csgSubscriptionDataRequested = Boolean.TRUE;
                        break;
                    case _ID_cw_Info:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException(
                                    "Error while decoding RequestedSubscriptionInfo: Parameter cwInfo is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        ais.readNull();
                        this.cwInfo = Boolean.TRUE;
                        break;
                    case _ID_clip_Info:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException(
                                    "Error while decoding RequestedSubscriptionInfo: Parameter clipInfo is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        ais.readNull();
                        this.clipInfo = Boolean.TRUE;
                        break;
                    case _ID_clir_Info:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException(
                                    "Error while decoding RequestedSubscriptionInfo: Parameter clirInfo is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        ais.readNull();
                        this.clirInfo = Boolean.TRUE;
                        break;
                    case _ID_hold_Info:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException(
                                    "Error while decoding RequestedSubscriptionInfo: Parameter holdInfo is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        ais.readNull();
                        this.holdInfo = Boolean.TRUE;
                        break;
                    case _ID_ect_Info:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException(
                                    "Error while decoding RequestedSubscriptionInfo: Parameter ectInfo is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        ais.readNull();
                        this.ectInfo = Boolean.TRUE;
                        break;
                    default:
                        ais.advanceElement();
                        break;
                }
            } else {
                ais.advanceElement();
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#encodeAll( org.mobicents.protocols.asn.AsnOutputStream)
     */
    public void encodeAll(AsnOutputStream asnOs) throws MAPException {
        this.encodeAll(asnOs, this.getTagClass(), this.getTag());
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#encodeAll( org.mobicents.protocols.asn.AsnOutputStream,
     * int, int)
     */
    public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {
        try {
            asnOs.writeTag(tagClass, this.getIsPrimitive(), tag);
            int pos = asnOs.StartContentDefiniteLength();
            this.encodeData(asnOs);
            asnOs.FinalizeContent(pos);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#encodeData (org.mobicents.protocols.asn.AsnOutputStream)
     */
    public void encodeData(AsnOutputStream asnOs) throws MAPException {

        if (this.requestedSSInfo != null) {
            ((SSForBSCodeImpl) this.requestedSSInfo).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                    _ID_requestedSS_Info);
        }
        
        try {
            if (this.odb) {
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _ID_odb);
            }
        } catch (IOException e) {
            throw new MAPException("IOException when encoding parameter odb: ", e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding parameter odb: ", e);
        }
        
        try {
            if (this.requestedCAMELSubscriptionInfo!=null) {
                asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_requestedCAMEL_SubscriptionInfo, this.requestedCAMELSubscriptionInfo.getCode());
            }
        } catch (IOException e) {
            throw new MAPException("IOException when encoding parameter requestedCAMELSubscriptionInfo: ", e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding parameter requestedCAMELSubscriptionInfo: ", e);
        }

        try {
            if (this.supportedVlrCamelPhases) {
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _ID_supportedVLR_CAMEL_Phases);
            }
        } catch (IOException e) {
            throw new MAPException("IOException when encoding parameter supportedVlrCamelPhases: ", e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding parameter supportedVlrCamelPhases: ", e);
        }

        try {
            if (this.supportedSgsnCamelPhases) {
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _ID_supportedSGSN_CAMEL_Phases);
            }
        } catch (IOException e) {
            throw new MAPException("IOException when encoding parameter supportedSgsnCamelPhases: ", e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding parameter supportedSgsnCamelPhases: ", e);
        }

        if (this.extensionContainer != null) {
            ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                    _ID_extensionContainer);
        }

        try {
            if (this.additionalRequestedCamelSubscriptionInfo!=null) {
                asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_additionalRequestedCAMEL_SubscriptionInfo, this.additionalRequestedCamelSubscriptionInfo.getCode());
            }
        } catch (IOException e) {
            throw new MAPException("IOException when encoding parameter additionalRequestedCamelSubscriptionInfo: ", e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding parameter additionalRequestedCamelSubscriptionInfo: ", e);
        }


        try {
            if (this.msisdnBsList) {
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _ID_msisdn_BS_List);
            }
        } catch (IOException e) {
            throw new MAPException("IOException when encoding parameter msisdnBsList: ", e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding parameter msisdnBsList: ", e);
        }
        
        try {
            if (this.csgSubscriptionDataRequested) {
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _ID_csg_SubscriptionDataRequested);
            }
        } catch (IOException e) {
            throw new MAPException("IOException when encoding parameter csgSubscriptionDataRequested: ", e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding parameter csgSubscriptionDataRequested: ", e);
        }
        
        try {
            if (this.cwInfo) {
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _ID_cw_Info);
            }
        } catch (IOException e) {
            throw new MAPException("IOException when encoding parameter cwInfo: ", e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding parameter cwInfo: ", e);
        }
        
        try {
            if (this.clipInfo) {
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _ID_clip_Info);
            }
        } catch (IOException e) {
            throw new MAPException("IOException when encoding parameter clipInfo: ", e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding parameter clipInfo: ", e);
        }
        
        try {
            if (this.clirInfo) {
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _ID_clir_Info);
            }
        } catch (IOException e) {
            throw new MAPException("IOException when encoding parameter clirInfo: ", e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding parameter clirInfo: ", e);
        }
        
        try {
            if (this.holdInfo) {
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _ID_hold_Info);
            }
        } catch (IOException e) {
            throw new MAPException("IOException when encoding parameter holdInfo: ", e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding parameter holdInfo: ", e);
        }
        
        try {
            if (this.ectInfo) {
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _ID_ect_Info);
            }
        } catch (IOException e) {
            throw new MAPException("IOException when encoding parameter ectInfo: ", e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding parameter ectInfo: ", e);
        }
    }

    public SSForBSCode getRequestedSSInfo() {
        return requestedSSInfo;
    }

    public boolean getOdb() {
        return odb;
    }

    public RequestedCAMELSubscriptionInfo getRequestedCAMELSubscriptionInfo() {
        return requestedCAMELSubscriptionInfo;
    }

    public boolean getSupportedVlrCamelPhases() {
        return supportedVlrCamelPhases;
    }

    public boolean getSupportedSgsnCamelPhases() {
        return supportedSgsnCamelPhases;
    }

    public MAPExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    public AdditionalRequestedCAMELSubscriptionInfo getAdditionalRequestedCamelSubscriptionInfo() {
        return additionalRequestedCamelSubscriptionInfo;
    }

    public boolean getMsisdnBsList() {
        return msisdnBsList;
    }

    public boolean getCsgSubscriptionDataRequested() {
        return csgSubscriptionDataRequested;
    }

    public boolean getCwInfo() {
        return cwInfo;
    }

    public boolean getClipInfo() {
        return clipInfo;
    }

    public boolean getClirInfo() {
        return clirInfo;
    }

    public boolean getHoldInfo() {
        return holdInfo;
    }

    public boolean getEctInfo() {
        return ectInfo;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (requestedSSInfo!=null) {
            sb.append(", requestedSSInfo");
            sb.append(requestedSSInfo);
        }

        if (odb) {
            sb.append(", odb");
        }

        if (this.requestedCAMELSubscriptionInfo != null) {
            sb.append(", requestedCAMELSubscriptionInfo=");
            sb.append(this.requestedCAMELSubscriptionInfo);
        }

        if (supportedVlrCamelPhases) {
            sb.append(", supportedVlrCamelPhases");
        }

        if (supportedSgsnCamelPhases) {
            sb.append(", supportedSgsnCamelPhases");
        }

        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.extensionContainer);
        }

        if (this.additionalRequestedCamelSubscriptionInfo != null) {
            sb.append(", additionalRequestedCamelSubscriptionInfo=");
            sb.append(this.additionalRequestedCamelSubscriptionInfo);
        }

        if (msisdnBsList) {
            sb.append(", msisdnBsList");
        }

        if (csgSubscriptionDataRequested) {
            sb.append(", csgSubscriptionDataRequested");
        }

        if (cwInfo) {
            sb.append(", cwInfo");
        }

        if (clipInfo) {
            sb.append(", clipInfo");
        }

        if (clirInfo) {
            sb.append(", clirInfo");
        }

        if (holdInfo) {
            sb.append(", holdInfo");
        }

        if (ectInfo) {
            sb.append(", ectInfo");
        }

        sb.append("]");
        return sb.toString();
    }
}
