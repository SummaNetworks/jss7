/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and/or its affiliates, and individual
 * contributors as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */

package org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation;

import java.util.ArrayList;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.BitSetStrictLength;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.primitives.Time;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.CAMELSubscriptionInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.CallHoldData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.CallWaitingData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.ClipData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.ClirData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.EctData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.ExtCwFeature;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.ODBInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.APN;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.BearerServiceCodeValue;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.CSGId;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.CSGSubscriptionData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtCallBarringFeature;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtForwFeature;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtForwOptionsForwardingReason;
import org.mobicents.protocols.ss7.map.api.service.supplementary.CliRestrictionOption;
import org.mobicents.protocols.ss7.map.api.service.supplementary.OverrideCategory;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SupplementaryCodeValue;
import org.mobicents.protocols.ss7.map.primitives.FTNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNSubaddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.TimeImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.APNImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.CSGIdImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.CSGSubscriptionDataImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtBasicServiceCodeImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtBearerServiceCodeImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtCallBarringFeatureImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtCwFeatureImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtForwFeatureImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtForwOptionsImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtSSStatusImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ODBDataImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ODBGeneralDataImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ODBHPLMNDataImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.PasswordImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.SSCodeImpl;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

/**
 * @author abhayani
 *
 */
public class NoteSubscriberDataModifiedRequestTest {

    // Real Trace
    byte[] data = new byte[] {
            48, 98, -96, 26, -95, 24, -128, 1, 69, -95, 8, 48, 6, -126, 1, 48, -124, 1, 15, 18, 4, 49, 50, 49, 50, 2, 1,
            12, 5, 0, -93, 15, 48, 11, 3, 5, 3, -1, -1, -1, -8, 3, 2, 4, -16, 5, 0, -95, 0, -119, 5, -111, -104, -104,
            -104, -104, -92, 8, 48, 6, -126, 1, 48, -126, 1, 15, -91, 5, -127, 1, 15, -126, 0, -90, 8, -127, 1, 15, -126,
            1, 1, -125, 0, -89, 8, -127, 1, 15, 2, 1, 2, -125, 0, -88, 5, -127, 1, 15, -126, 0};

    public byte[] getTimeData() {
        return new byte[] { 10, 22, 41, 34 };
    };

    public byte[] getAPNData() {
        return new byte[] { 6, 7 };
    };


    @Test(groups = { "functional.decode", "NoteSubscriberDataModifiedRequest" })
    public void testEncodeDecode() throws Exception {

        SSCodeImpl ssCode = new SSCodeImpl(SupplementaryCodeValue.mc);
        ExtBasicServiceCodeImpl basicService = new ExtBasicServiceCodeImpl(
                new ExtBearerServiceCodeImpl(BearerServiceCodeValue.allAlternateSpeech_DataCDA));
        ExtSSStatusImpl ssStatus = new ExtSSStatusImpl(true, true, true, true);
        new ExtForwFeatureImpl(
                basicService,
                ssStatus,
                new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "22556326543"),
                null, new ExtForwOptionsImpl(true, true, true, ExtForwOptionsForwardingReason.unconditional),
                25, null, null);

        ArrayList<ExtCwFeature> cwFeatureList = new ArrayList<ExtCwFeature>();
        cwFeatureList.add(new ExtCwFeatureImpl(new ExtBasicServiceCodeImpl(
                new ExtBearerServiceCodeImpl(BearerServiceCodeValue.allAlternateSpeech_DataCDA)),
                new ExtSSStatusImpl(true, true, true, true)));
        CallWaitingData callWaitingData = new CallWaitingDataImpl(cwFeatureList, false);
        CallHoldData callHoldData = new CallHoldDataImpl(true, new ExtSSStatusImpl(true, true, true, true));
        ClipData clipData = new ClipDataImpl(new ExtSSStatusImpl(true, true, true, true), OverrideCategory.overrideDisabled, true);
        ClirData clirData = new ClirDataImpl(true, CliRestrictionOption.permanent, new ExtSSStatusImpl(true, true, true, true));
        EctData ectData = new EctDataImpl(true, new ExtSSStatusImpl(true, true, true, true));

