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

package com.sanbo.moveonapp;

import java.util.Locale;

import com.actionbarsherlock.app.SherlockActivity;
import com.sanbo.datamodel.PublicTransport;
import com.sanbo.enumerated.TypeLanguage;
import com.sanbo.sinchronizable.SynchronizableActivity;
import com.sanbo.utils.Config;

import de.keyboardsurfer.android.widget.crouton.Crouton;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

public class MainActivity extends SherlockActivity implements SynchronizableActivity {
    // Duración en milisegundos que se mostrará el splash
    private final int DURACION_SPLASH = 500; // 1 segundo
    // Data we need
    private TypeLanguage myLang;
    private SharedPreferences    pref = null;    
    private PublicTransport myPublic = PublicTransport.getInstance();
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	int savedVersionNumber;
    	SharedPreferences myShared;
    	PackageInfo pi;
    	String initPrefs     		= "moveonapp_init_prefs";
    	String versionNumber 		= "version_number";
    	int currentVersionNumber	= 0;
        super.onCreate(savedInstanceState);
		if (Config.DEBUG) Log.d(Config.LOGTAG, "MainActivity.onCreate()");
		// MoveOnApp Preferences
    	pref = PreferenceManager.getDefaultSharedPreferences(this);
    	myPublic.getmPreferences().setmContext(this);
		myShared = getSharedPreferences(initPrefs, Context.MODE_PRIVATE);
		savedVersionNumber = myShared.getInt(versionNumber, 0);
		
		try {
	        pi = getPackageManager().getPackageInfo(getPackageName(), 0);
	        currentVersionNumber = pi.versionCode;
	    } catch (Exception e) {}
	    if (currentVersionNumber > savedVersionNumber) {
	    	//Test data from SharedPreferences and first use
	    	
	        Editor editor   = pref.edit();
	        editor.putString(Config.meter_options, Config.MyDISTANCE);
	        editor.putString(Config.user_options, Config.MyUSER);
	        editor.putString(Config.route_options, Config.MyROUTE);
	        editor.putString(Config.language_options, Config.MyLANGUAGE);
	        editor.putBoolean(Config.show_options, Config.isTRUE);    
	        editor.putBoolean(Config.transfer_options, Config.isTRUE);    
	        editor.commit();
	        
	    	// Here we can show in next versions NEW FEATURES	        
	    	//new NewFeaturesDialog(this).show();
	        
	        Editor editor0   = myShared.edit();
	        editor0.putInt(versionNumber, 0);
	        editor0.putInt(versionNumber, currentVersionNumber);
	        editor0.commit();
	    }
	    // force to refresh preferences
        myPublic.getmPreferences().set_check();
        //getting the language from SharedPreferences, by default is "en"
        try {
        	String tmp = pref.getString(Config.language_options, "");
        	if (tmp == ""){
        		myLang = TypeLanguage.ENGLISH;
        	} else {
        		myLang = TypeLanguage.typeOf(tmp);
        	}
        	Locale myLocale = new Locale(myLang.toString());
            Locale.setDefault(myLocale);
	        android.content.res.Configuration config = new android.content.res.Configuration();
	        config.locale = myLocale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());       	
        }
    	catch (Exception e){
    		Log.e(Config.LOGTAG, "MainActiviti.onCreate setting_language_preference");
    	}
        // Tenemos una plantilla llamada activity_main.xml donde mostraremos la información que queramos (logotipo, etc.)
        setContentView(R.layout.activity_main);
 
        new Handler().postDelayed(new Runnable(){
            public void run(){
            // Cuando pasen los 1 segundo, pasamos a la actividad principal de la aplicación
        	Intent intent = new Intent(MainActivity.this, LoadingActivity.class);
        	startActivity(intent);
        	finish();
            };
        }, DURACION_SPLASH);
    }
    
    @Override
    public void onRestart() {
    	super.onRestart();
    	int savedVersionNumber;
    	SharedPreferences myShared;
    	PackageInfo pi;
    	String initPrefs     		= "moveonapp_init_prefs";
    	String versionNumber 		= "version_number";
    	int currentVersionNumber	= 0;
		if (Config.DEBUG) Log.d(Config.LOGTAG, "MainActivity.onCreate()");
		// MoveOnApp Preferences
    	pref = PreferenceManager.getDefaultSharedPreferences(this);
    	myPublic.getmPreferences().setmContext(this);
		myShared = getSharedPreferences(initPrefs, Context.MODE_PRIVATE);
		savedVersionNumber = myShared.getInt(versionNumber, 0);
		try {
	        pi = getPackageManager().getPackageInfo(getPackageName(), 0);
	        currentVersionNumber = pi.versionCode;
	    } catch (Exception e) {}
	    if (currentVersionNumber > savedVersionNumber) {
	    	//Test data from SharedPreferences and first use
	    	
	        Editor editor   = pref.edit();
	        editor.putString(Config.meter_options, Config.MyDISTANCE);
	        editor.putString(Config.user_options, Config.MyUSER);
	        editor.putString(Config.route_options, Config.MyROUTE);
	        editor.putString(Config.language_options, Config.MyLANGUAGE);
	        editor.putBoolean(Config.show_options, Config.isTRUE);    
	        editor.putBoolean(Config.transfer_options, Config.isTRUE);    
	        editor.commit();
	        
	    	// Here we can show in next versions NEW FEATURES	        
	    	//new NewFeaturesDialog(this).show();
	        
	        Editor editor0   = myShared.edit();
	        editor0.putInt(versionNumber, 0);
	        editor0.putInt(versionNumber, currentVersionNumber);
	        editor0.commit();
	    }
	    // force to refresh preferences
        myPublic.getmPreferences().set_check();
        //getting the language from SharedPreferences, by default is "en"
        try {
        	String tmp = pref.getString(Config.language_options, "");
        	if (tmp == ""){
        		myLang = TypeLanguage.ENGLISH;
        	} else {
        		myLang = TypeLanguage.typeOf(tmp);
        	}
        	Locale myLocale = new Locale(myLang.toString());
            Locale.setDefault(myLocale);
	        android.content.res.Configuration config = new android.content.res.Configuration();
	        config.locale = myLocale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());       	
        }
    	catch (Exception e){
    		Log.e(Config.LOGTAG, "MainActiviti.onCreate setting_language_preference");
    	}
        // Tenemos una plantilla llamada activity_main.xml donde mostraremos la información que queramos (logotipo, etc.)
        setContentView(R.layout.activity_main);
 
        new Handler().postDelayed(new Runnable(){
            public void run(){
            // Cuando pasen los 1 segundo, pasamos a la actividad principal de la aplicación
        	Intent intent = new Intent(MainActivity.this, LoadingActivity.class);
        	startActivity(intent);
        	finish();
            };
        }, DURACION_SPLASH);
    }
    
    
		    
    @Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
    	getSupportMenuInflater().inflate(R.menu.main, menu);
    	return true;
	}
       
	@Override
	protected void onDestroy() {
		super.onDestroy();
	    if (Config.DEBUG) Log.d(Config.LOGTAG, "LoadingActivity.onDestroy()");
	    Crouton.cancelAllCroutons();
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
	public FragmentActivity getSynchronizableActivity() {
		// TODO Auto-generated method stub
		return null;
	} 

}
