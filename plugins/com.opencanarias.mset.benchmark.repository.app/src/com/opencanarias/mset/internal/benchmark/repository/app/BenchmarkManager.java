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
package com.opencanarias.mset.internal.benchmark.repository.app;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opencanarias.mset.repository.benchmark.BenchmarkUtils;
import com.opencanarias.mset.repository.benchmark.CSVUtils;
import com.opencanarias.mset.repository.benchmark.IAggregatedResult;
import com.opencanarias.mset.repository.benchmark.IBenchmarkCase;
import com.opencanarias.mset.repository.benchmark.IBenchmarkData;
import com.opencanarias.mset.repository.benchmark.IBenchmarkGroup;
import com.opencanarias.mset.repository.benchmark.IBenchmarkGroupRegistry;
import com.opencanarias.mset.repository.benchmark.IBenchmarkResult;
import com.opencanarias.mset.repository.benchmark.IModelRepository;
import com.opencanarias.mset.repository.benchmark.IModelRepositoryRegistry;

/**
 * Handles a repository benchmarking that gives progress report through logging,
 * and may be interrupted at any time. The results are exported to a CSV file
 * using {@link CSVUtils}.
 * 
 * @author vroldan
 * @see IModelRepositoryRegistry
 * @see IBenchmarkGroupRegistry
 * @see CSVUtils
 *
 */
public class BenchmarkManager {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private static final SimpleDateFormat SDF_FILE_NAME = new SimpleDateFormat("yyyyMMdd_HHmm"); //$NON-NLS-1$
	
	public static BenchmarkManager INSTANCE = new BenchmarkManager();
	
	private boolean shouldStop = false;
	
	public static final int DEFAULT_ITERATION_COUNT = 1;
	
	private BenchmarkManager() {}
	
	public void benchmarkRepository(String repositoryName, int iterations) {
		logger.info("Starting benchmarking for repository {}", repositoryName);		
		List<IModelRepository> registeredRepositories = IModelRepositoryRegistry.INSTANCE.getModelRepositories();

		logger.debug("Number of Model Repositories registered for benchmarking is {}", registeredRepositories.size());
		IModelRepository selectedRepository = null;
		for (IModelRepository repository : registeredRepositories) {			
			if (repository.getName().equalsIgnoreCase(repositoryName)) {
				logger.info("Repository \"{}\" found", repository.getName());
				selectedRepository = repository;
			}
		}
		if (selectedRepository == null) {
			logger.info("Could not find a registered repository with name \"{}\"", repositoryName);
			return;
		}
		if (selectedRepository.isAvailable()) {
			selectedRepository.clean(); // Clean before starting, in case last execution did not finish properly
			List<IBenchmarkGroup> allGroups = IBenchmarkGroupRegistry.INSTANCE.getAllBenchmarkGroups();
			logger.debug("Number of benchmark cases available is {}", allGroups.size());
			for (IBenchmarkGroup group: allGroups) {
				logger.debug("Group \"{}\" is registered and contains {} cases", group.getName(), group.getCases().size());	
			}
			generateCSV(doBenchmark(Collections.singletonList(selectedRepository), allGroups, iterations));
		} else {
			logger.info("Repository \"{}\" is registered but not available, cannot perform benchmarking", selectedRepository.getName());
			return;
		}		
	}

	public void benchmarkAll() {
		benchmarkAll(DEFAULT_ITERATION_COUNT);
	}
	
	public void benchmarkAll(int iterations) {
		logger.info("Benchmarking all available repositories");		
		List<IModelRepository> registeredRepositories = IModelRepositoryRegistry.INSTANCE.getModelRepositories();

		logger.debug("Number of Model Repositories registered for benchmarking is {}", registeredRepositories.size());
		List<IModelRepository> availableRepositories = new ArrayList<IModelRepository>();
		for (IModelRepository repository : registeredRepositories) {			
			if (!repository.isAvailable()) {
				logger.info("Repository \"{}\" is registered but not available, will not be benchmarked", repository.getName());
			} else {
				logger.debug("Repository \"{}\" is registered and will be benchmarked", repository.getName());
				availableRepositories.add(repository);
				logger.debug("Cleaning repository \"{}\" before benchmarking starts", repository.getName());
				repository.clean(); // Clean before starting, in case last execution did not finish properly
			}
		}
		List<IBenchmarkGroup> allGroups = IBenchmarkGroupRegistry.INSTANCE.getAllBenchmarkGroups();
		logger.debug("Number of benchmark cases available is {}", allGroups.size());
		for (IBenchmarkGroup group: allGroups) {
			logger.debug("Group \"{}\" is registered and contains {} cases", group.getName(), group.getCases().size());	
		}
		generateCSV(doBenchmark(availableRepositories, allGroups, iterations));		
	}

	public void stop() {
		logger.info("Stopping benchmark, please wait for benchmark case under execution to finish");
		setShouldStop(true);
	}

