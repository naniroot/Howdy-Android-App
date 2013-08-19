package com.ncsu.howdyapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;


public class BuddyList extends ListActivity {
	
	public ProgressDialog pDialog;
	public Handler handler = new Handler();
	
	JSONParser jParser = new JSONParser();

	public ArrayList<HashMap<String, String>> buddyList = new ArrayList<HashMap<String, String>>();
	
	public TextView user;
	public TextView presence;
	public String myname;
	public String mylocation;
//	public ListView list;
	
	private static String url_all_products = "http://152.14.241.131/android_howdy/buddy_list.php";
	
	private static String url_user = "http://152.14.241.131/android_howdy/user_presence.php";
	
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_BUDDIES = "buddies";
	private static final String TAG_PRESENCE = "presence";
	private static final String TAG_PID = "id";
	private static final String TAG_NAME = "name";
	private static final String TAG_LOCATION = "location";
	private static final String TAG_MY_NAME = "myname";
	private static final String TAG_MY_LOCATION = "mylocation";
	
	JSONArray buddies = null;
	
	String username;
	Double latitude;
	Double longitude;
	
	public String getUserNameFunction(){
    	String s = ((MyApplication) this.getApplication()).getUserName();
    	return s;
    }
	
	public Double getLatitudeFunction(){
    	Double s = ((MyApplication) this.getApplication()).getLatitude();
    	return s;
    }
	
	public Double getLongitudeFunction(){
    	Double s = ((MyApplication) this.getApplication()).getLongitude();
    	return s;
    }
	
	public int getFlag(){
    	int fl = ((MyApplication) this.getApplication()).getFlag();
    	return fl;
    }
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buddy_list);
        
        user = (TextView) findViewById(R.id.user);
        presence = (TextView) findViewById(R.id.presence);
        
        username=getUserNameFunction();
        latitude=getLatitudeFunction();
        longitude=getLongitudeFunction();
        
        Thread buddyThread = new Thread(new BuddyThread());
        buddyThread.start();
    }
	
	public class BuddyThread implements Runnable {

		public void run() {
			// TODO Auto-generated method stub
			try {
	            while(true) {
	            	
	            	buddyList.clear();
	            	
	        		Thread loadAllBuddies = new Thread(new LoadAllBuddies());
	                loadAllBuddies.start();
	                
	                try {
	        			loadAllBuddies.join();
	        		} catch (InterruptedException e) {
	        			// TODO Auto-generated catch block
	        			e.printStackTrace();
	        		}
	                
	                loadAllBuddies.stop();
	                
	                Thread.sleep(5000);
	            }
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }
		}
	}
	
	public void checkInClicked(View v) {
		Intent intent = new Intent(this, CheckIn.class);
		this.startActivity(intent);
	}
	
//	public void listClicked(View v) {
//		System.out.println("List item clicked");
//		Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage("");
//		startActivity(LaunchIntent);
//	}
	
	public class LoadAllBuddies implements Runnable {

		public void run() {
			// TODO Auto-generated method stub
			List<NameValuePair> params1 = new ArrayList<NameValuePair>();
			System.out.println("sending:"+username+Double.toString(latitude)+Double.toString(longitude));
			params1.add(new BasicNameValuePair("username",username));
			
			List<NameValuePair> params2 = new ArrayList<NameValuePair>();
			params2.add(new BasicNameValuePair("username",username));
			params2.add(new BasicNameValuePair("latitude",Double.toString(latitude)));
			params2.add(new BasicNameValuePair("longitude",Double.toString(longitude)));
			params2.add(new BasicNameValuePair("flag",Integer.toString(getFlag())));
			
			JSONObject json1 = jParser.makeHttpRequest(url_all_products, "POST", params1);
			System.out.println("username in param2"+ username);
			
			Log.d("json1: ", json1.toString());
			
			JSONObject json2 = jParser.makeHttpRequest(url_user, "POST", params2);
			
			Log.d("presence json2: ", json2.toString());
			
			JSONObject c1;
			try {
				int success = json2.getInt(TAG_SUCCESS);

				if (success == 1) {
					buddies = json2.getJSONArray(TAG_PRESENCE);
					c1 = buddies.getJSONObject(0);
				
					
					myname= c1.getString(TAG_MY_NAME);
					mylocation=c1.getString(TAG_MY_LOCATION);
				}
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			try {
				int success = json1.getInt(TAG_SUCCESS);

				if (success == 1) {
					buddies = json1.getJSONArray(TAG_BUDDIES);

					for (int i = 0; i < buddies.length(); i++) {
							JSONObject c = buddies.getJSONObject(i);
						
							String name = c.getString(TAG_NAME);
							String location = c.getString(TAG_LOCATION);
							
							System.out.println("Name: " + name + " Location: " + location);
							
							HashMap<String, String> map = new HashMap<String, String>();
						
							map.put(TAG_NAME, name);
							map.put(TAG_LOCATION, location);
							
							buddyList.add(map);
					}
				} 
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			handler.post(new Runnable() {

				public void run() {
					// TODO Auto-generated method stub
					ListAdapter adapter = new SimpleAdapter(BuddyList.this, buddyList,R.layout.list_item, new String[] {TAG_NAME, TAG_LOCATION}, new int[] { R.id.name,R.id.location, });
					setListAdapter(adapter);
					
					user.setText(myname);
					presence.setText(mylocation);
				}
		});
	}	
	}
}
