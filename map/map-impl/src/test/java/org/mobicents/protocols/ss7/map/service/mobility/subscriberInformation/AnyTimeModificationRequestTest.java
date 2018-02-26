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
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.primitives.SubscriberIdentity;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.ModificationInstruction;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedCAMELSubscriptionInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.BearerServiceCodeValue;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtForwOptionsForwardingReason;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ODBData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ODBGeneralData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ODBHPLMNData;
import org.mobicents.protocols.ss7.map.api.service.supplementary.CliRestrictionOption;
import org.mobicents.protocols.ss7.map.api.service.supplementary.OverrideCategory;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SupplementaryCodeValue;
import org.mobicents.protocols.ss7.map.primitives.AddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNSubaddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.SubscriberIdentityImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtBasicServiceCodeImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtBearerServiceCodeImpl;
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
 * @author eva ogallar
 *
 */
public class AnyTimeModificationRequestTest {

    // Real Trace
    byte[] data = new byte[] {
            48, -127, -90, -96, 9, -127, 7, -111, 85, 67, -103, 119, 21, 9, -127, 7, -111, 85, 67, 105, 38, -103, 52,
            -94, 25, -128, 1, 69, -126, 1, 48, -126, 1, 15, -125, 5, -111, -104, -104, -104, -104, -124, 1, 22, -123, 1,
            12, -122, 1, 1, -93, 21, -128, 1, 69, -126, 1, 48, -126, 1, 15, -125, 4, 49, 53, 50, 52, -124, 1, 12, -123,
            1, 1, -92, 9, -128, 1, 7, -126, 1, 1, -127, 1, 1, -89, 16, -96, 11, 3, 5, 3, 74, -43, 85, 80, 3, 2, 4, 80,
            -127, 1, 1, -88, 3, -127, 1, 1, -119, 2, 7, -128, -86, 3, -127, 1, 1, -85, 11, -96, 3, -126, 1, 48, -127, 1,
            15, -126, 1, 1, -84, 9, -127, 1, 1, -128, 1, 15, -126, 1, 1, -83, 9, -127, 1, 0, -128, 1, 15, -126, 1, 1,
            -82, 6, -128, 1, 15, -127, 1, 1, -81, 6, -128, 1, 15, -127, 1, 1, -122, 0
    };

    @Test(groups = { "functional.decode", "subscriberInformation" })
    public void testDecode() throws Exception {

        AsnInputStream ansIS = new AsnInputStream(data);
        int tag = ansIS.readTag();
        assertEquals(tag, Tag.SEQUENCE);

        AnyTimeModificationRequestImpl anyTimeMod = new AnyTimeModificationRequestImpl();
        anyTimeMod.decodeAll(ansIS);

        SubscriberIdentity subsId = anyTimeMod.getSubscriberIdentity();
        ISDNAddressString isdnAddress = subsId.getMSISDN();
        assertEquals(isdnAddress.getAddress(), "553499775190");

        ISDNAddressString gscmSCFAddress = anyTimeMod.getGsmSCFAddress();
        assertEquals(gscmSCFAddress.getAddress(), "553496629943");

        assertNotNull(anyTimeMod.getSubscriberIdentity());
        assertNotNull(anyTimeMod.getGsmSCFAddress());
        assertNotNull(anyTimeMod.getModificationRequestForCfInfo());
        assertNotNull(anyTimeMod.getModificationRequestForCbInfo());
        assertNotNull(anyTimeMod.getModificationRequestForCSI());
        assertNull(anyTimeMod.getExtensionContainer());
        assertNotNull(anyTimeMod.getLongFTNSupported());
        assertNotNull(anyTimeMod.getModificationRequestForODBdata());
        assertNotNull(anyTimeMod.getModificationRequestForIpSmGwData());
        assertNotNull(anyTimeMod.getActivationRequestForUEReachability());
        assertNotNull(anyTimeMod.getModificationRequestForCSG());
        assertNotNull(anyTimeMod.getModificationRequestForCwData());
        assertNotNull(anyTimeMod.getModificationRequestForClipData());
        assertNotNull(anyTimeMod.getModificationRequestForClirData());
        assertNotNull(anyTimeMod.getModificationRequestForHoldData());
        assertNotNull(anyTimeMod.getModificationRequestForEctData());
    }

