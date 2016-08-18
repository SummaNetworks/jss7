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
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.ClipData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSStatus;
import org.mobicents.protocols.ss7.map.api.service.supplementary.OverrideCategory;
import org.mobicents.protocols.ss7.map.datacoding.IntegerEncoderFacility;
import org.mobicents.protocols.ss7.map.datacoding.NullEncoderFacility;
import org.mobicents.protocols.ss7.map.datacoding.ObjectEncoderFacility;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtSSStatusImpl;

/**
 *
 <code>
 ClipData ::= SEQUENCE {
     ss-Status          [1] Ext-SS-Status,
     overrideCategory   [2] OverrideCategory,
     notificationToCSE  [3] NULL OPTIONAL,
     ...
 }
 </code>
 *
 * @author eva ogallar
 */
public class ClipDataImpl extends AbstractMAPAsnPrimitive implements ClipData, MAPAsnPrimitive {

    public static final String _PrimitiveName = "ClipData";
    private static final int _TAG_SS_STATUS = 1;
    private static final int _TAG_OVERRIDE_CATEGORY = 2;
    private static final int _TAG_NOTIFICATION_TO_CSE = 3;

    private ExtSSStatus ssStatus = null;
    private OverrideCategory overrideCategory = null;
    private boolean notificationToCSE;

    public ClipDataImpl() {

    }

    public ClipDataImpl(ExtSSStatus ssStatus, OverrideCategory overrideCategory, boolean notificationToCSE) {
        this.ssStatus = ssStatus;
        this.overrideCategory = overrideCategory;
        this.notificationToCSE = notificationToCSE;
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
    public OverrideCategory getOverrideCategory() {
        return overrideCategory;
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
        overrideCategory = null;
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
                    case _TAG_OVERRIDE_CATEGORY:
                        int i1 = (int) ais.readInteger();
                        this.overrideCategory = OverrideCategory.getInstance(i1);
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

        if (this.ssStatus == null || this.overrideCategory == null )
            throw new MAPParsingComponentException(
                    "Error while decoding " + _PrimitiveName + ": ssStatus, overrideCategory " +
                            "and gsmSCFAddress parameters are mandatory but some of them are not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);

    }

    public void encodeData(AsnOutputStream asnOs) throws MAPException {

        if (this.ssStatus == null) {
            throw new MAPException("Error while encoding " + _PrimitiveName + ": ssStatus required.");
        }

        if (this.overrideCategory == null) {
            throw new MAPException("Error while encoding " + _PrimitiveName + ": overrideCategory required.");
        }

        ((ExtSSStatusImpl) this.ssStatus).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_SS_STATUS);

        IntegerEncoderFacility.encode(asnOs, overrideCategory.getCode(), Tag.CLASS_CONTEXT_SPECIFIC,
                _TAG_OVERRIDE_CATEGORY, "overrideCategory");
        /*try {
            asnOs.writeInteger(this.overrideCategory.getCode(), Tag.CLASS_CONTEXT_SPECIFIC, _TAG_OVERRIDE_CATEGORY);
        } catch (IOException e) {
            throw new MAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new MAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
*/
        NullEncoderFacility.encode(asnOs, this.notificationToCSE, Tag.CLASS_CONTEXT_SPECIFIC,
                _TAG_NOTIFICATION_TO_CSE, "notificationToCSE");

    }

    @Override
    protected String getPrimitiveName() {
        return _PrimitiveName;
    }

}
