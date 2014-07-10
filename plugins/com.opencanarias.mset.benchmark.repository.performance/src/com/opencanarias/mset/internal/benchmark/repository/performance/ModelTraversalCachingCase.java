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

import com.opencanarias.mset.repository.benchmark.IBenchmarkCase;
import com.opencanarias.mset.repository.benchmark.IBenchmarkGroup;


/**
 * We are calculating the time required to iterate the whole model, with client/server caching enabled.  
 * 
 * @author vroldan
 *
 */
public class ModelTraversalCachingCase extends ModelTraversalNoCachingCase implements IBenchmarkCase {

	public ModelTraversalCachingCase(IBenchmarkGroup group) {
		super(group);
	}

	@Override
	protected boolean isCaching() {
		return true;
	}
}