    @Test(groups = { "functional.decode", "subscriberInformation" })
    public void testEncode() throws Exception {

        ISDNAddressString isdnAdd = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "553499775190");
        SubscriberIdentity subsId = new SubscriberIdentityImpl(isdnAdd);
        ISDNAddressString gscmSCFAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "553496629943");

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
        ISDNSubaddressStringImpl isdnSubaddressString = new ISDNSubaddressStringImpl(new byte[]{22});

        ModificationRequestForCFInfoImpl modificationRequestForCfInfo = new ModificationRequestForCFInfoImpl(
                ssCode, basicService, ssStatus, addressString, isdnSubaddressString, 12, ModificationInstruction.activate, null);

        ModificationRequestForCBInfoImpl modificationRequestForCbInfo = new ModificationRequestForCBInfoImpl(ssCode, basicService,
                ssStatus, new PasswordImpl("1524"), 12, ModificationInstruction.activate, null);

        ModificationRequestForCSIImpl modificationRequestForCSI = new ModificationRequestForCSIImpl(RequestedCAMELSubscriptionInfo.mCSI,
                ModificationInstruction.activate, ModificationInstruction.activate, null, null);
        // odbData
        ODBGeneralData oDBGeneralData = new ODBGeneralDataImpl(false, true, false, true, false, true, false, true, false, true, false, true, false, true,
                false, true, false, true, false, true, false, true, false, true, false, true, false, true, false);
        ODBHPLMNData odbHplmnData = new ODBHPLMNDataImpl(false, true, false, true);
        ODBData odbData = new ODBDataImpl(oDBGeneralData, odbHplmnData, null);
        ModificationRequestForODBdataImpl modificationRequestForODBdata = new ModificationRequestForODBdataImpl(
                odbData, ModificationInstruction.activate, null);

        ModificationRequestForIPSMGWDataImpl modificationRequestForIpSmGwData = new ModificationRequestForIPSMGWDataImpl(
                ModificationInstruction.activate, null);

        RequestedServingNodeImpl activationRequestForUEReachability = new RequestedServingNodeImpl(true);
        ModificationRequestForCSGImpl modificationRequestForCSG = new ModificationRequestForCSGImpl(ModificationInstruction.activate, null);
        ModificationRequestForCWInfoImpl modificationRequestForCwData = new ModificationRequestForCWInfoImpl(basicService,
                ssStatus, ModificationInstruction.activate, null);
        ModificationRequestForCLIPInfoImpl modificationRequestForClipData = new ModificationRequestForCLIPInfoImpl(ssStatus,
                OverrideCategory.overrideDisabled, ModificationInstruction.activate, null);

        ModificationRequestForCLIRInfoImpl modificationRequestForClirData = new ModificationRequestForCLIRInfoImpl(ssStatus,
                CliRestrictionOption.permanent, ModificationInstruction.activate, null);

        ModificationRequestForCHInfoImpl modificationRequestForHoldData = new ModificationRequestForCHInfoImpl(ssStatus,
                ModificationInstruction.activate, null);

        ModificationRequestForECTInfoImpl modificationRequestForEctData = new ModificationRequestForECTInfoImpl(ssStatus,
                ModificationInstruction.activate, null);

        AnyTimeModificationRequestImpl anyTimeInt = new AnyTimeModificationRequestImpl(subsId, gscmSCFAddress,
                modificationRequestForCfInfo, modificationRequestForCbInfo,
                modificationRequestForCSI, null, true, modificationRequestForODBdata,
                modificationRequestForIpSmGwData, activationRequestForUEReachability,
                modificationRequestForCSG, modificationRequestForCwData,
                modificationRequestForClipData, modificationRequestForClirData,
                modificationRequestForHoldData, modificationRequestForEctData);

        AsnOutputStream asnOS = new AsnOutputStream();
        anyTimeInt.encodeAll(asnOS);
        byte[] encodedData = asnOS.toByteArray();
        assertTrue(Arrays.equals(data, encodedData));
    }

    @Test(groups = { "functional.decode", "subscriberInformation" })
    public void testEncodeDecode() throws Exception {

        ISDNAddressString isdnAdd = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "553499775190");
        SubscriberIdentity subsId = new SubscriberIdentityImpl(isdnAdd);
        ISDNAddressString gscmSCFAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "553496629943");

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
        ISDNSubaddressStringImpl isdnSubaddressString = new ISDNSubaddressStringImpl(new byte[]{22});

        ModificationRequestForCFInfoImpl modificationRequestForCfInfo = new ModificationRequestForCFInfoImpl(
                ssCode, basicService, ssStatus, addressString, isdnSubaddressString, 12, ModificationInstruction.activate, null);

        ModificationRequestForCBInfoImpl modificationRequestForCbInfo = new ModificationRequestForCBInfoImpl(ssCode, basicService,
                ssStatus, new PasswordImpl("1524"), 12, ModificationInstruction.activate, null);

        ModificationRequestForCSIImpl modificationRequestForCSI = new ModificationRequestForCSIImpl(RequestedCAMELSubscriptionInfo.mCSI,
                ModificationInstruction.activate, ModificationInstruction.activate, null, null);
        // odbData
        ODBGeneralData oDBGeneralData = new ODBGeneralDataImpl(false, true, false, true, false, true, false, true, false, true, false, true, false, true,
                false, true, false, true, false, true, false, true, false, true, false, true, false, true, false);
        ODBHPLMNData odbHplmnData = new ODBHPLMNDataImpl(false, true, false, true);
        ODBData odbData = new ODBDataImpl(oDBGeneralData, odbHplmnData, null);
        ModificationRequestForODBdataImpl modificationRequestForODBdata = new ModificationRequestForODBdataImpl(
                odbData, ModificationInstruction.activate, null);

        ModificationRequestForIPSMGWDataImpl modificationRequestForIpSmGwData = new ModificationRequestForIPSMGWDataImpl(
                ModificationInstruction.activate, null);

        RequestedServingNodeImpl activationRequestForUEReachability = new RequestedServingNodeImpl(true);
        ModificationRequestForCSGImpl modificationRequestForCSG = new ModificationRequestForCSGImpl(ModificationInstruction.activate, null);
        ModificationRequestForCWInfoImpl modificationRequestForCwData = new ModificationRequestForCWInfoImpl(basicService,
                ssStatus, ModificationInstruction.activate, null);
        ModificationRequestForCLIPInfoImpl modificationRequestForClipData = new ModificationRequestForCLIPInfoImpl(ssStatus,
                OverrideCategory.overrideDisabled, ModificationInstruction.activate, null);

        ModificationRequestForCLIRInfoImpl modificationRequestForClirData = new ModificationRequestForCLIRInfoImpl(ssStatus,
                CliRestrictionOption.permanent, ModificationInstruction.activate, null);

        ModificationRequestForCHInfoImpl modificationRequestForHoldData = new ModificationRequestForCHInfoImpl(ssStatus,
                ModificationInstruction.activate, null);

        ModificationRequestForECTInfoImpl modificationRequestForEctData = new ModificationRequestForECTInfoImpl(ssStatus,
                ModificationInstruction.activate, null);

        AnyTimeModificationRequestImpl anyTimeInt = new AnyTimeModificationRequestImpl(subsId, gscmSCFAddress,
                modificationRequestForCfInfo, modificationRequestForCbInfo,
                modificationRequestForCSI, null, true, modificationRequestForODBdata,
                modificationRequestForIpSmGwData, activationRequestForUEReachability,
                modificationRequestForCSG, modificationRequestForCwData,
                modificationRequestForClipData, modificationRequestForClirData,
                modificationRequestForHoldData, modificationRequestForEctData);

        AsnOutputStream asnOS = new AsnOutputStream();
        anyTimeInt.encodeAll(asnOS);
        byte[] encodedData = asnOS.toByteArray();

        AsnInputStream ansIS = new AsnInputStream(encodedData);
        int tag = ansIS.readTag();
        assertEquals(tag, Tag.SEQUENCE);

        AnyTimeModificationRequestImpl anyTimeMod = new AnyTimeModificationRequestImpl();
        anyTimeMod.decodeAll(ansIS);

        subsId = anyTimeMod.getSubscriberIdentity();
        ISDNAddressString isdnAddress = subsId.getMSISDN();
        assertEquals(isdnAddress.getAddress(), "553499775190");

        gscmSCFAddress = anyTimeMod.getGsmSCFAddress();
        assertEquals(gscmSCFAddress.getAddress(), "553496629943");

        assertNotNull(anyTimeMod.getSubscriberIdentity());
        assertNotNull(anyTimeMod.getGsmSCFAddress());
        assertNotNull(anyTimeMod.getModificationRequestForCfInfo());
        assertNotNull(anyTimeMod.getModificationRequestForCbInfo());
        assertNotNull(anyTimeMod.getModificationRequestForCSI());
        assertNull(anyTimeMod.getExtensionContainer());
        assertNotNull(anyTimeMod.getLongFTNSupported());
        assertNotNull(anyTimeMod.getModificationRequestForODBdata());
        assertNotNull(anyTimeMod.getModificationRequestForIpSmGwData());
        assertNotNull(anyTimeMod.getActivationRequestForUEReachability());
        assertNotNull(anyTimeMod.getModificationRequestForCSG());
        assertNotNull(anyTimeMod.getModificationRequestForCwData());
        assertNotNull(anyTimeMod.getModificationRequestForClipData());
        assertNotNull(anyTimeMod.getModificationRequestForClirData());
        assertNotNull(anyTimeMod.getModificationRequestForHoldData());
        assertNotNull(anyTimeMod.getModificationRequestForEctData());
    }

}
