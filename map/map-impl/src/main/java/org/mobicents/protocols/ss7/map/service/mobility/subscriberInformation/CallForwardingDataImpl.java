package org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation;

import java.io.IOException;
import java.util.ArrayList;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.AbstractMAPAsnPrimitive;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.CallForwardingData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtForwFeature;
import org.mobicents.protocols.ss7.map.datacoding.NullEncoderFacility;
import org.mobicents.protocols.ss7.map.datacoding.ObjectEncoderFacility;
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

    protected void _decode(AsnInputStream ansIS, int length) throws AsnException, IOException, MAPParsingComponentException {

        forwardingFeatureList = new ArrayList<ExtForwFeature>();
        notificationToCSE = false;
        extensionContainer = null;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);
        int num = 0;
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                switch (tag) {
                    case Tag.NULL:
                        this.notificationToCSE = NullEncoderFacility.decode(ais, "notificationToCSE", getPrimitiveName());
                        break;
                    case Tag.SEQUENCE:
                        this.forwardingFeatureList.add((ExtForwFeature) ObjectEncoderFacility.
                                decodeObject(ais, new ExtForwFeatureImpl(), "forwardingFeatureList", getPrimitiveName()));
                        break;
                    default:
                        ais.advanceElement();
                        break;
                }
            } else if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                    case _TAG_EXTENSION_CONTAINER:
                        extensionContainer = (MAPExtensionContainer) ObjectEncoderFacility.
                                decodeObject(ais, new MAPExtensionContainerImpl(), "extensionContainer", getPrimitiveName());
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

    public void encodeData(AsnOutputStream asnOs) throws MAPException {

        if (this.forwardingFeatureList == null || this.forwardingFeatureList.size() < 1
                || this.forwardingFeatureList.size() > 32) {
            throw new MAPException("ForwardingFeatureList list must contains from 1 to 32 elemets");
        }

        for (ExtForwFeature extForwFeature : this.forwardingFeatureList) {
            ((ExtForwFeatureImpl) extForwFeature).encodeAll(asnOs);
        }

        NullEncoderFacility.encode(asnOs, this.notificationToCSE, "notificationToCSE");

        if (this.extensionContainer != null) {
            ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs,
                    Tag.CLASS_CONTEXT_SPECIFIC, _TAG_EXTENSION_CONTAINER);
        }

    }

    @Override
    protected String getPrimitiveName() {
        return _PrimitiveName;
    }

}
