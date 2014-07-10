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

import java.io.IOException;
import java.net.SocketAddress;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import net.spy.memcached.MemcachedClient;

import com.couchbase.client.ClusterManager;
import com.couchbase.client.CouchbaseClient;
import com.couchbase.client.CouchbaseConnectionFactory;
import com.couchbase.client.CouchbaseConnectionFactoryBuilder;
import com.couchbase.client.clustermanager.BucketType;

public class CouchbaseUtil {

	private static final int BUCKET_MEMORY = 100;

	private ClusterManager manager;

	private MemcachedClient client;
	
	private BucketType bucketType;

	private String host;

	private String port;

	private String user;

	private String pass;

	public CouchbaseUtil(BucketType bucketType, String host, String port, String user, String pass) {
		this.bucketType = bucketType;
		this.host = host;
		this.port = port;
		this.user = user;
		this.pass = pass;
		// Redirecting Logging to JUL
		Properties systemProperties = System.getProperties();
		systemProperties.put("net.spy.log.LoggerImpl", "net.spy.memcached.compat.log.SunLogger"); //$NON-NLS-1$ //$NON-NLS-2$
		System.setProperties(systemProperties);	
	}

	public boolean isBucketEmpty(String bucketName) {
		Map<SocketAddress, Map<String, String>> stats = getClient(bucketName)
				.getStats();
		for (Entry<SocketAddress, Map<String, String>> server : stats
				.entrySet()) {
			Map<String, String> serverStats = server.getValue();
			if (bucketName.equals(serverStats.get("ep_couch_bucket"))) { //$NON-NLS-1$
				String value = serverStats.get("curr_items"); //$NON-NLS-1$
				if (value != null) {
					if (Integer.parseInt(value) == 0) {
						return true;
					} else {
						return false;
					}
				}
			}
		}
		return false;
	}

	/*
	 * Makes sure the argument bucket is created and empty
	 */
	public void cleanBucket(String bucketName) throws Exception {
		if (!existBucket(bucketName)) {
			try {
				createBucket(bucketName);
				// it is necessary to wait a bit after creation or client connection will fail
				// see http://www.couchbase.com/forums/thread/number-buckets-must-be-power-two-0-and-0
				if (bucketType == BucketType.COUCHBASE) {
					Thread.sleep(1000);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			if (!isBucketEmpty(bucketName)) {
				flushBucket(bucketName);
			}

		}
		waitForWarmup(bucketName);
	}

	public void createBucket(String bucketName) throws Exception {
		ClusterManager manager = getClusterManager();
		manager.createNamedBucket(bucketType, bucketName, BUCKET_MEMORY, 0, pass, true);
	}

	public void flushBucket(String bucketName) {
		ClusterManager manager = getClusterManager();
		manager.flushBucket(bucketName);
	}

	public void deleteBucket(String bucketName) throws Exception {
		ClusterManager manager = getClusterManager();
		manager.deleteBucket(bucketName);
	}
	
	public void deleteAllBuckets() throws Exception {
		ClusterManager manager = getClusterManager();
		for (String bucketName : manager.listBuckets()) {
			manager.deleteBucket(bucketName);
		}
	}

	public boolean existBucket(String bucketName) throws Exception {
		ClusterManager manager = getClusterManager();
		return manager.listBuckets().contains(bucketName);
	}

	public ClusterManager getClusterManager() {
		if (manager == null) {
			List<URI> uris = new LinkedList<URI>();
			uris.add(URI.create("http://" + host + ":" + port + "/pools")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			manager = new ClusterManager(uris, user, pass);
		}
		return manager;
	}

	public MemcachedClient getClient(String bucketName) {
		if (client == null) {
			List<URI> uris = new LinkedList<URI>();
			uris.add(URI.create("http://" + host + ":" + port + "/pools")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			try {
				CouchbaseConnectionFactoryBuilder connectionFactoryBuilder = new CouchbaseConnectionFactoryBuilder();
				connectionFactoryBuilder.setOpTimeout(10000); // To avoid random "Cancelled" upon client.get() calls
			    CouchbaseConnectionFactory connectionFactory = connectionFactoryBuilder.buildCouchbaseConnection(uris, bucketName, pass);
			    client = new CouchbaseClient((CouchbaseConnectionFactory)connectionFactory);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return client;
	}

	public void waitForWarmup(String bucketName) throws Exception {
		// Couchbase-type bucket needs warmup time after flush, memcached does not
		boolean warmup = bucketType == BucketType.COUCHBASE? true : false;
		while (warmup) {
			warmup = false;
			Map<SocketAddress, Map<String, String>> stats = getClient(
					bucketName).getStats();
			for (Entry<SocketAddress, Map<String, String>> server : stats
					.entrySet()) {
				Map<String, String> serverStats = server.getValue();
				if (!serverStats.containsKey("ep_degraded_mode")) { //$NON-NLS-1$
					warmup = true;
					Thread.sleep(1000);
					break;
				}
				if (!serverStats.get("ep_degraded_mode").equals("0")) { //$NON-NLS-1$ //$NON-NLS-2$
					warmup = true;
					Thread.sleep(1000);
					break;
				}
			}
		}
	}
	
	public void clear() {
		if (client != null) {
			client.shutdown(10, TimeUnit.SECONDS);
			client = null;
		}
		if (manager != null) {
			manager.shutdown();
			manager = null;
		}
	}
}
