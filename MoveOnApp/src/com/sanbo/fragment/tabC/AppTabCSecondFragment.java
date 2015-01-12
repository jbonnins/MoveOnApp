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

package com.sanbo.fragment.tabC;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.google.android.gms.maps.model.LatLng;
import com.sanbo.fragment.base.AppConstants;
import com.sanbo.fragment.base.BaseFragment;
import com.sanbo.moveonapp.R;
import com.sanbo.sinchronizable.SynchronizableActivity;
import com.sanbo.synchronizers.LocationSynchronizer;
import com.sanbo.utils.Config;
import com.sanbo.utils.NetworkInformation;

import de.keyboardsurfer.android.widget.crouton.Crouton;


public class AppTabCSecondFragment extends BaseFragment implements SynchronizableActivity{
	@SuppressWarnings("unused")
	private static final String TAG = "AppTabC";
    private Button mGotoButton;
    // user Location
    private LocationSynchronizer mLocationSynchronizer;
	private Location myLocation;

	// Network Data
	private NetworkInformation network;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view       =   inflater.inflate(R.layout.app_tab_c_second_screen, container, false);

        mGotoButton =   (Button) view.findViewById(R.id.id_next_tab_c_button);
        mGotoButton.setOnClickListener(listener);
		// Retrieve user location
        mLocationSynchronizer = LocationSynchronizer.getInstance(this);
		myLocation = mLocationSynchronizer.getLocation();
        // network information
		network = NetworkInformation.getInstance();
		// saving myLocation to NetworkInformation
		if (myLocation != null){
			network.setMyLocation(new LatLng(myLocation.getLatitude(), 
					myLocation.getLongitude()));
		}

        return view;
    }

    private OnClickListener listener        =   new View.OnClickListener(){
        @Override
        public void onClick(View v){
            /* Go to next fragment in navigation stack*/
            mActivity.pushFragments(AppConstants.TAB_C, new AppTabCThirdFragment(),true,true);
        }
    };
    
    @Override
    public void onDestroy() {
        super.onDestroy();
		if (Config.DEBUG) Log.d(Config.LOGTAG, "AppTabCSecondFragment.onDestroy()");
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
