package org.pfc.socialframe.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.pfc.socialframe.controller.FriendsActivity;

import android.app.Activity;
import android.os.Bundle;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.SessionStore;
import com.facebook.android.Util;

public class ServiceFriends {
	private FriendsActivity fa;
	public Facebook mFacebook = new Facebook(Constants.APP_ID);
	private AsyncFacebookRunner mAsyncRunner;
	public ServiceFriends(FriendsActivity fa){
		this.fa = fa;
	}
	public void showFriends(Activity a){
		mAsyncRunner = new AsyncFacebookRunner(mFacebook);
		SessionStore.restore(mFacebook, a);
		Bundle params = new Bundle();
        params.putString("method", "fql.query");
        params.putString("query", "SELECT name,pic_small FROM user WHERE uid IN (SELECT uid2 FROM friend WHERE uid1= me())");
		mAsyncRunner.request(params, new RequestListenerBase() {
			@Override
			public void onComplete(String response, Object state) {
				try {
					response = "{\"data\":" + response + "}";
					JSONArray json = Util.parseJson(response).getJSONArray("data");
					ArrayList<User> friendlist = new ArrayList<User>();
					User f ;
					for(int i=0; i < json.length(); i++){
						f = new User("", "", "", "", "", "");
						f.setName(json.getJSONObject(i).getString("name"));
						f.setPicuser(json.getJSONObject(i).getString("pic_small"));
						friendlist.add(f);
					}
					fa.updateFriends(friendlist);
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (FacebookError e) {
					e.printStackTrace();
				}	
			}
		});
	}
}
