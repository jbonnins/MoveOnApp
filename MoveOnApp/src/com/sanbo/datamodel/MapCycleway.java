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
-- _id	FK_id_Cycleway	Latitude Longitude	NextPoint	PreviousPoint		MapCycleway
CREATE TABLE "MapCycleway" ("_id" INTEGER PRIMARY KEY ASC AUTOINCREMENT NOT NULL UNIQUE, "FK_id_Cycleway" INTEGER NOT NULL, "Latitude" INTEGER NOT NULL, "Longitude" INTEGER NOT NULL, "NextPoint" INTEGER, "PreviousPoint" INTEGER);
*/

public class MapCycleway {
	private int _id;
	private int FK_id_Cycleway;
	private GeoPosition Coordenates;
	private int NextPoint;
	private int PreviousPoint;
	
	public MapCycleway() {
		
	}
	/**
	 * @param _id
	 * @param fK_id_Cycleway
	 * @param geolocation
	 */
	public MapCycleway(int _id, int fK_id_Cycleway, GeoPosition coordenates) {
		this._id = _id;
		FK_id_Cycleway = fK_id_Cycleway;
		Coordenates = coordenates;
	}

	/**
	 * @param _id
	 * @param fK_id_Cycleway
	 * @param geolocation
	 * @param nextPoint
	 * @param previousPoint
	 */
	public MapCycleway(int _id, int fK_id_Cycleway, GeoPosition coordenates,
			int nextPoint, int previousPoint) {
		this._id = _id;
		FK_id_Cycleway = fK_id_Cycleway;
		Coordenates = coordenates;
		NextPoint = nextPoint;
		PreviousPoint = previousPoint;
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
	 * @return the fK_id_Cycleway
	 */
	public int getFK_id_Cycleway() {
		return FK_id_Cycleway;
	}
	/**
	 * @param fK_id_Cycleway the fK_id_Cycleway to set
	 */
	public void setFK_id_Cycleway(int fK_id_Cycleway) {
		FK_id_Cycleway = fK_id_Cycleway;
	}
	/**
	 * @return the coordenates
	 */
	public GeoPosition getCoordenates() {
		return Coordenates;
	}
	/**
	 * @param geolocation the coordenates to set
	 */
	public void setCoordenatos(GeoPosition coordenates) {
		Coordenates = coordenates;
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
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + FK_id_Cycleway;
		result = prime * result
				+ ((Coordenates == null) ? 0 : Coordenates.hashCode());
		result = prime * result + NextPoint;
		result = prime * result + PreviousPoint;
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
		if (!(obj instanceof MapCycleway)) {
			return false;
		}
		MapCycleway other = (MapCycleway) obj;
		if (_id != other._id) {
			return false;
		}
		return true;
	}

}
