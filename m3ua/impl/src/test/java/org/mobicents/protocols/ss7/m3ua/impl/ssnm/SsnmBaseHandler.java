package org.mobicents.protocols.ss7.m3ua.impl.ssnm;

import java.nio.ByteBuffer;

import com.sun.nio.sctp.SctpChannel;
import javolution.util.FastList;
import org.apache.log4j.Logger;
import org.mobicents.protocols.api.IpChannelType;
import org.mobicents.protocols.api.Management;
import org.mobicents.protocols.api.PayloadData;
import org.mobicents.protocols.sctp.ManagementImpl;
import org.mobicents.protocols.ss7.m3ua.ExchangeType;
import org.mobicents.protocols.ss7.m3ua.Functionality;
import org.mobicents.protocols.ss7.m3ua.IPSPType;
import org.mobicents.protocols.ss7.m3ua.Util;
import org.mobicents.protocols.ss7.m3ua.impl.AsImpl;
import org.mobicents.protocols.ss7.m3ua.impl.AsState;
import org.mobicents.protocols.ss7.m3ua.impl.AspFactoryImpl;
import org.mobicents.protocols.ss7.m3ua.impl.AspImpl;
import org.mobicents.protocols.ss7.m3ua.impl.AspState;
import org.mobicents.protocols.ss7.m3ua.impl.M3UAManagementImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.M3UAMessageImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.MessageFactoryImpl;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.ParameterFactoryImpl;
import org.mobicents.protocols.ss7.m3ua.message.MessageClass;
import org.mobicents.protocols.ss7.m3ua.message.MessageFactory;
import org.mobicents.protocols.ss7.m3ua.message.MessageType;
import org.mobicents.protocols.ss7.m3ua.message.ssnm.DestinationStateAudit;
import org.mobicents.protocols.ss7.m3ua.parameter.AffectedPointCode;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;
import org.mobicents.protocols.ss7.m3ua.parameter.TrafficModeType;
import org.mobicents.protocols.ss7.mtp.Mtp3PausePrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3ResumePrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3StatusPrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3TransferPrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3TransferPrimitiveFactory;
import org.mobicents.protocols.ss7.mtp.Mtp3UserPartListener;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import static org.testng.Assert.assertEquals;

/**
 * @author ajimenez, created on 26/11/20.
 */
public abstract class SsnmBaseHandler {

    static final Logger logger = Logger.getLogger(SsnmBaseHandler.class);

    static final String SERVER_NAME = "TestServer";
    static final String SERVER_HOST = "127.0.0.1";
    static final int SERVER_PORT = 2345;

    static final String SERVER_ASSOCIATION_NAME = "ServerAssociation";
    static final String CLIENT_ASSOCIATION_NAME = "ClientAssociation";

    static final String CLIENT_HOST = "127.0.0.1";
    static final int CLIENT_PORT = 2346;
    public static final String CLIENT_TEST_AS = "client-test-As";
    public static final String CLIENT_TEST_ASP = "client-test-Asp";
    public static final String SERVER_TEST_AS = "server-test-As";
    public static final String SERVER_TEST_ASP = "server-test-Asp";

    protected Management sctpManagement = null;
    protected M3UAManagementImpl m3uaMgmt = null;
    protected MessageFactory messageFactory = new MessageFactoryImpl();
    protected ParameterFactoryImpl parameterFactory = new ParameterFactoryImpl();

    protected AsImpl serverAs;
    protected AspImpl serverAsp;
    protected AspFactoryImpl serverAspFactory;

    protected AsImpl clientAs;
    protected AspImpl clientAsp;
    protected AspFactoryImpl clientAspFactory;

    protected Server server;
    protected Client client;

    protected Mtp3UserPartListenerImpl mtp3UserPartListener = null;

    private long [] routingContexts = {100l};
    private int clientPointCode = 2000;
    private int serverPointCode = 5000;


    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeMethod
    public abstract void setUp() throws Exception ;

