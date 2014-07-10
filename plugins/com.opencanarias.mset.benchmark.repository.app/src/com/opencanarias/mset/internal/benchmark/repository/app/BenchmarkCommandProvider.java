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

import java.text.MessageFormat;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;

import com.opencanarias.mset.repository.benchmark.IModelRepository;

/**
 * OSGi console command contribution for the benchmarking framework. Responds to:
 * <p>
 * <code>benchmark [repositoryName | all | stop]</code><p>
 * where <code>repository</code> is either an {@link IModelRepository} name, or <code>all</code>
 * in case we would like to benchmark all repositories. We would also provide the argument
 * <code>stop</code> to force the current benchmarking to finalize.
 * 
 * @author vroldan
 *
 */
public class BenchmarkCommandProvider implements CommandProvider {

	private static final String ARGUMENT_STOP = "stop"; //$NON-NLS-0$

	private static final String ARGUMENT_ALL = "all"; //$NON-NLS-0$
	
	private static final String ARGUMENT_LIST = "list"; //$NON-NLS-0$
	
	private static BenchmarkThread thread = new BenchmarkThread(); 
	
	@Override
	public String getHelp() {
		return "benchmark (repositoryName [iterations] | all [iterations] | list | stop)";
	}

	public Object _benchmark(CommandInterpreter interpreter) {
		String argument1 = interpreter.nextArgument();
		String argument2 = interpreter.nextArgument();
		if (argument1 == null) {
			interpreter.println("Benchmark option must be specified: <repository_name> | all | list | stop");
			return null;
		}
		
		if (argument1.equalsIgnoreCase(ARGUMENT_STOP)) {
			BenchmarkManager.INSTANCE.stop();
		} else if (argument1.equalsIgnoreCase(ARGUMENT_LIST)) {
			interpreter.println("List of available IModelRepository instances");
			for (String repoName : BenchmarkManager.INSTANCE.listRepositories()) {
				interpreter.println(repoName);
			}			
		} else {
			int iterations = BenchmarkManager.DEFAULT_ITERATION_COUNT;
			if (argument2 != null) {
				iterations = Integer.parseInt(argument2);
			}
			if (iterations < 1) {
				interpreter.println("Iteration must be a positive integer bigger than 1");
			} else {
				if (BenchmarkManager.INSTANCE.repositoryExists(argument1)) {
					if (!benchmarkAsync(argument1, iterations)) {
						interpreter.println("A benchmark is running, please wait until it finishes, of stop it using the \"stop\" argument");
					};
				} else {
					interpreter.println(MessageFormat.format("Unkown benchmark option or model repository name \"{0}\"", argument1));
				}
			}
		}
		
		return null;
	}

	private boolean benchmarkAsync(final String repository, int iterations) {
		synchronized (thread) {
			if (thread != null && thread.isAlive()) {
				return false;
			}
			thread = new BenchmarkThread();
			thread.setRepository(repository, iterations);
			thread.start();
		}
		return true;
	}

	private static class BenchmarkThread extends Thread {
		
		private String repository;
		private int iterations;
		
		public void setRepository(final String repository, int iterations) {
			this.repository = repository;
			this.iterations = iterations;
		}
		
		@Override
		public void run() {
			if (ARGUMENT_ALL.equalsIgnoreCase(repository)) {
				BenchmarkManager.INSTANCE.benchmarkAll(iterations);	
			} else {
				BenchmarkManager.INSTANCE.benchmarkRepository(repository, iterations);		
			}
			super.run();
		}
	}
	
}
