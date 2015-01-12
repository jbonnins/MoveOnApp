/*
 * Copyright 2013 Juan José Bonnín Sansó (jbonnins@uoc.edu)
 *
 * This file is part of MoveOnApp.
 *
 *    MoveOnApp is free software: you can redistribute it AND/or modify
 *    it under the terms of the Affero GNU General Public License version 3
 *    AS published by the Free Software Foundation.
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import com.sanbo.utils.Config;
import com.sanbo.utils.TimeFormater;
import com.sanbo.utils.TimeUtils;
import com.sanbo.coordinates.GeoPosition;
import com.sanbo.datamodel.*;
import com.sanbo.enumerated.TypeLanguage;
import com.sanbo.enumerated.TypeTransport;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.sanbo.moveonapp.R;



/**
 * @author Juanjo Bonnín
 *
 */
public class DataStore extends SQLiteOpenHelper {
	public static final int UNSUCCESFUL_INSERT = -1;
	// Our environment, our app  
	private Context myContext;
	// Route of the databases in Android system
	private static String DB_PATH = "";
	// Database Name
	private static String DB_NAME = "";
	// Database object
	private SQLiteDatabase myDataBase;
	// force rewrite DB every time you open APP
	private static boolean force = Config.FORCELOADINDB;
		
	// Tables AND Columns

	/*
	-- _id	FK_id_Station_Destiny	FK_id_Station_Source	EndTime	StartTime	TimeBetweenStations MetersBetweenStations		AlterMapRoute
	CREATE TABLE "AlterMapRoute" ("_id" INTEGER PRIMARY KEY ASC AUTOINCREMENT NOT NULL UNIQUE, 
	"FK_id_Station_Destiny" INTEGER NOT NULL, "FK_id_Station_Source" INTEGER NOT NULL, 
	"EndTime" NUMERIC, "StartTime" NUMERIC, "TimeBetweenStations" NUMERIC NOT NULL, 
	"MetersBetweenStations" INTEGER NOT NULL);
	*/

	static final String  T_AlterMapRoute = "AlterMapRoute";
	static final String  col_AlterMapRoute_id = T_AlterMapRoute + "." + "_id";	
	static final String  col_AlterMapRoute_FK_id_Station_Destiny = "FK_id_Station_Destiny";	
	static final String  col_AlterMapRoute_FK_id_Station_Source = "FK_id_Station_Source";	
	static final String  col_AlterMapRoute_StartTime = "StartTime";	
	static final String  col_AlterMapRoute_EndTime = "EndTime";	
	static final String  col_AlterMapRoute_TimeBetweenStations = "TimeBetweenStations";	
	static final String  col_AlterMapRoute_MetersBetweenStations = "MetersBetweenStations";	

	/*
	-- _id	FK_id_Station	Capacity BikesAvailable ParksFree		BikeStation
	CREATE TABLE "BikeStation" ("_id" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL UNIQUE, 
	"FK_id_Station" INTEGER NOT NULL, "Capacity" INTEGER, "BikesAvailable" INTEGER, "ParksFree" INTEGER);
	*/

	static final String  T_BikeStation = "BikeStation";		
	static final String  col_BikeStation_id = T_BikeStation + "." + "_id";	
	static final String  col_BikeStation_FK_id_Station = "FK_id_Station";	
	static final String  col_BikeStation_Capacity = "Capacity";	
	static final String  col_BikeStation_BikeAvailable = "BikesAvailable";	
	static final String  col_BikeStation_ParksFree = "ParksFree";	
	
	/*
CREATE TABLE "BusStation" ("_id" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL UNIQUE, 
	"FK_id_Station" INTEGER NOT NULL, "FK_id_Itinerary" INTEGER NOT NULL, "Destination" TEXT NOT NULL, 
	"FirstBus" NUMERIC, "FirstBusMeters" INTEGER, "SecondBus" NUMERIC, "SecondBusMeters" INTEGER)	*/

	static final String  T_BusStation = "BusStation";		
	static final String  col_BusStation_id = T_BusStation + "." + "_id";	
	static final String  col_BusStation_FK_id_Station = "FK_id_Station";	
	static final String  col_BusStation_FK_id_Itinerary = "FK_id_Itinerary";
	static final String  col_BusStation_Destination = "Destination";	
	static final String  col_BusStation_FirstBus = "FirstBus";	
	static final String  col_BusStation_FirstBusMeters = "FirstBusMeters";	
	static final String  col_BusStation_SecondBus = "SecondBus";	
	static final String  col_BusStation_SecondBusMeters = "SecondBusMeters";	
	// only to retry Line from BusStation
	static final String  col_BusStation_FK_id_Line = "FK_id_Line";
	
	/*
	-- _id	FK_id_CabOperator	Facebook	Licence	Name	Telephone	Twitter		Cab
	CREATE TABLE "Cab" ("_id" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL UNIQUE , 
	"FK_id_CabOperator" INTEGER, "Facebook" TEXT, "Licence" TEXT NOT NULL, "Name" TEXT,
	"Telephone" TEXT, "Twitter" TEXT);											
	*/
		
	static final String  T_Cab = "Cab";		
	static final String  col_Cab_id = T_Cab + "." + "_id";	
	static final String  col_Cab_FK_id_CabOperator = "FK_id_CabOperator";	
	static final String  col_Cab_Facebook = "Facebook";	
	static final String  col_Cab_Licence = "Licence";	
	static final String  col_Cab_Name = "Name";	
	static final String  col_Cab_Telephone = "Telephone";	
	static final String  col_Cab_Twitter = "Twitter";	
	
	/*	
	-- _id	Name	Telephone	Web		CabOperator
	CREATE TABLE "CabOperator" ("_id" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL UNIQUE, 
	"Name" TEXT NOT NULL, "Telephone" TEXT, "Web" TEXT);
	*/

	static final String  T_CabOperator = "CabOperator";		
	static final String  col_CabOperator_id = T_CabOperator + "." + "_id";	
	static final String  col_CabOperator_Name = "Name";	
	static final String  col_CabOperator_Telephone = "Telephone";	
	static final String  col_CabOperator_Web = "Web";	

	/*
	-- _id	FK_id_Cab	Name		FavouriteCab
	CREATE TABLE "FavouriteCab" ("_id" INTEGER PRIMARY KEY ASC AUTOINCREMENT NOT NULL UNIQUE, 
	"FK_id_Cab" INTEGER NOT NULL, "Name" TEXT);
	*/

	static final String  T_FavouriteCab = "FavouriteCab";		
	static final String  col_FavouriteCab_id = T_FavouriteCab + "." + "_id";	
	static final String  col_FavouriteCab_FK_id_Cab = "FK_id_Cab";	
	static final String  col_FavouriteCab_Name = "Name";	
	
	/*
	-- _id	FK_id_CabOperator	Name		FavouriteCabOperator
	CREATE TABLE "FavouriteCabOperator" ("_id" INTEGER PRIMARY KEY ASC AUTOINCREMENT NOT NULL UNIQUE, 
	"FK_id_CabOperator" INTEGER NOT NULL, "Name" TEXT);
	*/

	static final String  T_FavouriteCabOperator = "FavouriteCabOperator";		
	static final String  col_FavouriteCabOperator_id = T_FavouriteCabOperator + "." + "_id";	
	static final String  col_FavouriteCabOperator_FK_id_CabOperator = "FK_id_CabOperator";	
	static final String  col_FavouriteCabOperator_Name = "Name";	

	/*
	-- _id	FK_id_Line	Name		FavouriteLine
	CREATE TABLE "FavouriteLine" ("_id" INTEGER PRIMARY KEY ASC AUTOINCREMENT NOT NULL UNIQUE, 
	"FK_id_Line" INTEGER NOT NULL, "Name" TEXT);
	*/

	static final String  T_FavouriteLine = "FavouriteLine";		
	static final String  col_FavouriteLine_id = T_FavouriteLine + "." + "_id";	
	static final String  col_FavouriteLine_FK_id_Line = "FK_id_Line";	
	static final String  col_FavouriteLine_Name = "Name";	

	/*
	-- _id	FK_id_Place	Name		FavouritePlace
	CREATE TABLE "FavouritePlace" ("_id" INTEGER PRIMARY KEY ASC AUTOINCREMENT NOT NULL UNIQUE, 
	"FK_id_Place" INTEGER NOT NULL, "Name" TEXT);
	*/

	static final String  T_FavouritePlace = "FavouritePlace";		
	static final String  col_FavouritePlace_id = T_FavouritePlace + "." + "_id";	
	static final String  col_FavouritePlace_FK_id_Place = "FK_id_Place";	
	static final String  col_FavouritePlace_Name = "Name";	
	
	/*
	-- _id	FK_id_Station	Name		FavouriteStation
	CREATE TABLE "FavouriteStation" ("_id" INTEGER PRIMARY KEY ASC AUTOINCREMENT NOT NULL UNIQUE, 
	"FK_id_Station" INTEGER NOT NULL, "Name" TEXT);
	*/

	static final String  T_FavouriteStation = "FavouriteStation";		
	static final String  col_FavouriteStation_id = T_FavouriteStation + "." + "_id";	
	static final String  col_FavouriteStation_FK_id_Station = "FK_id_Station";	
	static final String  col_FavouriteStation_Name = "Name";	
	
	/*
	-- _id	FK_id_Line	StartTime	EndTime	 Monday Tuesday Wednesday Thrusday Friday	Saturday	Sunday_Holiday		Frequency
	CREATE TABLE "Frequency" ("_id" INTEGER PRIMARY KEY ASC AUTOINCREMENT NOT NULL UNIQUE, 
	"FK_id_Line" INTEGER NOT NULL, "StartTime" NUMERIC NOT NULL, "EndTime" NUMERIC NOT NULL, 
	"Monday" NUMERIC NOT NULL, "Tuesday" NUMERIC NOT NULL,"Wednesday" NUMERIC NOT NULL, 
	"Thursday" NUMERIC NOT NULL,"Friday" NUMERIC NOT NULL, "Saturday" NUMERIC NOT NULL, 
	"Sunday_Holiday" NUMERIC NOT NULL);
	*/

	static final String  T_Frequency = "Frequency";		
	static final String  col_Frequency_id = T_Frequency + "." + "_id";	
	static final String  col_Frequency_FK_id_Line = "FK_id_Line";	
	static final String  col_Frequency_StartTime = "StartTime";	
	static final String  col_Frequency_EndTime = "EndTime";	
	static final String  col_Frequency_Monday = "Monday";	
	static final String  col_Frequency_Tuesday = "Tuesday";	
	static final String  col_Frequency_Wednesday = "Wednesday";	
	static final String  col_Frequency_Thursday = "Thursday";	
	static final String  col_Frequency_Friday = "Friday";	
	static final String  col_Frequency_Saturday = "Saturday";	
	static final String  col_Frequency_Sunday_Holiday = "Sunday_Holiday";	

	/*
	-- _id	Date	FK_id_Transport		Holiday
	CREATE TABLE "Holiday" ("_id" INTEGER PRIMARY KEY ASC AUTOINCREMENT NOT NULL UNIQUE, 
	"FK_id_Transport" INTEGER NOT NULL, "Date" NUMERIC NOT NULL);
	*/

	static final String  T_Holiday = "Holiday";		
	static final String  col_Holiday_id = T_Holiday + "." + "_id";	
	static final String  col_Holiday_Date = "Date";	
	static final String  col_Holiday_FK_id_Transport = "FK_id_Transport";

	
	/*
	-- _id	FK_id_TypeTransport	Code Name	ColorHEX	DisabledFacilities	Itinerary
	CREATE TABLE "Itinerary" ("_id" INTEGER PRIMARY KEY ASC AUTOINCREMENT NOT NULL UNIQUE, 
	"FK_id_Transport" INTEGER NOT NULL, "Code" TEXT NOT NULL ,"Name" TEXT NOT NULL, 
	"ColorHEX" TEXT, "FavoriteItinerary" NUMERIC NOT NULL, "DisabledFacilities" NUMERIC NOT NULL )
	*/

	static final String  T_Itinerary = "Itinerary";		
	static final String  col_Itinerary_id = T_Itinerary + "." + "_id";	
	static final String  col_Itinerary_FK_id_Transport = "FK_id_Transport";	
	static final String  col_Itinerary_Code = "Code";	
	static final String  col_Itinerary_Name = "Name";	
	static final String  col_Itinerary_ColorHEX = "ColorHEX";
	static final String  col_Itinerary_FavoriteItinerary = "FavoriteItinerary";	
	static final String  col_Itinerary_DisabledFacilities = "DisabledFacilities";	
	
	/*
	-- _id	FK_id_TypeTransport	Code Name	ColorHEX	ItineraryByFavoriteTransport VIEW
	CREATE VIEW "ItineraryByFavoriteTransport" AS SELECT Itinerary._id AS _id, Itinerary.FK_id_Transport, 
	Itinerary.Code, Itinerary.Name, Itinerary.ColorHEX from Itinerary INNER JOIN Transport 
	ON Itinerary.FK_id_Transport = Transport._id where Transport.FavouriteTransport = "1"	*/
	
	static final String  V_ItineraryByFavoriteTransport = "ItineraryByFavoriteTransport";
	static final String  col_ItineraryByFavoriteTransport_id = V_ItineraryByFavoriteTransport + "." + "_id";	
	static final String  col_ItineraryByFavoriteTransport_FK_id_TypeTransport = "FK_id_TypeTransport";	
	static final String  col_ItineraryByFavoriteTransport_Code = "Code";	
	static final String  col_ItineraryByFavoriteTransport_Name = "Name";	
	static final String  col_ItineraryByFavoriteTransport_ColorHEX = "ColorHEX";	

	/*
	-- _id	FK_id_Itinerary	UNameOneWay	UNameOneWay2	UNameReturn	UNameReturn2	OneWayEMT	ReturnEMT		Line
	CREATE TABLE "Line" ("_id" INTEGER PRIMARY KEY ASC AUTOINCREMENT NOT NULL UNIQUE, 
	"FK_id_Itinerary" INTEGER NOT NULL, "UNameOneWay" TEXT, "UNameOneWay2" TEXT, "UNameReturn" TEXT, 
	"UNameReturn2" TEXT, "OneWayEMT" TEXT, "ReturnEMT" TEXT);
	*/

	static final String  T_Line = "Line";		
	static final String  col_Line_id = T_Line + "." + "_id";	
	static final String  col_Line_FK_id_Itinerary = "FK_id_Itinerary";	
	static final String  col_Line_UNameOneWay = "UNameOneWay";	
	static final String  col_Line_UNameOneWay2 = "UNameOneWay2";	
	static final String  col_Line_UNameReturn = "UNameReturn";	
	static final String  col_Line_UNameReturn2 = "UNameReturn2";	
	static final String  col_Line_OneWayEMT = "OneWayEMT";	
	static final String  col_Line_ReturnEMT = "ReturnEMT";	

	/*
	-- _id	FK_id_Line	FK_id_AlterMapRoute		LineAlterMap
	CREATE TABLE "LineAlterMap" ("_id" INTEGER PRIMARY KEY ASC AUTOINCREMENT NOT NULL UNIQUE, 
	"FK_id_Line" INTEGER NOT NULL, "FK_id_AlterMapRoute" INTEGER NOT NULL);
	*/

	static final String  T_LineAlterMap = "LineAlterMap";		
	static final String  col_LineAlterMap_id = T_LineAlterMap + "." + "_id";	
	static final String  col_LineAlterMap_FK_id_Line = "FK_id_Line";	
	static final String  col_LineAlterMap_FK_id_AlterMapRoute = "FK_id_AlterMapRoute";	

	/*
	-- _id	FK_id_Line	FK_id_Station	StationNext	StationPrevious	RouteOneWay	RouteReturn	TimeBetweenStations	MetersBetweenStations	OneDestination	TimeAdded	MetersAdded		LineStation
	CREATE TABLE "LineStation" ("_id" INTEGER PRIMARY KEY ASC AUTOINCREMENT NOT NULL UNIQUE, 
	"FK_id_Line" INTEGER NOT NULL, "FK_id_Station" INTEGER NOT NULL, "StationNext" INTEGER, 
	"StationPrevious" INTEGER, "RouteOneWay" NUMERIC NOT NULL, "RouteReturn" NUMERIC NOT NULL, 
	"TimeBetweenStations" NUMERIC NOT NULL, "MetersBetweenStations" INTEGER NOT NULL, 
	"OneDestination" NUMERIC NOT NULL, "TimeAdded" NUMERIC NOT NULL, "MetersAdded" INTEGER NOT NULL);
	*/
	static final String  T_LineStation = "LineStation";		
	static final String  col_LineStation_id = T_LineStation + "." + "_id";	
	static final String  col_LineStation_FK_id_Line = "FK_id_Line";	
	static final String  col_LineStation_FK_id_Station = "FK_id_Station";	
	static final String  col_LineStation_StationNext = "StationNext";	
	static final String  col_LineStation_StationPrevious = "StationPrevious";	
	static final String  col_LineStation_RouteOneWay = "RouteOneWay";	
	static final String  col_LineStation_RouteReturn = "RouteReturn";	
	static final String  col_LineStation_TimeBetweenStations = "TimeBetweenStations";	
	static final String  col_LineStation_MetersBetweenStations = "MetersBetweenStations";	
	static final String  col_LineStation_OneDestination = "OneDestination";		
	static final String  col_LineStation_TimeAdded = "TimeAdded";	
	static final String  col_LineStation_MetersAdded = "MetersAdded";	
	
	/*
	-- _id	FK_id_Cycleway	Latitude Longitude	NextPoint	PreviousPoint		MapCycleway
	CREATE TABLE "MapCycleway" ("_id" INTEGER PRIMARY KEY ASC AUTOINCREMENT NOT NULL UNIQUE, 
	"FK_id_Cycleway" INTEGER NOT NULL, "Latitude" INTEGER NOT NULL, "Longitude" INTEGER NOT NULL, 
	"NextPoint" INTEGER, "PreviousPoint" INTEGER);
	*/

	static final String  T_MapCycleway = "MapCycleway";		
	static final String  col_MapCycleway_id = T_MapCycleway + "." + "_id";	
	static final String  col_MapCycleway_FK_id_Cycleway = "FK_id_Cycleway";	
	static final String  col_MapCycleway_Latitude = "Latitude";	
	static final String  col_MapCycleway_Longitude = "Longitude";	
	static final String  col_MapCycleway_NextPoint = "NextPoint";	
	static final String  col_MapCycleway_PreviousPoint = "PreviousPoint";		
	
	/*
	-- _id	FK_id_LineStation	Latitude Longitude	NextPoint PreviousPoint	Velocity		MapPoint
	CREATE TABLE "MapPoint" ("_id" INTEGER PRIMARY KEY ASC AUTOINCREMENT NOT NULL UNIQUE, 
	"FK_id_LineStation" INTEGER NOT NULL, "Latitude" INTEGER NOT NULL, "Longitude" INTEGER NOT NULL, 
	"NextPoint" INTEGER, "PreviousPoint" INTEGER, "Velocity" INTEGER NOT NULL);
	*/

	static final String  T_MapPoint = "MapPoint";		
	static final String  col_MapPoint_id = T_MapPoint + "." + "_id";	
	static final String  col_MapPoint_FK_id_LineStation = "FK_id_LineStation";	
	static final String  col_MapPoint_Latitude = "Latitude";	
	static final String  col_MapPoint_Longitude = "Longitude";	
	static final String  col_MapPoint_NextPoint = "NextPoint";	
	static final String  col_MapPoint_PreviousPoint = "PreviousPoint";	
	static final String  col_MapPoint_Velocity = "Velocity";	

	/*
	-- _id	FK_id_Transport	DateEnd	DateStart	Message	Description	Language		Message
	CREATE TABLE "Message" ("_id" INTEGER PRIMARY KEY ASC AUTOINCREMENT NOT NULL UNIQUE, 
	"FK_id_TypeTransport" INTEGER NOT NULL, "DateEnd" NUMERIC, "DateStart" NUMERIC,"Message" TEXT, 
	"Description" TEXT, "Language" TEXT NOT NULL);
	*/

	static final String  T_Message = "Message";		
	static final String  col_Message_id = T_Message + "." + "_id";	
	static final String  col_Message_FK_id_Transport = "FK_id_Transport";	
	static final String  col_Message_DateEnd = "DateEnd";	
	static final String  col_Message_DateStart = "DateStart";	
	static final String  col_Message_Message = "Message";	
	static final String  col_Message_Description = "Description";	
	static final String  col_Message_Language = "Language";	
	
	/*
	-- _id	FK_id_AlterMapRoute	Latitude	Longitude	IsStation	NextPoint PreviousPoint	Velocity		NewPoint
	CREATE TABLE "NewPoint" ("_id" INTEGER PRIMARY KEY ASC AUTOINCREMENT NOT NULL UNIQUE, 
	"FK_id_AlterMapRoute" INTEGER NOT NULL, "Latitude" INTEGER NOT NULL, "Longitude" INTEGER NOT NULL, 
	"IsStation" NUMERIC NOT NULL, "NextPoint" INTEGER, "PreviousPoint" INTEGER, 
	"Velocity" INTEGER NOT NULL);
	*/

	static final String  T_NewPoint = "NewPoint";		
	static final String  col_NewPoint_id = T_NewPoint + "." + "_id";	
	static final String  col_NewPoint_FK_id_AlterMapRoute = "FK_id_AlterMapRoute";	
	static final String  col_NewPoint_Latitude = "Latitude";	
	static final String  col_NewPoint_Longitude = "Longitude";	
	static final String  col_NewPoint_IsStation = "IsStation";	
	static final String  col_NewPoint_NextPoint = "NextPoint";	
	static final String  col_NewPoint_PreviousPoint = "PreviousPoint";	
	static final String  col_NewPoint_Velocity = "Velocity";	

	/*
	-- _id	Name	Address	Latitude	Longitude		Place
	CREATE TABLE "Place" ("_id" INTEGER PRIMARY KEY ASC AUTOINCREMENT NOT NULL UNIQUE, 
	"Name" TEXT NOT NULL, "Address" TEXT, "Latitude" INTEGER NOT NULL, "Longitude" INTEGER NOT NULL);
	*/

	static final String  T_Place = "Place";		
	static final String  col_Place_id = T_Place + "." + "_id";	
	static final String  col_Place_Name = "Name";	
	static final String  col_Place_Address = "Address";	
	static final String  col_Place_Latitude = "Latitude";	
	static final String  col_Place_Longitude = "Longitude";	
	
	/*
	-- _id	FK_id_Line	ShuttleBus
	CREATE TABLE "ShuttleBus" ("_id" INTEGER PRIMARY KEY ASC AUTOINCREMENT NOT NULL UNIQUE, 
	"FK_id_Line" INTEGER NOT NULL);
	*/

	static final String  T_ShuttleBus = "ShuttleBus";		
	static final String  col_ShuttleBus_id = T_ShuttleBus + "." + "_id";	
	static final String  col_ShuttleBus_FK_id_Line = "FK_id_Line";	
	
	/*
	-- _id	FK_id_TypeTransport	Code	Name	Adapted_for_Disabled	Latitude	Longitude	TimeStamp	Transfer	Velocity	FK_id_TypeZone	Station					
	CREATE TABLE "Station" ("_id" INTEGER PRIMARY KEY ASC AUTOINCREMENT NOT NULL UNIQUE, 
	"FK_id_TypeTransport" INTEGER NOT NULL, "Code" TEXT NOT NULL, "Name" TEXT, 
	"Adapted_for_Disabled" NUMERIC NOT NULL, "Latitude" INTEGER NOT NULL, 
	"Longitude" INTEGER NOT NULL, "TimeStamp" TEXT, "Transfer" NUMERIC NOT NULL, 
	"Velocity" INTEGER, "FK_id_TypeZone" INTEGER);
	*/

	static final String  T_Station = "Station";		
	static final String  col_Station_id = T_Station + "." + "_id";	
	static final String  col_Station_FK_id_Transport = "FK_id_Transport";	
	static final String  col_Station_Code = "Code";	
	static final String  col_Station_Name = "Name";	
	static final String  col_Station_Adapted_for_Disabled = "Adapted_for_Disabled";	
	static final String  col_Station_Latitude = "Latitude";	
	static final String  col_Station_Longitude = "Longitude";	
	static final String  col_Station_TimeStamp = "TimeStamp";	
	static final String  col_Station_Transfer = "Transfer";	
	static final String  col_Station_Velocity = "Velocity";	
	static final String  col_Station_FK_id_TypeZone = "FK_id_TypeZone";	
	
	/*	
	-- _id	FK_id_Station	BusStation	InformationDesk	Metro	Parking	Taxi	Train		StationInfo
	CREATE TABLE "StationInfo" ("_id" INTEGER PRIMARY KEY ASC AUTOINCREMENT NOT NULL UNIQUE, 
	"FK_id_Station" INTEGER NOT NULL, "BusStation" NUMERIC NOT NULL, 
	"InformationDesk" NUMERIC NOT NULL, "Metro" NUMERIC NOT NULL, "Parking" NUMERIC NOT NULL, 
	"Taxi" NUMERIC NOT NULL, "Train" NUMERIC NOT NULL);
	*/

	static final String  T_StationInfo = "StationInfo";		
	static final String  col_StationInfo_id = T_StationInfo + "." + "_id";	
	static final String  col_StationInfo_FK_id_Station = "FK_id_Station";	
	static final String  col_StationInfo_BusStation = "BusStation";	
	static final String  col_StationInfo_InformationDesk = "InformationDesk";	
	static final String  col_StationInfo_Metro = "Metro";	
	static final String  col_StationInfo_Parking = "Parking";	
	static final String  col_StationInfo_Taxi = "Taxi";	
	static final String  col_StationInfo_Train = "Train";	

	/*
	-- _id	FK_id_Line	FK_id_Station_Start	FK_id_Station_End	Monday	Tuesday	Wednesday	Thursday	Friday	Saturday	Sunday_Holiday	Time	LastAssignedLine	DataInici	DataFi		Timetable
	CREATE TABLE "Timetable" ("_id" INTEGER PRIMARY KEY ASC AUTOINCREMENT NOT NULL UNIQUE, 
	"FK_id_Line" INTEGER NOT NULL, "FK_id_Station_Start" INTEGER, "FK_id_Station_End" INTEGER, 
	"Monday" NUMERIC NOT NULL, "Tuesday" NUMERIC NOT NULL,"Wednesday" NUMERIC NOT NULL, 
	"Thursday" NUMERIC NOT NULL,"Friday" NUMERIC NOT NULL, "Saturday" NUMERIC NOT NULL, 
	"Sunday_Holiday" NUMERIC NOT NULL, "Time" NUMERIC NOT NULL, "LastAssignedLine" INTEGER, 
	"DataInici" NUMERIC NOT NULL, "DataFi" NUMERIC);
	*/

