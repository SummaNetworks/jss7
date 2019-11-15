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
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeSubscriptionInterrogationRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedSubscriptionInfo;
import org.mobicents.protocols.ss7.map.datacoding.NullEncoderFacility;
import org.mobicents.protocols.ss7.map.datacoding.ObjectEncoderFacility;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.primitives.SubscriberIdentityImpl;
import org.mobicents.protocols.ss7.map.service.mobility.MobilityMessageImpl;

/**
 *
 * <code>
 *     AnyTimeSubscriptionInterrogationArg ::= SEQUENCE {
 *          subscriberIdentity          [0] SubscriberIdentity,
 *          requestedSubscriptionInfo   [1] RequestedSubscriptionInfo,
 *          gsmSCF-Address              [2] ISDN-AddressString,
 *          extensionContainer          [3] ExtensionContainer OPTIONAL,
 *          longFTN-Supported           [4] NULL OPTIONAL,
 *          ...
 *          }
 * </code>
 * @author eva ogallar
 *
 */
public class AnyTimeSubscriptionInterrogationRequestImpl extends MobilityMessageImpl implements AnyTimeSubscriptionInterrogationRequest,
        MAPAsnPrimitive {

    private static final int _TAG_SUBSCRIBER_IDENTITY = 0;
    private static final int _TAG_REQUESTED_SUBSCRIPTION_INFO = 1;
    private static final int _TAG_GSM_SCF_ADDRESS = 2;
    private static final int _TAG_EXTENSION_CONTAINER = 3;
    private static final int _TAG_LONG_FTN_SUPPORTED = 4;

    public static final String _PrimitiveName = "AnyTimeSubscriptionInterrogationRequest";

    private SubscriberIdentity subscriberIdentity;
    private RequestedSubscriptionInfo requestedSubscriptionInfo;
    private ISDNAddressString gsmSCFAddress;
    private MAPExtensionContainer extensionContainer;
    private boolean longFtnSupported;

    public AnyTimeSubscriptionInterrogationRequestImpl() {

    }

    public AnyTimeSubscriptionInterrogationRequestImpl(SubscriberIdentity subscriberIdentity, RequestedSubscriptionInfo requestedSubscriptionInfo,
                                                       ISDNAddressString gsmSCFAddress, MAPExtensionContainer extensionContainer, boolean longFtnSupported) {
        this.subscriberIdentity = subscriberIdentity;
        this.requestedSubscriptionInfo = requestedSubscriptionInfo;
        this.gsmSCFAddress = gsmSCFAddress;
        this.extensionContainer = extensionContainer;
        this.longFtnSupported = longFtnSupported;
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
        this.requestedSubscriptionInfo = null;
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
                    case _TAG_SUBSCRIBER_IDENTITY:
/*
                        subscriberIdentity = (SubscriberIdentity) ObjectEncoderFacility.
                                decodeObject(ais, new SubscriberIdentityImpl(), "subscriberIdentity", _PrimitiveName);
                        break;
                    // decode SubscriberIdentity
*/
                        if (ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": Parameter subscriberIdentity is primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);

                        this.subscriberIdentity = new SubscriberIdentityImpl();
                        AsnInputStream ais2 = ais.readSequenceStream();
                        ais2.readTag();
                        ((SubscriberIdentityImpl) this.subscriberIdentity).decodeAll(ais2);
                        break;

                    case _TAG_REQUESTED_SUBSCRIPTION_INFO:
/*
                        // decode RequestedInfo
                        if (ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": Parameter requestedInfo is primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.requestedSubscriptionInfo = new RequestedSubscriptionInfoImpl();
                        ((RequestedSubscriptionInfoImpl) this.requestedSubscriptionInfo).decodeAll(ais);
                        //break;
*/
                        requestedSubscriptionInfo = (RequestedSubscriptionInfo) ObjectEncoderFacility.
                                decodeObject(ais, new RequestedSubscriptionInfoImpl(), "requestedSubscriptionInfo", _PrimitiveName);
                        break;
                    case _TAG_GSM_SCF_ADDRESS:
                        gsmSCFAddress = (ISDNAddressString) ObjectEncoderFacility.
                                decodePrimitiveObject(ais, new ISDNAddressStringImpl(), "gsmSCFAddress", _PrimitiveName);
                        break;
                    case _TAG_EXTENSION_CONTAINER:
                        extensionContainer = (MAPExtensionContainer) ObjectEncoderFacility.
                                decodePrimitiveObject(ais, new MAPExtensionContainerImpl(), "extensionContainer", _PrimitiveName);
                        break;
                    case _TAG_LONG_FTN_SUPPORTED:
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
        if (this.subscriberIdentity == null /*|| this.requestedSubscriptionInfo == null || this.gsmSCFAddress == null*/) {
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
        }/*
        if (this.requestedSubscriptionInfo == null) {
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + " the mandatory parameter requestedSubscriptionInfo is not defined");
        }
        if (this.gsmSCFAddress == null) {
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + " the mandatory parameter gsmSCF-Address is not defined");
        }*/

        try {
            asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_SUBSCRIBER_IDENTITY);
            int pos = asnOs.StartContentDefiniteLength();
            ((SubscriberIdentityImpl) this.subscriberIdentity).encodeAll(asnOs);
            asnOs.FinalizeContent(pos);
        } catch (AsnException e) {
            throw new MAPException("AsnException while encoding parameter targetMS [1] SubscriberIdentity");
        }

        if (this.requestedSubscriptionInfo!=null) {
            ((RequestedSubscriptionInfoImpl) this.requestedSubscriptionInfo).encodeAll(
                    asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_REQUESTED_SUBSCRIPTION_INFO);
        }

        if (gsmSCFAddress!=null) {
            ((ISDNAddressStringImpl) this.gsmSCFAddress).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_GSM_SCF_ADDRESS);
        }

        if (this.extensionContainer != null) {
            ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_EXTENSION_CONTAINER);
        }

        NullEncoderFacility.encode(asnOs, this.longFtnSupported, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_LONG_FTN_SUPPORTED, "longFtnSupported");

    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation.
     * AnyTimeSubscriptionInterrogationRequestIndication#getSubscriberIdentity()
     */
    public SubscriberIdentity getSubscriberIdentity() {
        return this.subscriberIdentity;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation.
     * AnyTimeSubscriptionInterrogationRequestIndication#getRequestedSubscriptionInfo()
     */
    public RequestedSubscriptionInfo getRequestedSubscriptionInfo() {
        return this.requestedSubscriptionInfo;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation.
     * AnyTimeSubscriptionInterrogationRequestIndication#getGsmScfAddress()
     */
    public ISDNAddressString getGsmScfAddress() {
        return this.gsmSCFAddress;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation.
     * AnyTimeSubscriptionInterrogationRequestIndication#getLongFTNSupported()
     */
    public boolean getLongFTNSupported() {
        return longFtnSupported;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.subscriberInformation.
     * AnyTimeSubscriptionInterrogationRequestIndication#getExtensionContainer()
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
        return MAPMessageType.anyTimeSubscriptionInterrogation_Request;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.MAPMessage#getOperationCode()
     */
    public int getOperationCode() {
        return MAPOperationCode.anyTimeSubscriptionInterrogation;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.subscriberIdentity != null) {
            sb.append("subscriberIdentity=");
            sb.append(this.subscriberIdentity);
        }
        if (this.requestedSubscriptionInfo != null) {
            sb.append(", requestedInfo=");
            sb.append(this.requestedSubscriptionInfo);
        }
        if (this.gsmSCFAddress != null) {
            sb.append(", gsmSCFAddress=");
            sb.append(this.gsmSCFAddress);
        }
        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.extensionContainer);
        }
        if (longFtnSupported){
            sb.append(", longFtnSupported");
        }
        sb.append("]");
        return sb.toString();
    }
}
