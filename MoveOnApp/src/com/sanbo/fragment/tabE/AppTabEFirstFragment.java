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

package com.sanbo.fragment.tabE;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.TimeZone;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.gms.maps.model.LatLng;
import com.sanbo.adapter.ListViewAdapterBikeStation;
import com.sanbo.datamodel.Itinerary;
import com.sanbo.datamodel.PublicTransport;
import com.sanbo.datamodel.Station;
import com.sanbo.enumerated.TypeTransport;
import com.sanbo.fragment.base.BaseFragment;
import com.sanbo.image.Image;
import com.sanbo.moveonapp.MainActivity;
import com.sanbo.moveonapp.R;
import com.sanbo.notifier.MoveOnCroutonStyle;
import com.sanbo.sinchronizable.SynchronizableActivity;
import com.sanbo.synchronizers.BiciPalmaNetworkSynchronizer;
import com.sanbo.synchronizers.LocationSynchronizer;
import com.sanbo.utils.Config;
import com.sanbo.utils.NetworkInformation;
import com.sanbo.utils.SavingState;
import com.sanbo.utils.TimeFormater;
import com.sanbo.utils.TimeUtils;

import de.keyboardsurfer.android.widget.crouton.Crouton;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;



public class AppTabEFirstFragment extends BaseFragment implements SynchronizableActivity {
	@SuppressWarnings("unused")
	private static final String TAG = "AppTabE";
	// update time for bike stations
	private static final long UPDATE_TIME = 600000;

	public static final int INVALID_ROW_ID = -1;
	// to retrieve my location and set distance to station from the user
	public static final boolean closeStation = true;
	
	private ListView mListView;
	private ProgressBar myProgressBar;
	private TextView myShowMessage;
	private TextView isEmpty;
	private Context mContext;

	// Data
	private PublicTransport myPublic = PublicTransport.getInstance();
	private SavingState mySave;
	// user Location
    private LocationSynchronizer mLocationSynchronizer;
	private Location myLocation;

	// Synchronization
	private BiciPalmaNetworkSynchronizer synchronizer;
	// Network Data
	private NetworkInformation network;
	
	// View to show data from BikeStation
	private ListViewAdapterBikeStation adapter;
	private TextView myNameLine1;
	private TextView myNameLine2;
	private Itinerary myItinerary;
	private ArrayList<Station> myListBikeStation = null;
	private static Long myLastTimeStampDB = 0L;
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (Config.DEBUG) Log.d(Config.LOGTAG, "AppTabEFirstFragment.onCreate(Bundle savedInstanceState)");
		mySave = SavingState.getInstance();
		mContext = getSherlockActivity();
		mActivity = getMyActivity();
		mySave.setClosestStation(true);
		if (mySave.getMyItineraryBike() == null){
			mySave.setMyItineraryBike(myPublic.getFirstItineraryBiciPalma());
		}
		myItinerary = mySave.getMyItineraryBike();

		// Retrieve user location
        mLocationSynchronizer = LocationSynchronizer.getInstance(this);
		myLocation = mLocationSynchronizer.getLocation();
		network = NetworkInformation.getInstance();
		network.setLastUpdateTimeCityBik(myPublic.getTimeStamp(TypeTransport.BICIPALMA,null));
		if (myLocation != null){
			network.setMyLocation(new LatLng(myLocation.getLatitude(), 
					myLocation.getLongitude()));
		} else {
        	MoveOnCroutonStyle.croutonAlert( mActivity, R.string.error_location_NETWORK);
		}
		
		setHasOptionsMenu(true);
		
