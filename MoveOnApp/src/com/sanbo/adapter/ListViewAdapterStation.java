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
import com.sanbo.fragment.tabE.AppTabEFirstFragment;
import com.sanbo.image.Image;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class ListViewAdapterStation extends BaseAdapter {
    protected static final String TAG = "ListViewAdapterStation";

	public static final int INVALID_ROW_ID = -1;
	private static final int MAX_TRANSFER = 45;

    // Declare Variables
	private Context context;
	private Activity activity;
	private ListView mListView;
	
	/** A list containing some sample data to show. */
	private ArrayList<Station> myStations = null;
	
	/** Our model of public transport. */
	PublicTransport myPublic = PublicTransport.getInstance();
	
	// Data to next activity
	private SavingState savingState = SavingState.getInstance();
  
	private class ViewHolder {	 

    	//RelativeLayout holder_groupItem; //groupitem
    	CheckBox holder_check_station_favorite; //check_favorite_station
    	TextView holder_station_name; //station_code_name
    	ImageView holder_pmrimage; //pmrimage
    	TextView holder_distance; //meter_num 
    	ArrayList<ImageView> holder_imgtransfer; //imgtransfer1 a 45
		// RelativeLayout imgtransfergroup imgtransfergroup2 imgtransfergroup3
	}

	public ArrayList<Station> getStationList(){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "ListViewAdapterStation.getStationList()");
		// we get data of Stations from our DB
		return myStations;
	}

 	public ListViewAdapterStation(){
    	
    }

    public ListViewAdapterStation(Context pContext, ListView pListView, ArrayList<Station> mStationList) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "ListViewAdapterStation(Context context, ListView pListView, ArrayList<Station> mStationList)");    		 	 	 		
    	myStations = mStationList;
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
				Station st = (Station) textview.getTag();
				if (SavingState.getInstance().isClosestStation()){
					savingState.setMyStationC(st);
					savingState.setItemBusStationC(INVALID_ROW_ID);
				} else {
					savingState.setMyStationB(st);
					savingState.setItemBusStationB(INVALID_ROW_ID);					
				}
				savingState.setMyActiveStation(st);
				savingState.setMyActiveBusStationSelected(INVALID_ROW_ID);
				if (PublicTransport.IsBusStation(st)){
					if (SavingState.getInstance().isClosestStation()){
				        savingState.getMyActivity().pushFragments(AppConstants.TAB_C, 
				        		new AppTabAThirdFragment(),true,true);
					} else {
			            savingState.getMyActivity().pushFragments(AppConstants.TAB_B, 
			            		new AppTabAThirdFragment(),true,true);					
					}
				} else if (PublicTransport.IsBikeStation(st)){
			        savingState.getMyActivity().pushFragments(AppConstants.TAB_E, 
			        		new AppTabEFirstFragment(),true,true);					
				} else {
					// real time EMT and BiciPalma
					MoveOnCroutonStyle.croutonAlert(activity, 
							context.getResources().getString(R.string.Network_Service));
				}
			}
	    });
	}
    
    // How many items are in the data set represented by this Adapter
    @Override
    public int getCount() {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "ListViewAdapterStation.getCount()");
		return myStations.size();
    }

    // Get the data item associated with the specified position in the data set
    @Override
    public Station getItem(int position) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "ListViewAdapterStation.getItem(position)");
		return myStations.get(position);
    }
    
    // Get the id of the station of the line associated with the specified position in the list.
    @Override
    public long getItemId(int position) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "ListViewAdapterStation.getItemId(position)");
		return myStations.get(position).get_id();
    }

    // Get a View that displays the data at the specified position in the data set.
	// You can either create a View manually or inflate it from an XML layout file. 
    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "ListViewAdapterStation.getView(int position, View view, ViewGroup viewGroup)");    		 	 	 		
        // Declare Variables
		ViewHolder holder = null;
	    final View item;

	    if(convertView == null){
	    	item = LayoutInflater.from(context).inflate(R.layout.station_item, mListView, false);

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
		    // disabled facilities
		    holder.holder_pmrimage = (ImageView) item.findViewById(R.id.pmrimage);
		    //Station to favorite
		    holder.holder_check_station_favorite.setTag(getItem(position));
			holder.holder_check_station_favorite.setOnClickListener(new OnClickListener() {
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
			holder.holder_imgtransfer = new ArrayList<ImageView>();
			for (int i=0; i < MAX_TRANSFER; i++){
				String _image = "imgtransfer" + (i+1);
				ImageView image = new ImageView(context);
				try{
					image = (ImageView) item.findViewById(Config.getResId(_image));
					holder.holder_imgtransfer.add(image);
				}
				catch (Exception localException){
					// not controlled
					Log.e(Config.LOGTAG, "ListViewAdapterStation.getView().getImageTransfer");    		 	 	 		
				}
			}
	        item.setTag(holder);
	    } else {
	    	item = (View) convertView;	    	
	    	holder = (ViewHolder)item.getTag();    
	    	holder.holder_check_station_favorite.setTag(getItem(position));
			holder.holder_station_name.setTag(getItem(position));
	    }
	    //Filling data...
	    //Station name
		holder.holder_station_name.setText(Utils.getStationCodeName(
				getItem(position).getCode(), getItem(position).getName()));		
		//Station isAdapted_for_Disabled
		if (this.getItem(position).isAdapted_for_Disabled()){
			holder.holder_pmrimage.setVisibility(View.VISIBLE);			
		} else {
			holder.holder_pmrimage.setVisibility(View.GONE);						
		}
		//Station distance from the user we show distance in closed station			
		if (savingState.isClosestStation()){
			holder.holder_distance.setVisibility(View.VISIBLE);
			holder.holder_distance.setText(Utils.getStationDistance(
					getItem(position).getDistance()));
		} else {
			holder.holder_distance.setVisibility(View.GONE);			
		}
	    //Station to favorite
    	holder.holder_check_station_favorite.setChecked(getIsFavorite(position));
    	//Transfer from Station
		if (myPublic.getmPreferences().isOptionShowTransfer()){
			int min = (MAX_TRANSFER < getItem(position).getListTransfer().size())? 
					MAX_TRANSFER : getItem(position).getListTransfer().size();
			for (int i = 0; i < min; i++){
				holder.holder_imgtransfer.get(i).setImageResource(Image.getImageLine(context, 
						getItem(position).getListTransfer().get(i)));
			}
			for (int j = min; j < MAX_TRANSFER; j++){
				// empty image
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
    	return getItem(position).isFavorite();
    }

    public void updateResults(ArrayList<Station> newStations) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "ListViewAdapterStation.updateResults(ArrayList<Station> newStations)");
		myStations = newStations;
        
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
