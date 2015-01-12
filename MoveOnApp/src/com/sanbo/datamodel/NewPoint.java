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

import com.sanbo.coordinates.GeoPosition;

/*
-- _id	FK_id_AlterMapRoute	Latitude	Longitude	IsStation	NextPoint PreviousPoint	Velocity		NewPoint
CREATE TABLE "NewPoint" ("_id" INTEGER PRIMARY KEY ASC AUTOINCREMENT NOT NULL UNIQUE, "FK_id_AlterMapRoute" INTEGER NOT NULL, "Latitude" INTEGER NOT NULL, "Longitude" INTEGER NOT NULL, "IsStation" BOOLEAN NOT NULL, "NextPoint" INTEGER, "PreviousPoint" INTEGER, "Velocity" INTEGER);
*/

public class NewPoint {
	private int _id;
	private int FK_id_LineStation;
	private GeoPosition geoPosition;
	private int NextPoint;
	private int PreviousPoint;
	private int Velocity;
	
	public NewPoint() {
		
	}

	/**
	 * @param _id
	 * @param fK_id_LineStation
	 * @param geoPosition
	 * @param velocity
	 */
	public NewPoint(int _id, int fK_id_LineStation, GeoPosition geoPosition,
			int velocity) {
		this._id = _id;
		this.FK_id_LineStation = fK_id_LineStation;
		this.geoPosition = geoPosition;
		this.Velocity = velocity;
	}

	/**
	 * @param _id
	 * @param fK_id_LineStation
	 * @param geoPosition
	 * @param nextPoint
	 * @param previousPoint
	 * @param velocity
	 */
	public NewPoint(int _id, int fK_id_LineStation, GeoPosition geoPosition,
			int nextPoint, int previousPoint, int velocity) {
		this._id = _id;
		this.FK_id_LineStation = fK_id_LineStation;
		this.geoPosition = geoPosition;
		this.NextPoint = nextPoint;
		this.PreviousPoint = previousPoint;
		this.Velocity = velocity;
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
	 * @return the fK_id_LineStation
	 */
	public int getFK_id_LineStation() {
		return FK_id_LineStation;
	}

	/**
	 * @param fK_id_LineStation the fK_id_LineStation to set
	 */
	public void setFK_id_LineStation(int fK_id_LineStation) {
		FK_id_LineStation = fK_id_LineStation;
	}

	/**
	 * @return the geolocation
	 */
	public GeoPosition getGeolocation() {
		return geoPosition;
	}

	/**
	 * @param geoPosition the geolocation to set
	 */
	public void setGeolocation(GeoPosition geoPosition) {
		this.geoPosition = geoPosition;
	}

	/**
	 * @return the nextPoint
	 */
	public int getNextPoint() {
		return NextPoint;
	}

	/**
	 * @param nextPoint the nextPoint to set
	 */
	public void setNextPoint(int nextPoint) {
		NextPoint = nextPoint;
	}

	/**
	 * @return the previousPoint
	 */
	public int getPreviousPoint() {
		return PreviousPoint;
	}

	/**
	 * @param previousPoint the previousPoint to set
	 */
	public void setPreviousPoint(int previousPoint) {
		PreviousPoint = previousPoint;
	}

	/**
	 * @return the velocity
	 */
	public int getVelocity() {
		return Velocity;
	}

	/**
	 * @param velocity the velocity to set
	 */
	public void setVelocity(int velocity) {
		Velocity = velocity;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + FK_id_LineStation;
		result = prime * result
				+ ((geoPosition == null) ? 0 : geoPosition.hashCode());
		result = prime * result + NextPoint;
		result = prime * result + PreviousPoint;
		result = prime * result + Velocity;
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
		if (!(obj instanceof NewPoint)) {
			return false;
		}
		NewPoint other = (NewPoint) obj;
		if (_id != other._id) {
			return false;
		}
		return true;
	}
	
	
}
