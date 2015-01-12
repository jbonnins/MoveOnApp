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
 -- _id	FK_id_Itinerary	UNameOneWay	UNameOneWay2	UNameReturn	UNameReturn2	OneWayEMT	ReturnEMT		Line
 CREATE TABLE "Line" ("_id" INTEGER PRIMARY KEY ASC AUTOINCREMENT NOT NULL UNIQUE, "FK_id_Itinerary" INTEGER NOT NULL, "UNameOneWay" TEXT, "UNameOneWay2" TEXT, "UNameReturn" TEXT, "UNameReturn2" TEXT, "OneWayEMT" TEXT, "ReturnEMT" TEXT);
 */

import java.util.Calendar;
/*
 -- _id	FK_id_TypeTransport FK_id_Itinerary	NameAlternative	UNameOneWay	UNameReturn		Lines
 CREATE TABLE "Lines" ("_id" INTEGER PRIMARY KEY ASC AUTOINCREMENT NOT NULL UNIQUE, "FK_id_TypeTransport" INTEGER NOT NULL, "FK_id_Itinerary" INTEGER NOT NULL, "NameAlternative" TEXT NOT NULL, "UNameOneWay" TEXT, "UNameReturn" TEXT)
 */
import java.util.ArrayList;

public class Line {
	private int _id;
	private int FK_id_Itinerary;
	private String UNameOneWay;
	private String UNameOneWay2;
	private String UNameReturn;
	private String UNameReturn2;
	private String OneWayEMT;
	private String ReturnEMT;

	// if the line is favorite...previous to change this value, is change on DDBB
	private boolean isFavorite = false;
	// in our model if Itinerary has disabled facilities, all the lines of Itinerary has too.
	
	// Frequencies during the day ONCE
	private ArrayList<Frequency> Frequencies = null;
	// Route Oneway of the Line ONCE
	private ArrayList<Station> stationsOW = null;
	// Time to arrive to station on the route Oneway ONCE
	private ArrayList<Calendar> addedTimeOW = null;
	// Route Return of the Line in Circle Line is EMPTY ONCE
	private ArrayList<Station> stationsRT = null;
	// Time to arrive to station on the route Return ONCE
	private ArrayList<Calendar> addedTimeRT = null;
	// Timetable of the Line ONCE
	private ArrayList<TimeTable> timeTables = null;
	// AlterMap of the line ONCE
	private ArrayList<LineAlterMap> linesAlterMap = null;
	//Transfers by distinct TypeOfTransport
	private ArrayList<ItineraryTypeTransport> listTransfer;


	/**
	 * @param _id
	 * @param fK_id_Itinerary
	 * @param uNameOneWay
	 * @param uNameOneWay2
	 * @param uNameReturn
	 * @param uNameReturn2
	 */
	public Line(int _id, int fK_id_Itinerary, String uNameOneWay,
			String uNameOneWay2, String uNameReturn, String uNameReturn2) {
		this._id = _id;
		FK_id_Itinerary = fK_id_Itinerary;
		UNameOneWay = uNameOneWay;
		UNameOneWay2 = uNameOneWay2;
		UNameReturn = uNameReturn;
		UNameReturn2 = uNameReturn2;
	}

	/**
	 * @param _id
	 * @param fK_id_Itinerary
	 * @param uNameOneWay
	 * @param uNameOneWay2
	 * @param uNameReturn
	 * @param uNameReturn2
	 * @param isFavorite
	 */
	public Line(int _id, int fK_id_Itinerary, String uNameOneWay,
			String uNameOneWay2, String uNameReturn, String uNameReturn2, boolean isFavorite) {
		this._id = _id;
		FK_id_Itinerary = fK_id_Itinerary;
		UNameOneWay = uNameOneWay;
		UNameOneWay2 = uNameOneWay2;
		UNameReturn = uNameReturn;
		UNameReturn2 = uNameReturn2;
		this.isFavorite = isFavorite;
	}

