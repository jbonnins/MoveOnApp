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

public enum TypeTransport {
	/*	ENUMERATED
	-- _id	Transport		TypeTransport
	CREATE TABLE "TypeTransport" ("_id" INTEGER PRIMARY KEY ASC AUTOINCREMENT NOT NULL UNIQUE, "Transport" TEXT NOT NULL );
	*/
	
	PALMAPUBLICTRANSPORT("PALMAPUBLICTRANSPORT", 0),
    BUSPALMA("BUSPALMA", 1),
    METROPALMA("METROPALMA", 2),
    BICIPALMA("BICIPALMA", 3),
    TRENMALLORCA("TRENMALORCA", 4),
    BUSTIBMALLORCA("BUSTIBMALLORCA", 5),
    TRENSOLLER("TRENSOLLER", 6),
    TAXIPALMAOD("TAXIPALMAOD", 7),
    CARRUATGEPALMAOD("CARRUATGEPALMAOD", 8);
	
    private String stringValue;
    private int intValue;
    
    private TypeTransport(String toString, int value) {
        stringValue = toString;
        intValue = value;
    }

    public int getValue(){
    	return intValue;
    }

    @Override
    public String toString() {
        return stringValue;
    }
    
    public static TypeTransport getTypeTransport(int val) {
    	return TypeTransport.values()[val];
    }
    
    public static TypeTransport LastPublic(){
    	return TypeTransport.TRENSOLLER;
    }
    
};
