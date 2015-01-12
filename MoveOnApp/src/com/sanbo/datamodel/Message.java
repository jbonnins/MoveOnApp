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

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.sanbo.enumerated.TypeLanguage;

/*
-- _id	FK_id_Transport	DateEnd	DateStart	Message	Description	Language		Message
CREATE TABLE "Message" ("_id" INTEGER PRIMARY KEY ASC AUTOINCREMENT NOT NULL UNIQUE, "FK_id_Transport" INTEGER NOT NULL, "DateEnd" DATETIME, "DateStart" DATETIME,"Message" TEXT, "Description" TEXT, "Language" TEXT NOT NULL);
*/

public class Message {
	private int _id;
	private int FK_id_Transport;
	private Calendar DateEnd;
	private Calendar DateStart;
	private String Message;
	private String Description;
	private TypeLanguage Language;
	
	public Message() {
		
	}
	/**
	 * @param _id
	 * @param fK_id_Transport
	 * @param dateEnd
	 * @param dateStart
	 * @param message
	 * @param description
	 * @param Language
	 */
	public Message(int _id, int fK_id_Transport, Calendar dateEnd,
			Calendar dateStart, String message, String description,
			TypeLanguage language) {
		this._id = _id;
		FK_id_Transport = fK_id_Transport;
		DateEnd = dateEnd;
		DateStart = dateStart;
		Message = message;
		Description = description;
		this.Language = language;
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
	 * @return the fK_id_Transport
	 */
	public int getFK_id_Transport() {
		return FK_id_Transport;
	}
	/**
	 * @param fK_id_Transport the fK_id_Transport to set
	 */
	public void setFK_id_Transport(int fK_id_Transport) {
		FK_id_Transport = fK_id_Transport;
	}
	/**
	 * @return the dateEnd
	 */
	public Calendar getDateEnd() {
		return DateEnd;
	}
	/**
	 * @param dateEnd the dateEnd to set
	 */
	public void setDateEnd(Calendar dateEnd) {
		DateEnd = dateEnd;
	}
	/**
	 * @return the dateStart
	 */
	public Calendar getDateStart() {
		return DateStart;
	}
	/**
	 * @param dateStart the dateStart to set
	 */
	public void setDateStart(Calendar dateStart) {
		DateStart = dateStart;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return Message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		Message = message;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return Description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		Description = description;
	}
	/**
	 * @return the language
	 */
	public TypeLanguage getLanguage() {
		return Language;
	}
	/**
	 * @param language the language to set
	 */
	public void setLanguage(TypeLanguage language) {
		Language = language;
	}
	
	public boolean isOutOfDate() {
		 Calendar cal = new GregorianCalendar();
		 if (this.DateEnd != null)
			 if (this.DateEnd.before(cal))
			 	return true;
			 else
				return false;
		 else
			 return false;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((DateEnd == null) ? 0 : DateEnd.hashCode());
		result = prime * result
				+ ((DateStart == null) ? 0 : DateStart.hashCode());
		result = prime * result
				+ ((Description == null) ? 0 : Description.hashCode());
		result = prime * result + FK_id_Transport;
		result = prime * result
				+ ((Language == null) ? 0 : Language.hashCode());
		result = prime * result + ((Message == null) ? 0 : Message.hashCode());
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
		if (!(obj instanceof Message)) {
			return false;
		}
		Message other = (Message) obj;
		if (_id != other._id) {
			return false;
		}
		return true;
	}

	
	

}
