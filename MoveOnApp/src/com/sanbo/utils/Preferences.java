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

package com.sanbo.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.sanbo.enumerated.TypeLanguage;
import com.sanbo.enumerated.TypeRoute;
import com.sanbo.enumerated.TypeUser;
import com.sanbo.moveonapp.R;

public class Preferences {
    // SINGLETON DEFINITION
	private static Preferences INSTANCE = null;
	
	// Check for preferences
	private static boolean _check = true;
	
	// Context
	private Context mContext;
	
	//Preferences
	private TypeUser optionUser = TypeUser.USER;
	private int optionMeter = 0;
	private TypeRoute optionRoute = TypeRoute.FASTER;
	private TypeLanguage optionLanguage = TypeLanguage.ENGLISH;
	private boolean optionShowMessage = true;
	private boolean optionShowTransfer = true;
	
	// Creates instance of public transport
	private synchronized static void createInstance()
	{
		if (INSTANCE == null)
			INSTANCE = new Preferences();
			
		return;
	}
	  	
	/**
	 * @return the iNSTANCE
	 */
	public static Preferences getInstance() {
		if (INSTANCE == null)
			createInstance();
		return INSTANCE;
	}

	public Preferences(){
		
	}
		
	private synchronized void refreshPreferences() {
		// Retrieve preferences
        SharedPreferences myShared = PreferenceManager
                .getDefaultSharedPreferences(mContext);
 
        if (Config.DEBUG) Log.d(Config.LOGTAG, "Preferences.refreshPreferences()");
		if (_check){
			try{
				boolean byDefault = myShared.getBoolean(Config.bydefault_options, Config.isTRUE);
				
				if (byDefault){
					optionUser = TypeUser.USER;
					optionMeter = Integer.valueOf(Config.MyDISTANCE);
					optionRoute = TypeRoute.FASTER;
				} else {
					optionUser = TypeUser.typeOf(myShared.getString(Config.user_options, Config.MyUSER));
					optionMeter = Integer.valueOf(myShared.getString(Config.meter_options, Config.MyDISTANCE));
					optionRoute = TypeRoute.typeOf( myShared.getString(Config.route_options, Config.MyROUTE));
				}	
				optionLanguage = TypeLanguage.typeOf(myShared.getString(Config.language_options, Config.MyLANGUAGE));
				optionShowTransfer = myShared.getBoolean(Config.transfer_options, Config.isTRUE); 
				optionShowMessage = myShared.getBoolean(Config.show_options, Config.isTRUE);
				_check = false;
			}
	    	catch (Exception e){
	    		Log.e(Config.LOGTAG, "Preferences.refreshPreferences()");
	    	}
		}
	}
	
	/**
	 * @param context
	 * @param myShared
	 */
	public Preferences(Context context) {
		super();
		this.mContext = context;
	}

	/**
	 * @param optionUser
	 * @param optionMeter
	 * @param optionRoute
	 * @param optionLanguage
	 * @param optionTransfer
	 */
	public Preferences(TypeUser optionUser, int optionMeter, TypeRoute optionRoute,
			TypeLanguage optionLanguage, boolean optionShowTransfer) {
		super();
		this.optionUser = optionUser;
		this.optionMeter = optionMeter;
		this.optionRoute = optionRoute;
		this.optionLanguage = optionLanguage;
		this.optionShowTransfer = optionShowTransfer;
	}
	
	public String optionMeterToString(){
		String[] myArray = mContext.getResources().getStringArray(R.array.pref_transfer_distance);
		String str;
		switch ( optionMeter ) {
			case -1:
				// Translate -1 to Unlimited
				str = myArray[4];
				break;
			default:
				str = String.valueOf(optionMeter);
				break;
		}
		return str;
	}

	/**
	 * @return the _check
	 */
	public boolean is_check() {
		return _check;
	}

	/**
	 * _check to set TRUE
	 */
	public void set_check(){
		_check = true;
	}

	/**
	 * @return the mContext
	 */
	public Context getmContext() {
		return mContext;
	}

	/**
	 * @param mContext the mContext to set
	 */
	public void setmContext(Context mContext) {
		this.mContext = mContext;
	}

	/**
	 * @return the optionUser
	 */
	public TypeUser getOptionUser() {
		if (_check) refreshPreferences();
		return optionUser;
	}

	/**
	 * @return the optionMeter
	 */
	public int getOptionMeter() {
		if (_check) refreshPreferences();
		return optionMeter;
	}

	/**
	 * @return the optionRoute
	 */
	public TypeRoute getOptionRoute() {
		if (_check) refreshPreferences();
		return optionRoute;
	}

	/**
	 * @return the optionLanguage
	 */
	public TypeLanguage getOptionLanguage() {
		if (_check) refreshPreferences();
		return optionLanguage;
	}

	/**
	 * @param optionLanguage the optionLanguage to set
	 */
	public synchronized void setOptionLanguage(TypeLanguage optionLanguage) {
		// Retrieve preferences
        SharedPreferences myShared = PreferenceManager
                .getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = myShared.edit();
        editor.putString(Config.language_options, optionLanguage.toString());
        editor.commit();
        
		this.optionLanguage = optionLanguage;
	}

	/**
	 * @return the optionShowTransfer
	 */
	public boolean isOptionShowTransfer() {
		if (_check) refreshPreferences();
		return optionShowTransfer;
	}

	/**
	 * @param optionShowTransfer the optionShowTransfer to set
	 */
	public void setOptionShowTransfer(boolean optionShowTransfer) {
		// Retrieve preferences
        SharedPreferences myShared = PreferenceManager
                .getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = myShared.edit();
        editor.putBoolean(Config.show_options, optionShowTransfer);
        editor.commit();
        
		this.optionShowTransfer = optionShowTransfer;
	}

	/**
	 * @return the optionShowMessage
	 */
	public boolean isOptionShowMessage() {
		if (_check) refreshPreferences();
		return optionShowMessage;
	}

}
