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

import com.sanbo.datamodel.BusStation;
import com.sanbo.datamodel.Itinerary;
import com.sanbo.datamodel.PublicTransport;
import com.sanbo.datamodel.Station;
import com.sanbo.enumerated.TypeTransport;
import com.sanbo.fragment.tabA.AppTabAThirdFragment;
import com.sanbo.image.Image;
import com.sanbo.moveonapp.R;
import com.sanbo.notifier.MoveOnCroutonStyle;
import com.sanbo.utils.Config;
import com.sanbo.utils.NetworkInformation;
import com.sanbo.utils.SavingState;
import com.sanbo.utils.TimeUtils;
import com.sanbo.utils.Utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class ListViewAdapterNextBus extends BaseAdapter {
    protected static final String TAG = "ListViewAdapterBikeStation";

	public static final int INVALID_ROW_ID = -1;
    
    // Declare Variables
    private Context context;
    private Activity activity;
    
	private ListView mListView;
	
	/** A list containing some sample data to show. */
	private Station mBusStation;
	private int itemSelected;
	private View view;
	
	PublicTransport myPublic = PublicTransport.getInstance();
	SavingState savingState = SavingState.getInstance();
	
    private class ViewHolder {	
    	RelativeLayout holder_relativeLayout; // container
		TextView holder_nameLine1; // nameLine1 short name of line
		TextView holder_nameLine2; // nameLine2 full name of line include route
		TextView holder_firstbus; // firstbus time info first bus
		TextView holder_secondbus; // secondbus time info second bus
		ImageView holder_lineimage; // lineimage
	}
  
    public Station getLineList(){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "ListViewAdapterNextBus.getLineList()");
		Station res;
		
		// we get data of BikeStations from our DB
		res = mBusStation;
		
		return res;
    }
    
    public ListViewAdapterNextBus(){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "ListViewAdapterNextBus()");
    	
    }

    public ListViewAdapterNextBus(Context pContext, ListView pListView, Station myStation) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "ListViewAdapterNextBus(Context pContext, ListView pListView, Station.code =" + myStation.getCode());

		context = pContext;
    	activity = (Activity) pContext;

    	mListView = pListView;
    	mBusStation = myStation;
    	itemSelected = savingState.getItemStation();
    	try {
    		//showDataSelected(context, mBusStation, itemSelected);
		} catch (Exception e)
		{
			Log.e(AppTabAThirdFragment.class.toString(), e.getMessage());
		}				
    		   	
		setListEvent();
	}

	private void setListEvent() {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "ListViewAdapterNextBus.setListEvent()");

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
				if (Config.DEBUG) MoveOnCroutonStyle.croutonConfirm(activity, "You clicked on position : " + position + " and id : " + id);
		        /* Go to next fragment in navigation stack*/
				TextView textview = (TextView) view.findViewById(R.id.nameLine1);
				Station st = (Station) textview.getTag();
				savingState.setMyActiveStation(st);
				savingState.setMyActiveBusStationSelected(position);
			    // hide selected position
				updateResults(context, st, position);
			}
	    });
		
	}

    // How many items are in the data set represented by this Adapter
    @Override
    public int getCount() {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "ListViewAdapterNextBus.getCount()");
		return mBusStation.getBusStations().size();
    }
 
    // Get the data item associated with the specified position in the data set
    @Override
    public BusStation getItem(int position) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "ListViewAdapterNexBus.getItem(position)");    	
		return mBusStation.getBusStations().get(position);
    }
 
    // Get the id of the station associated with the specified position in the list.
    @Override
    public long getItemId(int position) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "ListViewAdapterNexBus.getItemId(position =" + position); 
		return mBusStation.getBusStations().get(position).get_id();
    }
    
    public long getTimeStamp(){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "ListViewAdapterNexBus.getTimeStamp()");    		 	 	 		
    	return mBusStation.getTimeStamp().getTimeInMillis();
   }
 
	// Get a View that displays the data at the specified position in the data set.
	// You can either create a View manually or inflate it from an XML layout file. 
    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "ListViewAdapterNexBus.getView(int position, View view, ViewGroup viewGroup)");    		 	 	 		
        // Declare Variables
		ViewHolder holder = null;

		final View item;

	    if(convertView == null){

	    	item = LayoutInflater.from(context).inflate(R.layout.item_line_nextbus, mListView, false);
	    	
		    holder = new ViewHolder();
		    holder.holder_relativeLayout = (RelativeLayout) item.findViewById(R.id.RelativeLayout1);
			//Get childrow.xml file elements and set values
		    // Line image
		    holder.holder_lineimage = (ImageView) item.findViewById(R.id.lineimage);
		    // Short Name of line
		    holder.holder_nameLine1 = (TextView) item.findViewById(R.id.nameLine1);
		    if (mBusStation.getBusStations() == null){
		    	holder.holder_nameLine1.setTag(null);		    	
		    } else {
		    	holder.holder_nameLine1.setTag(mBusStation);
		    }
		    // Long Name of line include route
		    holder.holder_nameLine2 = (TextView) item.findViewById(R.id.nameLine2);
		    // Firstbus
			holder.holder_firstbus = (TextView) item.findViewById(R.id.firstbus);
		    // Seconbus
			holder.holder_secondbus = (TextView) item.findViewById(R.id.secondbus);
			item.setTag(holder);
		}else {
	    	item = (View) convertView;	    	
	    	holder = (ViewHolder) item.getTag();    
			holder.holder_nameLine1.setTag(mBusStation);
		}
	    // Filling data
	    Itinerary it = getItem(position).getItinerary();
	    // Itinerary Image
	    holder.holder_lineimage.setImageResource(Image.getImageLine(context, it));
	    // Destination of line
	    holder.holder_nameLine1.setText(getItem(position).getFulName(
						getItem(position).getDestination())[0]);	
	    // we don't have info about route in v1.0
	    holder.holder_nameLine2.setText(getItem(position).getFulName(
				getItem(position).getDestination())[1]);	
	    if (getItem(position).getFirstBus() != null){
	    	Long myTime = getItem(position).getFirstBus().getTimeInMillis() - getTimeStamp();
	    	if (myTime < 0) myTime = 0L;
	    	holder.holder_firstbus.setText( context.getResources().getString(R.string.first_bus) + " " +
	    			Utils.getTimeFormated(myTime));
	    } else {
	    	holder.holder_firstbus.setText( context.getResources().getString(R.string.first_bus) + " " +
	    		context.getResources().getString(R.string.bydefault0));	    	
	    }
	    if (getItem(position).getSecondBus() != null){
	    	Long myTime = getItem(position).getSecondBus().getTimeInMillis() - getTimeStamp();
	    	if (myTime < 0) myTime = 0L;
	    	holder.holder_secondbus.setText( context.getResources().getString(R.string.next_bus) + " " +
		    	Utils.getTimeFormated(myTime));
	    } else {
	    	holder.holder_secondbus.setText(context.getResources().getString(R.string.next_bus) + " " +
		    		context.getResources().getString(R.string.bydefault0));	    		    	
	    }
	    if (itemSelected != position){
	    	holder.holder_relativeLayout.setVisibility(View.VISIBLE);
	    } else {
	    	holder.holder_relativeLayout.setVisibility(View.GONE);	    	
	    }
		return item;
	}
    
    public void updateResults(Context mContext, Station busStation, int position) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "ListViewAdapterNexBus.updateResults(Station busStation)");
    	
    	mBusStation = busStation;
    	itemSelected = position;
    	 
        // show item selected
        showDataSelected(mContext, mBusStation, itemSelected);
    	//Triggers the list update
        notifyDataSetChanged();
        
    }
        
    public void showDataSelected(Context context, Station busStation, int position){
		NetworkInformation network = NetworkInformation.getInstance();

    	if (SavingState.getInstance().getMyFragment() instanceof AppTabAThirdFragment){
    		AppTabAThirdFragment myFrag = (AppTabAThirdFragment) SavingState.getInstance().getMyFragment();
    		view = myFrag.getViewFromFragment();
    		view.postInvalidate();
    	}
		
		if (view != null){        	
    		// Refresh view
			//Retrieved time information
			TextView retrievedtime = (TextView) view.findViewById(R.id.retrievedtime);
			// Local Time
			if (network.getEmtStationNetwork().getTimeStamp() != null){
				busStation.setTimeStamp(network.getEmtStationNetwork().getTimeStamp());
				retrievedtime.setText(TimeUtils.getTimeStampToString(network.getEmtStationNetwork().getTimeStamp().getTimeInMillis(), 
						Config.MyTimeZone, context, true));
			} else {
				// timeStamp actualized
				retrievedtime.setText(TimeUtils.getTimeStampToString(busStation.getTimeStamp().getTimeInMillis(), 
						Config.MyTimeZone, context, true));				
			}
			//Line image of first option, by default the first item of the list
			retrievedtime.postInvalidate();
//			RelativeLayout relativeLayout1 = (RelativeLayout) view.findViewById(R.id.RelativeLayout1);
//			if (busStation.getBusStations() != null){ 
//				relativeLayout1.setVisibility(View.VISIBLE);
//			} else {
//				relativeLayout1.setVisibility(View.GONE);
//			}
			ImageView lineimage = (ImageView) view.findViewById(R.id.lineimage);
			//Short name of the line POWERED BY EMT service
			TextView nameLine1 = (TextView) view.findViewById(R.id.nameLine1);
			//Time in minutes and seconds to the first bus
			TextView firstbus = (TextView) view.findViewById(R.id.firstbus);
			//Time in minutes and seconds to the second bus
			TextView secondbus = (TextView) view.findViewById(R.id.secondbus);
			//Full name of the line FROM TO
			TextView nameLine2 = (TextView) view.findViewById(R.id.nameLine2);
			//Type of transport (in our model we have ONLY one TypeTransport per station
			ImageView image_typetransport = (ImageView) view.findViewById(R.id.image_typetransport);
			//Line image of first option, by default the first item of the list
			if (busStation.getBusStations() != null && busStation.getBusStations().size() > 0 && itemSelected != INVALID_ROW_ID){
				lineimage.setImageResource(Image.getImageItinerary(context, 
					busStation.getBusStations().get(itemSelected).getItinerary()));
				lineimage.postInvalidate();
				//Short name of the line POWERED BY EMT service
				nameLine1.setText(busStation.getBusStations().get(itemSelected).getFulName(
					busStation.getBusStations().get(itemSelected).getDestination())[0]);	
				nameLine1.postInvalidate();
				//Time in minutes and seconds to the first bus
				if (busStation.getBusStations().get(itemSelected).getFirstBus() != null) {
					long myTime = busStation.getBusStations().get(itemSelected).getFirstBus().getTimeInMillis()
							- busStation.getTimeStamp().getTimeInMillis();
					if (myTime < 0) myTime = 0L;
					firstbus.setText(context.getResources().getString(R.string.first_bus) + " " + Utils.getTimeFormated(myTime));
					firstbus.postInvalidate();
				} else {
					firstbus.setText(context.getResources().getString(R.string.first_bus) + " " + 
							context.getResources().getString(R.string.bydefault0));
					firstbus.postInvalidate();
				}
				//Time in minutes and seconds to the second bus
				if (busStation.getBusStations().get(itemSelected).getSecondBus() != null){
					long myTime = busStation.getBusStations().get(itemSelected).getSecondBus().getTimeInMillis()
							- busStation.getTimeStamp().getTimeInMillis();
					if (myTime < 0) myTime = 0L;
					secondbus.setText(context.getResources().getString(R.string.next_bus) + " " + Utils.getTimeFormated(myTime));
					secondbus.postInvalidate();
				} else {
					secondbus.setText(context.getResources().getString(R.string.next_bus) + " " + 
							context.getResources().getString(R.string.bydefault0));
					secondbus.postInvalidate();
	
				}
				//Full name of the line FROM TO WE DON'T HAVE ENOUGH INFORMATION
				nameLine2.setText(busStation.getBusStations().get(itemSelected).getFulName(
						busStation.getBusStations().get(itemSelected).getDestination())[1]);	
				nameLine2.postInvalidate();
				//Type of transport (in our model we have ONLY one TypeTransport per station
				image_typetransport.setImageResource(Image.getImageTransport(context, 
						TypeTransport.getTypeTransport(busStation.getBusStations().get(itemSelected).getItinerary().getTypeOfTransport())));
				image_typetransport.postInvalidate();		
			}
			//Indicates if the information is from EMT service
			TextView text_info1 = (TextView) view.findViewById(R.id.text_info1);
			//Indicates if the information is from Schedule (with MoveOn approximation)
			if (network.getEmtStationNetwork() != null && network.getEmtStationNetwork().getBusStations() != null &&
					busStation.getBusStations() != null &&
					network.getEmtStationNetwork().getBusStations().size() != busStation.getBusStations().size()){
				//Indicates if the information is from MoveOn TimeTable
				text_info1.setText(context.getResources().getString(R.string.poweredEMT) + " " + 
						context.getResources().getString(R.string.scheduled));
			} else if (network.getEmtStationNetwork() != null && network.getEmtStationNetwork().getBusStations() != null &&
					busStation.getBusStations() != null &&
					network.getEmtStationNetwork().getBusStations().size() == busStation.getBusStations().size()){
				text_info1.setText(context.getResources().getString(R.string.poweredEMT));
			} else {
				//Indicates if the information is from EMT service
				text_info1.setText(context.getResources().getString(R.string.scheduled));
			}
			text_info1.postInvalidate();
			// tittle of the list
			TextView myTitle = (TextView) view.findViewById (R.id.name_list);
			// value of tittle
			if (busStation.getBusStations() != null){
				myTitle.setText(context.getResources().getString(R.string.transfer) + " (" + (busStation.getBusStations().size()-1) + ")");
			} else {
				myTitle.setText(context.getResources().getString(R.string.transfer) + " (" + context.getResources().getString(R.string.bydefault0) + ")");			
			}
			myTitle.postInvalidate();
				
			// force refresh of view
		}

    }
    
    public void setViewFromFragment(View view){
    	this.view = view;
    }
        
}
    