	/**
	 * @param _id
	 * @param fK_id_Itinerary
	 * @param uNameOneWay
	 * @param uNameOneWay2
	 * @param uNameReturn
	 * @param uNameReturn2
	 * @param oneWayEMT
	 * @param returnEMT
	 */
	public Line(int _id, int fK_id_Itinerary, String uNameOneWay,
			String uNameOneWay2, String uNameReturn, String uNameReturn2,
			String oneWayEMT, String returnEMT) {
		this._id = _id;
		FK_id_Itinerary = fK_id_Itinerary;
		UNameOneWay = uNameOneWay;
		UNameOneWay2 = uNameOneWay2;
		UNameReturn = uNameReturn;
		UNameReturn2 = uNameReturn2;
		OneWayEMT = oneWayEMT;
		ReturnEMT = returnEMT;
	}

	/**
	 * @return the _id
	 */
	public int get_id() {
		return _id;
	}

	/**
	 * @param _id
	 *            the _id to set
	 */
	public void set_id(int _id) {
		this._id = _id;
	}

	/**
	 * @return the fK_id_Itinerary
	 */
	public int getFK_id_Itinerary() {
		return FK_id_Itinerary;
	}

	/**
	 * @param fK_id_Itinerary
	 *            the fK_id_Itinerary to set
	 */
	public void setFK_id_Itinerary(int fK_id_Itinerary) {
		FK_id_Itinerary = fK_id_Itinerary;
	}

	/**
	 * @return the uNameOneWay
	 */
	public String getUNameOneWay() {
		return UNameOneWay;
	}

	/**
	 * @param uNameOneWay
	 *            the uNameOneWay to set
	 */
	public void setUNameOneWay(String uNameOneWay) {
		UNameOneWay = uNameOneWay;
	}

	/**
	 * @return the uNameOneWay2
	 */
	public String getUNameOneWay2() {
		return UNameOneWay2;
	}

	/**
	 * @param uNameOneWay2
	 *            the uNameOneWay2 to set
	 */
	public void setUNameOneWay2(String uNameOneWay2) {
		UNameOneWay2 = uNameOneWay2;
	}

	/**
	 * @return the uNameReturn
	 */
	public String getUNameReturn() {
		return UNameReturn;
	}

	/**
	 * @param uNameReturn
	 *            the uNameReturn to set
	 */
	public void setUNameReturn(String uNameReturn) {
		UNameReturn = uNameReturn;
	}

	/**
	 * @return the uNameReturn2
	 */
	public String getUNameReturn2() {
		return UNameReturn2;
	}

	/**
	 * @param uNameReturn2
	 *            the uNameReturn2 to set
	 */
	public void setUNameReturn2(String uNameReturn2) {
		UNameReturn2 = uNameReturn2;
	}

	/**
	 * @return the oneWayEMT
	 */
	public String getOneWayEMT() {
		return OneWayEMT;
	}

	/**
	 * @param oneWayEMT
	 *            the oneWayEMT to set
	 */
	public void setOneWayEMT(String oneWayEMT) {
		OneWayEMT = oneWayEMT;
	}

	/**
	 * @return the returnEMT
	 */
	public String getReturnEMT() {
		return ReturnEMT;
	}

	/**
	 * @param returnEMT
	 *            the returnEMT to set
	 */
	public void setReturnEMT(String returnEMT) {
		ReturnEMT = returnEMT;
	}

	/**
	 * @return the frequencies
	 */
	public ArrayList<Frequency> getFrequencies() {
		return Frequencies;
	}

	/**
	 * @param frequencies
	 *            the frequencies to set
	 */
	public void setFrequencies(ArrayList<Frequency> frequencies) {
		Frequencies = frequencies;
	}

	/**
	 * @return the lineStationsOW
	 */
	public ArrayList<Station> getStationsOW() {
		return stationsOW;
	}

	/**
	 * @param lineStationsOW
	 *            the lineStationsOW to set
	 */
	public void setStationsOW(ArrayList<Station> pStationsOW) {
		stationsOW = pStationsOW;
	}

