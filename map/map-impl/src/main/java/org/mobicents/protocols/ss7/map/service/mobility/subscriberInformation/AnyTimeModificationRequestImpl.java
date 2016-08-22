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
import org.mobicents.protocols.ss7.map.api.MAPMessageType;
import org.mobicents.protocols.ss7.map.api.MAPOperationCode;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.SubscriberIdentity;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeModificationRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.ModificationRequestForCBInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.ModificationRequestForCFInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.ModificationRequestForCHInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.ModificationRequestForCLIPInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.ModificationRequestForCLIRInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.ModificationRequestForCSG;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.ModificationRequestForCSI;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.ModificationRequestForCWInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.ModificationRequestForECTInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.ModificationRequestForIPSMGWData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.ModificationRequestForODBdata;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedServingNode;
import org.mobicents.protocols.ss7.map.datacoding.NullEncoderFacility;
import org.mobicents.protocols.ss7.map.datacoding.ObjectEncoderFacility;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.primitives.SubscriberIdentityImpl;
import org.mobicents.protocols.ss7.map.service.mobility.MobilityMessageImpl;

/**
 * <code>
 * AnyTimeModificationArg ::= SEQUENCE {
 *      subscriberIdentity                     [0] SubscriberIdentity,
 *      gsmSCF-Address                         [1] ISDN-AddressString,
 *      modificationRequestFor-CF-Info         [2] ModificationRequestFor-CF-Info OPTIONAL,
 *      modificationRequestFor-CB-Info         [3] ModificationRequestFor-CB-Info OPTIONAL,
 *      modificationRequestFor-CSI             [4] ModificationRequestFor-CSI OPTIONAL,
 *      extensionContainer                     [5] ExtensionContainer OPTIONAL,
 *      longFTN-Supported                      [6] NULL OPTIONAL,
 *      modificationRequestFor-ODB-data        [7] ModificationRequestFor-ODB-data OPTIONAL,
 *      modificationRequestFor-IP-SM-GW-Data   [8] ModificationRequestFor-IP-SM-GW-Data OPTIONAL,
 *      activationRequestForUE-reachability    [9] RequestedServingNode OPTIONAL,
 *      modificationRequestFor-CSG            [10] ModificationRequestFor-CSG OPTIONAL,
 *      modificationRequestFor-CW-Data        [11] ModificationRequestFor-CW-Info OPTIONAL,
 *      modificationRequestFor-CLIP-Data      [12] ModificationRequestFor-CLIP-Info OPTIONAL,
 *      modificationRequestFor-CLIR-Data      [13] ModificationRequestFor-CLIR-Info OPTIONAL,
 *      modificationRequestFor-HOLD-Data      [14] ModificationRequestFor-CH-Info OPTIONAL,
 *      modificationRequestFor-ECT-Data       [15] ModificationRequestFor-ECT-Info OPTIONAL
 * }
 * </code>
 *
 * @author eva ogallar
 */
