package org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.AbstractMAPAsnPrimitive;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.ModificationInstruction;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.ModificationRequestForODBdata;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ODBData;
import org.mobicents.protocols.ss7.map.datacoding.IntegerEncoderFacility;
import org.mobicents.protocols.ss7.map.datacoding.ObjectEncoderFacility;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ODBDataImpl;

/**
 * <code>
     ModificationRequestFor-ODB-data ::= SEQUENCE {
         odb-data                  [0] ODB-Data OPTIONAL,
         modifyNotificationToCSE   [1] ModificationInstruction OPTIONAL,
         extensionContainer        [2] ExtensionContainer OPTIONAL,
         ...
     }

     WrongPasswordAttemptsCounter ::= INTEGER (0..4)
 * </code>
 *
 * @author eva ogallar
 */
public class ModificationRequestForODBdataImpl extends AbstractMAPAsnPrimitive implements ModificationRequestForODBdata {

    private ODBData odbData;
    private ModificationInstruction modifyNotificationToCSE;
    private MAPExtensionContainer extensionContainer;

    private final static int TAG_ODB_DATA = 0;
    private final static int TAG_MODIFY_NOTIFICATION_TO_CSE = 1;
    private final static int TAG_EXTENSION_CONTAINER = 2;


    public static final String PRIMITIVE_NAME = "ModificationRequestForODBdata";

    public ModificationRequestForODBdataImpl() {
    }

    public ModificationRequestForODBdataImpl(ODBData odbData, ModificationInstruction modifyNotificationToCSE, MAPExtensionContainer extensionContainer) {
        this.odbData = odbData;
        this.modifyNotificationToCSE = modifyNotificationToCSE;
        this.extensionContainer = extensionContainer;
    }

    @Override
    protected void _decode(AsnInputStream ansIS, int length) throws IOException, AsnException, MAPParsingComponentException {

        this.odbData = null;
        this.modifyNotificationToCSE = null;
        this.extensionContainer = null;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);

        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag(); //
            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                    case TAG_ODB_DATA:
                        odbData = (ODBData) ObjectEncoderFacility.decodeObject(ais, new ODBDataImpl(), "odbData", getPrimitiveName());
                        break;
                    case TAG_MODIFY_NOTIFICATION_TO_CSE:
                        modifyNotificationToCSE = ModificationInstruction.getInstance(
                                new IntegerEncoderFacility(getPrimitiveName()).decode(ais, "modifyNotificationToCSE"));
                        break;
                    case TAG_EXTENSION_CONTAINER:
                        extensionContainer = (MAPExtensionContainer) ObjectEncoderFacility.
                                decodePrimitiveObject(ais, new MAPExtensionContainerImpl(), "extensionContainer", getPrimitiveName());
                        break;
                    default:
                        ais.advanceElement();
                        break;
                }
            } else {
                ais.advanceElement();
            }
        }
        if (this.odbData == null) {
            throw new MAPParsingComponentException(
                    "Error while decoding " + getPrimitiveName() + ": odbData is mandatory but not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }

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
    public void encodeData(AsnOutputStream asnOs) throws MAPException {
        if (this.odbData == null) {
            throw new MAPException("Error while encoding " + getPrimitiveName()
                    + " the mandatory parameter odbData is not defined");
        }

        ((ODBDataImpl) this.odbData).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, TAG_ODB_DATA);

        if (this.modifyNotificationToCSE != null) {
            IntegerEncoderFacility.encode(asnOs, modifyNotificationToCSE.getCode(), Tag.CLASS_CONTEXT_SPECIFIC,
                    TAG_MODIFY_NOTIFICATION_TO_CSE, "modifyNotificationToCSE");
        }
        if (this.extensionContainer != null) {
            ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, TAG_EXTENSION_CONTAINER);
        }

    }

    @Override
    protected String getPrimitiveName() {
        return PRIMITIVE_NAME;
    }

    @Override
    public boolean getIsPrimitive() {
        return false;
    }

    public ODBData getOdbData() {
        return odbData;
    }

    public ModificationInstruction getModifyNotificationToCSE() {
        return modifyNotificationToCSE;
    }

    public MAPExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }
}
