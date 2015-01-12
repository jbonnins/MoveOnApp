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

package com.sanbo.coordinates;

import com.sanbo.utils.Config;

import android.location.Location;
import android.util.Log;

/**
 * @author admin
 * Represents the objects Geoposition containing fields Latitude and Longitude
 *
 */
public class GeoPosition
{
	private double latitude;
	private double longitude;
	
	 /**
	 * generates a Geoposition (0,0) 
	 */
	public GeoPosition(){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "GeoPosition.GeoPosition()");
	    this.latitude = 0.0D;
	    this.longitude = 0.0D;
	  }
	
	 /**
	 * @param latitude
	 * @param longitude
	 * generates a Geoposition (latitude, longitude)
	 */
	public GeoPosition(double latitude, double longitude)
	  {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "GeoPosition.GeoPosition(latitude, longitude)");
	    this.latitude = latitude;
	    this.longitude = longitude;
	  }
	
	 /**
	 * @param latitude
	 * @param longitude
	 * generates a Geoposition (latitude, longitude)
	 */
	public GeoPosition(String latitude, String longitude)
	  {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "GeoPosition.GeoPosition(latitude, longitude)");
	    this.latitude = Double.valueOf(latitude);
	    this.longitude = Double.valueOf(longitude);
	  }
	
	 /**
	 * @param latitude
	 * @param longitude
	 * generates a Geoposition (latitude, longitude)
	 */
	public GeoPosition(String latitude, String longitude, int numDecimal)
	  {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "GeoPosition.GeoPosition(latitude, longitude)");
	    this.latitude = Double.valueOf(latitude)/ Math.pow(1, numDecimal);
	    this.longitude = Double.valueOf(longitude)/ Math.pow(1, numDecimal);
	  }

	/**
	 * @param latitude
	 * @param longitude
	 * generates a Geoposition (latitude, longitude)
	 */
	public GeoPosition(int latitude, int longitude)
	  {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "GeoPosition.GeoPosition(latitude, longitude)");
	    this.latitude = Double.valueOf(latitude);
	    this.longitude = Double.valueOf(longitude);
	  }
	
	public GeoPosition(int latitude, int longitude, int numDecimal)
	  {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "GeoPosition.GeoPosition(latitude, longitude)");
	    this.latitude = Double.valueOf(latitude)/ Math.pow(1, numDecimal);
	    this.longitude = Double.valueOf(longitude) / Math.pow(1, numDecimal);
	  }


	/**
	 * @param mString
	 * retrieves Longitude and Latitude from string "longitude,latitude"
	 */
	public GeoPosition(String mString)
	  {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "GeoPosition.GeoPosition(string)");
		String[] mVal = mString.split(",");
		this.latitude = Double.parseDouble(mVal[0]);
	    this.longitude =Double.parseDouble(mVal[1]);
	  }
		
	  /**
	 * @return
	 */
	public boolean isNullPosition()
	  {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "GeoPosition.isNullPosition()");
	    return (this.latitude == 0.0D) && (this.longitude == 0.0D);
	  }
	
	  /**
	 * @return
	 */
	public GeoPosition getGeoPosition()
	  {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "GeoPosition.getGeoPosition()");
	    return this;
	  }
	
	  /**
	 * @return
	 */
	public double getLatitude()
	  {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "GeoPosition.getLatitude()");
	    return this.latitude;
	  }
	
	  /**
	 * @return
	 */
	public double getLatitudeMoveOn()
	  {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "GeoPosition.getLatitudeMoveOn()");
	    return Double.valueOf(latitude)/ Math.pow(10, Config.locationAccuracy);
	  }

	/**
	 * @return
	 */
	public double getLongitude()
	  {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "GeoPosition.getLongitude()");
	    return this.longitude;
	  }
	
	/**
	 * @return
	 */
	public double getLongitudeMoveOn()
	  {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "GeoPosition.getLongitude()");
	    return Double.valueOf(longitude)/ Math.pow(10, Config.locationAccuracy);
	  }

	/**
	 * @param mDouble
	 */
	public void setLatitude(double mDouble)
	  {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "GeoPosition.setLatitude(double)");
	    this.latitude = mDouble;
	  }
	
	  /**
	 * @param mDouble
	 */
	public void setLongitude(double mDouble)
	  {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "GeoPosition.setLongitude(double)");
	    this.longitude = mDouble;
	  }

	public Location getLocation(){
		Location mLocation = new Location("");
		mLocation.setLatitude(latitude);
		mLocation.setLongitude(longitude);
		return mLocation;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(latitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(longitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "GeoPosition.equals (Object)");
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GeoPosition other = (GeoPosition) obj;
		if (Double.doubleToLongBits(latitude) != Double
				.doubleToLongBits(other.latitude))
			return false;
		if (Double.doubleToLongBits(longitude) != Double
				.doubleToLongBits(other.longitude))
			return false;
		return true;
	}
}