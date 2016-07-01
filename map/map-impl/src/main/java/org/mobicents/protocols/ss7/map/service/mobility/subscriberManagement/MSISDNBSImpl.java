package org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement;

import java.io.IOException;
import java.util.ArrayList;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.AbstractMAPAsnPrimitive;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.MSISDNBS;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCode;
import org.mobicents.protocols.ss7.map.datacoding.ObjectEncoderFacility;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;

/**
 *
 <code>
 MSISDN-BS ::= SEQUENCE {
 msisdn              ISDN-AddressString,
 basicServiceList    [0] BasicServiceList OPTIONAL,
 extensionContainer  [1] ExtensionContainer OPTIONAL,
 ...
 }

 BasicServiceList ::= SEQUENCE SIZE (1..70) OF Ext-BasicServiceCode
 </code>
 *
 * @author eva ogallar
 */
public class MSISDNBSImpl extends AbstractMAPAsnPrimitive implements MSISDNBS {


    public static final String _PrimitiveName = "MSISDNBS";
    public static final int _TAG_basicServiceList = 0;
    public static final int _TAG_extensionContainer = 1;


    private ISDNAddressString msisdn;
    private ArrayList<ExtBasicServiceCode> basicServiceList;
    private MAPExtensionContainer extensionContainer;

    public MSISDNBSImpl() {
    }

    public MSISDNBSImpl(ISDNAddressString msisdn, ArrayList<ExtBasicServiceCode> basicServiceList, MAPExtensionContainer extensionContainer) {
        this.msisdn = msisdn;
        this.basicServiceList = basicServiceList;
        this.extensionContainer = extensionContainer;
    }

    @Override
    protected String getPrimitiveName() {
        return _PrimitiveName;
    }

    @Override
    public boolean getIsPrimitive() {
        return false;
    }

    @Override
    public ISDNAddressString getMsisdn() {
        return msisdn;
    }

    @Override
    public ArrayList<ExtBasicServiceCode> getBasicServiceList() {
        return basicServiceList;
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    @Override
    protected void _decode(AsnInputStream asnIS, int length) throws IOException, AsnException, MAPParsingComponentException {

        this.msisdn = null;
        this.basicServiceList = null;
        this.extensionContainer = null;

        AsnInputStream ais = asnIS.readSequenceStreamData(length);

        int num = 0;
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            switch (num) {
                case 0:
                    if (tag != Tag.STRING_OCTET || ais.getTagClass() != Tag.CLASS_UNIVERSAL || !ais.isTagPrimitive())
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".msisdn: bad tag, tag class or not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.msisdn = (ISDNAddressString) ObjectEncoderFacility.
                            decodePrimitiveObject(ais, new ISDNAddressStringImpl(), "msisdn", _PrimitiveName);
                    break;
                default:
                    switch (ais.getTagClass()) {
                        case Tag.CLASS_CONTEXT_SPECIFIC: {
                            switch (tag) {
                                case _TAG_basicServiceList:
                                    if (ais.isTagPrimitive())
                                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                                + ".basicServiceList: Parameter is primitive",
                                                MAPParsingComponentExceptionReason.MistypedParameter);

                                    AsnInputStream ais2 = ais.readSequenceStream();
                                    this.basicServiceList = new ArrayList<ExtBasicServiceCode>();
                                    while (true) {
                                        if (ais2.available() == 0)
                                            break;

                                        ais2.readTag();

                                        ExtBasicServiceCode extBasicServiceCode = new ExtBasicServiceCodeImpl();
                                        ((ExtBasicServiceCodeImpl) extBasicServiceCode).decodeAll(ais2);
                                        this.basicServiceList.add(extBasicServiceCode);
                                    }
                                    if (this.basicServiceList.size() < 1 || this.basicServiceList.size() > 70) {
                                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                                + ": Parameter basicServiceList size must be from 1 to 70, found: " + this.basicServiceList.size(),
                                                MAPParsingComponentExceptionReason.MistypedParameter);
                                    }
                                    break;
                                case _TAG_extensionContainer:
                                    this.extensionContainer= (MAPExtensionContainer) ObjectEncoderFacility.
                                            decodeObject(ais, new MAPExtensionContainerImpl(), "extensionContainer", _PrimitiveName);
                                    break;
                                default:
                                    ais.advanceElement();
                                    break;
                            }
                        }
                        break;
                        default:
                            ais.advanceElement();
                            break;
                    }
            }
            num++;
        }

        if (this.msisdn == null) {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": Parament msisdn is mandatory but does not found", MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    @Override
    public int getTag() throws MAPException {
        return Tag.SEQUENCE;
    }

    @Override
    public int getTagClass() {
        return Tag.CLASS_CONTEXT_SPECIFIC;
    }

    @Override
    public void encodeData(AsnOutputStream asnOs) throws MAPException {

        if (this.msisdn == null)
            throw new MAPException("Error while encoding" + _PrimitiveName + ": msisdn must not be null");

        ((ISDNAddressStringImpl) this.msisdn).encodeAll(asnOs);

        if (this.basicServiceList != null) {
            try {
                asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_basicServiceList);
                int pos = asnOs.StartContentDefiniteLength();
                for (ExtBasicServiceCode item : this.basicServiceList) {
                    ((ExtBasicServiceCodeImpl) item).encodeAll(asnOs);
                }
                asnOs.FinalizeContent(pos);
            } catch (AsnException e) {
                throw new MAPException("AsnException when encoding " + _PrimitiveName + ".basicServiceList: " + e.getMessage(), e);
            }
        }

        if (this.extensionContainer != null)
            ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                    _TAG_extensionContainer);

    }
}