public class AnyTimeModificationRequestImpl extends MobilityMessageImpl implements AnyTimeModificationRequest,
        MAPAsnPrimitive {

    private static final int TAG_SUBSCRIBER_IDENTITY = 0;
    private static final int TAG_GSM_SCF_ADDRESS = 1;
    private static final int TAG_EXTENSION_CONTAINER = 5;
    private static final int TAG_LONG_FTN_SUPPORTED = 6;
    private static final int TAG_MODIFICATION_REQUEST_FOR_CF_INFO = 2;
    private static final int TAG_MODIFICATION_REQUEST_FOR_CB_INFO = 3;
    private static final int TAG_MODIFICATION_REQUEST_FOR_CSI = 4;
    private static final int TAG_MODIFICATION_REQUEST_FOR_ODB_DATA = 7;
    private static final int TAG_MODIFICATION_REQUEST_FOR_IP_SM_GW_DATA = 8;
    private static final int TAG_ACTIVATION_REQUEST_FOR_UE_REACHABILITY = 9;
    private static final int TAG_MODIFICATION_REQUEST_FOR_CSG = 10;
    private static final int TAG_MODIFICATION_REQUEST_FOR_CW_DATA = 11;
    private static final int TAG_MODIFICATION_REQUEST_FOR_CLIP_DATA = 12;
    private static final int TAG_MODIFICATION_REQUEST_FOR_CLIR_DATA = 13;
    private static final int TAG_MODIFICATION_REQUEST_FOR_HOLD_DATA = 14;
    private static final int TAG_MODIFICATION_REQUEST_FOR_ECT_DATA = 15;

    public static final String _PrimitiveName = "AnyTimeModificationRequest";

    private SubscriberIdentity subscriberIdentity;
    private ISDNAddressString gsmSCFAddress;
    private ModificationRequestForCFInfo modificationRequestForCfInfo;
    private ModificationRequestForCBInfo modificationRequestForCbInfo;
    private ModificationRequestForCSI modificationRequestForCSI;
    private MAPExtensionContainer extensionContainer;
    private boolean longFtnSupported;
    private ModificationRequestForODBdata modificationRequestForODBdata;
    private ModificationRequestForIPSMGWData modificationRequestForIpSmGwData;
    private RequestedServingNode activationRequestForUEReachability;
    private ModificationRequestForCSG modificationRequestForCSG;
    private ModificationRequestForCWInfo modificationRequestForCwData;
    private ModificationRequestForCLIPInfo modificationRequestForClipData;
    private ModificationRequestForCLIRInfo modificationRequestForClirData;
    private ModificationRequestForCHInfo modificationRequestForHoldData;
    private ModificationRequestForECTInfo modificationRequestForEctData;


    public AnyTimeModificationRequestImpl() {

    }

    public AnyTimeModificationRequestImpl(SubscriberIdentity subscriberIdentity, ISDNAddressString gsmSCFAddress,
                                          ModificationRequestForCFInfo modificationRequestForCfInfo,
                                          ModificationRequestForCBInfo modificationRequestForCbInfo,
                                          ModificationRequestForCSI modificationRequestForCSI,
                                          MAPExtensionContainer extensionContainer, boolean longFtnSupported,
                                          ModificationRequestForODBdata modificationRequestForODBdata,
                                          ModificationRequestForIPSMGWData modificationRequestForIpSmGwData,
                                          RequestedServingNode activationRequestForUEReachability,
                                          ModificationRequestForCSG modificationRequestForCSG,
                                          ModificationRequestForCWInfo modificationRequestForCwData,
                                          ModificationRequestForCLIPInfo modificationRequestForClipData,
                                          ModificationRequestForCLIRInfo modificationRequestForClirData,
                                          ModificationRequestForCHInfo modificationRequestForHoldData,
                                          ModificationRequestForECTInfo modificationRequestForEctData) {
        this.subscriberIdentity = subscriberIdentity;
        this.gsmSCFAddress = gsmSCFAddress;
        this.modificationRequestForCfInfo = modificationRequestForCfInfo;
        this.modificationRequestForCbInfo = modificationRequestForCbInfo;
        this.modificationRequestForCSI = modificationRequestForCSI;
        this.extensionContainer = extensionContainer;
        this.longFtnSupported = longFtnSupported;
        this.modificationRequestForODBdata = modificationRequestForODBdata;
        this.modificationRequestForIpSmGwData = modificationRequestForIpSmGwData;
        this.activationRequestForUEReachability = activationRequestForUEReachability;
        this.modificationRequestForCSG = modificationRequestForCSG;
        this.modificationRequestForCwData = modificationRequestForCwData;
        this.modificationRequestForClipData = modificationRequestForClipData;
        this.modificationRequestForClirData = modificationRequestForClirData;
        this.modificationRequestForHoldData = modificationRequestForHoldData;
        this.modificationRequestForEctData = modificationRequestForEctData;
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

        this.subscriberIdentity = null;
        this.gsmSCFAddress = null;
        this.extensionContainer = null;
        this.longFtnSupported = false;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);

        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag(); //
            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                    case TAG_SUBSCRIBER_IDENTITY:
                        if (ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": Parameter subscriberIdentity is primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        subscriberIdentity = (SubscriberIdentity) ObjectEncoderFacility.decodeNestedObject(
                                ais, new SubscriberIdentityImpl(), "subscriberIdentity", _PrimitiveName);
                        break;

                    case TAG_MODIFICATION_REQUEST_FOR_CF_INFO:
                        modificationRequestForCfInfo = (ModificationRequestForCFInfo) ObjectEncoderFacility.decodeObject(
                                ais, new ModificationRequestForCFInfoImpl(), "modificationRequestForCfInfo", _PrimitiveName);
                        break;
                    case TAG_MODIFICATION_REQUEST_FOR_CB_INFO:
                        modificationRequestForCbInfo = (ModificationRequestForCBInfo) ObjectEncoderFacility.decodeObject(
                                ais, new ModificationRequestForCBInfoImpl(), "modificationRequestForCbInfo", _PrimitiveName);
                        break;
                    case TAG_MODIFICATION_REQUEST_FOR_CSI:
                        modificationRequestForCSI = (ModificationRequestForCSI) ObjectEncoderFacility.decodeObject(
                                ais, new ModificationRequestForCSIImpl(), "modificationRequestForCSI", _PrimitiveName);
                        break;
                    case TAG_MODIFICATION_REQUEST_FOR_ODB_DATA:
                        modificationRequestForODBdata = (ModificationRequestForODBdata) ObjectEncoderFacility.decodeObject(
                                ais, new ModificationRequestForODBdataImpl(), "modificationRequestForODBdata", _PrimitiveName);
                        break;
                    case TAG_MODIFICATION_REQUEST_FOR_IP_SM_GW_DATA:
                        modificationRequestForIpSmGwData = (ModificationRequestForIPSMGWData) ObjectEncoderFacility.decodeObject(
                                ais, new ModificationRequestForIPSMGWDataImpl(), "modificationRequestForIpSmGwData", _PrimitiveName);
                        break;
                    case TAG_ACTIVATION_REQUEST_FOR_UE_REACHABILITY:
                        activationRequestForUEReachability = (RequestedServingNode) ObjectEncoderFacility.decodePrimitiveObject(
                                ais, new RequestedServingNodeImpl(), "activationRequestForUEReachability", _PrimitiveName);
                        break;
                    case TAG_MODIFICATION_REQUEST_FOR_CSG:
                        modificationRequestForCSG = (ModificationRequestForCSG) ObjectEncoderFacility.decodeObject(
                                ais, new ModificationRequestForCSGImpl(), "modificationRequestForCSG", _PrimitiveName);
                        break;
                    case TAG_MODIFICATION_REQUEST_FOR_CW_DATA:
                        modificationRequestForCwData = (ModificationRequestForCWInfo) ObjectEncoderFacility.decodeObject(
                                ais, new ModificationRequestForCWInfoImpl(), "modificationRequestForCwData", _PrimitiveName);
                        break;
                    case TAG_MODIFICATION_REQUEST_FOR_CLIP_DATA:
                        modificationRequestForClipData = (ModificationRequestForCLIPInfo) ObjectEncoderFacility.decodeObject(
                                ais, new ModificationRequestForCLIPInfoImpl(), "modificationRequestForClipData", _PrimitiveName);
                        break;
                    case TAG_MODIFICATION_REQUEST_FOR_CLIR_DATA:
                        modificationRequestForClirData = (ModificationRequestForCLIRInfo) ObjectEncoderFacility.decodeObject(
                                ais, new ModificationRequestForCLIRInfoImpl(), "modificationRequestForClirData", _PrimitiveName);
                        break;
                    case TAG_MODIFICATION_REQUEST_FOR_HOLD_DATA:
                        modificationRequestForHoldData = (ModificationRequestForCHInfo) ObjectEncoderFacility.decodeObject(
                                ais, new ModificationRequestForCHInfoImpl(), "modificationRequestForHoldData", _PrimitiveName);
                        break;
                    case TAG_MODIFICATION_REQUEST_FOR_ECT_DATA:
                        modificationRequestForEctData = (ModificationRequestForECTInfo) ObjectEncoderFacility.decodeObject(
                                ais, new ModificationRequestForECTInfoImpl(), "modificationRequestForEctData", _PrimitiveName);
                        break;
                    case TAG_GSM_SCF_ADDRESS:
                        gsmSCFAddress = (ISDNAddressString) ObjectEncoderFacility.
                                decodePrimitiveObject(ais, new ISDNAddressStringImpl(), "gsmSCFAddress", _PrimitiveName);
                        break;
                    case TAG_EXTENSION_CONTAINER:
                        extensionContainer = (MAPExtensionContainer) ObjectEncoderFacility.
                                decodePrimitiveObject(ais, new MAPExtensionContainerImpl(), "extensionContainer", _PrimitiveName);
                        break;
                    case TAG_LONG_FTN_SUPPORTED:
                        this.longFtnSupported = NullEncoderFacility.decode(ais, "longFtnSupported", _PrimitiveName);
                        break;
                    default:
                        ais.advanceElement();
                        break;
                }
            } else {
                ais.advanceElement();
            }
        }
        if (this.subscriberIdentity == null || this.gsmSCFAddress == null) {
            throw new MAPParsingComponentException(
                    "Error while decoding " + _PrimitiveName + ": subscriberIdentity, requestedSubscriptionInfo " +
                            "and gsmSCFAddress parameters are mandatory but some of them are not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);
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
        if (this.subscriberIdentity == null) {
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + " the mandatory parameter subscriberIdentity is not defined");
        }
        if (this.gsmSCFAddress == null) {
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + " the mandatory parameter gsmSCF-Address is not defined");
        }

        try {
            asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, TAG_SUBSCRIBER_IDENTITY);
            int pos = asnOs.StartContentDefiniteLength();
            ((SubscriberIdentityImpl) this.subscriberIdentity).encodeAll(asnOs);
            asnOs.FinalizeContent(pos);
        } catch (AsnException e) {
            throw new MAPException("AsnException while encoding parameter targetMS [1] SubscriberIdentity");
        }

        ((ISDNAddressStringImpl) this.gsmSCFAddress).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, TAG_GSM_SCF_ADDRESS);

        if (this.modificationRequestForCfInfo != null) {
            ((ModificationRequestForCFInfoImpl) this.modificationRequestForCfInfo).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, TAG_MODIFICATION_REQUEST_FOR_CF_INFO);
        }
        if (this.modificationRequestForCbInfo != null) {
            ((ModificationRequestForCBInfoImpl) this.modificationRequestForCbInfo).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, TAG_MODIFICATION_REQUEST_FOR_CB_INFO);
        }
        if (this.modificationRequestForCSI != null) {
            ((ModificationRequestForCSIImpl) this.modificationRequestForCSI).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, TAG_MODIFICATION_REQUEST_FOR_CSI);
        }
        if (this.modificationRequestForODBdata != null) {
            ((ModificationRequestForODBdataImpl) this.modificationRequestForODBdata).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, TAG_MODIFICATION_REQUEST_FOR_ODB_DATA);
        }
        if (this.modificationRequestForIpSmGwData != null) {
            ((ModificationRequestForIPSMGWDataImpl) this.modificationRequestForIpSmGwData).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, TAG_MODIFICATION_REQUEST_FOR_IP_SM_GW_DATA);
        }
        if (this.activationRequestForUEReachability != null) {
            ((RequestedServingNodeImpl) this.activationRequestForUEReachability).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, TAG_ACTIVATION_REQUEST_FOR_UE_REACHABILITY);
        }
        if (this.modificationRequestForCSG != null) {
            ((ModificationRequestForCSGImpl) this.modificationRequestForCSG).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, TAG_MODIFICATION_REQUEST_FOR_CSG);
        }
        if (this.modificationRequestForCwData != null) {
            ((ModificationRequestForCWInfoImpl) this.modificationRequestForCwData).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, TAG_MODIFICATION_REQUEST_FOR_CW_DATA);
        }
        if (this.modificationRequestForClipData != null) {
            ((ModificationRequestForCLIPInfoImpl) this.modificationRequestForClipData).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, TAG_MODIFICATION_REQUEST_FOR_CLIP_DATA);
        }
        if (this.modificationRequestForClirData != null) {
            ((ModificationRequestForCLIRInfoImpl) this.modificationRequestForClirData).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, TAG_MODIFICATION_REQUEST_FOR_CLIR_DATA);
        }
        if (this.modificationRequestForHoldData != null) {
            ((ModificationRequestForCHInfoImpl) this.modificationRequestForHoldData).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, TAG_MODIFICATION_REQUEST_FOR_HOLD_DATA);
        }
        if (this.modificationRequestForEctData != null) {
            ((ModificationRequestForECTInfoImpl) this.modificationRequestForEctData).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, TAG_MODIFICATION_REQUEST_FOR_ECT_DATA);
        }

        if (this.extensionContainer != null) {
            ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, TAG_EXTENSION_CONTAINER);
        }

        NullEncoderFacility.encode(asnOs, this.longFtnSupported, Tag.CLASS_CONTEXT_SPECIFIC, TAG_LONG_FTN_SUPPORTED, "longFtnSupported");

    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation.
     * AnyTimeModificationRequestIndication#getSubscriberIdentity()
     */
    public SubscriberIdentity getSubscriberIdentity() {
        return this.subscriberIdentity;
    }

    public ISDNAddressString getGsmSCFAddress() {
        return this.gsmSCFAddress;
    }

    public ModificationRequestForECTInfo getModificationRequestForEctData() {
        return modificationRequestForEctData;
    }

    public ModificationRequestForCHInfo getModificationRequestForHoldData() {
        return modificationRequestForHoldData;
    }

    public ModificationRequestForCLIRInfo getModificationRequestForClirData() {
        return modificationRequestForClirData;
    }

    public ModificationRequestForCLIPInfo getModificationRequestForClipData() {
        return modificationRequestForClipData;
    }

    public ModificationRequestForCWInfo getModificationRequestForCwData() {
        return modificationRequestForCwData;
    }

    public ModificationRequestForCSG getModificationRequestForCSG() {
        return modificationRequestForCSG;
    }

    public RequestedServingNode getActivationRequestForUEReachability() {
        return activationRequestForUEReachability;
    }

    public ModificationRequestForIPSMGWData getModificationRequestForIpSmGwData() {
        return modificationRequestForIpSmGwData;
    }

    public ModificationRequestForODBdata getModificationRequestForODBdata() {
        return modificationRequestForODBdata;
    }

    public ModificationRequestForCSI getModificationRequestForCSI() {
        return modificationRequestForCSI;
    }

    public ModificationRequestForCBInfo getModificationRequestForCbInfo() {
        return modificationRequestForCbInfo;
    }

    public ModificationRequestForCFInfo getModificationRequestForCfInfo() {
        return modificationRequestForCfInfo;
    }

    /*
         * (non-Javadoc)
         *
         * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation.
         * AnyTimeModificationRequestIndication#getLongFTNSupported()
         */
    public boolean getLongFTNSupported() {
        return longFtnSupported;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation.
     * AnyTimeModificationRequestIndication#getExtensionContainer()
     */
    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.MAPMessage#getMessageType()
     */
    public MAPMessageType getMessageType() {
        return MAPMessageType.anyTimeModification_Request;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.MAPMessage#getOperationCode()
     */
    public int getOperationCode() {
        return MAPOperationCode.anyTimeModification;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.subscriberIdentity != null) {
            sb.append("subscriberIdentity=");
            sb.append(this.subscriberIdentity);
        }
        if (this.gsmSCFAddress != null) {
            sb.append(", gsmSCFAddress=");
            sb.append(this.gsmSCFAddress);
        }
        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.extensionContainer);
        }
        if (longFtnSupported) {
            sb.append(", longFtnSupported");
        }
        sb.append("]");
        return sb.toString();
    }
}