    public void setUp(Mtp3UserPartListener mtp3UserPartListener) throws Exception {

        if(mtp3UserPartListener == null)
            mtp3UserPartListener = new Mtp3UserPartListenerImpl();

        client = new Client();
        server = new Server();

        this.sctpManagement = new ManagementImpl("GatewayTest");
        this.sctpManagement.setPersistDir(Util.getTmpTestDir());
        this.sctpManagement.setSingleThread(true);
        this.sctpManagement.start();
        this.sctpManagement.setConnectDelay(1000 * 2);// setting connection
        // delay to 5 secs
        this.sctpManagement.removeAllResourses();

        this.m3uaMgmt = new M3UAManagementImpl("GatewayTest", null);
        this.m3uaMgmt.setPersistDir(Util.getTmpTestDir());
        this.m3uaMgmt.setTransportManagement(this.sctpManagement);
        this.m3uaMgmt.addMtp3UserPartListener(mtp3UserPartListener);
        this.m3uaMgmt.start();
        this.m3uaMgmt.removeAllResourses();

    }

    @AfterMethod
    public void tearDown() throws Exception {

        this.sctpManagement.stop();
        this.m3uaMgmt.stop();
    }

    // INVALID METHOD
    public void testSingleAspInAs() throws Exception {
        // 5.1.1. Single ASP in an Application Server ("1+0" sparing),

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

//        client.sendPayload();
//        server.sendPayload();

        Thread.sleep(1000);

        client.stopAsp();
        logger.debug("Stopped Client");
        // Give time to exchnge ASP_DOWN messages
        Thread.sleep(100);

        // The AS is Pending
        assertEquals(AsState.getState(clientAs.getPeerFSM().getState().getName()), AsState.PENDING);
        assertEquals(AsState.getState(serverAs.getLocalFSM().getState().getName()), AsState.PENDING);

        // Let the AS go in DOWN state
        Thread.sleep(4000);
        logger.debug("Woke from 4000 sleep");

        // The AS is Pending
        assertEquals(AsState.getState(clientAs.getPeerFSM().getState().getName()), AsState.DOWN);
        assertEquals(AsState.getState(serverAs.getLocalFSM().getState().getName()), AsState.DOWN);

        client.freeClient();
        server.stop();

        Thread.sleep(100);

        // we should receive two MTP3 data
        assertEquals(mtp3UserPartListener.getReceivedData().size(), 2);

    }

