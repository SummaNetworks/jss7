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
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.CallBarringData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.CallForwardingData;
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
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.OfferedCamel4CSIs;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SupportedCamelPhases;
import org.mobicents.protocols.ss7.map.api.service.supplementary.CliRestrictionOption;
import org.mobicents.protocols.ss7.map.api.service.supplementary.OverrideCategory;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
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
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.OfferedCamel4CSIsImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.SupportedCamelPhasesImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.PasswordImpl;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * @author eva ogallar
 *
 */
public class AnyTimeSubscriptionInterrogationResponseTest {

    // Real Trace
    byte[] data = new byte[] {
        48, 127, -95, 25, 48, 21, -126, 1, 48, -124, 1, 15, -123, 7, -111, 34, 85, 54, 98, 69, -13, -122, 1, -20, -121,
            1, 25, 5, 0, -94, 19, 48, 6, -126, 1, 48, -124, 1, 15, 18, 4, 49, 50, 51, 52, 2, 1, 3, 5, 0, -93, 15, 48,
            11, 3, 5, 3, -1, -1, -1, -8, 3, 2, 4, -16, 5, 0, -92, 0, -123, 2, 4, -16, -122, 2, 4, -16, -120, 2, 1, -2,
            -119, 2, 1, -2, -84, 8, 48, 6, -126, 1, 48, -126, 1, 15, -83, 5, -127, 1, 15, -126, 0, -82, 8, -127, 1, 15,
            -126, 1, 1, -125, 0, -81, 8, -127, 1, 15, 2, 1, 2, -125, 0, -80, 5, -127, 1, 15, -126, 0};

    @Test(groups = { "functional.decode", "anyTimeSubscriptionInterrogationResponse" })
    public void testDecode() throws Exception {

        AsnInputStream ansIS = new AsnInputStream(data);
        int tag = ansIS.readTag();
        assertEquals(tag, Tag.SEQUENCE);

        AnyTimeSubscriptionInterrogationResponseImpl ind = new AnyTimeSubscriptionInterrogationResponseImpl();
        ind.decodeAll(ansIS);

        Assert.assertNotNull(ind.getCallForwardingData());
        Assert.assertNotNull(ind.getCallBarringData());
        Assert.assertNotNull(ind.getOdbInfo());
        Assert.assertNotNull(ind.getCamelSubscriptionInfo());
        Assert.assertNotNull(ind.getSupportedSgsnCamelPhases());
        Assert.assertNotNull(ind.getSupportedVlrCamelPhases());
        Assert.assertNull(ind.getExtensionContainer());
        Assert.assertNotNull(ind.getOfferedCamel4CSIsInVlr());
        Assert.assertNotNull(ind.getOfferedCamel4CSIsInSgsn());
        Assert.assertNull(ind.getMsisdnBsList());
        Assert.assertNull(ind.getCsgSubscriptionDataList());
        Assert.assertNotNull(ind.getCwData());
        Assert.assertNotNull(ind.getChData());
        Assert.assertNotNull(ind.getClipData());
        Assert.assertNotNull(ind.getClirData());
        Assert.assertNotNull(ind.getEctData());
    }

    @Test(groups = { "functional.decode", "subscriberInformation" })
    public void testEncode() throws Exception {

        ArrayList<ExtForwFeature> forwardingFeatureList = new ArrayList<ExtForwFeature>();
        forwardingFeatureList.add(new ExtForwFeatureImpl(
                new ExtBasicServiceCodeImpl(
                        new ExtBearerServiceCodeImpl(BearerServiceCodeValue.allAlternateSpeech_DataCDA)),
                new ExtSSStatusImpl(true, true, true, true),
                new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "22556326543"),
                null, new ExtForwOptionsImpl(true, true, true, ExtForwOptionsForwardingReason.unconditional),
                25, null, null));

        CallForwardingData callForwardingData = new CallForwardingDataImpl(forwardingFeatureList, true, null);

        ArrayList<ExtCallBarringFeature> callBarringFeatureList = new ArrayList<ExtCallBarringFeature>();
        callBarringFeatureList.add(new ExtCallBarringFeatureImpl(
                new ExtBasicServiceCodeImpl(
                        new ExtBearerServiceCodeImpl(BearerServiceCodeValue.allAlternateSpeech_DataCDA)),
                new ExtSSStatusImpl(true, true, true, true), null));
        CallBarringData callBarringData = new CallBarringDataImpl(callBarringFeatureList,
                new PasswordImpl("1234"), 3, true, null);

        ODBInfo odbInfo = new ODBInfoImpl(new ODBDataImpl(
                new ODBGeneralDataImpl(true, true, true, true, true, true, true, true, true, true,
                        true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true),
                new ODBHPLMNDataImpl(true, true, true, true), null), true, null);
        CAMELSubscriptionInfo camelSubscriptionInfo = new CAMELSubscriptionInfoImpl(null,
                null, null, null, null, null, null, false, false, null, null, null, null, null, null, null, null, null, null,
                null, null, null, null);

        SupportedCamelPhases supportedCamelPhases = new SupportedCamelPhasesImpl(true, true, true, true);
        SupportedCamelPhases supportedCamelPhases1 = new SupportedCamelPhasesImpl(true, true, true, true);
        OfferedCamel4CSIs offeredCamel4CSIs = new OfferedCamel4CSIsImpl(true, true, true, true, true, true, true);
        OfferedCamel4CSIs offeredCamel4CSIs1 = new OfferedCamel4CSIsImpl(true, true, true, true, true, true, true);
        ArrayList<ExtCwFeature> cwFeatureList = new ArrayList<ExtCwFeature>();
        cwFeatureList.add(new ExtCwFeatureImpl(new ExtBasicServiceCodeImpl(
                new ExtBearerServiceCodeImpl(BearerServiceCodeValue.allAlternateSpeech_DataCDA)),
                new ExtSSStatusImpl(true, true, true, true)));
        CallWaitingData callWaitingData = new CallWaitingDataImpl(cwFeatureList, false);
        CallHoldData callHoldData = new CallHoldDataImpl(true, new ExtSSStatusImpl(true, true, true, true));
        ClipData clipData = new ClipDataImpl(new ExtSSStatusImpl(true, true, true, true), OverrideCategory.overrideDisabled, true);
        ClirData clirData = new ClirDataImpl(true, CliRestrictionOption.permanent, new ExtSSStatusImpl(true, true, true, true));
        EctData ectData = new EctDataImpl(true, new ExtSSStatusImpl(true, true, true, true));

        AnyTimeSubscriptionInterrogationResponseImpl anyTimeInt = new AnyTimeSubscriptionInterrogationResponseImpl(
                callForwardingData,
                callBarringData, odbInfo,
                camelSubscriptionInfo, supportedCamelPhases,
                supportedCamelPhases1, null,
                offeredCamel4CSIs,
                offeredCamel4CSIs1,
                null, null,
                callWaitingData, callHoldData,
                clipData, clirData,
                ectData);

        AsnOutputStream asnOS = new AsnOutputStream();
        anyTimeInt.encodeAll(asnOS);
        byte[] encodedData = asnOS.toByteArray();
        assertTrue(Arrays.equals(data, encodedData));
    }

}
