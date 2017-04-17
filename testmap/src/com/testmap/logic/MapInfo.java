package com.testmap.logic;

import java.io.Serializable;


public class MapInfo implements Serializable {
	/**
	 * Î¨Ò»±àÂë
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private double latitude;
	private double longitude;
	private String picurl;
	
	public MapInfo(String name, String picurl, double latitude, double longitude)
	{
		this.name=name;
		this.picurl=picurl;
		this.latitude=latitude;
		this.longitude=longitude;
	}
	
	public double getLatitude()
	{
		return latitude;
	}

	public void setLatitude(double latitude)
	{
		this.latitude = latitude;
	}

	public double getLongitude()
	{
		return longitude;
	}

	public void setLongitude(double longitude)
	{
		this.longitude = longitude;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public void setImgUrl(String picurl)
	{
		this.picurl = picurl;
	}

	public String getUrl(){
		return this.picurl;
	}

	
}