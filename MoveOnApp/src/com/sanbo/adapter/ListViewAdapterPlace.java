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

package com.sanbo.adapter;

import java.util.ArrayList;

import com.sanbo.datamodel.Place;
import com.sanbo.datamodel.PublicTransport;
import com.sanbo.fragment.base.AppConstants;
import com.sanbo.fragment.tabE.AppTabESecondFragment;
import com.sanbo.utils.Config;
import com.sanbo.utils.SavingState;
import com.sanbo.utils.Utils;
import com.sanbo.moveonapp.R;
import com.sanbo.notifier.MoveOnCroutonStyle;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class ListViewAdapterPlace extends BaseAdapter {
    protected static final String TAG = "ListViewAdapterPlace";

	public static final int INVALID_ROW_ID = -1;

    // Declare Variables
	private Context context;
	private Activity activity;
	private ListView mListView;
	
	/** A list containing some sample data to show. */
	private ArrayList<Place> myPlaces = null;
	
	/** Our model of public transport. */
	PublicTransport myPublic = PublicTransport.getInstance();
	
	// Data to next activity
	private SavingState savingState = SavingState.getInstance();
  
	private class ViewHolder {	 

    	//RelativeLayout holder_groupItem; //groupitem
    	CheckBox holder_check_station_favorite; //check_favorite_station
    	TextView holder_station_name; //station_code_name
    	TextView holder_address; //address 
    	TextView holder_distance; //meter_num 
	}

	public ArrayList<Place> getPlaceList(){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "ListViewAdapterStation.getPlaceList()");
		// we get data of Stations from our DB
		return myPlaces;
	}

 	public ListViewAdapterPlace(){
    	
    }

    public ListViewAdapterPlace(Context pContext, ListView pListView, ArrayList<Place> mPlaceList) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "ListViewAdapterPlace(Context pContext, ListView pListView, ArrayList<Place> mPlaceList)");    		 	 	 		
    	myPlaces = mPlaceList;
    	context = pContext;
    	activity = (Activity) pContext;
    	mListView = pListView;
 
    	setListEvent();
	}
 
    private void setListEvent() {

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				// arg2 = the id of the item in our view (List/Grid) that we clicked
				// arg3 = the id of the item that we have clicked
				// if we didn't assign any id for the Object (Book) the arg3 value is 0
				// That means if we comment, aBookDetail.setBookIsbn(i); arg3 value become 0
				if (Config.DEBUG) Toast.makeText(context, "You clicked on position : " + position + " and id : " + id, Toast.LENGTH_LONG).show();				
		        /* Go to next fragment in navigation stack*/
				TextView textview = (TextView) view.findViewById(R.id.station_code_name);
				Place pl = (Place) textview.getTag();
				savingState.setMyPlace(pl);
				savingState.setItemPlace(position);
		        savingState.getMyActivity().pushFragments(AppConstants.TAB_D, 
		            		new AppTabESecondFragment(),true,true);					
			}
	    });
	}
    
    // How many items are in the data set represented by this Adapter
    @Override
    public int getCount() {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "ListViewAdapterPlace.getCount()");
		return myPlaces.size();
    }

    // Get the data item associated with the specified position in the data set
    @Override
    public Place getItem(int position) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "ListViewAdapterPlace.getItem(position)");
		return myPlaces.get(position);
    }
    
    // Get the id of the station of the line associated with the specified position in the list.
    @Override
    public long getItemId(int position) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "ListViewAdapterPlace.getItemId(position)");
		return myPlaces.get(position).get_id();
    }

    // Get a View that displays the data at the specified position in the data set.
	// You can either create a View manually or inflate it from an XML layout file. 
    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "ListViewAdapterPlace.getView(int position, View view, ViewGroup viewGroup)");    		 	 	 		
        // Declare Variables
		ViewHolder holder = null;
	    final View item;

	    if(convertView == null){
	    	item = LayoutInflater.from(context).inflate(R.layout.place_item, mListView, false);

		    holder = new ViewHolder();
			//Get childrow.xml file elements and set values		    
		    // Name
		    holder.holder_station_name = (TextView) item.findViewById(R.id.station_code_name);
		    // Station tag
			holder.holder_station_name.setTag(getItem(position));
		    // distance to station
		    holder.holder_distance = (TextView) item.findViewById(R.id.meter_num);
		    // check favorite		    
		    holder.holder_check_station_favorite = (CheckBox) item.findViewById(R.id.check_favorite_station);
		    // address
		    holder.holder_address = (TextView) item.findViewById(R.id.address);
		    //Place to favorite
		    holder.holder_check_station_favorite.setTag(getItem(position));
			holder.holder_check_station_favorite.setOnClickListener(new OnClickListener() {
				  @Override
				  public void onClick(View v) {
			                //is chkIos checked?
					  CheckBox cb = (CheckBox) v;
					  Place pl = (Place) v.getTag();
					  if (cb.isChecked()){
						  savingDataToPublicTransport(pl, true);
					  }else{ 
						  savingDataToPublicTransport(pl, false);
					  }
				  }

			});	
	        item.setTag(holder);
	    } else {
	    	item = (View) convertView;	    	
	    	holder = (ViewHolder)item.getTag();    
	    	holder.holder_check_station_favorite.setTag(getItem(position));
			holder.holder_station_name.setTag(getItem(position));
	    }
	    //Filling data...
	    //Station name
		holder.holder_station_name.setText(getItem(position).getName());	
		holder.holder_address.setText(Utils.unescape(getItem(position).getAddress()));
		//Station distance from the user we show distance in closed station			
		holder.holder_distance.setText(Utils.getStationDistance(
					getItem(position).getDistance()));
	    //Station to favorite
    	holder.holder_check_station_favorite.setChecked(getIsFavorite(position));
	    return(item);
    }
    
    private boolean getIsFavorite(int position){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "ListViewAdapterLine.getIsFavorite(int position)");
    	return getItem(position).isFavourite();
    }

    public void updateResults(ArrayList<Place> newPlaces) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "ListViewAdapterPlace.updateResults(ArrayList<Place> newPlaces)");
		myPlaces = newPlaces;
        
        //Triggers the list update
        notifyDataSetChanged();
    }

    private void savingDataToPublicTransport(Place myPlace, boolean isFavorite){
    	//nothing to save, all info is in PublicTransport
    	if (myPublic.setIsFavoritePlace(myPlace, isFavorite)){
     		if (isFavorite){
	  			  MoveOnCroutonStyle.croutonConfirm(activity, context.getResources().getString(R.string.checked) +
	  				" " + context.getResources().getString(R.string.place) + myPlace.getName());
     		} else {
    			  MoveOnCroutonStyle.croutonInfo(activity, context.getResources().getString(R.string.unchecked) +
    	  			" " + context.getResources().getString(R.string.place) + myPlace.getName());
     		}
        } else {
    		  MoveOnCroutonStyle.croutonWarn(activity, context.getResources().getString(R.string.error_database));    		
        }
    	
    }
}
