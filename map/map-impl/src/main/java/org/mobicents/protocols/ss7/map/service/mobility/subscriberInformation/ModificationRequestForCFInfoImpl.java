package org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNSubaddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.AbstractMAPAsnPrimitive;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.ModificationInstruction;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.ModificationRequestForCFInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSStatus;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SSCode;
import org.mobicents.protocols.ss7.map.datacoding.IntegerEncoderFacility;
import org.mobicents.protocols.ss7.map.datacoding.ObjectEncoderFacility;
import org.mobicents.protocols.ss7.map.primitives.AddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNSubaddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtBasicServiceCodeImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtSSStatusImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.SSCodeImpl;

/**
 * <code>
 * ModificationRequestFor-CF-Info ::= SEQUENCE {
 *      ss-Code                  [0] SS-Code,
 *      basicService             [1] Ext-BasicServiceCode OPTIONAL,
 *      ss-Status                [2] Ext-SS-Status OPTIONAL,
 *      forwardedToNumber        [3] AddressString OPTIONAL,
 *      forwardedToSubaddress    [4] ISDN-SubaddressString OPTIONAL,
 *      noReplyConditionTime     [5] Ext-NoRepCondTime OPTIONAL,
 *      modifyNotificationToCSE  [6] ModificationInstruction OPTIONAL,
 *      extensionContainer       [7] ExtensionContainer OPTIONAL,
 *      ...
 * }
 * <p/>
 * Ext-NoRepCondTime ::= INTEGER (1..100)
 * -- Only values 5-30 are used.
 * -- Values in the ranges 1-4 and 31-100 are reserved for future use
 * -- If received:
 * -- values 1-4 shall be mapped on to value 5
 * -- values 31-100 shall be mapped on to value 30
 * </code>
 *
 * @author eva ogallar
 */
public class ModificationRequestForCFInfoImpl extends AbstractMAPAsnPrimitive implements ModificationRequestForCFInfo {

    private SSCode ssCode;
    private ExtBasicServiceCode basicService;
    private ExtSSStatus ssStatus;
    private AddressString forwardedToNumber;
    private ISDNSubaddressString forwardedToSubaddress;
    private Integer noReplyConditionTime;
    private ModificationInstruction modifyNotificationToCSE;
    private MAPExtensionContainer extensionContainer;

    private static final int TAG_SS_CODE = 0;
    private static final int TAG_BASIC_SERVICE = 1;
    private static final int TAG_SS_STATUS = 2;
    private static final int TAG_FORWARDED_TO_NUMBER = 3;
    private static final int TAG_FORWARDED_TO_SUBADDRESS = 4;
    private static final int TAG_NO_REPLY_CONDITION_TIME = 5;
    private static final int TAG_MODIFY_NOTIFICATION_TO_CSE = 6;
    private static final int TAG_EXTENSION_CONTAINER = 7;


    public static final String PRIMITIVE_NAME = "ModificationRequestForCFInfo";

    public ModificationRequestForCFInfoImpl() {
    }

    public ModificationRequestForCFInfoImpl(SSCode ssCode, ExtBasicServiceCode basicService, ExtSSStatus ssStatus,
                                            AddressString forwardedToNumber, ISDNSubaddressString forwardedToSubaddress,
                                            Integer noReplyConditionTime, ModificationInstruction modifyNotificationToCSE,
                                            MAPExtensionContainer extensionContainer) {
        this.ssCode = ssCode;
        this.basicService = basicService;
        this.ssStatus = ssStatus;
        this.forwardedToNumber = forwardedToNumber;
        this.forwardedToSubaddress = forwardedToSubaddress;
        this.noReplyConditionTime = noReplyConditionTime;
        this.modifyNotificationToCSE = modifyNotificationToCSE;
        this.extensionContainer = extensionContainer;
    }


    @Override
    protected void _decode(AsnInputStream ansIS, int length) throws IOException, AsnException, MAPParsingComponentException {

        this.ssCode = null;
        this.basicService = null;
        this.ssStatus = null;
        this.forwardedToNumber = null;
        this.forwardedToSubaddress = null;
        this.noReplyConditionTime = null;
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
                    case TAG_FORWARDED_TO_NUMBER:
                        forwardedToNumber = (AddressString) ObjectEncoderFacility.decodePrimitiveObject(ais, new AddressStringImpl(), "forwardedToNumber", getPrimitiveName());
                        break;
                    case TAG_FORWARDED_TO_SUBADDRESS:
                        forwardedToSubaddress = (ISDNSubaddressString) ObjectEncoderFacility.decodePrimitiveObject(ais, new ISDNSubaddressStringImpl(), "forwardedToSubaddress", getPrimitiveName());
                        break;
                    case TAG_NO_REPLY_CONDITION_TIME:
                        noReplyConditionTime = new IntegerEncoderFacility(getPrimitiveName()).decode(ais, "noReplyConditionTime");
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
        if (this.noReplyConditionTime !=null &&
                (this.noReplyConditionTime> 100 || this.noReplyConditionTime< 1 )){
            throw new MAPParsingComponentException(
                    "Error while decoding " + getPrimitiveName() + ": noReplyConditionTime must be between 1 and 100 ",
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
        if (this.forwardedToNumber != null) {
            ((AddressStringImpl) this.forwardedToNumber).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, TAG_FORWARDED_TO_NUMBER);
        }
        if (this.forwardedToSubaddress != null) {
            ((ISDNSubaddressStringImpl) this.forwardedToSubaddress).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, TAG_FORWARDED_TO_SUBADDRESS);
        }
        if (this.noReplyConditionTime != null) {
            IntegerEncoderFacility.encode(asnOs, noReplyConditionTime, Tag.CLASS_CONTEXT_SPECIFIC,
                    TAG_NO_REPLY_CONDITION_TIME, "noReplyConditionTime");
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

    public AddressString getForwardedToNumber() {
        return forwardedToNumber;
    }

    public ISDNSubaddressString getForwardedToSubaddress() {
        return forwardedToSubaddress;
    }

    public Integer getNoReplyConditionTime() {
        return noReplyConditionTime;
    }

    public ModificationInstruction getModifyNotificationToCSE() {
        return modifyNotificationToCSE;
    }

    public MAPExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }
}