	static final String  T_TimeTable = "TimeTable";		
	static final String  col_TimeTable_id =  T_TimeTable + "." + "_id";	
	static final String  col_TimeTable_FK_id_Line = "FK_id_Line";	
	static final String  col_TimeTable_FK_id_Station_Start = "FK_id_Station_Start";	
	static final String  col_TimeTable_FK_id_Station_End = "FK_id_Station_End";	
	static final String  col_TimeTable_Monday = "Monday";	
	static final String  col_TimeTable_Tuesday = "Tuesday";	
	static final String  col_TimeTable_Wednesday = "Wednesday";	
	static final String  col_TimeTable_Thursday = "Thursday";	
	static final String  col_TimeTable_Friday = "Friday";	
	static final String  col_TimeTable_Saturday = "Saturday";	
	static final String  col_TimeTable_Sunday_Holiday = "Sunday_Holiday";	
	static final String  col_TimeTable_Time = "Time";	
	static final String  col_TimeTable_LastAssignedLine = "LastAssignedLine";	
	static final String  col_TimeTable_DataInici = "DataInici";	
	static final String  col_TimeTable_DataFi = "DataFi";	
	
	/*
	-- _id	FK_id_TransportOnDemand	TimeEnd	TimeIni		TimetableOnDemand
	CREATE TABLE "TimetableOnDemand" ("_id" INTEGER PRIMARY KEY ASC AUTOINCREMENT NOT NULL UNIQUE, 
	"FK_id_TransportOnDemand" INTEGER NOT NULL, "TimeEnd" NUMERIC, "TimeIni" NUMERIC);
	*/

	static final String  T_TimeTableOnDemand = "TimeTableOnDemand";		
	static final String  col_TimeTableOnDemand_id = T_TimeTableOnDemand + "." + "_id";	
	static final String  col_TimeTableOnDemand_FK_id_TaxiStation = "FK_id_TransportOnDemand";	
	static final String  col_TimeTableOnDemand_TimeEnd = "TimeEnd";	
	static final String  col_TimeTableOnDemand_TimeIni = "TimeIni";	

	/*
	-- _id	FK_id_Itinerary_Destination	FK_id_Itinerary_Source	FK_id_Station_Destination	FK_id_Station_Source	Distance	Time		Transfer
	CREATE TABLE "Transfer" ("_id" INTEGER PRIMARY KEY ASC AUTOINCREMENT NOT NULL UNIQUE, 
	"FK_id_Itinerary_Destination" INTEGER NOT NULL, "FK_id_Itinerary_Source" INTEGER NOT NULL, 
	"FK_id_Station_Destination" INTEGER NOT NULL, "FK_id_Station_Source" INTEGER NOT NULL, 
	"Distance" INTEGER NOT NULL, "Time" NUMERIC NOT NULL);
	*/

	static final String  T_Transfer = "Transfer";		
	static final String  col_Transfer_id = T_Transfer + "." + "_id";	
	static final String  col_Transfer_FK_id_Itinerary_Destination = "FK_id_Itinerary_Destination";	
	static final String  col_Transfer_FK_id_Itinerary_Source = "FK_id_Itinerary_Source";	
	static final String  col_Transfer_FK_id_Station_Destination = "FK_id_Station_Destination";	
	static final String  col_Transfer_FK_id_Station_Source = "FK_id_Station_Source";	
	static final String  col_Transfer_Distance = "Distance";	
	static final String  col_Transfer_Time = "Time";
	
	/*
	-- _id	FavouriteTransferSource	VIEW
	CREATE VIEW "FavouriteTransferSource" AS  	SELECT Transfer._id AS _id from Transfer 
	INNER JOIN Transport INNER JOIN Itinerary ON Transport._id = Itinerary.FK_id_Transport 
	AND Itinerary._id = Transfer.FK_id_Itinerary_Source  where Transport.FavouriteTransport = 1;
	*/
	static final String  V_FavouriteTransferSource = "FavouriteTransferSource";
	static final String  col_FavouriteTransferSource_id = V_FavouriteTransferSource + "." + "_id";	
	
		
	/*
	-- _id	FK_id_TypeTransport	FavouriteTransport		Transport
	CREATE TABLE "Transport" ("_id" INTEGER PRIMARY KEY ASC AUTOINCREMENT NOT NULL UNIQUE, 
	"FK_id_TypeTransport" INTEGER NOT NULL, "FavouriteTransport" NUMERIC NOT NULL);
	*/

	static final String  T_Transport = "Transport";		
	static final String  col_Transport_id = T_Transport + "." + "_id";	
	static final String  col_Transport_FK_id_TypeTransport = "FK_id_TypeTransport";	
	static final String  col_Transport_FavouriteTransport = "FavouriteTransport";	
	
	/*
	-- _id	FK_id_Station	Capacity	OnDemandAvailable		TransportOnDemand
	CREATE TABLE "TransportOnDemand" ("_id" INTEGER PRIMARY KEY ASC AUTOINCREMENT NOT NULL UNIQUE, 
	"FK_id_Station" INTEGER NOT NULL, "Capacity" INTEGER, "OnDemandAvailable" INTEGER);
	*/
	
	static final String  T_TransportOnDemand = "TransportOnDemand";		
	static final String  col_TransportOnDemand_id = T_TransportOnDemand + "." + "_id";	
	static final String  col_TransportOnDemand_FK_id_Station = "FK_id_Station";	
	static final String  col_TransportOnDemand_Capacity = "Capacity";	
	static final String  col_TransportOnDemand_OnDemandAvailable = "OnDemandAvailable";
	
	/**
	* Constructor
	* Toma referencia hacia el contexto de la aplicación que lo invoca para poder acceder a los 'assets' y 'resources' de la aplicación.
	* Crea un objeto DBOpenHelper que nos permitirá controlar la apertura de la base de datos.
	* @param context
	*/
	public DataStore(Context mContext) {
		super(mContext,  "moveOnPalma_db.sqlite"	, null, 1);
		myContext = mContext;
		DB_PATH = myContext.getFilesDir().getPath() + File.separator;
		DB_NAME = myContext.getResources().getString(R.string.myDatabase);
	}

	/**
	* Función que devuelve un Booleano incluso si recibe Null 
	*/
	private boolean getBoolean(String val) {
		try {
			Boolean v = Integer.parseInt(val) == Config.TRUE;
			return v;
		}
		catch (NumberFormatException e) {
			// En caso de error devuelve falso, el valor no es 1
			Log.e(Config.LOGTAG, "DataStore.getBoolean(String val)");    		 	 	 					
		}
		return false;
	}
	
	/**
	* Función que devuelve un Booleano incluso si recibe Null 
	*/
	private String booleanToString(boolean val) {
		String res;
		
		res = String.valueOf(Config.FALSE);
		try {
			int v = val ? Config.TRUE : Config.FALSE;
			res = String.valueOf(v);
		}
		catch (Exception e) {
			// En caso de error devuelve falso, el valor no es 1
			Log.e(Config.LOGTAG, "DataStore.booleanToString(boolean val)");    		 	 	 					
		}
		return res;
	}

	
	/**
	* Crea una base de datos vacía en el sistema y la reescribe con nuestro fichero de base de datos. 
	*/
	public void createDataBase() throws IOException{
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.createDataBase()");    
	 
		boolean dbExist = checkDataBase();
		
		//We force to rewrite our DataBase, change it!!!
		if(dbExist && !force){
			if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.createDataBase().dbExist = true");    
			//la base de datos existe y no hacemos nada.
		}else{
			//Llamando a este método se crea la base de datos vacía en la ruta por defecto del sistema
			//de nuestra aplicación por lo que podremos sobreescribirla con nuestra base de datos.
			this.getReadableDatabase();
			if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.createDataBase().getReadableDatabase()");    		 
			try {		 
				copyDataBase();
			} catch (IOException e) {
				Log.e(Config.LOGTAG, "DataStore.createDataBase() Error copiando Base de Datos");    		 
				throw new Error("Error copiando Base de Datos");
			}
			force = false;
			if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.createDataBase() force change to FALSE");    
		}
	}
	
	/**
	* Comprueba si la base de datos existe para evitar copiar siempre el fichero cada vez que se abra la aplicación.
	* @return true si existe, false si no existe
	*/
	private boolean checkDataBase(){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.chechDataBase()");    		 	 
		SQLiteDatabase checkDB = null;
		 
		try{
			String myPath = DB_PATH + DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
		}catch(SQLiteException e) {
			//si llegamos aqui es porque la base de datos no existe todavía. 
			Log.e(Config.LOGTAG, "DataStore.checkDataBase()");    		 	 	 		
		}
		if(checkDB != null){
			checkDB.close();
			if (Config.DEBUG) Log.i(Config.LOGTAG, "DataStore.chechDataBase().true");    		 	 		
		}
		return checkDB != null ? true : false;
	}

	/**
	* Copia nuestra base de datos desde la carpeta assets a la recién creada
	* base de datos en la carpeta de sistema, desde dónde podremos acceder a ella.
	* Esto se hace con bytestream.
	*/
	private void copyDataBase() throws IOException{
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.copyDataBase()");    		 	 	 
		//Abrimos el fichero de base de datos como entrada
		InputStream myInput = myContext.getAssets().open(DB_NAME + ".mp3");
		 
		//Ruta a la base de datos vacía recién creada
		String outFileName = DB_PATH + DB_NAME;
		 
		//Abrimos la base de datos vacía como salida
		OutputStream myOutput = new FileOutputStream(outFileName);
		 
		//Transferimos los bytes desde el fichero de entrada al de salida
		//El límite està en 1 Mb
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer))>0){
			myOutput.write(buffer, 0, length);
		}		 
		//Liberamos los streams
		myOutput.flush();
		myOutput.close();
		myInput.close();
		if (Config.DEBUG) Log.i(Config.LOGTAG, "DataStore.copyDataBase().FINISHED");    		 	 	 		
	}
	
	/**
	* Abre la base de datos en modo lectura, para gestion de favoritos
	* es necesario permiso de escritura
	*/
	protected void openDataBase() throws SQLException{	 
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.openDataBase()");    		 	 	 
		//Abre la base de datos
		try {
			createDataBase();
		} catch (IOException e) {
			Log.e(Config.LOGTAG, "DataStore.openDataBase().error_loadingDB");    		 	 	 
			throw new Error(myContext.getResources().getString(R.string.error_loadingDB));
		} 
		String myPath = DB_PATH + DB_NAME;
		myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY); 
	    // Enable foreign key constraints
	    if (myDataBase.isReadOnly()) {
			if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.openDataBase() FK is ON");    		 	 	 
	        myDataBase.execSQL("PRAGMA foreign_keys = ON;");
	    } else {
	    	// nunca deberia ejecutarse, puesto que hemos abierto la DB en modo lectura
			if (Config.DEBUG) Log.e(Config.LOGTAG, "DataStore.openDataBase() FK is not ON");    		 	 	 
	    }
		if (Config.DEBUG) Log.i(Config.LOGTAG, "DataStore.openDataBase().FINISHED");    		 	 	 
	}
	
	/**
	* Abre la base de datos en modo escritura necesario para la gestión de favoritos
	*/
	protected void openDBWritable() throws SQLException{
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.openDBWritable()");    		 	 	 
		 
		//Abre la base de datos
		try {
			createDataBase();
		} catch (IOException e) {
			Log.e(Config.LOGTAG, "DataStore.openDBWritable().error_loadingDB");    		 	 	 
			throw new Error(myContext.getResources().getString(R.string.error_loadingDB));
		}
		 
		String myPath = DB_PATH + DB_NAME;
		myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE); 
		if (Config.DEBUG) Log.i(Config.LOGTAG, "DataStore.openDBWritable().FINISHED");    		 	 	 
	}

	@Override
	public synchronized void close() {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.close()");    		 	 	 
		if(myDataBase != null) {
			myDataBase.close();
		}
		super.close();
	}
	 
	@Override
	public void onCreate(SQLiteDatabase db) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.onCreate()");    		 	 	 
		// No s'utilitza, donat que importam la BD ja creada
	 
	}
	 
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Pendent d'implementar per a les actualitzación de la BD, inicialment cada modificació
		// implica la substitució de tota la BD, caldria optimitzar les modificacions reduint
		// la grandària de les dades a transferir, ho podem controlar amb el número de versió
		// de la nostra BD...inicialment té la versió 1 	 
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.onUpgrade()");    		 	 	 
	}
		
	// create PublicTransport Singleton class, si es modifica el tipus de tranport favorit
	// es modifica amb el set
	protected void createPublicTransport(TypeLanguage mLang, int distance){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.createPublicTransport()");  
		PublicTransport myPublic = PublicTransport.getInstance();
		myPublic.setDistance(distance);
		// Concept of Message only for FavourtiteTransport
		myPublic.setListAllMessages(getAllMessages(mLang));
		// Type of transport included in this app
		myPublic.setListAllTypeOfTransport(getAllTypeOfTransport(false));
		// Concept of Itinerary, every Itinerary can have different Lines considerate only FavouriteTransport
		myPublic.setListAllItineraries(getAllItinerary(distance, false));
		// Concept of Station
		myPublic.setListAllStations(getAllStationFavoriteTransport(distance, false));
		
		//We don't need the transfers instead of we calculate a route, not implemented in MoveOnApp v1.0
		//More than 10668 possibilities only with byke, subway AND bus :(
		// Concept of Transfer from one Line to other Line, only from favourite transport
		//(distance between station 0 < 150 m)   
		//myPublic.setListAllTransfers(getAllTransfer());
	}

	//private ArrayList<Transport> 
	private Cursor readAllTypeOfTransport(boolean onlyFavorite){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.readAllTypeOfTransport()");
		Cursor c= null;
		try{
			if (onlyFavorite){
				String [] columns=new String[]{"_id" , col_Transport_FK_id_TypeTransport , col_Transport_FavouriteTransport};
				String selection = col_Transport_FavouriteTransport + " =?";
				String[] selectionArgs = { booleanToString(onlyFavorite)};
				
				//Cursor c = myDataBase.query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, orderBy)
				//Cursor c = myDataBase.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit)
				//Cursor c = myDataBase.query(DISTINCT, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit).query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit)
				c = myDataBase.query(T_Transport, columns, selection, selectionArgs, null, null, null);
				
			} else {
				String [] columns=new String[]{"_id" , col_Transport_FK_id_TypeTransport , col_Transport_FavouriteTransport};
				
				//Cursor c = myDataBase.query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, orderBy)
				//Cursor c = myDataBase.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit)
				//Cursor c = myDataBase.query(DISTINCT, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit).query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit)
				c = myDataBase.query(T_Transport, columns, null, null, null, null, null);
				
			}
		}
        catch (Exception e) {
            Log.e(Config.LOGTAG, "DataStore.readAllTypeOfTransport() " + e.toString());
        }
		
		return c;
	}

	protected ArrayList<Transport> getAllTypeOfTransport(boolean onlyFavorite) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.getAllTypeOfTransport()");    		 	 	 		
		int myID;
		int myTransport;
		boolean myFavouriteTransport;
		Transport tr;

	    ArrayList<Transport> resultList = new ArrayList<Transport>();

	    // Function to retrieve all values from Transport
	    Cursor c = readAllTypeOfTransport(onlyFavorite);
	    try {
		    while (c.moveToNext())
		    {
		    	myID = c.getInt(c.getColumnIndex("_id"));
		    	// Typus de transport
		    	myTransport = c.getInt(c.getColumnIndex(col_Transport_FK_id_TypeTransport));
		    	myFavouriteTransport = getBoolean(c.getString(c.getColumnIndex(col_Transport_FavouriteTransport)));
		    	tr = new Transport(myID, myTransport, myFavouriteTransport);
	            resultList.add(tr);   
	        }
	    }
	    catch (Exception e) {
	    	Log.e(Config.LOGTAG, "DataStore.getAllTypeOfTransport() " + e.toString());
	    } finally {
	    	if(c != null) c.close();
	    }
	    return resultList;
	}
	
/*
	SELECT Itinerary._id AS _id, FK_id_Transport, Code, Name, ColorHEX, FavoriteItinerary, FK_id_TypeTransport 
	FROM Itinerary INNER JOIN Transport ON FK_id_Transport = Transport._id 
	WHERE FavouriteTransport = 1 ORDER BY Itinerary._id ASC;
*/	
	//private ArrayList<Itinerary> 
	private Cursor readAllItinerary(boolean onlyFavorite){		
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.readAllItinerary()"); 
		Cursor c = null;
		if (!onlyFavorite){
			try {
		
				//Create new querybuilder
				SQLiteQueryBuilder _QB = new SQLiteQueryBuilder();
				
				//Specify Itinerary table AND add JOIN to Transport table only for FavouriteTransport
				_QB.setTables(T_Itinerary + " INNER JOIN " + T_Transport);
				_QB.appendWhere(T_Itinerary + "." + col_Itinerary_FK_id_Transport + " = " + col_Transport_id);
				
				String projectionIn[] = new String[] { col_Itinerary_id + " AS _id" , T_Itinerary + "." + col_Itinerary_Code,
						T_Itinerary + "." + col_Itinerary_FK_id_Transport, T_Itinerary + "." + col_Itinerary_Name, 
						T_Itinerary + "." + col_Itinerary_ColorHEX, T_Itinerary + "." + col_Itinerary_FavoriteItinerary,
						T_Itinerary + "." + col_Itinerary_DisabledFacilities, T_Transport + "." + col_Transport_FK_id_TypeTransport};
				String selection = T_Transport + "." + col_Transport_FavouriteTransport + " =?";
				
				String[] selectionArgs = new String[] {String.valueOf(Config.TRUE)};
				String sortOrder = col_Itinerary_id + " ASC";
				//Get cursor
				c = _QB.query(myDataBase, projectionIn, selection, selectionArgs, null, null, sortOrder);
			}
	        catch (Exception e) {
	            Log.e(Config.LOGTAG, "DataStore.readAllTypeOfTransport() " + e.toString());
	        }
		} else {
			try {
				
				//Create new querybuilder
				SQLiteQueryBuilder _QB = new SQLiteQueryBuilder();
				
				//Specify Itinerary table AND add JOIN to Transport table only for FavouriteTransport
				_QB.setTables(T_Itinerary + " INNER JOIN " + T_Transport);
				_QB.appendWhere(T_Itinerary + "." + col_Itinerary_FK_id_Transport + " = " + col_Transport_id);
				
				String projectionIn[] = new String[] { col_Itinerary_id + " AS _id" , T_Itinerary + "." + col_Itinerary_Code,
						T_Itinerary + "." + col_Itinerary_FK_id_Transport, T_Itinerary + "." + col_Itinerary_Name, 
						T_Itinerary + "." + col_Itinerary_ColorHEX, T_Itinerary + "." + col_Itinerary_FavoriteItinerary,
						T_Itinerary + "." + col_Itinerary_DisabledFacilities, T_Transport + "." + col_Transport_FK_id_TypeTransport};
				String selection = T_Transport + "." + col_Transport_FavouriteTransport + " =? AND " +
						T_Itinerary + "." + col_Itinerary_FavoriteItinerary + " =?";
				
				String[] selectionArgs = new String[] {String.valueOf(Config.TRUE), String.valueOf(Config.TRUE)};
				String sortOrder = col_Itinerary_id + " ASC";
				//Get cursor
				c = _QB.query(myDataBase, projectionIn, selection, selectionArgs, null, null, sortOrder);
			}
	        catch (Exception e) {
	            Log.e(Config.LOGTAG, "DataStore.readAllTypeOfTransport() " + e.toString());
	        }
		}
		
		return c;
	}

	protected ArrayList<Itinerary> getAllItinerary(int distance, boolean onlyFavorite) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.getAllItinerary()");    		 	 	 		

		int myID;
		int myFK_id_Transport;
		String myCode;
		String myName;
		//String myColorHEX;
		boolean myFavorite;
		boolean myDisabledFacilities;
		int myTypeOfTransport;
		Itinerary it;
		
	    ArrayList<Itinerary> resultList = new ArrayList<Itinerary>();

	    // Function to retrieve all values from Itinerary
	    Cursor c = readAllItinerary(onlyFavorite);
	    try {
		    while (c.moveToNext())
		    {
		    	myID = c.getInt(c.getColumnIndex("_id"));
				myFK_id_Transport = c.getInt(c.getColumnIndex(col_Itinerary_FK_id_Transport));
				myCode = c.getString(c.getColumnIndex(col_Itinerary_Code));
				myName = c.getString(c.getColumnIndex(col_Itinerary_Name));
				//myColorHEX = c.getString(c.getColumnIndex(col_Itinerary_ColorHEX));
				myFavorite = getBoolean(c.getString(c.getColumnIndex(col_Itinerary_FavoriteItinerary)));
				myDisabledFacilities = getBoolean(c.getString(c.getColumnIndex(col_Itinerary_DisabledFacilities)));
				myTypeOfTransport = c.getInt(c.getColumnIndex(col_Transport_FK_id_TypeTransport));
		    	
		    	it = new Itinerary(myID, myFK_id_Transport, myCode, myName, myFavorite, myDisabledFacilities, myTypeOfTransport);
	            resultList.add(it);
	            //retrieve Lines from id_Itinerary, if our Itinerary is Bike only get first element
	            if (TypeTransport.BICIPALMA.equals(TypeTransport.getTypeTransport(myTypeOfTransport))){
	            	// BICIPALMA has only a Virtual Line
	            	it.setLines(getOnlyFirstLine(myID, distance));
	            } else {
		            it.setLines(getAllLines(myID, distance));
	            }	     
	            //retrieve Transfer from favorite type of transport from id_Itinerary
	            //it.setListTransfer(getAllTransferDistinctTypeOfTransportByIdItinerary(myID, distance));
		    }
	    }
	    catch (Exception e) {
	        Log.e(Config.LOGTAG, "DataStore.getAllItinerary() " + e.toString());
	    } finally {
		    if(c != null) c.close();
	    }
	    return resultList;
	}
	
	//private ArrayList<Line> 
	private Cursor readAllLines(int idItinerary){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.readAllLine(idItinerary)");
		Cursor c = null;
		try{
			String [] columns=new String[]{"_id", col_Line_UNameOneWay, 
					col_Line_UNameOneWay2 ,col_Line_UNameReturn, col_Line_UNameReturn2, 
					col_Line_OneWayEMT, col_Line_ReturnEMT};
			String selection = col_Line_FK_id_Itinerary+"=?";
			String[] where = new String[] {String.valueOf(idItinerary)};
			c = myDataBase.query(T_Line, columns, selection, where, null, null, null);
		}
	    catch (Exception e) {
	        Log.e(Config.LOGTAG, "DataStore.readAllLine(idItinerary) " + e.toString());
	    }
		return c;
	}

	protected ArrayList<Line> getAllLines(int idItinerary, int distance) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.getAllLines(" +idItinerary + ")");    		 	 	 		
		int myID;
		String myUNameOneWay;
		String myUNameOneWay2;
		String myUNameReturn;
		String myUNameReturn2;
		String myOneWayEMT;
		String myReturnEMT;
		
		boolean myFavourite;
		Line li;

		ArrayList<Line> resultList = new ArrayList<Line>();

	    // Function to retrieve all lines by idItinerary
	    Cursor c = readAllLines(idItinerary);
	    try {
		    while (c.moveToNext())
		    {
	        	myID = c.getInt(c.getColumnIndex("_id"));
	        	myUNameOneWay = c.getString(c.getColumnIndex(col_Line_UNameOneWay));
	        	myUNameOneWay2 = c.getString(c.getColumnIndex(col_Line_UNameOneWay2));
	        	myUNameReturn = c.getString(c.getColumnIndex(col_Line_UNameReturn));
	        	myUNameReturn2 = c.getString(c.getColumnIndex(col_Line_UNameReturn2));
	        	myOneWayEMT = c.getString(c.getColumnIndex(col_Line_OneWayEMT));
	        	myReturnEMT = c.getString(c.getColumnIndex(col_Line_ReturnEMT));
		    	
		    	li = new Line(myID, idItinerary, myUNameOneWay, myUNameOneWay2, myUNameReturn, myUNameReturn2, myOneWayEMT, myReturnEMT);
	            resultList.add(li);
	            // getting if Lines is Favourite
	            myFavourite = IsFavouriteLine(myID);
	            li.setFavorite(myFavourite);
	            //retrieve Transfer from favorite type of transport from id_Itinerary
	            //li.setListTransfer(getAllTransferDistinctTypeOfTransportByIdLine(myID, distance));
	        }
	    }
	    catch (Exception e) {
	    	Log.e(Config.LOGTAG, "DataStore.getAllLines(int idItinerary) " + e.toString());
	    } finally {
	    	if(c != null) c.close();
	    }
	    return resultList;	
	}
	
	//private ArrayList<Itinerary> 
	private Cursor readAllItineraryBiciPalma(){		
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.readAllItineraryBiciPalma()"); 
		Cursor c = null;
		try {
	
			//Create new querybuilder
			SQLiteQueryBuilder _QB = new SQLiteQueryBuilder();
			
			//Specify Itinerary table AND add JOIN to Transport table only for FavouriteTransport
			_QB.setTables(T_Itinerary + " INNER JOIN " + T_Transport);
			_QB.appendWhere(T_Itinerary + "." + col_Itinerary_FK_id_Transport + " = " + col_Transport_id);
			
			String projectionIn[] = new String[] { col_Itinerary_id + " AS _id", T_Itinerary + "." + col_Itinerary_FK_id_Transport,
					T_Itinerary + "." + col_Itinerary_Code, T_Itinerary + "." + col_Itinerary_Name, 
					T_Itinerary + "." + col_Itinerary_ColorHEX, T_Itinerary + "." + col_Itinerary_FavoriteItinerary,
					T_Itinerary + "." + col_Itinerary_DisabledFacilities, T_Transport + "." + col_Transport_FK_id_TypeTransport};
			String selection = T_Transport + "." + col_Transport_FavouriteTransport + " =? AND " 
					+ col_Transport_id + " =?";
			
			String[] selectionArgs = new String[] {String.valueOf(Config.TRUE), String.valueOf(TypeTransport.BICIPALMA.getValue())};
			String sortOrder = col_Itinerary_id + " ASC";
			//Get cursor
			c = _QB.query(myDataBase, projectionIn, selection, selectionArgs, null, null, sortOrder);
		}
        catch (Exception e) {
            Log.e(Config.LOGTAG, "DataStore.readAllTypeOfTransport() " + e.toString());
        }
		
		return c;
	}
	
	// retrive only the first Itinerary of the BiciPalma
	protected Itinerary getOnlyFirstItinerary(int distance) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.getAllItinerary()");    		 	 	 		

		int myID;
		int myFK_id_Transport;
		String myCode;
		String myName;
		//String myColorHEX;
		boolean myFavorite;
		boolean myDisabledFacilities;
		int myTypeOfTransport;
		Itinerary it = null;
		
	    // Function to retrieve all values from Itinerary
	    Cursor c = readAllItineraryBiciPalma();
	    try {
		    if (c.moveToNext())
		    {
		    	myID = c.getInt(c.getColumnIndex("_id"));
				myFK_id_Transport = c.getInt(c.getColumnIndex(col_Itinerary_FK_id_Transport));
				myCode = c.getString(c.getColumnIndex(col_Itinerary_Code));
				myName = c.getString(c.getColumnIndex(col_Itinerary_Name));
				//myColorHEX = c.getString(c.getColumnIndex(col_Itinerary_ColorHEX));
				myFavorite = getBoolean(c.getString(c.getColumnIndex(col_Itinerary_FavoriteItinerary)));
				myDisabledFacilities = getBoolean(c.getString(c.getColumnIndex(col_Itinerary_DisabledFacilities)));
				myTypeOfTransport = c.getInt(c.getColumnIndex(col_Transport_FK_id_TypeTransport));
		    	
		    	it = new Itinerary(myID, myFK_id_Transport, myCode, myName, myFavorite, myDisabledFacilities, myTypeOfTransport);
	            //retrieve Lines from id_Itinerary, if our Itinerary is Bike only get first element
	            if (TypeTransport.BICIPALMA.equals(TypeTransport.getTypeTransport(myTypeOfTransport))){
	            	// BICIPALMA has only a Virtual Line
	            	it.setLines(getOnlyFirstLine(myID, distance));
	            } else {
		            it.setLines(getAllLines(myID, distance));
	            }	     
	            //retrieve Transfer from favorite type of transport from id_Itinerary
	            //it.setListTransfer(getAllTransferDistinctTypeOfTransportByIdItinerary(myID, distance));
		    }
	    }
	    catch (Exception e) {
	        Log.e(Config.LOGTAG, "DataStore.getAllItinerary() " + e.toString());
	    } finally {
	    	if(c != null) c.close();
	    }
	    return it;
	}

	// retrive only the first line of the Itinerary (only for virtual Bike lines)
	protected ArrayList<Line> getOnlyFirstLine(int idItinerary, int distance) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.etOnlyFirstLine(" + idItinerary + ")");    		 	 	 		
		int myID;
		String myUNameOneWay;
		String myUNameOneWay2;
		String myUNameReturn;
		String myUNameReturn2;
		String myOneWayEMT;
		String myReturnEMT;
		
		boolean myFavourite;
		Line li;

		ArrayList<Line> resultList = new ArrayList<Line>();

	    // Function to retrieve all lines by idItinerary
	    Cursor c = readAllLines(idItinerary);
	    try {
		    if (c.moveToNext())
		    {
	        	myID = c.getInt(c.getColumnIndex("_id"));
	        	myUNameOneWay = c.getString(c.getColumnIndex(col_Line_UNameOneWay));
	        	myUNameOneWay2 = c.getString(c.getColumnIndex(col_Line_UNameOneWay2));
	        	myUNameReturn = c.getString(c.getColumnIndex(col_Line_UNameReturn));
	        	myUNameReturn2 = c.getString(c.getColumnIndex(col_Line_UNameReturn2));
	        	myOneWayEMT = c.getString(c.getColumnIndex(col_Line_OneWayEMT));
	        	myReturnEMT = c.getString(c.getColumnIndex(col_Line_ReturnEMT));
		    	
		    	li = new Line(myID, idItinerary, myUNameOneWay, myUNameOneWay2, myUNameReturn, myUNameReturn2, myOneWayEMT, myReturnEMT);
	            resultList.add(li);
	            // getting if Lines is Favourite
	            myFavourite = IsFavouriteLine(myID);
	            li.setFavorite(myFavourite);
	            //retrieve Transfer from favorite type of transport from id_Itinerary BICIPALMA Virtual Line
	            //li.setListTransfer(getAllTransferDistinctTypeOfTransportByIdItinerary(idItinerary, distance));
	        }
	    }
	    catch (Exception e) {
	    	Log.e(Config.LOGTAG, "DataStore.getOnlyFirstLine(int idItinerary) " + e.toString());
	    } finally {
	    	if(c != null) c.close();
	    }
	    return resultList;	
	}

	//private int
	private Cursor readLineIsCircleLine(int idLine){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.readLineIsCircleLine(idLine)");
		Cursor c=null;
		try{
			String [] columns=new String[]{col_LineStation_id};
			String selection = col_LineStation_FK_id_Line+"=? AND "+col_LineStation_RouteOneWay+"=? AND "+col_LineStation_RouteReturn+"=?";
			String[] where =  new String[] {String.valueOf(idLine),String.valueOf(Config.TRUE), String.valueOf(Config.TRUE)};
			c = myDataBase.query(T_LineStation, columns, selection, where, null, null, null);
		}
	    catch (Exception e) {
	        Log.e(Config.LOGTAG, "DataStore.readLineIsCircleLine(int idLine) " + e.toString());
	    }
		return c;
	}
			
	//boolean isCircleLine 
	protected boolean getLineIsCircleLine(int idLine){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.getLineIsCircleLine(idLine)");
		boolean res= false;
		try {
			res = (readLineIsCircleLine(idLine).getCount()>0);
		}
	        catch (Exception e) {
	            Log.e(Config.LOGTAG, "DataStore.getLineIsCircleLine(idLine) " + e.toString());
	        }
		return res;
	}

