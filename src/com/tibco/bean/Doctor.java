/*
 * Doctor.java
 * Jun 8, 2013
 * com.tibco.bean
 * Report
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
public class Doctor {
	private int doctorId;
	private String doctorName;
	private String password;
	private String type;
	private String status;
	private Boolean isDelete;
	private Date modifyDate;
	private Date createDate;
//	private String prescribingDoctorName;//v1.2 申请医生（开方医生）, 多个开方医生请以中文逗号分隔
	
 
	 
	/**
	 * return the value of this doctorId
	 * @return 
	 */
	public int getDoctorId() {
		return doctorId;
	}
	/**
	 * @param doctorId the doctorId to set  
	 *
	 */
	public void setDoctorId(int doctorId) {
		this.doctorId = doctorId;
	}
	 
	 
	/**
	 * return the value of this doctorName
	 * @return 
	 */
	public String getDoctorName() {
		return doctorName;
	}
	/**
	 * @param doctorName the doctorName to set  
	 *
	 */
	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}
	/**
	 * return the value of this password
	 * @return 
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set  
	 *
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * return the value of this type
	 * @return 
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set  
	 *
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * return the value of this status
	 * @return 
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set  
	 *
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * return the value of this isDelete
	 * @return 
	 */
	public Boolean getIsDelete() {
		return isDelete;
	}
	/**
	 * @param isDelete the isDelete to set  
	 *
	 */
	public void setIsDelete(Boolean isDelete) {
		this.isDelete = isDelete;
	}
	/**
	 * return the value of this modifyDate
	 * @return 
	 */
	public Date getModifyDate() {
		return modifyDate;
	}
	/**
	 * @param modifyDate the modifyDate to set  
	 *
	 */
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	/**
	 * return the value of this createDate
	 * @return 
	 */
	public Date getCreateDate() {
		return createDate;
	}
	/**
	 * @param createDate the createDate to set  
	 *
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	@Override
	public String toString() {
		return "Doctor [doctorId=" + doctorId + ", doctorName=" + doctorName
				+ ", password=" + password + ", type=" + type + ", status="
				+ status + ", isDelete=" + isDelete + ", modifyDate="
				+ modifyDate + ", createDate=" + createDate + "]";
	}
	
	
}

