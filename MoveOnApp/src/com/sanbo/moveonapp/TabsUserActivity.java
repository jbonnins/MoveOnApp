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


import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.sanbo.listener.ITabChangedListener;
import com.sanbo.listener.ListenerTabUser;
import com.sanbo.notifier.MoveOnCroutonStyle;
import com.sanbo.utils.Config;

import de.keyboardsurfer.android.widget.crouton.Crouton;

/**
 * This is the main fragment activity with action bar tabs and view pager.
 */
public class TabsUserActivity extends SherlockFragmentActivity implements ITabChangedListener {

	@SuppressWarnings("unused")
	private static final String TAG = "TabUserActivity";
	
    private ActionBar _actionBar;
    private ViewPager _viewPager;

	@SuppressWarnings("unused")
	private static boolean _firstTabShowed;
	@SuppressWarnings("unused")
	private static boolean _secondTabShowed;
    @SuppressWarnings("unused")
	private static boolean _thirdTabShowed;
	@SuppressWarnings("unused")
	private static boolean _fourthTabShowed;
   
	private static Menu _menuInstance;

    protected static TabsUserActivity THIS = null;

    /**
     * Adds three tabs to the Action Bar
     */
    private void addTabs() {
  	   if (Config.DEBUG) Log.d(Config.LOGTAG, "TabsUserActivity.addTabs()");
    	ListenerTabUser myTabListener = new ListenerTabUser(this, _viewPager);
        myTabListener.addTabChangedListener(this);

        // List Lines
        ActionBar.Tab firstTab = _actionBar.newTab();
        firstTab.setText(getResources().getString(R.string.first_user));
        //myTabListener.addTab(firstTab, FirstFragment.class, null);
        //myTabListener.addTab(firstTab, ProveFragmentList2.class, null);
        firstTab.setTabListener(myTabListener);

        // List Station
        ActionBar.Tab secondTab = _actionBar.newTab();
        secondTab.setText(getResources().getString(R.string.second_user));
        //myTabListener.addTab(secondTab, SecondFragment.class, null);
        //myTabListener.addTab(secondTab, ProveFragmentList2.class, null);
        secondTab.setTabListener(myTabListener);

        // List Nearby Stations
        ActionBar.Tab thirdTab = _actionBar.newTab();
        thirdTab.setText(getResources().getString(R.string.third_user));
        //myTabListener.addTab(thirdTab, ThirdFragment.class, null);
        //myTabListener.addTab(thirdTab, ProveFragmentList2.class, null);
        thirdTab.setTabListener(myTabListener);

        // List Favorites
        ActionBar.Tab fourthTab = _actionBar.newTab();
        fourthTab.setText(getResources().getString(R.string.fourth_user));
        //myTabListener.addTab(fourthTab, FourthFragment.class, null);
        //myTabListener.addTab(fourthTab, FragmentFavorites.class, null);
        fourthTab.setTabListener(myTabListener);
    }
   
    /**
     * Hides all action bar menu items.
     *
     * @param menu Action bar menu instance
     */
    private void hideAllActionItems(Menu menu) {
        if (menu != null) {
            for (int i = 0; i < menu.size(); i++)
                menu.getItem(i).setVisible(false);
        }
    }

    /**
     * Initializes the action bar and sets it's navigation mode.
     */
    private void initActionBar() {
   	   if (Config.DEBUG) Log.d(Config.LOGTAG, "TabsUserActivity.initActionBar() ");
        _actionBar = getSupportActionBar();
        _actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
    }

