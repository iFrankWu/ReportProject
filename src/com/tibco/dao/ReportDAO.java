/*
 * ReportDAO.java
 * Jun 24, 2013
 * com.tibco.dao
 * ReportProject
 * Copyright (C), 2013, TIBCO Software Inc.
 * 
 */
package com.tibco.dao;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

import com.shinetech.sql.DatabaseAccessBuilder;
import com.shinetech.sql.FieldParameter;
import com.shinetech.sql.FieldTypes;
import com.shinetech.sql.IDatabaseAccess;
import com.shinetech.sql.ResultSetHandler;
import com.shinetech.sql.exception.DBException;
import com.tibco.bean.Report;
import com.tibco.util.DateUtil;

/**
 * class description goes here.
 *
 * @author <a href="mailto:swu@tibco-support.com">Frank Wu</a>
 * @version 1.0.0
 */
public class ReportDAO {
	@SuppressWarnings("rawtypes")
	private IDatabaseAccess db = (new DatabaseAccessBuilder()).getDatabaseAccess();

	public void addDoctor(Report report) throws DBException {
		String sql = "insert into report(patientName,age,caseNumber,lastTimeMenstruation,pregnancyNumber,"
				+ "childbirthNumber,isMenopause,isLeucorrhea,isBleed,unregularBleed," + "otherComplaints,isSmooth,isAcuteInflammation,isHypertrophy,isPolyp,"
				+ "erosion,isTear,isNesslersGlandCyst,isWhite,isCancer," + "otherClinical,pointNumber,isComplete,checkResult,screening,"
				+ "checking,otherSuggestion,checkDate,isDelete,address,doctorName,doctorId,department,reason4doesNotComplete,phone,prescribingDoctorName,lct,hpv,touchbleeding,checkHpv)"
				+
				// add patientAddress after idDelete
				" values(?,?,?,?,?,   ?,?,?,?,?,   ?,?,?,?,?,  ?,?,?,?,?,  ?,?,?,?,?,  ?,?,now(),false,?,?,?,?,?,?,?,?,?,?,?)";
		// patientName,age,caseNumber,lastTimeMenstruation,pregnancyNumber,
		List<FieldParameter> fpList = new ArrayList<FieldParameter>();
		fpList.add(new FieldParameter(1, report.getPatientName(), FieldTypes.VARCHAR));
		fpList.add(new FieldParameter(2, report.getAge(), FieldTypes.INTEGER));
		fpList.add(new FieldParameter(3, report.getCaseNumber(), FieldTypes.VARCHAR));
		fpList.add(new FieldParameter(4, report.getLastTimeMenstruation(), FieldTypes.DATE));
		fpList.add(new FieldParameter(5, report.getPregnancyNumber(), FieldTypes.INTEGER));

		// "childbirthNumber,isMenopause,isLeucorrhea,isBleed,unregularBleed," +
		fpList.add(new FieldParameter(6, report.getChildbirthNumber(), FieldTypes.INTEGER));
		fpList.add(new FieldParameter(7, initBoolean(report.getIsMenopause()), FieldTypes.BOOLEAN));
		fpList.add(new FieldParameter(8, initBoolean(report.getIsLeucorrhea()), FieldTypes.BOOLEAN));
		fpList.add(new FieldParameter(9, initBoolean(report.getIsBleed()), FieldTypes.BOOLEAN));
		fpList.add(new FieldParameter(10, report.getUnregularBleed(), FieldTypes.VARCHAR));

		// "otherComplaints,isSmooth,isAcuteInflammation,isHypertrophy,isPolyp,"
		// +
		fpList.add(new FieldParameter(11, report.getOtherComplaints(), FieldTypes.VARCHAR));
		fpList.add(new FieldParameter(12, initBoolean(report.getIsSmooth()), FieldTypes.BOOLEAN));
		fpList.add(new FieldParameter(13, initBoolean(report.getIsAcuteInflammation()), FieldTypes.BOOLEAN));
		fpList.add(new FieldParameter(14, initBoolean(report.getIsHypertrophy()), FieldTypes.BOOLEAN));
		fpList.add(new FieldParameter(15, initBoolean(report.getIsPolyp()), FieldTypes.BOOLEAN));

		// "erosion,isTear,isNesslersGlandCyst,isWhite,isCancer," +
		fpList.add(new FieldParameter(16, report.getErosion(), FieldTypes.VARCHAR));
		fpList.add(new FieldParameter(17, initBoolean(report.getIsTear()), FieldTypes.BOOLEAN));
		fpList.add(new FieldParameter(18, initBoolean(report.getIsNesslersGlandCyst()), FieldTypes.BOOLEAN));
		fpList.add(new FieldParameter(19, initBoolean(report.getIsWhite()), FieldTypes.BOOLEAN));
		fpList.add(new FieldParameter(20, initBoolean(report.getIsCancer()), FieldTypes.BOOLEAN));

		// "ohterClinical,pointNumber,isComplete,checkResult,screening," +
		fpList.add(new FieldParameter(21, report.getOtherClinical(), FieldTypes.VARCHAR));
		fpList.add(new FieldParameter(22, report.getPointNumber(), FieldTypes.INTEGER));
		fpList.add(new FieldParameter(23, report.getIsComplete(), FieldTypes.VARCHAR));
		fpList.add(new FieldParameter(24, report.getCheckResult(), FieldTypes.VARCHAR));
		fpList.add(new FieldParameter(25, initBoolean(report.getScreening()), FieldTypes.BOOLEAN));

		// "checking,otherSuggestion,checkDate,doctorName,doctorId)" +
		fpList.add(new FieldParameter(26, initBoolean(report.getChecking()), FieldTypes.BOOLEAN));
		fpList.add(new FieldParameter(27, report.getOtherSuggestion(), FieldTypes.VARCHAR));
		// fpList.add(new
		// FieldParameter(27,report.getIsDelete(),FieldTypes.BOOLEAN));
		// add in 20130720 patientAddress
		fpList.add(new FieldParameter(28, report.getAddress(), FieldTypes.VARCHAR));
		fpList.add(new FieldParameter(29, report.getDoctorName(), FieldTypes.VARCHAR));
		fpList.add(new FieldParameter(30, report.getDoctorId(), FieldTypes.INTEGER));
		fpList.add(new FieldParameter(31, report.getDepartment(), FieldTypes.VARCHAR));
		fpList.add(new FieldParameter(32, report.getReason4doesNotComplete(), FieldTypes.VARCHAR));
		fpList.add(new FieldParameter(33, report.getPhone(), FieldTypes.VARCHAR));
		fpList.add(new FieldParameter(34, report.getPrescribingDoctorName(), FieldTypes.VARCHAR));

		fpList.add(new FieldParameter(35, report.getLct(), FieldTypes.VARCHAR));
		fpList.add(new FieldParameter(36, report.getHpv(), FieldTypes.VARCHAR));
		fpList.add(new FieldParameter(37, report.getTouchbleeding(), FieldTypes.VARCHAR));
		fpList.add(new FieldParameter(38, report.getCheckHpv(), FieldTypes.BOOLEAN));
		db.execute(sql, fpList);
	}

