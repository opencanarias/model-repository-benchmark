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

import java.text.MessageFormat;
import java.util.List;
import java.util.Random;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opencanarias.mset.benchmark.repository.model.node.NodeFactory;
import com.opencanarias.mset.benchmark.repository.model.node.NodePackage;
import com.opencanarias.mset.internal.repository.benchmark.BenchmarkModelImpl;
import com.opencanarias.mset.internal.repository.benchmark.ModelPropertiesImpl;

/**
 * Given an input {@link IModelProperties}, this class randomly
 * generates a model that honors the argument {@link IModelProperties properties}
 * specification: the generated {@link EObject} tree has the same amount of elements
 * as defined at {@link IModelProperties#getCount()}, and less or equal 
 * than {@link IModelProperties#getDepth()} as model depth, and less or equal than 
 * {@link IModelProperties#getWidth()} as model width. If {@link IModelProperties#getHasValues()}
 * is set to <code>true</code>, the model {@link EAttribute} will be set with random data,
 * respecting the size of {@link EAttribute} value defined at {@link IModelProperties#getValueLength()}.
 * 
 * @author vroldan
 *
 */
public class BenchmarkModelGenerator {

	/**
	 * {@link IModelProperties Properties} that define an empty model
	 */
	public final static IModelProperties VOID_PROPERTIES = new ModelPropertiesImpl();
	
	/**
	 * Singleton instance for an empty {@link IBenchmarkModel}
	 */
	public final static IBenchmarkModel VOID_MODEL = new BenchmarkModelImpl(null);

	private final Logger logger = LoggerFactory.getLogger(getClass());

	// IMPORTANT FOR ORACLE DATABASES : VACHAR2 have maximum length size (4000 characters)
	private static final int MAX_ARRAY_SIZE = 4000;

	private static BenchmarkModelGenerator instance = null;

	private final Random random = new Random();

	private final static int DEFAULT_LOWER_BOUND = 1;

	private final static int DEFAULT_UPPER_BOUND = 20000;
	
	private final static char[] RANDOM_CHARS = "abcdefghijklmnopqrstuvwxyz".toCharArray(); //$NON-NLS-1$

	private final Runtime runtime = Runtime.getRuntime();

	/**
	 * Returns a instance of this class.
	 * 
	 * @return an instance of {@link BenchmarkModelGenerator}
	 */
	public static BenchmarkModelGenerator getInstance() {
		if (instance == null) {
			instance = new BenchmarkModelGenerator();
		}
		return instance;
	}

	/**
	 * Returns an instance of the {@link IModelProperties} interface
	 * 
	 * @return an uninitialized instance of {@link IModelProperties}
	 */
	public static IModelProperties createProperties() {
		return new ModelPropertiesImpl();
	}

	/**
	 * Creates model with default EPackage and EFactory, according to the argument 
	 * {@link IModelProperties properties}
	 */
	public IBenchmarkModel generateModel(IModelProperties props) {
		return generateModel(NodeFactory.eINSTANCE, NodePackage.eINSTANCE, props);
	}

