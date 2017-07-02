/*
 * DoctorService.java
 * Apr 13, 2013
 * com.tibco.service
 * AngularJS
 * Copyright (C), 2013, TIBCO Software Inc.
 * 
 */
package com.tibco.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.shinetech.sql.exception.DBException;
import com.tibco.bean.Doctor;
import com.tibco.bean.Result;
import com.tibco.dao.DoctorDAO;
import com.tibco.util.Const;

/**
 * class description goes here.
 *
 * @author <a href="mailto:swu@tibco-support.com">Frank Wu</a>
 * @version 1.0.0
 */
public class DoctorService {
	private DoctorDAO doctorDAO = new DoctorDAO();
	public Result Login(Doctor doctor,HttpSession session) throws DBException{
		if(doctorDAO.existDoctorName(doctor.getDoctorName())){
			if(doctor.getPassword()!= null){
				Doctor u = doctorDAO.getDoctorByNameAndPasswd(doctor.getDoctorName(), doctor.getPassword());
				if(u != null){
					if(Const.STATUS_USEABLE.equals(u.getStatus())){
						session.setAttribute(Const.LOGIN_SUCCESS, true);
						session.setAttribute(Const.CURRENT_USER_TYPE, u.getType());
						session.setAttribute(Const.CURRENT_USER_ID, u.getDoctorId());
						session.setAttribute(Const.CURRENT_USER_NAME, u.getDoctorName());
						return new Result(true,u);
					}
					return new Result(false,"该医生当前状态为禁用状态，不可以登录，如有请联系管理员。");
				}
				return new Result(false,"密码错误。");
			}
	    	return new Result(false,"密码不能为空");
    	}
		return new Result(false,"该医生不存在，医生名：" +doctor.getDoctorName());
	}
	
	public List<Doctor> getDoctorList(Boolean withSystemAmin) throws DBException{
		return doctorDAO.getDoctorList(withSystemAmin);
	}
	public List<Doctor> getDoctorListByType(String type) throws DBException{
		return doctorDAO.getDoctorListByType(type);
	}
	
	public Result addDoctor(Doctor doctor) throws DBException{
		if(doctorDAO.existDoctorName(doctor.getDoctorName())){
			return new Result(false,"医生名:"+doctor.getDoctorName()+" 已存在。");
		}
		doctor.setModifyDate(new Date());
		doctorDAO.addDoctor(doctor);
		return new Result(true,"");
	}
	public Result removeDoctor(Integer id) throws DBException{
		doctorDAO.deleteDoctor(id);
		return new Result(true,"");
	}
	public Result updateDoctor(Doctor doctor,boolean  modifyPasswd) throws DBException{
		Integer count  = doctorDAO.doesExistOtherDocNamedThisDocName(doctor);
		if(count >0 ){
			return new Result(false,"医生名:"+doctor.getDoctorName()+" 已存在。");
		}
		doctorDAO.updateDoctor(doctor,modifyPasswd);
		return new Result(true,"");
	}
	
	public Doctor getDoctorById(Integer id) throws DBException{
		return doctorDAO.getDoctor(id);
	}
	public Doctor getDoctorByName(String doctorName) throws DBException{
		return doctorDAO.getDoctorByName(doctorName);
	}
}

