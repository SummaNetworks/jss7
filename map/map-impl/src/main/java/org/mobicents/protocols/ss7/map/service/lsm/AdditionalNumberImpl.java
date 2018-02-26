/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

package org.mobicents.protocols.ss7.map.service.lsm;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.service.lsm.AdditionalNumber;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;

/**
 *
 *
 *
 * @author amit bhayani
 * @author eva ogallar
 *
 */
public class AdditionalNumberImpl implements AdditionalNumber, MAPAsnPrimitive {

    private static final int _TAG_MSC_NUMBER = 0;
    private static final int _TAG_SGSN_NUMBER = 1;

    public static final String _PrimitiveName = "AdditionalNumber";

    private ISDNAddressString mscNumber = null;
    private ISDNAddressString sgsnNumber = null;

    /**
     *
     */
    public AdditionalNumberImpl() {
        super();
    }

    /**
     * @param isMSC
     * @param number
     */
    public AdditionalNumberImpl(ISDNAddressString number, boolean isMSC) {
        super();
        if (isMSC) {
            this.mscNumber = number;
        } else {
            this.sgsnNumber = number;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm.AdditionalNumber#getMSCNumber ()
     */
    public ISDNAddressString getMSCNumber() {
        return this.mscNumber;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm.AdditionalNumber# getSGSNNumber()
     */
    public ISDNAddressString getSGSNNumber() {
        return this.sgsnNumber;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#getTag()
     */
    public int getTag() throws MAPException {
        if (this.mscNumber != null) {
            return _TAG_MSC_NUMBER;
        } else {
            return _TAG_SGSN_NUMBER;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#getTagClass ()
     */
    public int getTagClass() {
        return Tag.CLASS_CONTEXT_SPECIFIC;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#getIsPrimitive ()
     */
    public boolean getIsPrimitive() {
        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#decodeAll
     * (org.mobicents.protocols.asn.AsnInputStream)
     */
    public void decodeAll(AsnInputStream ansIS) throws MAPParsingComponentException {
        try {
            int length = ansIS.readLength();
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new MAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": ", e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new MAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": ", e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#decodeData
     * (org.mobicents.protocols.asn.AsnInputStream, int)
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

    private void _decode(AsnInputStream asnIS, int length) throws MAPParsingComponentException, IOException, AsnException {

        if (asnIS.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !asnIS.isTagPrimitive())
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": bad tag class or is not primitive: TagClass=" + asnIS.getTagClass(),
                    MAPParsingComponentExceptionReason.MistypedParameter);

        switch (asnIS.getTag()) {
            case _TAG_MSC_NUMBER:
                this.mscNumber = new ISDNAddressStringImpl();
                ((ISDNAddressStringImpl) this.mscNumber).decodeData(asnIS, length);
                break;
            case _TAG_SGSN_NUMBER:
                this.sgsnNumber = new ISDNAddressStringImpl();
                ((ISDNAddressStringImpl) this.sgsnNumber).decodeData(asnIS, length);
                break;
            default:
                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                        + ": Expexted msc-Number [0] ISDN-AddressString or sgsn-Number [1] ISDN-AddressString, but found "
                        + asnIS.getTag(), MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#encodeAll
     * (org.mobicents.protocols.asn.AsnOutputStream)
     */
    public void encodeAll(AsnOutputStream asnOs) throws MAPException {
        this.encodeAll(asnOs, this.getTagClass(), this.getTag());
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#encodeAll
     * (org.mobicents.protocols.asn.AsnOutputStream, int, int)
     */
    public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {
        try {
            asnOs.writeTag(tagClass, true, tag);
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
     * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#encodeData
     * (org.mobicents.protocols.asn.AsnOutputStream)
     */
    public void encodeData(AsnOutputStream asnOs) throws MAPException {
        if (this.mscNumber == null && this.sgsnNumber == null)
            throw new MAPException("Error when encoding " + _PrimitiveName + ": both mscNumber and sgsnNumber must not be null");
        if (this.mscNumber != null && this.sgsnNumber != null)
            throw new MAPException("Error when encoding " + _PrimitiveName
                    + ": both mscNumber and sgsnNumber must not be not null");

        if (this.mscNumber != null) {
            ((ISDNAddressStringImpl) this.mscNumber).encodeData(asnOs);
        } else {
            ((ISDNAddressStringImpl) this.sgsnNumber).encodeData(asnOs);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AdditionalNumber [");

        if (this.mscNumber != null) {
            sb.append("msc-Number=");
            sb.append(this.mscNumber.toString());
        }
        if (this.sgsnNumber != null) {
            sb.append("sgsn-Number=");
            sb.append(this.sgsnNumber.toString());
        }

        sb.append("]");

        return sb.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((mscNumber == null) ? 0 : mscNumber.hashCode());
        result = prime * result + ((sgsnNumber == null) ? 0 : sgsnNumber.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AdditionalNumberImpl other = (AdditionalNumberImpl) obj;
        if (mscNumber == null) {
            if (other.mscNumber != null)
                return false;
        } else if (!mscNumber.equals(other.mscNumber))
            return false;
        if (sgsnNumber == null) {
            if (other.sgsnNumber != null)
                return false;
        } else if (!sgsnNumber.equals(other.sgsnNumber))
            return false;
        return true;
    }

}
