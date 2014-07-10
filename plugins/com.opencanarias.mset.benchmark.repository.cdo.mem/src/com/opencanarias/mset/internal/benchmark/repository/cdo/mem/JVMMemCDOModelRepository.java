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
package com.opencanarias.mset.internal.benchmark.repository.cdo.mem;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.eclipse.emf.cdo.server.CDOServerExporter;
import org.eclipse.emf.cdo.server.CDOServerImporter;
import org.eclipse.emf.cdo.server.IRepository;
import org.eclipse.emf.cdo.server.IStore;
import org.eclipse.emf.cdo.server.mem.IMEMStore;
import org.eclipse.emf.cdo.server.mem.MEMStoreUtil;
import org.eclipse.net4j.util.io.XMLOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opencanarias.mset.benchmark.repository.cdo.AbstractCDOModelRepository;
import com.opencanarias.mset.repository.benchmark.IModelRepository;

/**
 * Implementation of an {@link AbstractCDOModelRepository} using {@link IMEMStore} as backend
 * <p>
 * The {@link IModelRepository} interface considers the model repository state is persistent
 * between start/stop calls. MemStore is inherently volatile, so it requires some aditional
 * logic to persist the state before the {@link IModelRepository#stop()}.
 * 
 * @author vroldan
 *
 */
public class JVMMemCDOModelRepository extends AbstractCDOModelRepository {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public String getName() {
		return "CDO-MEM-JVM"; //$NON-NLS-1$
	}

	@Override
	protected IStore createStore() {
		return MEMStoreUtil.createMEMStore();
	}

	@Override
	 public void stop() {
		IRepository repository = getRepository();
		exportData(repository);
		super.stop();
	}

	@Override
	protected IRepository createRepository() {
		IRepository repository = super.createRepository();
		importData(repository);
		return repository;
	}

	@Override
	protected void doClean() {
		super.doClean();
		getTempFile().delete();
	}
	
	private void exportData(IRepository repository) {
		// Export repository to XML file
		CDOServerExporter<XMLOutput> cdoExporter = new CDOServerExporter.XML(repository);
		try { 
			FileOutputStream fO = new FileOutputStream(getTempFile());
			cdoExporter.exportRepository(fO);
			fO.close();
		} catch (Exception e) {
			logger.error("Error while backing up repository data on stop()", e);
			throw new RuntimeException("Error while backing up repository data on stop()", e);
		}
	}

	private void importData(IRepository repository) {
		if (getTempFile().exists() && getTempFile().length() > 0 ){
			repository.deactivate();
			CDOServerImporter cdoImorter = new CDOServerImporter.XML(repository);
			try {
				FileInputStream fI = new FileInputStream(getTempFile());
				cdoImorter.importRepository(fI);
				fI.close();
			} catch (Exception e) {
				logger.error("Error while importing data into repository", e);
				throw new RuntimeException("Error while importing data into repository", e);
			}
			finally {
				repository.activate();
			}
		}
	}
}
