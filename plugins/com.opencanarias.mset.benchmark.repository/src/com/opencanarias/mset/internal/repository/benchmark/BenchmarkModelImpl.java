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
package com.opencanarias.mset.internal.repository.benchmark;

import org.eclipse.emf.ecore.EObject;

import com.opencanarias.mset.repository.benchmark.IBenchmarkModel;

public class BenchmarkModelImpl implements IBenchmarkModel {
	
	private EObject model =  null;
	
	public BenchmarkModelImpl(final EObject model) {
		this.model = model;
	}

	public EObject getRoot() {
		return model;
	}
}