		// get Bike Station from Network
		synchronizer = BiciPalmaNetworkSynchronizer.getInstance(this);
		checkUpdate();
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "AppTabCSecondFragment.onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)");
	    View view       =   inflater.inflate(R.layout.line_list, container, false);
	    
		mListView = (ListView) view.findViewById(R.id.list);
		// divider for elements
		mListView.setDivider(mContext.getResources().getDrawable(R.drawable.line));

		myProgressBar = (ProgressBar) view.findViewById(R.id.progress);
		myShowMessage = (TextView) view.findViewById(R.id.showmessage);
		// tittle of the list
		TextView myTitle = (TextView) view.findViewById (R.id.name_list);
		
		// Line Image
		ImageView myLineImage = (ImageView) view.findViewById(R.id.lineimage);
		
		// Data of the Line1 we show the TypeTransport
		myNameLine1 = (TextView) view.findViewById(R.id.nameLine1);
		
		// Data of Line2 we show TimeStamp
		myNameLine2 = (TextView) view.findViewById(R.id.nameLine2);
		
		myNameLine2.setText(R.string.retrieved);
		//myNameLine2.setTextSize(mContext.getResources().getDimension(R.dimen.text_10));
		
		ImageView myTypeTransport = (ImageView) view.findViewById(R.id.typeTransport);
		// Not used on this list
		ImageView myPMR = (ImageView) view.findViewById(R.id.pmrimage);
		// BICIPALMA is not ready for PMR
		myPMR.setVisibility(View.GONE);
		// Line image from Itinerary
		myLineImage.setImageResource(Image.getImageLine(mContext, myItinerary));
		// TypeTransport Image
		myTypeTransport.setImageResource(Image.getImageTransport(mContext, 
				TypeTransport.getTypeTransport(TypeTransport.BICIPALMA.getValue())));
		
		// Data of Line Image we show the Itinerary Image
		myLineImage.setImageResource(Image.getImageLine(mContext, myItinerary));

		// Name TypeTransport
		myNameLine1.setText(getResources().getStringArray(R.array.type_of_transport)[TypeTransport.BICIPALMA.getValue()]);
		// TimeStamp
		if (network.getLastUpdateTimeCityBik() == 0){
			myNameLine2.setText(TimeUtils.getTimeStampToString(myPublic.getTimeStamp(TypeTransport.BICIPALMA, null), 
					Config.MyTimeZone, mContext, true));
		} else {
			myNameLine2.setText(TimeUtils.getTimeStampToString(
					network.getLastUpdateTimeCityBik(), Config.MyTimeZone, mContext, false));
		}
				
		// value of tittle
		myTitle.setText(R.string.listStations);
		
		isEmpty = (TextView) view.findViewById (R.id.empty);

		//Execute asynctask (we can send string array)
		new BackgroundTask(myNameLine2).execute("SOMETHING");

	    return view;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
		if (Config.DEBUG) Log.d(Config.LOGTAG, "AppTabCFirstFragment.onSaveInstanceState(Bundle outState)");
	    //Our state is saved on PublicTransport and in our DDBB
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	} 
	
	@Override
	public void onResume() {
		checkUpdate();
	    super.onResume();
	}
	
	private void checkUpdate() {		
		long now = Calendar.getInstance(TimeZone.getTimeZone(Config.MyTimeZone)).getTimeInMillis();
		long lastUpdated = now - network.getLastUpdateTimeCityBik();
		// force refresh every 10' to save battery
		if (network.getBikeStationNetwork() == null || (lastUpdated > UPDATE_TIME)) {
			// clear data and wait response from network
			network.setLastUpdateTimeCityBik(0);
			if (synchronizer == null) synchronizer = BiciPalmaNetworkSynchronizer.getInstance(this);
	        synchronizer.synchronize(this);
		} else if ((lastUpdated/1000) % 60 > 0) {
			MoveOnCroutonStyle.croutonConfirm(getSherlockActivity(), 
					TimeFormater.formatLastUpdated(lastUpdated, mContext));
		}
	}
	
	private class BackgroundTask extends AsyncTask<String, Void, String> {
		// What we need in temp loadar
		private TextView myRetrieved;		
		
		public BackgroundTask(TextView myText){
			myRetrieved = myText;
		}
	  
		@Override
		protected void onPreExecute()
		{
			//showing progressbar
			myProgressBar.setVisibility(View.VISIBLE);
			myShowMessage.setVisibility(View.VISIBLE);
			// Retrieve user location
			myLocation = LocationSynchronizer.getInstance(mActivity).getLocation();
			// saving myLocation to NetworkInformation
			if (myLocation != null){
				network.setMyLocation(new LatLng(myLocation.getLatitude(), 
						myLocation.getLongitude()));
			}
			
		}
	
		@Override
		protected String doInBackground(String... urls) 
		{
	 	    try
			{
	 			// Retrieving data from DB
	 	    	myListBikeStation = myPublic.getAllBikeStations();	 	    	
		 	    if (network.getMyLocation() != null){
		 	    	Iterator<Station> sta = myListBikeStation.iterator();
		 	    	while (sta.hasNext()){
		 	    		Station st = sta.next();
						float[] distance = new float[1];
	 					Location.distanceBetween(st.getGeoPosition().getLatitudeMoveOn(),
		 							                 st.getGeoPosition().getLongitudeMoveOn(),
		 											 network.getMyLocation().latitude,
		 		                                     network.getMyLocation().longitude, distance);
	 					st.setDistance(distance[0]);    		
		 	    	}
		 	    }
	 	    	// Order by Distance - Name - Code :)
	 	    	Collections.sort(myListBikeStation, Station.DistanceComparator);
	
		    	//our task
		    	adapter = new ListViewAdapterBikeStation(mContext, mListView, myListBikeStation);
				
			} catch (Exception e)
			{
				Log.e(MainActivity.class.toString(), e.getMessage());
			}
				//on post execute we can send an String	
				return "moveOn";
		 }
	
		@Override
		 protected void onPostExecute(String result) 
		 {
		     //Asyntask is finished, we hide progressbar
			 myProgressBar.setVisibility(View.GONE);
			 myShowMessage.setVisibility(View.GONE);
			 
			 actualizeTimeStamp(myRetrieved, network.getLastUpdateTimeCityBik());
			 myLastTimeStampDB = network.getLastUpdateTimeCityBik();
			 //now we can show our list or simply working
			 if (adapter != null && adapter.getCount() > 0)
			 {
				 mListView.setAdapter(adapter);
				 //show list
				 mListView.setVisibility(View.VISIBLE);
				 //unshow empty message
				 isEmpty.setVisibility(View.GONE);
				 mListView.clearFocus();
				 // Show item selected
				 if (mySave.getItemBikeStation() != INVALID_ROW_ID){
					 mListView.setSelection(mySave.getItemBikeStation());				 
				 }
			 }
			 else if (adapter != null){
			 	//show empty message
			 	isEmpty.setVisibility(View.VISIBLE);
			 	if (mySave.isFavoriteOn()){
			 		isEmpty.setText(R.string.emptyFavoriteList);
			 	}
			 }
			 //if not, we show an error
			 else
			 {
				 	//show empty message
				 	isEmpty.setVisibility(View.VISIBLE);
				 	//dialog error database
				 	Builder builder = new Builder(mContext);
					builder.setTitle(R.string.title_error);
					builder.setIcon(android.R.drawable.ic_dialog_info);
					builder.setMessage(R.string.error_database);					
					builder.setNeutralButton(getString(R.string.button_ok), null);			

					AlertDialog alertDialog = builder.create();
					alertDialog.show();
					alertDialog.setCancelable(false);
			 }
		  }
	}
	
	@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	    inflater.inflate(R.menu.refresh, menu);
    	synchronized(this){
    		if (myPublic.getmPreferences().isOptionShowTransfer()){ 
    			menu.findItem(R.id.menu_settings).setTitle(R.string.menu_transfer_off);
    		} else {
    			menu.findItem(R.id.menu_settings).setTitle(R.string.menu_transfer_on);
    		}
    	}
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	    if (Config.DEBUG) Log.i(Config.LOGTAG, "IN " + String.valueOf(item.getItemId()));
    	switch (item.getItemId()) {

    	case android.R.id.home:
	    	mySave.setItemBikeStation(INVALID_ROW_ID);
	    	mySave.setMyBikeStation(null);
	    	mySave.setClosestStation(false);
	    	return true;

	    case R.id.menu_refresh:	
		    if (Config.DEBUG) Log.i(Config.LOGTAG, "OUT Menu_refresh");
	        // icono de la aplicación pulsado en la barra de acción; One Way / Return
	        if (Config.DEBUG) Toast.makeText(mContext, getResources().getString(R.string.action_refresh_show), Toast.LENGTH_SHORT).show();
	        //actualizeList(true);
			myProgressBar.setVisibility(View.VISIBLE);
			myShowMessage.setVisibility(View.VISIBLE);
			myProgressBar.invalidate();
			myShowMessage.invalidate();
			// getting data from CityBik
	        BiciPalmaNetworkSynchronizer synchronizer =
	        		BiciPalmaNetworkSynchronizer.getInstance(this);
	        synchronizer.synchronize(this);
			return true;

	    case R.id.menu_settings:
	    	synchronized(this){
	    		final boolean showTransfer = myPublic.getmPreferences().isOptionShowTransfer();
	    		myPublic.getmPreferences().setOptionShowTransfer(!showTransfer);
	    		// updating...
	    		// here we force to refresh our current tab
	    		getSherlockActivity().supportInvalidateOptionsMenu();
	    		adapter.notifyDataSetChanged();
    		}	    	
			return true;
	    	
	    default:
		    if (Config.DEBUG) Log.i(Config.LOGTAG, "OUT DEF " +String.valueOf(item.getItemId()));
	        return super.onOptionsItemSelected(item);
	    }
    	//return true;
    }

    @Override
    public void onDestroy() {
 		if (Config.DEBUG) Log.d(Config.LOGTAG, "AppTabEFirstFragment.onDestroy()");
        //saving to database
    	if (BiciPalmaNetworkSynchronizer.getInstance(this).storeToDB())MoveOnCroutonStyle.croutonInfo(getSherlockActivity(), R.string.SavingData);
		LocationSynchronizer.getInstance(this).detachSynchronizableElement(this);
		BiciPalmaNetworkSynchronizer.getInstance(this).detachSynchronizableActivity(this);
	    Crouton.clearCroutonsForActivity(getSherlockActivity());
        super.onDestroy();
   }
    
	@Override
	public void onSuccessfulNetworkSynchronization() {
		// TODO Auto-generated method stub
		actualizeList(true);
		MoveOnCroutonStyle.croutonInfo(getSherlockActivity(), R.string.refresh_succesful);
	}

	@Override
	public void onUnsuccessfulNetworkSynchronization() {
		// TODO Auto-generated method stub
		actualizeList(false);
		MoveOnCroutonStyle.croutonAlert(getSherlockActivity(), R.string.connectivity_error);
	}

	@Override
	public void onLocationSynchronization() {
		// TODO Auto-generated method stub
	}

	@Override
	public SherlockFragmentActivity getSynchronizableActivity() {
		// TODO Auto-generated method stub
		return mActivity;
	}
	
	protected void actualizeTimeStamp(TextView myRetrieved, Long timeStamp){
		// TimeStamp
		myRetrieved.setText(TimeUtils.getTimeStampToString(timeStamp, Config.MyTimeZone, mContext, false));
		myRetrieved.invalidate();
	}
	
	public void actualizeList(boolean force){
		if (adapter != null){
			if ((network.getLastUpdateTimeCityBik()- myLastTimeStampDB > UPDATE_TIME) || force){
				myLastTimeStampDB = network.getLastUpdateTimeCityBik();
				// TimeStamp
				actualizeTimeStamp(myNameLine2, myLastTimeStampDB);
				myListBikeStation = myPublic.getAllBikeStations();
				// Actualize list
				getSherlockActivity().supportInvalidateOptionsMenu();
				adapter.notifyDataSetChanged();
				myProgressBar.setVisibility(View.GONE);
				myShowMessage.setVisibility(View.GONE);
				myProgressBar.invalidate();
				myShowMessage.invalidate();

			}
		} else {
			//Execute asynctask (we can send string array)
			new BackgroundTask(myNameLine2).execute("SOMETHING");
		}
	}

 
}


