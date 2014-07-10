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
package com.opencanarias.mset.benchmark.repository.cdo.util;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Configuration {

	private static final String CONFIG_FILE_NAME = "config"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(CONFIG_FILE_NAME);

	public static final Configuration INSTANCE = new Configuration();

	public static Configuration getInstance() {
		return INSTANCE;
	}

	protected ResourceBundle getResourceBundle() {
		return RESOURCE_BUNDLE;
	}
	
	public String getString(String key) {
		try {
			return getResourceBundle().getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}

	public int getInt(String key) {
		try {
			String value = getResourceBundle().getString(key);
			return Integer.parseInt(value);
		} catch (MissingResourceException e) {
			return -1;
		}
	}
	
}
