/*
 * Copyright (c) 2014 Open Canarias and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Jose David Lutzardo - initial API and implementation
 */
package com.opencanarias.mset.internal.benchmark.repository.cdo.db.psql;

import javax.sql.DataSource;

import org.eclipse.net4j.db.DBUtil;
import org.eclipse.net4j.db.IDBAdapter;
import org.postgresql.ds.PGSimpleDataSource;

import com.opencanarias.mset.benchmark.repository.cdo.db.AbstractDBCDOModelRepository;
import com.opencanarias.mset.benchmark.repository.cdo.util.Configuration;

public class JVMPostgresCDOModelRepository extends AbstractDBCDOModelRepository {

	private static final String AVAILABILITY_STATEMENT = "select datname from pg_catalog.pg_database"; //$NON-NLS-1$
	
	private static final String DB_ADAPTER_NAME = "postgresql"; //$NON-NLS-1$

	private static final String DATABASE_URL_PROTOCOL = "jdbc:postgresql:"; //$NON-NLS-1$
	
	private static final String HOST = Configuration.INSTANCE.getString("POSTGRES.HOST"); //$NON-NLS-1$

	private static final int PORT = Configuration.INSTANCE.getInt("POSTGRES.PORT"); //$NON-NLS-1$

	private static final String USER = Configuration.INSTANCE.getString("POSTGRES.USER"); //$NON-NLS-1$

	private static final String PASS = Configuration.INSTANCE.getString("POSTGRES.PASS"); //$NON-NLS-1$
	
	private static final String DATABASE = Configuration.INSTANCE.getString("POSTGRES.DATABASE"); //$NON-NLS-1$
	
	private String databaseLocation = null;

	@Override
	public String getName() {
		return "CDO-POSTGRES-JVM"; //$NON-NLS-1$
	}

	protected DataSource createDataSource() {
		PGSimpleDataSource source = new PGSimpleDataSource();
		source.setServerName(HOST);
		source.setDatabaseName(getRepositoryName()+"?searchpath=cdoserver"); //$NON-NLS-1$
		source.setUser(USER);
		source.setPassword(PASS);
		source.setPortNumber(PORT);
		return source;
	}
	
	protected IDBAdapter createDBAdapter() {
		return DBUtil.getDBAdapter(DB_ADAPTER_NAME);
	}
	
	protected String getDataSourceURL() {
		return DATABASE_URL_PROTOCOL + getDatabaseLocation() + ":" + getRepositoryName(); //$NON-NLS-1$
	}
	
	@Override
	protected String getRepositoryName() {
		return DATABASE;
	}
	
	private String getDatabaseLocation() {
		if (databaseLocation == null) {
			databaseLocation = HOST + ":" + PORT; //$NON-NLS-1$
		}
		return databaseLocation;
	}
		
	protected String getAvailabilityStatement() {
		return AVAILABILITY_STATEMENT;
	}
}
