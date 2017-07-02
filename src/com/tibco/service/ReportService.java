/*
 * ReportService.java
 * Jun 24, 2013
 * com.tibco.service
 * ReportProject
 * Copyright (C), 2013, TIBCO Software Inc.
 * 
 */
package com.tibco.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;
import org.json.simple.JSONObject;

import com.shinetech.sql.ResultSetHandler;
import com.shinetech.sql.exception.DBException;
import com.tibco.bean.Report;
import com.tibco.bean.Result;
import com.tibco.bean.Search;
import com.tibco.dao.ReportDAO;
import com.tibco.handle.ExportReportHandler;
import com.tibco.util.DateUtil;
import com.tibco.util.XLSExport;

/**
 * class description goes here.
 *
 * @author <a href="mailto:swu@tibco-support.com">Frank Wu</a>
 * @version 1.0.0
 */
public class ReportService {
	private ReportDAO reportDAO = new ReportDAO();
	public Result addReport(Report report) throws DBException{
		reportDAO.addDoctor(report);
		return new Result(true,"");
	}
	
	public Result updateReport(Report report) throws DBException{
		reportDAO.updateReport(report);
		return new Result(true,"");
	}
	
	public Report getReportByID(Integer reportId) throws DBException{
		return reportDAO.getReportByID(reportId);
	}
	
	public void deleteReportByID(Integer id) throws DBException{
		reportDAO.deleteReport(id);
	}
	
	public Map<String,Object> getNextPage(Integer minId,Integer size, Integer page, String sortColumn) throws DBException{
		List<Report> list = reportDAO.getNextPage(minId, size, page, sortColumn);
		Map map = new HashMap<String,Object>();
		
		map.put("reportList", list);
		map.put("allRecordNumber", reportDAO.getAllRecordNumber());
		if(list != null && !list.isEmpty()){
			map.put("maxId", list.get(0).getReportId());
			map.put("minId",list.get(list.size()-1).getReportId());
		}
		return map;
	}
	
	public Map<String,Object> getPrePage(Integer maxId,Integer size, Integer page, String sortColumn) throws DBException{
		List<Report> list = reportDAO.getPrePage(maxId, size, page, sortColumn);
		Map map = new HashMap<String,Object>();
		map.put("reportList", list);
		map.put("allRecordNumber", reportDAO.getAllRecordNumber());
		if(list != null && !list.isEmpty()){
			map.put("maxId", list.get(0).getReportId());
			map.put("minId",list.get(list.size()-1).getReportId());
		}
		return map;
	}
	
