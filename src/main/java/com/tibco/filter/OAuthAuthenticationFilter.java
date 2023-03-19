/*
 * OAuthAuthenticationFilter.java
 * May 8, 2013
 * com.tibco.filter
 * AngularJS
 * Copyright (C), 2013, TIBCO Software Inc.
 * 
 */
package com.tibco.filter;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.tibco.util.Const;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * class description goes here.
 *
 * @author <a href="mailto:wushexin@gmail.com">Frank Wu</a>
 * @version 1.0.0
 */
public class OAuthAuthenticationFilter implements ContainerRequestFilter {
    static {
        Const.initLogger();
    }

    private Logger logger = Logger.getLogger(this.getClass());
    @Context
    HttpServletRequest httpRequest;

    private List<String> whitePathList = Arrays.asList("hospital,doctor,report/1/Top/10/1/reportId".split(","));

    @Override
    public ContainerRequest filter(ContainerRequest containerRequest) {
        logger.info(new Date() + "\t" + containerRequest.getRequestUri() + "\t" + containerRequest.getMethod() + "\t" + containerRequest.getQueryParameters() + "\t" + containerRequest.getFormParameters());
        httpRequest.getSession().setMaxInactiveInterval(Const.SESSION_TIMEOUT_IN_MINUTE * 60);

        String path = containerRequest.getPath();
        if (Const.LOGIN_PATH.equals(containerRequest.getPath()) && Const.METHOD_POST.equals(containerRequest.getMethod())) {
            //login method
        } else if ((Const.LOGIN_PATH + "/1").equals(containerRequest.getPath())) {
            //logout
        } else if (containerRequest.getPath().startsWith("crm/clazz") && !Const.METHOD_GET.equals(containerRequest.getMethod())) {
            if (httpRequest.getSession().getAttribute(Const.CURRENT_USER_TYPE).equals(Const.TYPE_Doctor)) { //normal user has no privilegies for this operation
                throw new WebApplicationException(401);
            }

        } else if (containerRequest.getPath().startsWith(Const.OPEN_API_PATH) || containerRequest.getPath().contains("hdd")) {
            //open api don't check it
        } else {
            Object o = httpRequest.getSession().getAttribute(Const.LOGIN_SUCCESS);
            if (o == null) {
                String ip = getIpAddr(httpRequest);
                if ((Const.WHITE_ACCESS_IP_LIST != null && Const.WHITE_ACCESS_IP_LIST.contains(ip)) && (whitePathList.contains(path) || path.contains("getDetail"))) {
                    logger.error(new Date() + "\taccess white page : " + path + " from ip:  " + ip);
                } else {
                    logger.error(new Date() + "\t登陆失败 session中无用户信息");
                    throw new WebApplicationException(401);
                }
            } else {
                Boolean isLogin = (Boolean) o;
                if (!isLogin) {
                    logger.error(new Date() + "\t登陆失败 session中登陆为失败");
                    throw new WebApplicationException(401);
                }
            }
        }

        return containerRequest;
    }

    /**
     * 获取Ip地址
     */
    protected String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

}