	/**
	 * This method has a main loop which randomly attaches new EObjects to the
	 * tree, always trying to fulfill the {@link IModelProperties}
	 * constraints. Depending on the width and depth, the model would have a
	 * maximum number of elements, by definition. The current algorithm's
	 * difficulty to find where to add a new child during generation loop is
	 * directly proportional to how close the max count value is to the max
	 * allowed count according to the width and depth. The parent lookup process
	 * is random, and thats why its increasingly difficult to find a place to
	 * add the new EObject child.
	 * <p>
	 * The implementation guarantees the model wont exceed the given maximum
	 * values indicated in the {@link IModelProperties}, but does not
	 * guarantee that it will have exactly the same width and depth values: it
	 * will have less or equal values. The process also guarantees the model
	 * will have the exact same EObject count as specified in the
	 * {@link IModelProperties}.
	 */
	public IBenchmarkModel generateModel(EFactory factory, EPackage ePackage, IModelProperties props) {
		if (props == VOID_PROPERTIES) {
			return VOID_MODEL;
		}
		assertModelPropsAreFeasible(props);
		logger.trace("Starting generating model with properties {}", props);
		final boolean shouldGenerateEAttributes = props.getValueLength() > 0;
		final int heapPercentage = props.getMemUsage();
		final int maxObjectCount = props.getCount();
		int currentCount = 1;
		// counts the number of times the algorithm searches for an alternative
		// parent to satisfy the shape constaints
		int randomSearchMiss = 0;
		long initialFreeHeap = currentAvailableHeap(); // relative to max heap
														// size

		EObject rootEObject = createNode(factory, ePackage);
		EObject currentParent = rootEObject;
		setRandomAttributes(currentParent);
		
		while (((heapPercentage > IModelProperties.NOT_MEMORY_SENSITIVE) || (currentCount < maxObjectCount))
				&& heapUsageIsMet(heapPercentage, initialFreeHeap)) {
			// New child generation
			EObject child = createNode(factory, ePackage);
			if (shouldGenerateEAttributes) {
				setRandomAttributes(child);
			}

			// Ensure model shape
			boolean nextAddSatisfiesShape = false;
			// Look for the right place to attach the child.
			// This process is expensive, as the search is random
			// Keeps searching until it finds somewhere to place it and satisfy
			// the contraints
			int numberOfTrials = 0;
			do {
				currentParent = selectRandomParent(rootEObject, currentCount);
				int currentParentWidth = BenchmarkUtils.getEObjectWidth(currentParent);
				// we are going to add a child, so its current depth + 1
				int currentParentDepth = BenchmarkUtils.getEObjectDepth(currentParent) + 1;
				if ((currentParentWidth < props.getWidth())
						&& (currentParentDepth <= props.getDepth())) {
					addChild(currentParent, child);
					nextAddSatisfiesShape = true;
					currentCount++;
				} else {
					randomSearchMiss++;
					numberOfTrials++;
				}
			} while ((!nextAddSatisfiesShape)
					&& (currentCount < maxObjectCount));

			logger.trace("Tried {} times to look for the appropiate parent to attach child", numberOfTrials);
			if (logger.isTraceEnabled()) {
				logger.trace("Number of elements created so far is {}/{}", BenchmarkUtils.getModelSize(rootEObject), maxObjectCount);
			}
		}
		logger.trace("Finished creating {} elements", BenchmarkUtils.getModelSize(rootEObject));
		logger.trace("Algorithm total misses: {} times", randomSearchMiss);
		return new BenchmarkModelImpl(rootEObject);
	}

	/**
	 * Generates a instance of {@link IModelProperties} with
	 * randomly initialized attributes. Either count, depth and width
	 * shall have a random value between lowerRange and upperRange.
	 * 
	 * @param lowerRange the minimum value that count, depth or width may have
	 * @param upperRange the maximum value that count, depth or width may have
	 * @return an instance of {@link IModelProperties}, whose numeric attributes
	 *         must be within the argument range.
	 */
	public IModelProperties generateRandomProperties(int lowerRange, int upperRange) {
		IModelProperties props = null;
		int iterationLimit = 10;
		do {
			props = createProperties();		
			props.setValueLength(getRandomInt(lowerRange, upperRange));
			props.setCount(getRandomInt(lowerRange, upperRange));
			props.setDepth(getRandomInt(lowerRange, upperRange));
			props.setWidth(getRandomInt(lowerRange, upperRange));
			iterationLimit--;			
		} while (!checkModelPropsAreFeasible(props) && iterationLimit > 0);
		return props;
	}

	/**
	 * Generates an {@link IModelProperties} instance 
	 * that specifies a free heap memory percentage. The generator
	 * would create an instance that occupies a section of the heap in such a way
	 * that it leaves the argument percentage as free memory. This argument is
	 * provided to guarantee that the client is able to create big models that fit into
	 * memory, and OOME free.
	 * <p>
	 * These models are intended to put under stress the {@link IModelRepository}, and 
	 * shall prove its scalability. Be aware that generating these models require a 
	 * big amount of time.
	 * 
	 * @param heapPercentage
	 *            the percentage of memory we want to leave free in the heap
	 *            
	 */
	public IModelProperties generateRandomProperties(int heapPercentage) {
		IModelProperties props = generateRandomProperties();
		props.setMemUsage(heapPercentage);
		return props;
	}

	/**
	 * Generates an {@link IModelProperties} instance with random values.
	 * {@link IModelProperties#getHasValues()} returns true, and
	 * {@link IModelProperties#getMemUsage()} returns 
	 * {@link IModelProperties#NOT_MEMORY_SENSITIVE}
	 * 
	 * @return an {@link IModelProperties} instance with random values.
	 */
	public IModelProperties generateRandomProperties() {
		return generateRandomProperties(DEFAULT_LOWER_BOUND, DEFAULT_UPPER_BOUND);
	}

