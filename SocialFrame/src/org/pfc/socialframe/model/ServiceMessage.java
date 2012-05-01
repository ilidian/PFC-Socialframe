package org.pfc.socialframe.model;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.pfc.socialframe.controller.MessagesActivity;

import android.app.Activity;
import android.os.Bundle;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.SessionStore;
import com.facebook.android.Util;

public class ServiceMessage {
	private String resp;
	private String [][] corresp;
	private MessagesActivity ma;
	public Facebook mFacebook = new Facebook(Constants.APP_ID);
	private AsyncFacebookRunner mAsyncRunner;
	public ServiceMessage(MessagesActivity ma){
		this.ma = ma;
	}
	public void showMessages(Activity a){
		mAsyncRunner = new AsyncFacebookRunner(mFacebook);
		SessionStore.restore(mFacebook, a);
		Bundle params = new Bundle();
        params.putString("method", "fql.query");
        params.putString("query", "SELECT uid,name FROM user WHERE uid IN (SELECT uid2 FROM friend WHERE uid1= me())");
        try {
			resp = mFacebook.request(params);
			resp = "{\"data\":" + resp + "}";
			JSONArray json = Util.parseJson(resp).getJSONArray("data");
			corresp = new String [2][json.length()];
			for(int i=0; i < json.length(); i++){
				corresp[0][i] = String.valueOf(json.getJSONObject(i).getInt("uid"));
				corresp[1][i] = json.getJSONObject(i).getString("name");
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
        params2.putString("query", "SELECT author_id,body,created_time FROM message WHERE author_id != me() AND thread_id IN (SELECT thread_id FROM thread WHERE folder_id = 0) ORDER BY created_time DESC");
		mAsyncRunner.request(params2, new RequestListenerBase() {
			@Override
			public void onComplete(String response, Object state) {
				try {
					response = "{\"data\":" + response + "}";
					JSONArray json = Util.parseJson(response).getJSONArray("data");
					ArrayList<Message> messagelist = new ArrayList<Message>();
					Message m ;
					for(int i=0; i < json.length(); i++){
						m = new Message("", "", "");
						m.setDate(String.valueOf(json.getJSONObject(i).getInt("created_time")));
						m.setSender(getNameatUid(corresp, String.valueOf(json.getJSONObject(i).getInt("author_id"))));
						m.setMsg(json.getJSONObject(i).getString("body"));
						messagelist.add(m);
					}
					ma.updateMessages(messagelist);
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (FacebookError e) {
					e.printStackTrace();
				}	
			}
			private String getNameatUid(String[][] c, String uid) {
				String name = "";
				for(int i = 0; i < c[0].length;i++){
					if(c[0][i].equals(uid)) name = c[1][i]+":";
				}
				return name;
			}
		});
	}
}
