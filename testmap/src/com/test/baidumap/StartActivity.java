package com.test.baidumap;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import com.testmap.logic.ClientThread;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class StartActivity extends Activity {
	
	private int tag;	
//	SharedPreferences userName;
//	SharedPreferences passWord;
	SharedPreferences firstlogin;
	ProgressBar progressBar;
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			if(msg.what == 0x1){
				progressBar.setVisibility(View.VISIBLE);
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start);
		
		
		firstlogin = this.getSharedPreferences("Pref01",MODE_PRIVATE);
//		userName = this.getSharedPreferences("Pref02",MODE_PRIVATE);
//		passWord = this.getSharedPreferences("Pref02",MODE_PRIVATE);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		progressBar.setVisibility(View.INVISIBLE);
		
		ImageView logo;
		logo = (ImageView) findViewById(R.id.start);
		
		Animation anim = null;
		anim = AnimationUtils.loadAnimation(this, R.anim.anim_logo);
		logo.startAnimation(anim);
		
		tag = firstlogin.getInt("IsFirstLogin", 0);
		System.out.println("tag"+"="+tag+"\n");
		System.out.println("userName="+ firstlogin.getString("username", "false").toString());
		System.out.println("passWord="+firstlogin.getString("password", "false").toString());
		
		TimerTask task = new TimerTask() {
			
			@Override
			public void run() {
				if(tag==0){
					Intent intent = new Intent(StartActivity.this,LoginActivity.class);
					startActivity(intent);
					finish();
				}
				else{
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							try {
								new ClientThread().run();//需要连接一次
								ClientThread.setUser(firstlogin.getString("username", ""), firstlogin.getString("password", ""));
								new ClientThread().println("login&&" + firstlogin.getString("password", ""));
								String str = new ClientThread().readLine();								
								if(str.equals("true")){
									Intent intent = new Intent(StartActivity.this,TabActivity.class);
									startActivity(intent);
									finish();
								}
								else{
									Intent intent = new Intent(StartActivity.this,LoginActivity.class);
									startActivity(intent);
									finish();
								}
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}).start();
				}
			}
		};
		
		Timer timer = new Timer();
		timer.schedule(task, 2000);
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Message msg = new Message();
				msg.what = 0x1;
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				handler.sendMessage(msg);
			}
		}).start();
	}
}
