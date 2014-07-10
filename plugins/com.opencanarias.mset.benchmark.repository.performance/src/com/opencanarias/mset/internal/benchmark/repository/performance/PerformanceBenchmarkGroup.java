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

import java.util.Arrays;
import java.util.List;

import com.opencanarias.mset.repository.benchmark.IBenchmarkCase;
import com.opencanarias.mset.repository.benchmark.IBenchmarkGroup;

public class PerformanceBenchmarkGroup implements IBenchmarkGroup {
	
	public static final String GROUP_ID = "Performance"; //$NON-NLS-1$
	
	@Override
	public List<IBenchmarkCase> getCases() {
		return Arrays.<IBenchmarkCase>asList
				(
					new RepositorySetupCase(this),
					new ResourceCreationCase(this),
					new MinimalModelSaveCase(this),
					new ResourceLoadCase(this),
					new ModelTraversalNoCachingCase(this),
					new ModelTraversalCachingCase(this),
					new BigModelSaveCase(this),
					new BigModelTraversalNoCachingCase(this)
//					new HugeModelTraversalNoCachingCase(this)
				);
	}
	
	@Override
	public String getName() {
		return GROUP_ID;
	}
	
}
