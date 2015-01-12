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
-- _id	FK_id_Line	FK_id_Station_Start	FK_id_Station_End	Monday Tuesday Wednesday Thursday Friday	Saturday	Sunday_Holiday	Time	LastAssignedLine		Timetable
CREATE TABLE "Timetable" ("_id" INTEGER PRIMARY KEY ASC AUTOINCREMENT NOT NULL UNIQUE, "FK_id_Line" INTEGER NOT NULL, "FK_id_Station_Start" INTEGER, "FK_id_Station_End" INTEGER, "Monday" BOOLEAN NOT NULL, "Tuesday" BOOLEAN NOT NULL,"Wednesday" BOOLEAN NOT NULL, "Thursday" BOOLEAN NOT NULL,"Friday" BOOLEAN NOT NULL, "Saturday" BOOLEAN NOT NULL, "Sunday_Holiday" BOOLEAN NOT NULL, "Time" DATETIME NOT NULL, "LastAssignedLine" INTEGER);
*/

import java.util.Calendar;

public class TimeTable
{
	private int _id;
	  private int FK_id_Line;
	  private int FK_id_Station_Start;
	  private int FK_id_Station_End;
	  private boolean Monday;
	  private boolean Tuesday;
	  private boolean Wednesday;
	  private boolean Thursday;
	  private boolean Friday;
	  private boolean Saturday;
	  private boolean Sunday_Holiday;
	  private Calendar Time;
	  private int LastAssignedLine;
  	
	public TimeTable() {
		
	}

	/**
	 * @param _id
	 * @param fK_id_Line
	 * @param fK_id_Station_Start
	 * @param fK_id_Station_End
	 * @param monday
	 * @param tuesday
	 * @param wednesday
	 * @param thursday
	 * @param friday
	 * @param saturday
	 * @param sunday_Holiday
	 * @param time
	 * @param lastAssignedLine
	 */
	public TimeTable(int _id, int fK_id_Line, int fK_id_Station_Start,
			int fK_id_Station_End, boolean monday, boolean tuesday,
			boolean wednesday, boolean thursday, boolean friday,
			boolean saturday, boolean sunday_Holiday, Calendar time,
			int lastAssignedLine) {
		this._id = _id;
		FK_id_Line = fK_id_Line;
		FK_id_Station_Start = fK_id_Station_Start;
		FK_id_Station_End = fK_id_Station_End;
		Monday = monday;
		Tuesday = tuesday;
		Wednesday = wednesday;
		Thursday = thursday;
		Friday = friday;
		Saturday = saturday;
		Sunday_Holiday = sunday_Holiday;
		Time = time;
		LastAssignedLine = lastAssignedLine;
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
	 * @return the fK_id_Station_Start
	 */
	public int getFK_id_Station_Start() {
		return FK_id_Station_Start;
	}

	/**
	 * @param fK_id_Station_Start the fK_id_Station_Start to set
	 */
	public void setFK_id_Station_Start(int fK_id_Station_Start) {
		FK_id_Station_Start = fK_id_Station_Start;
	}

	/**
	 * @return the fK_id_Station_End
	 */
	public int getFK_id_Station_End() {
		return FK_id_Station_End;
	}

	/**
	 * @param fK_id_Station_End the fK_id_Station_End to set
	 */
	public void setFK_id_Station_End(int fK_id_Station_End) {
		FK_id_Station_End = fK_id_Station_End;
	}

	/**
	 * @return the monday
	 */
	public boolean isMonday() {
		return Monday;
	}

	/**
	 * @param monday the monday to set
	 */
	public void setMonday(boolean monday) {
		Monday = monday;
	}

	/**
	 * @return the tuesday
	 */
	public boolean isTuesday() {
		return Tuesday;
	}

	/**
	 * @param tuesday the tuesday to set
	 */
	public void setTuesday(boolean tuesday) {
		Tuesday = tuesday;
	}

	/**
	 * @return the wednesday
	 */
	public boolean isWednesday() {
		return Wednesday;
	}

	/**
	 * @param wednesday the wednesday to set
	 */
	public void setWednesday(boolean wednesday) {
		Wednesday = wednesday;
	}

	/**
	 * @return the thursday
	 */
	public boolean isThursday() {
		return Thursday;
	}

	/**
	 * @param thursday the thursday to set
	 */
	public void setThursday(boolean thursday) {
		Thursday = thursday;
	}

	/**
	 * @return the friday
	 */
	public boolean isFriday() {
		return Friday;
	}

	/**
	 * @param friday the friday to set
	 */
	public void setFriday(boolean friday) {
		Friday = friday;
	}

	/**
	 * @return the saturday
	 */
	public boolean isSaturday() {
		return Saturday;
	}

	/**
	 * @param saturday the saturday to set
	 */
	public void setSaturday(boolean saturday) {
		Saturday = saturday;
	}

	/**
	 * @return the sunday_Holiday
	 */
	public boolean isSunday_Holiday() {
		return Sunday_Holiday;
	}

	/**
	 * @param sunday_Holiday the sunday_Holiday to set
	 */
	public void setSunday_Holiday(boolean sunday_Holiday) {
		Sunday_Holiday = sunday_Holiday;
	}

	/**
	 * @return the time
	 */
	public Calendar getTime() {
		return Time;
	}

	/**
	 * @param time the time to set
	 */
	public void setTime(Calendar time) {
		Time = time;
	}

	/**
	 * @return the lastAssignedLine
	 */
	public int getLastAssignedLine() {
		return LastAssignedLine;
	}

	/**
	 * @param lastAssignedLine the lastAssignedLine to set
	 */
	public void setLastAssignedLine(int lastAssignedLine) {
		LastAssignedLine = lastAssignedLine;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + FK_id_Line;
		result = prime * result + FK_id_Station_End;
		result = prime * result + FK_id_Station_Start;
		result = prime * result + (Friday ? 1231 : 1237);
		result = prime * result + LastAssignedLine;
		result = prime * result + (Monday ? 1231 : 1237);
		result = prime * result + (Saturday ? 1231 : 1237);
		result = prime * result + (Sunday_Holiday ? 1231 : 1237);
		result = prime * result + (Thursday ? 1231 : 1237);
		result = prime * result + ((Time == null) ? 0 : Time.hashCode());
		result = prime * result + (Tuesday ? 1231 : 1237);
		result = prime * result + (Wednesday ? 1231 : 1237);
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
		if (!(obj instanceof TimeTable)) {
			return false;
		}
		TimeTable other = (TimeTable) obj;
		if (_id != other._id) {
			return false;
		}
		return true;
	}

  
  


  
}
