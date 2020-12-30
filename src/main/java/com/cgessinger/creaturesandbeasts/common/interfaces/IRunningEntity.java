package com.cgessinger.creaturesandbeasts.common.interfaces;

public interface IRunningEntity
{
	void setRunning(boolean running);

	boolean isRunning();

	/**
	 * @return the speed multiplier at which the entity will start running
	 */
	double getRunThreshold();
}
