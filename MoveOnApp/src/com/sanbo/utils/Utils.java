

package com.sanbo.utils;


import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.regex.Pattern;

import android.app.Application;
import android.util.Log;

public class Utils extends Application {

    	final static long MILLSECS_PER_DAY = 24 * 60 * 60 * 1000; //Milliseconds per day 
    	final static long MILISECS_PER_MINUTE = 60 * 1000; //Milliseconds per minute
    	final static long MILISECS_PER_SECOND = 1000; //Milliseconds per second

        @SuppressWarnings({ "rawtypes", "unchecked" })
        public static ArrayList<String> getOrderStreamTitle(ArrayList<String> list) {
                ArrayList<String> resultList = new ArrayList<String>();
                
                Comparator cmp = Collator.getInstance(java.util.Locale.UK);
                Object[] ob = list.toArray();
                Arrays.sort(ob, cmp);
                
                for(int i = 0 ; i < ob.length; i++) {
                        resultList.add((String) ob[i]);
                }
                
                return resultList;
        }
        
        public static boolean isNameExists(String name) {
            //return mDatabaseProvider.isNameExists(name);
        	return true;
        }
        
        public static String getStationDistance(Float distance){
        	final String metro = " m";
        	final String kmetro = " Km";
        	Float val;
        	String res;
        	
        	val = distance;
        	
        	if (val != null && val >= 0 ){
        		if (val > 10000D){
    	    		res = "( " + Math.round(val/1000) + kmetro + " )";
    	    	} else{
    	    		res = "( " + Math.round(val) + metro + " )";    		
    	    	}
        	} else {
        		res = "( == )";
        	}
    		return res;
        }

        public static String getStationCodeName(String code, String name){
        	final String separator = " :: ";
        	String mCode;
    		// formatter for code
        	if (isNumber(code) && Integer.valueOf(code) < 1000){
        		// Show station as 0000
        		try{
        			mCode = String.format("%03d", Integer.valueOf(code));
        		}
        	    catch (Exception e){
        	    	Log.e(Config.LOGTAG, "Utils.getStationName()");
        	    	// Error translating Station Code
        	    	mCode = code;
        	    }
        	} else {
        		mCode = code;
        	}
        	
    		return mCode + separator + name;
        }       
        
        private static final Pattern numberPattern = Pattern.compile("-?\\d+");
        
        public static boolean isNumber(String str){
        	return str != null && numberPattern.matcher(str).matches();
        }
        
        public static String getTimeFormated(String timeInSeconds){
        	final String seconds = " s";
        	final String minutes = " m ";
        	String res = null;
        	int myTimeMinutes;
        	int myTimeSeconds;
        	
        	try{
        		Integer myTime = Integer.valueOf(timeInSeconds);
        		if (myTime > 60){
        			myTimeMinutes = myTime/60;
        			myTimeSeconds = myTime - myTimeMinutes/60;
            		if (myTimeMinutes > 0){
            			res = myTimeMinutes + minutes;
            		} else {
            			res = "";
            		}
            		if (myTimeSeconds > 0){
            			res = res + myTimeSeconds + seconds;
            		} else {
            			res = "0";
            		}
             	}
        	}
        	catch (Exception e){
    	    	Log.e(Config.LOGTAG, "Utils.getTimeFormated()");
    	    	// Error translating time
    	    	res = "==";
    	    } 
       		return res;
        }

        public static String getTimeFormated(int timeInSeconds){
        	final String seconds = " s";
        	final String minutes = " m ";
        	String res = null;
        	int myTimeMinutes;
        	int myTimeSeconds;
        	
        	try{
        		if (timeInSeconds > 60){
        			myTimeMinutes = timeInSeconds/60;
        			myTimeSeconds = timeInSeconds - myTimeMinutes/60;
            		if (myTimeMinutes > 0){
            			res = myTimeMinutes + minutes;
            		} else {
            			res = "";
            		}
            		if (myTimeSeconds > 0){
            			res = res + myTimeSeconds + seconds;
            		} else {
            			res = "0";
            		}
             	}
        	}
        	catch (Exception e){
    	    	Log.e(Config.LOGTAG, "Utils.getTimeFormated()");
    	    	// Error translating time
    	    	res = "==";
    	    } 
       		return res;
        }
        
        public static String getTimeFormated(long timeInMiliSeconds){
        	final String seconds = " s";
        	final String minutes = " m ";
        	String res = null;
        	int myTimeMinutes = 0;
        	int myTimeSeconds = 0;
        	
        	try{
        		if (timeInMiliSeconds > MILLSECS_PER_DAY){
        			res = "++";
        		} else {
	        		if (timeInMiliSeconds > MILISECS_PER_MINUTE){
	        			myTimeMinutes =  Long.valueOf(timeInMiliSeconds / MILISECS_PER_MINUTE).intValue();
	        		}
        			myTimeSeconds = Long.valueOf((timeInMiliSeconds - MILISECS_PER_MINUTE * myTimeMinutes) 
        					/ MILISECS_PER_SECOND).intValue();
            		if (myTimeMinutes > 0){
            			res = myTimeMinutes + minutes;
            		} else {
            			res = "";
            		}
            		if (myTimeSeconds > 0){
            			res = res + myTimeSeconds + seconds;
            		} else {
            			res = "0";
            		}
             	}
        	}
        	catch (Exception e){
    	    	Log.e(Config.LOGTAG, "Utils.getTimeFormated()");
    	    	// Error translating time
    	    	res = "==";
    	    } 
       		return res;
        }
        
        public static String unescape(String description) {
            return description.replaceAll("\\\\n", "\\\n");
        }

        
}
