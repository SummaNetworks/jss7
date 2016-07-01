package org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.primitives.SubscriberIdentityImpl;
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

    private Map<Integer, Field> getContextSpecificMap() throws NoSuchFieldException {
        HashMap<Integer, Field> integerFieldHashMap = new HashMap<Integer, Field>();
        integerFieldHashMap.put(1,  this.getClass().getDeclaredField("callForwardingData"));
        integerFieldHashMap.put(2,  this.getClass().getDeclaredField("callBarringData"));
        integerFieldHashMap.put(3,  this.getClass().getDeclaredField("odbInfo"));
        integerFieldHashMap.put(4,  this.getClass().getDeclaredField("camelSubscriptionInfo"));
        integerFieldHashMap.put(5,  this.getClass().getDeclaredField("supportedVlrCamelPhases"));
        integerFieldHashMap.put(6,  this.getClass().getDeclaredField("supportedSgsnCamelPhases"));
        integerFieldHashMap.put(7,  this.getClass().getDeclaredField("extensionContainer"));
        integerFieldHashMap.put(8,  this.getClass().getDeclaredField("offeredCamel4CSIsInVlr"));
        integerFieldHashMap.put(9,  this.getClass().getDeclaredField("offeredCamel4CSIsInSgsn"));
        integerFieldHashMap.put(10, this.getClass().getDeclaredField("msisdnBsList"));
        integerFieldHashMap.put(11, this.getClass().getDeclaredField("csgSubscriptionDataList"));
        integerFieldHashMap.put(12, this.getClass().getDeclaredField("cwData"));
        integerFieldHashMap.put(13, this.getClass().getDeclaredField("chData"));
        integerFieldHashMap.put(14, this.getClass().getDeclaredField("clipData"));
        integerFieldHashMap.put(15, this.getClass().getDeclaredField("clirData"));
        integerFieldHashMap.put(16, this.getClass().getDeclaredField("ectData"));
        return integerFieldHashMap;
    }


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
                        callBarringData = (CallBarringData) ObjectEncoderFacility.decodeObject(ais, new CallBarringDataImpl(), "callBarringData", _PrimitiveName);
                        break;
                    case _TAG_odbInfo:
                        odbInfo = (ODBInfo) ObjectEncoderFacility.decodeObject(ais, new CallBarringDataImpl(), "callBarringData", _PrimitiveName);
                        break;
                    case _TAG_camelSubscriptionInfo:
                        camelSubscriptionInfo = (CAMELSubscriptionInfo) ObjectEncoderFacility.decodeObject(
                                ais, new CAMELSubscriptionInfoImpl(), "camelSubscriptionInfo", _PrimitiveName);
                        break;
                    case _TAG_supportedVlrCamelPhases:
                        supportedVlrCamelPhases = (SupportedCamelPhases) ObjectEncoderFacility.decodeObject(
                                ais, new SupportedCamelPhasesImpl(), "supportedVlrCamelPhases", _PrimitiveName);
                        break;
                    case _TAG_supportedSgsnCamelPhases:
                        supportedSgsnCamelPhases = (SupportedCamelPhases) ObjectEncoderFacility.decodeObject(
                                ais, new SupportedCamelPhasesImpl(), "supportedSgsnCamelPhases", _PrimitiveName);
                        break;
                    case _TAG_extensionContainer:
                        extensionContainer = (MAPExtensionContainer) ObjectEncoderFacility.decodeObject(
                                ais, new MAPExtensionContainerImpl(), "extensionContainer", _PrimitiveName);
                        break;
                    case _TAG_offeredCamel4CSIsInVlr:
                        offeredCamel4CSIsInVlr = (OfferedCamel4CSIs) ObjectEncoderFacility.decodeObject(
                                ais, new OfferedCamel4CSIsImpl(), "offeredCamel4CSIsInVlr", _PrimitiveName);
                        break;
                    case _TAG_offeredCamel4CSIsInSgsn:
                        offeredCamel4CSIsInSgsn = (OfferedCamel4CSIs) ObjectEncoderFacility.decodeObject(
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
                            if (tag2 != Tag.SEQUENCE || ais2.getTagClass() != Tag.CLASS_UNIVERSAL || ais2.isTagPrimitive())
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
                                ais, new CallHoldDataImpl(), "cwData", _PrimitiveName);
                        break;
                    case _TAG_clirData:
                        clirData = (ClirData) ObjectEncoderFacility.decodeObject(
                                ais, new ClirDataImpl(), "cwData", _PrimitiveName);
                        break;
                    case _TAG_clipData:
                        clipData = (ClipData) ObjectEncoderFacility.decodeObject(
                                ais, new ClipDataImpl(), "cwData", _PrimitiveName);
                        break;
                    case _TAG_ectData:
                        ectData = (EctData) ObjectEncoderFacility.decodeObject(
                                ais, new EctDataImpl(), "cwData", _PrimitiveName);
                        break;

                    default:
                        ais.advanceElement();
                        break;
                }
            } else {
                ais.advanceElement();
            }
        }
/*
        if (this.subscriberIdentity == null || this.requestedSubscriptionInfo == null || this.gsmSCFAddress == null)
            throw new MAPParsingComponentException(
                    "Error while decoding " + _PrimitiveName + ": subscriberIdentity, requestedSubscriptionInfo " +
                            "and gsmSCFAddress parameters are mandatory but some of them are not found",
                    MAPParsingComponentExceptionReason.MistypedParameter);
*/
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
        if (this.subscriberIdentity == null) {
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + " the mandatory parameter subscriberIdentity is not defined");
        }
        if (this.requestedSubscriptionInfo == null) {
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + " the mandatory parameter requestedSubscriptionInfo is not defined");
        }
        if (this.gsmSCFAddress == null) {
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + " the mandatory parameter gsmSCF-Address is not defined");
        }

        if (this.callForwardingData!=null) {
            ((CallForwardingDataImpl) this.callForwardingData).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_callForwardingData);
        }

        try {
            asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_SUBSCRIBER_IDENTITY);
            int pos = asnOs.StartContentDefiniteLength();
            ((SubscriberIdentityImpl) this.subscriberIdentity).encodeAll(asnOs);
            asnOs.FinalizeContent(pos);
        } catch (AsnException e) {
            throw new MAPException("AsnException while encoding " + _PrimitiveName
                    + " parameter subscriberIdentity [0] SubscriberIdentity");
        }

        ((RequestedInfoImpl) this.requestedSubscriptionInfo).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_REQUESTED_SUBSCRIPTION_INFO);

        ((ISDNAddressStringImpl) this.gsmSCFAddress).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_GSM_SCF_ADDRESS);

        if (this.extensionContainer != null) {
            ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                    _TAG_EXTENSION_CONTAINER);
        }

        try {
            if (this.longFtnSupported) {
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_LONG_FTN_SUPPORTED);
            }
        } catch (IOException e) {
            throw new MAPException("IOException when encoding parameter longFtnSupported: ", e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding parameter longFtnSupported: ", e);
        }
    }

    public MAPMessageType getMessageType() {
        return MAPMessageType.anyTimeSubscriptionInterrogation_Response;
    }

    public int getOperationCode() {
        return MAPOperationCode.anyTimeSubscriptionInterrogation;
    }
}
