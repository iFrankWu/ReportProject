/*
 * ExportReportHandler.java
 * Jul 8, 2013
 * com.tibco.handle
 * ReportProject
 * Copyright (C), 2013, TIBCO Software Inc.
 * 
 */
package com.tibco.handle;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang.StringUtils;

import com.shinetech.sql.ResultSetHandler;
import com.shinetech.sql.exception.DBException;
import com.tibco.bean.Hospital;
import com.tibco.dao.HospitalDAO;
import com.tibco.util.XLSExport;

/**
 * class description goes here.
 *
 * @author <a href="mailto:swu@tibco-support.com">Frank Wu</a>
 * @version 1.0.0
 */
public class ExportReportHandler implements ResultSetHandler {
	private Hospital hopital;
	private String tableHeader[] = "报告单ID,检查日期,姓名,年龄,病历号,主机序列号,手控器序列号,主诉,临床表现,初善仪检查结果,初善仪点探数量,是否绝经,主诉/白带多,主诉/性交出血,主诉/不规则流血,主诉/其他,临床表现/光滑,临床表现/急性炎症,临床表现/肥大,临床表现/息肉,临床表现/柱状上皮异位,临床表现/撕裂,临床表现/纳氏腺囊肿,临床表现/白斑,临床表现/可疑癌,临床表现/其他,操作人员,申请医生,主诉/LCT,主诉/HPV,临床表现/接触性出血,处理意见"
			.split(",");

	public ExportReportHandler(XLSExport xlsExport) throws DBException {
		this.xlsExport = xlsExport;
		HospitalDAO hospitalDAO = new HospitalDAO();
		hopital = hospitalDAO.getHospital();
		xlsExport.createRow(0);
		// create table header
		for (int i = 0; i < tableHeader.length; i++) {
			xlsExport.setCell(i, tableHeader[i]);
		}
	}

	private int rowIndex = 1;
	private XLSExport xlsExport;

