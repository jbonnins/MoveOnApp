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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.sanbo.datamodel.PublicTransport;
import com.sanbo.datamodel.Station;
import com.sanbo.enumerated.TypeTransport;
import com.sanbo.parser.BiciPalmaParser;
import com.sanbo.sinchronizable.SynchronizableActivity;
import com.sanbo.utils.Config;
import com.sanbo.utils.NetworkInformation;

public class BiciPalmaNetworkSynchronizer {
	// answer OK from server
	private static final int HTTP_STATUS_OK = 200;
	// Instance from biciPalmaNetworkSynchronizer
    // SINGLETON DEFINITION
	private static BiciPalmaNetworkSynchronizer INSTANCE = null;
	// Our data from Network
	private NetworkInformation network;
	// list of our activities --- probably we need
	private ArrayList<SynchronizableActivity> synchronizable_activities;

	// data
	private PublicTransport mPublic = PublicTransport.getInstance();
	
	private class SynchronizeTask extends AsyncTask <Void, Void, Void> {
        
		private SynchronizableActivity activity;
		private BiciPalmaNetworkSynchronizer synchronizer;
		private NetworkInformation network;
		private boolean connectivity;
		
		
		public SynchronizeTask (SynchronizableActivity pActivity) {
			activity     = pActivity;
			synchronizer = BiciPalmaNetworkSynchronizer.getInstance(pActivity);
			network      = NetworkInformation.getInstance();
			connectivity = true;
		}

    	protected Void doInBackground(Void... params) {
    		String jsonNetwork;
    		ArrayList <Station> parsedNetwork = null;
    		long   lastUpdateTime = 0;
    		ConnectivityManager conMgr =
    				(ConnectivityManager)activity.getSynchronizableActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
 
    		NetworkInfo i = conMgr.getActiveNetworkInfo();
    		if (i == null || !i.isAvailable() || !i.isConnected()) {
    			// get last data in our DB
    			connectivity = false;
    			if (network.getBikeStationNetwork() == null) {
    				lastUpdateTime = mPublic.getTimeStamp(TypeTransport.BICIPALMA, null);
    				
        			parsedNetwork  = mPublic.getAllBikeStations();
        			// our data in DB, as temporary available
            		network.setBikeStationNetwork(parsedNetwork);
            		network.setLastUpdateTimeCityBik(lastUpdateTime);
    			}
    		} else {
    			jsonNetwork    = synchronizer.getNetworkInfo();
    			// bydefault last update is current local time
    			lastUpdateTime = mPublic.getTimeStamp(TypeTransport.BICIPALMA, null);
    			parsedNetwork  = BiciPalmaParser.parseNetworkJSON(jsonNetwork);
    			
    			// save data to instance
        		network.setBikeStationNetwork(parsedNetwork);
        		         		
        		// add data to PublicTransport
        		mPublic.addDataNetworkToListBikeStations(parsedNetwork, network);
        		// store to DB
        		//mPublic.storeListAllBikeStations();
        		// now we can get timeStamp from DB
        		Long newUpdateTime = network.getLastUpdateTimeCityBik();
        		// just in case we don't have internet connection
        		newUpdateTime = newUpdateTime == null ? lastUpdateTime : newUpdateTime;
        		network.setLastUpdateTimeCityBik(newUpdateTime);

   		}		
        return null;
        }
		
        protected void onPostExecute(Void params) {
        	if (connectivity) {
	        	successfulNetworkSynchronization();
        	} else {
        		unSuccessfulNetworkSynchronization();
        	}
        	activity.onLocationSynchronization();
        }
    }
    			
	private BiciPalmaNetworkSynchronizer() {
		synchronizable_activities = new ArrayList<SynchronizableActivity>();
		network = NetworkInformation.getInstance();
	}
	
	public synchronized static BiciPalmaNetworkSynchronizer getInstance(SynchronizableActivity activity) {
		if (INSTANCE == null) {
			INSTANCE = new BiciPalmaNetworkSynchronizer();
		}		
		INSTANCE.addSynchronizableActivity(activity);
		
		return INSTANCE;
	}
  			
	private String getNetworkInfo() {
		StringBuilder builder = new StringBuilder();
		HttpClient    client  = new DefaultHttpClient();
		HttpGet 	  request = new HttpGet(Config.REPORT_URL_CITYBIK);
		String		  line;
		
		try {
			HttpResponse response = client.execute(request);
			StatusLine   status_line = response.getStatusLine();
			if (status_line.getStatusCode() == HTTP_STATUS_OK) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				InputStreamReader content_reader = new InputStreamReader(content);
				BufferedReader reader = new BufferedReader(content_reader);
				
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
			} else {
				if (Config.DEBUG) Log.e(BiciPalmaNetworkSynchronizer.class.toString(),
						"Failed getting data from Network");
			}
		} catch (Exception e) {
			Log.e(Config.LOGTAG, "BiciPalmaNetworkSynchronizer.getNetworkInfo() ");
		}		
		return builder.toString();
	}
   			
	public boolean storeToDB () {
		boolean res = false;
		// modify data in PublicTransport
		if (mPublic.getTimeStamp(TypeTransport.BICIPALMA, null).compareTo(network.getLastUpdateTimeCityBik()) < 0){
			res = mPublic.storeListAllBikeStations();
		} else {
			res = false;
		}
		return res;
	}

	private synchronized void successfulNetworkSynchronization () {
		@SuppressWarnings("unchecked")
		ArrayList<SynchronizableActivity> syncElements = (ArrayList<SynchronizableActivity>) synchronizable_activities.clone();
		for (SynchronizableActivity activity : syncElements) {
			activity.onSuccessfulNetworkSynchronization();
		}
	}
	
	private synchronized void unSuccessfulNetworkSynchronization () {
		@SuppressWarnings("unchecked")
		ArrayList<SynchronizableActivity> syncElements = (ArrayList<SynchronizableActivity>) synchronizable_activities.clone();
		for (SynchronizableActivity activity : syncElements) {
			activity.onUnsuccessfulNetworkSynchronization();
		}
	}
	
	public synchronized void addSynchronizableActivity(SynchronizableActivity activity) {
		if (!synchronizable_activities.contains(activity)) { 
			synchronizable_activities.add(activity);
		}
	}
	
	public synchronized void detachSynchronizableActivity(SynchronizableActivity activity) {
		synchronizable_activities.remove(activity);
	}
	
	public synchronized void synchronize(SynchronizableActivity activity) {
		new SynchronizeTask(activity).execute((Void [])null);
	}
}
