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
/*
-- _id	FK_id_Transport	Code Name	ColorHEX	Itinerary
CREATE TABLE "Itinerary" ("_id" INTEGER PRIMARY KEY ASC AUTOINCREMENT NOT NULL UNIQUE, "FK_id_Transport" INTEGER NOT NULL, "Code" TEXT NOT NULL, "Name" TEXT NOT NULL, "ColorHEX" TEXT);
*/

import com.sanbo.enumerated.TypeTransport;

public class Itinerary
{
	private int _id;
	private int FK_id_Transport;
	private String Code;
	private String Name;
	private String ColorHEX;
	private boolean DisabledFacilities;
	//this data in order to serve our ExpandableList
	private ArrayList<Line> lines = null;	
	//Itinerary could be favority, without any line as favorite
	private boolean isFavourite;
	//Image of TypeOfTransport from Itinerary
	private int typeOfTransport;
		//Transfers by distinct TypeOfTransport
	private ArrayList<ItineraryTypeTransport> listTransfer;

	//if is checked 
	//private boolean isChecked;
	
	/**
	 * @param _id
	 * @param fK_id_Transport
	 * @param code
	 * @param name
	 * @param isFavorite
	 */
	public Itinerary(int _id, int fK_id_Transport, String code,
			String name, boolean isFavorite, boolean haveDisabledFacilities, int typeOfTransport) {
		this._id = _id;
		FK_id_Transport = fK_id_Transport;
		Code = code;
		Name = name;
		this.isFavourite = isFavorite;
		this.DisabledFacilities = haveDisabledFacilities;
		this.setTypeOfTransport(typeOfTransport);
	}
	
	// info from EMT Palma
	public Itinerary(String code){
		_id = -1;
		Code = code;
		setTypeOfTransport(TypeTransport.BUSPALMA.getValue());	
	}

	/**
	 * @param _id
	 * @param fK_id_Transport
	 * @param code
	 * @param name
	 * @param colorHEX
	 */
	public Itinerary(int id, String code, String colorHEX) {
		_id = id;
		FK_id_Transport = id;
		Code = code;
		ColorHEX = colorHEX;
	}
	
	/**
	 * @return the _id
	 */
	public int get_id() {
		return _id;
	}
	/**
	 * @param _id the _id to set
	 */
	public void set_id(int _id) {
		this._id = _id;
	}
	/**
	 * @return the fK_id_TypeTransport
	 */
	public int getFK_id_Transport() {
		return FK_id_Transport;
	}
	/**
	 * @param fK_id_Transport the fK_id_Transport to set
	 */
	public void setFK_id_Transport(int fK_id_Transport) {
		FK_id_Transport = fK_id_Transport;
	}
	/**
	 * @return the code
	 */
	public String getCode() {
		return Code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		Code = code;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return Name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		Name = name;
	}
	/**
	 * @return the colorHEX
	 */
	public String getColorHEX() {
		return ColorHEX;
	}
	/**
	 * @param colorHEX the colorHEX to set
	 */
	public void setColorHEX(String colorHEX) {
		ColorHEX = colorHEX;
	}
	/**
	 * @return the lines
	 */
	public ArrayList<Line> getLines() {
		return lines;
	}
	/**
	 * @param lines the lines to set
	 */
	public void setLines(ArrayList<Line> lines) {
		this.lines = lines;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Code == null) ? 0 : Code.hashCode());
		result = prime * result
				+ ((ColorHEX == null) ? 0 : ColorHEX.hashCode());
		result = prime * result + (DisabledFacilities ? 1231 : 1237);
		result = prime * result + FK_id_Transport;
		result = prime * result + ((Name == null) ? 0 : Name.hashCode());
		result = prime * result + _id;
		result = prime * result + (isFavourite ? 1231 : 1237);
		result = prime * result + typeOfTransport;
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Itinerary other = (Itinerary) obj;
		if (_id != other._id)
			return false;
		return true;
	}
	/**
	 * @return the isFavourite
	 */
	public boolean isFavourite() {
		return isFavourite;
	}
	/**
	 * @param isFavourite the isFavourite to set
	 */
	public void setFavourite(boolean isFavourite) {
		this.isFavourite = isFavourite;
	}

	/**
	 * @return the typeOfTransport
	 */
	public int getTypeOfTransport() {
		return typeOfTransport;
	}

	/**
	 * @param typeOfTransport the typeOfTransport to set
	 */
	public void setTypeOfTransport(int typeOfTransport) {
		this.typeOfTransport = typeOfTransport;
	}

	/**
	 * @return the listTransfer
	 */
	public ArrayList<ItineraryTypeTransport> getListTransfer() {
		return listTransfer;
	}

	/**
	 * @param listTransfer the listTransfer to set
	 */
	public void setListTransfer(ArrayList<ItineraryTypeTransport> listTransfer) {
		this.listTransfer = listTransfer;
	}

	/**
	 * @return the disabledFacilities
	 */
	public boolean isDisabledFacilities() {
		return DisabledFacilities;
	}

	/**
	 * @param disabledFacilities the disabledFacilities to set
	 */
	public void setDisabledFacilities(boolean disabledFacilities) {
		DisabledFacilities = disabledFacilities;
	}


	
}