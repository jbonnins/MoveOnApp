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
package com.sanbo.database;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;

import com.sanbo.datamodel.BusStation;
import com.sanbo.datamodel.Cab;
import com.sanbo.datamodel.CabOperator;
import com.sanbo.datamodel.FavouriteLine;
import com.sanbo.datamodel.FavouriteStation;
import com.sanbo.datamodel.Frequency;
import com.sanbo.datamodel.Itinerary;
import com.sanbo.datamodel.ItineraryTypeTransport;
import com.sanbo.datamodel.Line;
import com.sanbo.datamodel.Message;
import com.sanbo.datamodel.Place;
import com.sanbo.datamodel.Station;
import com.sanbo.datamodel.TimeTable;
import com.sanbo.datamodel.Transfer;
import com.sanbo.datamodel.Transport;
import com.sanbo.enumerated.TypeLanguage;
import com.sanbo.enumerated.TypeTransport;
import com.sanbo.utils.Config;

/**
 * @author Juan José Bonnín
 * 
 * Here we have functions to access to database,
 * if we need more than one operation with database
 * use the boolean _close = false if you need more dat
 * at the end you have only one open and one close connection
 * to database.
 */
public class DBHelper {
	// single instance from our database
    // SINGLETON DEFINITION
	private static DBHelper INSTANCE = null;
	// database connected
	private static boolean isConnect = false;
	// database connected writable
	private boolean isWritable = false;
	// Tables, columns and queries
	private DataStore mDatabase;
	// Our environment, our app  
	private Context myContext;
	
	private DBHelper() {
		
	}

	private synchronized static void createInstance(){
		try {
			if (INSTANCE == null)
				INSTANCE = new DBHelper();
			return;
		}
		catch (Exception localException){
			// not controlled
	    	Log.e(Config.LOGTAG, "DBHelper.createInstance() " + localException.toString());			
		}
	}

	public synchronized static DBHelper getInstance(){
		if (INSTANCE == null)
			// first access, creates instance
			createInstance();
		return INSTANCE;
	}	
		
	private void close_Database(){
		try {
			// close DataStore
			mDatabase.close();
			// indicates DataStore is not connected
			isConnect = false;
			isWritable = false;
			return;
		}
		catch (Exception localException){
			// not controlled
	    	Log.e(Config.LOGTAG, "DBHelper.close_Database() " + localException.toString());						
		}
	}
	
	public boolean close(){
		try {
			close_Database();
		}
		catch (Exception localException){
		}
		return !isConnect;
	}

	private boolean open_Database(Context mContext, boolean writable){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DBHelper.open_Database()");    
		try {
			// define my context
			myContext = mContext;
			// define my database
			mDatabase = new DataStore(myContext);
			// creates my database only first access
			mDatabase.createDataBase();
			// open my database
			if (writable){
				mDatabase.openDBWritable();
			} else {
				mDatabase.openDataBase();
			}
			// indicates DataStore is connected
			isConnect = true;
			// return the value, not the object
			return isConnect;
		}
		catch (Exception localException){
			// not controlled
	    	Log.e(Config.LOGTAG, "DBHelper.open_Database(Context mContext, boolean writable) " + localException.toString());									
		}
		// if arrives here, indicates database not open
		return false;
	}
	
	// creating our model of transport
	public void createPublicTransport(Context mContext, TypeLanguage mLang, int distance){
		
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DBHelper.createPublicTransport()");
		if ((isConnect) || (open_Database(mContext, false))){
			if (Config.DEBUG) Log.i(Config.LOGTAG, "DBHelper.createPublicTransport().isConnect");
			
			// getting data from DataStore
			mDatabase.createPublicTransport(mLang, distance);
			// closing DataStore
			close_Database();
		}
	}
	
	// ask for all TypeTransport
	public ArrayList<Transport> getAllTypeOfTransport(boolean onlyFavorite, boolean _close){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DBHelper.getAllTypeOfTransport(boolean onlyFavorite, boolean _close)");
		
		ArrayList<Transport> resul = null;
		if ((isConnect) || (open_Database(myContext, false))){
			// getting data from DataStore
			resul = mDatabase.getAllTypeOfTransport(onlyFavorite);
			// closing DataStore
			if (_close) close_Database();
		}
		return resul;
	}
	
