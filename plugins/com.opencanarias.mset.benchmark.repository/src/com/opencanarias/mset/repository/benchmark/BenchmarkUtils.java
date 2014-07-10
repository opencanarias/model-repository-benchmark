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

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.BinaryResourceImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

import com.opencanarias.mset.internal.repository.benchmark.AggregatedResultImpl;
import com.opencanarias.mset.internal.repository.benchmark.BenchmarkDataImpl;
import com.opencanarias.mset.internal.repository.benchmark.BenchmarkMeterImpl;

/**
 * A set of general purpose utilities related with {@link IModelRepository} benchmarking.
 * 
 * @author vroldan
 * @see IBenchmarkMeter
 * @see EObject
 * @see EPackage
 * @see EFactory
 */
public class BenchmarkUtils {
	
	private static final String BINARY_RESOURCE_EXTENSION = "emfbin";
	
	private static final Resource.Factory BYNARY_RESOURCE_FACTORY = new Resource.Factory() {
		@Override
		public Resource createResource(URI uri) {
			return new BinaryResourceImpl(uri);
		}			
	};
	
	/**
	 * Creates an instance of a {@link IBenchmarkMeter}. This methods
	 * allows to define the heap polling interval for the meter.
	 * 
	 * @param pollingInterval frequency in milliseconds on how long it takes for the meter
	 *                        to take a heap snapshot
	 *                        
	 * @return an instance of {@link IBenchmarkMeter} with a predefined 
	 * 		  {@link IBenchmarkMeter#getPollingInterval() pollingInterval}
	 */
	public static IBenchmarkMeter getBenchmarkMeter(final long pollingInterval) {
		return new BenchmarkMeterImpl(pollingInterval);
	}
	
	/**
	 * Creates an instance of a {@link IBenchmarkMeter}. 
	 * The {@link IBenchmarkMeter#getPollingInterval() pollingInterval} is set to a default
	 * value.
	 * 
	 * @param pollingInterval frequency in milliseconds on how long it takes for the meter
	 *                        to take a heap snapshot
	 *                        
	 * @return an instance of {@link IBenchmarkMeter} with a predefined 
	 * 		  {@link IBenchmarkMeter#getPollingInterval() pollingInterval}
	 */
	public static IBenchmarkMeter getBenchmarkMeter() {
		return new BenchmarkMeterImpl();
	}
	
	/**
	 * Returns an {@link IBenchmarkData} instance, with a relationship to a 
	 * given repository.
	 * 
	 * @param repository {@link IModelRepository repository} 
	 *                    to be included into the {@link IBenchmarkData data} object 
	 * @return returns an instance of {@link IBenchmarkData}.
	 */
	public static IBenchmarkData getBenchmarkData(IModelRepository repository) {
		BenchmarkDataImpl data = new BenchmarkDataImpl();
		data.setRepository(repository);
		return data;
	}

	/**
	 * Given a reference model, translates it in terms of another model, by using using equivalence between
	 * source and target {@link EPackage} and {@link EFactory}. Source and target model are expected
	 * to be equal, but only differ in its {@link EPackage} declaration.
	 * <p>
	 * Certain repositories require to have your EMF models extend an specific EObject implementation.
	 * For instance, CDO has CDOObject and neo4emf has INeo4emfObject. To have a benchmark input model
	 * be tested against different {@link IModelRepository repositories}, we must provide
	 * to the repository the right model implementation. Therefore, if a given sample model for a test
	 * is defined, it must be previously translated into a repository-compatible implementation. 
	 * 
	 * @param rootEObject the source model to serve as reference for the translation. The output model should
	 *        be exactly equal to the source, but using a different model implementation.
	 *        
	 * @param ePackage {@link EPackage} definition for the target model. It allows the implementation
	 *        to find equivalences between source and target models
	 * 
	 * @param eFactory {@link EFactory} for the target model. It allows the implementation of this method
	 *        creating equivalent {@link EObject} instances in terms of the target model.
	 * 
	 * @return returns an exact copy of the source model, but which uses the model implementation described
	 *         by the argument {@link EPackage}.
	 */
	public static EObject copyModel(final EObject rootEObject, final EPackage ePackage, final EFactory eFactory) {
		EcoreUtil.Copier copier = new EcoreUtil.Copier() {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected EClass getTarget(EClass eClass) {
				return (EClass)ePackage.getEClassifier(eClass.getName());
			}
			@Override
			protected EStructuralFeature getTarget(EStructuralFeature eStructuralFeature) {
				EClass eClass = (EClass)ePackage.getEClassifier(((EClass)eStructuralFeature.eContainer()).getName());
				EStructuralFeature newFeature = eClass.getEStructuralFeature(eStructuralFeature.getName());
				return newFeature;
			}
		};
	    EObject result = copier.copy(rootEObject);
	    copier.copyReferences();
		return result;
	}

