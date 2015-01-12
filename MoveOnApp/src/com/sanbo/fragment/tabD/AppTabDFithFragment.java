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

package com.sanbo.fragment.tabD;

import java.util.ArrayList;
import java.util.Collections;
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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.gms.maps.model.LatLng;
import com.sanbo.adapter.ListViewAdapterPlace;
import com.sanbo.datamodel.Place;
import com.sanbo.datamodel.PublicTransport;
import com.sanbo.fragment.base.BaseFragment;
import com.sanbo.moveonapp.MainActivity;
import com.sanbo.moveonapp.R;
import com.sanbo.notifier.MoveOnCroutonStyle;
import com.sanbo.sinchronizable.SynchronizableActivity;
import com.sanbo.synchronizers.LocationSynchronizer;
import com.sanbo.utils.Config;
import com.sanbo.utils.NetworkInformation;
import com.sanbo.utils.SavingState;

import de.keyboardsurfer.android.widget.crouton.Crouton;



public class AppTabDFithFragment extends BaseFragment implements SynchronizableActivity{
		@SuppressWarnings("unused")
		private static final String TAG = "AppTabD";

		public static final int INVALID_ROW_ID = -1;

		private ListView mListView;
		private ProgressBar myProgressBar;
		private TextView myShowMessage;
		private TextView isEmpty;
		private Context mContext;

		private ListViewAdapterPlace adapter;
		ArrayList<Place> myPlaceList = null;

		// Data
		PublicTransport myPublic = PublicTransport.getInstance();
		private SavingState mySave = SavingState.getInstance();

	    // user Location
	    private LocationSynchronizer mLocationSynchronizer;
		private Location myLocation;

		// Network Data
		private NetworkInformation network;
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			if (Config.DEBUG) Log.d(Config.LOGTAG, "AppTabDFithFragment.onCreate(Bundle savedInstanceState)");
			mContext = getSherlockActivity();
			mActivity = getMyActivity();
			// Retrieve user location
	        mLocationSynchronizer = LocationSynchronizer.getInstance(this);
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
			if (Config.DEBUG) Log.d(Config.LOGTAG, "AppTabDFithFragment.onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)");
		    View view       =   inflater.inflate(R.layout.list_generic, container, false);
		    
			mListView = (ListView) view.findViewById(R.id.list);
			// divider for elements
			mListView.setDivider(mContext.getResources().getDrawable(R.drawable.line));

			myProgressBar = (ProgressBar) view.findViewById(R.id.progress);
			myShowMessage = (TextView) view.findViewById(R.id.showmessage);
			// tittle of the list
			TextView myTitle = (TextView) view.findViewById (R.id.name_list);
							
			// value of tittle
			myTitle.setText(R.string.listPlaces);
			
			isEmpty = (TextView) view.findViewById (R.id.empty);

			//Execute asyntask (we can send string array)
			new BackgroundTask().execute("SOMETHING");
					
		    return view;
		}
		
		@Override
		public void onSaveInstanceState(Bundle outState) {
		    super.onSaveInstanceState(outState);
			if (Config.DEBUG) Log.d(Config.LOGTAG, "AppTabDFithFragment.onSaveInstanceState(Bundle outState)");
		    //Our state is saved on PublicTransport and in our DDBB
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
	 	    	
	 	    	myPlaceList = myPublic.getListAllPlaces(mySave.isFavoriteOn()); 
	 	    	
	 	    	if (network.getMyLocation() != null){

		 	    	Iterator<Place> pla = myPlaceList.iterator();
		 	    	while (pla.hasNext()){
		 	    		Place pl = pla.next();
						float[] distance = new float[1];
							Location.distanceBetween(pl.getGeoPosition().getLatitudeMoveOn(),
		 							                 pl.getGeoPosition().getLongitudeMoveOn(),
		 											 network.getMyLocation().latitude,
		 		                                     network.getMyLocation().longitude, distance);
							pl.setDistance(distance[0]);    		
		 	    	}
	 	    	}
	 	    	// Order by Name - Code :)
	 	    	Collections.sort(myPlaceList, Place.DistanceComparator);
	 	    	
		    	//our task
		    	adapter = new ListViewAdapterPlace (mContext, mListView, myPlaceList);
				
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
				 if (mySave.getItemPlace() != INVALID_ROW_ID){
					 mListView.setSelection(mySave.getItemPlace());				 
				 }
			 } else if (adapter != null){
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
	    		menu.findItem(R.id.menu_settings).setEnabled(false);
	    	}
	        super.onCreateOptionsMenu(menu, inflater);
	    }
		
	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
		    if (Config.DEBUG) Log.i(Config.LOGTAG, "IN " + String.valueOf(item.getItemId()));
	    	switch (item.getItemId()) {

	    	case android.R.id.home:
		    	mySave.setItemPlace(INVALID_ROW_ID);
		    	mySave.setMyPlace(null);
		    	return true;

		    case R.id.menu_settings:
		    	synchronized(this){
		    		// here we force to refresh our current tab
		    		getSherlockActivity().supportInvalidateOptionsMenu();
		    		adapter.notifyDataSetChanged();
	    		}	    	
				return true;
				
		    case R.id.menu_refresh:
			    if (Config.DEBUG) Log.i(Config.LOGTAG, "OUT Menu_refresh");
		        // icono de la aplicación pulsado en la barra de acción; Refresh data
		        if (Config.DEBUG) Toast.makeText(mContext, getResources().getString(R.string.action_refresh_show), Toast.LENGTH_SHORT).show();
		        //Execute asyntask (we can send string array)
		        if (mySave.isFavoriteOn()){
		        	new BackgroundTask().execute("SOMETHING");
		        } else {
	    			  MoveOnCroutonStyle.croutonInfo(mActivity, mContext.getResources().getString(R.string.no_changes));

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
			if (Config.DEBUG) Log.d(Config.LOGTAG, "AppTabDFithFragment.onDestroy()");
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