        ODBInfo odbInfo = new ODBInfoImpl(new ODBDataImpl(
                new ODBGeneralDataImpl(true, true, true, true, true, true, true, true, true, true,
                        true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true),
                new ODBHPLMNDataImpl(true, true, true, true), null), true, null);
        CAMELSubscriptionInfo camelSubscriptionInfo = new CAMELSubscriptionInfoImpl(null,
                null, null, null, null, null, null, false, false, null, null, null, null, null, null, null, null, null, null,
                null, null, null, null);


        IMSIImpl imsi = new IMSIImpl("989898989898");
        ISDNAddressStringImpl msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "435435345345");

        ArrayList<ExtForwFeature> forwardingFeatureList = new ArrayList<ExtForwFeature>();
        forwardingFeatureList.add(new ExtForwFeatureImpl(basicService, ssStatus,
                new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "222222"),
                new ISDNSubaddressStringImpl(new byte[]{0x23}), new ExtForwOptionsImpl(true, true, true, ExtForwOptionsForwardingReason.unconditional),
                10, null, new FTNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "543534")));
        ExtForwardingInfoForCSEImpl extForwardingInfoForCSE = new ExtForwardingInfoForCSEImpl(ssCode, forwardingFeatureList, true, null);

        ArrayList<ExtCallBarringFeature> callBarringFeatureList = new ArrayList<ExtCallBarringFeature>();
        callBarringFeatureList.add(new ExtCallBarringFeatureImpl(
                new ExtBasicServiceCodeImpl(
                        new ExtBearerServiceCodeImpl(BearerServiceCodeValue.allAlternateSpeech_DataCDA)),
                new ExtSSStatusImpl(true, true, true, true), null));

        ExtCallBarringInfoForCSEImpl extCallBarringInfoForCSE = new ExtCallBarringInfoForCSEImpl(ssCode,
                callBarringFeatureList, new PasswordImpl("1212"), 12, true, null);

        BitSetStrictLength bs = new BitSetStrictLength(27);
        bs.set(0);
        bs.set(26);
        CSGId csgId = new CSGIdImpl(bs);
        Time expirationDate = new TimeImpl(this.getTimeData());
        ArrayList<APN> lipaAllowedAPNList = new ArrayList<APN>();
        APN apn = new APNImpl(this.getAPNData());
        lipaAllowedAPNList.add(apn);

        CSGSubscriptionDataImpl csgSubscriptionData = new CSGSubscriptionDataImpl(csgId, expirationDate, null, lipaAllowedAPNList);

        ArrayList<CSGSubscriptionData> csgSubscriptionDataList = new ArrayList<CSGSubscriptionData>();
        csgSubscriptionDataList.add(csgSubscriptionData);
        NoteSubscriberDataModifiedRequestImpl anyTimeInt = new NoteSubscriberDataModifiedRequestImpl(
                imsi, msisdn,
                extForwardingInfoForCSE, extCallBarringInfoForCSE, odbInfo, camelSubscriptionInfo,
                true, null, new ServingNodeImpl(true, true), csgSubscriptionDataList, callWaitingData,
                callHoldData, clipData, clirData, ectData);

        AsnOutputStream asnOS = new AsnOutputStream();
        anyTimeInt.encodeAll(asnOS);
        byte[] encodedData = asnOS.toByteArray();

        AsnInputStream ansIS = new AsnInputStream(encodedData);
        int tag = ansIS.readTag();
        assertEquals(tag, Tag.SEQUENCE);

        NoteSubscriberDataModifiedRequestImpl ind = new NoteSubscriberDataModifiedRequestImpl();
        ind.decodeAll(ansIS);

        assertNotNull(ind.getImsi());
        assertNotNull(ind.getMsisdn());
        assertNotNull(ind.getForwardingInfoForCSE());
        assertNotNull(ind.getCallBarringInfoForCSE());
        assertNotNull(ind.getOdbInfo());
        assertNotNull(ind.getCamelSubscriptionInfo());
        assertTrue(ind.getAllInformationSent());
        assertNull(ind.getExtensionContainer());
        assertNotNull(ind.getUeReachable());
        assertTrue(ind.getUeReachable().getSgsn());
        assertTrue(ind.getUeReachable().getMme());
        assertTrue(ind.getCsgSubscriptionDataList().size()>0);
        assertNotNull(ind.getCwData());
        assertNotNull(ind.getChData());
        assertNotNull(ind.getClipData());
        assertNotNull(ind.getClirData());
        assertNotNull(ind.getEctData());

    }

}
