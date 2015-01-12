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

/*
-- _id	FK_id_CabOperator	Facebook	Licence	Name	Telephone	Twitter		Cab
CREATE TABLE "Cab" ("_id" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL UNIQUE , "FK_id_CabOperator" INTEGER NOT NULL , "Facebook" TEXT, "Licence" TEXT NOT NULL, "Name" TEXT NOT NULL,"Telephone" TEXT, "Twitter" TEXT)
*/

public class Cab {
	private int _id;
	private int FK_id_CabOperator;
	private String Facebook;
	private String Licence;
	private String Name;
	private String Telephone;
	private String Twitter;
	private boolean isFavorite;

	
	public Cab() {
		
	}

	/**
	 * @param _id
	 * @param fK_id_CabOperator
	 * @param facebook
	 * @param licence
	 * @param name
	 * @param telephone
	 * @param twitter
	 */
	public Cab(int _id, int fK_id_CabOperator, String facebook,
			String licence, String name, String telephone, String twitter) {
		this._id = _id;
		FK_id_CabOperator = fK_id_CabOperator;
		Facebook = facebook;
		Licence = licence;
		Name = name;
		Telephone = telephone;
		Twitter = twitter;
	}
	/**
	 * @param _id
	 * @param fK_id_CabOperator
	 * @param facebook
	 * @param licence
	 * @param name
	 * @param telephone
	 * @param twitter
	 * @param isFavorite
	 */
	public Cab(int _id, int fK_id_CabOperator, String facebook,
			String licence, String name, String telephone, String twitter, boolean isFavorite) {
		this._id = _id;
		FK_id_CabOperator = fK_id_CabOperator;
		Facebook = facebook;
		Licence = licence;
		Name = name;
		Telephone = telephone;
		Twitter = twitter;
		this.isFavorite = isFavorite;
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
	 * @return the fK_id_CabOperator
	 */
	public int getFK_id_CabOperator() {
		return FK_id_CabOperator;
	}
	/**
	 * @param fK_id_CabOperator the fK_id_CabOperator to set
	 */
	public void setFK_id_CabOperator(int fK_id_CabOperator) {
		FK_id_CabOperator = fK_id_CabOperator;
	}
	/**
	 * @return the facebook
	 */
	public String getFacebook() {
		return Facebook;
	}
	/**
	 * @param facebook the facebook to set
	 */
	public void setFacebook(String facebook) {
		Facebook = facebook;
	}
	/**
	 * @return the licence
	 */
	public String getLicence() {
		return Licence;
	}
	/**
	 * @param licence the licence to set
	 */
	public void setLicence(String licence) {
		Licence = licence;
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
	 * @return the telephon
	 */
	public String getTelephone() {
		return Telephone;
	}
	/**
	 * @param telephon the telephon to set
	 */
	public void setTelephone(String telephone) {
		Telephone = telephone;
	}
	/**
	 * @return the twitter
	 */
	public String getTwitter() {
		return Twitter;
	}
	/**
	 * @param twitter the twitter to set
	 */
	public void setTwitter(String twitter) {
		Twitter = twitter;
	}
	public boolean isFavorite() {
		return isFavorite;
	}

	public void setFavorite(boolean isFavorite) {
		this.isFavorite = isFavorite;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + FK_id_CabOperator;
		result = prime * result
				+ ((Facebook == null) ? 0 : Facebook.hashCode());
		result = prime * result + ((Licence == null) ? 0 : Licence.hashCode());
		result = prime * result + ((Name == null) ? 0 : Name.hashCode());
		result = prime * result
				+ ((Telephone == null) ? 0 : Telephone.hashCode());
		result = prime * result + ((Twitter == null) ? 0 : Twitter.hashCode());
		result = prime * result + _id;
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Cab)) {
			return false;
		}
		Cab other = (Cab) obj;
		if (_id != other._id) {
			return false;
		}
		return true;
	}

}
