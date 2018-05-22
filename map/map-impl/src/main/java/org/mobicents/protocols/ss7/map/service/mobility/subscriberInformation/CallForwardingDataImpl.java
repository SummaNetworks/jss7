package org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation;

import java.io.IOException;
import java.util.ArrayList;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.AbstractMAPAsnPrimitive;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.CallForwardingData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtForwFeature;
import org.mobicents.protocols.ss7.map.datacoding.NullEncoderFacility;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtForwFeatureImpl;

/**
 *
 <code>
 CallForwardingData ::= SEQUENCE {
     forwardingFeatureList  Ext-ForwFeatureList,
     notificationToCSE      NULL OPTIONAL,
     extensionContainer     [0] ExtensionContainer OPTIONAL,
     ...
 }

 Ext-ForwFeatureList ::= SEQUENCE SIZE (1..32) OF Ext-ForwFeature
 </code>
 *
 * @author eva ogallar
 */
public class CallForwardingDataImpl extends AbstractMAPAsnPrimitive implements CallForwardingData, MAPAsnPrimitive {

    public static final String _PrimitiveName = "CallForwardingData";
    private static final int _TAG_EXTENSION_CONTAINER = 0;

    private ArrayList<ExtForwFeature> forwardingFeatureList;
    private boolean notificationToCSE;
    private MAPExtensionContainer extensionContainer;

    public CallForwardingDataImpl() {
    }

    public CallForwardingDataImpl(ArrayList<ExtForwFeature> forwardingFeatureList, boolean notificationToCSE, MAPExtensionContainer extensionContainer) {
        this.forwardingFeatureList = forwardingFeatureList;
        this.notificationToCSE = notificationToCSE;
        this.extensionContainer = extensionContainer;
    }

    @Override
    public ArrayList<ExtForwFeature> getForwardingFeatureList() {
        return forwardingFeatureList;
    }

    @Override
    public boolean getNotificationToCSE() {
        return notificationToCSE;
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
        return Tag.CLASS_CONTEXT_SPECIFIC;
    }

    @Override
    public boolean getIsPrimitive() {
        return false;
    }

    protected void _decode(AsnInputStream asnIS, int length) throws MAPParsingComponentException, IOException, AsnException {
        this.forwardingFeatureList = null;
        this.notificationToCSE = false;
        this.extensionContainer = null;

        AsnInputStream ais = asnIS.readSequenceStreamData(length);
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();
            switch (ais.getTagClass()) {
                case Tag.CLASS_UNIVERSAL:
                    switch (tag) {
                        case Tag.SEQUENCE:
                            if (ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".forwardingFeatureList: Parameter forwardingFeatureList is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            ExtForwFeature extForwFeature;
                            AsnInputStream ais2 = ais.readSequenceStream();
                            this.forwardingFeatureList = new ArrayList<ExtForwFeature>();
                            while (true) {
                                if (ais2.available() == 0)
                                    break;

                                if (ais2.readTag() != Tag.SEQUENCE || ais2.getTagClass() != Tag.CLASS_UNIVERSAL || ais2.isTagPrimitive())
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ".extForwFeature: Parameter extForwFeature is primitive",
                                            MAPParsingComponentExceptionReason.MistypedParameter);

                                extForwFeature = new ExtForwFeatureImpl();
                                ((ExtForwFeatureImpl)extForwFeature).decodeAll(ais2);
                                this.forwardingFeatureList.add(extForwFeature);
                            }

                            if (this.forwardingFeatureList.size() < 1 || this.forwardingFeatureList.size() > 32) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ": Parameter forwardingFeatureList size must be from 1 to 32, found: "
                                        + this.forwardingFeatureList.size(), MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            break;
                        case Tag.NULL:
                            if (!ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".isNotificationToCSE: Parameter is not primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);

                            ais.readNull();
                            this.notificationToCSE = Boolean.TRUE;
                            break;
                    }
                    break;
                case Tag.CLASS_CONTEXT_SPECIFIC:
                    switch (tag) {
                        case _TAG_EXTENSION_CONTAINER:
                            if (ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".extensionContainer: Parameter is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
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

        if (this.forwardingFeatureList == null) {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + "forwardingFeatureList is mandatory but it is absent",
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    public void encodeData(AsnOutputStream asnOs) throws MAPException {

        if (this.forwardingFeatureList == null || this.forwardingFeatureList.size() < 1
                || this.forwardingFeatureList.size() > 32) {
            throw new MAPException("ForwardingFeatureList list must contains from 1 to 32 elemets");
        }

        try {
            asnOs.writeTag(Tag.CLASS_UNIVERSAL, false, Tag.SEQUENCE);
            int pos = asnOs.StartContentDefiniteLength();
            for (ExtForwFeature extForwFeature : this.forwardingFeatureList) {
                ((ExtForwFeatureImpl) extForwFeature).encodeAll(asnOs);
            }
            asnOs.FinalizeContent(pos);

            NullEncoderFacility.encode(asnOs, this.notificationToCSE, "notificationToCSE");

            if (this.extensionContainer != null) {
                ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs,
                        Tag.CLASS_CONTEXT_SPECIFIC, _TAG_EXTENSION_CONTAINER);
            }
        } catch (AsnException ae) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + ae.getMessage(), ae);
        }

    }

    @Override
    protected String getPrimitiveName() {
        return _PrimitiveName;
    }

}
