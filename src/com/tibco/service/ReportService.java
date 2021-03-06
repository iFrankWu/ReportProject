/*
 * ReportService.java
 * Jun 24, 2013
 * com.tibco.service
 * ReportProject
 * Copyright (C), 2013, TIBCO Software Inc.
 * 
 */
package com.tibco.service;

import com.shinetech.sql.ResultSetHandler;
import com.shinetech.sql.exception.DBException;
import com.tibco.bean.Report;
import com.tibco.bean.Result;
import com.tibco.bean.Search;
import com.tibco.dao.PatientDAO;
import com.tibco.dao.ReportDAO;
import com.tibco.handle.ExportReportHandler;
import com.tibco.integration.hhd.HHDClient;
import com.tibco.util.DateUtil;
import com.tibco.util.XLSExport;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.eclipse.jetty.util.ajax.JSON;
import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * class description goes here.
 *
 * @author <a href="mailto:wushexin@gmail.com">Frank Wu</a>
 * @version 1.0.0
 */
public class ReportService {
    private ReportDAO reportDAO = new ReportDAO();

    private PatientDAO patientDAO = new PatientDAO();

    private HHDService hhdService = new HHDService();

    private Logger logger = Logger.getLogger(this.getClass());

    public Result addReport(Report report) throws DBException {
        logger.info(new Date() + "\t创建报告单\t" + JSON.toString(report));
        Integer reportId = report.getReportId();

        if (reportId != null && reportId > 0) {
            logger.info(new Date() + "\t" + "更新报告单reportId\t" + reportId);
            reportDAO.updateReport(report);
        } else {
            if (StringUtils.isBlank(report.getPatientName()) || report.getAge() == null) {
                return new Result(false, "姓名和年龄不能为空");
            }
            reportId = reportDAO.addReport(report);
            logger.info(new Date() + "\t" + "点击录入完成,新建报告单\t" + reportId);
            //只要当前的检查结果未获取成功 即可以触发
            if (report.getPnorValueResult() == null || StringUtils.isBlank(report.getUid())) {
                hhdService.socketStatus();
            }
        }
        return new Result(true, reportId);
    }

    public Result updateReport(Report report) throws DBException {
        reportDAO.updateReport(report);
        return new Result(true, "");
    }

    /**
     * 前端页面5s获取一个报告单数据
     *
     * @param reportId
     * @return
     * @throws Exception
     */
    public Report getReportByID(Integer reportId) throws Exception {
        Report report = reportDAO.getReportByID(reportId);
        try {
            if (report.getUid() == null || report.getPnorValueResult() == null) {
                HHDClient.IS_CHECK_FINISH = false;


                hhdService.socketStatus();
                //设备就绪不能发请求
//                if ("检查过程中...".equals(currentStatus) || "检查结束".equals(currentStatus) || "筛查错误".equals(currentStatus)) {
////                    HHDOpreationDTO hddOpreationDTO = new HHDOpreationDTO();
////                    hddOpreationDTO.setSocket_request("system_report");
////                    hhdService.commonRequest(hddOpreationDTO);
//                    hhdService.socketStatus();
//                } else if ("设备未就绪...".equals(currentStatus)) {
//                    hhdService.socketStatus();
//                } else {
//                    logger.error("currentStatus invalid : " + currentStatus + " :" + report);
//                    if ("登陆成功".equals(currentStatus) && LoginSuccessTimes++ > 5) {
//
//                        logger.error("手持设备未就绪，当前状态: " + currentStatus + " 已经超过次数" + LoginSuccessTimes + "，因此退出登陆，重试");
//                        LoginSuccessTimes = 0;
//                        hhdService.socketStatus();
//                        return report;
//                    }
//
//                    //检查结束 退出登陆等都不应该弹出
//                    if (StringUtils.isBlank(currentStatus) || "SocketChannel-Null".equals(currentStatus)) {
//                        throw new Exception("状态异常，建议断开WI-FI连接，重启手持设备，再连接WI-FI,点击'录入完成'按钮重试");
//                    }
//                }
            } else {
                logger.info("check finished for report : " + HHDClient.IS_CHECK_FINISH + " :" + report);
                HHDClient.IS_CHECK_FINISH = true;
            }
        } catch (Exception e) {
            logger.error("get detail got error : " + report, e);
            throw e;
        }
        return report;
    }

