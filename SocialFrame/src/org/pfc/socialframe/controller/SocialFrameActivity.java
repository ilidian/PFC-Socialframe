package org.pfc.socialframe.controller;

import org.pfc.socialframe.R;
import org.pfc.socialframe.model.ServiceLogin;

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
	private ProgressBar pb;
	private TextView tv;
    private Button b;
    private ImageView iv;
    private ServiceLogin sl;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        sl = new ServiceLogin(this);
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
    public void checkInternet(){
    	new Handler().postDelayed(new Runnable(){
            public void run() {
            	if(isNetworkAvaible()){
            		iv.setVisibility(0);
                    tv.setText("");
                    sl.facebookAvaible(SocialFrameActivity.this);
                }else{
                	tv.setText("SIN CONEXIÓN");
                	b.setVisibility(0);
//                    sl.facebookAvaible(SocialFrameActivity.this);
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
    public void ToReaderQR(){
    	Intent i=new Intent(SocialFrameActivity.this,ReaderQRActivity.class);
		startActivity(i); 
		finish();
    }
}