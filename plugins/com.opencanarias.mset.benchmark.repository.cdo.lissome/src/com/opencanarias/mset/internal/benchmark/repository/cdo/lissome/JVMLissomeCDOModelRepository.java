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
package com.opencanarias.mset.internal.benchmark.repository.cdo.lissome;

import java.io.File;

import javax.sql.DataSource;

import org.eclipse.emf.cdo.server.IStore;
import org.eclipse.emf.cdo.server.internal.lissome.db.Index;
import org.eclipse.emf.cdo.server.lissome.LissomeStoreUtil;
import org.eclipse.net4j.db.h2.H2Adapter;

import com.opencanarias.mset.benchmark.repository.cdo.AbstractCDOModelRepository;

@SuppressWarnings("restriction")
public class JVMLissomeCDOModelRepository extends AbstractCDOModelRepository {

	@Override
	public String getName() {
		return "CDO-Liss-JVM"; //$NON-NLS-1$
	}
	
	@Override
	protected IStore createStore() {
		File tempFolder = getTempDirectory();
		DataSource dataSource = Index.createDataSource(tempFolder, getRepositoryName(), null);
	    H2Adapter.createSchema(dataSource, getRepositoryName(), false);
		IStore store = LissomeStoreUtil.createStore(tempFolder);
		return store;
	}
	
	@Override
	protected String getSupportingAudits() {
		return Boolean.TRUE.toString();
	}
	
	@Override
	protected String getSupportingBranches() {
		return Boolean.TRUE.toString();
	}
}
