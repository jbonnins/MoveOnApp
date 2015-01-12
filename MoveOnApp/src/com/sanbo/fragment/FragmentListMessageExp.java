/*
 * Copyright 2013 Juan Jos� Bonn�n Sans� (jbonnins@uoc.edu)
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

package com.sanbo.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.sanbo.datamodel.*;
import com.sanbo.moveonapp.R;
import com.sanbo.utils.Config;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.MenuItem;
import com.sanbo.adapter.ListViewAdapterMessage;

public class FragmentListMessageExp extends SherlockFragment {
	@SuppressWarnings("unused")
	private static final String TAG = "ListMessageFix";
	
	private View _fragmentMessageExp;
	
    ArrayList<Message> myMessages;
    ListView list;
    ListViewAdapterMessage adapter;
 
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "FragmentListMessage.OnActivityCreated()");
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "FragmentListMessage.OnCreate()");
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "FragmentListMessage.OnCreate()");
        _fragmentMessageExp = inflater.inflate(R.layout.list_generic, container, false);
        if (_fragmentMessageExp == null)
            return null;

        // Generate sample data
        PublicTransport myPublic;
        myPublic = PublicTransport.getInstance();

        // Locate the ListView in fragmenttab1.xml
        list = (ListView) _fragmentMessageExp.findViewById(R.id.list);
 
        // Pass results to ListViewAdapter Class
        adapter = new ListViewAdapterMessage(getActivity(), myPublic.getListAllMessages());
        // Binds the Adapter to the ListView
        list.setAdapter(adapter);
        
        // here we have la click listener, but not integrated with pager

		return _fragmentMessageExp;
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "FragmentListMessage.onOptionItemSelected()");
		if (Config.DEBUG) Log.i(Config.LOGTAG, "Menu Item " + item.getTitle());
/*		// First tab only Home - Settings - About...
        if (item.getTitle().equals(getString(R.string.action_settings))) {
        	// show settings activity
            showSettingsActivity();
            return true;
        }
        if (item.getItemId() == android.R.id.home) {
        	// send to parent option menu selected
            return super.onOptionsItemSelected(item);
        }
*/        
		return false;
    }
    
    /**
     * Showing settings activity
     */
/*    @SuppressWarnings("unused")
	private void showSettingsActivity() {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "FragmentListMessage.showSettingsActivity()");

    	// TODO
        Toast.makeText(getActivity(),this.getResources().getString(R.string.action_settings_show), Toast.LENGTH_SHORT).show();
    }
*/ 
}