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
-- _id	FK_id_TransportOnDemand	TimeEnd	TimeIni		TimetableOnDemand
CREATE TABLE "TimetableOnDemand" ("_id" INTEGER PRIMARY KEY ASC AUTOINCREMENT NOT NULL UNIQUE, "FK_id_TransportOnDemand" INTEGER NOT NULL, "TimeEnd" DATETIME, "TimeIni" DATETIME);
*/

public class TimeTableOnDemand {
	private int _id;
	private int FK_id_TransportOnDemand;
	private Calendar TimeEnd;
	private Calendar TimeIni;
	
	public TimeTableOnDemand() {
		
	}

	/**
	 * @param _id
	 * @param fK_id_TransportOnDemand
	 * @param timeEnd
	 * @param timeIni
	 */
	public TimeTableOnDemand(int _id,
			int fK_id_TransportOnDemand, Calendar timeEnd,
			Calendar timeIni) {
		this._id = _id;
		FK_id_TransportOnDemand = fK_id_TransportOnDemand;
		TimeEnd = timeEnd;
		TimeIni = timeIni;
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
	 * @return the fK_id_TransportOnDemandStation
	 */
	public int getFK_id_TransportOnDemand() {
		return FK_id_TransportOnDemand;
	}

	/**
	 * @param fK_id_TransportOnDemand the fK_id_TransportOnDemand to set
	 */
	public void setFK_id_TransportOnDemand(int fK_id_TransportOnDemand) {
		FK_id_TransportOnDemand = fK_id_TransportOnDemand;
	}

	/**
	 * @return the timeEnd
	 */
	public Calendar getTimeEnd() {
		return TimeEnd;
	}

	/**
	 * @param timeEnd the timeEnd to set
	 */
	public void setTimeEnd(Calendar timeEnd) {
		TimeEnd = timeEnd;
	}

	/**
	 * @return the timeIni
	 */
	public Calendar getTimeIni() {
		return TimeIni;
	}

	/**
	 * @param timeIni the timeIni to set
	 */
	public void setTimeIni(Calendar timeIni) {
		TimeIni = timeIni;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + FK_id_TransportOnDemand;
		result = prime * result + ((TimeEnd == null) ? 0 : TimeEnd.hashCode());
		result = prime * result + ((TimeIni == null) ? 0 : TimeIni.hashCode());
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
		if (!(obj instanceof TimeTableOnDemand)) {
			return false;
		}
		TimeTableOnDemand other = (TimeTableOnDemand) obj;
		if (_id != other._id) {
			return false;
		}
		return true;
	}

}
