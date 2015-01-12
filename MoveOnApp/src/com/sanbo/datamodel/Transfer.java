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
-- _id	FK_id_Itinerary_Destination	FK_id_Itinerary_Source	FK_id_Station_Destination	FK_id_Station_Source	Distance	Time		Transfer
CREATE TABLE "Transfer" ("_id" INTEGER PRIMARY KEY ASC AUTOINCREMENT NOT NULL UNIQUE, "FK_id_Itinerary_Destination" INTEGER NOT NULL, "FK_id_Itinerary_Source" INTEGER NOT NULL, "FK_id_Station_Destination" INTEGER NOT NULL, "FK_id_Station_Source" INTEGER NOT NULL, "Distance" INTEGER NOT NULL, "Time" DATETIME NOT NULL);
*/

public class Transfer
{
	private int _id;
	private int FK_id_Itinerary_Destination;
	private int FK_id_Itinerary_Source;
	private int FK_id_Station_Destination;
	private int FK_id_Station_Source;
	private int Distance;
	private Calendar Time;
	
	public Transfer() {
		
	}

	/**
	 * @param _id
	 * @param fK_id_Itinerary_Destination
	 * @param fK_id_Itinerary_Source
	 * @param fK_id_Station_Destination
	 * @param fK_id_Station_Source
	 * @param distance
	 * @param time
	 */
	public Transfer(int _id, int fK_id_Itinerary_Destination,
			int fK_id_Itinerary_Source, int fK_id_Station_Destination,
			int fK_id_Station_Source, int distance, Calendar time) {
		this._id = _id;
		FK_id_Itinerary_Destination = fK_id_Itinerary_Destination;
		FK_id_Itinerary_Source = fK_id_Itinerary_Source;
		FK_id_Station_Destination = fK_id_Station_Destination;
		FK_id_Station_Source = fK_id_Station_Source;
		Distance = distance;
		Time = time;
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
	 * @return the fK_id_Itinerary_Destination
	 */
	public int getFK_id_Itinerary_Destination() {
		return FK_id_Itinerary_Destination;
	}

	/**
	 * @param fK_id_Itinerary_Destination the fK_id_Itinerary_Destination to set
	 */
	public void setFK_id_Itinerary_Destination(int fK_id_Itinerary_Destination) {
		FK_id_Itinerary_Destination = fK_id_Itinerary_Destination;
	}

	/**
	 * @return the fK_id_Itinerary_Source
	 */
	public int getFK_id_Itinerary_Source() {
		return FK_id_Itinerary_Source;
	}

	/**
	 * @param fK_id_Itinerary_Source the fK_id_Itinerary_Source to set
	 */
	public void setFK_id_Itinerary_Source(int fK_id_Itinerary_Source) {
		FK_id_Itinerary_Source = fK_id_Itinerary_Source;
	}

	/**
	 * @return the fK_id_Station_Destination
	 */
	public int getFK_id_Station_Destination() {
		return FK_id_Station_Destination;
	}

	/**
	 * @param fK_id_Station_Destination the fK_id_Station_Destination to set
	 */
	public void setFK_id_Station_Destination(int fK_id_Station_Destination) {
		FK_id_Station_Destination = fK_id_Station_Destination;
	}

	/**
	 * @return the fK_id_Station_Source
	 */
	public int getFK_id_Station_Source() {
		return FK_id_Station_Source;
	}

	/**
	 * @param fK_id_Station_Source the fK_id_Station_Source to set
	 */
	public void setFK_id_Station_Source(int fK_id_Station_Source) {
		FK_id_Station_Source = fK_id_Station_Source;
	}

	/**
	 * @return the distance
	 */
	public int getDistance() {
		return Distance;
	}

	/**
	 * @param distance the distance to set
	 */
	public void setDistance(int distance) {
		Distance = distance;
	}

	/**
	 * @return the time
	 */
	public Calendar getTime() {
		return Time;
	}

	/**
	 * @param time the time to set
	 */
	public void setTime(Calendar time) {
		Time = time;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Distance;
		result = prime * result + FK_id_Itinerary_Destination;
		result = prime * result + FK_id_Itinerary_Source;
		result = prime * result + FK_id_Station_Destination;
		result = prime * result + FK_id_Station_Source;
		result = prime * result + ((Time == null) ? 0 : Time.hashCode());
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
		if (!(obj instanceof Transfer)) {
			return false;
		}
		Transfer other = (Transfer) obj;
		if (_id != other._id) {
			return false;
		}
		return true;
	}

}