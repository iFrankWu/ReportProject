/*
 * Const.java
 * May 4, 2013
 * com.tibco.util
 * AngularJS
 * Copyright (C), 2013, TIBCO Software Inc.
 * 
 */
package com.tibco.util;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.xml.DOMConfigurator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * class description goes here.
 *
 * @author <a href="mailto:wushexin@gmail.com">Frank Wu</a>
 * @version 1.0.0
 */
public class Const {
    public static boolean doesLoggerInited = false;

    public static String STATUS_USEABLE = "可用";
    public static String STATUS_UNUSEABLE = "禁用";

    public static String TYPE_Direct = "管理员";
    public static String TYPE_ApplyDoctor = "申请医生";
    public static String TYPE_Doctor = "操作人员";
    public static String TYPE_SystemAdmin = "系统管理员";
    public static String LOGIN_SUCCESS = "loginSuccess";
    public static String LOGIN_PATH = "doctor";
    public static String METHOD_POST = "POST";
    public static String METHOD_GET = "GET";
    public static String CURRENT_USER_TYPE = "type";
    public static String CURRENT_USER_ID = "currentDoctorId";
    public static String CURRENT_USER_NAME = "currentDoctorName";

    public static String OPEN_API_PATH = "openapi";

    public static String ACTION_CheckStudent = "doesExistStudent";
    public static String ACTION_SearchStudent = "searchStudent";
    public static String ACTION_GetStuNameById = "getStuNameById";
    public static String ACTION_SerachPaymentByDoctor = "serachPaymentByDoctor";
    public static String ACTION_AddCommunicate = "addCommunicate";

    public static Integer SESSION_TIMEOUT_IN_MINUTE = 4 * 60;

    public static List<String> WHITE_ACCESS_IP_LIST ;


    public static void initLogger() {
        if (!doesLoggerInited) {
            String log4j = System.getProperty("user.dir") + File.separator + "config" + File.separator + "log4j.xml";
            System.out.println(log4j);
            DOMConfigurator.configure(log4j);
            doesLoggerInited = true;
            Properties pro = new Properties();
            try {
                pro.load(new FileInputStream("config/db.properties"));
                String sessionTimeOuts = pro.getProperty("default.sessionTimeout", "240");
                SESSION_TIMEOUT_IN_MINUTE = Integer.parseInt(sessionTimeOuts);
                System.out.println("SESSION_TIMEOUT_IN_MINUTE:" + SESSION_TIMEOUT_IN_MINUTE);

                String whiteAccesssIp = pro.getProperty("default.whiteAccessIp");
                if (StringUtils.isNotBlank(whiteAccesssIp)) {
                    WHITE_ACCESS_IP_LIST = Arrays.asList(whiteAccesssIp.split(","));
                }

                System.out.println("WHITE_ACCESS_IP_LIST:" + WHITE_ACCESS_IP_LIST);


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();

            }
        }
    }
}

