/*
 * Search.java
 * Jul 1, 2013
 * com.tibco.bean
 * ReportProject
 * Copyright (C), 2013, TIBCO Software Inc.
 * 
 */
package com.tibco.bean;

import java.util.Date;

/**
 * class description goes here.
 *
 * @author <a href="mailto:swu@tibco-support.com">Frank Wu</a>
 * @version 1.0.0
 */
public class Search extends Report {
	private Integer age2;
	private Date checkDate2;
	private Date lastTimeMenstruation2;
	/**
	 * return the value of this age2
	 * @return 
	 */
	public Integer getAge2() {
		return age2;
	}
	/**
	 * @param age2 the age2 to set  
	 *
	 */
	public void setAge2(Integer age2) {
		this.age2 = age2;
	}
	/**
	 * return the value of this checkDate2
	 * @return 
	 */
	public Date getCheckDate2() {
		return checkDate2;
	}
	/**
	 * @param checkDate2 the checkDate2 to set  
	 *
	 */
	public void setCheckDate2(Date checkDate2) {
		this.checkDate2 = checkDate2;
	}
	/**
	 * return the value of this lastTimeMenstruation2
	 * @return 
	 */
	public Date getLastTimeMenstruation2() {
		return lastTimeMenstruation2;
	}
	/**
	 * @param lastTimeMenstruation2 the lastTimeMenstruation2 to set  
	 *
	 */
	public void setLastTimeMenstruation2(Date lastTimeMenstruation2) {
		this.lastTimeMenstruation2 = lastTimeMenstruation2;
	}
	@Override
	public String toString() {
		return "Search [age2=" + age2 + ", checkDate2=" + checkDate2
				+ ", lastTimeMenstruation2=" + lastTimeMenstruation2 + "]";
	}
	
	
}

