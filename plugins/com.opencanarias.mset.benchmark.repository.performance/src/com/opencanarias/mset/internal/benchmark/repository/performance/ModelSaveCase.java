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
package com.opencanarias.mset.internal.benchmark.repository.performance;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

import com.opencanarias.mset.repository.benchmark.AbstractBenchmarkCase;
import com.opencanarias.mset.repository.benchmark.IBenchmarkCase;
import com.opencanarias.mset.repository.benchmark.IBenchmarkGroup;
import com.opencanarias.mset.repository.benchmark.IModelProperties;
import com.opencanarias.mset.repository.benchmark.IModelRepository;

public class ModelSaveCase extends AbstractBenchmarkCase implements IBenchmarkCase {

	private Resource res = null;
	private int eObjectCountLower;	
	private int eObjectCountUpper;
	
	/**
	 * Use this constructor in case the model should be randomly generated in runtime
	 */
	public ModelSaveCase(IBenchmarkGroup group, int eObjectCountLower, int eObjectCountUpper) {
		super(group);
		this.eObjectCountLower = eObjectCountLower;
		this.eObjectCountUpper = eObjectCountUpper;
	}

	/**
	 * Use this constructor in case we want to use a predefined model
	 */
	public ModelSaveCase(IBenchmarkGroup group, URI uri) {
		super(group, uri);
	}
	
	/**
	 * To make things even among repositories, we first create an empty resource.
	 * For instance, neo4emf initializes a whole Neo4j database for each resource,
	 * which would deviate benchmark values too much with other repositories.
	 */
	@Override
	protected void setUpCase(IModelRepository repository, EObject model) {
		super.setUpCase(repository, model);
		res = repository.createResource();
		repository.save(res);
	}
	
	@Override
	protected void executeMeasurable(IModelRepository repository, EObject model) {
		res.getContents().add(model);
		repository.save(res);
	}

	/**
	 * Unloads, so in next execution, a new resource will be created.
	 * This avoids adding eObjects to an already existent resource in next execution 
	 */
	@Override
	protected void tearDownCase(IModelRepository repository, EObject model) {
		super.tearDownCase(repository, model);
		res.unload();
		res = null;
	}
	
	@Override
	protected IModelProperties initProperties() {
		return getGenerator().generateRandomProperties(eObjectCountLower, eObjectCountUpper);
	}
}
