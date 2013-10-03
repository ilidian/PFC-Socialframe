package org.pfc.socialframe.model;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.pfc.socialframe.controller.EventsActivity;
import org.pfc.socialframe.controller.FeedActivity;
import org.pfc.socialframe.controller.FriendsActivity;
import org.pfc.socialframe.controller.InfoActivity;
import org.pfc.socialframe.controller.MessagesActivity;
import org.pfc.socialframe.controller.PhotoActivity;
import org.pfc.socialframe.controller.SocialFrameActivity;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.SessionStore;
import com.facebook.android.Util;
import com.facebook.android.Facebook.DialogListener;

import android.app.Activity;
import android.os.Bundle;

public class FacebookNetSocial extends SocialNetwork{
	private boolean skip;
	private String actors = "";
	private ArrayList<Feed> fds;
	private String [][] corresp;
	private String today;
	private Date today_date;
	private String [][] births;
	public Facebook mFacebook = new Facebook(Constants.APP_ID);
	private AsyncFacebookRunner mAsyncRunner = new AsyncFacebookRunner(mFacebook);
	public FacebookNetSocial(){
		super();
	}
	//Login de un usario
	public void login(SocialFrameActivity ac) {
		final SocialFrameActivity sfa = ac;
		SessionStore.restore(mFacebook, sfa);
        if( !mFacebook.isSessionValid()) {
        	mFacebook.authorize(sfa, Constants.perms, Facebook.FORCE_DIALOG_AUTH,new DialogListener() {
				@Override
				public void onFacebookError(FacebookError e) {
					e.printStackTrace();
				}		
				@Override
				public void onError(DialogError e){	
					e.printStackTrace();
				}			
				@Override
				public void onComplete(Bundle values) {
	    			SessionStore.save(mFacebook, sfa);
	    			sfa.ToReaderQR();
				}		
				@Override
				public void onCancel() {		
				}
			});	
        }else sfa.ToReaderQR();
	}
	
	//Mostrar la información del usuario o de los amigos
	public void showUser(InfoActivity ia, final User u, String owner){
		final InfoActivity infa = ia;
		SessionStore.restore(mFacebook, infa);
		Bundle params = new Bundle();
        params.putString("method", "fql.query");
        if(owner.equals("")){
            params.putString("query", "SELECT first_name,last_name,pic,locale,sex,birthday_date,current_location,hometown_location FROM user WHERE uid=me()");
        }else{
            params.putString("query", "SELECT first_name,last_name,pic,locale,sex,birthday_date,current_location,hometown_location FROM user WHERE uid="+owner);
        }
        mAsyncRunner.request(params, new RequestListenerBase() {		
			@Override
			public void onComplete(String response, Object state) {
				 try {
					 response = "{\"data\":" + response + "}";
					 JSONObject json = Util.parseJson(response).getJSONArray("data").getJSONObject(0);
					 u.setName(json.getString("first_name"));
					 u.setLastname(json.getString("last_name"));
					 u.setPicuser(json.getString("pic"));
					 if(!json.isNull("birthday_date")) u.setBirthday(UtilityFacebookNet.formatDate(json.getString("birthday_date")));
					 else u.setBirthday(" ");
					 u.setGender(json.getString("sex"));
					 if(!json.isNull("hometown_location")){
						 u.setCity(UtilityFacebookNet.formatLocation(json.getJSONObject("hometown_location").getString("city"),json.getJSONObject("hometown_location").getString("country")));
					 }else{
						 if(!json.isNull("current_location")){
							 u.setCity(UtilityFacebookNet.formatLocation(json.getJSONObject("current_location").getString("city"),json.getJSONObject("current_location").getString("country")));
						 }else{
							 u.setCity(UtilityFacebookNet.formatLocation("", json.getString("locale")));
						 }
					 }
					 infa.updateInfo(u);
				 } catch (JSONException e) {
					 e.printStackTrace();
				 } catch (FacebookError e) {
					 e.printStackTrace();
				 }						
			}
		});
	}
	
	//Muestra las peticiones de amistad
	public void showFriendRequest(FriendsActivity ac) {
		final FriendsActivity fa = ac;
		SessionStore.restore(mFacebook, fa);
        mAsyncRunner.request("me/friendrequests", new RequestListenerBase() {		
			@Override
			public void onComplete(String response, Object state) {
				 try {
					 String uid= "";
					 JSONArray json = Util.parseJson(response).getJSONArray("data");
					 if(json.length()!= 0){
						 uid = json.getJSONObject(0).getJSONObject("from").getString("id");
					 }
					 fa.updateFriendsRequests(uid);
				 } catch (JSONException e) {
					 e.printStackTrace();
				 } catch (FacebookError e) {
					 e.printStackTrace();
				 }						
			}
		});
	}
	
