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
package com.opencanarias.mset.internal.repository.benchmark;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;

import com.opencanarias.mset.repository.benchmark.IBenchmarkCase;
import com.opencanarias.mset.repository.benchmark.IBenchmarkResult;
import com.opencanarias.mset.repository.benchmark.IHeapSnapshot;
import com.opencanarias.mset.repository.benchmark.IModelProperties;
import com.opencanarias.mset.repository.benchmark.IModelRepository;

public class BenchmarkResultImpl implements IBenchmarkResult {

	private long cpuTime;
	
	private long realTime;
	
	private long averageUsedMemory;
	
	private long averageFreeMemory;
	
	private long averageTotalAvailableMemory;
	
	private long maxAvailableMemory;	
	
	private IBenchmarkCase benchmarkCase;
	
	private IModelRepository repository;
	
	private IModelProperties modelProperties;
	
	private List<IHeapSnapshot> heapSnapshots;
	
	private boolean failed;
	
	@Override
	public List<IHeapSnapshot> getHeapSnapshots() {
		return heapSnapshots;
	}
	
	public void setHeapSnapshots(List<IHeapSnapshot> snapshots) {
		heapSnapshots = Collections.unmodifiableList(snapshots);
	}

	@Override
	public long getCPUTime() {
		return cpuTime;
	}

	public void setCPUTime(long cpuTime) {
		this.cpuTime = cpuTime;
	}

	@Override
	public long getRealTime() {
		return realTime;
	}

	public void setRealTime(long realTime) {
		this.realTime = realTime;
	}

	@Override
	public long getAverageUsedMemory() {
		return averageUsedMemory;
	}

	public void setAverageUsedMemory(long averageUsedMemory) {
		this.averageUsedMemory = averageUsedMemory;
	}

	@Override
	public long getAverageFreeMemory() {
		return averageFreeMemory;
	}

	public void setAverageFreeMemory(long averageFreeMemory) {
		this.averageFreeMemory = averageFreeMemory;
	}

	@Override
	public long getAverageTotalAvailableMemory() {
		return averageTotalAvailableMemory;
	}

	public void setAverageTotalAvailableMemory(long averageTotalAvailableMemory) {
		this.averageTotalAvailableMemory = averageTotalAvailableMemory;
	}

	@Override
	public long getMaxAvailableMemory() {
		return maxAvailableMemory;
	}

	public void setMaxAvailableMemory(long maxAvailableMemory) {
		this.maxAvailableMemory = maxAvailableMemory;
	}	
	
	public IModelRepository getModelRepository() {
		return repository;
	}

	public void setModelRepository(IModelRepository repository) {
		this.repository = repository;
	}

	public IBenchmarkCase getBenchmarkCase() {
		return benchmarkCase;
	}

	public void setBenchmarkCase(IBenchmarkCase benchmarkCase) {
		this.benchmarkCase = benchmarkCase;
	}

	@Override
	public IModelProperties getModelProperties() {
		return modelProperties;
	}

	public void setModelProperties(IModelProperties modelProperties) {
		this.modelProperties = modelProperties;
	}
	
	@Override
	public String toString() {
		return MessageFormat.format("[{0}-{1}] modelSize: {8} / realTime: {3} / avgUsed: {4} / avgFree: {5} / avgTotalAvailable: {6} / avgMaxAvailable: {7}", 
				getBenchmarkCase() != null ? getBenchmarkCase().getName() : "NoCase", 
				getModelRepository() != null ? getModelRepository().getName() : "NoRepository", 
				getCPUTime(), getRealTime(), getAverageUsedMemory(), 
				getAverageFreeMemory(), getAverageTotalAvailableMemory(), getMaxAvailableMemory(),
				getModelProperties() != null ? getModelProperties().getCount() : "NoModel");
	}

	public boolean isFailed() {
		return failed;
	}

	public void setBenchmarkFailed(boolean benchmarkFailed) {
		this.failed = benchmarkFailed;
	}
}
