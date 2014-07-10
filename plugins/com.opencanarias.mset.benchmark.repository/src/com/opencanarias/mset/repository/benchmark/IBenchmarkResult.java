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

import java.util.List;

/**
 * Simple bean interface to model performance information of
 * a measurement performed by some other entity
 * (this framework proposes {@link IBenchmarkMeter} as the 
 * responsible for this concern).
 * <p>
 * This relates all actors involved in the benchmark, such as
 * the {@link IModelRepository}, the {@link IBenchmarkCase} that defines
 * the logic under testing, and the {@link IModelProperties} 
 * defining the characteristics of the input.
 * <p>
 * To ease interpretation of the results, a result may generate
 * a CSV representation of its attributes.
 *  
 * @author vroldan
 * @see IBenchmarkMeter
 * @see IBenchmarkCase
 * @see IModelProperties
 */
public interface IBenchmarkResult {
	
	/**
	 * Returns in milliseconds the amount of CPU Time the
	 * {@link IBenchmarkCase#executeBenchmark(IBenchmarkData)} logic required
	 * to be fully executed.
	 * 
	 * @return a long value representing the CPU time (in milliseconds) 
	 *         required to perform the {@link IBenchmarkCase} main logic
	 */
	public long getCPUTime();
	
	/**
	 * Returns in milliseconds the amount of real time (or wall time) the  
	 * {@link IBenchmarkCase#executeBenchmark(IBenchmarkData)} logic required
	 * to be fully executed.
	 * 
	 * @return a long value representing the real time (in milliseconds) 
	 *         required to perform the {@link IBenchmarkCase} main logic
	 */
	public long getRealTime();
	
	/**
	 * Returns the average of heap usage during the course of the
	 * logic implemented at {@link IBenchmarkCase#executeBenchmark(IBenchmarkData)}
	 * in megabytes.
	 * <p>
	 * If a more detailed insight of the behavior of the heap during the {@link IBenchmarkCase}
	 * is required, please take a look at {@link IBenchmarkResult#getHeapSnapshots()} 
	 * 
	 * @return a long representing the average amount of memory used in megabytes
	 */
	public long getAverageUsedMemory();
	
	/**
	 * Returns an average of the free heap memory during the course of the
	 * logic implemented at {@link IBenchmarkCase#executeBenchmark(IBenchmarkData)}
	 * in megabytes. This value is relative to the size of the heap during the execution of
	 * the logic to be measured, as specified at {@link IBenchmarkResult#getAverageTotalAvailableMemory()}
	 * <p>
	 * If a more detailed insight of the behavior of the heap during the {@link IBenchmarkCase}
	 * is required, please take a look at {@link IBenchmarkResult#getHeapSnapshots()} 
	 * 
	 * @return a long representing the average amount of free memory in megabytes
	 */
	public long getAverageFreeMemory();

	/**
	 * Returns an average of the available heap size during the course of the
	 * logic implemented at {@link IBenchmarkCase#executeBenchmark(IBenchmarkData)}
	 * in megabytes, which is always less or equal to {@link IBenchmarkResult#getMaxAvailableMemory()}
	 * <p>
	 * If a more detailed insight of the behavior of the heap during the {@link IBenchmarkCase}
	 * is required, please take a look at {@link IBenchmarkResult#getHeapSnapshots()} 
	 * 
	 * @return a long representing the average of the heap size in megabytes
	 */
	public long getAverageTotalAvailableMemory();
	
	/**
	 * Returns the maximum allocatable heap size in megabytes. This is constant for 
	 * a JVM instance, commonly set through the Xmx JVM parameter.
	 * 
	 * @return the maximum size in megabytes for the current Java process instance. 
	 */
	public long getMaxAvailableMemory();
	
	/**
	 * Returns all the captured {@link IHeapSnapshot} during the course
	 * of the {@link IBenchmarkCase} being measured. These {@link IHeapSnapshot snapshots}
	 * may be used for further analysis (i.e., peaks, minimums...).
	 * 
	 * @return The list of {@link IHeapSnapshot} gathered during the execution of the 
	 *         {@link IBenchmarkCase}.
	 */
	public List<IHeapSnapshot> getHeapSnapshots();
	
	/**
	 * The {@link IModelRepository} used by this {@link IBenchmarkCase} occurrence.
	 * 
	 * @return the model repository used in this particular {@link IBenchmarkCase} execution. 
	 */
	public IModelRepository getModelRepository();
	
	/**
	 * The {@link IBenchmarkCase} whose execution has been measured and is 
	 * depicted in this {@link IBenchmarkResult} instance.
	 *  
	 * @return the {@link IBenchmarkCase} instance executed that lead to this result.
	 */
	public IBenchmarkCase getBenchmarkCase();
	
	/**
	 * The properties of the model used as input for the {@link IBenchmarkCase}
	 * 
	 * @return the properties of the model used as input for the {@link IBenchmarkCase} 
	 */
	public IModelProperties getModelProperties();
	
	/**
	 * Returns whether the execution of the {@link IBenchmarkCase} succeeded with no errors.
	 * 
	 * @return true if the {@link IBenchmarkCase} finished successfully 
	 */
	public boolean isFailed();
}
