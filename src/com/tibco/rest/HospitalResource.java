/*
 * HospotalResource.java
 * Jun 30, 2013
 * com.tibco.rest
 * ReportProject
 * Copyright (C), 2013, TIBCO Software Inc.
 * 
 */
package com.tibco.rest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

import com.shinetech.sql.exception.DBException;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import com.tibco.bean.Doctor;
import com.tibco.bean.Hospital;
import com.tibco.bean.Result;
import com.tibco.service.DoctorService;
import com.tibco.service.HospitalService;
import com.tibco.service.LogRecordService;

/**
 * class description goes here.
 *
 * @author <a href="mailto:swu@tibco-support.com">Frank Wu</a>
 * @version 1.0.0
 */
@Path("/hospital")
public class HospitalResource {
	@Context
	HttpServletRequest request;
	@Context
	HttpServletResponse response;
	private Logger logger = Logger.getLogger(this.getClass());
		private HospitalService hospitalService = new HospitalService();
		private LogRecordService logService = new LogRecordService();
		private RestUnit restUnit = new RestUnit();

		@POST
//	    @Produces(MediaType.APPLICATION_JSON)
	    @Consumes(MediaType.MULTIPART_FORM_DATA)
	    public Response  updateHospital(@FormDataParam("file") InputStream uploadedInputStream,
				@FormDataParam("file") FormDataContentDisposition fileDetail,
				@FormDataParam("name") String name,
				@FormDataParam("machineNumber") String machineNumber,
				@FormDataParam("handController") String handController,
				@FormDataParam("firmwareVersion") String firmwareVersion,
				@FormDataParam("hospitalId") Integer hospitalId,
				@FormDataParam("department") String department
//				@FormDataParam("prescribingDoctorName") String prescribingDoctorName
			 ){

	    	if(!restUnit.isSystemAdmin(request)){
	    		return Response.status(500).entity("<font color='red' size='7px'>您无权限更新系统信息，请联系厂商</font>").build(); 
	    	}
	    	try{
	    		String uploadedFileLocation = null;
	    		if(uploadedInputStream == null || fileDetail== null || fileDetail.getFileName() == null || "".equals(fileDetail.getFileName()) ){

	    		}else{
		    		String prefix = "./web/";
		    		String IMAGE_PATTERN = "([^\\s]+(\\.(?i)(jpg|png|gif|bmp))$)";
		    		Pattern  pattern = Pattern.compile(IMAGE_PATTERN);
		    		Matcher  matcher = pattern.matcher(fileDetail.getFileName());
		    		if(matcher.matches()){
		    			//http://www.mkyong.com/regular-expressions/how-to-validate-image-file-extension-with-regular-expression/
			    		uploadedFileLocation = "upload/"+ fileDetail.getFileName();
						writeToFile(uploadedInputStream, prefix+uploadedFileLocation);
					}else{
						return Response.status(500).entity("<font color='red' size='7px'>更新系统信息失败，请上传图片文件</font>").build(); 
					}
					
		    	}
		    		Hospital hospital = new Hospital();
		    		hospital.setFirmwareVersion(firmwareVersion);
		    		hospital.setHandController(handController);
		    		hospital.setHospitalId(hospitalId);
		    		hospital.setMachineNumber(machineNumber);
		    		hospital.setDepartment(department);
//		    		hospital.setPrescribingDoctorName(prescribingDoctorName);
		    		if(uploadedFileLocation != null){
		    			hospital.setHospitalLogo(uploadedFileLocation);
		    		}
		    		hospital.setName(name);
		    		hospitalService.updateHosptal(hospital);
		    		logService.addLogRecord(request, "更新系统信息", hospital.toString());
		    		return Response.status(200).entity("<font  size='5px'>更新系统信息成功,请刷新页面获得更新后的系统信息</font>").build();
	    		 
	    	}catch(DBException e){
	    		e.printStackTrace();
	    		logger.error("更新系统信息失败："+e.getMessage());
	    		return Response.status(500).entity("<font color='red' size='7px'>更新系统信息失败："+e.getMessage()+"</font>").build();
	    	}
	    }
	    
	 // save uploaded file to new location
		private void writeToFile(InputStream uploadedInputStream,
				String uploadedFileLocation) {

			try {
				OutputStream out = new FileOutputStream(new File(
						uploadedFileLocation));
				int read = 0;
				byte[] bytes = new byte[1024];

				out = new FileOutputStream(new File(uploadedFileLocation));
				while ((read = uploadedInputStream.read(bytes)) != -1) {
					out.write(bytes, 0, read);
				}
				out.flush();
				out.close();
			} catch (IOException e) {

				e.printStackTrace();
			}

		}

	    
	    @GET
	    @Produces(MediaType.APPLICATION_JSON)
	    public Hospital getHospital(){
	    	try{
	    		Hospital hospital = hospitalService.getHospital();
	    		if(hospital == null){
	    			hospital = new Hospital();
			    	hospital.setHospitalLogo(null);
			    	hospital.setDepartment("妇科/体检");
			    	hospital.setFirmwareVersion("V1.0");
			    	hospital.setHandController("ASDF-FDES-SSDF-SSSS");
			    	hospital.setMachineNumber("100002");
			    	hospital.setName("北京市第一人民医院");
			    	Integer hospitalId = hospitalService.addHospital(hospital);
			    	hospital.setHospitalId(hospitalId);
	    		}
	    		logService.addLogRecord(request, "获取系统信息", "");
		    	return hospital;
	    	}catch(DBException e){
	    		e.printStackTrace();
	    		logger.error("获取系统信息失败："+e.getMessage());
	    	}
	    	return null;
	    }
	    @Path("{hospitalId}")
	    @DELETE
	    @Produces(MediaType.APPLICATION_JSON)
	    public Result deleteLogo(@PathParam("hospitalId")Integer hospitalId) {
	    	try {
	    		if(!restUnit.isSystemAdmin(request)){
	    			return new Result(false,"无权限删除Logo，请联系厂商。");
		    	}
				hospitalService.deleteLogo(hospitalId);
				logService.addLogRecord(request, "删除Logo", hospitalId+"");
			} catch (DBException e) {
				e.printStackTrace();
				
			}
	    	return new Result(true,"");
	    }
	    
	   
	    @PUT
	    @Produces(MediaType.APPLICATION_JSON)
	    public Doctor getDoctor(JSONObject para) {
	    	try {
	    		Integer id = (Integer)para.get("id");
	    		DoctorService doctorService = new DoctorService();
	    		logService.addLogRecord(request,"获取医生信息",id+"");
				return doctorService.getDoctorById(id);
			} catch (DBException e) {
				String msg = "获取医生信息失败： "+e.getMessage();
				logger.error(msg);
				e.printStackTrace();
				return null;
			}
	    	
	    }
}

