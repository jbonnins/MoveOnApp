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

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
//import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.sanbo.moveonapp.R;
import com.sanbo.utils.Config;
 
//define your custom com.sanbo.adapter
public class ListViewAdapterItinerary extends BaseAdapter {
 
    // Declare Variables
    Context context;
    String[] code;
    String[] name;
    int[] imageitinerary;
    //boolean[] isfavourite;
    LayoutInflater inflater;
 
 
    public ListViewAdapterItinerary(Context context, String[] code, String[] name,
            int[] imageitinerary) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "ListViewItinerary.ListViewAdapterItinerary(Context, code[] , name[], imageitinerary[])");
		// without checkbox
        this.context = context;
        this.code = code;
        this.name = name;
        this.imageitinerary = imageitinerary;
    }

    public int getCount() {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "ListViewItinerary.getCount()");
        return code.length;
    }
 
    public Object getItem(int position) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "ListViewItinerary.getItem(position)");
        return null;
    }
 
    public long getItemId(int position) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "ListViewItinerary.getItemId(position)");
        return position;
    }
    
    //class for caching the views in a row  
    private class ViewHolder
    {
        // Declare Variables
        TextView txtcode;
        TextView txtname;
        ImageView imgitinerary;
        //CheckBox checkbox;
    }
    
 
    public View getView(int position, View convertView, ViewGroup parent) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "ListViewItinerary.getView( int, View, ViewGroup)");

        // Declare Variables
    	ViewHolder viewHolder=new ViewHolder();
 
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
        View itemView = inflater.inflate(R.layout.copy_of_list_itinerary, parent, false);
 
        // Locate the TextViews in listview_item.xml
        viewHolder.txtcode = (TextView) itemView.findViewById(R.id.itinerarycode2);
        viewHolder.txtname = (TextView) itemView.findViewById(R.id.itineraryname2);
        // Locate the ImageView in listview_item.xml
        viewHolder.imgitinerary = (ImageView) itemView.findViewById(R.id.itineraryimage2);
        //viewHolder.checkbox = (CheckBox) itemView.findViewById(R.id.itinerarycheckbox2);
 
        // Capture position and set to the TextViews
        viewHolder.txtcode.setText(code[position]);
        viewHolder.txtname.setText(name[position]);
 
        // Capture position and set to the ImageView
        viewHolder.imgitinerary.setImageResource(imageitinerary[position]);
        //VITAL PART!!! Set the state of the 
        //CheckBox using the boolean array
        //viewHolder.checkbox.setChecked(isfavourite[position]);
        
/*        //VITAL PART!!! Set the state of the 
        //CheckBox using the boolean array
        viewHolder.checkBox.setChecked(checkBoxState[position]);
                  
               
        //for managing the state of the boolean
        /array according to the state of the
        //CheckBox
                
         viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
          
         public void onClick(View v) {
        	 if(((CheckBox)v).isChecked())
        		 checkBoxState[position]=true;
        	 else
        		 checkBoxState[position]=false;
         }
        });*/
      
        return itemView;
    }
}
