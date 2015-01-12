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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.sanbo.moveonapp.R;

import android.content.Context;
import android.util.Log;

public class TimeFormater
{
	private static TimeFormater INSTANCE = null;
	private static Calendar TimeEndMorning = Calendar.getInstance(Locale.getDefault());
	private static Calendar TimeEndNight = Calendar.getInstance(Locale.getDefault());
	
	private static void createInstance(){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "TimeFormater.createInstance()");
		if (INSTANCE == null){
			// Hora inici primer servei
			TimeEndMorning.set(Calendar.HOUR_OF_DAY, 4);
			TimeEndMorning.set(Calendar.MINUTE, 5);
			TimeEndMorning.set(Calendar.SECOND, 0);
			// Hora finalització darrer servei
			TimeEndNight.set(Calendar.HOUR_OF_DAY, 2);
			TimeEndMorning.set(Calendar.MINUTE, 30);
			TimeEndNight.set(Calendar.SECOND, 0);
			INSTANCE = new TimeFormater();
			// Per a Línia 41 (Exc. servei 23:45 diumenge correspon a divendres)
			// Cal adequar Weekday a Dl/Dj Friday Saturday i Sunday
			// o habilitar línies nocturnes...
		}
		return;
	}
	
	public static TimeFormater getInstance(){
		if (Config.DEBUG) Log.d("TimeFormater", "getInstance()");
		// Cream la instància de temps
		if (INSTANCE == null)
			createInstance();
		return INSTANCE;
	}
	
	public int compareDates(Calendar mCalendar1, Calendar mCalendar2)
	// retorna
	/* 0 si les dates són iguals
	 * 1 si mCalendar1 > mCalendar 2
	 * -1 si mCalendar1 < mCalendar2
	 * si mCalendar2 es null retorna 1
	 * si mCalendar2 != null i mCalendar1 es null retorna -1
	 */
	{
		if (Config.DEBUG) Log.d(Config.LOGTAG, "TimeFormater.compareDates( calendar1, calendar2)");
		if (mCalendar2 == null) {
			return 1;
		} else if (mCalendar1 == null) {
			return -1;
		} else {
			if (mCalendar1.get(Calendar.YEAR) > mCalendar2.get(Calendar.YEAR)) {
				return 1;
			} else if (mCalendar1.get(Calendar.YEAR) < mCalendar2.get(Calendar.YEAR)) {
				return -1;
			} else if (mCalendar1.get(Calendar.MONTH) > mCalendar2.get(Calendar.MONTH)) {
				return 1;
			} else if (mCalendar1.get(Calendar.MONTH) < mCalendar2.get(Calendar.MONTH)) {
				return -1;
			} else if (mCalendar1.get(Calendar.DAY_OF_MONTH) > mCalendar2.get(Calendar.DAY_OF_MONTH)) {
				return 1;
			} else if (mCalendar1.get(Calendar.DAY_OF_MONTH) < mCalendar2.get(Calendar.DAY_OF_MONTH)) {
				return -1;
			} else {
				// les dates són iguals
				return 0;
			}
		}
	}
	
	public int compareDates(Date mDate1, Date mDate2){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "TimeFormater.compareDates( date1, date2)");
		if (mDate2 == null) {
			return 1;
		} else if (mDate1 == null) {
			return -1;
		} else {
			Calendar mCalendar1;
			Calendar mCalendar2;
			mCalendar1 = Calendar.getInstance();
			mCalendar1.setTime(mDate1);
			mCalendar2 = Calendar.getInstance();
			mCalendar2.setTime(mDate2);
			if (mCalendar1.get(Calendar.YEAR) > mCalendar2.get(Calendar.YEAR)) {
				return 1;
			} else if (mCalendar1.get(Calendar.YEAR) < mCalendar2.get(Calendar.YEAR)) {
				return -1;
			} else if (mCalendar1.get(Calendar.MONTH) > mCalendar2.get(Calendar.MONTH)) {
				return 1;
			} else if (mCalendar1.get(Calendar.MONTH) < mCalendar2.get(Calendar.MONTH)) {
				return -1;
			} else if (mCalendar1.get(Calendar.DAY_OF_MONTH) > mCalendar2.get(Calendar.DAY_OF_MONTH)) {
				return 1;
			} else if (mCalendar1.get(Calendar.DAY_OF_MONTH) < mCalendar2.get(Calendar.DAY_OF_MONTH)) {
				return -1;
			} else {
				// les dates són iguals
				return 0;
			}
		}
	}
	
	public int compareHours(Calendar mCalendar1, Calendar mCalendar2){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "TimeFormater.compareHours( calendar1, calendar2)");
		if (mCalendar2 == null) {
			return 1;
		} else if (mCalendar1 == null) {
			return -1;
		} else {
			if (mCalendar1.equals(mCalendar2)) {
				return 0;
			} else if (mCalendar1.before(mCalendar2)) {
				return -1;
			} else {
				return 1;
			}
		}
	}
	
	public int compareHours(Date mDate1, Date mDate2){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "TimeFormater.compareHours( date1, date2)");

		// compara dos objectes Date
		if (mDate2 == null) {
			return 1;
		} else if (mDate1 == null) {
			return -1;
		} else {
			Calendar mCalendar1;
			Calendar mCalendar2;
			mCalendar1 = Calendar.getInstance();
			mCalendar1.setTime(mDate1);
			mCalendar2 = Calendar.getInstance();
			mCalendar2.setTime(mDate2);
			if (mCalendar1.equals(mCalendar2)) {
				return 0;
			} else if (mCalendar1.before(mCalendar2)) {
				return -1;
			} else {
				return 1;
			}
		}
	}
	
	public Calendar getDate(int mYear, int mMonth, int mDay_of_month){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "TimeFormater.getDate(year, month, day_of_month)");

		Calendar localCalendar = Calendar.getInstance(Locale.getDefault());
		localCalendar.set(Calendar.YEAR, mYear);
		localCalendar.set(Calendar.MONTH, mMonth);
		localCalendar.set(Calendar.DAY_OF_MONTH, mDay_of_month);
		return localCalendar;
	}

	public Calendar getDate(String mString)
	{
	    Calendar cal = null;
		try
		{
		    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		    Date date = format.parse(mString);
		    cal=Calendar.getInstance();
		    cal.setTime(date);
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
		    // parsing failed
			e.printStackTrace();
		}
		return cal;
	}
	
	public Calendar getTimeEndMorning(){
	return TimeEndMorning;
	}
	
	public Calendar getHoraLimiteNoche()
	{
	return TimeEndNight;
	}
	
	public Calendar getTime(int mHour, int mMinute, int mSecond)
	{
	Calendar localCalendar = Calendar.getInstance(Locale.getDefault());
	localCalendar.set(Calendar.HOUR, mHour);
	localCalendar.set(Calendar.MINUTE, mMinute);
	localCalendar.set(Calendar.SECOND, mSecond);
	return localCalendar;
	}
	
	public Calendar getTime(String mString)
	{
	    Calendar cal = null;
		try
		{
		    SimpleDateFormat format = new SimpleDateFormat("H:m");
		    Date date = format.parse(mString);
		    cal=Calendar.getInstance();
		    cal.setTime(date);
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
		    // parsing failed
			e.printStackTrace();
		}
		return cal;
	}
	
	public double getTime(Calendar paramCalendar)
	{
	return 3600 * paramCalendar.get(Calendar.SECOND) + 60 * paramCalendar.get(Calendar.MINUTE) + paramCalendar.get(Calendar.HOUR_OF_DAY);
	}
	
	public boolean isTimeValid(Calendar mCalendar)
	{
	return (compareHours(mCalendar, TimeEndMorning) >= 0) && (compareHours(mCalendar, TimeEndNight) <= 0);
	}
	
	public Calendar subtratTime(Calendar mCalendar1, Calendar mCalendar2)
	{
	Calendar localCalendar = Calendar.getInstance(Locale.getDefault());
	localCalendar.setTime(mCalendar1.getTime());
	localCalendar.add(Calendar.HOUR_OF_DAY, -mCalendar2.get(Calendar.HOUR_OF_DAY));
	localCalendar.add(Calendar.MINUTE, -mCalendar2.get(Calendar.MINUTE));
	localCalendar.add(Calendar.SECOND, -mCalendar2.get(Calendar.SECOND));
	return localCalendar;
	}
	
	public Calendar setTime(double mDouble)
	{
	Calendar localCalendar = Calendar.getInstance(Locale.getDefault());
	localCalendar.set(Calendar.HOUR_OF_DAY, (int)mDouble / 3600);
	localCalendar.set(Calendar.MINUTE, (int)mDouble / 60 % 60);
	localCalendar.set(Calendar.SECOND, (int)mDouble % 60);
	return localCalendar;
	}
	
	public Calendar addTime(Calendar mCalendar1, Calendar mCalendar2)
	{
	Calendar localCalendar = Calendar.getInstance(Locale.getDefault());
	localCalendar.setTime(mCalendar1.getTime());
	localCalendar.add(Calendar.HOUR_OF_DAY, mCalendar2.get(Calendar.HOUR_OF_DAY));
	localCalendar.add(Calendar.MINUTE, mCalendar2.get(Calendar.MINUTE));
	localCalendar.add(Calendar.SECOND, mCalendar2.get(Calendar.SECOND));
	return localCalendar;
	}
	
	public static String formatLastUpdated(long lastUpdated, Context context) {
		String ret = context.getResources().getString(R.string.last_updated) + " ";
		
		long interval = (Calendar.getInstance().getTimeInMillis() -
							lastUpdated) / 1000;
		
		long mins, hours, days;
				
		if ((days = interval/86400) > 0) {
			ret += context.getString(R.string.more_than) + " " + days + " d" +
					((!context.getString(R.string.ago).equals(""))?
							" " + context.getString(R.string.ago) : "");
		} else if ((hours = interval/3600) > 0) {
			ret += context.getString(R.string.more_than) + " " + hours + " h" +
					((!context.getString(R.string.ago).equals(""))?
							" " + context.getString(R.string.ago) : "");
		} else if ((mins = interval/60) > 0) {
			ret += mins + " m" +
					((!context.getString(R.string.ago).equals(""))?
							" " + context.getString(R.string.ago) : "");
		} else {
			ret += (interval%60) + " s" +
					((!context.getString(R.string.ago).equals(""))?
							" " + context.getString(R.string.ago) : "");
		}
		
		return ret; 
	}

}
