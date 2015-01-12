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

package com.sanbo.image;

import com.sanbo.datamodel.Itinerary;
import com.sanbo.datamodel.ItineraryTypeTransport;
import com.sanbo.enumerated.TypeTransport;

import android.content.Context;

/**
 * @author admin
 *
 */
public class Image {
	// Instance from Image
    // SINGLETON DEFINITION
	private static Image INSTANCE = null;
	private static Context context;
	
	// Creates instance of public transport
	private synchronized static void createInstance()
	{
		if (INSTANCE == null)
			INSTANCE = new Image();
		return;
	}
	  	
	/**
	 * @return the iNSTANCE
	 */
	public static Image getInstance() {
		if (INSTANCE == null)
			createInstance();
		return INSTANCE;
	}
	
	public void setContext(Context context){
		Image.context = context;
	}
	
	/**
	 * @return the context
	 */
	public Context getContext() {
		return context;
	}

	public static int getImageItinerary(Context pContext, Itinerary myItinerary) {
		Context myContext;
		int res;
		if (pContext == null)
			myContext = Image.context;
		else
			myContext = pContext;

		// valid for TypeTransport.BUSPALMA
		if (myItinerary.getTypeOfTransport() == TypeTransport.BUSPALMA.getValue())
			res = myContext.getResources().getIdentifier("emt"+ myItinerary.getCode(), "drawable", myContext.getPackageName());
		else if (myItinerary.getTypeOfTransport() == TypeTransport.METROPALMA.getValue())
			res = myContext.getResources().getIdentifier("metro"+ myItinerary.getCode().toLowerCase(), "drawable", myContext.getPackageName());
		else if (myItinerary.getTypeOfTransport() == TypeTransport.BICIPALMA.getValue())
			res = myContext.getResources().getIdentifier("bici", "drawable", myContext.getPackageName());
		else if (myItinerary.getTypeOfTransport() == TypeTransport.TRENMALLORCA.getValue())
			res = myContext.getResources().getIdentifier("tren", "drawable", myContext.getPackageName());
		else if (myItinerary.getTypeOfTransport() == TypeTransport.BUSTIBMALLORCA.getValue())
			res = myContext.getResources().getIdentifier("bus", "drawable", myContext.getPackageName());
		else if (myItinerary.getTypeOfTransport() == TypeTransport.TRENSOLLER.getValue())
			res = myContext.getResources().getIdentifier("tren", "drawable", myContext.getPackageName());
		else if (myItinerary.getTypeOfTransport() == TypeTransport.CARRUATGEPALMAOD.getValue())
			res = myContext.getResources().getIdentifier("carriage", "drawable", myContext.getPackageName());
		else
			res = -1;
		return res;
	}
						
	public static int getImageLine(Context pContext, ItineraryTypeTransport it) {
		Context myContext;
		int res;
		
		if (pContext == null)
			myContext = Image.context;
		else
			myContext = pContext;

		// valid for TypeTransport.BUSPALMA
		if (it.getFK_id_typeTransport() == TypeTransport.BUSPALMA.getValue())
			res = myContext.getResources().getIdentifier("emt"+ it.getCode(), "drawable", myContext.getPackageName());
		else if (it.getFK_id_typeTransport() == TypeTransport.METROPALMA.getValue())
			res = myContext.getResources().getIdentifier("metro"+ it.getCode().toLowerCase(), "drawable", myContext.getPackageName());
		else if (it.getFK_id_typeTransport() == TypeTransport.BICIPALMA.getValue())
			res = myContext.getResources().getIdentifier("bici", "drawable", myContext.getPackageName());
		else if (it.getFK_id_typeTransport() == TypeTransport.TRENMALLORCA.getValue())
			res = myContext.getResources().getIdentifier("tren", "drawable", myContext.getPackageName());
		else if (it.getFK_id_typeTransport() == TypeTransport.BUSTIBMALLORCA.getValue())
			res = myContext.getResources().getIdentifier("bus", "drawable", myContext.getPackageName());
		else if (it.getFK_id_typeTransport() == TypeTransport.TRENSOLLER.getValue())
			res = myContext.getResources().getIdentifier("tren", "drawable", myContext.getPackageName());
		else if (it.getFK_id_typeTransport() == TypeTransport.TAXIPALMAOD.getValue())
			res = myContext.getResources().getIdentifier("taxi", "drawable", myContext.getPackageName());
		else if (it.getFK_id_typeTransport() == TypeTransport.CARRUATGEPALMAOD.getValue())
			res = myContext.getResources().getIdentifier("carriage", "drawable", myContext.getPackageName());
		else {
			res = -1;
		}
		return res;
	}

	public static int getImageLine(Context pContext, Itinerary myItineraryOfLine) {
		Context myContext;
		int res;
		
		if (pContext == null)
			myContext = Image.context;
		else
			myContext = pContext;

		// valid for TypeTransport.BUSPALMA
		if (myItineraryOfLine.getFK_id_Transport() == TypeTransport.BUSPALMA.getValue())
			res = myContext.getResources().getIdentifier("emtpq"+ myItineraryOfLine.getCode(), "drawable", myContext.getPackageName());
		else if (myItineraryOfLine.getFK_id_Transport() == TypeTransport.METROPALMA.getValue())
			res = myContext.getResources().getIdentifier("metropq"+ myItineraryOfLine.getCode().toLowerCase(), "drawable", myContext.getPackageName());
		else if (myItineraryOfLine.getFK_id_Transport() == TypeTransport.BICIPALMA.getValue())
			res = myContext.getResources().getIdentifier("bicipq", "drawable", myContext.getPackageName());
		else
			res = -1;
		return res;
	}