	public List<Report> advanceSerach(Search search)throws DBException{
		StringBuffer sql = new StringBuffer("select *  from report where 1=1 "); 
		
		if(search.getReportId() != null){
			sql.append(" and reportId = ");
			sql.append(search.getReportId());
		}
		
		if(search.getReason4doesNotComplete() != null){
			sql.append(" and reason4doesNotComplete like '%");
			sql.append(search.getReason4doesNotComplete());
			sql.append("%'");
		}
	/*	private Integer reportId;*/
		
		/*Append sql start*/
		////**资料*/
	//	private String patientName;
		if(search.getPatientName()!=null){
			sql.append(" and patientName like '%");
			sql.append(search.getPatientName());
			sql.append("%'");
		}
	//	private Integer age;
		if(search.getAge()!=null&&search.getAge2()!=null&&search.getAge()<search.getAge2()){
			sql.append(" and age between ");
			sql.append(search.getAge());
			sql.append(" and ");
			sql.append(search.getAge2());
		}
		//private String caseNumber;
		if(search.getCaseNumber()!=null){
			sql.append(" and caseNumber like '%");
			sql.append(search.getCaseNumber());
			sql.append("%'");
		}
		//TODO  date
	//	private Date lastTimeMenstruation;
		if(search.getLastTimeMenstruation()!=null&&search.getLastTimeMenstruation2()!=null){
				//&&search.getLastTimeMenstruation()<search.getLastTimeMenstruation2()
			sql.append(" and lastTimeMenstruation between '");
			sql.append(DateUtil.formatTime(search.getLastTimeMenstruation()));
			sql.append("' and '");
			sql.append(DateUtil.getNextDay(search.getLastTimeMenstruation2()));
			sql.append("'");
		}
		//private Integer pregnancyNumber;
		if(search.getPregnancyNumber()!=null){
			sql.append(" and pregnancyNumber like '%");
			sql.append(search.getPregnancyNumber());
			sql.append("%'");
		}
		//private Integer childbirthNumber;
		if(search.getChildbirthNumber()!=null){
			sql.append(" and childbirthNumber like '%");
			sql.append(search.getChildbirthNumber());
			sql.append("%'");
		}
		//private Boolean isMenopause;
		if(search.getIsMenopause()!=null){
			sql.append(" and isMenopause= ");
			sql.append(search.getIsMenopause());
		}
		
		//主诉
	//	private Boolean isLeucorrhea;
		if(search.getIsLeucorrhea()!=null){
			sql.append(" and isLeucorrhea=");
			sql.append(search.getIsLeucorrhea());
		}
		//private Boolean isBleed;
		if(search.getIsBleed()!=null){
			sql.append(" and isBleed=");
			sql.append(search.getIsBleed());
		}
		//private String unregularBleed;
		if(search.getUnregularBleed()!=null){
			sql.append(" and unregularBleed like '%");
			sql.append(search.getUnregularBleed());
			sql.append("%'");
		}
		//private String otherComplaints;
		if(search.getOtherComplaints()!=null){
			sql.append(" and otherComplaints like '%");
			sql.append(search.getOtherComplaints());
			sql.append("%'");
		}
		
		//**临床表现*//*
		//private Boolean isSmooth;
		if(search.getIsSmooth()!=null){
			sql.append(" and isSmooth=");
			sql.append(search.getIsSmooth());
		}
		//private Boolean isAcuteInflammation;
		if(search.getIsAcuteInflammation()!=null){
			sql.append(" and isAcuteInflammation=");
			sql.append(search.getIsAcuteInflammation());
		}
		//private Boolean isHypertrophy;
		if(search.getIsHypertrophy()!=null){
			sql.append(" and isHypertrophy=");
			sql.append(search.getIsHypertrophy());
		}
		//private Boolean isPolyp;
		if(search.getIsPolyp()!=null){
			sql.append(" and isPolyp=");
			sql.append(search.getIsPolyp());
		}
	//	private String erosion;
		if(search.getErosion()!=null){
			sql.append(" and erosion like '%");
			sql.append(search.getErosion());
			sql.append("%'");
		}
		//private Boolean isTear;
		if(search.getIsTear()!=null){
			sql.append(" and isTear=");
			sql.append(search.getIsTear());
		}
		//private Boolean isNesslersGlandCyst;
		if(search.getIsNesslersGlandCyst()!=null){
			sql.append(" and isNesslersGlandCyst=");
			sql.append(search.getIsNesslersGlandCyst());
		}
		//private Boolean isWhite;
		if(search.getIsWhite()!=null){
			sql.append(" and isWhite =");
			sql.append(search.getIsWhite());
		}
		//private Boolean isCancer;
		if(search.getIsCancer()!=null){
			sql.append(" and isCancer= ");
			sql.append(search.getIsCancer());
		}
		//private String otherClinical;
		if(search.getOtherClinical()!=null){
			sql.append(" and otherClinical like '%");
			sql.append(search.getOtherClinical());
			sql.append("%'");
		}
		
		
		  //检查内容
	//	private Integer pointNumber;
		if(search.getPointNumber()!=null){
			sql.append(" and pointNumber like '%");
			sql.append(search.getPointNumber());
			sql.append("%'");
		}
	//	private Boolean isComplete;
		if(search.getIsComplete()!=null){
			sql.append(" and isComplete='");
			sql.append(search.getIsComplete());
			sql.append("'");
		}
		//private String checkResult;
		if(search.getCheckResult()!=null){
			sql.append(" and checkResult like '%");
			sql.append(search.getCheckResult());
			sql.append("%'");
		}
		
		//进一步处理意见
		//private Boolean screening;
		if(search.getScreening()!=null){
			sql.append(" and screening=");
			sql.append(search.getScreening());
		}
		//private Boolean checking;
		if(search.getChecking()!=null){
			sql.append(" and checking=");
			sql.append(search.getChecking());
		}
	//	private String otherSuggestion;
		if(search.getOtherSuggestion()!=null){
			sql.append(" and otherSuggestion like '%");
			sql.append(search.getOtherSuggestion());
			sql.append("%'");
		}
		//TODO date 
	//	private Date checkDate;
		if(search.getCheckDate()!=null&&search.getCheckDate2()!=null) {
			sql.append(" and checkDate between  '");
			sql.append(DateUtil.formatTime(search.getCheckDate()));
			sql.append("'  and '");
			sql.append(DateUtil.getNextDay(search.getCheckDate2()));
			sql.append("'");
		}
		//for example : select * from report where 1=1 and  lastTimeMenstruation BETWEEN 2013-07-06 and 2013-07-7;
		
		//private Date modifyDate;
		//ModifyDate method does not provide lookup
		
		if(search.getPhone() != null && !search.getPhone().trim().equals("")){
			sql.append(" and phone like '%");
			sql.append(search.getPhone());
			sql.append("%'");
		}
		//private String doctorName;
		if(search.getDoctorName()!=null){
			sql.append(" and doctorName like '%");
			sql.append(search.getDoctorName());
			sql.append("%'");
		}
		//private Integer doctorId;
		if(search.getDoctorId()!=null){
			sql.append(" and  doctorId like '%");
			sql.append(search.getDoctorId());
			sql.append("%'");
		}
		
		 
		if(search.getPrescribingDoctorName()!=null){
					sql.append(" and prescribingDoctorName like '%");
					sql.append(search.getPrescribingDoctorName());
					sql.append("%'");
		}
		
		//private Boolean isDelete;
		
		return reportDAO.advanceSerach(sql.toString());
	}


