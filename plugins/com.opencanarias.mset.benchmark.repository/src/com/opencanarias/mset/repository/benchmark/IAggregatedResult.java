/*
 * Copyright (c) 2014 Open Canarias and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    David Lutzardo Barroso - initial API and implementation
 */
package com.opencanarias.mset.repository.benchmark;

import java.util.List;

/**
 * Simple bean interface to group results {@link IBenchmarkResult}
 * Calculate statistics from group result (average, minimum, maximum and standard deviation)
 * 
 * @author dlutzardo
 * @see IBenchmarkResult
 */
public interface IAggregatedResult {
	
	/**
	 * An unmodifiable list of the {@link IBenchmarkResult}
	 * 
	 * @return 
	 */
	public List<IBenchmarkResult> getAggregatedResults(); 
	
	/** 
	 * Appends the result to this aggregation.
	 * 
	 * @param result {@link IBenchmarkResult} to be added to this aggregation
	 */
	public void addResult(IBenchmarkResult result);
	
	/**
	 * An unique and descriptive identification for this aggregate Result
	 * 
	 * @return a String representing the identification for this aggregate Result
	 */
	public String getName();
	
	/**
	 * Returns the average of the real time for the list of aggregated results
	 * @return a long representing the average amount of the real time (in milliseconds)
	 */
	public long getAvgRealTime();
	
	/**
	 * Returns the minimum value of the real time for the list of aggregated results
	 * @return a long representing the minimum value of the real time (in milliseconds)
	 */
	public long getMinRealTime();
	
	/**
	 * Returns the maximum value of the real time for the list of aggregated results
	 * @return a long representing the maximum value of the real time (in milliseconds)
	 */
	public long getMaxRealTime();
	
	/**
	 * Returns the standard deviation of the real time for all the aggregated {@link IBenchmarkResult}
	 * @return a long representing the standard deviation of the real time (in milliseconds)
	 */
	public long getRealTimeDeviation();
}
