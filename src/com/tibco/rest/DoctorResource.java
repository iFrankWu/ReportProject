/*
 * DoctorResource.java
 * Mar 28, 2013
 * org.timecontrol.rest
 * AngularJS
 * Copyright (C), 2013, TIBCO Software Inc.
 * 
 */
package com.tibco.rest;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.shinetech.sql.exception.DBException;
import com.tibco.bean.Doctor;
import com.tibco.bean.Result;
import com.tibco.service.DoctorService;
import com.tibco.service.LogRecordService;
import com.tibco.util.Const;

/*
 * @author <a href="mailto:swu@tibco-support.com">Frank Wu</a>
 * @version 1.0.0
 */
@Path("/doctor")
public class DoctorResource {
		@Context
	    HttpServletRequest request;
		
		private LogRecordService logService = new LogRecordService();
		private DoctorService doctorService = new DoctorService();
		private Logger logger = Logger.getLogger(this.getClass());
		
		private Boolean isAmin(){
			String doctorType = request.getSession().getAttribute(Const.CURRENT_USER_TYPE).toString();
			return Const.TYPE_Doctor.equals(doctorType) ? false : true;
		}
	    
	    @POST
	    @Produces(MediaType.APPLICATION_JSON)
	    public Result login(Doctor doctor) {
	    	try {
	    		Result result = doctorService.Login(doctor,request.getSession());
	    		if(result.getIsSuccess())
	    			logService.addLogRecord(request, "登陆", doctor.toString());
	    		return result;
			} catch (DBException e) {
				String msg = "登录失败: "+doctor.getDoctorName()+":"+e.getMessage();
				logger.error(msg);
				e.printStackTrace();
				return new Result(false,msg);
			}
	    }
	    
	    @Path("{id}")
	    @GET
	    @Produces(MediaType.APPLICATION_JSON)
	    public Result logout(@PathParam("id")Integer id) {
	    	try {
				logService.addLogRecord(request,"退出",null);
				request.getSession().invalidate();//直接销毁session
			} catch (DBException e) {
//				e.printStackTrace();
			}
	    	return new Result(true,"Logout success");
	    }
	    
	    @PUT
	    @Produces(MediaType.APPLICATION_JSON)
	    public Result addDoctor(Doctor doctor) {
	    	if(!isAmin()){
	    		return new Result(false,"执行失败：此操作需要主任医生权限");
	    	}
	    	try {
	    		logService.addLogRecord(request,"增加医生",doctor.toString());
				return doctorService.addDoctor(doctor);
			} catch (DBException e) {
				String msg = "增加医生失败，医生名字 : "+doctor.getDoctorName()+":"+e.getMessage();
				logger.error(msg);
				e.printStackTrace();
				return new Result(false,msg);
			}
	    }
	    @Path("{id}")
	    @PUT
	    @Produces(MediaType.APPLICATION_JSON)
	    public Result updateDoctor(@PathParam("id")Integer id,Doctor doctor){
	    	if(request.getSession().getAttribute(Const.CURRENT_USER_ID).equals(id) || isAmin()){
	    		try {
		    		Doctor old = doctorService.getDoctorById(doctor.getDoctorId());
		    		//if current user is normal doctor ,then can not update user type
		    		if(Const.TYPE_Doctor.equals(request.getSession().getAttribute(Const.CURRENT_USER_TYPE))){// doctor cann't change the type 
		    			if(!doctor.getType().equals(Const.TYPE_Doctor)){
		    				return new Result(false,"更新失败：没有权限修改医生类型，请联系管理员或厂商。");
		    			}
		    		}
		    		boolean modifyPasswd = true;
		    		if(doctor.getPassword() == null || doctor.getPassword().trim().equals("")){
		    			modifyPasswd  = false;
		    		}
		    		if(old.getType().equals(Const.TYPE_SystemAdmin)){// modified object is system administator
		    			if(((Integer)request.getSession().getAttribute(Const.CURRENT_USER_ID)).equals(doctor.getDoctorId()) ){
		    				logService.addLogRecord(request,"更新医生信息",doctor.toString());
		    				return doctorService.updateDoctor(doctor,modifyPasswd);
		    			}
		    			return new Result(false,"更新失败：不能更新超级用户");
		    		}
		    		logService.addLogRecord(request,"更新医生信息",doctor.toString());
					return doctorService.updateDoctor(doctor,modifyPasswd);
				} catch (DBException e) {
					String msg = "更新医生信息失败，医生名字 : "+doctor.getDoctorName()+":"+e.getMessage();
					logger.error(msg);
					e.printStackTrace();
					return new Result(false,msg);
				}
	    	}else{
	    		return new Result(false,"执行失败：此操作需要主任医生权限");
	    	}
	    }
	    
	    
	    @Path("{id}")
	    @DELETE
	    @Produces(MediaType.APPLICATION_JSON)
	    public Result removeDoctor(@PathParam("id")Integer id) {
	    	if(!isAmin()){
	    		return new Result(false,"删除失败：此操作需要管理权限");
	    	}
	    	try {
	    		if(request.getSession().getAttribute(Const.CURRENT_USER_ID).equals(id)){
	    			return new Result(false,"删除失败：不能够删除自身");
	    		}
	    		if(Const.TYPE_SystemAdmin.equals(doctorService.getDoctorById(id).getType())){// the only root structure
	    			return new Result(false,"删除失败：不能删除系统管理员");
	    		}
	    		logService.addLogRecord(request,"删除医生",id+"");
				return doctorService.removeDoctor(id);
			} catch (DBException e) {
				String msg = "删除失败，医生Id: "+id+":"+e.getMessage();
				logger.error(msg);
				e.printStackTrace();
				return new Result(false,msg);
				
			}
	    }
	  
	    @GET
	    @Produces(MediaType.APPLICATION_JSON)
	    public List<Doctor> getDoctorList() {
	    	try {
	    		Boolean withSystemAmin = false;
	    		if(request.getSession().getAttribute(Const.CURRENT_USER_TYPE).equals(Const.TYPE_SystemAdmin)){
	    			withSystemAmin = true;
	    		}
	    		logService.addLogRecord(request,"获取医生列表",null);
				return doctorService.getDoctorList(withSystemAmin);
			} catch (DBException e) {
				String msg = "获取医生列表失败： "+e.getMessage();
				logger.error(msg);
				e.printStackTrace();
				return null;
			}
	    	
	    }
}

