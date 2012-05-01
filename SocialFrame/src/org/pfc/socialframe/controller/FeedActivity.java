package org.pfc.socialframe.controller;

import java.util.ArrayList;
import java.util.List;

import org.pfc.socialframe.R;
import org.pfc.socialframe.model.Constants;
import org.pfc.socialframe.model.Feed;
import org.pfc.socialframe.model.ServiceFeed;
import org.pfc.socialframe.model.Utility;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class FeedActivity extends Activity{
	private ArrayList<Feed> feeds = null;
	private ProgressDialog pd;
	private FeedListViewAdapter adapter;
	private ServiceFeed sf;
	private ListView listf;
	private Button badd;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedwall);
        sf = new ServiceFeed(this);
        badd = (Button) findViewById(R.id.Addfeed);
        badd.getBackground().setColorFilter(0xAA00FFFF, PorterDuff.Mode.MULTIPLY);
        feeds = new ArrayList<Feed>();
        this.adapter = new FeedListViewAdapter(this, R.layout.feedrow, feeds);
        listf = (ListView) findViewById(R.id.listfeed);
        listf.setAdapter(adapter);
        pd = ProgressDialog.show(FeedActivity.this, "", "Cargando comentarios del muro...");
        sf.showFeeds(FeedActivity.this);
        badd.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				PackageManager pm = getPackageManager();
		        List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
		        if (activities.size() != 0) startVoiceRecognition();
			}
		});
        
    }
	@Override
	public void onBackPressed(){
		super.onBackPressed();
		Intent i=new Intent(FeedActivity.this,ReaderQRActivity.class);
		startActivity(i);
		finish();
	}
	public void updateFeeds(ArrayList<Feed> l){
		feeds = l;
		FeedActivity.this.runOnUiThread(new Runnable() {
			@Override
			public void run(){
				if(feeds != null && feeds.size() > 0){
		        	for(int i=0;i<feeds.size();i++) adapter.add(feeds.get(i));
		        }
		        adapter.notifyDataSetChanged(); 
				pd.dismiss();
			}
		});
	}
	private void startVoiceRecognition(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "HABLE AHORA ...");
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
        startActivityForResult(intent, Constants.REQUEST_CODE);
    }
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == Constants.REQUEST_CODE && resultCode == RESULT_OK){
        	String match = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0);
        	sf.postFeed(match, FeedActivity.this);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
	public class FeedListViewAdapter extends ArrayAdapter<Feed> {
        private ArrayList<Feed> items;
        public FeedListViewAdapter(Context context, int textViewResourceId, ArrayList<Feed> items) {
                super(context, textViewResourceId, items);
                this.items = items;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
                View v = convertView;
                if (v == null) {
                    LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.feedrow, null);
                }
                Feed f = items.get(position);
                if (f != null) {
                	TextView ta = (TextView) v.findViewById(R.id.feedactor);
                	TextView tm = (TextView) v.findViewById(R.id.feedmsg);
                	ImageView imp = (ImageView) v.findViewById(R.id.feedpic);
                	if (imp!= null) {
                		imp.setImageBitmap(Utility.getBitmap(f.getPic()));
                	}
                	if (ta!= null) {
                		if(f.getType()==1){
                    		ta.setText(f.getActor()+" respondio al comentario anterior");
                		}else{
                    		ta.setText(f.getActor());
                		}
                	}                         
                	if (tm != null) {             
                		tm.setText(f.getMsg());                             
                	}   
                }
                return v;
        }
    }
}
