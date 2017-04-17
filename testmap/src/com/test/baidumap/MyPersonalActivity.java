package com.test.baidumap;

import java.io.File;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.example.imageloader.ImageAdapter;
import com.example.imageloader.ImageLoader.Type;
import com.example.imageloader.WeixinActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.testmap.logic.ClientThread;
import com.testmap.view.CircleImageView;

public class MyPersonalActivity extends Activity {
	public static String headPicUrl = ClientThread.getPicIp() + "u1/mycourse.png";
	
	SharedPreferences sharedPref;
	SharedPreferences username;
	
	private TextView tvName;
	private TextView tvSex;
	private TextView tvAge;
	private TextView tvHome;
	private TextView tvWork;
	private TextView tvMoney;
	
	private static CircleImageView circleHeadPic;//新增2015-11-08
	
//	public static void changeHeadPic(final String picPath) {
//		new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				ArrayList<String> pics = new ArrayList<String>();
//				pics.add(picPath);
//				new PictureSender(pics, ClientThread.getUserName(), ClientThread.getUserPsd(), "head").run();
//				String headPicName = new File(picPath).getName();
//				new ClientThread().println("updateHeadPic&&" + headPicName);
//				PersonActivity.headPicUrl = ClientThread.getPicIp() + ClientThread.getUserName() + "/head/" + new File(ImageAdapter.mSelectedImg.get(0)).getName();
//				ImageLoader.getInstance().displayImage(headPicUrl, circleHeadPic);
//			}
//		}).start();
//		
//	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mypersonal_message);
		
		Init();
		tvSet();
		
	}

	private void tvSet() {
		
		tvSex.setText(sharedPref.getString("性别", ""));
		tvAge.setText(sharedPref.getString("年龄", ""));
		tvHome.setText(sharedPref.getString("家乡", ""));
		tvWork.setText(sharedPref.getString("职业", ""));
		tvMoney.setText(sharedPref.getString("收入", ""));
	}

	private void Init() {
		
		username = this.getSharedPreferences("Pref01", MODE_PRIVATE);
		String s = username.getString("username", "share");
		sharedPref = this.getSharedPreferences(s, MODE_PRIVATE);
		
		tvName = (TextView) findViewById(R.id.tv_name);
		tvSex = (TextView) findViewById(R.id.tv_sex);
		tvAge = (TextView) findViewById(R.id.tv_age);
		tvHome = (TextView) findViewById(R.id.tv_home);
		tvWork = (TextView) findViewById(R.id.tv_work);
		tvMoney = (TextView) findViewById(R.id.tv_money);
		
		circleHeadPic = (CircleImageView) findViewById(R.id.cirleimageview);
		//ImageLoader.getInstance().displayImage(headPicUrl, circleHeadPic);
		circleHeadPic.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MyPersonalActivity.this,WeixinActivity.class);
				intent.putExtra(WeixinActivity.WHATACTIVITY, WeixinActivity.PERSONACTIVIY);
				startActivity(intent);
			}
		});
		
//		String headurl = sharedPref.getString("头像", "none");
//		if(headurl.equals("none")){
//			//从文件中读取的头像路径不存在，则从网络加载
//			PersonActivity.headPicUrl = ClientThread.getPicIp() + ClientThread.getUserName() + "/head/" + new File(ImageAdapter.mSelectedImg.get(0)).getName();
//			ImageLoader.getInstance().displayImage(headPicUrl, circleHeadPic);
//		}else{
//			com.example.imageloader.ImageLoader.getInstance(3,Type.LIFO).loadImage(headurl,circleHeadPic);
//		}
		
	}
	
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		String headurl = sharedPref.getString("头像", "none");
		if(headurl.equals("none")){
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					/*MyPersonalActivity.headPicUrl = ClientThread.getPicIp() + ClientThread.getUserName() + "/head/" + new File(ImageAdapter.mSelectedImg.get(0)).getName();
					ImageLoader.getInstance().displayImage(headPicUrl, circleHeadPic);*/
				}
			}).start();
		}else{
			com.example.imageloader.ImageLoader.getInstance(3,Type.LIFO).loadImage(headurl,circleHeadPic);
		}
	}


}

