package org.mobicents.protocols.ss7.map.api.primitives.nokia;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.IMEI;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.AbstractMAPAsnPrimitive;
import org.mobicents.protocols.ss7.map.primitives.IMEIImpl;

/**
 * <code>
 * PrivateFeatureUlArgData ::= CHOICE {
 * adc [3] IMEI
 * }
 * </code>
 * <p>
 * User: Eva Ogallar
 * Date: 7/02/19 12:13
 */
public class PrivateFeatureUlArgDataImpl extends AbstractMAPAsnPrimitive implements PrivateFeatureUlArgData {


    private static final int _ADC = 3;

    public static final String _PrimitiveName = "PrivateFeatureUlArgDataImpl";

    private IMEI adc = null;

    /**
     *
     */
    public PrivateFeatureUlArgDataImpl() {
        super();
    }

    /**
     * @param number
     */
    public PrivateFeatureUlArgDataImpl(IMEI number) {
        super();
        this.adc = number;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.service.lsm.AdditionalNumber#getAdc ()
     */
    public IMEI getAdc() {
        return this.adc;
    }


    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#getTag()
     */
    public int getTag() throws MAPException {
        return _ADC;
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

    public void _decode(AsnInputStream asnIS, int length) throws MAPParsingComponentException, IOException, AsnException {

        if (asnIS.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !asnIS.isTagPrimitive())
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": bad tag class or is not primitive: TagClass=" + asnIS.getTagClass(),
                    MAPParsingComponentExceptionReason.MistypedParameter);

        switch (asnIS.getTag()) {
            case _ADC:
                this.adc = new IMEIImpl();
                ((IMEIImpl) this.adc).decodeData(asnIS, length);
                break;
            default:
                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                        + ": Expexted adc [3] IMEI , but found "
                        + asnIS.getTag(), MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#encodeData
     * (org.mobicents.protocols.asn.AsnOutputStream)
     */
    public void encodeData(AsnOutputStream asnOs) throws MAPException {
        if (this.adc == null)
            throw new MAPException("Error when encoding " + _PrimitiveName + ": both adc must not be null");

        ((IMEIImpl) this.adc).encodeData(asnOs);
    }

    @Override
    protected String getPrimitiveName() {
        return _PrimitiveName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AdditionalNumber [");

        if (this.adc != null) {
            sb.append("msc-Number=");
            sb.append(this.adc.toString());
        }
        sb.append("]");

        return sb.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((adc == null) ? 0 : adc.hashCode());
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
        PrivateFeatureUlArgDataImpl other = (PrivateFeatureUlArgDataImpl) obj;
        if (adc == null) {
            if (other.adc != null)
                return false;
        } else if (!adc.equals(other.adc))
            return false;
        return true;
    }

}
