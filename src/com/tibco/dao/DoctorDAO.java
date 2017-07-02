/*
 * DoctorDAO.java
 * Jun 8, 2013
 * com.tibco.dao
 * Report
 * Copyright (C), 2013, TIBCO Software Inc.
 * 
 */
package com.tibco.dao;

import java.util.ArrayList;
import java.util.List;

import com.shinetech.sql.DatabaseAccessBuilder;
import com.shinetech.sql.FieldParameter;
import com.shinetech.sql.FieldTypes;
import com.shinetech.sql.IDatabaseAccess;
import com.shinetech.sql.exception.DBException;
import com.tibco.bean.Doctor;

/**
 * class description goes here.
 *
 * @author <a href="mailto:swu@tibco-support.com">Frank Wu</a>
 * @version 1.0.0
 */
public class DoctorDAO {

	@SuppressWarnings("rawtypes")
	private IDatabaseAccess db = (new DatabaseAccessBuilder()).getDatabaseAccess();
	
	public  void addDoctor(Doctor doctor) throws DBException{
		String sql = "insert into doctors(doctorName,password,type,status,createDate) values(?,password(?),?,?,now())";
		List<FieldParameter> fpList = new ArrayList<FieldParameter>();
		fpList.add(new FieldParameter(1,doctor.getDoctorName(),FieldTypes.VARCHAR));
		fpList.add(new FieldParameter(2,doctor.getPassword(),FieldTypes.VARCHAR));
		fpList.add(new FieldParameter(3,doctor.getType(),FieldTypes.VARCHAR));
		fpList.add(new FieldParameter(4,doctor.getStatus(),FieldTypes.VARCHAR));
//		fpList.add(new FieldParameter(5,doctor.getPrescribingDoctorName(),FieldTypes.VARCHAR));
		db.execute(sql, fpList);
	}
	/**
	 * 是否返回系统管理员
	 * */
	public List<Doctor> getDoctorList(Boolean withSystemAmin) throws DBException{
		String sql = "select doctorId,doctorName,type,status,createDate,modifyDate from doctors where isDelete = false and type!='系统管理员'";
		if(withSystemAmin){
			 sql = "select doctorId,doctorName,type,status,createDate,modifyDate from doctors where isDelete = false";
		}
		return db.queryList(Doctor.class, sql);
	}
	public void deleteDoctor(Integer id) throws DBException{
		String sql = "update doctors set isDelete = true  where doctorId = ?";
		List<FieldParameter> fpList = new ArrayList<FieldParameter>();
		fpList.add(new FieldParameter(1,id,FieldTypes.INTEGER));
		db.execute(sql, fpList);
	}
	
	public Boolean existDoctorName(String doctorName)throws DBException{
		String sql = "select count(*) from doctors where doctorName = ? and isDelete = false ";
		List<FieldParameter> fpList = new ArrayList<FieldParameter>();
		fpList.add(new FieldParameter(1,doctorName,FieldTypes.VARCHAR));
		Integer i = (Integer)db.queryPrepareFirst(Integer.class, sql, fpList);
		if(i > 0){
			return true;
		}
	    return false;
	}
	public Doctor getDoctorByNameAndPasswd(String doctorName,String password) throws DBException{
		String sql = "select * from doctors where doctorName = ? and password=password(?) and isDelete = false and type != '申请医生'";
		List<FieldParameter> fpList = new ArrayList<FieldParameter>();
		fpList.add(new FieldParameter(1,doctorName,FieldTypes.VARCHAR));
		fpList.add(new FieldParameter(2,password,FieldTypes.VARCHAR));
		Object o = db.queryPrepareFirst(Doctor.class, sql, fpList);
		if(o == null)return null;
		return (Doctor)o;
	}
	public Doctor getDoctor(Integer id) throws DBException{
		String sql = "select * from doctors where doctorId = ?";
		List<FieldParameter> fpList = new ArrayList<FieldParameter>();
		fpList.add(new FieldParameter(1,id,FieldTypes.INTEGER));
		return (Doctor)db.queryPrepareFirst(Doctor.class, sql, fpList);
	}
	public Doctor getDoctorByName(String doctorName) throws DBException{
		String sql = "select * from doctors where doctorName = ?";
		List<FieldParameter> fpList = new ArrayList<FieldParameter>();
		fpList.add(new FieldParameter(1,doctorName,FieldTypes.VARCHAR));
		return (Doctor)db.queryPrepareFirst(Doctor.class, sql, fpList);
	}
	public void updateDoctor(Doctor doctor,boolean  modifyPasswd) throws DBException{
		String sql = "update doctors set doctorName=?,type=?,status=?,modifyDate=now(),password=password(?)   where doctorId =?";
		if(!modifyPasswd)
			sql = "update doctors set doctorName=?,type=?,status=?,modifyDate=now()where doctorId =?";
		List<FieldParameter> fpList = new ArrayList<FieldParameter>();
		fpList.add(new FieldParameter(1,doctor.getDoctorName(),FieldTypes.VARCHAR));
		fpList.add(new FieldParameter(2,doctor.getType(),FieldTypes.VARCHAR));
		fpList.add(new FieldParameter(3,doctor.getStatus(),FieldTypes.VARCHAR));
		if(modifyPasswd){
			fpList.add(new FieldParameter(4,doctor.getPassword(),FieldTypes.VARCHAR));
//			fpList.add(new FieldParameter(5,doctor.getPrescribingDoctorName(),FieldTypes.VARCHAR));
			fpList.add(new FieldParameter(5,doctor.getDoctorId(),FieldTypes.INTEGER));
		}else{
//			fpList.add(new FieldParameter(4,doctor.getPrescribingDoctorName(),FieldTypes.VARCHAR));
			fpList.add(new FieldParameter(4,doctor.getDoctorId(),FieldTypes.INTEGER));
		}
		db.execute(sql, fpList);
	}
	public Integer doesExistOtherDocNamedThisDocName(Doctor doctor) throws DBException{
		String sql = "select count(*) from doctors where doctorName = ? and isDelete = false and doctorId != ?";
		List<FieldParameter> fpList = new ArrayList<FieldParameter>();
		fpList.add(new FieldParameter(1,doctor.getDoctorName(),FieldTypes.VARCHAR));
		fpList.add(new FieldParameter(2,doctor.getDoctorId(),FieldTypes.INTEGER));
		return (Integer)db.queryPrepareFirst(Integer.class, sql, fpList);
	}
	/**
	 * 
	 * */
	public List<Doctor> getDoctorListByType(String type) throws DBException{
		String sql = "select doctorId,doctorName,type,status,createDate,modifyDate from doctors where isDelete = false and type =?";
		List<FieldParameter> fpList = new ArrayList<FieldParameter>();
		fpList.add(new FieldParameter(1,type,FieldTypes.VARCHAR));
		return db.queryPrepareList(Doctor.class, sql, fpList);
	}

}

