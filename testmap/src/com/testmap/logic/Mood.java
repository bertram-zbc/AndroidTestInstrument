package com.testmap.logic;

import java.util.ArrayList;

import com.testmap.logic.ListItemAdapter.MyCommentsAdapter;

public class Mood {
	public MyCommentsAdapter adapter = null;
	public com.testmap.logic.DetailZoneAdapter.MyCommentsAdapter zoneadapter = null;
	public ArrayList<Comment> cList = new ArrayList<Comment>();
	private int mid;
	private String author;
	private String content;
	private String publishTime;
	private int type;
	private String vPinyin;
	private int zNum;
	private String picUrls;
	private String region;
	private ArrayList<String> alPicUrls = new ArrayList<String>();
	private String headPic;
	private boolean isZan;
	public Mood(String mid, String author, String content, String publishTime,
			String type, String vPinyin, String zNum, String picUrls, String region, String headPic, String isZaned) {
		super();
		this.mid = Integer.parseInt(mid);
		this.author = author;
		this.content = content;
		this.publishTime = publishTime;
		this.type = Integer.parseInt(type);
		this.vPinyin = vPinyin;
		this.zNum = Integer.parseInt(zNum);
		this.picUrls = picUrls;
		this.region = region;
		this.headPic = ClientThread.getPicIp() + author + "/head/" + headPic;
		if(isZaned.equals("y")) {
			this.isZan = true;
		} else {
			this.isZan = false;
		}
		if(picUrls.contains("%%")) {
			String [] fileNames = picUrls.split("%%");
			for(int i = 0; i < fileNames.length; i++) {
				alPicUrls.add(ClientThread.getPicIp() + author + "/" + publishTime + "/" + fileNames[i]);
			}
		}
	}
	public boolean isZan() {
		return isZan;
	}
	public void setZan(boolean isZan) {
		this.isZan = isZan;
	}
	public ArrayList<String> getAlPicUrls() {
		return alPicUrls;
	}
	public void setAlPicUrls(ArrayList<String> alPicUrls) {
		this.alPicUrls = alPicUrls;
	}
	public String getHeadPic() {
		return headPic;
	}
	public void setHeadPic(String headPic) {
		this.headPic = headPic;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public int getMid() {
		return mid;
	}
	public void setMid(int mid) {
		this.mid = mid;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getPublishTime() {
		return publishTime;
	}
	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getvPinyin() {
		return vPinyin;
	}
	public void setvPinyin(String vPinyin) {
		this.vPinyin = vPinyin;
	}
	public int getzNum() {
		return zNum;
	}
	public void setzNum(int zNum) {
		this.zNum = zNum;
	}
	public String getPicUrls() {
		return picUrls;
	}
	public void setPicUrls(String picUrls) {
		this.picUrls = picUrls;
	}
	@Override
	public String toString() {
		return "Mood [mid=" + mid + ", author=" + author + ", content="
				+ content + ", publishTime=" + publishTime + ", type=" + type
				+ ", vPinyin=" + vPinyin + ", zNum=" + zNum + ", picUrls="
				+ picUrls + ", region=" + region + "]";
	}
	public Mood() {
		super();
	}
	
	public String toNetString() {
		return mid + "&&" + author + "&&" + content + "&&" + publishTime + "&&" + type + "&&"
				+ vPinyin + "&&" + zNum + "&&" + picUrls + "&&" + region;
	}
	
}
