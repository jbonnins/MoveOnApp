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

package com.sanbo.utils;

import java.util.List;

/**
 * @author admin
 *
 */
public class Haversine {
	public static final double R = 6372.8; // In kilometers
	public static double distHaversine(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
 
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return roundNumber(R * c, 2);
    }

	private static double roundNumber(double paramDouble, int paramInt)
	{
		int i = (int)Math.pow(10.0D, paramInt);
		return Math.rint(paramDouble * i) / i;
	}
	
	public static int[] buildIntArray(List<Integer> integers) {
	    int[] ints = new int[integers.size()];
	    int i = 0;
	    for (Integer n : integers) {
	        ints[i++] = n;
	    }
	    return ints;
	}

	public static boolean[] buildBoolArray(List<Boolean> booleans) {
	    boolean[] boolea = new boolean[booleans.size()];
	    int i = 0;
	    for (Boolean b : booleans) {
	        boolea[i++] = b;
	    }
	    return boolea;
	}
	
}
