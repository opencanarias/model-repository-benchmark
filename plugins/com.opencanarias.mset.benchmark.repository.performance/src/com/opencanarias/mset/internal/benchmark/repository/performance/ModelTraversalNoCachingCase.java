/*
 * Copyright (c) 2014 Open Canarias and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Jose David Lutzardo - initial API and implementation
 */
package com.opencanarias.mset.internal.benchmark.repository.performance;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

import com.opencanarias.mset.repository.benchmark.AbstractBenchmarkCase;
import com.opencanarias.mset.repository.benchmark.IBenchmarkCase;
import com.opencanarias.mset.repository.benchmark.IBenchmarkGroup;
import com.opencanarias.mset.repository.benchmark.IModelRepository;


/**
 * We are calculating the time required to iterate the whole model  
 * 
 * @author jdavidlb
 *
 */
public class ModelTraversalNoCachingCase extends AbstractBenchmarkCase implements IBenchmarkCase {

	private String uriFragment;
	
	private Resource res;
	 
	public ModelTraversalNoCachingCase(IBenchmarkGroup group, URI uri) {
		super(group, uri);
	}

	public ModelTraversalNoCachingCase(IBenchmarkGroup group) {
		super(group, SampleModelPool.get1KModelURI());
	}
	
	@Override
	protected void setUpCase(IModelRepository repository, EObject model) {
		super.setUpCase(repository, model);
		// First we persist the model in the repository
		Resource resourceToIterate = repository.createResource();
		resourceToIterate.getContents().add(model);
		repository.save(resourceToIterate);
		URI resourceURI = resourceToIterate.getURI();
		uriFragment = resourceToIterate.getURIFragment(model);
		
		// Then we unload all underlying resources and caches (to avoid repository optimizations)
		if (!isCaching()) {
			repository.restart();
		}
		
		// on a fresh repository, get the resource
		res = repository.loadResource(resourceURI);
	}

	/** 
	 * If set to true, the repository is allowed to use caching mechanisms
	 * to enhance performance.
	 * 
	 * @return true if repository caching is should be enabled during the benchmark, false otherwise. 
	 */
	protected boolean isCaching() {
		return false;
	}

	@Override
	protected void executeMeasurable(IModelRepository repository, EObject model) {
		TreeIterator<EObject> iter = res.getEObject(uriFragment).eAllContents();
		while (iter.hasNext()) {
			EObject eObjet = iter.next();
			eObjet.eContainer();
		}
	}
	
	/**
	 * Unloads, so in next execution, a new resource will be created.
	 * This avoids adding eObjects to an already existent resource in next execution 
	 */
	@Override
	protected void tearDownCase(IModelRepository repository, EObject model) {
		res.unload();
		res = null;
		uriFragment = null;
		super.tearDownCase(repository, model);
	}

}
