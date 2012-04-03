package org.pfc.socialframe.controller;

import org.pfc.socialframe.R;
import org.pfc.socialframe.model.ServiceUser;
import org.pfc.socialframe.model.User;
import org.pfc.socialframe.model.Utility;

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
	private ServiceUser su;
	private User user;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.status);
        su = new ServiceUser(this);
        user = new User("","","","","","");
        tvname = (TextView) findViewById(R.id.tvfbname);
        tvlast = (TextView) findViewById(R.id.tvfblastname);
        tvbirth = (TextView) findViewById(R.id.tvfbbirth);
        tvcity = (TextView) findViewById(R.id.tvfbcity);
        tvgender = (TextView) findViewById(R.id.tvfbgender);
        ivpic = (ImageView) findViewById(R.id.ivfbpicture);
        pd = ProgressDialog.show(InfoActivity.this, "", "Cargando información ...");
        su.showUser(InfoActivity.this,user);
    }
	public void updateInfo(final User u){
      InfoActivity.this.runOnUiThread(new Runnable() {		
		@Override
		public void run() {
			ivpic.setImageBitmap(Utility.getBitmap(u.getPicuser()));
			tvname.setText(u.getName());
			tvlast.setText(u.getLastname());
			tvbirth.setText(u.getBirthday());
			tvcity.setText(u.getCity());
			if(u.getGender().equals("male")) tvgender.setText("Hombre");
			else tvgender.setText("Mujer");
			pd.dismiss();
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
