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

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opencanarias.mset.internal.repository.benchmark.BenchmarkModelImpl;
import com.opencanarias.mset.internal.repository.benchmark.BenchmarkResultImpl;
import com.opencanarias.mset.repository.benchmark.IModelRepository.RepositoryStatus;

/**
 * Defines a base workflow for {@link IBenchmarkCase}, which commonly
 * passes the following stages:
 * <ol>
 *   <li>Initialize/load benchmark model</li>
 *   <li>Adapt benchmark model to target repository</li>
 *   <li>Set up benchmark case</li>
 *   <li>Perform JVM warm-up / calibration</li>
 *   <li>Execute logic to be measured</li>
 *   <li>Tear down benchmark case</li>   
 * </ol>
 * 
 * @author vroldan
 *
 */
public abstract class AbstractBenchmarkCase implements IBenchmarkCase {

	private IBenchmarkGroup group;
	
	private IBenchmarkMeter meter = BenchmarkUtils.getBenchmarkMeter();
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private IModelRepository lastUsedRepository = null;
	
	private boolean lastExecutionFailed = false;
	
	private IBenchmarkModel model = null;
	
	private IModelProperties properties = null;
	
	private final boolean isGeneratingModel;
	
	private final URI benchmarkModelURI;
	
	/**
	 * An AbstractBenchmarkCase must be associated with an IBenchmarkGroup 
	 */
	public AbstractBenchmarkCase(IBenchmarkGroup group) {
		this.group = group;
		isGeneratingModel = true;
		benchmarkModelURI = null;
	}
	
	/**
	 * An AbstractBenchmarkCase must be associated with an IBenchmarkGroup 
	 */
	public AbstractBenchmarkCase(IBenchmarkGroup group, URI sampleModel) {
		this.group = group;
		isGeneratingModel = false;
		benchmarkModelURI = sampleModel;
	}
	
	/**
	 * Executes the whole {@link IBenchmarkCase} workflow:
	 * <ol>
	 *   <li>{@link AbstractBenchmarkCase#getBenchmarkModel()}</li>
	 *   <li>{@link IModelRepository#adaptModel(EObject)}</li>
	 *   <li>{@link AbstractBenchmarkCase#setUpCase(IModelRepository, EObject)}</li>
	 *   <li>{@link AbstractBenchmarkCase#warmUp(IModelRepository)}</li>
	 *   <li>{@link AbstractBenchmarkCase#executeMeasurable(IModelRepository, EObject)}</li>
	 *   <li>{@link AbstractBenchmarkCase#tearDownCase(IModelRepository, EObject)}</li>   
	 * </ol>
	 */
	@Override
	public IBenchmarkResult executeBenchmark(IBenchmarkData data) {		
		lastExecutionFailed = false;
		IModelRepository repository = data.getRepository();
		lastUsedRepository = repository;
		String name = repository.getName();
		logger.debug("[{}] Preparing benchmark", name);
		logger.trace("[{}] getting benchmark model", name);
		IBenchmarkModel bModel = getBenchmarkModel();
		logger.trace("[{}] adapting benchmark model to repo", name);
		EObject model = repository.adaptModel(bModel.getRoot()); //
		try {
			logger.trace("[{}] Setting up case", name);
			setUpCase(repository, model);
			checkRepository(repository);			
			logger.trace("[{}] Finished setting up case", name);
			logger.debug("[{}] Starting benchmark", name);
			startMeasuring();
			executeMeasurable(repository, model);
			stopMeasuring();
			checkRepository(repository);
			logger.debug("[{}] Finished benchmark", name);
			logger.debug("[{}] Finished benchmark execution", name);
		} finally {
			logger.trace("[{}] Tearing down case", name);
			tearDownCase(repository, model);
			logger.trace("[{}] Finished tearing down case", name);
			System.gc();
		}
		return getResult();
	}

	/**
	 * Returns the {@link IBenchmarkGroup} that aggregates this {@link IBenchmarkCase}
	 */
	@Override
	public IBenchmarkGroup getGroup() {
		return group;
	}

	/**
	 * Lazily loads and caches the {@link IBenchmarkModel} to be used in this
	 * {@link IBenchmarkCase}.
	 */
	@Override
	public IBenchmarkModel getBenchmarkModel() {		
		if (model == null){
			model = initModel();
		}
		return model;
	}

	/**
	 * Clears any state this {@link IBenchmarkCase} may have
	 */
	@Override
	public void reset() {
		model = null;
		lastUsedRepository = null;
		lastExecutionFailed = false;
		System.gc(); // Make sure heap is as much free as possible before next case
	}

	@Override
	public String getName() {
		String className = getClass().getSimpleName();		
		int lastCaseIndex = className.lastIndexOf("Case"); //$NON-NLS-1$
		return className.substring(0, lastCaseIndex > -1 ? lastCaseIndex : className.length() - 1);
	}

	/**
	 * Starts gathering metrics of the thread under execution
	 */
	protected void startMeasuring() {
		meter.startMeasuring();
	}

	/**
	 * Stops gathering metrics of the thread under execution
	 */
	protected void stopMeasuring() {
		meter.stopMeasuring();
	}
	
