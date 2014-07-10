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

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opencanarias.mset.internal.repository.benchmark.BenchmarkMeterImpl.HeapSnapshot.ParameterType;
import com.opencanarias.mset.repository.benchmark.IBenchmarkMeter;
import com.opencanarias.mset.repository.benchmark.IBenchmarkResult;
import com.opencanarias.mset.repository.benchmark.IHeapSnapshot;

public class BenchmarkMeterImpl implements IBenchmarkMeter {

	private static final int UNDEFINED = -1;

	private Logger logger = LoggerFactory.getLogger(getClass());

	private List<HeapSnapshot> memSnapshots = new ArrayList<HeapSnapshot>();

	private long startCPUTime = UNDEFINED;
	
	private long stopCPUTime = UNDEFINED;
	
	private long startRealTime = UNDEFINED;

	private long stopRealTime = UNDEFINED;

	private PollingThread pollingThread;

	private final long pollingInterval;
	
	private final static int DEFAULT_POLLING_INTERVAL = 1000;

	public BenchmarkMeterImpl(long pollingInterval) {
		this.pollingInterval = pollingInterval; 
	}

	public BenchmarkMeterImpl() {
		this(DEFAULT_POLLING_INTERVAL); 
	}
	
	@Override
	public long getPollingInterval() {
		return pollingInterval;
	}

	public void startMeasuring() {
		startMeasuringTime();
		startMeasuringMem();
	}

	public void stopMeasuring() {
		stopMeasuringTime();
		pollingThread.endPolling();
	}

	public IBenchmarkResult getResult() {
		BenchmarkResultImpl result = new BenchmarkResultImpl();
		result.setRealTime(calculateElapsedRealTime());
		result.setCPUTime(calculateElapsedCPUTime() / 1000000);
		result.setAverageFreeMemory(calculateAverageValue(ParameterType.FREE));
		result.setAverageUsedMemory(calculateAverageValue(ParameterType.USED));
		result.setAverageTotalAvailableMemory(calculateAverageValue(ParameterType.TOTAL));
		result.setMaxAvailableMemory(HeapSnapshot.calculateMaxAvailableMemory());
		return result;
	}

	private long calculateElapsedRealTime() {
		return stopRealTime - startRealTime;
	}
	
	private long calculateElapsedCPUTime() {
		return stopCPUTime - startCPUTime;
	}

	private long getCurrentTime() {
		return System.currentTimeMillis();
	}

	private void startMeasuringTime() {
		startRealTime = getCurrentTime();
	}

	private void stopMeasuringTime() {
		stopRealTime = getCurrentTime();		
	}

	private void startMeasuringMem() {
		memSnapshots.clear();
		pollingThread = new PollingThread(pollingInterval);
		pollingThread.start();
	}

	private long calculateAverageValue(ParameterType type) {
		long average = 0;
		HeapSnapshot[] currentSnapshots = null;
		synchronized (memSnapshots) {
			if (memSnapshots.size() == 0) {
				return 0;
			}
			currentSnapshots = memSnapshots.toArray(new HeapSnapshot[memSnapshots.size()]);
		}
		
		for (HeapSnapshot snapshot : currentSnapshots) {
			switch (type) {
			case USED:
				average += snapshot.getUsedMemory();
				break;
			case FREE:
				average += snapshot.getFreeMemory();
				break;
			case TOTAL:
				average += snapshot.getTotalAvailableMemory();
				break;
			default:
				throw new RuntimeException("Unexpected memory parameter type");
			}

		}
		return average / currentSnapshots.length;
	}

	@Override
	public String toString() {
		if (stopRealTime == UNDEFINED) {
			return "BenchmarkMeter is running: elapsed so far "
					+ (System.currentTimeMillis() - startRealTime);
		}
		return "elapsed: " + calculateElapsedRealTime() 
				+ " / average (used: " + calculateAverageValue(ParameterType.USED)
				+ " / free: " + calculateAverageValue(ParameterType.FREE)
				+ " / total: " + calculateAverageValue(ParameterType.TOTAL)
				+ " / max: " + HeapSnapshot.calculateMaxAvailableMemory()
				+ ")";
	}

	public class PollingThread extends Thread {

		private final long pollingInterval;

		private boolean shouldPoll = true;

		public PollingThread() {
			this(DEFAULT_POLLING_INTERVAL);
		}

		public PollingThread(final long pollingInterval) {
			this.pollingInterval = pollingInterval;
			// IBenchmarkResult may be requested before the Thread gets started
			// This guarantees there is at least one snapshot
			memSnapshots.add(new HeapSnapshot());
		}

		public void endPolling() {
			shouldPoll = false;
		}

		@Override
		public void run() {
			try {
				logger.trace("started polling memory");
				while (shouldPoll) {
					HeapSnapshot snapshot = new HeapSnapshot();
					memSnapshots.add(snapshot);
					sleep(pollingInterval);
				}
				logger.trace("finished polling memory");
			} catch (InterruptedException e) {
				logger.error("Polling Thread abnormally finalized", e);
				throw new RuntimeException(e);
			}
		}
	}

	public static class HeapSnapshot implements IHeapSnapshot {

		enum ParameterType {
			USED, FREE, TOTAL
		}

		private final static int MEGA_BYTE_FACTOR = 1024 * 1024;

		private static Runtime runtime = Runtime.getRuntime();

		private final long usedMemory;

		private final long freeMemory;

		private final long totalAvailableMemory;
		
		private final long timestamp;

		public HeapSnapshot() {
			usedMemory = calculateUsedMemory();
			freeMemory = calculateFreeMemory();
			totalAvailableMemory = calculateTotalAvailableMemory();
			timestamp = System.currentTimeMillis();
		}

		public static long calculateUsedMemory() {
			return (runtime.totalMemory() - runtime.freeMemory())
					/ MEGA_BYTE_FACTOR;
		}

		public static long calculateFreeMemory() {
			return runtime.freeMemory() / MEGA_BYTE_FACTOR;
		}

		public static long calculateTotalAvailableMemory() {
			return runtime.totalMemory() / MEGA_BYTE_FACTOR;
		}

		public static long calculateMaxAvailableMemory() {
			return runtime.maxMemory() / MEGA_BYTE_FACTOR;
		}

		public static HeapSnapshot getSnapshot() {
			return new HeapSnapshot();
		}

		@Override
		public long getTimestamp() {
			return timestamp;
		}

		public long getUsedMemory() {
			return usedMemory;
		}

		public long getFreeMemory() {
			return freeMemory;
		}

		public long getTotalAvailableMemory() {
			return totalAvailableMemory;
		}

		@Override
		public long getMaxAvailableMemory() {
			return calculateMaxAvailableMemory();
		}

		@Override
		public String toString() {
			return "used: " + usedMemory + " / free: " + freeMemory
					+ " / totalAvailable: " + totalAvailableMemory
					+ " / maxAvailable: " + getMaxAvailableMemory();
		}
	}
}