	// ask for all Itineraries
	public ArrayList<Itinerary> getAllItinerary(int distance, boolean onlyFavorite, boolean _close){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DBHelper.getAllItinerary(int distance, boolean onlyFavorite, boolean _close)");
		
		ArrayList<Itinerary> resul = null;
		if ((isConnect) || (open_Database(myContext, false))){
			// getting data from DataStore
			resul = mDatabase.getAllItinerary(distance, onlyFavorite);
			// closing DataStore
			if (_close) close_Database();
		}
		return resul;
	}

	// ask for all Stations
	public ArrayList<Station> getAllStations(int distance, boolean onlyFavorite, boolean _close){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DBHelper.getAllStations(boolean _close)");
		
		ArrayList<Station> resul = null;
		if ((isConnect) || (open_Database(myContext, false))){
			// getting data from DataStore
			resul = mDatabase.getAllStationFavoriteTransport(distance, onlyFavorite);
			// closing DataStore
			if (_close) close_Database();
		}
		return resul;
	}
	
	// ask for all Messages, we know the active language
	public ArrayList<Message> getAllMessagesByLanguage(boolean _close, TypeLanguage lang){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DBHelper.getAllFrequencyByLineID(int)");
		// we need the active language, we got message in three languages en, ca, es by default in en
		
		ArrayList<Message> resul = null;
		if ((isConnect) || (open_Database(myContext, false))){
			// getting data from DataStore
			resul = mDatabase.getAllMessages(lang);
			// closing DataStore
			if (_close) close_Database();
		}
		return resul;
	}

	// ask if between A and B can we transfer 
	public boolean getTranferBetweenA_B(int idStationA, int idStationB, boolean _close){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DBHelper.getTranferBetweenA_B(intA, intB)"); 
		boolean resul = false;
		if ((isConnect) || (open_Database(myContext, false))){
			// getting data from DataStore
			resul = mDatabase.isTranferBetweenA_B(idStationA, idStationB);
			// closing DataStore
			if (_close) close_Database();
		}
		return resul;
	}

	// ask for all Stations
	public Itinerary getFirstItineraryBiciPalma(int distance, boolean _close){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DBHelper.getFirstItineraryBiciPalma(boolean _close)");
		
		Itinerary resul = null;
		if ((isConnect) || (open_Database(myContext, false))){
			// getting data from DataStore
			
			resul = mDatabase.getOnlyFirstItinerary(distance);
			// closing DataStore
			if (_close) close_Database();
		}
		return resul;
	}	
	
	// ask for all Stations
	public ArrayList<Station> getAllBikeStationsByLine(int idLine, int distance, boolean _close){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DBHelper.getAllBikeStationsByLine(int idLine, boolean _close)");
		
		ArrayList<Station> resul = null;
		if ((isConnect) || (open_Database(myContext, false))){
			// getting data from DataStore
			resul = mDatabase.getAllBikeStationsByLine(idLine, distance);
			// closing DataStore
			if (_close) close_Database();
		}
		return resul;
	}	
	
	// ask for all Stations
	public ArrayList<Station> getAllBikeStations(int idItinerary, int distance, boolean _close){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DBHelper.getAllBikeStations(int idLine, boolean _close)");
		
		ArrayList<Station> resul = null;
		if ((isConnect) || (open_Database(myContext, false))){
			// getting data from DataStore
			resul = mDatabase.getAllBikeStations(idItinerary, distance);
			// closing DataStore
			if (_close) close_Database();
		}
		return resul;
	}	

	// ask for StationOW of idLine 
	public ArrayList<Station> getAllStationOW(int idLine, int distance, boolean _close){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DBHelper.getAllStationOWByLineID(int)"); 
		ArrayList<Station> resul = null;
		if ((isConnect) || (open_Database(myContext, false))){
			// getting data from DataStore
			resul = mDatabase.getAllStationOW(idLine, distance);
			// closing DataStore
			if (_close) close_Database();
		}
		return resul;
	}
	
	// ask for StationRT of idLine 
	public ArrayList<Station> getAllStationRT(int idLine, int distance, boolean _close){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DBHelper.getAllStationRTByLineID(int)"); 
		ArrayList<Station> resul = null;
		if ((isConnect) || (open_Database(myContext, false))){
			// getting data from DataStore
			resul = mDatabase.getAllStationRT(idLine, distance);
			// closing DataStore
			if (_close) close_Database();
		}
		return resul;
	}

	// ask for TimeTable of idLine 
	public ArrayList<TimeTable> getAllTimeTable(int idLine, boolean _close){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DBHelper.getAllTimeTableByLineID(int)"); 
		ArrayList<TimeTable> resul = null;
		if ((isConnect) || (open_Database(myContext, false))){
			// getting data from DataStore
			resul = mDatabase.getAllTimeTable(idLine);
			// closing DataStore
			if (_close) close_Database();
		}
		return resul;
	}

