/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.protocols.ss7.map.service.pdpContextActivation;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.primitives.GSNAddress;
import org.mobicents.protocols.ss7.map.api.primitives.GSNAddressAddressType;
import org.mobicents.protocols.ss7.map.api.service.pdpContextActivation.FailureReportResponseImpl;
import org.mobicents.protocols.ss7.map.primitives.GSNAddressImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

/**
*
* @author eva ogallar
*
*/
public class FailureReportResponseTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 0 };
    }

    private byte[] getEncodedData2() {
        return new byte[] { 48, 48, -128, 5, 4, -64, -88, 4, 22, -95, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11,
                12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3,
                31, 32, 33};
    }

    private byte[] getAddressData() {
        return new byte[] { (byte) 192, (byte) 168, 4, 11 };
    }

    private byte[] getAddressData2() {
        return new byte[] { (byte) 192, (byte) 168, 4, 22 };
    }

    @Test(groups = { "functional.decode", "service.pdpContextActivation" })
    public void testDecode() throws Exception {

        byte[] rawData = getEncodedData();

        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        FailureReportResponseImpl impl = new FailureReportResponseImpl();
        impl.decodeAll(asn);
        assertNull(impl.getGgsnAddress());
        assertNull(impl.getExtensionContainer());


        rawData = getEncodedData2();

        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        impl = new FailureReportResponseImpl();
        impl.decodeAll(asn);

        assertEquals(impl.getGgsnAddress().getGSNAddressAddressType(), GSNAddressAddressType.IPv4);
        assertEquals(impl.getGgsnAddress().getGSNAddressData(), getAddressData2());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(impl.getExtensionContainer()));

    }

    @Test(groups = { "functional.encode", "service.pdpContextActivation" })
    public void testEncode() throws Exception {

        GSNAddress sgsnAddress = new GSNAddressImpl(GSNAddressAddressType.IPv4, getAddressData());
        FailureReportResponseImpl impl = new FailureReportResponseImpl(null, null);
        // GSNAddress sgsnAddress, GSNAddress ggsnAddress, Integer mobileNotReachableReason, MAPExtensionContainer extensionContainer

        AsnOutputStream asnOS = new AsnOutputStream();

        impl.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));


        GSNAddress ggsnAddress = new GSNAddressImpl(GSNAddressAddressType.IPv4, getAddressData2());
        impl = new FailureReportResponseImpl(ggsnAddress, MAPExtensionContainerTest.GetTestExtensionContainer());

        asnOS = new AsnOutputStream();

        impl.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedData2();
        assertTrue(Arrays.equals(rawData, encodedData));
    }

}
