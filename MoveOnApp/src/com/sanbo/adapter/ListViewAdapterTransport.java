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
import com.sanbo.datamodel.Transport;
import com.sanbo.image.Image;
import com.sanbo.moveonapp.R;
import com.sanbo.notifier.MoveOnCroutonStyle;
import com.sanbo.utils.Config;
import com.sanbo.enumerated.TypeTransport;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("InflateParams")
public class ListViewAdapterTransport extends BaseAdapter {
    protected static final String TAG = "ListViewAdapterTransport";

    // Declare Variables
    private Context context;
    private Activity activity;

	/** A list containing some sample data to show. */
	private ArrayList<Transport> myTransports;
    /** Our images for each item in the list */
    int[] myType_of_transport;
    PublicTransport myPublic = PublicTransport.getInstance();
    //my selection
    long mySelection;
	
    private class ViewHolder {
	   TextView holder_type_name;
	   CheckBox holder_check_favorite;
	   ImageView holder_type_image;
	}
    
    public ArrayList<Transport> getTransportList(){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "ListViewAdapterTransport.getTransportList()");    		 	 	 		
    	return myTransports;	
    }

    public String getTypeTransport(Transport myTransport){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "ListViewAdapterTransport.getTypeTransport(Transport myTransport)");    		 	 	 		
		return context.getResources().getStringArray(R.array.type_of_transport)[myTransport.getFK_id_TypeTransport()];	
    }

    public Drawable getImageTypeTransport(int position){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "ListViewAdapterTransport.Drawable getImageTypeTransport(int position)");    		 	 	 		
		return context.getResources().getDrawable(myType_of_transport[position]);
    }
    
    public ListViewAdapterTransport(){
    	
    }

    public ListViewAdapterTransport(Context context, ArrayList<Transport> myTransports) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "ListViewAdapterTransport(Context, ArrayList<Message>)");    		 	 	 		
    	this.myTransports = myTransports;
    	if (myTransports != null){
    		myType_of_transport = new int[myTransports.size()];
    		for (int i = 0; i< myTransports.size(); i++){
    			myType_of_transport[i] = Image.getImageTransport( context, 
    						TypeTransport.getTypeTransport(myTransports.get(i).getFK_id_TypeTransport())); 		
    		}
    	} else {
    		myType_of_transport = null;
    	}
    	this.context = context;
    	activity = (Activity) context;
    	mySelection = 0;
    	
     }
 
    // How many items are in the data set represented by this Adapter
    @Override
    public int getCount() {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "ListViewAdapterTransport.getCount()");    		 	 	 		
    	return myTransports.size();
    }
 
    // Get the data item associated with the specified position in the data set
    @Override
    public Object getItem(int position) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "ListViewAdapterTransport.getItem(position)");    		 	 	 		
    	return myTransports.get(position);
    }
 
    // Get the row id associated with the specified position in the list.
    @Override
    public long getItemId(int position) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "ListViewAdapterTransport.getItemId(position)");    		 	 	 		
    	return myTransports.get(position).get_id();
    }
 
	// Get a View that displays the data at the specified position in the data set.
	// You can either create a View manually or inflate it from an XML layout file. 
    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "ListViewAdapterTransport.getView(int position, View view, ViewGroup viewGroup)");    		 	 	 		
        // Declare Variables
		ViewHolder holder = null;
	    final View item;

	    if(convertView == null){
	        LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        item = inflator.inflate(R.layout.transport_item, null);

		    holder = new ViewHolder();
		    holder.holder_type_name = (TextView) item.findViewById(R.id.text_typetransport);
		    holder.holder_type_image = (ImageView) item.findViewById(R.id.image_typetransport);		    
		    holder.holder_check_favorite = (CheckBox) item.findViewById(R.id.check_favorite);
	        item.setTag(holder);
	    } else {
	    	item = (View) convertView;
	    	holder = (ViewHolder)convertView.getTag();
	    	holder.holder_check_favorite.setTag(myType_of_transport[position]);
	    }
        holder.holder_check_favorite.setTag(myTransports.get(position));		 
		holder.holder_type_name.setText(getTypeTransport(myTransports.get(position)));
		holder.holder_type_image.setImageDrawable(getImageTypeTransport(position));
		holder.holder_check_favorite.setChecked(myTransports.get(position).isFavouriteTransport());
		// we add each selected transport
		if (myTransports.get(position).isFavouriteTransport()) mySelection = mySelection + (1 << getItemId(position));
		
		holder.holder_check_favorite.setOnClickListener(new OnClickListener() {
			  @Override
			  public void onClick(View v) {
		                //is chkIos checked?
				  CheckBox cb = (CheckBox) v;
				  Transport tr = (Transport) v.getTag();
				  int pos = tr.get_id();
				  if (cb.isChecked()){
					  savingDataToPublicTransport(tr, true);
					  mySelection = mySelection + (1 << pos);
					  if (Config.DEBUG) Toast.makeText(context, context.getResources().getString(R.string.checked) +
							  " " + context.getResources().getStringArray(R.array.type_of_transport)[tr.getFK_id_TypeTransport()]
							  , Toast.LENGTH_LONG).show();
					  MoveOnCroutonStyle.croutonConfirm(activity, context.getResources().getString(R.string.unchecked) +
							  " " + context.getResources().getStringArray(R.array.type_of_transport)[tr.getFK_id_TypeTransport()]);
				  }else{ 
					  savingDataToPublicTransport(tr, false);
					  mySelection = mySelection - (1 << pos);
					  if (Config.DEBUG) Toast.makeText(context, context.getResources().getString(R.string.unselected) +
							  context.getResources().getStringArray(R.array.type_of_transport)[tr.getFK_id_TypeTransport()]
							  , Toast.LENGTH_LONG).show();
					  MoveOnCroutonStyle.croutonWarn(activity, context.getResources().getString(R.string.unselected) +
							  context.getResources().getStringArray(R.array.type_of_transport)[tr.getFK_id_TypeTransport()]);
					  
				  }
			  }
		});		
		
		return(item);
	}
    
    public void updateResults(ArrayList<Transport> newTransports) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "ListViewAdapterTransport. updateResults(ArrayList<Transport> newTransports)");    		 	 	 		
        myTransports = newTransports;
    	if (newTransports != null){
    		myType_of_transport = new int[newTransports.size()];
    		for (int i = 0; i< newTransports.size(); i++){
    			myType_of_transport[i] = Image.getImageTransport( context, 
    						TypeTransport.getTypeTransport(newTransports.get(i).getFK_id_TypeTransport())); 		
    		}
    	} else {
    		myType_of_transport = null;
    	}
        
        //Triggers the list update
        notifyDataSetChanged();
    }
    
    private void savingDataToPublicTransport(Transport myTransport, boolean isFavorite){
    	//nothing to save, all info is in PublicTransport
    	myPublic.setIsFavoriteTransport(myTransport, isFavorite);
    	if (myPublic.setIsFavoriteTransport(myTransport, isFavorite)){
     		if (isFavorite){
	  			  MoveOnCroutonStyle.croutonConfirm(activity, context.getResources().getString(R.string.checked) +
	  					" " + context.getResources().getString(R.string.type_of_transport) + " " + 
	  					  TypeTransport.getTypeTransport(myTransport.getFK_id_TypeTransport()).name());
     		} else {
    			  MoveOnCroutonStyle.croutonInfo(activity, context.getResources().getString(R.string.unchecked) +
    	  				" " + context.getResources().getString(R.string.station) + " " + 
    					  TypeTransport.getTypeTransport(myTransport.getFK_id_TypeTransport()).name());
     		}
        } else {
    		  MoveOnCroutonStyle.croutonWarn(activity, context.getResources().getString(R.string.error_database));    		
        }
    }
    
    public long getMySelection(){
    	return mySelection;
    }

}
    
