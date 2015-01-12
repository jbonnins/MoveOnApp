/*
C * Copyright 2013 Juan José Bonnín Sansó (jbonnins@uoc.edu)
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.TimeZone;

import android.location.Location;
import com.sanbo.coordinates.GeoPosition;
import com.sanbo.utils.Config;
import com.sanbo.utils.TimeUtils;

/*
-- _id	FK_id_Transport	Code	Name	Adapted_for_Disabled	Latitude	Longitude	TimeStamp	Transfer	Velocity	FK_id_Zone	Station					
CREATE TABLE "Station" ("_id" INTEGER PRIMARY KEY ASC AUTOINCREMENT NOT NULL UNIQUE, "FK_id_Transport" INTEGER NOT NULL, "Code" TEXT NOT NULL, "Name" TEXT, "Adapted_for_Disabled" BOOLEAN NOT NULL, "Latitude" INTEGER NOT NULL, "Longitude" INTEGER NOT NULL, "TimeStamp" TEXT, "Transfer" BOOLEAN NOT NULL, "Velocity" INTEGER, "FK_id_Zone" INTEGER);
*/



public class Station implements Comparable<Station>
{

	private int _id;
	private int FK_id_Transport;
	private String Code;
	private String Name;
	private boolean Adapted_for_Disabled;
	private GeoPosition geoPosition;
	// we have local time in miliseconds
	private Calendar TimeStamp = null;
	private boolean Transfer; 
	private int Velocity;
	private int FK_id_Zone;
	
	// next bus for EMT bus station
	private ArrayList<BusStation> busStations = null;
	// avaliability of bike station BICI Palma
	private BikeStation bikeStation = null;
	// lines at station for all types of station
	private ArrayList<LineStation> lineStations = null;	
	// if the station is favorite...previous to change this value, is change on DDBB
	private boolean isFavorite = false;

	// represents distance to the station from the user
	private Float distance;
	// represents angle towards the station from user position, if distance = -1 
	// not a valid bearing
	private float bearing;
	
	//Transfers by distinct TypeOfTransport
	private ArrayList<ItineraryTypeTransport> listTransfer;
	
	public Station()
	{
	}

	public Station(int _id, int fK_id_Transport, String code, String name,
			boolean adapted_for_Disabled, GeoPosition mGeoPosition,
			String timeStamp, boolean transfer, int velocity, int fK_id_Zone) {
		this._id = _id;
		FK_id_Transport = fK_id_Transport;
		Code = code;
		Name = name;
		Adapted_for_Disabled = adapted_for_Disabled;
		geoPosition = mGeoPosition;
		if (timeStamp != null){
			TimeStamp = Calendar.getInstance(TimeZone.getTimeZone(Config.MyTimeZone));
			TimeStamp.setTimeInMillis(Long.valueOf(timeStamp));
		}
		Transfer = transfer;
		Velocity = velocity;
		FK_id_Zone = fK_id_Zone;
	}

	/**
	 * @param _id
	 * @param fK_id_Transport
	 * @param code
	 * @param name
	 * @param adapted_for_Disabled
	 * @param mGeoPosition
	 * @param timeStamp
	 * @param transfer
	 * @param velocity
	 * @param fK_id_Zone
	 * @param userLocation
	 */

	public Station(int _id, int fK_id_Transport, String code, String name,
			boolean adapted_for_Disabled, GeoPosition mGeoPosition,
			String timeStamp, boolean transfer, int velocity, int fK_id_Zone, Location userLocation) {
		this._id = _id;
		FK_id_Transport = fK_id_Transport;
		Code = code;
		Name = name;
		Adapted_for_Disabled = adapted_for_Disabled;
		geoPosition = mGeoPosition;
		if (timeStamp != null){
			TimeStamp = Calendar.getInstance(TimeZone.getTimeZone(Config.MyTimeZone));
			TimeStamp.setTimeInMillis(Long.valueOf(timeStamp));
		}
		Transfer = transfer;
		Velocity = velocity;
		FK_id_Zone = fK_id_Zone;
		
		if (userLocation != null) {
			distance = geoPosition.getLocation().distanceTo(userLocation);
			bearing  = geoPosition.getLocation().bearingTo(userLocation);
		} else {
			distance = (float)-1;
		}
	}
	
	// Station from CityBik Api
    public Station(String code, String name, double stationLong, double stationLat,
	        String timeStamp, int freeSlots, int busySlots) {
    	// this id is temporary
    	_id = -1;
    	Code = code;
    	Name = name;
    	geoPosition = new GeoPosition(stationLat, stationLong);
    	// if null by default we send TimeStamp
    	long myMillis = TimeUtils.UTCtoMilliseconds(timeStamp);
		if (timeStamp != null){
			TimeStamp = Calendar.getInstance(TimeZone.getTimeZone(Config.MyTimeZone));
			TimeStamp.setTimeInMillis(myMillis);
		}
    	// info from bikeStation
    	bikeStation = new BikeStation(code, busySlots, freeSlots);    	
    }
    
