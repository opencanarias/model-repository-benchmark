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
package com.opencanarias.mset.benchmark.repository.cdo.db.mariadb;

import javax.sql.DataSource;

import org.eclipse.net4j.db.DBUtil;
import org.eclipse.net4j.db.IDBAdapter;
import org.mariadb.jdbc.MySQLDataSource;

import com.opencanarias.mset.benchmark.repository.cdo.db.AbstractDBCDOModelRepository;
import com.opencanarias.mset.benchmark.repository.cdo.util.Configuration;

public class JVMMariadbCDOModelRepository extends AbstractDBCDOModelRepository {

	private static final String AVAILABILITY_STATEMENT = "select table_name from information_schema.tables"; //$NON-NLS-1$

	private static final String DB_ADAPTER_NAME = "mysql"; //$NON-NLS-1$

	private static final String DATABASE_URL_PROTOCOL = "jdbc:mariadb:"; //$NON-NLS-1$
	
	private static final String HOST = Configuration.INSTANCE.getString("MARIADB.HOST"); //$NON-NLS-1$

	private static final int PORT = Configuration.INSTANCE.getInt("MARIADB.PORT"); //$NON-NLS-1$

	private static final String USER = Configuration.INSTANCE.getString("MARIADB.USER"); //$NON-NLS-1$

	private static final String PASS = Configuration.INSTANCE.getString("MARIADB.PASS"); //$NON-NLS-1$
	
	private static final String DATABASE = Configuration.INSTANCE.getString("MARIADB.DATABASE"); //$NON-NLS-1$

	@Override
	public String getName() {
		return "CDO-MARIADB-JVM"; //$NON-NLS-1$
	}

	protected DataSource createDataSource() {
		MySQLDataSource source = new MySQLDataSource();
		source.setServerName(HOST);
		source.setDatabaseName(getRepositoryName());
		source.setUser(USER);
		source.setPassword(PASS);
		source.setPortNumber(PORT);
		return source;
	}
	
	protected IDBAdapter createDBAdapter() {
		return DBUtil.getDBAdapter(DB_ADAPTER_NAME);
	}
	
	protected String getDataSourceURL() {
		return DATABASE_URL_PROTOCOL + "//" + HOST + ":" + PORT +  "/"  + getRepositoryName(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
	
	@Override
	protected String getRepositoryName() {
		return DATABASE;
	}
		
	protected String getAvailabilityStatement() {
		return AVAILABILITY_STATEMENT;
	}
}
