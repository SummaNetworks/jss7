package org.mobicents.protocols.ss7.map.datacoding;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;

/**
 * @author eva ogallar
 */
public class ObjectEncoderFacility {


    public static MAPAsnPrimitive decodeObject(AsnInputStream ais, MAPAsnPrimitive mapAsnPrimitive, String field, String primitiveName)
            throws MAPParsingComponentException, AsnException, IOException {
        if (ais.isTagPrimitive()) {
            throw new MAPParsingComponentException("Error while decoding " + primitiveName
                    + ": Parameter " + field + " is primitive",
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
        return decodeObjectNotFramed(ais, mapAsnPrimitive);
    }

    public static MAPAsnPrimitive decodePrimitiveObject(AsnInputStream ais, MAPAsnPrimitive mapAsnPrimitive,
                                                        String field, String primitiveName) throws MAPParsingComponentException, AsnException, IOException {
        if (!ais.isTagPrimitive()) {
            throw new MAPParsingComponentException("Error while decoding " + primitiveName
                    + ": Parameter " + field + " is not primitive",
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
        mapAsnPrimitive.decodeAll(ais);
        return mapAsnPrimitive;
    }

    private static MAPAsnPrimitive decodeObject(AsnInputStream ais, MAPAsnPrimitive mapAsnPrimitive)
            throws AsnException, IOException, MAPParsingComponentException {
        AsnInputStream ais2 = ais.readSequenceStream();
        ais2.readTag();
        mapAsnPrimitive.decodeAll(ais2);
        return mapAsnPrimitive;
    }

    private static MAPAsnPrimitive decodeObjectNotFramed(AsnInputStream ais, MAPAsnPrimitive mapAsnPrimitive)
            throws AsnException, IOException, MAPParsingComponentException {
        mapAsnPrimitive.decodeAll(ais);
        return mapAsnPrimitive;
    }


}
