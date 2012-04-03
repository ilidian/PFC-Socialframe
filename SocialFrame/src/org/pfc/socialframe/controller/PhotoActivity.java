package org.pfc.socialframe.controller;

import org.pfc.socialframe.R;
import org.pfc.socialframe.model.Constants;
import org.pfc.socialframe.model.ServicePhoto;
import org.pfc.socialframe.model.Utility;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class PhotoActivity extends Activity implements OnGestureListener{
	private int index = 0;
	private ImageView ivp;
	private ProgressDialog pd;
	private Bitmap[] photos;
	private ServicePhoto sp;
	private GestureDetector gesturedetector = null;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo);
        sp = new ServicePhoto(this);
        ivp = (ImageView) findViewById(R.id.ivphoto);
        gesturedetector = new GestureDetector(this, this);
        pd = ProgressDialog.show(PhotoActivity.this, "", "Cargando fotos ...");
        sp.showPhotos(PhotoActivity.this);
    }
	public void updatePhotos(String[] l){
		photos = new Bitmap[l.length];
		for(int i = 0; i< l.length; i++) photos[i]= Utility.getBitmap(l[i]);
		PhotoActivity.this.runOnUiThread(new Runnable() {
			@Override
			public void run(){
				ivp.setImageBitmap(photos[index]);
				pd.dismiss();
			}
		});
	}
	@Override
	public void onBackPressed(){
		super.onBackPressed();
		Intent i=new Intent(PhotoActivity.this,ReaderQRActivity.class);
		startActivity(i);
		finish();
	}
	@Override
    public boolean onTouchEvent(MotionEvent event) {
            return gesturedetector.onTouchEvent(event);
    }
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
