package org.pfc.socialframe;

import org.pfc.socialframe.R;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.SessionStore;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SocialFrameActivity extends Activity {
    /** Called when the activity is first created. */
	public static final String APP_ID = "227586584004016";
	public Facebook mFacebook = new Facebook(APP_ID);
	private AsyncFacebookRunner mAsyncRunner;
	private ProgressBar pb;
	private TextView tv;
    private Button b;
    private ImageView iv;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mAsyncRunner = new AsyncFacebookRunner(mFacebook); 
        pb =(ProgressBar) findViewById(R.id.Loadinit);
        tv = (TextView) findViewById(R.id.infoInit);
        b = (Button) findViewById(R.id.reCheck);
        iv = (ImageView) findViewById(R.id.inFB);
        checkInternet();
        b.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				b.setVisibility(4);
				pb.setVisibility(0);
				tv.setText("Comprobando conexión...");
				checkInternet();
			}
		});
    }
  //Metodo para comprobar si tenemos acceso a Internet en el terminal.
    public boolean isNetworkAvaible(){
         ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
         NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
         return activeNetworkInfo != null;
    }
    public void facebookAvaible(){
    	SessionStore.restore(mFacebook, this);
        if( !mFacebook.isSessionValid()) {
        	mFacebook.authorize(SocialFrameActivity.this, new String[] { "" },mFacebook.FORCE_DIALOG_AUTH,new DialogListener() {
				@Override
				public void onFacebookError(FacebookError e) {
					// TODO Auto-generated method stub				
				}		
				@Override
				public void onError(DialogError e) {
					// TODO Auto-generated method stub				
				}			
				@Override
				public void onComplete(Bundle values) {
					// TODO Auto-generated method stub
	    			SessionStore.save(mFacebook, SocialFrameActivity.this);
	    			Intent i=new Intent(SocialFrameActivity.this,ReaderQRActivity.class);
    				startActivity(i);
				}		
				@Override
				public void onCancel() {
					// TODO Auto-generated method stub		
				}
			});	
        }else{
        	Intent i=new Intent(SocialFrameActivity.this,ReaderQRActivity.class);
			startActivity(i);
        }
    }
    public void checkInternet(){
    	new Handler().postDelayed(new Runnable(){
            public void run() {
            	if(isNetworkAvaible()){
            		iv.setVisibility(0);
                    tv.setText("");
                    facebookAvaible();
                    finish();
                }else{
                	tv.setText("SIN CONEXIÓN");
                	b.setVisibility(0);
                }
            		pb.setVisibility(4);
            }
        }, 1500);
    } 
}