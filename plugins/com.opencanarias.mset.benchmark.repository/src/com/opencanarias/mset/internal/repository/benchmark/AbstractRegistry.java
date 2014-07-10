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
package com.opencanarias.mset.internal.repository.benchmark;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractRegistry<T> {
	
	private List<T> registeredElements = Collections.synchronizedList(new ArrayList<T>());
	
	protected List<T> getRegisteredElements() {
		return Collections.unmodifiableList(registeredElements);
	}

	protected void addElement(T element) {
		if (!registeredElements.contains(element)) {
			registeredElements.add(element);
		}
	}

	protected void removeElement(T element) {
		registeredElements.remove(element);

	}

	protected void removeAllElements() {
		registeredElements.clear();
	}
}