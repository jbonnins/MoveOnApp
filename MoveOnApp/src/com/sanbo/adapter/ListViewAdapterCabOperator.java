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

import com.sanbo.datamodel.Cab;
import com.sanbo.datamodel.CabOperator;
import com.sanbo.datamodel.PublicTransport;
import com.sanbo.fragment.base.AppConstants;
import com.sanbo.fragment.tabE.AppTabESecondFragment;
import com.sanbo.utils.Config;
import com.sanbo.utils.SavingState;
import com.sanbo.moveonapp.R;
import com.sanbo.notifier.MoveOnCroutonStyle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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


public class ListViewAdapterCabOperator extends BaseAdapter {
    protected static final String TAG = "ListViewAdapterCab";

	public static final int INVALID_ROW_ID = -1;

    // Declare Variables
	private Context context;
	private Activity activity;
	private ListView mListView;
	
	/** A list containing some sample data to show. */
	private ArrayList<CabOperator> myCabOperators = null;
	
	/** Our model of public transport. */
	PublicTransport myPublic = PublicTransport.getInstance();
	
	// Data to next activity
	private SavingState savingState = SavingState.getInstance();
  
	private class ViewHolder {	 

    	//RelativeLayout holder_groupItem; //groupitem
    	CheckBox holder_check_station_favorite; //check_favorite_station
    	TextView holder_station_name; //cab_code_name
    	TextView holder_telephone; //telephone 
    	TextView holder_web_address; //web_address
	}
	
