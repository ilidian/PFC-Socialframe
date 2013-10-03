package org.pfc.socialframe.controller;

import java.util.ArrayList;

import org.pfc.socialframe.R;
import org.pfc.socialframe.model.Event;
import org.pfc.socialframe.model.FacebookNetSocial;
import org.pfc.socialframe.model.SocialNetwork;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class EventsActivity extends Activity{
	private ListView listb, liste;
	private TextView tvdayword;
	private ProgressDialog pd;
	private ArrayList<Event> ev;
	private ArrayAdapter<String> adapterb, adaptere;
	private ArrayList<String> birthdays, events;
	private Toast toast;
	    @Override  
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState); 
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.event);
	        birthdays = new ArrayList<String>();
	        events = new ArrayList<String>();
	        ev = new ArrayList<Event>();
	        tvdayword = (TextView) findViewById(R.id.dayword);
	        listb = (ListView) findViewById(R.id.listbirth);
	        adapterb = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, birthdays);
	        listb.setAdapter(adapterb);
	        liste = (ListView) findViewById(R.id.listevent);
	        adaptere = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, events);
	        liste.setAdapter(adaptere);
	        pd = ProgressDialog.show(EventsActivity.this, "", Html.fromHtml("<font color='white'>" + "Cargando cumpleaños y eventos ..." + "</font>"));
	        SocialNetwork sn = new FacebookNetSocial();
	        sn.showEvents(EventsActivity.this);
	        //Pulsar sobre un evento de la lista
	        liste.setOnItemClickListener(new OnItemClickListener() {
	            @Override
	            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
	            	showInfoEvent(position);
	            }
	        });
	    }   
	    //Volver atrás
	    @Override
		public void onBackPressed(){
			super.onBackPressed();
			Intent i=new Intent(EventsActivity.this,ReaderQRActivity.class);
			startActivity(i);
			finish();
		}
	    //Actualizar eventos y cumpleaños
	    public void updateEvents(ArrayList<String> b, ArrayList<Event> e, String day_today, String month_today){
	    	final String dt = "Hoy, "+day_today+" "+month_today;
        	ev = e;
        	for(int i=0; i < b.size(); i++){
        		birthdays.add(b.get(i));
        	}
        	for(int j=0; j < e.size(); j++){
        		events.add(e.get(j).getTitle());
        	}
        	EventsActivity.this.runOnUiThread(new Runnable() {
    			@Override
    			public void run(){
    		        adapterb.notifyDataSetChanged();
    		        adaptere.notifyDataSetChanged();
    		        tvdayword.setText(dt);
    				pd.dismiss();
    			}
    		});
	   }
	   //Mostrar información de un evento
	   private void showInfoEvent(int position){
		   LayoutInflater inflater = getLayoutInflater();
		   View layout = inflater.inflate(R.layout.toast, null);
		   TextView tvcreator = (TextView) layout.findViewById(R.id.creator);
		   TextView tvtime = (TextView) layout.findViewById(R.id.time);
		   TextView tvlocation = (TextView) layout.findViewById(R.id.location);
		   TextView tvdesc = (TextView) layout.findViewById(R.id.description);
		   String creator = "", time = "", location = "", desc = "";
		   for(int i=0;i< ev.size();i++){
			   if(ev.get(i).getTitle().equals(events.get(position))){
				   if(ev.get(i).getCreator().equals("")){
					   creator = "Usted";
				   }else{
					   creator = ev.get(i).getCreator();
				   }
				   time = ev.get(i).getTime();
				   location = ev.get(i).getLocation();
				   desc = ev.get(i).getDescription();
       		}
       	}
		tvcreator.setText(creator);
		tvtime.setText(time);
		tvlocation.setText(location);
		tvdesc.setText(desc);
       	toast = new Toast(getApplicationContext());
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(layout);
		toast.show();
	   }
}
