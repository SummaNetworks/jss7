package org.mobicents.protocols.ss7.map.datacoding;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;

/**
 * @author eva ogallar
 */
public class IntegerEncoderFacility {

    private final String primitiveName;

    public IntegerEncoderFacility(String primitiveName) {
        this.primitiveName = primitiveName;
    }

    public int decode(AsnInputStream ais, String field) throws MAPParsingComponentException, AsnException, IOException {


        if (!ais.isTagPrimitive())
            throw new MAPParsingComponentException(
                    "Error while decoding " + primitiveName +" : Parameter " + field + " is not primitive",
                    MAPParsingComponentExceptionReason.MistypedParameter);
        return (int) ais.readInteger();
    }

    public void encode(AsnOutputStream asnOs, Integer fieldValue, int tagClass, int tagPossition, final String fieldName) throws MAPException {
        try {
            asnOs.writeInteger(tagClass, tagPossition, fieldValue);
        } catch (IOException e) {
            throw new MAPException("IOException when encoding parameter " + fieldName + ": ", e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding parameter " + fieldName + ": ", e);
        }
    }

    public void encode(AsnOutputStream asnOs, Integer fieldValue, final String fieldName) throws MAPException {
        try {
            asnOs.writeInteger(fieldValue);
        } catch (IOException e) {
            throw new MAPException("IOException when encoding parameter " + fieldName + ": ", e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding parameter " + fieldName + ": ", e);
        }
    }


}
