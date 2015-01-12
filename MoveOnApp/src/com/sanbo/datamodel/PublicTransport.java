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
package com.sanbo.datamodel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import android.location.Location;
import android.util.Log;

import com.sanbo.database.DBHelper;
import com.sanbo.enumerated.TypeLanguage;
import com.sanbo.enumerated.TypeTransport;
import com.sanbo.utils.Config;
import com.sanbo.utils.Haversine;
import com.sanbo.utils.NetworkInformation;
import com.sanbo.utils.Preferences;
import com.sanbo.coordinates.*;

/**
 * @author Juanjo Bonnín
 * Singleton Class, we can acceded to all public transport model from this point
 * We collect all the methods, all the data at this point
 *
 */
public class PublicTransport {
	// Instance from PublicTransport
    // SINGLETON DEFINITION
	private static PublicTransport INSTANCE = null;
	// distance to collect transfer
	private Integer distance;
	
	// Preferences
	private Preferences mPreferences;

	// Type of transport included in this app
	private static boolean _TypeOfTransport;
	private ArrayList<Transport> listAllTypeOfTransport;
	// Concept of Line, every Itinerary can have different Lines
	private static boolean _Itinerary;
	private static boolean _ItineraryWithTransfer;
	private ArrayList<Itinerary> listAllItineraries;
	// Concept of Station ordered by Name
	private static boolean _Station;
	private static boolean _StationWithTransfer;
	private ArrayList<Station> listAllStations;
	// Concept of Message affecting to the transport
	private static boolean _Message;
	private ArrayList<Message> listAllMessages;
	// Concept of Bike Stations ordered by Name
	private static boolean _BikeStation;
	private ArrayList<Station> listAllBikeStations;
	// 
	
	// Concept of Transfer from one Line to other Line (distance between station < 100 m
	// Change distance of transfer between 0 to 150 m, every time you change it, you get new data
	// 0 m transfer at same stop = Train, Bus 0r Subway...we fixed Station Subwa and Train same place
	// but only according Transfer on T_Station, if there is not a transfer on the same station...
	private boolean _Transfer;
	private ArrayList<Transfer> listAllTransfers;
	// Concept of Route from point to point
	private boolean _RouteStation;
	private ArrayList<Station> listStationsRoute;
	
	private PublicTransport() {
		mPreferences = Preferences.getInstance();
	}
	
	// Creates instance of public transport
	private synchronized static void createInstance()
	{
		if (INSTANCE == null)
			INSTANCE = new PublicTransport();
		return;
	}
	  	
	/**
	 * @return the iNSTANCE
	 */
	public static synchronized PublicTransport getInstance() {
		if (INSTANCE == null)
			createInstance();
		return INSTANCE;
	}
	
	private void refreshDistance(){
		// refresh distance value at the end
		if (distance == null || distance != mPreferences.getOptionMeter()){
			distance = mPreferences.getOptionMeter();
			// Force get data
			ResetAllLists();
		}
	}
		
	public boolean isChangedLanguage(TypeLanguage mLang){
		 if (mPreferences.getOptionLanguage().toString().compareToIgnoreCase(mLang.toString()) != 0){
				mPreferences.setOptionLanguage(mLang);
		 		return true;
		 }
		 return false;
	}
	
	public void ResetAllLists(){
		
		refreshListAllTypeOfTransport();
		refreshListAllItineraries();
		refreshListAllStations();
		refreshListAllMessages();
		refreshListAllTransfers();
		refreshListStationsRoute();
		refreshListAllBikeStations();
	}
	
	/**
	 * @return the listAllTypeOfTransport
	 */
	public ArrayList<Transport> getListAllTypeOfTransport(boolean onlyFavorite) {
		ArrayList<Transport> theList = null;
		synchronized(this){
			if (!onlyFavorite){
				if (!_TypeOfTransport) {
					// return transfer list, first time get data from DB
					DBHelper DataConnect = DBHelper.getInstance();
					listAllTypeOfTransport = DataConnect.getAllTypeOfTransport(false, true);
					_TypeOfTransport = true;
				}
				theList = listAllTypeOfTransport;
			} else {
				// return transfer list, first time get data from DB
				DBHelper DataConnect = DBHelper.getInstance();
				theList = DataConnect.getAllTypeOfTransport(true, true);				
			}
		}
		return theList;
	}
	
	/**
	 * @param listAllTypeOfTransport the listAllTypeOfTransport to set
	 */
	public void setListAllTypeOfTransport(ArrayList<Transport> listTypeOfTransport) {
		_TypeOfTransport = true;
		listAllTypeOfTransport = listTypeOfTransport;
	}
	
	public synchronized boolean refreshListAllTypeOfTransport(){
		listAllTypeOfTransport = null;
		_TypeOfTransport = false;
		return true;
	}
	
