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

public enum TypeRoute {
	/*	ENUMERATED
	 * Type of routes
	*/

	FASTER("FASTER"), LESSTRANSFER("LESSTRANSFER"), ACCESIBLE("ACCESIBLE");
	
	private String stringValue;
	
	private TypeRoute(String str){
		this.stringValue = str;
	}
		
    @Override
    public String toString() {
        return stringValue;
    }

    public static TypeRoute typeOf(String str){
    	// by default TypeRoute is FASTER
    	TypeRoute res = TypeRoute.FASTER;
    	
    	if (str.compareToIgnoreCase(TypeRoute.LESSTRANSFER.stringValue) == 0) res = TypeRoute.LESSTRANSFER;
    	else if (str.compareToIgnoreCase(TypeRoute.ACCESIBLE.stringValue)==0) res = TypeRoute.ACCESIBLE;
    		
    	return res;
    }

};

