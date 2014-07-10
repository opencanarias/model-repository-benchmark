/*
 * Copyright (c) 2014 Open Canarias and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Victor Roldan Betancort - initial API and implementation
 */
package com.opencanarias.mset.repository.benchmark;

/**
 * This interface serves to perform measurements for a given execution context.
 * Performance information is modeled as an {@link IBenchmarkResult} instance.
 * <p>
 * When the meter is started, a set of measures takes place. Some measures are based
 * on statistical calculation over a set of samples (averages, peaks, minimums...), and 
 * therefore data gathering takes place on an specific frequency basis, with the purpose
 * of allowing the user to control the accuracy / overhead ratio derived from sampling. 
 * <p>
 * For instance, heap measurements require obtaining snapshots of its status during
 * the execution of a process.
 * 
 * @author vroldan
 * @see IBenchmarkResult
 */
public interface IBenchmarkMeter {

	/**
	 * Returns the time it takes before another performance 
	 * snapshot is taken. This amount is specified in milliseconds.
	 *   
	 * @return an amount of time between snapshots, in milliseconds 
	 */
	public long getPollingInterval();

	/**
	 * Commands the meter to start collecting performance information.
	 * Subsequent calls on an started meter are ignored.
	 */
	public void startMeasuring();

	/**
	 * Commands the meter to stop collecting performance information.
	 * Subsequent calls on an stopped meter are ignored.
	 */
	public void stopMeasuring();

	/**
	 * Models all the performance information collected on the previous
	 * run into an {@link IBenchmarkResult} instance. If the meter
	 * hasn't been previously started and stopped afterwards, no
	 * information would have been collected, and null will be returned. 
	 * 
	 * @return the information collected described into an {@link IBenchmarkResult} 
	 *         instance for the last run. If no previous run has taken place,
	 *         null is returned.
	 */
	public IBenchmarkResult getResult();

}