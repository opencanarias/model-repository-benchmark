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

import org.eclipse.emf.ecore.EObject;

import com.opencanarias.mset.repository.benchmark.AbstractBenchmarkCase;
import com.opencanarias.mset.repository.benchmark.IBenchmarkCase;
import com.opencanarias.mset.repository.benchmark.IBenchmarkGroup;
import com.opencanarias.mset.repository.benchmark.IModelRepository;

public class ResourceCreationCase extends AbstractBenchmarkCase implements IBenchmarkCase {

	public ResourceCreationCase(IBenchmarkGroup group) {
		super(group);
	}

	@Override
	protected void executeMeasurable(IModelRepository repository, EObject model) {
		repository.save(repository.createResource());		
	}
	
}
