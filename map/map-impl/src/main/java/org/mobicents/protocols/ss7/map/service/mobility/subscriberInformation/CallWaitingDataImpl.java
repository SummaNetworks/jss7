package org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation;

import java.io.IOException;
import java.util.ArrayList;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.AbstractMAPAsnPrimitive;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.CallWaitingData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.ExtCwFeature;
import org.mobicents.protocols.ss7.map.datacoding.NullEncoderFacility;
import org.mobicents.protocols.ss7.map.datacoding.ObjectEncoderFacility;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtCwFeatureImpl;

/**
 *
 <code>
 CallWaitingData ::= SEQUENCE {
     cwFeatureList     [1] Ext-CwFeatureList,
     notificationToCSE [2] NULL OPTIONAL,
 ... }

 Ext-CwFeatureList ::= SEQUENCE SIZE (1..maxNumOfExt-BasicServiceGroups) OF Ext-CwFeature

 maxNumOfExt-BasicServiceGroups INTEGER ::= 32
 </code>
 *
 * @author eva ogallar
 */
public class CallWaitingDataImpl extends AbstractMAPAsnPrimitive implements CallWaitingData, MAPAsnPrimitive {

    public static final String _PrimitiveName = "CallWaitingData";
    private static final int _TAG_CW_FEATURE_LIST = 1;
    private static final int _TAG_NOTIFICATION_TO_CSE = 2;

    private ArrayList<ExtCwFeature> cwFeatureList;
    private boolean notificationToCSE;

    public CallWaitingDataImpl() {
    }

    public CallWaitingDataImpl(ArrayList<ExtCwFeature> cwFeatureList, boolean notificationToCSE) {
        this.cwFeatureList = cwFeatureList;
        this.notificationToCSE = notificationToCSE;
    }

    @Override
    public boolean getNotificationToCSE() {
        return notificationToCSE;
    }

    @Override
    public ArrayList<ExtCwFeature> getCwFeatureList() {
        return cwFeatureList;
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

        cwFeatureList = new ArrayList<ExtCwFeature>();
        notificationToCSE = false;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);
        int num = 0;
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                    case _TAG_CW_FEATURE_LIST:
                        this.cwFeatureList.add((ExtCwFeature) ObjectEncoderFacility.
                                decodeObject(ais, new ExtCwFeatureImpl(), "cwFeatureList", getPrimitiveName()));
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
   }

    public void encodeData(AsnOutputStream asnOs) throws MAPException {

        if (this.cwFeatureList == null || this.cwFeatureList.size() < 1
                || this.cwFeatureList.size() > 32) {
            throw new MAPException("CwFeatureList list must contains from 1 to 32 elements");
        }

        for (ExtCwFeature extCwFeature : this.cwFeatureList) {
            ((ExtCwFeatureImpl) extCwFeature).encodeAll(asnOs);
        }

        NullEncoderFacility.encode(asnOs, this.notificationToCSE, Tag.CLASS_CONTEXT_SPECIFIC,
                _TAG_NOTIFICATION_TO_CSE, "notificationToCSE");



    }

    @Override
    protected String getPrimitiveName() {
        return _PrimitiveName;
    }

}
