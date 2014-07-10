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

import java.util.HashMap;
import java.util.Map;

import com.opencanarias.mset.repository.benchmark.IBenchmarkData;
import com.opencanarias.mset.repository.benchmark.IModelRepository;

public class BenchmarkDataImpl implements IBenchmarkData {

	private Map<String, Object> options = new HashMap<String, Object>();
	
	private IModelRepository repository;
	
	@Override
	public Map<String, Object> getOptions() {
		return options;
	}

	@Override
	public IModelRepository getRepository() {
		return repository;
	}

	public void setRepository(IModelRepository repository) {
		this.repository = repository;
	}

}
