package org.mobicents.protocols.ss7.map.service.callhandling;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPMessageType;
import org.mobicents.protocols.ss7.map.api.MAPOperationCode;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.callhandling.CallTerminationIndicator;
import org.mobicents.protocols.ss7.map.api.service.callhandling.IstAlertResponse;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;


/**
 *
 * <code>
 *  IST-AlertRes ::= SEQUENCE{
 *        istAlertTimer                 [0]  IST-AlertTimerValue      OPTIONAL,
 *        istInformationWithdraw        [1]  NULL      OPTIONAL,
 *        callTerminationIndicator      [2]  CallTerminationIndicator      OPTIONAL,
 *        extensionContainer            [3]  ExtensionContainer OPTIONAL,
 *        ...}
 *
 * IST-AlertTimerValue ::= INTEGER (15..255)
 *</code>
 *
 * @author eva ogallar
 *
 */
public class IstAlertResponseImpl extends CallHandlingMessageImpl implements IstAlertResponse {

    public static final String _PrimitiveName = "IstAlertResponse";

    private static final int TAG_istAlertTimer = 0;
    private static final int TAG_istInformationWithdraw = 1;
    private static final int TAG_callTerminationIndicator = 2;
    private static final int TAG_extensionContainer = 3;

    private Integer istAlertTimer;
    private boolean istInformationWithdraw;
    private CallTerminationIndicator callTerminationIndicator;
    private MAPExtensionContainer extensionContainer;

    public IstAlertResponseImpl() {
    }

    public IstAlertResponseImpl(Integer istAlertTimer, boolean istInformationWithdraw,
                                CallTerminationIndicator callTerminationIndicator,
                                MAPExtensionContainer extensionContainer) {
        this.istAlertTimer = istAlertTimer;
        this.istInformationWithdraw = istInformationWithdraw;
        this.callTerminationIndicator = callTerminationIndicator;
        this.extensionContainer = extensionContainer;
    }

    @Override
    public Integer getIstAlertTimer() {
        return istAlertTimer;
    }

    public boolean getIstInformationWithdraw() {
        return istInformationWithdraw;
    }

    @Override
    public CallTerminationIndicator getCallTerminationIndicator() {
        return callTerminationIndicator;
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    @Override
    public MAPMessageType getMessageType() {
        return MAPMessageType.istAlert_Response;
    }

    @Override
    public int getOperationCode() {
        return MAPOperationCode.istAlert;
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

    @Override
    public void decodeAll(AsnInputStream ansIS) throws MAPParsingComponentException {
        try {
            int length = ansIS.readLength();
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new MAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new MAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    @Override
    public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {
        try {
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new MAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new MAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }

    }

    private void _decode(AsnInputStream ansIS, int length) throws AsnException, IOException, MAPParsingComponentException {

        this.extensionContainer = null;
        this.callTerminationIndicator = null;
        this.istAlertTimer = null;
        this.istInformationWithdraw = false;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);
        while (true) {
            if (ais.available() == 0) {
                break;
            }

            int tag = ais.readTag();
            switch (ais.getTagClass()) {
                case Tag.CLASS_CONTEXT_SPECIFIC:
                    switch (tag) {
                        case TAG_istAlertTimer:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".istAlertTimer: Parameter is not primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                            this.istAlertTimer = (int) ais.readInteger();
                            break;
                        case TAG_istInformationWithdraw:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName +
                                        ".istInformationWithdraw: Parameter is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            ais.readNull();
                            this.istInformationWithdraw = true;
                            break;
                        case TAG_callTerminationIndicator:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ".callTerminationIndicator: is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            int i1 = (int) ais.readInteger();
                            this.callTerminationIndicator = CallTerminationIndicator.getInstance(i1);
                            break;
                        case TAG_extensionContainer:
                            if (ais.isTagPrimitive()) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".extensionContainer: is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            this.extensionContainer = new MAPExtensionContainerImpl();
                            ((MAPExtensionContainerImpl) this.extensionContainer).decodeAll(ais);
                            break;
                        default:
                            ais.advanceElement();
                            break;
                    }
                    break;

                default:
                    ais.advanceElement();
                    break;
            }

        }
    }

    @Override
    public void encodeAll(AsnOutputStream asnOs) throws MAPException {
        this.encodeAll(asnOs, this.getTagClass(), this.getTag());
    }

    @Override
    public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {
        try {
            asnOs.writeTag(tagClass, this.getIsPrimitive(), tag);
            int pos = asnOs.StartContentDefiniteLength();
            this.encodeData(asnOs);
            asnOs.FinalizeContent(pos);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }

    }

    @Override
    public void encodeData(AsnOutputStream asnOs) throws MAPException {
        try {
            if (this.istAlertTimer!=null){
                asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, TAG_istAlertTimer, istAlertTimer);
            }
            if (this.istInformationWithdraw){
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, TAG_istInformationWithdraw);
            }
            if (this.callTerminationIndicator != null) {
                try {
                    asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, TAG_callTerminationIndicator, this.callTerminationIndicator.getCode());
                } catch (IOException e) {
                    throw new MAPException("IOException while encoding " + _PrimitiveName + " parameter callTerminationIndicator", e);
                } catch (AsnException e) {
                    throw new MAPException("IOException while encoding " + _PrimitiveName + " parameter callTerminationIndicator", e);
                }
            }
            if (this.extensionContainer != null)
                ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        TAG_extensionContainer);
        } catch (IOException e) {
            throw new MAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.istAlertTimer != null) {
            sb.append("istAlertTimer=");
            sb.append(istAlertTimer);
            sb.append(", ");
        }

        if (this.istInformationWithdraw) {
            sb.append("istInformationWithdraw, ");
        }

        if (this.callTerminationIndicator != null) {
            sb.append("callTerminationIndicator=");
            sb.append(this.callTerminationIndicator);
            sb.append(", ");
        }

        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.extensionContainer.toString());
        }

        sb.append("]");

        return sb.toString();
    }

}
