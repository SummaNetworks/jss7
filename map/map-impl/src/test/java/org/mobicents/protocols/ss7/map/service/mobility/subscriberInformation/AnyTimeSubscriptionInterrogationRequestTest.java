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

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.primitives.SubscriberIdentity;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.AdditionalRequestedCAMELSubscriptionInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedCAMELSubscriptionInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedSubscriptionInfo;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SSCode;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SSForBSCode;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SupplementaryCodeValue;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.SubscriberIdentityImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.SSCodeImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.SSForBSCodeImpl;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * @author eva ogallar
 *
 */
public class AnyTimeSubscriptionInterrogationRequestTest {

    // Real Trace
    byte[] data = new byte[] {
            48, 51, -96, 6, -128, 4, 51, 51, 68, 68, -95, 31, -95, 3, 4, 1, 33, -126, 0, -125, 1, 8, -124, 0, -123, 0,
            -121, 1, 3, -120, 0, -119, 0, -118, 0, -117, 0, -116, 0, -115, 0, -114, 0, -126, 6, -111, 17, 17, 33, 34, -14, -124, 0
    };

    @Test(groups = { "functional.decode", "anyTimeSubscriptionInterrogationRequest" })
    public void testDecode() throws Exception {

        AsnInputStream ansIS = new AsnInputStream(data);
        int tag = ansIS.readTag();
        assertEquals(tag, Tag.SEQUENCE);

        AnyTimeSubscriptionInterrogationRequestImpl anyTimeInt = new AnyTimeSubscriptionInterrogationRequestImpl();
        anyTimeInt.decodeAll(ansIS);

        SubscriberIdentity subsId = anyTimeInt.getSubscriberIdentity();
        IMSI isdnAddress = subsId.getIMSI();
        assertEquals(isdnAddress.getData(), "33334444");

        ISDNAddressString gscmSCFAddress = anyTimeInt.getGsmScfAddress();
        assertEquals(gscmSCFAddress.getAddress(), "111112222");

        RequestedSubscriptionInfo requestedInfo = anyTimeInt.getRequestedSubscriptionInfo();

        assertFalse(requestedInfo.getRequestedSSInfo().getLongFtnSupported());
        assertEquals(requestedInfo.getRequestedSSInfo().getSsCode().getData(), SupplementaryCodeValue.cfu.getCode());
    }

    @Test(groups = { "functional.decode", "subscriberInformation" })
    public void testEncode() throws Exception {

        IMSI imsi = new IMSIImpl("33334444");
        SubscriberIdentity subscriberIdentity = new SubscriberIdentityImpl(imsi);
        SSCode ssCode = new SSCodeImpl(SupplementaryCodeValue.cfu);
        SSForBSCode ssForBSCode = new SSForBSCodeImpl(ssCode, null, false);

        RequestedSubscriptionInfo requestedInfo = new RequestedSubscriptionInfoImpl(
                ssForBSCode, true, RequestedCAMELSubscriptionInfo.dcsi, true, true, null,
                AdditionalRequestedCAMELSubscriptionInfo.dImCSI, true, true, true, true, true, true, true);
        ISDNAddressString gsmSCFAddress = new ISDNAddressStringImpl(AddressNature.international_number,
                NumberingPlan.ISDN, "111112222");

        AnyTimeSubscriptionInterrogationRequestImpl anyTimeInt = new AnyTimeSubscriptionInterrogationRequestImpl(
                subscriberIdentity, requestedInfo, gsmSCFAddress, null,true);

        AsnOutputStream asnOS = new AsnOutputStream();
        anyTimeInt.encodeAll(asnOS);
        byte[] encodedData = asnOS.toByteArray();
        assertTrue(Arrays.equals(data, encodedData));
    }

}


                /*callForwardingData,
                callBarringData, odbInfo,
                camelSubscriptionInfo, supportedCamelPhases,
                supportedCamelPhases1, null,
                offeredCamel4CSIs,
                offeredCamel4CSIs1,
                null, null,
                callWaitingData, callHoldData,
                clipData, clirData,
                ectData*/
  /*      ArrayList<ExtForwFeature> forwardingFeatureList = new ArrayList<ExtForwFeature>();
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
        CallWaitingData callWaitingData = new CallWaitingDataImpl(new ArrayList<ExtCwFeature>(), false);
        CallHoldData callHoldData = new CallHoldDataImpl(true, new ExtSSStatusImpl(true, true, true, true));
        ClipData clipData = new ClipDataImpl(true, OverrideCategory.overrideDisabled, new ExtSSStatusImpl(true, true, true, true));
        ClirData clirData = new ClirDataImpl(true, CliRestrictionOption.permanent, new ExtSSStatusImpl(true, true, true, true));
        EctData ectData = new EctDataImpl(true, new ExtSSStatusImpl(true, true, true, true));
*/
/*
                    ArrayList<MSISDNBS> msisdnBsList = new ArrayList<MSISDNBS>();
                    MSISDNBS msisdnBs = new MsisdnBs(new ISDNAddressString(),
                            mapParameterFactory.);
                    msisdnBsList.add(msisdnBs);
*/