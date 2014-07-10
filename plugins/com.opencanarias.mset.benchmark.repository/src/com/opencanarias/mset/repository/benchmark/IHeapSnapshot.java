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
 * Represents the status of the Java Heap in a arbitrary moment
 * during the execution of the application.
 * 
 * @author vroldan
 *
 */
public interface IHeapSnapshot {
	
	/**
	 * Indicates the time in which the snapshot was taken.
	 * The value is returned in milliseconds.
	 *   
	 * @return the time the snapshot was taken in milliseconds
	 */
	public long getTimestamp();
	
	/**
	 * Returns heap usage at the time the snapshot was taken, in megabytes.
	 * 
	 * @return a long representing the amount of heap memory used in megabytes
	 */
	public long getUsedMemory();

	/**
	 * Returns the free heap memory at the time the snapshot was taken, in megabytes.
	 * This value is relative to the size of the heap as specified 
	 * at {@link IHeapSnapshot#getTotalAvailableMemory()}
	 * 
	 * @return a long representing the amount of free memory in megabytes
	 */
	public long getFreeMemory();

	/**
	 * Returns the available heap size at the time the snapshot was taken, in megabytes, 
	 * which is always less or equal to {@link IHeapSnapshot#getMaxAvailableMemory()}
	 * 
	 * @return a long representing the heap size in megabytes
	 */
	public long getTotalAvailableMemory();
	
	/**
	 * Returns the maximum allocatable heap size in megabytes. This is constant for 
	 * a JVM instance, commonly set through the Xmx JVM parameter.
	 * 
	 * @return the maximum size in megabytes defined at the time snapshot was taken 
	 */
	public long getMaxAvailableMemory();
}