	//Muestra la lista de amigos
	public void showFriends(FriendsActivity ac) {
		final FriendsActivity fa = ac;
		SessionStore.restore(mFacebook, fa);
		Bundle params = new Bundle();
        params.putString("method", "fql.query");
        params.putString("query", "SELECT uid,name,pic FROM user WHERE uid IN (SELECT uid2 FROM friend WHERE uid1= me())");
        mAsyncRunner.request(params, new RequestListenerBase() {		
			@Override
			public void onComplete(String response, Object state) {
				 try {
					 response = "{\"data\":" + response + "}";
					 JSONArray json = Util.parseJson(response).getJSONArray("data");
					 ArrayList<User> friendlist = new ArrayList<User>();
					 User f ;
					 for(int i=0; i < json.length(); i++){
						 f = new User("", "", "", "", "", "","");
						 f.setUid(json.getJSONObject(i).getString("uid"));
						 f.setName(json.getJSONObject(i).getString("name"));
						 f.setPicuser(json.getJSONObject(i).getString("pic"));
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
	
	//Aceptar la peticion de amistad
	public void acceptFriend(FriendsActivity ac, String uid) {
		final FriendsActivity fa = ac;
		SessionStore.restore(mFacebook, fa);
		Bundle params = new Bundle();
		params.putString("id", uid);
	    mFacebook.dialog(fa, "friends", params, new DialogListener(){
	        @Override
	        public void onComplete(Bundle values) {
	        	fa.refresh();
	        }
	        @Override
	        public void onFacebookError(FacebookError e) {
	        	e.printStackTrace();
	        }
	        @Override
	        public void onCancel() {
	        }
			@Override
			public void onError(DialogError e) {
				e.printStackTrace();
			}});
	}
	
	//Muestra los feeds del muro propio o de algún amigo
	public void showFeeds(FeedActivity ac, String owner) {
		final FeedActivity fa = ac;
		String resp;
		SessionStore.restore(mFacebook, fa);
		Bundle params = new Bundle();
        params.putString("method", "fql.query");
        if (owner.equals("")){
            params.putString("query", "SELECT actor_id,message,comments FROM stream WHERE source_id=me() AND (type=56 OR type=46) AND strlen(message)!=0 LIMIT 30");
        }else{
            params.putString("query", "SELECT actor_id,message,comments FROM stream WHERE source_id="+owner+" AND (type=56 OR type=46) AND strlen(message)!=0 LIMIT 30");
        }
        try {
			resp = mFacebook.request(params);
			resp = "{\"data\":" + resp + "}";
			JSONArray json = Util.parseJson(resp).getJSONArray("data");
			fds = new ArrayList<Feed>();
			Feed f,c;
			if(json.length()!=0){
				for(int i=0; i < json.length(); i++){
					//0 = feed | 1 = comment
					f = new Feed(0, "", "", "");
					if(!actors.contains(json.getJSONObject(i).getString("actor_id"))) actors = actors + json.getJSONObject(i).getString("actor_id")+ ",";
					f.setActor(json.getJSONObject(i).getString("actor_id"));
					f.setMsg(json.getJSONObject(i).getString("message"));
					if(!f.getMsg().startsWith("http")){
						fds.add(f);
						skip = false;
					}
					else skip = true;
					if(json.getJSONObject(i).getJSONObject("comments").getInt("count")!=0){
						JSONArray jsonc = json.getJSONObject(i).getJSONObject("comments").getJSONArray("comment_list");
						for(int j=0; j < jsonc.length(); j++){
							c = new Feed(1,"", "", "");
							if(!actors.contains(jsonc.getJSONObject(j).getString("fromid"))) actors = actors + jsonc.getJSONObject(j).getString("fromid")+ ",";
							c.setActor(jsonc.getJSONObject(j).getString("fromid"));
							c.setMsg(jsonc.getJSONObject(j).getString("text"));
							if(!c.getMsg().startsWith("http")){
								fds.add(c);
								skip = false;
							}
							else skip = true;
						}
					}
				}
				actors = actors.substring(0, actors.length()-1);
			}else{
				skip = true;
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
		if(!skip){
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
		}else fa.updateFeeds(null);
	}

	//Sube un feed a tu muro propio o de algún amigo
	public void postFeed(Activity a, String msg, String owner) {
		SessionStore.restore(mFacebook, a);
		try{
			Bundle parameters = new Bundle();
			if (owner.equals("")) owner = "me";
		    parameters.putString("message", msg);
		    mFacebook.request(owner+"/feed",parameters,"POST");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	//Muestra los mensajes privados propios
	public void showMessages(MessagesActivity ac) {
		final MessagesActivity ma = ac;
		String resp;
		SessionStore.restore(mFacebook, ma);
		Bundle params = new Bundle();
        params.putString("method", "fql.query");
        params.putString("query", "SELECT uid,name FROM user WHERE uid IN (SELECT uid2 FROM friend WHERE uid1= me())");
        try {
			resp = mFacebook.request(params);
			resp = "{\"data\":" + resp + "}";
			JSONArray json = Util.parseJson(resp).getJSONArray("data");
			corresp = new String [2][json.length()];
			for(int i=0; i < json.length(); i++){
				corresp[0][i] = json.getJSONObject(i).getString("uid");
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
						m = new Message("","", "", "");
						m.setUid(json.getJSONObject(i).getString("author_id"));
						m.setDate(String.valueOf(json.getJSONObject(i).getInt("created_time")));
						m.setSender(UtilityFacebookNet.getNameatUid(corresp, json.getJSONObject(i).getString("author_id")));
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
		});
	}

	//Muestra los cumpleaños y eventos del dia en el que estas
	public void showEvents(EventsActivity ac) {
		final EventsActivity ea = ac;
		String resp;
		SessionStore.restore(mFacebook, ea);
		today_date = new Date();
		today = UtilityFacebookNet.changeFormatDate(today_date);
		Bundle params = new Bundle();
        params.putString("method", "fql.query");
        params.putString("query", "SELECT uid,name,birthday_date FROM user WHERE uid IN (SELECT uid2 FROM friend WHERE uid1= me())");
        try {
			resp = mFacebook.request(params);
			resp = "{\"data\":" + resp + "}";
			JSONArray json = Util.parseJson(resp).getJSONArray("data");
			births = new String [3][json.length()];
			for(int i=0; i < json.length(); i++){
				births[0][i] = json.getJSONObject(i).getString("uid");
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
							e.setCreator(UtilityFacebookNet.uidToName(births, String.valueOf(json.getJSONObject(i).getInt("creator"))));
							e.setDescription(json.getJSONObject(i).getString("description"));
							e.setDate(String.valueOf(json.getJSONObject(i).getInt("start_time")));
							e.setLocation(json.getJSONObject(i).getString("location"));
							if(e.getDate().equals(today)) eventlist.add(e);
						}
					}
					String [] t = today.split("/");
					ea.updateEvents(UtilityFacebookNet.filterBirths(births,today), eventlist, t[1], UtilityFacebookNet.monthToText(t[0]));
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (FacebookError e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	//Muestra las fotos propias o las de un amigo
	public void showPhotos(PhotoActivity ac, String owner) {
		final PhotoActivity pa = ac;
		SessionStore.restore(mFacebook, pa);
		Bundle params = new Bundle();
        params.putString("method", "fql.query");
        if(owner.equals("")){
            params.putString("query", "SELECT src FROM photo WHERE aid IN (SELECT aid FROM album WHERE owner=me())");
        }else{
            params.putString("query", "SELECT src FROM photo WHERE aid IN (SELECT aid FROM album WHERE owner="+owner+")");
        }
		mAsyncRunner.request(params, new RequestListenerBase() {
			@Override
			public void onComplete(String response, Object state) {
				try {
					response = "{\"data\":" + response + "}";
					JSONArray json = Util.parseJson(response).getJSONArray("data");
					ArrayList<String> photolist = new ArrayList<String>();
					String [] photoarray = null;
					if(json.length()!=0){
						for(int i=0; i < json.length(); i++){
							photolist.add(json.getJSONObject(i).getString("src"));
						}
						photoarray = new String[photolist.size()];
						photoarray = photolist.toArray(photoarray);
					}
					pa.updatePhotos(photoarray);
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (FacebookError e) {
					e.printStackTrace();
				}	
			}
		});
	}
	
	//Sube una foto
	public void uploadPhoto(PhotoActivity ac, Bundle b) {
		try{
			SessionStore.restore(mFacebook, ac);
		    mFacebook.request("me/photos",b,"POST"); 
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}	
}
