package org.pfc.socialframe.controller;

import org.pfc.socialframe.R;
import org.pfc.socialframe.model.Constants;
import org.pfc.socialframe.model.FacebookNetSocial;
import org.pfc.socialframe.model.SocialNetwork;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SocialFrameActivity extends Activity {
    /** Called when the activity is first created. */
	private ProgressBar pb;
	private TextView tv;
    private Button b;
    private ImageView iv;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        pb =(ProgressBar) findViewById(R.id.Loadinit);
        tv = (TextView) findViewById(R.id.infoInit);
        b = (Button) findViewById(R.id.reCheck);
        iv = (ImageView) findViewById(R.id.inFB);
        checkInternet();
        b.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				b.setVisibility(4);
				pb.setVisibility(0);
				tv.setText("Comprobando conexión...");
				checkInternet();
			}
		});
    }
    //Comprobar si hay internet
    public void checkInternet(){
    	new Handler().postDelayed(new Runnable(){
            public void run() {
            	if(isNetworkAvaible()){
            		iv.setVisibility(0);
                    tv.setText("");
                    SocialNetwork sn = new FacebookNetSocial();
                    sn.login(SocialFrameActivity.this);
                }else{
                	tv.setText("SIN CONEXIÓN A INTERNET");
                	b.setVisibility(0);
                }
            		pb.setVisibility(4);
            }
        }, 1500);
    } 
    //Metodo para comprobar si tenemos acceso a Internet en el terminal.
    public boolean isNetworkAvaible(){
         ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
         NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
         return activeNetworkInfo != null;
    }
    //Lanzar el lector de códigos QR
    public void ToReaderQR(){
    	Bundle p = new Bundle();
    	p.putInt("from", Constants.FROMMAIN);
    	Intent i=new Intent(SocialFrameActivity.this,HelpActivity.class);
    	i.putExtras(p);
		startActivity(i);
		finish();
    }
}