package org.mobicents.protocols.ss7.tcap;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.sccp.message.SccpDataMessage;

/**
 * @author ajimenez, created on 23/07/19.
 */
public class TimeFilterImpl {
    private static final Logger logger = Logger.getLogger(TimeFilterImpl.class);


    private long maxAgeForBeginMessageMilliseconds;
    private int rampDurationInSeconds;
    private int rampMessagesByStep;
    private long rampFirstMessageTimeStamp;
    private AtomicInteger [] rampMessageReceivedInStep;

    public TimeFilterImpl(int maxAgeForBeginMessageMilliseconds){
        this.maxAgeForBeginMessageMilliseconds = maxAgeForBeginMessageMilliseconds;
    }

    public TimeFilterImpl(int rampDurationInSeconds, int rampMessagesByStep){
        this.rampDurationInSeconds = rampDurationInSeconds;
        this.rampMessagesByStep = rampMessagesByStep;
        if(this.rampDurationInSeconds > 0){
            rampMessageReceivedInStep = new AtomicInteger[rampDurationInSeconds];
        }
    }

    private ByteBuffer buffer = ByteBuffer.allocate(16);
    private synchronized int getDialogId(byte[] bytes) {
        //Position of otid usually is on 4ª o 5ª position of TCAP packet.
        if(bytes[3] == 4) {
            buffer.put(bytes, 4, 4);
        }else if(bytes[4] == 4){
            buffer.put(bytes, 5, 4);
        }else{
            return -1;
        }
        buffer.flip();//need flip
        int result = buffer.getInt(); //32bits
        buffer.clear();
        return result;
    }

    public boolean isInTime(SccpDataMessage message){
        boolean result = isInTimeFastDropping(message);
        result &= isInTimeRampControl(message);
        return result;
    }

    private boolean isInTimeFastDropping(SccpDataMessage message) {
        boolean result = true;
        if(maxAgeForBeginMessageMilliseconds > 0) {
            long diff;
            if ((diff = (System.currentTimeMillis() - message.getReceivedTimeStamp())) > maxAgeForBeginMessageMilliseconds) {
                if(logger.isEnabledFor(Level.WARN))
                    logger.warn(String.format("Dropping message, age: %d ms > %d ms, otid %d", diff,
                        maxAgeForBeginMessageMilliseconds, getDialogId(message.getData())));
                result = false;
            }
        }
        if(message.getReceivedTimeStamp() == 0){
            logger.debug("isInTimeFastDropping(): Message without time-stamp. It should be a buckle, dropping. " +
                    "Check destination PC. otid: "+getDialogId(message.getData()));
            result = false;
        }
        return result;
    }


    private boolean isInTimeRampControl(SccpDataMessage message){
        boolean result = true;
        if(rampMessagesByStep > 0 && rampDurationInSeconds > 0) {
            if(rampFirstMessageTimeStamp == 0){
                rampFirstMessageTimeStamp = System.currentTimeMillis();
            }else{
                int seconds = (int) ((System.currentTimeMillis() - rampFirstMessageTimeStamp)/1000L);
                if(seconds < rampDurationInSeconds){
                    int current = rampMessageReceivedInStep[seconds].getAndIncrement();
                    if (rampMessagesByStep * (seconds + 1) < current){
                        result = false;
                        if(logger.isEnabledFor(Level.WARN))
                            logger.warn(String.format("Dropping message by ramp: Second %d, received %d, otid %d", seconds, current,
                                    getDialogId(message.getData())));
                    }else{
                        rampMessageReceivedInStep[seconds].getAndIncrement();
                    }
                }else if (rampMessageReceivedInStep != null){
                    //Free resources.
                    rampMessageReceivedInStep = null;
                }
            }
        }

        return result;
    }


    public void setMaxAgeForBeginMessageMilliseconds(long maxAgeForBeginMessageMilliseconds) {
        this.maxAgeForBeginMessageMilliseconds = maxAgeForBeginMessageMilliseconds;
    }

    public void setRampDurationInSeconds(int rampDurationInSeconds) {
        this.rampDurationInSeconds = rampDurationInSeconds;
        if(this.rampDurationInSeconds > 0){
            rampMessageReceivedInStep = new AtomicInteger[rampDurationInSeconds];
            for(int i = 0; i < rampDurationInSeconds; i++){
                rampMessageReceivedInStep[i] = new AtomicInteger();
            }
        }
    }

    public void setRampMessagesIncrementBySecond(int rampMessagesByStep) {
        this.rampMessagesByStep = rampMessagesByStep;
    }
}