	// ask for Frequency of idLine 
	public ArrayList<Frequency> getAllFrequency(int idLine, boolean _close){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DBHelper.getAllFrequencyByLineID(int)"); 
		ArrayList<Frequency> resul = null;
		if ((isConnect) || (open_Database(myContext, false))){
			// getting data from DataStore
			resul = mDatabase.getAllFrequency(idLine);
			// closing DataStore
			if (_close) close_Database();
		}
		return resul;
	}
	
	// ask for all Transfers (pobrably we need distance between station, is better to get thetransfers we need
	public ArrayList<Transfer> getAllTransfers(boolean _close){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DBHelper.getAllFrequencyByLineID(int)"); 
		ArrayList<Transfer> resul = null;
		if ((isConnect) || (open_Database(myContext, false))){
			// getting data from DataStore
			resul = mDatabase.getAllTransfer();
			// closing DataStore
			if (_close) close_Database();
		}
		return resul;
	}
	
	// Itinerary Code
	// Transport FK_id_TypeOfTransport
	// ask for all Transfers by distinct type of transport by Id_Itinerary
	public ArrayList<ItineraryTypeTransport> getAllTransferFavouriteTransportByIdItinerary(Itinerary myItinerary, int distance, boolean _close){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DBHelper.getAllFrequencyByLineID(int)"); 
		ArrayList<ItineraryTypeTransport> resul = null;
		if ((isConnect) || (open_Database(myContext, false))){
			// getting data from DataStore
			resul = mDatabase.getAllTransferDistinctTypeOfTransportByIdItinerary(myItinerary.get_id(), distance);
			// closing DataStore
			if (_close) close_Database();
		}
		return resul;
	}
	
	// Itinerary Code
	// Transport FK_id_TypeOfTransport
	// ask for all Transfers by distinct type of transport by Id_Itinerary
	public ArrayList<ItineraryTypeTransport> getAllTransferFavouriteTransportByIdLine(Line myLine, int distance, boolean _close){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DBHelper.getAllTransferFavouriteTransportByIdLine(myLine, distance, true)"); 
		ArrayList<ItineraryTypeTransport> resul = null;
		if ((isConnect) || (open_Database(myContext, false))){
			// getting data from DataStore
			resul = mDatabase.getAllTransferDistinctTypeOfTransportByIdLine(myLine.get_id(), distance);
			// closing DataStore
			if (_close) close_Database();
		}
		return resul;
	}
	
	// Itinerary Code
	// Transport FK_id_TypeOfTransport
	// ask for all Transfers by distinct type of transport by Id_Itinerary
	public ArrayList<ItineraryTypeTransport> getAllTransferFavouriteTransportByIdStation(Station myStation, Line myLine, int distance, boolean _close){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DBHelper.getAllTransferFavouriteTransportByIdStation(Line Line, Station Station, int distance, boolean _close)"); 
		ArrayList<ItineraryTypeTransport> resul = null;
		if ((isConnect) || (open_Database(myContext, false))){
			// getting data from DataStore
			resul = mDatabase.getAllTransferDistinctTypeOfTransportByIdStation(myStation.get_id(), myLine.get_id(), distance);
			// closing DataStore
			if (_close) close_Database();
		}
		return resul;
	}
	
	// Itinerary Code
	// Transport FK_id_TypeOfTransport
	// ask for all Transfers by distinct type of transport by Id_Station
	public ArrayList<ItineraryTypeTransport> getAllTransferFavouriteTransportByIdStation(Station myStation, int distance, boolean _close){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DBHelper.getAllTransferFavouriteTransportByIdStation(Station myStation, int distance, boolean _close)"); 
		ArrayList<ItineraryTypeTransport> resul = null;
		if ((isConnect) || (open_Database(myContext, false))){
			// getting data from DataStore
			resul = mDatabase.getAllTransferDistinctTypeOfTransportByIdStation(myStation.get_id(), distance);
			// closing DataStore
			if (_close) close_Database();
		}
		return resul;
	}
	
