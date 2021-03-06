/*
 * Copyright 2013 Juan Jos� Bonn�n Sans� (jbonnins@uoc.edu)
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

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.gms.maps.model.LatLng;
import com.sanbo.adapter.ExpandableListItineraryAdapter;
import com.sanbo.datamodel.Itinerary;
import com.sanbo.datamodel.PublicTransport;
import com.sanbo.fragment.base.BaseFragment;
import com.sanbo.moveonapp.R;
import com.sanbo.notifier.MoveOnCroutonStyle;
import com.sanbo.sinchronizable.SynchronizableActivity;
import com.sanbo.synchronizers.LocationSynchronizer;
import com.sanbo.utils.Config;
import com.sanbo.utils.NetworkInformation;
import com.sanbo.utils.SavingState;

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
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;


public class AppTabAFirstFragment extends BaseFragment implements SynchronizableActivity {
	@SuppressWarnings("unused")
	private static final String TAG = "AppTabA";

	public static final int INVALID_ROW_ID = -1;
	
	private ExpandableListView mExpandableListView;
	private ProgressBar myProgressBar;
	private TextView myShowMessage;
	private TextView isEmpty;
	private Context mContext;
	//private AppMainTabActivity mActivity;
	private PublicTransport myPublic = PublicTransport.getInstance();
	private ExpandableListItineraryAdapter adapter;
	private ArrayList<Itinerary> myItineraryList = null;
	
	private SavingState mySave = SavingState.getInstance();
	
    // user Location
    private LocationSynchronizer mLocationSynchronizer;
    private Location myLocation;
	// Network Data
	private NetworkInformation network;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (Config.DEBUG) Log.d(Config.LOGTAG, "AppTabCFirstFragment.onCreate(Bundle savedInstanceState)");
		mContext = getSherlockActivity();
		//we allways show Station OW when we select a Line
		mySave.setOneWay(true);
		// Retrieve user location
		try {
			mLocationSynchronizer = LocationSynchronizer.getInstance(this);
			myLocation = mLocationSynchronizer.getLocation();
		} catch (Exception e)
		{
			Log.e(AppTabAFirstFragment.class.toString(), e.getMessage());
		}
		network = NetworkInformation.getInstance();
		// saving myLocation to NetworkInformation
		if (myLocation != null){
			network.setMyLocation(new LatLng(myLocation.getLatitude(), 
					myLocation.getLongitude()));
		} else {
        	MoveOnCroutonStyle.croutonAlert( mActivity, R.string.error_location_NETWORK);
		}

		setHasOptionsMenu(true);
	}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "AppTabCFirstFragment.onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)");
		
		View view       =   inflater.inflate(R.layout.expandablelist_generic, container, false);
        
		mExpandableListView = (ExpandableListView) view.findViewById(R.id.list);
		// divider for elements
		mExpandableListView.setDivider(mContext.getResources().getDrawable(R.drawable.line));
		mExpandableListView.setChildDivider(mContext.getResources().getDrawable(R.drawable.line));
		
		myProgressBar = (ProgressBar) view.findViewById(R.id.progress);
		myShowMessage = (TextView) view.findViewById(R.id.showmessage);
		// tittle of the list
		TextView myTittle = (TextView) view.findViewById (R.id.name_list);
		// value of tittle
		myTittle.setText(R.string.listLines);
		
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
		SavingState mySave = SavingState.getInstance();
		mySave.setFirstVisibleLine(mExpandableListView.getFirstVisiblePosition());
    }
    
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
    	super.onRestoreInstanceState(savedInstanceState);
    } 
    
    @Override
	public void onResume() {
        super.onResume();
  
     }
    
	private class BackgroundTask extends AsyncTask<String, Void, String> {
		// What we need in temp loadar
	  
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
	    	// only the first time we get the list
	    	myItineraryList = myPublic.getListAllItinerariesWithTransfer(mySave.isFavoriteOn());
				
			if (myLocation != null){
				network.setMyLocation(new LatLng(myLocation.getLatitude(), 
						myLocation.getLongitude()));
			}
			
	    	//our task
	    	adapter = new ExpandableListItineraryAdapter(mContext, mExpandableListView, myItineraryList);
	    	
			
		} catch (Exception e)
		{
			Log.e(AppTabAFirstFragment.class.toString(), e.getMessage());
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
		 if (adapter != null && adapter.getGroupCount() > 0)
		 {
			 mExpandableListView.setAdapter(adapter);
			 //show list
			 mExpandableListView.setVisibility(View.VISIBLE);
			 //unshow empty message
			 isEmpty.setVisibility(View.GONE);
			 mExpandableListView.clearFocus();
			 if (mySave.getGroupItineary() != INVALID_ROW_ID){
				 mExpandableListView.expandGroup(mySave.getGroupItineary());
			 }
			 if (mySave.getFirstVisibleLine() != INVALID_ROW_ID){
				mExpandableListView.setSelection(mySave.getFirstVisibleLine());		
			 }
		 }
		 //if we don't have data to show
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
        inflater.inflate(R.menu.main, menu);
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
	    	// reset itinerary view
	    	mySave.setGroupItineary(INVALID_ROW_ID);
	    	mySave.setChildLine(INVALID_ROW_ID);
	    	mySave.setMyItinerary(null);
	    	mySave.setMyLine(null);	    	
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
		if (Config.DEBUG) Log.d(Config.LOGTAG, "AppTabAFirstFragment.onDestroy()");
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
