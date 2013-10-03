package org.pfc.socialframe.controller;

import org.pfc.socialframe.R;
import org.pfc.socialframe.model.FacebookNetSocial;
import org.pfc.socialframe.model.SocialNetwork;
import org.pfc.socialframe.model.User;
import org.pfc.socialframe.model.Utility;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class InfoActivity extends Activity{
	private TextView tvname, tvlast, tvcity, tvbirth, tvgender;
	private ImageView ivpic;
	private ProgressDialog pd;
	private User user;
	private String owner;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.status);
        user = new User("","","","","","","");
        tvname = (TextView) findViewById(R.id.tvfbname);
        tvlast = (TextView) findViewById(R.id.tvfblastname);
        tvbirth = (TextView) findViewById(R.id.tvfbbirth);
        tvcity = (TextView) findViewById(R.id.tvfbcity);
        tvgender = (TextView) findViewById(R.id.tvfbgender);
        ivpic = (ImageView) findViewById(R.id.ivfbpicture);
        owner = "";
        if(getIntent().hasExtra("uid")){
        	Bundle b = getIntent().getExtras();
        	owner = b.getString("uid");
        }
        pd = ProgressDialog.show(InfoActivity.this, "", "Cargando información ...");
        SocialNetwork sn = new FacebookNetSocial();
        sn.showUser(InfoActivity.this, user, owner);
    }
	//Actualizar información de usuario
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
	//Volver atrás
	@Override
	public void onBackPressed(){
		super.onBackPressed();
		if(owner.equals("")){
			Intent i=new Intent(InfoActivity.this,ReaderQRActivity.class);
			startActivity(i);
		}
		finish();
	}
}
