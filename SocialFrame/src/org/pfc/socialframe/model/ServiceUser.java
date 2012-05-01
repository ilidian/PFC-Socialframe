package org.pfc.socialframe.model;

import org.json.JSONException;
import org.json.JSONObject;
import org.pfc.socialframe.controller.InfoActivity;

import android.app.Activity;
import android.os.Bundle;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.SessionStore;
import com.facebook.android.Util;

public class ServiceUser {
	private InfoActivity iac;
	public Facebook mFacebook = new Facebook(Constants.APP_ID);
	private AsyncFacebookRunner mAsyncRunner;
	public ServiceUser(InfoActivity iac){
		this.iac = iac;
	}
	public void showUser(Activity ia, final User u){
		mAsyncRunner = new AsyncFacebookRunner(mFacebook);
		SessionStore.restore(mFacebook, ia);
		Bundle params = new Bundle();
        params.putString("method", "fql.query");
        params.putString("query", "SELECT first_name,last_name,pic,locale,sex,birthday_date,current_location,hometown_location FROM user WHERE uid=me()");
        mAsyncRunner.request(params, new RequestListenerBase() {		
			@Override
			public void onComplete(String response, Object state) {
				 try {
					 response = "{\"data\":" + response + "}";
					 JSONObject json = Util.parseJson(response).getJSONArray("data").getJSONObject(0);
					 u.setName(json.getString("first_name"));
					 u.setLastname(json.getString("last_name"));
					 u.setPicuser(json.getString("pic"));
					 u.setBirthday(formatDate(json.getString("birthday_date")));
					 u.setGender(json.getString("sex"));
					 if(!json.isNull("hometown_location")){
						 u.setCity(formatLocation(json.getJSONObject("hometown_location").getString("city"),json.getJSONObject("hometown_location").getString("country")));
					 }else{
						 if(!json.isNull("current_location")){
							 u.setCity(formatLocation(json.getJSONObject("current_location").getString("city"),json.getJSONObject("current_location").getString("country")));
						 }else{
							 u.setCity(formatLocation("", json.getString("locale")));
						 }
					 }
					 iac.updateInfo(u);
				 } catch (JSONException e) {
					 e.printStackTrace();
				 } catch (FacebookError e) {
					 e.printStackTrace();
				 }						
			}
		});
	}
	public String formatDate(String birthday){
		String date = "";
		String month = "";
		String[] d = new String[3];
		d = birthday.split("/");
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
		date = d[1]+" de "+month+" de "+d[2];
		return date;
	}
	public String formatLocation(String city, String country){
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
}
