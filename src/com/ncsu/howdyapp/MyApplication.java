package com.ncsu.howdyapp;

import android.app.Application;

public class MyApplication extends Application {

	private String username;
	public Double latitude;
	public Double longitude;
	public int flag;
	public String stringFlag;
	
	public int getFlag() {
		System.out.println("in get flag");
		return flag;
	}
	
	public void setFlag(int flag) {
		System.out.println("in set flag");
		this.flag = flag;
		System.out.println("setflag: " + this.flag);
	}
	
	public String getUserName() {
		System.out.println("in get user name " + username);
		return username;
	}
	
	public void setUserName(String username) {
		System.out.println("in set username");
		this.username=username;
		System.out.println(username);
	}
	
	public Double getLatitude() {
		System.out.println("in get latitude");
		return latitude;
	}
	
	public void setLatitude(Double latitude) {
		System.out.println("in set latitude");
		this.latitude=latitude;
		System.out.println(latitude);
	}
	
	public Double getLongitude() {
		System.out.println("in get longitude");
		return longitude;
	}
	
	public void setLongitude(Double longitude) {
		System.out.println("in set longitude");
		this.longitude=longitude;
		System.out.println(longitude);
	}
}