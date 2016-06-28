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
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.primitives.GSNAddressImpl;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.service.pdpContextActivation.PdpContextActivationMessageImpl;

import java.io.IOException;

/**
 * @author eva ogallar
 */
public class FailureReportRequestImpl extends PdpContextActivationMessageImpl implements FailureReportRequest {

    protected static final int _TAG_imsi = 0;
    protected static final int _TAG_ggsnNumber = 1;
    protected static final int _TAG_ggsnAddress = 2;
    protected static final int _TAG_extensionContainer = 3;

    public static final String _PrimitiveName = "FailureReportRequest";

    private IMSI imsi;
    private ISDNAddressString ggsnNumber;
    private GSNAddress ggsnAddress;
    private MAPExtensionContainer extensionContainer;

    public FailureReportRequestImpl() {
    }

    /**
     * @param imsi
     * @param ggsnNumber
     * @param ggsnAddress
     * @param extensionContainer
     */
    public FailureReportRequestImpl(IMSI imsi, ISDNAddressString ggsnNumber, GSNAddress ggsnAddress, MAPExtensionContainer extensionContainer) {
        this.imsi = imsi;
        this.ggsnAddress = ggsnAddress;
        this.ggsnNumber = ggsnNumber;
        this.extensionContainer = extensionContainer;
    }

    @Override
    public MAPMessageType getMessageType() {
        return MAPMessageType.failureReport_Request;
    }

    @Override
    public int getOperationCode() {
        return MAPOperationCode.failureReport;
    }

    @Override
    public IMSI getImsi() {
        return imsi;
    }

    @Override
    public GSNAddress getGgsnAddress() {
        return ggsnAddress;
    }

    @Override
    public ISDNAddressString getGgsnNumber() {
        return ggsnNumber;
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
        return extensionContainer;
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

    private void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException, AsnException {

        this.imsi = null;
        this.ggsnAddress = null;
        this.ggsnNumber = null;
        this.extensionContainer = null;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);
        int num = 0;
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                    case _TAG_imsi:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ".imsi: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.imsi = new IMSIImpl();
                        ((IMSIImpl) this.imsi).decodeAll(ais);
                        break;
                    case _TAG_ggsnNumber:
                        if (!ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ".ggsnNumber: Parameter is not primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        this.ggsnNumber = new ISDNAddressStringImpl();
                        ((ISDNAddressStringImpl) this.ggsnNumber).decodeAll(ais);
                        break;
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

        if (this.imsi == null)
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ": Parameter imsi is mandator but not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);
        if (this.ggsnNumber == null)
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ": Parameter ggsnNumber is mandator but not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);
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
        if (this.imsi == null)
            throw new MAPException("IMSI parameter must not be null");
        if (this.ggsnNumber == null)
            throw new MAPException("ggsnNumber parameter must not be null");

        ((IMSIImpl) this.imsi).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_imsi);
        ((ISDNAddressStringImpl) this.ggsnNumber).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_ggsnNumber);
        if (this.ggsnAddress != null)
            ((GSNAddressImpl) this.ggsnAddress).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_ggsnAddress);
        if (this.extensionContainer != null)
            ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_extensionContainer);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.imsi != null) {
            sb.append("imsi=");
            sb.append(imsi);
            sb.append(", ");
        }
        if (this.ggsnNumber != null) {
            sb.append("ggsnNumber=");
            sb.append(ggsnNumber);
            sb.append(", ");
        }
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
