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
package com.opencanarias.mset.internal.benchmark.repository.neo4j;

import java.io.File;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opencanarias.mset.benchmark.repository.model.neo4emf.node.NodeFactory;
import com.opencanarias.mset.benchmark.repository.model.neo4emf.node.NodePackage;
import com.opencanarias.mset.benchmark.repository.model.neo4emf.reltypes.ReltypesMappings;
import com.opencanarias.mset.repository.benchmark.StubModelRepository;

import fr.inria.atlanmod.neo4emf.INeo4emfResource;
import fr.inria.atlanmod.neo4emf.INeo4emfResourceFactory;

public class Neo4jModelRepository extends StubModelRepository {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public String getName() {
		return "neo4emf";
	}
	
	@Override
	public Resource createResource() {
		logger.trace("Creating INeo4emfResource");
		// Create the resourceSet 
		ResourceSet rSet = new ResourceSetImpl(); 
		
		File file = createTempFile();

		// Create an URI with neo4emf as protocol
		URI uri = URI.createURI("neo4emf:/" + file.getAbsolutePath());

		// Attach this protocol to INeo4emfResourceFactory 
		// If the relationshipMap is not set don't forget to register the package 
		// It is automatically generated 
		rSet.getResourceFactoryRegistry().getProtocolToFactoryMap().put("neo4emf", INeo4emfResourceFactory.eINSTANCE.setRelationshipsMap(ReltypesMappings.getInstance().getMap()));

		// Create the resource 
		return rSet.createResource(uri);
	}

	@Override
	public Resource loadResource(URI uri) {
		logger.trace("Loading INeo4emfResource");
		ResourceSet rSet = new ResourceSetImpl(); 
		rSet.getResourceFactoryRegistry().getProtocolToFactoryMap().put("neo4emf", INeo4emfResourceFactory.eINSTANCE.setRelationshipsMap(ReltypesMappings.getInstance().getMap()));
		Resource res = rSet.getResource(uri, true);
		logger.trace("Finished loading INeo4emfResource");
		return res;
	}

	@Override
	public EObject loadEObject(URI resourceURI, String uriFragment) {
		throw new UnsupportedOperationException("not implemented");
	}

	@Override
	public void unloadResource(Resource res) {
		super.unloadResource(res);
		INeo4emfResource neoRes = (INeo4emfResource)res;
		neoRes.shutdown();
	}

	protected EFactory getEFactory() {
		return NodeFactory.eINSTANCE;
	}
	
	protected EPackage getEPackage() {
		return NodePackage.eINSTANCE;
	}
	
	@Override
	public boolean isAvailable() {
		// FIXME disabled until repository properly works
		return false;
	}
	
}
