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

import java.util.ArrayList;

public class ImageStation {
	// represent transfer at same station, next station are not included in v1.0	
	  private ArrayList<Integer> imageTransfer;
	  private String code;
	/**
	 * @param imageTransfer
	 * @param code
	 */
	public ImageStation(ArrayList<Integer> imageTransfer, String code) {
		this.imageTransfer = imageTransfer;
		this.code = code;
	}
	/**
	 * @return the imageTransfer
	 */
	public ArrayList<Integer> getImageTransfer() {
		return imageTransfer;
	}
	/**
	 * @param imageTransfer the imageTransfer to set
	 */
	public void setImageTransfer(ArrayList<Integer> imageTransfer) {
		this.imageTransfer = imageTransfer;
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
	
	public int numTransfer() {
		if (imageTransfer != null)
			return imageTransfer.size();
		else
			return 0;
	}

}