	/**
	 * @return the listAllItineraries
	 */
	public ArrayList<Itinerary> getListAllItineraries() {
		synchronized(this){
			if (!_Itinerary) {
				// return transfer list, first time get data from DB
				DBHelper DataConnect = DBHelper.getInstance();
				listAllItineraries = DataConnect.getAllItinerary(mPreferences.getOptionMeter(), false, true);
				_Itinerary = true;
				_ItineraryWithTransfer = false;
			}
		}
		return listAllItineraries;
	}
	/**
	 * @param listAllItineraries the listAllItineraries to set
	 */
	public void setListAllItineraries(ArrayList<Itinerary> listItineraries) {
		_Itinerary = true;
		listAllItineraries = listItineraries;
	}
	
	/**
	 * @return the listAllItineraries with Transfer information
	 */
	public ArrayList<Itinerary> getListAllItinerariesWithTransfer(boolean onlyFavorite){
		ArrayList<Itinerary> theList = new ArrayList<Itinerary>();
		if (distance == null || distance != mPreferences.getOptionMeter()) refreshDistance();
		if (!onlyFavorite){
			if (!_ItineraryWithTransfer || !_Itinerary){
				synchronized(this){
					if (!_Itinerary) {
						// return transfer list, first time get data from DB
						DBHelper DataConnect = DBHelper.getInstance();
						listAllItineraries = DataConnect.getAllItinerary(mPreferences.getOptionMeter(), false, false);
						_Itinerary = true;
						
					}
					Iterator<Itinerary> iti = listAllItineraries.iterator();
					while(iti.hasNext()){
						Itinerary it = iti.next();
						it.setListTransfer(getAllTransferFavouriteTransportByIdItinerary(it, false));
						ArrayList<Line> myLineList = it.getLines();
						Iterator<Line> lin = myLineList.iterator();
				    	while(lin.hasNext()){
				    		Line li = lin.next();
				            if (TypeTransport.BICIPALMA.equals(TypeTransport.getTypeTransport(it.getTypeOfTransport()))){
					    		li.setListTransfer(getAllTransferFavouriteTransportByIdItinerary(it, false));
				            } else {	
				            	li.setListTransfer(getAllTransferFavouriteTransportByIdLine(li, false));
				            }
				    	}
					}
					DBHelper DataConnect2 = DBHelper.getInstance();
					DataConnect2.close();
					_ItineraryWithTransfer = true;
				}
			}
			theList = listAllItineraries;
		} else {
			synchronized(this){
				// return transfer list, first time get data from DB
				DBHelper DataConnect = DBHelper.getInstance();
				theList = DataConnect.getAllItinerary(mPreferences.getOptionMeter(), true, false);
					
				Iterator<Itinerary> iti = theList.iterator();
				while(iti.hasNext()){
					Itinerary it = iti.next();
					it.setListTransfer(getAllTransferFavouriteTransportByIdItinerary(it, false));
					ArrayList<Line> myLineList = it.getLines();
					Iterator<Line> lin = myLineList.iterator();
			    	while(lin.hasNext()){
			    		Line li = lin.next();
			            if (TypeTransport.BICIPALMA.equals(TypeTransport.getTypeTransport(it.getTypeOfTransport()))){
				    		li.setListTransfer(getAllTransferFavouriteTransportByIdItinerary(it, false));
			            } else {	
			            	li.setListTransfer(getAllTransferFavouriteTransportByIdLine(li, false));
			            }
			    	}
				}
				DBHelper DataConnect2 = DBHelper.getInstance();
				DataConnect2.close();
			}
		}
		return theList;	
	}

	public synchronized boolean refreshListAllItineraries(){
		listAllItineraries = null;
		_Itinerary = false;
		_ItineraryWithTransfer = false;
		return true;
	}
	
	
	/**
	 * @return the listAllStations
	 */
	public ArrayList<Station> getListAllStations(boolean onlyFavorite) {
		ArrayList<Station> theList = null;
		if (!onlyFavorite){
			synchronized(this){
				if (!_Station) {
					if (distance == null || distance != mPreferences.getOptionMeter()) refreshDistance();
					// return transfer list, first time get data from DB
					DBHelper DataConnect = DBHelper.getInstance();
					listAllStations = DataConnect.getAllStations(distance, false, true);
					_Station = true;
				}
				theList = listAllStations;
			}
		} else {
			synchronized(this){
				if (distance == null || distance != mPreferences.getOptionMeter()) refreshDistance();
				// return transfer list, first time get data from DB
				DBHelper DataConnect = DBHelper.getInstance();
				theList = DataConnect.getAllStations(distance, true, true);
			}
		}
		return theList;
	}
	/**
	 * @param listAllStations the listAllStations to set
	 */
	public void setListAllStations(ArrayList<Station> listStations) {
		_Station = true;
		listAllStations = listStations;
	}
	
	public synchronized boolean refreshListAllStations(){
		listAllStations = null;
		_Station = false;
		_StationWithTransfer = false;
		return true;
	}
	
