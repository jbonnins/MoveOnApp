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


import com.sanbo.datamodel.Cab;
import com.sanbo.datamodel.Itinerary;
import com.sanbo.datamodel.Line;
import com.sanbo.datamodel.Place;
import com.sanbo.datamodel.Station;
import com.sanbo.fragment.base.AppMainTabActivity;
import com.sanbo.fragment.base.BaseFragment;

public class SavingState {
	public static final int INVALID_ROW_ID = -1;


	// Instance from PublicTransport
    // SINGLETON DEFINITION
	private static SavingState INSTANCE = null;
	// reference to AppMainTabActivity;
	private AppMainTabActivity myActivity = null;

	// reference to BaseActivity
	private BaseFragment myFragment = null;
	//Preferences for Tab Navigation
	private String tabInit = null;
	//For TabA
	private int groupItineary = INVALID_ROW_ID;
	private int firstVisibleLine = INVALID_ROW_ID;
	//For TabA-2 + TabA
	private int childLine = INVALID_ROW_ID;
	private Itinerary myItinerary = null;
	private Line myLine = null;
	private boolean closestStation = false;
	//For TabA-3 + TabA-2
	private int itemStation = INVALID_ROW_ID;
	private boolean oneWay = true;
	//For TabA-3
	private Station myStation = null;
	private int itemBusStation = INVALID_ROW_ID;
	//For TabB
	private Station myStationB = null;
	private int itemBusStationB = INVALID_ROW_ID;
	//For TabC
	private Station myStationC = null;
	private int itemBusStationC = INVALID_ROW_ID;
	//For active Station
	private Station myActiveStation = null;
	private int myActiveBusStationSelected = INVALID_ROW_ID;
	// depends on Station and FK_id_Transport, if we don't have valid data
	// show data from timetable if we don't have Internet connection
	//For TabE
	private Itinerary myItineraryBike = null;
	private Station myBikeStation = null;
	private int itemBikeStation = INVALID_ROW_ID;
	
	// favorite view
	private boolean favoriteOn = false;
	private boolean lastFavorite = true;

	// favorite place
	private int itemPlace =  INVALID_ROW_ID;
	private Place myPlace = null;
	// favorite cab
	private int itemCab =  INVALID_ROW_ID;
	private Cab myCab = null;
	// favorite cabOperator
	private int itemCabOperator =  INVALID_ROW_ID;
	private Cab myCabOperator = null;
	
	//BusStation we use Station
	//BikeStation we use ArrayList<Station>
	//TrainStation we use Station, not implemented yet

	// Creates instance of public transport
	private synchronized static void createInstance()
	{
		if (INSTANCE == null)
			INSTANCE = new SavingState();
		return;
	}
	  	
	/**
	 * @return the iNSTANCE
	 */
	public synchronized static SavingState getInstance() {
		if (INSTANCE == null)
			createInstance();
		return INSTANCE;
	}

	/**
	 * 
	 */
	private SavingState() {
		super();
	}

	/**
	 * @return the myActivity
	 */
	public AppMainTabActivity getMyActivity() {
		return myActivity;
	}

	/**
	 * @param myActivity the myActivity to set
	 */
	public void setMyActivity(AppMainTabActivity myActivity) {
		this.myActivity = myActivity;
	}
	
	/**
	 * @return the mFragment
	 */
	public BaseFragment getMyFragment() {
		return myFragment;
	}

	/**
	 * @param mFragment the mFragment to set
	 */
	public void setMyFragment(BaseFragment mFragment) {
		this.myFragment = mFragment;
	}

	/**
	 * @return the tabInit
	 */
	public String getTabInit() {
		return tabInit;
	}

	/**
	 * @param tabInit the tabInit to set
	 */
	public void setTabInit(String tabInit) {
		this.tabInit = tabInit;
	}

	/**
	 * @return the groupItineary
	 */
	public int getGroupItineary() {
		return groupItineary;
	}

