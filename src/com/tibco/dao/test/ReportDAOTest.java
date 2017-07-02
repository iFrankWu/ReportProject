/*
 * ReportDAOTest.java
 * Jul 7, 2013
 * com.tibco.dao.test
 * ReportProject
 * Copyright (C), 2013, TIBCO Software Inc.
 * 
 */
package com.tibco.dao.test;

import junit.framework.TestCase;

import com.shinetech.sql.exception.DBException;
import com.tibco.dao.ReportDAO;

/**
 * class description goes here.
 *
 * @author <a href="mailto:swu@tibco-support.com">Frank Wu</a>
 * @version 1.0.0
 */
public class ReportDAOTest extends TestCase{
	private ReportDAO dao = new ReportDAO();
	public void testStatisticCheckResult(){
		try {
			dao.statisticCheckResult(null, null);
		} catch (DBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
	}
}

