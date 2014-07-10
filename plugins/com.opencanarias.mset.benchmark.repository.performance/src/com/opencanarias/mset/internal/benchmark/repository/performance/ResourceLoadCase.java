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
import com.opencanarias.mset.repository.benchmark.IModelRepository;

public class ResourceLoadCase extends AbstractBenchmarkCase implements IBenchmarkCase {

	private URI resourceUriToLoad;
	
	public ResourceLoadCase(IBenchmarkGroup group) {
		super(group, SampleModelPool.get100ModelURI());
	}

	@Override
	protected void setUpCase(IModelRepository repository, EObject model) {
		super.setUpCase(repository, model);
		Resource res = repository.createResource();
		resourceUriToLoad = res.getURI();
		res.getContents().add(model);
		repository.save(res);
		repository.unloadResource(res);	
	}
	
	@Override
	protected void executeMeasurable(IModelRepository repository, EObject model) {
		repository.loadResource(resourceUriToLoad);
	}
	
}
