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

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.sanbo.database.DBHelper;
import com.sanbo.datamodel.PublicTransport;
import com.sanbo.enumerated.TypeLanguage;
import com.sanbo.notifier.MoveOnCroutonStyle;
import com.sanbo.utils.Config;

import de.keyboardsurfer.android.widget.crouton.Crouton;

public class LoadingActivity extends SherlockFragmentActivity
{
  private Context context = this;
  private static TypeLanguage mLang;	
  private static boolean isLoading = true;
  private final Handler mHandler = new Handler();
  private PublicTransport myPublic = PublicTransport.getInstance();
  
  private final Runnable mUpdateResults = new Runnable()
  {
    public void run()
    {
      LoadingActivity.this.updateUI();
    }
  };
  private TextView textCarga;
  private final int textLoadTime = 800;

  private void set_SharedPreferences(){
	if (Config.DEBUG) Log.d(Config.LOGTAG, "Loading_Activity.set_SharedPreferences()");
    try {
    	myPublic.getmPreferences().setmContext(context);
    }
	catch (Exception e){
		Log.e(Config.LOGTAG, "Loading_Activity.set_Option_Meter");
	}
  }
	  
private void set_OptionMeter()
  {
	if (Config.DEBUG) Log.d(Config.LOGTAG, "Loading_Activity.set_OptionMeter()");
    // Retrieve preferences		

	//getting the distance from SharedPreferences, by default is 0
    try {
    	// Initialize context and meter in PublicTransport
    	myPublic.getmPreferences().setmContext(context);
    	// the distance in preferences to get DATA
    	myPublic.setDistance(myPublic.getmPreferences().getOptionMeter());
    }
	catch (Exception e){
		Log.e(Config.LOGTAG, "Loading_Activity.set_Option_Meter");
	}
  }
    
private void set_LoadingText()
    {
  	if (Config.DEBUG) Log.d(Config.LOGTAG, "Loading_Activity.set_LoadingText()");
      // Retrieve preferences
      SharedPreferences pref = PreferenceManager
              .getDefaultSharedPreferences(this);
      
      //getting the language from SharedPreferences, by default is "en"
      try {
      	String tmp = pref.getString(Config.language_options, "");
      	if (tmp == ""){
      		mLang = TypeLanguage.ENGLISH;
      	} else {
      		mLang = TypeLanguage.typeOf(tmp);
      	}
      	myPublic.getmPreferences().setOptionLanguage(mLang);
      }
  	catch (Exception e){
  		Log.e(Config.LOGTAG, "MainActivity.onCreate setting_language_preference");
  	}

    new Thread()
    {
      public void run()
      {
        try
        {
          while (isLoading)
          {
            sleep(textLoadTime);
            LoadingActivity.this.mHandler.post(LoadingActivity.this.mUpdateResults);
          }
        }
        catch (InterruptedException localInterruptedException)
        {
      		Log.e(Config.LOGTAG, "LoadingActivity.run()");       	
        }
      }
    }
    .start();
  }

  private void updateUI()
  {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "updateUI()");
	    int n = ((int)(java.lang.Math.random() * (-1 + getResources().getStringArray(R.array.array_gettingData).length)));
	    if (n<0 || n> getResources().getStringArray(R.array.array_gettingData).length)
	    	n=0;
		this.textCarga.setText(getResources().getStringArray(R.array.array_gettingData)[n]);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
	if (Config.DEBUG) Log.d(Config.LOGTAG, "Loading_Activity.onCreate()");    
    setContentView(R.layout.activity_loading);
    this.textCarga = ((TextView)findViewById(R.id.loadingMessage));
    set_SharedPreferences();
    set_LoadingText();
    set_OptionMeter();
    new CargaBaseDatos().execute(new Void[0]);
  }
  
	@Override
	protected void onDestroy() {
		super.onDestroy();
	    if (Config.DEBUG) Log.d(Config.LOGTAG, "LoadingActivity.onDestroy()");
	    Crouton.clearCroutonsForActivity(LoadingActivity.this);
	} 

  private class CargaBaseDatos extends AsyncTask<Void, Void, Boolean>
  {
	  
    private CargaBaseDatos()
    {
    }

	@Override
	protected Boolean doInBackground(Void... params) {
	  Boolean val = false;
	  if (Config.DEBUG) Log.d(Config.LOGTAG, "Loading_Activity.CargaBaseDatos.doInBackgroud()");
	  
      try
      {
    	  
        DBHelper.getInstance().createPublicTransport(LoadingActivity.this, myPublic.getmPreferences().getOptionLanguage(), myPublic.getmPreferences().getOptionMeter());
        val = true;
      }
      catch (Exception localException)
      {
    	  Log.e(Config.LOGTAG, "LoadingActivity.doInBackground");
    	  cancel(true);
      }
      return val;
    }

    protected void onPostExecute(Boolean mBoolean)
    {
      if (mBoolean.booleanValue()) {
    	isLoading = false;  
        startActivity(new Intent(LoadingActivity.this.getApplicationContext(), TabsActivity.class));
        //closing Activity_loading
        finish();
      }else {
        if (Config.DEBUG) Toast.makeText(LoadingActivity.this.getApplicationContext(), LoadingActivity.this.getResources().getString(R.string.error_loadingDB), Toast.LENGTH_SHORT).show();
    	MoveOnCroutonStyle.croutonAlert(LoadingActivity.this, R.string.error_loadingDB);
      }
      return;
    }

  }
}

