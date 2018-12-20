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
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.AbstractMAPAsnPrimitive;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.CAMELSubscriptionInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.DCSI;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.GPRSCSI;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.MCSI;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.MGCSI;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.MTsmsCAMELTDPCriteria;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.OBcsmCamelTdpCriteria;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.OCSI;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SMSCSI;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SSCSI;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SpecificCSIWithdraw;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.TBcsmCamelTdpCriteria;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.TCSI;
import org.mobicents.protocols.ss7.map.datacoding.NullEncoderFacility;
import org.mobicents.protocols.ss7.map.datacoding.ObjectEncoderFacility;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.DCSIImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.GPRSCSIImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.MCSIImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.MGCSIImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.MTsmsCAMELTDPCriteriaImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.OBcsmCamelTdpCriteriaImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.OCSIImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.SMSCSIImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.SSCSIImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.SpecificCSIWithdrawImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.TBcsmCamelTdpCriteriaImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.TCSIImpl;

/**
 * @author eva ogallar
 */
public class CAMELSubscriptionInfoImpl extends AbstractMAPAsnPrimitive implements CAMELSubscriptionInfo , MAPAsnPrimitive {

    public static final String _PrimitiveName = "CAMELSubscriptionInfo";

    public static final int _TAG_o_CSI                             = 0;
    public static final int _TAG_o_BcsmCamelTDP_CriteriaList       = 1;
    public static final int _TAG_d_CSI                             = 2;
    public static final int _TAG_t_CSI                             = 3;
    public static final int _TAG_t_BCSM_CAMEL_TDP_CriteriaList     = 4;
    public static final int _TAG_vt_CSI                            = 5;
    public static final int _TAG_vt_BCSM_CAMEL_TDP_CriteriaList    = 6;
    public static final int _TAG_tif_CSI                           = 7;
    public static final int _TAG_tif_CSI_NotificationToCSE         = 8;
    public static final int _TAG_gprs_CSI                          = 9;
    public static final int _TAG_mo_sms_CSI                        = 10;
    public static final int _TAG_ss_CSI                            = 11;
    public static final int _TAG_m_CSI                             = 12;
    public static final int _TAG_extensionContainer                = 13;
    public static final int _TAG_specificCSIDeletedList            = 14;
    public static final int _TAG_mt_sms_CSI                        = 15;
    public static final int _TAG_mt_smsCAMELTDP_CriteriaList       = 16;
    public static final int _TAG_mg_csi                            = 17;
    public static final int _TAG_o_IM_CSI                          = 18;
    public static final int _TAG_o_IM_BcsmCamelTDP_CriteriaList    = 19;
    public static final int _TAG_d_IM_CSI                          = 20;
    public static final int _TAG_vt_IM_CSI                         = 21;
    public static final int _TAG_vt_IM_BCSM_CAMEL_TDP_CriteriaList = 22;

    private OCSI oCsi;
    private ArrayList<OBcsmCamelTdpCriteria> oBcsmCamelTDPCriteriaList;
    private DCSI dCsi;
    private TCSI tCsi;
    private ArrayList<TBcsmCamelTdpCriteria> tBcsmCamelTdpCriteriaList;
    private TCSI vtCsi;
    private ArrayList<TBcsmCamelTdpCriteria> vtBcsmCamelTdpCriteriaList;
    private boolean tifCsi;
    private boolean tifCsiNotificationToCSE;
    private GPRSCSI gprsCsi;
    private SMSCSI moSmsCsi;
    private SSCSI ssCsi;
    private MCSI mCsi;
    private MAPExtensionContainer extensionContainer;
    private SpecificCSIWithdraw specificCSIDeletedList;
    private SMSCSI mtSmsCsi;
    private ArrayList<MTsmsCAMELTDPCriteria> mtSmsCamelTdpCriteriaList;
    private MGCSI mgCsi;
    private OCSI oImCsi;
    private ArrayList<OBcsmCamelTdpCriteria> oImBcsmCamelTdpCriteriaList;
    private DCSI dImCsi;
    private TCSI vtImCsi;
    private ArrayList<TBcsmCamelTdpCriteria> vtImBcsmCamelTdpCriteriaList;


