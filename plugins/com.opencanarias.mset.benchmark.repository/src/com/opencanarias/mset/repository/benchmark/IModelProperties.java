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

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;

/** 
 * Defines a set of attributes that determine the shape of 
 * an {@link EObject} tree. This is commonly used by
 * the {@link BenchmarkModelGenerator} class as input 
 * specification for the models to be generated.
 * 
 * @author vroldan
 * @see BenchmarkModelGenerator
 */
public interface IModelProperties {

	/**
	 * Indicates this {@link IModelProperties} defines
	 * a model that is not memory sensitive, and so
	 * any generator should not care about the size in
	 * memory of the model.
	 */
	public final static int NOT_MEMORY_SENSITIVE = -1;
	
	/**
	 * Returns the number of {@link EObject} a model should have.
	 * 
	 * @return a non negative number of {@link EObject} 
	 */
	public int getCount();

	/**
	 * Sets the number of {@link EObject} a model should have
	 * 
	 * @param count a non negative number of {@link EObject}
	 */
	public void setCount(final int count);

	/**
	 * Indicates the maximum length any containment path within a model
	 * may have, this is, a certain {@link EObject} in a model should 
	 * not have more than this number of parents above it.
	 *   
	 * @return a non-negative integer defining the max length 
	 *         of any containment path within a model
	 */
	public int getDepth();

	/**
	 * Sets the maximum length any containment path within a model may have.
	 *   
	 * @param depth a non-negative integer defining the max length 
	 *              of any containment path within a model
	 */
	public void setDepth(final int depth);

	/**
	 * Defines the maximum number of children any {@link EObject} in the
	 * model may have.
	 * 
	 * @return a non-negative integer indicating the maximum number of
	 *         children any {@link EObject} within the model may have
	 */
	public int getWidth();

	/**
	 * Establishes the maximum number of children any {@link EObject} in
	 * the model may have.
	 * 
	 * @param width a non-negative integer indicating the maximum number
	 *              of children any {@link EObject} within the model may have
	 */
	public void setWidth(final int width);

	/**
	 * Returns the length of the value of every {@link EAttribute} in the model.
	 * This serves as a way to indicate how heavy {@link EObject eObjects} are in memory.
	 * For instance, indicates the length of strings or arrays.
	 * If set to zero, EAttributes should be left uninitialized. 
	 * 
	 * @return a non-negative integer indicating the length of every {@link EAttribute} value
	 *         in the model
	 */
	public int getValueLength();

	/**
	 * Sets the length every {@link EAttribute} value must have in the model.
	 *  
	 * @param valueLength a non-negative integer indicating the length
	 *                    of every {@link EAttribute} value
	 */
	public void setValueLength(final int valueLength);

	/**
	 * Returns a value representing a percentage (ranging 5 - 100) indicating
	 * the amount of free heap memory that must remain once this model is 
	 * instantiated. This attribute servers as a way to define big models that
	 * fit into the current process heap. Values under 5 are not allowed for 
	 * safety reasons.
	 * <p>
	 * If set to {@link IModelProperties#NOT_MEMORY_SENSITIVE}, memory sensitivity
	 * should be deactivated, and size relative to heap is ignored.
	 * 
	 * @return a percentage of free memory that must remain after a model with 
	 *         this {@link IModelProperties} is instantiated
	 */
	public int getMemUsage();

	/**
	 * Sets the percentage of memory that must remain free after a model
	 * with this {@link IModelProperties properties} is instantiated.
	 * 
	 * @param memUsage a non-negative integer ranging from 5 to 100 indicating
	 *                 the percentage of free heap memory remaining after
	 *                 the model is instantiated
	 */
	public void setMemUsage(final int memUsage);
	
}
