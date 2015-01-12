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
import java.util.Comparator;
import java.util.TimeZone;

import com.sanbo.utils.Config;
import com.sanbo.utils.Utils;


/*
-- _id	FK_id_Station	FK_id_Line	FirstBus	SecondBus	BusStation	
CREATE TABLE "BusStation" ("_id" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL UNIQUE, "FK_id_Station" INTEGER NOT NULL, "FK_id_Line" INTEGER NOT NULL, "FirstBus" DATETIME, "SecondBus" DATETIME);
*/

public class BusStation implements Comparable<BusStation> {
	private int _id;
	private int FK_id_Station;
	private int FK_id_Itinerary;
	private int FK_id_Line;
	private String Destination;
	private Calendar FirstBus;
	private int FirstBusMeters;
	private Calendar SecondBus;
	private int SecondBusMeters;
	// temporary Itinerary
	private Itinerary itinerary;
	private Line line;

	public BusStation() {
		
	}

	/**
	 * @param _id
	 * @param fK_id_Station
	 * @param fK_id_Itinerary
	 * @param destination
	 * @param firstBus
	 * @param firsBusMeters
	 * @param secondBus
	 * @param secondBusMeters
	 */
	public BusStation(int _id, int fK_id_Station, int fK_id_Itinerary,
			String destination, int firstBus, int firstBusMeters,
			int secondBus, int secondBusMeters) {
		super();
		this._id = _id;
		FK_id_Station = fK_id_Station;
		FK_id_Itinerary = fK_id_Itinerary;
		Destination = destination;
		FirstBus = Calendar.getInstance(TimeZone.getTimeZone(Config.MyTimeZone));
		FirstBus.setTimeInMillis(firstBus);
		FirstBusMeters = firstBusMeters;
		SecondBus = Calendar.getInstance(TimeZone.getTimeZone(Config.MyTimeZone));
		SecondBus.setTimeInMillis(secondBus);
		SecondBusMeters = secondBusMeters;
	}

	/**
	 * @param _id
	 * @param fK_id_Station
	 * @param fK_id_Itinerary
	 * @param destination
	 * @param firstBus
	 * @param firsBusMeters
	 * @param secondBus
	 * @param secondBusMeters
	 */
	public BusStation(int _id, int fK_id_Station, int fK_id_Itinerary,
			int fk_id_Line, String destination, Calendar firstBus, 
			int firstBusMeters, Calendar secondBus, int secondBusMeters) {
		
		super();
		this._id = _id;
		FK_id_Station = fK_id_Station;
		FK_id_Itinerary = fK_id_Itinerary;
		FK_id_Line = fk_id_Line;
		Destination = destination;
		FirstBus = Calendar.getInstance(TimeZone.getTimeZone(Config.MyTimeZone));
		FirstBus = firstBus;
		FirstBusMeters = firstBusMeters;
		SecondBus = secondBus;
		SecondBusMeters = secondBusMeters;
	}

