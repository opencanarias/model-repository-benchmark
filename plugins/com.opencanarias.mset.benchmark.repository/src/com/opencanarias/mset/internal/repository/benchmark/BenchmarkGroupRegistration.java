package com.opencanarias.mset.internal.repository.benchmark;

import com.opencanarias.mset.repository.benchmark.IBenchmarkGroup;
import com.opencanarias.mset.repository.benchmark.IBenchmarkGroupRegistry;

/**
 * This class servers to inject {@link IBenchmarkGroup} instances 
 * (registered using OSGi services) into {@link IBenchmarkGroupRegistry}
 * <p>
 * The component definition is found on OSGI-INF/benchmarkGroupRegistration.xml
 * 
 * @author vroldan
 *
 */
public class BenchmarkGroupRegistration {
	
	public void addBenchmarkGroup(IBenchmarkGroup group) {
		IBenchmarkGroupRegistry.INSTANCE.addBenchmarkGroup(group);
	}
	
	public void removeBenchmarkGroup(IBenchmarkGroup group) {
		IBenchmarkGroupRegistry.INSTANCE.removeBenchmarkGroup(group);
	}
	
}
