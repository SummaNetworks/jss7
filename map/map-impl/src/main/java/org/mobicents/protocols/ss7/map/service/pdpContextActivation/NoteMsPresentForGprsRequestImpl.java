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

package org.mobicents.protocols.ss7.map.service.pdpContextActivation;

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
import org.mobicents.protocols.ss7.map.api.primitives.GSNAddress;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.pdpContextActivation.NoteMsPresentForGprsRequest;
import org.mobicents.protocols.ss7.map.datacoding.ObjectEncoderFacility;
import org.mobicents.protocols.ss7.map.primitives.GSNAddressImpl;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;

/**
 *
 * NoteMsPresentForGprsArg ::= SEQUENCE { 
 *          imsi [0] IMSI, 
 *          sgsn-Address [1] GSN-Address, 
 *          ggsn-Address [2] GSN-Address OPTIONAL,
 *          extensionContainer [3] ExtensionContainer OPTIONAL, 
 *          ...}
 * @author eva ogallar
 *
 */
public class NoteMsPresentForGprsRequestImpl extends PdpContextActivationMessageImpl implements NoteMsPresentForGprsRequest {
    
    protected static final int _TAG_imsi = 0;
    protected static final int _TAG_sgsnAddress = 1;
    protected static final int _TAG_ggsnAddress = 2;
    protected static final int _TAG_extensionContainer = 3;

    public static final String _PrimitiveName = "NoteMsPresentForGprsRequest";

    private IMSI imsi;
    private GSNAddress sgsnAddress;
    private GSNAddress ggsnAddress;
    private MAPExtensionContainer extensionContainer;

    public NoteMsPresentForGprsRequestImpl() {
    }

    public NoteMsPresentForGprsRequestImpl(IMSI imsi, GSNAddress sgsnAddress, GSNAddress ggsnAddress, MAPExtensionContainer extensionContainer) {
        this.imsi = imsi;
        this.sgsnAddress = sgsnAddress;
        this.ggsnAddress = ggsnAddress;
        this.extensionContainer = extensionContainer;
    }

    @Override
    public MAPMessageType getMessageType() {
        return MAPMessageType.noteMsPresentForGprs_Request;
    }

    @Override
    public int getOperationCode() {
        return MAPOperationCode.noteMsPresentForGprs;
    }

    @Override
    public IMSI getImsi() {
        return imsi;
    }

    @Override
    public GSNAddress getGgsnAddress() {
        return ggsnAddress;
    }

    @Override
    public GSNAddress getSgsnAddress() {
        return sgsnAddress;
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
        return extensionContainer;
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
    public boolean getIsPrimitive() {
        return false;
    }

    @Override
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

    @Override
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

        this.imsi = null;
        this.ggsnAddress = null;
        this.sgsnAddress = null;
        this.extensionContainer = null;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);
        int num = 0;
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                case _TAG_imsi:
                    imsi = (IMSI) ObjectEncoderFacility.decodePrimitiveObject(
                            ais, new IMSIImpl(), "imsi", _PrimitiveName);
                    break;
                case _TAG_sgsnAddress:
                    sgsnAddress = (GSNAddress) ObjectEncoderFacility.decodePrimitiveObject(
                            ais, new GSNAddressImpl(), "sgsnAddress", _PrimitiveName);
                    break;
                case _TAG_ggsnAddress:
                    ggsnAddress = (GSNAddress) ObjectEncoderFacility.decodePrimitiveObject(
                            ais, new GSNAddressImpl(), "ggsnAddress", _PrimitiveName);
                    break;
                case _TAG_extensionContainer:
                    extensionContainer = (MAPExtensionContainer) ObjectEncoderFacility.
                            decodeObject(ais, new MAPExtensionContainerImpl(), "extensionContainer", _PrimitiveName);
                    break;
                default:
                    ais.advanceElement();
                    break;
                }
            } else {
                ais.advanceElement();
            }

            num++;
        }

        validate();
    }

    private void validate() throws MAPParsingComponentException {
        if (this.imsi == null)
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ": Parameter imsi is mandator but not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);
        if (this.sgsnAddress == null)
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ": Parameter ggsnNumber is mandator but not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);
    }

    @Override
    public void encodeAll(AsnOutputStream asnOs) throws MAPException {
        this.encodeAll(asnOs, this.getTagClass(), this.getTag());
    }

    @Override
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

    @Override
    public void encodeData(AsnOutputStream asnOs) throws MAPException {
        try {
            validate();
        } catch (MAPParsingComponentException e) {
            throw new MAPException(e.getMessage());
        }

        ((IMSIImpl) this.imsi).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_imsi);
        ((GSNAddressImpl) this.sgsnAddress).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_sgsnAddress);

        if (this.ggsnAddress != null) {
            ((GSNAddressImpl) this.ggsnAddress).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_ggsnAddress);
        }

        if (this.extensionContainer != null) {
            ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_extensionContainer);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.imsi != null) {
            sb.append("imsi=");
            sb.append(imsi);
            sb.append(", ");
        }
        if (this.sgsnAddress != null) {
            sb.append("sgsnNumber=");
            sb.append(sgsnAddress);
            sb.append(", ");
        }
        if (this.ggsnAddress != null) {
            sb.append("ggsnAddress=");
            sb.append(ggsnAddress);
            sb.append(", ");
        }
        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(extensionContainer);
            sb.append(", ");
        }

        sb.append("]");

        return sb.toString();
    }

}
