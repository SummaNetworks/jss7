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
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.GSNAddress;
import org.mobicents.protocols.ss7.map.api.primitives.GSNAddressAddressType;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.service.pdpContextActivation.FailureReportRequestImpl;
import org.mobicents.protocols.ss7.map.primitives.GSNAddressImpl;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
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
public class FailureReportRequestTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 16, -128, 7, 17, 17, 33, 34, 34, 51, -13, -127, 5, -111, -120, -120, 0, 0 };
    }

    private byte[] getEncodedData2() {
        return new byte[] { 48, 64, -128, 7, 17, 17, 33, 34, 34, 51, -13, -127, 5, -111, -120, -120, 0, 0,
                -126, 5, 4, -64, -88, 4, 22, -93, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15,
                48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33};
    }

    private byte[] getAddressData() {
        return new byte[] { (byte) 192, (byte) 168, 4, 22 };
    }

    @Test(groups = { "functional.decode", "service.pdpContextActivation" })
    public void testDecode() throws Exception {

        byte[] rawData = getEncodedData();

        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        FailureReportRequestImpl impl = new FailureReportRequestImpl();
        impl.decodeAll(asn);

        assertEquals(impl.getImsi().getData(), "1111122222333");
        assertNull(impl.getGgsnAddress());
        assertEquals(impl.getGgsnNumber().getAddress(), "88880000");
        assertNull(impl.getExtensionContainer());


        rawData = getEncodedData2();

        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        impl = new FailureReportRequestImpl();
        impl.decodeAll(asn);

        assertEquals(impl.getImsi().getData(), "1111122222333");
        assertEquals(impl.getGgsnAddress().getGSNAddressAddressType(), GSNAddressAddressType.IPv4);
        assertEquals(impl.getGgsnAddress().getGSNAddressData(), getAddressData());
        assertEquals(impl.getGgsnNumber().getAddress(), "88880000");
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(impl.getExtensionContainer()));
    }

    @Test(groups = { "functional.encode", "service.pdpContextActivation" })
    public void testEncode() throws Exception {
/*
 * FailureReportArg ::= SEQUENCE {
 *          imsi                [0] IMSI,
 *          ggsn-Number         [1] ISDN-AddressString,
 *          ggsn-Address        [2] GSN-Address OPTIONAL,
 *          extensionContainer  [3] ExtensionContainer     OPTIONAL,
 *          ...}
 */
        IMSI imsi = new IMSIImpl("1111122222333");
        ISDNAddressString ggsnNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "88880000");
        FailureReportRequestImpl impl = new FailureReportRequestImpl(imsi, ggsnNumber, null, null);

        AsnOutputStream asnOS = new AsnOutputStream();

        impl.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));


        GSNAddress ggsnAddress = new GSNAddressImpl(GSNAddressAddressType.IPv4, getAddressData());
        impl = new FailureReportRequestImpl(imsi, ggsnNumber, ggsnAddress, MAPExtensionContainerTest.GetTestExtensionContainer());

        asnOS = new AsnOutputStream();

        impl.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedData2();
        assertTrue(Arrays.equals(rawData, encodedData));
    }

}
