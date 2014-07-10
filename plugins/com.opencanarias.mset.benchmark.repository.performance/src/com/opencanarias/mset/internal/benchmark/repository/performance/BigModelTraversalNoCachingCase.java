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

import com.opencanarias.mset.repository.benchmark.IBenchmarkGroup;

public class BigModelTraversalNoCachingCase extends ModelTraversalNoCachingCase {

	public BigModelTraversalNoCachingCase(IBenchmarkGroup group) {
		super(group, SampleModelPool.get10KModelURI());
	}	
	
}
