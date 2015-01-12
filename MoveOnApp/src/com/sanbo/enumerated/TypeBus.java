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

public enum TypeBus {
	/*
		_id	Model
		0	UNKNOWN
		1	IVECO
		2	MERCEDES
	 */
	UNKNOWN("Unknown",0),
    IVECO("Iveco", 1),
    MERCEDES("Mercedes", 2);

    private String stringValue;
    private int intValue;
    private TypeBus(String toString, int value) {
        stringValue = toString;
        intValue = value;
    }
    
    public int getValue(){
    	return intValue;
    }
    
    public static TypeBus getTypeBus(int val) {
    	return TypeBus.values()[val];
    }
    		    	
    @Override
    public String toString() {
        return stringValue;
    }
};