package com.cgessinger.creaturesandbeasts.util;

public interface IRunningEntity {
    boolean isRunning();

    void setRunning(boolean running);

    /**
     * @return the speed multiplier at which the entity will start running
     */
    double getRunThreshold();
}
