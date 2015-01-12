/*
 * Copyright 2013 Juan José Bonnín Sansó (jbonnins@uoc.edu)
 *
 * This file is part of MoveOnApp.
 *
 *    MoveOnApp is free software: you can redistribute it and/or modify
 *    it under the terms of the Affero GNU General Public License version 3
 *    as published by the Free Software Foundation.
 *
 *    MoveOnApp is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    Affero GNU General Public License for more details
 *    (https://www.gnu.org/licenses/agpl-3.0.html).
 *    
 *    All the creations are under sanbo package, which is my short surname. 
 */
package com.sanbo.datamodel;

import java.util.ArrayList;

/*
-- _id	Name	Telephone	Web		CabOperator
CREATE TABLE "CabsOperator" ("_id" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL UNIQUE, "Name" TEXT NOT NULL, "Telephone" TEXT, "Web" TEXT)
*/

public class CabOperator {
	private int _id;
	private String Name;
	private String Telephone;
	private String Web;
	private ArrayList<Cab> cabs;
	private boolean isFavorite;

	public CabOperator() {
		
	}
	/**
	 * @param _id
	 * @param name
	 * @param telephon
	 * @param web
	 */
	public CabOperator(int _id, String name, String telephone, String web) {
		this._id = _id;
		Name = name;
		Telephone = telephone;
		Web = web;
	}
	/**
	 * @param _id
	 * @param name
	 * @param telephon
	 * @param web
	 * @param isFavorite
	 */
	public CabOperator(int _id, String name, String telephone, String web, boolean isFavorite) {
		this._id = _id;
		Name = name;
		Telephone = telephone;
		Web = web;
		this.setFavorite(isFavorite);
	}

/**
	 * @return the _id
	 */
	public int get_id() {
		return _id;
	}
	/**
	 * @param _id the _id to set
	 */
	public void set_id(int _id) {
		this._id = _id;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return Name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		Name = name;
	}
	/**
	 * @return the telephone
	 */
	public String getTelephone() {
		return Telephone;
	}
	/**
	 * @param telephon the telephon to set
	 */
	public void setTelephone(String telephone) {
		Telephone = telephone;
	}
	/**
	 * @return the web
	 */
	public String getWeb() {
		return Web;
	}
	/**
	 * @param web the web to set
	 */
	public void setWeb(String web) {
		Web = web;
	}
	public boolean isFavorite() {
		return isFavorite;
	}
	public void setFavorite(boolean isFavorite) {
		this.isFavorite = isFavorite;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Name == null) ? 0 : Name.hashCode());
		result = prime * result
				+ ((Telephone == null) ? 0 : Telephone.hashCode());
		result = prime * result + ((Web == null) ? 0 : Web.hashCode());
		result = prime * result + _id;
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof CabOperator)) {
			return false;
		}
		CabOperator other = (CabOperator) obj;
		if (_id != other._id) {
			return false;
		}
		return true;
	}
	/**
	 * @return the Cab
	 */
	public ArrayList<Cab> getCabs() {
		return cabs;
	}
	/**
	 * @param Cab the Cab to set
	 */
	public void setCabs(ArrayList<Cab> cabs) {
		this.cabs = cabs;
	}
}
