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

package org.mobicents.protocols.ss7.map.service.sms;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.LMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.lsm.AdditionalNumber;
import org.mobicents.protocols.ss7.map.api.service.lsm.NetworkNodeDiameterAddress;
import org.mobicents.protocols.ss7.map.api.service.sms.LocationInfoWithLMSI;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.LMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.primitives.SequenceBase;
import org.mobicents.protocols.ss7.map.service.lsm.AdditionalNumberImpl;
import org.mobicents.protocols.ss7.map.service.lsm.NetworkNodeDiameterAddressImpl;

/**
 * <code>
 * v3
 * LocationInfoWithLMSI ::= SEQUENCE {
 * networkNode-Number    [1] ISDN-AddressString,
 * lmsi                  LMSI OPTIONAL,
 * extensionContainer    ExtensionContainer OPTIONAL,
 * ...,
 * gprsNodeIndicator     [5] NULL OPTIONAL,
 * -- gprsNodeIndicator is set only if the SGSN number is sent as the
 * -- Network Node Number
 * additional-Number     [6] Additional-Number OPTIONAL
 * -- NetworkNode-number can be either msc-number or sgsn-number
 * <p>
 * Additional-Number ::= CHOICE {
 * msc-Number          [0] ISDN-AddressString,
 * sgsn-Number         [1] ISDN-AddressString
 * }
 * -- additional-number can be either msc-number or sgsn-number
 * -- if received networkNode-number is msc-number then the
 * -- additional number is sgsn-number
 * -- if received networkNode-number is sgsn-number then the
 * -- additional number is msc-number
 * }
 * <p>
 * v1-2
 * LocationInfoWithLMSI ::= SEQUENCE {
 * locationInfo    LocationInfo,
 * lmsi            LMSI                    OPTIONAL,
 * ...}
 * <p>
 * LocationInfo ::= CHOICE {
 * roamingNumber    [0] ISDN-AddressString,
 * -- NU>1 roamingNumber must not be used in version greater 1
 * msc-Number       [1] ISDN-AddressString
 * }
 * <p>
 * TODO:
 * LocationInfoWithLMSI ::= SEQUENCE {
 * networkNode-Number [1] ISDN-AddressString,
 * lmsi LMSI OPTIONAL,
 * extensionContainer ExtensionContainer OPTIONAL,
 * ...,
 * gprsNodeIndicator [5] NULL OPTIONAL,
 * -- gprsNodeIndicator is set only if the SGSN number is sent as the
 * -- Network Node Number
 * additional-Number [6] Additional-Number OPTIONAL,
 * networkNodeDiameterAddress [7] NetworkNodeDiameterAddress OPTIONAL,
 * additionalNetworkNodeDiameterAddress [8] NetworkNodeDiameterAddress OPTIONAL,
 * thirdNumber [9] Additional-Number OPTIONAL,
 * thirdNetworkNodeDiameterAddress [10] NetworkNodeDiameterAddress OPTIONAL,
 * imsNodeIndicator [11] NULL OPTIONAL
 * -- gprsNodeIndicator and imsNodeIndicator shall not both be present.
 * -- additionalNumber and thirdNumber shall not both contain the same type of number.
 * }
 * </code>
 */
public class LocationInfoWithLMSIImpl extends SequenceBase implements LocationInfoWithLMSI {

    private static final int _TAG_NetworkNodeNumber_or_MscNumber = 1;
    private static final int _TAG_RoamingNumber = 0;
    private static final int _TAG_GprsNodeIndicator = 5;
    private static final int _TAG_AdditionalNumber = 6;
    private static final int _TAG_NetworkNodeDiameterAddress = 7;
    private static final int _TAG_AdditionalNetworkNodeDiameterAddress = 8;
    private static final int _TAG_ThirdNumber = 9;
    private static final int _TAG_ThirdNetworkNodeDiameterAddress = 10;
    private static final int _TAG_ImsNodeIndicator = 11;
    public static final String PRIMITIVE_NAME = "LocationInfoWithLMSI";

    private ISDNAddressString networkNodeNumber;
    private ISDNAddressString roamingNumber;
    private LMSI lmsi;
    private MAPExtensionContainer extensionContainer;
    private boolean gprsNodeIndicator;
    private AdditionalNumber additionalNumber;
    private NetworkNodeDiameterAddress networkNodeDiameterAddress;
    private NetworkNodeDiameterAddress additionalNetworkNodeDiameterAddress;
    private AdditionalNumber thirdNumber;
    private NetworkNodeDiameterAddress thirdNetworkNodeDiameterAddress;
    private boolean imsNodeIndicator;

    private long mapProtocolVersion;

