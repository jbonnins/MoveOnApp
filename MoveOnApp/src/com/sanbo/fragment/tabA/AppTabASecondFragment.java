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

import java.util.ArrayList;
import java.util.Iterator;

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

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.gms.maps.model.LatLng;
import com.sanbo.adapter.ListViewAdapterLine;
import com.sanbo.datamodel.Itinerary;
import com.sanbo.datamodel.Line;
import com.sanbo.datamodel.PublicTransport;
import com.sanbo.datamodel.Station;
import com.sanbo.enumerated.TypeTransport;
import com.sanbo.fragment.base.BaseFragment;
import com.sanbo.image.Image;
import com.sanbo.moveonapp.R;
import com.sanbo.sinchronizable.SynchronizableActivity;
import com.sanbo.synchronizers.LocationSynchronizer;
import com.sanbo.utils.Config;
import com.sanbo.utils.NetworkInformation;
import com.sanbo.utils.SavingState;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;


public class AppTabASecondFragment extends BaseFragment implements SynchronizableActivity {
	@SuppressWarnings("unused")
	private static final String TAG = "AppTabA";

	public static final int INVALID_ROW_ID = -1;

	private ListView mListView;
	private ProgressBar myProgressBar;
	private TextView myShowMessage;
	private TextView isEmpty;
	private Context mContext;
	private ListViewAdapterLine adapter;
	private ImageView myLineImage;
	private TextView myNameLine1;
	private TextView myNameLine2;
	private static boolean oneWay = true; 
	private Line myLine;
	private Itinerary myItinerary;
	private ArrayList<Station> myListLineStation = null;
	
	private PublicTransport myPublic = PublicTransport.getInstance();

	private SavingState mySave = SavingState.getInstance();
	
    // user Location
    private LocationSynchronizer mLocationSynchronizer;
	private Location myLocation;

	// Network Data
	private NetworkInformation network;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (Config.DEBUG) Log.d(Config.LOGTAG, "AppTabCSecondFragment.onCreate(Bundle savedInstanceState)");
		mContext = this.getSherlockActivity();
		myLine = mySave.getMyLine();
		myItinerary = mySave.getMyItinerary();
		mLocationSynchronizer = LocationSynchronizer.getInstance(this);
		// Retrieve user location
		myLocation = mLocationSynchronizer.getLocation();

		network = NetworkInformation.getInstance();
		// saving myLocation to NetworkInformation
		if (myLocation != null){
			network.setMyLocation(new LatLng(myLocation.getLatitude(), 
					myLocation.getLongitude()));
		}
		setHasOptionsMenu(true);
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
		TextView myTittle = (TextView) view.findViewById (R.id.name_list);
		
		// Data of the Line
		myLineImage = (ImageView) view.findViewById(R.id.lineimage);
		myNameLine1 = (TextView) view.findViewById(R.id.nameLine1);
		myNameLine2 = (TextView) view.findViewById(R.id.nameLine2);
		ImageView myTypeTransport = (ImageView) view.findViewById(R.id.typeTransport);
		ImageView myPMR = (ImageView) view.findViewById(R.id.pmrimage);
		
		myLineImage.setImageResource(Image.getImageItinerary(mContext, myItinerary));

		myTypeTransport.setImageResource(Image.getImageTransport(mContext, 
				TypeTransport.getTypeTransport(mySave.getMyItinerary().getTypeOfTransport())));
		if (mySave.getMyItinerary().isDisabledFacilities()){
			myPMR.setVisibility(View.VISIBLE);
		}else{
			myPMR.setVisibility(View.GONE);
		}
		
		if (oneWay){
			myNameLine1.setText(myLine.getUNameOneWay());
			myNameLine2.setText(myLine.getUNameOneWay2());			
		}else{
			myNameLine1.setText(myLine.getUNameReturn());
			myNameLine2.setText(myLine.getUNameReturn2());
		}

		
		// value of tittle
		myTittle.setText(R.string.listStations);
		
		isEmpty = (TextView) view.findViewById (R.id.empty);

