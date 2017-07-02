/*
 * RecordResource.java
 * Jul 1, 2013
 * com.tibco.rest
 * ReportProject
 * Copyright (C), 2013, TIBCO Software Inc.
 * 
 */
package com.tibco.rest;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.json.simple.JSONObject;

import com.shinetech.sql.exception.DBException;
import com.tibco.bean.Doctor;
import com.tibco.bean.LogRecord;
import com.tibco.service.DoctorService;
import com.tibco.service.HospitalService;
import com.tibco.service.LogRecordService;
import com.tibco.service.ReportService;
import com.tibco.util.Const;

/**
 * class description goes here.
 *
 * @author <a href="mailto:swu@tibco-support.com">Frank Wu</a>
 * @version 1.0.0
 */
@Path("/record")
public class LogRecordResource {
		@Context
	    HttpServletRequest request;
		private LogRecordService service = new LogRecordService();
		private HospitalService hospitalService = new HospitalService();
		private ReportService reportService = new ReportService();
		
		private DoctorService doctorService = new DoctorService();
		private Logger logger = Logger.getLogger(this.getClass());
	    @POST
	    @Produces(MediaType.APPLICATION_JSON)
	    public Map<String,Object> getRecordList(JSONObject para){
//	    	System.out.println(para);
	    	Map<String,Object> map = new HashMap<String,Object>();
	    	if(para.get("onlyMyself") != null && para.get("onlyMyself").equals(true)){
	    		para.put("doctorId", request.getSession().getAttribute(Const.CURRENT_USER_ID));
	    	}else{
	    		if(para.get("doctorName") != null){
	    			String doctorName = (String)para.get("doctorName");
	    			try {
						Doctor doctor = doctorService.getDoctorByName(doctorName);
						if(doctor == null){
							map.put("isSuccess", false);
							map.put("description", "获取日志失败：不存在医生名字为 "+doctorName);
							return map;
						}
						if(request.getSession().getAttribute(Const.CURRENT_USER_TYPE).equals(Const.TYPE_Direct)){
							if(doctor.getType().equals(Const.TYPE_SystemAdmin)){//  admin forbidden to search  system admin log
								map.put("isSuccess", false);
								map.put("description", "获取日志失败：不存在医生名字为 "+doctorName);
								return map;
							}
						}
						para.put("doctorId",doctor.getDoctorId());
					} catch (DBException e) {
						e.printStackTrace();
						map.put("isSuccess", false);
						map.put("description", "获取日志失败："+e.getMessage());
						logger.error("获取日志失败："+e.getMessage());
						return map;
					}
	    		}
	    	}
	    	List<LogRecord> logs;
			try {
				logs = service.getLogList(para);
				map.put("isSuccess", true);
				map.put("recordList", logs);
				service.addLogRecord(request,"获取日志",para.toJSONString());
			} catch (DBException e) {
				e.printStackTrace();
				map.put("isSuccess", false);
				map.put("description", "获取日志失败："+e.getMessage());
				logger.error("获取日志失败："+e.getMessage());
			}
	    	return map;
	    }
	    
	    @PUT
	    @Produces(MediaType.APPLICATION_JSON)
	    public Map<String,Object> statistic(JSONObject data){
	    	Map<String,Object> map = new HashMap<String,Object>();
	    	map.put("isSuccess", true);
	    	try {
				map.put("statisticResult", reportService.statistic(data));
				service.addLogRecord(request,"统计检查结果",data.toJSONString());
			} catch (DBException e) {
				e.printStackTrace();
				map.put("isSuccess", false);
				map.put("descrption", "获取统计结果失败");
			}
	    	return map;
	    }
 
	    
	    @POST
	    @Path("exports")
	    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8")
	    public Response exportExcel(String requestStr)  throws Exception  {
	    	
	    	System.out.println(requestStr);
	    	String startDate = null,endDate = null;
	    	if(requestStr != null){
	    		String req[] = requestStr.split("&");
	    		String startDates[] = req[0].split("=");
	    		String endDates[] = req[1].split("=");
	    		if(startDates.length == 2){
	    			startDate = startDates[1];
	    		}
	    		if(endDates.length ==2){
	    			endDate = endDates[1];
	    		}
	    	}

	    	final Workbook workbook = reportService.exportDate(startDate, endDate);

	        StreamingOutput stream = new StreamingOutput() {
	            public void write(OutputStream output) throws IOException, WebApplicationException {
	                try {
	                	workbook.write(output);
	                } catch (Exception e) {
	                    throw new WebApplicationException(e);
	                }
	            }
	        };
	    	service.addLogRecord(request,"导出报告单",requestStr);
	    	
	    	String exportName = "truscreen_"+hospitalService.getHospital().getName();//+startDate+"_"+endDate+".xls";
	     
	    	if(startDate != null){
	    		exportName += "_s_"+startDate;
	    	}
	    	if(endDate != null){
	    		exportName += "_e_"+endDate;
	    	}
	    	exportName += ".xls";
	    	exportName =  URLEncoder.encode(exportName, "UTF-8");
	        return Response.ok(stream).header("content-disposition","attachment; filename = "+exportName).build();

	    }
}

