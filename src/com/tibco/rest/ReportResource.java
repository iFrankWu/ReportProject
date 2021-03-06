/*
 * ReportResource.java
 * Jun 24, 2013
 * com.tibco.rest
 * ReportProject
 * Copyright (C), 2013, TIBCO Software Inc.
 * 
 */
package com.tibco.rest;

import com.tibco.bean.Report;
import com.tibco.bean.Result;
import com.tibco.bean.Search;
import com.tibco.service.LogRecordService;
import com.tibco.service.ReportService;
import com.tibco.util.Const;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

/**
 * class description goes here.
 *
 * @author <a href="mailto:wushexin@gmail.com">Frank Wu</a>
 * @version 1.0.0
 */
@Path("/report")
public class ReportResource {
    @Context
    HttpServletRequest request;
    private Logger logger = Logger.getLogger(this.getClass());
    private ReportService service = new ReportService();
    private LogRecordService logService = new LogRecordService();
    private RestUnit restUnit = new RestUnit();

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Result addReport(Report report) {
        Result result = null;
        try {
            report.setDoctorId((Integer) request.getSession().getAttribute(Const.CURRENT_USER_ID));
            report.setDoctorName((String) request.getSession().getAttribute(Const.CURRENT_USER_NAME));
            result = service.addReport(report);

            logService.addLogRecord(request, "增加报告单", report.toString());
        } catch (Exception e) {
            String msg = "创建报告单失败： " + e.getMessage();
            logger.error(msg, e);
            result = new Result(false, e.getMessage());
        }
        return result;
    }

    @Path("{reportId}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Result deleteReport(@PathParam("reportId") Integer reportId) {
        try {
            Report report = service.getReportByID(reportId);
            Boolean isOwner = report.getDoctorId().equals(request.getSession().getAttribute(Const.CURRENT_USER_ID));
            if (isOwner || restUnit.isDirect(request)) {
                service.deleteReportByID(reportId);
                logService.addLogRecord(request, "删除报告单", reportId + "");
                return new Result(true, "delete report~~");
            } else {
                return new Result(false, "没有权限删除该报告单");
            }
        } catch (Exception e) {
            String msg = "删除该报告单失败： " + e.getMessage();
            logger.error(msg, e);
            return new Result(false, e.getMessage());
        }
    }

    @Path("{reportId}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Result updateReport(@PathParam("reportId") Integer reportId, Report report) {
        try {
            if (report.getDoctorId() == null || report.getReportId() == null) {
                return new Result(false, "传入参数有误，请退出重新登录后尝试。");
            }
            Boolean isOwner = report.getDoctorId().equals(request.getSession().getAttribute(Const.CURRENT_USER_ID));
            Report beforeUpReport = service.getReportByID(report.getReportId());
            if (doesChangeCheckResult(beforeUpReport.getCheckResult(), report.getCheckResult())) {
                if (!isOwner) {
                    return new Result(false, "报告单的检查检查结果，仅限此报告单的创建者修改，请联系此报告单的创建者：" + report.getDoctorName());
                }
            }
            if (isOwner || restUnit.isDirect(request)) {
                service.updateReport(report);
                logService.addLogRecord(request, "更新报告单", report.toString());
                return new Result(true, "update~~");
            } else {
                return new Result(false, "没有权限更新该报告单");
            }
        } catch (Exception e) {
            String msg = "更新报告单内容失败，报告单ID: " + report.getReportId() + "，错误原因： " + e.getMessage();
            logger.error(msg, e);
            return new Result(false, msg);
        }
    }

    private boolean doesChangeCheckResult(String beforeUpValue, String updateValue) {
        if (beforeUpValue == null) {
            return updateValue == null ? false : true;
        }
        if (beforeUpValue.equals(updateValue)) {
            return false;
        } else {
            return true;
        }
    }

    @Path("{reportId}/Next/{size}/{page}/{sortColumn}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> getNextPage(@PathParam("reportId") Integer maxId, @PathParam("size") Integer size, @PathParam("page") Integer page, @PathParam("sortColumn") String sortColumn) {
        try {
            if (maxId == 1) {// since angularJS will not send the int value zero.
                maxId = 0;
            }
            logService.addLogRecord(request, "获取下一页报告单", maxId + "" + size + "" + page + "" + sortColumn);
            return service.getNextPage(maxId, size, page, sortColumn);
        } catch (Exception e) {
            logger.error("", e);
        }
        return null;
    }

