/*
 * Jetty.java
 * Jun 5, 2013
 * com.tibco.test
 * AngularJS
 * Copyright (C), 2013, TIBCO Software Inc.
 * 
 */
package com.tibco.test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.spi.container.servlet.ServletContainer;
import com.tibco.util.Const;

/**
 * class description goes here.
 *
 * @author <a href="mailto:swu@tibco-support.com">Frank Wu</a>
 * @version 1.0.0
 */
public class Jetty {
	public static void main(String[] args) throws Exception {
		Const.initLogger();
	    Server server = new Server(8081);
	    ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
	   
	    context.setResourceBase("./web");
//	    context.addServlet(new ServletHolder(new ServletContainer(new PackagesResourceConfig("com.tibco.rest"),new POJOMappingFeature(true) )), "/rest/*");
		
		
		
		
		 Map<String,Object> initMap = new HashMap<String, Object>();
		    initMap.put("com.sun.jersey.api.json.POJOMappingFeature", "true");
		    initMap.put("com.sun.jersey.config.property.packages", "com.tibco.rest");
		    initMap.put("com.sun.jersey.spi.container.ContainerRequestFilters", "com.tibco.filter.OAuthAuthenticationFilter");
		    //initMap.put("com.sun.jersey.api.json.POJOMappingFeature", "true");
		    ServletHolder servletHolder = new ServletHolder(new ServletContainer(new PackagesResourceConfig(initMap)));
		    
		    context.addServlet(servletHolder, "/truscreen/*");
		    
		    context.setWelcomeFiles(new String[] { "index.html" });
			context.addServlet(new ServletHolder(new DefaultServlet()), "/*");
			server.setHandler(context);
			
//			context.
		if(!new File("./web/upload").exists()){
            File f = new File("./web/upload");
            f.mkdir();
        }
		
		
//		context.addFilter(OAuthAuthenticationFilter.class, "/", null);
	    server.start();
	    System.out.println(context.getResourceBase());
	    

//		Server server = new Server(8082);
//		ServletContextHandler context = new ServletContextHandler(
//				ServletContextHandler.SESSIONS);
//		context.setResourceBase("./WebRoot/web");
//		context.setWelcomeFiles(new String[] { "index.html" });
////		context.setContextPath("/hello");
//		server.setHandler(context);
//	 System.out.println(context.getResourceBase());
//		context.addServlet(new ServletHolder(new HelloServlet()), "/hello/*");
//		context.addServlet(new ServletHolder(new JSONServlet()), "/json/*");
//		context.addServlet(new ServletHolder(new DefaultServlet()), "/*");
//		server.start();
		System.out.println("http://localhost:8081");
	
	}
	public static class JSONServlet extends HttpServlet {
		private static final long serialVersionUID = -6154475799000019575L;
		private static final String greeting = "Hello JSON";

		protected void doGet(HttpServletRequest request,
				HttpServletResponse response) throws ServletException,
				IOException {
			response.setContentType("text/html");
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().println(greeting);
		}
		protected void doPost(HttpServletRequest request,
				HttpServletResponse response) throws ServletException,
				IOException {
			response.setContentType("text/html");
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().println(greeting);
		}

	}
}

