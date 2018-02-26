package org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.AbstractMAPAsnPrimitive;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.CallHoldData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSStatus;
import org.mobicents.protocols.ss7.map.datacoding.NullEncoderFacility;
import org.mobicents.protocols.ss7.map.datacoding.ObjectEncoderFacility;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtSSStatusImpl;

/**
 *
 <code>
 CallHoldData ::= SEQUENCE {
     ss-Status          [1] Ext-SS-Status,
     notificationToCSE  [2] NULL OPTIONAL,
     ...
 }
 </code>
 *
 * @author eva ogallar
 */
public class CallHoldDataImpl extends AbstractMAPAsnPrimitive implements CallHoldData, MAPAsnPrimitive {

    public static final String _PrimitiveName = "CallHoldData";
    private static final int _TAG_SS_STATUS = 1;
    private static final int _TAG_NOTIFICATION_TO_CSE = 2;

    private boolean notificationToCSE;
    private ExtSSStatus ssStatus = null;

    public CallHoldDataImpl() {

    }

    public CallHoldDataImpl(boolean notificationToCSE, ExtSSStatus ssStatus) {
        this.notificationToCSE = notificationToCSE;
        this.ssStatus = ssStatus;
    }

    @Override
    public ExtSSStatus getSsStatus() {
        return ssStatus;
    }

    @Override
    public boolean getNotificationToCSE() {
        return notificationToCSE;
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

        ssStatus = null;
        notificationToCSE = false;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);
        int num = 0;
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                    case _TAG_SS_STATUS:
                        this.ssStatus = (ExtSSStatus) ObjectEncoderFacility.
                                decodePrimitiveObject(ais, new ExtSSStatusImpl(), "ssStatus", getPrimitiveName());
                        break;
                    case _TAG_NOTIFICATION_TO_CSE:
                        this.notificationToCSE = NullEncoderFacility.decode(ais, "notificationToCSE", getPrimitiveName());
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

        if (this.ssStatus == null)
            throw new MAPParsingComponentException(
                    "Error while decoding " + _PrimitiveName + ": ssStatus parameter is" +
                            " mandatory but not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);

    }

    public void encodeData(AsnOutputStream asnOs) throws MAPException {

        if (this.ssStatus == null) {
            throw new MAPException("Error while encoding " + _PrimitiveName + ": ssStatus required.");
        }

        ((ExtSSStatusImpl) this.ssStatus).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_SS_STATUS);

        NullEncoderFacility.encode(asnOs, this.notificationToCSE, Tag.CLASS_CONTEXT_SPECIFIC,
                _TAG_NOTIFICATION_TO_CSE, "notificationToCSE");

    }

    @Override
    protected String getPrimitiveName() {
        return _PrimitiveName;
    }

}
