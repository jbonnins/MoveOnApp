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
import java.util.List;
/*
-- _id	FK_id_Line	FK_id_Station	StationNext	StationPrevious	RouteOneWay	RouteReturn	TimeBetweenStations	MetersBetweenStations	OneDestination	TimeAdded	MetersAdded		LineStation
CREATE TABLE "LineStation" ("_id" INTEGER PRIMARY KEY ASC AUTOINCREMENT NOT NULL UNIQUE, "FK_id_Line" INTEGER NOT NULL, "FK_id_Station" INTEGER NOT NULL, "StationNext" INTEGER, "StationPrevious" INTEGER, "RouteOneWay" BOOLEAN NOT NULL, "RouteReturn" BOOLEAN NOT NULL, "TimeBetweenStations" DATETIME NOT NULL, "MetersBetweenStations" INTEGER NOT NULL, "OneDestination" BOOLEAN NOT NULL, "TimeAdded" DATETIME NOT NULL, "MetersAdded" INTEGER NOT NULL);
*/

public class LineStation {
	private int _id;
	private int FK_id_Line;
	private int FK_id_Station;
	private int StationNext;
	private int StationPrevious;
	private boolean RouteOneWay;
	private boolean RouteReturn;
	private Calendar TimeBetweenStations;
	private Long MetersBetweenStations;
	private boolean OneDestination;
	private Calendar TimeAdded;
	private Long MetersAdded;
		
	private List<MapPoint> mapPoints;
	
	public LineStation() {
		
	}
	/**
	 * @param _id
	 * @param fK_id_Line
	 * @param fK_id_Station
	 * @param stationNext
	 * @param stationPrevious
	 * @param routeOneWay
	 * @param routeReturn
	 * @param timeBetweenStations
	 * @param metersBetweenStations
	 * @param oneDestination
	 */
	public LineStation(int _id, int fK_id_Line, int fK_id_Station,
			boolean routeOneWay, boolean routeReturn, 
			Calendar timeBetweenStations, Long metersBetweenStations, 
			boolean oneDestination) {
		this._id = _id;
		FK_id_Line = fK_id_Line;
		FK_id_Station = fK_id_Station;
		RouteOneWay = routeOneWay;
		RouteReturn = routeReturn;
		TimeBetweenStations = timeBetweenStations;
		MetersBetweenStations = metersBetweenStations;
		OneDestination = oneDestination;		
	}
	
	/**
	 * @param _id
	 * @param fK_id_Line
	 * @param fK_id_Station
	 * @param stationNext
	 * @param stationPrevious
	 * @param routeOneWay
	 * @param routeReturn
	 * @param timeBetweenStations
	 * @param metersBetweenStations
	 * @param oneDestination
	 */
	public LineStation(int _id, int fK_id_Line, int fK_id_Station,
			int stationNext, int stationPrevious, boolean routeOneWay,
			boolean routeReturn, Calendar timeBetweenStations,
			Long metersBetweenStations, boolean oneDestination) {
		this._id = _id;
		FK_id_Line = fK_id_Line;
		FK_id_Station = fK_id_Station;
		StationNext = stationNext;
		StationPrevious = stationPrevious;
		RouteOneWay = routeOneWay;
		RouteReturn = routeReturn;
		TimeBetweenStations = timeBetweenStations;
		MetersBetweenStations = metersBetweenStations;
		OneDestination = oneDestination;		
	}
	
