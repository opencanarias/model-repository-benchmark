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
package com.opencanarias.mset.internal.benchmark.repository.cdo.db.h2;

import org.eclipse.net4j.acceptor.IAcceptor;
import org.eclipse.net4j.connector.IConnector;
import org.eclipse.net4j.tcp.TCPUtil;
import org.eclipse.net4j.util.container.IManagedContainer;

public class TCPH2CDOModelRepository extends JVMH2CDOModelRepository {
	
	private final String hostName = "localhost"; //$NON-NLS-1$
	
	@Override
	protected IAcceptor createAcceptor(IManagedContainer container) {
		return TCPUtil.getAcceptor(container, hostName);
	}

	@Override
	protected IConnector createConnector(IManagedContainer container) {
		return TCPUtil.getConnector(container, hostName);
	}
	
	@Override
	public String getName() {
		return "CDO-H2-TCP"; //$NON-NLS-1$
	}
}