	/**
	 * @return the listAllStations with Transfer information
	 */
	public ArrayList<Station> getListAllStationsWithTransfer(boolean onlyFavorite ){
		ArrayList<Station> theList = null;
		if (distance == null || distance != mPreferences.getOptionMeter()) refreshDistance();
		if (!onlyFavorite){
			if (!_StationWithTransfer || !_Station){
				synchronized(this){
					if (!_Station) {
						// return transfer list, first time get data from DB
						DBHelper DataConnect = DBHelper.getInstance();
						listAllStations = DataConnect.getAllStations(mPreferences.getOptionMeter(), false, false);
						_Station = true;
						
					}
					if (!_StationWithTransfer){
						Iterator<Station> sta = listAllStations.iterator();
						while(sta.hasNext()){
							Station st = sta.next();
							//st.setListTransfer(getAllTransferByIdStation(st, false));
							st.setListTransfer(getAllTransferFavouriteTransportByIdStation(st));
						} 
						DBHelper DataConnect2 = DBHelper.getInstance();
						DataConnect2.close();
						_StationWithTransfer = true;
					}
					theList = listAllStations;
				}
			}
		} else {
			synchronized(this){
				// return transfer list, first time get data from DB
				DBHelper DataConnect = DBHelper.getInstance();
				theList = DataConnect.getAllStations(mPreferences.getOptionMeter(), true, false);
					
				Iterator<Station> sta = theList.iterator();
				while(sta.hasNext()){
					Station st = sta.next();
					//st.setListTransfer(getAllTransferByIdStation(st, false));
					st.setListTransfer(getAllTransferFavouriteTransportByIdStation(st));
				} 
				DBHelper DataConnect2 = DBHelper.getInstance();
				DataConnect2.close();
			}			
		}
		return theList;	
	}


	/**
	 * @return the listAllTransfers
	 */
	public ArrayList<Transfer> getListAllTransfers() {
		synchronized(this){
			if (!_Transfer) {
				// return transfer list, first time get data from DB
				DBHelper DataConnect = DBHelper.getInstance();
				listAllTransfers = DataConnect.getAllTransfers(true);
				_Station = true;
			}
		}
		return listAllTransfers;
	}
	
	/**
	 * @param listAllTransfers the listAllTransfers to set
	 */
	public void setListAllTransfers(ArrayList<Transfer> listTransfers) {
		_Transfer = true;
		listAllTransfers = listTransfers;
	}
	
	public synchronized boolean refreshListAllTransfers(){
		listAllTransfers = null;
		_Transfer = false;
		return true;
	}

	/**
	 * @return the listAllMessages
	 */
	public ArrayList<Message> getListAllMessages() {
		synchronized(this){
			if (!_Message) {
				// return transfer list, first time get data from DB
				DBHelper DataConnect = DBHelper.getInstance();
				listAllMessages = DataConnect.getAllMessagesByLanguage(true, mPreferences.getOptionLanguage());
				_Message = true;
			}
		}
		return listAllMessages;
	}

	/**
	 * @param listAllMessages the listAllMessages to set
	 */
	public void setListAllMessages(ArrayList<Message> listMessages) {
		_Message = true;
		listAllMessages = listMessages;
	}
	
	public synchronized boolean refreshListAllMessages(){
		listAllMessages = null;
		_Message = false;
		return true;
	}
	
	//Getting information from the System Data
	public int getDistanceToStation(GeoPosition mGeoPosition, Station mStation){
	    return (int)Haversine.distHaversine(mGeoPosition.getLatitude(), mGeoPosition.getLongitude(), 
	    		mStation.getGeoPosition().getLatitude(), mStation.getGeoPosition().getLongitude());
	}

	public boolean isTransfer(Station mStationA, Station mStationB){
	    if (mStationA.isTransfer() && mStationB.isTransfer()){
	    	//Both Stations have Transfer
			DBHelper DataConnect = DBHelper.getInstance();
			boolean res = DataConnect.getTranferBetweenA_B(mStationA.get_id(), mStationB.get_id(), true);
	    	return res;
	    }
	    return false;
	}
	
	public boolean isBikeFavorite(){
		DBHelper DataConnect = DBHelper.getInstance();
		boolean res = DataConnect.IsTransportFavorite(TypeTransport.BICIPALMA.getValue(), true);
		return res;
	}
	
	public ArrayList<Station> getNextStations(GeoPosition mGeoPosition){
		//Return List of stations ordered by distance to user
	    ArrayList<Station> mArrayList = new ArrayList<Station>();
	    Iterator<Station> mIterator = listAllStations.iterator();
	    Location mLocation = mGeoPosition.getLocation();
	    while (mIterator.hasNext()){
	    	Station mStation = (Station) mIterator.next();
	    	mStation.setDistance(mLocation);
	    }
	    Collections.sort(mArrayList, Station.DistanceComparator);
	    return mArrayList;
	}
	
	public boolean IsCircleLine(int idLine){
		// return TRUE if the Line has the same route One Way and Return
		DBHelper DataConnect = DBHelper.getInstance();
		boolean res;

		res = DataConnect.getIsCircleLine(idLine, true);

		return res;
	}

	public boolean IsBikeLine(int idLine){
		// return if Line is BikeLine
		DBHelper DataConnect = DBHelper.getInstance();
		boolean res;

		res = DataConnect.getIsBikeLine(idLine, true);

		return res;
	}
	
	public static boolean IsBikeStation(Station myStation){
		// return if Station is BikeStation
		return TypeTransport.getTypeTransport(
				myStation.getFK_id_Transport()).equals(
						TypeTransport.BICIPALMA);
	}
	
