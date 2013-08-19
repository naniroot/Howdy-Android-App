package com.ncsu.howdyapp;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends Activity implements OnClickListener {

	public EditText name;
	public EditText username;
	public EditText password;
	public EditText confirmPassword;
	public Button register;
	public Intent intenthowdy;
	public int success = 0;
	
	public static String url_register_user = "http://152.14.241.131/android_howdy/user_registration.php";
	public JSONParser jsonParser = new JSONParser();
	public static final String TAG_SUCCESS = "success";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        
        name = (EditText) findViewById(R.id.name);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        confirmPassword = (EditText) findViewById(R.id.confirmPassword);
        
        System.out.println("in register");
        
        View v = findViewById(R.id.registerNew);
        v.setOnClickListener(this);
    }

	public void onClick(View v) {
    	
		System.out.println("in on click listener");
		
    	if(v.getId() == R.id.registerNew) {
    		
    		intenthowdy = new Intent(this, Howdy.class);
    		Thread registerhelper = new Thread(new RegisterHelper());
    		registerhelper.start();
    		
    		try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
    		if(success == 1) {
				Toast.makeText(getApplicationContext(), "User Registered Successfully!", Toast.LENGTH_LONG).show();
				System.out.println("user registered!");
			}
			
			else {
				Toast.makeText(getApplicationContext(), "User Registration Failed", Toast.LENGTH_LONG).show();
			}

    		
    		this.startActivity(intenthowdy);
    	}
	}
	
	public class RegisterHelper implements Runnable {

		public void run() {
			
			System.out.println("in registered helper");
			
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("name", name.getText().toString()));
			params.add(new BasicNameValuePair("username", username.getText().toString()));
			params.add(new BasicNameValuePair("password", password.getText().toString()));
			
			JSONObject json = jsonParser.makeHttpRequest(url_register_user, "POST", params);
			
			try {
				
				success = json.getInt(TAG_SUCCESS);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