	private Boolean initBoolean(Boolean flag) {
		return flag == null ? false : flag;
	}

	/**
	 * 以当前最小reportId起 ，向后得第page页的数据
	 * 
	 * @param minId
	 *            最小Id值
	 * @param size
	 *            每页大小
	 * @param page
	 *            第几页 与当前相差，比如当前在第三页，那么查找第四页 传入page值为1 就可以
	 * @param sortColumn
	 * @return
	 * @throws DBException
	 *             List<Report>
	 */
	public List<Report> getNextPage(Integer minId, Integer size, Integer page, String sortColumn) throws DBException {
		String sql = "select * from report where reportId < ? and isDelete=false order by ? DESC limit ?,?";
		List<FieldParameter> fpList = new ArrayList<FieldParameter>();
		fpList.add(new FieldParameter(1, minId, FieldTypes.INTEGER));
		if (sortColumn == null) {
			sortColumn = "reportId";
		}
		fpList.add(new FieldParameter(2, sortColumn, FieldTypes.VARCHAR));
		fpList.add(new FieldParameter(3, size * (page - 1), FieldTypes.INTEGER));
		fpList.add(new FieldParameter(4, size, FieldTypes.INTEGER));
		return (List<Report>) db.queryPrepareList(Report.class, sql, fpList);
	}