	/**
	 * This method determines if, given the argument properties, it is feasible
	 * to build a model that satisfies the specified constraints. The formula is
	 * sum(width^depth) from 0 to depth.
	 */
	private boolean checkModelPropsAreFeasible(IModelProperties props) {
		return calculateMaxModelSize(props) >= props.getCount();
	}

	private long calculateMaxModelSize(IModelProperties props) {
		final int width = props.getWidth();
		final int depth = props.getDepth();
		long maxTreeCount = 0;
		for (int i = 0; i < depth; i++) {
			maxTreeCount += Math.round(Math.pow(width, i));
		}
		return Math.abs(maxTreeCount);
	}

	private void assertModelPropsAreFeasible(IModelProperties props) {
		if (!checkModelPropsAreFeasible(props)) {
			throw new IllegalArgumentException(
					MessageFormat
							.format("Is not possible to generate a model with the given properties: max is {0} and count is set to {1}",
									calculateMaxModelSize(props),
									props.getCount()));
		}
	}

	private long currentAvailableHeap() {
		return runtime.maxMemory()
				- (runtime.totalMemory() - runtime.freeMemory());
	}

	private boolean heapUsageIsMet(int heapPercentage, long initialFreeHeap) {
		if (heapPercentage == IModelProperties.NOT_MEMORY_SENSITIVE) {
			return true;
		}
		long currentFreeHeap = currentAvailableHeap();
		long currentUsage = (currentFreeHeap * 100) / initialFreeHeap;
		logger.debug("Initial: {} currentUsage: {} -> {}%", initialFreeHeap,
				currentFreeHeap, currentUsage);
		logger.debug("MAX: " + runtime.maxMemory() + " TOTAL: "
				+ runtime.totalMemory() + " FREE: " + runtime.freeMemory()
				+ " - " + currentUsage + "%");
		return currentUsage > heapPercentage;
	}

	private void setRandomAttributes(EObject child) {
		for (EAttribute eAttribute : child.eClass().getEAllAttributes()) {
			child.eSet(eAttribute, generateRandomAttribute(eAttribute));
		}

	}

	private Object generateRandomAttribute(EAttribute eAttribute) {
		EDataType type = (EDataType) eAttribute.getEType();
		if (type == EcorePackage.Literals.ESTRING) {
			return getRandomString(Math.abs(getRandomInt(0, MAX_ARRAY_SIZE)));
		} else if (type == EcorePackage.Literals.EINT) {
			return random.nextInt();
		} else if (type == EcorePackage.Literals.EBYTE_ARRAY) {
			byte[] byteArray = new byte[Math.abs(getRandomInt(0, MAX_ARRAY_SIZE))];
			random.nextBytes(byteArray);
			return byteArray;
		}
		throw new IllegalArgumentException("Unsupported EDataType " + type);
	}

	@SuppressWarnings("unchecked")
	private void addChild(EObject rootEObject, EObject child) {
		List<EObject> containmentFeature = (List<EObject>) rootEObject
				.eGet(rootEObject.eClass().getEAllContainments().get(0));
		containmentFeature.add(child);
	}

	private EObject selectRandomParent(EObject rootEObject, int currentSize) {
		TreeIterator<EObject> it = rootEObject.eAllContents();
		int randomJumps = getRandomInt(0, currentSize);
		EObject selectedElement = rootEObject;
		while (it.hasNext() && randomJumps > 0) {
			selectedElement = it.next();
			randomJumps--;
		}
		return selectedElement;
	}

	private EObject createNode(EFactory factory, EPackage ePackage) {
		return factory.create((EClass) ePackage.getEClassifiers().get(0));
	}

	private int getRandomInt(int lowerRange, int upperRange) {
		if (lowerRange == upperRange) {
			return lowerRange;
		}
		int randomValue = lowerRange + (int) (random.nextInt(upperRange - lowerRange));
		return randomValue;
	}

	private String getRandomString(int length) {
		char[] text = new char[length];
		for (int i = 0; i < length; i++) {
			text[i] = RANDOM_CHARS[(random.nextInt(RANDOM_CHARS.length))];
		}
		return new String(text);
	}
}
