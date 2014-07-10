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
package com.opencanarias.mset.internal.benchmark.repository.app;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

/**
 * An Equinox application that starts benchmarking all registered repositories
 * right away.
 * <p>
 * The main loop may be stopped any moment through OSGI console commands
 * thanks to {@link BenchmarkCommandProvider}.
 *  
 * @author vroldan
 *
 */
public class BenchmarkApplication implements IApplication {

	@Override
	public Object start(IApplicationContext context) throws Exception {
		BenchmarkManager.INSTANCE.benchmarkAll();
		return null;
	}

	@Override
	public void stop() {

	}
}
