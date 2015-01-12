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
import com.sanbo.fragment.tabA.AppTabAThirdFragment;
import com.sanbo.image.Image;
import com.sanbo.utils.Config;
import com.sanbo.utils.SavingState;
import com.sanbo.utils.Utils;
import com.sanbo.moveonapp.R;
import com.sanbo.notifier.MoveOnCroutonStyle;

import android.annotation.SuppressLint;
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


public class ListViewAdapterLine extends BaseAdapter {
    protected static final String TAG = "ListViewAdapterLine";

	public static final int INVALID_ROW_ID = -1;
	private static final int MAX_TRANSFER = 45;
   
    // Declare Variables
	private Context mContext;
	private Activity activity;
	
	// Our data content
	private ListView mListView;
	
	// Data to next activity
	private SavingState savingState = SavingState.getInstance();

	/** A list containing some sample data to show. */
	private ArrayList<Station> myStationList = null;
	
	PublicTransport myPublic = PublicTransport.getInstance();
    
    private class ViewHolder {	   
    	CheckBox holder_station_favorite; // check_favorite_station
	   	TextView holder_station_name;	// station_code_name  
	   	ArrayList<ImageView> holder_imgtransfer; // imgtransfer1 to imgtransfer45
	   	TextView holder_station_distance;	// meter_num
	   	ImageView holder_pmrimage; // pmrimage
	   	// RelativeLayout imgtransfergroup imgtransfergroup2 imgtransfergroup3

    }
    