/*	
	SELECT FK_id_Transport FROM Line as a Join Itinerary as b Join Transport as c 
	on FK_id_Itinerary = b._id and FK_id_Transport = c._id 
	where a._id = 3000001;
*/
		
	//private int
	private Cursor readLineIsBikeLine(int idLine){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.readLineIsBikeLine(idLine)");
		Cursor c=null;
		
		try{
			//Create new querybuilder
			SQLiteQueryBuilder _QB = new SQLiteQueryBuilder();
			
			//Specify Itinerary table AND add JOIN to Line table (use full_id for joining categories table)
			_QB.setTables(T_Line + " INNER JOIN " + T_Itinerary + " INNER JOIN " + T_Transport + " ON " + 
					col_Line_FK_id_Itinerary + " = " + col_Itinerary_id + " AND " + col_Itinerary_FK_id_Transport + " = " + col_Transport_id);
			String [] columns=new String[]{col_Transport_FK_id_TypeTransport};
			String selection = col_Line_id +"=? AND "+ col_Transport_FK_id_TypeTransport + "=?";
			String[] where =  new String[] {String.valueOf(idLine), String.valueOf(TypeTransport.BICIPALMA.getValue())};
			c = _QB.query(myDataBase, columns, selection, where, null, null, null);
		}
	    catch (Exception e) {
	        Log.e(Config.LOGTAG, "DataStore.readLineIsBikeLine(idLine) " + e.toString());
	    }		
		return c;
	}
			
	//boolean isBikeLine 
	protected boolean getLineIsBikeLine(int idLine){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.getLineIsBikeLine(int idLine)");
		boolean res= false;
		try {
			res = (readLineIsBikeLine(idLine).getCount()>0);
		}
	        catch (Exception e) {
	            Log.e(Config.LOGTAG, "DataStore.getLineIsBikeLine(idLine) " + e.toString());
	        }
		return res;
	}

	/*
	 * Select _id from Station where _id =1000001 AND FK_id_Transport = 3
	 */
		
	/*	
	SELECT FK_id_Transport FROM Line as a Join Itinerary as b Join Transport as c 
	on FK_id_Itinerary = b._id and FK_id_Transport = c._id 
	where a._id = 3000001;
*/
		
	//private int
	private Cursor readLineIsBusLine(int idLine){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.readLineIsBusLine(int idLine)");
		Cursor c=null;
		
		try{
			//Create new querybuilder
			SQLiteQueryBuilder _QB = new SQLiteQueryBuilder();
			
			//Specify Itinerary table AND add JOIN to Line table (use full_id for joining categories table)
			_QB.setTables(T_Line + " INNER JOIN " + T_Itinerary + " INNER JOIN " + T_Transport + " ON " + 
					col_Line_FK_id_Itinerary + " = " + col_Itinerary_id + " AND " + col_Itinerary_FK_id_Transport + " = " + col_Transport_id);
			String [] columns=new String[]{col_Transport_FK_id_TypeTransport};
			String selection = col_Line_id +"=? AND "+ col_Transport_FK_id_TypeTransport + "=?";
			String[] where =  new String[] {String.valueOf(idLine), String.valueOf(TypeTransport.BUSPALMA.getValue())};
			c = _QB.query(myDataBase, columns, selection, where, null, null, null);
		}
	    catch (Exception e) {
	        Log.e(Config.LOGTAG, "DataStore.readLineIsBusLine(int idLine) " + e.toString());
	    }		
		return c;
	}
			
	//boolean isBikeLine 
	protected boolean getLineIsBusLine(int idLine){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.getLineIsBusLine(int idLine)");
		boolean res= false;
		try {
			res = (readLineIsBusLine(idLine).getCount()>0);
		}
	        catch (Exception e) {
	            Log.e(Config.LOGTAG, "DataStore.getLineIsBusLine(int idLine) " + e.toString());
	        }
		return res;
	}


	
	//private isFavouriteLine
	private Cursor readFavouriteLine(int idLine){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.readLineIsFavourite(idLine)");
		Cursor c=null;
		try{
			String [] columns=new String[]{"_id", col_FavouriteLine_Name};
			String selection = col_FavouriteLine_FK_id_Line + " =?";
			String[] where =  new String[] {String.valueOf(idLine)};
			c = myDataBase.query(T_FavouriteLine, columns, selection, where, null, null, null);
		}
	    catch (Exception e) {
	        Log.e(Config.LOGTAG, "DataStore.readLineIsFavourite(idLine) " + e.toString());
	    }
		return c;
	}
	
	//FavouriteLine 
	protected FavouriteLine getFavouriteLine(int idLine){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.getFavouriteLine(idLine)");
		int myID;
		String myName;
		
		FavouriteLine fl = null;
	    // Function to retrieve value from FavoriteLine
	    Cursor c = readFavouriteLine(idLine);
	    
        try {
        	if(c.moveToFirst()) {
        		myID = c.getInt(c.getColumnIndex("_id"));
        		myName = c.getString(c.getColumnIndex(col_FavouriteLine_Name));
        		
        		fl = new FavouriteLine(myID, idLine, myName);
        	}
        }
        catch (Exception e) {
	        Log.e(Config.LOGTAG, "DataStore.getFavouriteLine(idLine)" + e.toString());
	    } finally {
	    	if(c != null) c.close();
	    }
	    return fl;
	}
	
	//FavouriteLine 
	protected boolean IsFavouriteLine(int idLine){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.IsFavouriteLine(" + idLine + ")");
		boolean res= false;
		try {
			res = (readFavouriteLine(idLine).getCount()>0);
		}
	        catch (Exception e) {
	            Log.e(Config.LOGTAG, "DataStore.IsFavouriteLine(int myID) " + e.toString());
	        }
		return res;
		
	}
	
	//private isFavouriteTransport
	private Cursor readTransport(int idTransport){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.readFavouriteTransport(idTransport)");
		Cursor c = null;
		try{
			String [] columns = new String[]{"_id", col_Transport_FK_id_TypeTransport, col_Transport_FavouriteTransport};
			String selection = col_Transport_id + "=?";
			String[] where =  new String[] {String.valueOf(idTransport)};
			c = myDataBase.query(T_Transport, columns, selection, where, null, null, null);
		}
	    catch (Exception e) {
	        Log.e(Config.LOGTAG, "DataStore.readFavouriteTransport(idTransport) " + e.toString());
	    }
		return c;
	}
	
	//FavouriteTransport 
	protected boolean isTransportFavorite(int idTransport){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.isTransportFavorite(idTransport)");
		boolean res = false;

		// Function to retrieve value from Transport
	    Cursor c = readTransport(idTransport);
	    
        try {
        	if(c.moveToFirst()) {
        		// access to the favourite column
        		res = (c.getInt(c.getColumnIndex(col_Transport_FavouriteTransport)) == Config.TRUE) ? true : false;
        	}
        }
        catch (Exception e) {
	        Log.e(Config.LOGTAG, "DataStore.isTransportFavorite(idTransport) " + e.toString());
	    } finally {	    
	    	if(c != null) c.close();
	    }
	    return res;
	}
		
	//private ArrayList<Station> 
	private Cursor readAllStation(){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.readAllStation()");    		 	 	 		
		Cursor c=null;
		try{
			String [] columns=new String[]{"_id",col_Station_FK_id_Transport, col_Station_Code, 
					col_Station_Name, col_Station_Adapted_for_Disabled, col_Station_Latitude, 
					col_Station_Longitude, col_Station_TimeStamp, col_Station_Transfer, col_Station_Velocity,
					col_Station_FK_id_TypeZone};
			String orderBy = col_Station_Name + " ASC";	
			c = myDataBase.query(T_Station, columns, null, null, null, null, orderBy);
		}
	    catch (Exception e) {
	        Log.e(Config.LOGTAG, "DataStore.readAllTypeOfTransport() " + e.toString());
	    }		
		return c;
	}
	
	protected ArrayList<Station> getAllStation(int distance) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.getAllStation()");   
		
		int myID;
		int myTransport;
		String myCode;
		String myName;
		boolean myAdapted_for_Disabled;
		GeoPosition myCoordinates;
		String myTimeStamp;
		boolean myTransfer;
		int myVelocity;
		int myZone;
		Station st;
		boolean myFavourite;
		
	    ArrayList<Station> resultList = new ArrayList<Station>();

	    // Function to retrieve all values from Station
	    Cursor c = readAllStation();
	    try{
		    while (c.moveToNext())
		    {
		    	myID = c.getInt(c.getColumnIndex("_id"));
		    	myTransport = c.getInt(c.getColumnIndex(col_Station_FK_id_Transport));
		    	myCode = c.getString(c.getColumnIndex(col_Station_Code));
		    	myName = c.getString(c.getColumnIndex(col_Station_Name));
				myAdapted_for_Disabled = getBoolean(c.getString(c.getColumnIndex(col_Station_Adapted_for_Disabled)));
				myCoordinates = new GeoPosition(c.getString(c.getColumnIndex(col_Station_Latitude)), c.getString(c.getColumnIndex(col_Station_Longitude)));
				myTimeStamp = c.getString(c.getColumnIndex(col_Station_TimeStamp));
				myTransfer = getBoolean(c.getString(c.getColumnIndex(col_Station_Transfer)));
				myVelocity = c.getInt(c.getColumnIndex(col_Station_Velocity));
				myZone = c.getInt(c.getColumnIndex(col_Station_FK_id_TypeZone));
				st = new Station(myID, myTransport, myCode, myName, myAdapted_for_Disabled, myCoordinates, myTimeStamp, myTransfer, myVelocity, myZone);
	            // getting if Station is Favourite
	            myFavourite = IsFavouriteStation(myID);
	            st.setFavorite(myFavourite);
	            //retrieve Transfer from favorite type of transport from id_Itinerary
		    	//st.setListTransfer(this.getAllTransferDistinctTypeOfTransportByIdStation(myID, distance));
	            resultList.add(st);
		    }
	    }
	    catch (Exception e) {
	            Log.e(Config.LOGTAG, "DataStore.getAllStation() " + e.toString());
	    }
	    finally {
		    if(c != null) c.close();
	    }
	    return resultList;
	}
	/*
	
SELECT DISTINCT a._id AS _id, a.FK_id_Transport, a.Code, a.Name, a.Adapted_for_Disabled, 
	a.Latitude, a.Longitude, a.TimeStamp, a.Transfer, a.Velocity, a.FK_id_TypeZone 
	FROM Station AS a INNER JOIN LineStation AS b INNER JOIN Line AS c INNER JOIN 
	Itinerary AS d INNER JOIN Transport AS e ON a._id = b.FK_id_Station AND 
	b.FK_id_Line = c._id AND c.FK_id_Itinerary = d._id AND d.FK_id_Transport = e._id
	WHERE e.FavouriteTransport = 1 ORDER BY a.Name ASC, a.Code ASC;	
	*/
	//private ArrayList<Station> 
	private Cursor readAllStationFavoriteTransport(boolean onlyFavorite){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.readAllStationFavoriteTransport(boolean onlyFavorite)");    		 	 	 		
		Cursor c=null;
		if (!onlyFavorite){
			try {				
				//Create new querybuilder
				SQLiteQueryBuilder _QB = new SQLiteQueryBuilder();
				
				// Distinct TRUE
				_QB.setDistinct(true);
				//Specify Itinerary table AND add JOIN to Transport table only for FavouriteTransport
				_QB.setTables(T_Station + " INNER JOIN " + T_LineStation + " INNER JOIN " + T_Line + " INNER JOIN " +  
						T_Itinerary + " INNER JOIN " + T_Transport);
				_QB.appendWhere( col_Station_id + " = " + T_LineStation + "." + col_LineStation_FK_id_Station +
						" AND " + T_LineStation + "." + col_LineStation_FK_id_Line + " = " +
						col_Line_id + " AND " + T_Line + "." + col_Line_FK_id_Itinerary +
						" = " + col_Itinerary_id + " AND " + T_Itinerary + "." + col_Itinerary_FK_id_Transport +
						" = " + col_Transport_id);
	
				String projectionIn[] = new String[]{ col_Station_id + " AS _id", T_Station + "." + col_Station_FK_id_Transport, 
						T_Station + "." + col_Station_Code, T_Station + "." + col_Station_Name, 
						T_Station + "." + col_Station_Adapted_for_Disabled, T_Station + "." + col_Station_Latitude, 
						T_Station + "." + col_Station_Longitude, T_Station + "." + col_Station_TimeStamp, 
						T_Station + "." + col_Station_Transfer, T_Station + "." + col_Station_Velocity,
						T_Station + "." + col_Station_FK_id_TypeZone};
				
				String selection = T_Transport + "." + col_Transport_FavouriteTransport + " =?";
				
				String[] selectionArgs = new String[] {String.valueOf(Config.TRUE)};
				String sortOrder = col_Transport_id + " DESC, " + T_Station + "." + col_Station_Name + " ASC, " + 
						T_Station + "." + col_Station_Code + " ASC";
				//Get cursor
				c = _QB.query(myDataBase, projectionIn, selection, selectionArgs, null, null, sortOrder);
			}
	        catch (Exception e) {
	            Log.e(Config.LOGTAG, "DataStore.readAllStationFavoriteTransport(boolean onlyFavorite) " + e.toString());
	        }
		} else {
			try {				
				//Create new querybuilder
				SQLiteQueryBuilder _QB = new SQLiteQueryBuilder();
				
				// Distinct TRUE
				_QB.setDistinct(true);
				//Specify Itinerary table AND add JOIN to Transport table only for FavouriteTransport
				_QB.setTables(T_Station + " INNER JOIN " + T_LineStation + " INNER JOIN " + T_Line + " INNER JOIN " +  
						T_Itinerary + " INNER JOIN " + T_Transport + " INNER JOIN " + T_FavouriteStation);
				_QB.appendWhere( col_Station_id + " = " + T_LineStation + "." + col_LineStation_FK_id_Station +
						" AND " + T_LineStation + "." + col_LineStation_FK_id_Line + " = " +
						col_Line_id + " AND " + T_Line + "." + col_Line_FK_id_Itinerary +
						" = " + col_Itinerary_id + " AND " + T_Itinerary + "." + col_Itinerary_FK_id_Transport +
						" = " + col_Transport_id + " AND " + col_Station_id + " = " + 
						T_FavouriteStation + "." + col_FavouriteStation_FK_id_Station);
	
				String projectionIn[] = new String[]{ col_Station_id + " AS _id", T_Station + "." + col_Station_FK_id_Transport, 
						T_Station + "." + col_Station_Code, T_Station + "." + col_Station_Name, 
						T_Station + "." + col_Station_Adapted_for_Disabled, T_Station + "." + col_Station_Latitude, 
						T_Station + "." + col_Station_Longitude, T_Station + "." + col_Station_TimeStamp, 
						T_Station + "." + col_Station_Transfer, T_Station + "." + col_Station_Velocity,
						T_Station + "." + col_Station_FK_id_TypeZone};
				
				String selection = T_Transport + "." + col_Transport_FavouriteTransport + " =?";
				
				String[] selectionArgs = new String[] {String.valueOf(Config.TRUE)};
				String sortOrder = col_Transport_id + " DESC, " + T_Station + "." + col_Station_Name + " ASC, " + 
						T_Station + "." + col_Station_Code + " ASC";
				//Get cursor
				c = _QB.query(myDataBase, projectionIn, selection, selectionArgs, null, null, sortOrder);
			}
	        catch (Exception e) {
	            Log.e(Config.LOGTAG, "DataStore.readAllStationFavoriteTransport(boolean onlyFavorite) " + e.toString());
	        }
		}
		return c;
	}
	
	protected ArrayList<Station> getAllStationFavoriteTransport(int distance, boolean onlyFavorite) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.getAllStationFavoriteTransport(int distance)");   
		
		int myID;
		int myTransport;
		String myCode;
		String myName;
		boolean myAdapted_for_Disabled;
		GeoPosition myCoordinates;
		String myTimeStamp;
		boolean myTransfer;
		int myVelocity;
		int myZone;
		Station st;
		boolean myFavourite;
		
	    ArrayList<Station> resultList = new ArrayList<Station>();

	    // Function to retrieve all values from Station
	    Cursor c = readAllStationFavoriteTransport(onlyFavorite);
	    try{
		    while (c.moveToNext())
		    {
		    	myID = c.getInt(c.getColumnIndex("_id"));
		    	myTransport = c.getInt(c.getColumnIndex(col_Station_FK_id_Transport));
		    	myCode = c.getString(c.getColumnIndex(col_Station_Code));
		    	myName = c.getString(c.getColumnIndex(col_Station_Name));
				myAdapted_for_Disabled = getBoolean(c.getString(c.getColumnIndex(col_Station_Adapted_for_Disabled)));
				myCoordinates = new GeoPosition(c.getString(c.getColumnIndex(col_Station_Latitude)), c.getString(c.getColumnIndex(col_Station_Longitude)));
				myTimeStamp = c.getString(c.getColumnIndex(col_Station_TimeStamp));
				myTransfer = getBoolean(c.getString(c.getColumnIndex(col_Station_Transfer)));
				myVelocity = c.getInt(c.getColumnIndex(col_Station_Velocity));
				myZone = c.getInt(c.getColumnIndex(col_Station_FK_id_TypeZone));		    	
		    	st = new Station(myID, myTransport, myCode, myName, myAdapted_for_Disabled, myCoordinates, myTimeStamp, myTransfer, myVelocity, myZone);
	            // getting if Station is Favourite
	            myFavourite = IsFavouriteStation(myID);
	            st.setFavorite(myFavourite);
	            //retrieve Transfer from favorite type of transport from id_Itinerary
		    	//st.setListTransfer(this.getAllTransferDistinctTypeOfTransportByIdStation(myID, distance));
	            resultList.add(st);
		    }
	    }
	    catch (Exception e) {
	            Log.e(Config.LOGTAG, "DataStore.getAllStationFavoriteTransport(int distance) " + e.toString());
	    }
	    finally {
		    if(c != null) c.close();
	    }
	    return resultList;
	}
	
	
	//private ArrayList<Message> 
	private Cursor readAllMessages(TypeLanguage language){		
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.readAllMessages()");
		Cursor c=null;
		try{
			//Create new querybuilder
			SQLiteQueryBuilder _QB = new SQLiteQueryBuilder();
	
			//Specify Itinerary table AND add JOIN to Line table (use full_id for joining categories table)
			_QB.setTables(T_Message + " INNER JOIN " + T_Transport + " ON " + 
			        col_Transport_id + " = " + T_Message + "." + col_Message_FK_id_Transport);
			String [] columns=new String[]{col_Message_id, col_Message_FK_id_Transport,
					col_Message_DateEnd, col_Message_DateStart, col_Message_Message,
					col_Message_Description};
			String selection = col_Transport_FavouriteTransport +"=? AND "+ col_Message_Language + "=?";
			String[] where =  new String[] {String.valueOf(Config.TRUE), language.toString()};
			String orderBy = col_Message_DateStart + " ASC";	
			c = _QB.query(myDataBase, columns, selection, where, null, null, orderBy);
		}
	    catch (Exception e) {
	        Log.e(Config.LOGTAG, "DataStore.readAllMessages(language) " + e.toString());
	    }		
		return c;
	}
	
	// ArrayList<Message>
	protected ArrayList<Message> getAllMessages(TypeLanguage language) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.getAllMessage(language)");    		 	 	 		

		int myID;
		int myFK_id_Transport;
		TimeFormater tf = TimeFormater.getInstance();
		Calendar myDateEnd;
		Calendar myDateStart;
		String myMessage;
		String myDescription;
		TypeLanguage myLanguage;
		
		Message ms;
		
	    ArrayList<Message> resultList = new ArrayList<Message>();
	    // Function to retrieve all values from Message where Language = LanguageActive
	    Cursor c = readAllMessages(language);
	    try{
		    while (c.moveToNext())
		    {
		    	myID = c.getInt(c.getColumnIndex("_id"));
				myFK_id_Transport = c.getInt(c.getColumnIndex(col_Message_FK_id_Transport));
				myDateEnd = tf.getDate(c.getString(c.getColumnIndex(col_Message_DateEnd)));
				myDateStart = tf.getDate(c.getString(c.getColumnIndex(col_Message_DateStart)));
				myMessage = c.getString(c.getColumnIndex(col_Message_Message));
				myDescription = c.getString(c.getColumnIndex(col_Message_Description));
				myLanguage = language;				
		    	
		    	ms = new Message(myID, myFK_id_Transport, myDateEnd, myDateStart,
		    			myMessage, myDescription, myLanguage);
		    	resultList.add(ms);
	        }
	    }
	    catch (Exception e) {
	            Log.e(Config.LOGTAG, "DataStore.getAllMessages(Language) " + e.toString());
	    } finally {
		    if(c != null) c.close();
	    }
	    return resultList;
	}	
	
	//private ArrayList<Transport> 
	private Cursor readAllTransferIsFavourite(){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.readAllTransferIsFavourite()");
		Cursor c=null;
		try{
			//Create new querybuilder
			SQLiteQueryBuilder _QB = new SQLiteQueryBuilder();
	
			//Specify Itinerary table AND add JOIN to Line table (use full_id for joining categories table)
			_QB.setTables(T_Transfer + " INNER JOIN " + T_Itinerary + " INNER JOIN " + T_Transport +
			        " INNER JOIN " + V_FavouriteTransferSource + " ON " + 
			        col_Transport_id + " = " + col_Itinerary_FK_id_Transport + " AND " +
					col_Itinerary_id + " = " + col_Transfer_FK_id_Itinerary_Destination + " AND " + 
					col_Transfer_id + " = " + col_FavouriteTransferSource_id);
	
			String [] columns=new String[]{col_Transfer_id ,col_Transfer_FK_id_Itinerary_Destination, 
					col_Transfer_FK_id_Itinerary_Source, col_Transfer_FK_id_Station_Destination, 
					col_Transfer_FK_id_Station_Source, col_Transfer_Distance, col_Transfer_Time};
			String selection = col_Transport_FavouriteTransport + "=?";
			String[] where =  new String[] {String.valueOf(Config.TRUE)};
			
			c = _QB.query(myDataBase, columns, selection, where, null, null, null);
		}
	    catch (Exception e) {
	        Log.e(Config.LOGTAG, "DataStore.readAllTransferIsFavourite() " + e.toString());
	    }		
		return c;
	}

	protected ArrayList<Transfer> getAllTransfer() {		
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.getAllTransfer()");    		 	 	 		
		int myID;
		int myFK_id_Itinerary_Destination;
		int myFK_id_Itinerary_Source;
		int myFK_id_Station_Destination;
		int myFK_id_Station_Source;
		int myDistance;
		TimeFormater tf = TimeFormater.getInstance();
		Calendar myTime;

		Transfer tr;

	    ArrayList<Transfer> resultList = new ArrayList<Transfer>();

	    // Function to retrieve all values from Transfer
	    Cursor c = readAllTransferIsFavourite();
	    try {
			if (Config.DEBUG) Log.i(Config.LOGTAG, "DataStore.getAllTransfer()." + c.getCount());    		 	 	 		
		    while (c.moveToNext())
		    {
		    	// problems with the name of _id both of next instruction don't work properly
		    	//myID = c.getInt(c.getColumnIndex("_id"));
		    	//myID = c.getInt(c.getColumnIndex(col_Transfer_id));	
		    	myID = c.getInt(0);
		    	myFK_id_Itinerary_Destination = c.getInt(c.getColumnIndex(col_Transfer_FK_id_Itinerary_Destination));
		    	myFK_id_Itinerary_Source = c.getInt(c.getColumnIndex(col_Transfer_FK_id_Itinerary_Source));
		    	myFK_id_Station_Destination = c.getInt(c.getColumnIndex(col_Transfer_FK_id_Station_Destination));
		    	myFK_id_Station_Source = c.getInt(c.getColumnIndex(col_Transfer_FK_id_Station_Source));
		    	myDistance = c.getInt(c.getColumnIndex(col_Transfer_Distance));
		    	myTime = tf.getTime(c.getString(c.getColumnIndex(col_Transfer_Time)));
		    	
		    	tr = new Transfer(myID, myFK_id_Itinerary_Destination, myFK_id_Itinerary_Source, myFK_id_Station_Destination,
		    			myFK_id_Station_Source, myDistance, myTime);
		    	
	            resultList.add(tr);
	        }
	    }
	    catch (Exception e) {
	    	Log.e(Config.LOGTAG, "DataStore.getAllTransfer() " + e.toString());
	    } finally {		    
		    if(c != null) c.close();
		}
	    return resultList;	
	}

	//private FavouriteStation
	private Cursor readFavouriteStation(int idStation){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.readFavouriteStation(int idStation)");
		Cursor c=null;
		try{
			String [] columns=new String[]{"_id", col_FavouriteStation_Name};
			String selection = col_FavouriteStation_FK_id_Station + "=?";
			String[] where = new String[] {String.valueOf(idStation)};
			c = myDataBase.query(T_FavouriteStation, columns, selection, where, null, null, null);
		}
	    catch (Exception e) {
	        Log.e(Config.LOGTAG, "DataStore.readFavouriteStation(int idStation) " + e.toString());
	    }		
		return c;
	}
	
	//FavouriteStation 
	protected FavouriteStation getFavouriteStation(int idStation){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.getFavouriteStation(idStation)");
		int myID;
		// int myFK_id_Station;
		String myName;
		
		FavouriteStation fs = null;
	    // Function to retrieve all values from Itinerary
	    Cursor c = readFavouriteStation(idStation);
	    
        try {
        	if (c.moveToFirst()) {
        		myID = c.getInt(c.getColumnIndex("_id"));
        		// myFK_id_Station = idStation;
        		myName = c.getString(c.getColumnIndex(col_FavouriteStation_Name));
        	
        		fs = new FavouriteStation(myID, idStation, myName);
        	}
        }
        catch (Exception e) {
	        Log.e(Config.LOGTAG, "DataStore.getFavouriteStation(idStation)" + e.toString());
	    } finally {
	    	if(c != null) c.close();
	    }
	    return fs;
	}

	//IsFavouriteStation 
	protected boolean IsFavouriteStation(int idStation){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.isFavouriteStation(" + idStation + ")");
		
		boolean res= false;
		try {
			res = (readFavouriteStation(idStation).getCount()>0);
		}
	        catch (Exception e) {
	            Log.e(Config.LOGTAG, "DataStore.IsFavouriteStation(int idStation) " + e.toString());
	        }
		return res;
		
	}

	/*
	SELECT Station._id as _id, FK_id_Transport, Code, Name, Adapted_for_Disabled, Latitude, Longitude, TimeStamp, Transfer, 
		Velocity, Fk_id_TypeZone FROM Station INNER JOIN Transport ON FK_id_Transport = Transport._id WHERE 
		Transport.FK_id_typeTransport = 3  ORDER BY Station.Name ASC;
   */
	//private ArrayList<BikeStation> 
	// we get at PublicTransport level, not at the database
	private Cursor readAllBikeStationsByLine(int idLine){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.readAllBikeStationsByLine(int idLine)");    	
		Cursor c = null;
		try{
			if (getLineIsBikeLine(idLine)){
				//Create new querybuilder
				SQLiteQueryBuilder _QB = new SQLiteQueryBuilder();

				//Specify Station table AND add JOIN to Transport table (use full_id for joining categories table)
				_QB.setTables(T_Station + " INNER JOIN " + T_Transport);
				_QB.appendWhere(col_Station_FK_id_Transport + " = " + col_Transport_id);
				
				String [] _projectionIn=new String[]{col_Station_id + " AS _id",
						col_Station_FK_id_Transport, col_Station_Code, col_Station_Name,
						col_Station_Adapted_for_Disabled, col_Station_Latitude, col_Station_Longitude,
						col_Station_TimeStamp, col_Station_Transfer, col_Station_Velocity,
						col_Station_FK_id_TypeZone};
				
				String _selection = col_Transport_FK_id_TypeTransport + " =?";
				String[] _selectionArgs = new String[] {String.valueOf(TypeTransport.BICIPALMA.getValue())};
				String _sortOrder = col_Station_Name + " ASC";	
				
				String _groupBy = null;
				String _having = null;
				 
				//Get cursor
				c = _QB.query(myDataBase, _projectionIn, _selection, _selectionArgs, _groupBy, _having, _sortOrder);	
			}
		}
	    catch (Exception e) {
	        Log.e(Config.LOGTAG, "DataStore.readAllBikeStationsByLine(int idLine) " + e.toString());
	    }		
		return c;
	}

	protected ArrayList<Station> getAllBikeStationsByLine(int idLine, int distance) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.getAllBikeStationsByLine(int idLine)");    		 	 	 		

		// Station
		int myID;
		int myFK_id_Transport;
		String myCode;
		String myName;
		boolean myAdapted_for_Disabled;
		GeoPosition myGeoPosition;
		String myTimeStamp;
		boolean myTransfer; 
		int myVelocity;
		int myFK_id_Zone;
		
		boolean myFavourite;

		Station st;
	
		ArrayList<Station> resultList = new ArrayList<Station>();

	    // Function to retrieve all lines by idItinerary
	    Cursor c = readAllBikeStationsByLine(idLine);
	    try {
		    while (c.moveToNext())
		    {
	        	myID = c.getInt(c.getColumnIndex("_id"));
	    		myFK_id_Transport = c.getInt(c.getColumnIndex(col_Station_FK_id_Transport));
	    		myCode = c.getString(c.getColumnIndex(col_Station_Code));
	    		myName = c.getString(c.getColumnIndex(col_Station_Name));
	    		myAdapted_for_Disabled = getBoolean(c.getString(c.getColumnIndex(col_Station_Adapted_for_Disabled)));
	    		myGeoPosition = new GeoPosition(c.getInt(c.getColumnIndex(col_Station_Latitude)), 
	    				c.getInt(c.getColumnIndex(col_Station_Longitude)), Config.locationAccuracy);
    			myTimeStamp = c.getString(c.getColumnIndex(col_Station_TimeStamp));
    			myTransfer = getBoolean(c.getString(c.getColumnIndex(col_Station_Transfer))); 
    			myVelocity = c.getInt(c.getColumnIndex(col_Station_Velocity));
    			myFK_id_Zone = c.getInt(c.getColumnIndex(col_Station_FK_id_TypeZone));

	    		st = new Station(myID, myFK_id_Transport, myCode, myName, myAdapted_for_Disabled, 
	    				myGeoPosition, myTimeStamp, myTransfer, myVelocity, myFK_id_Zone);
	            // getting if Station is Favourite
	            myFavourite = IsFavouriteStation(myID);
	            st.setFavorite(myFavourite);
	            // List of transfers
	            st.setListTransfer(getAllTransferDistinctTypeOfTransportByIdStation(myID, idLine, distance));
	            
	            resultList.add(st);

	        }
	    }
        catch (Exception e) {
            Log.e(Config.LOGTAG, "DataStore.getAllBikeStationsByLine(int idLine) " + e.toString());
        } finally {
	    	if(c != null) c.close();
        }
	    return resultList;	
	}	

	/*
SELECT a._id as _id, FK_id_Transport, Code, Name, Adapted_for_Disabled, Latitude, Longitude, 
		TimeStamp, Transfer, Velocity, Fk_id_TypeZone, c._id as _idBike, Capacity, BikesAvailable, ParksFree 
		FROM Station AS a INNER JOIN Transport AS b INNER JOIN BikeStation AS c ON 
		a.FK_id_Transport = b._id AND a._id = c.FK_id_Station  WHERE b._id = 3  
		ORDER BY a.Name ASC;
   */
	//private ArrayList<BikeStation> 
	// we get at PublicTransport level, not at the database
	private Cursor readAllBikeStations(){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.readAllBikeStations(int idTransport)");
		//Only BiciPalma stations
		//TypeTransport.BICIPALMA.getValue() == idTransport
		Cursor c = null;
		try{
			
			//Create new querybuilder
			SQLiteQueryBuilder _QB = new SQLiteQueryBuilder();

			//Specify Station table AND add JOIN to Transport table (use full_id for joining categories table)
			_QB.setTables(T_Station + " INNER JOIN " + T_Transport + " INNER JOIN " + T_BikeStation);
			_QB.appendWhere(col_Station_FK_id_Transport + " = " + col_Transport_id + " AND " + 
					col_Station_id + " = " + col_BikeStation_FK_id_Station);
			
			String [] _projectionIn=new String[]{col_Station_id + " AS _id",
					col_Station_FK_id_Transport, col_Station_Code, col_Station_Name,
					col_Station_Adapted_for_Disabled, col_Station_Latitude, col_Station_Longitude,
					col_Station_TimeStamp, col_Station_Transfer, col_Station_Velocity,
					col_Station_FK_id_TypeZone, col_BikeStation_id + " AS _idBike", col_BikeStation_Capacity, 
					col_BikeStation_BikeAvailable, col_BikeStation_ParksFree};
			
			String _selection = col_Transport_id + " =?";
			String[] _selectionArgs = new String[] {String.valueOf(TypeTransport.BICIPALMA.getValue())};
			String _sortOrder = col_Station_Name + " ASC";	
			
			String _groupBy = null;
			String _having = null;
			 
			//Get cursor
			c = _QB.query(myDataBase, _projectionIn, _selection, _selectionArgs, _groupBy, _having, _sortOrder);	
		}
	    catch (Exception e) {
	        Log.e(Config.LOGTAG, "DataStore.readAllStationOW(idLine) " + e.toString());
	    }		
		return c;
	}

	// Virtual Line of BICIPALMA
	protected ArrayList<Station> getAllBikeStations(int idItinerary, int distance) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.getAllBikeStations()");    		 	 	 		

		// Station
		int myID;
		int myFK_id_Transport;
		String myCode;
		String myName;
		boolean myAdapted_for_Disabled;
		GeoPosition myGeoPosition;
		String myTimeStamp;
		boolean myTransfer; 
		int myVelocity;
		int myFK_id_Zone;
		// BikeStation
		int myIDbike;
		int myCapacity;
		int myBikesAvailable;
		int myParksFree;
		
		
		boolean myFavourite;

		Station st;
		BikeStation bs;
	
		ArrayList<Station> resultList = new ArrayList<Station>();

	    // Function to retrieve all lines by idItinerary
	    Cursor c = readAllBikeStations();
	    try {
		    while (c.moveToNext())
		    {
	        	myID = c.getInt(c.getColumnIndex("_id"));
	    		myFK_id_Transport = c.getInt(c.getColumnIndex(col_Station_FK_id_Transport));
	    		myCode = c.getString(c.getColumnIndex(col_Station_Code));
	    		myName = c.getString(c.getColumnIndex(col_Station_Name));
	    		myAdapted_for_Disabled = getBoolean(c.getString(c.getColumnIndex(col_Station_Adapted_for_Disabled)));
	    		myGeoPosition = new GeoPosition(c.getInt(c.getColumnIndex(col_Station_Latitude)), 
	    				c.getInt(c.getColumnIndex(col_Station_Longitude)), Config.locationAccuracy);
    			myTimeStamp = c.getString(c.getColumnIndex(col_Station_TimeStamp));
    			myTransfer = getBoolean(c.getString(c.getColumnIndex(col_Station_Transfer))); 
    			myVelocity = c.getInt(c.getColumnIndex(col_Station_Velocity));
    			myFK_id_Zone = c.getInt(c.getColumnIndex(col_Station_FK_id_TypeZone));

	    		st = new Station(myID, myFK_id_Transport, myCode, myName, myAdapted_for_Disabled, 
	    				myGeoPosition, myTimeStamp, myTransfer, myVelocity, myFK_id_Zone);
	            //retrieve Transfer from favorite type of transport from id_Itinerary
		    	st.setListTransfer(getAllTransferDistinctTypeOfTransportByIdStationItinerary(myID, idItinerary, distance));

	            // getting if Station is Favourite
	            myFavourite = IsFavouriteStation(myID);
	            st.setFavorite(myFavourite);
	            
	            myIDbike 			= c.getInt(c.getColumnIndex("_idBike"));
	    		myCapacity			= c.getInt(c.getColumnIndex(col_BikeStation_Capacity));
	    		myBikesAvailable	= c.getInt(c.getColumnIndex(col_BikeStation_BikeAvailable));
	    		myParksFree			= c.getInt(c.getColumnIndex(col_BikeStation_ParksFree));
	            
	            // create new BikeStation
	            bs = new BikeStation(myIDbike, myID, myCapacity, myBikesAvailable, myParksFree);

	            // add to Station
	            st.setBikeStation(bs);
	            // new Station
		    	resultList.add(st);
	        }
	    }
        catch (Exception e) {
            Log.e(Config.LOGTAG, "DataStore.getAllBikeStations " + e.toString());
        } finally {
	    	if(c != null) c.close();
        }
	    return resultList;	
	}	
	
	
