package com.sanbo.parser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.sanbo.datamodel.BusStation;
import com.sanbo.datamodel.Itinerary;
import com.sanbo.datamodel.Station;
import com.sanbo.utils.Config;


public class EMTParser {
	
	// ugly solution for line 7 and lin 14
	// line 7 from Son Gotleu first is to Son Xigala and second to Son Vida
	// line 7 from Son Xigala and Son Vida we mix time
	// line 14 from Eusebi Estada is to San Jordi and second to S'Hostalot
	// line 14 from San Jordi and S'Hostalot we mix time
	// we check in PublicTransport.addDataNetworkToListBusStations
    
    public static Station parseNetworkJSON (String myCode, String data){
    	// Temporary identifier
    	//int _id = -1;
        Station mStation = null;
        ArrayList<BusStation> mBusStation = new ArrayList<BusStation>(); 
        // station data
        JSONObject jsonObject;
        // line data
        JSONArray jsonArray;
        // timeStamp
        String timeStamp;
        // nombreParada
        String nombreParada;
        // FirstBus
        JSONObject myBus;
        Long mySecondsBus;
        int myMeters;
        Calendar cal;
        Long myTime;
        String line;
        
        // we don't have information from station except name and timestamp :(
        

        try {
            jsonObject = new JSONObject(data);
            timeStamp = jsonObject.getString("timestamp");
            nombreParada = jsonObject.getString("nombreParada");
            if (timeStamp != null && nombreParada != null){		
            	mStation = new Station(myCode, nombreParada , timeStamp);
            	
            }
            try {
            	jsonArray = jsonObject.getJSONArray("estimaciones");
            } catch (Exception e) {
    			Log.e(Config.LOGTAG, "EMTParser.parseNetworkJSON (String data) "  + e.toString());
    			jsonArray = new JSONArray();
            }
            if (jsonArray != null){
	            for (int i = 0; i < jsonArray.length(); i++) {
	                jsonObject = jsonArray.getJSONObject(i);
	                            	            	
	            	// First bus seconds to milliseconds
	                try{
	                	myBus = jsonObject.getJSONObject("vh_first");
	                } catch (Exception e) {
	        			Log.e(Config.LOGTAG, "EMTParser.parseNetworkJSON (String data) "  + e.toString());
	        			myBus = null;
	                }
	            	if ( myBus != null){
	            		try{
	            			mySecondsBus = myBus.getLong("seconds");
	            			mySecondsBus = mySecondsBus < 0 ? 0 : mySecondsBus;
		                } catch (Exception e) {
		        			Log.e(Config.LOGTAG, "EMTParser.parseNetworkJSON (String data) "  + e.toString());
		        			mySecondsBus = null;
		                }
		            	if (mySecondsBus != null) { 
		            		mySecondsBus = mySecondsBus * 1000; // seconds to milliseconds
		                	cal = Calendar.getInstance(TimeZone.getTimeZone(Config.MyTimeZone));
		                	myTime = mStation.getTimeStamp().getTimeInMillis() + mySecondsBus;
		                	cal.setTimeInMillis(myTime);
		                	try {
		                		myMeters = myBus.getInt("meters");
			                } catch (Exception e) {
			        			Log.e(Config.LOGTAG, "EMTParser.parseNetworkJSON (String data) "  + e.toString());
			        			myMeters = -1;
			                }
		            	} else {
		            		cal = null;
		                	myMeters = -1;
		            	}
		            	BusStation newBusStation = new BusStation(myBus.getString("destino"), cal, myMeters);
		            	try{
		            		line = jsonObject.getString("line");
		                } catch (Exception e) {
		        			Log.e(Config.LOGTAG, "EMTParser.parseNetworkJSON (String data) "  + e.toString());
		        			line = null;
		                }
		            	if (line != null){
		            		Itinerary newItinerary = new Itinerary(line);
		            		// Itinerary of BusStation
		            		newBusStation.setItinerary(newItinerary);
		            	}
		            	// Second bus
		            	try{
		            		myBus = jsonObject.getJSONObject("vh_second");
		                } catch (Exception e) {
		        			Log.e(Config.LOGTAG, "EMTParser.parseNetworkJSON (String data) "  + e.toString());
		        			myBus = null;
		                }
		            	if (myBus != null){
		            		try{
		            			mySecondsBus = myBus.getLong("seconds");
		            			mySecondsBus = mySecondsBus < 0 ? 0 : mySecondsBus;
			                } catch (Exception e) {
			        			Log.e(Config.LOGTAG, "EMTParser.parseNetworkJSON (String data) "  + e.toString());
			        			mySecondsBus = null;
			                }
			            	if (mySecondsBus != null) { 
			            		mySecondsBus = mySecondsBus * 1000; // seconds to milliseconds
			                	cal = Calendar.getInstance(TimeZone.getTimeZone(Config.MyTimeZone));
			                	myTime = mStation.getTimeStamp().getTimeInMillis() + mySecondsBus;
			                	cal.setTimeInMillis(myTime);
			                	try{
			                		myMeters = myBus.getInt("meters");
				                } catch (Exception e) {
				        			Log.e(Config.LOGTAG, "EMTParser.parseNetworkJSON (String data) "  + e.toString());
				        			myMeters = -1;
				                }
			                	//if (myMeters < 0) myMeters = 0;
			            	} else {
			            		cal = null;
			                	myMeters = -1;
			            	} 	
		            	} else {
		            		cal = null;
		                	myMeters = -1;		            		
		            	}
		            	newBusStation.setSecondBus(cal);
		            	newBusStation.setSecondBusMeters(myMeters);
		            	// finished BusStation
		            	mBusStation.add(newBusStation);	            		
	            	}
	            }
            }
            mStation.setBusStations(mBusStation);
        } catch (Exception e) {
			Log.e(Config.LOGTAG, "EMTParser.parseNetworkJSON (String data) "  + e.toString());
        }
        return mStation;        
    }
}
