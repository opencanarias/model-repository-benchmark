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
package com.opencanarias.mset.benchmark.repository.cdo.db;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.eclipse.emf.cdo.server.IStore;
import org.eclipse.emf.cdo.server.db.CDODBUtil;
import org.eclipse.emf.cdo.server.db.IDBStore;
import org.eclipse.emf.cdo.server.db.mapping.IMappingStrategy;
import org.eclipse.net4j.db.DBUtil;
import org.eclipse.net4j.db.IDBAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opencanarias.mset.benchmark.repository.cdo.AbstractCDOModelRepository;
import com.opencanarias.mset.repository.benchmark.IModelRepository.IModelRepositoryListener.EventLevel;

public abstract class AbstractDBCDOModelRepository extends AbstractCDOModelRepository {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private DataSource dataSource = null;
	private IMappingStrategy mappingStrategy = null;
	private IDBStore store = null; 

	protected IStore createStore() {
		mappingStrategy = createMappingStrategy();
		Map<String, String> props = new HashMap<String, String>();
		props.put(IMappingStrategy.PROP_QUALIFIED_NAMES, "true"); //$NON-NLS-1$
		mappingStrategy.setProperties(props);
		store = CDODBUtil.createStore(mappingStrategy, createDBAdapter(), DBUtil.createConnectionProvider(getDataSource()));
		mappingStrategy.setStore(store);
		return store;
	}

	protected IMappingStrategy createMappingStrategy() {
		return CDODBUtil.createHorizontalMappingStrategy(false);
	}
	
	protected IMappingStrategy getMappingStrategy() {
		return mappingStrategy;
	}

	protected abstract IDBAdapter createDBAdapter();

	protected abstract String getDataSourceURL();

	protected DataSource getDataSource() {
		if (dataSource == null) {
			dataSource = createDataSource();
		}
		return dataSource;
	}

	protected abstract DataSource createDataSource();

	@Override
	protected void doClean() {
		super.doClean();
		try {
			DBUtil.dropAllTables(getDataSource().getConnection(), getRepositoryName());
		} catch (SQLException e) {
			logger.error("Error while performing database cleansing", e);
			fireRepositoryEvent(EventLevel.ERROR, "Error on database doClean", e);
		}		
	}
	
	abstract protected String getAvailabilityStatement();

	@Override
	public boolean isAvailable() {
		Statement statement =  null;
		try {
			statement = getDataSource().getConnection().createStatement();
			return statement.execute(getAvailabilityStatement());
		} catch (SQLException e) {
			logger.error("Error on database isAvailable", e);
			fireRepositoryEvent(EventLevel.ERROR, "Error on database isAvailable", e);
		} finally {
			try {
				if (statement !=  null)
					statement.close();
			} catch (SQLException e) {
				logger.error("Error on database isAvailable", e);
				fireRepositoryEvent(EventLevel.ERROR, "Error on database isAvailable", e);
			}
		}
		return false;
	}
}
