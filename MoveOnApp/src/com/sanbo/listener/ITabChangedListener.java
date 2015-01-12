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

/**
 * @author admin
 *
 */
import android.view.View;
import com.actionbarsherlock.app.ActionBar;

/**
 * Custom Action Bar Tab Changed Listener.
 */
public interface ITabChangedListener {
    /**
     * This method is called when the user has changed page of the tab view.
     * @param pageIndex Index of the current page.
     * @param tab Instance of the selected tab control.
     * @param tabView Instance of the tab view.
     */
    void onTabChanged(int pageIndex, ActionBar.Tab tab, View tabView);
}