	public List<Report> getPrePage(Integer maxId, Integer size, Integer page, String sortColumn) throws DBException {
		String sql = "select * from (select * from report where reportId >  ? and isDelete=false order by ? ASC limit ?,?) as a order by ? DESC";
		if (sortColumn == null) {
			sortColumn = "reportId";
		}
		List<FieldParameter> fpList = new ArrayList<FieldParameter>();
		fpList.add(new FieldParameter(1, maxId, FieldTypes.INTEGER));
		fpList.add(new FieldParameter(2, sortColumn, FieldTypes.VARCHAR));
		fpList.add(new FieldParameter(3, size * (page - 1), FieldTypes.INTEGER));
		fpList.add(new FieldParameter(4, size, FieldTypes.INTEGER));
		fpList.add(new FieldParameter(5, sortColumn, FieldTypes.VARCHAR));
		return (List<Report>) db.queryPrepareList(Report.class, sql, fpList);
	}

	/**
	 * the max reportID
	 */
	public List<Report> getCurrentPage(Integer maxId, Integer size, Integer page, String sortColumn) throws DBException {
		String sql = "select * from report where reportId <= ? and isDelete=false order by ? DESC limit 0,?";
		if (sortColumn == null) {
			sortColumn = "reportId";
		}
		List<FieldParameter> fpList = new ArrayList<FieldParameter>();
		fpList.add(new FieldParameter(1, maxId, FieldTypes.INTEGER));
		fpList.add(new FieldParameter(2, sortColumn, FieldTypes.VARCHAR));
		fpList.add(new FieldParameter(3, size, FieldTypes.INTEGER));
		return (List<Report>) db.queryPrepareList(Report.class, sql, fpList);
	}

	/**
	 * the max reportID
	 */
	public List<Report> getTopPage(Integer size, String sortColumn) throws DBException {
		String sql = "select * from report where isDelete=false order by ? desc limit 0,?";
		if (sortColumn == null) {
			sortColumn = "reportId";
		}
		List<FieldParameter> fpList = new ArrayList<FieldParameter>();
		fpList.add(new FieldParameter(1, sortColumn, FieldTypes.VARCHAR));
		fpList.add(new FieldParameter(2, size, FieldTypes.INTEGER));
		return (List<Report>) db.queryPrepareList(Report.class, sql, fpList);
	}

	public Integer getAllRecordNumber() throws DBException {
		String sql = "select count(*) from report";
		return (Integer) db.queryFirst(Integer.class, sql);
	}

	public Report getReportByID(Report report) throws DBException {
		Report returnBean;
		String sql = "select * from report where reportId=?";
		List<FieldParameter> fplist = new ArrayList<FieldParameter>();
		fplist.add(new FieldParameter(1, report.getReportId(), FieldTypes.INTEGER));
		returnBean = (Report) db.queryPrepareFirst(Report.class, sql, fplist);
		return returnBean;
	}

