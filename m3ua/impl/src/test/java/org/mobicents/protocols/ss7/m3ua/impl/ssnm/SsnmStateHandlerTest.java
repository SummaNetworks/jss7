package org.mobicents.protocols.ss7.m3ua.impl.ssnm;

import org.mobicents.protocols.ss7.m3ua.impl.SsnmStateHandler;
import org.mobicents.protocols.ss7.m3ua.impl.message.ssnm.DestinationAvailableImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.ssnm.DestinationUnavailableImpl;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.ParameterFactoryImpl;
import org.mobicents.protocols.ss7.m3ua.message.ssnm.DestinationAvailable;
import org.mobicents.protocols.ss7.m3ua.message.ssnm.DestinationUnavailable;
import org.mobicents.protocols.ss7.m3ua.parameter.AffectedPointCode;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertEquals;

/**
 * @author ajimenez, created on 12/5/23.
 * This test check the behaviour of {SsnmStatehandler} class.
 *
 * Name nomenclature:
 * Given_Preconditions_When_StateUnderTest_Then_ExpectedBehavior
 **/
public class SsnmStateHandlerTest {

    private static final ParameterFactoryImpl paramFactory = new ParameterFactoryImpl();

    @Test
    public void given_DunaWithMask_WHEN_checkProhibited_THEN_prohibitsAffected() {
        SsnmStateHandler ssnmStateHandler = new SsnmStateHandler("Test1");
        int[] pointCodes = {0b1001}; short[] masks = {2}; //Mask is applied in binary. This configuration refers to 8,9,10, and 11
        //int[] pointCodes = {0b1001, 0b1011}; short[] masks = {2, 2}; //Mask is applied in binary.
        AffectedPointCode afpc = paramFactory.createAffectedPointCode(pointCodes, masks);
        DestinationUnavailable duna = new DestinationUnavailableImpl();
        duna.setAffectedPointCodes(afpc);
        ssnmStateHandler.processDuna(duna);

        //Prohibited
        assertTrue(ssnmStateHandler.isProhibited(8));
        assertTrue(ssnmStateHandler.isProhibited(9));
        assertTrue(ssnmStateHandler.isProhibited(10));
        assertTrue(ssnmStateHandler.isProhibited(11));

        //No prohibited
        assertFalse(ssnmStateHandler.isProhibited(7));
        assertFalse(ssnmStateHandler.isProhibited(12));
    }

    @Test
    public void given_2DunasWithMasks_WHEN_checkProhibited_THEN_ProhibitsAffected() {
        SsnmStateHandler ssnmStateHandler = new SsnmStateHandler("Test2");
        //Mask is applied in binary.
        // First pair refers to 8,9,10, and 11
        // Second pair refers to 16,17,18,19,20,21,22,23
        int[] pointCodes = {0b1001, 0b10001}; short[] masks = {2,3};

        AffectedPointCode afpc = paramFactory.createAffectedPointCode(pointCodes, masks);
        DestinationUnavailable duna = new DestinationUnavailableImpl();
        duna.setAffectedPointCodes(afpc);
        ssnmStateHandler.processDuna(duna);

        //Prohibited
        assertTrue(ssnmStateHandler.isProhibited(8));
        assertTrue(ssnmStateHandler.isProhibited(9));
        assertTrue(ssnmStateHandler.isProhibited(10));
        assertTrue(ssnmStateHandler.isProhibited(11));

        //No prohibited
        assertFalse(ssnmStateHandler.isProhibited(7));
        assertFalse(ssnmStateHandler.isProhibited(12));

        //Prohibited
        assertTrue(ssnmStateHandler.isProhibited(16));
        assertTrue(ssnmStateHandler.isProhibited(17));
        assertTrue(ssnmStateHandler.isProhibited(18));
        assertTrue(ssnmStateHandler.isProhibited(19));
        assertTrue(ssnmStateHandler.isProhibited(20));
        assertTrue(ssnmStateHandler.isProhibited(21));
        assertTrue(ssnmStateHandler.isProhibited(22));
        assertTrue(ssnmStateHandler.isProhibited(23));

        //No prohibited
        assertFalse(ssnmStateHandler.isProhibited(15));
        assertFalse(ssnmStateHandler.isProhibited(24));

    }

    @Test
    public void given_DunaAndDavaWithMasks_WHEN_checkProhibited_THEN_ProhibitsAffected() {

        SsnmStateHandler ssnmStateHandler = new SsnmStateHandler("Test3");

        AffectedPointCode afpcDuna = paramFactory.createAffectedPointCode(new int[]{0b1001}, new short[]{2});
        DestinationUnavailable duna = new DestinationUnavailableImpl();
        duna.setAffectedPointCodes(afpcDuna);

        ssnmStateHandler.processDuna(duna);

        //This enables a not disabled one, so is ignored.
        AffectedPointCode afpcDava = paramFactory.createAffectedPointCode(new int[]{0b1100}, new short[]{0});
        DestinationAvailable dava = new DestinationAvailableImpl();
        dava.setAffectedPointCodes(afpcDava);

        ssnmStateHandler.processDava(dava);

        //Prohibited
        assertTrue(ssnmStateHandler.isProhibited(8));
        assertTrue(ssnmStateHandler.isProhibited(9));
        assertTrue(ssnmStateHandler.isProhibited(10));
        assertTrue(ssnmStateHandler.isProhibited(11));
    }

