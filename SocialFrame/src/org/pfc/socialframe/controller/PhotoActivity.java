package org.pfc.socialframe.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.pfc.socialframe.R;
import org.pfc.socialframe.model.Constants;
import org.pfc.socialframe.model.FacebookNetSocial;
import org.pfc.socialframe.model.SocialNetwork;
import org.pfc.socialframe.model.Utility;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.View;
import android.view.Window;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;

public class PhotoActivity extends Activity implements OnGestureListener{
	private int index = 0;
	private String owner;
	private ScaleImageView ivp;
	private ProgressDialog pd;
	private Bitmap[] photos;
	private Button bup;
	private GestureDetector gesturedetector = null;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.photo);
        ivp = (ScaleImageView) findViewById(R.id.ivphoto);
        bup = (Button) findViewById(R.id.btnphotoup);
        bup.getBackground().setColorFilter(0xAA00FFFF, PorterDuff.Mode.MULTIPLY);
        final Dialog d = new Dialog(this);
    	d.setContentView(R.layout.dialogfriend);
        gesturedetector = new GestureDetector(this, this);
        owner = "";
        if(getIntent().hasExtra("uid")){
        	Bundle b = getIntent().getExtras();
        	owner = b.getString("uid");
        	bup.setVisibility(4);
        }
        pd = ProgressDialog.show(PhotoActivity.this, "", "Cargando fotos ...");
        SocialNetwork sn = new FacebookNetSocial();
        sn.showPhotos(PhotoActivity.this, owner);
        //Opciones de subir foto
        bup.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				d.setContentView(R.layout.dialogphoto);
				d.setTitle("Subir foto a Facebook!!!");
				ImageButton ibc = (ImageButton) d.findViewById(R.id.imgbcam);
		    	ImageButton ibg = (ImageButton) d.findViewById(R.id.imgbgallery);
		    	//Subir foto a través de la cámara
		    	ibc.setOnClickListener(new OnClickListener(){
					public void onClick(View arg0) {
						Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
		       			startActivityForResult(intent, Constants.TAKE_PICTURE);
						d.dismiss();
					}	
		    	});
		    	//Subir foto a través de la galería
		    	ibg.setOnClickListener(new OnClickListener(){
					public void onClick(View arg0) {
						Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
	       				startActivityForResult(intent, Constants.CHOOSE_PICTURE);
						d.dismiss();
					}	
		    	});
		    	d.show();
			}
		});
    }
	//Foto subida
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Bundle p = new Bundle();
		if(requestCode == Constants.CHOOSE_PICTURE){
			Uri photoUri = data.getData();
			if (photoUri != null) {
				try {
					p.putByteArray("photo", Utility.scaleImage(getApplicationContext(), photoUri));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if(requestCode == Constants.TAKE_PICTURE){
			Bitmap bmp = (Bitmap) data.getExtras().get("data");
	        ByteArrayOutputStream stream = new ByteArrayOutputStream();
	        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
	        byte[] byteArray = stream.toByteArray();
	        p.putByteArray("photo", byteArray);
		}
        SocialNetwork s = new FacebookNetSocial();
        s.uploadPhoto(PhotoActivity.this, p);
	}
	//Volver atrás
	@Override
	public void onBackPressed(){
		super.onBackPressed();
		if(owner.equals("")){
			Intent i=new Intent(PhotoActivity.this,ReaderQRActivity.class);
			startActivity(i);
		}
		finish();
	}
	//Actualizar fotos
	public void updatePhotos(String[] l){
		if(l!= null){
			photos = new Bitmap[l.length];
			for(int i = 0; i< l.length; i++) photos[i]= Utility.getBitmap(l[i]);
			PhotoActivity.this.runOnUiThread(new Runnable() {
				@Override
				public void run(){
					ivp.setImageBitmap(photos[index]);
					pd.dismiss();
				}
			});
		}else{
			pd.dismiss();
			finish();
		}
	}
	//Evento de tocar marco
	@Override
    public boolean onTouchEvent(MotionEvent event) {
            return gesturedetector.onTouchEvent(event);
    }
	//Evento para arrastar la foto siguiente o anterior
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,float velocityY) {
		float ev1X = e1.getX();
		float ev2X = e2.getX();
		final float xdistance = Math.abs(ev1X - ev2X);
        final float xvelocity = Math.abs(velocityX);
        if( (xvelocity > Constants.SWIPE_MIN_VELOCITY) && (xdistance > Constants.SWIPE_MIN_DISTANCE) ){
			if(ev1X > ev2X){  //hacia la izquierda
				index++;
				if (index >= photos.length) {
					index = 0;
				}
				ivp.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_right));
				ivp.setImageBitmap(photos[index]);
			}
			else{
				index--;
				if (index < 0) {  //hacia la derecha
					index = photos.length-1;
				}
				ivp.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.slide_in_left));
				ivp.setImageBitmap(photos[index]);
			}
        }
        
		return false;
	}
	@Override
	public boolean onDown(MotionEvent arg0) {
		return false;
	}
	@Override
	public void onLongPress(MotionEvent e) {
	}
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}
	@Override
	public void onShowPress(MotionEvent e) {
	}
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}
}
