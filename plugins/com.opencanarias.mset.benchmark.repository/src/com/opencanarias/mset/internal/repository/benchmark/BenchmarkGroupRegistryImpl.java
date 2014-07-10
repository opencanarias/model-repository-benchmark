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

import com.opencanarias.mset.repository.benchmark.IBenchmarkGroup;
import com.opencanarias.mset.repository.benchmark.IBenchmarkGroupRegistry;

public class BenchmarkGroupRegistryImpl extends AbstractRegistry<IBenchmarkGroup> implements IBenchmarkGroupRegistry {

	@Override
	public List<IBenchmarkGroup> getAllBenchmarkGroups() {
		return getRegisteredElements();
	}

	@Override
	public void addBenchmarkGroup(IBenchmarkGroup group) {
		addElement(group);		
	}

	@Override
	public void removeBenchmarkGroup(IBenchmarkGroup group) {
		removeElement(group);
	}

	@Override
	public void removeAllBenchmarkGroups() {
		removeAllElements();
	}
}
