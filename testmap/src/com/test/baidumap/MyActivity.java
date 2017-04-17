package com.test.baidumap;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.imageloader.ImageLoader.Type;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.testmap.logic.ClientThread;
import com.testmap.view.CircleImageView;

public class MyActivity extends Activity {
	
	SharedPreferences sharedPref;
	SharedPreferences user_headpic;
	Editor editor;
	
	private LinearLayout user;
	
	private RelativeLayout rl_wantcity;
	private RelativeLayout rl_myfriend;
	private RelativeLayout rl_relative;
	private RelativeLayout rl_webinfo;
	private RelativeLayout rl_zanEd;
	
	private static ImageView img_newrela;
	private static ImageView img_newweb;
	
	private SharedPreferences username;
	private RelativeLayout logout;
	
	private CircleImageView civUser;
	public static String headPicUrl = ClientThread.getPicIp() + "u1/mycourse.png";
	
//	Handler handler = new Handler(){
//		public void handleMessage(Message msg) {
//			switch(msg.what){
//			case 0x1:
//			}
//		};
//	};
//	
	private static Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if(msg.what == 0x1) {
				int [] nums = (int[]) msg.obj;
				if(nums[0] > 0) {
					img_newrela.setVisibility(View.VISIBLE);
				} else {
					img_newrela.setVisibility(View.GONE);
				}
				if(nums[1] > 0) {
					img_newweb.setVisibility(View.VISIBLE);
				} else {
					img_newweb.setVisibility(View.GONE);
				}
			}
		};
	};
	
	
	private void changeNew() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				new ClientThread().println("newInfoNum");
				try {
					String recvString = new ClientThread().readLine();
					if(recvString == null) {
						return;
					}
					String [] numstr = recvString.split("&&");
					int [] nums = {Integer.parseInt(numstr[0]), Integer.parseInt(numstr[1])};
					Message msg = new Message();
					msg.obj = nums;
					msg.what = 0x1;
					handler.sendMessage(msg);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}).start();
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my);
		
		sharedPref = this.getSharedPreferences("Pref01", MODE_PRIVATE);
		editor = sharedPref.edit();
		
		rl_wantcity = (RelativeLayout) findViewById(R.id.id_mywantcity);
		rl_myfriend = (RelativeLayout) findViewById(R.id.id_myfriend);
		rl_relative = (RelativeLayout) findViewById(R.id.id_relative);
		rl_webinfo = (RelativeLayout) findViewById(R.id.id_webinfo);
		rl_zanEd = (RelativeLayout) findViewById(R.id.id_zanEd);
		
		img_newrela = (ImageView) findViewById(R.id.id_img_newrela);
		img_newweb = (ImageView) findViewById(R.id.id_img_newweb);
		img_newrela.setVisibility(View.GONE);
		img_newweb.setVisibility(View.GONE);
		changeNew();
		
		//想去的城市
		rl_wantcity.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "to write", 1000).show();
			}
		});
		
		//我的好友
		rl_myfriend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MyActivity.this, FriendListActivity.class);
				startActivity(intent);
				
			}
		});
		
		//与我相关
		rl_relative.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				img_newrela.setVisibility(View.GONE);
				Intent intent = new Intent(MyActivity.this, RelaCommentInfoActivity.class);
				startActivity(intent);
			}
		});
		
		//消息中心
		rl_webinfo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				img_newweb.setVisibility(View.GONE);
				Intent intent = new Intent(MyActivity.this, WebInfoActivity.class);
				startActivity(intent);
			}
		});
		
		//我的动态
		rl_zanEd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				username = MyActivity.this.getSharedPreferences("Pref01", MODE_PRIVATE);
				String s = new String();
				s = username.getString("username", "share");
				Toast.makeText(getApplicationContext(), "to write", 100).show();
				Intent intent = new Intent(MyActivity.this, PersonMoodsActivity.class);
				intent.putExtra(PersonMoodsActivity.UNAME, s);
				intent.putExtra(PersonMoodsActivity.HEADPIC, headPicUrl);
				startActivity(intent);
			}
		});
		
		
		civUser = (CircleImageView) findViewById(R.id.cirleimageview);
		civUser.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MyActivity.this,MyPersonalActivity.class);
				//intent.putExtra(PersonActivity.WHO, ClientThread.getUserName());
				startActivity(intent);				
			}
		});
		
		logout = (RelativeLayout) findViewById(R.id.rela_logout);
		logout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				editor.putInt("IsFirstLogin", 0);
				editor.commit();
				Intent intent = new Intent(MyActivity.this,LoginActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		String s = sharedPref.getString("username", "share");
		user_headpic = this.getSharedPreferences(s, MODE_PRIVATE);
		String headurl = user_headpic.getString("头像", "none");
		if(headurl.equals("none")){

			ImageLoader.getInstance().displayImage(headPicUrl, civUser);
		}else{
			com.example.imageloader.ImageLoader.getInstance(3,Type.LIFO).loadImage(headurl,civUser);
		}
		
		changeNew();
	}

}