		//Execute asyntask (we can send string array)
		new BackgroundTask().execute("SOMETHING");
		
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
		mySave.setClosestStation(false);
    } 
    
    @Override
	public void onResume() {
        super.onResume();
		mySave.setClosestStation(false);
    }

	private class BackgroundTask extends AsyncTask<String, Void, String> {
		// What we need in temp loadar
		PublicTransport myPublic = PublicTransport.getInstance();
	  
	@Override
	protected void onPreExecute()
	{
		  //showing progressbar
		  myProgressBar.setVisibility(View.VISIBLE);
		  myShowMessage.setVisibility(View.VISIBLE);


	}
	
	@Override
	protected String doInBackground(String... urls) 
	{			
	    try
		{
	    	// we get data in Line throw PublicTransport
	    	myListLineStation = myPublic.getAllStationByLine(myLine, oneWay);
			if (myLocation != null){
				network.setMyLocation(new LatLng(myLocation.getLatitude(), 
						myLocation.getLongitude()));
			}
	    	
	    	if (network.getMyLocation() != null){
		    	
	 	    	Iterator<Station> sta = myListLineStation.iterator();
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
	    	//our task
	    	adapter = new ListViewAdapterLine(mContext, mListView, myListLineStation);
		
		} catch (Exception e)
		{
			Log.e(AppTabASecondFragment.class.toString(), e.getMessage());
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
	
		 //now we can show our list or simply working
		 if (adapter != null && adapter.getCount() > 0)
		 {
			 mListView.setAdapter(adapter);
			 //show list
			 mListView.setVisibility(View.VISIBLE);
			 //unshow empty message
			 isEmpty.setVisibility(View.GONE);
			 mListView.clearFocus();
			 if (mySave.getItemStation() != INVALID_ROW_ID){
				 mListView.setSelection(mySave.getItemStation());				 
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
    	if (myPublic.IsBikeLine(myLine.get_id())){
        	if (Config.DEBUG) Toast.makeText(mContext, mContext.getResources().getString(R.string.bike_line), Toast.LENGTH_SHORT).show();
        	//Crouton.makeText(mActivity, mContext.getResources().getString(R.string.bike_line), Style.INFO).show();
    	}else if (myPublic.IsCircleLine(myLine.get_id())){
        	if (Config.DEBUG) Toast.makeText(mContext, mContext.getResources().getString(R.string.circle_line), Toast.LENGTH_SHORT).show();
        	//Crouton.makeText(mActivity, mContext.getResources().getString(R.string.circle_line), Style.INFO).show();
    	}

	  }
	}
	
	@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.line, menu);
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
        //super.onOptionsItemSelected(item);
	    if (Config.DEBUG) Log.i(Config.LOGTAG, "IN " + String.valueOf(item.getItemId()));
    	switch (item.getItemId()) {
	    case android.R.id.home:
	    	mySave.setItemStation(INVALID_ROW_ID);
	    	mySave.setMyStation(null);
	    	return true;

	    case R.id.menu_return:	
		    if (Config.DEBUG) Log.i(Config.LOGTAG, "OUT Menu_return");
	        // icono de la aplicación pulsado en la barra de acción; One Way / Return
	        if (Config.DEBUG) Toast.makeText(getMyActivity(), getResources().getString(R.string.action_return_show), Toast.LENGTH_SHORT).show();

	    	if (myPublic.IsBikeLine(myLine.get_id())){
	        	if (Config.DEBUG) Toast.makeText(mContext, mContext.getResources().getString(R.string.bike_line), Toast.LENGTH_SHORT).show();
	        	Crouton.makeText(mActivity, mContext.getResources().getString(R.string.bike_line), Style.INFO).show();
	    	}else if (myPublic.IsCircleLine(myLine.get_id())){
	        	if (Config.DEBUG) Toast.makeText(mContext, mContext.getResources().getString(R.string.circle_line), Toast.LENGTH_SHORT).show();
	        	Crouton.makeText(mActivity, mContext.getResources().getString(R.string.circle_line), Style.INFO).show();
	    	}
	        
		    synchronized(this){
		    	final boolean isOneWay = oneWay;
		    	mySave.setOneWay(!isOneWay);
		    	oneWay = !isOneWay;	
			    mySave.setItemStation(-1);
		    }

			if (oneWay){
				myNameLine1.setText(myLine.getUNameOneWay());
				myNameLine2.setText(myLine.getUNameOneWay2());			
			}else{
				myNameLine1.setText(myLine.getUNameReturn());
				myNameLine2.setText(myLine.getUNameReturn2());
			}
		    
	    	new BackgroundTask().execute("SOMETHING");

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
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
		if (Config.DEBUG) Log.d(Config.LOGTAG, "AppTabASecondFragment.onDestroy()");
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
