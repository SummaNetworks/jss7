package org.mobicents.protocols.ss7.map.api.primitives.nokia;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.AbstractMAPAsnPrimitive;


/**
 * <code>
    UlArgData ::= SEQUENCE {
         privateFeatureCode  [1] PrivateFeatureCode OPTIONAL,
         privateFeatureUlArgData  PrivateFeatureUlArgData OPTIONAL,
 ... }
 * </code>
 *
 * User: Eva Ogallar
 * Date: 7/02/19 12:13
 */
public class UlArgDataImpl extends AbstractMAPAsnPrimitive implements UlArgData {

    public static final int _TAG_privateFeatureCode = 1;
    public static final int _TAG_imei = 3;

    public static final String _PrimitiveName = "UlArgDataImpl";

    private PrivateFeatureUlArgData privateFeatureUlArgData;
    private PrivateFeatureCode privateFeatureCode;

    public UlArgDataImpl() {
    }

    public UlArgDataImpl(PrivateFeatureUlArgData privateFeatureUlArgData,
                                 PrivateFeatureCode privateFeatureCode) {
        this.privateFeatureUlArgData = privateFeatureUlArgData;
        this.privateFeatureCode = privateFeatureCode;
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

    public void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException, AsnException {

        this.privateFeatureUlArgData = null;
        this.privateFeatureCode = null;

        AsnInputStream ais = ansIS.readSequenceStreamData(length); //cojo el bloque de bytes. y el ansIS ha movido el cursor a la pos 7
        while (true) {
            if (ais.available() == 0) {
                break;
            }

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                    case _TAG_privateFeatureCode:
                        if (!ais.isTagPrimitive()) {
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": Parameter [privateFeatureCode [1] PrivateFeatureCode ] is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        }
                        this.privateFeatureCode = new PrivateFeatureCodeImpl();
                        ((PrivateFeatureCodeImpl) this.privateFeatureCode).decodeAll(ais);
                        break;
                    case _TAG_imei:
                        this.privateFeatureUlArgData = new PrivateFeatureUlArgDataImpl();
                        ((PrivateFeatureUlArgDataImpl) this.privateFeatureUlArgData).decodeAll(ais);
                        break;
                    default:
                        ais.advanceElement();
                        break;
                }
            }
        }
    }

    @Override
    public void encodeData(AsnOutputStream asnOs) throws MAPException {
        if (this.privateFeatureUlArgData != null) {
            ((PrivateFeatureUlArgDataImpl) this.privateFeatureUlArgData).encodeAll(asnOs);
        }
        if (this.privateFeatureCode != null) {
            ((PrivateFeatureCodeImpl) this.privateFeatureCode).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                    _TAG_privateFeatureCode);
        }
    }

    @Override
    protected String getPrimitiveName() {
        return _PrimitiveName;
    }

    @Override
    public PrivateFeatureUlArgData getPrivateFeatureUlArgData() {
        return this.privateFeatureUlArgData;
    }

    @Override
    public PrivateFeatureCode getPrivateFeatureCode() {
        return this.privateFeatureCode;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append("\n [");

        if (this.privateFeatureUlArgData != null) {
            sb.append("privateFeatureUlArgData=");
            sb.append(this.privateFeatureUlArgData.toString());
            sb.append(", ");
        }

        if (this.privateFeatureCode != null) {
            sb.append("privateFeatureCode=");
            sb.append(privateFeatureCode.toString());
            sb.append(", ");
        }

        sb.append("]");

        return sb.toString();
    }

}
