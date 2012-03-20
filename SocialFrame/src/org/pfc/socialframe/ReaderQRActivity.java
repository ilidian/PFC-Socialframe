package org.pfc.socialframe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class ReaderQRActivity extends Activity{
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
        startActivityForResult(intent, 0);
	}
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
	 	   if (requestCode == 0) {
	 	      if (resultCode == RESULT_OK) {
	 	         String contents = intent.getStringExtra("SCAN_RESULT");
	 	         handlerCodeQR(contents);
	 	         // Handle successful scan
	 	      } else if (resultCode == RESULT_CANCELED) {
	 	         // Handle cancel
	 	      }
	 	   }
	}
	private void handlerCodeQR(String contents) {
		Intent i=new Intent(ReaderQRActivity.this,InfoActivity.class);
		startActivity(i);
		finish();
	}
}