	/**
	 * @param _id
	 * @param fK_id_Line
	 * @param fK_id_Station
	 * @param stationNext
	 * @param stationPrevious
	 * @param routeOneWay
	 * @param routeReturn
	 * @param timeBetweenStations
	 * @param metersBetweenStations
	 * @param oneDestination
	 * @param TimeAdded
	 * @param metersAdded
	 */
	public LineStation(int _id, int fK_id_Line, int fK_id_Station,
			int stationNext, int stationPrevious, boolean routeOneWay,
			boolean routeReturn, Calendar timeBetweenStations,
			Long metersBetweenStations, boolean oneDestination,
			Calendar timeAdded,Long metersAdded) {
		this._id = _id;
		FK_id_Line = fK_id_Line;
		FK_id_Station = fK_id_Station;
		StationNext = stationNext;
		StationPrevious = stationPrevious;
		RouteOneWay = routeOneWay;
		RouteReturn = routeReturn;
		TimeBetweenStations = timeBetweenStations;
		MetersBetweenStations = metersBetweenStations;
		OneDestination = oneDestination;
		TimeAdded = timeAdded;
		MetersAdded = metersAdded;
		
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
	 * @return the fK_id_Line
	 */
	public int getFK_id_Line() {
		return FK_id_Line;
	}
	/**
	 * @param fK_id_Line the fK_id_Line to set
	 */
	public void setFK_id_Line(int fK_id_Line) {
		FK_id_Line = fK_id_Line;
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
	 * @return the stationNext
	 */
	public int getStationNext() {
		return StationNext;
	}
	/**
	 * @param stationNext the stationNext to set
	 */
	public void setStationNext(int stationNext) {
		StationNext = stationNext;
	}
	/**
	 * @return the stationPrevious
	 */
	public int getStationPrevious() {
		return StationPrevious;
	}
	/**
	 * @param stationPrevious the stationPrevious to set
	 */
	public void setStationPrevious(int stationPrevious) {
		StationPrevious = stationPrevious;
	}
	/**
	 * @return the routeOneWay
	 */
	public boolean getRouteOneWay() {
		return RouteOneWay;
	}
	/**
	 * @param routeOneWay the routeOneWay to set
	 */
	public void setRouteOneWay(boolean routeOneWay) {
		RouteOneWay = routeOneWay;
	}
	/**
	 * @return the routeReturn
	 */
	public boolean getRouteReturn() {
		return RouteReturn;
	}
	/**
	 * @param routeReturn the routeReturn to set
	 */
	public void setRouteReturn(boolean routeReturn) {
		RouteReturn = routeReturn;
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
	 * @return the oneDestination
	 */
	public boolean isOneDestination() {
		return OneDestination;
	}
	/**
	 * @param oneDestination the oneDestination to set
	 */
	public void setOneDestination(boolean oneDestination) {
		OneDestination = oneDestination;
	}
	public Calendar getTimeAdded() {
		return TimeAdded;
	}
	public void setTimeAdded(Calendar timeAdded) {
		TimeAdded = timeAdded;
	}
	public Long getMetersAdded() {
		return MetersAdded;
	}
	public void setMetersAdded(Long metersAdded) {
		MetersAdded = metersAdded;
	}
	/**
	 * @return the mapPoints
	 */
	public List<MapPoint> getMapPoints() {
		return mapPoints;
	}
	/**
	 * @param mapPoint the mapPoints to set
	 */
	public void setMapPoint(List<MapPoint> mapPoints) {
		this.mapPoints = mapPoints;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + FK_id_Line;
		result = prime * result + FK_id_Station;
		result = prime * result
				+ ((MetersAdded == null) ? 0 : MetersAdded.hashCode());
		result = prime
				* result
				+ ((MetersBetweenStations == null) ? 0 : MetersBetweenStations
						.hashCode());
		result = prime * result + (OneDestination ? 1231 : 1237);
		result = prime * result + (RouteOneWay ? 1231 : 1237);
		result = prime * result + (RouteReturn ? 1231 : 1237);
		result = prime * result + StationNext;
		result = prime * result + StationPrevious;
		result = prime * result
				+ ((TimeAdded == null) ? 0 : TimeAdded.hashCode());
		result = prime
				* result
				+ ((TimeBetweenStations == null) ? 0 : TimeBetweenStations
						.hashCode());
		result = prime * result + _id;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LineStation other = (LineStation) obj;
		if (_id != other._id)
			return false;
		return true;
	}

}
