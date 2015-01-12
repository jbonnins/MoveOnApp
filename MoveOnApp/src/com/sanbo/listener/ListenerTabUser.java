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

package com.sanbo.listener;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentStatePagerAdapter;
//import android.support.v4.app.FragmentTransaction;
//import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.sanbo.utils.Config;

import java.util.ArrayList;

/**
 * This is the custom pager tab listener. Tabs are implemented in ActionBar.
 * Every tab's content is a Sherlock fragment.
 */

public class ListenerTabUser extends FragmentStatePagerAdapter implements ActionBar.TabListener, ViewPager.OnPageChangeListener {
    private final Context _context;
    private final ActionBar _actionBar;
    private final ViewPager _viewPager;
    private final ArrayList<TabInfo> _tabs = new ArrayList<TabInfo>();
    private final ArrayList<ITabChangedListener> _tabChangedListeners = new ArrayList<ITabChangedListener>();

    static final class TabInfo {
    	private final Class<?> _class;
        private final Bundle _args;

        TabInfo(Class<?> clss, Bundle args) {
    		if (Config.DEBUG) Log.d(Config.LOGTAG, "ListenerTabUser.TabInfoFINALCLASS");
            _class = clss;
            _args = args;
        }
    }

    public ListenerTabUser(SherlockFragmentActivity activity, ViewPager pager) {
       super(activity.getSupportFragmentManager());
	   if (Config.DEBUG) Log.d(Config.LOGTAG, "ListenerTabUser.ListenerTabUser(SherlockFragmentActivity activity, ViewPager pager)");
        _context = activity;
        _actionBar = activity.getSupportActionBar();
        _viewPager = pager;
        _viewPager.setAdapter(this);
        _viewPager.setOnPageChangeListener(this);
    }

    /**
     * Adds tabs to the ActionBar.
     *
     * @param tab  Tab which will added
     * @param clss Class which is connected with the tab
     * @param args Extra tab arguments
     */
    public void addTab(ActionBar.Tab tab, Class<?> clss, Bundle args) {
 	   if (Config.DEBUG) Log.d(Config.LOGTAG, "ListenerTabUser.addTab(Tab, Bundle)");
        TabInfo info = new TabInfo(clss, args);
        tab.setTag(info);
        tab.setTabListener(this);
        _tabs.add(info);
        _actionBar.addTab(tab);
        notifyDataSetChanged();
    }

    public void addTabChangedListener(ITabChangedListener listener) {
  	   if (Config.DEBUG) Log.d(Config.LOGTAG, "ListenerTabUser.addTabChangedListener(ITabChangedListener)");
        _tabChangedListeners.add(listener);
    }

    public int getCount() {
        return _tabs.size();
    }

    public Fragment getItem(int position) {
   	   if (Config.DEBUG) Log.d(Config.LOGTAG, "ListenerTabUser.getItem(int))");
        TabInfo info = _tabs.get(position);
        return Fragment.instantiate(_context, info._class.getName(), info._args);
    }

    private void notifyTabChangedListeners(int tabIndex, Tab tab, View tabView) {
   	   if (Config.DEBUG) Log.d(Config.LOGTAG, "ListenerTabUser.notifyTabChangedListeners(int tabIndex, Tab tab, View tabView)");
        for (ITabChangedListener listener : _tabChangedListeners) {
            listener.onTabChanged(tabIndex, tab, tabView);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset,
                               int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
   	   if (Config.DEBUG) Log.d(Config.LOGTAG, "ListenerTabUser.onPageSelected(int position)");
        _actionBar.setSelectedNavigationItem(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    	   if (Config.DEBUG) Log.d(Config.LOGTAG, "ListenerTabUser.onPageScrollStateChanged(int state)");
    	   // control scrolling
    }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
 	   if (Config.DEBUG) Log.d(Config.LOGTAG, "ListenerTabUser.onTabSelected(Tab tab, FragmentTransaction ft)");
        Object tag = tab.getTag();
        for (int i = 0; i < _tabs.size(); i++) {
            if (_tabs.get(i) == tag) {
                _viewPager.setCurrentItem(i);
                notifyTabChangedListeners(i, tab, tab.getCustomView());
            }
        }
    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
  	   if (Config.DEBUG) Log.d(Config.LOGTAG, "ListenerTabUser.onTabUnselected(Tab tab, FragmentTransaction ft)");
    }

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
   	   if (Config.DEBUG) Log.d(Config.LOGTAG, "ListenerTabUser.onTabReselected(Tab tab, FragmentTransaction ft)");
    }
}