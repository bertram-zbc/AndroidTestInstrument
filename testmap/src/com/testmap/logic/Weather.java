package com.testmap.logic;

public class Weather {
	private String date = null;
	private String temperature = null;
	private String weather = null;

	public String getDate(){
		return this.date;
	}
	
	public String getTemperature(){
		return this.temperature;
	}
	
	public String getWeather(){
		return this.weather;
	}

	public void setDate(String dayDate){
		this.date=dayDate;
	}

	public void setTemperature(String temperature){
		this.temperature=temperature;
	}

	public void setWeather(String weather){
		this.weather=weather;
	}

}
