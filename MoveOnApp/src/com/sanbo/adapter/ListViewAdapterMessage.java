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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import com.sanbo.datamodel.Message;
import com.sanbo.enumerated.TypeTransport;
import com.sanbo.image.Image;
import com.sanbo.moveonapp.R;
import com.sanbo.utils.Config;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListViewAdapterMessage extends BaseAdapter {
    protected static final String TAG = "ListViewAdapterMessage";

    // Declare Variables
    Context context;
    ArrayList<Message> myMessages;
    int[] type_of_transport;
    LayoutInflater inflater;
 
    public ListViewAdapterMessage(Context context, ArrayList<Message> myMessages) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "ListViewAdapterMessage(Context, ArrayList<Message>)");    		 	 	 		
    	this.myMessages = myMessages;
    	if (myMessages != null){
    		type_of_transport = new int[myMessages.size()];
    		for (int i = 0; i< myMessages.size(); i++){
    			type_of_transport[i] = Image.getImageTransport( context, 
    						TypeTransport.getTypeTransport(myMessages.get(i).getFK_id_Transport())); 		
    		}
    	} else {
    		type_of_transport = null;
    	}
    	this.context = context;
    	
     }
 
    // How many items are in the data set represented by this Adapte
    @Override
    public int getCount() {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "ListViewAdapterMessage.getCount()");    		 	 	 		
    	return myMessages.size();
    }
 
    // Get the data item associated with the specified position in the data set
    @Override
    public Object getItem(int position) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "ListViewAdapterMessage.getItem(position)");    		 	 	 		
    	return myMessages.get(position);
    }
 
    // Get the row id associated with the specified position in the list.
    @Override
    public long getItemId(int position) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "ListViewAdapterMessage.getItemId(position)");    		 	 	 		
    	return myMessages.get(position).get_id();
    }
 
	// Get a View that displays the data at the specified position in the data set.
	// You can either create a View manually or inflate it from an XML layout file. 
    public View getView(int position, View convertView, ViewGroup parent) {
 
        // Declare Variables
    	TextView dataInici;
    	TextView dataEnd;
    	TextView textMessage;
    	TextView textMessage1;
    	TextView descMessage;
    	ImageView imgType;
 
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
        View itemView = inflater.inflate(R.layout.message_item, parent, false);
 
        // Locate the TextViews in listview_item.xml
        dataInici = (TextView) itemView.findViewById(R.id.textDateIni);
        dataEnd = (TextView) itemView.findViewById(R.id.textDateEnd);
        descMessage = (TextView) itemView.findViewById(R.id.textDescription);
        textMessage = (TextView) itemView.findViewById(R.id.textMessage);
        textMessage1 = (TextView) itemView.findViewById(R.id.textMessage1);
               // Locate the ImageView in message_item.xml
        imgType = (ImageView) itemView.findViewById(R.id.image_typetransport);
 
        // Capture position and set to the TextViews
        SimpleDateFormat format = 
                new SimpleDateFormat("EEE dd-MMM-yyyy HH:mm");
        Calendar miData = myMessages.get(position).getDateStart();
        String miFecha = format.format(miData.getTime());
        // Initial data from message
        dataInici.setText(miFecha);
        miData = myMessages.get(position).getDateEnd();
        miFecha = format.format(miData.getTime());
        dataEnd.setText(miFecha);
        String[] desc = myMessages.get(position).getMessage().split("#", 2);
        // we have "always" # in our message text
        textMessage.setText(desc[0]);
        textMessage1.setText(desc[1]);
        descMessage.setText(myMessages.get(position).getDescription());
         
        // Capture position and set to the ImageView
        imgType.setImageResource(type_of_transport[position]);
 
        return itemView;
    }
    
    public void updateResults(ArrayList<Message> newMessages) {
        myMessages = newMessages;
    	if (newMessages != null){
    		type_of_transport = new int[newMessages.size()];
    		for (int i = 0; i< newMessages.size(); i++){
    			type_of_transport[i] = Image.getImageTransport( context, 
    						TypeTransport.getTypeTransport(newMessages.get(i).getFK_id_Transport())); 		
    		}
    	} else {
    		type_of_transport = null;
    	}
        
        //Triggers the list update
        notifyDataSetChanged();
    }
}
	


	
