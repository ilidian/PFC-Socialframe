package org.pfc.socialframe.model;

import org.pfc.socialframe.controller.SocialFrameActivity;

import android.app.Activity;
import android.os.Bundle;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.SessionStore;
import com.facebook.android.Facebook.DialogListener;

public class ServiceLogin {
	public Facebook mFacebook = new Facebook(Constants.APP_ID);
	private SocialFrameActivity sfa;
	public ServiceLogin(SocialFrameActivity sfa){
		this.sfa = sfa;
	}
	//Método para comprobar si un usuario está logueado
    @SuppressWarnings("static-access")
	public void facebookAvaible(final Activity sf){
    	SessionStore.restore(mFacebook, sf);
        if( !mFacebook.isSessionValid()) {
        	mFacebook.authorize(sf, new String[] {""}, mFacebook.FORCE_DIALOG_AUTH,new DialogListener() {
				@Override
				public void onFacebookError(FacebookError e) {
					e.printStackTrace();
				}		
				@Override
				public void onError(DialogError e){	
					e.printStackTrace();
				}			
				@Override
				public void onComplete(Bundle values) {
	    			SessionStore.save(mFacebook, sf);
	    			sfa.ToReaderQR();
				}		
				@Override
				public void onCancel() {		
				}
			});	
        }else sfa.ToReaderQR();
    }
}
