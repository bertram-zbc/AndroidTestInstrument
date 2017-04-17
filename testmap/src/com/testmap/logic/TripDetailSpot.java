package com.testmap.logic;

public class TripDetailSpot {
	
	private String name;
	private double lat;
	private double lon;
	private String picurl;
	private String url;
	
	private String day;
	private int IsFirstOfDay;
	private int IsHotel;
	
	public void setName(String name){
		this.name=name;
	}
	public String getName(){
		return this.name;
	}
	
	public void setDay(String day){
		this.day=day;
	}
	public String getDay(){
		return this.day;
	}
	
	public void setIsFirstOfDay(int i){
		this.IsFirstOfDay=i;
	}
	public int getIsFirstOfDay(){
		return this.IsFirstOfDay;
	}
	
	public void setIsHotel(int i){
		this.IsHotel = i;
	}
	public int getIsHotel(){
		return this.IsHotel;
	}

}