	public static boolean IsBusStation(Station myStation){
		// return if Station is BikeStation
		return TypeTransport.getTypeTransport(
				myStation.getFK_id_Transport()).equals(
						TypeTransport.BUSPALMA);
	}

	private boolean IsBikeLine(int idLine, boolean _close){
		// return list of stations of the Line One Way
		DBHelper DataConnect = DBHelper.getInstance();
		boolean res;

		res = DataConnect.getIsBikeLine(idLine, _close);

		return res;
	}
	
	public boolean IsBusLine(int idLine){
		// return if Line is BusLine
		DBHelper DataConnect = DBHelper.getInstance();
		boolean res;

		res = DataConnect.getIsBusLine(idLine, true);

		return res;
	}
	

	// To control our model we need to know the line 
	// get all the Station OW one way, if circle line RT = OW
	protected ArrayList<Station> getAllStationOW(int idLine){
		// return list of stations of the Line One Way
		DBHelper DataConnect = DBHelper.getInstance();
		ArrayList<Station> res = null;
		if (distance == null || distance != mPreferences.getOptionMeter())refreshDistance();
		if (IsBikeLine(idLine, false)){
			if (listAllBikeStations == null){ 
				Itinerary myItinerary = DataConnect.getItineraryOfLine(idLine, true);
				res = DataConnect.getAllBikeStations(myItinerary.get_id(), distance, true);
				
			} else {
				res = listAllBikeStations;
			}
		} else {
			res = DataConnect.getAllStationOW(idLine, distance, true);
		}
		return res;
	}
	
	// get all the Stations RT return
	protected ArrayList<Station> getAllStationRT(int idLine){
		// return list of Station of the Line Return
		DBHelper DataConnect = DBHelper.getInstance();
		ArrayList<Station> res = null;
		if (distance == null || distance != mPreferences.getOptionMeter()) refreshDistance();

		res = DataConnect.getAllStationRT(idLine, distance, true);
		
		return res;
		
	}
	
	public ArrayList<Station> getAllStationByLine(Line myLine, boolean oneWay){
		ArrayList<Station> res = new ArrayList<Station>();
		synchronized(this){
			if ((oneWay || IsCircleLine(myLine.get_id()) || IsBikeLine(myLine.get_id()))){
				// get stations list one way first time we access to the line
				if (myLine.getStationsOW() == null){
					myLine.setStationsOW(getAllStationOW(myLine.get_id()));
				}
				res = myLine.getStationsOW();
			}else {
				if (!oneWay && myLine.getStationsRT() == null){
					myLine.setStationsRT(getAllStationRT(myLine.get_id()));
				}
				res = myLine.getStationsRT();
			}
		}
		return res;
	}

	// To control our model we need to know if the line 
	public ArrayList<Station> getAllBikeStations(Itinerary myItinerary){
		// return list of stations of the Virtual Line BICI PALMA
		synchronized(this){
			if (!_BikeStation) {
				if (distance == null || distance != mPreferences.getOptionMeter()) refreshDistance();
				// return transfer list, first time get data from DB
				DBHelper DataConnect = DBHelper.getInstance();
				listAllBikeStations = DataConnect.getAllBikeStations(myItinerary.get_id(), distance, true);
				_BikeStation = true;
			}
		}
		return listAllBikeStations;
	}
	
	public ArrayList<TimeTable> getAllTimeTableByLine(int idLine){
		// devuelve la lista de estaciones en sentido de ida de idLine
		DBHelper DataConnect = DBHelper.getInstance();
		ArrayList<TimeTable> res = null;
		
		res = DataConnect.getAllTimeTable(idLine, true);
		
		return res;
		
	}

	public ArrayList<Frequency> getAllFrequencyByLine(int idLine){
		// devuelve la lista de estaciones en sentido de ida de idLine
		DBHelper DataConnect = DBHelper.getInstance();
		ArrayList<Frequency> res = null;
		
		res = DataConnect.getAllFrequency(idLine, true);
		
		return res;
	}
	
	// Transport FK_id_TypeOfTransport
	protected ArrayList<ItineraryTypeTransport> getAllTransferFavouriteTransportByIdItinerary(Itinerary myItinerary, boolean _close){
		// devuelve la lista de itinerarios transbordables a transportes favoritos
		DBHelper DataConnect = DBHelper.getInstance();
		
		ArrayList<ItineraryTypeTransport> res = null;
		if (myItinerary.getListTransfer() == null){
			res = DataConnect.getAllTransferFavouriteTransportByIdItinerary(myItinerary, mPreferences.getOptionMeter(), _close);
		} else {
			res = myItinerary.getListTransfer();
		}
		return res;
	}

	// Transport FK_id_TypeOfTransport
	protected ArrayList<ItineraryTypeTransport> getAllTransferFavouriteTransportByIdLine(Line myLine, boolean _close){
		// devuelve la lista de itinerarios transbordables por línea a transportes favoritos
		DBHelper DataConnect = DBHelper.getInstance();
		ArrayList<ItineraryTypeTransport> res = null;
		if (myLine.getListTransfer() == null){
			res = DataConnect.getAllTransferFavouriteTransportByIdLine(myLine, mPreferences.getOptionMeter(), _close);
		} else {
			res = myLine.getListTransfer();
		}
		
		return res;
	}
	
