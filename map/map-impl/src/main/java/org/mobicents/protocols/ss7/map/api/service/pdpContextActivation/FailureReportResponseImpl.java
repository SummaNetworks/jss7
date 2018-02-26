package org.mobicents.protocols.ss7.map.api.service.pdpContextActivation;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPMessageType;
import org.mobicents.protocols.ss7.map.api.MAPOperationCode;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.GSNAddress;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.primitives.GSNAddressImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.service.pdpContextActivation.PdpContextActivationMessageImpl;

import java.io.IOException;

/**
 * @author eva ogallar
 */
public class FailureReportResponseImpl extends PdpContextActivationMessageImpl implements FailureReportResponse {

    protected static final int _TAG_ggsnAddress = 0;
    protected static final int _TAG_extensionContainer = 1;

    public static final String _PrimitiveName = "FailureReportResponse";

    private GSNAddress ggsnAddress;
    private MAPExtensionContainer extensionContainer;

    public FailureReportResponseImpl() {
    }

    public FailureReportResponseImpl(GSNAddress ggsnAddress, MAPExtensionContainer extensionContainer) {
        this.ggsnAddress = ggsnAddress;
        this.extensionContainer = extensionContainer;
    }

    public GSNAddress getGgsnAddress() {
        return ggsnAddress;
    }

    public MAPExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    public int getTag() throws MAPException {
        return Tag.SEQUENCE;
    }

    public int getTagClass() {
        return Tag.CLASS_UNIVERSAL;
    }

    public boolean getIsPrimitive() {
        return false;
    }

    public void decodeAll(AsnInputStream ansIS) throws MAPParsingComponentException {
        try {
            int length = ansIS.readLength();
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new MAPParsingComponentException("IOException when decoding "+ _PrimitiveName + ": ", e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new MAPParsingComponentException("AsnException when decoding "+ _PrimitiveName + ": ", e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    private void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException, AsnException {

        this.ggsnAddress = null;
        this.extensionContainer = null;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);
        int num = 0;
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                    case _TAG_ggsnAddress:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ".ggsnAddress: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.ggsnAddress = new GSNAddressImpl();
                        ((GSNAddressImpl) this.ggsnAddress).decodeAll(ais);
                        break;
                    case _TAG_extensionContainer:
                        if (ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".extensionContainer: Parameter extensionContainer is primitive", MAPParsingComponentExceptionReason.MistypedParameter);
                        this.extensionContainer = new MAPExtensionContainerImpl();
                        ((MAPExtensionContainerImpl) this.extensionContainer).decodeAll(ais);
                        break;

                    default:
                        ais.advanceElement();
                        break;
                }
            } else {
                ais.advanceElement();
            }

            num++;
        }

    }


    /*          ggsn-Address [0] GSN-Address OPTIONAL,
    *          extensionContainer [1] ExtensionContainer OPTIONAL,
*/

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

    public void encodeAll(AsnOutputStream asnOs) throws MAPException {
        this.encodeAll(asnOs, this.getTagClass(), this.getTag());
    }

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

    public void encodeData(AsnOutputStream asnOs) throws MAPException {
        if (this.ggsnAddress != null)
            ((GSNAddressImpl) this.ggsnAddress).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_ggsnAddress);
        if (this.extensionContainer != null)
            ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_extensionContainer);

    }

    public MAPMessageType getMessageType() {
        return MAPMessageType.failureReport_Response;
    }

    public int getOperationCode() {
        return MAPOperationCode.failureReport;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.ggsnAddress != null) {
            sb.append("ggsnAddress=");
            sb.append(ggsnAddress);
            sb.append(", ");
        }
        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(extensionContainer);
            sb.append(", ");
        }

        sb.append("]");

        return sb.toString();
    }

}
