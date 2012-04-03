package org.pfc.socialframe.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.pfc.socialframe.controller.InfoActivity;

import android.app.Activity;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.SessionStore;
import com.facebook.android.Util;
import com.facebook.android.AsyncFacebookRunner.RequestListener;

public class ServiceUser {
	private String picid, url, n, resp;
	private InfoActivity iac;
	public Facebook mFacebook = new Facebook(Constants.APP_ID);
	private AsyncFacebookRunner mAsyncRunner;
	public ServiceUser(InfoActivity iac){
		this.iac = iac;
	}
	public void showUser(Activity ia, final User u){
		mAsyncRunner = new AsyncFacebookRunner(mFacebook);
		SessionStore.restore(mFacebook, ia);
        try {
			resp = mFacebook.request("me/albums");
			JSONArray jarray = Util.parseJson(resp).getJSONArray("data");
			JSONObject json;
			for(int i=0; i<jarray.length();i++){
				json = jarray.getJSONObject(i);
				n = json.getString("name");
				if (n.equals("Profile Pictures")) picid = json.getString("cover_photo");
			}
			resp = mFacebook.request(picid);
			json = Util.parseJson(resp);
			url = json.getString("picture");
			u.setPicuser(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (FacebookError e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
        mAsyncRunner.request("me", new RequestListener() {
			@Override
			public void onMalformedURLException(MalformedURLException e, Object state) {}
			@Override
			public void onIOException(IOException e, Object state) {}	
			@Override
			public void onFileNotFoundException(FileNotFoundException e, Object state) {}			
			@Override
			public void onFacebookError(FacebookError e, Object state) {}		
			@Override
			public void onComplete(String response, Object state) {
				 try {
						JSONObject json = Util.parseJson(response);
						JSONObject jsonc = Util.parseJson(response).getJSONObject("location");
						u.setName(json.getString("first_name"));
						u.setLastname(json.getString("last_name"));
						u.setBirthday(formatDate(json.getString("birthday")));
						u.setGender(json.getString("gender"));
						u.setCity(formatLocation(jsonc.getString("name"),json.getString("locale")));
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
		String location = "";
		String[] cnt = country.split("_");
		for(int i = 0; i < Constants.Countries[0].length; i++){
			if(cnt[1].equals(Constants.Countries[0][i])) location = Constants.Countries[1][i];
		}
		return city + ", "+ location;
	}
}