    public ArrayList<Station> getStationList(){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "ListViewAdapterLine.getStationList()");
		return myStationList;
    }
    
    public ListViewAdapterLine(){
    	
    }

    public ListViewAdapterLine(Context context, ListView pListView, ArrayList<Station> myLineStation) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "ListViewAdapterLine(Context context, Line myLine)");    		 	 	 		
    	myStationList = myLineStation;
    	mContext = context;
    	activity = (Activity) context;
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
				if (Config.DEBUG) Toast.makeText(mContext, "You clicked on position : " + position + " and id : " + id, Toast.LENGTH_LONG).show();				
		        /* Go to next fragment in navigation stack*/
				TextView myStationName = (TextView) view.findViewById(R.id.station_code_name);
				Station st = (Station) myStationName.getTag();
				savingState.setMyStation(st);
				savingState.setItemStation(position);
				savingState.setMyActiveStation(st);
				savingState.setMyActiveBusStationSelected(INVALID_ROW_ID);
				if (myPublic.IsBusLine(savingState.getMyLine().get_id())){
					savingState.getMyActivity().pushFragments(AppConstants.TAB_A, 
							new AppTabAThirdFragment(),true,true);
				} else {
					// real time EMT and BiciPalma
					MoveOnCroutonStyle.croutonAlert(activity, 
							mContext.getResources().getString(R.string.Network_Service));
				}
			}
	    });
	}

    // How many items are in the data set represented by this Adapter
    @Override
    public int getCount() {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "ListViewAdapterLine.getCount()");
		return myStationList.size();
    }
 
    // Get the data item associated with the specified position in the data set
    @Override
    public Station getItem(int position) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "ListViewAdapterLine.getItem(position)");    
		return myStationList.get(position);
    }
 
    // Get the id of the statlion of the line associated with the specified position in the list.
    @Override
    public long getItemId(int position) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "ListViewAdapterLine.getItemId(position)"); 
		return myStationList.get(position).get_id();
    }
 
	// Get a View that displays the data at the specified position in the data set.
	// You can either create a View manually or inflate it from an XML layout file. 
    @SuppressLint("InflateParams")
	@Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "ListViewAdapterLine.getView(int position, View view, ViewGroup viewGroup)");    		 	 	 		
        // Declare Variables
		ViewHolder holder = null;
	    final View item;

	    if(convertView == null){
	        LayoutInflater inflator = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        item = inflator.inflate(R.layout.station_item, null);

		    holder = new ViewHolder();
		    holder.holder_station_favorite = (CheckBox) item.findViewById(R.id.check_favorite_station);
		    holder.holder_station_favorite.setTag(getItem(position));
		    holder.holder_station_name = (TextView) item.findViewById(R.id.station_code_name);
		    holder.holder_station_name.setTag(getItem(position));
		    holder.holder_station_distance = (TextView) item.findViewById(R.id.meter_num);
		    holder.holder_pmrimage = (ImageView) item.findViewById(R.id.pmrimage);
			// Show transfer if selected
			holder.holder_imgtransfer = new ArrayList<ImageView>();
			for (int i=0; i < MAX_TRANSFER; i++){
				String _image = "imgtransfer" + (i+1);
				ImageView image = new ImageView(mContext);
				try{
					image = (ImageView) item.findViewById(Config.getResId(_image));
					holder.holder_imgtransfer.add(image);
				}
				catch (Exception localException){
					// not controlled
					Log.e(Config.LOGTAG, "ExpandableListAdapterItinerary.getChildView.gettingImageTransfer()");    		 	 	 		
				}
			}    
	        item.setTag(holder);	        
	    } else {
	    	item = (View) convertView;
	    	holder = (ViewHolder)convertView.getTag();
	    	holder.holder_station_favorite.setTag(myStationList.get(position));
		    holder.holder_station_name.setTag(myStationList.get(position));
	    }
	    // Station Code :: Name - Code if number 000 format
	    holder.holder_station_name.setText(Utils.getStationCodeName(
	    		myStationList.get(position).getCode(), myStationList.get(position).getName()));
	    holder.holder_station_distance.setText(Utils.getStationDistance(myStationList.get(position).getDistance()));
	    //Station isAdapted for PMR
	    if (myStationList.get(position).isAdapted_for_Disabled()){
	    	holder.holder_pmrimage.setVisibility(View.VISIBLE);	    	
	    } else {
	    	holder.holder_pmrimage.setVisibility(View.GONE);
	    }
	    //Station to favorite
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
    	//Transfer from Station
		if (myPublic.getmPreferences().isOptionShowTransfer()){
			int min = (MAX_TRANSFER < myStationList.get(position).getListTransfer().size())? 
					MAX_TRANSFER : myStationList.get(position).getListTransfer().size();
			// Probably we show as a possible transfer the same Itinerary, here we show BIKE as a Tranfer
			for (int i = 0; i < min; i++){
				holder.holder_imgtransfer.get(i).setImageResource(Image.getImageLine(mContext, 
						myStationList.get(position).getListTransfer().get(i)));
			}
			for (int j = min; j < MAX_TRANSFER; j++){
				holder.holder_imgtransfer.get(j).setImageResource(R.drawable.emtblackpqt);
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
		return(item);
	}
    
    private boolean getIsFavorite(int position){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "ListViewAdapterLine.getIsFavorite(int position)");
    	return this.myStationList.get(position).isFavorite();
    }
    
    public void updateResults(ArrayList<Station> myLineStation) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "ListViewAdapterLine.updateResults(Line newLine, boolean isOneWay)");
		myStationList = myLineStation;
		
        //Triggers the list update
        notifyDataSetChanged();
    }
    
    private void savingDataToPublicTransport(Station myStation, boolean isFavorite){
    	//nothing to save, all info is in PublicTransport
    	if (myPublic.setIsFavoriteStation(myStation, isFavorite)){
     		if (isFavorite){
	  			  MoveOnCroutonStyle.croutonConfirm(activity, mContext.getResources().getString(R.string.checked) +
	  				" " + mContext.getResources().getString(R.string.station) + " " + myStation.getCode());
     		} else {
    			  MoveOnCroutonStyle.croutonInfo(activity, mContext.getResources().getString(R.string.unchecked) +
    	  			" " + mContext.getResources().getString(R.string.station) + " " + myStation.getCode());
     		}
        } else {
    		  MoveOnCroutonStyle.croutonWarn(activity, mContext.getResources().getString(R.string.error_database));    		
        }
    }

}

