/*
 * DoctorDAOTest.java
 * Jun 8, 2013
 * com.tibco.dao.test
 * Report
 * Copyright (C), 2013, TIBCO Software Inc.
 * 
 */
package com.tibco.dao.test;

import java.util.Date;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.shinetech.sql.exception.DBException;
import com.tibco.bean.Doctor;
import com.tibco.dao.DoctorDAO;
import com.tibco.util.Const;

/**
 * class description goes here.
 *
 * @author <a href="mailto:swu@tibco-support.com">Frank Wu</a>
 * @version 1.0.0
 */
public class DoctorDAOTest  extends TestCase {
	private DoctorDAO dao = new DoctorDAO();
	private int id = 3;
	public void testGetDoctorListByType(){
		try {
			 List<Doctor> doctors = dao.getDoctorListByType("申请医生");
			 
			 for (Doctor doctor : doctors) {
				System.out.println(doctor.getDoctorName());
			}
			assertTrue(true);
		} catch (DBException e) {
			e.printStackTrace();
			assertTrue(false);
			
		}
		System.exit(1);
	}
	public void testAddDoctor(){
		Doctor doctor = new Doctor();
		doctor.setCreateDate(new Date());
		doctor.setDoctorName("Frank Wu");
		doctor.setPassword("1");
		doctor.setStatus("可用");
		doctor.setType("管理员");
		try {
			dao.addDoctor(doctor);
			assertTrue(true);
		} catch (DBException e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}
	public void testGetdoctorList(){
		try {
			List<Doctor> list = dao.getDoctorList(true);
			assertTrue(list.isEmpty());
		} catch (DBException e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}
	public void testDeletedoctor(){
		try {
			dao.deleteDoctor(1);
			assertTrue(true);
		} catch (DBException e) {
			e.printStackTrace();
			assertTrue(false);
			
		}
	}
	public void testExistDoctorName(){
		try {
			 dao.existDoctorName("Frank Wu");
			assertTrue(true);
		} catch (DBException e) {
			e.printStackTrace();
			assertTrue(false);
			
		}
	}
	
	public void testGetDoctorByName(){
		try {
			 dao.getDoctorByName("Frank Wu");
			assertTrue(true);
		} catch (DBException e) {
			e.printStackTrace();
			assertTrue(false);
			
		}
	}
	
	public void testGetDoctorByNameAndPasswd(){
		try {
			 dao.getDoctorByNameAndPasswd("Frank Wu","1");
			assertTrue(true);
		} catch (DBException e) {
			e.printStackTrace();
			assertTrue(false);
			
		}
	}
	
	public void testGetDoctor(){
		try {
			 Doctor doctor = dao.getDoctor(id);
			 System.out.println(id+":"+doctor.getDoctorName());
			 System.out.println(id+":"+doctor.getStatus());
			 System.out.println(id+":"+doctor.getType());
			assertTrue(true);
		} catch (DBException e) {
			e.printStackTrace();
			assertTrue(false);
			
		}
	}
	
	
	
	public void testUpdateDoctors(){
		try {
			Doctor doctor = dao.getDoctor(id);
			doctor.setDoctorName("Frank Wu");
			doctor.setPassword("2");
			doctor.setStatus("禁用");
			doctor.setType("管理员");
			 dao.updateDoctor(doctor, false);
			 dao.updateDoctor(doctor, true);
			assertTrue(true);
		} catch (DBException e) {
			e.printStackTrace();
			assertTrue(false);
			
		}
	}
	public static Test suite(){
		Const.initLogger();
		return new TestSuite(DoctorDAOTest.class);
	}

}

