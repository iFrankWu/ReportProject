package com.tibco.rest;

import com.tibco.bean.Result;
import com.tibco.integration.hhd.HHDClient;
import com.tibco.service.HHDService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/hdd")
public class HHDResource {

    private HHDService hhdService = new HHDService();

    private Logger logger = Logger.getLogger(this.getClass());

    @Path("{command}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Result executeHHDCommand(@PathParam("command") String command) throws Exception {
        Result result = new Result(true, "");

        logger.info("start process command : " + command);
        switch (command) {
            case "connect":
                HHDClient.getInstance().connect();
                break;
            case "login":
                hhdService.login();
                break;
            case "ready":
                hhdService.ready();
                break;
            case "logout":
                hhdService.logout();
                break;
            case "socket_status":
                hhdService.socketStatus();
            case "system_report":
                hhdService.systemReport();
                break;
            case "terminate":
                hhdService.terminate();
                break;

            case "exit":
                hhdService.exit();
                break;

            case "current":
                HHDClient.getInstance().getCurrecntStatus();
                break;

            default:
                logger.info("No command doing : " + command);

        }
        String status = "获取不到当前设备状态";
        if (StringUtils.isNotBlank(HHDClient.getInstance().getCurrecntStatus())) {
            status = HHDClient.getInstance().getCurrecntStatus();
        }
        result.setDescription(status);

        logger.info("start process command : " + result.getDescription());
        return result;

    }


}
