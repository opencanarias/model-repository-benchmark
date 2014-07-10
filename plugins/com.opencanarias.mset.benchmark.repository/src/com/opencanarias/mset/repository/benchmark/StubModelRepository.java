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

/**
 * Abstract class for a repository that does not need lifecycle control
 * (i.e., XMI, neo4emf...)
 * 
 * @author vroldan
 *
 */
public abstract class StubModelRepository extends AbstractModelRepository {

	@Override
	public void start() {
		setStatus(RepositoryStatus.RUNNING);
	}

	@Override
	public void stop() {
		setStatus(RepositoryStatus.STOPPED);
	}

	@Override
	public void clean() { }

	@Override
	public String getRepositoryURI() {
		return ""; //$NON-NLS-1$
	}
}
