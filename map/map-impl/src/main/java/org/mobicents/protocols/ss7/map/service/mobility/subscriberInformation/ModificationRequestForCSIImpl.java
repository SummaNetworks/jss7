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
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.AdditionalRequestedCAMELSubscriptionInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.ModificationInstruction;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.ModificationRequestForCSI;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedCAMELSubscriptionInfo;
import org.mobicents.protocols.ss7.map.datacoding.IntegerEncoderFacility;
import org.mobicents.protocols.ss7.map.datacoding.ObjectEncoderFacility;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;

/**
 * <code>
 * ModificationRequestFor-CSI ::= SEQUENCE {
 *      requestedCamel-SubscriptionInfo    [0] RequestedCAMEL-SubscriptionInfo,
 *      modifyNotificationToCSE            [1] ModificationInstruction OPTIONAL,
 *      modifyCSI-State                    [2] ModificationInstruction OPTIONAL,
 *      extensionContainer                 [3] ExtensionContainer OPTIONAL,
 *      additionalRequestedCAMEL-SubscriptionInfo  [4] AdditionalRequestedCAMEL-SubscriptionInfo OPTIONAL
 * }
 * -- requestedCamel-SubscriptionInfo shall be discarded if
 * -- additionalRequestedCAMEL-SubscriptionInfo is received
 * <p/>
 * </code>
 *
 * @author eva ogallar
 */
public class ModificationRequestForCSIImpl extends AbstractMAPAsnPrimitive implements ModificationRequestForCSI {

    private RequestedCAMELSubscriptionInfo requestedCAMELSubscriptionInfo;
    private ModificationInstruction modifyNotificationToCSE;
    private ModificationInstruction modifyCSIState;
    private MAPExtensionContainer extensionContainer;
    private AdditionalRequestedCAMELSubscriptionInfo additionalRequestedCAMELSubscriptionInfo;

    private static final int TAG_REQUESTED_CAMEL_SUBSCRIPTION_INFO = 0;
    private static final int TAG_MODIFY_NOTIFICATION_TO_CSE = 1;
    private static final int TAG_MODIFY_CSI_STATE = 2;
    private static final int TAG_EXTENSION_CONTAINER = 3;
    private static final int TAG_ADDITIONAL_REQUESTED_CAMEL_SUBSCRIPTION_INFO = 4;


    public static final String PRIMITIVE_NAME = "ModificationRequestForCSI";

    public ModificationRequestForCSIImpl() {
    }

    public ModificationRequestForCSIImpl(RequestedCAMELSubscriptionInfo requestedCAMELSubscriptionInfo,
                                         ModificationInstruction modifyNotificationToCSE, ModificationInstruction modifyCSIState,
                                         MAPExtensionContainer extensionContainer,
                                         AdditionalRequestedCAMELSubscriptionInfo additionalRequestedCAMELSubscriptionInfo) {
        this.requestedCAMELSubscriptionInfo = requestedCAMELSubscriptionInfo;
        this.modifyNotificationToCSE = modifyNotificationToCSE;
        this.modifyCSIState = modifyCSIState;
        this.extensionContainer = extensionContainer;
        this.additionalRequestedCAMELSubscriptionInfo = additionalRequestedCAMELSubscriptionInfo;
    }

    @Override
    protected void _decode(AsnInputStream ansIS, int length) throws IOException, AsnException, MAPParsingComponentException {

        this.requestedCAMELSubscriptionInfo = null;
        this.modifyCSIState = null;
        this.modifyNotificationToCSE = null;
        this.additionalRequestedCAMELSubscriptionInfo = null;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);

        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag(); //
            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                    case TAG_REQUESTED_CAMEL_SUBSCRIPTION_INFO:
                        requestedCAMELSubscriptionInfo = RequestedCAMELSubscriptionInfo.getInstance(
                                IntegerEncoderFacility.decode(ais, "requestedCAMELSubscriptionInfo", getPrimitiveName()));
                        break;
                    case TAG_ADDITIONAL_REQUESTED_CAMEL_SUBSCRIPTION_INFO:
                        additionalRequestedCAMELSubscriptionInfo = AdditionalRequestedCAMELSubscriptionInfo.getInstance(
                                IntegerEncoderFacility.decode(ais, "additionalRequestedCAMELSubscriptionInfo", getPrimitiveName()));
                        break;
                    case TAG_MODIFY_CSI_STATE:
                        modifyCSIState = ModificationInstruction.getInstance(IntegerEncoderFacility.decode(ais, "modifyCSIState", getPrimitiveName()));
                        break;
                    case TAG_MODIFY_NOTIFICATION_TO_CSE:
                        modifyNotificationToCSE = ModificationInstruction.getInstance(IntegerEncoderFacility.decode(ais, "modifyNotificationToCSE", getPrimitiveName()));
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
        if (this.requestedCAMELSubscriptionInfo == null) {
            throw new MAPParsingComponentException(
                    "Error while decoding " + getPrimitiveName() + ": requestedCAMELSubscriptionInfo is mandatory but not found",
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
        if (this.requestedCAMELSubscriptionInfo == null) {
            throw new MAPException("Error while encoding " + getPrimitiveName()
                    + " the mandatory parameter requestedCAMELSubscriptionInfo is not defined");
        }

        IntegerEncoderFacility.encode(asnOs, requestedCAMELSubscriptionInfo.getCode(), Tag.CLASS_CONTEXT_SPECIFIC,
                TAG_REQUESTED_CAMEL_SUBSCRIPTION_INFO, "requestedCAMELSubscriptionInfo");

        if (this.additionalRequestedCAMELSubscriptionInfo != null) {
            IntegerEncoderFacility.encode(asnOs, additionalRequestedCAMELSubscriptionInfo.getCode(), Tag.CLASS_CONTEXT_SPECIFIC,
                    TAG_ADDITIONAL_REQUESTED_CAMEL_SUBSCRIPTION_INFO, "additionalRequestedCAMELSubscriptionInfo");
        }
        if (this.modifyCSIState != null) {
            IntegerEncoderFacility.encode(asnOs, modifyCSIState.getCode(), Tag.CLASS_CONTEXT_SPECIFIC,
                    TAG_MODIFY_CSI_STATE, "modifyCSIState");
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


    public RequestedCAMELSubscriptionInfo getRequestedCamelSubscriptionInfo() {
        return requestedCAMELSubscriptionInfo;
    }

    public ModificationInstruction getModifyCSIState() {
        return modifyCSIState;
    }

    public AdditionalRequestedCAMELSubscriptionInfo getAdditionalRequestedCamelSubscriptionInfo() {
        return additionalRequestedCAMELSubscriptionInfo;
    }

    public ModificationInstruction getModifyNotificationToCSE() {
        return modifyNotificationToCSE;
    }

    public MAPExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }
}
