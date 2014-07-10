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
package com.opencanarias.mset.internal.benchmark.repository.cdo.db.oracle;

import java.sql.SQLException;

import javax.sql.DataSource;

import oracle.jdbc.pool.OracleDataSource;

import org.eclipse.net4j.db.DBUtil;
import org.eclipse.net4j.db.IDBAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opencanarias.mset.benchmark.repository.cdo.db.AbstractDBCDOModelRepository;
import com.opencanarias.mset.benchmark.repository.cdo.util.Configuration;
import com.opencanarias.mset.repository.benchmark.IModelRepository.IModelRepositoryListener.EventLevel;

public class JVMOracleCDOModelRepository extends AbstractDBCDOModelRepository {

	private static final String AVAILABILITY_STATEMENT = "select table_name from all_tables"; //$NON-NLS-1$

	private static final String DB_ADAPTER_NAME = "oracle"; //$NON-NLS-1$

	private static final String DATABASE_URL_PROTOCOL = "jdbc:oracle:"; //$NON-NLS-1$
	
	private static final String HOST = Configuration.INSTANCE.getString("ORACLE.HOST"); //$NON-NLS-1$

	private static final int PORT = Configuration.INSTANCE.getInt("ORACLE.PORT"); //$NON-NLS-1$

	private static final String USER = Configuration.INSTANCE.getString("ORACLE.USER"); //$NON-NLS-1$

	private static final String PASS = Configuration.INSTANCE.getString("ORACLE.PASS"); //$NON-NLS-1$
	
	private static final String DATABASE = Configuration.INSTANCE.getString("ORACLE.DATABASE"); //$NON-NLS-1$

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public String getName() {
		return "CDO-ORACLE-JVM"; //$NON-NLS-1$
	}

	protected DataSource createDataSource() {
		try {
			OracleDataSource source = new OracleDataSource();
			source.setUser(USER);
			source.setPassword(PASS);
			source.setURL(getDataSourceURL());		
			return source;
		} catch (SQLException e) {
			logger.error("Error while creating datasource for oracle", e);
			fireRepositoryEvent(EventLevel.ERROR, "Error while creating datasource for oracle", e);
		}
	    return null;
	}
	
	protected IDBAdapter createDBAdapter() {
		return DBUtil.getDBAdapter(DB_ADAPTER_NAME);
	}
	
	protected String getDataSourceURL() {
		// "jdbc:oracle:thin:@host:port:SID";
		return DATABASE_URL_PROTOCOL + "thin:@" + HOST + ":" + PORT +  ":"  + DATABASE; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
	
	@Override
	protected String getRepositoryName() {
		return USER;
	}
		
	protected String getAvailabilityStatement() {
		return AVAILABILITY_STATEMENT;
	}
}
