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


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.sanbo.fragment.base.AppConstants;
import com.sanbo.fragment.tabA.AppTabAFirstFragment;
import com.sanbo.fragment.tabB.AppTabBFirstFragment;
import com.sanbo.fragment.tabD.AppTabDFithFragment;
import com.sanbo.fragment.tabD.AppTabDFourthFragment;
import com.sanbo.fragment.tabD.AppTabDThirdFragment;
import com.sanbo.image.Image;
import com.sanbo.moveonapp.R;
import com.sanbo.moveonapp.TypeTransportActivity;
import com.sanbo.notifier.MoveOnCroutonStyle;
import com.sanbo.utils.Config;
import com.sanbo.utils.SavingState;

public class ListViewAdapterFavorites extends BaseAdapter {
    protected static final String TAG = "ListViewAdapterFavorites";

	public static final int INVALID_ROW_ID = -1;

    // Declare Variables
	private Context context;
	private Activity activity;
	private ListView mListView;
	
	/** A list containing some sample data to show. */
    private String[] favoriteType = null;
    private String[] nameFavorite = null;
    private int[] myFavorite; // our images to represent favoriteType

    // Data to next activity
	private SavingState mySave = SavingState.getInstance();

	private class ViewHolder {	 

    	// CheckBox we use to retrieve all data or just my favorites
 	    TextView holder_type_name;
	    CheckBox holder_check_favorite;
	    ImageView holder_type_image;
	}

	public String[] getFavoriteTypeList(){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "ListViewAdapterFavorites.getFavoriteTypeList())");
		// we get data of Stations from our DB
		return favoriteType;
	}

    public ListViewAdapterFavorites() {
    }

    public ListViewAdapterFavorites(Context pContext, ListView pListView, String[] pFavoriteType, String[] pNameFavorite) {
       	if (Config.DEBUG) Log.d(Config.LOGTAG, "ListViewAdapterFavorites(Context context, String[] favoriteType, String[] imageType)");    		 	 	 		
        context = pContext;
        favoriteType = pFavoriteType;
        nameFavorite = pNameFavorite;
       	if (favoriteType != null){
    		myFavorite = new int[favoriteType.length];
    		for (int i = 0; i< favoriteType.length; i++){
    			myFavorite[i] = Image.getImageFavorite( context, favoriteType[i]); 		
    		}
    	} else {
    		myFavorite = null;
    	}
   	
    	mListView = pListView; 
    	// first step
    	mySave.setFavoriteOn(true);

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
				//TextView myText = (TextView) view.findViewById(R.id.text_typetransport);
				//String myFavorite = (String) myText.getTag();
				//showNext(myFavorite, position);
				CheckBox cb = (CheckBox) view.findViewById(R.id.check_favorite);
				// if favorite is checked on selected item
				mySave.setFavoriteOn(cb.isChecked());
				// last state for favorite
				mySave.setLastFavorite(cb.isChecked());
				
				switch (position){
				
				//TypeFavorite."CAPS"
				case 0:
					mySave.getMyActivity().pushFragments(AppConstants.TAB_D, new AppTabDThirdFragment(), true, true);
					break;

				//TypeFavorite."CAPS OPERATOR"
				case 1:
					mySave.getMyActivity().pushFragments(AppConstants.TAB_D, new AppTabDFourthFragment(), true, true);
					break;

				//TypeFavorite."LINES"
				case 2:
					mySave.getMyActivity().pushFragments(AppConstants.TAB_D, new AppTabAFirstFragment(), true, true);
					break;

				//TypeFavorite."STATIONS"
				case 3:
					mySave.getMyActivity().pushFragments(AppConstants.TAB_D, new AppTabBFirstFragment(), true, true);
					break;

				//TypeFavorite."PLACES"
				case 4:
					mySave.getMyActivity().pushFragments(AppConstants.TAB_D, new AppTabDFithFragment(), true, true);
					break;

				//TypeFavorite."TRANSPORT TYPE"
				case 5:
					// Reuse previous activity, but we can show only favorites or all :)
		            context.startActivity(new Intent(context, TypeTransportActivity.class));        	
					break;


				default:
		  			  MoveOnCroutonStyle.croutonWarn(activity, context.getResources().getString(R.string.error_database));
				}
			}
	    });
	}
    
    // How many items are in the data set represented by this Adapter
    @Override
    public int getCount() {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "ListViewAdapterFavorites.getCount()");
		return favoriteType.length;
	}

    // Get the data item associated with the specified position in the data set
    @Override
    public String getItem(int position) {
    	if (Config.DEBUG) Log.d(Config.LOGTAG, "ListViewAdapterFavorites.getItem(position)");    		 	 	 		
		return favoriteType[position];
	}
	
    // Get the id of the station of the line associated with the specified position in the list.
    @Override
    public long getItemId(int position) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "ListViewAdapterStation.getItemId(position)");
		return position;
    }
	
    public Drawable getImageMyFavorite(int position){
    	if (Config.DEBUG) Log.d(Config.LOGTAG, "ListViewAdapterTransport.Drawable getImageTypeTransport(int position)");    		 	 	 		
    	return context.getResources().getDrawable(myFavorite[position]);
    }
    
    public String[] getFavoriteList(){
    	if (Config.DEBUG) Log.d(Config.LOGTAG, "ListViewAdapterTransport.getTransportList()");    		 	 	 		
    	return favoriteType;	
    }
	
	 // Get a View that displays the data at the specified position in the data set.
	 // You can either create a View manually or inflate it from an XML layout file. 
	 @SuppressLint("InflateParams")
	@Override
	 public View getView(int position, View convertView, ViewGroup viewGroup) {
	 	if (Config.DEBUG) Log.d(Config.LOGTAG, "ListViewAdapterTransport.getView(int position, View view, ViewGroup viewGroup)");    		 	 	 		
		// Declare Variables
		ViewHolder holder = null;
		final View item;
		
	    if(convertView == null){
	    	 item = LayoutInflater.from(context).inflate(R.layout.transport_item, mListView, false);
		
		     holder = new ViewHolder();
		     holder.holder_type_name = (TextView) item.findViewById(R.id.text_typetransport);
		     holder.holder_type_image = (ImageView) item.findViewById(R.id.image_typetransport);		    
		     holder.holder_check_favorite = (CheckBox) item.findViewById(R.id.check_favorite);
		     item.setTag(holder);
		} else {
		 	 item = (View) convertView;
		 	 holder = (ViewHolder)convertView.getTag();
		}
		holder.holder_type_name.setText(nameFavorite[position]);
		holder.holder_type_name.setTag(favoriteType[position]);
		holder.holder_type_image.setImageDrawable(getImageMyFavorite(position));
		// The list of favorites :)
		holder.holder_check_favorite.setChecked(true);
		 	
		return(item);
	}
}
 
	

    
    
    
    
