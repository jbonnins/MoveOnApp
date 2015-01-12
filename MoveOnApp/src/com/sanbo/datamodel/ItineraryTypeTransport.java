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

public class ItineraryTypeTransport {
	
	String code;
	int FK_id_typeTransport;
	/**
	 * @param code
	 * @param fK_id_typeTransport
	 */
	public ItineraryTypeTransport(String code, int fK_id_typeTransport) {
		super();
		this.code = code;
		FK_id_typeTransport = fK_id_typeTransport;
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
	 * @return the fK_id_typeTransport
	 */
	public int getFK_id_typeTransport() {
		return FK_id_typeTransport;
	}
	/**
	 * @param fK_id_typeTransport the fK_id_typeTransport to set
	 */
	public void setFK_id_typeTransport(int fK_id_typeTransport) {
		FK_id_typeTransport = fK_id_typeTransport;
	}

}
