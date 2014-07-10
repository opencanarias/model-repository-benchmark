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

import java.util.List;

import com.opencanarias.mset.repository.benchmark.IModelRepository;
import com.opencanarias.mset.repository.benchmark.IModelRepositoryRegistry;


public class ModelRepositoryRegistryImpl extends AbstractRegistry<IModelRepository> implements IModelRepositoryRegistry {

	@Override
	public List<IModelRepository> getModelRepositories() {
		return getRegisteredElements();
	}

	@Override
	public void addModelRepository(IModelRepository repository) {
		addElement(repository);
		
	}

	@Override
	public void removeModelRepository(IModelRepository repository) {
		removeElement(repository);
	}

	@Override
	public void removeAllModelRepositories() {
		removeAllElements();		
	}
	
}
