/*
 * Copyright (c) 2014 Open Canarias and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  David Lutzardo Barroso - initial API and implementation
 *  
 */
package com.opencanarias.mset.internal.repository.benchmark;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.opencanarias.mset.repository.benchmark.IBenchmarkResult;
import com.opencanarias.mset.repository.benchmark.IAggregatedResult;

/**
 * @author dlutzardo
 */
public class AggregatedResultImpl implements IAggregatedResult {

	private List<IBenchmarkResult> aggregateResults = new ArrayList<IBenchmarkResult>();;
	
	private long avgRealTime = 0;

	private long minRealTime = Long.MAX_VALUE;
	
	private long maxRealTime = Long.MIN_VALUE;
	
	private long realTimeDeviation = 0;
	
	private boolean requiresRecalulation = false;
	
	@Override
	public List<IBenchmarkResult> getAggregatedResults() {		
		return Collections.unmodifiableList(aggregateResults);
	}
	
	@Override
	public void addResult(IBenchmarkResult result) {
		aggregateResults.add(result);
		requiresRecalulation = true;
	}
		
	@Override
	public long getAvgRealTime() {		
		if (requiresRecalulation) {
			recalculateAggregation();
		}
		return avgRealTime;
	}
	
	@Override
	public String getName() {
		if (getAggregatedResults().size() == 0 )
			return "NoAggregatedResults"; //$NON-NLS-1$
		return getAggregatedResults().get(0).getBenchmarkCase().getName() + " | " + getAggregatedResults().get(0).getModelRepository().getName(); //$NON-NLS-1$
	}

	@Override
	public long getMinRealTime() {
		if (requiresRecalulation) {
			recalculateAggregation();
		}
		return minRealTime;
	}

	@Override
	public long getMaxRealTime() {
		if (requiresRecalulation) {
			recalculateAggregation();
		}
		return maxRealTime;
	}

	@Override
	public long getRealTimeDeviation() {
		if (requiresRecalulation) {
			recalculateAggregation();
		}
		return realTimeDeviation;
	}

	@Override
	public String toString() {
		return MessageFormat.format("[{0}]: iterations: {1} / avgRealTime: {2} / realTimeDev: {3} / minRealTime: {4} / maxRealTime: {5}",  //$NON-NLS-1$
				getName(), getAggregatedResults().size(), getAvgRealTime(), getRealTimeDeviation(), getMinRealTime(), getMaxRealTime());
	}

	private void recalculateAggregation() {
		avgRealTime = calculateAvgRealTime();
		minRealTime = calculateMinRealTime();
		maxRealTime = calculateMaxRealTime();
		realTimeDeviation = calculateRealTimeDeviation(avgRealTime);		
		requiresRecalulation = false;		
	}

	private long calculateRealTimeDeviation(long averageRealTime) {
		int count = getAggregatedResults().size();
		if (count < 2)
			return 0;
		double sum = 0;
		for(IBenchmarkResult result : getAggregatedResults()) {
			long value = result.getRealTime();
			sum += (value - averageRealTime) * (value - averageRealTime);
		}
		return (long) Math.sqrt(sum / (count - 1));
	}

	private long calculateAvgRealTime() {
		long summatory = 0;
		for (final IBenchmarkResult result : aggregateResults) {
			summatory += result.getRealTime();
		}
		return summatory / aggregateResults.size();
	}
	
	private long calculateMinRealTime() {
		long minimalValue = Long.MAX_VALUE;
		for (final IBenchmarkResult result : aggregateResults) {
			if (result.getRealTime() < minimalValue) {
				minimalValue = result.getRealTime(); 
			}
		}
		return minimalValue;
	}
	
	private long calculateMaxRealTime() {
		long maximumValue = Long.MIN_VALUE;
		for (final IBenchmarkResult result : aggregateResults) {
			if (result.getRealTime() > maximumValue) {
				maximumValue = result.getRealTime(); 
			}
		}
		return maximumValue;
	}
}
