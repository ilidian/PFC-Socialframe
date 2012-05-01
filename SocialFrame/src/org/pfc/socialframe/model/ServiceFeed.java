package org.pfc.socialframe.model;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.pfc.socialframe.controller.FeedActivity;
import android.app.Activity;
import android.os.Bundle;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.SessionStore;
import com.facebook.android.Util;

public class ServiceFeed {
	private String resp;
	private String actors = "";
	private ArrayList<Feed> fds;
	private FeedActivity fa;
	public Facebook mFacebook = new Facebook(Constants.APP_ID);
	private AsyncFacebookRunner mAsyncRunner;
	public ServiceFeed(FeedActivity fa){
		this.fa=fa;
	}
	public void showFeeds(Activity a){
		mAsyncRunner = new AsyncFacebookRunner(mFacebook);
		SessionStore.restore(mFacebook, a);
		Bundle params = new Bundle();
        params.putString("method", "fql.query");
        params.putString("query", "SELECT actor_id,message,comments FROM stream WHERE source_id=me() AND (type=56 OR type=46) AND strlen(message)!=0");
        try {
			resp = mFacebook.request(params);
			resp = "{\"data\":" + resp + "}";
			JSONArray json = Util.parseJson(resp).getJSONArray("data");
			fds = new ArrayList<Feed>();
			Feed f,c;
			for(int i=0; i < json.length(); i++){
				//0 = feed | 1 = comment
				f = new Feed(0, "", "", "");
				if(!actors.contains(json.getJSONObject(i).getString("actor_id"))) actors = actors + json.getJSONObject(i).getString("actor_id")+ ",";
				f.setActor(json.getJSONObject(i).getString("actor_id"));
				f.setMsg(json.getJSONObject(i).getString("message"));
				fds.add(f);
				if(json.getJSONObject(i).getJSONObject("comments").getInt("count")!=0){
					JSONArray jsonc = json.getJSONObject(i).getJSONObject("comments").getJSONArray("comment_list");
					for(int j=0; j < jsonc.length(); j++){
						c = new Feed(1,"", "", "");
						if(!actors.contains(jsonc.getJSONObject(j).getString("fromid"))) actors = actors + jsonc.getJSONObject(j).getString("fromid")+ ",";
						c.setActor(jsonc.getJSONObject(j).getString("fromid"));
						c.setMsg(jsonc.getJSONObject(j).getString("text"));
						fds.add(c);
					}
				}
			}
			actors = actors.substring(0, actors.length()-1);
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
        params2.putString("query", "SELECT uid,name,pic_small FROM user WHERE uid IN ("+actors+")");
        mAsyncRunner.request(params2, new RequestListenerBase() {
			@Override
			public void onComplete(String response, Object state) {
				try {
					response = "{\"data\":" + response + "}";
					JSONArray json = Util.parseJson(response).getJSONArray("data");
					for(int i=0; i < json.length(); i++){
						for(int j=0; j < fds.size(); j++){
							if(json.getJSONObject(i).getString("uid").equals(fds.get(j).getActor())){
								fds.get(j).setActor(json.getJSONObject(i).getString("name"));
								fds.get(j).setPic(json.getJSONObject(i).getString("pic_small"));
							}
						}
					}
					fa.updateFeeds(fds);
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (FacebookError e) {
					e.printStackTrace();
				}	
			}
        });
	}
	public void postFeed(String msg, Activity a){
		try{
			SessionStore.restore(mFacebook, a);
			Bundle parameters = new Bundle();
		    parameters.putString("message", msg);
		    mFacebook.request("me/feed",parameters,"POST"); 
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
