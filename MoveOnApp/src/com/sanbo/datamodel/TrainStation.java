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
-- _id	FK_id_Station	FK_id_Itinerary	FirstTrain	SecondTrain	Destination		TrainStation	
CREATE TABLE "TrainStation" ("_id" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL UNIQUE, "FK_id_Station" INTEGER NOT NULL, "FK_id_Itinerary" INTEGER NOT NULL, "Destination" TEXT NOT NULL, "FirstTrain" DATETIME, "SecondTrain" DATETIME);
*/

public class TrainStation {
	private int _id;
	private int FK_id_Station;
	private int FK_id_Itinerary;
	private String Destination;
	private Calendar FirstTrain;
	private Calendar Secondtrain;

	public TrainStation() {
		
	}

	/**
	 * @param _id
	 * @param fK_id_Station
	 * @param fK_id_Itinerary
	 * @param destination
	 * @param firstTrain
	 * @param secondtrain
	 */
	public TrainStation(int _id, int fK_id_Station, int fK_id_Itinerary,
			String destination, Calendar firstTrain, Calendar secondtrain) {
		this._id = _id;
		FK_id_Station = fK_id_Station;
		FK_id_Itinerary = fK_id_Itinerary;
		Destination = destination;
		FirstTrain = firstTrain;
		Secondtrain = secondtrain;
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
	 * @return the fK_id_Itinerary
	 */
	public int getFK_id_Itinerary() {
		return FK_id_Itinerary;
	}

	/**
	 * @param fK_id_Itinerary the fK_id_Itinerary to set
	 */
	public void setFK_id_Itinerary(int fK_id_Itinerary) {
		FK_id_Itinerary = fK_id_Itinerary;
	}

	/**
	 * @return the destination
	 */
	public String getDestination() {
		return Destination;
	}

	/**
	 * @param destination the destination to set
	 */
	public void setDestination(String destination) {
		Destination = destination;
	}

	/**
	 * @return the firstTrain
	 */
	public Calendar getFirstTrain() {
		return FirstTrain;
	}

	/**
	 * @param firstTrain the firstTrain to set
	 */
	public void setFirstTrain(Calendar firstTrain) {
		FirstTrain = firstTrain;
	}

	/**
	 * @return the secondtrain
	 */
	public Calendar getSecondtrain() {
		return Secondtrain;
	}

	/**
	 * @param secondtrain the secondtrain to set
	 */
	public void setSecondtrain(Calendar secondtrain) {
		Secondtrain = secondtrain;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((Destination == null) ? 0 : Destination.hashCode());
		result = prime * result + FK_id_Itinerary;
		result = prime * result + FK_id_Station;
		result = prime * result
				+ ((FirstTrain == null) ? 0 : FirstTrain.hashCode());
		result = prime * result
				+ ((Secondtrain == null) ? 0 : Secondtrain.hashCode());
		result = prime * result + _id;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TrainStation other = (TrainStation) obj;
		if (_id != other._id)
			return false;
		return true;
	}



}
