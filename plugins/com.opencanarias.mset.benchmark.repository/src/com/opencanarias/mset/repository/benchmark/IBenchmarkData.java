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

import java.util.Map;

/**
 * This interface encapsulates all information required for
 * {@link IBenchmarkCase} to execute.
 *  
 * @author vroldan
 * @see IModelRepository
 */
public interface IBenchmarkData {

	/**
	 * A generic map to introduce options that 
	 * shall modify behavior of the {@link IBenchmarkCase}
	 * to be executed. Any changes introduced into
	 * the map returned are permanent. 
	 * 
	 * @return a modifiable copy of the underlying options map
	 */
	public Map<String, Object> getOptions();
	
	/**
	 * The repository against which the {@link IBenchmarkCase} will be executed.
	 * 
	 * @return the instance of an {@link IModelRepository} that will be used
	 *         for the execution of the {@link IBenchmarkCase}.
	 */
	public IModelRepository getRepository();
	
}
