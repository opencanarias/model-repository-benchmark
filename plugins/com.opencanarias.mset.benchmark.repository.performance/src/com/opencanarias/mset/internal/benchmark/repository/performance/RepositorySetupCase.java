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

public class RepositorySetupCase extends AbstractBenchmarkCase implements IBenchmarkCase {
	
	public RepositorySetupCase(IBenchmarkGroup group) {
		super(group);
	}

	@Override
	protected void executeMeasurable(IModelRepository repository, EObject model) {
		repository.start();		
	}

	@Override
	protected void setUpCase(IModelRepository repository, EObject model) {
		// Do not start server in setup stage, we want to measure it under executeMeasurable 
	}
	
}
