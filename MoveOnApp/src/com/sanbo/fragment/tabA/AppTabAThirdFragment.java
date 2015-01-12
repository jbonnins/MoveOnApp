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

package com.sanbo.fragment.tabA;


import java.util.Calendar;
import java.util.Collections;
import java.util.TimeZone;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.gms.maps.model.LatLng;
import com.sanbo.adapter.ListViewAdapterNextBus;
import com.sanbo.datamodel.BusStation;
import com.sanbo.datamodel.PublicTransport;
import com.sanbo.datamodel.Station;
import com.sanbo.enumerated.TypeTransport;
import com.sanbo.fragment.base.BaseFragment;
import com.sanbo.image.Image;
import com.sanbo.moveonapp.R;
import com.sanbo.notifier.MoveOnCroutonStyle;
import com.sanbo.sinchronizable.SynchronizableActivity;
import com.sanbo.synchronizers.EMTNetworkSynchronizer;
import com.sanbo.synchronizers.LocationSynchronizer;
import com.sanbo.utils.Config;
import com.sanbo.utils.NetworkInformation;
import com.sanbo.utils.SavingState;
import com.sanbo.utils.TimeFormater;
import com.sanbo.utils.TimeUtils;
import com.sanbo.utils.Utils;

import de.keyboardsurfer.android.widget.crouton.Crouton;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class AppTabAThirdFragment extends BaseFragment implements SynchronizableActivity {
	@SuppressWarnings("unused")
	private static final String TAG = "AppTabA";
	
	// update time for bike stations
	private static final long UPDATE_TIME = 600000;

	public static final int INVALID_ROW_ID = -1;
	// to retrieve my location and set distance to station from the user
	public static final boolean closeStation = false;
	
	// View to show data from BusStation
	private ListViewAdapterNextBus adapter;
	private ListView mListView;
	private ProgressBar myProgressBar;
	private TextView myShowMessage;
	private TextView isEmpty;
	private Context mContext;

	// Data
	private PublicTransport myPublic;
	private SavingState mySave;
    // user Location
    private LocationSynchronizer mLocationSynchronizer;
	private Location myLocation;
	
	// Synchronization
	private EMTNetworkSynchronizer synchronizer;
	// Network Data
	private NetworkInformation network;

	//BusStationLayout
	//Data Station selected
	//private LinearLayout groupItem;
		private TextView retrievedtime;
		//private RelativeLayout RelativeLayoutStation;
			private CheckBox check_favorite_station;
			private ImageView pmrimage;
			private TextView station_code_name;
			//private RelativeLayout station_code_name2;
		    	private TextView station_name;
				private TextView meter_num;
	//private RelativeLayout relativeLayout1;
		private ImageView lineimage;
		private TextView nameLine1;
		private TextView firstbus;
		private TextView secondbus;
		private TextView nameLine2;
	//private ImageView imageView1; // line separator
	//private RelativeLayout RelativeLayout3;
	private ImageView image_typetransport;
		private TextView text_info1;
	
	//when select 	
	private Station myStation;
	//by default first line is selected from estation
	private int itemSelected;
	private static Long myLastTimeStampDB = 0L;
	private boolean showData = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (Config.DEBUG) Log.d(Config.LOGTAG, "AppTabAThirdFragment.onCreate(Bundle savedInstanceState)");
		//references to PublicTransport and SavingState
		mySave = SavingState.getInstance();
		myPublic = PublicTransport.getInstance();
		
		mySave.setMyFragment(this);

		mContext = getSherlockActivity();
		mActivity = getMyActivity();
		//my selection
		myStation = mySave.getMyActiveStation();
		// at first INVALID_ROW_ID is byDefault changed to 0, first line at the bus stop
		itemSelected = mySave.getMyActiveBusStationSelected();
		// Retrieve user location
        mLocationSynchronizer = LocationSynchronizer.getInstance(this);
		myLocation = mLocationSynchronizer.getLocation();

		setHasOptionsMenu(true);
		
		network = NetworkInformation.getInstance();
		// if we don't have previous data from network, timeStamp is now
		if (myPublic.getTimeStamp(TypeTransport.BUSPALMA, myStation) == null){
			myLastTimeStampDB = 0L;
		}
		network.setLastUpdateTimeEMT(myLastTimeStampDB);

		// get Bus Station from Network
		synchronizer = EMTNetworkSynchronizer.getInstance(this);
		checkUpdate();	
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "AppTabAThirdFragment.onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)");
		// by default show first at top, by default First busStation selected 
		if (itemSelected == INVALID_ROW_ID) itemSelected = 0;
		
		View view       =   inflater.inflate(R.layout.station_nextbus, container, false);
	    
		mListView = (ListView) view.findViewById(R.id.list);
		// divider for elements
		mListView.setDivider(mContext.getResources().getDrawable(R.drawable.line));
		// progress bar indeterminate 
		myProgressBar = (ProgressBar) view.findViewById(R.id.progress);
		myShowMessage = (TextView) view.findViewById(R.id.showmessage);
		// tittle of the list
		TextView myTitle = (TextView) view.findViewById (R.id.name_list);
		//private LinearLayout groupItem;
		//Retrieved time information
		retrievedtime = (TextView) view.findViewById(R.id.retrievedtime);
		//private RelativeLayout RelativeLayoutStation;
		//Station Favorite
		check_favorite_station = (CheckBox) view.findViewById(R.id.check_favorite_station);
		//check_favorite_station.setTag(myStation);
	    check_favorite_station.setTag(myStation);
		check_favorite_station.setOnClickListener(new OnClickListener() {
			  @Override
			  public void onClick(View v) {
		                //is chkIos checked?
				  CheckBox cb = (CheckBox) v;
				  Station st = (Station) v.getTag();
				  if (cb.isChecked()){
					  savingDataToPublicTransport(st, true);
				  }else{ 
					  savingDataToPublicTransport(st, false);
				  }
			  }

		});	
		//PMR Station Facilities
		pmrimage = (ImageView) view.findViewById(R.id.pmrimage);
		// Station code --> String not only numeric
		station_code_name = (TextView) view.findViewById(R.id.station_code_name);
		//private RelativeLayout station_code_name2;
		// Station name
		station_name = (TextView) view.findViewById(R.id.station_name);
		//Distance to Station
		meter_num = (TextView) view.findViewById(R.id.meter_num);

		//relativeLayout1 = (RelativeLayout) view.findViewById(R.id.RelativeLayout1);
		//Line image of first option, by default the first item of the list
		lineimage = (ImageView) view.findViewById(R.id.lineimage);
		//Short name of the line POWERED BY EMT service
		nameLine1 = (TextView) view.findViewById(R.id.nameLine1);
		//Time in minutes and seconds to the first bus
		firstbus = (TextView) view.findViewById(R.id.firstbus);
		//Time in minutes and seconds to the second bus
		secondbus = (TextView) view.findViewById(R.id.secondbus);
		//Full name of the line FROM TO
		nameLine2 = (TextView) view.findViewById(R.id.nameLine2);
		//private ImageView imageView1; // line separator
		//private RelativeLayout RelativeLayout3;
		//Type of transport (in our model we have ONLY one TypeTransport per station
		image_typetransport = (ImageView) view.findViewById(R.id.image_typetransport);
		//Indicates if the information is from Schedule (with MoveOn approximation)
		//Indicates if the information is from EMT service
		text_info1 = (TextView) view.findViewById(R.id.text_info1);

		isEmpty = (TextView) view.findViewById (R.id.empty);

		//Filling Data
		if (myStation.getTimeStamp() != null){
			retrievedtime.setText(TimeUtils.getTimeStampToString(myStation.getTimeStamp().getTimeInMillis(), 
				Config.MyTimeZone, mContext, true));
		} else {
			retrievedtime.setText(TimeUtils.getTimeStampToString(
					Calendar.getInstance(TimeZone.getTimeZone(Config.MyTimeZone)).getTimeInMillis(), 
					Config.MyTimeZone, mContext, true));
		}
	    //Station to favorite
    	check_favorite_station.setChecked(myStation.isFavorite());
		//PMR Station Facilities
		if (myStation.isAdapted_for_Disabled()){
			pmrimage.setVisibility(View.VISIBLE);			
		} else {
			pmrimage.setVisibility(View.GONE);						
		}
		// Station code --> String not only numeric
		station_code_name.setText(myStation.getCode());
		//	Station name
		station_name.setText(myStation.getName());
		//Distance to Station
		if (myLocation != null){
			float[] distance = new float[1];
			Location.distanceBetween(myStation.getGeoPosition().getLatitudeMoveOn(),
					myStation.getGeoPosition().getLongitudeMoveOn(),
					myLocation.getLatitude(), myLocation.getLongitude(), distance);
			myStation.setDistance(distance[0]);    		
			meter_num.setText(Utils.getStationDistance(myStation.getDistance()));	    
		} else {
			meter_num.setText(mContext.getResources().getString(R.string.bydefault0));
		}
		//Line image of first option, by default the first item of the list
		if (myStation.getBusStations() != null){
			lineimage.setImageResource(Image.getImageItinerary(mContext, 
				myStation.getBusStations().get(itemSelected).getItinerary()));
			//Short name of the line POWERED BY EMT service
			nameLine1.setText(myStation.getBusStations().get(itemSelected).getFulName(
					myStation.getBusStations().get(itemSelected).getDestination())[0]);
			//Time in minutes and seconds to the first bus
			if (myStation.getBusStations().get(itemSelected).getFirstBus() != null) {
				long myTime = myStation.getBusStations().get(itemSelected).getFirstBus().getTimeInMillis()
						- myStation.getTimeStamp().getTimeInMillis();
				if (myTime < 0) myTime = 0L;
				firstbus.setText(mContext.getResources().getString(R.string.first_bus) + " " + Utils.getTimeFormated(myTime));
			} else {
				firstbus.setText(mContext.getResources().getString(R.string.first_bus) + " " + 
						mContext.getResources().getString(R.string.bydefault0));
			}
			//Time in minutes and seconds to the second bus
			if (myStation.getBusStations().get(itemSelected).getSecondBus() != null){
				long myTime = myStation.getBusStations().get(itemSelected).getSecondBus().getTimeInMillis()
						- myStation.getTimeStamp().getTimeInMillis();
				if (myTime < 0) myTime = 0L;
				secondbus.setText(mContext.getResources().getString(R.string.next_bus) + " " + Utils.getTimeFormated(myTime));
			} else {
				secondbus.setText(mContext.getResources().getString(R.string.next_bus) + " " + 
						mContext.getResources().getString(R.string.bydefault0));
			}
			//Full name of the line FROM TO WE DON'T HAVE ENOUGH INFORMATION
			nameLine2.setText(myStation.getBusStations().get(itemSelected).getFulName(
					myStation.getBusStations().get(itemSelected).getDestination())[1]);				
			//Type of transport (in our model we have ONLY one TypeTransport per station
			image_typetransport.setImageResource(Image.getImageTransport(mContext, 
					TypeTransport.getTypeTransport(myStation.getBusStations().get(itemSelected).getItinerary().getTypeOfTransport())));
		}
		//Indicates if the information is from Schedule (with MoveOn approximation)
		if (network.getEmtStationNetwork() != null && myStation.getBusStations() != null && 
				network.getEmtStationNetwork().getBusStations() != null &&
				network.getEmtStationNetwork().getBusStations().size() != myStation.getBusStations().size()){
			//Indicates if the information is from MoveOn TimeTable
			text_info1.setText(mContext.getResources().getString(R.string.poweredEMT) + "\n" + 
					mContext.getResources().getString(R.string.scheduled));
		} else if (network.getEmtStationNetwork() != null && myStation.getBusStations() != null &&
				network.getEmtStationNetwork().getBusStations() != null &&
				network.getEmtStationNetwork().getBusStations().size() == myStation.getBusStations().size()){
					text_info1.setText(mContext.getResources().getString(R.string.poweredEMT));
		} else {
			//Indicates if the information is from EMT service
			text_info1.setText(mContext.getResources().getString(R.string.scheduled));
		}				
		// value of tittle
		if (myStation.getBusStations() != null){
			myTitle.setText(mContext.getResources().getString(R.string.transfer) + " (" + (myStation.getBusStations().size()-1) + ")");
		} else {
			myTitle.setText(mContext.getResources().getString(R.string.transfer) + " (" + mContext.getResources().getString(R.string.bydefault0) + ")");			
		}

		//Execute asyntask (we can send string array)
		//new BackgroundTask(myView, itemSelected, myStation).execute("SOMETHING");

		//showing progressbar
		myProgressBar.setVisibility(View.VISIBLE);
		myShowMessage.setVisibility(View.VISIBLE);

		
		return view;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
		if (Config.DEBUG) Log.d(Config.LOGTAG, "AppTabAThirdFragment.onSaveInstanceState(Bundle outState)");
	    //Our state is saved on PublicTransport and in our DDBB
	}
		
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
 		if (Config.DEBUG) Log.d(Config.LOGTAG, "AppTabAThirdFragment.onRestoreInstanceState(Bundle savedInstanceState)");
	} 

	@Override
	public void onResume() {
	    super.onResume();
 		if (Config.DEBUG) Log.d(Config.LOGTAG, "AppTabAThirdFragment.onResume()");
 		if (adapter != null && showData){
 			// at first INVALID_ROW_ID is byDefault changed to 0, first line at the bus stop
 			itemSelected = mySave.getMyActiveBusStationSelected();
 			try{
 				adapter.showDataSelected(mContext, myStation, itemSelected);
			} catch (Exception e)
			{
				Log.e(AppTabAThirdFragment.class.toString(), e.getMessage());
			}				
 		}
 	}
	
	public View getViewFromFragment(){
		return this.getView();
	}
	
	private void checkUpdate() {		
 		if (Config.DEBUG) Log.d(Config.LOGTAG, "AppTabAThirdFragment.checkUpdate()");
		long now = Calendar.getInstance(TimeZone.getTimeZone(Config.MyTimeZone)).getTimeInMillis();
		long lastUpdated = now - network.getLastUpdateTimeEMT();
		// force refresh every 10' to save battery
		if (network.getEmtStationNetwork() == null || (lastUpdated > UPDATE_TIME)) {
			// clear data and wait response from network
			network.setLastUpdateTimeEMT(0);
			if (synchronizer == null) synchronizer = EMTNetworkSynchronizer.getInstance(this);
	        synchronizer.synchronize(this);
		} else {
			//Execute asynctask (we can send string array)
			new BackgroundTask(this.getView(), myStation).execute("SOMETHING");			
		}
	}
	
	private class BackgroundTask extends AsyncTask<String, Void, String> {
		// What we need in temp loadar
		//private NetworkInformation myNetwork;
		private View myView;
		private Station myStation;
		
		public BackgroundTask(View view, Station mStation){
	 		if (Config.DEBUG) Log.d(Config.LOGTAG, "AppTabAThirdFragment.BackgroundTask(View, itemSelected =" + itemSelected
	 				+ " Station.code =" + mStation.getCode());
	 		myView = view;
			myStation = mStation;
		}
	
		@Override
		protected void onPreExecute()
		{
	 		if (Config.DEBUG) Log.d(Config.LOGTAG, "AppTabAThirdFragment.onPreExecute()");
			//showing progressbar
			 myProgressBar.setVisibility(View.VISIBLE);
			 myShowMessage.setVisibility(View.VISIBLE);
			// Retrieve user location
			Location myLocation = LocationSynchronizer.getInstance(mActivity).getLocation();
			// saving myLocation to NetworkInformation
			if (myLocation != null){
				network.setMyLocation(new LatLng(myLocation.getLatitude(), 
						myLocation.getLongitude()));
			}

		}

		@Override
		protected String doInBackground(String... urls) 
		{
	 		if (Config.DEBUG) Log.d(Config.LOGTAG, "AppTabAThirdFragment.doInBackground(String... urls)");
	 	    try
			{	
				// Retrieving data from DBHelper
	 	    	
	 	    	myStation.setBusStations(myPublic.getListAllBusStations(myStation));
	 	    	
				float[] distance = new float[1];
				if (network.getMyLocation() != null){
					Location.distanceBetween(myStation.getGeoPosition().getLatitudeMoveOn(),
	 				myStation.getGeoPosition().getLongitudeMoveOn(),
	 					network.getMyLocation().latitude,
	 		            network.getMyLocation().longitude, distance);
					myStation.setDistance(distance[0]);
				}
	 	    	// Order by FirstBus (Time) - Code :)
				if (myStation.getBusStations() != null){
					Collections.sort(myStation.getBusStations(), BusStation.TimeCodeComparator);
				}
		 			
		    	//our task
		    	adapter = new ListViewAdapterNextBus(mContext, mListView, myStation);
		    	adapter.setViewFromFragment(myView);
	
			} catch (Exception e)
			{
				Log.e(AppTabAThirdFragment.class.toString(), e.getMessage());
			}
				//on post execute we can send an String	
				return "moveOn Asynctask";
		 }
		
		@Override
		 protected void onPostExecute(String result){
	 		if (Config.DEBUG) Log.d(Config.LOGTAG, "AppTabAThirdFragment.onPostExecute(" + result);
		     //Asyntask is finished, we hide progressbar
			 myProgressBar.setVisibility(View.GONE);
			 myShowMessage.setVisibility(View.GONE);
			 
			 myLastTimeStampDB = network.getLastUpdateTimeEMT();
			 
			 //now we can show our list or simply working
			 if (adapter != null && adapter.getCount() >= 0)
			 {
				 mListView.setAdapter(adapter);
				 //show list
				 mListView.setVisibility(View.VISIBLE);
				 //unshow empty message
				 isEmpty.setVisibility(View.GONE);
				 mListView.clearFocus();
				 // Show item selected
		 	    try
				{	

		 	    	adapter.updateResults(mContext, myStation, itemSelected);
			 	} catch (Exception e)
				{
					Log.e(AppTabAThirdFragment.class.toString(), e.getMessage());
				}
				long now = Calendar.getInstance(TimeZone.getTimeZone(Config.MyTimeZone)).getTimeInMillis();
				long lastUpdated = now - network.getLastUpdateTimeEMT();
				if (!(network.getEmtStationNetwork() == null || (lastUpdated > UPDATE_TIME))) {
					if ((lastUpdated/1000) % 60 > 0) {
						try{
							MoveOnCroutonStyle.croutonConfirm(mActivity, 
								TimeFormater.formatLastUpdated(network.getLastUpdateTimeEMT(), mContext));
						} catch (NullPointerException e)
						{
							Log.e(AppTabAThirdFragment.class.toString(), e.getMessage());
						}
					}
				}
			 }
			 //if not, we show an error
			 else
			 {
				 //show empty message
				 isEmpty.setVisibility(View.VISIBLE);
			 }
		  }
	}
		
	@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
 		if (Config.DEBUG) Log.d(Config.LOGTAG, "AppTabAThirdFragment.onCreateOptionsMenu(Menu menu, MenuInflater inflater)");
	    inflater.inflate(R.menu.refresh, menu);
    	synchronized(this){
    		// settings is not enables, we don't show transfer by default
    		menu.findItem(R.id.menu_settings).setEnabled(false);
    	}
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	    if (Config.DEBUG) Log.i(Config.LOGTAG, "IN " + String.valueOf(item.getItemId()));
    	switch (item.getItemId()) {

    	case android.R.id.home:
    		mySave.setMyActiveBusStationSelected(INVALID_ROW_ID);
    		mySave.setMyActiveStation(null);
	    	mySave.setClosestStation(false);
 	    	return true;
 	    	
    	case R.id.menu_refresh:	
		    if (Config.DEBUG) Log.i(Config.LOGTAG, "OUT Menu_refresh");
	        // icono de la aplicación pulsado en la barra de acción; One Way / Return
	        if (Config.DEBUG) Toast.makeText(mContext, getResources().getString(R.string.action_refresh_show), Toast.LENGTH_SHORT).show();	        
	    	myProgressBar.setVisibility(View.VISIBLE);
	    	myShowMessage.setVisibility(View.VISIBLE);
	    	myProgressBar.invalidate();
	    	myShowMessage.invalidate();
	    	// getting data from EMT
	        EMTNetworkSynchronizer synchronizer =
	        		EMTNetworkSynchronizer.getInstance(this);
	        synchronizer.synchronize(this);
	    	return true;
	    	
	    case R.id.menu_settings:
	    	synchronized(this){
	    		final boolean showTransfer = myPublic.getmPreferences().isOptionShowTransfer();
	    		myPublic.getmPreferences().setOptionShowTransfer(!showTransfer);
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
        super.onDestroy();
        if (Config.DEBUG) Log.d(Config.LOGTAG, "AppTabAThirdFragment.onDestroy()");
        //saving to database
    	if (EMTNetworkSynchronizer.getInstance(this).storeToDB(myStation))MoveOnCroutonStyle.croutonInfo(getSherlockActivity(), R.string.SavingData);
		LocationSynchronizer.getInstance(this).detachSynchronizableElement(this);
		EMTNetworkSynchronizer.getInstance(this).detachSynchronizableActivity(this);
		network.setEmtStationNetwork(null);
		network.setLastUpdateTimeEMT(0);
	    Crouton.clearCroutonsForActivity(getSherlockActivity());
   }
    
	@Override
	public void onSuccessfulNetworkSynchronization() {
 		if (Config.DEBUG) Log.d(Config.LOGTAG, "AppTabAThirdFragment.onSuccessfulNetworkSynchronization()");
		// TODO Auto-generated method stub	
		actualizeList(true);
		MoveOnCroutonStyle.croutonConfirm(getSherlockActivity(), R.string.refresh_succesful);
	}

	@Override
	public void onUnsuccessfulNetworkSynchronization() {
 		if (Config.DEBUG) Log.d(Config.LOGTAG, "AppTabAThirdFragment.onUnsuccessfulNetworkSynchronization()");
		// TODO Auto-generated method stub
		actualizeList(false);
		MoveOnCroutonStyle.croutonAlert(getSherlockActivity(), R.string.connectivity_error);
	}
   
	@Override
	public void onLocationSynchronization() {
 		if (Config.DEBUG) Log.d(Config.LOGTAG, "AppTabAThirdFragment.onLocationSynchronization()");
		// TODO Auto-generated method stub
	}
    	
	@Override
	public SherlockFragmentActivity getSynchronizableActivity() {
 		if (Config.DEBUG) Log.d(Config.LOGTAG, "AppTabAThirdFragment.getSynchronizableActivity()");
		// TODO Auto-generated method stub
		return mActivity;
	}

	
 	private void savingDataToPublicTransport(Station myStation, boolean isFavorite){
 		if (Config.DEBUG) Log.d(Config.LOGTAG, "AppTabAThirdFragment.savingDataToPublicTransport(Station.code =" + myStation.getCode()
 				+ " isFavorite " + Boolean.toString(isFavorite));
    	//nothing to save, all info is in PublicTransport
    	if (myPublic.setIsFavoriteStation(myStation, isFavorite)){
     		if (isFavorite){
	  			  MoveOnCroutonStyle.croutonConfirm(mActivity, mContext.getResources().getString(R.string.checked) +
	  					" " + mContext.getResources().getString(R.string.station) + " " + myStation.getCode());
     		} else {
    			  MoveOnCroutonStyle.croutonInfo(mActivity, mContext.getResources().getString(R.string.unchecked) +
    					" " + mContext.getResources().getString(R.string.station) + " " + myStation.getCode());
     		}
        } else {
    		  MoveOnCroutonStyle.croutonWarn(mActivity, mContext.getResources().getString(R.string.error_database));    		
        }
    	
    }
	
	public void actualizeList(boolean force){
 		if (Config.DEBUG) Log.d(Config.LOGTAG, "AppTabAThirdFragment.actualizeList(" + Boolean.toString(force));
		if (adapter != null){
			if ((network.getLastUpdateTimeEMT() - myLastTimeStampDB > UPDATE_TIME) || force){
				myLastTimeStampDB = network.getLastUpdateTimeEMT();
				Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(Config.MyTimeZone));
				cal.setTimeInMillis(myLastTimeStampDB);
				myStation.setTimeStamp(cal);
				// TimeStamp
				myStation.setBusStations(myPublic.getListAllBusStations(myStation));
				// Actualize list
				getSherlockActivity().supportInvalidateOptionsMenu();
				myProgressBar.setVisibility(View.GONE);
				myShowMessage.setVisibility(View.GONE);
				myProgressBar.invalidate();
				myShowMessage.invalidate();
				adapter.showDataSelected(mContext, myStation, itemSelected);
				adapter.notifyDataSetChanged();

			}
		} else {
			//Execute asynctask (we can send string array)
			new BackgroundTask(this.getView(), myStation).execute("SOMETHING");
		}
	}

 
}

