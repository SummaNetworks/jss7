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
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.CAMELSubscriptionInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.CallHoldData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.CallWaitingData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.ClipData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.ClirData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.EctData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.ExtCallBarringInfoForCSE;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.ExtForwardingInfoForCSE;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.NoteSubscriberDataModifiedRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.ODBInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.ServingNode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.CSGSubscriptionData;
import org.mobicents.protocols.ss7.map.datacoding.NullEncoderFacility;
import org.mobicents.protocols.ss7.map.datacoding.ObjectEncoderFacility;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.service.mobility.MobilityMessageImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.CSGSubscriptionDataImpl;

/**
 * <code>
 * NoteSubscriberDataModifiedArg ::= SEQUENCE {
 * imsi                    IMSI,
 * msisdn                  ISDN-AddressString,
 * forwardingInfoFor-CSE     [0] Ext-ForwardingInfoFor-CSE OPTIONAL,
 * callBarringInfoFor-CSE    [1] Ext-CallBarringInfoFor-CSE OPTIONAL,
 * odb-Info                  [2] ODB-Info OPTIONAL,
 * camel-SubscriptionInfo    [3] CAMEL-SubscriptionInfo OPTIONAL,
 * allInformationSent        [4] NULL OPTIONAL,
 * extensionContainer        ExtensionContainer OPTIONAL,
 * ...,
 * ue-reachable              [5] ServingNode OPTIONAL,
 * csg-SubscriptionDataList  [6] CSG-SubscriptionDataList OPTIONAL,
 * cw-Data                   [7] CallWaitingData OPTIONAL,
 * ch-Data                   [8] CallHoldData OPTIONAL,
 * clip-Data                 [9] ClipData OPTIONAL,
 * clir-Data                 [10] ClirData OPTIONAL,
 * ect-data                  [11] EctData OPTIONAL
 * }
 * <p/>
 * CSG-SubscriptionDataList ::= SEQUENCE SIZE (1..50) OF CSG-SubscriptionData
 * </code>
 *
 * @author eva ogallar
 */