	// Transport FK_id_TypeOfTransport
	protected ArrayList<ItineraryTypeTransport> getAllTransferFavouriteTransportByIdLineStation(Station myStation, Line myLine){
		// devuelve la lista de itinerarios transbordables por estación a transportes favoritos
		DBHelper DataConnect = DBHelper.getInstance();
		ArrayList<ItineraryTypeTransport> res = null;
		if (myLine.getListTransfer() == null){
			if (IsBikeLine(myLine.get_id())){
				res = DataConnect.getAllTransferFavouriteTransportByIdStationItinerary(myStation, myLine, mPreferences.getOptionMeter(), true);				
			} else {
				res = DataConnect.getAllTransferFavouriteTransportByIdStation(myStation, myLine, mPreferences.getOptionMeter(), true);
			}
		} else {
			res = myLine.getListTransfer();
		}
		
		return res;
	}

	// Transport FK_id_TypeOfTransport
	protected ArrayList<ItineraryTypeTransport> getAllTransferFavouriteTransportByIdStation(Station myStation){
		// devuelve la lista de itinerarios transbordables por estación a transportes favoritos
		DBHelper DataConnect = DBHelper.getInstance();
		ArrayList<ItineraryTypeTransport> res = null;
		if (myStation.getListTransfer() == null){
			res = DataConnect.getAllTransferFavouriteTransportByIdStation(myStation, mPreferences.getOptionMeter(), true);
		} else {
			res = myStation.getListTransfer();
		}
		
		return res;
	}

	// Transport FK_id_TypeOfTransport
	protected ArrayList<ItineraryTypeTransport> getAllTransferByIdStation(Station myStation, boolean _close){
		// devuelve la lista de itinerarios transbordables por estación a transportes favoritos
		DBHelper DataConnect = DBHelper.getInstance();
		ArrayList<ItineraryTypeTransport> res = null;
		if (myStation.getListTransfer() == null){
			res = DataConnect.getAllTransferByIdStation(myStation, mPreferences.getOptionMeter(), _close);
		} else {
			res = myStation.getListTransfer();
		}
		
		return res;
	}
	
	/**
	 * @return if operation ends successfully
	 */
	public synchronized boolean IsFavoriteItinerary(Itinerary myItinerary, boolean isFavorite){
		DBHelper DataConnect = DBHelper.getInstance();
		myItinerary.setFavourite(isFavorite);
		return DataConnect.setIsFavoriteItinerary(myItinerary,isFavorite, true);
	}
	
	/**
	 * @return if operation ends successfully
	 */
	public synchronized boolean setIsFavoriteItinerary(Itinerary myItinerary, boolean isFavorite){
		DBHelper DataConnect = DBHelper.getInstance();
		myItinerary.setFavourite(isFavorite);
		return DataConnect.setIsFavoriteItinerary(myItinerary,isFavorite, true);
	}

	/**
	 * @return if operation ends successfully
	 */
	public boolean HasDisabledFacilities(Line myLine){
		DBHelper DataConnect = DBHelper.getInstance();
		return DataConnect.HasDisabledFacilities(myLine.getFK_id_Itinerary(), true);
	}
	
	/**
	 * @return if operation ends successfully
	 */
	public synchronized boolean setIsFavoriteLine(Line myLine, boolean isFavorite){
		DBHelper DataConnect = DBHelper.getInstance();
		myLine.setFavorite(isFavorite);
		return DataConnect.setIsFavoriteLine(myLine,isFavorite, true);
	}
	
	/**
	 * @return if operation ends successfully
	 */
	public synchronized boolean setIsFavoriteTransport(Transport myTransport, boolean isFavorite){
		DBHelper DataConnect = DBHelper.getInstance();
		myTransport.setIsFavouriteTransport(isFavorite);
		return DataConnect.setIsFavoriteTransport(myTransport, isFavorite, true);
	}

	/**
	 * @return if operation ends successfully
	 */
	public synchronized boolean setIsFavoriteStation(Station myStation, boolean isFavorite){
		DBHelper DataConnect = DBHelper.getInstance();
		myStation.setFavorite(isFavorite);
		return DataConnect.setIsFavoriteStation(myStation, isFavorite, true);
	}
	
	/**
	 * @return the last TimeStamp BiciPalma
	 */
	public synchronized Long getTimeStamp(TypeTransport mType, Station myStation) {
		Long timeStamp = null;
		if (myStation != null){
			if (myStation.getTimeStamp() != null){
				timeStamp = myStation.getTimeStamp().getTimeInMillis();
			}
		}
		if (timeStamp == null){
			// return transfer list, first time get data from DB
			DBHelper DataConnect = DBHelper.getInstance();
			timeStamp = DataConnect.getTimeStamp(mType, myStation, true);
		}
		return timeStamp;
	}	
	
	/**
	 * @return Itinerary of Line
	 */
	public Itinerary getItineraryOfLine(Line myLine){
		DBHelper DataConnect = DBHelper.getInstance();
		return DataConnect.getItineraryOfLine(myLine.get_id(), true);
	}
	