	/**
	 * Generates an {@link IBenchmarkResult} instance taking
	 * into account all the metrics gathered during the execution.
	 */
	protected IBenchmarkResult getResult() {
		BenchmarkResultImpl result = (BenchmarkResultImpl)meter.getResult();
		result.setModelRepository(lastUsedRepository);
		result.setBenchmarkCase(this);
		result.setModelProperties(getProperties());
		result.setBenchmarkFailed(lastExecutionFailed);
		return result;
	}
	
	/**
	 * Liberates all resources required during the benchmark execution,
	 * and leaves this instance clean and state-less, ready for re-execution
	 * with different parameters.
	 * 
	 * @param repository the {@link IModelRepository} used during the execution
	 * @param model the {@link EObject} tree used during this benchmark's execution
	 */
	protected void tearDownCase(IModelRepository repository, EObject model) {
		if (repository.getStatus() == RepositoryStatus.RUNNING) {
			repository.stop();
		}
		repository.clean();
	}



	/**
	 * Prepares all the necessary elements necessary 
	 * before the {@link AbstractBenchmarkCase#executeMeasurable(IModelRepository, EObject) 
	 * measurable logic} is executed
	 * 
	 * @param repository the {@link IModelRepository} to be used in this benchmark execution
	 * @param model the input model to be used in this benchmark execution
	 */
	protected void setUpCase(IModelRepository repository, EObject model) {
		if (repository.getStatus() != RepositoryStatus.RUNNING) {
			logger.trace("Setup: Starting repository {}", repository.getName());
			repository.start();
			logger.trace("Setup: Finished starting repository {}", repository.getName());
		}
		warmUp(repository);
	}

	/**
	 * This method contains the logic to be measured
	 * 
	 * @param repository the {@link IModelRepository} to be used during the logic to be measured
	 * @param model the input model to be used during the logic to be measured
	 */
	protected abstract void executeMeasurable(IModelRepository repository, EObject model);
		
	/**
	 * Return the {@link BenchmarkModelGenerator} instance to be used in case generation
	 * is needed in any {@link IBenchmarkCase}.
	 * 
	 * @return an instance of {@link BenchmarkModelGenerator} ready to be used 
	 *         for model generation purposes by any subclass.
	 */
	protected BenchmarkModelGenerator getGenerator() {
		return BenchmarkModelGenerator.getInstance();
	}

	/**
	 * Get the IModelProperties to be used in case the model is generated at runtime.
	 */
	protected IModelProperties getProperties() {
		if (properties == null) {
			if (getBenchmarkModelURI() != null) {
				properties = BenchmarkUtils.getPropertiesFromModel(getBenchmarkModel().getRoot());
			} else {
				properties = initProperties();	
			}
		}
		return properties;
	}

	/**
	 * Creates an instance of IModelProperties that defines the qualities of 
	 * the model in case it is generated at runtime.
	 */
	protected IModelProperties initProperties() {
		return BenchmarkModelGenerator.VOID_PROPERTIES;
	}

	/**
	 * Instantiates in memory the {@link IBenchmarkModel} to be used during the {@link IBenchmarkCase}
	 * 
	 * @return a new instance of the {@link IBenchmarkCase} input model
	 */
	protected IBenchmarkModel initModel() {
		if (isGeneratingModel()) {
			return getGenerator().generateModel(getProperties());
		} else {			
			ResourceSet rSet = new ResourceSetImpl();
			BenchmarkUtils.registerBinaryResourceFactory(rSet);
			Resource res = rSet.getResource(getBenchmarkModelURI(), true);
			IBenchmarkModel benchmarkModel = new BenchmarkModelImpl(res.getContents().get(0));
			return benchmarkModel;
		}
	}
	
	/**
	 * Instead of generating in runtime the {@link IBenchmarkModel} from a predefined
	 * {@link IModelProperties}, the model for the benchmark case will be loaded
	 * from the URI returned by this method.
	 * 
	 * @return the URI location for the model to be used during the benchmark case
	 */
	protected URI getBenchmarkModelURI() {
		return benchmarkModelURI;
	}
	
	/*
	 * Serves as workaround for a bug on LissomeStore repository,
	 * which does not persist correctly EPackages
	 */
	private void warmUp(IModelRepository repository) {
		Resource res = repository.createResource();				
		IModelProperties props = BenchmarkModelGenerator.createProperties();
		props.setCount(1);
		props.setWidth(1);
		props.setDepth(1);
		IBenchmarkModel warmUpModel = BenchmarkModelGenerator.getInstance().generateModel(props);
		EObject adaptedRoot = repository.adaptModel(warmUpModel.getRoot());
		res.getContents().add(adaptedRoot);
		repository.save(res);	
	}

	private void checkRepository(IModelRepository repository) {
		if (repository.getStatus() == RepositoryStatus.ERROR) {
			lastExecutionFailed = true;
		}
	}

	/**
	 * If set to true, the {@link IBenchmarkModel} wont be generated on runtime,
	 * instead it will be loaded from the URI at getSampleModelURI()
	 * 
	 * @return
	 */
	private boolean isGeneratingModel() {
		return isGeneratingModel;
	}
}
