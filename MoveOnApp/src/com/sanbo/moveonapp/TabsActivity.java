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

import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.sanbo.adapter.CustomPagerAdapter;
import com.sanbo.adapter.ListViewAdapterLanguage;
import com.sanbo.adapter.ListViewAdapterMessage;
import com.sanbo.datamodel.PublicTransport;
import com.sanbo.enumerated.TypeLanguage;
import com.sanbo.fragment.RootFragment;
import com.sanbo.fragment.base.AppConstants;
import com.sanbo.fragment.base.AppMainTabActivity;
import com.sanbo.image.ImageLanguage;
import com.sanbo.moveonapp.widgets.CreditsDialog;
import com.sanbo.notifier.MoveOnCroutonStyle;
import com.sanbo.sinchronizable.SynchronizableActivity;
import com.sanbo.synchronizers.LocationSynchronizer;
import com.sanbo.utils.Config;
import com.sanbo.utils.SavingState;

import de.keyboardsurfer.android.widget.crouton.Crouton;


public class TabsActivity extends SherlockFragmentActivity implements SynchronizableActivity {
	@SuppressWarnings("unused")
	private static final String TAG = "TabsActivity";
	// we use to save our state between tabs, ugly solution
	private SavingState savingState = SavingState.getInstance();
	
	private boolean loaded = false;
	private boolean showLang = false;
	private boolean showDialog = false;
	//Objects to control language
	private RelativeLayout rl;
	private TextView txt_hello, txt_version, txt_textList;
	private Button button1, button2, button3, button4, button5;
	private ImageView img_icon;
	private ViewPager vp;
	private ProgressBar myProgressBar;
	private TextView isEmpty;


	private Locale myLocale;
	
	private Context context;
	// After 2 seconds show the menu button and take out the message
	private Timer timer;
	MyTimerTask myTimerTask;
	
	// For this example, only two pages
    static final int LIST_MESSAGES = 2;
    
	ViewPager mPager;
	SlidePagerAdapter mPagerAdapter;
	
	ListView listviewMessage;
    ListView listviewMessageExp;
    
    ListViewAdapterMessage mlistAdapter;
    
    // Data we need
    PublicTransport myPublic = PublicTransport.getInstance();
    SavingState mySave = SavingState.getInstance();
    Location myLocation;
    
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		if (savedInstanceState != null){
			loaded = savedInstanceState.getBoolean("loaded", false);
			showLang = savedInstanceState.getBoolean("showLang", false);
			showDialog = savedInstanceState.getBoolean("showDialog", false);
		}
		try{
			LocationSynchronizer mLocationSynchronizer = LocationSynchronizer.getInstance((SynchronizableActivity) getSynchronizableActivity());
			myLocation = mLocationSynchronizer.getLocation();
			if (myLocation == null) MoveOnCroutonStyle.croutonAlert( this, R.string.error_location_NETWORK);
		} catch (Exception e){
	    		Log.e(Config.LOGTAG, "TabsActivity.onResume getting show Defaults from SharedPreferences");
	    }
		//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_tabs);
		// Show the Up button in the action bar:
		ActionBar actionBar = getSupportActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
		context = this;

	
		//ProgressBar while is filling the list
		myProgressBar = (ProgressBar) findViewById(R.id.progress);
		isEmpty = (TextView) findViewById(R.id.empty);
		//Vector of pages
	    Vector<View> pages = new Vector<View>();
	    //List Message showing description on click show Exp List
	    listviewMessage = new ListView(context);
	    listviewMessage.setDivider(context.getResources().getDrawable(R.drawable.line));
	    listviewMessage.setDividerHeight(1);
	    //List Message showing complete description, onClickItem
	    listviewMessageExp = new ListView(context);
	    listviewMessageExp.setDivider(context.getResources().getDrawable(R.drawable.line));
	    listviewMessageExp.setDividerHeight(1);

	    // two pages
	    pages.add(listviewMessage);
	    pages.add(listviewMessageExp);
	    
	    // our adapter for pager
	    vp = (ViewPager) findViewById(R.id.pager);
	    CustomPagerAdapter adapter = new CustomPagerAdapter(context, pages);
	    vp.setAdapter(adapter);