/*
SELECT Station._id as _id, FK_id_Transport, Code, Name, Adapted_for_Disabled, 
Latitude, Longitude, TimeStamp, Transfer, Velocity, Fk_id_TypeZone FROM 
LineStation INNER JOIN Station ON LineStation.FK_id_Station = Station._id 
WHERE FK_id_Line = 1000002 AND RouteOneWay = 1 
ORDER BY LineStation.StationPrevious ASC;
 */
	//private ArrayList<LineStation> 
	// we get at PublicTransport level, not at the database
	private Cursor readAllStationOW(int idLine){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.readAllStationOW(idLine)");    	
		Cursor c = null;
		try{
			//Create new querybuilder
			SQLiteQueryBuilder _QB = new SQLiteQueryBuilder();

			//Specify Itinerary table AND add JOIN to Line table (use full_id for joining categories table)
			_QB.setTables(T_LineStation + " INNER JOIN " + T_Station);
			_QB.appendWhere(T_LineStation + "." + col_LineStation_FK_id_Station + " = " + col_Station_id);
			
			String [] _projectionIn=new String[]{col_Station_id + " AS _id",
					col_Station_FK_id_Transport, col_Station_Code, col_Station_Name,
					col_Station_Adapted_for_Disabled, col_Station_Latitude, col_Station_Longitude,
					col_Station_TimeStamp, col_Station_Transfer, col_Station_Velocity,
					col_Station_FK_id_TypeZone};
			
			String _selection = col_LineStation_FK_id_Line + " =? AND " + col_LineStation_RouteOneWay + " =?";
			String[] _selectionArgs = new String[] {String.valueOf(idLine), String.valueOf(Config.TRUE)};
			String _sortOrder = col_LineStation_StationPrevious + " ASC";	
			
			String _groupBy = null;
			String _having = null;
			 
			//Get cursor
			c = _QB.query(myDataBase, _projectionIn, _selection, _selectionArgs, _groupBy, _having, _sortOrder);			
		}
	    catch (Exception e) {
	        Log.e(Config.LOGTAG, "DataStore.readAllStationOW(idLine) " + e.toString());
	    }		
		return c;
	}

	protected ArrayList<Station> getAllStationOW(int idLine, int distance) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.getAllStationOW(idLine)");    		 	 	 		

		// Station
		int myID;
		int myFK_id_Transport;
		String myCode;
		String myName;
		boolean myAdapted_for_Disabled;
		GeoPosition myGeoPosition;
		String myTimeStamp;
		boolean myTransfer; 
		int myVelocity;
		int myFK_id_Zone;
		
		boolean myFavourite;

		Station st;
	
		ArrayList<Station> resultList = new ArrayList<Station>();

	    // Function to retrieve all lines by idItinerary
	    Cursor c = readAllStationOW(idLine);
	    try {
		    while (c.moveToNext())
		    {
	        	myID = c.getInt(c.getColumnIndex("_id"));
	    		myFK_id_Transport = c.getInt(c.getColumnIndex(col_Station_FK_id_Transport));
	    		myCode = c.getString(c.getColumnIndex(col_Station_Code));
	    		myName = c.getString(c.getColumnIndex(col_Station_Name));
	    		myAdapted_for_Disabled = getBoolean(c.getString(c.getColumnIndex(col_Station_Adapted_for_Disabled)));
	    		myGeoPosition = new GeoPosition(c.getInt(c.getColumnIndex(col_Station_Latitude)), 
	    				c.getInt(c.getColumnIndex(col_Station_Longitude)), Config.locationAccuracy);
    			myTimeStamp = c.getString(c.getColumnIndex(col_Station_TimeStamp));
    			myTransfer = getBoolean(c.getString(c.getColumnIndex(col_Station_Transfer))); 
    			myVelocity = c.getInt(c.getColumnIndex(col_Station_Velocity));
    			myFK_id_Zone = c.getInt(c.getColumnIndex(col_Station_FK_id_TypeZone));

	    		st = new Station(myID, myFK_id_Transport, myCode, myName, myAdapted_for_Disabled, 
	    				myGeoPosition, myTimeStamp, myTransfer, myVelocity, myFK_id_Zone);
	            // getting if Station is Favourite
	            myFavourite = IsFavouriteStation(myID);
	            st.setFavorite(myFavourite);
	            //retrieve Transfer from favorite type of transport from id_Itinerary
		    	st.setListTransfer(getAllTransferDistinctTypeOfTransportByIdStation(myID, idLine, distance));

		    	// new Station
	            resultList.add(st);
	        }
	    }
        catch (Exception e) {
            Log.e(Config.LOGTAG, "DataStore.getAllStationOW(idLine) " + e.toString());
        } finally {
		    if(c != null) c.close();
        }
	    return resultList;	
	}
	
	// Route Return of the Line in Circle Line is EMPTY ONCE	
	// private ArrayList<LineStation> 
	//SELECT * from LineStation where FK_id_Line=1 AND RouteReturn=1 ORDER BY StationPrevious ASC;
	// Time to arrive to station ON the route Return ONCE
	// private ArrayList<Calendar> AddedTimeRT;
	// we get at PublicTransport level, not at the database
	private Cursor readAllStationRT(int idLine){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.readAllStationRT(int idLine)");    	
		Cursor c = null;
		try{
			//Create new querybuilder
			SQLiteQueryBuilder _QB = new SQLiteQueryBuilder();

			//Specify Itinerary table AND add JOIN to Line table (use full_id for joining categories table)
			_QB.setTables(T_LineStation + " INNER JOIN " + T_Station);
			_QB.appendWhere(T_LineStation + "." + col_LineStation_FK_id_Station + " = " + col_Station_id);

			String [] _projectionIn=new String[]{col_Station_id + " AS _id",
					col_Station_FK_id_Transport, col_Station_Code, col_Station_Name,
					col_Station_Adapted_for_Disabled, col_Station_Latitude, col_Station_Longitude,
					col_Station_TimeStamp, col_Station_Transfer, col_Station_Velocity,
					col_Station_FK_id_TypeZone};
			
			String _selection = col_LineStation_FK_id_Line + " =? AND " + col_LineStation_RouteReturn + " =?";
			String[] _selectionArgs = new String[] {String.valueOf(idLine), String.valueOf(Config.TRUE)};
			String _sortOrder = col_LineStation_StationPrevious + " ASC";	
			
			String _groupBy = null;
			String _having = null;
			 
			//Get cursor
			c = _QB.query(myDataBase, _projectionIn, _selection, _selectionArgs, _groupBy, _having, _sortOrder);
			//query(SQLiteDatabase db, String[] projectionIn, String selection, String[] selectionArgs, String groupBy, String having, String sortOrder, String limit)
		}
	    catch (Exception e) {
	        Log.e(Config.LOGTAG, "DataStore.readAllStationOW(idLine) " + e.toString());
	    }		
		return c;
	}

	protected ArrayList<Station> getAllStationRT(int idLine, int distance) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.getAllStationRT(idLine)");    		 	 	 		
		// Station
		int myID;
		int myFK_id_Transport;
		String myCode;
		String myName;
		boolean myAdapted_for_Disabled;
		GeoPosition myGeoPosition;
		String myTimeStamp;
		boolean myTransfer; 
		int myVelocity;
		int myFK_id_Zone;
		
		boolean myFavourite;

		Station st;

		ArrayList<Station> resultList = new ArrayList<Station>();

	    // Function to retrieve all lines by idItinerary
	    Cursor c = readAllStationRT(idLine);
	    try {
		    while (c.moveToNext())
		    {
	        	myID = c.getInt(c.getColumnIndex("_id"));
	    		myFK_id_Transport = c.getInt(c.getColumnIndex(col_Station_FK_id_Transport));
	    		myCode = c.getString(c.getColumnIndex(col_Station_Code));
	    		myName = c.getString(c.getColumnIndex(col_Station_Name));
	    		myAdapted_for_Disabled = getBoolean(c.getString(c.getColumnIndex(col_Station_Adapted_for_Disabled)));
	    		myGeoPosition = new GeoPosition(c.getInt(c.getColumnIndex(col_Station_Latitude)), 
	    				c.getInt(c.getColumnIndex(col_Station_Longitude)), Config.locationAccuracy);
    			myTimeStamp = c.getString(c.getColumnIndex(col_Station_TimeStamp));
    			myTransfer = getBoolean(c.getString(c.getColumnIndex(col_Station_Transfer))); 
    			myVelocity = c.getInt(c.getColumnIndex(col_Station_Velocity));
    			myFK_id_Zone = c.getInt(c.getColumnIndex(col_Station_FK_id_TypeZone));

	    		st = new Station(myID, myFK_id_Transport, myCode, myName, myAdapted_for_Disabled, 
	    				myGeoPosition, myTimeStamp, myTransfer, myVelocity, myFK_id_Zone);
	            // getting if Station is Favourite
	            myFavourite = IsFavouriteStation(myID);
	            st.setFavorite(myFavourite);
	            //retrieve Transfer from favorite type of transport from id_Itinerary
		    	st.setListTransfer(getAllTransferDistinctTypeOfTransportByIdStation(myID, idLine, distance));

		    	// new Station
	            resultList.add(st);
	        }
	    }
        catch (Exception e) {
            Log.e(Config.LOGTAG, "DataStore.getAllStationOW(idLine) " + e.toString());
        } finally {
        	if(c != null) c.close();
        }
	    return resultList;	
	}
		
	// private ArrayList<TimeTable> TimeTables;
	private Cursor readAllTimeTable(int idLine){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.readAllTimeTable(idLine)");    		 	 	 		
		Cursor c=null;
		try{

			String [] columns=new String[]{"_id", col_TimeTable_FK_id_Station_Start,
					col_TimeTable_FK_id_Station_End, col_TimeTable_Monday, col_TimeTable_Tuesday,
					col_TimeTable_Wednesday, col_TimeTable_Thursday, col_TimeTable_Friday, col_TimeTable_Saturday,
					col_TimeTable_Sunday_Holiday, col_TimeTable_Time, col_TimeTable_LastAssignedLine};
			String selection = col_TimeTable_FK_id_Line+"=?";
			String[] where = new String[] {String.valueOf(idLine)};
			String orderBy = col_TimeTable_FK_id_Station_Start + " ASC " + col_TimeTable_FK_id_Station_End + " ASC ";
			c = myDataBase.query(T_TimeTable, columns, selection, where,null, null, orderBy);
		}
	    catch (Exception e) {
	        Log.e(Config.LOGTAG, "DataStore.readAllTimeTable(idLine)" + e.toString());
	    }		
		return c;
	}

	protected ArrayList<TimeTable> getAllTimeTable(int idLine) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.getAllTimeTable(idLine) ");    		 	 	 		

		int myID;
		int myFK_id_Station_Start;
		int myFK_id_Station_End;
		boolean myMonday;
		boolean myTuesday;
		boolean myWednesday;
		boolean myThursday;
		boolean myFriday;
		boolean mySaturday;
		boolean mySunday_Holiday;
		TimeFormater tf= TimeFormater.getInstance();
		Calendar myTime;
		int myLastAssignedLine;
	
		TimeTable tt;

		ArrayList<TimeTable> resultList = new ArrayList<TimeTable>();

	    // Function to retrieve all lines by idItinerary
	    Cursor c = readAllTimeTable(idLine);
	    try {
		    while (c.moveToNext())
		    {
	        	myID = c.getInt(c.getColumnIndex("_id"));
	        	myFK_id_Station_Start = c.getInt(c.getColumnIndex(col_TimeTable_FK_id_Station_Start));
	        	myFK_id_Station_End = c.getInt(c.getColumnIndex(col_TimeTable_FK_id_Station_End));
	    		myMonday = getBoolean(c.getString(c.getColumnIndex(col_TimeTable_Monday)));
	    		myTuesday = getBoolean(c.getString(c.getColumnIndex(col_TimeTable_Tuesday)));
	    		myWednesday = getBoolean(c.getString(c.getColumnIndex(col_TimeTable_Wednesday)));
	    		myThursday = getBoolean(c.getString(c.getColumnIndex(col_TimeTable_Thursday)));
	    		myFriday = getBoolean(c.getString(c.getColumnIndex(col_TimeTable_Friday)));
	    		mySaturday = getBoolean(c.getString(c.getColumnIndex(col_TimeTable_Saturday)));
	    		mySunday_Holiday = getBoolean(c.getString(c.getColumnIndex(col_TimeTable_Sunday_Holiday)));
		    	myTime = tf.getTime(c.getString(c.getColumnIndex(col_TimeTable_Time)));
	    		myLastAssignedLine = c.getInt(c.getColumnIndex(col_TimeTable_LastAssignedLine));
	        	
		    	tt = new TimeTable(myID, idLine, myFK_id_Station_Start, myFK_id_Station_End,
		    			myMonday, myTuesday, myWednesday, myThursday, myFriday, mySaturday,
		    			mySunday_Holiday, myTime, myLastAssignedLine);
	            resultList.add(tt);
	        }
	    }
	    catch (Exception e) {
	        Log.e(Config.LOGTAG, "DataStore.getAllTimeTable(idLine)  " + e.toString());
	    } finally {
		    if(c != null) c.close();
	    }
	    return resultList;	
	}
			
	// private ArrayList<Frequency>
	private Cursor readAllFrequency(int idLine){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.readAllFrequency(idLine)");    		 	 	 		
		Cursor c=null;
		try{
		
			String [] columns=new String[]{"_id", col_Frequency_StartTime,
					col_Frequency_EndTime, col_Frequency_Monday, col_Frequency_Tuesday,
					col_Frequency_Wednesday, col_Frequency_Thursday, col_Frequency_Friday, col_Frequency_Saturday,
					col_Frequency_Sunday_Holiday};
			String selection = col_Frequency_FK_id_Line+"=?";
			String[] where = new String[] {String.valueOf(idLine)};
			String orderBy = col_Frequency_StartTime + " ASC";
			c = myDataBase.query(T_Frequency, columns, selection, where, null, null, orderBy);
		}
	    catch (Exception e) {
	        Log.e(Config.LOGTAG, "DataStore.readAllFrequency(int idLine) " + e.toString());
	    }		
		return c;
	}

	//private ArrayList<Frequency>
	protected ArrayList<Frequency> getAllFrequency(int idLine) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.getAllFrequency(idLine)");    		 	 	 		

		int myID;
		TimeFormater tf= TimeFormater.getInstance();
		Calendar myStartTime;
		Calendar myEndTime;
		Calendar myMonday;
		Calendar myTuesday;
		Calendar myWednesday;
		Calendar myThursday;
		Calendar myFriday;
		Calendar mySaturday;
		Calendar mySunday_Holiday;
	
		Frequency fr;

		ArrayList<Frequency> resultList = new ArrayList<Frequency>();

	    // Function to retrieve all the frequencies by idLine
	    Cursor c = readAllFrequency(idLine);
	    try {
		    while (c.moveToNext())
		    {
	        	myID = c.getInt(c.getColumnIndex("_id"));
		    	myStartTime = tf.getTime(c.getString(c.getColumnIndex(col_Frequency_StartTime)));
		    	myEndTime = tf.getTime(c.getString(c.getColumnIndex(col_Frequency_EndTime)));
	    		myMonday = tf.getTime(c.getString(c.getColumnIndex(col_Frequency_Monday)));
	    		myTuesday = tf.getTime(c.getString(c.getColumnIndex(col_Frequency_Tuesday)));
	    		myWednesday = tf.getTime(c.getString(c.getColumnIndex(col_Frequency_Wednesday)));
	    		myThursday = tf.getTime(c.getString(c.getColumnIndex(col_Frequency_Thursday)));
	    		myFriday = tf.getTime(c.getString(c.getColumnIndex(col_Frequency_Friday)));
	    		mySaturday = tf.getTime(c.getString(c.getColumnIndex(col_Frequency_Saturday)));
	    		mySunday_Holiday = tf.getTime(c.getString(c.getColumnIndex(col_Frequency_Sunday_Holiday)));
	        	
		    	fr = new Frequency(myID, idLine, myStartTime, myEndTime,
		    			myMonday, myTuesday, myWednesday, myThursday, myFriday, mySaturday,
		    			mySunday_Holiday);
	            resultList.add(fr);
	        }
	    }
	    catch (Exception e) {    
	    	Log.e(Config.LOGTAG, "DataStore.getAllFrequency(idLine) " + e.toString());
	    } finally {    
	    	if(c != null) c.close();
	    }
	    return resultList;	
	}

	//private ArrayList<Integer> 
	private Cursor readTransferBetweenA_B(int idStationA, int idStationB){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.readTransferBetweenA_B(idStationA, idStationB)");    		 	 	 		
		Cursor c=null;
		try{
			String [] columns=new String[]{"_id"};
			String selection = col_Transfer_FK_id_Station_Destination+"=?"+col_Transfer_FK_id_Station_Source+"=?";
			String[] where = new String[] {String.valueOf(idStationA),String.valueOf(idStationB)};
			c = myDataBase.query(T_Transfer, columns, selection, where, null, null, null);
		}
	    catch (Exception e) {
	        Log.e(Config.LOGTAG, "DataStore.readTransferBetweenA_B(idStationA, idStationB) " + e.toString());
	    }		
		return c;
	}	
	
	protected boolean isTranferBetweenA_B(int idStationA, int idStationB) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.isTranferBetweenA_B( idStationA, idStationB)");   
		boolean resul = false;
		
	    // Function to retrieve connection betwenn Station A AND Station B
	    Cursor c = readTransferBetweenA_B(idStationA, idStationB);
	    try {
		    //there is a transfer between A_B
		    if (c != null) {
		    	resul = true;
		    	return resul;
		    }
	    }
		catch (Exception e) {    
		    	Log.e(Config.LOGTAG, "DataStore.isTranferBetweenA_B( idStationA, idStationB) " + e.toString());
		} finally {
			if(c != null) c.close();
		}
		return false;
	}

	//private ArrayList<Itinerary>
	private Cursor readItineraryFavourite(int idItinerary){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.readItineraryFavourite(idItinerary)");  
		Cursor c=null;
		try{
		
			//Create new querybuilder
			SQLiteQueryBuilder _QB = new SQLiteQueryBuilder();
			
			//Specify Itinerary table AND add JOIN to Line table (use full_id for joining categories table)
			_QB.setTables(T_Line + " INNER JOIN " + T_FavouriteLine + " ON " + 
			        col_Line_id + " = " + col_FavouriteLine_FK_id_Line);
			         
			//Get cursor
			c = _QB.query(myDataBase, null, null, null, null, null, null);		
		}
	    catch (Exception e) {
	        Log.e(Config.LOGTAG, "DataStore.readItineraryFavourite(idItinerary) " + e.toString());
	    }		
		return c;
	}

	//FavouriteLine 
	protected boolean IsFavouriteItinerary(int idItinerary){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.IsFavouriteItinerary(idItinerary)");
		boolean res= false;
	    Cursor c = readItineraryFavourite(idItinerary);
		try {
			res = (c.getCount()>0);
		}
	    catch (Exception e) {
	        Log.e(Config.LOGTAG, "DataStore.IsFavouriteItinerary(idItinerary) " + e.toString());
	    } finally {		    
	    	if(c != null) c.close();
	    }
	    return res;			
	}
	
	/** 
	 * Execute sql update command. 
	 * 
	 * @param values 
	 *            The parameters of update command. 
	 * @param whereClause 
	 *            The parameters of update command. 
	 * @param whereArgs 
	 *            The parameters of update command. 
	 * @return The number of affected records. 
	 */ 
	
	//private int num Rows afected 
	private int writeIsFavoriteTransport(int transport_id, boolean isFavorite){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.writeIsFavoriteTransport(Transport myTransport)");
		int res;
		//Changing data
		String table = T_Transport;
		ContentValues value = new ContentValues();
			value.put(col_Transport_FavouriteTransport, booleanToString(isFavorite));
		String whereClause = "_id = ?";
		String[] whereArgs = new String[]{String.valueOf(transport_id)};
		
		try{ 
			//database operation
			res = myDataBase.update(table, value, whereClause, whereArgs);
		}
	    catch (Exception e) {
            Log.e(Config.LOGTAG, "DataStore.setIsFavoriteTransport(Transport myTransport) " + e.toString());
            res = -1;
        }
		return res;
	}
		
	protected boolean setIsFavoriteTransport(Transport myTransport, boolean isFavorite) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.setIsFavoriteTransport(Transport myTransport)");
		
		return (writeIsFavoriteTransport(myTransport.get_id(), isFavorite) == 1);
	}
	
	
	//private ArrayList<Transport> 
	private long writeIsFavoriteItinerary(int itinerary_id, boolean isFavorite){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.writeIsFavoriteItinerary(int itinerary_id, String name_alternative)");
		long res;
		String table = T_Itinerary;
		ContentValues values = new ContentValues();
		values.put(col_Itinerary_FavoriteItinerary, booleanToString(isFavorite));
		String whereClause = "_id=?";
		String[] whereArgs = new String[]{String.valueOf(itinerary_id)};
		
		try{ 
			//database operation
			res = myDataBase.update(table, values, whereClause, whereArgs);
		}
	    catch (Exception e) {
	        Log.e(Config.LOGTAG, "DataStore.writeIsFavoriteItinerary(int itinerary_id, boolean isFavorite) " + e.toString());
	        res = -1;
	    }
		return res;
	}
	
	protected boolean setIsFavoriteItinerary(Itinerary myItinerary, boolean isFavorite) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore. setIsFavoriteItinerary(int itinerary_id, boolean isFavorite)");
		
		return (writeIsFavoriteItinerary(myItinerary.get_id(), isFavorite) == 1);
	}

	//private ArrayList<Transport> 
	private long writeIsFavoriteLine(int line_id, boolean isFavorite){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.writeIsFavoriteLine(int line_id, boolean isFavorite)");
		long res;
		if (isFavorite){
			//insert
			ContentValues value = new ContentValues();
			value.put(col_FavouriteLine_FK_id_Line, line_id);			
			String nullColumnHack = null;
			
			try{ 
				//database operation			
				res = myDataBase.insert(T_FavouriteLine, nullColumnHack, value);
			} catch (Exception e) {
		        Log.e(Config.LOGTAG, "DataStore.writeIsFavoriteLine(int line_id, boolean isFavorite, String name).insert " + e.toString());
		        res = -1;
		    }			
		} else {
			//delete
			String whereClause = col_FavouriteLine_FK_id_Line + " = ?";
			String[] whereArgs ={ String.valueOf(line_id) };
			try{ 
				//database operation			
				res = myDataBase.delete(T_FavouriteLine, whereClause, whereArgs);
			} catch (Exception e) {
		        Log.e(Config.LOGTAG, "DataStore.writeIsFavoriteLine(int line_id, boolean isFavorite, String name).delete " + e.toString());
		        res = -1;
		    }
		}
		return res;
	}
	

	protected boolean setIsFavoriteLine(Line myLine, boolean isFavorite) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore. setIsFavoriteLine(Line myLine, boolean isFavorite)");
		
		return (writeIsFavoriteLine(myLine.get_id(), isFavorite) > 0);
	}

	//private ArrayList<Station> 
	private long writeIsFavoriteStation(int station_id, boolean isFavorite){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.writeIsFavoriteLine(int line_id, boolean isFavorite)");
		long res;
		if (isFavorite){
			//insert
			ContentValues value = new ContentValues();
			value.put(col_FavouriteStation_FK_id_Station, station_id);			
			String nullColumnHack = null;
			
			try{ 
				//database operation			
				res = myDataBase.insert(T_FavouriteStation, nullColumnHack, value);
			} catch (Exception e) {
		        Log.e(Config.LOGTAG, "DataStore.writeIsFavoriteStation(int station_id, boolean isFavorite).insert " + e.toString());
		        res = -1;
		    }
				
		} else {
			//delete
			String whereClause = col_FavouriteStation_FK_id_Station + " = ?";
			String[] whereArgs ={ String.valueOf(station_id) };
			try{ 
				//database operation			
				res = myDataBase.delete(T_FavouriteStation, whereClause, whereArgs);
			} catch (Exception e) {
		        Log.e(Config.LOGTAG, "DataStore.writeIsFavoriteStation(int station_id, boolean isFavorite).delete " + e.toString());
		        res = -1;
		    }
		}
		return res;
	}
	
	protected boolean setIsFavoriteStation(Station myStation, boolean isFavorite) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore. setIsFavoriteStation(Station myStation, boolean isFavorite)");
		
		return (writeIsFavoriteStation(myStation.get_id(), isFavorite) > 0);
	}

	