    public CAMELSubscriptionInfoImpl() {
    }

    public CAMELSubscriptionInfoImpl(OCSI oCsi, ArrayList<OBcsmCamelTdpCriteria> oBcsmCamelTDPCriteriaList, DCSI dCsi,
                                     TCSI tCsi, ArrayList<TBcsmCamelTdpCriteria> tBcsmCamelTdpCriteriaList, TCSI vtCsi,
                                     ArrayList<TBcsmCamelTdpCriteria> vtBcsmCamelTdpCriteriaList, boolean tifCsi,
                                     boolean tifCsiNotificationToCSE, GPRSCSI gprsCsi, SMSCSI moSmsCsi, SSCSI ssCsi,
                                     MCSI mCsi, MAPExtensionContainer extensionContainer,
                                     SpecificCSIWithdraw specificCSIDeletedList, SMSCSI mtSmsCsi,
                                     ArrayList<MTsmsCAMELTDPCriteria> mtSmsCamelTdpCriteriaList, MGCSI mgCsi, OCSI oImCsi,
                                     ArrayList<OBcsmCamelTdpCriteria> oImBcsmCamelTdpCriteriaList, DCSI dImCsi, TCSI vtImCsi,
                                     ArrayList<TBcsmCamelTdpCriteria> vtImBcsmCamelTdpCriteriaList) {
        this.oCsi = oCsi;
        this.oBcsmCamelTDPCriteriaList = oBcsmCamelTDPCriteriaList;
        this.dCsi = dCsi;
        this.tCsi = tCsi;
        this.tBcsmCamelTdpCriteriaList = tBcsmCamelTdpCriteriaList;
        this.vtCsi = vtCsi;
        this.vtBcsmCamelTdpCriteriaList = vtBcsmCamelTdpCriteriaList;
        this.tifCsi = tifCsi;
        this.tifCsiNotificationToCSE = tifCsiNotificationToCSE;
        this.gprsCsi = gprsCsi;
        this.moSmsCsi = moSmsCsi;
        this.ssCsi = ssCsi;
        this.mCsi = mCsi;
        this.extensionContainer = extensionContainer;
        this.specificCSIDeletedList = specificCSIDeletedList;
        this.mtSmsCsi = mtSmsCsi;
        this.mtSmsCamelTdpCriteriaList = mtSmsCamelTdpCriteriaList;
        this.mgCsi = mgCsi;
        this.oImCsi = oImCsi;
        this.oImBcsmCamelTdpCriteriaList = oImBcsmCamelTdpCriteriaList;
        this.dImCsi = dImCsi;
        this.vtImCsi = vtImCsi;
        this.vtImBcsmCamelTdpCriteriaList = vtImBcsmCamelTdpCriteriaList;
    }

    public OCSI getOCsi() {
        return oCsi;
    }

    public ArrayList<OBcsmCamelTdpCriteria> getOBcsmCamelTDPCriteriaList() {
        return oBcsmCamelTDPCriteriaList;
    }

    public DCSI getDCsi() {
        return dCsi;
    }

    public TCSI getTCsi() {
        return tCsi;
    }

    public ArrayList<TBcsmCamelTdpCriteria> getTBcsmCamelTdpCriteriaList() {
        return tBcsmCamelTdpCriteriaList;
    }

    @Override
    public TCSI getVtCsi() {
        return vtCsi;
    }

    @Override
    public ArrayList<TBcsmCamelTdpCriteria> getVtBcsmCamelTdpCriteriaList() {
        return vtBcsmCamelTdpCriteriaList;
    }

    @Override
    public boolean getTifCsi() {
        return tifCsi;
    }

    @Override
    public boolean getTifCsiNotificationToCSE() {
        return tifCsiNotificationToCSE;
    }

    @Override
    public GPRSCSI getGprsCsi() {
        return gprsCsi;
    }

