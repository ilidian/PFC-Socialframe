package org.pfc.socialframe.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
//Saber que mes es
public class UtilityFacebookNet {
	public static String formatDate(String birthday){
		String date = "";
		String month = "";
		String[] d = new String[2];
		d = birthday.substring(0, 5).split("/");
		switch(Integer.parseInt(d[0])){
		case 1:
			month = "Enero";
			break;
		case 2:
			month = "Febrero";
			break;
		case 3:
			month = "Marzo";
			break;
		case 4:
			month = "Abril";
			break;
		case 5:
			month = "Mayo";
			break;
		case 6:
			month = "Junio";
			break;
		case 7:
			month = "Julio";
			break;
		case 8:
			month = "Agosto";
			break;
		case 9:
			month = "Septiembre";
			break;
		case 10:
			month = "Octubre";
			break;
		case 11:
			month = "Noviembre";
			break;
		case 12:
			month = "Diciembre";
			break;
		}
		date = d[1]+" de "+month;
		return date;
	}
	//Convertir un uid a name en mensajes privados
	public static String getNameatUid(String[][] c, String uid) {
		String name = "";
		for(int i = 0; i < c[0].length;i++){
			if(c[0][i].equals(uid)) name = c[1][i]+":";
		}
		return name;
	}
	//Dar formato a una localización
	public static String formatLocation(String city, String country){
		String loc = "", location="";
		if(country.contains("_")){
			String[] cnt = country.split("_");
			for(int i = 0; i < Constants.Countries[0].length; i++){
				if(cnt[1].equals(Constants.Countries[0][i])) loc = Constants.Countries[1][i];
			}
		}else loc = country;
		if(city.equals("")) location= loc;
		else location = city+", "+loc;
		return location;
	}
	public static String uidToName(String[][] c, String uid) {
		String name = "";
		for(int i = 0; i < c[0].length;i++){
			if(c[0][i].equals(uid)) name = c[1][i];
		}
		return name;
	}
	//Dia actual
	public static Date getTomorrow(Date fch) {
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(fch.getTime());
        cal.add(Calendar.DATE, 1);
        return new Date(cal.getTimeInMillis());
    }
	//Dar formato a una fecha
	public static String changeFormatDate(Date d){
		String[] dat = d.toString().split(" ");
		String month = formatMonth(dat[1]);
		return month+"/"+dat[2];
	}
	//Dar formato al mes
	public static String monthToText(String mt) {
		String m = "";
		if(mt.equals("01")) m = "Enero";
		if(mt.equals("02")) m = "Febrero";
		if(mt.equals("03")) m = "Marzo";
		if(mt.equals("04")) m = "Abril";
		if(mt.equals("05")) m = "Mayo";
		if(mt.equals("06")) m = "Junio";
		if(mt.equals("07")) m = "Julio";
		if(mt.equals("08")) m = "Agosto";
	    if(mt.equals("09")) m = "Septiembre";
		if(mt.equals("10")) m = "Octubre";
		if(mt.equals("11")) m = "Noviembre";
		if(mt.equals("12")) m = "Diciembre";
		return m;
	}
	//Dar número al mes
	public static String formatMonth(String m){
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
	//Filtrar cumpleaños
	public static ArrayList<String> filterBirths(String[][] b, String t) {
		ArrayList<String> l = new ArrayList<String>();
		for(int i=0; i < b[0].length; i++){
			if(b[2][i].equals(t)){
				l.add(b[1][i]);
			}
		}
		return l;
	}
}
