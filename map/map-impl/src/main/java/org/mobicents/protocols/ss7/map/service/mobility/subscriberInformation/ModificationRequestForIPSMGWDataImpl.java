package org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.AbstractMAPAsnPrimitive;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.ModificationInstruction;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.ModificationRequestForIPSMGWData;
import org.mobicents.protocols.ss7.map.datacoding.IntegerEncoderFacility;
import org.mobicents.protocols.ss7.map.datacoding.ObjectEncoderFacility;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;

/**
 * <code>
     ModificationRequestFor-IP-SM-GW-Data ::= SEQUENCE {
         modifyRegistrationStatus   [0] ModificationInstruction OPTIONAL,
         extensionContainer         [1] ExtensionContainer OPTIONAL,
         ...
     }
 * </code>
 *
 * @author eva ogallar
 */
public class ModificationRequestForIPSMGWDataImpl extends AbstractMAPAsnPrimitive implements ModificationRequestForIPSMGWData {

    private ModificationInstruction modifyRegistrationStatus;
    private MAPExtensionContainer extensionContainer;

    private static final int TAG_MODIFY_REGISTRATION_STATUS = 0;
    private static final int TAG_EXTENSION_CONTAINER = 1;


    public static final String PRIMITIVE_NAME = "ModificationRequestForIPSMGWData";

    public ModificationRequestForIPSMGWDataImpl() {
    }

    public ModificationRequestForIPSMGWDataImpl(ModificationInstruction modifyRegistrationStatus, MAPExtensionContainer extensionContainer) {
        this.modifyRegistrationStatus = modifyRegistrationStatus;
        this.extensionContainer = extensionContainer;
    }

    @Override
    protected void _decode(AsnInputStream ansIS, int length) throws IOException, AsnException, MAPParsingComponentException {

        this.modifyRegistrationStatus = null;
        this.extensionContainer = null;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);

        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag(); //
            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                    case TAG_MODIFY_REGISTRATION_STATUS:
                        modifyRegistrationStatus = ModificationInstruction.getInstance(
                                new IntegerEncoderFacility(getPrimitiveName()).decode(ais, "modifyRegistrationStatus"));
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

        if (this.modifyRegistrationStatus != null) {
            IntegerEncoderFacility.encode(asnOs, modifyRegistrationStatus.getCode(), Tag.CLASS_CONTEXT_SPECIFIC,
                    TAG_MODIFY_REGISTRATION_STATUS, "modifyRegistrationStatus");
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

    public ModificationInstruction getModifyRegistrationStatus() {
        return modifyRegistrationStatus;
    }

    public MAPExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }
}
