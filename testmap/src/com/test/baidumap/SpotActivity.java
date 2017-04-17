package com.test.baidumap;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.testmap.logic.ClientThread;

public class SpotActivity extends Activity {
	public static final String VIEWPINYIN = "viewPinYin";
	private String vPinYin;
	private String city;
	private String [] viewInfo;
	TextView name;
	TextView abstr;
	TextView price;
	TextView youhui;
	TextView openTime;
	TextView desciption;
	TextView tv_see_moods;
	ImageView img;
	RatingBar rate;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if(msg.what == 0x1) {
				name.setText("");
				rate.setRating(0);
				abstr.setText("无此景点信息");
				price.setText("暂无信息");
				youhui.setText("暂无信息");
				openTime.setText("暂无信息");
				desciption.setText("暂无信息");
			} else if(msg.what == 0x2) {
				String [] view = (String[]) msg.obj;
				name.setText(view[0]);
				abstr.setText(view[2]);
				price.setText(view[9]);
				youhui.setText(view[7]);
				openTime.setText(view[8]);
				desciption.setText(view[3]);
				ImageLoader.getInstance().displayImage(view[11], img);
				if(view[4].equals("0")){
					rate.setRating(0);
				}else if(view[4].equals("1")){
					rate.setRating(1);
				}else if(view[4].equals("2")){
					rate.setRating(2);
				}else if(view[4].equals("3")){
					rate.setRating(3);
				}else if(view[4].equals("4")){
					rate.setRating(4);
				}else if(view[4].equals("5")){
					rate.setRating(5);
				}
				
			} else if(msg.what == 0x3) {
				Toast.makeText(SpotActivity.this, "与服务器失联，请稍候重试", Toast.LENGTH_SHORT).show();
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.spot);
		viewInfo = null;
		abstr = (TextView) findViewById(R.id.abstr);
		price = (TextView) findViewById(R.id.price);
		youhui = (TextView) findViewById(R.id.youhui);
		openTime = (TextView) findViewById(R.id.opentime);
		desciption = (TextView) findViewById(R.id.desc);
		img = (ImageView) findViewById(R.id.img);
		name = (TextView) findViewById(R.id.textView2);
		rate = (RatingBar) findViewById(R.id.ratingBar1);
		tv_see_moods = (TextView) findViewById(R.id.id_seemoods);
		
		vPinYin = getIntent().getStringExtra(VIEWPINYIN);
		tv_see_moods.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(viewInfo != null && !viewInfo.equals("nothing")) {
					Intent intent = new Intent(SpotActivity.this, CityViewActivity.class);
					intent.putExtra(CityViewActivity.WHATTYPE, CityViewActivity.VIEW);
					intent.putExtra(CityViewActivity.CITYNAME, viewInfo[12].replaceAll(".+?%%", "").replace("市", ""));
					intent.putExtra(CityViewActivity.VIEWPINYIN, vPinYin);
					intent.putExtra(CityViewActivity.VIEWNAME, name.getText().toString());
					startActivity(intent);
				}
			}
		});
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				new ClientThread().println("getAllInfoOfView&&" + vPinYin);
				try {
					/*name + "&&" + vid + "&&" + abstr + "&&" + description + "&&" + star + "&&" + telephone + "&&" + attentionName + "&&"
				+ attentionDesc + "&&" + openTime + "&&" + price + "&&" + url + "&&" + picUrl;
				*/
					String message = new ClientThread().readLine();
					if(message == null) {
						handler.sendEmptyMessage(0x3);
						return;
					}
					if(message.equals("nothing")) {
						handler.sendEmptyMessage(0x1);
						return;
					}
					
					viewInfo = message.split("&&");
					for(String str : viewInfo) {
						if(str == null) {
							str = "暂无信息";
						}
					}
//					for(int i=0;i<view.length;i++){
//						if(view[i].equals(""))
//							view[i]="暂无信息";
//					}
					Message msg = new Message();
					msg.obj = viewInfo;
					for(int i=0;i<viewInfo.length;i++){
						System.out.println("!!!!!!!!!"+viewInfo[i]);
					}
					msg.what = 0x2;
					handler.sendMessage(msg);
					
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
				
				
			}
		}).start();
	}
}
