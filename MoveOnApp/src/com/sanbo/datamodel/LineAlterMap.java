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
-- _id	FK_id_Line	FK_id_AlterMapRoute		LineAlterMap
CREATE TABLE "LineAlterMap" ("_id" INTEGER PRIMARY KEY ASC AUTOINCREMENT NOT NULL UNIQUE, "FK_id_Line" INTEGER NOT NULL, "FK_id_AlterMapRoute" INTEGER NOT NULL)
*/

public class LineAlterMap {
	private int _id;
	private int FK_id_Line;
	private int FK_id_AlterMapRoute;
	/**
	 * @return the _id
	 */
	public int get_id() {
		return _id;
	}
	/**
	 * @param _id
	 * @param fK_id_Line
	 * @param fK_id_AlterMapRoute
	 */
	public LineAlterMap(int _id, int fK_id_Line, int fK_id_AlterMapRoute) {
		this._id = _id;
		FK_id_Line = fK_id_Line;
		FK_id_AlterMapRoute = fK_id_AlterMapRoute;
	}
	/**
	 * @param _id the _id to set
	 */
	public void set_id(int _id) {
		this._id = _id;
	}
	/**
	 * @return the fK_id_Line
	 */
	public int getFK_id_Line() {
		return FK_id_Line;
	}
	/**
	 * @param fK_id_Line the fK_id_Line to set
	 */
	public void setFK_id_Line(int fK_id_Line) {
		FK_id_Line = fK_id_Line;
	}
	/**
	 * @return the fK_id_AlterMapRoute
	 */
	public int getFK_id_AlterMapRoute() {
		return FK_id_AlterMapRoute;
	}
	/**
	 * @param fK_id_AlterMapRoute the fK_id_AlterMapRoute to set
	 */
	public void setFK_id_AlterMapRoute(int fK_id_AlterMapRoute) {
		FK_id_AlterMapRoute = fK_id_AlterMapRoute;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + FK_id_AlterMapRoute;
		result = prime * result + FK_id_Line;
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
		if (!(obj instanceof LineAlterMap)) {
			return false;
		}
		LineAlterMap other = (LineAlterMap) obj;
		if (_id != other._id) {
			return false;
		}
		return true;
	}
}