    // Station from EMT-pasoporparada-svr
    public Station(String code, String name, String timeStamp) {
    	// this id is temporary
    	_id = -1;
    	Code = code;
    	Name = name;
		if (timeStamp != null){
			TimeStamp = Calendar.getInstance();
			TimeStamp.setTimeZone(TimeZone.getTimeZone(Config.MyTimeZone));
			TimeStamp.setTimeInMillis(Long.valueOf(timeStamp));
		}
    }
    
    // Station from EMT-pasoporparada-svr
    public Station(String code, String name, Calendar timeStamp) {
    	// this id is temporary
    	_id = -1;
    	Code = code;
    	Name = name;
    	TimeStamp = timeStamp;

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
	 * @return the fK_id_Transport
	 */
	public int getFK_id_Transport() {
		return FK_id_Transport;
	}

	/**
	 * @param fK_id_Transport the fK_id_Transport to set
	 */
	public void setFK_id_Transport(int fK_id_Transport) {
		FK_id_Transport = fK_id_Transport;
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

	/**
	 * @return the name
	 */
	public String getName() {
		return Name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		Name = name;
	}

	/**
	 * @return the adapted_for_Disabled
	 */
	public boolean isAdapted_for_Disabled() {
		return Adapted_for_Disabled;
	}

	/**
	 * @param adapted_for_Disabled the adapted_for_Disabled to set
	 */
	public void setAdapted_for_Disabled(boolean adapted_for_Disabled) {
		Adapted_for_Disabled = adapted_for_Disabled;
	}

	/**
	 * @return the geoPosition
	 */
	public GeoPosition getGeoPosition() {
		return geoPosition;
	}

	/**
	 * @param geoPosition the geoPosition to set
	 */
	public void setGeoPosition(GeoPosition geoPosition) {
		this.geoPosition = geoPosition;
	}

	/**
	 * @return the timeStamp
	 */
	public Calendar getTimeStamp() {
		return TimeStamp;
	}

	/**
	 * @param timeStamp the timeStamp to set
	 */
	public void setTimeStamp(Calendar timeStamp) {
		if (timeStamp != null){
			TimeStamp = Calendar.getInstance(TimeZone.getTimeZone(Config.MyTimeZone));
			TimeStamp.setTimeInMillis(timeStamp.getTimeInMillis());
		} else {
			TimeStamp = null;
		}
		
	}

	/**
	 * @param timeStamp the timeStamp to set
	 */
	public void setTimeStamp(Long timeStampInMillis) {
		TimeStamp = Calendar.getInstance();
		if (timeStampInMillis != null){
			TimeStamp.setTimeZone(TimeZone.getTimeZone(Config.MyTimeZone));
			TimeStamp.setTimeInMillis(timeStampInMillis);
		}
;
	}

	/**
	 * @return the transfer
	 */
	public boolean isTransfer() {
		return Transfer;
	}

	/**
	 * @param transfer the transfer to set
	 */
	public void setTransfer(boolean transfer) {
		Transfer = transfer;
	}

	/**
	 * @return the velocity
	 */
	public int getVelocity() {
		return Velocity;
	}

	/**
	 * @param velocity the velocity to set
	 */
	public void setVelocity(int velocity) {
		Velocity = velocity;
	}

	/**
	 * @return the fK_id_Zone
	 */
	public int getFK_id_Zone() {
		return FK_id_Zone;
	}

	/**
	 * @param fK_id_Zone the fK_id_Zone to set
	 */
	public void setFK_id_Zone(int fK_id_Zone) {
		FK_id_Zone = fK_id_Zone;
	}

	/**
	 * @return the busStations
	 */
	public ArrayList<BusStation> getBusStations() {
		return busStations;
	}

	/**
	 * @param busStations the busStations to set
	 */
	public void setBusStations(ArrayList<BusStation> busStations) {
		this.busStations = busStations;
	}

	/**
	 * @return the bikeStation
	 */
	public BikeStation getBikeStation() {
		return bikeStation;
	}

	/**
	 * @param bikeStation the bikeStation to set
	 */
	public void setBikeStation(BikeStation bikeStation) {
		this.bikeStation = bikeStation;
	}

	/**
	 * @return the lineStations
	 */
	public ArrayList<LineStation> getLineStations() {
		return lineStations;
	}

	/**
	 * @param lineStations the lineStations to set
	 */
	public void setLineStations(ArrayList<LineStation> lineStations) {
		this.lineStations = lineStations;
	}

	/**
	 * @return the isFavorite
	 */
	public boolean isFavorite() {
		return isFavorite;
	}

	/**
	 * @param isFavorite the isFavorite to set
	 */
	public void setFavorite(boolean isFavorite) {
		this.isFavorite = isFavorite;
	}

	/**
	 * @return the distance
	 */
	public Float getDistance() {
		return distance;
	}

	/**
	 * @return the bearing
	 */
	public float getBearing() {
		return bearing;
	}

	/**
	 * @param userLocation referenced to set distance and bearing
	 */
	public void setDistance(Location userLocation) {
		if (userLocation != null) {
			distance = geoPosition.getLocation().distanceTo(userLocation);
			bearing  = geoPosition.getLocation().bearingTo(userLocation);
		} else {
			distance = (float)-1;
		}
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
	 * @param distance the distance to set
	 */
	public void setDistance(Float distance) {
		this.distance = distance;
	}

	/**
	 * @param bearing the bearing to set
	 */
	public void setBearing(float bearing) {
		this.bearing = bearing;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (Adapted_for_Disabled ? 1231 : 1237);
		result = prime * result + ((Code == null) ? 0 : Code.hashCode());
		result = prime * result + FK_id_Transport;
		result = prime * result + FK_id_Zone;
		result = prime * result + ((Name == null) ? 0 : Name.hashCode());
		result = prime * result
				+ ((TimeStamp == null) ? 0 : TimeStamp.hashCode());
		result = prime * result + (Transfer ? 1231 : 1237);
		result = prime * result + Velocity;
		result = prime * result + _id;
		result = prime * result + Float.floatToIntBits(bearing);
		result = prime * result
				+ ((bikeStation == null) ? 0 : bikeStation.hashCode());
		result = prime * result
				+ ((busStations == null) ? 0 : busStations.hashCode());
		result = prime * result
				+ ((distance == null) ? 0 : distance.hashCode());
		result = prime * result
				+ ((geoPosition == null) ? 0 : geoPosition.hashCode());
		result = prime * result + (isFavorite ? 1231 : 1237);
		result = prime * result
				+ ((lineStations == null) ? 0 : lineStations.hashCode());
		result = prime * result
				+ ((listTransfer == null) ? 0 : listTransfer.hashCode());
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
		Station other = (Station) obj;
		if (Adapted_for_Disabled != other.Adapted_for_Disabled)
			return false;
		if (Code == null) {
			if (other.Code != null)
				return false;
		} else if (!Code.equals(other.Code))
			return false;
		if (FK_id_Transport != other.FK_id_Transport)
			return false;
		if (FK_id_Zone != other.FK_id_Zone)
			return false;
		if (Name == null) {
			if (other.Name != null)
				return false;
		} else if (!Name.equals(other.Name))
			return false;
		if (TimeStamp == null) {
			if (other.TimeStamp != null)
				return false;
		} else if (!TimeStamp.equals(other.TimeStamp))
			return false;
		if (Transfer != other.Transfer)
			return false;
		if (Velocity != other.Velocity)
			return false;
		if (_id != other._id)
			return false;
		if (Float.floatToIntBits(bearing) != Float
				.floatToIntBits(other.bearing))
			return false;
		if (bikeStation == null) {
			if (other.bikeStation != null)
				return false;
		} else if (!bikeStation.equals(other.bikeStation))
			return false;
		if (busStations == null) {
			if (other.busStations != null)
				return false;
		} else if (!busStations.equals(other.busStations))
			return false;
		if (distance == null) {
			if (other.distance != null)
				return false;
		} else if (!distance.equals(other.distance))
			return false;
		if (geoPosition == null) {
			if (other.geoPosition != null)
				return false;
		} else if (!geoPosition.equals(other.geoPosition))
			return false;
		if (isFavorite != other.isFavorite)
			return false;
		if (lineStations == null) {
			if (other.lineStations != null)
				return false;
		} else if (!lineStations.equals(other.lineStations))
			return false;
		if (listTransfer == null) {
			if (other.listTransfer != null)
				return false;
		} else if (!listTransfer.equals(other.listTransfer))
			return false;
		return true;
	}

	@Override
	public int compareTo(Station another) {
		// TODO Auto-generated method stub
        Boolean interF = this.isFavorite;
        Boolean exterF = another.isFavorite();
        
        return exterF.compareTo(interF);
	}
	
    /**
     * Comparator to sort stations list or array in order of distance
     * if there are no distance between, then use order by name
     */
    public static Comparator<Station> DistanceComparator = new Comparator<Station>() {
 
        @Override
        public int compare(Station s1, Station s2) {
        	int res = (int) (s1.getDistance() - s2.getDistance());
        	if (res == 0) res = s1.getName().compareToIgnoreCase(s2.getName());
        	return res > 0  ? 1 : res < 0 ? -1 : 0;  
        }
    };
    
    /**
     * Comparator to sort stations list or array in order of Name
     */
    public static Comparator<Station> NameComparator = new Comparator<Station>() {
 
        @Override
        public int compare(Station s1, Station s2) {
        	int res = s1.getName().compareToIgnoreCase(s2.getName());
        	return res > 0  ? 1 : res < 0 ? -1 : 0;
        }
    };

}
