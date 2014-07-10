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

/**
 * A benchmark case defines an arbitrary set of model operations,
 * with a reference model as input persisted in a given {@link IModelRepository}. The implementation
 * is both model and {@link IModelRepository} agnostic. Benchmark cases are only
 * useful to obtain runtime information described in an {@link IBenchmarkResult} instance.
 * The way the {@link IBenchmarkResult result} is calculated is undefined,
 * but it should be homogeneous among other benchmark cases to make the comparison valid.
 * 
 * @see IModelRepository
 * @see IBenchmarkResult
 * @see EObject
 * @author vroldan
 */
public interface IBenchmarkCase {

	/**
	 * Performs the execution of this benchmark case, considering
	 * a set of variable parameters passed through an {@link IBenchmarkData}
	 * instance. The implementation may be composed of several stages, 
	 * and it is also implementation specific which 
	 * parts of that logic are to be measured. The logic operates
	 * over an {@link EObject} tree defined by the case itself.
	 * 
	 * @param data contains options and most importantly the repository where
	 *             there model will be persisted.
	 * @return 
	 */
	public IBenchmarkResult executeBenchmark(IBenchmarkData data);

	/**
	 * A textual identification to make this {@link IBenchmarkCase case} human readable
	 * and unambiguously identifiable among a set of cases.
	 * 
	 * @return a String representing the identification of this case 
	 */
	public String getName();

	/**
	 * {@link IBenchmarkCase} are usually grouped depending on their sematic relationship.
	 * This method returns the {@link IBenchmarkGroup group} this {@link IBenchmarkCase case}
	 * belongs to.
	 * 
	 * @return the aggregation of {@link IBenchmarkCase cases} this one belongs to.
	 */
	public IBenchmarkGroup getGroup();
	
	/**
	 * A {@link IBenchmarkCase}'s measurable logic depends pretty much on the input model.
	 * The shape, size and type of information that is modeled in the input {@link EObject} tree
	 * conditions the performance of the logic of this {@link IBenchmarkCase case}. Therefore,
	 * there is an important relationship between those two actors. A case will usually
	 * define what kind of model should serve as input for the measurable logic.  
	 * 
	 * @return the reference model for this {@link IBenchmarkCase} that will be used by the logic.
	 *         We get a repository independent instance.
	 */
	public IBenchmarkModel getBenchmarkModel();

	/**
	 * {@link IBenchmarkCase} must be executed under the same conditions for 
	 * every repository. To achieve this, some caching may be necessary.
	 * This method completely resets the state of the case (i.e, 
	 * unloading test-models). This guarantees that there is not
	 * an increasing number of {@link IBenchmarkCase} registered and 
	 * keeping an state that may distort memory usage statistics. 
	 */
	public void reset();
}