/*	
 SELECT distinct d.FK_id_TypeTransport AS FK_id_TypeTransport, c.Code AS Code  
 FROM Itinerary AS a INNER JOIN Transfer AS b INNER JOIN Itinerary AS c INNER JOIN 
 Transport AS d INNER JOIN Transport AS e ON a._id = b.FK_id_Itinerary_Source 
 AND b.FK_id_Itinerary_Destination = c._id AND c.FK_id_Transport = d._id
 AND a.FK_id_Transport = e._id WHERE a._id = 1000001 AND b.Distance <=0 ORDER BY 
 d.FK_id_TypeTransport DESC, c._id ASC;
 
 TypeTransport i Code
*/	
	
	private Cursor readAllTransferDistinctTypeOfTransportByIdItinerary(int idItinerary, int distance){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.readAllTransferDistinctTypeOfTransportByIdItinerary(int idItinerary)");
		String[] selectionArgs;
		String sql;
		
		Cursor c=null;
		try{
			if (distance < 0){
				selectionArgs= new String[]{String.valueOf(idItinerary)};
				sql = " SELECT distinct d.FK_id_TypeTransport AS FK_id_TypeTransport, c.Code AS Code " +  
							"FROM Itinerary AS a INNER JOIN Transfer AS b INNER JOIN Itinerary AS c INNER JOIN " + 
							"Transport AS d INNER JOIN Transport AS e ON a._id = b.FK_id_Itinerary_Source " + 
							"AND b.FK_id_Itinerary_Destination = c._id AND c.FK_id_Transport = d._id " +
							"AND a.FK_id_Transport = e._id WHERE a._id = ? ORDER BY " + 
							"d.FK_id_TypeTransport DESC, c._id ASC;";			
			} else {
				selectionArgs= new String[]{String.valueOf(idItinerary), String.valueOf(distance)};
				sql = " SELECT distinct d.FK_id_TypeTransport AS FK_id_TypeTransport, c.Code AS Code " +  
						"FROM Itinerary AS a INNER JOIN Transfer AS b INNER JOIN Itinerary AS c INNER JOIN " + 
						"Transport AS d INNER JOIN Transport AS e ON a._id = b.FK_id_Itinerary_Source " + 
						"AND b.FK_id_Itinerary_Destination = c._id AND c.FK_id_Transport = d._id " +
						"AND a.FK_id_Transport = e._id WHERE a._id = ? AND b.Distance <= ? ORDER BY " + 
						"d.FK_id_TypeTransport DESC, c._id ASC;";			
			}
			c= myDataBase.rawQuery(sql, selectionArgs);
		}
	    catch (Exception e) {
	        Log.e(Config.LOGTAG, "DataStore.readAllTransferFavouriteTransportByIdItinerary(int idItinerary)" + e.toString());
	    }		
		return c;
	}
	
	protected ArrayList<ItineraryTypeTransport> getAllTransferDistinctTypeOfTransportByIdItinerary(int idItinerary, int distance) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.getAllTransferDistinctTypeOfTransportByIdItinerary(int idItinerary, int distance)  ");    		 	 	 		
		String code;
		int fK_id_TypeTransport;

		ArrayList<ItineraryTypeTransport> resultList = new ArrayList<ItineraryTypeTransport>();

	    // Function to retrieve all transferables from idItinerary where transport are favortites
	    Cursor c = readAllTransferDistinctTypeOfTransportByIdItinerary(idItinerary, distance);
	    try {
		    while (c.moveToNext()){
		    	code = c.getString(c.getColumnIndex(col_Itinerary_Code));
		    	fK_id_TypeTransport = c.getInt(c.getColumnIndex(col_Transport_FK_id_TypeTransport));
		    	ItineraryTypeTransport it = new ItineraryTypeTransport(code, fK_id_TypeTransport);
	            resultList.add(it);
	        }
	    }
	    catch (Exception e) {
	        Log.e(Config.LOGTAG, "DataStore.getAllTransferDistinctTypeOfTransportByIdItinerary(int idItinerary, int distance) " + e.toString());
	    } finally {    
		   	if(c != null) c.close();
		}
	    return resultList;	
	}

	/*	
	 
	 SELECT distinct d.FK_id_TypeTransport AS FK_id_TypeTransport, c.Code AS Code  
		 FROM Itinerary AS a INNER JOIN Transfer AS b INNER JOIN Itinerary AS c INNER JOIN 
		 Transport AS d INNER JOIN Transport AS e INNER JOIN Line AS f INNER JOIN LineStation AS g
	     ON a._id = b.FK_id_Itinerary_Source AND b.FK_id_Itinerary_Destination = c._id 
	     AND c.FK_id_Transport = d._id AND a.FK_id_Transport = e._id AND a._id = f.FK_id_Itinerary 
	     AND f._id = g.FK_id_Line AND g.FK_id_Station = b.FK_id_Station_Source
	     WHERE d.FavouriteTransport = 1 AND e.FavouriteTransport = 1 AND f._id = 1000053 
		 AND b.Distance <= 200 ORDER BY d.FK_id_TypeTransport DESC, c._id ASC;	 
	 
	 TypeTransport i Code
	*/	

	private Cursor readAllTransferDistinctTypeOfTransportByIdLine(int idLine, int distance){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.readAllTransferDistinctTypeOfTransportByIdLine(int idLine)");
		String[] selectionArgs;
		String sql;
		
		Cursor c=null;
		try{
			if (distance < 0){
				selectionArgs= new String[]{String.valueOf(idLine)};
				sql = " SELECT distinct d.FK_id_TypeTransport AS FK_id_TypeTransport, c.Code AS Code " +
							 "FROM Itinerary AS a INNER JOIN Transfer AS b INNER JOIN Itinerary AS c INNER JOIN " +
							 "Transport AS d INNER JOIN Transport AS e INNER JOIN Line AS f INNER JOIN LineStation AS g " +
						     "ON a._id = b.FK_id_Itinerary_Source AND b.FK_id_Itinerary_Destination = c._id " +
						     "AND c.FK_id_Transport = d._id AND a.FK_id_Transport = e._id AND a._id = f.FK_id_Itinerary " + 
						     "AND f._id = g.FK_id_Line AND g.FK_id_Station = b.FK_id_Station_Source " +
						     "WHERE d.FavouriteTransport = 1 AND e.FavouriteTransport = 1 AND f._id = ? " + 
							 "ORDER BY d.FK_id_TypeTransport DESC, c._id ASC;";
			} else {
				selectionArgs= new String[]{String.valueOf(idLine), String.valueOf(distance)};
				sql = " SELECT distinct d.FK_id_TypeTransport AS FK_id_TypeTransport, c.Code AS Code " +
						 "FROM Itinerary AS a INNER JOIN Transfer AS b INNER JOIN Itinerary AS c INNER JOIN " +
						 "Transport AS d INNER JOIN Transport AS e INNER JOIN Line AS f INNER JOIN LineStation AS g " +
					     "ON a._id = b.FK_id_Itinerary_Source AND b.FK_id_Itinerary_Destination = c._id " +
					     "AND c.FK_id_Transport = d._id AND a.FK_id_Transport = e._id AND a._id = f.FK_id_Itinerary " + 
					     "AND f._id = g.FK_id_Line AND g.FK_id_Station = b.FK_id_Station_Source " +
					     "WHERE d.FavouriteTransport = 1 AND e.FavouriteTransport = 1 AND f._id = ? " + 
						 "AND b.Distance <= ? ORDER BY d.FK_id_TypeTransport DESC, c._id ASC;";
			}
			c= myDataBase.rawQuery(sql, selectionArgs);
		}
	    catch (Exception e) {
	        Log.e(Config.LOGTAG, "DataStore.readAllTransferDistinctTypeOfTransportByIdLine(int idItinerary)" + e.toString());
	    }		
		return c;
	}
	
	protected ArrayList<ItineraryTypeTransport> getAllTransferDistinctTypeOfTransportByIdLine(int idLine, int distance) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.getAllTransferDistinctTypeOfTransportByIdLine(int idLine, int distance)  ");    		 	 	 		
		String code;
		int fK_id_TypeTransport;

		ArrayList<ItineraryTypeTransport> resultList = new ArrayList<ItineraryTypeTransport>();

	    // Function to retrieve all transferables from idItinerary where transport are favortites
	    Cursor c = readAllTransferDistinctTypeOfTransportByIdLine(idLine, distance);
	    try {
		    while (c.moveToNext()){
		    	code = c.getString(c.getColumnIndex(col_Itinerary_Code));
		    	fK_id_TypeTransport = c.getInt(c.getColumnIndex(col_Transport_FK_id_TypeTransport));
		    	ItineraryTypeTransport it = new ItineraryTypeTransport(code, fK_id_TypeTransport);
	            resultList.add(it);
	        }
	    }
	    catch (Exception e) {
	        Log.e(Config.LOGTAG, "DataStore.getAllTransferDistinctTypeOfTransportByIdLine(int idLine, int distance) " + e.toString());
	    } finally {
		    if(c != null) c.close();
	    }
	    return resultList;	
	}
		
