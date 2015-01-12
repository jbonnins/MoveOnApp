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
-- _id	FK_id_Station	BusStation	InformationDesk	Metro	Parking	Taxi	Train		StationInfo
CREATE TABLE "StationInfo" ("_id" INTEGER PRIMARY KEY ASC AUTOINCREMENT NOT NULL UNIQUE, "FK_id_Station" INTEGER NOT NULL, "BusStation" BOOLEAN NOT NULL, "InformationDesk" BOOLEAN NOT NULL, "Metro" BOOLEAN NOT NULL, "Parking" BOOLEAN NOT NULL, "Taxi" BOOLEAN NOT NULL, "Train" BOOLEAN NOT NULL);
*/

public class StationInfo
{
private int _id;
private int FK_id_Station;
private boolean BusStation;
private boolean InformationDesk;
private boolean Metro;
private boolean Parking;
private boolean Taxi;
private boolean Train;

/**
 * constructor StationInfo
 */
public StationInfo()
  {
  }

  /**
 * @param mID
 * @param mFK_id_Station
 * @param mBusStation
 * @param mInformationDesk
 * @param mMetro
 * @param mParking
 * @param mTaxi
 * @param mTrain
 */
public StationInfo(int mID, int mFK_id_Station, boolean mBusStation, boolean mInformationDesk, boolean mMetro, boolean mParking, boolean mTaxi, boolean mTrain)
  {
    this._id = mID;
    this.FK_id_Station = mFK_id_Station;
    this.BusStation = mBusStation;
    this.InformationDesk = mInformationDesk;
    this.Metro = mMetro;
    this.Parking = mParking;
    this.Taxi = mTaxi;
    this.Train = mTrain;
  }

	/**
	 * @return
	 */
	public int get_id() {
		return _id;
	}
	
	/**
	 * @param _id
	 */
	public void set_id(int _id) {
		this._id = _id;
	}
	
	/**
	 * @return
	 */
	public int getFK_id_Station() {
		return FK_id_Station;
	}
	
	/**
	 * @param fK_id_Station
	 */
	public void setFK_id_Station(int fK_id_Station) {
		FK_id_Station = fK_id_Station;
	}
	
	/**
	 * @return
	 */
	public boolean haveBusStation() {
		return BusStation;
	}
	
	/**
	 * @param busStation
	 */
	public void setBusStation(boolean busStation) {
		BusStation = busStation;
	}
	
	/**
	 * @return
	 */
	public boolean haveInformationDesk() {
		return InformationDesk;
	}
	
	/**
	 * @param informationDesk
	 */
	public void setInformationDesk(boolean informationDesk) {
		InformationDesk = informationDesk;
	}
	
	/**
	 * @return
	 */
	public boolean haveMetro() {
		return Metro;
	}
	
	/**
	 * @param metro
	 */
	public void setMetro(boolean metro) {
		Metro = metro;
	}
	
	/**
	 * @return
	 */
	public boolean haveParking() {
		return Parking;
	}
	
	/**
	 * @param parking
	 */
	public void setParking(boolean parking) {
		Parking = parking;
	}
	
	/**
	 * @return
	 */
	public boolean haveTaxi() {
		return Taxi;
	}
	
	/**
	 * @param taxi
	 */
	public void setTaxi(boolean taxi) {
		Taxi = taxi;
	}
	
	/**
	 * @return
	 */
	public boolean haveTrain() {
		return Train;
	}
	
	/**
	 * @param train
	 */
	public void setTrain(boolean train) {
		Train = train;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (BusStation ? 1231 : 1237);
		result = prime * result + FK_id_Station;
		result = prime * result + (InformationDesk ? 1231 : 1237);
		result = prime * result + (Metro ? 1231 : 1237);
		result = prime * result + (Parking ? 1231 : 1237);
		result = prime * result + (Taxi ? 1231 : 1237);
		result = prime * result + (Train ? 1231 : 1237);
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
		if (!(obj instanceof StationInfo)) {
			return false;
		}
		StationInfo other = (StationInfo) obj;
		if (_id != other._id) {
			return false;
		}
		return true;
	}
}