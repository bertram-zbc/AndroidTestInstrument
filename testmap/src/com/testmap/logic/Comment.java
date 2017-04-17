package com.testmap.logic;


public class Comment {
	@Override
	public String toString() {
		return "Comment [cid=" + cid + ", author=" + author + ", mid=" + mid
				+ ", content=" + content + ", publishTime=" + publishTime
				+ ", type=" + type + ", replyCid=" + replyCid + ", pCid="
				+ pCid + "]";
	}
	public String toNetString() {
		return cid + "&&" + author + "&&" + mid + "&&" + content + "&&"
				+ publishTime + "&&" + type + "&&" + replyCid + "&&" + pCid;
	}
	private int cid;
	private String author;
	private int mid;
	private String content;
	private String publishTime;
	private int type;
	private int replyCid;
	private int pCid;
	private String replyUname;
	public int getCid() {
		return cid;
	}
	public void setCid(int cid) {
		this.cid = cid;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public int getMid() {
		return mid;
	}
	public void setMid(int mid) {
		this.mid = mid;
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
	public int getReplyCid() {
		return replyCid;
	}
	public void setReplyCid(int replyCid) {
		this.replyCid = replyCid;
	}
	public int getpCid() {
		return pCid;
	}
	public void setpCid(int pCid) {
		this.pCid = pCid;
	}
	public Comment(String cid, String author, String mid, String content,
			String publishTime, String type, String replyCid, String pCid, String replyUname) {
		super();
		this.cid = Integer.parseInt(cid);
		this.author = author;
		this.mid = Integer.parseInt(mid);
		this.content = content;
		this.publishTime = publishTime;
		this.type = Integer.parseInt(type);
		this.replyCid = Integer.parseInt(replyCid);
		this.pCid = Integer.parseInt(pCid);
		this.replyUname = replyUname;
	}
	public String getReplyUname() {
		return replyUname;
	}
	public void setReplyUname(String replyUname) {
		this.replyUname = replyUname;
	}
	public Comment() {
		super();
	}
	
}