/*
             
SELECT distinct d.FK_id_TypeTransport AS FK_id_TypeTransport, c.Code AS Code
		 FROM Itinerary AS a INNER JOIN Transfer AS b INNER JOIN Itinerary AS c 
		 INNER JOIN Transport AS d INNER JOIN Transport AS e INNER JOIN Line AS f 
		 INNER JOIN LineStation AS g INNER JOIN Station AS h
	     ON a._id = b.FK_id_Itinerary_Source AND b.FK_id_Itinerary_Destination = c._id 
	     AND c.FK_id_Transport = d._id AND a.FK_id_Transport = e._id 
	     AND a._id = f.FK_id_Itinerary AND f._id = g.FK_id_Line 
	     AND g.FK_id_Station = b.FK_id_Station_Source AND h._id = g.FK_id_Station
	     WHERE d.FavouriteTransport = 1 AND e.FavouriteTransport = 1 
	     AND h._id = 1000466 AND f._id = 1000028
	     ORDER BY d.FK_id_TypeTransport DESC, c._id ASC;
		 
SELECT distinct d.FK_id_TypeTransport AS FK_id_TypeTransport, c.Code AS Code
		 FROM Itinerary AS a INNER JOIN Transfer AS b INNER JOIN Itinerary AS c 
		 INNER JOIN Transport AS d INNER JOIN Transport AS e INNER JOIN Line AS f 
		 INNER JOIN LineStation AS g INNER JOIN Station AS h
	     ON a._id = b.FK_id_Itinerary_Source AND b.FK_id_Itinerary_Destination = c._id 
	     AND c.FK_id_Transport = d._id AND a.FK_id_Transport = e._id 
	     AND a._id = f.FK_id_Itinerary AND f._id = g.FK_id_Line 
	     AND g.FK_id_Station = b.FK_id_Station_Source AND h._id = g.FK_id_Station
	     WHERE d.FavouriteTransport = 1 AND e.FavouriteTransport = 1 
	     AND h._id = 1000466 AND f._id = 1000028 AND b.Distance <= 200
	     ORDER BY d.FK_id_TypeTransport DESC, c._id ASC;
	     		 
		 TypeTransport i Code
		*/	

	private Cursor readAllTransferDistinctTypeOfTransportByIdStation(int idStation, int idLine, int distance){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.readAllTransferDistinctTypeOfTransportByIdStation(int idStation, int idItinerary, int distance)");
		String[] selectionArgs;
		String sql;
		
		Cursor c=null;
		try{
			if (distance < 0){
				selectionArgs= new String[]{String.valueOf(idStation), String.valueOf(idLine)};
				sql = "SELECT distinct d.FK_id_TypeTransport AS FK_id_TypeTransport, c.Code AS Code " +
						 "FROM Itinerary AS a INNER JOIN Transfer AS b INNER JOIN Itinerary AS c " +
						 "INNER JOIN Transport AS d INNER JOIN Transport AS e INNER JOIN Line AS f " + 
						 "INNER JOIN LineStation AS g INNER JOIN Station AS h " +
					     "ON a._id = b.FK_id_Itinerary_Source AND b.FK_id_Itinerary_Destination = c._id " + 
					     "AND c.FK_id_Transport = d._id AND a.FK_id_Transport = e._id " + 
					     "AND a._id = f.FK_id_Itinerary AND f._id = g.FK_id_Line " +
					     "AND g.FK_id_Station = b.FK_id_Station_Source AND h._id = g.FK_id_Station " +
					     "WHERE d.FavouriteTransport = 1 AND e.FavouriteTransport = 1 " + 
					     "AND h._id = ? AND f._id = ? " +
					     "ORDER BY d.FK_id_TypeTransport DESC, c._id ASC;";
			} else {
				selectionArgs= new String[]{String.valueOf(idStation), String.valueOf(idLine), 
						String.valueOf(distance)};
				sql = "SELECT distinct d.FK_id_TypeTransport AS FK_id_TypeTransport, c.Code AS Code " +
						 "FROM Itinerary AS a INNER JOIN Transfer AS b INNER JOIN Itinerary AS c " +
						 "INNER JOIN Transport AS d INNER JOIN Transport AS e INNER JOIN Line AS f " + 
						 "INNER JOIN LineStation AS g INNER JOIN Station AS h " +
					     "ON a._id = b.FK_id_Itinerary_Source AND b.FK_id_Itinerary_Destination = c._id " + 
					     "AND c.FK_id_Transport = d._id AND a.FK_id_Transport = e._id " + 
					     "AND a._id = f.FK_id_Itinerary AND f._id = g.FK_id_Line " +
					     "AND g.FK_id_Station = b.FK_id_Station_Source AND h._id = g.FK_id_Station " +
					     "WHERE d.FavouriteTransport = 1 AND e.FavouriteTransport = 1 " + 
					     "AND h._id = ? AND f._id = ? AND b.Distance <= ? " +
					     "ORDER BY d.FK_id_TypeTransport DESC, c._id ASC;";
			}
			c= myDataBase.rawQuery(sql, selectionArgs);
		}
	    catch (Exception e) {
	        Log.e(Config.LOGTAG, "DataStore.readAllTransferFavouriteTransportByIdItinerary(int idStation, int idItinerary, int distance)" + e.toString());
	    }		
		return c;
	}
	
	protected ArrayList<ItineraryTypeTransport> getAllTransferDistinctTypeOfTransportByIdStation(int idStation, int idLine, int distance) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.getAllTransferDistinctTypeOfTransportByIdStation(int idStation, int idLine, int distance)  ");    		 	 	 		
		String code;
		int fK_id_TypeTransport;

		ArrayList<ItineraryTypeTransport> resultList = new ArrayList<ItineraryTypeTransport>();

	    // Function to retrieve all transferables from idItinerary where transport are favortites
	    Cursor c = readAllTransferDistinctTypeOfTransportByIdStation(idStation, idLine, distance);
	    try {
		    while (c.moveToNext()){
		    	code = c.getString(c.getColumnIndex(col_Itinerary_Code));
		    	fK_id_TypeTransport = c.getInt(c.getColumnIndex(col_Transport_FK_id_TypeTransport));
		    	ItineraryTypeTransport it = new ItineraryTypeTransport(code, fK_id_TypeTransport);
	            resultList.add(it);
	        }
	    }
	    catch (Exception e) {
	        Log.e(Config.LOGTAG, "DataStore.readAllTransferDistinctTypeOfTransportByIdStation(idStation, idLine, distance) " + e.toString());
	    } finally {
		    if(c != null) c.close();
		}
	    return resultList;	
	}
	
	/*	
	 *  * SELECT * FROM
			(SELECT distinct e.FK_id_TypeTransport AS FK_id_TypeTransport, c.Code AS Code
		 	FROM Itinerary AS b INNER JOIN Transfer AS a INNER JOIN Itinerary AS c 
            INNER JOIN Transport AS e INNER JOIN Transport AS f
	     	ON b._id = a.FK_id_Itinerary_Source AND a.FK_id_Itinerary_Destination = c._id
            AND c.FK_id_Transport = e._id AND b.FK_id_Transport = f._id 
	     	WHERE e.FavouriteTransport = 1 AND f.FavouriteTransport = 1 
	     	AND a.FK_id_Station_Source = 1000001 AND a.Distance <= 0
	     	ORDER BY e.FK_id_TypeTransport DESC, c._id ASC)
			UNION SELECT * FROM (
			SELECT distinct d.FK_id_TypeTransport AS FK_id_TypeTransport, c.Code AS Code
			FROM LineStation AS a INNER JOIN Line AS b INNER JOIN Itinerary AS c INNER JOIN Transport AS d
		    ON a.FK_id_Line = b._id AND b.FK_id_Itinerary = c._id AND c.FK_id_Transport = d._id
	 	    WHERE d.FavouriteTransport = 1 
		    AND a.FK_id_Station = 1000001 
	        ORDER BY d.FK_id_TypeTransport DESC, c._id ASC);
	        
		SELECT * FROM
			(SELECT distinct e.FK_id_TypeTransport AS FK_id_TypeTransport, c.Code AS Code
		 	FROM Itinerary AS b INNER JOIN Transfer AS a INNER JOIN Itinerary AS c 
            INNER JOIN Transport AS e INNER JOIN Transport AS f
	     	ON b._id = a.FK_id_Itinerary_Source AND a.FK_id_Itinerary_Destination = c._id
            AND c.FK_id_Transport = e._id AND b.FK_id_Transport = f._id 
	     	WHERE e.FavouriteTransport = 1 AND f.FavouriteTransport = 1 
	     	AND a.FK_id_Station_Source = 1000001 AND a.Distance <= 0
	     	ORDER BY e.FK_id_TypeTransport DESC, c._id ASC)
			UNION SELECT * FROM (
			SELECT distinct d.FK_id_TypeTransport AS FK_id_TypeTransport, c.Code AS Code
			FROM LineStation AS a INNER JOIN Line AS b INNER JOIN Itinerary AS c INNER JOIN Transport AS d
		    ON a.FK_id_Line = b._id AND b.FK_id_Itinerary = c._id AND c.FK_id_Transport = d._id
	 	    WHERE d.FavouriteTransport = 1 
		    AND a.FK_id_Station = 1000001 
	        ORDER BY d.FK_id_TypeTransport DESC, c._id ASC);
	        	        
		     		 
			 TypeTransport i Code
			*/	

	private Cursor readAllTransferDistinctTypeOfTransportByIdStation(int idStation, int distance){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.readAllTransferDistinctTypeOfTransportByIdStation(int idStation, int distance)");
		String[] selectionArgs;
		String sql;
		
		Cursor c=null;
		try{
			if (distance < 0){
				selectionArgs= new String[]{String.valueOf(idStation), String.valueOf(idStation)};
				sql = "SELECT * FROM " +
						"(SELECT distinct e.FK_id_TypeTransport AS FK_id_TypeTransport, c.Code AS Code, c._id AS _id " +
						"FROM Itinerary AS b INNER JOIN Transfer AS a INNER JOIN Itinerary AS c " +
						"INNER JOIN Transport AS e INNER JOIN Transport AS f " +
						"ON b._id = a.FK_id_Itinerary_Source AND a.FK_id_Itinerary_Destination = c._id " +
						"AND c.FK_id_Transport = e._id AND b.FK_id_Transport = f._id " +
						"WHERE e.FavouriteTransport = 1 AND f.FavouriteTransport = 1 " +
						"AND a.FK_id_Station_Source = ?) " +
						"UNION SELECT * FROM ( " +
						"SELECT distinct d.FK_id_TypeTransport AS FK_id_TypeTransport, c.Code AS Code, c._id AS _id " +
						"FROM LineStation AS a INNER JOIN Line AS b INNER JOIN Itinerary AS c INNER JOIN Transport AS d " +
						"ON a.FK_id_Line = b._id AND b.FK_id_Itinerary = c._id AND c.FK_id_Transport = d._id " +
						"WHERE d.FavouriteTransport = 1 " +
						"AND a.FK_id_Station = ?) " + 
						"ORDER BY FK_id_TypeTransport DESC, _id ASC;";
			} else {
				selectionArgs= new String[]{String.valueOf(idStation), 
						String.valueOf(distance), String.valueOf(idStation)};
				sql = "SELECT * FROM " +
						"(SELECT distinct e.FK_id_TypeTransport AS FK_id_TypeTransport, c.Code AS Code, c._id AS _id " +
						"FROM Itinerary AS b INNER JOIN Transfer AS a INNER JOIN Itinerary AS c " +
						"INNER JOIN Transport AS e INNER JOIN Transport AS f " +
						"ON b._id = a.FK_id_Itinerary_Source AND a.FK_id_Itinerary_Destination = c._id " +
						"AND c.FK_id_Transport = e._id AND b.FK_id_Transport = f._id " +
						"WHERE e.FavouriteTransport = 1 AND f.FavouriteTransport = 1 " +
						"AND a.FK_id_Station_Source = ? AND a.Distance <= ?) " +
						"UNION SELECT * FROM ( " +
						"SELECT distinct d.FK_id_TypeTransport AS FK_id_TypeTransport, c.Code AS Code, c._id AS _id " +
						"FROM LineStation AS a INNER JOIN Line AS b INNER JOIN Itinerary AS c INNER JOIN Transport AS d " +
						"ON a.FK_id_Line = b._id AND b.FK_id_Itinerary = c._id AND c.FK_id_Transport = d._id " +
						"WHERE d.FavouriteTransport = 1 " +
						"AND a.FK_id_Station = ?) " + 
						"ORDER BY FK_id_TypeTransport DESC, _id ASC;";
			}
			c= myDataBase.rawQuery(sql, selectionArgs);
		}
	    catch (Exception e) {
	        Log.e(Config.LOGTAG, "DataStore.readAllTransferDistinctTypeOfTransportByIdStation(int idStation, int distance)" + e.toString());
	    }		
		return c;
	}
	
	
	protected ArrayList<ItineraryTypeTransport> getAllTransferDistinctTypeOfTransportByIdStation(int idStation, int distance) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.getAllTransferDistinctTypeOfTransportByIdStation(idStation =" + idStation);    		 	 	 		
		String code;
		int fK_id_TypeTransport;

		ArrayList<ItineraryTypeTransport> resultList = new ArrayList<ItineraryTypeTransport>();

	    // Function to retrieve all transferables from idItinerary where transport are favortites
	    Cursor c = readAllTransferDistinctTypeOfTransportByIdStation(idStation, distance);
	    try {
		    while (c.moveToNext()){
		    	code = c.getString(c.getColumnIndex(col_Itinerary_Code));
		    	fK_id_TypeTransport = c.getInt(c.getColumnIndex(col_Transport_FK_id_TypeTransport));
		    	ItineraryTypeTransport it = new ItineraryTypeTransport(code, fK_id_TypeTransport);
	            resultList.add(it);
	        }
	    }
	    catch (Exception e) {
	        Log.e(Config.LOGTAG, "DataStore.readAllTransferDistinctTypeOfTransportByIdStation(idStation, distance) " + e.toString());
	    } finally {
		    if(c != null) c.close();
	    }
	    return resultList;	
	}
	
	/*	
	SELECT distinct d.FK_id_TypeTransport AS FK_id_TypeTransport, c.Code AS Code
			 FROM Itinerary AS a INNER JOIN Transfer AS b INNER JOIN Itinerary AS c 
			 INNER JOIN Transport AS d INNER JOIN Transport AS e INNER JOIN Line AS f 
			 INNER JOIN LineStation AS g INNER JOIN Station AS h
		     ON a._id = b.FK_id_Itinerary_Source AND b.FK_id_Itinerary_Destination = c._id 
		     AND c.FK_id_Transport = d._id AND a.FK_id_Transport = e._id 
		     AND a._id = f.FK_id_Itinerary AND f._id = g.FK_id_Line 
		     AND g.FK_id_Station = b.FK_id_Station_Source AND h._id = g.FK_id_Station
		     WHERE h._id = 1000031 ORDER BY d.FK_id_TypeTransport DESC, c._id ASC;
			 
	SELECT distinct d.FK_id_TypeTransport AS FK_id_TypeTransport, c.Code AS Code
			 FROM Itinerary AS a INNER JOIN Transfer AS b INNER JOIN Itinerary AS c 
			 INNER JOIN Transport AS d INNER JOIN Transport AS e INNER JOIN Line AS f 
			 INNER JOIN LineStation AS g INNER JOIN Station AS h
		     ON a._id = b.FK_id_Itinerary_Source AND b.FK_id_Itinerary_Destination = c._id 
		     AND c.FK_id_Transport = d._id AND a.FK_id_Transport = e._id 
		     AND a._id = f.FK_id_Itinerary AND f._id = g.FK_id_Line 
		     AND g.FK_id_Station = b.FK_id_Station_Source AND h._id = g.FK_id_Station
		     WHERE h._id = 1000031 AND b.Distance <= 200
		     ORDER BY d.FK_id_TypeTransport DESC, c._id ASC;
		     		 
			 TypeTransport i Code
			*/	

	private Cursor readAllTransferByIdStation(int idStation, int distance){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.readAllTransferByIdStation(int idStation, int distance)");
		String[] selectionArgs;
		String sql;
		
		Cursor c=null;
		try{
			if (distance < 0){
				selectionArgs= new String[]{String.valueOf(idStation)};
				sql = "	SELECT distinct d.FK_id_TypeTransport AS FK_id_TypeTransport, c.Code AS Code " +
						 "FROM Itinerary AS a INNER JOIN Transfer AS b INNER JOIN Itinerary AS c " +
						 "INNER JOIN Transport AS d INNER JOIN Transport AS e INNER JOIN Line AS f " +
						 "INNER JOIN LineStation AS g INNER JOIN Station AS h " +
					     "ON a._id = b.FK_id_Itinerary_Source AND b.FK_id_Itinerary_Destination = c._id " + 
					     "AND c.FK_id_Transport = d._id AND a.FK_id_Transport = e._id " +
					     "AND a._id = f.FK_id_Itinerary AND f._id = g.FK_id_Line " +
					     "AND g.FK_id_Station = b.FK_id_Station_Source AND h._id = g.FK_id_Station " +
					     "WHERE h._id = ? ORDER BY d.FK_id_TypeTransport DESC, c._id ASC; ";
			} else {
				selectionArgs= new String[]{String.valueOf(idStation), String.valueOf(distance)};
				sql = "SELECT distinct d.FK_id_TypeTransport AS FK_id_TypeTransport, c.Code AS Code " +
						 "FROM Itinerary AS a INNER JOIN Transfer AS b INNER JOIN Itinerary AS c " +
						 "INNER JOIN Transport AS d INNER JOIN Transport AS e INNER JOIN Line AS f " + 
						 "INNER JOIN LineStation AS g INNER JOIN Station AS h " +
					     "ON a._id = b.FK_id_Itinerary_Source AND b.FK_id_Itinerary_Destination = c._id " + 
					     "AND c.FK_id_Transport = d._id AND a.FK_id_Transport = e._id " +  
					     "AND a._id = f.FK_id_Itinerary AND f._id = g.FK_id_Line " +
					     "AND g.FK_id_Station = b.FK_id_Station_Source AND h._id = g.FK_id_Station " +
					     "WHERE h._id = ? AND b.Distance <= ? ORDER BY d.FK_id_TypeTransport DESC, c._id ASC;";
			}
			c= myDataBase.rawQuery(sql, selectionArgs);
		}
	    catch (Exception e) {
	        Log.e(Config.LOGTAG, "DataStore.readAllTransferByIdStation(int idStation, int distance)" + e.toString());
	    }		
		return c;
	}
	
	protected ArrayList<ItineraryTypeTransport> getAllTransferByIdStation(int idStation, int distance) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.getAllTransferByIdStation(" + idStation +", distance " + distance + ")");    		 	 	 		
		String code;
		int fK_id_TypeTransport;

		ArrayList<ItineraryTypeTransport> resultList = new ArrayList<ItineraryTypeTransport>();

	    // Function to retrieve all transferables from idItinerary where transport are favortites
	    Cursor c = readAllTransferByIdStation(idStation, distance);
	    try {
		    while (c.moveToNext()){
		    	code = c.getString(c.getColumnIndex(col_Itinerary_Code));
		    	fK_id_TypeTransport = c.getInt(c.getColumnIndex(col_Transport_FK_id_TypeTransport));
		    	ItineraryTypeTransport it = new ItineraryTypeTransport(code, fK_id_TypeTransport);
	            resultList.add(it);
	        }
	    }
	    catch (Exception e) {
	        Log.e(Config.LOGTAG, "DataStore.readAllTransferByIdStation(idStation, distance) " + e.toString());
	    } finally {
		    if(c != null) c.close();
	    }
	    return resultList;	
	}

	
	/*	
	SELECT distinct d.FK_id_TypeTransport AS FK_id_TypeTransport, c.Code AS Code
			 FROM Itinerary AS a INNER JOIN Transfer AS b INNER JOIN Itinerary AS c 
			 INNER JOIN Transport AS d INNER JOIN Transport AS e INNER JOIN Line AS f 
			 INNER JOIN LineStation AS g INNER JOIN Station AS h
		     ON a._id = b.FK_id_Itinerary_Source AND b.FK_id_Itinerary_Destination = c._id 
		     AND c.FK_id_Transport = d._id AND a.FK_id_Transport = e._id 
		     AND a._id = f.FK_id_Itinerary AND f._id = g.FK_id_Line 
		     AND g.FK_id_Station = b.FK_id_Station_Source AND h._id = g.FK_id_Station
		     WHERE d.FavouriteTransport = 1 AND e.FavouriteTransport = 1 
		     AND h._id = 1000466 AND a._id = 1000028
		     ORDER BY d.FK_id_TypeTransport DESC, c._id ASC;
			 
	SELECT distinct d.FK_id_TypeTransport AS FK_id_TypeTransport, c.Code AS Code
			 FROM Itinerary AS a INNER JOIN Transfer AS b INNER JOIN Itinerary AS c 
			 INNER JOIN Transport AS d INNER JOIN Transport AS e INNER JOIN Line AS f 
			 INNER JOIN LineStation AS g INNER JOIN Station AS h
		     ON a._id = b.FK_id_Itinerary_Source AND b.FK_id_Itinerary_Destination = c._id 
		     AND c.FK_id_Transport = d._id AND a.FK_id_Transport = e._id 
		     AND a._id = f.FK_id_Itinerary AND f._id = g.FK_id_Line 
		     AND g.FK_id_Station = b.FK_id_Station_Source AND h._id = g.FK_id_Station
		     WHERE d.FavouriteTransport = 1 AND e.FavouriteTransport = 1 
		     AND h._id = 1000466 AND a._id = 1000028 AND b.Distance <= 200
		     ORDER BY d.FK_id_TypeTransport DESC, c._id ASC;
		     		 
			 TypeTransport i Code
			*/	

		private Cursor readAllTransferDistinctTypeOfTransportByIdStationItinerary(int idStation, int idItinerary, int distance){
			if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.readAllTransferDistinctTypeOfTransportByIdStationItinerary(int idStation, int idItinerary, int distance)");
			String[] selectionArgs;
			String sql;
			
			Cursor c=null;
			try{
				if (distance < 0){
					selectionArgs= new String[]{String.valueOf(idStation), String.valueOf(idItinerary)};
					sql = "SELECT distinct d.FK_id_TypeTransport AS FK_id_TypeTransport, c.Code AS Code " +
							 "FROM Itinerary AS a INNER JOIN Transfer AS b INNER JOIN Itinerary AS c " +
							 "INNER JOIN Transport AS d INNER JOIN Transport AS e INNER JOIN Line AS f " +
							 "INNER JOIN LineStation AS g INNER JOIN Station AS h " +
						     "ON a._id = b.FK_id_Itinerary_Source AND b.FK_id_Itinerary_Destination = c._id " +
						     "AND c.FK_id_Transport = d._id AND a.FK_id_Transport = e._id " +
						     "AND a._id = f.FK_id_Itinerary AND f._id = g.FK_id_Line " +
						     "AND g.FK_id_Station = b.FK_id_Station_Source AND h._id = g.FK_id_Station " +
						     "WHERE d.FavouriteTransport = 1 AND e.FavouriteTransport = 1 " +
						     "AND h._id = ? AND a._id = ? " +
						     "ORDER BY d.FK_id_TypeTransport DESC, c._id ASC;";
				} else {
					selectionArgs= new String[]{String.valueOf(idStation), String.valueOf(idItinerary), String.valueOf(distance)};
					sql = "SELECT distinct d.FK_id_TypeTransport AS FK_id_TypeTransport, c.Code AS Code " +
							 "FROM Itinerary AS a INNER JOIN Transfer AS b INNER JOIN Itinerary AS c " +
							 "INNER JOIN Transport AS d INNER JOIN Transport AS e INNER JOIN Line AS f " +
							 "INNER JOIN LineStation AS g INNER JOIN Station AS h " +
						     "ON a._id = b.FK_id_Itinerary_Source AND b.FK_id_Itinerary_Destination = c._id " + 
						     "AND c.FK_id_Transport = d._id AND a.FK_id_Transport = e._id " +
						     "AND a._id = f.FK_id_Itinerary AND f._id = g.FK_id_Line " +
						     "AND g.FK_id_Station = b.FK_id_Station_Source AND h._id = g.FK_id_Station " +
						     "WHERE d.FavouriteTransport = 1 AND e.FavouriteTransport = 1 " +
						     "AND h._id = ? AND a._id = ? AND b.Distance <= ? " +
						     "ORDER BY d.FK_id_TypeTransport DESC, c._id ASC;";
				}
				c= myDataBase.rawQuery(sql, selectionArgs);
			}
		    catch (Exception e) {
		        Log.e(Config.LOGTAG, "DataStore.readAllTransferDistinctTypeOfTransportByIdStationItinerary(int idStation, int idItinerary, int distance)" + e.toString());
		    }		
			return c;
		}
		
		protected ArrayList<ItineraryTypeTransport> getAllTransferDistinctTypeOfTransportByIdStationItinerary(int idStation, int idItinerary, int distance) {
			if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.getAllTransferDistinctTypeOfTransportByIdStationItinerary(int idStation, int idItinerary, int distance)  ");    		 	 	 		
			String code;
			int fK_id_TypeTransport;

			ArrayList<ItineraryTypeTransport> resultList = new ArrayList<ItineraryTypeTransport>();

		    // Function to retrieve all transferables from idItinerary where transport are favortites
		    Cursor c = readAllTransferDistinctTypeOfTransportByIdStationItinerary(idStation, idItinerary, distance);
		    try {
			    while (c.moveToNext()){
			    	code = c.getString(c.getColumnIndex(col_Itinerary_Code));
			    	fK_id_TypeTransport = c.getInt(c.getColumnIndex(col_Transport_FK_id_TypeTransport));
			    	ItineraryTypeTransport it = new ItineraryTypeTransport(code, fK_id_TypeTransport);
		            resultList.add(it);
		        }
		    }
		    catch (Exception e) {
		        Log.e(Config.LOGTAG, "DataStore.readAllTransferDistinctTypeOfTransportByIdStation(idStation, idLine, distance) " + e.toString());
		    } finally {
			    if(c != null) c.close();
		    }
		    return resultList;	
		}
	
			
/*
	SELECT TimeStamp FROM TypeTransport AS a INNER JOIN Transport AS b INNER JOIN 
		Station AS c ON a._id = b.FK_id_TypeTransport AND b._id = c.FK_id_Transport 
		WHERE Transport = "BICIPALMA" ORDER BY TimeStamp ASC LIMIT 1;
*/		
	//private TimeStamp
	private Cursor readTimeStamp(int idTypeTransport, int idStation){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.readTimeStamp(int idItinerary)");  
		Cursor c=null;
		try{
		
			//Create new querybuilder
			SQLiteQueryBuilder _QB = new SQLiteQueryBuilder();
			
			//Specify Transport table AND add JOIN to Station table - TypeTransport is a Enumerated type
				_QB.setTables(T_Transport + " INNER JOIN " + T_Station);
				_QB.appendWhere(col_Transport_id + " = " + T_Station + "." + col_Station_FK_id_Transport);

				String [] _projectionIn = new String[]{col_Station_TimeStamp};
				// idStation < 0 --> all TypeTransport, else for Station
				String _selection;
				String[] _selectionArgs;
				if (idStation < 0){
					_selectionArgs = new String[] {String.valueOf(idTypeTransport)};
					_selection = col_Transport_FK_id_TypeTransport + " =?";
				}else{
					_selectionArgs = new String[] {String.valueOf(idTypeTransport), String.valueOf(idStation)};					
					_selection = col_Transport_FK_id_TypeTransport + " =? AND " + col_Station_id + " =?";
				}
				String _sortOrder = col_Station_TimeStamp + " ASC";
				String _limit = "1";
				
				String _groupBy = null;
				String _having = null;
				 
				//Get cursor REVISAR QUERY
				//c = _QB.query(myDataBase, _projectionIn, _selection, _selectionArgs, _groupBy, _having, _sortOrder);
				c = _QB.query(myDataBase, _projectionIn, _selection, _selectionArgs, _groupBy, _having, _sortOrder, _limit);
			}
		    catch (Exception e) {
		        Log.e(Config.LOGTAG, "DataStore.readTimeStamp(int idItinerary) " + e.toString());
		    }		
			return c;
	}
			
	//TimeStamp for BiciPalma 
	protected Long getTimeStamp(TypeTransport mType, int idStation) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.getTimeStamp(TypeTransport mType, int idStation)");
		Long res= null;	
	    Cursor c = readTimeStamp(mType.getValue(), idStation);
	    try {
		    if (c.moveToNext())
		    {
	        	String resul = c.getString(c.getColumnIndex(col_Station_TimeStamp));
	        	res = Long.valueOf(resul);
	        }
	    }
	    catch (Exception e) {
	    	Log.e(Config.LOGTAG, "DataStore.getTimeStamp(TypeTransport mType, int idStation) " + e.toString());
	    } finally {
		    if(c != null) c.close();
	    }
	    return res;
	}
	
	/*
	SELECT b._id as _id, b.FK_id_Transport, b.Code, b.Name, b.ColorHEX, b.FavoriteItinerary, b.DisabledFacilities 
	FROM Line AS a INNER JOIN Itinerary AS b INNER JOIN Transport AS c ON a.FK_id_Itinerary = b._id AND b.FK_id_Transport = c._id 
    WHERE a._id = 3000001 AND c.FavouriteTransport = 1;
*/	
	//private Itinerary 
	private Cursor readItineraryOfLine(int idLine){		
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.readItineraryOfLine(int idLine)"); 
		Cursor c = null;
		try {
	
			//Create new querybuilder
			SQLiteQueryBuilder _QB = new SQLiteQueryBuilder();
			
			//Specify Itinerary table AND add JOIN to Transport table only for FavouriteTransport
			_QB.setTables(T_Line + " INNER JOIN " + T_Itinerary + " INNER JOIN " + T_Transport);
			_QB.appendWhere(col_Itinerary_id + " = " + T_Line + "." + col_Line_FK_id_Itinerary + 
					" AND " + T_Itinerary + "." + col_Itinerary_FK_id_Transport + " = " + col_Transport_id);

			String projectionIn[] = new String[] { col_Itinerary_id + " AS _id", T_Itinerary + "." + col_Itinerary_FK_id_Transport, 
					T_Itinerary + "." + col_Itinerary_Code, T_Itinerary + "." + col_Itinerary_Name, 
					T_Itinerary + "." + col_Itinerary_ColorHEX, T_Itinerary + "." + col_Itinerary_FavoriteItinerary, 
					T_Itinerary + "." + col_Itinerary_DisabledFacilities, T_Transport + "." + col_Transport_FK_id_TypeTransport};
			
			String selection = col_Line_id + " =? AND " + T_Transport + "." + col_Transport_FavouriteTransport + " = ?";
			
			String[] selectionArgs = new String[] {String.valueOf(idLine), String.valueOf(Config.TRUE)};
			
			//Get cursor
			c = _QB.query(myDataBase, projectionIn, selection, selectionArgs, null, null, null);
		}
        catch (Exception e) {
            Log.e(Config.LOGTAG, "DataStore.readAllTypeOfTransport() " + e.toString());
        }
		return c;
	}

	//Itinerary of Line 
	protected Itinerary getItineraryOfLine(int idLine) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.getItineraryOfLine(int idLine)");    		 	 	 		

		int myID;
		int myFK_id_Transport;
		String myCode;
		String myName;
		//String myColorHEX;
		boolean myFavorite;
		boolean myDisabledFacilities;
		int myTypeOfTransport;
		Itinerary it = null;
		
	    // Function to retrieve all values from Itinerary
	    Cursor c = readItineraryOfLine(idLine);
	    try {
		    if (c.moveToNext())
		    {
		    	myID = c.getInt(c.getColumnIndex("_id"));
				myFK_id_Transport = c.getInt(c.getColumnIndex(col_Itinerary_FK_id_Transport));
				myCode = c.getString(c.getColumnIndex(col_Itinerary_Code));
				myName = c.getString(c.getColumnIndex(col_Itinerary_Name));
				//myColorHEX = c.getString(c.getColumnIndex(col_Itinerary_ColorHEX));
				myFavorite = getBoolean(c.getString(c.getColumnIndex(col_Itinerary_FavoriteItinerary)));
				myDisabledFacilities = getBoolean(c.getString(c.getColumnIndex(col_Itinerary_DisabledFacilities)));
				myTypeOfTransport = c.getInt(c.getColumnIndex(col_Transport_FK_id_TypeTransport));
		    	
		    	it = new Itinerary(myID, myFK_id_Transport, myCode, myName, myFavorite, myDisabledFacilities, myTypeOfTransport);
	            //retrieve Lines from id_Itinerary, if our Itinerary is Bike only get first element
	            //retrieve Transfer from favorite type of transport from id_Itinerary
	            //it.setListTransfer(getAllTransferDistinctTypeOfTransportByIdItinerary(myID, distance));
		    }
	    }
	    catch (Exception e) {
	        Log.e(Config.LOGTAG, "DataStore.getItineraryOfLine(int idLine) " + e.toString());
	    } finally {
		    if(c != null) c.close();
	    }
	    return it;
	}
	

	//private ArrayList<Itinerary>
	private Cursor readItineraryHasDisabledFaciities(int idItinerary){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.readItineraryHasDisabledFaciities(int idItinerary)");  
		Cursor c=null;
		try{
		
			//Create new querybuilder
			SQLiteQueryBuilder _QB = new SQLiteQueryBuilder();
			
			//Specify Itinerary table
			_QB.setTables(T_Itinerary);

			String selection = col_Itinerary_id + " =? AND " + T_Itinerary + "." + col_Itinerary_DisabledFacilities + " =?";
			
			String[] selectionArgs = new String[] {String.valueOf(idItinerary), String.valueOf(Config.TRUE)};
			
			//Get cursor
			c = _QB.query(myDataBase, null, selection, selectionArgs, null, null, null);			         
		}
	    catch (Exception e) {
	        Log.e(Config.LOGTAG, "DataStore.readItineraryFavourite(idItinerary) " + e.toString());
	    }		
		return c;
	}

	//FavouriteLine 
	protected boolean getItineraryHasDisabledFacilities(int idItinerary){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.getItineraryHasDisabledFacilities(int idItinerary)");
		boolean res= false;
	    Cursor c = readItineraryHasDisabledFaciities(idItinerary);
		try {
			res = (c.getCount()>0);
		}
	    catch (Exception e) {
	        Log.e(Config.LOGTAG, "DataStore.readItineraryHasDisabledFaciities(idItinerary) " + e.toString());
	    } finally {
		    if(c != null) c.close();
		}
	    return res;			
	}