	// Not implemented in version 1.0
	/**
	 * @return the listStationsRoute
	 */
	public ArrayList<Station> getListStationsRoute() {
		synchronized(this){
			if (!_RouteStation) {
				// return transfer list, first time get data from DB
				//DBHelper DataConnect = DBHelper.getInstance();
				//listStationsRoute = DataConnect.getAllStationsroute(Station origen, Station destiny, String optionRoute);
				_RouteStation = true;
			}
		}
		return listStationsRoute;
	}
	/**
	 * @param listStationsRoute the listStationsRoute to set
	 */
	public void setListStationsRoute(ArrayList<Station> mListStationsRoute) {
		_RouteStation = true;
		listStationsRoute = mListStationsRoute;
	}
	
	public synchronized boolean refreshListStationsRoute(){
		listStationsRoute = null;
		_RouteStation = false;
		return true;
	}
	

	/**
	 * @return the mPreferences
	 */
	public Preferences getmPreferences() {
		return mPreferences;
	}

	/**
	 * @param mPreferences the mPreferences to set
	 */
	public void setmPreferences(Preferences myPreferences) {
		mPreferences = myPreferences;
	}

	/**
	 * @return the distance
	 */
	public int getDistance() {
		if (distance == null || distance != getmPreferences().getOptionMeter()) refreshDistance();
		return distance;
	}

	/**
	 * @param distance the distance to set
	 */
	public void setDistance(int mDistance) {
		distance = mDistance;
	}

	/**
	 * @return the listAllBikeStations
	 */
	public ArrayList<Station> getListAllBikeStations(Itinerary myItinerary) {
		synchronized(this){
			if (!_BikeStation) {
				if (distance == null || distance != mPreferences.getOptionMeter()) refreshDistance();

				// return list of Bike Station, first time get data from DB
				DBHelper DataConnect = DBHelper.getInstance();
				listAllBikeStations = DataConnect.getAllBikeStations(myItinerary.get_id(), distance, true);
				_TypeOfTransport = true;
			}
		}
		return listAllBikeStations;
	}
	
	/**
	 * @param listStationsRoute the listStationsRoute to set
	 */
	public void setListAllBikeStations(ArrayList<Station> listBikeStations) {
		_BikeStation = true;	
		listAllBikeStations = listBikeStations;
	}

	/**
	 * @param listStationsRoute the listStationsRoute to set
	 */
	public synchronized boolean storeListAllBikeStations() {
		boolean res;
		_BikeStation = false;	
		// sending data to DB
		DBHelper DataConnect = DBHelper.getInstance();
		res = DataConnect.setBikeStations(listAllBikeStations, true);
		
		return res;

	}
	
	/**
	 * @param listStationsRoute the listStationsRoute to set
	 */
	public boolean addDataNetworkToListBikeStations(ArrayList<Station> mlistBikeStationsNetwork, NetworkInformation network) {	
		listAllBikeStations = getAllBikeStations();
		
		
		// modify data in PublicTransport		
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		for (int n = 0; n < listAllBikeStations.size(); n++){
			map.put(listAllBikeStations.get(n).getCode(), n);
		}
		for (int n = 0; n < mlistBikeStationsNetwork.size(); n++){
			int pos = map.get(mlistBikeStationsNetwork.get(n).getCode());
			listAllBikeStations.get(pos).getBikeStation().setBikesAvailable(
					mlistBikeStationsNetwork.get(n).getBikeStation().getBikesAvailable());
			listAllBikeStations.get(pos).getBikeStation().setParksFree(
					mlistBikeStationsNetwork.get(n).getBikeStation().getParksFree());
			if (listAllBikeStations.get(pos).getBikeStation().getCapacity() < 
					mlistBikeStationsNetwork.get(n).getBikeStation().getCapacity()) 
					listAllBikeStations.get(pos).getBikeStation().setCapacity(
							mlistBikeStationsNetwork.get(n).getBikeStation().getCapacity());
			listAllBikeStations.get(pos).setTimeStamp(mlistBikeStationsNetwork.get(n).getTimeStamp());
			if (network.getLastUpdateTimeCityBik() == 0) network.setLastUpdateTimeCityBik(mlistBikeStationsNetwork.get(n).getTimeStamp().getTimeInMillis());
			if (network.getLastUpdateTimeCityBik() > mlistBikeStationsNetwork.get(n).getTimeStamp().getTimeInMillis()) network.setLastUpdateTimeCityBik(mlistBikeStationsNetwork.get(n).getTimeStamp().getTimeInMillis());
		}
		return _BikeStation;
		
	}
	
	/**
	 * @param listAllBikeStations the listAllBikeStations to set
	 */
	public synchronized boolean refreshListAllBikeStations() {
		listAllBikeStations = null;
		_BikeStation = false;
		return true;
	}
	
	/**
	 * @return the FirstItineraryBiciPalma
	 */
	public Itinerary getFirstItineraryBiciPalma() {
		Itinerary it = null;
		
		synchronized(this){
			if (distance == null || distance != mPreferences.getOptionMeter()) refreshDistance();

			// return first Itinerary of BiciPalma
			DBHelper DataConnect = DBHelper.getInstance();
			it= DataConnect.getFirstItineraryBiciPalma(distance, true);
		}
		return it;
	}

