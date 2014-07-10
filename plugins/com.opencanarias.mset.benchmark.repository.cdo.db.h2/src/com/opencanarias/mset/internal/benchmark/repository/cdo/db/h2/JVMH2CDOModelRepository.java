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
package com.opencanarias.mset.internal.benchmark.repository.cdo.db.h2;

import java.sql.Connection;
import java.sql.Statement;

import javax.sql.DataSource;

import org.eclipse.net4j.db.DBUtil;
import org.eclipse.net4j.db.IDBAdapter;
import org.h2.jdbcx.JdbcDataSource;

import com.opencanarias.mset.benchmark.repository.cdo.db.AbstractDBCDOModelRepository;
import com.opencanarias.mset.repository.benchmark.IModelRepository.IModelRepositoryListener.EventLevel;

public class JVMH2CDOModelRepository extends AbstractDBCDOModelRepository {

	private static final String AVAILABILITY_STATEMENT = "select table_name from information_schema.tables"; //$NON-NLS-1$

	private static final String DB_ADAPTER_NAME = "h2"; //$NON-NLS-1$

	private static final String DATABASE_URL_PROTOCOL = "jdbc:h2:"; //$NON-NLS-1$

	private static final String OPTIONS = ";CACHE_SIZE=250000;TRACE_LEVEL_FILE=0"; //$NON-NLS-1$
	
	protected DataSource createDataSource() {
		JdbcDataSource dataSource = new JdbcDataSource();
		dataSource.setURL(getDataSourceURL());
		return dataSource;
	}
	
	@Override
	public String getName() {
		return "CDO-H2-JVM"; //$NON-NLS-1$
	}
	
	protected IDBAdapter createDBAdapter() {
		return DBUtil.getDBAdapter(DB_ADAPTER_NAME);
	}
	
	protected String getDataSourceURL() {
		return DATABASE_URL_PROTOCOL + getTempDirectoryPath() + getRepositoryName() + OPTIONS;
	}
	
	@Override
	protected void shutDownBackend() {
		Connection connection = null;
		Statement statement = null;
		try {
			connection = getDataSource().getConnection();
			statement = connection.createStatement();
			statement.execute("SHUTDOWN"); //$NON-NLS-1$
		} catch (Throwable t1) {
			fireRepositoryEvent(EventLevel.ERROR, "Error on database shutdown", t1);
		} finally {
			try {
				statement.close();
				connection.close();
			} catch (Throwable t2) {
				fireRepositoryEvent(EventLevel.ERROR, "Error on database shutdown", t2);
			}
		}
	}

	@Override
	protected String getAvailabilityStatement() {
		return AVAILABILITY_STATEMENT;
	}
}
