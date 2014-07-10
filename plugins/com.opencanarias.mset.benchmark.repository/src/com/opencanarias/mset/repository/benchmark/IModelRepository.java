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

import org.eclipse.emf.ecore.EObject;

/**
 * A model repository represents a persistence mechanisms for an EMF-based model.
 * A repository is a stateful entity, capable of providing basic model operations
 * as specified at {@link IModelOperation}. It must be able to keep state
 * even when stopped, unless it is explicitly cleaned.
 * <p>
 * Every {@link IModelRepository} has different characteristics and features,
 * sometimes overlapping, in other case exclusive, but the main purpose
 * of the {@link IModelRepository} is to hide implementation and allow the client
 * to measure its performance when operating against an {@link IBenchmarkCase}.
 * <p>
 * Bear in mind the state itself may have side-effects on the performance
 * for a given {@link IBenchmarkCase}, so ideally repositories should be clean
 * before executing the case, unless thats a requirement for the case.
 * 
 * @author vroldan
 *
 */
public interface IModelRepository extends IModelOperation {

	/**
	 * This enum models the status of the {@link IModelRepository}, 
	 * so clients may be aware of what is the current status of the 
	 * repository before committing some actions. 
	 * 
	 * @author vroldan
	 *
	 */
	public enum RepositoryStatus {
		/**
		 * The repository is initializing, a transitory state before
		 * becoming {@link RepositoryStatus#RUNNING}. It is unavailable while in this state.
		 */
		STARTING,
		/**
		 * The repository is started and ready to operate with it. It should remain
		 * in this state unless any lifecycle-related operation is triggered, such
		 * as {@link IModelRepository#stop() stop} or {@link IModelRepository#clean() clean}.
		 * 
		 * @see IModelRepository#stop()
		 * @see IModelRepository#clean()
		 * @see IModelRepository#restart()
		 */
		RUNNING,
		/**
		 * The repository is stopping its service, so it is unavailable while in this state.
		 * It is a transitory state before it becomes {@link RepositoryStatus#STOPPED} 
		 */
		STOPPING,
		/**
		 * The repository is shut down, and wont accept any {@link IModelOperation model operation}, 
		 * but it is ready to be started if commanded.
		 * 
		 * @see IModelRepository#start()
		 * @see IModelRepository#clean()
		 */
		STOPPED,
		/**
		 * The repository is restarting itself. It is unavailable while in this state.
		 * It everything goes well, it should transit to {@link RepositoryStatus#RUNNING}
		 */
		RESTARTING,
		/**
		 * The repository is cleaning itself. It is unavailable while in this state.
		 * After the cleansing process, it should transit to its original state before
		 * commanded to {@link IModelRepository#stop() clean} itself
		 * 
		 * @see IModelRepository#clean()
		 */
		CLEANING,
		/**
		 * If some unrecoverable error happens during its lifecycle, the {@link IModelRepository}
		 * transit to error state. In this state the {@link IModelRepository} is unresponsive to
		 * any {@link IModelOperation model operation}.   
		 */
		ERROR
	}
	
	/**
	 * Identifier for this model repository implementation
	 */
	public String getName();
	
	/**
	 * Commands the repository to start. The repository
	 * is considered started when it reaches {@link RepositoryStatus#RUNNING}.
	 * Before that, the repository indicates status {@link RepositoryStatus#STARTING}
	 * during the set up process.
	 */
	public void start();

	/**
	 * Commands the repository to stop. The repository
	 * is considered stopped when it reaches {@link RepositoryStatus#STOPPED}.
	 * Before that, the repository indicates status {@link RepositoryStatus#STOPPING}
	 * during the shutdown process.
	 */
	public void stop();
	
	/**
	 * Commands the repository to restart.
	 */
	public void restart();
	
	/**
	 * Commands the repository to remove any persistent state (i.e.,
	 * model data). May imply the repository going off for a while.
	 * In case shutting down is required, the repository must be
	 * automatically left in the original state before being 
     * commanded to clean.
	 */
	public void clean();

	/**
	 * Determines whether this repository is available and may be 
	 * started and be ready to operate
	 * 
	 * @return true if the repository is ready to be started and operate,
	 *              false otherwise.
	 */
	public boolean isAvailable();
	
	/**
	 * Returns the current state of the repository
	 * 
	 * @return an RepositoryStatus instance representing the current status
	 *         of the repository.
	 */
	public RepositoryStatus getStatus();

	/**
	 * Gets the string representing the URI of the repository in case it
	 * is necessary (i.e., cdo://localhost/repository1). Otherwise, it would
	 * return an empty String.
	 * 
	 * @return the String representing the URI of the repository
	 */
	public String getRepositoryURI();	
	
	/**
	 * In some scenarios, repositories do not support plain {@link EObject},
	 * this is, they support model persistence and operation only if the 
	 * model extends specially crafted implementations of {@link EObject}.
	 * For instance, the CDO framework defines the CDOObject.
	 * <p>
	 * In such case, this method contain the logic that converts
	 * a plain {@link EObject} model into a repository-specific one.
	 * 
	 * @param model a repository agnostic {@link EObject} tree
	 * @return a repository-compatible version of the input {@link EObject} tree
	 */
	public EObject adaptModel(EObject model);
	
	/**
	 * Registers a new {@link IModelRepositoryListener} to this {@link IModelRepository}.
	 * The listener will be synchronously notified on every relevant event.
	 * 
	 * @param listener the listener that shall be notified upon {@link IModelRepository} events
	 */
	public void addListener(IModelRepositoryListener listener);
	
	/**
	 * Returns true if the argument listener is already registered, false otherwise. 
	 */
	public boolean containsListener(IModelRepositoryListener listener);
	
	/**
	 * Unregisters the argument {@link IModelRepositoryListener listener}, so 
	 * it wont be notified upon events anymore
	 * 
	 * @param listener the listener instance to unregister
	 */
	public void removeListener(IModelRepositoryListener listener);

	/**
	 * Determines if this repository is able to handle interaction with the argument {@link Object}.
	 * 
	 * @return true if the {@link IModelRepository} is able to interact with this kind of Object
	 */
	public boolean supports(Object object);

	/**
	 * Interface for a repository event listener. Receives {@link RepositoryStatus status}
	 * change notification and other events (exceptions, warnings, general purpose information...).
	 * 
	 * @author vroldan
	 *
	 */
	public static interface IModelRepositoryListener {
	
		public enum EventLevel {
			INFO,
			WARN,
			ERROR
		}
		
		/**
		 * Called whenever a {@link RepositoryStatus} shift has taken place,
		 * indicating the previous status and the current status
		 * 
		 * @param oldStatus the previous status before the event took place
		 * @param newStatus the current status after the event finished
		 */
		public void statusChanged(RepositoryStatus oldStatus, RepositoryStatus newStatus);

		/**
		 * Called whenever some event in the repository logic threw an exception.
		 * Indicates the level of severity, a message and the {@link Throwable} thrown.
		 */
		public void repositoryEvent(EventLevel level, String msg, Throwable t);

		/**
		 * Called whenever some event in the repository logic took place.
		 * Indicates the level of severity a message.
		 */
		public void repositoryEvent(EventLevel level, String msg);
		
	}
}