	/**
	 * @param groupItineary the groupItineary to set
	 */
	public void setGroupItineary(int groupItineary) {
		this.groupItineary = groupItineary;
	}

	/**
	 * @return the firstVisibleLine
	 */
	public int getFirstVisibleLine() {
		return firstVisibleLine;
	}

	/**
	 * @param firstVisibleLine the firstVisibleLine to set
	 */
	public void setFirstVisibleLine(int firstVisibleLine) {
		this.firstVisibleLine = firstVisibleLine;
	}

	/**
	 * @return the childLine
	 */
	public int getChildLine() {
		return childLine;
	}

	/**
	 * @param childLine the childLine to set
	 */
	public void setChildLine(int childLine) {
		this.childLine = childLine;
	}

	/**
	 * @return the myItinerary
	 */
	public Itinerary getMyItinerary() {
		return myItinerary;
	}

	/**
	 * @param myItinerary the myItinerary to set
	 */
	public void setMyItinerary(Itinerary myItinerary) {
		this.myItinerary = myItinerary;
	}

	/**
	 * @return the myLine
	 */
	public Line getMyLine() {
		return myLine;
	}

	/**
	 * @param myLine the myLine to set
	 */
	public void setMyLine(Line myLine) {
		this.myLine = myLine;
	}

	/**
	 * @return the closestStation
	 */
	public boolean isClosestStation() {
		return closestStation;
	}

	/**
	 * @param closestStation the closestStation to set
	 */
	public void setClosestStation(boolean closestStation) {
		this.closestStation = closestStation;
	}

	/**
	 * @return the itemStation
	 */
	public int getItemStation() {
		return itemStation;
	}

	/**
	 * @param itemStation the itemStation to set
	 */
	public void setItemStation(int itemStation) {
		this.itemStation = itemStation;
	}

	/**
	 * @return the myStation
	 */
	public Station getMyStation() {
		return myStation;
	}

	/**
	 * @param myStation the myStation to set
	 */
	public void setMyStation(Station myStation) {
		this.myStation = myStation;
	}

	/**
	 * @return the oneWay
	 */
	public boolean isOneWay() {
		return oneWay;
	}

	/**
	 * @param oneWay the oneWay to set
	 */
	public void setOneWay(boolean oneWay) {
		this.oneWay = oneWay;
	}

	/**
	 * @return the itemBusStation
	 */
	public int getItemBusStation() {
		return itemBusStation;
	}

	/**
	 * @param itemBusStation the itemBusStation to set
	 */
	public void setItemBusStation(int itemBusStation) {
		this.itemBusStation = itemBusStation;
	}

	/**
	 * @return the myStationB
	 */
	public Station getMyStationB() {
		return myStationB;
	}

	/**
	 * @param myStationB the myStationB to set
	 */
	public void setMyStationB(Station myStationB) {
		this.myStationB = myStationB;
	}

	/**
	 * @return the itemBusStationB
	 */
	public int getItemBusStationB() {
		return itemBusStationB;
	}

	/**
	 * @param itemBusStationB the itemBusStationB to set
	 */
	public void setItemBusStationB(int itemBusStationB) {
		this.itemBusStationB = itemBusStationB;
	}

	/**
	 * @return the myStationC
	 */
	public Station getMyStationC() {
		return myStationC;
	}

	/**
	 * @param myStationC the myStationC to set
	 */
	public void setMyStationC(Station myStationC) {
		this.myStationC = myStationC;
	}

	/**
	 * @return the itemBusStationC
	 */
	public int getItemBusStationC() {
		return itemBusStationC;
	}

	/**
	 * @param itemBusStationC the itemBusStationC to set
	 */
	public void setItemBusStationC(int itemBusStationC) {
		this.itemBusStationC = itemBusStationC;
	}

	/**
	 * @return the myActiveStation
	 */
	public Station getMyActiveStation() {
		return myActiveStation;
	}