	public Map<String,Object> getCurrentPage(Integer maxId,Integer size, Integer page, String sortColumn) throws DBException{
		List<Report> list = reportDAO.getCurrentPage(maxId, size, page, sortColumn);
		Map map = new HashMap<String,Object>();
		map.put("reportList", list);
		map.put("allRecordNumber", reportDAO.getAllRecordNumber());
		if(list != null && !list.isEmpty()){
			map.put("maxId", list.get(0).getReportId());
			map.put("minId",list.get(list.size()-1).getReportId());
		}
		return map;
	}
	public Map<String,Object> getTopPage(Integer size, String sortColumn) throws DBException{
		List<Report> list = reportDAO.getTopPage(size, sortColumn);
		Map map = new HashMap<String,Object>();
		map.put("reportList", list);
		map.put("allRecordNumber", reportDAO.getAllRecordNumber());
		if(list != null && !list.isEmpty()){
			map.put("maxId", list.get(0).getReportId());
			map.put("minId",list.get(list.size()-1).getReportId());
		}
		return map;
	}
	public Map<String,Object> statistic(JSONObject data) throws DBException{
		final Map<String,Object> map = new HashMap<String,Object>();
		 reportDAO.statisticCheckResult(data, new ResultSetHandler(){

			@Override
			public void handle(ResultSet rs) {
				try {
					map.put("normal", rs.getInt("normal"));
					map.put("exception", rs.getInt("exception"));
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			 
		 });
		return map;
	}
	public Workbook exportDate(String startDate,String endDate) throws DBException{
	   	 XLSExport e  =   new  XLSExport();
		 reportDAO.exportData(startDate, endDate, new ExportReportHandler(e));
		 return e.getWorkBook();
	}
	public List<Report> getReportByCaseNumber(String caseNumber)throws DBException{
		return reportDAO.getReportsByCaseNumber(caseNumber);
	}
}

