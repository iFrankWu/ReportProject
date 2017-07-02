/*
 * HospitalService.java
 * Jul 7, 2013
 * com.tibco.service
 * ReportProject
 * Copyright (C), 2013, TIBCO Software Inc.
 * 
 */
package com.tibco.service;

import com.shinetech.sql.exception.DBException;
import com.tibco.bean.Hospital;
import com.tibco.dao.HospitalDAO;

/**
 * class description goes here.
 *
 * @author <a href="mailto:swu@tibco-support.com">Frank Wu</a>
 * @version 1.0.0
 */
public class HospitalService {
	private HospitalDAO hospitalDAO = new HospitalDAO();
	public Hospital getHospital() throws DBException{
		return hospitalDAO.getHospital();
	}
	
	public void updateHosptal(Hospital hospital)throws DBException{
		hospitalDAO.updateHospital(hospital);
	}
	public Integer addHospital(Hospital hospital)throws DBException{
		return hospitalDAO.addHospital(hospital);
	}
	public void deleteLogo(Integer hospitalId)throws DBException{
		hospitalDAO.deleteLogo(hospitalId);
	}
}

