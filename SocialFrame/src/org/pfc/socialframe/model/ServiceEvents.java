package org.pfc.socialframe.model;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.pfc.socialframe.controller.EventsActivity;

import android.app.Activity;
import android.os.Bundle;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.SessionStore;
import com.facebook.android.Util;

public class ServiceEvents {
	@SuppressWarnings("unused")
	private String resp, today, tomorrow;
	private Date today_date, tomorrow_date;
	private String [][] births;
	private EventsActivity ea;
	public Facebook mFacebook = new Facebook(Constants.APP_ID);
	private AsyncFacebookRunner mAsyncRunner;
	public ServiceEvents(EventsActivity ea){
		this.ea = ea;
	}
	public void showEvents(Activity a){
		mAsyncRunner = new AsyncFacebookRunner(mFacebook);
		SessionStore.restore(mFacebook, a);
		today_date = new Date();
		tomorrow_date = getTomorrow(today_date);
		today = changeFormatDate(today_date);
		tomorrow = changeFormatDate(tomorrow_date);
		Bundle params = new Bundle();
        params.putString("method", "fql.query");
        params.putString("query", "SELECT uid,name,birthday_date FROM user WHERE uid IN (SELECT uid2 FROM friend WHERE uid1= me())");
        try {
			resp = mFacebook.request(params);
			resp = "{\"data\":" + resp + "}";
			JSONArray json = Util.parseJson(resp).getJSONArray("data");
			births = new String [3][json.length()];
			for(int i=0; i < json.length(); i++){
				births[0][i] = String.valueOf(json.getJSONObject(i).getInt("uid"));
				births[1][i] = json.getJSONObject(i).getString("name");
				if(json.getJSONObject(i).isNull("birthday_date")){
					births[2][i] = " ";
				}else{
					births[2][i] = json.getJSONObject(i).getString("birthday_date").substring(0, 5);
				}	
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (FacebookError e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Bundle params2 = new Bundle();
        params2.putString("method", "fql.query");
        params2.putString("query", "SELECT name,creator,description,start_time,location FROM event WHERE eid IN (SELECT eid FROM event_member WHERE uid=me())");
		mAsyncRunner.request(params2, new RequestListenerBase() {
			@Override
			public void onComplete(String response, Object state) {
				try {
					response = "{\"data\":" + response + "}";
					JSONArray json = Util.parseJson(response).getJSONArray("data");
					ArrayList<Event> eventlist = new ArrayList<Event>();
					Event e;
					if(json.length() > 0){
						for(int i=0; i < json.length(); i++){
							e = new Event("", "", "", "", "", "");
							e.setTitle(json.getJSONObject(i).getString("name"));
							e.setCreator(uidToName(births, String.valueOf(json.getJSONObject(i).getInt("creator"))));
							e.setDescription(json.getJSONObject(i).getString("description"));
							e.setDate(String.valueOf(json.getJSONObject(i).getInt("start_time")));
							e.setLocation(json.getJSONObject(i).getString("location"));
							if(e.getDate().equals(today)) eventlist.add(e);
						}
					}
					String [] t = today.split("/");
					ea.updateEvents(filterBirths(births,today), eventlist, t[1], monthToText(t[0]));
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (FacebookError e) {
					e.printStackTrace();
				}
			}
		});
	}
	private String uidToName(String[][] c, String uid) {
		String name = "";
		for(int i = 0; i < c[0].length;i++){
			if(c[0][i].equals(uid)) name = c[1][i];
		}
		return name;
	}
	private Date getTomorrow(Date fch) {
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(fch.getTime());
        cal.add(Calendar.DATE, 1);
        return new Date(cal.getTimeInMillis());
    }
	private String changeFormatDate(Date d){
		String[] dat = d.toString().split(" ");
		String month = formatMonth(dat[1]);
		return month+"/"+dat[2];
	}
	private String monthToText(String mt) {
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
	private String formatMonth(String m){
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
	private ArrayList<String> filterBirths(String[][] b, String t) {
		ArrayList<String> l = new ArrayList<String>();
		for(int i=0; i < b[0].length; i++){
			if(b[2][i].equals(today)){
				l.add(b[1][i]);
			}
		}
		return l;
	}
}
