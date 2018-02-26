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
public class NullEncoderFacility{

    public static boolean decode(AsnInputStream ais, String field, String primitiveName) throws MAPParsingComponentException, AsnException, IOException {
        if (!ais.isTagPrimitive())
            throw new MAPParsingComponentException(
                    "Error while decoding " + primitiveName +" : Parameter " + field + " is not primitive",
                    MAPParsingComponentExceptionReason.MistypedParameter);
        ais.readNull();
        return Boolean.TRUE;
    }

    public static void encode(AsnOutputStream asnOs, boolean fieldValue, int tagClass, int tagPossition, final String fieldName) throws MAPException {
        try {
            if (fieldValue) {
                asnOs.writeNull(tagClass, tagPossition);
            }
        } catch (IOException e) {
            throw new MAPException("IOException when encoding parameter " + fieldName + ": ", e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding parameter " + fieldName + ": ", e);
        }
    }

    public static void encode(AsnOutputStream asnOs, boolean fieldValue, final String fieldName) throws MAPException {
        try {
            if (fieldValue) {
                asnOs.writeNull();
            }
        } catch (IOException e) {
            throw new MAPException("IOException when encoding parameter " + fieldName + ": ", e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding parameter " + fieldName + ": ", e);
        }
    }


}
