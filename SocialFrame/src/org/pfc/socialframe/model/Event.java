package org.pfc.socialframe.model;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Event {
	private String title;
	private String creator;
	private String description;
	private String date;
	@SuppressWarnings("unused")
	private String timedate;
	private String location;
	public Event(String title, String creator, String description, String date, String timedate, String location){
		this.setTitle(title);
		this.setCreator(creator);
		this.setDescription(description);
		this.setDate(date);
		this.setTime(timedate);
		this.setLocation(location);
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTitle() {
		return title;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getCreator() {
		return creator;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDescription() {
		return description;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getDate() {
		long unixTime = Long.parseLong(date);  
		long timestamp = unixTime*1000; 
		Calendar cal = new GregorianCalendar();
		cal.setTimeInMillis(timestamp);
		cal.add(Calendar.HOUR, -7);
		java.util.Date d = new java.util.Date(cal.getTimeInMillis());
		String[] dat = d.toString().split(" ");
		String month = numberMonth(dat[1]);
		return month+"/"+dat[2];
	}
	public void setTime(String timedate) {
		this.timedate = timedate;
	}
	public String getTime() {
		long unixTime = Long.parseLong(date);  
		long timestamp = unixTime*1000;
		Calendar cal = new GregorianCalendar();
		cal.setTimeInMillis(timestamp);
		cal.add(Calendar.HOUR, -7);
		java.util.Date d = new java.util.Date(cal.getTimeInMillis());
		String[] dat = d.toString().split(" ");
		String[] hour = dat[3].split(":");
		return hour[0]+":"+hour[1];
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getLocation() {
		return location;
	}
	public static String numberMonth(String m){
		String mo = "";
		if(m.equals("Jan")) mo = "01";
		if(m.equals("Feb")) mo = "02";
		if(m.equals("Mar")) mo = "03";
		if(m.equals("Apr")) mo = "04";
		if(m.equals("May")) mo = "05";
		if(m.equals("Jun")) mo = "06";
		if(m.equals("Jul")) mo = "07";
		if(m.equals("Aug")) mo = "08";
	    if(m.equals("Sep")) mo = "09";
		if(m.equals("Oct")) mo = "10";
		if(m.equals("Nov")) mo = "11";
		if(m.equals("Dec")) mo = "12";
		return mo;
	}
}