    public LocationInfoWithLMSIImpl(long mapProtocolVersion) {
        super(PRIMITIVE_NAME);
        this.mapProtocolVersion = mapProtocolVersion;
    }

    public LocationInfoWithLMSIImpl(long mapProtocolVersion, ISDNAddressString networkNodeNumber, LMSI lmsi, MAPExtensionContainer extensionContainer, boolean gprsNodeIndicator,
                                    AdditionalNumber additionalNumber) {
        super(PRIMITIVE_NAME);
        this.mapProtocolVersion = mapProtocolVersion;

        this.networkNodeNumber = networkNodeNumber;
        this.lmsi = lmsi;
        this.extensionContainer = extensionContainer;
        this.gprsNodeIndicator = gprsNodeIndicator;
        this.additionalNumber = additionalNumber;
    }

    public LocationInfoWithLMSIImpl(long mapProtocolVersion, ISDNAddressString networkNodeNumber,
                                    LMSI lmsi, MAPExtensionContainer extensionContainer, boolean gprsNodeIndicator,
                                    AdditionalNumber additionalNumber, NetworkNodeDiameterAddress networkNodeDiameterAddress,
                                    NetworkNodeDiameterAddress additionalNetworkNodeDiameterAddress, AdditionalNumber thirdNumber,
                                    NetworkNodeDiameterAddress thirdNetworkNodeDiameterAddress, boolean imsNodeIndicator) {
        super(PRIMITIVE_NAME);
        this.networkNodeNumber = networkNodeNumber;
        this.lmsi = lmsi;
        this.extensionContainer = extensionContainer;
        this.gprsNodeIndicator = gprsNodeIndicator;
        this.additionalNumber = additionalNumber;
        this.mapProtocolVersion = mapProtocolVersion;
        this.networkNodeDiameterAddress = networkNodeDiameterAddress;
        this.additionalNetworkNodeDiameterAddress = additionalNetworkNodeDiameterAddress;
        this.thirdNumber = thirdNumber;
        this.thirdNetworkNodeDiameterAddress = thirdNetworkNodeDiameterAddress;
        this.imsNodeIndicator = imsNodeIndicator;
    }

    @Override
    public ISDNAddressString getNetworkNodeNumber() {
        return this.networkNodeNumber;
    }

