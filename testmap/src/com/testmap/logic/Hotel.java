package com.testmap.logic;

import com.baidu.mapapi.model.LatLng;

public class Hotel {
	String name;
	LatLng latLng;
	String address;
	String telephone;
	double price;
	String link;


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LatLng getLatLng(){
		return latLng;
	}
	
	public void setLatLng(LatLng latLng){
		this.latLng=latLng;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double d) {
		this.price = d;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}
}
