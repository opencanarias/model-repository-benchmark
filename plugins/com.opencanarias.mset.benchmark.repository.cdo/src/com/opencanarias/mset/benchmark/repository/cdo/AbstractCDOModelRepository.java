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
package com.opencanarias.mset.benchmark.repository.cdo;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.eclipse.emf.cdo.common.model.CDOPackageInfo;
import org.eclipse.emf.cdo.eresource.CDOResource;
import org.eclipse.emf.cdo.net4j.CDONet4jSession;
import org.eclipse.emf.cdo.net4j.CDONet4jSessionConfiguration;
import org.eclipse.emf.cdo.net4j.CDONet4jUtil;
import org.eclipse.emf.cdo.server.CDOServerUtil;
import org.eclipse.emf.cdo.server.IRepository;
import org.eclipse.emf.cdo.server.IStore;
import org.eclipse.emf.cdo.server.net4j.CDONet4jServerUtil;
import org.eclipse.emf.cdo.session.CDOSession;
import org.eclipse.emf.cdo.spi.common.revision.InternalCDORevisionManager;
import org.eclipse.emf.cdo.transaction.CDOTransaction;
import org.eclipse.emf.cdo.util.CDOUtil;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.net4j.Net4jUtil;
import org.eclipse.net4j.acceptor.IAcceptor;
import org.eclipse.net4j.connector.IConnector;
import org.eclipse.net4j.jvm.JVMUtil;
import org.eclipse.net4j.tcp.TCPUtil;
import org.eclipse.net4j.util.container.ContainerUtil;
import org.eclipse.net4j.util.container.IManagedContainer;
import org.eclipse.net4j.util.lifecycle.LifecycleUtil;
import org.eclipse.net4j.util.om.OMPlatform;
import org.eclipse.net4j.util.om.log.AbstractLogHandler;
import org.eclipse.net4j.util.om.log.EclipseLoggingBridge;
import org.eclipse.net4j.util.om.log.OMLogger;
import org.eclipse.net4j.util.om.log.OMLogger.Level;
import org.eclipse.net4j.util.om.trace.PrintTraceHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opencanarias.mset.benchmark.repository.model.cdo.node.NodeFactory;
import com.opencanarias.mset.benchmark.repository.model.cdo.node.NodePackage;
import com.opencanarias.mset.repository.benchmark.AbstractModelRepository;
import com.opencanarias.mset.repository.benchmark.IModelRepository.IModelRepositoryListener.EventLevel;

public abstract class AbstractCDOModelRepository extends AbstractModelRepository {

	private String repoName = "repo"; //$NON-NLS-1$
	
	private final Logger logger = LoggerFactory.getLogger(getClass());

	private IAcceptor acceptor = null;
	private IRepository repository = null;
	private IManagedContainer container = null;
	
	public AbstractCDOModelRepository() {
		super();
		OMPlatform.INSTANCE.setDebugging(false);
		OMPlatform.INSTANCE.removeLogHandler(EclipseLoggingBridge.INSTANCE);
		OMPlatform.INSTANCE.addLogHandler(new AbstractLogHandler() {

			@Override
			protected void writeLog(OMLogger logger, org.eclipse.net4j.util.om.log.OMLogger.Level level, String msg, Throwable t) throws Throwable {
				if (level == Level.ERROR) {
					fireRepositoryEvent(EventLevel.ERROR, msg, t);
				} else if (level == Level.WARN) {
					fireRepositoryEvent(EventLevel.WARN, msg, t);
				} else if (level == Level.INFO) {
					fireRepositoryEvent(EventLevel.INFO, msg, t);
				} else if (level == Level.DEBUG) {
					fireRepositoryEvent(EventLevel.INFO, msg, t);
				}
			}
			
		});
		OMPlatform.INSTANCE.addTraceHandler(PrintTraceHandler.CONSOLE);
	}
	
	protected IAcceptor createAcceptor(IManagedContainer container) {
		return JVMUtil.getAcceptor(container, getRepositoryName());
	}
	
	protected IConnector createConnector(IManagedContainer container) {
		IConnector connector = JVMUtil.getConnector(container, getRepositoryName()); //$NON-NLS-1$
		connector.setOpenChannelTimeout(3000);
		return connector;
	}

	protected IRepository createRepository() {
		return CDOServerUtil.createRepository(getRepositoryName(), createStore(), createRepositoryProperties());
	}

	protected String getSupportingBranches() {
		return "false"; //$NON-NLS-1$
	}

	protected String getSupportingAudits() {
		return "false"; //$NON-NLS-1$
	}
	
	protected String getRepositoryName() {
		return repoName;
	}

	protected IManagedContainer getManagedContainer() {
		// IPluginContainer.INSTANCE;		
		// Manually prepare a container each time
		if (container == null) {
			logger.trace("Initialising CDO Managed Container");
			container = ContainerUtil.createContainer();
		    Net4jUtil.prepareContainer(container); // Register Net4j factories
		    TCPUtil.prepareContainer(container); // Register TCP factories
		    JVMUtil.prepareContainer(container); // Register JVM factories
		    CDONet4jUtil.prepareContainer(container); // Register CDO factories
		    CDONet4jServerUtil.prepareContainer(container); // Register CDO Server Net4j Factories
		    ContainerUtil.prepareContainer(container);
			LifecycleUtil.activate(container);
		    logger.trace("Finished initialising CDO Managed Container");
		}
		return container;
	}

