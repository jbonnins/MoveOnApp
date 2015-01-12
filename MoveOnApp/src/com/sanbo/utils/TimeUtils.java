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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import com.sanbo.moveonapp.R;

import android.content.Context;
import android.util.Log;


public class TimeUtils {
	
	  private TimeUtils() {
	  }

	  // Time format from CityBik server
	  private static SimpleDateFormat UTCTimeFormatter = new SimpleDateFormat(
	      "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	  {
	    UTCTimeFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));
	  }
	  
	  private static boolean isUTCTimeFormat(String strUTCTime) {
	    boolean isValid = false;
	    try {
	      UTCTimeFormatter.parse(strUTCTime);
	      isValid = true;
	    } catch (ParseException e) {
    		Log.e(Config.LOGTAG, "TimeUtils.isUTCTimeFormat(String strUTCTime)");	    	
	    }
	    if (strUTCTime.length() < 20)
	      isValid = false;
	    return isValid;
	  }
	  
	  
	  public static Long UTCtoMilliseconds(String UTCtime){
		  // current time zone in our device
		  Long mMillis;
		  TimeZone localTimezone = TimeZone.getTimeZone(Config.MyTimeZone);  
			
 	      Calendar UTCTime = new GregorianCalendar(localTimezone);
		  if (isUTCTimeFormat(UTCtime)){
				SimpleDateFormat sdf = UTCTimeFormatter;
				Date mDate;
				try {
					mDate = sdf.parse(UTCtime);
					UTCTime.setTime(mDate);
					mMillis = UTCTime.getTimeInMillis();
				} catch (ParseException e) {
				// TODO Auto-generated catch block
		    		Log.e(Config.LOGTAG, "TimeUtils.UTCtoMilliseconds(String UTCtime)");	    	
					mMillis = 0L;
				}
		} else {
			UTCTime = Calendar.getInstance();		
			mMillis = UTCTime.getTimeInMillis();
		}
		return mMillis; // UTC in milliseconds
	 }
	  
	  public static Long MillisecondsToLocalTime(String millisecondsUTC, String timeZone){
		  // current time zone in our device
		  TimeZone localTimezone = TimeZone.getTimeZone(timeZone);  
		  Long mMillis = localTimezone.getRawOffset() + Long.valueOf(millisecondsUTC);
		  return mMillis;
	  }
	  
	  public static Long UTCMillisecondsToLocalTimeLong(Long millisecondsUTC, String timeZone){
		  // current time zone in our device
		  TimeZone localTimezone = TimeZone.getTimeZone(timeZone);  
		  Long mMillis = localTimezone.getRawOffset() + millisecondsUTC;
		  return mMillis;
	  }

	  
	  public static Calendar MillisecondsLocalTime(Long millisLocalTime, String timeZone){
		  // current time zone in our device
		  TimeZone localTimezone = TimeZone.getTimeZone(timeZone);  
		  Calendar cal = new GregorianCalendar(localTimezone);
		  cal.setTimeInMillis(millisLocalTime - localTimezone.getRawOffset());
		  return cal;
	  }
	  
	  public static Calendar MillisecondsLocalTime(String millisLocalTime, String timeZone){
		  // current time zone in our device
		  TimeZone localTimezone = TimeZone.getTimeZone(timeZone);  
		  Calendar cal = new GregorianCalendar(localTimezone);
		  cal.setTimeInMillis(Long.valueOf(millisLocalTime));
		  return cal;
	  }
 
	  
	public static String getTimeStampToString(Long myTimeStamp, String timeZone, Context mContext,boolean localTime){
		String res;

		TimeZone localTimezone = TimeZone.getTimeZone(timeZone);  
		int addTime = localTimezone.getRawOffset();
 	    Calendar cal = new GregorianCalendar(localTimezone);
		if (myTimeStamp == null){
			myTimeStamp = 0L;
		} else {
			if (localTime){
				cal.setTimeInMillis(myTimeStamp);				
			} else {
				cal.setTimeInMillis(myTimeStamp + addTime);
			}
		}
		res = DateFormater.DateTimeFormat(cal);
		res = mContext.getResources().getString(R.string.retrieved) + " " + res;
		
		return res;
	}
	  
}
	  