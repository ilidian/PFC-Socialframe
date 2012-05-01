package org.pfc.socialframe.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.pfc.socialframe.controller.PhotoActivity;

import android.app.Activity;
import android.os.Bundle;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.SessionStore;
import com.facebook.android.Util;

public class ServicePhoto {
	private PhotoActivity pha;
	public Facebook mFacebook = new Facebook(Constants.APP_ID);
	private AsyncFacebookRunner mAsyncRunner;
	public ServicePhoto(PhotoActivity pha){
		this.pha = pha;
	}
	public void showPhotos(Activity a){
		mAsyncRunner = new AsyncFacebookRunner(mFacebook);
		SessionStore.restore(mFacebook, a);
		Bundle params = new Bundle();
        params.putString("method", "fql.query");
        params.putString("query", "SELECT src FROM photo WHERE aid IN (SELECT aid FROM album WHERE owner=me())");
		mAsyncRunner.request(params, new RequestListenerBase() {
			@Override
			public void onComplete(String response, Object state) {
				try {
					response = "{\"data\":" + response + "}";
					JSONArray json = Util.parseJson(response).getJSONArray("data");
					ArrayList<String> photolist = new ArrayList<String>();
					for(int i=0; i < json.length(); i++){
						photolist.add(json.getJSONObject(i).getString("src"));
					}
					String [] photoarray = new String[photolist.size()];
					photoarray = photolist.toArray(photoarray);
					pha.updatePhotos(photoarray);
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (FacebookError e) {
					e.printStackTrace();
				}	
			}
		});
	}
}
