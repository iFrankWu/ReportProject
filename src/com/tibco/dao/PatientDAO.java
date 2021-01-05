package com.tibco.dao;

import com.shinetech.sql.DatabaseAccessBuilder;
import com.shinetech.sql.IDatabaseAccess;
import com.shinetech.sql.ResultSetHandler;
import com.shinetech.sql.exception.DBException;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class PatientDAO {

    private Logger logger = Logger.getLogger(this.getClass());

    private IDatabaseAccess db = (new DatabaseAccessBuilder()).getDatabaseAccess("sqlserver");

    public void getPatientInfo() throws DBException {
        String sql = "select top 10 * from [WK_Interface].[dbo].[WK_ZYBRJBXX]";
        db.handleList(sql, new ResultSetHandler() {
            @Override
            public void handle(ResultSet resultSet) {
                try {
                    ResultSetMetaData rsmd = resultSet.getMetaData();
                    int columnCount = rsmd.getColumnCount();
                    for (int i = 1; i <= columnCount; i++) {
                        Object r = resultSet.getObject(i);
                        logger.info(rsmd.getCatalogName(i) + ":" + r);
                    }
                } catch (Exception e) {
                    logger.error(e);
                }
            }
        });

    }
}
