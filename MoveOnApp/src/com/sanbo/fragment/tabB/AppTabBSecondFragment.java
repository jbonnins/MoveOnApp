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

package com.sanbo.fragment.tabB;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.google.android.gms.maps.model.LatLng;
import com.sanbo.fragment.base.BaseFragment;
import com.sanbo.moveonapp.R;
import com.sanbo.sinchronizable.SynchronizableActivity;
import com.sanbo.synchronizers.LocationSynchronizer;
import com.sanbo.utils.Config;
import com.sanbo.utils.NetworkInformation;

import de.keyboardsurfer.android.widget.crouton.Crouton;



public class AppTabBSecondFragment extends BaseFragment implements SynchronizableActivity {
	@SuppressWarnings("unused")
	private static final String TAG = "AppTabB";
    // user Location
    private LocationSynchronizer mLocationSynchronizer;
	private Location myLocation;

	// Network Data
	private NetworkInformation network;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        View view       =   inflater.inflate(R.layout.app_tab_b_second_screen, container, false);
		// Retrieve user location
        mLocationSynchronizer = LocationSynchronizer.getInstance(this);
		myLocation = mLocationSynchronizer.getLocation();
		
		network = NetworkInformation.getInstance();
		// saving myLocation to NetworkInformation
		if (myLocation != null){
			network.setMyLocation(new LatLng(myLocation.getLatitude(), 
					myLocation.getLongitude()));
		}
        return view;
	}
	
    @Override
    public void onDestroy() {
        super.onDestroy();
		if (Config.DEBUG) Log.d(Config.LOGTAG, "AppTabBSecondFragment.onDestroy()");
        //not necessary because we always save our new data to DB
		mLocationSynchronizer.detachSynchronizableElement(this);
	    Crouton.clearCroutonsForActivity(getSherlockActivity());
    }
    
	@Override
	public void onSuccessfulNetworkSynchronization() {
		// TODO Auto-generated method stub	
	}

	@Override
	public void onUnsuccessfulNetworkSynchronization() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onLocationSynchronization() {
		// TODO Auto-generated method stub
	}

	@Override
	public SherlockFragmentActivity getSynchronizableActivity() {
		// TODO Auto-generated method stub
		return getSherlockActivity();
	}
	
}
