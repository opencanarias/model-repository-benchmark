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

import com.opencanarias.mset.internal.repository.benchmark.BenchmarkGroupRegistryImpl;

/**
 * This registry intends to be a place to contribute {@link IBenchmarkGroup} instances
 * that will usually contain {@link IBenchmarkCase} instances.
 * <p>
 * The registry serves as a central place to get references to {@link IBenchmarkCase} so
 * that any client can use it to perform benchmarks against {@link IModelRepository}.
 * <p>
 * Users may also be interested in the {@link IModelRepositoryRegistry} to get
 * contributed {@link IModelRepository} instances.
 *  
 * @author vroldan
 * @see IModelRepositoryRegistry
 * @see IBenchmarkGroup
 */
public interface IBenchmarkGroupRegistry {
	
	/**
	 * Singleton instance for an {@link IBenchmarkGroupRegistry}. {@link IBenchmarkGroup}
	 * instances are contributed to this registry through Eclipse's extension point
	 * mechanism 
	 */
	public IBenchmarkGroupRegistry INSTANCE = new BenchmarkGroupRegistryImpl();
	
	/**
	 * Returns an unmodifiable list of {@link IBenchmarkGroup} instances registered.
	 *  
	 * @return an unmodifiable list of {@link IBenchmarkGroup group} instances
	 */
	public List<IBenchmarkGroup> getAllBenchmarkGroups();
	
	/**
	 * Registers a new {@link IBenchmarkGroup}, so it becomes available
	 * through this API.
	 *  
	 * @param group {@link IBenchmarkGroup} instance to add to this registry
	 */
	void addBenchmarkGroup(IBenchmarkGroup group);

	/**
	 * Removes a given {@link IBenchmarkGroup} instance from the registry.
	 *  
	 * @param group {@link IBenchmarkGroup} instance to remove from this registry
	 */
	void removeBenchmarkGroup(IBenchmarkGroup group);
	
	/**
	 * Completely clears up the registry so no {@link IBenchmarkGroup} instance
	 * would be available.
	 */
	void removeAllBenchmarkGroups();
}
