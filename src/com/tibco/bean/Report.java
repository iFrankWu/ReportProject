/*
 * Report.java
 * Jun 24, 2013
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
public class Report { 
	private Integer reportId;
	  /**资料*/ 
	private String patientName;
	private Integer age;
	private String caseNumber;
	private Date lastTimeMenstruation;
	private Integer pregnancyNumber;
	private Integer childbirthNumber;
	private Boolean isMenopause;
	private String department;//检查部门
	private String phone;
  /**主诉*/ 
	private Boolean isLeucorrhea;
	private Boolean isBleed;
	private String unregularBleed;
	private String otherComplaints;
	 
	  /**临床表现*/ 
	private Boolean isSmooth;
	private Boolean isAcuteInflammation;
	private Boolean isHypertrophy;
	private Boolean isPolyp;
	private String erosion;
	private Boolean isTear;
	private Boolean isNesslersGlandCyst;
	private Boolean isWhite;
	private Boolean isCancer;
	private String otherClinical;
	  /**检查内容*/
	private Integer pointNumber;
	private String reason4doesNotComplete;
	private String isComplete;
	private String checkResult;

 
	 /**进一步处理意见*/ 
	private Boolean screening;
	private Boolean checking;
	private String otherSuggestion;
 
	private Date checkDate;
	private Date modifyDate;

	private String doctorName;
	private Integer doctorId;
	private Boolean isDelete;
	private String address;
	
	private String prescribingDoctorName;//v1.2 增加开方医生
	
	private String lct ; //V1.3 added
	private String hpv; //V1.3 added
	private String touchbleeding; //V1.3 added
	
	private Boolean checkHpv;//checkHpv v1.4 addded 建议进行HPV筛查
	
	
	public Boolean getCheckHpv() {
		return checkHpv;
	}
	public void setCheckHpv(Boolean checkHpv) {
		this.checkHpv = checkHpv;
	}
	public String getLct() {
		return lct;
	}
	public void setLct(String lct) {
		this.lct = lct;
	}
	public String getHpv() {
		return hpv;
	}
	public void setHpv(String hpv) {
		this.hpv = hpv;
	}
	public String getTouchbleeding() {
		return touchbleeding;
	}
	public void setTouchbleeding(String touchbleeding) {
		this.touchbleeding = touchbleeding;
	}
	/**
	 * return the value of this prescribingDoctorName
	 * @return 
	 */
	public String getPrescribingDoctorName() {
		return prescribingDoctorName;
	}
	/**
	 * @param prescribingDoctorName the prescribingDoctorName to set  
	 *
	 */
	public void setPrescribingDoctorName(String prescribingDoctorName) {
		this.prescribingDoctorName = prescribingDoctorName;
	}
	/**
	 * return the value of this phone
	 * @return 
	 */
	public String getPhone() {
		return phone;
	}
	/**
	 * @param phone the phone to set  
	 *
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}
	/**
	 * return the value of this department
	 * @return 
	 */
	public String getDepartment() {
		return department;
	}
	/**
	 * @param department the department to set  
	 *
	 */
	public void setDepartment(String department) {
		this.department = department;
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
	 * return the value of this reportId
	 * @return 
	 */
	public Integer getReportId() {
		return reportId;
	}
	/**
	 * @param reportId the reportId to set  
	 *
	 */
	public void setReportId(Integer reportId) {
		this.reportId = reportId;
	}
	/**
	 * return the value of this patientName
	 * @return 
	 */
	public String getPatientName() {
		return patientName;
	}
	/**
	 * @param patientName the patientName to set  
	 *
	 */
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	/**
	 * return the value of this age
	 * @return 
	 */
	public Integer getAge() {
		return age;
	}
	/**
	 * @param age the age to set  
	 *
	 */
	public void setAge(Integer age) {
		this.age = age;
	}
	/**
	 * return the value of this caseNumber
	 * @return 
	 */
	public String getCaseNumber() {
		return caseNumber;
	}
	/**
	 * @param caseNumber the caseNumber to set  
	 *
	 */
	public void setCaseNumber(String caseNumber) {
		this.caseNumber = caseNumber;
	}
	/**
	 * return the value of this lastTimeMenstruation
	 * @return 
	 */
	public Date getLastTimeMenstruation() {
		return lastTimeMenstruation;
	}
	/**
	 * @param lastTimeMenstruation the lastTimeMenstruation to set  
	 *
	 */
	public void setLastTimeMenstruation(Date lastTimeMenstruation) {
		this.lastTimeMenstruation = lastTimeMenstruation;
	}
	/**
	 * return the value of this pregnancyNumber
	 * @return 
	 */
	public Integer getPregnancyNumber() {
		return pregnancyNumber;
	}
	/**
	 * @param pregnancyNumber the pregnancyNumber to set  
	 *
	 */
	public void setPregnancyNumber(Integer pregnancyNumber) {
		this.pregnancyNumber = pregnancyNumber;
	}
	/**
	 * return the value of this childbirthNumber
	 * @return 
	 */
	public Integer getChildbirthNumber() {
		return childbirthNumber;
	}
	/**
	 * @param childbirthNumber the childbirthNumber to set  
	 *
	 */
	public void setChildbirthNumber(Integer childbirthNumber) {
		this.childbirthNumber = childbirthNumber;
	}
	/**
	 * return the value of this isMenopause
	 * @return 
	 */
	public Boolean getIsMenopause() {
		return isMenopause;
	}
	/**
	 * @param isMenopause the isMenopause to set  
	 *
	 */
	public void setIsMenopause(Boolean isMenopause) {
		this.isMenopause = isMenopause;
	}
	/**
	 * return the value of this isLeucorrhea
	 * @return 
	 */
	public Boolean getIsLeucorrhea() {
		return isLeucorrhea;
	}
	/**
	 * @param isLeucorrhea the isLeucorrhea to set  
	 *
	 */
	public void setIsLeucorrhea(Boolean isLeucorrhea) {
		this.isLeucorrhea = isLeucorrhea;
	}
	/**
	 * return the value of this isBleed
	 * @return 
	 */
	public Boolean getIsBleed() {
		return isBleed;
	}
	/**
	 * @param isBleed the isBleed to set  
	 *
	 */
	public void setIsBleed(Boolean isBleed) {
		this.isBleed = isBleed;
	}
	/**
	 * return the value of this unregularBleed
	 * @return 
	 */
	public String getUnregularBleed() {
		return unregularBleed;
	}
	/**
	 * @param unregularBleed the unregularBleed to set  
	 *
	 */
	public void setUnregularBleed(String unregularBleed) {
		this.unregularBleed = unregularBleed;
	}
	/**
	 * return the value of this otherComplaints
	 * @return 
	 */
	public String getOtherComplaints() {
		return otherComplaints;
	}
	/**
	 * @param otherComplaints the otherComplaints to set  
	 *
	 */
	public void setOtherComplaints(String otherComplaints) {
		this.otherComplaints = otherComplaints;
	}
	/**
	 * return the value of this isSmooth
	 * @return 
	 */
	public Boolean getIsSmooth() {
		return isSmooth;
	}
	/**
	 * @param isSmooth the isSmooth to set  
	 *
	 */
	public void setIsSmooth(Boolean isSmooth) {
		this.isSmooth = isSmooth;
	}
	/**
	 * return the value of this isAcuteInflammation
	 * @return 
	 */
	public Boolean getIsAcuteInflammation() {
		return isAcuteInflammation;
	}
	/**
	 * @param isAcuteInflammation the isAcuteInflammation to set  
	 *
	 */
	public void setIsAcuteInflammation(Boolean isAcuteInflammation) {
		this.isAcuteInflammation = isAcuteInflammation;
	}
	/**
	 * return the value of this isHypertrophy
	 * @return 
	 */
	public Boolean getIsHypertrophy() {
		return isHypertrophy;
	}
	/**
	 * @param isHypertrophy the isHypertrophy to set  
	 *
	 */
	public void setIsHypertrophy(Boolean isHypertrophy) {
		this.isHypertrophy = isHypertrophy;
	}
	/**
	 * return the value of this isPolyp
	 * @return 
	 */
	public Boolean getIsPolyp() {
		return isPolyp;
	}
	/**
	 * @param isPolyp the isPolyp to set  
	 *
	 */
	public void setIsPolyp(Boolean isPolyp) {
		this.isPolyp = isPolyp;
	}
	/**
	 * return the value of this erosion
	 * @return 
	 */
	public String getErosion() {
		return erosion;
	}
	/**
	 * @param erosion the erosion to set  
	 *
	 */
	public void setErosion(String erosion) {
		this.erosion = erosion;
	}
	/**
	 * return the value of this isTear
	 * @return 
	 */
	public Boolean getIsTear() {
		return isTear;
	}
	/**
	 * @param isTear the isTear to set  
	 *
	 */
	public void setIsTear(Boolean isTear) {
		this.isTear = isTear;
	}
	/**
	 * return the value of this isNesslersGlandCyst
	 * @return 
	 */
	public Boolean getIsNesslersGlandCyst() {
		return isNesslersGlandCyst;
	}
	/**
	 * @param isNesslersGlandCyst the isNesslersGlandCyst to set  
	 *
	 */
	public void setIsNesslersGlandCyst(Boolean isNesslersGlandCyst) {
		this.isNesslersGlandCyst = isNesslersGlandCyst;
	}
	/**
	 * return the value of this isWhite
	 * @return 
	 */
	public Boolean getIsWhite() {
		return isWhite;
	}
	/**
	 * @param isWhite the isWhite to set  
	 *
	 */
	public void setIsWhite(Boolean isWhite) {
		this.isWhite = isWhite;
	}
	/**
	 * return the value of this isCancer
	 * @return 
	 */
	public Boolean getIsCancer() {
		return isCancer;
	}
	/**
	 * @param isCancer the isCancer to set  
	 *
	 */
	public void setIsCancer(Boolean isCancer) {
		this.isCancer = isCancer;
	}
	 
	/**
	 * return the value of this otherClinical
	 * @return 
	 */
	public String getOtherClinical() {
		return otherClinical;
	}
	/**
	 * @param otherClinical the otherClinical to set  
	 *
	 */
	public void setOtherClinical(String otherClinical) {
		this.otherClinical = otherClinical;
	}
	/**
	 * return the value of this pointNumber
	 * @return 
	 */
	public Integer getPointNumber() {
		return pointNumber;
	}
	/**
	 * @param pointNumber the pointNumber to set  
	 *
	 */
	public void setPointNumber(Integer pointNumber) {
		this.pointNumber = pointNumber;
	}
	 
	/**
	 * return the value of this isComplete
	 * @return 
	 */
	public String getIsComplete() {
		return isComplete;
	}
	/**
	 * @param isComplete the isComplete to set  
	 *
	 */
	public void setIsComplete(String isComplete) {
		this.isComplete = isComplete;
	}
	/**
	 * return the value of this checkResult
	 * @return 
	 */
	public String getCheckResult() {
		return checkResult;
	}
	/**
	 * @param checkResult the checkResult to set  
	 *
	 */
	public void setCheckResult(String checkResult) {
		this.checkResult = checkResult;
	}
	/**
	 * return the value of this screening
	 * @return 
	 */
	public Boolean getScreening() {
		return screening;
	}
	/**
	 * @param screening the screening to set  
	 *
	 */
	public void setScreening(Boolean screening) {
		this.screening = screening;
	}
	/**
	 * return the value of this checking
	 * @return 
	 */
	public Boolean getChecking() {
		return checking;
	}
	/**
	 * @param checking the checking to set  
	 *
	 */
	public void setChecking(Boolean checking) {
		this.checking = checking;
	}
	/**
	 * return the value of this otherSuggestion
	 * @return 
	 */
	public String getOtherSuggestion() {
		return otherSuggestion;
	}
	/**
	 * @param otherSuggestion the otherSuggestion to set  
	 *
	 */
	public void setOtherSuggestion(String otherSuggestion) {
		this.otherSuggestion = otherSuggestion;
	}
	/**
	 * return the value of this checkDate
	 * @return 
	 */
	public Date getCheckDate() {
		return checkDate;
	}
	/**
	 * @param checkDate the checkDate to set  
	 *
	 */
	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
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
	 * return the value of this doctorId
	 * @return 
	 */
	public Integer getDoctorId() {
		return doctorId;
	}
	/**
	 * @param doctorId the doctorId to set  
	 *
	 */
	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}
	public Boolean getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(Boolean isDelete) {
		this.isDelete = isDelete;
	}
	
	 
	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * return the value of this reason4doesNotComplete
	 * @return 
	 */
	public String getReason4doesNotComplete() {
		return reason4doesNotComplete;
	}
	/**
	 * @param reason4doesNotComplete the reason4doesNotComplete to set  
	 *
	 */
	public void setReason4doesNotComplete(String reason4doesNotComplete) {
		this.reason4doesNotComplete = reason4doesNotComplete;
	}
	@Override
	public String toString() {
		return "Report [reportId=" + reportId + ", patientName=" + patientName
				+ ", age=" + age + ", caseNumber=" + caseNumber
				+ ", lastTimeMenstruation=" + lastTimeMenstruation
				+ ", pregnancyNumber=" + pregnancyNumber
				+ ", childbirthNumber=" + childbirthNumber + ", isMenopause="
				+ isMenopause + ", department=" + department + ", phone="
				+ phone + ", isLeucorrhea=" + isLeucorrhea + ", isBleed="
				+ isBleed + ", unregularBleed=" + unregularBleed
				+ ", otherComplaints=" + otherComplaints + ", isSmooth="
				+ isSmooth + ", isAcuteInflammation=" + isAcuteInflammation
				+ ", isHypertrophy=" + isHypertrophy + ", isPolyp=" + isPolyp
				+ ", erosion=" + erosion + ", isTear=" + isTear
				+ ", isNesslersGlandCyst=" + isNesslersGlandCyst + ", isWhite="
				+ isWhite + ", isCancer=" + isCancer + ", otherClinical="
				+ otherClinical + ", pointNumber=" + pointNumber
				+ ", reason4doesNotComplete=" + reason4doesNotComplete
				+ ", isComplete=" + isComplete + ", checkResult=" + checkResult
				+ ", screening=" + screening + ", checking=" + checking
				+ ", otherSuggestion=" + otherSuggestion + ", checkDate="
				+ checkDate + ", modifyDate=" + modifyDate + ", doctorName="
				+ doctorName + ", doctorId=" + doctorId + ", isDelete="
				+ isDelete + ", address=" + address
				+ ", prescribingDoctorName=" + prescribingDoctorName + "]";
	}
	 
	
	
}

