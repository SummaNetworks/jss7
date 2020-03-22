package com.summanetworks.topic;

import com.summanetworks.topic.TopicSccpMessage;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.sccp.impl.parameter.GlobalTitle0010Impl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.SccpAddressImpl;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author ajimenez, created on 22/3/20.
 */
public class TopicSccpMessageTest {


    @Test
    public void byteConversionTest(){

        TopicSccpMessage tsm = new TopicSccpMessage();
        tsm.id = 33;
        tsm.ssn = 8;
        tsm.localAddress = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, new GlobalTitle0010Impl("102030",1), 5566, 8);
        tsm.localAddress = null;
        tsm.remoteAddress = null;
        tsm.data  = new byte [2];
        tsm.data[0] = 0x1;
        tsm.data[0] = 0x3;

        byte [] tsmBytes =  tsm.toBytes();
        TopicSccpMessage tsmLoaded = TopicSccpMessage.fromBytes(tsmBytes);

        Assert.assertEquals(tsmLoaded.id, tsm.id);
        Assert.assertEquals(tsmLoaded.ssn, tsm.ssn);
        Assert.assertEquals(tsmLoaded.localAddress, tsm.localAddress);
        Assert.assertNull(tsmLoaded.remoteAddress);
        Assert.assertEquals(tsmLoaded.data, tsm.data);
    }


}
