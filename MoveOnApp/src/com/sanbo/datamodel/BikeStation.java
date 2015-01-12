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
-- _id	FK_id_Station	Capacity BikesAvailable ParksFree		BikeStation
CREATE TABLE "BikeStation" ("_id" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL UNIQUE, "FK_id_Station" INTEGER NOT NULL, "Capacity" INTEGER NOT NULL, "BikesAvailable" INTEGER NOT NULL, "ParksFree" INTEGER NOT NULL)
*/

public class BikeStation {
	private int _id;
	private int FK_id_Station;
	private int Capacity;
	private int BikesAvailable;
	private int ParksFree;
	// temporary StationCode
	private String Code;

	public BikeStation() {
		
	}

	/**
	 * @param _id
	 * @param fK_id_Station
	 * @param capacity
	 * @param bikesAvailable
	 * @param parksFree
	 */
	public BikeStation(int _id, int fK_id_Station, int capacity,
			int bikesAvailable, int parksFree) {
		this._id = _id;
		FK_id_Station = fK_id_Station;
		Capacity = capacity;
		BikesAvailable = bikesAvailable;
		ParksFree = parksFree;
	}
	
	// To construct a bikeStation from CityBik Api
	public BikeStation(String code, int bikesAvailable, int parksFree) {
		// this id is temporary
		_id = -1;
		FK_id_Station = -1;
		Capacity = bikesAvailable + parksFree;
		BikesAvailable = bikesAvailable;
		ParksFree = parksFree;
		Code = code;
	}
	

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + BikesAvailable;
		result = prime * result + Capacity;
		result = prime * result + FK_id_Station;
		result = prime * result + ParksFree;
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
		if (!(obj instanceof BikeStation)) {
			return false;
		}
		BikeStation other = (BikeStation) obj;
		if (_id != other._id) {
			return false;
		}
		return true;
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
	 * @return the bikesAvailable
	 */
	public int getBikesAvailable() {
		return BikesAvailable;
	}

	/**
	 * @param bikesAvailable the bikesAvailable to set
	 */
	public void setBikesAvailable(int bikesAvailable) {
		BikesAvailable = bikesAvailable;
	}

	/**
	 * @return the parksFree
	 */
	public int getParksFree() {
		return ParksFree;
	}

	/**
	 * @param parksFree the parksFree to set
	 */
	public void setParksFree(int parksFree) {
		ParksFree = parksFree;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return Code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		Code = code;
	}
	
	

}