	/**
	 * @param myActiveStation the myActiveStation to set
	 */
	public void setMyActiveStation(Station myActiveStation) {
		this.myActiveStation = myActiveStation;
	}

	/**
	 * @return the myActiveBusStationSelected
	 */
	public int getMyActiveBusStationSelected() {
		return myActiveBusStationSelected;
	}

	/**
	 * @param myActiveBusStationSelected the myActiveBusStationSelected to set
	 */
	public void setMyActiveBusStationSelected(int myActiveBusStationSelected) {
		this.myActiveBusStationSelected = myActiveBusStationSelected;
	}

	/**
	 * @return the myItineraryBike
	 */
	public Itinerary getMyItineraryBike() {
		return myItineraryBike;
	}

	/**
	 * @param myItineraryBike the myItineraryBike to set
	 */
	public void setMyItineraryBike(Itinerary myItineraryBike) {
		this.myItineraryBike = myItineraryBike;
	}

	/**
	 * @return the myBikeStation
	 */
	public Station getMyBikeStation() {
		return myBikeStation;
	}

	/**
	 * @param myBikeStation the myBikeStation to set
	 */
	public void setMyBikeStation(Station myBikeStation) {
		this.myBikeStation = myBikeStation;
	}

	/**
	 * @return the itemBikeStation
	 */
	public int getItemBikeStation() {
		return itemBikeStation;
	}

	/**
	 * @param itemBikeStation the itemBikeStation to set
	 */
	public void setItemBikeStation(int itemBikeStation) {
		this.itemBikeStation = itemBikeStation;
	}

	/**
	 * @return the favoriteOn
	 */
	public boolean isFavoriteOn() {
		return favoriteOn;
	}

	/**
	 * @param favoriteOn the favoriteOn to set
	 */
	public void setFavoriteOn(boolean favoriteOn) {
		this.favoriteOn = favoriteOn;
	}

	/**
	 * @return the lastFavorite
	 */
	public boolean isLastFavorite() {
		return lastFavorite;
	}

	/**
	 * @param lastFavorite the lastFavorite to set
	 */
	public void setLastFavorite(boolean lastFavorite) {
		this.lastFavorite = lastFavorite;
	}

	/**
	 * @return the itemPlace
	 */
	public int getItemPlace() {
		return itemPlace;
	}

	/**
	 * @param itemPlace the itemPlace to set
	 */
	public void setItemPlace(int itemPlace) {
		this.itemPlace = itemPlace;
	}

	/**
	 * @return the myPlace
	 */
	public Place getMyPlace() {
		return myPlace;
	}

	/**
	 * @param myPlace the myPlace to set
	 */
	public void setMyPlace(Place myPlace) {
		this.myPlace = myPlace;
	}

	/**
	 * @return the itemCab
	 */
	public int getItemCab() {
		return itemCab;
	}

	/**
	 * @param itemCab the itemCab to set
	 */
	public void setItemCab(int itemCab) {
		this.itemCab = itemCab;
	}

	/**
	 * @return the itemCabOperator
	 */
	public int getItemCabOperator() {
		return itemCabOperator;
	}

	/**
	 * @param itemCabOperator the itemCabOperator to set
	 */
	public void setItemCabOperator(int itemCabOperator) {
		this.itemCabOperator = itemCabOperator;
	}

	/**
	 * @return the myCabOperator
	 */
	public Cab getMyCabOperator() {
		return myCabOperator;
	}

	/**
	 * @param myCabOperator the myCabOperator to set
	 */
	public void setMyCabOperator(Cab myCabOperator) {
		this.myCabOperator = myCabOperator;
	}

	/**
	 * @param myCab the myCab to set
	 */
	public void setMyCab(Cab myCab) {
		this.myCab = myCab;
	}

	/**
	 * @return the myCab
	 */
	public Cab getMyCab() {
		return myCab;
	}

}

