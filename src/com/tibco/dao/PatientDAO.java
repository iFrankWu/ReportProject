package com.tibco.dao;

import com.shinetech.sql.*;
import com.shinetech.sql.exception.DBException;
import com.tibco.bean.Report;
import com.tibco.util.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PatientDAO {

    private Logger logger = Logger.getLogger(this.getClass());
    /**
     * 住院ID
     */
    private final static String COLUMN_ZYID = "ZYID";
    /**
     * 姓名
     */
    private final static String COLUMN_XM = "XM";
    /**
     * 门诊号
     */
    private final static String COLUMN_MZH = "MZH";
    /**
     * 出生日期
     */
    private final static String COLUMN_CSRQ = "csrq";

    /**
     * 家庭住址
     */
    private final static String COLUMN_JTZZ = "jtzz";
    /**
     * 联系电话
     */
    private final static String COLUMN_LXDH = "lxdh";


    private final  static String CUST_NAME = "cust_name";
    private final  static String CUST_BIRTHDAY = "cust_birthday";
    private final  static String CUST_AGE = "cust_age";

    /**
     * 姓名
     */
    private final  static String XM0000 = "XM0000";
    /**
     * 出生日期
     */
    private final  static String CSRQ00 = "CSRQ00";


    private IDatabaseAccess db = (new DatabaseAccessBuilder()).getDatabaseAccess("mysql");

    public Report getPatientInfo(String mzh, String zyid) throws DBException {
        if (StringUtils.isBlank(mzh)) {
            return new Report();
        }

        if(mzh.equals("-1") ){
            return mockReport();
        }

        final Report report = new Report();

        String sql = "select  XM0000, CSRQ00  from vw_esi_hmgnyq_tjz000 where TJH000 = ？ limit 1";
        List<FieldParameter> fpList = new ArrayList<FieldParameter>();
        fpList.add(new FieldParameter(1, mzh, FieldTypes.VARCHAR));

        db.handleList(sql, fpList, new ResultSetHandler() {
            @Override
            public void handle(ResultSet resultSet) {
                try {
                    ResultSetMetaData rsmd = resultSet.getMetaData();
                    int columnCount = rsmd.getColumnCount();
                    for (int i = 1; i <= columnCount; i++) {
                        Object r = resultSet.getObject(i);
                        logger.info(rsmd.getColumnName(i) + ":\t" + r);
                    }

                    report.setPatientName(resultSet.getString(XM0000));
//                    report.setAge(resultSet.getInt(CUST_AGE));
//                    report.setPatientName(resultSet.getString(COLUMN_XM));
//                    report.setOutpatientNo(resultSet.getString(COLUMN_MZH));
//                    report.setAdmissionNo(resultSet.getString(COLUMN_ZYID));
//                    report.setAddress(resultSet.getString(COLUMN_JTZZ));
//                    report.setPhone(resultSet.getString(COLUMN_LXDH));
                    String csrq = resultSet.getString(CSRQ00);
                    if (StringUtils.isNotBlank(csrq)) {
                        int csrqYear = DateUtil.getYear(csrq);
                        int thisYear = DateUtil.getYear(new Date());
                        int age = thisYear - csrqYear;
                        report.setAge(age);
                    }
                } catch (Exception e) {
                    logger.error(e);
                }
            }
        });

        return report;

    }



    private Report mockReport(){
        Report report = new Report();
        report.setAge(22);
        report.setPhone("18899996666");
        report.setAdmissionNo("-1");
        report.setOutpatientNo("-1");
        report.setPatientName("测试姓名");
        report.setAddress("北京市西长安街3号");
        return  report;
    }
}
