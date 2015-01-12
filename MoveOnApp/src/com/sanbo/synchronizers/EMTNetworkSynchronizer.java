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
import java.util.Calendar;
import java.util.TimeZone;

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
import com.sanbo.parser.EMTParser;
import com.sanbo.sinchronizable.SynchronizableActivity;
import com.sanbo.utils.Config;
import com.sanbo.utils.NetworkInformation;
import com.sanbo.utils.SavingState;

public class EMTNetworkSynchronizer {
	// answer OK from server
	private static final int HTTP_STATUS_OK = 200;
	// Instance from EMTNetworkSynchronizer
    // SINGLETON DEFINITION
	private static EMTNetworkSynchronizer INSTANCE = null;
	// list of our activities --- probably we need
	private ArrayList<SynchronizableActivity> synchronizable_activities;
	
	// data
	private PublicTransport mPublic = PublicTransport.getInstance();
	private SavingState mSave = SavingState.getInstance();

	private class SynchronizeTask extends AsyncTask <Void, Void, Void> {
        
		private SynchronizableActivity activity;
		private EMTNetworkSynchronizer	synchronizer;
		private NetworkInformation network;
		private boolean connectivity;

	

		public SynchronizeTask (SynchronizableActivity mActivity) {
			if (Config.DEBUG) Log.d(Config.LOGTAG, "EMTNetworkSynchronizer.SynchronizeTask (SynchronizableActivity mActivity)");
			activity     = mActivity;
			synchronizer = EMTNetworkSynchronizer.getInstance(activity);
			network      = NetworkInformation.getInstance();
			connectivity = true;
		}

		protected Void doInBackground(Void... params) {
			if (Config.DEBUG) Log.d(Config.LOGTAG, "EMTNetworkSynchronizer.doInBackground(Void... params)");
    		String jsonNetwork;
    		Station myStation = mSave.getMyActiveStation();
    		Station parsedNedwork = null;
    		// starting synchronization
    		Long   lastUpdateTime = 0L;
    		ConnectivityManager conMgr =
    				(ConnectivityManager)activity.getSynchronizableActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

    		NetworkInfo i = conMgr.getActiveNetworkInfo();
    		if (i == null || !i.isAvailable() || !i.isConnected()) {
    			// get last data in our DB
    			connectivity = false;
    			if (network.getEmtStationNetwork() == null) {
    				lastUpdateTime = mPublic.getTimeStamp(TypeTransport.BUSPALMA, myStation);
    				if (lastUpdateTime == null) lastUpdateTime = 0L;

    				parsedNedwork  = new Station(mSave.getMyActiveStation().getCode(), 
    						mSave.getMyActiveStation().getName(), mSave.getMyActiveStation().getTimeStamp());
    				
    				parsedNedwork.setBusStations(mPublic.getListAllBusStations(myStation));
        			// our data in DB, as temporary available
            		network.setEmtStationNetwork(parsedNedwork);
            		network.setLastUpdateTimeEMT(lastUpdateTime);
    			}
    		} else {
    			jsonNetwork    = synchronizer.getNetworkInfo(myStation);
    			// bydefault last update is current local time
    			lastUpdateTime = mPublic.getTimeStamp(TypeTransport.BUSPALMA, myStation);
    			parsedNedwork = EMTParser.parseNetworkJSON(myStation.getCode(), jsonNetwork);
   			
    			// save data to instance
        		network.setEmtStationNetwork(parsedNedwork);
    			         		
        		// add data to PublicTransport, here we check data from network...
        		mPublic.addDataNetworkToListBusStations(mSave.getMyActiveStation(), parsedNedwork);
    			// store to DB
        		//mPublic.storeListAllBusStations(mSave.getMyStation());
    			// now we can get timeStamp from DB
    			Long newUpdateTime = parsedNedwork.getTimeStamp().getTimeInMillis();
    			// just in case we don't have internet connection
    			newUpdateTime = newUpdateTime == null ? lastUpdateTime : newUpdateTime;
    			if (newUpdateTime == null) 
    				newUpdateTime = Calendar.getInstance(TimeZone.getTimeZone(Config.MyTimeZone)).getTimeInMillis();
    			network.setLastUpdateTimeEMT(newUpdateTime);

    		}		
    	return null;
    	}

