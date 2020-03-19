package com.summanetworks.topic;

/**
 * @author ajimenez, created on 15/3/20.
 */
public class TopicConfig {

    private static TopicConfig instance;


    int maxTCPFrameSize;


    public static TopicConfig getInstance(){
        return instance;
    }


    public int getMaxTCPFrameSize() {
        return maxTCPFrameSize;
    }
}