		//Execute asyntask (we can send string array)
		new BackgroundTask().execute("SOMETHING");
	    //mlistAdapter = new ListViewAdapterMessage(context, myPublic.getListAllMessages());
	    //listviewMessageExp.setAdapter(mlistAdapter);
	    //listviewMessage.setAdapter(mlistAdapter);
	    
	    // click event for each row of the list
	    listviewMessage.setOnItemClickListener(new OnItemClickListener() {

	    	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
	    		// arg2 = the id of the item in our view (List/Grid) that we clicked
	    		// arg3 = the id of the item that we have clicked
	    		// if we didn't assign any id for the Object (Book) the arg3 value is 0
	    		// That means if we comment, aBookDetail.setBookIsbn(i); arg3 value become 0
	    		if (Config.DEBUG) Toast.makeText(getApplicationContext(), "You clicked on position : " + arg2 + " and id : " + arg3, Toast.LENGTH_LONG).show();
	    		TextView c = (TextView) arg1.findViewById(R.id.textDescription);
	    		int changeit=(View.VISIBLE != c.getVisibility())? View.VISIBLE : View.GONE;
	    		c.setVisibility(changeit);
	    		listviewMessage.setSelection(arg2);
	    	}

	    });
	    
	            
       	// Objects to change on the fly
		this.rl = (RelativeLayout) findViewById(R.id.rl_tabs);
		this.txt_hello = (TextView)findViewById(R.id.tabsMessage);
		this.txt_version = (TextView)findViewById(R.id.tabsVersionNumber);
		this.txt_textList = (TextView) findViewById (R.id.textList);
		this.img_icon = (ImageView) findViewById (R.id.tabsImageIcon);
		this.button1 = (Button)findViewById(R.id.button1);
		this.button2 = (Button)findViewById(R.id.button2);
		this.button3 = (Button)findViewById(R.id.button3);
		this.button4 = (Button)findViewById(R.id.button4);
		this.button5 = (Button)findViewById(R.id.button5);
		
		loadLocale();
		//Configuration Language
		if (loaded){
			showButtons();
		} else {
	        // instance of timer and myTimerTask
	    	txt_hello.setVisibility(View.VISIBLE);
	    	txt_hello.setText(R.string.dataBaseLoaded);
	    	txt_version.setVisibility(View.VISIBLE);
	    	img_icon.setVisibility(View.VISIBLE);
	        timer = new Timer();
	        myTimerTask = new MyTimerTask();
	        timer.schedule(myTimerTask, 2000);							
		}

		((Button)findViewById(R.id.button1)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	//Open activity of routes not impelented on v1.0
            	if (Config.DEBUG) Toast.makeText(v.getContext(),v.getContext().getResources().getString(R.string.routesNotImplemented), Toast.LENGTH_SHORT).show();
            	MoveOnCroutonStyle.croutonWarn(TabsActivity.this, R.string.routesNotImplemented);
            	//MoveOnCroutonStyle.croutonAlert(TabsActivity.this, R.string.error_loadingDB);
            	//MoveOnCroutonStyle.croutonFly(TabsActivity.this, R.string.error_loadingDB);
            	//MoveOnCroutonStyle.croutonInfo(TabsActivity.this, R.string.error_loadingDB);
            	//MoveOnCroutonStyle.croutonConfirm(TabsActivity.this, R.string.error_loadingDB);
            	
                //Initialize route Activity
                //startActivity(new Intent(context, ItineraryActivity.class));
                
            }
        });
        ((Button)findViewById(R.id.button2)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	//Open tabs User showing LINES
            	if (Config.DEBUG) Toast.makeText(v.getContext(),v.getContext().getResources().getString(R.string.lines), Toast.LENGTH_SHORT).show();
            	savingState.setTabInit(AppConstants.TAB_A);
                //Open tabs showwing LINES
                startActivity(new Intent(context, AppMainTabActivity.class));
             }
        });
        ((Button)findViewById(R.id.button3)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	//Open tabs User showing STATIONS
            	if (Config.DEBUG) Toast.makeText(v.getContext(),v.getContext().getResources().getString(R.string.stations), Toast.LENGTH_SHORT).show();
            	savingState.setTabInit(AppConstants.TAB_B);
                //Open tabs showwing LINES
                startActivity(new Intent(context, AppMainTabActivity.class));
             }
        });
        ((Button)findViewById(R.id.button4)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	//Open tabs User showing FAVORITES
            	if (Config.DEBUG) Toast.makeText(v.getContext(),v.getContext().getResources().getString(R.string.favourits), Toast.LENGTH_SHORT).show();
            	savingState.setTabInit(AppConstants.TAB_D);
                //Open tabs showwing LINES
                startActivity(new Intent(context, AppMainTabActivity.class));
             }
        });
        ((Button)findViewById(R.id.button5)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	//Open tabs User showing NEARBY STATION
            	if (Config.DEBUG) Toast.makeText(v.getContext(),v.getContext().getResources().getString(R.string.nearbystations), Toast.LENGTH_SHORT).show();
            	savingState.setTabInit(AppConstants.TAB_C);
                //Open tabs showwing LINES
                startActivity(new Intent(context, AppMainTabActivity.class));
             }
        });
        
        if (showLang) menuLanguage();
        if (showDialog) showDialog();
        

	}
	
	/* PagerAdapter class */
	public class SlidePagerAdapter extends FragmentPagerAdapter {
		public SlidePagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			/*
			 * IMPORTANT: This is the point. We create a RootFragment acting as
			 * a container for other fragments
			 */
			if (position == 0)
				return new RootFragment();
			else
				//changed for testing
				return new RootFragment();
		}

		@Override
		public int getCount() {
			return LIST_MESSAGES;
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	    if (Config.DEBUG) Log.d(Config.LOGTAG, "Tabs_Activity.onDestroy()");
	    Crouton.clearCroutonsForActivity(TabsActivity.this);
	} 
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.tabs, menu);
        return true;
    }
 
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	    if (Config.DEBUG) Log.i(Config.LOGTAG, "IN " + String.valueOf(item.getItemId()));
    	switch (item.getItemId()) {
        case android.R.id.home:	
    	    if (Config.DEBUG) Log.i(Config.LOGTAG, "OUT HOME" + "Pulsado Home");
            // icono de la aplicación pulsado en la barra de acción; ir a inicio
    	    if (Config.DEBUG) Toast.makeText(this, this.getResources().getString(R.string.action_home_show), Toast.LENGTH_SHORT).show();
            openQuitDialog();
            return true;

        case R.id.menu_user:
    	    if (Config.DEBUG) Log.i(Config.LOGTAG, "OUT Menu_user "+ String.valueOf(item.getItemId()));
        	
    	    if (Config.DEBUG) Toast.makeText(this, this.getResources().getString(R.string.action_menu_user_show), Toast.LENGTH_SHORT).show();
            MoveOnCroutonStyle.croutonAlert(TabsActivity.this, 
            		this.getResources().getString(R.string.preferences_type_of_user) 
            		+ " : " + myPublic.getmPreferences().getOptionUser().toString() 
            		+ "\n" + this.getResources().getString(R.string.custom_menu));
            //startActivity(new Intent(this, TabsUserActivity.class));
            return true;
        
        case R.id.menu_settings:
    	    if (Config.DEBUG) Log.i(Config.LOGTAG, "OUT Menu_settings "+ String.valueOf(item.getItemId()));
        	
    	    if (Config.DEBUG) Toast.makeText(this, this.getResources().getString(R.string.action_settings_show), Toast.LENGTH_SHORT).show();
        	Intent preferences_activity =
        	new Intent(this, PreferencesActivity.class);
        	this.startActivity(preferences_activity);
            return true;
        
        case R.id.menu_language:
    	    if (Config.DEBUG) Log.i(Config.LOGTAG, "OUT Menu_language "+ String.valueOf(item.getItemId()));
        	
    	    if (Config.DEBUG) Toast.makeText(this, this.getResources().getString(R.string.action_language_show), Toast.LENGTH_SHORT).show();
            menuLanguage();
            return true;
        
        case R.id.menu_typetransport:
    	    if (Config.DEBUG) Log.i(Config.LOGTAG, "OUT Menu_typetransport "+ String.valueOf(item.getItemId()));
    	    if (Config.DEBUG) Toast.makeText(this, this.getResources().getString(R.string.action_type_transport_show), Toast.LENGTH_SHORT).show();
    	    savingState.setFavoriteOn(false);
        	// Activity TypeTransport
            context.startActivity(new Intent(context, TypeTransportActivity.class));        	
            return true;
        
        case R.id.menu_favorite:
    	    if (Config.DEBUG) Log.i(Config.LOGTAG, "OUT Menu_favorite "+ String.valueOf(item.getItemId()));
    	    if (Config.DEBUG) Toast.makeText(this, this.getResources().getString(R.string.action_favorite_show), Toast.LENGTH_SHORT).show();
    	     
        	savingState.setTabInit(AppConstants.TAB_D);
            //Open tabs showwing Favorite
            startActivity(new Intent(context, AppMainTabActivity.class));
            return true;
        
        case R.id.menu_credits:
    	    if (Config.DEBUG) Log.i(Config.LOGTAG, "OUT CREDIT "+ item.getItemId());
    	    if (Config.DEBUG) Toast.makeText(this, this.getResources().getString(R.string.action_credits_show), Toast.LENGTH_SHORT).show();
        	showDialog();
            return true;
        
        default:
    	    if (Config.DEBUG) Log.i(Config.LOGTAG, "OUT DEF " +String.valueOf(item.getItemId()));
            return super.onOptionsItemSelected(item);
        }
        //return true;
    }
    
    @Override
    public void onBackPressed() {
    	// TODO Auto-generated method stub
    	openQuitDialog();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
       // Retrieve preferences
        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(this);
     
        // Data we need
        boolean myShow = true;
        
        try {
        	myShow = pref.getBoolean(Config.show_options, true);
        }
       	catch (Exception e){
    		Log.e(Config.LOGTAG, "TabsActivity.onResume getting show Defaults from SharedPreferences");
    	}
        
   
        //saving preferences, ready!
        myPublic.getmPreferences().setmContext(context);
        // First we check for changes
        //myPublic.getmPreferences().set_check();


        if (myShow){
         	
        	// message in first activity, remind the user which type of user is selected
            if (Config.DEBUG) Toast.makeText(this, 
            		this.getResources().getString(R.string.preferences_type_of_user) 
            		+ " : " + myPublic.getmPreferences().getOptionUser().toString() 
            		+ "\n" + this.getResources().getString(R.string.preferences_Transfer_Distance) 
            		+ " : " + myPublic.getmPreferences().optionMeterToString() 
            		+ "\n" + this.getResources().getString(R.string.preferencesOptionsForRoutes) 
            		+ " : " + myPublic.getmPreferences().getOptionRoute().toString(), Toast.LENGTH_SHORT).show() ;     	
        
            MoveOnCroutonStyle.croutonInfo(TabsActivity.this, 
            		this.getResources().getString(R.string.preferences_type_of_user) 
            		+ " : " + myPublic.getmPreferences().getOptionUser().toString() 
            		+ "\n" + this.getResources().getString(R.string.preferences_Transfer_Distance) 
            		+ " : " + myPublic.getmPreferences().optionMeterToString()
            		+ "\n" + this.getResources().getString(R.string.preferencesOptionsForRoutes) 
            		+ " : " + myPublic.getmPreferences().getOptionRoute().toString());
        }
        mlistAdapter.updateResults(myPublic.getListAllMessages());	
     
     }
    
    @Override
    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
    	super.onConfigurationChanged(newConfig);
    	if (myLocale != null){
            newConfig.locale = myLocale;
            Locale.setDefault(myLocale);
            getBaseContext().getResources().updateConfiguration(newConfig, getBaseContext().getResources().getDisplayMetrics());
        }
    }

    //here is how we show dialog
    public void showDialog()
    {
        CreditsDialog dialog = new CreditsDialog(this);
        dialog.show();
        showDialog = false;
    }

    
    private void openQuitDialog(){
     AlertDialog.Builder quitDialog 
      = new AlertDialog.Builder(TabsActivity.this);
     
     quitDialog.setMessage(R.string.see_you_later)
     		.setTitle(R.string.moveon_menu)
     		.setIcon(R.drawable.type_transport);
     
     quitDialog.setPositiveButton(R.string.yes, new OnClickListener(){

      @Override
      public void onClick(DialogInterface dialog, int which) {
       // TODO Auto-generated method stub
    	  TabsActivity.this.finish(); 
      }});
     
     quitDialog.setNeutralButton(R.string.no, new OnClickListener(){

      public void onClick(DialogInterface dialog, int which) {
       // TODO Auto-generated method stub
       
      }});
     
     quitDialog.show();
    }
    
    public void loadLocale()
    {
    	SharedPreferences pref = PreferenceManager
                 .getDefaultSharedPreferences(this);
    	try {
    		String language = pref.getString(Config.language_options, "");
    	    changeLang(TypeLanguage.typeOf(language));
    	}catch (Exception e){
    		Log.e(Config.LOGTAG, "TabsActivity.onResume getting ROUTE data from SharedPreferences");
    		//we don't have language preference
    	}
    }

    public void saveLocale(TypeLanguage mLang)
    {
    	SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(this);
    	SharedPreferences.Editor editor = pref.edit();
    	editor.putString(Config.language_options, mLang.toString());
    	editor.commit();
    }
    
    public void changeLang(TypeLanguage myLang)
    {
    	if (myLang.toString().equalsIgnoreCase(""))
    		return;
    	myLocale = new Locale(myLang.toString());
    	saveLocale(myLang);
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        // on change language
    	if (PublicTransport.getInstance().isChangedLanguage(myLang)){
    		// change text in Buttons
            updateTexts();
    		// update Menu
            invalidateOptionsMenu();
            // update listMessage
    		if(PublicTransport.getInstance().refreshListAllMessages()){
    			new UpdateBackgroundTask().execute("SOMETHING");
     			mlistAdapter.updateResults(PublicTransport.getInstance().getListAllMessages());
    		}
    	}
    	
    }
    
    private void updateTexts()
    {
    	// Changing texts of activity
    	txt_hello.setText(R.string.refreshMessage);
    	txt_textList.setText(R.string.message);
    	button1.setText(R.string.howiget);
    	button2.setText(R.string.lines);
    	button3.setText(R.string.stations);
    	button4.setText(R.string.favourits);
    	button5.setText(R.string.nearbystations);    	
    }

    private void menuLanguage(){
    	ArrayList<ImageLanguage> myLang = new ArrayList<ImageLanguage>();
    	ImageLanguage newLang;
    	showLang = true;
    	
    	//Accedemos al objeto 'Recursos' desde la Activity
    	Resources res = this.getResources();
    	String[] myCode = res.getStringArray(R.array.language_code);
    	String[] myNom = res.getStringArray(R.array.language_options);
    	for (int i = 0; i <= myCode.length-1; i++) {
    		int myImage = context.getResources().getIdentifier(myCode[i], "drawable", context.getPackageName());
    		newLang = new ImageLanguage(myImage, myCode[i], myNom[i]);
    		myLang.add(newLang);
    	}
    	ListViewAdapterLanguage adapter;
    	adapter = new ListViewAdapterLanguage(this, myLang);
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setTitle(R.string.selectlang);
    	builder.setAdapter(adapter, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				// When clicked, show a toast with the TextView text
				changeLang(TypeLanguage.values()[which]);

				//saveLocale(which);
                dialog.dismiss();
                showLang = false;
			}
		});
    	
    	AlertDialog alert = builder.create();
    	alert.show();
    }
	
	class MyTimerTask extends TimerTask {

		  @Override
		  public void run() {
		   
		   runOnUiThread(new Runnable(){

		    @Override
		    public void run() {
		    	// Showing buttons
		    	showButtons();
		    }});
		  }
	}
	
	public void showButtons(){
    	rl.setBackgroundColor(getResources().getColor(R.color.colorTexto_Blanco));
    	txt_hello.setVisibility(View.GONE);
    	txt_version.setVisibility(View.GONE);
    	img_icon.setVisibility(View.GONE);
    	txt_textList.setVisibility(View.VISIBLE);
    	button1.setVisibility(View.VISIBLE);
    	button2.setVisibility(View.VISIBLE);
    	button3.setVisibility(View.VISIBLE);
    	button4.setVisibility(View.VISIBLE);
    	button5.setVisibility(View.VISIBLE);
    	vp.setVisibility(View.VISIBLE);
	}
	
	@Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null)
        	loaded = savedInstanceState.getBoolean("loaded");
			showLang = savedInstanceState.getBoolean("showLang", false);
			showDialog = savedInstanceState.getBoolean("showDialog", false);

    }
    
	@Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        loaded = true;
        outState.putBoolean("loaded", true);
        outState.putBoolean("showLang", showLang);
        outState.putBoolean("showDialog", showDialog);
    }
	
	private class BackgroundTask extends AsyncTask<String, Void, String> {
		// What we need in temp loadar
	  
	@Override
	protected void onPreExecute()
	{
		  //showing progressbar
		  myProgressBar.setVisibility(View.VISIBLE);
	}
	
	@Override
	protected String doInBackground(String... urls) 
	{			
	    try
		{
	    	//our task
		    mlistAdapter = new ListViewAdapterMessage(context, myPublic.getListAllMessages());
			listviewMessageExp.setAdapter(mlistAdapter);
			listviewMessage.setAdapter(mlistAdapter);

			
		} catch (Exception e)
		{
			Log.e(MainActivity.class.toString(), e.getMessage());
		}
			//on post execute we can send an String	
			return "moveOn";
	 }

	 @Override
	 protected void onPostExecute(String result) 
	 {
	    //Asyntask is finished, we hide progressbar
		 myProgressBar.setVisibility(View.GONE);
		 //now we can show our list or simply working
		 if (mlistAdapter != null && mlistAdapter.getCount() > 0)
		 {
			 //listviewMessageExp.setAdapter(mlistAdapter);
			 //listviewMessage.setAdapter(mlistAdapter);
			 listviewMessageExp.setVisibility(View.VISIBLE);
			 listviewMessage.setVisibility(View.VISIBLE);

		 }
		 else if (mlistAdapter != null){
		 	//show empty message
		 	isEmpty.setVisibility(View.VISIBLE);
		 	if (mySave.isFavoriteOn()){
		 		isEmpty.setText(R.string.emptyFavoriteList);
		 	}
		 }
		 //if not, we show an error
		 else
		 {
			 	Builder builder = new Builder(TabsActivity.this);
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
	
	private class UpdateBackgroundTask extends AsyncTask<String, Void, String> {
		// What we need in temp loadar
	  
	@Override
	protected void onPreExecute()
	{
		  //showing progressbar
		  myProgressBar.setVisibility(View.VISIBLE);
	}
	
	@Override
	protected String doInBackground(String... urls) 
	{			
	    try
		{
	    	//our task
 			mlistAdapter.updateResults(PublicTransport.getInstance().getListAllMessages());

			
		} catch (Exception e)
		{
			Log.e(MainActivity.class.toString(), e.getMessage());
		}
			//on post execute we can send an String	
			return "moveOn";
	 }

	 @Override
	 protected void onPostExecute(String result) 
	 {
	    //Asyntask is finished, we hide progressbar
		 myProgressBar.setVisibility(View.GONE);
		 //now we can show our list or simply working
		 if (mlistAdapter != null && mlistAdapter.getCount() > 0)
		 {
			 listviewMessageExp.setVisibility(View.VISIBLE);
			 listviewMessage.setVisibility(View.VISIBLE);
		 }
		 else if (mlistAdapter != null){
			 	//show empty message
			 	isEmpty.setVisibility(View.VISIBLE);
			 	if (mySave.isFavoriteOn()){
			 		isEmpty.setText(R.string.emptyFavoriteList);
			 	}			 
		 }
		 //if not, we show an error
		 else
		 {
			 	Builder builder = new Builder(TabsActivity.this);
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