    public void deleteReportByID(Integer id) throws DBException {
        reportDAO.deleteReport(id);
    }

    public Map<String, Object> getNextPage(Integer minId, Integer size, Integer page, String sortColumn) throws DBException {
        List<Report> list = reportDAO.getNextPage(minId, size, page, sortColumn);
        Map map = new HashMap<String, Object>();

        map.put("reportList", list);
        map.put("allRecordNumber", reportDAO.getAllRecordNumber());
        if (list != null && !list.isEmpty()) {
            map.put("maxId", list.get(0).getReportId());
            map.put("minId", list.get(list.size() - 1).getReportId());
        }
        return map;
    }

    public Map<String, Object> getPrePage(Integer maxId, Integer size, Integer page, String sortColumn) throws DBException {
        List<Report> list = reportDAO.getPrePage(maxId, size, page, sortColumn);
        Map map = new HashMap<String, Object>();
        map.put("reportList", list);
        map.put("allRecordNumber", reportDAO.getAllRecordNumber());
        if (list != null && !list.isEmpty()) {
            map.put("maxId", list.get(0).getReportId());
            map.put("minId", list.get(list.size() - 1).getReportId());
        }
        return map;
    }

    public List<Report> advanceSerach(Search search) throws DBException {
        StringBuffer sql = new StringBuffer("select *  from report where 1=1 ");

        if (search.getReportId() != null) {
            sql.append(" and reportId = ");
            sql.append(search.getReportId());
        }

        if (search.getReason4doesNotComplete() != null) {
            sql.append(" and reason4doesNotComplete like '%");
            sql.append(search.getReason4doesNotComplete());
            sql.append("%'");
        }
    /*	private Integer reportId;*/

		/*Append sql start*/
        ////**资料*/
        //	private String patientName;
        if (search.getPatientName() != null) {
            sql.append(" and patientName like '%");
            sql.append(search.getPatientName());
            sql.append("%'");
        }
        //	private Integer age;
        if (search.getAge() != null && search.getAge2() != null && search.getAge() < search.getAge2()) {
            sql.append(" and age between ");
            sql.append(search.getAge());
            sql.append(" and ");
            sql.append(search.getAge2());
        }
        //private String caseNumber;
        if (search.getCaseNumber() != null) {
            sql.append(" and caseNumber like '%");
            sql.append(search.getCaseNumber());
            sql.append("%'");
        }
        //TODO  date
        //	private Date lastTimeMenstruation;
        if (search.getLastTimeMenstruation() != null && search.getLastTimeMenstruation2() != null) {
            //&&search.getLastTimeMenstruation()<search.getLastTimeMenstruation2()
            sql.append(" and lastTimeMenstruation between '");
            sql.append(DateUtil.formatTime(search.getLastTimeMenstruation()));
            sql.append("' and '");
            sql.append(DateUtil.getNextDay(search.getLastTimeMenstruation2()));
            sql.append("'");
        }
        //private Integer pregnancyNumber;
        if (search.getPregnancyNumber() != null) {
            sql.append(" and pregnancyNumber like '%");
            sql.append(search.getPregnancyNumber());
            sql.append("%'");
        }
        //private Integer childbirthNumber;
        if (search.getChildbirthNumber() != null) {
            sql.append(" and childbirthNumber like '%");
            sql.append(search.getChildbirthNumber());
            sql.append("%'");
        }
        //private Boolean isMenopause;
        if (search.getIsMenopause() != null) {
            sql.append(" and isMenopause= ");
            sql.append(search.getIsMenopause());
        }

        //主诉
        //	private Boolean isLeucorrhea;
        if (search.getIsLeucorrhea() != null) {
            sql.append(" and isLeucorrhea=");
            sql.append(search.getIsLeucorrhea());
        }
        //private Boolean isBleed;
        if (search.getIsBleed() != null) {
            sql.append(" and isBleed=");
            sql.append(search.getIsBleed());
        }
        //private String unregularBleed;
        if (search.getUnregularBleed() != null) {
            sql.append(" and unregularBleed like '%");
            sql.append(search.getUnregularBleed());
            sql.append("%'");
        }
        //private String otherComplaints;
        if (search.getOtherComplaints() != null) {
            sql.append(" and otherComplaints like '%");
            sql.append(search.getOtherComplaints());
            sql.append("%'");
        }

        //**临床表现*//*
        //private Boolean isSmooth;
        if (search.getIsSmooth() != null) {
            sql.append(" and isSmooth=");
            sql.append(search.getIsSmooth());
        }
        //private Boolean isAcuteInflammation;
        if (search.getIsAcuteInflammation() != null) {
            sql.append(" and isAcuteInflammation=");
            sql.append(search.getIsAcuteInflammation());
        }
        //private Boolean isHypertrophy;
        if (search.getIsHypertrophy() != null) {
            sql.append(" and isHypertrophy=");
            sql.append(search.getIsHypertrophy());
        }
        //private Boolean isPolyp;
        if (search.getIsPolyp() != null) {
            sql.append(" and isPolyp=");
            sql.append(search.getIsPolyp());
        }
        //	private String erosion;
        if (search.getErosion() != null) {
            sql.append(" and erosion like '%");
            sql.append(search.getErosion());
            sql.append("%'");
        }
        //private Boolean isTear;
        if (search.getIsTear() != null) {
            sql.append(" and isTear=");
            sql.append(search.getIsTear());
        }
        //private Boolean isNesslersGlandCyst;
        if (search.getIsNesslersGlandCyst() != null) {
            sql.append(" and isNesslersGlandCyst=");
            sql.append(search.getIsNesslersGlandCyst());
        }
        //private Boolean isWhite;
        if (search.getIsWhite() != null) {
            sql.append(" and isWhite =");
            sql.append(search.getIsWhite());
        }
        //private Boolean isCancer;
        if (search.getIsCancer() != null) {
            sql.append(" and isCancer= ");
            sql.append(search.getIsCancer());
        }
        //private String otherClinical;
        if (search.getOtherClinical() != null) {
            sql.append(" and otherClinical like '%");
            sql.append(search.getOtherClinical());
            sql.append("%'");
        }


        //检查内容
        //	private Integer pointNumber;
        if (search.getPointNumber() != null) {
            sql.append(" and pointNumber like '%");
            sql.append(search.getPointNumber());
            sql.append("%'");
        }
        //	private Boolean isComplete;
        if (search.getIsComplete() != null) {
            sql.append(" and isComplete='");
            sql.append(search.getIsComplete());
            sql.append("'");
        }
        //private String checkResult;
        if (search.getCheckResult() != null) {
            sql.append(" and checkResult like '%");
            sql.append(search.getCheckResult());
            sql.append("%'");
        }

        //进一步处理意见
        //private Boolean screening;
        if (search.getScreening() != null) {
            sql.append(" and screening=");
            sql.append(search.getScreening());
        }
        //private Boolean checking;
        if (search.getChecking() != null) {
            sql.append(" and checking=");
            sql.append(search.getChecking());
        }
        //	private String otherSuggestion;
        if (search.getOtherSuggestion() != null) {
            sql.append(" and otherSuggestion like '%");
            sql.append(search.getOtherSuggestion());
            sql.append("%'");
        }
        //TODO date
        //	private Date checkDate;
        if (search.getCheckDate() != null && search.getCheckDate2() != null) {
            sql.append(" and checkDate between  '");
            sql.append(DateUtil.formatTime(search.getCheckDate()));
            sql.append("'  and '");
            sql.append(DateUtil.getNextDay(search.getCheckDate2()));
            sql.append("'");
        }
        //for example : select * from report where 1=1 and  lastTimeMenstruation BETWEEN 2013-07-06 and 2013-07-7;

        //private Date modifyDate;
        //ModifyDate method does not provide lookup

        if (search.getPhone() != null && !search.getPhone().trim().equals("")) {
            sql.append(" and phone like '%");
            sql.append(search.getPhone());
            sql.append("%'");
        }
        //private String doctorName;
        if (search.getDoctorName() != null) {
            sql.append(" and doctorName like '%");
            sql.append(search.getDoctorName());
            sql.append("%'");
        }
        //private Integer doctorId;
        if (search.getDoctorId() != null) {
            sql.append(" and  doctorId like '%");
            sql.append(search.getDoctorId());
            sql.append("%'");
        }


        if (search.getPrescribingDoctorName() != null) {
            sql.append(" and prescribingDoctorName like '%");
            sql.append(search.getPrescribingDoctorName());
            sql.append("%'");
        }

        //private Boolean isDelete;

        return reportDAO.advanceSerach(sql.toString());
    }


