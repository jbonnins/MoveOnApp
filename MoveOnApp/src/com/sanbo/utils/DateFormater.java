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

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import com.sanbo.moveonapp.R;

import android.content.Context;

/**
 * @author admin
 *
 */
public class DateFormater
{

/**
 * @param mContext the place where you make the call
 * @param mDate Date we want to get as text
 * @return String of Date
 */
public static String formatPeriod(Context mContext, Date mDate)
  // retorna la cadena en format de text - s'utilitza a les rutes
  {
	  Calendar localCalendar = Calendar.getInstance(Locale.getDefault());
	  localCalendar.setTime(mDate);
	  String str = "";
	  if (localCalendar.get(Calendar.HOUR_OF_DAY) > 1)
	  {
	    str = String.valueOf(localCalendar.get(Calendar.HOUR_OF_DAY)) + " " + mContext.getResources().getString(R.string.messageHours);
	  } else if  (localCalendar.get(Calendar.HOUR_OF_DAY) == 1) {
	    	str = str + String.valueOf(localCalendar.get(Calendar.HOUR_OF_DAY)) + " " + mContext.getResources().getString(R.string.messageHour);
	  }
	  if (localCalendar.get(Calendar.MINUTE) > 1)
	  {
	    str = String.valueOf(localCalendar.get(Calendar.MINUTE)) + " " + mContext.getResources().getString(R.string.messageMinutes);
	  } else if  (localCalendar.get(Calendar.HOUR_OF_DAY) == 1) {
	    	str = str + String.valueOf(localCalendar.get(Calendar.MINUTE)) + " " + mContext.getResources().getString(R.string.messageMinute);
	  }
	  return str;
  }
	  
/**
 * @param mDate 
 * @return DateToHHMMSS
 */
public static String TimeFormat(Date mDate)
  // retorna el format hores-minuts-segons
  {
    return new SimpleDateFormat("HH:mm:ss").format(mDate);
  }


/**
 * @param mDate 
 * @return DateTo dd/MM/yyyy HH:mm:ss
 */
public static String DateTimeFormat(Calendar mCal)
  // retorna el format hores-minuts-segons
  {
	SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy  HH:mm:ss");
    return sdf.format(mCal.getTime());
  }
}
