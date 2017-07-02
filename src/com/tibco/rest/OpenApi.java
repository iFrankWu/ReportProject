/*
 * OpenApi.java
 * Jul 16, 2013
 * com.tibco.rest
 * ReportProject
 * Copyright (C), 2013, TIBCO Software Inc.
 * 
 */
package com.tibco.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.tibco.service.LogRecordService;
import com.tibco.service.ReportService;

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
	@Context
	HttpServletRequest request;
	 	@Path("{caseNumber}/Pre/{size}/")
	    @GET
	    @Produces(MediaType.APPLICATION_JSON)
	    public Map<String,Object> getPrePage( @PathParam("caseNumber")String caseNumber){
	 		
	 		Map<String,Object> map = new HashMap<String,Object>();
	 		try{
//	 			logService.addLogRecord(request, "开发API，开始获取报告单", caseNumber);
		    	 List reportList = service.getReportByCaseNumber(caseNumber);
		    	 map.put("reports", reportList);
		    	 map.put("isSuccess", true);
		    	 map.put("description", "获取报告单成功");
		    //	 logService.addLogRecord(request, "开发API，获取报告单成功", caseNumber);
	    	}catch(Exception e){
	    		e.printStackTrace();
	    		map.put("description", "获取报告单失败，原因："+e.getMessage());
    			map.put("isSuccess", false);
    			map.put("reports",null);
	    	}
	    	return map;
	    }
}