	public void updateReport(Report report) throws DBException {
		String sql = "update report  set patientName=?,age=?,caseNumber=?,lastTimeMenstruation=?,pregnancyNumber=?,"
				+ "childbirthNumber=?,isMenopause=?,isLeucorrhea=?,isBleed=?,unregularBleed=?,"
				+ "otherComplaints=?,isSmooth=?,isAcuteInflammation=?,isHypertrophy=?,isPolyp=?,"
				+ "erosion=?,isTear=?,isNesslersGlandCyst=?,isWhite=?,isCancer=?," + "otherClinical=?,pointNumber=?,isComplete=?,checkResult=?,screening=?,"
				+ "checking=?,otherSuggestion=?,doctorName=?,doctorId=?,modifyDate=now(),department=?,"
				+ "address=?,reason4doesNotComplete=?,phone=?,prescribingDoctorName=?,lct =? ," + "hpv =?,touchbleeding = ? , checkHpv = ? where reportId=?";
		// 1.patientName=?,age=?,caseNumber=?,lastTimeMenstruation=?,pregnancyNumber=?,"
		// +
		List<FieldParameter> fplist = new ArrayList<FieldParameter>();
		fplist.add(new FieldParameter(1, report.getPatientName(), FieldTypes.VARCHAR));
		fplist.add(new FieldParameter(2, report.getAge(), FieldTypes.INTEGER));
		fplist.add(new FieldParameter(3, report.getCaseNumber(), FieldTypes.VARCHAR));
		fplist.add(new FieldParameter(4, report.getLastTimeMenstruation(), FieldTypes.DATE));
		fplist.add(new FieldParameter(5, report.getPregnancyNumber(), FieldTypes.INTEGER));

		// 2
		// childbirthNumber=?,isMenopause=?,isLeucorrhea=?,isBleed=?,unregularBleed=?,"
		// +
		fplist.add(new FieldParameter(6, report.getChildbirthNumber(), FieldTypes.INTEGER));
		fplist.add(new FieldParameter(7, initBoolean(report.getIsMenopause()), FieldTypes.BOOLEAN));
		fplist.add(new FieldParameter(8, initBoolean(report.getIsLeucorrhea()), FieldTypes.BOOLEAN));
		fplist.add(new FieldParameter(9, initBoolean(report.getIsBleed()), FieldTypes.BOOLEAN));
		fplist.add(new FieldParameter(10, report.getUnregularBleed(), FieldTypes.VARCHAR));

		// 3
		// "otherComplaints=?,isSmooth=?,isAcuteInflammation=?,isHypertrophy=?,isPolyp=?,"
		// +
		fplist.add(new FieldParameter(11, report.getOtherComplaints(), FieldTypes.VARCHAR));
		fplist.add(new FieldParameter(12, initBoolean(report.getIsSmooth()), FieldTypes.BOOLEAN));
		fplist.add(new FieldParameter(13, initBoolean(report.getIsAcuteInflammation()), FieldTypes.BOOLEAN));
		fplist.add(new FieldParameter(14, initBoolean(report.getIsHypertrophy()), FieldTypes.BOOLEAN));
		fplist.add(new FieldParameter(15, initBoolean(report.getIsPolyp()), FieldTypes.BOOLEAN));

		// 4 "erosion=?,isTear=?,isNesslersGlandCyst=?,isWhite=?,isCancer=?," +
		fplist.add(new FieldParameter(16, report.getErosion(), FieldTypes.VARCHAR));
		fplist.add(new FieldParameter(17, initBoolean(report.getIsTear()), FieldTypes.BOOLEAN));
		fplist.add(new FieldParameter(18, initBoolean(report.getIsNesslersGlandCyst()), FieldTypes.BOOLEAN));
		fplist.add(new FieldParameter(19, initBoolean(report.getIsWhite()), FieldTypes.BOOLEAN));
		fplist.add(new FieldParameter(20, initBoolean(report.getIsCancer()), FieldTypes.BOOLEAN));

		// 5
		// "otherClinical=?,pointNumber=?,isComplete=?,checkResult=?,screening=?,"
		// +
		fplist.add(new FieldParameter(21, report.getOtherClinical(), FieldTypes.VARCHAR));
		fplist.add(new FieldParameter(22, report.getPointNumber(), FieldTypes.INTEGER));
		fplist.add(new FieldParameter(23, report.getIsComplete(), FieldTypes.VARCHAR));
		fplist.add(new FieldParameter(24, report.getCheckResult(), FieldTypes.VARCHAR));
		fplist.add(new FieldParameter(25, initBoolean(report.getScreening()), FieldTypes.BOOLEAN));

		// 6
		// "checking=?,otherSuggestion=?,doctorName=?,doctorId=?,modifyDate=now()"
		// +
		fplist.add(new FieldParameter(26, initBoolean(report.getChecking()), FieldTypes.BOOLEAN));
		fplist.add(new FieldParameter(27, report.getOtherSuggestion(), FieldTypes.VARCHAR));
		fplist.add(new FieldParameter(28, report.getDoctorName(), FieldTypes.VARCHAR));
		fplist.add(new FieldParameter(29, report.getDoctorId(), FieldTypes.INTEGER));
		fplist.add(new FieldParameter(30, report.getDepartment(), FieldTypes.VARCHAR));
		// add patientAddress in 20130720
		fplist.add(new FieldParameter(31, report.getAddress(), FieldTypes.VARCHAR));
		fplist.add(new FieldParameter(32, report.getReason4doesNotComplete(), FieldTypes.VARCHAR));
		// 7 " where reportId=?";

		fplist.add(new FieldParameter(33, report.getPhone(), FieldTypes.VARCHAR));
		fplist.add(new FieldParameter(34, report.getPrescribingDoctorName(), FieldTypes.VARCHAR));

		fplist.add(new FieldParameter(35, report.getLct(), FieldTypes.VARCHAR));
		fplist.add(new FieldParameter(36, report.getHpv(), FieldTypes.VARCHAR));
		fplist.add(new FieldParameter(37, report.getTouchbleeding(), FieldTypes.VARCHAR));
		fplist.add(new FieldParameter(38, report.getCheckHpv(), FieldTypes.BOOLEAN));
		fplist.add(new FieldParameter(39, report.getReportId(), FieldTypes.INTEGER));
		db.execute(sql, fplist);
	}

