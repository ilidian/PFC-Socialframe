package org.pfc.socialframe.model;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Message {
	private String date;
	private String sender;
	private String msg;
	public Message(String date, String sender, String msg){
		this.setDate(date);
		this.setSender(sender);
		this.setMsg(msg);
	}
	public String getDate() {
		long unixTime = Long.parseLong(date);  
		long timestamp = unixTime*1000;
		Calendar cal = new GregorianCalendar();
		cal.setTimeInMillis(timestamp);
		cal.add(Calendar.HOUR, -7);
		java.util.Date d = new java.util.Date(cal.getTimeInMillis());
		String[] dat = d.toString().split(" ");
		String month = formatMonth(dat[1]);
		String[] hour = dat[3].split(":");
		return dat[2]+"-"+month+"-"+dat[5]+"  "+hour[0]+":"+hour[1];
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	private String formatMonth(String m){
		String mo = "";
		if(m.equals("Jan")) mo = "Enero";
		if(m.equals("Feb")) mo = "Febrero";
		if(m.equals("Mar")) mo = "Marzo";
		if(m.equals("Apr")) mo = "Abril";
		if(m.equals("May")) mo = "Mayo";
		if(m.equals("Jun")) mo = "Junio";
		if(m.equals("Jul")) mo = "Julio";
		if(m.equals("Aug")) mo = "Agosto";
	    if(m.equals("Sep")) mo = "Septiembre";
		if(m.equals("Oct")) mo = "Octubre";
		if(m.equals("Nov")) mo = "Noviembre";
		if(m.equals("Dec")) mo = "Diciembre";
		return mo;
	}
}
