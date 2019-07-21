package com.tibco.rest;

import com.tibco.bean.HHDOpreationDTO;
import com.tibco.integration.hhd.HHDClient;
import com.tibco.service.HHDService;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/hdd")
public class HHDResource {

    private HHDService hhdService = new HHDService();


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public void hddOpertaion(HHDOpreationDTO hddOpreationDTO) throws Exception {
        String socketRequest = hddOpreationDTO.getSocket_request();
        switch (socketRequest) {
            case "connect":
                HHDClient.getInstance().connect();
                break;
            case "login":
                hhdService.login();
                break;
            case "ready":
                hhdService.commonRequest(hddOpreationDTO);
                break;
            case "logout":
                hhdService.commonRequest(hddOpreationDTO);
                break;
            case "start":
                hhdService.start("11", 11);
                break;
            case "socket_status":
                hhdService.commonRequest(hddOpreationDTO);
            case "system_report":
                hhdService.commonRequest(hddOpreationDTO);
                break;
            case "terminate":
                hhdService.commonRequest(hddOpreationDTO);
                break;

            case "exit":
                hhdService.commonRequest(hddOpreationDTO);
                break;

            default:
                System.out.println("No action doing");

        }

    }


}
