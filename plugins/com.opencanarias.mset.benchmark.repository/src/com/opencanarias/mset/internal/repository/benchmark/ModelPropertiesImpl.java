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

import java.text.MessageFormat;

import com.opencanarias.mset.repository.benchmark.IModelProperties;

public class ModelPropertiesImpl implements IModelProperties {
	
	private int count = 0;

	private int depth = 0;

	private int width = 0;

	private int valueSize = 0;

	private int memUsage = NOT_MEMORY_SENSITIVE;
	
	public int getCount() {
		return count;
	}

	public void setCount(final int count) {
		if (count < 0) {
			throw new IllegalArgumentException("count parameter must be non-negative");
		}
		this.count = count;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(final int depth) {
		if (depth < 0) {
			throw new IllegalArgumentException("depth parameter must be non-negative");
		}
		this.depth = depth;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(final int width) {
		if (width < 0) {
			throw new IllegalArgumentException("width parameter must be non-negative");
		}
		this.width = width;
	}

	public int getValueLength() {
		return valueSize;
	}

	public void setValueLength(int valueSize) {
		if (valueSize < 0) {
			throw new IllegalArgumentException("valueSize parameter must be non-negative");
		}
		this.valueSize = valueSize;
	}

	public int getMemUsage() {
		return memUsage;
	}

	public void setMemUsage(final int memPercentageUsage) {
		if (memPercentageUsage > IModelProperties.NOT_MEMORY_SENSITIVE && memPercentageUsage < 5) {
			throw new IllegalArgumentException("Memory left should be over 5% for safety reasons");
		}
		if (memPercentageUsage > 100) {
			throw new IllegalArgumentException("Cannot achieve free memory bigger than 100%");
		}
		if (memPercentageUsage < IModelProperties.NOT_MEMORY_SENSITIVE) {
			throw new IllegalArgumentException("Negative percentage of free memory is not allowed: value must range 5% - 100%");
		}
		this.memUsage = memPercentageUsage;
	}

	@Override
	public String toString() {
		return MessageFormat.format("ModelProps: count:{0} depth:{1} width:{2} valueSize:{3} mem%:{4}",
						getCount(), getDepth(), getWidth(), getValueLength(),
						getMemUsage());
	}
}
