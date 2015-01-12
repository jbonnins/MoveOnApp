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
-- _id	FK_id_Station	Capacity	OnDemandAvailable		TransportOnDemand
CREATE TABLE "TransportOnDemand" ("_id" INTEGER PRIMARY KEY ASC AUTOINCREMENT NOT NULL UNIQUE, "FK_id_Station" INTEGER NOT NULL, "Capacity" INTEGER, "OnDemandAvailable" INTEGER);
*/

public class TransportOnDemand {
	private int _id;
	private int FK_id_Station;
	private int Capacity;
	private int OnDemandAvailable;
	
	public TransportOnDemand() {
		
	}

	/**
	 * @param _id
	 * @param fK_id_Station
	 * @param capacity
	 * @param onDemandAvailable
	 */
	public TransportOnDemand(int _id, int fK_id_Station, int capacity, int onDemandAvailable) {
		this._id = _id;
		FK_id_Station = fK_id_Station;
		Capacity = capacity;
		OnDemandAvailable = onDemandAvailable;
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
	 * @return the fK_id_Station
	 */
	public int getFK_id_Station() {
		return FK_id_Station;
	}

	/**
	 * @param fK_id_Station the fK_id_Station to set
	 */
	public void setFK_id_Station(int fK_id_Station) {
		FK_id_Station = fK_id_Station;
	}

	/**
	 * @return the capacity
	 */
	public int getCapacity() {
		return Capacity;
	}

	/**
	 * @param capacity the capacity to set
	 */
	public void setCapacity(int capacity) {
		Capacity = capacity;
	}

	/**
	 * @return the onDemandAvailable
	 */
	public int getOnDemandAvailable() {
		return OnDemandAvailable;
	}

	/**
	 * @param onDemandAvailable the onDemandAvailable to set
	 */
	public void setOnDemandAvailable(int onDemandAvailable) {
		OnDemandAvailable = onDemandAvailable;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Capacity;
		result = prime * result + FK_id_Station;
		result = prime * result + OnDemandAvailable;
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
		if (!(obj instanceof TransportOnDemand)) {
			return false;
		}
		TransportOnDemand other = (TransportOnDemand) obj;
		if (_id != other._id) {
			return false;
		}
		return true;
	}
	
	
}
