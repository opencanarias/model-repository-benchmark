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

import com.opencanarias.mset.internal.repository.benchmark.ModelRepositoryRegistryImpl;

/**
 * Represents a registry to keep reference to reusable {@link IModelRepository} instances.
 * Is meant to be a central place to register and acquire {@link IModelRepository} instances
 * for benchmarking purposes. Clients may use instances registered here freely.
 *
 * @author vroldan
 *
 */
public interface IModelRepositoryRegistry {
	
	/**
	 * Singleton instance for an {@link IModelRepositoryRegistry}. {@link IModelRepository}
	 * instances are contributed to this registry through Eclipse's extension point
	 * mechanism 
	 */
	public IModelRepositoryRegistry INSTANCE = new ModelRepositoryRegistryImpl();
	
	/**
	 * Returns an unmodifiable list containing all registered {@link IModelRepository} instances in this
	 * registry. 
	 */
	public List<IModelRepository> getModelRepositories();
	
	/**
	 * Registers a new {@link IModelRepository}, so it becomes available
	 * through this API.
	 *  
	 * @param group {@link IModelRepository} instance to add to this registry
	 */
	void addModelRepository(IModelRepository repository);
	
	/**
	 * Removes a given {@link IModelRepository} instance from the registry.
	 *  
	 * @param group {@link IModelRepository} instance to remove from this registry
	 */
	void removeModelRepository(IModelRepository repository);
	
	/**
	 * Completely clears up the registry so no {@link IModelRepository} instance
	 * would be available.
	 */
	void removeAllModelRepositories();
}
