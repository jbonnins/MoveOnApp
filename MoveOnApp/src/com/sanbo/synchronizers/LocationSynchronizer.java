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

package com.sanbo.synchronizers;

import java.util.ArrayList;

import com.sanbo.sinchronizable.SynchronizableActivity;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class LocationSynchronizer {
	private static LocationSynchronizer INSTANCE = null;
	
	private Location 	     location = null;
	private LocationManager  manager;
	private LocationListener listener;
	
	private static ArrayList<SynchronizableActivity> synchronizableElements = null;
	
	public synchronized static LocationSynchronizer getInstance (SynchronizableActivity activity) {
		if (INSTANCE == null) {
			INSTANCE = new LocationSynchronizer(activity.getSynchronizableActivity());
		}		
		INSTANCE.addSynchronizableElement(activity);
		
		return INSTANCE;
	}
	
	public LocationSynchronizer (FragmentActivity context) {		
		manager  = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		listener = new LocationListener() {

			@Override
			public void onLocationChanged(Location l) {
				location = l;
				updateViews();
			}
			
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {}
			
			@Override
			public void onProviderEnabled(String provider) {}
			
			@Override
			public void onProviderDisabled(String provider) {
				if ((provider.equals(LocationManager.GPS_PROVIDER) &&
						!manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) ||
					(provider.equals(LocationManager.NETWORK_PROVIDER) &&
						!manager.isProviderEnabled(LocationManager.GPS_PROVIDER))) {
					location = null;
					updateViews();
				}					
			}
		};
		
		manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000L, 0, listener);
		manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000L, 0, listener);
		
		if (location == null)
			location =  manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		
		if (location == null)
			location =  manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
				
		synchronizableElements = new ArrayList<SynchronizableActivity>();
	}
	
	private synchronized void updateViews () {
		@SuppressWarnings("unchecked")
		ArrayList<SynchronizableActivity> syncElements = (ArrayList<SynchronizableActivity>) synchronizableElements.clone();
		for (SynchronizableActivity activity : syncElements)
			activity.onLocationSynchronization();
	}
	
	public synchronized void addSynchronizableElement(SynchronizableActivity element) {
		if (!synchronizableElements.contains(element)) { 
			synchronizableElements.add(element);
		}
	}
	
	public synchronized void detachSynchronizableElement(SynchronizableActivity element) {
		synchronizableElements.remove(element);
	}
	
	public Location getLocation () {
		return location;
	}
		
}

	
	
	