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

public class ImageLine {
	  private int imageLine;
	  private String code;
	/**
	 * @return the imageLine
	 */
	public int getImageLine() {
		return imageLine;
	}
	/**
	 * @param imageLine the imageLine to set
	 */
	public void setImageLine(int imageLine) {
		this.imageLine = imageLine;
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
	 * @param imageLine
	 * @param code
	 */
	public ImageLine(int imageLine, String code) {
		this.imageLine = imageLine;
		this.code = code;
	}


}
