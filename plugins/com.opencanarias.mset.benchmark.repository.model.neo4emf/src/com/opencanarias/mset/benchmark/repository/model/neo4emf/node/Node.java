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
package com.opencanarias.mset.benchmark.repository.model.neo4emf.node;

import org.eclipse.emf.common.util.EList;

import fr.inria.atlanmod.neo4emf.INeo4emfObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Node</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.opencanarias.mset.benchmark.repository.model.neo4emf.node.Node#getChild <em>Child</em>}</li>
 *   <li>{@link com.opencanarias.mset.benchmark.repository.model.neo4emf.node.Node#getString <em>String</em>}</li>
 *   <li>{@link com.opencanarias.mset.benchmark.repository.model.neo4emf.node.Node#getInteger <em>Integer</em>}</li>
 *   <li>{@link com.opencanarias.mset.benchmark.repository.model.neo4emf.node.Node#getByteArray <em>Byte Array</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.opencanarias.mset.benchmark.repository.model.neo4emf.node.NodePackage#getNode()
 * @model
 * @extends INeo4emfObject
 * @generated
 */
public interface Node extends INeo4emfObject {

	/**
	 * Returns the value of the '<em><b>Child</b></em>' containment reference list.
	 * The list contents are of type {@link com.opencanarias.mset.benchmark.repository.model.neo4emf.node.Node}.
	 * <!-- begin-user-doc -->
	 *XX6a
	 * <p>
	 * If the meaning of the '<em>Child</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Child</em>' containment reference list.
	 * @see com.opencanarias.mset.benchmark.repository.model.neo4emf.node.NodePackage#getNode_Child()
	 * @model containment="true"
	 * @generated
	 */
	EList<Node> getChild();
	/**
	 * Returns the value of the '<em><b>String</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 *XX6a
	 * <p>
	 * If the meaning of the '<em>String</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>String</em>' attribute.
	 * @see #setString(String)
	 * @see com.opencanarias.mset.benchmark.repository.model.neo4emf.node.NodePackage#getNode_String()
	 * @model
	 * @generated
	 */
	String getString();
	/**
	 * Sets the value of the '{@link com.opencanarias.mset.benchmark.repository.model.neo4emf.node.Node#getString <em>String</em>}' attribute.
	 * <!-- begin-user-doc -->
	 *YY1
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>String</em>' attribute.
	 * @see #getString()
	 * @generated
	 */
	void setString(String value);

	/**
	 * Returns the value of the '<em><b>Integer</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 *XX6a
	 * <p>
	 * If the meaning of the '<em>Integer</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Integer</em>' attribute.
	 * @see #setInteger(int)
	 * @see com.opencanarias.mset.benchmark.repository.model.neo4emf.node.NodePackage#getNode_Integer()
	 * @model
	 * @generated
	 */
	int getInteger();
	/**
	 * Sets the value of the '{@link com.opencanarias.mset.benchmark.repository.model.neo4emf.node.Node#getInteger <em>Integer</em>}' attribute.
	 * <!-- begin-user-doc -->
	 *YY1
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Integer</em>' attribute.
	 * @see #getInteger()
	 * @generated
	 */
	void setInteger(int value);

	/**
	 * Returns the value of the '<em><b>Byte Array</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 *XX6a
	 * <p>
	 * If the meaning of the '<em>Byte Array</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Byte Array</em>' attribute.
	 * @see #setByteArray(byte[])
	 * @see com.opencanarias.mset.benchmark.repository.model.neo4emf.node.NodePackage#getNode_ByteArray()
	 * @model
	 * @generated
	 */
	byte[] getByteArray();
	/**
	 * Sets the value of the '{@link com.opencanarias.mset.benchmark.repository.model.neo4emf.node.Node#getByteArray <em>Byte Array</em>}' attribute.
	 * <!-- begin-user-doc -->
	 *YY1
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Byte Array</em>' attribute.
	 * @see #getByteArray()
	 * @generated
	 */
	void setByteArray(byte[] value);





} // Node
