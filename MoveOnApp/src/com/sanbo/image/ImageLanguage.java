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

public class ImageLanguage {
	private int imageLanguage;
	private String code;
	private String Language;

	public ImageLanguage(int imageLanguage, String code, String language) {
		super();
		this.imageLanguage = imageLanguage;
		this.code = code;
		Language = language;
	}

	public int getImageLanguage() {
		return imageLanguage;
	}

	public void setImageLanguage(int imageLanguage) {
		this.imageLanguage = imageLanguage;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getLanguage() {
		return Language;
	}

	public void setLanguage(String language) {
		Language = language;
	}
	
}