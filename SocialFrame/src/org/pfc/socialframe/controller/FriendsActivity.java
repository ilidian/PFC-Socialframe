package org.pfc.socialframe.controller;

import java.util.ArrayList;

import org.pfc.socialframe.R;
import org.pfc.socialframe.model.ServiceFriends;
import org.pfc.socialframe.model.User;
import org.pfc.socialframe.model.Utility;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class FriendsActivity extends ListActivity {
	private ArrayList<User> friends = null;
	private ProgressDialog pd;
	private ServiceFriends sf;
    private IconListViewAdapter adapter;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
        sf = new ServiceFriends(this);
        friends = new ArrayList<User>();
        this.adapter = new IconListViewAdapter(this, R.layout.iconrow, friends);
        setListAdapter(this.adapter); 
        pd = ProgressDialog.show(FriendsActivity.this, "", "Cargando amigos ...");
        sf.showFriends(FriendsActivity.this);
    }
    @Override
	public void onBackPressed(){
		super.onBackPressed();
		Intent i=new Intent(FriendsActivity.this,ReaderQRActivity.class);
		startActivity(i);
		finish();
	}
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {       
//        Toast.makeText(this, local.getLocalName(), 
//          		Toast.LENGTH_LONG).show();      
    }
    
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
    
    public class IconListViewAdapter extends ArrayAdapter<User> {

        private ArrayList<User> items;
        public IconListViewAdapter(Context context, int textViewResourceId, ArrayList<User> items) {
                super(context, textViewResourceId, items);
                this.items = items;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
                View v = convertView;
                if (v == null) {
                    LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.iconrow, null);
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
