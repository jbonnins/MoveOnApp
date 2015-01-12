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

package com.sanbo.fragment.tabD;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.sanbo.adapter.ListViewAdapterFavorites;
import com.sanbo.fragment.base.BaseFragment;
import com.sanbo.moveonapp.MainActivity;
import com.sanbo.moveonapp.R;
import com.sanbo.sinchronizable.SynchronizableActivity;
import com.sanbo.utils.Config;
import com.sanbo.utils.SavingState;

import de.keyboardsurfer.android.widget.crouton.Crouton;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class AppTabDFirstFragment extends BaseFragment implements SynchronizableActivity{
	@SuppressWarnings("unused")
	private static final String TAG = "AppTabD";
	private SherlockFragmentActivity mContext;
	private TextView isEmpty;
	private ProgressBar myProgressBar;
	private TextView myShowMessage;
	private ListView mListView;
	private ListViewAdapterFavorites adapter;
	private SavingState mySave = SavingState.getInstance();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (Config.DEBUG) Log.d(Config.LOGTAG, "AppTabDFirstFragment.onCreate(Bundle savedInstanceState)");
		mContext = this.getSherlockActivity();

		setHasOptionsMenu(true);
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "AppTabDFirstFragment.onCreateView(" +
				"LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)");
        View view       =   inflater.inflate(R.layout.list_generic, container, false);
        
		mListView = (ListView) view.findViewById(R.id.list);
		// divider for elements
		mListView.setDivider(mContext.getResources().getDrawable(R.drawable.line));

		myProgressBar = (ProgressBar) view.findViewById(R.id.progress);
		myShowMessage = (TextView) view.findViewById(R.id.showmessage);
		// tittle of the list
		TextView myTittle = (TextView) view.findViewById (R.id.name_list);
		
		// Data of the Line

		
		// value of tittle
		myTittle.setText(R.string.favourits);
		
		isEmpty = (TextView) view.findViewById (R.id.empty);

		//Execute asyntask (we can send string array)
		new BackgroundTask().execute("SOMETHING");
		
        return view;
    }
	
    @Override
	public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
		if (Config.DEBUG) Log.d(Config.LOGTAG, "AppTabDFirstFragment.onSaveInstanceState(Bundle outState)");
        //Our state is saved on PublicTransport and in our DDBB
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
    	super.onRestoreInstanceState(savedInstanceState);
		if (Config.DEBUG) Log.d(Config.LOGTAG, "AppTabDFirstFragment.onRestoreInstanceState(Bundle savedInstanceState)");
    } 
    
    @Override
	public void onResume() {
        super.onResume();
		if (Config.DEBUG) Log.d(Config.LOGTAG, "AppTabDFirstFragment.onResume()");
    }

	private class BackgroundTask extends AsyncTask<String, Void, String> {
		// What we need in temp loader
	  
	

	@Override
	protected void onPreExecute()
	{
		  //showing progressbar
		  myProgressBar.setVisibility(View.VISIBLE);
		  myShowMessage.setVisibility(View.VISIBLE);

	}
	
	@Override
	protected String doInBackground(String... urls) 
	{			
	    try
		{
	    	// we get data in Line throw PublicTransport
	    	String[] myListFavorite = mContext.getResources().getStringArray(R.array.mytype_of_favorit);
	    	String[] myNameFavorite = mContext.getResources().getStringArray(R.array.type_of_favorit);
	    	
	    	//our task
	    	adapter = new ListViewAdapterFavorites(mContext, mListView, myListFavorite, myNameFavorite);
			
		} catch (Exception e)
		{
			Log.e(MainActivity.class.toString(), e.getMessage());
		}
			//on post execute we can send an String	
			return "moveOn Favorite";
	 }

	 @Override
	 protected void onPostExecute(String result) 
	 {
	    //Asyntask is finished, we hide progressbar
		 myProgressBar.setVisibility(View.GONE);
		 myShowMessage.setVisibility(View.GONE);
	
		 //now we can show our list or simply working
		 if (adapter != null && adapter.getCount() > 0)
		 {
			 mListView.setAdapter(adapter);
			 //show list
			 mListView.setVisibility(View.VISIBLE);
			 //unshow empty message
			 isEmpty.setVisibility(View.GONE);
			 mListView.clearFocus();
		 }
		 else if (adapter != null){
		 	//show empty message
		 	isEmpty.setVisibility(View.VISIBLE);
		 	if (mySave.isFavoriteOn()){
		 		isEmpty.setText(R.string.emptyFavoriteList);
		 	}
		 }
		 //if not, we show an error
		 else
		 {
			 	//show empty message
			 	isEmpty.setVisibility(View.VISIBLE);
			 	//dialog error database
			 	Builder builder = new Builder(mContext);
				builder.setTitle(R.string.title_error);
				builder.setIcon(android.R.drawable.ic_dialog_info);
				builder.setMessage(R.string.error_database);					
				builder.setNeutralButton(getString(R.string.button_ok), null);			

				AlertDialog alertDialog = builder.create();
				alertDialog.show();
				alertDialog.setCancelable(false);
		 }
	  }
	}
	
	@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
    	synchronized(this){
    		// settings is not enables, we don't show transfer by default
    		menu.findItem(R.id.menu_settings).setVisible(false);
    	}
        super.onCreateOptionsMenu(menu, inflater);
    }
	    
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
	    if (Config.DEBUG) Log.i(Config.LOGTAG, "IN " + String.valueOf(item.getItemId()));
    	switch (item.getItemId()) {
	    case android.R.id.home:
	    	return true;

         default:
    	    if (Config.DEBUG) Log.i(Config.LOGTAG, "OUT DEF " +String.valueOf(item.getItemId()));
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
		if (Config.DEBUG) Log.d(Config.LOGTAG, "AppTabDFirstFragment.onDestroy()");
        //not necessary because we always save our new data to DB
	    Crouton.clearCroutonsForActivity(getSherlockActivity());
    }
    
	@Override
	public void onSuccessfulNetworkSynchronization() {
		// TODO Auto-generated method stub	
	}

	@Override
	public void onUnsuccessfulNetworkSynchronization() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onLocationSynchronization() {
		// TODO Auto-generated method stub
	}

	@Override
	public SherlockFragmentActivity getSynchronizableActivity() {
		// TODO Auto-generated method stub
		return getSherlockActivity();
	}
    
   
}
