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
package com.opencanarias.mset.repository.benchmark;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opencanarias.mset.repository.benchmark.IModelRepository.IModelRepositoryListener.EventLevel;

/**
 * Base class for an {@link IModelRepository}. Extenders only need
 * to define how the repository is {@link IModelRepository#start() started},
 * {@link IModelRepository#stop() stopped} and {@link IModelRepository#clean() cleaned}, 
 * and to define repository specific {@link EPackage} and {@link EFactory} and its
 * {@link IModelRepository#getRepositoryURI() uri}.
 * <p>
 * Furthermore, clients will have to implement part of the {@link IModelOperation} logic,
 * which is repository specific.
 * <p>
 * This abstract class provides mainly deals with {@link IModelRepository repository} 
 * notification handling and adds logic common to most {@link IModelRepository repositories}
 * 
 * @author vroldan
 *
 */
public abstract class AbstractModelRepository implements IModelRepository {

	private List<IModelRepositoryListener> listeners = new ArrayList<IModelRepositoryListener>();
	
	private RepositoryStatus status = RepositoryStatus.STOPPED;
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private File tempDirectory = null;
	private File tempFile = null;
	
	public AbstractModelRepository() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
		    public void run() {
		    	if (AbstractModelRepository.this.getStatus() != RepositoryStatus.STOPPED) {
		    		AbstractModelRepository.this.stop();
		    	}
		    }
		});
	}
		
	public void restart() {
		setStatus(RepositoryStatus.RESTARTING);
		stop();
		start();
	}

	public RepositoryStatus getStatus() {
		return status;
	}
	
	public void addListener(IModelRepositoryListener listener) {
		listeners.add(listener);
	}
	
	public void removeListener(IModelRepositoryListener listener) {
		listeners.remove(listener);
	}
	
	public boolean containsListener(IModelRepositoryListener listener) {
		return listeners.contains(listener);
	}
	
	@Override
	public EObject adaptModel(EObject model) {
		return BenchmarkUtils.copyModel(model, getEPackage(), getEFactory());
	}

	@Override
	public String getName() {
		return getClass().getSimpleName();
	}

	@Override
	public void save(Resource res) {
		try {
			logger.trace("Starting saving {} Resource {}", getName(), res.getURI());
			res.save(null);
			logger.trace("Saved {} Resource {}", getName(), res.getURI());
		} catch (IOException e) {
			logger.error("Something happened while saving resource {}", res.getURI(), e);
			throw new RuntimeException(e);
		}		
	}

	@Override
	public void save(EObject model) {
		Resource res = createResource();
		logger.trace("Attaching model to Resource");
		res.getContents().add(model);
		save(res);
	}

	@Override
	public void unloadResource(Resource res) {
		res.unload();
	}

	@Override
	public boolean isAvailable() {
		return true;
	}

	@Override
	public boolean supports(Object object) {
		return true;
	}
	
	/**
	 * Notifies all {@link IModelRepositoryListener} about a new event,
	 * with a {@link Throwable} associated to the event 
	 */
	protected void fireRepositoryEvent(EventLevel level, String msg, Throwable t) {
		for (IModelRepositoryListener listener : listeners) {
			listener.repositoryEvent(level, msg, t);
		}
	}
	
	/**
	 * Notifies all {@link IModelRepositoryListener} about a new event. 
	 */	
	protected void fireRepositoryEvent(EventLevel level, String msg) {
		for (IModelRepositoryListener listener : listeners) {
			listener.repositoryEvent(level, msg);
		}
	}

	/**
	 * Indicates the repository to transit to a new {@link RepositoryStatus state}
	 */
	protected void setStatus(RepositoryStatus newStatus) {
		fireStatusChanged(newStatus);
		status = newStatus;
	}
	
	/**
	 * Returns a new random {@link String}
	 */
	protected String getRandomString() {
		return UUID.randomUUID().toString();
	}

	/**
	 * Lazily creates a new temporary directory.
	 * This directory is randomly named, and is unique
	 * for each instance of {@link AbstractModelRepository}.
	 * 
	 * @return a lazily created temporary directory
	 */
	protected File getTempDirectory(){
		if (tempDirectory == null) {
			tempDirectory = createTempDirectory();
		}
		return tempDirectory;
	}

	/**
	 * Lazily creates a new temporary {@link File}.
	 * This file is randomly named, and is unique
	 * for each instance of {@link AbstractModelRepository}.
	 * 
	 * @return a lazily created temporary {@link File}
	 */
	protected File getTempFile(){
		if (tempFile == null) {
			tempFile = createTempFile();
		}
		return tempFile;
	}

	/**
	 * Creates a new randomly-named temporary directory each time is called
	 */
	protected File createTempDirectory() {
		final File temp = createTempFile();
		try {		
			if (!(temp.mkdir())) {
				throw new IOException(MessageFormat.format("Could not create temp directory \"{0}\"", temp.getAbsolutePath()));
			}
		} catch (Throwable t) {
			fireRepositoryEvent(EventLevel.ERROR, "Error when creating a temp folder", t);
			throw new RuntimeException(t);
		}
		return temp;
	}
	
	/**
	 * Creates a new randomly-named temporary file each time is called
	 */
	protected File createTempFile() {
		final File temp;
		try {
			temp = File.createTempFile("model_repository_" + getName() + "_", Long.toString(System.nanoTime()));	 //$NON-NLS-1$ //$NON-NLS-2$
			if (!(temp.delete())) {
				throw new IOException(MessageFormat.format("Could not delete temp file \"{0}\"",temp.getAbsolutePath()));
			}	
			temp.deleteOnExit();
		} catch (Throwable t) {
			fireRepositoryEvent(EventLevel.ERROR, "Error when creating temp file", t);
			throw new RuntimeException(t);
		}
		return temp;
	}

	/**
	 * The {@link EFactory} instance to be used during the {@link IModelRepository#adaptModel(EObject) model adaptation}
	 * 
	 * @return the {@link EFactory} that corresponds to the repository-specific model
	 */
	protected abstract EFactory getEFactory();

	/**
	 * The {@link EPackage} instance to be used during the {@link IModelRepository#adaptModel(EObject) model adaptation}
	 * 
	 * @return the {@link EPackage} that corresponds to the repository-specific model
	 */
	protected abstract EPackage getEPackage();

	/**
	 * Notifies all registered {@link IModelRepositoryListener} 
	 * about a {@link RepositoryStatus status} shift.	
	 */
	private void fireStatusChanged(RepositoryStatus newStatus) {
		for (IModelRepositoryListener listener : listeners) {
			listener.statusChanged(status, newStatus);
		}
	}
	
}
