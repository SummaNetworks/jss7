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

package org.mobicents.protocols.ss7.map.service.callhandling;

import org.apache.log4j.Logger;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.callhandling.CallTerminationIndicator;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

/*
 *
 * @author eva ogallar
 *
 */
public class IstAlertResponseTest {
    public static final int IST_ALERT_TIMER = 10;
    public static final boolean IST_INFORMATION_WITHDRAW = true;
    public static final CallTerminationIndicator TERMINATE_ALL_CALL_ACTIVITIES = CallTerminationIndicator.terminateAllCallActivities;
    Logger logger = Logger.getLogger(IstAlertResponseTest.class);

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeTest
    public void setUp() {
    }

    @AfterTest
    public void tearDown() {
    }

    private byte[] getData2() {
        return new byte[] { 48, 49, -128, 1, 10, -127, 0, -126, 1, 1, -93, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4,
                11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95,
                3, 31, 32, 33};
    }


    public byte[] getData1() {
        return new byte[] { 48, 0 };
    }

    @Test(groups = { "functional.decode", "service.callhandling" })
    public void testDecode() throws Exception {

        byte[] data = this.getData1();
        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();
        IstAlertResponseImpl prim = new IstAlertResponseImpl();
        prim.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertNull(prim.getExtensionContainer());


        data = this.getData2();
        asn = new AsnInputStream(data);
        tag = asn.readTag();
        prim = new IstAlertResponseImpl();
        prim.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertTrue(IST_ALERT_TIMER == prim.getIstAlertTimer());
        assertEquals(prim.getIstInformationWithdraw(), IST_INFORMATION_WITHDRAW);
        assertEquals(prim.getCallTerminationIndicator(), TERMINATE_ALL_CALL_ACTIVITIES );
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(prim.getExtensionContainer()));

    }

    @Test(groups = { "functional.encode", "service.callhandling" })
    public void testEncode() throws Exception {
        byte[] data = getData1();

        IstAlertResponseImpl sri = new IstAlertResponseImpl();

        AsnOutputStream asnOS = new AsnOutputStream();
        sri.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();
        assertEquals(data, encodedData);

        // all
        MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();

        IstAlertResponseImpl prim = new IstAlertResponseImpl(IST_ALERT_TIMER, IST_INFORMATION_WITHDRAW,
                TERMINATE_ALL_CALL_ACTIVITIES, extensionContainer);

        asnOS = new AsnOutputStream();
        prim.encodeAll(asnOS);
        encodedData = asnOS.toByteArray();

        assertEquals(getData2(), encodedData);
    }

    @Test(groups = { "functional.serialize", "service.callhandling" })
    public void testSerialization() throws Exception {

    }
}