	    protected void onPostExecute(Void params) {
			if (Config.DEBUG) Log.d(Config.LOGTAG, "EMTNetworkSynchronizer.onPostExecute(Void params)");
			if (connectivity) {
	        	successfulNetworkSynchronization();
        	} else {
        		unSuccessfulNetworkSynchronization();
        	}
        }
	}
    			
	private EMTNetworkSynchronizer() {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "EMTNetworkSynchronizer.EMTNetworkSynchronizer()");
		synchronizable_activities = new ArrayList<SynchronizableActivity>();
	}
	
	public synchronized static EMTNetworkSynchronizer getInstance(SynchronizableActivity activity) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "EMTNetworkSynchronizer.onPostExecute(Void params)");
		if (INSTANCE == null) {
			INSTANCE = new EMTNetworkSynchronizer();
		}		
		INSTANCE.addSynchronizableActivity(activity);
		
		return INSTANCE;
	}

	private String getNetworkInfo(Station myStation) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "EMTNetworkSynchronizer.getNetworkInfo (Station.code =" + myStation.getCode());
		StringBuilder builder = new StringBuilder();
		HttpClient    client  = new DefaultHttpClient();
		// we add the code to the request
		HttpGet 	  request = new HttpGet(Config.REPORT_URL_EMT + myStation.getCode());
		String		  line;
		
		try {
			HttpResponse response = client.execute(request);
			StatusLine   status_line = response.getStatusLine();
			if (status_line.getStatusCode() == HTTP_STATUS_OK) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();							
				InputStreamReader content_reader = new InputStreamReader(content, "UTF-8");
				BufferedReader reader = new BufferedReader(content_reader, 8000);
				
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
			} else {
				if (Config.DEBUG) Log.e(EMTNetworkSynchronizer.class.toString(),
						"Failed getting data from Network");
			}
		} catch (Exception e) {
			Log.e(Config.LOGTAG, "EMTNetworkSynchronizer.getNetworkInfo() ");
		}		
		return builder.toString();
	}
 
	public boolean storeToDB (Station myStation) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "EMTNetworkSynchronizer.storeToDB (Station.code =" + myStation.getCode());
		boolean res = false;
		// modify data in PublicTransport
		if (mPublic.getTimeStamp(TypeTransport.BUSPALMA, myStation).compareTo(myStation.getTimeStamp().getTimeInMillis()) < 0){
			res = mPublic.storeListAllBusStations(myStation);
		} else {
			res = false;
		}
		return res;
	}
	
	private synchronized void successfulNetworkSynchronization () {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "EMTNetworkSynchronizer.successfulNetworkSynchronization ()");
		@SuppressWarnings("unchecked")
		ArrayList<SynchronizableActivity> syncElements = (ArrayList<SynchronizableActivity>) synchronizable_activities.clone();
		for (SynchronizableActivity activity : syncElements) {
			activity.onSuccessfulNetworkSynchronization();
		}
	}
	
	private synchronized void unSuccessfulNetworkSynchronization () {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "EMTNetworkSynchronizer.unSuccessfulNetworkSynchronization ()");
		@SuppressWarnings("unchecked")
		ArrayList<SynchronizableActivity> syncElements = (ArrayList<SynchronizableActivity>) synchronizable_activities.clone();
		for (SynchronizableActivity activity : syncElements) {
			activity.onUnsuccessfulNetworkSynchronization();
		}
	}
		
	public synchronized void addSynchronizableActivity(SynchronizableActivity activity) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "EMTNetworkSynchronizer.addSynchronizableActivity(SynchronizableActivity activity)");
		if (!synchronizable_activities.contains(activity)) { 
			synchronizable_activities.add(activity);
		}
	}
	
	public synchronized void detachSynchronizableActivity(SynchronizableActivity activity) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "EMTNetworkSynchronizer.detachSynchronizableActivity(SynchronizableActivity activity)");
		synchronizable_activities.remove(activity);
	}
	
	public synchronized void synchronize(SynchronizableActivity activity) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "EMTNetworkSynchronizer.synchronize(SynchronizableActivity activity)");
		new SynchronizeTask(activity).execute((Void [])null);
	}
}

	