    public Map<String, Object> getCurrentPage(Integer maxId, Integer size, Integer page, String sortColumn) throws DBException {
        List<Report> list = reportDAO.getCurrentPage(maxId, size, page, sortColumn);
        Map map = new HashMap<String, Object>();
        map.put("reportList", list);
        map.put("allRecordNumber", reportDAO.getAllRecordNumber());
        if (list != null && !list.isEmpty()) {
            map.put("maxId", list.get(0).getReportId());
            map.put("minId", list.get(list.size() - 1).getReportId());
        }
        return map;
    }

    public Map<String, Object> getTopPage(Integer size, String sortColumn) throws DBException {
        List<Report> list = reportDAO.getTopPage(size, sortColumn);
        Map map = new HashMap<String, Object>();
        map.put("reportList", list);
        map.put("allRecordNumber", reportDAO.getAllRecordNumber());
        if (list != null && !list.isEmpty()) {
            map.put("maxId", list.get(0).getReportId());
            map.put("minId", list.get(list.size() - 1).getReportId());
        }
        return map;
    }

    public Map<String, Object> statistic(JSONObject data) throws DBException {
        final Map<String, Object> map = new HashMap<String, Object>();
        reportDAO.statisticCheckResult(data, new ResultSetHandler() {

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

    public Workbook exportDate(String startDate, String endDate) throws DBException {
        XLSExport e = new XLSExport();
        reportDAO.exportData(startDate, endDate, new ExportReportHandler(e));
        return e.getWorkBook();
    }

    public List<Report> getReportByCaseNumber(String caseNumber) throws DBException {
        return reportDAO.getReportsByCaseNumber(caseNumber);
    }

   public Report getReportsByOutpatientNoOrAdmissionNo(String no) throws DBException{
        return reportDAO.getReportsByOutpatientNoOrAdmissionNo(no);
    }

    public String parsePnormFromCsv(String file) {
//       String file = "/Users/frank/Downloads/1333B2-000181-result.csv";
        //声明流对象
        FileInputStream fis = null;
        try {
            //创建流对象
            fis = new FileInputStream(file);
            //读取数据，并将读取到的数据存储到数组中
            BufferedReader input = new BufferedReader(new InputStreamReader(fis));
            String line = null;
            while ((line = input.readLine()) != null) {
                if (line.contains("P NORM")) {
                    String norm[] = line.split(",");
                    String pnorm = norm[1].split("\r\n")[0];
                    return pnorm;
                }
            }

            return "0";
        } catch (Exception e) {
            logger.error(file, e);
        } finally {
            try {
                //关闭流，释放资源
                fis.close();
            } catch (Exception e) {
            }
        }
        return "0";
    }

    public Report getPatientInfo(String mzh, String zyid) throws DBException {
        return patientDAO.getPatientInfo(mzh, zyid);
    }
}

