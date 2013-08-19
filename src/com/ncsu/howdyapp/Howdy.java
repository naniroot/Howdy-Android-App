package com.ncsu.howdyapp;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Howdy extends Activity implements OnClickListener {

	public Button login;
	public Button register;
	public EditText username;
	public EditText password;
	public JSONParser jsonparserobject = new JSONParser();
	public double myLatitude = 0, myLongitude = 0;
	public static String url_login_user = "http://152.14.241.131/android_howdy/login_authentication.php";
	public static final String TAG_SUCCESS = "success", TAG_MESSAGE = "message";
	public int success = 3;
	public String message;
	public double[] latitudearray = new double[2];
	public double[] longitudearray = new double[2];
	public double threshold = 0.000001;
	public Handler handler = new Handler();
	public List<NameValuePair> params = new ArrayList<NameValuePair>();
	public JSONObject json;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_howdy);
   
        login = (Button) findViewById(R.id.login);
        register = (Button) findViewById(R.id.register);
        
        username = (EditText) findViewById(R.id.loginusername);
        password = (EditText) findViewById(R.id.loginpassword);

        latitudearray[0] = 0;
    	latitudearray[1] = 0;
    	longitudearray[0] = 0;
    	longitudearray[1] = 0;
    	
        LocationManager mlocmanager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        LocationListener mloclistener = new MyLocationListener();
        mlocmanager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, mloclistener);        
                
        View v = findViewById(R.id.login);
        v.setOnClickListener(this);
        
        View w = findViewById(R.id.register);
        w.setOnClickListener(this);
    }
    
    public class LoginHelper implements Runnable {

		public void run() {
			
			System.out.println("in login helper");
				
				System.out.println("username: " + username.getText().toString());
				
				setUserNameFunction();
				
				System.out.println("MyApplication username: " + getUserNameFunction());
				
				params.add(new BasicNameValuePair("username", username.getText().toString()));
				params.add(new BasicNameValuePair("password", password.getText().toString()));
				
//				handler.post(new Runnable() {
//
//					public void run() {
						// TODO Auto-generated method stub
						json = jsonparserobject.makeHttpRequest(url_login_user, "POST", params);
						System.out.println("makeHttpRequest");
//					}
//					
//				});
				
				try {
					System.out.println("try loop");
					success = json.getInt(TAG_SUCCESS);
					message = json.getString(TAG_MESSAGE);
					System.out.println("Http reply: " + success + " " + message);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
    }
    
    public int getFlag(){
    	int fl = ((MyApplication) this.getApplication()).getFlag();
    	return fl;
    }
    
    public void setFlag(int flag){
    	((MyApplication) this.getApplication()).setFlag(flag);
    }
    
    public String getUserNameFunction(){
    	String s = ((MyApplication) this.getApplication()).getUserName();
    	return s;
    }
    
    public void setUserNameFunction(){
    	((MyApplication) this.getApplication()).setUserName(username.getText().toString());
    }
    
    public void setLatitudeFunction(){
    	((MyApplication) this.getApplication()).setLatitude(myLatitude);
    }
    
    public void setLongitudeFunction(){
    	((MyApplication) this.getApplication()).setLongitude(myLongitude);
    }
    
    public class MyLocationListener implements LocationListener {

		public void onLocationChanged(Location location) {
			
			latitudearray[0] = latitudearray[1];
			longitudearray[0] = longitudearray[1];
			myLongitude = location.getLongitude();
			myLatitude = location.getLatitude();
			latitudearray[1] = myLatitude;
			longitudearray[1] = myLongitude;
			
			if(latitudearray[0] == 0 || longitudearray[0] == 0) {
				latitudearray[0] = latitudearray[1];
				longitudearray[0] = longitudearray[1];
			}
			
			System.out.println("location: " + location.getLatitude() + " " + location.getLongitude());
			System.out.println("Latitude difference: " + Math.abs(latitudearray[1] - latitudearray[0]));
			System.out.println("Longitude difference: " + Math.abs(longitudearray[1] - longitudearray[1]));
			
			if(Math.abs(latitudearray[1] - latitudearray[0]) >= threshold || Math.abs(longitudearray[1] - longitudearray[1]) >= threshold) {
				//user is driving
				setFlag(1);
			} else {
				//user is not driving
				setFlag(0);
			}
			setLongitudeFunction();
			setLatitudeFunction();
			
			//Toast.makeText(getApplicationContext(), "My location:\n" + myLongitude + "\n" + myLatitude, Toast.LENGTH_SHORT).show();
		}

		public void onProviderDisabled(String provider) {

			
		}

		public void onProviderEnabled(String provider) {

			
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {

			
		}
    }
    
    public void onClick(View x) {
    	
    	if(x.getId() == R.id.login) {
    		
    		System.out.println("in login on click");
    		
    		Thread loginhelper = new Thread(new LoginHelper());
    		loginhelper.start();
    		
    		try {
				loginhelper.join();
				System.out.println("login helper join");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
    		if(success == 1) {
    			Intent intent = new Intent(this, BuddyList.class);
        		this.startActivity(intent);
        		System.out.println("JSON message: " + message);
        		loginhelper.stop();
    		}
    		
    		else {
    			Toast.makeText(getApplicationContext(),message, Toast.LENGTH_LONG).show();
    		}
    	}
    	
    	if(x.getId() == R.id.register) {
    		
    		Intent intentregister = new Intent(this, Register.class);
    		this.startActivity(intentregister);
    	}
    }
}