	// Itinerary Code
	// Transport FK_id_TypeOfTransport
	// ask for all Transfers by distinct type of transport by Id_Station
	public ArrayList<ItineraryTypeTransport> getAllTransferByIdStation(Station myStation, int distance, boolean _close){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DBHelper.getAllTransferByIdStation(" + myStation.get_id() + ", distance " + distance + ")"); 
		ArrayList<ItineraryTypeTransport> resul = null;
		if ((isConnect) || (open_Database(myContext, false))){
			// getting data from DataStore
			resul = mDatabase.getAllTransferByIdStation(myStation.get_id(), distance);
			// closing DataStore
			if (_close) close_Database();
		}
		return resul;
	}

	// Itinerary Code
	// Transport FK_id_TypeOfTransport
	// ask for all Transfers by distinct type of transport by Id_Itinerary
	public ArrayList<ItineraryTypeTransport> getAllTransferFavouriteTransportByIdStationItinerary(Station myStation, Line myLine, int distance, boolean _close){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DBHelper.getAllTransferFavouriteTransportByIdStation(Line Line, Station Station, int distance, boolean _close)"); 
		ArrayList<ItineraryTypeTransport> resul = null;
		if ((isConnect) || (open_Database(myContext, false))){
			// getting data from DataStore
			resul = mDatabase.getAllTransferDistinctTypeOfTransportByIdStationItinerary(myStation.get_id(), myLine.getFK_id_Itinerary(), distance);
			// closing DataStore
			if (_close) close_Database();
		}
		return resul;
	}
	
	

	// save favorite Transport
	public boolean setIsFavoriteTransport(Transport myTransport, boolean isFavorite, boolean _close){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DBHelper.setIsFavoriteTransport(Transport myTransport, boolean _close)");
		boolean resul = false;
		if ((isWritable) || (open_Database(myContext, true))){
			// sending data to DataStore
			resul = mDatabase.setIsFavoriteTransport(myTransport, isFavorite);
			// closing DataStore
			if (_close) close_Database();
		}
		return resul;
	}

	// save favorite Itinerary
	public boolean setIsFavoriteItinerary(Itinerary myItinerary, boolean isFavorite, boolean _close){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DBHelper.setIsFavoriteItinerary(int Itinerary_id, boolean isFavorite, boolean _close)");
		boolean resul = false;
		if ((isWritable) || (open_Database(myContext, true))){
			// sending data to DataStore
			resul = mDatabase.setIsFavoriteItinerary(myItinerary, isFavorite);
			// closing DataStore
			if (_close) close_Database();
		}
		return resul;
	}
	
	// get is favorite Line
	public FavouriteLine getFavoriteLine(int idLine, boolean _close){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DBHelper.getFavoriteLine(Line myLine, boolean isFavorite, boolean _close)");
		FavouriteLine resul = null;
		if ((isConnect) || (open_Database(myContext, false))){
			// sending data to DataStore
			resul = mDatabase.getFavouriteLine(idLine);
			// closing DataStore
			if (_close) close_Database();
		}
		return resul;
	}

	// is type transport favorite
	public boolean IsTransportFavorite(int idTransport, boolean _close){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DBHelper.IsTypeTransportFavorite(int idTypeTransport, boolean _close)");
		boolean resul = false;
		if ((isConnect) || (open_Database(myContext, false))){
			// sending data to DataStore
			resul = mDatabase.isTransportFavorite(idTransport);
			// closing DataStore
			if (_close) close_Database();
		}
		return resul;
	}
	
	// is favorite Line
	public boolean IsFavoriteLine(int idLine, boolean _close){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DBHelper.setIsFavoriteLine(Line myLine, boolean isFavorite, boolean _close)");
		boolean resul = false;
		if ((isConnect) || (open_Database(myContext, false))){
			// sending data to DataStore
			resul = mDatabase.IsFavouriteLine(idLine);
			// closing DataStore
			if (_close) close_Database();
		}
		return resul;
	}

	// save favorite Line
	public boolean setIsFavoriteLine(Line myLine, boolean isFavorite, boolean _close){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DBHelper.setIsFavoriteLine(Line myLine, boolean isFavorite, boolean _close)");
		boolean resul = false;
		if ((isWritable) || (open_Database(myContext, true))){
			// sending data to DataStore
			resul = mDatabase.setIsFavoriteLine(myLine, isFavorite);
			// closing DataStore
			if (_close) close_Database();
		}
		return resul;
	}
	
	// get is favorite Station
	public FavouriteStation getFavoriteStation(int idStation, boolean _close){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DBHelper.getFavoriteStation(int idStation, boolean _close)");
		FavouriteStation resul = null;
		if ((isConnect) || (open_Database(myContext, false))){
			// sending data to DataStore
			resul = mDatabase.getFavouriteStation(idStation);
			// closing DataStore
			if (_close) close_Database();
		}
		return resul;
	}