/*	
	UPDATE BikeStation SET Capacity = ?, BikeAvailable = ?, ParksFree = ? WHERE _id = (SELECT
		_id FROM CabOperator WHERE FK_id_Station = ? limit 1);
*/	
	//private ArrayList<Station> of BikeStation
	private boolean writeBikeStations(ArrayList<Station> pBikeStationsList){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.writeBikeStations(ArrrayList<Station> pBikeStationsList)");
		//result for each BikeStation
		int res = 0;
		int res2 = 0;
		Iterator<Station> sta = pBikeStationsList.iterator();
		// Table BikeStation
		String table = T_BikeStation;
		String whereClause = col_BikeStation_FK_id_Station + " =?";

		String table2 = T_Station;
		String whereClause2 = "_id =?";
		while(sta.hasNext()){
			Station st = sta.next();
			//Changing data
			ContentValues value = new ContentValues();
			value.put("_id", String.valueOf(st.getBikeStation().get_id()));
			value.put(col_BikeStation_FK_id_Station, String.valueOf(st.get_id()));
			value.put(col_BikeStation_Capacity , String.valueOf(st.getBikeStation().getCapacity()));
			value.put(col_BikeStation_BikeAvailable , String.valueOf(st.getBikeStation().getBikesAvailable()));
			value.put(col_BikeStation_ParksFree , String.valueOf(st.getBikeStation().getParksFree()));
			String[] whereArgs = new String[]{String.valueOf(st.get_id())};	
						
			try{ 
				//database operation
				int resul = myDataBase.update(table, value, whereClause, whereArgs);
				res = resul == 1 ? res + 1 : res;
			}
		    catch (Exception e) {
	            Log.e(Config.LOGTAG, "DataStore.writeBikeStations BikeStation " + 
	            			st.getCode() + e.toString());
	        }
			
			//Changing data
			ContentValues value2 = new ContentValues();
			value2.put(col_Station_TimeStamp, String.valueOf(st.getTimeStamp().getTimeInMillis()));
			String[] whereArgs2 = new String[]{String.valueOf(st.get_id())};	
			
			try{ 
				//database operation
				int resul2 = myDataBase.update(table2, value2, whereClause2, whereArgs2);
				res2 = resul2 == 1 ? res2 + 1 : res2;
			}
		    catch (Exception e) {
	            Log.e(Config.LOGTAG, "DataStore.writeBikeStations BikeStation " + 
	            			st.get_id() + e.toString());
	        }
		}
		// num of succeful operations
		return (res == pBikeStationsList.size() && res2 == res);
	}
	
	// write data BikeStations to DB
	protected boolean setBikeStations(ArrayList<Station> pBikeStationsList){
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.setBikeStations(ArrayList<Station> pBikeStationsList)");
		boolean res = writeBikeStations(pBikeStationsList);
		return res;
	}
	
	/*
	SELECT * FROM Line WHERE FK_id_Itinerary = 1000001 AND (OneWayEMT = "AEROPORT" OR ReturnEMT= "AEROPORT"); 
*/	
	//private Line 
	private Cursor readLineFromBusStation(int idItinerary, String oneWayEMT, String returnEMT){		
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.readLineFromBusStation(int idItinerary, String oneWayEMT, String returnEMT)"); 
		Cursor c = null;
		try{
			String [] columns=new String[]{"_id", col_Line_UNameOneWay, 
					col_Line_UNameOneWay2 ,col_Line_UNameReturn, col_Line_UNameReturn2, 
					col_Line_OneWayEMT, col_Line_ReturnEMT};
			String selection = col_Line_FK_id_Itinerary+"=? AND (" + col_Line_OneWayEMT + "=? OR "
					+ col_Line_ReturnEMT + "=?)";
			String[] where = new String[] {String.valueOf(idItinerary), oneWayEMT, returnEMT};
			c = myDataBase.query(T_Line, columns, selection, where, null, null, null);
		}
	    catch (Exception e) {
	        Log.e(Config.LOGTAG, "DataStore.readAllLine(idItinerary) " + e.toString());
	    }
		return c;
	}

	//Itinerary of Line 
	protected Line getLineFromBusStation(int idItinerary, String oneWayEMT, String returnEMT) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.getLineFromBusStation(int idItinerary, String oneWayEMT, String returnEMT)");    		 	 	 		

		int myID;
		String myUNameOneWay;
		String myUNameOneWay2;
		String myUNameReturn;
		String myUNameReturn2;
		String myOneWayEMT;
		String myReturnEMT;
		
		//boolean myFavourite;
		Line ln = null;

	    // Function to retrieve all lines by idItinerary
	    Cursor c = readLineFromBusStation(idItinerary, oneWayEMT, returnEMT);
	    try {
		    if (c.moveToNext())
		    {
	        	myID = c.getInt(c.getColumnIndex("_id"));
	        	myUNameOneWay = c.getString(c.getColumnIndex(col_Line_UNameOneWay));
	        	myUNameOneWay2 = c.getString(c.getColumnIndex(col_Line_UNameOneWay2));
	        	myUNameReturn = c.getString(c.getColumnIndex(col_Line_UNameReturn));
	        	myUNameReturn2 = c.getString(c.getColumnIndex(col_Line_UNameReturn2));
	        	myOneWayEMT = c.getString(c.getColumnIndex(col_Line_OneWayEMT));
	        	myReturnEMT = c.getString(c.getColumnIndex(col_Line_ReturnEMT));
		    	
		    	ln = new Line(myID, idItinerary, myUNameOneWay, myUNameOneWay2, myUNameReturn, myUNameReturn2, myOneWayEMT, myReturnEMT);
	            // getting if Lines is Favourite NOT IN VIEW
	            //myFavourite = IsFavouriteLine(myID);
	            //li.setFavorite(myFavourite);
	            //retrieve Transfer from favorite type of transport from id_Itinerary
	            //li.setListTransfer(getAllTransferDistinctTypeOfTransportByIdLine(myID, distance));
	        }
	    }
	    catch (Exception e) {
	    	Log.e(Config.LOGTAG, "DataStore.getLineFromBusStation(int idItinerary, String oneWayEMT, String returnEMT) " + e.toString());
	    } finally {
	    	if(c != null) c.close();
	    }
	    return ln;	
	}

	
	/*
	 * 
SELECT _id, FK_id_Itinerary, UNameOneWay, UNameOneWay2, UNameReturn, UnameReturn2, OneWayEMT, ReturnEMT 
FROM Line 
WHERE _id = 1000001
*/	
	//private Line

	private Cursor readLine(int idLine){		
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.readLine(idLine =" + idLine); 
		Cursor c = null;
		try {
	
			//Create new querybuilder
			SQLiteQueryBuilder _QB = new SQLiteQueryBuilder();
			
			//Specify Itinerary table AND add JOIN to Transport table only for FavouriteTransport
			_QB.setTables(T_Line);
			//_QB.appendWhere("");
			
			String projectionIn[] = new String[] { col_Line_id + " AS _id", T_Line+ "." + col_Line_FK_id_Itinerary,
					T_Line+ "." + col_Line_UNameOneWay, T_Line+ "." + col_Line_UNameOneWay2, T_Line+ "." + col_Line_UNameReturn,
					T_Line+ "." + col_Line_UNameReturn2, T_Line+ "." + col_Line_OneWayEMT, T_Line+ "." + col_Line_ReturnEMT};
			
			String selection = "_id =?";
			
			String[] selectionArgs = new String[] {String.valueOf(idLine)};
			//Get cursor
			c = _QB.query(myDataBase, projectionIn, selection, selectionArgs, null, null, null);
		}
        catch (Exception e) {
            Log.e(Config.LOGTAG, "DataStore.readLine(int idLine) " + e.toString());
        }
		
		return c;
	}

	protected Line getLine(int idLine) {
		if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.getLine(idLine =" + idLine);    		 	 	 		

		//int myID;
		int myFK_id_Itinerary;
		String myUNameOneWay;
		String myUNameOneWay2;
		String myUNameReturn;
		String myUNameReturn2;
		String myOneWayEMT;
		String myReturnEMT;	
		
		//boolean myFavourite;
		Line ln = null;

	    // Function to retrieve all lines by idItinerary
	    Cursor c = readLine(idLine);
	    try {
		    if (c.moveToNext())
		    {
	        	//myID = idLine;
		    	myFK_id_Itinerary = c.getInt(c.getColumnIndex(col_Line_FK_id_Itinerary));
	        	myUNameOneWay = c.getString(c.getColumnIndex(col_Line_UNameOneWay));
	        	myUNameOneWay2 = c.getString(c.getColumnIndex(col_Line_UNameOneWay2));
	        	myUNameReturn = c.getString(c.getColumnIndex(col_Line_UNameReturn));
	        	myUNameReturn2 = c.getString(c.getColumnIndex(col_Line_UNameReturn2));
	        	myOneWayEMT = c.getString(c.getColumnIndex(col_Line_OneWayEMT));
	        	myReturnEMT = c.getString(c.getColumnIndex(col_Line_ReturnEMT));
		    	
		    	ln = new Line(idLine, myFK_id_Itinerary, myUNameOneWay, myUNameOneWay2, myUNameReturn, myUNameReturn2, myOneWayEMT, myReturnEMT);
	            // getting if Lines is Favourite
	            //myFavourite = IsFavouriteLine(idLine);
	            //ln.setFavorite(myFavourite);
	            //retrieve Transfer from favorite type of transport from id_Itinerary
	            //li.setListTransfer(getAllTransferDistinctTypeOfTransportByIdLine(myID, distance));
	        }
	    }
	    catch (Exception e) {
	    	Log.e(Config.LOGTAG, "DataStore.getLine(int idLine) " + e.toString());
	    } finally {
		    if(c != null) c.close();
	    }
	    return ln;	
	}
	
	
	
	/*	
SELECT DISTINCT b.FK_id_Itinerary, b.OneWayEMT AS Destination,  c.Code 
	FROM LineStation AS a INNER JOIN Line AS b INNER JOIN Itinerary AS c 
	ON a.FK_id_Line = b._id AND b.FK_id_Itinerary = c._id 
	WHERE a.FK_id_Station = 1000071 AND RouteOneWay = 1

UNION

SELECT DISTINCT b.FK_id_Itinerary, b.ReturnEMT AS Destination,  c.Code 
	FROM LineStation AS a INNER JOIN Line AS b INNER JOIN Itinerary AS c 
	ON a.FK_id_Line = b._id AND b.FK_id_Itinerary = c._id 
	WHERE a.FK_id_Station = 1000071 AND RouteOneWay = 0 AND RouteReturn = 1
	
SELECT * FROM (
	SELECT DISTINCT b.FK_id_Itinerary, b.OneWayEMT AS Destination,  c.Code 
	FROM LineStation AS a INNER JOIN Line AS b INNER JOIN Itinerary AS c 
	ON a.FK_id_Line = b._id AND b.FK_id_Itinerary = c._id 
	WHERE a.FK_id_Station = 1000001 AND RouteOneWay = 1 AND StationNext NOT NULL

UNION

	SELECT DISTINCT b.FK_id_Itinerary, b.ReturnEMT AS Destination,  c.Code 
	FROM LineStation AS a INNER JOIN Line AS b INNER JOIN Itinerary AS c 
	ON a.FK_id_Line = b._id AND b.FK_id_Itinerary = c._id 
	WHERE a.FK_id_Station = 1000001 AND RouteOneWay = 0 AND RouteReturn = 1 AND StationNext NOT NULL) 

ORDER BY FK_id_Itinerary ASC	
	
	BusStation
	*/	
	
		private long insertBusStation(BusStation myBusStation){
			//insert
			ContentValues values = new ContentValues();
			values.put(col_BusStation_FK_id_Station, myBusStation.getFK_id_Station());
			values.put(col_BusStation_FK_id_Itinerary, myBusStation.getFK_id_Itinerary());
			values.put(col_BusStation_FK_id_Line, myBusStation.getFK_id_Line());
			values.put(col_BusStation_Destination, myBusStation.getDestination());
			if (myBusStation.getFirstBus() != null){ 
				values.put(col_BusStation_FirstBus, myBusStation.getFirstBus().getTimeInMillis());
				values.put(col_BusStation_FirstBusMeters, myBusStation.getFirsBusMeters());
			} else {
				values.put(col_BusStation_FirstBusMeters, -1);				
			}
			if (myBusStation.getSecondBus() != null){
				values.put(col_BusStation_SecondBus, myBusStation.getSecondBus().getTimeInMillis());
				values.put(col_BusStation_SecondBusMeters, myBusStation.getSecondBusMeters());
			} else {
				values.put(col_BusStation_SecondBusMeters, -1);
			}
			
			String nullColumnHack = col_BusStation_FirstBus;

			return myDataBase.insert(T_BusStation, nullColumnHack, values);
		}

		private Cursor createAllBusStation(int idStation){
			if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.readAllBusStation(int idStation)");
			String[] selectionArgs;
			String sql;
			
			Cursor c=null;
			try{
				selectionArgs= new String[]{String.valueOf(idStation), String.valueOf(Config.TRUE),
						String.valueOf(idStation), String.valueOf(Config.FALSE), String.valueOf(Config.TRUE)};
				sql = "SELECT * FROM (SELECT DISTINCT b.FK_id_Itinerary AS FK_id_Itinerary, " +
							"b.OneWayEMT AS Destination, c.Code AS Code, b._id AS FK_id_Line " +
							"FROM LineStation AS a INNER JOIN Line AS b " +
							"INNER JOIN Itinerary AS c " +
							"ON a.FK_id_Line = b._id AND b.FK_id_Itinerary = c._id " +
							"WHERE a.FK_id_Station = ? AND RouteOneWay = ? AND StationNext NOT NULL " +
							"UNION " +
							"SELECT DISTINCT b.FK_id_Itinerary, b.ReturnEMT AS Destination,  c.Code, b._id AS FK_id_Line " +
							"FROM LineStation AS a INNER JOIN Line AS b INNER JOIN Itinerary AS c " +
							"ON a.FK_id_Line = b._id AND b.FK_id_Itinerary = c._id " +
							"WHERE a.FK_id_Station = ? AND RouteOneWay = ? AND RouteReturn = ? AND StationNext NOT NULL) " +
							"ORDER BY FK_id_Itinerary ASC, FK_id_Line ASC;";
				c= myDataBase.rawQuery(sql, selectionArgs);
			}
		    catch (Exception e) {
		        Log.e(Config.LOGTAG, "DataStore.readAllTransferDistinctTypeOfTransportByIdStationItinerary(int idStation, int idItinerary, int distance)" + e.toString());
		    }		
			return c;
		}
		
		protected ArrayList<BusStation> CreateListAllBusStations(int idStation) {
			if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.CreateListAllBusStations(int idStation)");
			int _id;
			int fk_id_Itinerary;
			int fk_id_Line;
			String destination;
			int firstBusMeters = -1;
			Calendar firstBus = null;
			Calendar secondBus = null;
			int secondBusMeters = -1;

			ArrayList<BusStation> resultList = new ArrayList<BusStation>();

		    // Function to retrieve all transferables from idItinerary where transport are favortites
		    Cursor c = createAllBusStation(idStation);
		    try {
			    while (c.moveToNext()){
			    	//fk_id_Station = idStation;
			    	fk_id_Itinerary = c.getInt(c.getColumnIndex(col_BusStation_FK_id_Itinerary));
			    	destination = c.getString(c.getColumnIndex(col_BusStation_Destination));
			    	fk_id_Line = c.getInt(c.getColumnIndex(col_BusStation_FK_id_Line));
			    	BusStation bs = new BusStation(-1, idStation, fk_id_Itinerary, fk_id_Line, destination, 
			    			firstBus, firstBusMeters, secondBus, secondBusMeters);
			    	try{
			    		_id = (int) insertBusStation(bs);
				    	if (_id != UNSUCCESFUL_INSERT) {
				    		// after insert we have the _id
				    		bs.set_id(_id);
				    		Itinerary it = null;
				    		it = getItinerary(fk_id_Itinerary);
				    		bs.setItinerary(it);
				    		Line ln = getLine(fk_id_Line);
				    		bs.setLine(ln);
				    		
				    		resultList.add(bs);				    		
				    	}
			    	}catch (Exception e) {
					        Log.e(Config.LOGTAG, "DataStore.readAllTransferDistinctTypeOfTransportByIdStation(idStation, idLine, distance) " + e.toString());
			    	}
		        }
		    }
		    catch (Exception e) {
		        Log.e(Config.LOGTAG, "DataStore.readAllTransferDistinctTypeOfTransportByIdStation(idStation, idLine, distance) " + e.toString());
		    } finally {
			    if(c != null) c.close();
		    }
		    return resultList;	
		}
		
/*	
SELECT * FROM BusStation WHERE FK_id_Station = 1000071;
			
BusStation
*/	
			
		private Cursor readAllBusStation(int idStation){
			if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.readAllBusStation(int idStation)");
			String[] selectionArgs;
			String sql;
			
			Cursor c=null;
			try{
				selectionArgs= new String[]{String.valueOf(idStation)};
				sql = "SELECT * FROM BusStation WHERE FK_id_Station = ? ORDER BY FirstBus ASC";
				c = myDataBase.rawQuery(sql, selectionArgs);
			}
		    catch (Exception e) {
		        Log.e(Config.LOGTAG, "DataStore.readAllTransferDistinctTypeOfTransportByIdStationItinerary(int idStation, int idItinerary, int distance)" + e.toString());
		    }		
			return c;
		}

		protected ArrayList<BusStation> getListAllBusStations(int idStation) {
			if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.getListAllBusStations(int idStation)");
			int _id;
			int fk_id_Itinerary;
			int fk_id_Line;
			String destination;
			int firstBusMeters;
			Calendar firstBus;
			Calendar secondBus;
			int secondBusMeters;

			ArrayList<BusStation> resultList = new ArrayList<BusStation>();

		    // Function to retrieve all transferables from idItinerary where transport are favortites
		    Cursor c = readAllBusStation(idStation);
		    try {
			    while (c.moveToNext()){
			    	_id = c.getInt(c.getColumnIndex("_id"));
			    	//fk_id_Station = idStation;
			    	fk_id_Itinerary = c.getInt(c.getColumnIndex(col_BusStation_FK_id_Itinerary));
			    	fk_id_Line = c.getInt(c.getColumnIndex(col_BusStation_FK_id_Line));
			    	destination = c.getString(c.getColumnIndex(col_BusStation_Destination));
			    	if (c.getString(c.getColumnIndex(col_BusStation_FirstBus)) != null){
			    		firstBus = TimeUtils.MillisecondsLocalTime(c.getString(c.getColumnIndex(col_BusStation_FirstBus)), Config.MyTimeZone);
			    	} else {
			    		firstBus = null;
			    	}
			    	firstBusMeters = c.getInt(c.getColumnIndex(col_BusStation_FirstBusMeters));
			    	if (c.getString(c.getColumnIndex(col_BusStation_SecondBus)) != null){
			    		secondBus = TimeUtils.MillisecondsLocalTime(c.getString(c.getColumnIndex(col_BusStation_SecondBus)), Config.MyTimeZone);
			    	} else {
			    		secondBus = null;
			    	}
			    	secondBusMeters = c.getInt(c.getColumnIndex(col_BusStation_SecondBusMeters));
			    	BusStation bs = new BusStation(_id, idStation, fk_id_Itinerary, fk_id_Line, 
			    			destination, firstBus, firstBusMeters, secondBus, secondBusMeters);
			    	// information from Itinerary at BusStation info EMT
			    	bs.setItinerary(getItinerary(fk_id_Itinerary));
			    	Line ln = getLine(fk_id_Line);
			    	bs.setLine(ln);
		            resultList.add(bs);
		        }
		    }
		    catch (Exception e) {
		        Log.e(Config.LOGTAG, "DataStore.readAllTransferDistinctTypeOfTransportByIdStation(idStation, idLine, distance) " + e.toString());
		    } finally {
			    if(c != null) c.close();
		    }
		    return resultList;	
		}
		
		/*
		SELECT Itinerary._id AS _id, FK_id_Transport, Code, Name, ColorHEX, FavoriteItinerary, FK_id_TypeTransport 
		FROM Itinerary INNER JOIN Transport ON FK_id_Transport = Transport._id 
		WHERE FavouriteTransport = 1 AND Itinerary._id = 1000001 ORDER BY Itinerary._id ASC;
	*/	
		//private Itinerary 
		private Cursor readItinerary(int idItinerary){		
			if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.readItinerary(int idItinerary)"); 
			Cursor c = null;
			try {
		
				//Create new querybuilder
				SQLiteQueryBuilder _QB = new SQLiteQueryBuilder();
				
				//Specify Itinerary table AND add JOIN to Transport table only for FavouriteTransport
				_QB.setTables(T_Itinerary + " INNER JOIN " + T_Transport);
				_QB.appendWhere(T_Itinerary + "." + col_Itinerary_FK_id_Transport + " = " + col_Transport_id);
				
				String projectionIn[] = new String[] { col_Itinerary_id + " AS _id" , T_Itinerary + "." + col_Itinerary_Code,
						T_Itinerary + "." + col_Itinerary_FK_id_Transport, T_Itinerary + "." + col_Itinerary_Name, 
						T_Itinerary + "." + col_Itinerary_ColorHEX, T_Itinerary + "." + col_Itinerary_FavoriteItinerary,
						T_Itinerary + "." + col_Itinerary_DisabledFacilities, T_Transport + "." + col_Transport_FK_id_TypeTransport};
				String selection = T_Transport + "." + col_Transport_FavouriteTransport + " =? AND " +
						col_Itinerary_id + " = ?";
				
				String[] selectionArgs = new String[] {String.valueOf(Config.TRUE), String.valueOf(idItinerary)};
				String sortOrder = col_Itinerary_id + " ASC";
				//Get cursor
				c = _QB.query(myDataBase, projectionIn, selection, selectionArgs, null, null, sortOrder);
			}
	        catch (Exception e) {
	            Log.e(Config.LOGTAG, "DataStore.readItinerary(int idItinerary) " + e.toString());
	        }	
			return c;
		}

		protected Itinerary getItinerary(int idItinerary) {
			if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.getItinerary(int idItinearary)");    		 	 	 		

			int myID;
			int myFK_id_Transport;
			String myCode;
			String myName;
			//String myColorHEX;
			boolean myFavorite;
			boolean myDisabledFacilities;
			int myTypeOfTransport;
			
		    Itinerary it = null;

		    // Function to retrieve all values from Itinerary
		    Cursor c = readItinerary(idItinerary);
		    try {
			    if (c.moveToNext())
			    {
			    	myID = c.getInt(c.getColumnIndex("_id"));
					myFK_id_Transport = c.getInt(c.getColumnIndex(col_Itinerary_FK_id_Transport));
					myCode = c.getString(c.getColumnIndex(col_Itinerary_Code));
					myName = c.getString(c.getColumnIndex(col_Itinerary_Name));
					//myColorHEX = c.getString(c.getColumnIndex(col_Itinerary_ColorHEX));
					myFavorite = getBoolean(c.getString(c.getColumnIndex(col_Itinerary_FavoriteItinerary)));
					myDisabledFacilities = getBoolean(c.getString(c.getColumnIndex(col_Itinerary_DisabledFacilities)));
					myTypeOfTransport = c.getInt(c.getColumnIndex(col_Transport_FK_id_TypeTransport));
			    	
			    	it = new Itinerary(myID, myFK_id_Transport, myCode, myName, 
			    			myFavorite, myDisabledFacilities, myTypeOfTransport);
		            //retrieve Lines from id_Itinerary, if our Itinerary is Bike only get first element
//		            if (TypeTransport.BICIPALMA.equals(TypeTransport.getTypeTransport(myTypeOfTransport))){
//		            	// BICIPALMA has only a Virtual Line
//		            	it.setLines(getOnlyFirstLine(myID, Config.INVALID_ROW_ID));
//		            } else {
//			            it.setLines(getAllLines(myID, Config.INVALID_ROW_ID));
//		            }	     
		            //retrieve Transfer from favorite type of transport from id_Itinerary
		            //it.setListTransfer(getAllTransferDistinctTypeOfTransportByIdItinerary(myID, distance));
			    }
		    }
		    catch (Exception e) {
		        Log.e(Config.LOGTAG, "DataStore.getAllItinerary() " + e.toString());
		    } finally {
			    if(c != null) c.close();
		    }
		    return it;
		}
		
		/*	
		UPDATE BikeStation SET Capacity = ?, BikeAvailable = ?, ParksFree = ? WHERE _id = (SELECT
			_id FROM CabOperator WHERE FK_id_Station = ? limit 1);
	*/	
		//private ArrayList<Station> of BikeStation
		private synchronized boolean writeBusStations(Station pStation){
			if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.writeBusStations(Station pStation)");
			//result for each BusStation
			int res = 0;
			int res2 = 0;
			Iterator<BusStation> bst = pStation.getBusStations().iterator();
			// Table BikeStation
			String table = T_BusStation;
			String whereClause = col_BusStation_id + " =?";

			String table2 = T_Station;
			String whereClause2 = col_Station_id + " =?";
			while(bst.hasNext()){
				BusStation bs = bst.next();
				//Changing data
				ContentValues values = new ContentValues();
				//values.put("_id", String.valueOf(bs.get_id()));
				if (bs.getFirstBus() != null){
					values.put(col_BusStation_FirstBus, String.valueOf(bs.getFirstBus().getTimeInMillis()));
					values.put(col_BusStation_FirstBusMeters, String.valueOf(bs.getFirsBusMeters()));
				} else {
					values.putNull(col_BusStation_FirstBus);
					values.put(col_BusStation_FirstBusMeters, -1);
				}
				if (bs.getSecondBus() != null){
					values.put(col_BusStation_SecondBus, String.valueOf(bs.getSecondBus().getTimeInMillis()));
					values.put(col_BusStation_FirstBusMeters, String.valueOf(bs.getSecondBusMeters()));
				} else {
					values.putNull(col_BusStation_SecondBus);
					values.put(col_BusStation_SecondBusMeters, -1);
				}
				String[] whereArgs = new String[]{String.valueOf(bs.get_id())};	
				if (values.size() > 0){			
					try{ 
						//database operation
						int resul = myDataBase.update(table, values, whereClause, whereArgs);
						res = resul == 1 ? res + 1 : res;
					}
				    catch (Exception e) {
			            Log.e(Config.LOGTAG, "DataStore.writeBusStations(Station pStation) " + 
			            			bs.getDestination() + e.toString());
			        }
				} else {
					res = res +1;
				}
				
				//Changing data TimeStamp in myStation
				ContentValues values2 = new ContentValues();
				values2.put(col_Station_TimeStamp, String.valueOf(pStation.getTimeStamp().getTimeInMillis()));
				String[] whereArgs2 = new String[]{String.valueOf(pStation.get_id())};	
				
				try{ 
					//database operation
					int resul2 = myDataBase.update(table2, values2, whereClause2, whereArgs2);
					res2 = resul2 == 1 ? res2 + 1 : res2;
				}
			    catch (Exception e) {
		            Log.e(Config.LOGTAG, "DataStore.writeBusStations(Station pStation) " + 
		            			pStation.get_id() + e.toString());
		        }
			}
			// num of succeful operations
			return (res == pStation.getBusStations().size() && res2 == res);
		}
		
		// write data BikeStations to DB
		protected boolean setBusStations(Station pStation){
			if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.setBusStations(Station pStation)");
			boolean res = writeBusStations(pStation);
			return res;
		}
		
		//private ArrayList<Place> 
		private Cursor readAllPlaces(boolean onlyFavorite){
			if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.readAllPlaces(boolean onlyFavorite)");
			Cursor c= null;
			try{
				if (onlyFavorite){
					try {				
						//Create new querybuilder
						SQLiteQueryBuilder _QB = new SQLiteQueryBuilder();
						
						// Distinct TRUE
						_QB.setDistinct(true);
						//Specify Plcae table AND add JOIN to FavouritePlace
						_QB.setTables(T_Place + " INNER JOIN " + T_FavouritePlace);
					
						_QB.appendWhere( col_Place_id + " = " + T_FavouritePlace + "." + col_FavouritePlace_FK_id_Place);

						String projectionIn[] = new String[]{ col_Place_id + " AS _id", T_Place + "." + col_Place_Name, 
								T_Place + "." + col_Place_Address, T_Place + "." + col_Place_Latitude, 
								T_Place + "." + col_Place_Longitude};
						
						//String selection = "T_PTransport + "." + col_Transport_FavouriteTransport + " =?";
						String selection = null;
						//String[] selectionArgs = new String[] {String.valueOf(Config.TRUE)};
						String[] selectionArgs = null;
						
						String sortOrder = T_Place + "." + col_Place_Name + " ASC";
						//Get cursor
						c = _QB.query(myDataBase, projectionIn, selection, selectionArgs, null, null, sortOrder);
					}
			        catch (Exception e) {
			            Log.e(Config.LOGTAG, "DataStore.readAllPlaces(boolean onlyFavorite) " + e.toString());
			        }
				} else {
					try {				
						//Create new querybuilder
						SQLiteQueryBuilder _QB = new SQLiteQueryBuilder();
						
						// Distinct TRUE
						_QB.setDistinct(true);
						//Specify Plcae table AND add JOIN to FavouritePlace
						_QB.setTables(T_Place);
					
						//_QB.appendWhere( col_Place_id + " = " + T_FavouritePlace + "." + col_FavouritePlace_FK_id_Place);

						String projectionIn[] = new String[]{ col_Place_id + " AS _id", T_Place + "." + col_Place_Name, 
								T_Place + "." + col_Place_Address, T_Place + "." + col_Place_Latitude, 
								T_Place + "." + col_Place_Longitude};
						
						//String selection = "T_PTransport + "." + col_Transport_FavouriteTransport + " =?";
						String selection = null;
						//String[] selectionArgs = new String[] {String.valueOf(Config.TRUE)};
						String[] selectionArgs = null;
						
						String sortOrder = T_Place + "." + col_Place_Name + " ASC";
						//Get cursor
						c = _QB.query(myDataBase, projectionIn, selection, selectionArgs, null, null, sortOrder);
					}
			        catch (Exception e) {
			            Log.e(Config.LOGTAG, "DataStore.readAllPlaces(boolean onlyFavorite) " + e.toString());
			        }
				}
			}
	        catch (Exception e) {
	            Log.e(Config.LOGTAG, "DataStore.readAllTypeOfTransport() " + e.toString());
	        }		
			return c;
		}

		protected ArrayList<Place> getAllPlaces(boolean onlyFavorite) {
			if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.getAllPlaces(boolean onlyFavorite)");
			int myID;
			String myName;
			String myAddress;
			GeoPosition myCoordinates;
			Place pl;

			boolean myFavourite;
			
			ArrayList<Place> resultList = new ArrayList<Place>();

		    // Function to retrieve all values from Place
		    Cursor c = readAllPlaces(onlyFavorite);
		    try {
			    while (c.moveToNext())
			    {
			    	myID = c.getInt(c.getColumnIndex("_id"));
			    	// Place
					myName = c.getString(c.getColumnIndex(col_Place_Name));
					myAddress = c.getString(c.getColumnIndex(col_Place_Address));
					myCoordinates = new GeoPosition(c.getString(c.getColumnIndex(col_Place_Latitude)), c.getString(c.getColumnIndex(col_Place_Longitude)));
					pl = new Place( myID, myName, myAddress, myCoordinates, onlyFavorite);
		            // getting if Place is Favourite
		            myFavourite = IsFavouritePlace(myID);
		            pl.setFavourite(myFavourite);

		            resultList.add(pl);		            
		        }
		    }
		    catch (Exception e) {
		    	Log.e(Config.LOGTAG, "DataStore.getAllPlaces(boolean onlyFavorite) " + e.toString());
		    } finally {
			    if(c != null) c.close();
		    }
		    return resultList;
		}
		
		//private ArrayList<Cab> 
		private Cursor readAllCabs(boolean onlyFavorite){
			if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.readAllCabs(boolean onlyFavorite)");
			Cursor c= null;
			try{
				if (onlyFavorite){
					try {				
						//Create new querybuilder
						SQLiteQueryBuilder _QB = new SQLiteQueryBuilder();
						
						// Distinct TRUE
						_QB.setDistinct(true);
						//Specify Cab table AND add JOIN to FavouriteCab
						_QB.setTables(T_Cab + " INNER JOIN " + T_FavouriteCab);
					
						_QB.appendWhere( col_Cab_id + " = " + T_FavouriteCab + "." + col_FavouriteCab_FK_id_Cab);

						String projectionIn[] = new String[]{ col_Cab_id + " AS _id", T_Cab + "." + col_Cab_FK_id_CabOperator, 
								T_Cab + "." + col_Cab_Facebook, T_Cab + "." + col_Cab_Licence, 
								T_Cab + "." + col_Cab_Name, T_Cab + "." + col_Cab_Telephone, T_Cab + "." + col_Cab_Twitter};
						
						String selection = null;
						String[] selectionArgs = null;
						
						String sortOrder = T_Cab + "." + col_Cab_Name + " ASC";
						//Get cursor
						c = _QB.query(myDataBase, projectionIn, selection, selectionArgs, null, null, sortOrder);
					}
			        catch (Exception e) {
			            Log.e(Config.LOGTAG, "DataStore.readAllCabs(boolean onlyFavorite) " + e.toString());
			        }
				} else {
					try {				
						//Create new querybuilder
						SQLiteQueryBuilder _QB = new SQLiteQueryBuilder();
						
						// Distinct TRUE
						_QB.setDistinct(true);
						//Specify Plcae table AND add JOIN to FavouritePlace
						_QB.setTables(T_Cab);
					
						//_QB.appendWhere( col_Place_id + " = " + T_FavouritePlace + "." + col_FavouritePlace_FK_id_Place);

						String projectionIn[] = new String[]{ col_Cab_id + " AS _id", T_Cab + "." + col_Cab_FK_id_CabOperator, 
								T_Cab + "." + col_Cab_Facebook, T_Cab + "." + col_Cab_Licence, 
								T_Cab + "." + col_Cab_Name, T_Cab + "." + col_Cab_Telephone, T_Cab + "." + col_Cab_Twitter};
												
						//String selection = "T_PTransport + "." + col_Transport_FavouriteTransport + " =?";
						String selection = null;
						//String[] selectionArgs = new String[] {String.valueOf(Config.TRUE)};
						String[] selectionArgs = null;
						
						String sortOrder = T_Cab + "." + col_Cab_Name + " ASC";
						//Get cursor
						c = _QB.query(myDataBase, projectionIn, selection, selectionArgs, null, null, sortOrder);
					}
			        catch (Exception e) {
			            Log.e(Config.LOGTAG, "DataStore.readAllCabs(boolean onlyFavorite)) " + e.toString());
			        }
				}
			}
	        catch (Exception e) {
	            Log.e(Config.LOGTAG, "DataStore.readAllCabs(boolean onlyFavorite) " + e.toString());
	        }		
			return c;
		}
	
		protected ArrayList<Cab> getAllCabs(boolean onlyFavorite) {
			if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.getAllCabs(boolean onlyFavorite)");
			
			int myID;
		    int myFK_id_CabOperator;
		    String myFacebook;
			String myLicence;
			String myName;
			String myTelephone;
			String myTwitter;
			//boolean isFavorite;


			ArrayList<Cab> resultList = new ArrayList<Cab>();

		    // Function to retrieve all values from Place
		    Cursor c = readAllCabs(onlyFavorite);
		    try {
			    while (c.moveToNext())
			    {
			    	myID = c.getInt(c.getColumnIndex("_id"));
			    	// Cab
				    myFK_id_CabOperator = c.getInt(c.getColumnIndex(col_Cab_FK_id_CabOperator));
				    myFacebook = c.getString(c.getColumnIndex(col_Cab_Facebook));
					myLicence = c.getString(c.getColumnIndex(col_Cab_Licence));
					myName = c.getString(c.getColumnIndex(col_Cab_Name));
					myTelephone = c.getString(c.getColumnIndex(col_Cab_Telephone));
					myTwitter = c.getString(c.getColumnIndex(col_Cab_Twitter));
					//isFavorite = onlyFavorite;
					
					Cab cb = new Cab( myID, myFK_id_CabOperator, myFacebook, myLicence, myName, myTelephone, myTwitter, onlyFavorite);
		            resultList.add(cb);		            
		        }
		    }
		    catch (Exception e) {
		    	Log.e(Config.LOGTAG, "DataStore.getAllCabs(boolean onlyFavorite) " + e.toString());
		    } finally {
			    if(c != null) c.close();
		    }
		    return resultList;
		}


		//private ArrayList<CabOperator> 
		private Cursor readAllCabOperators(boolean onlyFavorite){
			if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.readAllCabOperators(boolean onlyFavorite)");
			Cursor c= null;
			try{
				if (onlyFavorite){
					try {				
						//Create new querybuilder
						SQLiteQueryBuilder _QB = new SQLiteQueryBuilder();
						
						// Distinct TRUE
						_QB.setDistinct(true);
						//Specify Cab table AND add JOIN to FavouriteCab
						_QB.setTables(T_CabOperator + " INNER JOIN " + T_FavouriteCabOperator);
					
						_QB.appendWhere( col_CabOperator_id + " = " + T_FavouriteCabOperator + 
								"." + col_FavouriteCabOperator_FK_id_CabOperator);

						String projectionIn[] = new String[]{ col_CabOperator_id + " AS _id", 
								T_CabOperator + "." + col_CabOperator_Name,
								T_CabOperator + "." + col_CabOperator_Telephone, 
								T_CabOperator + "." + col_CabOperator_Web};
								
						String selection = null;
						String[] selectionArgs = null;
						
						String sortOrder = T_CabOperator + "." + col_CabOperator_Name + " ASC";
						//Get cursor
						c = _QB.query(myDataBase, projectionIn, selection, selectionArgs, null, null, sortOrder);
					}
			        catch (Exception e) {
			            Log.e(Config.LOGTAG, "DataStore.readAllCabOperators(boolean onlyFavorite) " + e.toString());
			        }
				} else {
					try {				
						//Create new querybuilder
						SQLiteQueryBuilder _QB = new SQLiteQueryBuilder();
						
						// Distinct TRUE
						_QB.setDistinct(true);
						//Specify Plcae table AND add JOIN to FavouritePlace
						_QB.setTables(T_CabOperator);
					
						//_QB.appendWhere( col_Place_id + " = " + T_FavouritePlace + "." + col_FavouritePlace_FK_id_Place);

						String projectionIn[] = new String[]{ col_CabOperator_id + " AS _id", 
								T_CabOperator + "." + col_CabOperator_Name,
								T_CabOperator + "." + col_CabOperator_Telephone, 
								T_CabOperator + "." + col_CabOperator_Web};
						
						//String selection = "T_PTransport + "." + col_Transport_FavouriteTransport + " =?";
						String selection = null;
						//String[] selectionArgs = new String[] {String.valueOf(Config.TRUE)};
						String[] selectionArgs = null;
						
						String sortOrder = T_CabOperator + "." + col_CabOperator_Name + " ASC";
						//Get cursor
						c = _QB.query(myDataBase, projectionIn, selection, selectionArgs, null, null, sortOrder);
					}
			        catch (Exception e) {
			            Log.e(Config.LOGTAG, "DataStore.readAllCabOperators(boolean onlyFavorite)) " + e.toString());
			        }
				}
			}
	        catch (Exception e) {
	            Log.e(Config.LOGTAG, "DataStore.readAllCabOperators(boolean onlyFavorite) " + e.toString());
	        }		
			return c;
		}
	
		protected ArrayList<CabOperator> getAllCabOperators(boolean onlyFavorite) {
			if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.getAllCabOperators(boolean onlyFavorite)");
			
			int myID;
			String myName;
			String myTelephone;
			String myWeb;
			//boolean isFavorite;
			
			ArrayList<CabOperator> resultList = new ArrayList<CabOperator>();

		    // Function to retrieve all values from Place
		    Cursor c = readAllCabOperators(onlyFavorite);
		    try {
			    while (c.moveToNext())
			    {
			    	myID = c.getInt(c.getColumnIndex("_id"));
			    	// CabOperator
					myName = c.getString(c.getColumnIndex(col_CabOperator_Name));
					myTelephone = c.getString(c.getColumnIndex(col_CabOperator_Telephone));
					myWeb = c.getString(c.getColumnIndex(col_CabOperator_Web));
					//isFavorite = onlyFavorite;
					
					CabOperator cb = new CabOperator( myID, myName, myTelephone, myWeb, onlyFavorite);
		            resultList.add(cb);		            
		        }
		    }
		    catch (Exception e) {
		    	Log.e(Config.LOGTAG, "DataStore.getAllCabOperators(boolean onlyFavorite) " + e.toString());
		    } finally {
		    	if(c != null) c.close();
			}
		    return resultList;
		}
		
		//private Place 
		private long writeIsFavoritePlace(int place_id, boolean isFavorite){
			if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.writeIsFavoritePlace(int place_id, boolean isFavorite)");
			long res;
			if (isFavorite){
				//insert
				ContentValues value = new ContentValues();
				value.put(col_FavouritePlace_FK_id_Place, place_id);			
				String nullColumnHack = null;
				
				try{ 
					//database operation			
					res = myDataBase.insert(T_FavouritePlace, nullColumnHack, value);
				} catch (Exception e) {
			        Log.e(Config.LOGTAG, "DataStore.writeIsFavoritePlace(int place_id, boolean isFavorite, String name).insert " + e.toString());
			        res = -1;
			    }
					
			} else {
				//delete
				String whereClause = col_FavouritePlace_FK_id_Place + " = ?";
				String[] whereArgs ={ String.valueOf(place_id) };
				try{ 
					//database operation			
					res = myDataBase.delete(T_FavouritePlace, whereClause, whereArgs);
				} catch (Exception e) {
			        Log.e(Config.LOGTAG, "DataStore.writeIsFavoritePlace(int place_id, boolean isFavorite, String name).delete " + e.toString());
			        res = -1;
			    }
			}
			return res;
		}
		

		protected boolean setIsFavoritePlace(Place myPlace, boolean isFavorite) {
			if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore. setIsFavoritePlace(Place myPlace, boolean isFavorite)");
			
			return (writeIsFavoritePlace(myPlace.get_id(), isFavorite) > 0);
		}
		
		//private isFavouritePlace
		private Cursor readFavouritePlace(int idPlace){
			if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.readFavouritePlace(" + idPlace + ")");
			Cursor c=null;
			try{
				String [] columns=new String[]{"_id", col_FavouritePlace_Name};
				String selection = col_FavouritePlace_FK_id_Place + " =?";
				String[] where =  new String[] {String.valueOf(idPlace)};
				c = myDataBase.query(T_FavouritePlace, columns, selection, where, null, null, null);
			}
		    catch (Exception e) {
		        Log.e(Config.LOGTAG, "DataStore.readFavouritePlace(int idPlace) " + e.toString());
		    }
			return c;
		}
		
		//FavouritePlace 
		protected FavouritePlace getFavouritePlace(int idPlace){
			if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.getFavouriteLine(idLine)");
			int myID;
			String myName;
			
			FavouritePlace fp = null;
		    // Function to retrieve value from FavoriteLine
		    Cursor c = readFavouritePlace(idPlace);
		    
	        try {
	        	if(c.moveToFirst()) {
	        		myID = c.getInt(c.getColumnIndex("_id"));
	        		myName = c.getString(c.getColumnIndex(col_FavouritePlace_Name));
	        		
	        		fp = new FavouritePlace(myID, idPlace, myName);
	        	}
	        }
	        catch (Exception e) {
		        Log.e(Config.LOGTAG, "DataStore.getFavouriteLine(idLine)" + e.toString());
		    } finally {
		    	if(c != null) c.close();
		    }
		    return fp;
		}
		
		//FavouritePlace 
		protected boolean IsFavouritePlace(int idPlace){
			if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.IsFavouritePlace(" + idPlace + ")");
			boolean res= false;
			try {
				res = (readFavouritePlace(idPlace).getCount()>0);
			}
	        catch (Exception e) {
	            Log.e(Config.LOGTAG, "DataStore.IsFavouritePlace(int idPlace) " + e.toString());
	        }
			return res;
			
		}
		
		//private isFavouritePlace
		private Cursor readFavouriteCab(int idCab){
			if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.readFavouriteCab(" + idCab + ")");
			Cursor c=null;
			try{
				String [] columns=new String[]{"_id", col_FavouriteCab_Name};
				String selection = col_FavouriteCab_FK_id_Cab + " =?";
				String[] where =  new String[] {String.valueOf(idCab)};
				c = myDataBase.query(T_FavouriteCab, columns, selection, where, null, null, null);
			}
		    catch (Exception e) {
		        Log.e(Config.LOGTAG, "DataStore.readFavouriteCab(int idCab) " + e.toString());
		    }
			return c;
		}
		
		//private Place 
		private long writeIsFavoriteCab(int cab_id, boolean isFavorite){
			if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.writeIsFavoritePlace(int place_id, boolean isFavorite)");
			long res;
			if (isFavorite){
				//insert
				ContentValues value = new ContentValues();
				value.put(col_FavouriteCab_FK_id_Cab, cab_id);			
				String nullColumnHack = null;
				
				try{ 
					//database operation			
					res = myDataBase.insert(T_FavouriteCab, nullColumnHack, value);
				} catch (Exception e) {
			        Log.e(Config.LOGTAG, "DataStore.writeIsFavoriteCab(int cab_id, boolean isFavorite, String name).insert " + e.toString());
			        res = -1;
			    }
					
			} else {
				//delete
				String whereClause = col_FavouriteCab_FK_id_Cab + " = ?";
				String[] whereArgs ={ String.valueOf(cab_id) };
				try{ 
					//database operation			
					res = myDataBase.delete(T_FavouriteCab, whereClause, whereArgs);
				} catch (Exception e) {
			        Log.e(Config.LOGTAG, "DataStore.writeIsFavoriteCab(int Cab_id, boolean isFavorite, String name).delete " + e.toString());
			        res = -1;
			    }
			}
			return res;
		}
		

		protected boolean setIsFavoriteCab(Cab myCab, boolean isFavorite) {
			if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore. setIsFavoriteCab(Cab myCab, boolean isFavorite)");
			
			return (writeIsFavoriteCab(myCab.get_id(), isFavorite) > 0);
		}
		
		//FavouritePlace 
		protected boolean IsFavouriteCab(int idCab){
			if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.IsFavouriteCab(" + idCab + ")");
			boolean res= false;
			try {
				res = (readFavouriteCab(idCab).getCount()>0);
			}
		        catch (Exception e) {
		            Log.e(Config.LOGTAG, "DataStore.IsFavouriteCab(int idCab) " + e.toString());
		        }
			return res;
			
		}
		
		//private Place 
		private long writeIsFavoriteCabOperator(int cabOperator_id, boolean isFavorite){
			if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore.writeIsFavoriteCabOperator(int cabOperator_id, boolean isFavorite)");
			long res;
			if (isFavorite){
				//insert
				ContentValues value = new ContentValues();
				value.put(col_FavouriteCabOperator_FK_id_CabOperator, cabOperator_id);			
				String nullColumnHack = null;
				
				try{ 
					//database operation			
					res = myDataBase.insert(T_FavouriteCabOperator, nullColumnHack, value);
				} catch (Exception e) {
			        Log.e(Config.LOGTAG, "DataStore.writeIsFavoriteCabOperator(int cabOperator_id, boolean isFavorite, String name).insert " + e.toString());
			        res = -1;
			    }
					
			} else {
				//delete
				String whereClause = col_FavouriteCabOperator_FK_id_CabOperator + " = ?";
				String[] whereArgs ={ String.valueOf(cabOperator_id) };
				try{ 
					//database operation			
					res = myDataBase.delete(T_FavouriteCabOperator, whereClause, whereArgs);
				} catch (Exception e) {
			        Log.e(Config.LOGTAG, "DataStore.writeIsFavoriteCabOperator(int CabOperator_id, boolean isFavorite, String name).delete " + e.toString());
			        res = -1;
			    }
			}
			return res;
		}

		protected boolean setIsFavoriteCabOperator(CabOperator myCabOperator, boolean isFavorite) {
			if (Config.DEBUG) Log.d(Config.LOGTAG, "DataStore. setIsFavoriteCabOperator(CabOperator myCabOperator, boolean isFavorite)");
			
			return (writeIsFavoriteCabOperator(myCabOperator.get_id(), isFavorite) > 0);
		}

}

	
	// not implemented in version v 1.0
	//private TransportOnDemand TransportOD;
	//private BikeStation Bike;	

	
	// To complete Line it is not used in this first version
	// AlterMap of the line ONCE
	//private ArrayList<LineAlterMap> LinesAlterMap;
	
		// not implemented in version v 1.0
		//private TransportOnDemand TransportOD;
		//private BikeStation Bike;	
		
		// To complete Line it is not used in this firs version
		// AlterMap of the line ONCE
		//private ArrayList<LineAlterMap> LinesAlterMap;
		
