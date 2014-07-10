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
package com.opencanarias.mset.benchmark.repository.tests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opencanarias.mset.repository.benchmark.BenchmarkModelGenerator;
import com.opencanarias.mset.repository.benchmark.BenchmarkUtils;
import com.opencanarias.mset.repository.benchmark.IBenchmarkCase;
import com.opencanarias.mset.repository.benchmark.IBenchmarkModel;
import com.opencanarias.mset.repository.benchmark.IModelProperties;

public class ModelGeneratorTest {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private BenchmarkModelGenerator generator = BenchmarkModelGenerator.getInstance();
	
	/**
	 * Generates a random model.
	 */
	@Test
	public void generateDefaultRandomModel() {
		IModelProperties props = generator.generateRandomProperties();
		IBenchmarkModel generatedModel = generator.generateModel(props);		
		logSpecs(generatedModel.getRoot());
		assertNotNull(generatedModel);
	}
	
	/**
	 * Generates a random model which occupies the 50% of the free space available in the heap
	 * WARNING: this test takes time, as the number of EObjects allowed in memory is high
	 */
	@Test @Ignore
	public void generateModelHalfHeapSpace() {
		IBenchmarkModel generatedModel = generator.generateModel(generator.generateRandomProperties(50));
		logger.info("generated 50% heap sized model with {} elements", BenchmarkUtils.getModelSize(generatedModel.getRoot()));
		logSpecs(generatedModel.getRoot());
		generatedModel = null;
		System.gc();
	}
	
	/**
	 * Generates a random model which occupies 75% of the free space available in the heap
	 * WARNING: this test takes time, as the number of EObjects allowed in memory is high
	 */
	@Test @Ignore
	public void generateThreeFourthsHeapSpace() {
		IBenchmarkModel generatedModel = generator.generateModel(generator.generateRandomProperties(25));
		logger.info("generated 75% heap sized model with {} elements", BenchmarkUtils.getModelSize(generatedModel.getRoot()));
		logSpecs(generatedModel.getRoot());
		generatedModel = null;
		System.gc();
	}
	
	/**
	 * Generates a random model which occupies 95% of the free space available in the heap
	 * WARNING: this test takes time, as the number of EObjects allowed in memory is high
	 */
	@Test @Ignore
	public void generateAlmostAsHeapSpace() {
		IBenchmarkModel generatedModel = generator.generateModel(generator.generateRandomProperties(5));
		logger.info("generated 95% heap sized model with {} elements", BenchmarkUtils.getModelSize(generatedModel.getRoot()));
		logSpecs(generatedModel.getRoot());
		generatedModel = null;
		System.gc();
	}
	
	/**
	 * An object tree with a maximum of 10 child per node, and a maximum depth of 3 levels allows
	 * a maximum of 111 elements. The current algorithm's difficulty to find where to add a new child during
	 * generation loop is directly proportional to how close the max count value is to the max allowed count
	 * according to the width and depth. The parent lookup process is random, and thats why its increasingly
	 * difficult to find a place to add the new EObject child.
	 */
	@Test
	public void testModelShaping() {
		IModelProperties props = BenchmarkModelGenerator.createProperties();
		final int count = 111;
		final int width = 10;
		final int depth = 3;
		props.setCount(count);
		props.setWidth(width);
		props.setDepth(depth);
		IBenchmarkModel generatedModel = generator.generateModel(props);
		logSpecs(generatedModel.getRoot());
	}
	
	/**
	 * An object tree with a maximum of 10 child per node, and a maximum depth of 3 levels allows
	 * a maximum of 111 elements. If count is set bigger than that, the generator should fail
	 * to avoid entering in an infinite loop trying to achieve an impossible model.
	 */
	@Test
	public void testCheckingImpossibleModelShape() {
		IModelProperties props = BenchmarkModelGenerator.createProperties();
		final int count = 112;
		final int width = 10;
		final int depth = 3;
		props.setCount(count);
		props.setWidth(width);
		props.setDepth(depth);
		
		try {
			generator.generateModel(props);
			fail("Model generator should have failed because of unachivable model properties");
		} catch (IllegalArgumentException e) { 
			logger.debug("Successfully identified illegal model shape");
		}
		
	}
	
	/**
	 * Routine to generate and serialize test-models. This is useful to provide a deterministic
	 * set of predictable model.
	 * <p>
	 * Enable this in case you are looking to generate a sample model pool to be used in {@link IBenchmarkCase}
	 */
	@Test @Ignore
	public void testGenerateModelPool() {		
		{
			IModelProperties props = BenchmarkModelGenerator.createProperties();		
			props.setCount(1);
			props.setWidth(10);
			props.setDepth(10);				
			for (int i = 0; i < 10; i++) {
				saveToBinModel(props, "model_1_elements_" + i);
				logger.info("generated a 1 element model and saved to emfbin");
			}
		}
		{
			IModelProperties props = BenchmarkModelGenerator.createProperties();		
			props.setCount(10);
			props.setWidth(10);
			props.setDepth(10);				
			for (int i = 0; i < 10; i++) {
				saveToBinModel(props, "model_10_elements_" + i);
				logger.info("generated a 10 element model and saved to emfbin");
			}
		}
		{
			IModelProperties props = BenchmarkModelGenerator.createProperties();		
			props.setCount(100);
			props.setWidth(10);
			props.setDepth(10);				
			for (int i = 0; i < 10; i++) {
				saveToBinModel(props, "model_100_elements_" + i);
				logger.info("generated a 100 element model and saved to emfbin");
			}
		}
		{
			IModelProperties props = BenchmarkModelGenerator.createProperties();		
			props.setCount(1000);
			props.setWidth(100);
			props.setDepth(20);				
			for (int i = 0; i < 10; i++) {
				saveToBinModel(props, "model_1k_elements_" + i);
				logger.info("generated a 1k element model and saved to emfbin");
			}
		}
		{
			IModelProperties props = BenchmarkModelGenerator.createProperties();		
			props.setCount(10000);
			props.setWidth(300);
			props.setDepth(30);				
			for (int i = 0; i < 10; i++) {
				saveToBinModel(props, "model_10k_elements_" + i);
				logger.info("generated a 10k element model and saved to emfbin");
			}
		}
		{
			IModelProperties props = BenchmarkModelGenerator.createProperties();		
			props.setCount(100000);
			props.setWidth(700);
			props.setDepth(40);				
			for (int i = 0; i < 10; i++) {
				saveToBinModel(props, "model_100k_elements_" + i);
				logger.info("generated a 100k element model and saved to emfbin");
			}
		}
	}

	private void saveToBinModel(IModelProperties props, final String modelName) {
		Resource res = BenchmarkUtils.createBinaryResource(URI.createFileURI(".\\" + modelName + "." + BenchmarkUtils.getBinaryResourceExtension()));
		EObject root = generator.generateModel(props).getRoot();
		res.getContents().add(root);
		try {
			Map<String, Object> options = new HashMap<String, Object>();
			options.put(Resource.OPTION_ZIP, true);
			res.save(options);
			logSpecs(root);
		} catch (IOException e) {
			logger.error("Error while saving binary emf resource", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Emits a log a shape analysis of the argument model: it logs count, depth and width.
	 */
	private void logSpecs(EObject generatedModel) {
		logger.info("generated model with {} elements, a width of {} and a depth of {}", 
			BenchmarkUtils.getModelSize(generatedModel), 
			BenchmarkUtils.getModelWidth(generatedModel), 
			BenchmarkUtils.getModelDepth(generatedModel));
	}
	
}
