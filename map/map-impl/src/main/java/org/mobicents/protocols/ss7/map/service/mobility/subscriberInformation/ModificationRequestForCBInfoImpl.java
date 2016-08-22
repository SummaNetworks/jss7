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
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.ModificationRequestForCBInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSStatus;
import org.mobicents.protocols.ss7.map.api.service.supplementary.Password;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SSCode;
import org.mobicents.protocols.ss7.map.datacoding.IntegerEncoderFacility;
import org.mobicents.protocols.ss7.map.datacoding.ObjectEncoderFacility;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtBasicServiceCodeImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtSSStatusImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.PasswordImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.SSCodeImpl;

/**
 * <code>
     ModificationRequestFor-CB-Info ::= SEQUENCE {
         ss-Code             [0] SS-Code,
         basicService        [1] Ext-BasicServiceCode OPTIONAL,
         ss-Status           [2] Ext-SS-Status OPTIONAL,
         password            [3] Password OPTIONAL,
         wrongPasswordAttemptsCounter   [4] WrongPasswordAttemptsCounter OPTIONAL,
         modifyNotificationToCSE        [5] ModificationInstruction OPTIONAL,
         extensionContainer             [6] ExtensionContainer OPTIONAL,
         ...
     }

     WrongPasswordAttemptsCounter ::= INTEGER (0..4)
 * </code>
 *
 * @author eva ogallar
 */
public class ModificationRequestForCBInfoImpl extends AbstractMAPAsnPrimitive implements ModificationRequestForCBInfo {

    private SSCode ssCode;
    private ExtBasicServiceCode basicService;
    private ExtSSStatus ssStatus;
    private Password password;
    private Integer wrongPasswordAttemptsCounter;
    private ModificationInstruction modifyNotificationToCSE;
    private MAPExtensionContainer extensionContainer;

    private final static int TAG_SS_CODE = 0;
    private final static int TAG_BASIC_SERVICE = 1;
    private final static int TAG_SS_STATUS = 2;
    private final static int TAG_PASSWORD = 3;
    private final static int TAG_WRONG_PASSWORD_ATTEMPTS_COUNTER = 4;
    private final static int TAG_MODIFY_NOTIFICATION_TO_CSE = 5;
    private final static int TAG_EXTENSION_CONTAINER = 6;


    public static final String PRIMITIVE_NAME = "ModificationRequestForCBInfo";

    public ModificationRequestForCBInfoImpl() {
    }

    public ModificationRequestForCBInfoImpl(SSCode ssCode, ExtBasicServiceCode basicService, ExtSSStatus ssStatus,
                                            Password password, Integer wrongPasswordAttemptsCounter,
                                            ModificationInstruction modifyNotificationToCSE, MAPExtensionContainer extensionContainer) {
        this.ssCode = ssCode;
        this.basicService = basicService;
        this.ssStatus = ssStatus;
        this.password = password;
        this.wrongPasswordAttemptsCounter = wrongPasswordAttemptsCounter;
        this.modifyNotificationToCSE = modifyNotificationToCSE;
        this.extensionContainer = extensionContainer;
    }

    @Override
    protected void _decode(AsnInputStream ansIS, int length) throws IOException, AsnException, MAPParsingComponentException {


        this.ssCode = null;
        this.basicService = null;
        this.ssStatus = null;
        this.password = null;
        this.wrongPasswordAttemptsCounter = null;
        this.modifyNotificationToCSE = null;
        this.extensionContainer = null;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);

        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag(); //
            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                    case TAG_SS_CODE:
                        ssCode = (SSCode) ObjectEncoderFacility.decodePrimitiveObject(ais, new SSCodeImpl(), "ssCode", getPrimitiveName());
                        break;
                    case TAG_BASIC_SERVICE:
                        basicService = (ExtBasicServiceCode) ObjectEncoderFacility.decodePrimitiveObject(ais, new ExtBasicServiceCodeImpl(), "basicService", getPrimitiveName());
                        break;
                    case TAG_SS_STATUS:
                        ssStatus = (ExtSSStatus) ObjectEncoderFacility.decodePrimitiveObject(ais, new ExtSSStatusImpl(), "ssStatus", getPrimitiveName());
                        break;
                    case TAG_PASSWORD:
                        password = (Password) ObjectEncoderFacility.decodePrimitiveObject(ais, new PasswordImpl(), "password", getPrimitiveName());
                        break;
                    case TAG_WRONG_PASSWORD_ATTEMPTS_COUNTER:
                        wrongPasswordAttemptsCounter = new IntegerEncoderFacility(getPrimitiveName()).decode(ais, "wrongPasswordAttemptsCounter");
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
        if (this.ssCode == null) {
            throw new MAPParsingComponentException(
                    "Error while decoding " + getPrimitiveName() + ": ssCode is mandatory but not found",
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
        if (this.ssCode == null) {
            throw new MAPException("Error while encoding " + getPrimitiveName()
                    + " the mandatory parameter ssCode is not defined");
        }

        ((SSCodeImpl) this.ssCode).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, TAG_SS_CODE);

        if (this.basicService != null) {
            ((ExtBasicServiceCodeImpl) this.basicService).encodeAll(asnOs);
        }
        if (this.ssStatus != null) {
            ((ExtSSStatusImpl) this.ssStatus).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, TAG_SS_STATUS);
        }
        if (this.password != null) {
            ((PasswordImpl) this.password).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, TAG_PASSWORD);
        }
        if (this.wrongPasswordAttemptsCounter != null) {
            IntegerEncoderFacility.encode(asnOs, wrongPasswordAttemptsCounter, Tag.CLASS_CONTEXT_SPECIFIC,
                    TAG_WRONG_PASSWORD_ATTEMPTS_COUNTER, "wrongPasswordAttemptsCounter");
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

    public SSCode getSsCode() {
        return ssCode;
    }

    public ExtBasicServiceCode getBasicService() {
        return basicService;
    }

    public ExtSSStatus getSsStatus() {
        return ssStatus;
    }

    public Password getPassword() {
        return password;
    }

    public Integer getWrongPasswordAttemptsCounter() {
        return wrongPasswordAttemptsCounter;
    }

    public ModificationInstruction getModifyNotificationToCSE() {
        return modifyNotificationToCSE;
    }

    public MAPExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }
}
