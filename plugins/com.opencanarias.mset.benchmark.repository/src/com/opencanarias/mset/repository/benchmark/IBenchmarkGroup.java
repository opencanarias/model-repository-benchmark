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
package com.opencanarias.mset.repository.benchmark;

import java.util.List;

/**
 * An {@link IBenchmarkCase} aggregation: a {@link IBenchmarkGroup group}
 * defines a set of semantically related {@link IBenchmarkCase cases}
 * 
 * @author vroldan
 * @see IBenchmarkCase
 */
public interface IBenchmarkGroup {
	
	/**
	 * A list of {@link IBenchmarkCase}. They should some kind of 
	 * semantic relation, but nothing prevents the opposite. 
	 * The list returned is modifiable and any change to it is persistent.
	 * 
	 * @return a modifiable list of {@link IBenchmarkCase} 
	 *         aggregated by this {@link IBenchmarkGroup} group.
	 */
	public List<IBenchmarkCase> getCases();

	/**
	 * An unique and descriptive identification for this group.
	 * 
	 * @return a String representing the identification for this group
	 */
	public String getName();
	
}
