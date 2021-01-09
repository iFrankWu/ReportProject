/*
 * OpenApi.java
 * Jul 16, 2013
 * com.tibco.rest
 * ReportProject
 * Copyright (C), 2013, TIBCO Software Inc.
 *
 */
package com.tibco.rest;

import com.tibco.bean.Report;
import com.tibco.service.LogRecordService;
import com.tibco.service.ReportService;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * class description goes here.
 *
 * @author <a href="mailto:swu@tibco-support.com">Frank Wu</a>
 * @version 1.0.0
 */
@Path("/openapi")
public class OpenApi {
    private ReportService service = new ReportService();
    private LogRecordService logService = new LogRecordService();

    private Logger logger = Logger.getLogger(this.getClass());

    @Context
    HttpServletRequest request;

    @Path("{caseNumber}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> getReportList(@PathParam("caseNumber") String caseNumber) {

        Map<String, Object> map = new HashMap<String, Object>();
        try {
            List reportList = service.getReportByCaseNumber(caseNumber);
            map.put("reports", reportList);
            map.put("isSuccess", true);
            map.put("description", "获取报告单成功");
        } catch (Exception e) {
            logger.error("获取报告单失败，原因", e);
            map.put("description", "获取报告单失败，原因：" + e.getMessage());
            map.put("isSuccess", false);
            map.put("reports", null);
        }
        return map;
    }

    @Path("detail/{no}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> getReport(@PathParam("no") String no) {

        Map<String, Object> map = new HashMap<String, Object>();
        try {
            Report report = service.getReportsByOutpatientNoOrAdmissionNo(no);
            map.put("report", report);
            map.put("isSuccess", true);
            map.put("description", "获取报告单成功");
        } catch (Exception e) {
            logger.error("获取报告单失败，原因", e);
            map.put("description", "获取报告单失败，原因：" + e.getMessage());
            map.put("isSuccess", false);
            map.put("report", null);
        }
        return map;
    }


}

