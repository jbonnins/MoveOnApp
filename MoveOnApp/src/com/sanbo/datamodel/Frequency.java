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
/*
-- _id	FK_id_Line	StartTime	EndTime	 Monday Tuesday Wednesday Thrusday Friday	Saturday	Sunday_Holiday		Frequency
CREATE TABLE "Frequency" ("_id" INTEGER PRIMARY KEY ASC AUTOINCREMENT NOT NULL UNIQUE, "FK_id_Line" INTEGER NOT NULL, "StartTime" DATETIME NOT NULL, "EndTime" DATETIME NOT NULL, "Monday" BOOLEAN NOT NULL, "Tuesday" BOOLEAN NOT NULL,"Wednesday" BOOLEAN NOT NULL, "Thursday" BOOLEAN NOT NULL,"Friday" BOOLEAN NOT NULL, "Saturday" BOOLEAN NOT NULL, "Sunday_Holiday" BOOLEAN NOT NULL);
*/


import java.util.Calendar;

public class Frequency
{

	private int _id;
	private int FK_id_Line;
	private Calendar StartTime;
	private Calendar EndTime;
	private Calendar Monday;
	private Calendar Tuesday;
	private Calendar Wednesday;
	private Calendar Thursday;
	private Calendar Friday;	
	private Calendar Saturday;
	private Calendar Sunday_Holiday;
	/**
	 * @param _id
	 * @param fK_id_Line
	 * @param startTime
	 * @param endTime
	 * @param monday
	 * @param tuesday
	 * @param wednesday
	 * @param thursday
	 * @param friday
	 * @param saturday
	 * @param sunday_Holiday
	 */
	public Frequency(int _id, int fK_id_Line, Calendar startTime,
			Calendar endTime, Calendar monday, Calendar tuesday,
			Calendar wednesday, Calendar thursday, Calendar friday,
			Calendar saturday, Calendar sunday_Holiday) {
		this._id = _id;
		FK_id_Line = fK_id_Line;
		StartTime = startTime;
		EndTime = endTime;
		Monday = monday;
		Tuesday = tuesday;
		Wednesday = wednesday;
		Thursday = thursday;
		Friday = friday;
		Saturday = saturday;
		Sunday_Holiday = sunday_Holiday;
	}
	/**
	 * @return the _id
	 */
	public int get_id() {
		return _id;
	}
	/**
	 * @param _id the _id to set
	 */
	public void set_id(int _id) {
		this._id = _id;
	}
	/**
	 * @return the fK_id_Line
	 */
	public int getFK_id_Line() {
		return FK_id_Line;
	}
	/**
	 * @param fK_id_Line the fK_id_Line to set
	 */
	public void setFK_id_Line(int fK_id_Line) {
		FK_id_Line = fK_id_Line;
	}
	/**
	 * @return the startTime
	 */
	public Calendar getStartTime() {
		return StartTime;
	}
	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(Calendar startTime) {
		StartTime = startTime;
	}
	/**
	 * @return the endTime
	 */
	public Calendar getEndTime() {
		return EndTime;
	}
	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(Calendar endTime) {
		EndTime = endTime;
	}
	/**
	 * @return the monday
	 */
	public Calendar getMonday() {
		return Monday;
	}
	/**
	 * @param monday the monday to set
	 */
	public void setMonday(Calendar monday) {
		Monday = monday;
	}
	/**
	 * @return the tuesday
	 */
	public Calendar getTuesday() {
		return Tuesday;
	}
	/**
	 * @param tuesday the tuesday to set
	 */
	public void setTuesday(Calendar tuesday) {
		Tuesday = tuesday;
	}
	/**
	 * @return the wednesday
	 */
	public Calendar getWednesday() {
		return Wednesday;
	}
	/**
	 * @param wednesday the wednesday to set
	 */
	public void setWednesday(Calendar wednesday) {
		Wednesday = wednesday;
	}
	/**
	 * @return the thursday
	 */
	public Calendar getThursday() {
		return Thursday;
	}
	/**
	 * @param thursday the thursday to set
	 */
	public void setThursday(Calendar thursday) {
		Thursday = thursday;
	}
	/**
	 * @return the friday
	 */
	public Calendar getFriday() {
		return Friday;
	}
	/**
	 * @param friday the friday to set
	 */
	public void setFriday(Calendar friday) {
		Friday = friday;
	}
	/**
	 * @return the saturday
	 */
	public Calendar getSaturday() {
		return Saturday;
	}
	/**
	 * @param saturday the saturday to set
	 */
	public void setSaturday(Calendar saturday) {
		Saturday = saturday;
	}
	/**
	 * @return the sunday_Holiday
	 */
	public Calendar getSunday_Holiday() {
		return Sunday_Holiday;
	}
	/**
	 * @param sunday_Holiday the sunday_Holiday to set
	 */
	public void setSunday_Holiday(Calendar sunday_Holiday) {
		Sunday_Holiday = sunday_Holiday;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((EndTime == null) ? 0 : EndTime.hashCode());
		result = prime * result + FK_id_Line;
		result = prime * result + ((Friday == null) ? 0 : Friday.hashCode());
		result = prime * result + ((Monday == null) ? 0 : Monday.hashCode());
		result = prime * result
				+ ((Saturday == null) ? 0 : Saturday.hashCode());
		result = prime * result
				+ ((StartTime == null) ? 0 : StartTime.hashCode());
		result = prime * result
				+ ((Sunday_Holiday == null) ? 0 : Sunday_Holiday.hashCode());
		result = prime * result
				+ ((Thursday == null) ? 0 : Thursday.hashCode());
		result = prime * result + ((Tuesday == null) ? 0 : Tuesday.hashCode());
		result = prime * result
				+ ((Wednesday == null) ? 0 : Wednesday.hashCode());
		result = prime * result + _id;
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Frequency)) {
			return false;
		}
		Frequency other = (Frequency) obj;
		if (_id != other._id) {
			return false;
		}
		return true;
	}

}

