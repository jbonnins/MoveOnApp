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

import java.util.Calendar;
/*
-- _id	Date	FK_id_Transport		Holiday
CREATE TABLE "Holiday" ("_id" INTEGER PRIMARY KEY ASC AUTOINCREMENT NOT NULL UNIQUE, "FK_id_Transport" INTEGER NOT NULL, "Date" DATETIME NOT NULL);
*/

public class Holiday {
	private int _id;
	private int FK_id_Transport;
	private Calendar DateHoliday;
	/**
	 * @param _id
	 * @param fK_id_Transport
	 * @param dateHoliday
	 */
	public Holiday(int _id, int fK_id_Transport, Calendar dateHoliday) {
		this._id = _id;
		FK_id_Transport = fK_id_Transport;
		DateHoliday = dateHoliday;
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
	 * @return the fK_id_Transport
	 */
	public int getFK_id_Transport() {
		return FK_id_Transport;
	}
	/**
	 * @param fK_id_Transport the fK_id_Transport to set
	 */
	public void setFK_id_Transport(int fK_id_Transport) {
		FK_id_Transport = fK_id_Transport;
	}
	/**
	 * @return the dateHoliday
	 */
	public Calendar getDateHoliday() {
		return DateHoliday;
	}
	/**
	 * @param dateHoliday the dateHoliday to set
	 */
	public void setDateHoliday(Calendar dateHoliday) {
		DateHoliday = dateHoliday;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((DateHoliday == null) ? 0 : DateHoliday.hashCode());
		result = prime * result + FK_id_Transport;
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
		if (!(obj instanceof Holiday)) {
			return false;
		}
		Holiday other = (Holiday) obj;
		if (_id != other._id) {
			return false;
		}
		return true;
	}
	

}
