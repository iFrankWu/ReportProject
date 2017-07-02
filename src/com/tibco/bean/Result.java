/*
 * Result.java
 * Mar 28, 2013
 * org.timecontrol.db
 * AngularJS
 * Copyright (C), 2013, TIBCO Software Inc.
 * 
 */
package com.tibco.bean;
/**
 * class description goes here.
 *
 * @author <a href="mailto:swu@tibco-support.com">Frank Wu</a>
 * @version 1.0.0
 */
public class Result {

	private Boolean isSuccess;
	private Object description;
	
	public Result(Boolean isSuccess,Object description){
		this.isSuccess = isSuccess;
		this.description = description;
	}
	/**
	 * return the value of this isSuccess
	 * @return 
	 */
	public Boolean getIsSuccess() {
		return isSuccess;
	}
	/**
	 * @param isSuccess the isSuccess to set  
	 *
	 */
	public void setIsSuccess(Boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	/**
	 * return the value of this decription
	 * @return 
	 */
	public Object getDescription() {
		return description;
	}
	/**
	 * @param decription the decription to set  
	 *
	 */
	public void setDescription(Object description) {
		this.description = description;
	}
	

}