	@Override
	public void handle(ResultSet rs) {
		xlsExport.createRow(rowIndex);
		try {
			xlsExport.setCell(0, rs.getInt("reportId"));
			xlsExport.setCell(1, rs.getDate("checkDate") + "");
			xlsExport.setCell(2, rs.getString("patientName"));
			xlsExport.setCell(3, rs.getInt("age"));
			xlsExport.setCell(4, rs.getString("caseNumber"));
			xlsExport.setCell(5, hopital.getMachineNumber());
			xlsExport.setCell(6, hopital.getHandController());
			xlsExport.setCell(7, getMainCompliants(rs));
			xlsExport.setCell(8, getClinical(rs));
			xlsExport.setCell(9, rs.getString("checkResult"));
			xlsExport.setCell(10, rs.getInt("pointNumber"));
			xlsExport.setCell(11, rs.getBoolean("isMenopause") == true ? "是" : "否");

			/**
			 * 主诉
			 */
			xlsExport.setCell(12, rs.getBoolean("isLeucorrhea") == true ? "是" : "否");
			xlsExport.setCell(13, rs.getBoolean("isBleed") == true ? "是" : "否");
			xlsExport.setCell(14, rs.getString("unregularBleed"));
			xlsExport.setCell(15, rs.getString("otherComplaints"));

			/**
			 * 临床表现
			 */
			xlsExport.setCell(16, rs.getBoolean("isSmooth") == true ? "是" : "否");
			xlsExport.setCell(17, rs.getBoolean("isAcuteInflammation") == true ? "是" : "否");
			xlsExport.setCell(18, rs.getBoolean("isHypertrophy") == true ? "是" : "否");
			xlsExport.setCell(19, rs.getBoolean("isPolyp") == true ? "是" : "否");
			xlsExport.setCell(20, rs.getString("erosion"));
			xlsExport.setCell(21, rs.getBoolean("isTear") == true ? "是" : "否");
			xlsExport.setCell(22, rs.getBoolean("isNesslersGlandCyst") == true ? "是" : "否");
			xlsExport.setCell(23, rs.getBoolean("isWhite") == true ? "是" : "否");
			xlsExport.setCell(24, rs.getBoolean("isCancer") == true ? "是" : "否");
			xlsExport.setCell(25, rs.getString("otherClinical"));

			xlsExport.setCell(26, rs.getString("doctorName"));
			xlsExport.setCell(27, rs.getString("prescribingDoctorName"));

			xlsExport.setCell(28, rs.getString("lct"));
			xlsExport.setCell(29, rs.getString("hpv"));
			xlsExport.setCell(30, rs.getString("touchbleeding"));
			String nextStepSuggestion = "";
			if (rs.getBoolean("screening")) {
				nextStepSuggestion = "按照国家宫颈癌筛查指南定期筛查";
			}

			if (rs.getBoolean("checkHpv")) {
				if (nextStepSuggestion.length() > 0) {
					nextStepSuggestion += "/建议进行HPV筛查";
				} else {
					nextStepSuggestion = "建议进行HPV筛查";
				}
			}

			if (rs.getBoolean("checking")) {
				if (nextStepSuggestion.length() > 0) {
					nextStepSuggestion += "/进行阴道镜或阴道镜下活检";
				} else {
					nextStepSuggestion = "进行阴道镜或阴道镜下活检";
				}
			}

			if (StringUtils.isNotBlank(rs.getString("otherSuggestion"))) {
				if (nextStepSuggestion.length() > 0) {
					nextStepSuggestion += "/";
				}
				nextStepSuggestion += rs.getString("otherSuggestion");
			}
			xlsExport.setCell(31, nextStepSuggestion);

			rowIndex++;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getMainCompliants(ResultSet rs) throws SQLException {
		String compliants = "";
		Boolean isLeucorrhea = rs.getBoolean("isLeucorrhea");
		Boolean isBleed = rs.getBoolean("isBleed");
		String unregularBleed = rs.getString("unregularBleed");
		String otherComplaints = rs.getString("otherComplaints");
		if (isLeucorrhea) {
			compliants += "白带多/";
		}
		if (isBleed) {
			compliants += "性交出血/";
		}
		if (unregularBleed != null) {
			compliants += "不规则流血:" + unregularBleed + "/";
		}
		if (otherComplaints != null) {
			compliants += otherComplaints;
		}
		if (compliants.endsWith("/")) {
			compliants = compliants.substring(0, compliants.length() - 1);
		}
		return compliants;
	}

	private String getClinical(ResultSet rs) throws SQLException {
		String clinical = "";
		Boolean isSmooth = rs.getBoolean("isSmooth");
		clinical = getBooleanValue(clinical, isSmooth, "光滑");

		Boolean isAcuteInflammation = rs.getBoolean("isAcuteInflammation");
		clinical = getBooleanValue(clinical, isAcuteInflammation, "急性炎症");

		Boolean isHypertrophy = rs.getBoolean("isHypertrophy");
		clinical = getBooleanValue(clinical, isHypertrophy, "肥大");

		Boolean isPolyp = rs.getBoolean("isPolyp");
		clinical = getBooleanValue(clinical, isPolyp, "息肉");

		String erosion = rs.getString("erosion");
		clinical = getStringValue(clinical, erosion, "糜烂");

		Boolean isTear = rs.getBoolean("isTear");
		clinical = getBooleanValue(clinical, isTear, "撕裂");

		Boolean isNesslersGlandCyst = rs.getBoolean("isNesslersGlandCyst");
		clinical = getBooleanValue(clinical, isNesslersGlandCyst, "纳氏腺囊肿");

		Boolean isWhite = rs.getBoolean("isWhite");
		clinical = getBooleanValue(clinical, isWhite, "白斑");

		Boolean isCancer = rs.getBoolean("isCancer");
		clinical = getBooleanValue(clinical, isCancer, "可疑癌");

		String otherClinical = rs.getString("otherClinical");
		if (otherClinical != null) {
			clinical += otherClinical;
		}
		if (clinical.endsWith("/")) {
			clinical = clinical.substring(0, clinical.length() - 1);
		}
		return clinical;
	}

	private String getBooleanValue(String resultStr, Boolean isTure, String lableName) {
		if (isTure) {
			resultStr += lableName + "/";
		}
		return resultStr;
	}

	private String getStringValue(String resultStr, String isNull, String lableName) {
		if (isNull != null) {
			resultStr += lableName + ":" + isNull + "/";
		}
		return resultStr;
	}

}
