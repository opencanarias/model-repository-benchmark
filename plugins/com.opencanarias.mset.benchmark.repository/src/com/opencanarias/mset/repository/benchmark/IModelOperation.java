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

/**
 * Interface to represent common model operations.
 * 
 * @author vroldan
 *
 */
public interface IModelOperation {

	/**
	 * Persists the argument {@link EObject} tree in an 
	 * arbitrary {@link Resource} implementation. Therefore, the {@link EObject} 
	 * tree should not be already contained in a {@link Resource resource}.
	 *  
	 * @param model the {@link EObject} tree to persist
	 */
	public void save(EObject model);

	/**
	 * Persists the argument {@link Resource}.
	 *  
	 * @param res the {@link Resource resource} to persist. 
	 */
	public void save(Resource res);
	
	/**
	 * Creates a new and empty {@link Resource} instance.
	 * The Resource is also registered into a new {@link ResourceSet} instance.
	 * 
	 * @return a new {@link Resource} instance, and a new container {@link ResourceSet}
	 */
	public Resource createResource();
	
	/**
	 * Returns a {@link Resource} representing the one persisted
	 * in the argument {@link URI}. The content of the resource is loaded
	 * into memory by default, unless a lazy-loading mechanism is available.
	 * 
	 * @param uri the {@link URI} location for a pre-exiting persisted model 
	 * @return the appropriate {@link Resource} instance for the argument {@link URI}
	 */
	public Resource loadResource(URI uri);
	
	/**
	 * For an already loaded Resource, this method guarantees its contents
	 * become dereferenced, ready for garbage collection. This method
	 * guarantees the contained {@link EObject} tree is completely dereferenced
	 * by the framework. Any referencing beyond that is out of the frameworks control.
	 * 
	 * @param res the {@link Resource} to unload
	 */
	public void unloadResource(Resource res);
	
	/**
	 * Returns the EObject represented by a {@link URI} and a fragment.
	 * Bare in mind some implementations do not allow fine grained
	 * loading of EObjects, so the whole {@link EObject} tree may be loaded
	 * in some scenarios.
	 * 
	 * @param resourceURI The {@link URI} representing the container {@link Resource}
	 * @param fragment represents the identification for an {@link EObject} within
	 *                 the resource persisted in the argument {@link URI}
	 * @return the EObject indicated by a resource {@link URI} and fragment
	 */
	public EObject loadEObject(URI resourceURI, String fragment);	
}