    @Path("{reportId}/Pre/{size}/{page}/{sortColumn}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> getPrePage(@PathParam("reportId") Integer minId, @PathParam("size") Integer size, @PathParam("page") Integer page, @PathParam("sortColumn") String sortColumn) {
        try {
            logService.addLogRecord(request, "获取上一页报告单", minId + "" + size + "" + page + "" + sortColumn);
            return service.getPrePage(minId, size, page, sortColumn);
        } catch (Exception e) {
            logger.error("", e);
        }
        return null;
    }

    @Path("{reportId}/Current/{size}/{page}/{sortColumn}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> getCurrentPage(@PathParam("reportId") Integer minId, @PathParam("size") Integer size, @PathParam("page") Integer page, @PathParam("sortColumn") String sortColumn) {
        try {
//    		if(minId == 1){// since angularJS will not send the int value zero.
//    			minId = 0;
//    		}
            logService.addLogRecord(request, "获取当前页报告单", minId + "" + size + "" + page + "" + sortColumn);
            return service.getCurrentPage(minId, size, page, sortColumn);
        } catch (Exception e) {
            logger.error("", e);
        }
        return null;
    }

    @Path("{reportId}/Top/{size}/{page}/{sortColumn}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> getTopPage(@PathParam("reportId") Integer minId, @PathParam("size") Integer size, @PathParam("page") Integer page, @PathParam("sortColumn") String sortColumn) {
        try {
            logService.addLogRecord(request, "获得第一页报告单", minId + "" + size + "" + page + "" + sortColumn);
            return service.getTopPage(size, sortColumn);
        } catch (Exception e) {
            logger.error("", e);
        }
        return null;
    }

    @Path("{reportId}/Detail/{size}/{page}/{sortColumn}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> getDetail(@PathParam("reportId") Integer reportId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("isSuccess", false);
        try {

            Report report = service.getReportByID(reportId);
            map.put("isSuccess", true);
            map.put("data", report);
            return map;
        } catch (Exception e) {
            logger.error("", e);
            map.put("isSuccess", false);
            map.put("data", e.getMessage());
        }
        return map;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> advanceSearch(Search search) {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("isSuccess", true);
        try {
            map.put("reportList", service.advanceSerach(search));
            logService.addLogRecord(request, "高级搜索", search.toString());
        } catch (Exception e) {
            logger.error("", e);

        }
        return map;
    }

    @Path("{reportId}/Next/{size}/{page}/{sortColumn}/{uid}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Result getPNormByUid(@PathParam("reportId") Integer minId, @PathParam("size") Integer size, @PathParam("page") Integer page, @PathParam("sortColumn") String sortColumn, @PathParam("uid") String uid) {
//        String os = System.getProperty("os.name");
//        String osUser = System.getProperty("user.name");
//        String dir = "/Users/frank/Downloads";
//        if (os != null && os.startsWith("Windows")) {
//            dir = "D:\\truscreen\\checkresult";
//        }
//
//        File file = new File(dir);
//        if (file.isDirectory()) {
//            for (File checkResultFile : file.listFiles()) {
//                if (checkResultFile.getName().endsWith("-" + uid + "-result.csv")) {
//                    String pnorm = service.parsePnormFromCsv(checkResultFile.getAbsolutePath());
//                    return new Result(true, Float.parseFloat(pnorm));
//                }
//            }
//        }
//        return new Result(false, "获取pnorm值失败，请检查是否文件已经同步到" + dir + "文件夹下");

        //UID为UEID 是手持设备返回的ID

        return null;
    }

    @Path("{reportId}/Next/{size}/{page}/{sortColumn}/{uid}/{patientName}/{outpatientNo}/{admissionNo}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Result getPatientInfo(@PathParam("reportId") Integer minId,
                                 @PathParam("size") Integer size, @PathParam("page") Integer page,
                                 @PathParam("sortColumn") String sortColumn, @PathParam("uid") String uid,
                                 @PathParam("patientName") String patientName,
                                 @PathParam("outpatientNo") String outpatientNo,
                                 @PathParam("admissionNo") String admissionNo
    ) {
        try {
            //outpatientNo： 门诊  admissionNo：住院
            logger.info("outpatientNo:" + outpatientNo + ",admissionNo:" +admissionNo );

            if(StringUtils.equals(outpatientNo,"-2")){
                outpatientNo = "";
            }
            if(StringUtils.equals(admissionNo,"-2")){
                admissionNo = "";
            }


            Report report = service.getPatientInfo(outpatientNo, admissionNo);
            return new Result(true, report);
        } catch (Exception e) {
            logger.error("aa", e);
        }

        return null;
    }
}

