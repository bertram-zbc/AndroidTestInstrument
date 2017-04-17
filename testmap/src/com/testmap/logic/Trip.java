package com.testmap.logic;

public class Trip {
	private String city;
	private String time;
	private String route;
	private String SharedId;
	
	public void setCity(String city){
		this.city=city;
	}
	public String getCity(){
		return this.city;
	}
	
	public void setTime(String time){
		this.time=time;
	}
	public String getTime(){
		return this.time;
	}
	
	public void setRoute(String route){
		this.route=route;
	}
	public String getRoute(){
		return this.route;
	}
	
	public void setSharedId(String id){
		this.SharedId=id;
	}
	public String getSharedId(){
		return this.SharedId;
	}

}
