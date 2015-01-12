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

public class ImageItinerary {
	  private int imageItinerary;
	  private String code;
	/**
	 * @return the imageItinerary
	 */
	public int getImageItinerary() {
		return imageItinerary;
	}
	/**
	 * @param imageItinerary the imageItinerary to set
	 */
	public void setImageItinerary(int imageItinerary) {
		this.imageItinerary = imageItinerary;
	}
	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * @param imageItinerary
	 * @param code
	 */
	public ImageItinerary(int imageItinerary, String code) {
		this.imageItinerary = imageItinerary;
		this.code = code;
	}	
}
