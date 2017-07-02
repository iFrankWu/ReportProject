/*
 * TempTest.java
 * May 23, 2013
 * com.tibco.test
 * AngularJS
 * Copyright (C), 2013, TIBCO Software Inc.
 * 
 */
package com.tibco.test;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;

import com.shinetech.sql.exception.DBException;
import com.tibco.bean.Report;
import com.tibco.dao.DoctorDAO;
import com.tibco.dao.ReportDAO;

/**
 * class description goes here.
 *
 * @author <a href="mailto:swu@tibco-support.com">Frank Wu</a>
 * @version 1.0.0
 */
public class TempTest {

	/**
	 *  TODO what's the function for this method.
	 *  @param args
	 *	void
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws ParseException {
//		Date d = format.parse("2012/2/3");
//		System.out.println(cryptWithMD5("hello"));
//		System.out.println(cryptWithMD5("hello"));
		DoctorDAO dao =new DoctorDAO();
		ReportDAO reportDAO = new ReportDAO();
		int id = 3;
		try {
//			 Doctor doctor = dao.getDoctor(id);
//			 System.out.println(id+":"+doctor.getDoctorName());
//			 System.out.println(id+":"+doctor.getStatus());
//			 System.out.println(id+":"+doctor.getType());
			System.out.println(reportDAO.getAllRecordNumber());
			for(Report report : reportDAO.getNextPage(3, 3, 2, null)){
				System.out.println(report.getReportId()+":"+report.getPatientName());
			}
			
			for(Report report : reportDAO.getPrePage(12, 3, 1, null)){
				System.out.println(report.getReportId()+":"+report.getPatientName());
			}
			
		} catch (DBException e) {
			e.printStackTrace();
			
		}
	}
	 private static MessageDigest md;

	   public static String cryptWithMD5(String pass){
	    try {
	        md = MessageDigest.getInstance("MD5");
	        byte[] passBytes = pass.getBytes();
	        md.reset();
	        byte[] digested = md.digest(passBytes);
	        StringBuffer sb = new StringBuffer();
	        for(int i=0;i<digested.length;i++){
	            sb.append(Integer.toHexString(0xff & digested[i]));
	        }
	        return sb.toString();
	    } catch (NoSuchAlgorithmException ex) {
	    	ex.printStackTrace();
	    }
	        return null;

	   }

}

