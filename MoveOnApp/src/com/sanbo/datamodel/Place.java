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

import java.util.Comparator;

import android.location.Location;

import com.sanbo.coordinates.GeoPosition;

/*
-- _id	Name	Address	GeoLocation		Place
CREATE TABLE "Place" ("_id" INTEGER PRIMARY KEY ASC AUTOINCREMENT NOT NULL UNIQUE, "Name" TEXT NOT NULL, "Address" TEXT, "Geolocation" TEXT)
*/

public class Place {
	private int _id;
	private String Name;
	private String Address;
	private GeoPosition geoPosition;
	private boolean isFavourite;
	
	// represents distance to the station from the user
	private Float distance;
	// represents angle towards the station from user position, if distance = -1 
	// not a valid bearing
	private float bearing;


	/**
	 * @param _id
	 * @param name
	 * @param address
	 * @param geoLocation
	 * @param isfavorite
	 */
	public Place(int _id, String name, String address, GeoPosition geoLocation, boolean isFavorite) {
		this._id = _id;
		Name = name;
		Address = address;
		geoPosition = geoLocation;
		this.isFavourite = isFavorite;
	}

	/**
	 * @param _id
	 * @param name
	 * @param address
	 * @param geoLocation
	 */
	public Place(int _id, String name, String address, GeoPosition geoLocation) {
		this._id = _id;
		Name = name;
		Address = address;
		geoPosition = geoLocation;
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

	public Place(int _id, String name, String address, GeoPosition mGeoPosition, Location userLocation) {
		this._id = _id;
		Name = name;
		Address = address;
		geoPosition = mGeoPosition;
		if (userLocation != null) {
			distance = geoPosition.getLocation().distanceTo(userLocation);
			bearing  = geoPosition.getLocation().bearingTo(userLocation);
		} else {
			distance = (float)-1;
		}
	}
	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Address == null) ? 0 : Address.hashCode());
		result = prime * result
				+ ((geoPosition == null) ? 0 : geoPosition.hashCode());
		result = prime * result + ((Name == null) ? 0 : Name.hashCode());
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
		if (!(obj instanceof Place)) {
			return false;
		}
		Place other = (Place) obj;
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
	 * @return the address
	 */
	public String getAddress() {
		return Address;
	}
	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		Address = address;
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
	 * @return the geoLocation
	 */
	public GeoPosition getGeoLocation() {
		return geoPosition;
	}
	/**
	 * @param geoLocation the geoLocation to set
	 */
	public void setGeoLocation(GeoPosition geoLocation) {
		geoPosition = geoLocation;
	}
	public boolean isFavourite() {
		return isFavourite;
	}
	public void setFavourite(boolean isFavourite) {
		this.isFavourite = isFavourite;
	}
	
    /**
	 * @return the distance
	 */
	public Float getDistance() {
		return distance;
	}

	/**
	 * @param distance the distance to set
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
	 * @param distance the distance to set
	 */
	public void setDistance(Float distance) {
		this.distance = distance;
	}

	/**
	 * @return the bearing
	 */
	public float getBearing() {
		return bearing;
	}

	/**
	 * @param bearing the bearing to set
	 */
	public void setBearing(float bearing) {
		this.bearing = bearing;
	}

	/**
     * Comparator to sort stations list or array in order of distance
     * if there are no distance between, then use order by name
     */
    public static Comparator<Place> DistanceComparator = new Comparator<Place>() {
 
        @Override
        public int compare(Place s1, Place s2) {
        	int res = (int) (s1.getDistance() - s2.getDistance());
        	if (res == 0) res = s1.getName().compareToIgnoreCase(s2.getName());
        	return res > 0  ? 1 : res < 0 ? -1 : 0;  
        }
    };


}
