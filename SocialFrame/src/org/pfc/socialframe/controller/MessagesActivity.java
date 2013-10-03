package org.pfc.socialframe.controller;

import java.util.ArrayList;

import org.pfc.socialframe.R;
import org.pfc.socialframe.model.Constants;
import org.pfc.socialframe.model.FacebookNetSocial;
import org.pfc.socialframe.model.Message;
import org.pfc.socialframe.model.SocialNetwork;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MessagesActivity extends ListActivity{
	private ArrayList<Message> messages = null;
	private ProgressDialog pd;
    private MessageListViewAdapter adapter;
    private Message m;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.list);
        messages = new ArrayList<Message>();
        this.adapter = new MessageListViewAdapter(this, R.layout.messagerow, messages);
        setListAdapter(this.adapter); 
        pd = ProgressDialog.show(MessagesActivity.this, "", "Cargando mensajes ...");
        SocialNetwork sn = new FacebookNetSocial();
        sn.showMessages(MessagesActivity.this);
    }
    //Volver atrás
    @Override
	public void onBackPressed(){
		super.onBackPressed();
		Intent i=new Intent(MessagesActivity.this,ReaderQRActivity.class);
		startActivity(i);
		finish();
	}
    //Pulsar sobre un mensaje para contestar
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {       
    	m = messages.get(position);
    	Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "HABLE DESPACIO, ALTO Y CLARO ...");
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
        startActivityForResult(intent, Constants.REQUEST_CODE);
    }
    //Comentario enviado
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == Constants.REQUEST_CODE && resultCode == RESULT_OK){
        	String match = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0);
        	SocialNetwork s = new FacebookNetSocial();
        	s.postFeed(MessagesActivity.this,match, m.getUid());
        	Toast toast =Toast.makeText(getApplicationContext(),"CONTESTACIÓN ENVIADA AL AMIGO.", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
			toast.show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    //Actualizar mensajes privados
    public void updateMessages(ArrayList<Message> l){
        	messages = l;
        	MessagesActivity.this.runOnUiThread(new Runnable() {
    			@Override
    			public void run(){
    				if(messages != null && messages.size() > 0){
    		        	for(int i=0;i<messages.size();i++) adapter.add(messages.get(i));
    		        }
    		        adapter.notifyDataSetChanged(); 
    				pd.dismiss();
    			}
    		});
    }
    //Clase para mostrar los mensajes privados
    public class MessageListViewAdapter extends ArrayAdapter<Message> {

        private ArrayList<Message> items;
        public MessageListViewAdapter(Context context, int textViewResourceId, ArrayList<Message> items) {
                super(context, textViewResourceId, items);
                this.items = items;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
                View v = convertView;
                if (v == null) {
                    LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.messagerow, null);
                }
                Message m = items.get(position);
                if (m != null) {
                	TextView td = (TextView) v.findViewById(R.id.date);
                	TextView ts = (TextView) v.findViewById(R.id.sender);
                	TextView tm = (TextView) v.findViewById(R.id.msg);
                	if (td!= null) {
                		td.setText(m.getDate());
                	}                        
                	if (ts != null) {             
                		ts.setText(m.getSender());                             
                	}   
                	if (tm != null) {             
                		tm.setText(m.getMsg());                             
                	}   
                }
                return v;
        }
    }
}
