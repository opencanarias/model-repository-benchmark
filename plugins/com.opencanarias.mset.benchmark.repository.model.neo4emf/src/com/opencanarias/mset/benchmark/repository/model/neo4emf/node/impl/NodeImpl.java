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
package com.opencanarias.mset.benchmark.repository.model.neo4emf.node.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import com.opencanarias.mset.benchmark.repository.model.neo4emf.node.Node;
import com.opencanarias.mset.benchmark.repository.model.neo4emf.node.NodePackage;

import fr.inria.atlanmod.neo4emf.INeo4emfNotification;
import fr.inria.atlanmod.neo4emf.INeo4emfResource;
import fr.inria.atlanmod.neo4emf.impl.Neo4emfObject;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Node</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.opencanarias.mset.benchmark.repository.model.neo4emf.node.impl.NodeImpl#getChild <em>Child</em>}</li>
 *   <li>{@link com.opencanarias.mset.benchmark.repository.model.neo4emf.node.impl.NodeImpl#getString <em>String</em>}</li>
 *   <li>{@link com.opencanarias.mset.benchmark.repository.model.neo4emf.node.impl.NodeImpl#getInteger <em>Integer</em>}</li>
 *   <li>{@link com.opencanarias.mset.benchmark.repository.model.neo4emf.node.impl.NodeImpl#getByteArray <em>Byte Array</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class NodeImpl extends Neo4emfObject implements Node {

	 
	
	/**
	 * The cached value of the data structure {@link DataNode <em>data</em> } 
	 * @generated
	 */
	 	protected DataNode data;
	 
	 
	 
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected NodeImpl() {
		super();
		
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	 
	protected DataNode getData(){
		if ( data == null || !(data instanceof DataNode)){
			data = new DataNode();
			if (isLoaded())
			((INeo4emfResource) this.eResource()).fetchAttributes(this);
			}
		return (DataNode) data;
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return NodePackage.Literals.NODE;
	}

	/**
	 * <!-- begin-user-doc -->
	 *XX7
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Node> getChild() {	
	   
		
		if (getData().child == null){
		getData().child = new EObjectContainmentEList<Node>(Node.class, this, NodePackage.NODE__CHILD);
			if (isLoaded()) 
			((INeo4emfResource) this.eResource()).getOnDemand(this, NodePackage.NODE__CHILD);			}
		return getData().child;	
	}
	/**
	 * <!-- begin-user-doc -->
	 *XX7
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getString() {	
	  		
		if ( isLoaded()) 
			eNotify(new ENotificationImpl(this, INeo4emfNotification.GET, NodePackage.NODE__STRING, null, null));
		return getData().string;	
	}
 /**
 * <!-- begin-user-doc -->
 *YY2
 * <!-- end-user-doc -->
 * @generated
 */
	public void setString(String newString) {
	
		
		String oldString = getData().string;
		getData().string = newString;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(
			this, Notification.SET,
			NodePackage.NODE__STRING,
			oldString, getData().string));
	}
	/**
	 * <!-- begin-user-doc -->
	 *XX7
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getInteger() {	
	  		
		if ( isLoaded()) 
			eNotify(new ENotificationImpl(this, INeo4emfNotification.GET, NodePackage.NODE__INTEGER, null, null));
		return getData().integer;	
	}
 /**
 * <!-- begin-user-doc -->
 *YY2
 * <!-- end-user-doc -->
 * @generated
 */
	public void setInteger(int newInteger) {
	
		
		int oldInteger = getData().integer;
		getData().integer = newInteger;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(
			this, Notification.SET,
			NodePackage.NODE__INTEGER,
			oldInteger, getData().integer));
	}
	/**
	 * <!-- begin-user-doc -->
	 *XX7
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public byte[] getByteArray() {	
	  		
		if ( isLoaded()) 
			eNotify(new ENotificationImpl(this, INeo4emfNotification.GET, NodePackage.NODE__BYTE_ARRAY, null, null));
		return getData().byteArray;	
	}
 /**
 * <!-- begin-user-doc -->
 *YY2
 * <!-- end-user-doc -->
 * @generated
 */
	public void setByteArray(byte[] newByteArray) {
	
		
		byte[] oldByteArray = getData().byteArray;
		getData().byteArray = newByteArray;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(
			this, Notification.SET,
			NodePackage.NODE__BYTE_ARRAY,
			oldByteArray, getData().byteArray));
	}
	/**
	 * <!-- begin-user-doc -->
	 *YY13
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case NodePackage.NODE__CHILD:
				return ((InternalEList<?>)getChild()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 *YY15
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case NodePackage.NODE__CHILD:
				return getChild();
			case NodePackage.NODE__STRING:
				return getString();
			case NodePackage.NODE__INTEGER:
				return getInteger();
			case NodePackage.NODE__BYTE_ARRAY:
				return getByteArray();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 *YY16
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case NodePackage.NODE__CHILD:
				getChild().clear();
				getChild().addAll((Collection<? extends Node>)newValue);
				return;
			case NodePackage.NODE__STRING:
				setString((String)newValue);
				return;
			case NodePackage.NODE__INTEGER:
				setInteger((Integer)newValue);
				return;
			case NodePackage.NODE__BYTE_ARRAY:
				setByteArray((byte[])newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 *YY17
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case NodePackage.NODE__CHILD:
				getChild().clear();
				return;
			case NodePackage.NODE__STRING:
				setString(DataNode.STRING_EDEFAULT);
				return;
			case NodePackage.NODE__INTEGER:
				setInteger(DataNode.INTEGER_EDEFAULT);
				return;
			case NodePackage.NODE__BYTE_ARRAY:
				setByteArray(DataNode.BYTE_ARRAY_EDEFAULT);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 *YY18
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case NodePackage.NODE__CHILD:
				return getChild() != null && !getChild().isEmpty();
			case NodePackage.NODE__STRING:
				return DataNode.STRING_EDEFAULT == null ? getString() != null : !DataNode.STRING_EDEFAULT.equals(getString());
			case NodePackage.NODE__INTEGER:
				return getInteger() != DataNode.INTEGER_EDEFAULT;
			case NodePackage.NODE__BYTE_ARRAY:
				return DataNode.BYTE_ARRAY_EDEFAULT == null ? getByteArray() != null : !DataNode.BYTE_ARRAY_EDEFAULT.equals(getByteArray());
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 *YY27
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		if (data != null) result.append(data.toString());
		
		return result.toString();
		}




// data Class generation 
protected static  class DataNode {


	/**
	 * The cached value of the '{@link #getChild() <em>Child</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getChild()
	 * @generated
	 * @ordered
	 */
	protected EList<Node> child;

	/**
	 * The default value of the '{@link #getString() <em>String</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getString()
	 * @generated
	 * @ordered
	 */
	protected static final String STRING_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getString() <em>String</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getString()
	 * @generated
	 * @ordered
	 */
	protected String string = STRING_EDEFAULT;

	/**
	 * The default value of the '{@link #getInteger() <em>Integer</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInteger()
	 * @generated
	 * @ordered
	 */
	protected static final int INTEGER_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getInteger() <em>Integer</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInteger()
	 * @generated
	 * @ordered
	 */
	protected int integer = INTEGER_EDEFAULT;

	/**
	 * The default value of the '{@link #getByteArray() <em>Byte Array</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getByteArray()
	 * @generated
	 * @ordered
	 */
	protected static final byte[] BYTE_ARRAY_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getByteArray() <em>Byte Array</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getByteArray()
	 * @generated
	 * @ordered
	 */
	protected byte[] byteArray = BYTE_ARRAY_EDEFAULT;

	/**
	 *Constructor of DataNode
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DataNode() {
		
	}
	
		
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String toString(){	
		StringBuffer result = new StringBuffer(super.toString());		
		result.append(" (string: ");
		result.append(string);
		result.append(", integer: ");
		result.append(integer);
		result.append(", byteArray: ");
		result.append(byteArray);
		result.append(')');
		return result.toString();
	}
		
}//end data class
} //NodeImpl
