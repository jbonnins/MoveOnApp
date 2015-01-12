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

import com.sanbo.moveonapp.R;
import java.lang.reflect.Field;


/**
 * USAGE:
 * 		DECLARE LOG MESSAGES
 * if (Config.DEBUG) Log.e(Config.LOGTAG, "Mensaje de error");
 * if (Config.DEBUG) Log.w(Config.LOGTAG, "Mensaje de warning");
 * if (Config.DEBUG) Log.i(Config.LOGTAG, "Mensaje de información");
 * if (config.DEBUG) Log.d(Config.LOGTAG, "Mensaje de depuración");
 * if (Config.DEBUG) Log.v(Config.LOGTAG, "Mensaje de verbose");
 * 
 */
public class Config {
	// byDefault OPTIONS
	public static final int INVALID_ROW_ID = -1;
	public static final boolean MyBYDEFAULT = true;
	public static final String MyDISTANCE = "0";
	public static final String MyUSER = "USER";
	public static final String MyROUTE = "FASTER";
	public static final String MyLANGUAGE = "en";
	public static final boolean isTRUE = true;
	// Debug OPTIONS change to false or true for showing debug messages
	public static final boolean DEBUG = false;
	public static final String LOGTAG = "moveOnApp";
	// Database OPTIONS
	public static final int TRUE = 1;
	public static final int FALSE = 0;
	public static final boolean FORCELOADINDB = false;
	// GPS Location OPTIONS
	public static final int locationAccuracy = 7;
	// SharedPreferences TAGS
	public static final String language_options = "language_options";
	public static final String show_options = "show_options";
	public static final String route_options = "route_options";
	public static final String meter_options = "meter_options";
	public static final String user_options = "user_options";
	public static final String transfer_options = "transfer_options";
	public static final String bydefault_options = "bydefault_options";
	// Temporary, we include in our DB in TimeZone in next version
	public static final String MyTimeZone = "Europe/Madrid";
	public static final String MyGMTZone = "GMT";
	// New ISSUE Address 
	public static final String REPORT_URL = "https://github.com/jbonnins/MoveOnApp/issues/new";
	// URL EMT NEXT BUS api connection
	public static final String REPORT_URL_EMT = "http://www.emtpalma.es/EMTPalma/Front/pasoporparada.es.svr?p=";
	// URL Citibyk for BICIPALAMA api connection
	public static final String REPORT_URL_CITYBIK = "http://api.citybik.es/bicipalma.json";
	// Time to force refresh data 10'
	public static final long UPDATE_TIME = 600000;

	  
	  
	  public static Long hash(int[] array){
	      Long h = (long) (1 << array.length);
	      for (int i = 0; i < array.length; i++)
	      {
	          h = h | (array[i] << (array.length - i - 1));
	      }
	      return h;
	  }
	  
	 public static boolean unHash(Long value, int position){
		 int res = (int) (value & (1 << position));
		 
		 return (res == 1);
	 }
	 
	 public static int getResStringId(String name) throws Exception{
		R.string string = new R.string();
		Field field = R.string.class.getDeclaredField(name);
		return (Integer) field.get(string);
	 }
	 
	 public static int getResId(String name) throws Exception{
		R.id string = new R.id();
		Field field = R.id.class.getDeclaredField(name);
		return (Integer) field.get(string);
	 }
	 
	 
	 

}


