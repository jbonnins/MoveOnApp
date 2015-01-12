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

import com.sanbo.datamodel.PublicTransport;
import com.sanbo.datamodel.Station;
import com.sanbo.fragment.base.AppConstants;
import com.sanbo.fragment.tabE.AppTabESecondFragment;
import com.sanbo.image.Image;
import com.sanbo.moveonapp.R;
import com.sanbo.notifier.MoveOnCroutonStyle;
import com.sanbo.utils.Config;
import com.sanbo.utils.SavingState;
import com.sanbo.utils.Utils;

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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class ListViewAdapterBikeStation extends BaseAdapter {
    protected static final String TAG = "ListViewAdapterBikeStation";

	public static final int INVALID_ROW_ID = -1;
	private static final int MAX_TRANSFER = 45;
    
    // Declare Variables
    private Context context;
    private Activity activity;
    
	private ListView mListView;
	
	/** A list containing some sample data to show. */
	private ArrayList<Station> mBikeStations = null;
	
	PublicTransport myPublic = PublicTransport.getInstance();
	SavingState savingState = SavingState.getInstance();
	
    private class ViewHolder {	   
		CheckBox holder_station_favorite; // check_favorite_station
		TextView holder_station_name; // station_code_name
		// Bike service not available for disable people
	   	//ImageView holder_pmrimage; // pmrimage
		TextView holder_station_bikes; // bike_num
		TextView holder_station_parks; // park_num
		TextView holder_station_distance; // meter_num
		ArrayList<ImageView> imgtransfer; // imgtransfer1 to imgtransfer45
		// RelativeLayout imgtransfergroup imgtransfergroup2 imgtransfergroup3
	}
  
    public ArrayList<Station> getStationList(){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "ListViewAdapterLine.getStationList()");
		ArrayList<Station> res;
		
		// we get data of BikeStations from our DB
		res = mBikeStations;
		
		return res;
    }
    
    public ListViewAdapterBikeStation(){
    	
    }

    public ListViewAdapterBikeStation(Context pContext, ListView pListView, ArrayList<Station> bikeStations) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "ListViewAdapterLine(Context context, Line myLine)");    		 	 	 		

		context = pContext;
    	activity = (Activity) context;

    	mListView = pListView;
    	mBikeStations = bikeStations;
    	
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
				//
				TextView textview = (TextView) view.findViewById(R.id.station_code_name);
				Station st = (Station) textview.getTag();
				savingState.setMyBikeStation(st);
				savingState.setItemBikeStation(position); 
	            savingState.getMyActivity().pushFragments(AppConstants.TAB_E, new AppTabESecondFragment(), true, true);
			}
	    });
	}

    // How many items are in the data set represented by this Adapter
    @Override
    public int getCount() {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "ListViewAdapterLine.getCount()");
		return mBikeStations.size();
    }
 
    // Get the data item associated with the specified position in the data set
    @Override
    public Station getItem(int position) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "ListViewAdapterLine.getItem(position)");    	
		return mBikeStations.get(position);
    }
 
    // Get the id of the station associated with the specified position in the list.
    @Override
    public long getItemId(int position) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "ListViewAdapterLine.getItemId(position)"); 
		return mBikeStations.get(position).get_id();
    }
 
	// Get a View that displays the data at the specified position in the data set.
	// You can either create a View manually or inflate it from an XML layout file. 
    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "ListViewAdapterLine.getView(int position, View view, ViewGroup viewGroup)");    		 	 	 		
        // Declare Variables
		ViewHolder holder = null;

		final View item;

	    if(convertView == null){

	    	item = LayoutInflater.from(context).inflate(R.layout.bike_station_item, mListView, false);
	    	
		    holder = new ViewHolder();

			//Get childrow.xml file elements and set values
		    // Name
		    holder.holder_station_name = (TextView) item.findViewById(R.id.station_code_name);
		    // BikeStation name
			holder.holder_station_name.setTag(getItem(position));
			// Num bikes
		    holder.holder_station_bikes = (TextView) item.findViewById(R.id.bike_num);
		    // Num parks
		    holder.holder_station_parks = (TextView) item.findViewById(R.id.park_num);
		    // distance to station
		    holder.holder_station_distance = (TextView) item.findViewById(R.id.meter_num);
		    // check favorite
			holder.holder_station_favorite = (CheckBox) item.findViewById(R.id.check_favorite_station);
			// BikeStation favorite
			holder.holder_station_favorite.setTag(mBikeStations.get(position));
	    	holder.holder_station_favorite.setChecked(getIsFavorite(position));;
			holder.holder_station_favorite.setOnClickListener(new OnClickListener() {
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
			// Catching image transfer in View Item
			holder.imgtransfer = new ArrayList<ImageView>();
			for (int i=0; i < MAX_TRANSFER; i++){
				String _image = "imgtransfer" + (i+1);
				ImageView image = new ImageView(context);
				try{
					image = (ImageView) item.findViewById(Config.getResId(_image));
					holder.imgtransfer.add(image);
				}
				catch (Exception localException){
					// not controlled
					Log.e(Config.LOGTAG, "ListViewAdapterBikeStation.getView(int position, View convertView, ViewGroup viewGroup).gettingImageTransfer()");    		 	 	 		
				}
			}
		    item.setTag(holder);
		}else {
	    	item = (View) convertView;	    	
	    	holder = (ViewHolder)item.getTag();    
	    	holder.holder_station_favorite.setTag(mBikeStations.get(position));
			holder.holder_station_name.setTag(mBikeStations.get(position));
		}
	    // Filling data
	    holder.holder_station_name.setText(Utils.getStationCodeName(mBikeStations.get(position).getCode(), 
	    		mBikeStations.get(position).getName()));
	    if (mBikeStations.get(position).getBikeStation().getBikesAvailable() > 9)
	    	holder.holder_station_bikes.setText(String.valueOf(mBikeStations.get(position).getBikeStation().getBikesAvailable()));
	    else
	    	holder.holder_station_bikes.setText(" " + String.valueOf(mBikeStations.get(position).getBikeStation().getBikesAvailable()));	    	
	    holder.holder_station_parks.setText(String.valueOf(mBikeStations.get(position).getBikeStation().getParksFree()));
	    holder.holder_station_distance.setText(Utils.getStationDistance(mBikeStations.get(position).getDistance()));	    
	    //Station to favorite
    	holder.holder_station_favorite.setChecked(mBikeStations.get(position).isFavorite());
    	//Transfer from Station
		if (myPublic.getmPreferences().isOptionShowTransfer()){
			int min = (MAX_TRANSFER < mBikeStations.get(position).getListTransfer().size())? 
					MAX_TRANSFER : mBikeStations.get(position).getListTransfer().size();
			for (int i = 0; i < min; i++){
				holder.imgtransfer.get(i).setImageResource(Image.getImageLine(context, 
						mBikeStations.get(position).getListTransfer().get(i)));
			}
			for (int j = min; j < MAX_TRANSFER; j++){
				holder.imgtransfer.get(j).setImageResource(R.drawable.emtblackpqt);
			}
			// show only what you need
			RelativeLayout c1;
			if (min > MAX_TRANSFER*2/3){
				c1 = (RelativeLayout) item.findViewById(R.id.imgtransfergroup3);
				c1.setVisibility(View.VISIBLE);
				c1 = (RelativeLayout) item.findViewById(R.id.imgtransfergroup2);
				c1.setVisibility(View.VISIBLE);
				c1 = (RelativeLayout) item.findViewById(R.id.imgtransfergroup);
				c1.setVisibility(View.VISIBLE);																									
			} else if (min <= MAX_TRANSFER*2/3){
				c1 = (RelativeLayout) item.findViewById(R.id.imgtransfergroup3);
				c1.setVisibility(View.GONE);
				if (min <= MAX_TRANSFER*1/3){
					c1 = (RelativeLayout) item.findViewById(R.id.imgtransfergroup2);
					c1.setVisibility(View.GONE);
					if (min == 0){
						c1 = (RelativeLayout) item.findViewById(R.id.imgtransfergroup);
						c1.setVisibility(View.GONE);															
					} else {
						c1 = (RelativeLayout) item.findViewById(R.id.imgtransfergroup);
						c1.setVisibility(View.VISIBLE);																					
					}
				} else {
					c1 = (RelativeLayout) item.findViewById(R.id.imgtransfergroup2);
					c1.setVisibility(View.VISIBLE);
					c1 = (RelativeLayout) item.findViewById(R.id.imgtransfergroup);
					c1.setVisibility(View.VISIBLE);																														
				}
			}			
		} else {
	        // Hide Transfer of Itinerary to make more clear
			RelativeLayout c = (RelativeLayout) item.findViewById(R.id.imgtransfergroup);
			c.setVisibility(View.GONE);
			c = (RelativeLayout) item.findViewById(R.id.imgtransfergroup2);
			c.setVisibility(View.GONE);
			c = (RelativeLayout) item.findViewById(R.id.imgtransfergroup3);
			c.setVisibility(View.GONE);			
		}
		return item;
	}
    
    private boolean getIsFavorite(int position){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "ListViewAdapterLine.getIsFavorite(int position)");
    	return getItem(position).isFavorite();
    }

		
    public void updateResults(ArrayList<Station> bikeStations) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "ListViewAdapterLine.updateResults(Line newLine, boolean isOneWay)");
    	
    	mBikeStations = bikeStations;
        
        //Triggers the list update
        notifyDataSetChanged();
    }
    
    private void savingDataToPublicTransport(Station myStation, boolean isFavorite){
    	//nothing to save, all info is in PublicTransport
    	if (myPublic.setIsFavoriteStation(myStation, isFavorite)){
     		if (isFavorite){
	  			  MoveOnCroutonStyle.croutonConfirm(activity, context.getResources().getString(R.string.checked) +
	  				" " + context.getResources().getString(R.string.station) + " " + myStation.getCode());
     		} else {
    			  MoveOnCroutonStyle.croutonInfo(activity, context.getResources().getString(R.string.unchecked) +
    	  			" " + context.getResources().getString(R.string.station) + " " + myStation.getCode());
     		}
        } else {
    		  MoveOnCroutonStyle.croutonWarn(activity, context.getResources().getString(R.string.error_database));    		
        }
    }

}
    
