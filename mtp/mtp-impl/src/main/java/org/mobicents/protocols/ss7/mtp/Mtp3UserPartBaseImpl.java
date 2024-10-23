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

package org.mobicents.protocols.ss7.mtp;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

// lic dep 1

/**
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public abstract class Mtp3UserPartBaseImpl implements Mtp3UserPart {

    private static final Logger logger = LogManager.getLogger(Mtp3UserPartBaseImpl.class);

    private static final String LICENSE_PRODUCT_NAME = "Mobicents-jSS7";

    //Type of Executors that can be configured by System properties.
    public static final String MTP_3_EXECUTOR_TYPE_PROPERTY_KEY = "mtp3.executor.type";
    private static final String EXECUTOR_FORK_JOIN ="FORK_JOIN";
    private static final String EXECUTOR_FIXED ="FIXED";
    private static final String EXECUTOR_CACHED_LINKED_Q ="CACHED_LQ";
    private static final String EXECUTOR_CACHED_SYNCHRONIZED_Q ="CACHED_SQ";

    private int maxSls = 32;
    private int slsFilter = 0x1F;

    // The count of threads that will be used for message delivering to
    // Mtp3UserPartListener's
    // For single thread model this value should be equal 1
    // TODO: make it configurable
    protected int deliveryTransferMessageThreadCount = Runtime.getRuntime().availableProcessors() * 4;

    protected boolean isStarted = false;

    private CopyOnWriteArrayList<Mtp3UserPartListener> userListeners = new CopyOnWriteArrayList<Mtp3UserPartListener>();
    // a thread pool for delivering Mtp3TransferMessage messages
    private ExecutorService msgDeliveryExecutor;
    // a thread for delivering PAUSE, RESUME and STATUS messages
    private ExecutorService msgDeliveryExecutorSystem;
    private int[] slsTable = null;

    private RoutingLabelFormat routingLabelFormat = RoutingLabelFormat.ITU;

    private Mtp3TransferPrimitiveFactory mtp3TransferPrimitiveFactory = null;

    private boolean useLsbForLinksetSelection = true;

    private final String productName;

    public Mtp3UserPartBaseImpl(String productName) {
        if(productName == null){
            this.productName = LICENSE_PRODUCT_NAME;
        } else {
            this.productName = productName;
        }
    }

    public int getDeliveryMessageThreadCount() {
        return this.deliveryTransferMessageThreadCount;
    }

    public void setDeliveryMessageThreadCount(int deliveryMessageThreadCount) {
        if (deliveryMessageThreadCount > 0)
            this.deliveryTransferMessageThreadCount = deliveryMessageThreadCount;
    }

    @Override
    public void addMtp3UserPartListener(Mtp3UserPartListener listener) {
        this.userListeners.add(listener);
    }

    @Override
    public void removeMtp3UserPartListener(Mtp3UserPartListener listener) {
        this.userListeners.remove(listener);
    }

    /*
     * For classic MTP3 this value is maximum SIF length minus routing label length. This method should be overloaded if
     * different message length is supported.
     */
    @Override
    public int getMaxUserDataLength(int dpc) {
        switch (this.routingLabelFormat) {
            case ITU:
                // For PC_FORMAT_14, the MTP3 Routing Label takes 4 bytes - OPC/DPC
                // = 16 bits each and SLS = 4 bits
                return 272 - 4;
            case ANSI_Sls8Bit:
                // For PC_FORMAT_24, the MTP3 Routing Label takes 6 bytes - OPC/DPC
                // = 24 bits each and SLS = 8 bits
                return 272 - 7;
            default:
                // TODO : We don't support rest just yet
                return -1;

        }
    }

    @Override
    public RoutingLabelFormat getRoutingLabelFormat() {
        return this.routingLabelFormat;
    }

    @Override
    public void setRoutingLabelFormat(RoutingLabelFormat routingLabelFormat) {
        this.routingLabelFormat = routingLabelFormat;
    }

    @Override
    public Mtp3TransferPrimitiveFactory getMtp3TransferPrimitiveFactory() {
        return this.mtp3TransferPrimitiveFactory;
    }

    @Override
    public boolean isUseLsbForLinksetSelection() {
        return useLsbForLinksetSelection;
    }

    @Override
    public void setUseLsbForLinksetSelection(boolean useLsbForLinksetSelection) {
        this.useLsbForLinksetSelection = useLsbForLinksetSelection;
    }

    public void start() throws Exception {
        // lic dep 2

        if (this.isStarted)
            return;

        if (!(this.routingLabelFormat == RoutingLabelFormat.ITU || this.routingLabelFormat == RoutingLabelFormat.ANSI_Sls8Bit || this.routingLabelFormat == RoutingLabelFormat.ANSI_Sls5Bit)) {
            throw new Exception("Invalid PointCodeFormat set. We support only ITU or ANSI now");
        }

        switch (this.routingLabelFormat) {
            case ITU:
                this.maxSls = 16;
                this.slsFilter = 0x0f;
                break;
            case ANSI_Sls5Bit:
                this.maxSls = 32;
                this.slsFilter = 0x1f;
                break;
            case ANSI_Sls8Bit:
                this.maxSls = 256;
                this.slsFilter = 0xff;
                break;
            default:
                throw new Exception("Invalid SLS length");
        }

        this.slsTable = new int[maxSls];

        this.mtp3TransferPrimitiveFactory = new Mtp3TransferPrimitiveFactory(this.routingLabelFormat);

        this.createSLSTable(this.deliveryTransferMessageThreadCount);


        String defaultExecutorType = EXECUTOR_FIXED;
        String executorTypeParameter = System.getProperty(MTP_3_EXECUTOR_TYPE_PROPERTY_KEY);
        if (executorTypeParameter == null ||
                !Arrays.asList(EXECUTOR_FIXED, EXECUTOR_FORK_JOIN, EXECUTOR_CACHED_LINKED_Q, EXECUTOR_CACHED_SYNCHRONIZED_Q)
                        .contains(executorTypeParameter)) {
            executorTypeParameter = defaultExecutorType;
        }

        if (EXECUTOR_FIXED.equals(executorTypeParameter)) {
            buildFixedExecutor();
        } else if (EXECUTOR_FORK_JOIN.equals(executorTypeParameter)) {
            buildForkJoinExecutor();
        } else if (EXECUTOR_CACHED_SYNCHRONIZED_Q.equals(executorTypeParameter)) {
            buildCachedSQExecutor();
        } else if (EXECUTOR_CACHED_LINKED_Q.equals(executorTypeParameter)) {
            buildCachedLQExecutor();
        }else{
            logger.error("Invalid executor determined!! Choosing default one.");
            buildFixedExecutor();
        }

        this.msgDeliveryExecutorSystem = Executors.newSingleThreadScheduledExecutor(
                //new DefaultThreadFactory("Mtp3-DeliveryExecutorSystem")
                );

        this.isStarted = true;
    }

    private void buildCachedLQExecutor() {
        logger.info("Using Cached executor with LinkedBlockingQueue");
        //Cache thread pool with LinkedBlockingQueue, starting in the half of the configured thread-pool.
        this.msgDeliveryExecutor = new ThreadPoolExecutor(
                this.deliveryTransferMessageThreadCount / 2,
                this.deliveryTransferMessageThreadCount,
                60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(1_048_576), //Set 2^20; Default value Integer.MAX_VALUE
                new ThreadFactory() {
                    private AtomicInteger idx = new AtomicInteger();

                    @Override
                    public Thread newThread(Runnable runnable) {
                        return new Thread(runnable, "Mtp3ChLBQExecutor-" + idx.getAndIncrement());
                    }
                });
    }

    private void buildCachedSQExecutor() {
        logger.info("Using Cached executor with SynchronousQueue");
        //Cache thread pool with SynchronousQueue, starting in the half of the configured thread-pool.
        this.msgDeliveryExecutor = new ThreadPoolExecutor(
                this.deliveryTransferMessageThreadCount / 2,
                this.deliveryTransferMessageThreadCount,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>(),
                new ThreadFactory() {
                    private AtomicInteger idx = new AtomicInteger();

                    @Override
                    public Thread newThread(Runnable runnable) {
                        return new Thread(runnable, "Mtp3ChSQExecutor-" + idx.getAndIncrement());
                    }
                });
    }

    private void buildForkJoinExecutor() {
        logger.info("Using ForkJoinPool executor");
        final ForkJoinPool.ForkJoinWorkerThreadFactory factory =
                new ForkJoinPool.ForkJoinWorkerThreadFactory() {
                    @Override
                    public ForkJoinWorkerThread newThread(ForkJoinPool pool) {
                        final ForkJoinWorkerThread worker = ForkJoinPool.defaultForkJoinWorkerThreadFactory.newThread(pool);
                        worker.setName("Mtp3FJExecutor-" + worker.getPoolIndex());
                        return worker;
                    }
                };
        this.msgDeliveryExecutor = new ForkJoinPool(deliveryTransferMessageThreadCount,
                factory, null, true);
    }

    private void buildFixedExecutor() {
        logger.info("Using FixedThreadPool executor");
        this.msgDeliveryExecutor = Executors.newFixedThreadPool(this.deliveryTransferMessageThreadCount,
                new ThreadFactory() {
                    private AtomicInteger idx = new AtomicInteger();

                    @Override
                    public Thread newThread(Runnable runnable) {
                        return new Thread(runnable, "Mtp3FxExecutor-" + idx.getAndIncrement());
                    }
                });
    }

    public void stop() throws Exception {

        if (!this.isStarted)
            return;

        this.isStarted = false;

        this.msgDeliveryExecutor.shutdown();
        this.msgDeliveryExecutorSystem.shutdown();
    }

    /**
     * Deliver an incoming message to the local user
     *
     * @param msg
     * @param effectiveSls For the thread selection (for message delivering)
     */
    AtomicInteger roundRobbing = new AtomicInteger(0);
    protected void sendTransferMessageToLocalUser(Mtp3TransferPrimitive msg, int seqControl) {
        if (this.isStarted) {
            if(logger.isTraceEnabled()) {
                long msAgo = System.currentTimeMillis() - msg.getCreationTime();
                if (msAgo > 5)
                    logger.trace(String.format("Adding to executor message created [%d]ms ago", msAgo));
            }
            MsgTransferDeliveryHandler hdl = new MsgTransferDeliveryHandler(msg);
            this.msgDeliveryExecutor.execute(hdl);
        } else {
            logger.error(String.format(
                    "Received Mtp3TransferPrimitive=%s but Mtp3UserPart is not started. Message will be dropped", msg));
        }
    }

    protected void sendPauseMessageToLocalUser(Mtp3PausePrimitive msg) {
        if (this.isStarted) {
            MsgSystemDeliveryHandler hdl = new MsgSystemDeliveryHandler(msg);
            this.msgDeliveryExecutorSystem.execute(hdl);
        } else {
            logger.error(String.format(
                    "Received Mtp3PausePrimitive=%s but MTP3 is not started. Message will be dropped", msg));
        }
    }

    protected void sendResumeMessageToLocalUser(Mtp3ResumePrimitive msg) {
        logger.info("sendResumeMessageToLocalUser()");
        if (this.isStarted) {
            logger.info("sendResumeMessageToLocalUser() isStarted");
            MsgSystemDeliveryHandler hdl = new MsgSystemDeliveryHandler(msg);
            this.msgDeliveryExecutorSystem.execute(hdl);
        } else {
            logger.info("sendResumeMessageToLocalUser() no Started");
            logger.error(String.format(
                    "Received Mtp3ResumePrimitive=%s but MTP3 is not started. Message will be dropped", msg));
        }
    }

    protected void sendStatusMessageToLocalUser(Mtp3StatusPrimitive msg) {
        if (this.isStarted) {
            MsgSystemDeliveryHandler hdl = new MsgSystemDeliveryHandler(msg);
            this.msgDeliveryExecutorSystem.execute(hdl);
        } else {
            logger.error(String.format(
                    "Received Mtp3StatusPrimitive=%s but MTP3 is not started. Message will be dropped", msg));
        }
    }

    private void createSLSTable(int minimumBoundThread) {
        int stream = 0;
        for (int i = 0; i < maxSls; i++) {
            if (stream >= minimumBoundThread) {
                stream = 0;
            }
            slsTable[i] = stream++;
        }
    }

    private class MsgTransferDeliveryHandler implements Runnable {

        private Mtp3TransferPrimitive msg;

        public MsgTransferDeliveryHandler(Mtp3TransferPrimitive msg) {
            this.msg = msg;
        }

        @Override
        public void run() {
            if (isStarted) {
                if(logger.isTraceEnabled()) {
                    long msAgo = System.currentTimeMillis() - msg.getCreationTime();
                    if(msAgo > 5)
                        logger.trace(String.format("Processing message received [%d]ms ago.", msAgo));
                }
                try {
                    for (Mtp3UserPartListener lsn : userListeners) {
                        lsn.onMtp3TransferMessage(this.msg);
                    }
                } catch (Exception e) {
                    logger.error("Exception while delivering a system messages to the MTP3-user: " + e.getMessage(), e);
                } catch (Throwable e){
                    logger.error("Throwable while delivering a system messages to the MTP3-user: " + e.getMessage(), e);
                }
            } else {
                logger.error(String.format(
                        "Received Mtp3TransferPrimitive=%s but Mtp3UserPart is not started. Message will be dropped", msg));
            }
        }
    }

    private class MsgSystemDeliveryHandler implements Runnable {

        Mtp3Primitive msg;

        public MsgSystemDeliveryHandler(Mtp3Primitive msg) {
            this.msg = msg;
        }

        @Override
        public void run() {
            logger.info("MsgSystemDeliveryHandler() Handling: "+msg.toString());
            if (isStarted) {
                try {
                    for (Mtp3UserPartListener lsn : userListeners) {
                        if (this.msg.getType() == Mtp3Primitive.PAUSE)
                            lsn.onMtp3PauseMessage((Mtp3PausePrimitive) this.msg);
                        if (this.msg.getType() == Mtp3Primitive.RESUME)
                            lsn.onMtp3ResumeMessage((Mtp3ResumePrimitive) this.msg);
                        if (this.msg.getType() == Mtp3Primitive.STATUS)
                            lsn.onMtp3StatusMessage((Mtp3StatusPrimitive) this.msg);
                    }
                } catch (Exception e) {
                    logger.error("Exception while delivering a payload messages to the MTP3-user: " + e.getMessage(), e);
                }
            } else {
                logger.error(String.format(
                        "Received Mtp3Primitive=%s but Mtp3UserPart is not started. Message will be dropped", msg));
            }
        }
    }
}
