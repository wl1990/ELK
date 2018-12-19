package com.test.esdemo.utils;


import org.apache.commons.lang.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BDateUtil {

	public static String getDateStrNow() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		return format.format(new Date());
	}
	/**
	 * 毫秒级时间
	 * @return
	 */
	public static String getDateTimeNow(){
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		return format.format(new Date());
	}
	public static String formatDateStr(String yyyyMMddHHmmss){
		if(StringUtils.isBlank(yyyyMMddHHmmss)){
			return null;
		}
		try {
			Date date = new SimpleDateFormat("yyyyMMddHHmmss").parse(yyyyMMddHHmmss);
			
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Date getDate(String yyyyMMddHHmmss){
		if(StringUtils.isBlank(yyyyMMddHHmmss)){
			return null;
		}
		try {
			return new SimpleDateFormat("yyyyMMddHHmmss").parse(yyyyMMddHHmmss);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Date getDate(String time, String format){
		if(StringUtils.isBlank(time)){
			return null;
		}
		try {
			return new SimpleDateFormat(format).parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getDateStr(String format){
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat dateformat = new SimpleDateFormat(format);
		return dateformat.format(cal.getTime());
	}
	
	public static String getDateStrOffHour(Integer offHour, String format){
		if(offHour == null){
			offHour = 0;
		}
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR_OF_DAY, offHour);
		SimpleDateFormat dateformat = new SimpleDateFormat(format);
		return dateformat.format(cal.getTime());
	}
	
	public static String getDateStrOffDay(Integer offDay, String format){
		if(offDay == null){
			offDay = 0;
		}
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, offDay);
		SimpleDateFormat dateformat = new SimpleDateFormat(format);
		return dateformat.format(cal.getTime());
	}
	
	public static String getDateStrOffDay(String inDateStr, String inFormat, String outFormat, Integer offDay){
		if(offDay == null){
			offDay = 0;
		}
		Date date = null;
		try {
			date = new SimpleDateFormat(inFormat).parse(inDateStr);
		} catch (ParseException e) {
			date = new Date();
		}
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, offDay);
		SimpleDateFormat dateformat = new SimpleDateFormat(outFormat);
		return dateformat.format(cal.getTime());
	}
	
	public static String getDateStrOffMinute(String inDateStr, String inFormat, String outFormat, Integer offMinute){
		if(offMinute == null){
			offMinute = 0;
		}
		Date date = null;
		try {
			date = new SimpleDateFormat(inFormat).parse(inDateStr);
		} catch (ParseException e) {
			date = new Date();
		}
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MINUTE, offMinute);
		SimpleDateFormat dateformat = new SimpleDateFormat(outFormat);
		return dateformat.format(cal.getTime());
	}
	
	public static String getDateStr(String inDateStr, String inFormat, String outFormat){
		try {
			Date date = new SimpleDateFormat(inFormat).parse(inDateStr);
			return new SimpleDateFormat(outFormat).format(date.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void sleep(int second){
		if(second <= 0){
			return;
		}
		try {
			Thread.sleep(second * 1000);
		} catch (Exception e) {
		}
	}

	/**
	 * yyyy-MM-dd HH:mm:ss to yyyyMMddHHmmss
	 * @param time
	 * @return
	 */
	public static String convertTime(String time) {
		time = time.replaceAll("-", "");
		time = time.replaceAll(" ", "");
		time = time.replaceAll(":", "");
		return time;
	}

	public static void main(String[] args) {
	/*	String datetime = BDateUtil.getDateStrOffDay(1,"yyyyMMdd");
		System.out.println("args = [" + datetime + "]");*/

	}

	
}