	// is favorite Station
	public boolean IsFavoriteStation(int idStation, boolean _close){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DBHelper.IsFavoriteStation(int idStation, boolean _close)");
		boolean resul = false;
		if ((isConnect) || (open_Database(myContext, false))){
			// sending data to DataStore
			resul = mDatabase.IsFavouriteStation(idStation);
			// closing DataStore
			if (_close) close_Database();
		}
		return resul;
	}

	// save favorite Station
	public boolean setIsFavoriteStation(Station myStation, boolean isFavorite, boolean _close){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DBHelper.setIsFavoriteStation(Station myStation, boolean isFavorite, boolean _close)");
		boolean resul = false;
		if ((isWritable) || (open_Database(myContext, true))){
			// sending data to DataStore
			resul = mDatabase.setIsFavoriteStation(myStation, isFavorite);
			// closing DataStore
			if (_close) close_Database();
		}
		return resul;
	}

	// is Circle Line	
	public boolean getIsCircleLine(int idLine, boolean _close){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DBHelper.getIsCircleLine(Line myLine, boolean _close)");
		boolean resul = false;
		if ((isConnect) || (open_Database(myContext, false))){
			// sending data to DataStore
			resul = mDatabase.getLineIsCircleLine(idLine);
			// closing DataStore
			if (_close) close_Database();
		}
		return resul;	
	}
	
	// is Bike Line	
	public boolean getIsBikeLine(int idLine, boolean _close){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DBHelper.getIsBikeLine(Line myLine, boolean _close)");
		boolean resul = false;
		if ((isConnect) || (open_Database(myContext, false))){
			// sending data to DataStore
			resul = mDatabase.getLineIsBikeLine(idLine);
			// closing DataStore
			if (_close) close_Database();
		}
		return resul;	
	}
	
	// is Bus Line	
	public boolean getIsBusLine(int idLine, boolean _close){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DBHelper.getIsBusLine(int idLine, boolean _close)");
		boolean resul = false;
		if ((isConnect) || (open_Database(myContext, false))){
			// sending data to DataStore
			resul = mDatabase.getLineIsBusLine(idLine);
			// closing DataStore
			if (_close) close_Database();
		}
		return resul;	
	}

	// TimeStamp of TypeTransport
	public Long getTimeStamp(TypeTransport mType, Station myStation, boolean _close){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DBHelper.getTimeStamp(TypeTransport mType, boolean _close)");
		Long resul = null;
		int idStation;
		if (myStation == null) {
			idStation = -1;
		} else {
			idStation = myStation.get_id();
		}
		if ((isConnect) || (open_Database(myContext, false))){
			// getting data from DataStore
			resul = mDatabase.getTimeStamp(mType, idStation);
			// closing DataStore
			if (_close) close_Database();
		}
		return resul;			
	}
	
	public Itinerary getItineraryOfLine(int idLine, boolean _close){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DBHelper.getItineraryOfLine(int idLine, boolean _close)");
		Itinerary resul = null;
		if ((isConnect) || (open_Database(myContext, false))){
			// getting data from DataStore
			resul = mDatabase.getItineraryOfLine(idLine);
			// closing DataStore
			if (_close) close_Database();
		}
		return resul;			
	}
	
	// has disabled facilities	
	public boolean HasDisabledFacilities(int idItinerary, boolean _close){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DBHelper.getHasDisabledFacilities(int idItinerary, boolean _close)");
		boolean resul = false;
		if ((isConnect) || (open_Database(myContext, false))){
			// sending data to DataStore
			resul = mDatabase.getItineraryHasDisabledFacilities(idItinerary);
			// closing DataStore
			if (_close) close_Database();
		}
		return resul;	
	}
	
	// modify BikeStation to DB
	public boolean setBikeStations(ArrayList<Station> pBikeStationsList, boolean _close){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DBHelper.setBikeStations(ArrayList<Station> pBikeStationsList)");
		boolean resul = false;
		if ((isWritable) || (open_Database(myContext, true))){
			// sending data to DataStore
			resul = mDatabase.setBikeStations(pBikeStationsList);
			// closing DataStore
			if (_close) close_Database();
		}
		return resul;	
		
	}
		