    /**
     * Initializes the view pager and sets it as a content view.
     */
    private void initViewPager() {
   	   if (Config.DEBUG) Log.d(Config.LOGTAG, "TabsUserActivity.initViewPager()");
        _viewPager = new ViewPager(this);
        _viewPager.setId(R.id.pager1);
        setContentView(_viewPager);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
   	   if (Config.DEBUG) Log.d(Config.LOGTAG, "TabsUserActivity.onCreate(Bundle savedInstanceState)");

        THIS = this;
        initActionBar();
        initViewPager();
        addTabs();
		// Show the Up button in the action bar:
		ActionBar actionBar = getSupportActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);

   }
   
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	_menuInstance = menu;
    	//menu.add(getString(R.string.action_search)).setIcon(R.drawable.llistes).setVisible(true).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
    	return true;
    }
    
    // Income from fragment
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	    if (Config.DEBUG) Log.i(Config.LOGTAG, "IN " + String.valueOf(item.getItemId()));
    	switch (item.getItemId()) {
        case android.R.id.home:	
    	    if (Config.DEBUG) Log.i(Config.LOGTAG, "OUT HOME" + "Pulsado Home");
            // icono de la aplicación pulsado en la barra de acción; ir a inicio
            if (Config.DEBUG) Toast.makeText(this, this.getResources().getString(R.string.action_home_show), Toast.LENGTH_SHORT).show();
            MoveOnCroutonStyle.croutonConfirm(TabsUserActivity.this, R.string.action_home_show);
            finish();
            return true;
            
        case R.id.menu_search:
    	    if (Config.DEBUG) Log.i(Config.LOGTAG, "OUT CREDIT "+ item.getItemId());
            if (Config.DEBUG) Toast.makeText(this, this.getResources().getString(R.string.action_search_show), Toast.LENGTH_SHORT).show();
            MoveOnCroutonStyle.croutonConfirm(TabsUserActivity.this, R.string.action_search_show);
            //active search interactive
            return true;

        default:
    	    if (Config.DEBUG) Log.i(Config.LOGTAG, "OUT DEF " + String.valueOf(item.getItemId()));
            return super.onOptionsItemSelected(item);
        }
        //return true;
    }

    @Override
    public void onTabChanged(int pageIndex, ActionBar.Tab tab, View tabView) {
   	   if (Config.DEBUG) Log.d(Config.LOGTAG, "TabsUserActivity.onTabChanged(int pageIndex, ActionBar.Tab tab, View tabView)");
        resetVisibilityFields();
        if (_menuInstance != null) {
        	// hide all menus and show inside the fragment what you need
            hideAllActionItems(_menuInstance);            
            
            switch (pageIndex) {
                case 0:
                    showFirstTabActionItems(_menuInstance);
                    break;

                case 1:
                    showSecondTabActionItems(_menuInstance);
                    break;

                case 2:
                    showThirdTabActionItems(_menuInstance);
                    break;

                case 3:
                    showFourthTabActionItems(_menuInstance);
                default:    
                    break;
            }
        }
    }

    /**
     * Sets all tabs action item visibility fields to false.
     */
    private void resetVisibilityFields() {
   	   if (Config.DEBUG) Log.d(Config.LOGTAG, "TabsUserActivity.resetVisibilityFields()");
        _firstTabShowed = false;
        _secondTabShowed = false;
        _thirdTabShowed = false;
        _fourthTabShowed = false;
    }

    /**
     * Shows First tab action items.
     *
     * @param menu Action bar menu instance
     */
    private void showFirstTabActionItems(Menu menu) {
   	   if (Config.DEBUG) Log.d(Config.LOGTAG, "TabsUserActivity.showFirstTabActionItems(Menu menu)");
       if (menu != null && menu.size() == 1) {
           //menu.getItem(0).setVisible(true);
           _firstTabShowed = true;
       }
    }


    /**
     * Shows Second tab action items.
     *
     * @param menu Action bar menu instance
     */
    private void showSecondTabActionItems(Menu menu) {
   	   if (Config.DEBUG) Log.d(Config.LOGTAG, "TabsUserActivity.showSecondTabActionItems(Menu menu)");
       if (menu != null && menu.size() == 1) {
           //menu.getItem(0).setVisible(true);
           _secondTabShowed = true;
       }
   }
    /**
     * Shows Third tab action items.
     *
     * @param menu Action bar menu instance
     */
    private void showThirdTabActionItems(Menu menu) {
   	   if (Config.DEBUG) Log.d(Config.LOGTAG, "TabsUserActivity.showThirdTabActionItems(Menu menu)");
       if (menu != null && menu.size() == 1) {
           //menu.getItem(0).setVisible(true);
           _thirdTabShowed = true;
       }
    }
    
    /**
     * Shows Fourth tab action items.
     *
     * @param menu Action bar menu instance
     */
    private void showFourthTabActionItems(Menu menu) {
  	   if (Config.DEBUG) Log.d(Config.LOGTAG, "TabsUserActivity.showFourthTabActionItems(Menu menu)");
       if (menu != null && menu.size() == 1) {
           //menu.getItem(0).setVisible(true);
           _fourthTabShowed = true;
       }
    }
    
	@Override
	protected void onDestroy() {
		super.onDestroy();
	    if (Config.DEBUG) Log.d(Config.LOGTAG, "Tabs_Activity.onDestroy()");
	    Crouton.clearCroutonsForActivity(TabsUserActivity.this);
	} 



 }