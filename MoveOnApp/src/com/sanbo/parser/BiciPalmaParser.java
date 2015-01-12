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
package com.sanbo.parser;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.sanbo.datamodel.Station;
import com.sanbo.utils.Config;


	public class BiciPalmaParser {
		
	    public static ArrayList <Station> parseNetworkJSON (String data)    {
	        ArrayList <Station> stations = new ArrayList<Station>();
	        JSONArray jsonArray;
	        JSONObject jsonObject;
	        Long lngAcum=0L, latAcum=0L;
			@SuppressWarnings("unused")
			int id;
	        
	        try {
	            jsonArray = new JSONArray(data);
	            
	            for (int i = 0; i < jsonArray.length(); i++) {
	                jsonObject = jsonArray.getJSONObject(i);
	                
	                lngAcum += jsonObject.getLong("lng");
	                latAcum += jsonObject.getLong("lat");
	                
	                id = jsonObject.getInt("id");
	                // Latitude & Longitude with 6 digits, we divide by "/1e7"
	                stations.add(new Station(
	                	 jsonObject.getString("number"),
                         jsonObject.getString("name"),
                         jsonObject.getDouble("lng") * 10,
                         jsonObject.getDouble("lat") * 10,
                         jsonObject.getString("timestamp"),
                         jsonObject.getInt("free"),
                         jsonObject.getInt("bikes")));
	            }
	        } catch (Exception e) {
				Log.e(Config.LOGTAG, "BiciPalmaParser.parseNetworkJSON (String data) ");
	        }
	        
	        return stations;
	    }
	}