    @Override
    public LMSI getLMSI() {
        return this.lmsi;
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    public boolean getGprsNodeIndicator() {
        return gprsNodeIndicator;
    }

    @Override
    public AdditionalNumber getAdditionalNumber() {
        return this.additionalNumber;
    }

    @Override
    public NetworkNodeDiameterAddress getNetworkNodeDiameterAddress() {
        return networkNodeDiameterAddress;
    }

    @Override
    public NetworkNodeDiameterAddress getAdditionalNetworkNodeDiameterAddress() {
        return additionalNetworkNodeDiameterAddress;
    }

    @Override
    public AdditionalNumber getThirdNumber() {
        return thirdNumber;
    }

    @Override
    public NetworkNodeDiameterAddress getThirdNetworkNodeDiameterAddress() {
        return thirdNetworkNodeDiameterAddress;
    }

    @Override
    public boolean getImsNodeIndicator() {
        return imsNodeIndicator;
    }

    protected void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException, AsnException {
        this.networkNodeNumber = null;
        this.lmsi = null;
        this.extensionContainer = null;
        this.gprsNodeIndicator = false;
        this.additionalNumber = null;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);

        int num = 0;
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            if (num == 0) {
                // first parameter is mandatory - networkNode-Number
                if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive() ||
                        (tag != _TAG_NetworkNodeNumber_or_MscNumber && (tag != _TAG_RoamingNumber || this.mapProtocolVersion != 1)))
                    throw new MAPParsingComponentException(
                            "Error when decoding LocationInfoWithLMSI: networkNode-Number: tagClass or tag is bad or element is not primitive: tagClass="
                                    + ais.getTagClass() + ", Tag=" + tag, MAPParsingComponentExceptionReason.MistypedParameter);

                if (tag == _TAG_NetworkNodeNumber_or_MscNumber) {
                    this.networkNodeNumber = new ISDNAddressStringImpl();
                    ((ISDNAddressStringImpl) this.networkNodeNumber).decodeAll(ais);
                } else {
                    this.roamingNumber = new ISDNAddressStringImpl();
                    ((ISDNAddressStringImpl) this.roamingNumber).decodeAll(ais);
                }
                //break;
            } else {
                // optional parameters
                if (ais.getTagClass() == Tag.CLASS_UNIVERSAL) {

                    switch (tag) {
                        case Tag.STRING_OCTET:
                            if (!ais.isTagPrimitive() || this.lmsi != null)
                                throw new MAPParsingComponentException("Error when decoding " + _PrimitiveName
                                        + ": lmsi: double element or element is not primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                            this.lmsi = new LMSIImpl();
                            ((LMSIImpl) this.lmsi).decodeAll(ais);
                            break;

                        case Tag.SEQUENCE:
                            if (ais.isTagPrimitive() || this.extensionContainer != null)
                                throw new MAPParsingComponentException("Error when decoding " + _PrimitiveName
                                        + ": extensionContainer: double element or element is primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                            this.extensionContainer = new MAPExtensionContainerImpl();
                            ((MAPExtensionContainerImpl) this.extensionContainer).decodeAll(ais);
                            break;

                        default:
                            ais.advanceElement();
                            break;
                    }
                } else if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {

                    switch (tag) {
                        case _TAG_GprsNodeIndicator:
                            if (!ais.isTagPrimitive() || this.gprsNodeIndicator)
                                throw new MAPParsingComponentException("Error when decoding " + _PrimitiveName
                                        + ": gprsNodeIndicator: double element or element is not primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                            ais.readNull();
                            this.gprsNodeIndicator = true;
                            break;

                        case _TAG_AdditionalNumber:
                            if (ais.isTagPrimitive() || this.additionalNumber != null)
                                throw new MAPParsingComponentException("Error when decoding " + _PrimitiveName
                                        + ": additionalNumber: double element or element is primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                            AsnInputStream ais2 = ais.readSequenceStream();
                            ais2.readTag();
                            this.additionalNumber = new AdditionalNumberImpl();
                            ((AdditionalNumberImpl) this.additionalNumber).decodeAll(ais2);
                            break;

                        case _TAG_NetworkNodeDiameterAddress:
                            if (ais.isTagPrimitive() || this.networkNodeDiameterAddress != null)
                                throw new MAPParsingComponentException("Error when decoding " + _PrimitiveName
                                        + ": networkNodeDiameterAddress: double element or element is primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                            this.networkNodeDiameterAddress = new NetworkNodeDiameterAddressImpl();
                            ((NetworkNodeDiameterAddressImpl) this.networkNodeDiameterAddress).decodeAll(ais);
                            break;

                        case _TAG_AdditionalNetworkNodeDiameterAddress:
                            if (ais.isTagPrimitive() || this.additionalNetworkNodeDiameterAddress != null)
                                throw new MAPParsingComponentException("Error when decoding " + _PrimitiveName
                                        + ": additionalNetworkNodeDiameterAddress: double element or element is primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                            this.additionalNetworkNodeDiameterAddress = new NetworkNodeDiameterAddressImpl();
                            ((NetworkNodeDiameterAddressImpl) this.additionalNetworkNodeDiameterAddress).decodeAll(ais);
                            break;

                        case _TAG_ThirdNumber:
                            if (ais.isTagPrimitive() || this.thirdNumber != null)
                                throw new MAPParsingComponentException("Error when decoding " + _PrimitiveName
                                        + ": thirdNumber: double element or element is primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                            ais2 = ais.readSequenceStream();
                            ais2.readTag();
                            this.thirdNumber = new AdditionalNumberImpl();
                            ((AdditionalNumberImpl) this.thirdNumber).decodeAll(ais2);
                            break;

                        case _TAG_ThirdNetworkNodeDiameterAddress:
                            if (ais.isTagPrimitive() || this.thirdNetworkNodeDiameterAddress != null)
                                throw new MAPParsingComponentException("Error when decoding " + _PrimitiveName
                                        + ": thirdNetworkNodeDiameterAddress: double element or element is primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                            this.thirdNetworkNodeDiameterAddress = new NetworkNodeDiameterAddressImpl();
                            ((NetworkNodeDiameterAddressImpl) this.thirdNetworkNodeDiameterAddress).decodeAll(ais);
                            break;
                        case _TAG_ImsNodeIndicator:
                            if (!ais.isTagPrimitive() || this.imsNodeIndicator)
                                throw new MAPParsingComponentException("Error when decoding " + _PrimitiveName
                                        + ": imsNodeIndicator: double element or element is not primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                            ais.readNull();
                            this.imsNodeIndicator = true;
                            break;


                        default:
                            ais.advanceElement();
                            break;
                    }
                } else {
                    ais.advanceElement();
                }
            }

            num++;
        }

        if (this.networkNodeNumber == null)
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ": 1 parameter is mandatory, found " + num,
                    MAPParsingComponentExceptionReason.MistypedParameter);
    }

    public void encodeData(AsnOutputStream asnOs) throws MAPException {

        try {
            if (this.networkNodeNumber == null && mapProtocolVersion > 1)
                throw new MAPException("Error while encoding " + _PrimitiveName + ": networkNodeNumber must not be null");

            if ((this.networkNodeNumber == null && this.roamingNumber == null) && mapProtocolVersion == 1)
                throw new MAPException("Error while encoding " + _PrimitiveName + ": networkNodeNumber and roaming number must not be null for protocol version 1");

            if (this.networkNodeNumber != null)
                ((ISDNAddressStringImpl) this.networkNodeNumber).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_NetworkNodeNumber_or_MscNumber);
            else
                ((ISDNAddressStringImpl) this.roamingNumber).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_RoamingNumber);

            if (this.lmsi != null)
                ((LMSIImpl) this.lmsi).encodeAll(asnOs);

            if (mapProtocolVersion > 2) {
                if (this.extensionContainer != null)
                    ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs);

                if (gprsNodeIndicator)
                    asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_GprsNodeIndicator);

                if (this.additionalNumber != null) {
                    asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_AdditionalNumber);
                    int pos = asnOs.StartContentDefiniteLength();
                    ((AdditionalNumberImpl) this.additionalNumber).encodeAll(asnOs);
                    asnOs.FinalizeContent(pos);
                }

                if (this.networkNodeDiameterAddress != null) {
                    ((NetworkNodeDiameterAddressImpl) this.networkNodeDiameterAddress).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_NetworkNodeDiameterAddress);

/*

                    asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_NetworkNodeDiameterAddress);
                    int pos = asnOs.StartContentDefiniteLength();
                    ((NetworkNodeDiameterAddressImpl) this.networkNodeDiameterAddress).encodeAll(asnOs);
                    asnOs.FinalizeContent(pos);
*/
                }
                if (this.additionalNetworkNodeDiameterAddress != null) {
                    ((NetworkNodeDiameterAddressImpl) this.additionalNetworkNodeDiameterAddress).encodeAll(
                            asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_AdditionalNetworkNodeDiameterAddress);

                    /*
                    asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_AdditionalNetworkNodeDiameterAddress);
                    int pos = asnOs.StartContentDefiniteLength();
                    ((NetworkNodeDiameterAddressImpl) this.additionalNetworkNodeDiameterAddress).encodeAll(asnOs);
                    asnOs.FinalizeContent(pos);
*/
                }
                if (this.thirdNumber != null) {
                    asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_ThirdNumber);
                    int pos = asnOs.StartContentDefiniteLength();
                    ((AdditionalNumberImpl) this.thirdNumber).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                            ((AdditionalNumberImpl) this.thirdNumber).getTag());
                    asnOs.FinalizeContent(pos);
                }
                if (this.thirdNetworkNodeDiameterAddress != null) {
                    ((NetworkNodeDiameterAddressImpl) this.thirdNetworkNodeDiameterAddress).encodeAll(
                            asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_ThirdNetworkNodeDiameterAddress);
/*
                    asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_ThirdNetworkNodeDiameterAddress);
                    int pos = asnOs.StartContentDefiniteLength();
                    ((NetworkNodeDiameterAddressImpl) this.thirdNetworkNodeDiameterAddress).encodeAll(asnOs);
                    asnOs.FinalizeContent(pos);
*/
                }

                if (imsNodeIndicator)
                    asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_ImsNodeIndicator);

            }
        } catch (IOException e) {
            throw new MAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.networkNodeNumber != null) {
            sb.append("networkNodeNumber=");
            sb.append(this.networkNodeNumber.toString());
        }
        if (this.lmsi != null) {
            sb.append(", lmsi=");
            sb.append(this.lmsi.toString());
        }
        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.extensionContainer.toString());
        }
        if (this.gprsNodeIndicator) {
            sb.append(", gprsNodeIndicator");
        }
        if (this.additionalNumber != null) {
            sb.append(", additionalNumber=");
            sb.append(this.additionalNumber.toString());
        }

        if (this.networkNodeDiameterAddress != null) {
            sb.append(", networkNodeDiameterAddress=").append(this.networkNodeDiameterAddress.toString());
        }
        if (this.additionalNetworkNodeDiameterAddress != null) {
            sb.append(", additionalNetworkNodeDiameterAddress=").append(this.additionalNetworkNodeDiameterAddress.toString());
        }
        if (this.thirdNumber != null) {
            sb.append(", thirdNumber=").append(this.thirdNumber.toString());
        }
        if (this.thirdNetworkNodeDiameterAddress != null) {
            sb.append(", thirdNetworkNodeDiameterAddress=").append(this.thirdNetworkNodeDiameterAddress.toString());
        }

        sb.append("]");

        return sb.toString();
    }
}
