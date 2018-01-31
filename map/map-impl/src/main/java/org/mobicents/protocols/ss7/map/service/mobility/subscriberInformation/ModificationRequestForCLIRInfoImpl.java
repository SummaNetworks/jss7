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
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.ModificationRequestForCLIRInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSStatus;
import org.mobicents.protocols.ss7.map.api.service.supplementary.CliRestrictionOption;
import org.mobicents.protocols.ss7.map.datacoding.IntegerEncoderFacility;
import org.mobicents.protocols.ss7.map.datacoding.ObjectEncoderFacility;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtSSStatusImpl;

/**
 * <code>
 * ModificationRequestFor-CLIR-Info ::= SEQUENCE {
 *      ss-Status                [0] Ext-SS-Status OPTIONAL,
 *      cliRestrictionOption     [1] CliRestrictionOption OPTIONAL,
 *      modifyNotificationToCSE  [2] ModificationInstruction OPTIONAL,
 *      extensionContainer       [3] ExtensionContainer OPTIONAL,
 *      ...
 * }
 * </code>
 *
 * @author eva ogallar
 */
public class ModificationRequestForCLIRInfoImpl extends AbstractMAPAsnPrimitive implements ModificationRequestForCLIRInfo {

    private ExtSSStatus ssStatus;
    private CliRestrictionOption cliRestrictionOption;
    private ModificationInstruction modifyNotificationToCSE;
    private MAPExtensionContainer extensionContainer;

    private static final int TAG_SS_STATUS = 0;
    private static final int TAG_CLI_RESTRICTION_OPTION = 1;
    private static final int TAG_MODIFY_NOTIFICATION_TO_CSE = 2;
    private static final int TAG_EXTENSION_CONTAINER = 3;


    public static final String PRIMITIVE_NAME = "ModificationRequestForCLIRInfo";

    public ModificationRequestForCLIRInfoImpl() {
    }

    public ModificationRequestForCLIRInfoImpl(ExtSSStatus ssStatus, CliRestrictionOption cliRestrictionOption,
                                              ModificationInstruction modifyNotificationToCSE, MAPExtensionContainer extensionContainer) {
        this.ssStatus = ssStatus;
        this.cliRestrictionOption = cliRestrictionOption;
        this.modifyNotificationToCSE = modifyNotificationToCSE;
        this.extensionContainer = extensionContainer;
    }

    @Override
    protected void _decode(AsnInputStream ansIS, int length) throws IOException, AsnException, MAPParsingComponentException {

        this.ssStatus = null;
        this.cliRestrictionOption = null;
        this.modifyNotificationToCSE = null;
        this.extensionContainer = null;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);

        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag(); //
            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                    case TAG_SS_STATUS:
                        ssStatus = (ExtSSStatus) ObjectEncoderFacility.decodePrimitiveObject(ais, new ExtSSStatusImpl(), "ssStatus", getPrimitiveName());
                        break;
                    case TAG_CLI_RESTRICTION_OPTION:
                        cliRestrictionOption = CliRestrictionOption.getInstance(new IntegerEncoderFacility(getPrimitiveName()).decode(ais, "cliRestrictionOption"));
                        break;
                    case TAG_MODIFY_NOTIFICATION_TO_CSE:
                        modifyNotificationToCSE = ModificationInstruction.getInstance(new IntegerEncoderFacility(getPrimitiveName()).decode(ais, "modifyNotificationToCSE"));
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

        if (this.cliRestrictionOption != null) {
            IntegerEncoderFacility.encode(asnOs, cliRestrictionOption.getCode(), Tag.CLASS_CONTEXT_SPECIFIC,
                    TAG_CLI_RESTRICTION_OPTION, "cliRestrictionOption");
        }
        if (this.ssStatus != null) {
            ((ExtSSStatusImpl) this.ssStatus).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, TAG_SS_STATUS);
        }
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

    public CliRestrictionOption getCliRestrictionOption() {
        return cliRestrictionOption;
    }

    public ExtSSStatus getSsStatus() {
        return ssStatus;
    }

    public ModificationInstruction getModifyNotificationToCSE() {
        return modifyNotificationToCSE;
    }

    public MAPExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }
}