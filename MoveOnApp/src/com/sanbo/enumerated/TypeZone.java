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

package com.sanbo.enumerated;

public enum TypeZone {
	/*	ENUMERATED
	-- _id	Zone		TypeZone
	CREATE TABLE "TypeZone" ("_id" INTEGER PRIMARY KEY ASC AUTOINCREMENT NOT NULL UNIQUE, "Zone" TEXT NOT NULL );
	*/
	
	EMTZONE1("EMTZone1", 1),
	EMTZONE2("EMTZone2", 2),
    METROZONE1("METROZone1", 3);
	    
    private String stringValue;
    private int intValue;
    
    private TypeZone(String toString, int value) {
        stringValue = toString;
        intValue = value;
    }

    public int Value(){
    	return intValue;
    }
    
    public static TypeZone getTypeZone(int val) {
    	return TypeZone.values()[val];
    }

    @Override
    public String toString() {
        return stringValue;
    }
}
