package com.tibco.bean;


public class HHDOpreationDTO {

    /**
     * 登陆名
     */
    private String login_1;

    /**
     * 密码
     */
    private String login_2;

    /**
     * 请求状态
     * <p>
     * There are 7 TCP client requests which be made using the string socket_request=‘<command>’, these are:
     * <p>
     * ·         login
     * <p>
     * ·         logout
     * <p>
     * ·         ready
     * <p>
     * ·         start
     * <p>
     * ·         system_report
     * <p>
     * ·         exit
     * <p>
     * ·         terminate (Not fully implemented yet)
     */
    private String socket_request;

    /**
     * 病人姓名
     */
    private String patient_05;
    /**
     * 姓名年龄
     */
    private String patient_06;


    public String getLogin_1() {
        return login_1;
    }

    public void setLogin_1(String login_1) {
        this.login_1 = login_1;
    }

    public String getLogin_2() {
        return login_2;
    }

    public void setLogin_2(String login_2) {
        this.login_2 = login_2;
    }

    public String getSocket_request() {
        return socket_request;
    }

    public void setSocket_request(String socket_request) {
        this.socket_request = socket_request;
    }

    public String getPatient_05() {
        return patient_05;
    }

    public void setPatient_05(String patient_05) {
        this.patient_05 = patient_05;
    }

    public String getPatient_06() {
        return patient_06;
    }

    public void setPatient_06(String patient_06) {
        this.patient_06 = patient_06;
    }
}
