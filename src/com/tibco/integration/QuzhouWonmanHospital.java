package com.tibco.integration;

import com.alibaba.fastjson.JSONObject;
import com.shinetech.sql.exception.DBException;
import com.tibco.bean.Hospital;
import com.tibco.bean.Report;
import com.tibco.dao.HospitalDAO;
import com.tibco.dao.ReportDAO;
import com.tibco.integration.net.HttpSender;
import com.tibco.service.LogRecordService;
import com.tibco.util.RSAToolUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.tibco.util.Const.INTEGRATION_SERICE_URL;


/**
 * quzhou woman hospital integration
 */
public class QuzhouWonmanHospital {
    private static final Logger logger = Logger.getLogger(QuzhouWonmanHospital.class);
    private ReportDAO reportDAO = new ReportDAO();
    private LogRecordService logService = new LogRecordService();

    private HospitalDAO hospitalDAO = new HospitalDAO();


    private String serviceUrl = INTEGRATION_SERICE_URL;

    public void commitReport(Integer reportId) throws DBException, IOException {
        if (StringUtils.isBlank(INTEGRATION_SERICE_URL)) {
            logger.warn("no INTEGRATION_SERICE_URL found");
            return;
        }
        Report report = reportDAO.getReportByID(reportId);

        Hospital hospital = hospitalDAO.getHospital();

        JSONObject reportJson = (JSONObject) JSONObject.toJSON(report);

        JSONObject hospitalJson = (JSONObject) JSONObject.toJSON(hospital);

        reportJson.putAll(hospitalJson);

        String data = RSAToolUtil.RSAEncode(reportJson.toJSONString());
        logger.info("commit.report: " + reportJson);


        JSONObject rsaData = new JSONObject();
        rsaData.put("data",data);

        logger.info("commit.rsa.data: " + rsaData.toJSONString());

        String result = HttpSender.sendPost(serviceUrl, rsaData.toJSONString());


        JSONObject rst = (JSONObject) JSONObject.parse(result);
        if (StringUtils.isNotBlank(rst.toString()) && "1".equals(rst.getString("code"))) {
            reportDAO.updateReportStatus(report.getReportId(), "SYNCED");
        }

    }


    public void batchCommitReport() throws Exception {
        List<Report> reportList = reportDAO.getReportByStatus("INIT");
        for (Report report : reportList) {
            try {
                commitReport(report.getReportId());
            } catch (Exception e) {
                logger.error("commit serivce error :"+ JSONObject.toJSONString(report), e);
            }
        }
    }


    public void init() {
        if (StringUtils.isBlank(INTEGRATION_SERICE_URL)) {
            logger.warn("no INTEGRATION_SERICE_URL found");
            return;
        }
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1);
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    batchCommitReport();
                } catch (Exception e) {
                    logger.error("timer error", e);
                }
            }
        }, 0, 2, TimeUnit.MINUTES);
    }
}
