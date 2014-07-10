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
package com.opencanarias.mset.repository.benchmark;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * Encapsulates an input EMF-based model and serves as input for {@link IBenchmarkCase}
 *  
 * @author jdavidlb
 */
public interface IBenchmarkModel {
	
	/**
	 * Returns the {@link EObject} root of the model tree.
	 * The tree should not be contained in a {@link Resource}
	 * 
	 * @return
	 */
	public EObject getRoot();
}
