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
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.CAMELSubscriptionInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.CallHoldData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.CallWaitingData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.ClipData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.ClirData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.EctData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.ExtCwFeature;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.ODBInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.BearerServiceCodeValue;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtCallBarringFeature;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtForwFeature;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtForwOptionsForwardingReason;
import org.mobicents.protocols.ss7.map.api.service.supplementary.CliRestrictionOption;
import org.mobicents.protocols.ss7.map.api.service.supplementary.OverrideCategory;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SupplementaryCodeValue;
import org.mobicents.protocols.ss7.map.primitives.AddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.FTNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNSubaddressStringImpl;
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
public class AnyTimeModificationResponseTest {

    // Real Trace
    byte[] data = new byte[] {
            48, 98, -96, 26, -95, 24, -128, 1, 69, -95, 8, 48, 6, -126, 1, 48, -124, 1, 15, 18, 4, 49, 50, 49, 50, 2, 1,
            12, 5, 0, -93, 15, 48, 11, 3, 5, 3, -1, -1, -1, -8, 3, 2, 4, -16, 5, 0, -95, 0, -119, 5, -111, -104, -104,
            -104, -104, -92, 8, 48, 6, -126, 1, 48, -126, 1, 15, -91, 5, -127, 1, 15, -126, 0, -90, 8, -127, 1, 15, -126,
            1, 1, -125, 0, -89, 8, -127, 1, 15, 2, 1, 2, -125, 0, -88, 5, -127, 1, 15, -126, 0};

    @Test(groups = { "functional.decode", "AnyTimeModificationResponse" })
    public void testDecode() throws Exception {

        AsnInputStream ansIS = new AsnInputStream(data);
        int tag = ansIS.readTag();
        assertEquals(tag, Tag.SEQUENCE);

        AnyTimeModificationResponseImpl anyTimeMod = new AnyTimeModificationResponseImpl();
        anyTimeMod.decodeAll(ansIS);

        assertNotNull(anyTimeMod.getSsInfoForCSE());
        assertNotNull(anyTimeMod.getCamelSubscriptionInfo());
        assertNull(anyTimeMod.getExtensionContainer());
        assertNotNull(anyTimeMod.getOdbInfo());
        assertNotNull(anyTimeMod.getCwData());
        assertNotNull(anyTimeMod.getChData());
        assertNotNull(anyTimeMod.getClipData());
        assertNotNull(anyTimeMod.getClirData());
        assertNotNull(anyTimeMod.getEctData());
        assertNotNull(anyTimeMod.getServiceCentreAddress());
        assertNull(anyTimeMod.getExtensionContainer());
    }

    @Test(groups = { "functional.decode", "AnyTimeModificationResponse" })
    public void testEncode() throws Exception {

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

        AddressStringImpl addressString = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "89898989");

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

        ArrayList<ExtCallBarringFeature> callBarringFeatureList = new ArrayList<ExtCallBarringFeature>();
        callBarringFeatureList.add(new ExtCallBarringFeatureImpl(
                new ExtBasicServiceCodeImpl(
                        new ExtBearerServiceCodeImpl(BearerServiceCodeValue.allAlternateSpeech_DataCDA)),
                new ExtSSStatusImpl(true, true, true, true), null));

        ExtSSInfoForCSEImpl ssInfoForCSE = new ExtSSInfoForCSEImpl(new ExtCallBarringInfoForCSEImpl(ssCode,
                callBarringFeatureList, new PasswordImpl("1212"), 12, true, null));

        AnyTimeModificationResponseImpl anyTimeInt = new AnyTimeModificationResponseImpl(
                ssInfoForCSE, camelSubscriptionInfo, null, odbInfo, callWaitingData, callHoldData, clipData,
                clirData, ectData, addressString);

