package org.mobicents.protocols.ss7.m3ua.impl.ssnm;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mobicents.protocols.ss7.m3ua.impl.AsState;
import org.mobicents.protocols.ss7.m3ua.impl.AspState;
import org.mobicents.protocols.ss7.mtp.Mtp3PausePrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3ResumePrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3StatusPrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3TransferPrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3UserPartListener;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * @author ajimenez, created on 26/11/20.
 */
public class SsnmDaudHandlerTest extends SsnmBaseHandler{

    private static final Logger logger = LogManager.getLogger(SsnmDaudHandlerTest.class);

    //Setup: Configurar la parte basica del test.


    //Por cada test, configurar o cambiar los estados pendientes.
    //Lanzar los diferentes tipos de DAUD.
    //Hará falta tests para el resto de mensajes...


    @BeforeMethod
    @Override
    public void setUp() throws Exception{
        super.setUp(new Mtp3UserPartListenerCustom());
    }

    @Test
    public void testDaudDava() throws Exception {
        logger.info("Starting server...");
        System.out.println("Starting server");
        server.start();
        Thread.sleep(100);

        System.out.println("Starting Client");
        client.start();

        Thread.sleep(10000);

        // Both AS and ASP should be ACTIVE now
        assertEquals(AspState.getState(serverAsp.getPeerFSM().getState().getName()), AspState.ACTIVE);
        assertEquals(AsState.getState(serverAs.getLocalFSM().getState().getName()), AsState.ACTIVE);
        assertEquals(AspState.getState(clientAsp.getLocalFSM().getState().getName()), AspState.ACTIVE);
        assertEquals(AsState.getState(clientAs.getPeerFSM().getState().getName()), AsState.ACTIVE);
        Thread.sleep(1000);

        //Client is AS and send the DAUD. Server acts as SGW so respond with DAVA (or DUNA)
        //server.sendDaud();
        client.sendDaud();

        // Capturar el retorno

        Thread.sleep(3000);

        client.stopAsp();
        Thread.sleep(3000);
        client.freeClient();
        server.stop();

    }

    private class Mtp3UserPartListenerCustom implements Mtp3UserPartListener {
        @Override
        public void onMtp3TransferMessage(Mtp3TransferPrimitive msg) {
            logger.info("onMtp3TransferMessage()...");
        }

        @Override
        public void onMtp3PauseMessage(Mtp3PausePrimitive msg) {
            logger.info("onMtp3PauseMessage()...");
        }

        @Override
        public void onMtp3ResumeMessage(Mtp3ResumePrimitive msg) {

            logger.info("================================>>> onMtp3ResumeMessage()...");
            logger.info("Message affected DPC: "+msg.getAffectedDpc());
        }

        @Override
        public void onMtp3StatusMessage(Mtp3StatusPrimitive msg) {
            logger.info("onMtp3StatusMessage()...");
        }
    }

}
