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


import java.util.Vector;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class CustomPagerAdapter extends PagerAdapter {
    protected static final String TAG = "ListViewAdapter";

 @SuppressWarnings("unused")
 private Context mContext;
 private Vector<View> pages;

 public CustomPagerAdapter(Context context, Vector<View> pages) {
  this.mContext=context;
  this.pages=pages;
 }
 
 @Override
 public Object instantiateItem(ViewGroup container, int position) {
  View page = pages.get(position);
  container.addView(page);
  return page;
 }
 
 @Override
 public int getCount() {
  return pages.size();
 }

 @Override
 public boolean isViewFromObject(View view, Object object) {
  return view.equals(object);
 }
 
 @Override
 public void destroyItem(ViewGroup container, int position, Object object) {
  container.removeView((View) object);
 }
 
 

}