	// To control our model we need to know if the line 
	public ArrayList<Station> getAllBikeStations(){
		// return list of stations of the Virtual Line BICI PALMA
		synchronized(this){
			if (!_BikeStation) {
				Itinerary it = getFirstItineraryBiciPalma();
				if (distance == null || distance != mPreferences.getOptionMeter()) refreshDistance();
				// return transfer list, first time get data from DB
				DBHelper DataConnect = DBHelper.getInstance();
				listAllBikeStations = DataConnect.getAllBikeStations(it.get_id(), distance, true);
				_BikeStation = true;
			}
		}
		return listAllBikeStations;
	}
		
	public ArrayList<BusStation> getListAllBusStations(Station mStation){
		ArrayList<BusStation> mBusStations = mStation.getBusStations();
		//get data from BusStation if not create new data
		if (mBusStations == null){
			DBHelper DataConnect = DBHelper.getInstance();
			mBusStations = DataConnect.getListAllBusStations(mStation.get_id(), true);
		}
		return mBusStations;
	}
	
	public Itinerary getItinerary(int idItinerary){
		//get data from Itinerary
		Itinerary it;
		DBHelper DataConnect = DBHelper.getInstance();
		it = DataConnect.getItinerary(idItinerary, true);
		return it;
	}

	/**
	 * @param listStationsRoute the listStationsRoute to set
	 * 
	 * 	// ugly solution for line 7 and lin 14
	 *	// line 7 from Son Gotleu first is to Son Xigala and second to Son Vida
	 *	// line 7 from Son Xigala and Son Vida we mix time
	 *	// line 14 from Eusebi Estada is to San Jordi and second to S'Hostalot
	 *	// line 14 from San Jordi and S'Hostalot we mix time
	 *	// we check in PublicTransport.addDataNetworkToListBusStations
	 *
	 */
	
	public boolean addDataNetworkToListBusStations(Station myStation, Station myStationNetwork) {
		HashMap<String, Integer> newValues = new HashMap<String, Integer>();
		HashMap<String, Integer> normLine = new HashMap<String, Integer>();
		normLine.put("7SON GOTLEU", 1);
		normLine.put("14PALMA", 2);
		normLine.put("14SON FERRIOL", 3);
		normLine.put("23PALMA", 4);
		
		ArrayList<BusStation> listAllBusStation = new ArrayList<BusStation>();
		// if we have previous data
		if (myStation.getBusStations() == null){
			listAllBusStation = getListAllBusStations(myStation);
			// we don't have info from timetable...DELETING DATA :(
			Iterator<BusStation> itbs = listAllBusStation.iterator();
			while(itbs.hasNext())
			{
			    BusStation bs = itbs.next();
			    //Do something with BusStation
			    bs.setFirstBus(null);
			    bs.setFirsBusMeters(-1);
			    bs.setSecondBus(null);
			    bs.setSecondBusMeters(-1);
			}
			myStation.setBusStations(listAllBusStation);
 
		}
		// modify data in PublicTransport, our hash map with 2 keys Code, Destination
		HashMap<String, HashMap<String, Integer>> mapOut = new HashMap<String, HashMap<String, Integer>>();
		HashMap<String, Integer> mapIn = new HashMap<String, Integer>();
		
		for (int n = 0; n < myStation.getBusStations().size(); n++){
			mapOut.put(myStation.getBusStations().get(n).getItinerary().getCode().toUpperCase(), mapIn);
			mapIn.put(myStation.getBusStations().get(n).getDestination().toUpperCase(), n);
		}
		for (int n = 0; n < myStationNetwork.getBusStations().size(); n++){
			// ugly solution to distinguish line from EMT Service
			int enNomrLine = -1;
			@SuppressWarnings("unused")
			int enNewValues = -1;
			try {
				// if code+destination is in value normalize
				enNomrLine = normLine.get(myStationNetwork.getBusStations().get(n).getItinerary().getCode().toUpperCase() +
						myStationNetwork.getBusStations().get(n).getDestination().toUpperCase());
				if (enNomrLine > 0){
					// if code+destination is in new values
					enNewValues = newValues.get(myStationNetwork.getBusStations().get(n).getItinerary().getCode().toUpperCase() +
						myStationNetwork.getBusStations().get(n).getDestination().toUpperCase());
					// change destination to code+destination+2
					myStationNetwork.getBusStations().get(n).setDestination(
							myStationNetwork.getBusStations().get(n).getItinerary().getCode().toUpperCase() +
							myStationNetwork.getBusStations().get(n).getDestination().toUpperCase() + "2");
				}
			} catch (Exception e){
				if (Config.DEBUG) Log.e(Config.LOGTAG, "PublicTransport.addDataNetworkToListBusStations(Station.code =" + myStation.getCode()
						+ " Itinerary.code =" + myStationNetwork.getBusStations().get(n).getItinerary().getCode()+ " Destination =" + myStationNetwork.getBusStations().get(n).getDestination());
				if (enNomrLine > 0){
					// if code+destination is in value normalize and not in new values, we add code+destination and change destination
					newValues.put(myStationNetwork.getBusStations().get(n).getItinerary().getCode().toUpperCase() +
						myStationNetwork.getBusStations().get(n).getDestination().toUpperCase(), n);
					myStationNetwork.getBusStations().get(n).setDestination(
							myStationNetwork.getBusStations().get(n).getItinerary().getCode().toUpperCase() +
							myStationNetwork.getBusStations().get(n).getDestination().toUpperCase());
				} else {
					enNomrLine = -1;
				}
			}
			// now destination is normalized :(
			HashMap<String, Integer> mMap = mapOut.get(myStationNetwork.getBusStations().get(n).getItinerary().getCode().toUpperCase());
			int pos = -1;
			try{
				pos = mMap.get(myStationNetwork.getBusStations().get(n).getDestination().toUpperCase());					
				// we got new data from network
				if (myStationNetwork.getBusStations().get(n).getFirstBus() != null){
					myStation.getBusStations().get(pos).setFirstBus(myStationNetwork.getBusStations().get(n).getFirstBus());
					myStation.getBusStations().get(pos).setFirsBusMeters(myStationNetwork.getBusStations().get(n).getFirsBusMeters());
				} else {
					myStation.getBusStations().get(pos).setFirstBus(null);
					myStation.getBusStations().get(pos).setFirsBusMeters(Config.INVALID_ROW_ID);
				}
				if (myStationNetwork.getBusStations().get(n).getSecondBus() != null){			
					myStation.getBusStations().get(pos).setSecondBus(myStationNetwork.getBusStations().get(n).getSecondBus());
					myStation.getBusStations().get(pos).setSecondBusMeters(myStationNetwork.getBusStations().get(n).getSecondBusMeters());
				} else {
					myStation.getBusStations().get(pos).setSecondBus(null);
					myStation.getBusStations().get(pos).setSecondBusMeters(Config.INVALID_ROW_ID);
				}
			} catch (Exception e){
				if (Config.DEBUG) Log.e(Config.LOGTAG, "PublicTransport.addDataNetworkToListBusStations(Station.code =" + myStation.getCode()
						+ " Itinerary.code =" + myStationNetwork.getBusStations().get(n).getItinerary().getCode()+ " Destination =" + myStationNetwork.getBusStations().get(n).getDestination());
			}
		}
		if (myStationNetwork.getTimeStamp() != null){
			myStation.setTimeStamp(myStationNetwork.getTimeStamp());
		}
		// now we can save to DB
		return true;
	}
	
