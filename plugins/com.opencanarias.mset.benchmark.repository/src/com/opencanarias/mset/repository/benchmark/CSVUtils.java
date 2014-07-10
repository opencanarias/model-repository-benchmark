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
 * Utility class to format {@link IBenchmarkResult} into CSV format 
 * to ease readability and further analysis of the results.
 * 
 * @author vroldan
 *
 */
public class CSVUtils {	

	private static String FIELD_SEPARATOR = "\t"; //$NON-NLS-1$
	
	private static String LINE_SEPARATOR = "\r\n"; //$NON-NLS-1$

	private static String CSV_RESULT_HEADER = "Case" + FIELD_SEPARATOR 
			+ "Repository" + FIELD_SEPARATOR 
			+ "size" + FIELD_SEPARATOR 
			+ "depth" + FIELD_SEPARATOR 
			+ "width" + FIELD_SEPARATOR 
			+ "realTime" + FIELD_SEPARATOR 
			+ "avgUsed" + FIELD_SEPARATOR 
			+ "avgFree" + FIELD_SEPARATOR 
			+ "avgTotalAvailable" + FIELD_SEPARATOR 
			+ "avgMaxAvailable" + LINE_SEPARATOR;
	
	private static String CSV_AGGREGATION_HEADER = "CaseName" + FIELD_SEPARATOR
			+ "RepoName" + FIELD_SEPARATOR
			+ "Iterations" + FIELD_SEPARATOR
			+ "avgRealTime" + FIELD_SEPARATOR
			+ "realTimeDev" + FIELD_SEPARATOR
			+ "minRealTime" + FIELD_SEPARATOR 
			+ "maxRealTime" + FIELD_SEPARATOR 
			+ LINE_SEPARATOR;
	
	/**
	 * Returns the String constant representing the CSV field separator
	 * 
	 * @return the String constant representing the CSV field separator
	 */
	public static String getFieldSeparator() {
		return FIELD_SEPARATOR;
	}
	
	/**
	 * Returns the String constant representing the CSV line separator
	 * 
	 * @return the String constant representing the CSV line separator
	 */
	public static String getLineSeparator() {
		return LINE_SEPARATOR;
	}
	
	/**
	 * Returns the CSV header listing all the attributes of an {@link IBenchmarkResult}
	 * 
	 * @return the String constant representing the header listing with all attributes
	 */
	public static String getResultHeader() {
		return CSV_RESULT_HEADER;
	}
	
	/**
	 * Returns the CSV header listing all the attributes for an {@link IAggregatedResult}
	 * 
	 * @return the String constant representing the header listing with all attributes
	 */
	public static String getAggregationHeader() {
		return CSV_AGGREGATION_HEADER;
	}
	
	/**
	 * Returns the CSV-formated entry for the argument {@link IBenchmarkResult}. 
	 * Attributes are separated using
	 * {@link CSVUtils#getFieldSeparator()} and lines are separated using
	 * {@link CSVUtils#getLineSeparator()}
	 * 
	 * @return a String listing all the result's attributes, with CSV Format.
	 */
	public static String toEntry(IBenchmarkResult result) {
		return  (result.getBenchmarkCase() != null ? result.getBenchmarkCase().getName() : "NoCase") + FIELD_SEPARATOR  + 
				(result.getModelRepository() != null ? result.getModelRepository().getName() : "NoRepository") + FIELD_SEPARATOR 
				+ result.getModelProperties().getCount() + FIELD_SEPARATOR 
				+ result.getModelProperties().getDepth() + FIELD_SEPARATOR
				+ result.getModelProperties().getWidth() + FIELD_SEPARATOR
				+ result.getRealTime() + FIELD_SEPARATOR 
				+ result.getAverageUsedMemory() + FIELD_SEPARATOR 
				+  result.getAverageFreeMemory() + FIELD_SEPARATOR 
				+ result.getAverageTotalAvailableMemory() + FIELD_SEPARATOR 
				+ result.getMaxAvailableMemory() 
				+ (result.isFailed() ? FIELD_SEPARATOR + "failed" + LINE_SEPARATOR : LINE_SEPARATOR);				
	}
	
	/**
	 * Returns the CSV-formated entry for the argument {@link IAggregatedResult}. 
	 * Attributes are separated using
	 * {@link CSVUtils#getFieldSeparator()} and lines are separated using
	 * {@link CSVUtils#getLineSeparator()}
	 * 
	 * @return a CSV formated String, listing all the aggregation attributes
	 */
	public static String toEntry(IAggregatedResult result) {
		String caseName = result.getAggregatedResults().size() > 0 ? result.getAggregatedResults().get(0).getBenchmarkCase().getName() : "NoName";
		String repoName = result.getAggregatedResults().size() > 0 ? result.getAggregatedResults().get(0).getModelRepository().getName() : "NoName";
		return  caseName + FIELD_SEPARATOR  
				+ repoName + FIELD_SEPARATOR
				+ result.getAggregatedResults().size() + FIELD_SEPARATOR
				+ result.getAvgRealTime() + FIELD_SEPARATOR
				+ result.getRealTimeDeviation() + FIELD_SEPARATOR
				+ result.getMinRealTime() + FIELD_SEPARATOR
				+ result.getMaxRealTime() + LINE_SEPARATOR;
	}
}
