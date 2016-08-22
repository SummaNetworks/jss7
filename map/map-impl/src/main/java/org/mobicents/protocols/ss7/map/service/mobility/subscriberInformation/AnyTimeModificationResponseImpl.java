package org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation;

import java.io.IOException;
import java.lang.reflect.Field;
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
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeModificationResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.CAMELSubscriptionInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.CallHoldData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.CallWaitingData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.ClipData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.ClirData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.EctData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.ExtSSInfoForCSE;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.ODBInfo;
import org.mobicents.protocols.ss7.map.datacoding.ObjectEncoderFacility;
import org.mobicents.protocols.ss7.map.primitives.AddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.service.mobility.MobilityMessageImpl;

/**
 *
 * <code>
 * AnyTimeModificationRes ::= SEQUENCE {
 *      ss-InfoFor-CSE           [0] Ext-SS-InfoFor-CSE OPTIONAL,
 *      camel-SubscriptionInfo   [1] CAMEL-SubscriptionInfo OPTIONAL,
 *      extensionContainer       [2] ExtensionContainer OPTIONAL,
 *      odb-Info                 [3] ODB-Info OPTIONAL,
 *      cw-Data                  [4] CallWaitingData OPTIONAL,
 *      ch-Data                  [5] CallHoldData OPTIONAL,
 *      clip-Data                [6] ClipData OPTIONAL,
 *      clir-Data                [7] ClirData OPTIONAL,
 *      ect-data                 [8] EctData OPTIONAL,
 *      serviceCentreAddress     [9] AddressString OPTIONAL
 * }
 * </code>
 *
 * @author eva ogallar
 */
public class AnyTimeModificationResponseImpl extends MobilityMessageImpl implements AnyTimeModificationResponse,
        MAPAsnPrimitive {

    public static final String _PrimitiveName = "AnyTimeModificationResponse";

    private static final int _TAG_ss_InfoFor_CSE = 0;
    private static final int _TAG_camelSubscriptionInfo = 1;
    private static final int _TAG_extensionContainer = 2;
    private static final int _TAG_odbInfo = 3;
    private static final int _TAG_cwData = 4;
    private static final int _TAG_chData = 5;
    private static final int _TAG_clipData = 6;
    private static final int _TAG_clirData = 7;
    private static final int _TAG_ectData = 8;
    private static final int _TAG_serviceCentreAddress = 9;

    private ExtSSInfoForCSE ssInfoForCSE;
    private CAMELSubscriptionInfo camelSubscriptionInfo;
    private MAPExtensionContainer extensionContainer;
    private ODBInfo odbInfo;
    private CallWaitingData cwData;
    private CallHoldData chData;
    private ClipData clipData;
    private ClirData clirData;
    private EctData ectData;
    private AddressString serviceCentreAddress;


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


    public AnyTimeModificationResponseImpl() {
    }

    public AnyTimeModificationResponseImpl(ExtSSInfoForCSE ssInfoForCSE, CAMELSubscriptionInfo camelSubscriptionInfo,
                                           MAPExtensionContainer extensionContainer, ODBInfo odbInfo, CallWaitingData cwData,
                                           CallHoldData chData, ClipData clipData, ClirData clirData, EctData ectData,
                                           AddressString serviceCentreAddress) {
        this.ssInfoForCSE = ssInfoForCSE;
        this.camelSubscriptionInfo = camelSubscriptionInfo;
        this.extensionContainer = extensionContainer;
        this.odbInfo = odbInfo;
        this.cwData = cwData;
        this.chData = chData;
        this.clipData = clipData;
        this.clirData = clirData;
        this.ectData = ectData;
        this.serviceCentreAddress = serviceCentreAddress;
    }

    public ExtSSInfoForCSE getSsInfoForCSE() {
        return ssInfoForCSE;
    }

    public ODBInfo getOdbInfo() {
        return odbInfo;
    }

    public CAMELSubscriptionInfo getCamelSubscriptionInfo() {
        return camelSubscriptionInfo;
    }

    public AddressString getServiceCentreAddress() {
        return serviceCentreAddress;
    }

    public MAPExtensionContainer getExtensionContainer() {
        return extensionContainer;
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

        this.ssInfoForCSE = null;
        this.odbInfo = null;
        this.camelSubscriptionInfo = null;
        this.serviceCentreAddress = null;
        this.extensionContainer = null;
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
                    case _TAG_ss_InfoFor_CSE:
                        AsnInputStream ais2 = ais.readSequenceStream();
                        ais2.readTag();
                        this.ssInfoForCSE = new ExtSSInfoForCSEImpl();
                        ((ExtSSInfoForCSEImpl) this.ssInfoForCSE).decodeAll(ais2);
                        break;
                    case _TAG_odbInfo:
                        odbInfo = (ODBInfo) ObjectEncoderFacility.decodeObject(ais, new ODBInfoImpl(), "odbInfo", _PrimitiveName);
                        break;
                    case _TAG_camelSubscriptionInfo:
                        camelSubscriptionInfo = (CAMELSubscriptionInfo) ObjectEncoderFacility.decodeObject(
                                ais, new CAMELSubscriptionInfoImpl(), "camelSubscriptionInfo", _PrimitiveName);
                        break;
                    case _TAG_extensionContainer:
                        extensionContainer = (MAPExtensionContainer) ObjectEncoderFacility.decodePrimitiveObject(
                                ais, new MAPExtensionContainerImpl(), "extensionContainer", _PrimitiveName);
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
                    case _TAG_serviceCentreAddress:
                        serviceCentreAddress = (AddressString) ObjectEncoderFacility.decodePrimitiveObject(
                                ais, new AddressStringImpl(), "serviceCentreAddress", _PrimitiveName);
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
        try {
            if (this.ssInfoForCSE !=null) {
                asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_ss_InfoFor_CSE);
                int pos = asnOs.StartContentDefiniteLength();
                ((ExtSSInfoForCSEImpl) this.ssInfoForCSE).encodeAll(asnOs);
                asnOs.FinalizeContent(pos);
            }
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }

        if (this.odbInfo!=null) {
            ((ODBInfoImpl) this.odbInfo).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_odbInfo);
        }

        if (this.camelSubscriptionInfo!=null) {
            ((CAMELSubscriptionInfoImpl) this.camelSubscriptionInfo).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_camelSubscriptionInfo);
        }

        if (this.serviceCentreAddress !=null) {
            ((AddressStringImpl) this.serviceCentreAddress).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_serviceCentreAddress);
        }

        if (this.extensionContainer!=null) {
            ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_extensionContainer);
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


    }

    public MAPMessageType getMessageType() {
        return MAPMessageType.anyTimeModification_Response;
    }

    public int getOperationCode() {
        return MAPOperationCode.anyTimeModification;
    }
}
