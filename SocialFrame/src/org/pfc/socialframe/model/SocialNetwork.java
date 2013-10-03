package org.pfc.socialframe.model;

import org.pfc.socialframe.controller.EventsActivity;
import org.pfc.socialframe.controller.FeedActivity;
import org.pfc.socialframe.controller.FriendsActivity;
import org.pfc.socialframe.controller.InfoActivity;
import org.pfc.socialframe.controller.MessagesActivity;
import org.pfc.socialframe.controller.PhotoActivity;
import org.pfc.socialframe.controller.SocialFrameActivity;

import android.app.Activity;
import android.os.Bundle;

public abstract class SocialNetwork {
		
	abstract public void login(SocialFrameActivity ac);
	
	abstract public void showUser(InfoActivity ac, final User u, String owner);
	
	abstract public void showFriendRequest(FriendsActivity ac);
	
	abstract public void showFriends(FriendsActivity ac);
	
	abstract public void acceptFriend(FriendsActivity ac, String uid);
	
	abstract public void showFeeds(FeedActivity ac, String owner);
	
	abstract public void postFeed(Activity a, String msg, String owner);
	
	abstract public void showMessages(MessagesActivity ac);
	
	abstract public void showEvents(EventsActivity ac);
	
	abstract public void showPhotos(PhotoActivity ac, String owner);
	
	abstract public void uploadPhoto(PhotoActivity ac, Bundle b);
}
