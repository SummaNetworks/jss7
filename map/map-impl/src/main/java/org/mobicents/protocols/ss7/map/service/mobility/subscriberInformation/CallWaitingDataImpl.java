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
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.CallWaitingData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.ExtCwFeature;
import org.mobicents.protocols.ss7.map.datacoding.NullEncoderFacility;
import org.mobicents.protocols.ss7.map.datacoding.ObjectEncoderFacility;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;
import org.mobicents.protocols.ss7.map.primitives.SequenceBase;
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
public class CallWaitingDataImpl extends SequenceBase implements CallWaitingData, MAPAsnPrimitive {

    public static final String _PrimitiveName = "CallWaitingData";
    private static final int _TAG_CW_FEATURE_LIST = 1;
    private static final int _TAG_NOTIFICATION_TO_CSE = 2;

    private ArrayList<ExtCwFeature> cwFeatureList;
    private boolean notificationToCSE;

    public CallWaitingDataImpl() {
        super("CallWaitingData");
    }

    public CallWaitingDataImpl(ArrayList<ExtCwFeature> cwFeatureList, boolean notificationToCSE) {
        super("CallWaitingData");
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

    private String getPrimitiveName() {
        return _PrimitiveName;
    }

    @Override
    protected void _decode(AsnInputStream asnIS, int length) throws MAPParsingComponentException, IOException, AsnException {
        this.cwFeatureList = null;
        this.notificationToCSE = false;

        AsnInputStream ais = asnIS.readSequenceStreamData(length);
        while (true) {
            if (ais.available() == 0) {
                break;
            }

            int tag = ais.readTag();
            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                    case _TAG_CW_FEATURE_LIST:
                        if (ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": Parameter cwFeatureList is primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);

                        this.cwFeatureList = new ArrayList<>();
                        AsnInputStream ais2 = ais.readSequenceStream();
                        while (true) {
                            if (ais2.available() == 0) {
                                break;
                            }

                            if (ais2.readTag() != Tag.SEQUENCE || ais2.getTagClass() != Tag.CLASS_UNIVERSAL || ais2.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".extCwFeature: Parameter extCwFeature is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);

                            this.cwFeatureList.add((ExtCwFeature) ObjectEncoderFacility.
                                    decodeObject(ais2, new ExtCwFeatureImpl(), "cwFeatureList", getPrimitiveName()));

                        }

                        if (this.cwFeatureList.size() < 1 || this.cwFeatureList.size() > 32) {
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": Parameter cwFeatureList size must be from 1 to 32, found: "
                                    + this.cwFeatureList.size(), MAPParsingComponentExceptionReason.MistypedParameter);
                        }
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
        }

        if (this.cwFeatureList == null) {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + "cwFeatureList is mandatory but it is absent",
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }


    public void encodeData(AsnOutputStream asnOs) throws MAPException {

        if (this.cwFeatureList == null || this.cwFeatureList.size() < 1
                || this.cwFeatureList.size() > 32) {
            throw new MAPException("CwFeatureList list must contains from 1 to 32 elements");
        }

        try {
            asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_CW_FEATURE_LIST);
            int pos = asnOs.StartContentDefiniteLength();
            for (ExtCwFeature extCwFeature : this.cwFeatureList) {
                ((ExtCwFeatureImpl) extCwFeature).encodeAll(asnOs);
            }
            asnOs.FinalizeContent(pos);

            NullEncoderFacility.encode(asnOs, this.notificationToCSE, Tag.CLASS_CONTEXT_SPECIFIC,
                    _TAG_NOTIFICATION_TO_CSE, "notificationToCSE");
        } catch (AsnException ae) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + ae.getMessage(), ae);
        }

    }


    public void encodeData2(AsnOutputStream asnOs) throws MAPException {
        if (this.cwFeatureList == null) {
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + " the mandatory parameter cwFeatureList is not defined");
        }

        if (this.cwFeatureList.size() < 1 || this.cwFeatureList.size() > 32) {
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + " size cwFeatureList is out of range (1..32). Actual size: "
                    + this.cwFeatureList.size());
        }

        try {
            asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_CW_FEATURE_LIST);
            int pos = asnOs.StartContentDefiniteLength();
            for (ExtCwFeature extCwFeature: this.cwFeatureList) {
                ((ExtCwFeatureImpl)extCwFeature).encodeAll(asnOs);
            }
            asnOs.FinalizeContent(pos);

            if (this.notificationToCSE) {
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_NOTIFICATION_TO_CSE);
            }
        } catch (IOException e) {
            throw new MAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (AsnException ae) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + ae.getMessage(), ae);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.cwFeatureList != null) {
            sb.append("cwFeatureList=[");
            boolean firstItem = true;
            for (ExtCwFeature extCwFeature : cwFeatureList) {
                if (firstItem) {
                    firstItem = false;
                } else {
                    sb.append(", ");
                }
                sb.append(extCwFeature);
            }
            sb.append("], ");
        }
        if (this.notificationToCSE) {
            sb.append("isNotificationToCSE, ");
        }

        sb.append("]");
        return sb.toString();
    }

}
