package com.tibco.dao;

import java.util.ArrayList;
import java.util.List;

import com.shinetech.sql.DatabaseAccessBuilder;
import com.shinetech.sql.FieldParameter;
import com.shinetech.sql.FieldTypes;
import com.shinetech.sql.IDatabaseAccess;
import com.shinetech.sql.exception.DBException;
import com.tibco.bean.Hospital;

public class HospitalDAO {
	private IDatabaseAccess db = (new DatabaseAccessBuilder()).getDatabaseAccess();
	
	/**
	 * add HospitalBean
	 * @param hospitalBean
	 * @throws DBException
	 */
	public void updateHospital(Hospital hospitalBean) throws DBException{
		String sql="update hospital  set name = ?,  hospitalLogo = ?, machineNumber =? ,handController = ?,firmwareVersion = ?,department = ? where hospitalId = ?";
		if(hospitalBean.getHospitalLogo() == null){
			 sql="update hospital  set name = ?,   machineNumber =? ,handController = ?,firmwareVersion = ?, department =? where hospitalId = ?";
			 List<FieldParameter> fpList = new ArrayList<FieldParameter>();
				fpList.add(new FieldParameter(1, hospitalBean.getName(), FieldTypes.VARCHAR));
				fpList.add(new FieldParameter(2, hospitalBean.getMachineNumber(), FieldTypes.VARCHAR));
				fpList.add(new FieldParameter(3, hospitalBean.getHandController(), FieldTypes.VARCHAR));
				fpList.add(new FieldParameter(4, hospitalBean.getFirmwareVersion(), FieldTypes.VARCHAR));
				fpList.add(new FieldParameter(5, hospitalBean.getDepartment(), FieldTypes.VARCHAR));
				//fpList.add(new FieldParameter(6, hospitalBean.getPrescribingDoctorName(), FieldTypes.VARCHAR));
				fpList.add(new FieldParameter(6, hospitalBean.getHospitalId(), FieldTypes.INTEGER));
				
				db.execute(sql, fpList);
				return;
		} 
		List<FieldParameter> fpList = new ArrayList<FieldParameter>();
		fpList.add(new FieldParameter(1, hospitalBean.getName(), FieldTypes.VARCHAR));
		fpList.add(new FieldParameter(2, hospitalBean.getHospitalLogo(), FieldTypes.VARCHAR));
		fpList.add(new FieldParameter(3, hospitalBean.getMachineNumber(), FieldTypes.VARCHAR));
		fpList.add(new FieldParameter(4, hospitalBean.getHandController(), FieldTypes.VARCHAR));
		fpList.add(new FieldParameter(5, hospitalBean.getFirmwareVersion(), FieldTypes.VARCHAR));
		fpList.add(new FieldParameter(6, hospitalBean.getDepartment(), FieldTypes.VARCHAR));
//		fpList.add(new FieldParameter(7, hospitalBean.getPrescribingDoctorName(), FieldTypes.VARCHAR));
		fpList.add(new FieldParameter(7, hospitalBean.getHospitalId(), FieldTypes.INTEGER));
		db.execute(sql, fpList);
	}
	
	public Integer addHospital(Hospital hospitalBean) throws DBException{
		String getNextAutoIdSql = "select auto_increment from information_schema.`TABLES` where table_name='hospital'";
		Integer hospitalId =  (Integer)db.queryFirst(Integer.class, getNextAutoIdSql);
		String sql="insert into hospital  (name,hospitalLogo,machineNumber,handController,firmwareVersion,hospitalId,department)  values(?,?,?,?,?,?,?)";
		List<FieldParameter> fpList = new ArrayList<FieldParameter>();
		fpList.add(new FieldParameter(1, hospitalBean.getName(), FieldTypes.VARCHAR));
		fpList.add(new FieldParameter(2, hospitalBean.getHospitalLogo(), FieldTypes.VARCHAR));
		fpList.add(new FieldParameter(3, hospitalBean.getMachineNumber(), FieldTypes.VARCHAR));
		fpList.add(new FieldParameter(4, hospitalBean.getHandController(), FieldTypes.VARCHAR));
		fpList.add(new FieldParameter(5, hospitalBean.getFirmwareVersion(), FieldTypes.VARCHAR));
		fpList.add(new FieldParameter(6, hospitalId, FieldTypes.INTEGER));
		fpList.add(new FieldParameter(7, hospitalBean.getDepartment(), FieldTypes.VARCHAR));
//		fpList.add(new FieldParameter(7, hospitalBean.getPrescribingDoctorName(), FieldTypes.VARCHAR));
		db.execute(sql, fpList);
		return hospitalId;
	}
	public Hospital getHospital() throws DBException{
		String sql = "select  * from hospital limit 0,1";
		return (Hospital)db.queryFirst(Hospital.class, sql);
	}
	
	/**
	 * get the hospitalList.size
	 * @return
	 * @throws DBException
	 */
	public int selectHospitalListSize() throws DBException{
		String sql="select COUNT(*) from  loginrecord";
		List<FieldParameter> fpList = new ArrayList<FieldParameter>();
		//there is no parameter, the fpList.size=0
		Integer count = (Integer)db.queryPrepareFirst(Integer.class, sql, fpList);
		return count;
	}
	
	public void deleteLogo(Integer hospitalId) throws DBException{
		String sql = "update  hospital set hospitalLogo = null  where hospitalId=?";
		List<FieldParameter> fpList = new ArrayList<FieldParameter>();
		fpList.add(new FieldParameter(1,hospitalId,FieldTypes.INTEGER));
		db.execute(sql, fpList);
	}

	
}