	protected CDOSession createSession() {
		// Create connector
		IConnector connector = createConnector(getManagedContainer());

		// Create configuration
		CDONet4jSessionConfiguration configuration = CDONet4jUtil.createNet4jSessionConfiguration();
		configuration.setRepositoryName(getRepositoryName());
		configuration.setConnector(connector);

		// Open session
		CDONet4jSession session = configuration.openNet4jSession();
		session.options().setPassiveUpdateEnabled(true);
		session.options().setGeneratedPackageEmulationEnabled(true);

		// load persisted EPackages in the session (EPackage emulation must be enabled)
		for (CDOPackageInfo info : session.getPackageRegistry() .getPackageInfos()) {
			session.getPackageRegistry().put(info.getPackageURI(), info.getEPackage());
		}

		session.options().getNet4jProtocol().setTimeout(-1L);
		session.options().setCommitTimeout(Integer.MAX_VALUE);

		session.options().setCollectionLoadingPolicy(CDOUtil.createCollectionLoadingPolicy(0, 300));
		return session;
	}
	
	protected CDOTransaction createTransaction() {
		CDOTransaction transaction = createSession().openTransaction();
		transaction.options().setRevisionPrefetchingPolicy(CDOUtil.createRevisionPrefetchingPolicy(100));
		return transaction;
	}

	protected String getTempDirectoryPath() {		
		return getTempDirectory().getAbsolutePath() + "\\"; //$NON-NLS-1$
	}
	
	protected CDOResource getResource(String path) {
		return createTransaction().getOrCreateResource(path);
	}
	
	protected CDOResource createRandomResource() {
		return getResource(UUID.randomUUID().toString());
	}
	
	protected EFactory getEFactory() {
		return NodeFactory.eINSTANCE;
	}
	
	protected EPackage getEPackage() {
		return NodePackage.eINSTANCE;
	}

	@Override
	public Resource createResource() {
		logger.trace("Creating CDOResource");
		Resource res = createRandomResource();
		logger.trace("Created CDOResource");
		return res;
	}

	@Override
	public Resource loadResource(URI uri) {
		return getResource(uri.path());
	}

	@Override
	public EObject loadEObject(URI resourceURI, String uriFragment) {
		return loadResource(resourceURI).getEObject(uriFragment);
	}
	
	protected abstract IStore createStore();

	protected void shutDownBackend() {};

	protected void doClean() {}

	/**
	 * Returns true if the repository must be stopped before proceeding 
	 * to execute the backend cleansing. False if it may be cleaned online.
	 */
	protected boolean requiresOfflineClean() {
		return true;
	}
	
	private Map<String, String> createRepositoryProperties() {
		Map<String, String> props = new HashMap<String, String>();
		props.put(IRepository.Props.SUPPORTING_AUDITS, getSupportingAudits());
	    props.put(IRepository.Props.SUPPORTING_BRANCHES, getSupportingBranches());
		return props;
	}

	public void start() {
		if (getStatus() == RepositoryStatus.RUNNING) {
			return;
		}
		setStatus(RepositoryStatus.STARTING);
		try {
			logger.trace("Starting Server {}", getName());
			container = getManagedContainer();
	
			logger.trace("Starting IAcceptor");
			if (acceptor == null)
				acceptor = createAcceptor(container);
	
			logger.trace("Starting IRepository");
			if (repository == null) {
				repository = createRepository();
				CDOServerUtil.addRepository(container, repository);
			}
		} catch (Exception e) {
			logger.error("Error while starting CDO Model Repository", e);
			setStatus(RepositoryStatus.ERROR);
			return;
		}
		setStatus(RepositoryStatus.RUNNING);
	}

	protected IRepository getRepository() {
		return repository;
	}
	
	public void stop() {
		if (getStatus() == RepositoryStatus.STOPPED) {
			return;
		}
		setStatus(RepositoryStatus.STOPPING);
		if (acceptor != null)
			LifecycleUtil.deactivate(acceptor);

		if (repository != null) {
			// this actually frees a lot of memory!
			clearRepositoryCache();
			LifecycleUtil.deactivate(repository);
		}

		if (container != null) {
			LifecycleUtil.deactivate(container);
		}
		
		acceptor = null;
		repository = null;
		container = null;
		shutDownBackend();
		setStatus(RepositoryStatus.STOPPED);
	}

	protected void clearRepositoryCache() {
		if (getRepository() != null) {
			InternalCDORevisionManager manager = ((InternalCDORevisionManager) getRepository().getRevisionManager());
			if (manager != null && manager.getCache() != null) {
				manager.getCache().clear();
			}
		}
	}
	
	public void clean() {
		RepositoryStatus originalStatus = getStatus();
		if (requiresOfflineClean()) {
			if (originalStatus != RepositoryStatus.STOPPED) {
				stop();
			}
		}
		setStatus(RepositoryStatus.CLEANING);
		try {
			doClean();
		} catch (Throwable t) {
			logger.error("Error while performing repository cleansing", t);
			fireRepositoryEvent(EventLevel.ERROR, "Error on repository clean", t);			
		}
		if (requiresOfflineClean()) {
			if (originalStatus == RepositoryStatus.RUNNING) {
				start();				
			}
		}
		setStatus(originalStatus);
	}

	public String getRepositoryURI() {
		return repository.getUUID();
	}
}
