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
package com.opencanarias.mset.benchmark.repository.model.cdo.node;

import org.eclipse.emf.cdo.CDOObject;
import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Node</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.opencanarias.mset.benchmark.repository.model.cdo.node.Node#getChild <em>Child</em>}</li>
 *   <li>{@link com.opencanarias.mset.benchmark.repository.model.cdo.node.Node#getString <em>String</em>}</li>
 *   <li>{@link com.opencanarias.mset.benchmark.repository.model.cdo.node.Node#getInteger <em>Integer</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.opencanarias.mset.benchmark.repository.model.cdo.node.NodePackage#getNode()
 * @model
 * @extends CDOObject
 * @generated
 */
public interface Node extends CDOObject {
	/**
	 * Returns the value of the '<em><b>Child</b></em>' containment reference list.
	 * The list contents are of type {@link com.opencanarias.mset.benchmark.repository.model.cdo.node.Node}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Child</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Child</em>' containment reference list.
	 * @see com.opencanarias.mset.benchmark.repository.model.cdo.node.NodePackage#getNode_Child()
	 * @model containment="true"
	 * @generated
	 */
	EList<Node> getChild();

	/**
	 * Returns the value of the '<em><b>String</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>String</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>String</em>' attribute.
	 * @see #setString(String)
	 * @see com.opencanarias.mset.benchmark.repository.model.cdo.node.NodePackage#getNode_String()
	 * @model
	 * @generated
	 */
	String getString();

	/**
	 * Sets the value of the '{@link com.opencanarias.mset.benchmark.repository.model.cdo.node.Node#getString <em>String</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>String</em>' attribute.
	 * @see #getString()
	 * @generated
	 */
	void setString(String value);

	/**
	 * Returns the value of the '<em><b>Integer</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Integer</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Integer</em>' attribute.
	 * @see #setInteger(int)
	 * @see com.opencanarias.mset.benchmark.repository.model.cdo.node.NodePackage#getNode_Integer()
	 * @model
	 * @generated
	 */
	int getInteger();

	/**
	 * Sets the value of the '{@link com.opencanarias.mset.benchmark.repository.model.cdo.node.Node#getInteger <em>Integer</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Integer</em>' attribute.
	 * @see #getInteger()
	 * @generated
	 */
	void setInteger(int value);

} // Node
