package com.tibco.integration.hhd;

import com.tibco.bean.Report;
import com.tibco.dao.ReportDAO;
import com.tibco.service.HHDService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jetty.util.ajax.JSON;

import java.nio.charset.Charset;
import java.util.Map;

public class HHDResponseHandler {

    private ReportDAO reportDAO = new ReportDAO();
    private Logger logger = Logger.getLogger(this.getClass());

    private HHDService hhdService = new HHDService();


    public void handler(Map<String, String> responseMap) throws Exception {

        if (!HHDClient.getInstance().status) {
            return;
        }

        //登陆失败
        //'设备未就绪...'
        String status = responseMap.get("socket_status");
        if (StringUtils.isNotBlank(status)) {
            HHDClient.getInstance().setCurrecntStatus(status);
        }
        if ("退出登陆".equals(status)) {
            Thread.sleep(2000L);
            hhdService.login();
            return;
        }

        if ("登陆失败".equals(status)) {
            Thread.sleep(2000L);
            hhdService.login();
            return;
        }

        if ("登陆成功".equals(status)) {
            Thread.sleep(2000L);
            hhdService.ready();
            return;
        }
        if ("设备未就绪...".equals(status)) {
            Thread.sleep(2000L);
            hhdService.ready();
            return;
        }

        if ("检查过程中...".equals(status)) {
            return;
        }

        if ("设备就绪".equals(status)) {
            Thread.sleep(2000L);
            Report report = reportDAO.getLastReport();
            if (report.getUid() == null || report.getPnorValueResult() == null) {
                if (HHDClient.getInstance().isConnectedFisrt()) {
                    logger.info("设备刚刚连接WI-FI成功，忽略Start命令");
                    return;
                }
                hhdService.start(report.getPatientName(), report.getAge());
            }
            return;
        }


        if ("检查结束".equals(status)) {
            // hhdService.systemReport();
        }
        if (responseMap.containsKey("patient_01")) {
            String uid = responseMap.get("patient_01");
            Report report = reportDAO.getReportByUID(uid);
            if (report == null) {
                String name = responseMap.get("patient_05");
                String age = responseMap.get("patient_06");
                if (StringUtils.isBlank(name) || StringUtils.isBlank(age)) {
                    logger.info("患者姓名为空，结果忽略:" + JSON.toString(responseMap));
                    report = reportDAO.getLastReport();
                    reportDAO.updateReport(uid, report.getReportId());
                    return;
                }
                report = reportDAO.getReportByNameAndAge(name, Integer.parseInt(age));
                if (report == null) {
                    logger.warn(String.format("通过姓名%s和年龄%s无法获取报告单，可能是乱码导致", name, age));
                    logger.warn("GBK" + new String(name.getBytes(), Charset.forName("GBK")));
                    logger.warn("ISO-8859-11" + new String(name.getBytes(), Charset.forName("ISO-8859-1")));
                    report = reportDAO.getLastReport();
                }
                reportDAO.updateReport(uid, report.getReportId());
            } else {
                String checkDate = responseMap.get("patient_02");
                String checkResult = responseMap.get("screening_result");
                String pnorm = responseMap.get("screening_pnorm");
                String points = responseMap.get("screening_nSpots");
                if (StringUtils.isBlank(checkResult) || StringUtils.isBlank(pnorm) || StringUtils.isBlank(points)) {
                    logger.info("检查结果为空或者pnorm值为空，结果忽略:" + JSON.toString(responseMap));
                    return;
                }
                Float pnormValue = Float.parseFloat(pnorm);

                reportDAO.updateReport(Integer.parseInt(points), pnormValue, uid);
//                hhdService.logout();
                //断开手持设备
//                hhdService.terminate();
            }
        }

    }


}