    /**
     * @return true if sctp is supported by this OS and false in not
     */
    public static boolean checkSctpEnabled() {
        try {
            SctpChannel socketChannel = SctpChannel.open();
            socketChannel.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    protected class Client {

        public Client() {
        }

        public void start() throws Exception {

            IpChannelType ipChannelType = IpChannelType.TCP;
            if (checkSctpEnabled())
                ipChannelType = IpChannelType.SCTP;

            // 1. Create SCTP Association
            sctpManagement.addAssociation(CLIENT_HOST, CLIENT_PORT, SERVER_HOST, SERVER_PORT, CLIENT_ASSOCIATION_NAME,
                    ipChannelType, null);

            // 2. Create AS
            // m3ua as create rc <rc> <ras-name>
            RoutingContext rc = parameterFactory.createRoutingContext(routingContexts);
            TrafficModeType trafficModeType = parameterFactory.createTrafficModeType(TrafficModeType.Loadshare);
            clientAs = (AsImpl) m3uaMgmt.createAs(CLIENT_TEST_AS, Functionality.AS, ExchangeType.SE, IPSPType.CLIENT, rc,
                    trafficModeType, 1, null);

            // 3. Create ASP
            // m3ua asp create ip <local-ip> port <local-port> remip <remip>
            // remport <remport> <asp-name>
            clientAspFactory = (AspFactoryImpl) m3uaMgmt.createAspFactory(CLIENT_TEST_ASP, CLIENT_ASSOCIATION_NAME, false);

            // 4. Assign ASP to AS
            clientAsp = m3uaMgmt.assignAspToAs(CLIENT_TEST_AS, CLIENT_TEST_ASP);

            // 5. Define Route
            // Define Route
            m3uaMgmt.addRoute(serverPointCode, clientPointCode, -1, CLIENT_TEST_AS);
            m3uaMgmt.addRoute(clientPointCode, serverPointCode, -1, CLIENT_TEST_AS);

            // 6. Start ASP
            m3uaMgmt.startAsp(CLIENT_TEST_ASP);

        }

        public void stopAsp() throws Exception {
            // 1. stop ASP
            m3uaMgmt.stopAsp(CLIENT_TEST_ASP);

        }

        public void freeClient() throws Exception {

            // 2.Remove route
            m3uaMgmt.removeRoute(clientPointCode, serverPointCode, -1, CLIENT_TEST_AS);
            m3uaMgmt.removeRoute(serverPointCode, clientPointCode, -1, CLIENT_TEST_AS);

            // 3. Unassign ASP from AS
            // clientM3UAMgmt.
            m3uaMgmt.unassignAspFromAs(CLIENT_TEST_AS, CLIENT_TEST_ASP);

            // 4. destroy aspFactory
            m3uaMgmt.destroyAspFactory(CLIENT_TEST_ASP);

            // 5. Destroy As
            m3uaMgmt.destroyAs(CLIENT_TEST_AS);

            // 6. remove sctp
            sctpManagement.removeAssociation(CLIENT_ASSOCIATION_NAME);
        }

        public void sendPayload() throws Exception {
            Mtp3TransferPrimitiveFactory factory = m3uaMgmt.getMtp3TransferPrimitiveFactory();
            Mtp3TransferPrimitive mtp3TransferPrimitive = factory.createMtp3TransferPrimitive(3, 1, 0, 123, 1408, 1,
                    new byte[] { 1, 2, 3, 4 });
            m3uaMgmt.sendMessage(mtp3TransferPrimitive);
        }

        public void sendDaud() throws Exception {
            DestinationStateAudit msg = (DestinationStateAudit) messageFactory.createMessage(
                    MessageClass.SIGNALING_NETWORK_MANAGEMENT, MessageType.DESTINATION_STATE_AUDIT);
            AffectedPointCode apc = parameterFactory.createAffectedPointCode (new int [] {serverPointCode}, new short []{0});
            RoutingContext rc = parameterFactory.createRoutingContext(routingContexts);
            msg.setAffectedPointCodes(apc);
            msg.setRoutingContexts(rc);

            ByteBuffer bb = ByteBuffer.allocateDirect(1024);
            ((M3UAMessageImpl) msg).encode(bb);
            bb.flip();
            byte[] data = new byte[bb.limit()];
            bb.get(data);

            PayloadData payloadData = new org.mobicents.protocols.api.PayloadData(data.length, data, true, true,
                    3, 0);

            clientAspFactory.getAssociation().send(payloadData);
        }
    }

    protected class Server {

        public Server() {

        }

        public void start() throws Exception {

            IpChannelType ipChannelType = IpChannelType.TCP;
            if (checkSctpEnabled())
                ipChannelType = IpChannelType.SCTP;

            // 1. Create SCTP Server
            sctpManagement.addServer(SERVER_NAME, SERVER_HOST, SERVER_PORT, ipChannelType, null);

            // 2. Create SCTP Server Association
            sctpManagement.addServerAssociation(CLIENT_HOST, CLIENT_PORT, SERVER_NAME, SERVER_ASSOCIATION_NAME, ipChannelType);

            // 3. Start Server
            sctpManagement.startServer(SERVER_NAME);

            // 4. Create RAS
            // m3ua ras create rc <rc> rk dpc <dpc> opc <opc-list> si <si-list>
            // traffic-mode {broadcast|loadshare|override} <ras-name>
            RoutingContext rc = parameterFactory.createRoutingContext(new long[] { 100l });
            TrafficModeType trafficModeType = parameterFactory.createTrafficModeType(TrafficModeType.Loadshare);
            serverAs = (AsImpl) m3uaMgmt.createAs(SERVER_TEST_AS, Functionality.SGW, ExchangeType.SE, IPSPType.CLIENT, rc,
                    trafficModeType, 1, null);

            // 5. Create RASP
            // m3ua rasp create <asp-name> <assoc-name>"
            serverAspFactory = (AspFactoryImpl) m3uaMgmt.createAspFactory(SERVER_TEST_ASP, SERVER_ASSOCIATION_NAME, false);

            // 6. Assign ASP to AS
            serverAsp = m3uaMgmt.assignAspToAs(SERVER_TEST_AS, SERVER_TEST_ASP);

            // 5. Define Route
            // Define Route
            m3uaMgmt.addRoute(clientPointCode, serverPointCode, -1, SERVER_TEST_AS);
            m3uaMgmt.addRoute(serverPointCode, clientPointCode, -1, SERVER_TEST_AS);

            // 7. Start ASP
            m3uaMgmt.startAsp(SERVER_TEST_ASP);

        }

        public void stop() throws Exception {
            m3uaMgmt.stopAsp(SERVER_TEST_ASP);

            // 2.Remove route
            m3uaMgmt.removeRoute(clientPointCode, serverPointCode, -1, SERVER_TEST_AS);
            m3uaMgmt.removeRoute(serverPointCode, clientPointCode, -1, SERVER_TEST_AS);

            m3uaMgmt.unassignAspFromAs(SERVER_TEST_AS, SERVER_TEST_ASP);

            // 4. destroy aspFactory
            m3uaMgmt.destroyAspFactory(SERVER_TEST_ASP);

            // 5. Destroy As
            m3uaMgmt.destroyAs(SERVER_TEST_AS);

            sctpManagement.removeAssociation(SERVER_ASSOCIATION_NAME);

            sctpManagement.stopServer(SERVER_NAME);
            sctpManagement.removeServer(SERVER_NAME);
        }

//        public void sendPayload() throws Exception {
//            Mtp3TransferPrimitiveFactory factory = m3uaMgmt.getMtp3TransferPrimitiveFactory();
//            Mtp3TransferPrimitive mtp3TransferPrimitive = factory.createMtp3TransferPrimitive(3, 1, 0, 1408, 123, 1,
//                    new byte[] { 1, 2, 3, 4 });
//            m3uaMgmt.sendMessage(mtp3TransferPrimitive);
//        }

        public void sendDaud() throws Exception {
            DestinationStateAudit msg = (DestinationStateAudit) messageFactory.createMessage(
                    MessageClass.SIGNALING_NETWORK_MANAGEMENT, MessageType.DESTINATION_STATE_AUDIT);
            AffectedPointCode apc = parameterFactory.createAffectedPointCode (new int [] {clientPointCode}, new short []{0});
            RoutingContext rc = parameterFactory.createRoutingContext(routingContexts);
            msg.setAffectedPointCodes(apc);
            msg.setRoutingContexts(rc);

            ByteBuffer bb = ByteBuffer.allocateDirect(1024);
            ((M3UAMessageImpl) msg).encode(bb);
            bb.flip();
            byte[] data = new byte[bb.limit()];
            bb.get(data);

            PayloadData payloadData = new org.mobicents.protocols.api.PayloadData(data.length, data, true, true,
                    3, 0);

            serverAspFactory.getAssociation().send(payloadData);
        }

    }

    private class Mtp3UserPartListenerImpl implements Mtp3UserPartListener {

        private FastList<Mtp3TransferPrimitive> receivedData = new FastList<Mtp3TransferPrimitive>();

        public FastList<Mtp3TransferPrimitive> getReceivedData() {
            return receivedData;
        }

        @Override
        public void onMtp3PauseMessage(Mtp3PausePrimitive arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onMtp3ResumeMessage(Mtp3ResumePrimitive arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onMtp3StatusMessage(Mtp3StatusPrimitive arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onMtp3TransferMessage(Mtp3TransferPrimitive value) {
            receivedData.add(value);
        }

    }
}
