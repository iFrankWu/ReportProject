package com.tibco.bean;

public class Hospital {
	private int hospitalId;
	private String name;
	private String department;
	private String machineNumber;
	private String handController;
	private String firmwareVersion;
	/**
	 * 癌症运算法则
	 */
	private String algorithm;
	/**
	 * 应校准日期
	 */
	private String adjustDate;
	private String hospitalLogo;


	/**
	 * return the value of this department
	 *
	 * @return
	 */
	public String getDepartment() {
		return department;
	}

	/**
	 * @param department the department to set
	 */
	public void setDepartment(String department) {
		this.department = department;
	}

	/**
	 * return the value of this hospitalLogo
	 *
	 * @return
	 */
	public String getHospitalLogo() {
		return hospitalLogo;
	}

	/**
	 * @param hospitalLogo the hospitalLogo to set
	 */
	public void setHospitalLogo(String hospitalLogo) {
		this.hospitalLogo = hospitalLogo;
	}

	/**
	 * return the value of this handController
	 *
	 * @return
	 */
	public String getHandController() {
		return handController;
	}

	/**
	 * @param handController the handController to set
	 */
	public void setHandController(String handController) {
		this.handController = handController;
	}

	public int getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(int hospitalId) {
		this.hospitalId = hospitalId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getMachineNumber() {
		return machineNumber;
	}

	public void setMachineNumber(String machineNumber) {
		this.machineNumber = machineNumber;
	}


	public String getFirmwareVersion() {
		return firmwareVersion;
	}

	public void setFirmwareVersion(String firmwareVersion) {
		this.firmwareVersion = firmwareVersion;
	}

	public String getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	public String getAdjustDate() {
		return adjustDate;
	}

	public void setAdjustDate(String adjustDate) {
		this.adjustDate = adjustDate;
	}

	@Override
	public String toString() {
		return "Hospital{" +
				"hospitalId=" + hospitalId +
				", name='" + name + '\'' +
				", department='" + department + '\'' +
				", machineNumber='" + machineNumber + '\'' +
				", handController='" + handController + '\'' +
				", firmwareVersion='" + firmwareVersion + '\'' +
				", algorithm='" + algorithm + '\'' +
				", adjustDate='" + adjustDate + '\'' +
				", hospitalLogo='" + hospitalLogo + '\'' +
				'}';
	}
}
