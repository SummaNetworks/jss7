package org.mobicents.protocols.ss7.tcap;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.sccp.message.SccpMessage;

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
        this.maxAgeForBeginMessageMilliseconds = maxAgeForBeginMessageMilliseconds;
        this.rampDurationInSeconds = rampDurationInSeconds;
        this.rampMessagesByStep = rampMessagesByStep;
        if(this.rampDurationInSeconds > 0){
            rampMessageReceivedInStep = new AtomicInteger[rampDurationInSeconds];
        }
    }


    public boolean isInTime(SccpMessage message){
        boolean result = isInTimeFastDroppig(message);
        result &= isInTimeRampControl();
        return result;
    }

    private boolean isInTimeFastDroppig(SccpMessage message) {
        boolean result = true;
        if(maxAgeForBeginMessageMilliseconds > 0) {
            long diff;
            if ((diff = (System.currentTimeMillis() - message.getReceivedTimeStamp())) > maxAgeForBeginMessageMilliseconds) {
                logger.debug(String.format("Dropping message, age: %d ms > %d ms", diff, maxAgeForBeginMessageMilliseconds));
                result = false;
            }
        }
        return result;
    }


    private boolean isInTimeRampControl(){
        boolean result = true;
        if(rampMessagesByStep > 0 && rampDurationInSeconds > 0) {
            if(rampFirstMessageTimeStamp == 0){
                rampFirstMessageTimeStamp = System.currentTimeMillis();
            }else{
                int seconds = (int) ((System.currentTimeMillis() - rampFirstMessageTimeStamp)/1000L);
                if(seconds < rampDurationInSeconds){
                    int current = rampMessageReceivedInStep[seconds].get();
                    if (rampMessagesByStep * seconds < current){
                        result = false;
                        logger.debug(String.format("Dropping message by ramp: Second %d, received %d", seconds, current));
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
