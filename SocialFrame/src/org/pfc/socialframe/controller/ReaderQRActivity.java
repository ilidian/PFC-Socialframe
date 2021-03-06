package org.pfc.socialframe.controller;


import org.pfc.socialframe.R;
import org.pfc.socialframe.model.Constants;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

public class ReaderQRActivity extends Activity{
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
        startActivityForResult(intent, 0);
        setContentView(R.layout.credits);
	}
	//Volver atr�s
	@Override
	public void onBackPressed(){
		super.onBackPressed();
		finish();
	}
	//C�digo qr capturado
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
	 	   if (requestCode == 0) {
	 	      if (resultCode == RESULT_OK) {
	 	         String contents = intent.getStringExtra("SCAN_RESULT");
	 	         handlerCodeQR(contents.toLowerCase());
	 	      } else if (resultCode == RESULT_CANCELED) {
	 	      }
	 	   }
	}
	@SuppressWarnings("rawtypes")
	private void handlerCodeQR(String contents) {
		Class c = null;
		Bundle b = new Bundle();
		if(contents.equals(Constants.InfoQR[0])||contents.equals(Constants.InfoQR[1])||contents.equals(Constants.InfoQR[2])) c = InfoActivity.class;
		if(contents.equals(Constants.FriendsQR[0])||contents.equals(Constants.FriendsQR[1])||contents.equals(Constants.FriendsQR[2])) c = FriendsActivity.class;
		if(contents.equals(Constants.PhotosQR[0])||contents.equals(Constants.PhotosQR[1])||contents.equals(Constants.PhotosQR[2])){
			c = HelpActivity.class;
			b.putInt("from", Constants.FROMREADER);
		}
		if(contents.equals(Constants.MessagesQR[0])||contents.equals(Constants.MessagesQR[1])||contents.equals(Constants.MessagesQR[2])) c = MessagesActivity.class;
		if(contents.equals(Constants.FeedQR[0])||contents.equals(Constants.FeedQR[1])||contents.equals(Constants.FeedQR[2])) c = FeedActivity.class;
		if(contents.equals(Constants.EventsQR[0])||contents.equals(Constants.EventsQR[1])||contents.equals(Constants.EventsQR[2])) c = EventsActivity.class;
		Intent i=new Intent(ReaderQRActivity.this,c);
		i.putExtras(b);
		startActivity(i); 
		finish();
	}
}
