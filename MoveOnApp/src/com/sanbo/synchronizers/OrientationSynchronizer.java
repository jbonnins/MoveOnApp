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
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class OrientationSynchronizer {

	private static Sensor              sensor;
	private static SensorManager 	   manager;
	private static SensorEventListener listener;
	
	private static float orientation;
	
	private static ArrayList<SynchronizableActivity> synchronizable_activities;
	
	@SuppressWarnings("deprecation")
	public static void init (Context context) {
		manager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		sensor  = manager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		
		listener = new SensorEventListener() {
			
			@Override
			public void onSensorChanged(SensorEvent event) {
				orientation = event.values[0];
				updateViews();
			}
			
			@Override
			public void onAccuracyChanged(Sensor sensor, int accuracy) {}
		};
		
		manager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
		
		synchronizable_activities = new ArrayList<SynchronizableActivity>();
	}
	
	private static void updateViews () {
		for (SynchronizableActivity activity : synchronizable_activities) {
			activity.onLocationSynchronization();
		}
	}
	
	public static void addSynchronizableActivity(SynchronizableActivity activity) {
		synchronizable_activities.add(activity);
	}
	
	public static void detachSynchronizableActivity(SynchronizableActivity activity) {
		synchronizable_activities.remove(activity);
	}	
	
	public static float getOrientation () {
		return orientation;
	}
}
