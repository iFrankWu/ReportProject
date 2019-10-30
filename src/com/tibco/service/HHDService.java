package com.tibco.service;

import com.tibco.bean.HHDOpreationDTO;
import com.tibco.integration.hhd.HHDClient;

public class HHDService {

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
    }

    public void commonRequest(HHDOpreationDTO hddOpreationDTO) {
        HHDClient client = HHDClient.getInstance();
        StringBuffer request = new StringBuffer();
        request.append("socket_request='" + hddOpreationDTO.getSocket_request() + "'");
        request.append("\r\n");
        client.sendMsg(request.toString());
    }


    public void ready() {

        if ("设备就绪".equals(HHDClient.getInstance().getCurrecntStatus())) {
            return;
        }

        HHDClient.getInstance().status = true;
        HHDClient client = HHDClient.getInstance();
        StringBuffer request = new StringBuffer();
        request.append("socket_request='ready'");
        request.append("\r\n");
        client.sendMsg(request.toString());
    }

    public void terminate() {
        HHDClient.getInstance().status = false;
        HHDClient client = HHDClient.getInstance();
        StringBuffer request = new StringBuffer();
        request.append("socket_request='terminate'");
        request.append("\r\n");
        client.sendMsg(request.toString());
    }

    public void logout() {
        HHDClient.getInstance().status = false;
        HHDClient client = HHDClient.getInstance();
        StringBuffer request = new StringBuffer();
        request.append("socket_request='logout'");
        request.append("\r\n");
        client.sendMsg(request.toString());
    }

    public void exit() {
        HHDClient.getInstance().status = false;
        HHDClient client = HHDClient.getInstance();
        StringBuffer request = new StringBuffer();
        request.append("socket_request='exit'");
        request.append("\r\n");
        client.sendMsg(request.toString());
    }

    public void systemReport() {
        HHDClient client = HHDClient.getInstance();
        StringBuffer request = new StringBuffer();
        request.append("socket_request='system_report'");
        request.append("\r\n");
        client.sendMsg(request.toString());
    }

    public void socketStatus() {
        HHDClient client = HHDClient.getInstance();
        StringBuffer request = new StringBuffer();
        request.append("socket_request='socket_status'");
        request.append("\r\n");
        client.sendMsg(request.toString());
    }
}