	/**
	 * delete report ,just update the report's status, set isDelete=true;
	 * 
	 * @param id
	 * @throws DBException
	 */
	public void deleteReport(Integer id) throws DBException {
		String sql = "update report set isDelete=true where reportId=?";
		List<FieldParameter> fpList = new ArrayList<FieldParameter>();
		fpList.add(new FieldParameter(1, id, FieldTypes.INTEGER));
		db.execute(sql, fpList);
	}

	public List<Report> advanceSerach(String sql) throws DBException {
		System.out.println(sql);
		return (List<Report>) db.queryList(Report.class, sql);
		// final List<Report> reports = new ArrayList<Report>();
		// db.handleList(sql, new ResultSetHandler(){
		//
		// @Override
		// public void handle(ResultSet rs) {
		// try {
		// Integer sid = rs.getInt("reportId");
		// Report report = getReportByID(sid);
		// reports.add(report);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }
		// });
		// return reports;
	}

	/**
	 * @param reportId
	 * @return
	 * @throws DBException
	 */
	public Report getReportByID(Integer reportId) throws DBException {
		String sql = "select * from report  where reportId=?";
		List<FieldParameter> fpList = new ArrayList<FieldParameter>();
		fpList.add(new FieldParameter(1, reportId, FieldTypes.INTEGER));
		return (Report) db.queryPrepareFirst(Report.class, sql, fpList);
	}

	// 报表统计
	/**
	 * getNormalReport,isDelete==false
	 * 
	 * @return
	 * @throws DBException
	 */
	public void statisticCheckResult(JSONObject data, ResultSetHandler handler) throws DBException {

		// select * from (select count(*) as normal from report where isDelete =
		// false and checkResult='正常') as a, (select count(*) as exception from
		// report where isDelete = false and checkResult='异常') as b

		String normalSql = "select COUNT(*) as normal from  report   where isDelete=false and checkResult='正常' and isComplete='完成' ";
		String exceptionSql = "select COUNT(*) as exception from  report   where isDelete=false and checkResult='异常' and isComplete='完成' ";
		String condition = "";
		if (data != null) {
			Object date3 = data.get("date3");
			Object date4 = data.get("date4");
			Object doctorName = data.get("doctorName");
			if (doctorName != null && !doctorName.toString().trim().equals("")) {
				condition += " and doctorName = '" + doctorName + "'";
			}

			if (date3 != null && !date3.toString().trim().equals("")) {
				condition += " and checkDate >= '" + date3 + "'";
			}

			if (date4 != null && !date4.toString().trim().equals("")) {
				condition += " and checkDate < '" + DateUtil.getNextDay(date4.toString()) + "'";
			}
		}
		normalSql += condition;
		exceptionSql += condition;
		String sql = "select normal,exception from (" + normalSql + ") as a,(" + exceptionSql + ") as b";
		// System.out.println(sql);
		db.handleList(sql, handler);
	}

	public void exportData(String startDate, String endDate, ResultSetHandler handler) throws DBException {
		String sql = "select * from report  where 1=1 and isDelete = false";
		if (startDate != null) {
			sql += " and checkDate >= '" + startDate + "'";
		}

		if (endDate != null) {
			sql += " and checkDate < '" + DateUtil.getNextDay(endDate) + "'";
		}
		db.handleList(sql, handler);
	}

	public List<Report> getReportsByCaseNumber(String caseNumber) throws DBException {
		String sql = "select * from report where caseNumber = ? ";
		List<FieldParameter> fpList = new ArrayList<FieldParameter>();
		fpList.add(new FieldParameter(1, caseNumber, FieldTypes.VARCHAR));
		return db.queryPrepareList(Report.class, sql, fpList);
	}
}
