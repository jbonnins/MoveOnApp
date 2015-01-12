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

import com.sanbo.image.ImageLanguage;
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

public class ListViewAdapterLanguage extends BaseAdapter {

    // Declare Variables
    Context context;
    ArrayList<ImageLanguage> myLang;
    LayoutInflater inflater;
 
    public ListViewAdapterLanguage(Context context, ArrayList<ImageLanguage> language) {
    	myLang = language;
    	this.context = context;
    }
 
    // How many items are in the data set represented by this Adapte
    @Override
    public int getCount() {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "istViewAdapterLanguage.getCount()");    		 	 	 		
    	return myLang.size();
    }
 
    // Get the data item associated with the specified position in the data set
    @Override
    public Object getItem(int position) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "istViewAdapterLanguage.getItem(position)");    		 	 	 		
    	return myLang.get(position);
    }
 
    // Get the row id associated with the specified position in the list.
    @Override
    public long getItemId(int position) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "istViewAdapterLanguage.getItemId(position)");    		 	 	 		
    	return position;
    }
 
	// Get a View that displays the data at the specified position in the data set.
	// You can either create a View manually or inflate it from an XML layout file. 
    public View getView(int position, View convertView, ViewGroup parent) {
 
        // Declare Variables
        TextView txtcountry;
        ImageView imgflag;
 
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
        View itemView = inflater.inflate(R.layout.language_item, parent, false);
 
        // Locate the TextViews in listview_item.xml
        txtcountry = (TextView) itemView.findViewById(R.id.language_option);
        // Locate the ImageView in listview_item.xml
        imgflag = (ImageView) itemView.findViewById(R.id.flag_option);
 
        // Capture position and set to the TextViews
        txtcountry.setText(myLang.get(position).getLanguage());
 
        // Capture position and set to the ImageView
        imgflag.setImageResource(myLang.get(position).getImageLanguage());
 
        return itemView;
    }
}
	
	
