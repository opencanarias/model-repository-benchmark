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
package com.opencanarias.mset.internal.benchmark.repository.cdo.couchbase;

import java.net.URI;
import java.util.Collections;

import org.eclipse.emf.cdo.server.IStore;
import org.eclipse.emf.cdo.server.internal.CouchbaseStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.couchbase.client.clustermanager.BucketType;
import com.opencanarias.mset.benchmark.repository.cdo.AbstractCDOModelRepository;
import com.opencanarias.mset.benchmark.repository.cdo.util.Configuration;

@SuppressWarnings("restriction")
public class JVMCouchbaseCDOModelRepository extends AbstractCDOModelRepository {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private CouchbaseUtil util;
	
	private static final String HOST = Configuration.INSTANCE.getString("COUCHBASE.HOST"); //$NON-NLS-1$

	private static final String PORT = Configuration.INSTANCE.getString("COUCHBASE.PORT"); //$NON-NLS-1$

	private static final String USER = Configuration.INSTANCE.getString("COUCHBASE.USER"); //$NON-NLS-1$

	private static final String PASS = Configuration.INSTANCE.getString("COUCHBASE.PASS"); //$NON-NLS-1$

	private static final URI COUCHBASE_URI = URI.create("http://" + HOST + ":" + PORT + "/pools"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	
	@Override
	public String getName() {
		return "CDO-Couch-JVM"; //$NON-NLS-1$
	}
	
	@Override
	protected IStore createStore() {	
		return new CouchbaseStore(Collections.singletonList(COUCHBASE_URI), getRepositoryName(), USER, PASS);
	}

	@Override
	public boolean isAvailable() {		
		try {
			return getCouchbaseUtil().existBucket(getRepositoryName());
		} catch (Exception e) {
			logger.error("Error while determining Couchbase availability", e); //$NON-NLS-1$
			return false;
		}
	}
	
	@Override
	protected void doClean() {
		try {
			getCouchbaseUtil().cleanBucket(getRepositoryName());
		} catch (Exception ex) {
			logger.error("Error while cleaning CouchbaseStore", ex); //$NON-NLS-1$
			throw new RuntimeException(ex);
		}
	}

	private CouchbaseUtil getCouchbaseUtil() {
		if (util == null) {
			util = new CouchbaseUtil(BucketType.MEMCACHED, HOST, PORT, USER, PASS);
		}
		return util;
	}
	/**
	 * Current CouchbaseStore commit logic does not scale; removing big commit cases  
	 */
	@Override
	public boolean supports(Object object) {
		if (object != null) {
			String className = object.getClass().getSimpleName();
			if ("HugeModelTraversalNoCachingCase".equals(className)) { //$NON-NLS-1$
				return false;
			}
			
		}
		return super.supports(object);
	}
}
