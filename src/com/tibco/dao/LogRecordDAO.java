package com.tibco.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

import com.shinetech.sql.DatabaseAccessBuilder;
import com.shinetech.sql.FieldParameter;
import com.shinetech.sql.FieldTypes;
import com.shinetech.sql.IDatabaseAccess;
import com.shinetech.sql.ResultSetHandler;
import com.shinetech.sql.exception.DBException;
import com.tibco.bean.LogRecord;
import com.tibco.util.DateUtil;

public class LogRecordDAO {
	private IDatabaseAccess db = (new DatabaseAccessBuilder()).getDatabaseAccess();
	
	/**
	 * add the login info
	 * @param loginRecordBean
	 * @throws DBException
	 */
	public  void addLoginRecord(LogRecord loginRecordBean) throws DBException{
		////recordId  doctorId loginTime 
		String sql="insert into logrecord(doctorId,methodName,methodParam,ip)  values(?,?,?,?)";
		List<FieldParameter> fpList = new ArrayList<FieldParameter>();
		fpList.add(new FieldParameter(1, loginRecordBean.getDoctorId(), FieldTypes.INTEGER));
		fpList.add(new FieldParameter(2, loginRecordBean.getMethodName(), FieldTypes.VARCHAR));
		fpList.add(new FieldParameter(3, loginRecordBean.getMethodParam(), FieldTypes.VARCHAR));
		fpList.add(new FieldParameter(4, loginRecordBean.getIp(), FieldTypes.VARCHAR));
		db.execute(sql, fpList);
	}
	
	/**
	 * delete the log record
	 * @param loginRecordId
	 * @throws DBException
	 */
	public void deleteLoginRecord(Integer loginRecordId) throws DBException{
		String sql = "delete from logRecord where recordId=?";
		List<FieldParameter> fpList = new ArrayList<FieldParameter>();
		fpList.add(new FieldParameter(1,loginRecordId,FieldTypes.INTEGER));
		db.execute(sql, fpList);
	}
	
	
/**
 * 	select the logginRecord by the condition 
 * @param sql
 * @return List<LoginRecordBean>
 * @throws DBException
 */
	public List<LogRecord> advanceLogSerach(String sql) throws DBException{
		final List<LogRecord> logs = new ArrayList<LogRecord>();
		db.handleList(sql, new ResultSetHandler(){

			@Override
			public void handle(ResultSet rs) {
				try {
					Integer sid = rs.getInt("recordId");
					LogRecord log = getLogByID(sid);
					logs.add(log);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		return logs;
	} 
	
	
	private LogRecord getLogByID(Integer loginRecordId) throws DBException{
		String sql = "select * from logRecord where recordIdl=?";
		List<FieldParameter> fpList = new ArrayList<FieldParameter>();
		fpList.add(new FieldParameter(1,loginRecordId,FieldTypes.INTEGER));
		return (LogRecord) db.queryPrepareFirst(LogRecord.class, sql, fpList);
	}
	
	public List<LogRecord> getLogList(JSONObject json)throws DBException{
//		Integer doctorId,String methodName,Date date1,Date date2
		Object doctorId = json.get("doctorId");
		Object methodName = json.get("methodName");
		Object date1 = json.get("date1");
		Object date2 = json.get("date2");
		String sql = "select * from logrecord where 1=1";
		if(doctorId != null  && !doctorId.toString().trim().equals("")){
			sql += " and doctorId = "+(Integer)doctorId; 
		}
		if(methodName != null  && !methodName.toString().trim().equals("")){
			sql += " and methodName = '"+methodName+"'";
		}
		if(date1 != null  && !date1.toString().trim().equals("")){
			sql += " and operateTime >= '"+date1+"'"; 
		}
		
		if(date2 != null  && !date2.toString().trim().equals("")){
			sql += " and operateTime < '"+DateUtil.getNextDay(date2.toString())+"'"; 
		}
	
		sql += " order by recordId DESC"; 
		if(date1 == null && date2 == null){
			sql += " limit 0,100";
		}
		System.out.println(sql);
		return db.queryList(LogRecord.class, sql);
	}

	
}
