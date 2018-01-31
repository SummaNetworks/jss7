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

package org.mobicents.protocols.ss7.sccp.impl.translation;

import static org.testng.Assert.assertTrue;

import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.sccp.LoadSharingAlgorithm;
import org.mobicents.protocols.ss7.sccp.OriginationType;
import org.mobicents.protocols.ss7.sccp.RuleType;
import org.mobicents.protocols.ss7.sccp.impl.SccpHarness;
import org.mobicents.protocols.ss7.sccp.impl.User;
import org.mobicents.protocols.ss7.sccp.message.SccpDataMessage;
import org.mobicents.protocols.ss7.sccp.message.SccpMessage;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle0010;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author amit bhayani
 * @author baranowb
 */
public class GT0010SccpStackImplTest extends SccpHarness {

    private SccpAddress a1, a2;

    public GT0010SccpStackImplTest() {
    }

    @BeforeClass
    public void setUpClass() throws Exception {
        this.sccpStack1Name = "GT0010TestSccpStack1";
        this.sccpStack2Name = "GT0010TestSccpStack2";
    }

    @AfterClass
    public void tearDownClass() throws Exception {
    }

    @BeforeMethod
    public void setUp() throws Exception {
        super.setUp();

    }

    @AfterMethod
    public void tearDown() {
        super.tearDown();
    }

    protected static final String GT1_digits = "1234567890";
    protected static final String GT2_digits = "09876432";

    protected static final String GT1_pattern_digits = "1/???????/90";
    protected static final String GT2_pattern_digits = "0/??????/2";

    @Test(groups = { "gtt", "functional.route" })
    public void testRemoteRoutingBasedOnGT_DPC_SSN() throws Exception {

        GlobalTitle gt1 = super.sccpProvider1.getParameterFactory().createGlobalTitle(GT1_digits, 0);
        GlobalTitle gt2 = super.sccpProvider1.getParameterFactory().createGlobalTitle(GT2_digits, 0);

        a1 = super.sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt1, 0, getSSN());
        a2 = super.sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt2, 0, getSSN());

        // add addresses to translate
        SccpAddress primary1SccpAddress = super.sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, super.sccpProvider1.getParameterFactory().createGlobalTitle("-/-/-"), getStack2PC(), getSSN());
        SccpAddress primary2SccpAddress = super.sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, super.sccpProvider1.getParameterFactory().createGlobalTitle("-/-/-"), getStack1PC(), getSSN());
        super.router1.addRoutingAddress(22, primary1SccpAddress);
        super.router2.addRoutingAddress(33, primary2SccpAddress);

        SccpAddress rule1SccpAddress = super.sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, super.sccpProvider1.getParameterFactory().createGlobalTitle(GT2_pattern_digits, 0), 0, getSSN());
        SccpAddress rule2SccpAddress = super.sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, super.sccpProvider1.getParameterFactory().createGlobalTitle(
                GT1_pattern_digits, 0), 0, getSSN());
        SccpAddress patternDefaultCalling = super.sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, super.sccpProvider1.getParameterFactory().createGlobalTitle("*", 0), 0, 0);

        super.router1.addRule(1, RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.ALL, rule1SccpAddress,
                "K/R/K", 22, -1, null, 0, patternDefaultCalling);
        super.router2.addRule(1, RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.ALL, rule2SccpAddress,
                "R/R/R", 33, -1, null, 0, patternDefaultCalling);

        // now create users, we need to override matchX methods, since our rules do kinky stuff with digits, plus
        User u1 = new User(sccpStack1.getSccpProvider(), a1, a2, getSSN()) {

            protected boolean matchCalledPartyAddress() {
                SccpMessage msg = messages.get(0);
                SccpDataMessage udt = (SccpDataMessage) msg;
                SccpAddress addressToMatch = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack1PC(),
                        getSSN());
                if (!addressToMatch.equals(udt.getCalledPartyAddress())) {
                    return false;
                }
                return true;
            }

        };
        User u2 = new User(sccpStack2.getSccpProvider(), a2, a1, getSSN()) {

            protected boolean matchCalledPartyAddress() {
                SccpMessage msg = messages.get(0);
                SccpDataMessage udt = (SccpDataMessage) msg;
                SccpAddress addressToMatch = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, sccpProvider1.getParameterFactory().createGlobalTitle("02",0), getStack2PC(), getSSN());
                if (!addressToMatch.equals(udt.getCalledPartyAddress())) {
                    return false;
                }
                return true;
            }

        };

        u1.register();
        u2.register();

        u1.send();
        u2.send();

        Thread.currentThread().sleep(3000);

        assertTrue(u1.check(), "Message not received");
        assertTrue(u2.check(), "Message not received");
    }

    // TODO: add more ?
}
