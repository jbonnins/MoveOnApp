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

package com.sanbo.utils;

import java.util.ArrayList;

import com.google.android.gms.maps.model.LatLng;
import com.sanbo.datamodel.Station;

public class NetworkInformation {
	// We have only one instance of NetworkInformation
	private static NetworkInformation INSTANCE = null;
	
	// Data from EMT
	private Station emtStationNetwork;
	// Data from BiciPalma
	private ArrayList<Station> bikeStationNetwork;
	// the center of our map
	private LatLng center;
	// init counter last update 
	private LatLng myLocation;
	// init counter last update 
	private long lastUpdateTimeCityBik;
	// init counter last update 
	private long lastUpdateTimeEMT;
	// init counter last update 
	private long lastUpdateLocation;
		
	private NetworkInformation () {
		// Palma center from CityBik
		center = new LatLng(39.5746890, 2.6513320);
		lastUpdateTimeCityBik = 0;
		lastUpdateTimeEMT = 0;
		lastUpdateLocation = 0;
		bikeStationNetwork	= null;
		emtStationNetwork	= null;

	}
	
	public static synchronized NetworkInformation getInstance () {
		if (INSTANCE == null) {
			INSTANCE = new NetworkInformation();
		}
		return INSTANCE;
	}

	/**
	 * @return the bikeStationNetwork
	 */
	public ArrayList<Station> getBikeStationNetwork() {
		return bikeStationNetwork;
	}

	/**
	 * @param bikeStationNetwork the bikeStationNetwork to set
	 */
	public void setBikeStationNetwork(ArrayList<Station> bikeStationNetwork) {
		this.bikeStationNetwork = bikeStationNetwork;
	}

	/**
	 * @return the center
	 */
	public LatLng getCenter() {
		return center;
	}

	/**
	 * @param center the center to set
	 */
	public void setCenter(LatLng center) {
		this.center = center;
	}

	/**
	 * @return the myLocation
	 */
	public LatLng getMyLocation() {
		return myLocation;
	}

	/**
	 * @param myLocation the myLocation to set
	 */
	public void setMyLocation(LatLng myLocation) {
		this.myLocation = myLocation;
	}

	/**
	 * @return the lastUpdateTimeCityBik
	 */
	public long getLastUpdateTimeCityBik() {
		return lastUpdateTimeCityBik;
	}

	/**
	 * @param lastUpdateTimeCityBik the lastUpdateTimeCityBik to set
	 */
	public void setLastUpdateTimeCityBik(long lastUpdateTimeCityBik) {
		this.lastUpdateTimeCityBik = lastUpdateTimeCityBik;
	}

	/**
	 * @return the lastUpdateTimeEMT
	 */
	public long getLastUpdateTimeEMT() {
		return lastUpdateTimeEMT;
	}

	/**
	 * @param lastUpdateTimeEMT the lastUpdateTimeEMT to set
	 */
	public void setLastUpdateTimeEMT(long lastUpdateTimeEMT) {
		this.lastUpdateTimeEMT = lastUpdateTimeEMT;
	}

	/**
	 * @return the emtStationNetwork
	 */
	public Station getEmtStationNetwork() {
		return emtStationNetwork;
	}

	/**
	 * @param emtStationNetwork the emtStationNetwork to set
	 */
	public void setEmtStationNetwork(Station emtStationNetwork) {
		this.emtStationNetwork = emtStationNetwork;
	}

	/**
	 * @return the lastUpdateLocation
	 */
	public long getLastUpdateLocation() {
		return lastUpdateLocation;
	}

	/**
	 * @param lastUpdateLocation the lastUpdateLocation to set
	 */
	public void setLastUpdateLocation(long lastUpdateLocation) {
		this.lastUpdateLocation = lastUpdateLocation;
	}


	
}