    @Test
    public void given_2DunaAnd1DavaOfLastPC_WHEN_checkProhibited_THEN_ProhibitsAffected1DavaNot2Dava() {

        SsnmStateHandler ssnmStateHandler = new SsnmStateHandler("Test4");

        int[] pointCodes = {0b1001, 0b10001}; short[] masks = {2, 3};

        AffectedPointCode afpc = paramFactory.createAffectedPointCode(pointCodes, masks);
        DestinationUnavailable duna = new DestinationUnavailableImpl();
        duna.setAffectedPointCodes(afpc);
        ssnmStateHandler.processDuna(duna);

        //This enables a not disabled one, so is ignored.
        AffectedPointCode afpcDava = paramFactory.createAffectedPointCode(new int[]{0b10001}, new short[]{0});
        DestinationAvailable dava = new DestinationAvailableImpl();
        dava.setAffectedPointCodes(afpcDava);

        ssnmStateHandler.processDava(dava);

        //Prohibited
        assertTrue(ssnmStateHandler.isProhibited(8));
        assertTrue(ssnmStateHandler.isProhibited(9));
        assertTrue(ssnmStateHandler.isProhibited(10));
        assertTrue(ssnmStateHandler.isProhibited(11));


        //Prohibited
        assertTrue(ssnmStateHandler.isProhibited(16));
        assertTrue(ssnmStateHandler.isProhibited(18));
        assertTrue(ssnmStateHandler.isProhibited(19));
        assertTrue(ssnmStateHandler.isProhibited(20));
        assertTrue(ssnmStateHandler.isProhibited(21));
        assertTrue(ssnmStateHandler.isProhibited(22));
        assertTrue(ssnmStateHandler.isProhibited(23));


        //No prohibited
        assertFalse(ssnmStateHandler.isProhibited(17));
        assertFalse(ssnmStateHandler.isProhibited(15));
        assertFalse(ssnmStateHandler.isProhibited(24));


    }

    @Test
    public void given_2DunaAnd1DavaMasked_WHEN_checkProhibited_THEN_ProhibitsAffected1DavaNot2Dava() {

        SsnmStateHandler ssnmStateHandler = new SsnmStateHandler("Test5");

        int[] pointCodes = {0b1001, 0b10001}; short[] masks = {2, 3};

        AffectedPointCode afpc = paramFactory.createAffectedPointCode(pointCodes, masks);
        DestinationUnavailable duna = new DestinationUnavailableImpl();
        duna.setAffectedPointCodes(afpc);
        ssnmStateHandler.processDuna(duna);

        //This enables a not disabled one, so is ignored.
        AffectedPointCode afpcDava = paramFactory.createAffectedPointCode(new int[]{0b10001}, new short[]{2});
        DestinationAvailable dava = new DestinationAvailableImpl();
        dava.setAffectedPointCodes(afpcDava);

        ssnmStateHandler.processDava(dava);

        //Prohibited
        assertTrue(ssnmStateHandler.isProhibited(8));
        assertTrue(ssnmStateHandler.isProhibited(9));
        assertTrue(ssnmStateHandler.isProhibited(10));
        assertTrue(ssnmStateHandler.isProhibited(11));

        //Prohibited
        assertTrue(ssnmStateHandler.isProhibited(20));
        assertTrue(ssnmStateHandler.isProhibited(21));
        assertTrue(ssnmStateHandler.isProhibited(22));
        assertTrue(ssnmStateHandler.isProhibited(23));

        //No prohibited
        assertFalse(ssnmStateHandler.isProhibited(15));

        assertFalse(ssnmStateHandler.isProhibited(16));
        assertFalse(ssnmStateHandler.isProhibited(17));
        assertFalse(ssnmStateHandler.isProhibited(18));
        assertFalse(ssnmStateHandler.isProhibited(19));

        assertFalse(ssnmStateHandler.isProhibited(24));
    }
    @Test
    public void given_2RepeatedDunaAnd1DavaToClear_WHEN_checkNumberOfDuna_THEN_empty() {

        SsnmStateHandler ssnmStateHandler = new SsnmStateHandler("Test6");

        int[] pointCodes = {0b1001, 0b1001}; short[] masks = {2, 2};

        AffectedPointCode afpc = paramFactory.createAffectedPointCode(pointCodes, masks);
        DestinationUnavailable duna = new DestinationUnavailableImpl();
        duna.setAffectedPointCodes(afpc);
        ssnmStateHandler.processDuna(duna);

        //This enables a not disabled one, so is ignored.
        AffectedPointCode afpcDava = paramFactory.createAffectedPointCode(new int[]{0b1001}, new short[]{2});
        DestinationAvailable dava = new DestinationAvailableImpl();
        dava.setAffectedPointCodes(afpcDava);
        ssnmStateHandler.processDava(dava);

        assertEquals(0, ssnmStateHandler.activeDunasCount());
    }




}