        AsnOutputStream asnOS = new AsnOutputStream();
        anyTimeInt.encodeAll(asnOS);
        byte[] encodedData = asnOS.toByteArray();
        assertTrue(Arrays.equals(data, encodedData));
    }


    @Test(groups = { "functional.decode", "AnyTimeModificationResponse" })
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

        AddressStringImpl addressString = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "89898989");

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

        ArrayList<ExtCallBarringFeature> callBarringFeatureList = new ArrayList<ExtCallBarringFeature>();
        callBarringFeatureList.add(new ExtCallBarringFeatureImpl(
                new ExtBasicServiceCodeImpl(
                        new ExtBearerServiceCodeImpl(BearerServiceCodeValue.allAlternateSpeech_DataCDA)),
                new ExtSSStatusImpl(true, true, true, true), null));

        ExtSSInfoForCSEImpl ssInfoForCSE = new ExtSSInfoForCSEImpl(new ExtCallBarringInfoForCSEImpl(ssCode,
                callBarringFeatureList, new PasswordImpl("1212"), 12, true, null));

        AnyTimeModificationResponseImpl anyTimeInt = new AnyTimeModificationResponseImpl(
                ssInfoForCSE, camelSubscriptionInfo, null, odbInfo, callWaitingData, callHoldData, clipData,
                clirData, ectData, addressString);

        AsnOutputStream asnOS = new AsnOutputStream();
        anyTimeInt.encodeAll(asnOS);
        byte[] encodedData = asnOS.toByteArray();

        AsnInputStream ansIS = new AsnInputStream(encodedData);
        int tag = ansIS.readTag();
        assertEquals(tag, Tag.SEQUENCE);

        AnyTimeModificationResponseImpl anyTimeMod = new AnyTimeModificationResponseImpl();
        anyTimeMod.decodeAll(ansIS);

        assertNotNull(anyTimeMod.getCamelSubscriptionInfo());
        assertNull(anyTimeMod.getExtensionContainer());
        assertNotNull(anyTimeMod.getOdbInfo());
        assertNotNull(anyTimeMod.getCwData());
        assertNotNull(anyTimeMod.getChData());
        assertNotNull(anyTimeMod.getClipData());
        assertNotNull(anyTimeMod.getClirData());
        assertNotNull(anyTimeMod.getEctData());
        assertNotNull(anyTimeMod.getServiceCentreAddress());
        assertNull(anyTimeMod.getExtensionContainer());

        ArrayList<ExtForwFeature> forwardingFeatureList = new ArrayList<ExtForwFeature>();
        forwardingFeatureList.add(new ExtForwFeatureImpl(basicService, ssStatus,
                new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "222222"),
                new ISDNSubaddressStringImpl(new byte[]{0x23}), new ExtForwOptionsImpl(true, true, true, ExtForwOptionsForwardingReason.unconditional),
                10, null, new FTNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "543534")));
        ssInfoForCSE = new ExtSSInfoForCSEImpl(new ExtForwardingInfoForCSEImpl(ssCode,
                forwardingFeatureList, true, null));

        anyTimeInt = new AnyTimeModificationResponseImpl(
                ssInfoForCSE, camelSubscriptionInfo, null, odbInfo, callWaitingData, callHoldData, clipData,
                clirData, ectData, addressString);

        asnOS = new AsnOutputStream();
        anyTimeInt.encodeAll(asnOS);
        encodedData = asnOS.toByteArray();

        ansIS = new AsnInputStream(encodedData);
        tag = ansIS.readTag();
        assertEquals(tag, Tag.SEQUENCE);

        anyTimeMod = new AnyTimeModificationResponseImpl();
        anyTimeMod.decodeAll(ansIS);

        assertNotNull(anyTimeMod.getCamelSubscriptionInfo());
        assertNull(anyTimeMod.getExtensionContainer());
        assertNotNull(anyTimeMod.getOdbInfo());
        assertNotNull(anyTimeMod.getCwData());
        assertNotNull(anyTimeMod.getChData());
        assertNotNull(anyTimeMod.getClipData());
        assertNotNull(anyTimeMod.getClirData());
        assertNotNull(anyTimeMod.getEctData());
        assertNotNull(anyTimeMod.getServiceCentreAddress());
        assertNull(anyTimeMod.getExtensionContainer());
    }

}