	/**
	 * @return the addedTimeOW
	 */
	public ArrayList<Calendar> getAddedTimeOW() {
		return addedTimeOW;
	}

	/**
	 * @param addedTimeOW
	 *            the addedTimeOW to set
	 */
	public void setAddedTimeOW(ArrayList<Calendar> pAddedTimeOW) {
		addedTimeOW = pAddedTimeOW;
	}

	/**
	 * @return the lineStationsRT
	 */
	public ArrayList<Station> getStationsRT() {
		return stationsRT;
	}

	/**
	 * @param lineStationsRT
	 *            the lineStationsRT to set
	 */
	public void setStationsRT(ArrayList<Station> pStationsRT) {
		stationsRT = pStationsRT;
	}

	/**
	 * @return the addedTimeRT
	 */
	public ArrayList<Calendar> getAddedTimeRT() {
		return addedTimeRT;
	}

	/**
	 * @param addedTimeRT
	 *            the addedTimeRT to set
	 */
	public void setAddedTimeRT(ArrayList<Calendar> pAddedTimeRT) {
		addedTimeRT = pAddedTimeRT;
	}

	/**
	 * @return the timeTables
	 */
	public ArrayList<TimeTable> getTimeTables() {
		return timeTables;
	}

	/**
	 * @param timeTables
	 *            the timeTables to set
	 */
	public void setTimeTables(ArrayList<TimeTable> pTimeTables) {
		timeTables = pTimeTables;
	}

	/**
	 * @return the linesAlterMap
	 */
	public ArrayList<LineAlterMap> getLinesAlterMap() {
		return linesAlterMap;
	}

	/**
	 * @param linesAlterMap
	 *            the linesAlterMap to set
	 */
	public void setLinesAlterMap(ArrayList<LineAlterMap> pLinesAlterMap) {
		linesAlterMap = pLinesAlterMap;
	}

	/**
	 * @return the isFavourite
	 */
	public boolean isFavorite() {
		return isFavorite;
	}

	/**
	 * @param isFavourite
	 *            the isFavourite to set
	 */
	public void setFavorite(boolean isFavorite) {
		this.isFavorite = isFavorite;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + FK_id_Itinerary;
		result = prime * result
				+ ((OneWayEMT == null) ? 0 : OneWayEMT.hashCode());
		result = prime * result
				+ ((ReturnEMT == null) ? 0 : ReturnEMT.hashCode());
		result = prime * result
				+ ((UNameOneWay == null) ? 0 : UNameOneWay.hashCode());
		result = prime * result
				+ ((UNameOneWay2 == null) ? 0 : UNameOneWay2.hashCode());
		result = prime * result
				+ ((UNameReturn == null) ? 0 : UNameReturn.hashCode());
		result = prime * result
				+ ((UNameReturn2 == null) ? 0 : UNameReturn2.hashCode());
		result = prime * result + _id;
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
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
		if (!(obj instanceof Line)) {
			return false;
		}
		Line other = (Line) obj;
		if (_id != other._id) {
			return false;
		}
		return true;
	}

	/**
	 * @return the listTransfer
	 */
	public ArrayList<ItineraryTypeTransport> getListTransfer() {
		return listTransfer;
	}

	/**
	 * @param listTransfer the listTransfer to set
	 */
	public void setListTransfer(ArrayList<ItineraryTypeTransport> listTransfer) {
		this.listTransfer = listTransfer;
	}
	
	/**
	 * @param listTransfer the listTransfer to set
	 */
    public String getLineShortNameEMT(boolean oneWay){
    	String res;
    	if (oneWay) 
    		res = OneWayEMT;
    	else
    		res = ReturnEMT;
    	return res;
    }
    
	/**
	 * @param listTransfer the listTransfer to set
	 */
    public String[] getLineFullName(boolean oneWay){
    	String[] res = new String[2];
    	if (oneWay){ 
    		res[0] = UNameOneWay;
    		res[1] = UNameOneWay2;
    	} else {
    		res[0] = UNameReturn;
    		res[1] = UNameReturn2;
    	}
    	return res;
    }

}