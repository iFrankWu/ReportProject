package com.tibco.service;

import com.tibco.integration.hhd.HHDClient;
import org.apache.log4j.Logger;

import java.util.Date;

public class HHDService {

    private Logger logger = Logger.getLogger(this.getClass());

    /**
     * {"socket_request":"login","login_1":"TruScreenSKT","login_2":"Socket123","patient_05":"lina","patient_06":32}
     *
     * @param
     */
    public void login() {
        HHDClient client = HHDClient.getInstance();
        StringBuffer request = new StringBuffer();
        request.append("login_1='TruScreenSKT'");
        request.append("\r\n");
        request.append("login_2='Socket123'");
        request.append("\r\n");
        request.append("socket_request='login'");
        request.append("\r\n");
        client.sendMsg(request.toString());

        logger.info(new Date() + "\t发起手持设备登陆请求:" + HHDClient.getInstance().getCurrecntStatus() + "\t" + request.toString());
    }

    /**
     * {"socket_request":"start","patient_05":"lina","patient_06":32}
     *
     * @param patientName
     */
    public void start(String patientName, int age) {
        HHDClient client = HHDClient.getInstance();
        StringBuffer request = new StringBuffer();
        request.append("patient_05='" + patientName + "'");
        request.append("\r\n");
        request.append("patient_06='" + age + "'");
        request.append("\r\n");
        request.append("socket_request='start'");
        request.append("\r\n");
        client.sendMsg(request.toString());

        logger.info(new Date() + "\t发起手持设备开始检测请求:" + HHDClient.getInstance().getCurrecntStatus() + "\t" + request.toString());
    }


    public void ready() {

        if ("设备就绪".equals(HHDClient.getInstance().getCurrecntStatus())) {
            logger.info(new Date()+"\t当前状态已经为设备就绪，忽略请求:" + HHDClient.getInstance().getCurrecntStatus());
            return;
        }

        HHDClient.getInstance().status = true;
        HHDClient client = HHDClient.getInstance();
        StringBuffer request = new StringBuffer();
        request.append("socket_request='ready'");
        request.append("\r\n");
        client.sendMsg(request.toString());

        logger.info(new Date() + "\t发起手持设备就绪请求:" + HHDClient.getInstance().getCurrecntStatus() + "\t" + request.toString());
    }

    public void terminate() {
        HHDClient.getInstance().status = false;
        HHDClient client = HHDClient.getInstance();
        StringBuffer request = new StringBuffer();
        request.append("socket_request='terminate'");
        request.append("\r\n");
        client.sendMsg(request.toString());

        logger.info(new Date() + "\t发起手持设备中断请求:" + HHDClient.getInstance().getCurrecntStatus() + "\t" + request.toString());
    }

    public void logout() {
        HHDClient.getInstance().status = false;
        HHDClient client = HHDClient.getInstance();
        StringBuffer request = new StringBuffer();
        request.append("socket_request='logout'");
        request.append("\r\n");
        client.sendMsg(request.toString());

        logger.info(new Date() + "\t发起手持设备登出请求:" + HHDClient.getInstance().getCurrecntStatus() + "\t" + request.toString());
    }

    public void exit() {
        HHDClient.getInstance().status = false;
        HHDClient client = HHDClient.getInstance();
        StringBuffer request = new StringBuffer();
        request.append("socket_request='exit'");
        request.append("\r\n");
        client.sendMsg(request.toString());
        logger.info(new Date() + "\t发起手持设备退出请求:" + HHDClient.getInstance().getCurrecntStatus() + "\t" + request.toString());
    }

    public void systemReport() {
        HHDClient client = HHDClient.getInstance();
        StringBuffer request = new StringBuffer();
        request.append("socket_request='system_report'");
        request.append("\r\n");
        client.sendMsg(request.toString());

        logger.info(new Date() + "\t检测结束获取报告单结果请求:" + HHDClient.getInstance().getCurrecntStatus() + "\t" + request.toString());
    }

    public void socketStatus() {
        HHDClient client = HHDClient.getInstance();
        StringBuffer request = new StringBuffer();
        request.append("socket_status?");
        request.append("\r\n");
        client.sendMsg(request.toString());

        logger.info(new Date() + "\t获取手持设备状态:" + HHDClient.getInstance().getCurrecntStatus() + "\t" + request.toString());
    }
}
