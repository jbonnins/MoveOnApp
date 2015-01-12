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

/*
-- _id	FK_id_Cab	Name		FavouriteCab
CREATE TABLE "FavouriteCab" ("_id" INTEGER PRIMARY KEY ASC AUTOINCREMENT NOT NULL UNIQUE, "FK_id_Cab" INTEGER NOT NULL, "Name" TEXT);
*/

public class FavouriteCab
{
	private int _id;
	private int FK_id_Cab;
	private String Name;

	public FavouriteCab()
	{
	}
	/**
	 * @param _id
	 * @param fK_id_Cab
	 * @param name
	 */
	public FavouriteCab(int _id, int fK_id_Cab, String name) {
		this._id = _id;
		FK_id_Cab = fK_id_Cab;
		Name = name;
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
	 * @return the fK_id_Cab
	 */
	public int getFK_id_Cab() {
		return FK_id_Cab;
	}
	/**
	 * @param fK_id_Cab the fK_id_Cab to set
	 */
	public void setFK_id_Cab(int fK_id_Cab) {
		FK_id_Cab = fK_id_Cab;
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
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + FK_id_Cab;
		result = prime * result + ((Name == null) ? 0 : Name.hashCode());
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
		if (!(obj instanceof FavouriteCab)) {
			return false;
		}
		FavouriteCab other = (FavouriteCab) obj;
		if (_id != other._id) {
			return false;
		}
		return true;
	}


}
