package org.pfc.socialframe.controller;

import java.util.ArrayList;

import org.pfc.socialframe.R;
import org.pfc.socialframe.model.FacebookNetSocial;
import org.pfc.socialframe.model.SocialNetwork;
import org.pfc.socialframe.model.User;
import org.pfc.socialframe.model.Utility;

import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class FriendsActivity extends ListActivity {
	private ArrayList<User> friends = null;
	private ProgressDialog pd;
    private FriendListViewAdapter adapter;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.list);
        friends = new ArrayList<User>();
        this.adapter = new FriendListViewAdapter(this, R.layout.friendrow, friends);
        setListAdapter(this.adapter); 
        pd = ProgressDialog.show(FriendsActivity.this, "", "Cargando amigos ...");
        SocialNetwork sn = new FacebookNetSocial();
        sn.showFriendRequest(FriendsActivity.this);
        sn.showFriends(FriendsActivity.this);
    }
    //Volver atrás
    @Override
	public void onBackPressed(){
		super.onBackPressed();
		Intent i=new Intent(FriendsActivity.this,ReaderQRActivity.class);
		startActivity(i);
		finish();
	}
    //Actualizar tras aceptar un amigo
	public void refresh() {
		Intent i=new Intent(FriendsActivity.this,FriendsActivity.class);
		startActivity(i);
		finish();
	}
	//Pulsar sobre un amigo para que aparezcan las opciones
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {       
    	User u = friends.get(position);
    	final Dialog d = new Dialog(this);
    	d.setContentView(R.layout.dialogfriend);
    	final Bundle b = new Bundle();
        b.putString("uid", u.getUid());
    	d.setTitle((u.getName()+" " +u.getLastname()).toUpperCase());
    	ImageButton ibi = (ImageButton) d.findViewById(R.id.imgbinfo);
    	ImageButton ibf = (ImageButton) d.findViewById(R.id.imgbfeed);
    	ImageButton ibp = (ImageButton) d.findViewById(R.id.imgphoto);
    	ibi.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) {
				Intent i=new Intent(FriendsActivity.this,InfoActivity.class);
                i.putExtras(b);
				startActivity(i); 
				d.dismiss();
			}	
    	});
    	ibf.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) {
				Intent i=new Intent(FriendsActivity.this,FeedActivity.class);
                i.putExtras(b);
				startActivity(i); 
				d.dismiss();
			}	
    	});
    	ibp.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) {
				Intent i=new Intent(FriendsActivity.this,PhotoActivity.class);
                i.putExtras(b);
				startActivity(i); 
				d.dismiss();
			}	
    	});
    	d.show();
    }
    //Actualizar peticiones de amistad
    public void updateFriendsRequests(String uid){
    	if(!uid.equals("")){
    		final String id = uid;
        	FriendsActivity.this.runOnUiThread(new Runnable() {
    			@Override
    			public void run(){
    				SocialNetwork s = new FacebookNetSocial();
    				s.acceptFriend(FriendsActivity.this, id);
    			}
    		});
    	}
    } 
    //Actualizar lista de amigos
    public void updateFriends(ArrayList<User> l){
    	friends = l;
    	FriendsActivity.this.runOnUiThread(new Runnable() {
			@Override
			public void run(){
				if(friends != null && friends.size() > 0){
		        	for(int i=0;i<friends.size();i++) adapter.add(friends.get(i));
		        }
		        adapter.notifyDataSetChanged(); 
				pd.dismiss();
			}
		});
    }  
    //Clase para mostrar la lista de amigos
    public class FriendListViewAdapter extends ArrayAdapter<User> {

        private ArrayList<User> items;
        public FriendListViewAdapter(Context context, int textViewResourceId, ArrayList<User> items) {
                super(context, textViewResourceId, items);
                this.items = items;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
                View v = convertView;
                if (v == null) {
                    LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.friendrow, null);
                }
                User o = items.get(position);
                if (o != null) {
                	TextView tt = (TextView) v.findViewById(R.id.namefriend);
                	ImageView im = (ImageView) v.findViewById(R.id.pic); 
                	if (im!= null) {
                		im.setImageBitmap(Utility.getBitmap(o.getPicuser()));
                	}                        
                	if (tt != null) {             
                		tt.setText(o.getName());                             
                	}   	                    	                        
                }
                return v;
        }
    }
}
