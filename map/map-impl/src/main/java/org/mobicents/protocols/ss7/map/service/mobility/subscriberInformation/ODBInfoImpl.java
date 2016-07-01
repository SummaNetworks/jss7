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
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.ODBInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ODBData;
import org.mobicents.protocols.ss7.map.datacoding.NullEncoderFacility;
import org.mobicents.protocols.ss7.map.datacoding.ObjectEncoderFacility;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ODBDataImpl;

/**
 *
 <code>
 ODB-Info ::= SEQUENCE {
 odb-Data            ODB-Data,
 notificationToCSE   NULL OPTIONAL,
 extensionContainer  ExtensionContainer OPTIONAL,
 ...
 }
 </code>
 *
 * @author eva ogallar
 */
public class ODBInfoImpl extends AbstractMAPAsnPrimitive implements ODBInfo, MAPAsnPrimitive {

    public static final String _PrimitiveName = "ODBInfo";
    private static final int _TAG_EXTENSION_CONTAINER = 0;

    private ODBData odbData;
    private boolean notificationToCSE;
    private MAPExtensionContainer extensionContainer;

    public ODBInfoImpl() {
    }

    public ODBInfoImpl(ODBData odbData, boolean notificationToCSE, MAPExtensionContainer extensionContainer) {
        this.odbData = odbData;
        this.notificationToCSE = notificationToCSE;
        this.extensionContainer = extensionContainer;
    }

    @Override
    public ODBData getOdbData() {
        return odbData;
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

        odbData = null;
        notificationToCSE = false;
        extensionContainer = null;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);
        int num = 0;
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();
            switch (num) {
                case 0:
                    if (tag != Tag.SEQUENCE || ais.getTagClass() != Tag.CLASS_UNIVERSAL || ais.isTagPrimitive())
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ": Parameter 0 bad tag, tag class or primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);

                    this.odbData = new ObjectEncoderFacility<ODBDataImpl>(_PrimitiveName).
                            decodeObject(ais, new ODBDataImpl(), "odbData", getPrimitiveName());
                    break;
                default:
                    if (ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                        switch (tag) {
                            case Tag.NULL:
                                this.notificationToCSE = new NullEncoderFacility(_PrimitiveName).decode(ais, "notificationToCSE", getPrimitiveName());
                                break;
                            case Tag.SEQUENCE:
                                extensionContainer = new ObjectEncoderFacility<MAPExtensionContainerImpl>(_PrimitiveName).
                                        decodeObject(ais, new MAPExtensionContainerImpl(), "extensionContainer", getPrimitiveName());
                                break;
                            default:
                                ais.advanceElement();
                                break;
                        }
                    } else {
                        ais.advanceElement();
                    }
                    break;

            }
            num++;
        }
        if (this.odbData == null) {
            throw new MAPParsingComponentException(
                    "Error while decoding " + _PrimitiveName + ": odbData parameter is mandatory but empty",
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
   }

    public void encodeData(AsnOutputStream asnOs) throws MAPException {

        if (this.odbData == null) {
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + " the mandatory parameter odbData is not defined");
        }

        ((ODBDataImpl) odbData).encodeAll(asnOs);

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
