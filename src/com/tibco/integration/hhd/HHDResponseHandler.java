package com.tibco.integration.hhd;

import com.tibco.bean.Report;
import com.tibco.dao.ReportDAO;
import com.tibco.service.HHDService;
import com.tibco.util.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jetty.util.ajax.JSON;

import java.nio.charset.Charset;
import java.util.Date;
import java.util.Map;

import static com.tibco.integration.hhd.HHDClient.IS_CHECK_FINISH;

public class HHDResponseHandler {

    private ReportDAO reportDAO = new ReportDAO();
    private Logger logger = Logger.getLogger(this.getClass());

    private HHDService hhdService = new HHDService();

    private int hhdNotReadyTimes = 0;

    /**
     * 是否需要发送system_report命令
     */
    private static boolean requireSendSystemReportCmd = true;

    /**
     * 此处是处理手持设备返回的结果 需要加锁
     *
     * @param responseMap
     * @throws Exception
     */
    public synchronized void handler(Map<String, String> responseMap) throws Exception {

        //登陆失败
        //'设备未就绪...'
        String status = responseMap.get("socket_status");
        if (StringUtils.isNotBlank(status)) {
            HHDClient.getInstance().setCurrecntStatus(status);
        }

        if(HHDClient.getInstance().lastCommandContext.isFromTimer){
            logger.info(new Date() + "\t定时任务发送的请求 不触发后续操作 它仅仅是为了设备不进入充电状态" +  HHDClient.getInstance().lastCommandContext);
            return;
        }

        if ("退出登陆".equals(status)) {
            Thread.sleep(1000L);
            hhdService.login();
            return;
        }

        if ("登陆失败".equals(status)) {
//            Thread.sleep(5000L);
            Thread.sleep(1000L);
            hhdService.login();
            return;
        }

        if ("登陆成功".equals(status)) {
//            Thread.sleep(2000L);
            Thread.sleep(1000L);
            hhdService.ready();
            return;
        }
        if ("设备未就绪...".equals(status)) {
//            Thread.sleep(5000L);
            hhdNotReadyTimes++;
            Thread.sleep(1000L);
            //设备返回5次未就绪 一般是设备死机了
            if (hhdNotReadyTimes > 5) {
                logger.error(new Date() + "\t设备超过5次返回 设备未就绪一般是设备死机了 此时中断连接，退出登陆，重新登陆尝试");
                hhdService.exit();
                hhdService.socketStatus();
                hhdNotReadyTimes = 0;
                return;
            }
            hhdService.ready();
            return;
        }

        if ("检查过程中...".equals(status)) {
            //检查过程中 一般需要好几分钟 可以等等再去请求 降低HHD服务压力
            Thread.sleep(5000L);
            return;
        }

        if ("设备就绪".equals(status)) {
//            Thread.sleep(5000L);
            requireSendSystemReportCmd = true;
            hhdNotReadyTimes = 0;
            Thread.sleep(1000L);
            Report report = reportDAO.getLastReport();
            boolean lastReportHasFinish = report.getUid() == null || report.getPnorValueResult() == null;
            logger.info(new Date() + "\t设备刚刚就绪 上次检查是否已经结束?\t" + !lastReportHasFinish + "\treport为" + report);

            if (lastReportHasFinish) {
                //如最后一条是今天的 则发起start检测
                if (report.getModifyDate().after(DateUtil.getTodayDate())) {
                    hhdService.start(report.getPatientName(), report.getAge());
                }else{
                    logger.info(new Date() + "\t设备刚刚就绪 上次检查没结束，但是不是今天的 退出下试试 创建日期：" + report.getModifyDate());
                    hhdService.exit();
                }
            }
            return;
        }


        if ("检查结束".equals(status)) {
//            Thread.sleep(5000L);
            Thread.sleep(1000L);

            if (!IS_CHECK_FINISH) {
//                仅当hhd返回的数据不携带业务数据 才发起获取报告单 否则不发起
                if (responseMap.size() < 3 && requireSendSystemReportCmd) {
                    logger.info(new Date() + "\t报告单未结束,发送system_report获取报告单" + responseMap);
                    requireSendSystemReportCmd = false;
                    hhdService.systemReport();
                    return;
                } else {
                    logger.info(new Date() + "\t报告单未结束,但hhd返回数据中已经包括检查结果，因此不发送system-report获取报告单" + responseMap + "requireSendSystemReportCmd："+requireSendSystemReportCmd);
                }
            } else {
                //注：一个报告单检查完了后 系统会一直返回检查结束，如果此时继续发送systemReport那么则会陷入死循环
                //因此在此从数据库中捞取最新的报告单，并启动hdd检查
                logger.info(new Date() + "\t报告单已经结束,不发送system_report命令,IS_CHECK_FINISH:" + HHDClient.IS_CHECK_FINISH);
                Report report = reportDAO.getLastReport();
                boolean lastReportHasFinish = report.getUid() == null || report.getPnorValueResult() == null;
                logger.info(new Date() + "\t获取最新创建的报告单,该报告单是否结束：\t" + !lastReportHasFinish + "\treport为" + report);

                if (lastReportHasFinish) {
                    //如最后一条是今天的 则发起start检测
                    if (report.getModifyDate().after(DateUtil.getTodayDate())) {
//                        hhdService.start(report.getPatientName(), report.getAge());
                        hhdService.ready();
                    }
                }
                return;
            }
        }
        if (responseMap.containsKey("patient_01")) {
            String uid = responseMap.get("patient_01");
            Report report = reportDAO.getReportByUID(uid);
            if (report == null) {
                String name = responseMap.get("patient_05");
                String age = responseMap.get("patient_06");
                if (StringUtils.isBlank(name) || StringUtils.isBlank(age)) {
                    logger.info("患者姓名为空，结果忽略:" + JSON.toString(responseMap));
                    report = reportDAO.getLastReport();
                    reportDAO.updateReport(uid, report.getReportId());
                    return;
                }
                report = reportDAO.getReportByNameAndAge(name, Integer.parseInt(age));
                if (report == null) {
                    logger.warn(String.format("通过姓名%s和年龄%s无法获取报告单，可能是乱码导致", name, age));
                    logger.warn("GBK" + new String(name.getBytes(), Charset.forName("GBK")));
                    logger.warn("ISO-8859-11" + new String(name.getBytes(), Charset.forName("ISO-8859-1")));
                    report = reportDAO.getLastReport();
                }
                reportDAO.updateReport(uid, report.getReportId());
            } else {
//                String checkDate = responseMap.get("patient_02");
                String checkResult = responseMap.get("screening_result");
                String pnorm = responseMap.get("screening_pnorm");
                String points = responseMap.get("screening_nSpots");
                //hhd返回结果 可能会不完整 数据被截断 因此此时需要再次获取报告单检查结果
                if (StringUtils.isBlank(checkResult) || StringUtils.isBlank(pnorm) || StringUtils.isBlank(points)) {
                    logger.info(new Date() + "\t检查结果为空或者pnorm值为空，结果忽略:" + JSON.toString(responseMap));
                    hhdService.systemReport();
                    return;
                }
                Float pnormValue = Float.parseFloat(pnorm);

                reportDAO.updateReport(Integer.parseInt(points), pnormValue, uid);

                if (!IS_CHECK_FINISH) {
                    logger.info(new Date() + "\t报告单已结束,发送exit命令，退出本次检查uid:\t" + uid);
                    IS_CHECK_FINISH = true;
                    requireSendSystemReportCmd = true;
                    Thread.sleep(1000L);
                    hhdService.exit();
                }


                //断开手持设备 会断开wifi连接
//                hhdService.terminate();
            }
        }

    }


}
