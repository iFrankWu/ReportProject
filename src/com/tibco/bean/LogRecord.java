package com.tibco.bean;

import java.util.Date;

public class LogRecord {
	private int  recordId;
	private int  doctorId;
	private Date operateTime;
	private String methodName;
	private String methodParam;
	private String ip;
	
	/**
	 * return the value of this methodParam
	 * @return 
	 */
	public String getMethodParam() {
		return methodParam;
	}
	/**
	 * @param methodParam the methodParam to set  
	 *
	 */
	public void setMethodParam(String methodParam) {
		this.methodParam = methodParam;
	}
	public int getRecordId() {
		return recordId;
	}
	public void setRecordId(int recordId) {
		this.recordId = recordId;
	}
	public int getDoctorId() {
		return doctorId;
	}
	public void setDoctorId(int doctorId) {
		this.doctorId = doctorId;
	}
 
	/**
	 * return the value of this operateTime
	 * @return 
	 */
	public Date getOperateTime() {
		return operateTime;
	}
	/**
	 * @param operateTime the operateTime to set  
	 *
	 */
	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}
	 
	/**
	 * return the value of this methodName
	 * @return 
	 */
	public String getMethodName() {
		return methodName;
	}
	/**
	 * @param methodName the methodName to set  
	 *
	 */
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	
}
