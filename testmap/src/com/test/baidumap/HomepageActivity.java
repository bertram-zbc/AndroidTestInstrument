package com.test.baidumap;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

public class HomepageActivity extends Activity {
	
	SharedPreferences sharedPref;
	SharedPreferences username;
	private ImageButton newTrip;
	private ImageButton historyTrip;
	private ImageButton img_map;
	
	private ImageView background2;
	private ImageView background3;
	private ImageView background4;
	
	private Animation image_rotate1 = null;
	private Animation image_rotate2 = null;
	private Animation image_rotate3 = null;
	LinearInterpolator lin = null;
	
	private ScrollView scroll;
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			if(msg.what == 0x1){
				image_rotate1.setInterpolator(lin);		
				background2.setAnimation(image_rotate1);
				
				image_rotate2.setInterpolator(lin);
				background3.setAnimation(image_rotate2);
				
				image_rotate3.setInterpolator(lin);
				background4.setAnimation(image_rotate3);
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.homepage);
		
		init();
		
		newTrip.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				int tag = sharedPref.getInt("isMessage", 0);
				if(tag==1){
					Intent intent = new Intent(HomepageActivity.this,ConditionActivity.class);
					startActivity(intent);
				}
				else{
					Intent intent = new Intent(HomepageActivity.this,ViewPagerActivity.class);
					startActivity(intent);
				}
			}
		});
		
		historyTrip.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HomepageActivity.this,MyTripActivity.class);
				startActivity(intent);
			}
		});
		
		img_map.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HomepageActivity.this,MapActivity.class);
				startActivity(intent);
			}
		});
		
		//scroll.requestDisallowInterceptTouchEvent(false);
		scroll.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
	}


	private void init() {
		
		username = this.getSharedPreferences("Pref01", MODE_PRIVATE);
		String s = username.getString("username", "share");
		sharedPref = this.getSharedPreferences(s, MODE_PRIVATE);
		
				
		newTrip = (ImageButton) findViewById(R.id.newtrip);
		historyTrip = (ImageButton) findViewById(R.id.historytrip);
		
		scroll = (ScrollView) findViewById(R.id.scroll);
		
		background2 = (ImageView) findViewById(R.id.background2);
		background3 = (ImageView) findViewById(R.id.background3);
		background4 = (ImageView) findViewById(R.id.background4);

		image_rotate1 = AnimationUtils.loadAnimation(this, R.anim.image_rotate1);
		image_rotate2 = AnimationUtils.loadAnimation(this, R.anim.image_rotate2);
		image_rotate3 = AnimationUtils.loadAnimation(this, R.anim.image_rotate3);
		img_map = (ImageButton) findViewById(R.id.img_map);
		
		lin = new LinearInterpolator();
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Message message = new Message();
				message.what = 0x1;
				handler.sendMessage(message);
				
			}
		}).start();
	}

			

}
