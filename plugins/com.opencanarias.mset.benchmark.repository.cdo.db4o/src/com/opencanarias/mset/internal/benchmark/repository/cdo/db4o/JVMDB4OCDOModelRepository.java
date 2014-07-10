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
package com.opencanarias.mset.internal.benchmark.repository.cdo.db4o;

import java.io.File;

import org.eclipse.emf.cdo.server.IStore;
import org.eclipse.emf.cdo.server.internal.db4o.DB4OStore;

import com.opencanarias.mset.benchmark.repository.cdo.AbstractCDOModelRepository;

@SuppressWarnings("restriction")
public class JVMDB4OCDOModelRepository extends AbstractCDOModelRepository {

	@Override
	public String getName() {
		return "CDO-DB4O-JVM"; //$NON-NLS-1$
	}
	
	@Override
	protected IStore createStore() {
		File tempFolder = getTempFile();
		IStore store = new DB4OStore(tempFolder.getAbsolutePath(), 1231);
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