	/**
	 * Returns the number of EObjects a given {@link EObject} tree root has.
	 * Nothing above that root will be considered.
	 *  
	 * @param root the root of an {@link EObject} tree to perform counting 
	 * @return the number of elements of the given {@link EObject} tree
	 */
	public static int getModelSize(final EObject root) {
		TreeIterator<EObject> it = root.eAllContents();
		int count = root != null ? 1 : 0;
		while (it.hasNext()) {
			it.next();
			count++;
		}
		return count;
	}

	/**
	 * Calculates the depth of a given {@link EObject} tree, this is,
	 * the maximum distance between the root an a leaf element. 
	 *  
	 * @param root the root of an {@link EObject} tree that servers as start
	 *        point for the calculation 
	 * @return the max depth of the argument {@link EObject} tree
	 */
	public static int getModelDepth(final EObject root) {
		TreeIterator<EObject> it = root.eAllContents();
		int maxDepth = 0;
		while (it.hasNext()) {
			EObject currentRoot = it.next();
			int currentDepth = getEObjectDepth(currentRoot);
			if (currentDepth > maxDepth) {
				maxDepth = currentDepth;
			}
		}
		return maxDepth;
	}

	/**
	 * Determines the distance between the argument EObject
	 * and the root of the tree. The root is the {@link EObject}
	 * which has {@link EObject#eContainer()} equal to null
	 * 
	 * @param child the child EObject we wish to calculate is depth within
	 *              the tree. 
	 * @return the number of direct parent containers above the argument child
	 */
	public static int getEObjectDepth(final EObject child) {
		int currentDepth = 1;
		EObject currentChild = child;
		while ((currentChild = currentChild.eContainer()) != null) {
			currentDepth++;
		}
		return currentDepth;
	}

	/**
	 * Calculates the width of the model, this is, the maximum
	 * number of children an EObject within this tree contains.
	 * <p>
	 * Only containment relationships are considered in this
	 * calculation.
	 * 
	 * @param root the root of an {@link EObject} tree that servers as start
	 *        point for the calculation 
	 * @return a number representing the maximum number of children
	 *         {@link EObject eObjects} found in an arbitrary parent
	 *         within this tree
	 */
	public static int getModelWidth(final EObject root) {
		TreeIterator<EObject> it = root.eAllContents();
		int width = 0;
		while (it.hasNext()) {
			EObject eObject = it.next();
			int currentElementWidth = getEObjectWidth(eObject);
			if (currentElementWidth > width) {
				width = currentElementWidth;
			}
		}
		return width;
	}

	/**
	 * Returns the width of an the argument {@link EObject}, this is,
	 * the number of children it contains. Non-containment relationships
	 * are not taken into account.
	 * 
	 * @param eObject the element to calculate its width
	 * @return a number representing the width of the {@link EObject}
	 */
	public static int getEObjectWidth(final EObject eObject) {
		return eObject.eContents().size();
	}
	
	/**
	 * Analyzes an existing model and generates the equivalent {@link IModelProperties}.
	 * 
	 * @param root the root of the model to determine its properties
	 * @return an {@link IModelProperties} instance representing the properties of the argument model
	 */
	public static IModelProperties getPropertiesFromModel(final EObject root) {
		IModelProperties properties = BenchmarkModelGenerator.createProperties();		
		// TODO Optimize the algorithm
		properties.setCount(getModelSize(root));
		properties.setDepth(getModelDepth(root));
		properties.setWidth(getModelWidth(root));		
		return properties;
	}
	
	/**
	 * Creates an instance of EMFs {@link BinaryResourceImpl} at the argument location URI
	 * 
	 * @param uri the location {@link URI} for the BinaryResource 
	 * @return EMF's {@link Resource} using {@link BinaryResourceImpl}
	 */
	static public Resource createBinaryResource(URI uri) {		
		ResourceSet rSet = new ResourceSetImpl();
		registerBinaryResourceFactory(rSet);
		return rSet.createResource(uri);
	}

	/**
	 * Prepares a {@link ResourceSet} to be able to load {@link BinaryResourceImpl}.
	 * The resource extension matching the {@link Resource.Factory} is provided
	 * at {@link BenchmarkUtils#getBinaryResourceExtension()}
	 * 
	 * @param rSet the {@link ResourceSet} to prepare for {@link BinaryResourceImpl}
	 */
	static public void registerBinaryResourceFactory(ResourceSet rSet) {
		rSet.getLoadOptions().put(Resource.OPTION_ZIP, true);
		rSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(getBinaryResourceExtension(), BYNARY_RESOURCE_FACTORY);		
	}

	/**
	 * The proposed extension defined for {@link BinaryResourceImpl}
	 * @return the file extension for a {@link BinaryResourceImpl}, shall be used
	 *         in the {@link Resource} {@link URI}
	 */
	public static String getBinaryResourceExtension() {
		return BINARY_RESOURCE_EXTENSION;
	}
	
	/**
	 * Creates an instance of IAggregateResult
	 *  
	 * @return new instance of {@link IAggregateResult}
	 */
	public static IAggregatedResult createAggregateResult(){
		return new AggregatedResultImpl(); 
	}
}