	public List<String> listRepositories() {
		List<String> result = new ArrayList<String>();
		for (IModelRepository repo : IModelRepositoryRegistry.INSTANCE.getModelRepositories()) {
			result.add(repo.getName());
		}
		return result;
	}

	public boolean repositoryExists(String repoName) {		
		for (IModelRepository repo : IModelRepositoryRegistry.INSTANCE.getModelRepositories()) {
			if (repoName.equalsIgnoreCase(repo.getName())) {
				return true;
			}
		}
		return false;
	}

	private void generateCSV(final  Map<String, IAggregatedResult> mapResults) {
		StringBuffer aggregationDetails = new StringBuffer();
		aggregationDetails.append(CSVUtils.getAggregationHeader());
		StringBuffer iterationDetails = new StringBuffer();
		iterationDetails.append(CSVUtils.getLineSeparator());
		iterationDetails.append("Iteration Details");
		iterationDetails.append(CSVUtils.getLineSeparator());
		iterationDetails.append(CSVUtils.getLineSeparator());
		iterationDetails.append(CSVUtils.getResultHeader());
		for(String key : mapResults.keySet()) {
			IAggregatedResult aggregateResult = mapResults.get(key);
			aggregationDetails.append(CSVUtils.toEntry(aggregateResult));
			for (IBenchmarkResult result : aggregateResult.getAggregatedResults()) { 
				iterationDetails.append(CSVUtils.toEntry(result));
				logger.info(result.toString());
			}
			logger.info(aggregateResult.toString());
		}
		aggregationDetails.append(iterationDetails);
		try {
			File file = createFile(getFilePath(), aggregationDetails);
			logger.info("Results available in file \"{}\".", file.getAbsolutePath());
		} catch (IOException e) {
			logger.error("Error file creating CSV file with results", e);
			throw new RuntimeException("Error file creating CSV file with results", e);
		}
	}

	private Map<String, IAggregatedResult> doBenchmark(List<IModelRepository> repositories, List<IBenchmarkGroup> groups, int iterations) {
		Map<String, IAggregatedResult> aggregatedResults = new HashMap<String, IAggregatedResult>();
		int progressCount = 0;
		for (IBenchmarkGroup group: groups) {
			progressCount += group.getCases().size() * repositories.size() * iterations;
		}
		logger.info("Executing Model Repository Benchmarks");
		logger.info("Total combinations to test: {}", progressCount);
		int currentProgressCount = 0;
		for (int iteration = 1; iteration <= iterations; iteration++ ) {
			logger.info("Executing Iteration: " + (iteration) + "/" + iterations);
			for (IBenchmarkGroup group: groups) {
				for (IBenchmarkCase benchmarkCase: group.getCases()) {
					for (IModelRepository repository : repositories) {
						final boolean supported = repository.supports(benchmarkCase);
						boolean benchmarkFailed = false;
						if (supported) {
							IBenchmarkData data = BenchmarkUtils.getBenchmarkData(repository);
							try {
								IBenchmarkResult result = benchmarkCase.executeBenchmark(data);
								benchmarkFailed = result.isFailed();
								String name = benchmarkCase.getName() + "-" + repository.getName();  
								if (!aggregatedResults.containsKey(name) )
									aggregatedResults.put(name, BenchmarkUtils.createAggregateResult());
								aggregatedResults.get(name).addResult(result);
							} catch (Exception e) {
								logger.error("Test {} with repo {} failed", benchmarkCase.getName(), repository.getName());
								logger.error("Exception caught while execution benchmark", e);
								benchmarkFailed = true;
							}
						}
						currentProgressCount++;
						logger.info("Progress {}% ({}/{}) | [{}]-[{}] {} {}", 
								((currentProgressCount * 100) / progressCount), 
								currentProgressCount, progressCount,
								benchmarkCase.getName(), repository.getName(), 
								supported ? "" : "(skipped)",
								benchmarkFailed ? "(failed)" : "");
						if (isShouldStop()) {
							setShouldStop(false);
							benchmarkCase.reset();
							logger.info("Benchmark Stopped");
							return aggregatedResults;
						}
					}
					benchmarkCase.reset();
				}
			}
		}
		return aggregatedResults;
	}

	private String getFilePath() {
		return "results/results_"+ SDF_FILE_NAME.format(new Date()) + ".csv"; //$NON-NLS-1$ //$NON-NLS-2$
	}	
	

	private File createFile(String fullPath, StringBuffer content) throws IOException {
		File file = new File(fullPath);
		createFile(file, content);
		return file;
	}
	
	private void createFile(File file, StringBuffer content) throws IOException {
		if (!file.exists()) {
			File fDir = file.getParentFile();
			if (fDir != null && !fDir.exists())
				fDir.mkdirs();
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(content.toString().getBytes());
			fos.close();
		}
	}

	private boolean isShouldStop() {
		return shouldStop;
	}

	private void setShouldStop(boolean shouldStop) {
		this.shouldStop = shouldStop;
	}
}
