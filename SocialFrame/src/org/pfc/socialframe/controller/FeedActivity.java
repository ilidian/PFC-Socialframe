package org.pfc.socialframe.controller;

import java.util.ArrayList;
import java.util.List;

import org.pfc.socialframe.R;
import org.pfc.socialframe.model.Constants;
import org.pfc.socialframe.model.FacebookNetSocial;
import org.pfc.socialframe.model.Feed;
import org.pfc.socialframe.model.SocialNetwork;
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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FeedActivity extends Activity{
	private ArrayList<Feed> feeds = null;
	private ProgressDialog pd;
	private FeedListViewAdapter adapter;
	private ListView listf;
	private Button badd;
	private String owner;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.feedwall);
        badd = (Button) findViewById(R.id.Addfeed);
        badd.getBackground().setColorFilter(0xAA00FFFF, PorterDuff.Mode.MULTIPLY);
        feeds = new ArrayList<Feed>();
        this.adapter = new FeedListViewAdapter(this, R.layout.feedrow, feeds);
        listf = (ListView) findViewById(R.id.listfeed);
        listf.setAdapter(adapter);
        owner = "";
        if(getIntent().hasExtra("uid")){
        	Bundle b = getIntent().getExtras();
        	owner = b.getString("uid");
        }
        pd = ProgressDialog.show(FeedActivity.this, "", "Cargando comentarios del muro...");
        SocialNetwork sn = new FacebookNetSocial();
        sn.showFeeds(FeedActivity.this, owner);
        //Saber si tenemos reconocedor de voz
        badd.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				PackageManager pm = getPackageManager();
		        List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
		        if (activities.size() != 0) startVoiceRecognition();
			}
		});
        
    }
	//Volver atrás
	@Override
	public void onBackPressed(){
		super.onBackPressed();
		if(owner.equals("")){
			Intent i=new Intent(FeedActivity.this,ReaderQRActivity.class);
			startActivity(i);
		}
		finish();
	}
	//Actualizar tras realizar un comentario
	public void refresh(){
		Intent i = new Intent(FeedActivity.this, FeedActivity.class);
		startActivity(i);
		finish();
	}
	//Actualizar comentarios del muro
	public void updateFeeds(ArrayList<Feed> l){
		if(l!=null){
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
		}else{
			pd.dismiss();
			Toast toast =Toast.makeText(getApplicationContext(),"COMENTARIOS DEMASIADO ANTIGUOS. NO SE MUESTRAN.", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
			toast.show();
		}
	}
	//Reconocedor de voz
	private void startVoiceRecognition(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "HABLE DESPACIO, ALTO Y CLARO ...");
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
        startActivityForResult(intent, Constants.REQUEST_CODE);
    }
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == Constants.REQUEST_CODE && resultCode == RESULT_OK){
        	String match = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0);
        	SocialNetwork s = new FacebookNetSocial();
        	s.postFeed(FeedActivity.this,match, owner);
        	refresh();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
	//Clase para mostrar la lista de comentarios
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
