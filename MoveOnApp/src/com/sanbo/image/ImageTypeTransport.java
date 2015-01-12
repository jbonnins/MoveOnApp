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

import com.sanbo.enumerated.TypeTransport;

public class ImageTypeTransport {
	private int imageTransport;
	private TypeTransport myType;
	/**
	 * @param imageTransport
	 * @param myType
	 */
	public ImageTypeTransport(int imageTransport, TypeTransport myType) {
		super();
		this.imageTransport = imageTransport;
		this.myType = myType;
	}
	/**
	 * @return the imageTransport
	 */
	public int getImageTransport() {
		return imageTransport;
	}
	/**
	 * @param imageTransport the imageTransport to set
	 */
	public void setImageTransport(int imageTransport) {
		this.imageTransport = imageTransport;
	}
	/**
	 * @return the myType
	 */
	public TypeTransport getMyType() {
		return myType;
	}
	/**
	 * @param myType the myType to set
	 */
	public void setMyType(TypeTransport myType) {
		this.myType = myType;
	}
	

}
