package com.ncsu.howdyapp;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CheckIn extends Activity {

	public EditText checkin; 
	public Button submit;
	public TextView latitude;
	public TextView longitude;
	public TextView myname;
	public TextView myusername;
	public String username10;
	public String name10;
	
	public String checkinusername;
    public Double checkinlatitude;
    public Double checkinlongitude;
    public String checkinlocation;
    public int checkinresponse;
	
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_MY_NAME = "myname";
	private static final String TAG_NAME_ARRAY = "namearray";
	
	private static String url_name = "http://152.14.241.131/android_howdy/name.php";
	
	private static String url_check_in = "http://152.14.241.131/android_howdy/user_check_in.php";
	
	JSONArray name = null;
	JSONParser jParser = new JSONParser();
	public Handler handler = new Handler();
	
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
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);
        
        checkin = (EditText) findViewById(R.id.checkinbox);
        submit = (Button) findViewById(R.id.submit);
        latitude = (TextView) findViewById(R.id.mylatitude);
        longitude = (TextView) findViewById(R.id.mylongitude);
        myname = (TextView) findViewById(R.id.myname);
        myusername = (TextView) findViewById(R.id.myusername);
        
        checkinusername = getUserNameFunction();
        checkinlatitude = getLatitudeFunction();
        checkinlongitude = getLongitudeFunction();
        username10 = checkinusername;
        
        
        handler.post(new Runnable() {
			public void run() {
				myusername.setText(checkinusername);
				latitude.setText(checkinlatitude.toString());
				longitude.setText(checkinlongitude.toString());
			}
        });  
        
        Thread getname = new Thread(new GetNameThread());
        getname.start();
        try {
			getname.join();
			System.out.println("in get name join");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        getname.stop();
        System.out.println("get name stopped");
    }
    
    public class GetNameThread implements Runnable {

		public void run() {
			// TODO Auto-generated method stub
			List<NameValuePair> params1 = new ArrayList<NameValuePair>();
			params1.add(new BasicNameValuePair("username",username10));
			
			JSONObject json3 = jParser.makeHttpRequest(url_name, "POST", params1);
			
			JSONObject c1;
			try {
				int success = json3.getInt(TAG_SUCCESS);

				if (success == 1) {
					name = json3.getJSONArray(TAG_NAME_ARRAY);
					c1 = name.getJSONObject(0);
				
					
					name10 = c1.getString(TAG_MY_NAME);
					
					myname.setText(name10);
				}
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}	
    }
    
    public void onSubmitClicked(View v) {
    	
//    	checkinlocation = checkin.getText().toString();
//    	System.out.println("checkinlocation: " + checkinlocation);
    	
    	Thread userCheckIn = new Thread(new UserCheckInHandler());
    	userCheckIn.start();
    	try {
			userCheckIn.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        userCheckIn.stop();
        
        if(checkinresponse == 1) {
        	Intent intent = new Intent(this, BuddyList.class);
    		this.startActivity(intent);
        }
    }
    
    public class UserCheckInHandler implements Runnable {

		public void run() {
			// TODO Auto-generated method stub
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("username",checkinusername));
			params.add(new BasicNameValuePair("latitude",Double.toString(checkinlatitude)));
			params.add(new BasicNameValuePair("longitude",Double.toString(checkinlongitude)));
//			params.add(new BasicNameValuePair("latitude","36"));
//			params.add(new BasicNameValuePair("longitude","-79"));
			params.add(new BasicNameValuePair("location",checkin.getText().toString()));
			
			JSONObject json1 = jParser.makeHttpRequest(url_check_in, "POST", params);

			try {
				checkinresponse = json1.getInt(TAG_SUCCESS);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(checkinresponse == 0) {
//				Toast.makeText(getApplicationContext(), "Check In failed", Toast.LENGTH_LONG).show();
				System.out.println("checkin response 0");
			} else {
//				Toast.makeText(getApplicationContext(), "User check in successful", Toast.LENGTH_LONG).show();
				System.out.println("checkin response 1");
			}
		}	
    }
}
