/*
 * ReportDAOTest.java
 * Jul 7, 2013
 * com.tibco.dao.test
 * ReportProject
 * Copyright (C), 2013, TIBCO Software Inc.
 *
 */
package com.tibco.dao.test;

import com.shinetech.sql.exception.DBException;
import com.tibco.dao.HospitalDAO;
import com.tibco.dao.ReportDAO;
import junit.framework.TestCase;

/**
 * HistoryDAOTest
 *
 * @author <a href="mailto:wushexin@gmail.com">Frank Wu</a>
 * @version 1.0.0
 */
public class HistoryDAOTest extends TestCase {
    private HospitalDAO dao = new HospitalDAO();

    public void testDeleteLogo() {
        try {
            dao.deleteLogo(1);
        } catch (DBException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
    }
}

