/*
 * OAuthAuthenticationFilter.java
 * May 8, 2013
 * com.tibco.filter
 * AngularJS
 * Copyright (C), 2013, TIBCO Software Inc.
 * 
 */
package com.tibco.filter;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.tibco.util.Const;

/**
 * class description goes here.
 * 
 * @author <a href="mailto:swu@tibco-support.com">Frank Wu</a>
 * @version 1.0.0
 */
public class OAuthAuthenticationFilter implements ContainerRequestFilter {
	static{
		Const.initLogger();
	}
	@Context
    HttpServletRequest httpRequest;
	
	@Override
	public ContainerRequest filter(ContainerRequest containerRequest) {
		System.out.println(containerRequest.getPath()+":"+containerRequest.getMethod());
		httpRequest.getSession().setMaxInactiveInterval(Const.SESSION_TIMEOUT_IN_MINUTE*60);
		if(Const.LOGIN_PATH.equals(containerRequest.getPath()) && Const.METHOD_POST.equals(containerRequest.getMethod())){
			//login method
		}else if((Const.LOGIN_PATH+"/1").equals(containerRequest.getPath())){
			//logout
		}else if(containerRequest.getPath().startsWith("crm/clazz") && !Const.METHOD_GET.equals(containerRequest.getMethod())){
			if( httpRequest.getSession().getAttribute(Const.CURRENT_USER_TYPE).equals(Const.TYPE_Doctor)) //normal user has no privilegies for this operation
				throw new WebApplicationException(401);
		}else if(containerRequest.getPath().startsWith(Const.OPEN_API_PATH)){
			//open api don't check it
		}else{
			Object o = httpRequest.getSession().getAttribute(Const.LOGIN_SUCCESS);
			if(o == null ){
				throw new WebApplicationException(401);
			}else{
				 Boolean isLogin = (Boolean)o;
				 if(!isLogin)
					 throw new WebApplicationException(401);
			}
		}
 
//		// Read the OAuth parameters from the request
//		OAuthServerRequest request = new OAuthServerRequest(containerRequest);
//		OAuthParameters params = new OAuthParameters();
//		params.readRequest(request);
//		
//		
//		System.out.println("------------");
//		System.out.println(1+""+containerRequest.getCookies());
//		System.out.println("filter is working......");
//		
//		System.out.println(2+""+params.toString());
//		
//		System.out.println(3+""+containerRequest.getProperties());
//		System.out.println(4+""+containerRequest.getRequestHeaders());
//		System.out.println(5+""+containerRequest.getQueryParameters());
//		System.out.println(6+""+containerRequest.getMethod());
//		System.out.println(7+""+containerRequest.getPath());
//		System.out.println(8+""+httpRequest.getSession());
//		System.out.println("------------");
//		
//		// Set the secret(s), against which we will verify the request
//		OAuthSecrets secrets = new OAuthSecrets();
//		 
//		// ... secret setting code ...
//		
//		// Check that the timestamp has not expired
//		String timestampStr = params.getTimestamp();
//		// ... timestamp checking code ...
//		
//		// Verify the signature
//		try {
//			if(!OAuthSignature.verify(request, params, secrets)) {
//				throw new WebApplicationException(401);
//			}
//		} catch (OAuthSignatureException e) {
////			throw new WebApplicationException(e, 401);
//			e.printStackTrace();
//		}
		return containerRequest;
	}
	public void initSystem(){
		
	}
}