	// getting data from EMT pasoporparada, 
	public BusStation(String destination, 
			Calendar firstBus, int firstBusMeters) {
		super();
		_id = -1;
		FK_id_Station = -1;
		FK_id_Itinerary = -1;
		FK_id_Line = -1;
		Destination = destination;
		FirstBus = firstBus;
		FirstBusMeters = firstBusMeters;
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
	
	public String[] getFulName(String destination){
		String[] res = null;
		if (destination.compareToIgnoreCase(line.getReturnEMT())==0){
			res = line.getLineFullName(false);
		} else {
			res = line.getLineFullName(true);
		}
		return res;
	}

	/**
	 * @return the firstBus
	 */
	public Calendar getFirstBus() {
		return FirstBus;
	}

	/**
	 * @param firstBus the firstBus to set
	 */
	public void setFirstBus(Calendar firstBus) {
		FirstBus = firstBus;
	}

	/**
	 * @return the firsBusMeters
	 */
	public int getFirsBusMeters() {
		return FirstBusMeters;
	}

	/**
	 * @param firsBusMeters the firsBusMeters to set
	 */
	public void setFirsBusMeters(int firsBusMeters) {
		FirstBusMeters = firsBusMeters;
	}

	/**
	 * @return the secondBus
	 */
	public Calendar getSecondBus() {
		return SecondBus;
	}

	/**
	 * @param secondBus the secondBus to set
	 */
	public void setSecondBus(Calendar secondBus) {
		SecondBus = secondBus;
	}

	/**
	 * @return the secondBusMeters
	 */
	public int getSecondBusMeters() {
		return SecondBusMeters;
	}

	/**
	 * @param secondBusMeters the secondBusMeters to set
	 */
	public void setSecondBusMeters(int secondBusMeters) {
		SecondBusMeters = secondBusMeters;
	}

	/**
	 * @return the itinerary
	 */
	public Itinerary getItinerary() {
		return itinerary;
	}

	/**
	 * @param itinerary the itinerary to set
	 */
	public void setItinerary(Itinerary itinerary) {
		this.itinerary = itinerary;
	}

	/**
	 * @return the line
	 */
	public Line getLine() {
		return line;
	}

	/**
	 * @param line the line to set
	 */
	public void setLine(Line line) {
		this.line = line;
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
				+ ((FirstBus == null) ? 0 : FirstBus.hashCode());
		result = prime * result + FirstBusMeters;
		result = prime * result
				+ ((SecondBus == null) ? 0 : SecondBus.hashCode());
		result = prime * result + SecondBusMeters;
		result = prime * result + _id;
		result = prime * result
				+ ((itinerary == null) ? 0 : itinerary.hashCode());
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
		BusStation other = (BusStation) obj;
		if (Destination == null) {
			if (other.Destination != null)
				return false;
		} else if (!Destination.equals(other.Destination))
			return false;
		if (FK_id_Itinerary != other.FK_id_Itinerary)
			return false;
		if (FK_id_Station != other.FK_id_Station)
			return false;
		if (FirstBus == null) {
			if (other.FirstBus != null)
				return false;
		} else if (!FirstBus.equals(other.FirstBus))
			return false;
		if (FirstBusMeters != other.FirstBusMeters)
			return false;
		if (SecondBus == null) {
			if (other.SecondBus != null)
				return false;
		} else if (!SecondBus.equals(other.SecondBus))
			return false;
		if (SecondBusMeters != other.SecondBusMeters)
			return false;
		if (_id != other._id)
			return false;
		if (itinerary == null) {
			if (other.itinerary != null)
				return false;
		} else if (!itinerary.equals(other.itinerary))
			return false;
		return true;
	}

	@Override
	public int compareTo(BusStation another) {
		// TODO Auto-generated method stub
		return FirstBus.compareTo(another.getFirstBus());
	}
	    
    /**
     * Comparator to sort stations list or array in order of FirstBus (Time) and Code
     */
    public static Comparator<BusStation> TimeCodeComparator = new Comparator<BusStation>() {
 
        @Override
        public int compare(BusStation s1, BusStation s2) {
        	int res;
        	if (s1.getFirstBus() == null && s2.getFirstBus() == null){
        		res = s1.getItinerary().getCode().compareToIgnoreCase(s2.getItinerary().getCode());
        	} else if (s1.getFirstBus() == null){
        		res = 1;
        	} else if (s2.getFirstBus() == null){
        		res = -1;
        	} else if (s1.getFirstBus().before(s2.getFirstBus())){
        		res = -1;
        	} else if (s1.getFirstBus().after(s2.getFirstBus())){
        		res = 1;
        	} else if (Utils.isNumber(s1.getItinerary().getCode()) 
        			&& Utils.isNumber(s2.getItinerary().getCode())){
        		res = Long.valueOf(s1.getItinerary().getCode()).compareTo(
        				Long.valueOf(s2.getItinerary().getCode()));
        	} else {
       			res = s1.getItinerary().getCode().compareToIgnoreCase(
       					s2.getItinerary().getCode());        		
        	}
        	return res;
        }
    };



}