    @Override
    public SMSCSI getMoSmsCsi() {
        return moSmsCsi;
    }

    @Override
    public SSCSI getSsCsi() {
        return ssCsi;
    }

    public MCSI getMCsi() {
        return mCsi;
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    @Override
    public SpecificCSIWithdraw getSpecificCSIDeletedList() {
        return specificCSIDeletedList;
    }

    @Override
    public SMSCSI getMtSmsCsi() {
        return mtSmsCsi;
    }

    @Override
    public ArrayList<MTsmsCAMELTDPCriteria> getMtSmsCamelTdpCriteriaList() {
        return mtSmsCamelTdpCriteriaList;
    }

    @Override
    public MGCSI getMgCsi() {
        return mgCsi;
    }

    public OCSI getOImCsi() {
        return oImCsi;
    }

    public ArrayList<OBcsmCamelTdpCriteria> getOImBcsmCamelTdpCriteriaList() {
        return oImBcsmCamelTdpCriteriaList;
    }

    public DCSI getDImCsi() {
        return dImCsi;
    }

    @Override
    public TCSI getVtImCsi() {
        return vtImCsi;
    }

    @Override
    public ArrayList<TBcsmCamelTdpCriteria> getVtImBcsmCamelTdpCriteriaList() {
        return vtImBcsmCamelTdpCriteriaList;
    }

    @Override
    protected void _decode(AsnInputStream ansIS, int length) throws IOException, AsnException, MAPParsingComponentException {

        this.oCsi = null;
        this.oBcsmCamelTDPCriteriaList = null;
        this.dCsi = null;
        this.tCsi = null;
        this.tBcsmCamelTdpCriteriaList = null;
        this.vtCsi = null;
        this.vtBcsmCamelTdpCriteriaList = null;
        this.tifCsi = false;
        this.tifCsiNotificationToCSE = false;
        this.gprsCsi = null;
        this.moSmsCsi = null;
        this.ssCsi = null;
        this.mCsi = null;
        this.extensionContainer = null;

        this.mtSmsCsi = null;
        this.mtSmsCamelTdpCriteriaList = null;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);

        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            switch (ais.getTagClass()) {
                case Tag.CLASS_CONTEXT_SPECIFIC:

                    switch (tag) {
                        case _TAG_o_CSI:
                            oCsi = (OCSI) ObjectEncoderFacility.decodeObject(ais, new OCSIImpl(), "oCsi", getPrimitiveName());
                            break;
                        case _TAG_o_BcsmCamelTDP_CriteriaList:
                            if (ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".oBcsmCamelTDPCriteriaList: is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            AsnInputStream ais2 = ais.readSequenceStream();
                            this.oBcsmCamelTDPCriteriaList = new ArrayList<OBcsmCamelTdpCriteria>();
                            while (true) {
                                if (ais2.available() == 0)
                                    break;

                                int tag2 = ais2.readTag();
                                if (tag2 != Tag.SEQUENCE || ais2.getTagClass() != Tag.CLASS_UNIVERSAL || ais2.isTagPrimitive())
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ": bad oBcsmCamelTDPCriteria tag or tagClass or is primitive ",
                                            MAPParsingComponentExceptionReason.MistypedParameter);

                                this.oBcsmCamelTDPCriteriaList.add((OBcsmCamelTdpCriteria) ObjectEncoderFacility.decodeObject(
                                        ais2, new OBcsmCamelTdpCriteriaImpl(), "OBcsmCamelTdpCriteria", getPrimitiveName()));
                            }

                            if (this.oBcsmCamelTDPCriteriaList.size() < 1 || this.oBcsmCamelTDPCriteriaList.size() > 10) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ": Parameter oBcsmCamelTDPCriteriaList size must be from 1 to 10, found: "
                                        + this.oBcsmCamelTDPCriteriaList.size(),
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            break;
                        case _TAG_d_CSI:
                            dCsi = (DCSI) ObjectEncoderFacility.decodeObject(ais, new DCSIImpl(), "dCsi", getPrimitiveName());
                            break;
                        case _TAG_t_CSI:
                            tCsi = (TCSI) ObjectEncoderFacility.decodeObject(ais, new TCSIImpl(), "tCsi", getPrimitiveName());
                            break;
                        case _TAG_t_BCSM_CAMEL_TDP_CriteriaList:
                            if (ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".tBcsmCamelTdpCriteriaList: is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            AsnInputStream ais3 = ais.readSequenceStream();
                            this.tBcsmCamelTdpCriteriaList = new ArrayList<TBcsmCamelTdpCriteria>();
                            while (true) {
                                if (ais3.available() == 0)
                                    break;

                                int tag2 = ais3.readTag();
                                if (tag2 != Tag.SEQUENCE || ais3.getTagClass() != Tag.CLASS_UNIVERSAL || ais3.isTagPrimitive())
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ": bad tBcsmCamelTdpCriteria tag or tagClass or is primitive ",
                                            MAPParsingComponentExceptionReason.MistypedParameter);

                                TBcsmCamelTdpCriteria elem = new TBcsmCamelTdpCriteriaImpl();
                                ((TBcsmCamelTdpCriteriaImpl) elem).decodeAll(ais3);
                                this.tBcsmCamelTdpCriteriaList.add(elem);
                            }

                            if (this.tBcsmCamelTdpCriteriaList.size() < 1 || this.tBcsmCamelTdpCriteriaList.size() > 10) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ": Parameter tBcsmCamelTdpCriteriaList from 1 to 10, found: "
                                        + this.tBcsmCamelTdpCriteriaList.size(),
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            break;
                        case _TAG_vt_CSI:
                            vtCsi = (TCSI) ObjectEncoderFacility.decodeObject(ais, new TCSIImpl(), "vtCsi", getPrimitiveName());
                            break;
                        case _TAG_vt_BCSM_CAMEL_TDP_CriteriaList:
                            if (ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".vtBcsmCamelTdpCriteriaList: is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            AsnInputStream ais4 = ais.readSequenceStream();
                            this.vtBcsmCamelTdpCriteriaList = new ArrayList<TBcsmCamelTdpCriteria>();
                            while (true) {
                                if (ais4.available() == 0)
                                    break;

                                int tag2 = ais4.readTag();
                                if (tag2 != Tag.SEQUENCE || ais4.getTagClass() != Tag.CLASS_UNIVERSAL || ais4.isTagPrimitive())
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ": bad oBcsmCamelTDPCriteria tag or tagClass or is primitive ",
                                            MAPParsingComponentExceptionReason.MistypedParameter);

                                this.vtBcsmCamelTdpCriteriaList.add((TBcsmCamelTdpCriteria) ObjectEncoderFacility.decodeObject(
                                        ais4, new TBcsmCamelTdpCriteriaImpl(), "vtBcsmCamelTdpCriteriaList", getPrimitiveName()));
                            }

                            if (this.vtBcsmCamelTdpCriteriaList.size() < 1 || this.vtBcsmCamelTdpCriteriaList.size() > 10) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ": Parameter vtBcsmCamelTdpCriteriaList size must be from 1 to 10, found: "
                                        + this.vtBcsmCamelTdpCriteriaList.size(),
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            break;
                        case _TAG_tif_CSI:
                            this.tifCsi = NullEncoderFacility.decode(ais, "tifCsi", getPrimitiveName());
                            break;
                        case _TAG_tif_CSI_NotificationToCSE:
                            this.tifCsiNotificationToCSE = NullEncoderFacility.decode(ais, "tifCsiNotificationToCSE", getPrimitiveName());
                            break;
                        case _TAG_gprs_CSI:
                            this.gprsCsi = (GPRSCSI) ObjectEncoderFacility.decodeObject(ais, new GPRSCSIImpl(), "gprsCsi", getPrimitiveName());
                            break;
                        case _TAG_mo_sms_CSI:
                            this.moSmsCsi = (SMSCSI) ObjectEncoderFacility.decodeObject(ais, new SMSCSIImpl(), "moSmsCsi", getPrimitiveName());
                            break;
                        case _TAG_ss_CSI:
                            this.ssCsi = (SSCSI) ObjectEncoderFacility.decodeObject(ais, new SSCSIImpl(), "ssCsi", getPrimitiveName());
                            break;
                        case _TAG_m_CSI:
                            this.mCsi = (MCSI) ObjectEncoderFacility.decodeObject(ais, new MCSIImpl(), "mCsi", getPrimitiveName());
                            break;
                        case _TAG_extensionContainer:
                            this.extensionContainer = (MAPExtensionContainer) ObjectEncoderFacility.decodeObject(
                                    ais, new MAPExtensionContainerImpl(), "extensionContainer", getPrimitiveName());
                            break;
                        case _TAG_specificCSIDeletedList:
                            this.specificCSIDeletedList = (SpecificCSIWithdraw) ObjectEncoderFacility.decodePrimitiveObject(
                                    ais, new SpecificCSIWithdrawImpl(), "specificCSIDeletedList", getPrimitiveName());
                            break;
                        case _TAG_mt_sms_CSI:
                            this.mtSmsCsi = (SMSCSI) ObjectEncoderFacility.decodeObject(ais, new SMSCSIImpl(), "mtSmsCsi", getPrimitiveName());
                            break;
                        case _TAG_mt_smsCAMELTDP_CriteriaList:
                            if (ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".vtBcsmCamelTdpCriteriaList: is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            AsnInputStream ais5 = ais.readSequenceStream();
                            this.mtSmsCamelTdpCriteriaList = new ArrayList<MTsmsCAMELTDPCriteria>();
                            while (true) {
                                if (ais5.available() == 0)
                                    break;

                                int tag2 = ais5.readTag();
                                if (tag2 != Tag.SEQUENCE || ais5.getTagClass() != Tag.CLASS_UNIVERSAL || ais5.isTagPrimitive())
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ": bad mtSmsCamelTdpCriteria tag or tagClass or is primitive ",
                                            MAPParsingComponentExceptionReason.MistypedParameter);

                                this.mtSmsCamelTdpCriteriaList.add((MTsmsCAMELTDPCriteria) ObjectEncoderFacility.decodeObject(
                                        ais5, new MTsmsCAMELTDPCriteriaImpl(), "mtSmsCamelTdpCriteriaList", getPrimitiveName()));
                            }

                            if (this.mtSmsCamelTdpCriteriaList.size() < 1 || this.mtSmsCamelTdpCriteriaList.size() > 10) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ": Parameter mtSmsCamelTdpCriteriaList size must be from 1 to 10, found: "
                                        + this.mtSmsCamelTdpCriteriaList.size(),
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            break;
                        case _TAG_mg_csi:
                            this.mgCsi = (MGCSI) ObjectEncoderFacility.decodeObject(ais, new MGCSIImpl(), "mgCsi", getPrimitiveName());
                            break;
                        case _TAG_o_IM_CSI:
                            this.oImCsi = (OCSI) ObjectEncoderFacility.decodeObject(ais, new OCSIImpl(), "oImCsi", getPrimitiveName());
                            break;
                        case _TAG_o_IM_BcsmCamelTDP_CriteriaList:
                            if (ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".oImBcsmCamelTdpCriteriaList: is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            AsnInputStream ais6 = ais.readSequenceStream();
                            this.oImBcsmCamelTdpCriteriaList = new ArrayList<OBcsmCamelTdpCriteria>();
                            while (true) {
                                if (ais6.available() == 0)
                                    break;

                                int tag2 = ais6.readTag();
                                if (tag2 != Tag.SEQUENCE || ais6.getTagClass() != Tag.CLASS_UNIVERSAL || ais6.isTagPrimitive())
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ": bad oImBcsmCamelTdpCriteria tag or tagClass or is primitive ",
                                            MAPParsingComponentExceptionReason.MistypedParameter);

                                this.oImBcsmCamelTdpCriteriaList.add((OBcsmCamelTdpCriteria) ObjectEncoderFacility.decodeObject(
                                        ais6, new OBcsmCamelTdpCriteriaImpl(), "OBcsmCamelTdpCriteria", getPrimitiveName()));
                            }

                            if (this.oImBcsmCamelTdpCriteriaList.size() < 1 || this.oImBcsmCamelTdpCriteriaList.size() > 10) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ": Parameter oImBcsmCamelTdpCriteriaList size must be from 1 to 10, found: "
                                        + this.oImBcsmCamelTdpCriteriaList.size(),
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            break;
                        case _TAG_d_IM_CSI:
                            this.dImCsi = (DCSI) ObjectEncoderFacility.decodeObject(ais, new DCSIImpl(), "dImCsi", getPrimitiveName());
                            break;
                        case _TAG_vt_IM_CSI:
                            this.vtImCsi = (TCSI) ObjectEncoderFacility.decodeObject(ais, new TCSIImpl(), "vtImCsi", getPrimitiveName());
                            break;
                        case _TAG_vt_IM_BCSM_CAMEL_TDP_CriteriaList:
                            if (ais.isTagPrimitive())
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ".oImBcsmCamelTdpCriteriaList: is primitive",
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            AsnInputStream ais7 = ais.readSequenceStream();
                            this.vtImBcsmCamelTdpCriteriaList = new ArrayList<TBcsmCamelTdpCriteria>();
                            while (true) {
                                if (ais7.available() == 0)
                                    break;

                                int tag2 = ais7.readTag();
                                if (tag2 != Tag.SEQUENCE || ais7.getTagClass() != Tag.CLASS_UNIVERSAL || ais7.isTagPrimitive())
                                    throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ": bad oImBcsmCamelTdpCriteria tag or tagClass or is primitive ",
                                            MAPParsingComponentExceptionReason.MistypedParameter);

                                this.vtImBcsmCamelTdpCriteriaList.add((TBcsmCamelTdpCriteria) ObjectEncoderFacility.decodeObject(
                                        ais7, new TBcsmCamelTdpCriteriaImpl(), "TBcsmCamelTdpCriteria", getPrimitiveName()));
                            }

                            if (this.vtImBcsmCamelTdpCriteriaList.size() < 1 || this.vtImBcsmCamelTdpCriteriaList.size() > 10) {
                                throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
                                        + ": Parameter oImBcsmCamelTdpCriteriaList size must be from 1 to 10, found: "
                                        + this.vtImBcsmCamelTdpCriteriaList.size(),
                                        MAPParsingComponentExceptionReason.MistypedParameter);
                            }
                            break;
                        default:
                            ais.advanceElement();
                            break;
                    }
                    break;