	// only transfer at same station max value = 10...we SHOW the first 10
	public static int getImageStation(Context pContext, Itinerary myItineraryInStation) {
		Context myContext;
		int res;
		
		if (pContext == null)
			myContext = Image.context;
		else
			myContext = pContext;
		
		// valid for TypeTransport.BUSPALMA
		if (myItineraryInStation.getFK_id_Transport() == TypeTransport.BUSPALMA.getValue())
			res = myContext.getResources().getIdentifier("emtpqt"+ myItineraryInStation.getCode(), "drawable", myContext.getPackageName());
		else if (myItineraryInStation.getFK_id_Transport() == TypeTransport.METROPALMA.getValue())
			res = myContext.getResources().getIdentifier("metropqt"+ myItineraryInStation.getCode().toLowerCase(), "drawable", myContext.getPackageName());
		else if (myItineraryInStation.getFK_id_Transport() == TypeTransport.BICIPALMA.getValue())
			res = myContext.getResources().getIdentifier("bicipqt", "drawable", myContext.getPackageName());
		else
			res = -1;
		return res;
	}
	
	public static int getImageLang(Context pContext, String myLang){
		Context myContext;
		int res;
		
		if (pContext == null)
			myContext = Image.context;
		else
			myContext = pContext;
		
		if (myLang.compareToIgnoreCase("ca") == 0)
			res = myContext.getResources().getIdentifier("ca", "drawable", myContext.getPackageName());
		else if (myLang.compareToIgnoreCase("es") == 0)
			res = myContext.getResources().getIdentifier("es", "drawable", myContext.getPackageName());
		else if (myLang.compareToIgnoreCase("en") == 0)
			res = myContext.getResources().getIdentifier("en", "drawable", myContext.getPackageName());
		else
			res = -1;
		return res;
	}
	
	/**
	 * @param myContext
	 * @param myType
	 * @return
	 */
	public static int getImageTransport(Context pContext, TypeTransport myType){
		int myImage;
		Context myContext;
		if (pContext == null)
			myContext = Image.context;
		else
			myContext = pContext;

		switch (myType){
		
			//TypeTransport.BUSPALMA
			case BUSPALMA:
				myImage = myContext.getResources().getIdentifier("bus", "drawable", myContext.getPackageName());
				break;
			//TypeTransport.METROPALMA
			case METROPALMA:
				myImage =  myContext.getResources().getIdentifier("metro", "drawable", myContext.getPackageName());
				break;
			//TypeTransport.BICIPALMA
			case BICIPALMA:
				myImage =  myContext.getResources().getIdentifier("bici_t", "drawable", myContext.getPackageName());
				break;
			//TypeTransport.BUSTIBMALLORCA
			case BUSTIBMALLORCA:
				myImage =  myContext.getResources().getIdentifier("bus", "drawable", myContext.getPackageName());
				break;
			//TypeTransport.TRENMALLORCA
			case TRENMALLORCA:
				myImage =  myContext.getResources().getIdentifier("tren", "drawable", myContext.getPackageName());
				break;
			//TypeTransport.TRENSOLLER
			case TRENSOLLER:
				myImage =  myContext.getResources().getIdentifier("tren", "drawable", myContext.getPackageName());
				break;
			//TypeTransport.TAXIPALMAOD
			case TAXIPALMAOD:
				myImage =  myContext.getResources().getIdentifier("taxi", "drawable", myContext.getPackageName());
				break;
			//TypeTransport.CARRUATGEPALMAOD
			case CARRUATGEPALMAOD:
				myImage =  myContext.getResources().getIdentifier("carriage", "drawable", myContext.getPackageName());
				break;
			//TypeTransport.PALMAPUBLICTRANSPORT
			default:
				myImage =  myContext.getResources().getIdentifier("tab_a_icon", "drawable", myContext.getPackageName());
		}
		return myImage;
	}

	public static int getImageFavorite(Context pContext, String favoriteType){
		int myImage;
		Context myContext;
		if (pContext == null)
			myContext = Image.context;
		else
			myContext = pContext;

		if (favoriteType.compareToIgnoreCase("CAPS") == 0){
			myImage = myContext.getResources().getIdentifier("poitaxi", "drawable", myContext.getPackageName());
		}else if (favoriteType.compareToIgnoreCase("CAPS OPERATOR") == 0){
			myImage = myContext.getResources().getIdentifier("poitaxi", "drawable", myContext.getPackageName());
		}else if (favoriteType.compareToIgnoreCase("LINES") == 0){
			myImage = myContext.getResources().getIdentifier("poibus", "drawable", myContext.getPackageName());
		}else if (favoriteType.compareToIgnoreCase("STATIONS") == 0){
			myImage = myContext.getResources().getIdentifier("nearbystations", "drawable", myContext.getPackageName());
		}else if (favoriteType.compareToIgnoreCase("PLACES") == 0){
			myImage = myContext.getResources().getIdentifier("favorits", "drawable", myContext.getPackageName());
		}else if (favoriteType.compareToIgnoreCase("TRANSPORT TYPE") == 0){
			myImage = myContext.getResources().getIdentifier("type_transport_button", "drawable", myContext.getPackageName());
		}else{
			myImage = myContext.getResources().getIdentifier("moveon", "drawable", myContext.getPackageName());
		}
		return myImage;
	}
		
}
			
