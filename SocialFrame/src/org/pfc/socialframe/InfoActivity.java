package org.pfc.socialframe;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.SessionStore;
import com.facebook.android.Util;
import com.facebook.android.AsyncFacebookRunner.RequestListener;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class InfoActivity extends Activity{
	private TextView tvname, tvlast, tvcity, tvbirth, tvgender;
	private ImageView ivpic;
	private ProgressDialog pd;
	private String picid, url, n, resp;
	public static final String APP_ID = "227586584004016";
	public Facebook mFacebook = new Facebook(APP_ID);
	private AsyncFacebookRunner mAsyncRunner;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.status);
        tvname = (TextView) findViewById(R.id.tvfbname);
        tvlast = (TextView) findViewById(R.id.tvfblastname);
        tvbirth = (TextView) findViewById(R.id.tvfbbirth);
        tvcity = (TextView) findViewById(R.id.tvfbcity);
        tvgender = (TextView) findViewById(R.id.tvfbgender);
        ivpic = (ImageView) findViewById(R.id.ivfbpicture);
        mAsyncRunner = new AsyncFacebookRunner(mFacebook);
        SessionStore.restore(mFacebook, this);
        pd = ProgressDialog.show(InfoActivity.this, "", "Cargando información ...");
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
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FacebookError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
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
						final String name = json.getString("first_name");
						final String last = json.getString("last_name");
						final String birth = json.getString("birthday");
						final String gender = json.getString("gender");
						final String city = jsonc.getString("name");
						InfoActivity.this.runOnUiThread(new Runnable() {		
							@Override
							public void run() {
								ivpic.setImageBitmap(Utility.getBitmap(url));
								tvname.setText(name);
								tvlast.setText(last);
								tvbirth.setText(birth);
								tvcity.setText(city);
								if(gender.equals("male")) tvgender.setText("Hombre");
								else tvgender.setText("Mujer");
								pd.dismiss();
							}
						});
					} catch (JSONException e) {
						e.printStackTrace();
					} catch (FacebookError e) {
						e.printStackTrace();
					}						
			}
		});
    }
	@Override
	public void onBackPressed(){
		super.onBackPressed();
		Intent i=new Intent(InfoActivity.this,ReaderQRActivity.class);
		startActivity(i);
		finish();
	}
}
