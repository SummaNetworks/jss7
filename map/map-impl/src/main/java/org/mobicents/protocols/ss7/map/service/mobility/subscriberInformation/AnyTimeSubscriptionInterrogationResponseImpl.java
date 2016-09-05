package org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation;

import java.io.IOException;
import java.util.ArrayList;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPMessageType;
import org.mobicents.protocols.ss7.map.api.MAPOperationCode;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeSubscriptionInterrogationResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.CAMELSubscriptionInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.CallBarringData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.CallForwardingData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.CallHoldData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.CallWaitingData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.ClipData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.ClirData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.EctData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.MSISDNBS;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.ODBInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.CSGSubscriptionData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.OfferedCamel4CSIs;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SupportedCamelPhases;
import org.mobicents.protocols.ss7.map.datacoding.ObjectEncoderFacility;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.service.mobility.MobilityMessageImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.CSGSubscriptionDataImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.MSISDNBSImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.OfferedCamel4CSIsImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.SupportedCamelPhasesImpl;

/**
 * @author eva ogallar
 */
public class AnyTimeSubscriptionInterrogationResponseImpl extends MobilityMessageImpl implements AnyTimeSubscriptionInterrogationResponse,
        MAPAsnPrimitive {

    public static final String _PrimitiveName = "AnyTimeSubscriptionInterrogationResponse";

    private static final int _TAG_callForwardingData = 1;
    private static final int _TAG_callBarringData = 2;
    private static final int _TAG_odbInfo = 3;
    private static final int _TAG_camelSubscriptionInfo = 4;
    private static final int _TAG_supportedVlrCamelPhases = 5;
    private static final int _TAG_supportedSgsnCamelPhases = 6;
    private static final int _TAG_extensionContainer = 7;
    private static final int _TAG_offeredCamel4CSIsInVlr = 8;
    private static final int _TAG_offeredCamel4CSIsInSgsn = 9;
    private static final int _TAG_msisdnBsList = 10;
    private static final int _TAG_csgSubscriptionDataList = 11;
    private static final int _TAG_cwData = 12;
    private static final int _TAG_chData = 13;
    private static final int _TAG_clipData = 14;
    private static final int _TAG_clirData = 15;
    private static final int _TAG_ectData = 16;

    private CallForwardingData callForwardingData;
    private CallBarringData callBarringData;
    private ODBInfo odbInfo;
    private CAMELSubscriptionInfo camelSubscriptionInfo;
    private SupportedCamelPhases supportedVlrCamelPhases;
    private SupportedCamelPhases supportedSgsnCamelPhases;
    private MAPExtensionContainer extensionContainer;
    private OfferedCamel4CSIs offeredCamel4CSIsInVlr;
    private OfferedCamel4CSIs offeredCamel4CSIsInSgsn;
    private ArrayList<MSISDNBS> msisdnBsList;
    private ArrayList<CSGSubscriptionData> csgSubscriptionDataList;
    private CallWaitingData cwData;
    private CallHoldData chData;
    private ClipData clipData;
    private ClirData clirData;
    private EctData ectData;
    
    public AnyTimeSubscriptionInterrogationResponseImpl() {
    }

    public AnyTimeSubscriptionInterrogationResponseImpl(
            CallForwardingData callForwardingData, CallBarringData callBarringData, ODBInfo odbInfo,
            CAMELSubscriptionInfo camelSubscriptionInfo, SupportedCamelPhases supportedVlrCamelPhases,
            SupportedCamelPhases supportedSgsnCamelPhases, MAPExtensionContainer extensionContainer,
            OfferedCamel4CSIs offeredCamel4CSIsInVlr, OfferedCamel4CSIs offeredCamel4CSIsInSgsn,
            ArrayList<MSISDNBS> msisdnBsList, ArrayList<CSGSubscriptionData> csgSubscriptionDataList, CallWaitingData cwData,
            CallHoldData chData, ClipData clipData, ClirData clirData, EctData ectData) {
        this.callForwardingData = callForwardingData;
        this.callBarringData = callBarringData;
        this.odbInfo = odbInfo;
        this.camelSubscriptionInfo = camelSubscriptionInfo;
        this.supportedVlrCamelPhases = supportedVlrCamelPhases;
        this.supportedSgsnCamelPhases = supportedSgsnCamelPhases;
        this.extensionContainer = extensionContainer;
        this.offeredCamel4CSIsInVlr = offeredCamel4CSIsInVlr;
        this.offeredCamel4CSIsInSgsn = offeredCamel4CSIsInSgsn;
        this.msisdnBsList = msisdnBsList;
        this.csgSubscriptionDataList = csgSubscriptionDataList;
        this.cwData = cwData;
        this.chData = chData;
        this.clipData = clipData;
        this.clirData = clirData;
        this.ectData = ectData;
    }

    public CallForwardingData getCallForwardingData() {
        return callForwardingData;
    }

    public CallBarringData getCallBarringData() {
        return callBarringData;
    }

    public ODBInfo getOdbInfo() {
        return odbInfo;
    }

    public CAMELSubscriptionInfo getCamelSubscriptionInfo() {
        return camelSubscriptionInfo;
    }

    public SupportedCamelPhases getSupportedVlrCamelPhases() {
        return supportedVlrCamelPhases;
    }

    public SupportedCamelPhases getSupportedSgsnCamelPhases() {
        return supportedSgsnCamelPhases;
    }

    public MAPExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    public OfferedCamel4CSIs getOfferedCamel4CSIsInVlr() {
        return offeredCamel4CSIsInVlr;
    }

    public OfferedCamel4CSIs getOfferedCamel4CSIsInSgsn() {
        return offeredCamel4CSIsInSgsn;
    }

    public ArrayList<MSISDNBS> getMsisdnBsList() {
        return msisdnBsList;
    }

    public ArrayList<CSGSubscriptionData> getCsgSubscriptionDataList() {
        return csgSubscriptionDataList;
    }

    public CallWaitingData getCwData() {
        return cwData;
    }

    public CallHoldData getChData() {
        return chData;
    }

    public ClipData getClipData() {
        return clipData;
    }

    public ClirData getClirData() {
        return clirData;
    }

    public EctData getEctData() {
        return ectData;
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
            throw new MAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new MAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    MAPParsingComponentExceptionReason.MistypedParameter);
        }
    }


    private void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException, AsnException {
        AsnInputStream ais = ansIS.readSequenceStreamData(length);

        this.callForwardingData = null;
        this.callBarringData = null;
        this.odbInfo = null;
        this.camelSubscriptionInfo = null;
        this.supportedVlrCamelPhases = null;
        this.supportedSgsnCamelPhases = null;
        this.extensionContainer = null;
        this.offeredCamel4CSIsInVlr = null;
        this.offeredCamel4CSIsInSgsn = null;
        this.msisdnBsList = null;
        this.csgSubscriptionDataList = null;
        this.cwData = null;
        this.chData = null;
        this.clipData = null;
        this.clirData = null;
        this.ectData = null;


        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();
            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                    case _TAG_callForwardingData:
                        callForwardingData = (CallForwardingData) ObjectEncoderFacility.decodeObject(
                                ais, new CallForwardingDataImpl(), "callForwardingData", _PrimitiveName);
                        break;
                    case _TAG_callBarringData:
                        callBarringData = (CallBarringData) ObjectEncoderFacility.decodeObject(
                                ais, new CallBarringDataImpl(), "callBarringData", _PrimitiveName);
                        break;
                    case _TAG_odbInfo:
                        odbInfo = (ODBInfo) ObjectEncoderFacility.decodeObject(ais, new ODBInfoImpl(), "odbInfo", _PrimitiveName);
                        break;
                    case _TAG_camelSubscriptionInfo:
                        camelSubscriptionInfo = (CAMELSubscriptionInfo) ObjectEncoderFacility.decodeObject(
                                ais, new CAMELSubscriptionInfoImpl(), "camelSubscriptionInfo", _PrimitiveName);
                        break;
                    case _TAG_supportedVlrCamelPhases:
                        supportedVlrCamelPhases = (SupportedCamelPhases) ObjectEncoderFacility.decodePrimitiveObject(
                                ais, new SupportedCamelPhasesImpl(), "supportedVlrCamelPhases", _PrimitiveName);
                        break;
                    case _TAG_supportedSgsnCamelPhases:
                        supportedSgsnCamelPhases = (SupportedCamelPhases) ObjectEncoderFacility.decodePrimitiveObject(
                                ais, new SupportedCamelPhasesImpl(), "supportedSgsnCamelPhases", _PrimitiveName);
                        break;
                    case _TAG_extensionContainer:
                        extensionContainer = (MAPExtensionContainer) ObjectEncoderFacility.decodePrimitiveObject(
                                ais, new MAPExtensionContainerImpl(), "extensionContainer", _PrimitiveName);
                        break;
                    case _TAG_offeredCamel4CSIsInVlr:
                        offeredCamel4CSIsInVlr = (OfferedCamel4CSIs) ObjectEncoderFacility.decodePrimitiveObject(
                                ais, new OfferedCamel4CSIsImpl(), "offeredCamel4CSIsInVlr", _PrimitiveName);
                        break;
                    case _TAG_offeredCamel4CSIsInSgsn:
                        offeredCamel4CSIsInSgsn = (OfferedCamel4CSIs) ObjectEncoderFacility.decodePrimitiveObject(
                                ais, new OfferedCamel4CSIsImpl(), "offeredCamel4CSIsInSgsn", _PrimitiveName);
                        break;
                    case _TAG_msisdnBsList:
                        if (ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".msisdnBsList: is primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        AsnInputStream ais2 = ais.readSequenceStream();
                        this.msisdnBsList = new ArrayList<MSISDNBS>();
                        while (true) {
                            if (ais2.available() == 0)
                                break;

                            int tag2 = ais2.readTag();
                            if (tag2 != Tag.SEQUENCE || ais2.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || ais2.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ": bad MSISDNBS tag or tagClass or is primitive ",
                                        MAPParsingComponentExceptionReason.MistypedParameter);

                            MSISDNBS elem = new MSISDNBSImpl();
                            ((MSISDNBSImpl) elem).decodeAll(ais2);
                            this.msisdnBsList.add(elem);
                        }

                        if (this.msisdnBsList.size() < 1 || this.msisdnBsList.size() > 50) {
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": Parameter msisdnBsList from 1 to 50, found: "
                                    + this.msisdnBsList.size(),
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        }
                        break;
                    case _TAG_csgSubscriptionDataList:
                        if (ais.isTagPrimitive())
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".msisdnBsList: is primitive",
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        AsnInputStream ais3 = ais.readSequenceStream();
                        this.csgSubscriptionDataList = new ArrayList<CSGSubscriptionData>();
                        while (true) {
                            if (ais3.available() == 0)
                                break;

                            int tag2 = ais3.readTag();
                            if (tag2 != Tag.SEQUENCE || ais3.getTagClass() != Tag.CLASS_UNIVERSAL || ais3.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ": bad MSISDNBS tag or tagClass or is primitive ",
                                        MAPParsingComponentExceptionReason.MistypedParameter);

                            CSGSubscriptionDataImpl elem = new CSGSubscriptionDataImpl();
                            elem.decodeAll(ais3);
                            this.csgSubscriptionDataList.add(elem);
                        }

                        if (this.csgSubscriptionDataList.size() < 1 || this.csgSubscriptionDataList.size() > 50) {
                            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": Parameter csgSubscriptionDataList from 1 to 50, found: "
                                    + this.csgSubscriptionDataList.size(),
                                    MAPParsingComponentExceptionReason.MistypedParameter);
                        }
                        break;
                    case _TAG_cwData:
                        cwData = (CallWaitingData) ObjectEncoderFacility.decodeObject(
                                ais, new CallWaitingDataImpl(), "cwData", _PrimitiveName);
                        break;
                    case _TAG_chData:
                        chData = (CallHoldData) ObjectEncoderFacility.decodeObject(
                                ais, new CallHoldDataImpl(), "chData", _PrimitiveName);
                        break;
                    case _TAG_clirData:
                        clirData = (ClirData) ObjectEncoderFacility.decodeObject(
                                ais, new ClirDataImpl(), "clirData", _PrimitiveName);
                        break;
                    case _TAG_clipData:
                        clipData = (ClipData) ObjectEncoderFacility.decodeObject(
                                ais, new ClipDataImpl(), "clipData", _PrimitiveName);
                        break;
                    case _TAG_ectData:
                        ectData = (EctData) ObjectEncoderFacility.decodeObject(
                                ais, new EctDataImpl(), "ectData", _PrimitiveName);
                        break;
                    default:
                        ais.advanceElement();
                        break;
                }
            } else {
                ais.advanceElement();
            }
        }
    }


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

        if (this.callForwardingData!=null) {
            ((CallForwardingDataImpl) this.callForwardingData).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_callForwardingData);
        }

        if (this.callBarringData!=null) {
            ((CallBarringDataImpl) this.callBarringData).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_callBarringData);
        }

        if (this.odbInfo!=null) {
            ((ODBInfoImpl) this.odbInfo).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_odbInfo);
        }

        if (this.camelSubscriptionInfo!=null) {
            ((CAMELSubscriptionInfoImpl) this.camelSubscriptionInfo).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_camelSubscriptionInfo);
        }

        if (this.supportedVlrCamelPhases!=null) {
            ((SupportedCamelPhasesImpl) this.supportedVlrCamelPhases).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_supportedVlrCamelPhases);
        }

        if (this.supportedSgsnCamelPhases!=null) {
            ((SupportedCamelPhasesImpl) this.supportedSgsnCamelPhases).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_supportedSgsnCamelPhases);
        }

        if (this.extensionContainer!=null) {
            ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_extensionContainer);
        }

        if (this.offeredCamel4CSIsInVlr!=null) {
            ((OfferedCamel4CSIsImpl) this.offeredCamel4CSIsInVlr).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_offeredCamel4CSIsInVlr);
        }

        if (this.offeredCamel4CSIsInSgsn!=null) {
            ((OfferedCamel4CSIsImpl) this.offeredCamel4CSIsInSgsn).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_offeredCamel4CSIsInSgsn);
        }

        if (this.cwData!=null) {
            ((CallWaitingDataImpl) this.cwData).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_cwData);
        }

        if (this.chData!=null) {
            ((CallHoldDataImpl) this.chData).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_chData);
        }

        if (this.clipData!=null) {
            ((ClipDataImpl) this.clipData).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_clipData);
        }

        if (this.clirData!=null) {
            ((ClirDataImpl) this.clirData).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_clirData);
        }

        if (this.ectData!=null) {
            ((EctDataImpl) this.ectData).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_ectData);
        }

        if (this.msisdnBsList != null) {
            try {
                asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_msisdnBsList);
                int pos = asnOs.StartContentDefiniteLength();
                for (MSISDNBS elem : this.msisdnBsList) {
                    ((MSISDNBSImpl) elem).encodeAll(asnOs);
                }
                asnOs.FinalizeContent(pos);
            } catch (AsnException e) {
                throw new MAPException("AsnException when encoding " + _PrimitiveName + ".msisdnBsList: "
                        + e.getMessage(), e);
            }
        }

        if (this.csgSubscriptionDataList != null) {
            try {
                asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_csgSubscriptionDataList);
                int pos = asnOs.StartContentDefiniteLength();
                for (CSGSubscriptionData elem : this.csgSubscriptionDataList) {
                    ((CSGSubscriptionDataImpl) elem).encodeAll(asnOs);
                }
                asnOs.FinalizeContent(pos);
            } catch (AsnException e) {
                throw new MAPException("AsnException when encoding " + _PrimitiveName + ".csgSubscriptionDataList: "
                        + e.getMessage(), e);
            }
        }

    }

    public MAPMessageType getMessageType() {
        return MAPMessageType.anyTimeSubscriptionInterrogation_Response;
    }

    public int getOperationCode() {
        return MAPOperationCode.anyTimeSubscriptionInterrogation;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");
        if (this.callForwardingData != null) {
            sb.append("callForwardingData=");
            sb.append(this.callForwardingData);
        }
        if (this.callBarringData != null) {
            sb.append(", callBarringData=");
            sb.append(this.callBarringData);
        }
        if (this.odbInfo != null) {
            sb.append(", odbInfo=");
            sb.append(this.odbInfo);
        }
        if (this.camelSubscriptionInfo != null) {
            sb.append(", camelSubscriptionInfo=");
            sb.append(this.camelSubscriptionInfo);
        }
        if (this.supportedVlrCamelPhases != null) {
            sb.append(", supportedVlrCamelPhases=");
            sb.append(this.supportedVlrCamelPhases);
        }
        if (this.supportedSgsnCamelPhases != null) {
            sb.append(", supportedSgsnCamelPhases=");
            sb.append(this.supportedSgsnCamelPhases);
        }
        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.extensionContainer);
        }
        if (this.offeredCamel4CSIsInVlr != null) {
            sb.append(", offeredCamel4CSIsInVlr=");
            sb.append(this.offeredCamel4CSIsInVlr);
        }
        if (this.offeredCamel4CSIsInSgsn != null) {
            sb.append(", offeredCamel4CSIsInSgsn=");
            sb.append(this.offeredCamel4CSIsInSgsn);
        }
        if (this.msisdnBsList != null) {
            sb.append(", msisdnBsList=");
            sb.append(this.msisdnBsList);
        }
        if (this.csgSubscriptionDataList != null) {
            sb.append(", csgSubscriptionDataList=");
            sb.append(this.csgSubscriptionDataList);
        }
        if (this.cwData != null) {
            sb.append(", cwData=");
            sb.append(this.cwData);
        }
        if (this.chData != null) {
            sb.append(", chData=");
            sb.append(this.chData);
        }
        if (this.clipData != null) {
            sb.append(", clipData=");
            sb.append(this.clipData);
        }
        if (this.clirData != null) {
            sb.append(", clirData=");
            sb.append(this.clirData);
        }
        if (this.ectData != null) {
            sb.append(", ectData=");
            sb.append(this.ectData);
        }
        sb.append("]");
        return sb.toString();
    }

}
