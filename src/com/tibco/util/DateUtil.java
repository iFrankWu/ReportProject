/*
 * DateUtil.java	 <br>
 * 2011-7-23<br>
 * com.shinetech.util <br>
 * Copyright (C), 2011-2012, ShineTech. Co., Ltd.<br>
 * 
 */
package com.tibco.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Class description goes here.
 *
 * @author <a href="mailto:wushexin@gmail.com">Frank.Wu</a>
 * @version 1.0.0
 */

public class DateUtil {
	public static String TIME_FORMAT_TYPE = "yyyy-MM-dd HH:mm:ss";
	public static String DATA_FORMAT_TYPE = "yyyy-MM-dd";
	private static DateFormat dateFormat= new SimpleDateFormat(DATA_FORMAT_TYPE);
	private static DateFormat timeFormat= new SimpleDateFormat(TIME_FORMAT_TYPE);
	public static String getNowDate(){
		return timeFormat.format(Calendar.getInstance().getTime());
	}
	public static String getToday(){
		return timeFormat.format(Calendar.getInstance().getTime()).substring(0,10);
	}
	public static String getYestorday(){
		Calendar cl = Calendar.getInstance();
		cl.set(Calendar.DAY_OF_MONTH, cl.get(Calendar.DAY_OF_MONTH)-1);
		return timeFormat.format(cl.getTime()).substring(0, 10);
	}
	public static String getFormatDate(Date date){
		return timeFormat.format(date).substring(0,10);
	}
	public static String formatDate(Date date){
		return timeFormat.format(date).substring(0,10);
	}
	public static String formatTime(Date date){
		return timeFormat.format(date).substring(0,10);
	}
	public static String getNextDay(String date){
		try{
			Calendar cl = Calendar.getInstance();
			cl.setTime(dateFormat.parse(date));
			cl.set(Calendar.DAY_OF_MONTH, cl.get(Calendar.DAY_OF_MONTH)+1);
			return dateFormat.format(cl.getTime()).substring(0, 10);
		}catch(Exception e)
		{
			e.printStackTrace();
		}	
		return null;
	}
	
	public static String getNextDay(Date date){
		try{
			Calendar cl = Calendar.getInstance();
			cl.setTime(date);
			cl.set(Calendar.DAY_OF_MONTH, cl.get(Calendar.DAY_OF_MONTH)+1);
			return dateFormat.format(cl.getTime()).substring(0, 10);
		}catch(Exception e)
		{
			e.printStackTrace();
		}	
		return null;
	}
	
	
	public static String formatTime(String date){
		return dateFormat.format(date).substring(0,10);
	}
	public static void main(String[] args) {
//		System.out.println(getNowDate());
//		System.out.println(getToday());
//		System.out.println(getYestorday());
		System.out.println(getNextDay("2013-07-31"));
	}
}
