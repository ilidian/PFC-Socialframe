package org.pfc.socialframe.controller;

import org.pfc.socialframe.R;
import org.pfc.socialframe.model.Constants;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class HelpActivity extends Activity{
	private ImageButton im;
	private int from;
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Bundle b = getIntent().getExtras();
    	from = b.getInt("from");
    	if(from == Constants.FROMMAIN) setContentView(R.layout.helpqr);
    	if(from == Constants.FROMREADER) setContentView(R.layout.helpphoto);
        im = (ImageButton) findViewById(R.id.ibhelpqr);
        //Pulsar sobre la imagen para continuar
        im.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = null;
				if(from == Constants.FROMMAIN) i=new Intent(HelpActivity.this,ReaderQRActivity.class);
				if(from == Constants.FROMREADER) i=new Intent(HelpActivity.this,PhotoActivity.class);
				startActivity(i); 
				finish();
			}
		});
	}
}
