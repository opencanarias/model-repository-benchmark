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
package com.opencanarias.mset.benchmark.repository.tests;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opencanarias.mset.repository.benchmark.BenchmarkUtils;
import com.opencanarias.mset.repository.benchmark.IBenchmarkMeter;

public class BenchmarkMeterTest {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Test
	public void testMeter() throws Throwable {
		logger.debug("Testing meter");
		IBenchmarkMeter meter = BenchmarkUtils.getBenchmarkMeter(100);
		meter.startMeasuring();		
		Thread.sleep(1000);
		meter.stopMeasuring();
		logger.debug(meter.getResult().toString());
		
		// TODO: do some assertions here
	}
}
