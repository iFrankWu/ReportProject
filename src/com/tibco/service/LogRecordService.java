package com.tibco.service;

import com.shinetech.sql.exception.DBException;
import com.tibco.bean.LogRecord;
import com.tibco.bean.Result;
import com.tibco.dao.LogRecordDAO;
import com.tibco.util.Const;
import org.json.simple.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class LogRecordService {
    private LogRecordDAO logDAO = new LogRecordDAO();

    public Result addLogRecord(LogRecord log) {
        try {
            logDAO.addLoginRecord(log);
            return new Result(true, "");
        } catch (Exception e) {
            return null;
        }
    }

    public Result addLogRecord(HttpServletRequest request, String operateName, String requestDate) throws DBException {
        LogRecord log = new LogRecord();
        log.setIp(request.getRemoteAddr());
        Object doctorId = request.getSession().getAttribute(Const.CURRENT_USER_ID);
        if (doctorId != null)
            log.setDoctorId((Integer) doctorId);
        log.setMethodName(operateName);
        log.setMethodParam(requestDate);
        return addLogRecord(log);
    }

    public void deleteLoginRecord(Integer loginRecordId) throws DBException {
        logDAO.deleteLoginRecord(loginRecordId);
    }

    public List<LogRecord> getLogList(JSONObject json) throws DBException {
        return logDAO.getLogList(json);
    }
}