public class NoteSubscriberDataModifiedRequestImpl extends MobilityMessageImpl implements NoteSubscriberDataModifiedRequest,
        MAPAsnPrimitive {

    public static final String _PrimitiveName = "NoteSubscriberDataModifiedRequest";

    public static final int _TAG_forwardingInfoFor_CSE = 0;
    public static final int _TAG_callBarringInfoFor_CSE = 1;
    private static final int _TAG_odbInfo = 2;
    private static final int _TAG_camelSubscriptionInfo = 3;
    private static final int _TAG_allInformationSent = 4;
    private static final int _TAG_uereachable = 5;
    private static final int _TAG_csg_SubscriptionDataList = 6;
    private static final int _TAG_cwData = 7;
    private static final int _TAG_chData = 8;
    private static final int _TAG_clipData = 9;
    private static final int _TAG_clirData = 10;
    private static final int _TAG_ectData = 11;

    private IMSI imsi;
    private ISDNAddressString msisdn;
    private ExtForwardingInfoForCSE forwardingInfoForCSE;
    private ExtCallBarringInfoForCSE callBarringInfoForCSE;
    private ODBInfo odbInfo;
    private CAMELSubscriptionInfo camelSubscriptionInfo;
    private boolean allInformationSent;
    private MAPExtensionContainer extensionContainer;
    private ServingNode ueReachable;
    private ArrayList<CSGSubscriptionData> csgSubscriptionDataList;
    private CallWaitingData cwData;
    private CallHoldData chData;
    private ClipData clipData;
    private ClirData clirData;
    private EctData ectData;


    public NoteSubscriberDataModifiedRequestImpl() {
    }

    public NoteSubscriberDataModifiedRequestImpl(IMSI imsi, ISDNAddressString msisdn, ExtForwardingInfoForCSE forwardingInfoForCSE,
                                                 ExtCallBarringInfoForCSE callBarringInfoForCSE, ODBInfo odbInfo,
                                                 CAMELSubscriptionInfo camelSubscriptionInfo, boolean allInformationSent,
                                                 MAPExtensionContainer extensionContainer, ServingNode ueReachable,
                                                 ArrayList<CSGSubscriptionData> csgSubscriptionDataList, CallWaitingData cwData,
                                                 CallHoldData chData, ClipData clipData, ClirData clirData, EctData ectData) {
        this.imsi = imsi;
        this.msisdn = msisdn;
        this.forwardingInfoForCSE = forwardingInfoForCSE;
        this.callBarringInfoForCSE = callBarringInfoForCSE;
        this.odbInfo = odbInfo;
        this.camelSubscriptionInfo = camelSubscriptionInfo;
        this.allInformationSent = allInformationSent;
        this.extensionContainer = extensionContainer;
        this.ueReachable = ueReachable;
        this.csgSubscriptionDataList = csgSubscriptionDataList;
        this.cwData = cwData;
        this.chData = chData;
        this.clipData = clipData;
        this.clirData = clirData;
        this.ectData = ectData;
    }

    public ArrayList<CSGSubscriptionData> getCsgSubscriptionDataList() {
        return csgSubscriptionDataList;
    }

    public ServingNode getUeReachable() {
        return ueReachable;
    }

    public boolean getAllInformationSent() {
        return allInformationSent;
    }

    public ExtCallBarringInfoForCSE getCallBarringInfoForCSE() {
        return callBarringInfoForCSE;
    }

    public ExtForwardingInfoForCSE getForwardingInfoForCSE() {
        return forwardingInfoForCSE;
    }

    public ISDNAddressString getMsisdn() {
        return msisdn;
    }

    public IMSI getImsi() {
        return imsi;
    }

    public ODBInfo getOdbInfo() {
        return odbInfo;
    }

    public CAMELSubscriptionInfo getCamelSubscriptionInfo() {
        return camelSubscriptionInfo;
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

        this.imsi = null;
        this.msisdn = null;
        this.forwardingInfoForCSE = null;
        this.callBarringInfoForCSE = null;
        this.odbInfo = null;
        this.camelSubscriptionInfo = null;
        this.allInformationSent = false;
        this.extensionContainer = null;
        this.ueReachable = null;
        this.csgSubscriptionDataList = null;
        this.cwData = null;
        this.chData = null;
        this.clipData = null;
        this.clirData = null;
        this.ectData = null;

        int num = 0;
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            switch (num) {
                case 0:
                    // imsi
                    if (ais.getTagClass() != Tag.CLASS_UNIVERSAL || !ais.isTagPrimitive() || tag != Tag.STRING_OCTET)
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".imsi: Parameter 0 bad tag or tag class or not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.imsi = new IMSIImpl();
                    ((IMSIImpl) this.imsi).decodeAll(ais);
                    break;

                case 1:
                    // sgsnNumber
                    if (ais.getTagClass() != Tag.CLASS_UNIVERSAL || !ais.isTagPrimitive() || tag != Tag.STRING_OCTET)
                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                + ".imsi: Parameter 0 bad tag or tag class or not primitive",
                                MAPParsingComponentExceptionReason.MistypedParameter);
                    this.msisdn = new ISDNAddressStringImpl();
                    ((ISDNAddressStringImpl) this.msisdn).decodeAll(ais);
                    break;

                default:
                    if (ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                        switch (tag) {
                            case Tag.SEQUENCE:
                                // extensionContainer
                                if (ais.isTagPrimitive())
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ".extensionContainer: Parameter extensionContainer is primitive",
                                            MAPParsingComponentExceptionReason.MistypedParameter);
                                this.extensionContainer = new MAPExtensionContainerImpl();
                                ((MAPExtensionContainerImpl) this.extensionContainer).decodeAll(ais);
                                break;
                            default:
                                ais.advanceElement();
                                break;
                        }
                    } else if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                        switch (tag) {
                            case _TAG_forwardingInfoFor_CSE:
                                forwardingInfoForCSE = (ExtForwardingInfoForCSE) ObjectEncoderFacility.decodeObject(ais,
                                        new ExtForwardingInfoForCSEImpl(), "forwardingInfoForCSE", _PrimitiveName);
                                break;
                            case _TAG_callBarringInfoFor_CSE:
                                callBarringInfoForCSE = (ExtCallBarringInfoForCSE) ObjectEncoderFacility.decodeObject(ais,
                                        new ExtCallBarringInfoForCSEImpl(), "callBarringInfoForCSE", _PrimitiveName);
                                break;
                            case _TAG_odbInfo:
                                odbInfo = (ODBInfo) ObjectEncoderFacility.decodeObject(ais, new ODBInfoImpl(), "odbInfo", _PrimitiveName);
                                break;
                            case _TAG_camelSubscriptionInfo:
                                camelSubscriptionInfo = (CAMELSubscriptionInfo) ObjectEncoderFacility.decodeObject(
                                        ais, new CAMELSubscriptionInfoImpl(), "camelSubscriptionInfo", _PrimitiveName);
                                break;
                            case _TAG_allInformationSent:
                                allInformationSent = NullEncoderFacility.decode(ais, "allInformationSent", _PrimitiveName);
                                break;
                            case _TAG_uereachable:
                                ueReachable = (ServingNode) ObjectEncoderFacility.decodePrimitiveObject(
                                        ais, new ServingNodeImpl(), "ueReachable", _PrimitiveName);
                                break;
                            case _TAG_csg_SubscriptionDataList:
                                if (ais.isTagPrimitive())
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ".csg_SubscriptionDataList: Parameter is primitive",
                                            MAPParsingComponentExceptionReason.MistypedParameter);

                                AsnInputStream ais4 = ais.readSequenceStream();
                                this.csgSubscriptionDataList = new ArrayList<CSGSubscriptionData>();
                                while (true) {
                                    if (ais4.available() == 0)
                                        break;

                                    int tag4 = ais4.readTag();
                                    if (tag4 != Tag.SEQUENCE || ais4.getTagClass() != Tag.CLASS_UNIVERSAL || ais4.isTagPrimitive())
                                        throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                                + ": bad csgSubscriptionDataList element tag or tagClass or is primitive ",
                                                MAPParsingComponentExceptionReason.MistypedParameter);

                                    CSGSubscriptionData csgSubscriptionData = new CSGSubscriptionDataImpl();
                                    ((CSGSubscriptionDataImpl) csgSubscriptionData).decodeAll(ais4);
                                    csgSubscriptionDataList.add(csgSubscriptionData);
                                }
                                if (this.csgSubscriptionDataList.size() < 1 || this.csgSubscriptionDataList.size() > 50) {
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ": Parameter csgSubscriptionDataList size must be from 1 to 50, found: "
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
                    break;
            }
            num++;
        }

        validate();
    }

    private void validate() throws MAPParsingComponentException {
        if (this.imsi == null || this.msisdn == null) {
            throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": imsi or msisdn is null ", MAPParsingComponentExceptionReason.MistypedParameter);
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
            validate();
        } catch (MAPParsingComponentException e) {
            throw new MAPException(e.getMessage());
        }

        try {

            ((IMSIImpl) this.imsi).encodeAll(asnOs);

            ((ISDNAddressStringImpl) this.msisdn).encodeAll(asnOs);

            if (this.forwardingInfoForCSE != null) {
                ((ExtForwardingInfoForCSEImpl) this.forwardingInfoForCSE).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_forwardingInfoFor_CSE);
            }

            if (this.callBarringInfoForCSE != null) {
                ((ExtCallBarringInfoForCSEImpl) this.callBarringInfoForCSE).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_callBarringInfoFor_CSE);
            }

            if (this.odbInfo != null) {
                ((ODBInfoImpl) this.odbInfo).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_odbInfo);
            }

            if (this.camelSubscriptionInfo != null) {
                ((CAMELSubscriptionInfoImpl) this.camelSubscriptionInfo).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_camelSubscriptionInfo);
            }

            if (allInformationSent) {
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_allInformationSent);
            }

            if (extensionContainer != null) {
                ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs);
            }


            if (ueReachable != null) {
                ((ServingNodeImpl) this.ueReachable).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_uereachable);
            }

            if (this.csgSubscriptionDataList != null) {
                try {
                    asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_csg_SubscriptionDataList);
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

            if (this.cwData != null) {
                ((CallWaitingDataImpl) this.cwData).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_cwData);
            }

            if (this.chData != null) {
                ((CallHoldDataImpl) this.chData).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_chData);
            }

            if (this.clipData != null) {
                ((ClipDataImpl) this.clipData).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_clipData);
            }

            if (this.clirData != null) {
                ((ClirDataImpl) this.clirData).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_clirData);
            }

            if (this.ectData != null) {
                ((EctDataImpl) this.ectData).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_ectData);
            }
        } catch (IOException e) {
            throw new MAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }

    }

    public MAPMessageType getMessageType() {
        return MAPMessageType.noteSubscriberDataModified_Request;
    }

    public int getOperationCode() {
        return MAPOperationCode.noteSubscriberDataModified;
    }
}
