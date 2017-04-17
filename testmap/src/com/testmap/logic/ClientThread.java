package com.testmap.logic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.Semaphore;

public class ClientThread implements Runnable {
	static private Socket socket = null;
	static private PrintWriter out = null;
	static private BufferedReader in = null;
	static private String userName = null;
	static private String userPsd = null;
	private static String ipAddr = "115.159.102.138";
	private static String picIp = "http://" + ipAddr + "/";//recommendPic/";
	
	private static boolean isLinking = false;
	private static Semaphore firstPrint = new Semaphore(0);
	private static Semaphore firstRead = new Semaphore(1);
	
	public static String getPicIp() {
		return picIp;
	}

	public static void setPicIp(String picIp) {
		ClientThread.picIp = picIp;
	}

	public static String getIpAddr() {
		return ipAddr;
	}

	public static void setIpAddr(String ipAddr) {
		ClientThread.ipAddr = ipAddr;
	}

	public static void setUser(String uName, String psd) {
		userName = uName;
		userPsd = psd;
	}

	public static String getUserName() {
		return userName;
	}

	public static String getUserPsd() {
		return userPsd;
	}

	public ClientThread() {
		isLinking = false;
	}
	public static void close() {
		try {
			socket.close();
			out.close();
			in.close();
			socket = null;
			out = null;
			in = null;
		} catch (IOException e) {
			return;
		}
	}
	public void println(String msg) {
		try {
			firstRead.acquire();
			if(userName == null) {
				System.exit(0);
			}
			if(out != null) {
				out.println(userName + "&&" + msg);
			} else {
				run();
				firstRead.release();
				println(msg);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			firstPrint.release();
		}
		
	}
	@SuppressWarnings("finally")
	public String readLine() throws IOException {
		String recvString = null;
		try {
			firstPrint.acquire();
			if(in != null) {
				recvString =  in.readLine();
				if(null == recvString) {
					new Thread(new ClientThread()).start();
				}
			} else {
				new Thread(new ClientThread()).start();
				recvString =  null;
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			firstRead.release();
			return recvString;
		}
		
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			if(isLinking) {
				return;
			}
			isLinking = true;
				if(socket != null) {
					close();
				}
				System.out.println("hhhhhhhhhhhhhhhhhhhhhhhhhhh");
				socket = new Socket(ipAddr, 8888);
				if(socket != null) {
					out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(ClientThread.socket.getOutputStream())),true);  
					in = new BufferedReader(new InputStreamReader(ClientThread.socket.getInputStream()));
					/*if(out != null && in != null) {
					//	break;
					}*/
				}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			isLinking = false;
		}
	}
}
