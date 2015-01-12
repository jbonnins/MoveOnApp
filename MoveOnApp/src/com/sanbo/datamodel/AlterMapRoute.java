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
import java.util.ArrayList;

/*
-- _id	FK_id_Station_Destiny	FK_id_Station_Source	EndTime	StartTime	TimeBetweenStations MetersBetweenStations		AlterMapRoute
CREATE TABLE "AlterMapRoute" ("_id" INTEGER PRIMARY KEY ASC AUTOINCREMENT NOT NULL UNIQUE, "FK_id_Station_Destiny" INTEGER NOT NULL, "FK_id_Station_Source" INTEGER NOT NULL, "EndTime" TEXT, "StartTime" TEXT NOT NULL, "TimeBetweenStations" TEXT NOT NULL, MetersBetweenStations INTEGER NOT NULL)
*/

public class AlterMapRoute {
	private int _id;
	private int FK_id_Station_Destiny;
	private int FK_id_Station_Source;
	private Calendar EndTime;
	private Calendar StartTime;
	private Calendar TimeBetweenStations;
	private Long MetersBetweenStations;
	private ArrayList<NewPoint> newPoints = null;
	private ArrayList<LineAlterMap> linesAlterMap = null;
	
	public AlterMapRoute() {
		
	}
	/**
	 * @param _id
	 * @param fK_id_Station_Destiny
	 * @param fK_id_Station_Source
	 * @param endTime
	 * @param startTime
	 * @param timeBetweenStations
	 * @param metersBetweenStations
	 */
	public AlterMapRoute(int _id, int fK_id_Station_Destiny,
			int fK_id_Station_Source, Calendar endTime, Calendar startTime,
			Calendar timeBetweenStations, Long metersBetweenStations) {
		this._id = _id;
		FK_id_Station_Destiny = fK_id_Station_Destiny;
		FK_id_Station_Source = fK_id_Station_Source;
		EndTime = endTime;
		StartTime = startTime;
		TimeBetweenStations = timeBetweenStations;
		MetersBetweenStations = metersBetweenStations;
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
	 * @return the fK_id_Station_Destiny
	 */
	public int getFK_id_Station_Destiny() {
		return FK_id_Station_Destiny;
	}
	/**
	 * @param fK_id_Station_Destiny the fK_id_Station_Destiny to set
	 */
	public void setFK_id_Station_Destiny(int fK_id_Station_Destiny) {
		FK_id_Station_Destiny = fK_id_Station_Destiny;
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
	 * @return the endTime
	 */
	public Calendar getEndTime() {
		return EndTime;
	}
	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(Calendar endTime) {
		EndTime = endTime;
	}
	/**
	 * @return the startTime
	 */
	public Calendar getStartTime() {
		return StartTime;
	}
	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(Calendar startTime) {
		StartTime = startTime;
	}
	/**
	 * @return the timeBetweenStations
	 */
	public Calendar getTimeBetweenStations() {
		return TimeBetweenStations;
	}
	/**
	 * @param timeBetweenStations the timeBetweenStations to set
	 */
	public void setTimeBetweenStations(Calendar timeBetweenStations) {
		TimeBetweenStations = timeBetweenStations;
	}
	/**
	 * @return the metersBetweenStations
	 */
	public Long getMetersBetweenStations() {
		return MetersBetweenStations;
	}
	/**
	 * @param metersBetweenStations the metersBetweenStations to set
	 */
	public void setMetersBetweenStations(Long metersBetweenStations) {
		MetersBetweenStations = metersBetweenStations;
	}
	/**
	 * @return the newPoints
	 */
	public ArrayList<NewPoint> getNewPoints() {
		return newPoints;
	}
	/**
	 * @param newPoint the newPoints to set
	 */
	public void setNewPoints(ArrayList<NewPoint> newPoint) {
		this.newPoints = newPoint;
	}
	/**
	 * @return the lineAlterMap
	 */
	public ArrayList<LineAlterMap> getLinesAlterMap() {
		return linesAlterMap;
	}
	/**
	 * @param lineAlterMap the lineAlterMap to set
	 */
	public void setLinesAlterMap(ArrayList<LineAlterMap> linesAlterMap) {
		this.linesAlterMap = linesAlterMap;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((EndTime == null) ? 0 : EndTime.hashCode());
		result = prime * result + FK_id_Station_Destiny;
		result = prime * result + FK_id_Station_Source;
		result = prime
				* result
				+ ((MetersBetweenStations == null) ? 0 : MetersBetweenStations
						.hashCode());
		result = prime * result
				+ ((StartTime == null) ? 0 : StartTime.hashCode());
		result = prime
				* result
				+ ((TimeBetweenStations == null) ? 0 : TimeBetweenStations
						.hashCode());
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
		if (!(obj instanceof AlterMapRoute)) {
			return false;
		}
		AlterMapRoute other = (AlterMapRoute) obj;
		if (_id != other._id) {
			return false;
		}
		return true;
	}


}
