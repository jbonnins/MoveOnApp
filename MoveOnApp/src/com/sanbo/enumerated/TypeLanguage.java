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

/**
 * @author Juan José Bonnín Sansó
 *
 */
public enum TypeLanguage {
	/*	ENUMERATED
	-- _id	LanguageSTR		TypeLanguage
	CREATE TABLE "TypeLanguage" ("_id" INTEGER PRIMARY KEY ASC AUTOINCREMENT NOT NULL UNIQUE, "LanguageSTR" TEXT NOT NULL );
	*/

	CATALAN("ca", 0),
	ENGLISH("en", 1),
	CASTELLANO("es", 2);
	    
    private String stringValue;
    private int intValue;
    private TypeLanguage(String toString, int value) {
        stringValue = toString;
        intValue = value;
    }

    public int Value(){
    	return intValue;
    }

    @Override
    public String toString() {
        return stringValue;
    }
    
    public static TypeLanguage typeOf(String mlang){
    	TypeLanguage res;
    	if (mlang.compareToIgnoreCase(TypeLanguage.ENGLISH.toString())==0)
    		res = TypeLanguage.ENGLISH;
    	else if (mlang.compareToIgnoreCase(TypeLanguage.CATALAN.toString())==0)
    		res = TypeLanguage.CATALAN;
    	else if  (mlang.compareToIgnoreCase(TypeLanguage.CASTELLANO.toString())==0)
    		res = TypeLanguage.CASTELLANO;
    	else
    		// by default Language ENGLISH
    		res = TypeLanguage.ENGLISH;
    	return res;
    }
    		
};
