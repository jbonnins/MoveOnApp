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

public enum TypeUser {
	/*	ENUMERATED
	-- _id	Actor		TypeUser
	CREATE TABLE "TypeUser" ("_id" INTEGER PRIMARY KEY ASC AUTOINCREMENT NOT NULL UNIQUE, "Actor" TEXT NOT NULL );
	*/
	USER("USER"), INSPECTOR("INSPECTOR"), DRIVING("DRIVING"), DRIVER("DRIVER");

	private String stringValue;
		
	private TypeUser(String str){
		this.stringValue = str;
	}
	
    @Override
    public String toString() {
        return stringValue;
    }
    
    public static TypeUser typeOf(String str){
    	// by default TypeUser is USER
    	TypeUser res = TypeUser.USER;
    	
    	if (str.compareToIgnoreCase(TypeUser.DRIVER.stringValue)==0) res = TypeUser.DRIVER;
    	else if  (str.compareToIgnoreCase(TypeUser.DRIVING.stringValue)==0) res = TypeUser.DRIVING;
    	else if (str.compareToIgnoreCase(TypeUser.INSPECTOR.stringValue)==0) res = TypeUser.INSPECTOR;
    		
    	return res;
    }
	
};

