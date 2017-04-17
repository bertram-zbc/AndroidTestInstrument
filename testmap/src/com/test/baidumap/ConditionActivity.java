package com.test.baidumap;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.testmap.logic.ClientThread;

public class ConditionActivity extends Activity {
	
	public static ConditionActivity instance;
	
	private TextView tvback;
	private EditText edit_startplace;
	private TextView tv_endplace;
	private ImageButton endplace;
	
	
	private SeekBar mSeekbarOfpeople;
	private TextView textOfpeople;
	private SeekBar mSeekbarOfmoney;
	private TextView textOfmoney;
	private SeekBar mSeekbarOfday;
	private TextView textOfday;
	private ImageButton searchBtn;//搜索按钮
	//释放压力
	private TextView textview5;
	private int textview5_num = 0;
	//休闲娱乐
	private TextView textview4;
	private int textview4_num = 0;
	//结交朋友
	private TextView textview6;
	private int textview6_num = 0;
	//欣赏祖国大好河山
	private TextView textview7;
	private int textview7_num = 0;
	//绘画摄影
	private TextView textview8;
	private int textview8_num = 0;
	//增长见识
	private TextView textview9;
	private int textview9_num = 0;
	//没有目的，就是想走走
	private TextView textview10;
	private int textview10_num = 0;
	
	//发送给服务器的字符串
		private String startPlace = null;
		private String endPlace = null;
		private String aim = null;
		private String viewp = null;
		private String budget = null;
		private String time = null;
		private String viewLine = null;
		