	/**
	 * @param listStationsRoute the listStationsRoute to set
	 */
	public boolean storeListAllBusStations(Station myStation) {
		boolean res;
		// sending data to DB
		DBHelper DataConnect = DBHelper.getInstance();
		res = DataConnect.setBusStations(myStation, true);
		
		return res;

	}
	
	/**
	 * @return the listAllPlaces
	 */
	public ArrayList<Place> getListAllPlaces(boolean onlyFavorite) {
		ArrayList<Place> theList = null;
		synchronized(this){
			// return transfer list, first time get data from DB
			DBHelper DataConnect = DBHelper.getInstance();
			theList = DataConnect.getAllPlaces(onlyFavorite, true);
		}
		return theList;
	}

	/**
	 * @return the listAllCabs
	 */
	public ArrayList<Cab> getListAllCabs(boolean onlyFavorite) {
		ArrayList<Cab> theList = null;
		synchronized(this){
			// return transfer list, first time get data from DB
			DBHelper DataConnect = DBHelper.getInstance();
			theList = DataConnect.getAllCabs(onlyFavorite, true);
		}
		return theList;
	}

	/**
	 * @return the listAllsOperator
	 */
	public ArrayList<CabOperator> getListAllCabOperators(boolean onlyFavorite) {
		ArrayList<CabOperator> theList = null;
		synchronized(this){
			// return transfer list, first time get data from DB
			DBHelper DataConnect = DBHelper.getInstance();
			theList = DataConnect.getAllCabOperators(onlyFavorite, true);
		}
		return theList;
	}
	
	/**
	 * @return if operation ends successfully
	 */
	public synchronized boolean setIsFavoritePlace(Place myPlace, boolean isFavorite){
		DBHelper DataConnect = DBHelper.getInstance();
		myPlace.setFavourite(isFavorite);
		return DataConnect.setIsFavoritePlace(myPlace,isFavorite, true);
	}

	/**
	 * @return if operation ends successfully
	 */
	public synchronized boolean setIsFavoriteCab(Cab myCab, boolean isFavorite){
		DBHelper DataConnect = DBHelper.getInstance();
		myCab.setFavorite(isFavorite);
		return DataConnect.setIsFavoriteCab(myCab,isFavorite, true);
	}
	
	/**
	 * @return if operation ends successfully
	 */
	public synchronized boolean setIsFavoriteCabOperator(CabOperator myCabOperator, boolean isFavorite){
		DBHelper DataConnect = DBHelper.getInstance();
		myCabOperator.setFavorite(isFavorite);
		return DataConnect.setIsFavoriteCabOperator(myCabOperator,isFavorite, true);
	}


}
 				 
	