                default:
                    ais.advanceElement();
                    break;
            }

        }

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
    public void encodeData(AsnOutputStream asnOs) throws MAPException {
/*
    private OCSI oCsi;
    private ArrayList<OBcsmCamelTdpCriteria> oBcsmCamelTDPCriteriaList;
    private DCSI dCsi;
    private TCSI tCsi;
    private ArrayList<TBcsmCamelTdpCriteria> tBcsmCamelTdpCriteriaList;
    private TCSI vtCsi;
    private ArrayList<TBcsmCamelTdpCriteria> vtBcsmCamelTdpCriteriaList;
    private boolean tifCsi;
    private boolean tifCsiNotificationToCSE;
    private GPRSCSI gprsCsi;
    private SMSCSI moSmsCsi;
    private SSCSI ssCsi;
    private MCSI mCsi;
    private MAPExtensionContainer extensionContainer;
    private SpecificCSIWithdraw specificCSIDeletedList;
    private SMSCSI mtSmsCsi;
    private ArrayList<MTsmsCAMELTDPCriteria> mtSmsCamelTdpCriteriaList;
    private MGCSI mgCsi;
    private OCSI oImCsi;
    private ArrayList<OBcsmCamelTdpCriteria> oImBcsmCamelTdpCriteriaList;
    private DCSI dImCsi;
    private TCSI vtImCsi;
    private ArrayList<TBcsmCamelTdpCriteria> vtImBcsmCamelTdpCriteriaList;
 */

        if (this.oBcsmCamelTDPCriteriaList != null
                && (this.oBcsmCamelTDPCriteriaList.size() < 1 || this.oBcsmCamelTDPCriteriaList.size() > 10)) {
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + ": Parameter oBcsmCamelTDPCriteriaList size must be from 1 to 10, found: "
                    + this.oBcsmCamelTDPCriteriaList.size());
        }

        if (this.mtSmsCamelTdpCriteriaList != null
                && (this.mtSmsCamelTdpCriteriaList.size() < 1 || this.mtSmsCamelTdpCriteriaList.size() > 10)) {
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + ": Parameter mtSmsCamelTdpCriteriaList size must be from 1 to 10, found: "
                    + this.mtSmsCamelTdpCriteriaList.size());
        }

        if (this.tBcsmCamelTdpCriteriaList != null
                && (this.tBcsmCamelTdpCriteriaList.size() < 1 || this.tBcsmCamelTdpCriteriaList.size() > 10)) {
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + ": Parameter tBcsmCamelTdpCriteriaList size must be from 1 to 10, found: "
                    + this.tBcsmCamelTdpCriteriaList.size());
        }

        if (this.vtBcsmCamelTdpCriteriaList != null
                && (this.vtBcsmCamelTdpCriteriaList.size() < 1 || this.vtBcsmCamelTdpCriteriaList.size() > 10)) {
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + ": Parameter vtBcsmCamelTdpCriteriaList size must be from 1 to 10, found: "
                    + this.vtBcsmCamelTdpCriteriaList.size());
        }

        if (this.oImBcsmCamelTdpCriteriaList != null
                && (this.oImBcsmCamelTdpCriteriaList.size() < 1 || this.oImBcsmCamelTdpCriteriaList.size() > 10)) {
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + ": Parameter oImBcsmCamelTdpCriteriaList size must be from 1 to 10, found: "
                    + this.oImBcsmCamelTdpCriteriaList.size());
        }

        if (this.vtImBcsmCamelTdpCriteriaList != null
                && (this.vtImBcsmCamelTdpCriteriaList.size() < 1 || this.vtImBcsmCamelTdpCriteriaList.size() > 10)) {
            throw new MAPException("Error while encoding " + _PrimitiveName
                    + ": Parameter vtImBcsmCamelTdpCriteriaList size must be from 1 to 10, found: "
                    + this.vtImBcsmCamelTdpCriteriaList.size());
        }

        try {
            if (this.oCsi != null) {
                ((OCSIImpl) this.oCsi).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_o_CSI);
            }
            if (this.oBcsmCamelTDPCriteriaList != null) {
                asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_o_BcsmCamelTDP_CriteriaList);
                int pos = asnOs.StartContentDefiniteLength();
                for (OBcsmCamelTdpCriteria oBcsmCamelTdpCriteria : this.oBcsmCamelTDPCriteriaList) {
                    ((OBcsmCamelTdpCriteriaImpl) oBcsmCamelTdpCriteria).encodeAll(asnOs);
                }
                asnOs.FinalizeContent(pos);
            }
            if (this.dCsi != null) {
                ((DCSIImpl) this.dCsi).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_d_CSI);
            }
            if (this.tCsi != null) {
                ((TCSIImpl) this.tCsi).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_t_CSI);
            }
            if (this.tBcsmCamelTdpCriteriaList != null) {
                asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_t_BCSM_CAMEL_TDP_CriteriaList);
                int pos = asnOs.StartContentDefiniteLength();
                for (TBcsmCamelTdpCriteria tbcsmCamelTdpCriteria : this.tBcsmCamelTdpCriteriaList) {
                    ((TBcsmCamelTdpCriteriaImpl) tbcsmCamelTdpCriteria).encodeAll(asnOs);
                }
                asnOs.FinalizeContent(pos);
            }
            if (this.vtCsi != null) {
                ((TCSIImpl) this.vtCsi).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_vt_CSI);
            }
            if (this.vtBcsmCamelTdpCriteriaList != null) {
                asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_vt_BCSM_CAMEL_TDP_CriteriaList);
                int pos = asnOs.StartContentDefiniteLength();
                for (TBcsmCamelTdpCriteria tbcsmCamelTdpCriteria : this.vtBcsmCamelTdpCriteriaList) {
                    ((TBcsmCamelTdpCriteriaImpl) tbcsmCamelTdpCriteria).encodeAll(asnOs);
                }
                asnOs.FinalizeContent(pos);
            }
            if (this.tifCsi) {
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_tif_CSI);
            }
            if (this.tifCsiNotificationToCSE) {
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_tif_CSI_NotificationToCSE);
            }
            if (this.gprsCsi != null) {
                ((GPRSCSIImpl) this.gprsCsi).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_gprs_CSI);
            }
            if (this.moSmsCsi != null) {
                ((SMSCSIImpl) this.moSmsCsi).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_mo_sms_CSI);
            }
            if (this.ssCsi != null) {
                ((SSCSIImpl) this.ssCsi).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_ss_CSI);
            }
            if (this.mCsi != null) {
                ((MCSIImpl) this.mCsi).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_m_CSI);
            }
            if (this.extensionContainer != null) {
                ((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _TAG_extensionContainer);
            }
            if (this.specificCSIDeletedList != null) {
                ((SpecificCSIWithdrawImpl) this.specificCSIDeletedList).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _TAG_specificCSIDeletedList);
            }
            if (this.mtSmsCsi != null) {
                ((SMSCSIImpl) this.mtSmsCsi).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_mt_sms_CSI);
            }
            if (this.mtSmsCamelTdpCriteriaList != null) {
                asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_mt_smsCAMELTDP_CriteriaList);
                int pos = asnOs.StartContentDefiniteLength();
                for (MTsmsCAMELTDPCriteria mtSMSCAMELTDPCriteria : this.mtSmsCamelTdpCriteriaList) {
                    ((MTsmsCAMELTDPCriteriaImpl) mtSMSCAMELTDPCriteria).encodeAll(asnOs);
                }
                asnOs.FinalizeContent(pos);
            }
            if (this.mgCsi != null) {
                ((MGCSIImpl) this.mgCsi).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_mg_csi);
            }
            if (this.oImCsi != null) {
                ((OCSIImpl) this.oImCsi).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_o_IM_CSI);
            }
            if (this.oImBcsmCamelTdpCriteriaList != null) {
                asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_o_IM_BcsmCamelTDP_CriteriaList);
                int pos = asnOs.StartContentDefiniteLength();
                for (OBcsmCamelTdpCriteria oBcsmCamelTdpCriteria : this.oImBcsmCamelTdpCriteriaList) {
                    ((OBcsmCamelTdpCriteriaImpl) oBcsmCamelTdpCriteria).encodeAll(asnOs);
                }
                asnOs.FinalizeContent(pos);
            }
            if (this.dImCsi != null) {
                ((DCSIImpl) this.dImCsi).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_d_IM_CSI);
            }
            if (this.vtImCsi != null) {
                ((TCSIImpl) this.vtImCsi).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_vt_IM_CSI);
            }
            if (this.vtImBcsmCamelTdpCriteriaList != null) {
                asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG_vt_IM_BCSM_CAMEL_TDP_CriteriaList);
                int pos = asnOs.StartContentDefiniteLength();
                for (TBcsmCamelTdpCriteria tBcsmCamelTdpCriteria : this.vtImBcsmCamelTdpCriteriaList) {
                    ((OBcsmCamelTdpCriteriaImpl) tBcsmCamelTdpCriteria).encodeAll(asnOs);
                }
                asnOs.FinalizeContent(pos);
            }
        } catch (IOException e) {
            throw new MAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new MAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    protected String getPrimitiveName() {
        return _PrimitiveName;
    }

    @Override
    public boolean getIsPrimitive() {
        return false;
    }
}
