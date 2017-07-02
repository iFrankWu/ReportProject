/*
 * RestUnit.java
 * Jul 7, 2013
 * com.tibco.rest
 * ReportProject
 * Copyright (C), 2013, TIBCO Software Inc.
 * 
 */
package com.tibco.rest;

import javax.servlet.http.HttpServletRequest;

import com.tibco.util.Const;

/**
 * class description goes here.
 *
 * @author <a href="mailto:swu@tibco-support.com">Frank Wu</a>
 * @version 1.0.0
 */
public class RestUnit {
	public Boolean isSystemAdmin(HttpServletRequest request){
		String currUserType = (String)request.getSession().getAttribute(Const.CURRENT_USER_TYPE);
		if(Const.TYPE_SystemAdmin.equals(currUserType)){
			return true;
		}
		return false;
	}
	public Boolean isDirect(HttpServletRequest request){
		String currUserType = (String)request.getSession().getAttribute(Const.CURRENT_USER_TYPE);
		if(Const.TYPE_Direct.equals(currUserType)){
			return true;
		}
		return false;
	}
	public Boolean isDoctor(HttpServletRequest request){
		String currUserType = (String)request.getSession().getAttribute(Const.CURRENT_USER_TYPE);
		if(Const.TYPE_Doctor.equals(currUserType)){
			return true;
		}
		return false;
	}
	public Boolean notADoctor(HttpServletRequest request){
		String currUserType = (String)request.getSession().getAttribute(Const.CURRENT_USER_TYPE);
		if(Const.TYPE_Doctor.equals(currUserType)){
			return false;
		}
		return true;
	}
}

