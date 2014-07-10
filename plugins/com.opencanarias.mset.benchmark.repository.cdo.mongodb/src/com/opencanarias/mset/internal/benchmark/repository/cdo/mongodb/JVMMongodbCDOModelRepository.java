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
package com.opencanarias.mset.internal.benchmark.repository.cdo.mongodb;

import java.net.UnknownHostException;

import org.eclipse.emf.cdo.server.IStore;
import org.eclipse.emf.cdo.server.mongodb.CDOMongoDBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoURI;
import com.opencanarias.mset.benchmark.repository.cdo.AbstractCDOModelRepository;
import com.opencanarias.mset.benchmark.repository.cdo.util.Configuration;
import com.opencanarias.mset.repository.benchmark.IModelRepository.IModelRepositoryListener.EventLevel;

public class JVMMongodbCDOModelRepository extends AbstractCDOModelRepository {

	private static final String MONGODB_URL_PREFIX = "mongodb://"; //$NON-NLS-1$

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private static final String HOST = Configuration.INSTANCE.getString("MONGODB.HOST"); //$NON-NLS-1$

	private static final String MONGO_LOCATION_URI = MONGODB_URL_PREFIX + HOST;	
	
	private static final MongoURI MONGO_URI = new MongoURI(MONGO_LOCATION_URI);
	@Override
	public String getName() {
		return "CDO-MongoDB-JVM"; //$NON-NLS-1$
	}
	
	@Override
	protected IStore createStore() {	
		return CDOMongoDBUtil.createStore(MONGO_LOCATION_URI, getRepositoryName());
	}

	@Override
	public boolean isAvailable() {		
		try {
			Mongo mongo = new Mongo(MONGO_URI);
			mongo.getConnector().getDBPortPool(mongo.getAddress()).get().ensureOpen();
		} catch (Exception e) {
			fireRepositoryEvent(EventLevel.ERROR, "MongoDB is not available", e);
			logger.error("MongoDB is not available", e);
			return false;
		}
		return true;
	}
	
	@Override
	protected void doClean() {
		try {
			Mongo mongo = new Mongo(MONGO_URI);
		    DB db = mongo.getDB(getRepositoryName());
			db.dropDatabase();
		} catch (UnknownHostException e) {
			fireRepositoryEvent(EventLevel.ERROR, "Error while cleaning MongoDB", e);
			logger.error("Error while cleaning MongoDB", e);
		}		
	}
	
	/**
	 * MongoDBStore does not support big models due to a limitation in the implementation
	 * (MongoDB document limit is 16MB).
	 * <p>
	 * MongoDBStore has also a bug regarding with restarting, the store is unable
	 * to load the RootResource properly. 
	 */
	@Override
	public boolean supports(Object object) {
		if (object != null) {
			String className = object.getClass().getSimpleName();
			if ("BigModelSaveCase".equals(className) //$NON-NLS-1$
					|| "ModelTraversalNoCachingCase".equals(className) //$NON-NLS-1$
					|| "BigModelTraversalNoCachingCase".equals(className) //$NON-NLS-1$
				    || "HugeModelTraversalNoCachingCase".equals(className)) { //$NON-NLS-1$
				return false;
			}
			
		}
		return super.supports(object);
	}
}