	public ArrayList<BusStation> getListAllBusStations(int idStation, boolean _close){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DBHelper.getListAllBusStations(int idStation, boolean _close)"); 
		ArrayList<BusStation> resul = null;
		if ((isConnect) || (open_Database(myContext, true))){
			// getting data from DataStore
			resul = mDatabase.getListAllBusStations(idStation);
			synchronized (this){
				if (resul.size() == 0){
					if ((isWritable) || (open_Database(myContext, true))){
						resul = mDatabase.CreateListAllBusStations(idStation);
					}
				}
				// closing DataStore
				if (_close) close_Database();
			}
		}
		return resul;
	}
	
	public Itinerary getItinerary(int idItinerary, boolean _close){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DBHelper.getItinerary(int idItinerary, boolean _close)"); 
		Itinerary resul = null;
		if ((isConnect) || (open_Database(myContext, false))){
			// getting data from DataStore
			resul = mDatabase.getItinerary(idItinerary);
			// closing DataStore
			if (_close) close_Database();
		}
		return resul;
	}
	
	// modify BusStation to DB
	public boolean setBusStations(Station pStation, boolean _close){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DBHelper.setBusStations(Station pStation, boolean _close)");
		boolean resul = false;
		if ((isWritable) || (open_Database(myContext, true))){
			// sending data to DataStore
			resul = mDatabase.setBusStations(pStation);
			// closing DataStore
			if (_close) close_Database();
		}
		return resul;	
		
	}
	
	// ask for all Places
	public ArrayList<Place> getAllPlaces(boolean onlyFavorite, boolean _close){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DBHelper.getAllPlaces(int distance, boolean onlyFavorite, boolean _close)");
		
		ArrayList<Place> resul = null;
		if ((isConnect) || (open_Database(myContext, false))){
			// getting data from DataStore
			resul = mDatabase.getAllPlaces(onlyFavorite);
			// closing DataStore
			if (_close) close_Database();
		}
		return resul;
	}
	
	// ask for all Cabs
	public ArrayList<Cab> getAllCabs(boolean onlyFavorite, boolean _close){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DBHelper.getAllCabs(boolean onlyFavorite, boolean _close)");
		
		ArrayList<Cab> resul = null;
		if ((isConnect) || (open_Database(myContext, false))){
			// getting data from DataStore
			resul = mDatabase.getAllCabs(onlyFavorite);
			// closing DataStore
			if (_close) close_Database();
		}
		return resul;
	}

	// ask for all CabOperators
	public ArrayList<CabOperator> getAllCabOperators(boolean onlyFavorite, boolean _close){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DBHelper.getAllCabs(boolean onlyFavorite, boolean _close)");
		
		ArrayList<CabOperator> resul = null;
		if ((isConnect) || (open_Database(myContext, false))){
			// getting data from DataStore
			resul = mDatabase.getAllCabOperators(onlyFavorite);
			// closing DataStore
			if (_close) close_Database();
		}
		return resul;
	}
	
	
	// save favorite Place
	public boolean setIsFavoritePlace(Place myPlace, boolean isFavorite, boolean _close){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DBHelper.setIsFavoritePlace(Place myPlace, boolean isFavorite, boolean _close)");
		boolean resul = false;
		if ((isWritable) || (open_Database(myContext, true))){
			// sending data to DataStore
			resul = mDatabase.setIsFavoritePlace(myPlace, isFavorite);
			// closing DataStore
			if (_close) close_Database();
		}
		return resul;
	}

	// save favorite Cab
	public boolean setIsFavoriteCab(Cab myCab, boolean isFavorite, boolean _close){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DBHelper.setIsFavoriteCab(Cab myCab, boolean isFavorite, boolean _close)");
		boolean resul = false;
		if ((isWritable) || (open_Database(myContext, true))){
			// sending data to DataStore
			resul = mDatabase.setIsFavoriteCab(myCab, isFavorite);
			// closing DataStore
			if (_close) close_Database();
		}
		return resul;
	}
	
	// save favorite CabOperator
	public boolean setIsFavoriteCabOperator(CabOperator myCabOperator, boolean isFavorite, boolean _close){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DBHelper.setIsFavoriteCabOperaor(CabOperator myCabOperator, boolean isFavorite, boolean _close)");
		boolean resul = false;
		if ((isWritable) || (open_Database(myContext, true))){
			// sending data to DataStore
			resul = mDatabase.setIsFavoriteCabOperator(myCabOperator, isFavorite);
			// closing DataStore
			if (_close) close_Database();
		}
		return resul;
	}


}
	
	
	
	
	
	
	
	 