  	public ArrayList<CabOperator> getCabList(){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "ListViewAdapterCabOperator.getCabList()");
		// we get data of Stations from our DB
		return myCabOperators;
	}

 	public ListViewAdapterCabOperator(){
    	
    }

    public ListViewAdapterCabOperator(Context pContext, ListView pListView, ArrayList<CabOperator> mCabOperatorList) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "ListViewAdapterCabOperator(Context pContext, ListView pListView, ArrayList<CabOperator> mCabOperatorList)");    		 	 	 		
    	myCabOperators = mCabOperatorList;
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
				Cab cb = (Cab) textview.getTag();
				savingState.setMyCabOperator(cb);
				savingState.setItemCabOperator(position);
		        savingState.getMyActivity().pushFragments(AppConstants.TAB_D, 
		            		new AppTabESecondFragment(),true,true);					
			}
	    });
	}
    
    // How many items are in the data set represented by this Adapter
    @Override
    public int getCount() {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "ListViewAdapterCabOperator.getCount()");
		return myCabOperators.size();
    }

    // Get the data item associated with the specified position in the data set
    @Override
    public CabOperator getItem(int position) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "ListViewAdapterCabOperator.getItem(position)");
		return myCabOperators.get(position);
    }
    
    // Get the id of the station of the line associated with the specified position in the list.
    @Override
    public long getItemId(int position) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "ListViewAdapterCabOperator.getItemId(position)");
		return myCabOperators.get(position).get_id();
    }

    // Get a View that displays the data at the specified position in the data set.
	// You can either create a View manually or inflate it from an XML layout file. 
    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "ListViewAdapterCabOperator.getView(int position, View view, ViewGroup viewGroup)");    		 	 	 		
        // Declare Variables
		ViewHolder holder = null;
	    final View item;

	    if(convertView == null){
	    	item = LayoutInflater.from(context).inflate(R.layout.caboperator_item, mListView, false);

		    holder = new ViewHolder();
			//Get childrow.xml file elements and set values		    
		    // Name
		    holder.holder_station_name = (TextView) item.findViewById(R.id.cab_code_name);
		    // Station tag
			holder.holder_station_name.setTag(getItem(position));
		    // telephone
		    holder.holder_telephone = (TextView) item.findViewById(R.id.telephone);
		    holder.holder_telephone.setTag(getItem(position));
			holder.holder_telephone.setOnClickListener(new OnClickListener() {
		    	public void onClick(View v){
		    		
		    		//Se obtiene campo teléfono introducido por el usuario
		    		TextView tv = (TextView) v;
		    		CabOperator co = (CabOperator) tv.getTag();
		    		String numero = co.getTelephone();
		    		
		    		//Si no existe teléfono, se asigna teléfono de emergencias
		    		if (!numero.equals("")) {
		    		
			    		//Se parse el teléfono a URI
			    		Uri number = Uri.parse("tel:"+numero);
			    		
			    		//Se lanza el intent para llamada
			    		Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
			    		context.startActivity(callIntent);
		    		}
		    	}
		    });
			
		    // web
		    holder.holder_web_address = (TextView) item.findViewById(R.id.web_address);
		    holder.holder_web_address.setTag(getItem(position));
			holder.holder_web_address.setOnClickListener(new OnClickListener() {
		    	public void onClick(View v){
		    		
		    		//Se obtiene campo teléfono introducido por el usuario
		    		TextView tv = (TextView) v;
		    		CabOperator co = (CabOperator) tv.getTag();
		    		String web = co.getWeb();
		    		
    		        Intent intent = new Intent();
    		        intent.setAction(Intent.ACTION_VIEW);
    		        intent.addCategory(Intent.CATEGORY_BROWSABLE);
    		        intent.setData(Uri.parse("http://" + web));
    		        context.startActivity(intent);
    		    }
    		});
			
		    // check favorite		    
		    holder.holder_check_station_favorite = (CheckBox) item.findViewById(R.id.check_favorite_station);
		    
		    //CabOperator to favorite
		    holder.holder_check_station_favorite.setTag(getItem(position));
			holder.holder_check_station_favorite.setOnClickListener(new OnClickListener() {
				  @Override
				  public void onClick(View v) {
			                //is chkIos checked?
					  CheckBox cb = (CheckBox) v;
					  CabOperator cabOperator = (CabOperator) v.getTag();
					  if (cb.isChecked()){
						  savingDataToPublicTransport(cabOperator, true);
					  }else{ 
						  savingDataToPublicTransport(cabOperator, false);
					  }
				  }

			});	
	        item.setTag(holder);  
	    } else {
	    	item = (View) convertView;	    	
	    	holder = (ViewHolder)item.getTag();    
	    	holder.holder_check_station_favorite.setTag(getItem(position));
	    	holder.holder_web_address.setTag(getItem(position));
	    	holder.holder_telephone.setTag(getItem(position));
			holder.holder_station_name.setTag(getItem(position));
	    }
	    //Filling data...
	    //Station name
		holder.holder_station_name.setText(getItem(position).getName());	
		holder.holder_telephone.setText(getItem(position).getTelephone());
		holder.holder_web_address.setText(getItem(position).getWeb());
		
	    //Station to favorite
    	holder.holder_check_station_favorite.setChecked(getIsFavorite(position));
	    return(item);
   }
    
    private boolean getIsFavorite(int position){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "ListViewAdapterCab.getIsFavorite(int position)");
    	return getItem(position).isFavorite();
    }

    public void updateResults(ArrayList<CabOperator> newCabOperators) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "ListViewAdapterCabOperator.updateResults(ArrayList<Cab> newCabs)");
		myCabOperators = newCabOperators;
        
        //Triggers the list update
        notifyDataSetChanged();
    }

    private void savingDataToPublicTransport(CabOperator myCabOperator, boolean isFavorite){
    	//nothing to save, all info is in PublicTransport
    	if (myPublic.setIsFavoriteCabOperator(myCabOperator, isFavorite)){
     		if (isFavorite){
	  			  MoveOnCroutonStyle.croutonConfirm(activity, context.getResources().getString(R.string.checked) +
	  				" " + context.getResources().getString(R.string.caboperator) + " " + myCabOperator.getName());
     		} else {
    			  MoveOnCroutonStyle.croutonInfo(activity, context.getResources().getString(R.string.unchecked) +
    	  			" " + context.getResources().getString(R.string.caboperator) + " " + myCabOperator.getName());
     		}
        } else {
    		  MoveOnCroutonStyle.croutonWarn(activity, context.getResources().getString(R.string.error_database));    		
        }
    	
    }
}