		private Handler handler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				if(msg.what == 0x1) {
					Toast.makeText(ConditionActivity.this, "未找到符合条件的景点/路线", Toast.LENGTH_SHORT).show();
				} else if(msg.what == 0x2) {
					Toast.makeText(ConditionActivity.this, "网络错误，请重试", Toast.LENGTH_SHORT).show();
				}
			};
		};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.condition);
		instance = this;
		
		edit_startplace = (EditText) findViewById(R.id.edit_startplace);
		edit_startplace.setText("我的位置");
		tv_endplace = (TextView) findViewById(R.id.edit_endplace);
		tvback = (TextView) findViewById(R.id.tvback);
		tvback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
			
		textview4 = (TextView) findViewById(R.id.textView4);
		textview4.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				textview4_num++;
				if(textview4_num % 2==0){
					textview4.setTextColor(Color.parseColor("#b2b2b2"));
					textview4.setBackgroundResource(R.drawable.rectangle);
				}
				else{
					textview4.setTextColor(Color.parseColor("#FFFFFF"));
					textview4.setBackgroundResource(R.drawable.rectangle1);
				}
			}
		});
		
		textview5 = (TextView) findViewById(R.id.textView5);
		textview5.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				textview5_num++;
				if(textview5_num % 2==0){
					textview5.setTextColor(Color.parseColor("#b2b2b2"));
					textview5.setBackgroundResource(R.drawable.rectangle);
				}
				else{
					textview5.setTextColor(Color.parseColor("#FFFFFF"));
					textview5.setBackgroundResource(R.drawable.rectangle1);
				}
			}
		});
		
		textview6 = (TextView) findViewById(R.id.textView6);
		textview6.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				textview6_num++;
				if(textview6_num % 2==0){
					textview6.setTextColor(Color.parseColor("#b2b2b2"));
					textview6.setBackgroundResource(R.drawable.rectangle);
				}
				else{
					textview6.setTextColor(Color.parseColor("#FFFFFF"));
					textview6.setBackgroundResource(R.drawable.rectangle1);
				}
			}
		});
		
		textview7 = (TextView) findViewById(R.id.textView7);
		textview7.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				textview7_num++;
				if(textview7_num % 2==0){
					textview7.setTextColor(Color.parseColor("#b2b2b2"));
					textview7.setBackgroundResource(R.drawable.rectangle);
				}
				else{
					textview7.setTextColor(Color.parseColor("#FFFFFF"));
					textview7.setBackgroundResource(R.drawable.rectangle1);
				}
			}
		});
		
		textview8 = (TextView) findViewById(R.id.textView8);
		textview8.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				textview8_num++;
				if(textview8_num % 2==0){
					textview8.setTextColor(Color.parseColor("#b2b2b2"));
					textview8.setBackgroundResource(R.drawable.rectangle);
				}
				else{
					textview8.setTextColor(Color.parseColor("#FFFFFF"));
					textview8.setBackgroundResource(R.drawable.rectangle1);
				}
			}
		});
		
		textview9 = (TextView) findViewById(R.id.textView9);
		textview9.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				textview9_num++;
				if(textview9_num % 2==0){
					textview9.setTextColor(Color.parseColor("#b2b2b2"));
					textview9.setBackgroundResource(R.drawable.rectangle);
				}
				else{
					textview9.setTextColor(Color.parseColor("#FFFFFF"));
					textview9.setBackgroundResource(R.drawable.rectangle1);
				}
			}
		});
		
		textview10 = (TextView) findViewById(R.id.textView10);
		textview10.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				textview10_num++;
				if(textview10_num % 2==0){
					textview10.setTextColor(Color.parseColor("#b2b2b2"));
					textview10.setBackgroundResource(R.drawable.rectangle);
				}
				else{
					textview10.setTextColor(Color.parseColor("#FFFFFF"));
					textview10.setBackgroundResource(R.drawable.rectangle1);
				}
			}
		});
		
		endplace = (ImageButton) findViewById(R.id.imageButton1);
		endplace.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ConditionActivity.this,SortListActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("IsRecommand", "no");
				intent.putExtras(bundle);
				startActivityForResult(intent, 0);
			}
		});
		
		textOfpeople=(TextView) findViewById(R.id.textOfpeople);
		mSeekbarOfpeople=(SeekBar) findViewById(R.id.seekBar_people);
		mSeekbarOfpeople.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			//设置参数，5个中断点
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				int seekProgress = mSeekbarOfpeople.getProgress();  
		        if(seekProgress<13){  
		        	mSeekbarOfpeople.setProgress(0);
		        	textOfpeople.setText("不限");
		        	viewp = 0 + "";
		        }else if(seekProgress>=13 && seekProgress<38){  
		        	mSeekbarOfpeople.setProgress(25);  
		        	textOfpeople.setText("亲子");
		        	viewp = 1 + "";
		        }else if(seekProgress>=38 && seekProgress<63){  
		            mSeekbarOfpeople.setProgress(50); 
		            textOfpeople.setText("朋友");
		            viewp = 2 + "";
		        }else if(seekProgress>=63 && seekProgress<88){  
		            mSeekbarOfpeople.setProgress(75);
		            textOfpeople.setText("情侣");
		            viewp = 3 + "";
		        }else if(seekProgress>=88){  
		            mSeekbarOfpeople.setProgress(100);
		            textOfpeople.setText("长辈");
		            viewp = 4 + "";
		        }
				
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}  
		});
		
		textOfmoney=(TextView) findViewById(R.id.textOfmoney);
		mSeekbarOfmoney=(SeekBar) findViewById(R.id.seekBar_money);
		mSeekbarOfmoney.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				int seekProgress = mSeekbarOfmoney.getProgress();  
		        if(seekProgress<10){  
		        	mSeekbarOfmoney.setProgress(0);
		        	textOfmoney.setText("不限");
		        	budget = "" + 0;
		        }else if(seekProgress>=10 && seekProgress<30){  
		        	mSeekbarOfmoney.setProgress(20);  
		        	textOfmoney.setText("0-0.5k");
		        	budget = "" + 1;
		        }else if(seekProgress>=30 && seekProgress<50){  
		        	mSeekbarOfmoney.setProgress(40); 
		        	textOfmoney.setText("0.5-1k");
		        	budget = "" + 2;
		        }else if(seekProgress>=50 && seekProgress<70){  
		        	mSeekbarOfmoney.setProgress(60);
		        	textOfmoney.setText("1-5k");
		        	budget = "" + 3;
		        }else if(seekProgress>=70 && seekProgress<90){  
		        	mSeekbarOfmoney.setProgress(80);
		        	textOfmoney.setText("5-10k");
		        	budget = "" + 4;
		        }else if(seekProgress>=90){
		        	mSeekbarOfmoney.setProgress(100);
		        	textOfmoney.setText("10k+");
		        	budget = "" + 5;
		        }
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		textOfday=(TextView) findViewById(R.id.textOfday);
		mSeekbarOfday=(SeekBar) findViewById(R.id.seekBar_day);
		mSeekbarOfday.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				int seekProgress = mSeekbarOfday.getProgress();  
		        if(seekProgress<8){  
		        	mSeekbarOfday.setProgress(0);
		        	textOfday.setText("不限");
		        	time = "" + 0;
		        }else if(seekProgress>=8 && seekProgress<25){  
		        	mSeekbarOfday.setProgress(17);  
		        	textOfday.setText("1");
		        	time = "" + 1;
		        }else if(seekProgress>=25 && seekProgress<41){  
		        	mSeekbarOfday.setProgress(33); 
		        	textOfday.setText("2");
		        	time = "" + 2;
		        }else if(seekProgress>=41 && seekProgress<58){  
		        	mSeekbarOfday.setProgress(50);
		        	textOfday.setText("3");
		        	time = "" + 3;
		        }else if(seekProgress>=58 && seekProgress<75){  
		        	mSeekbarOfday.setProgress(67);
		        	textOfday.setText("4");
		        	time = "" + 4;
		        }else if(seekProgress>=75 && seekProgress<91){
		        	mSeekbarOfday.setProgress(83);
		        	textOfday.setText("5");
		        	time = "" + 5;
		        }else if(seekProgress>=91){
		        	mSeekbarOfday.setProgress(100);
		        	textOfday.setText("5+");	
		        	time = "" + 6;
		        }
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				
			}
		});
		
		searchBtn = (ImageButton) findViewById(R.id.searchbutton);
		searchBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startPlace = "南京";
				
				aim = "" + (textview4_num % 2) + (textview5_num % 2) + (textview6_num % 2) + (textview7_num % 2) + (textview8_num % 2) + (textview9_num % 2) + (textview10_num % 2);
				viewp = viewp == null ? "0" : viewp;
				budget = budget == null ? "0" : budget;
				time = time == null ? "0" : time;
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							new ClientThread().println("refference&&" + startPlace + "&&" + endPlace + "&&" + aim + "&&" + viewp + "&&" + budget + "&&" + time);
							viewLine = new ClientThread().readLine();
							if(viewLine == null) {
								handler.sendEmptyMessage(0x2);
								return;
							}
							if(viewLine.equals("nothing")) {
								handler.sendEmptyMessage(0x1);
								return;
							}
							Intent intent = new Intent();
							intent.setClass(ConditionActivity.this, RouteActivity.class);
							Bundle bundle = new Bundle();
							bundle.putString("viewLine", viewLine);
							intent.putExtras(bundle);
							intent.putExtra("city", endPlace);
//							startActivityForResult(intent, 0);
							startActivity(intent);
							//finish();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//viewLine为返回的路线信息
					}
				}).start();
				
			}
		});
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case 1:
			Bundle bundle = data.getExtras();
			if(bundle != null){
				String dest_city = bundle.getString("dest_city");
				tv_endplace.setText(dest_city);
				endPlace = dest_city; 
			}
			break;
//		case 2:
//			break;
		default:
			break;
		}
	}
	
}

