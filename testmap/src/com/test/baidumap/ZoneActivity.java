package com.test.baidumap;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.imageloader.ImageLoader;
import com.example.imageloader.ImageLoader.Type;
import com.example.imageloader.WeixinActivity;
import com.example.imagepager.ImagePagerActivity;
import com.example.sendpicture.PictureSender;
import com.testmap.logic.ClientThread;

public class ZoneActivity extends Activity {
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 0x100) {
				Toast.makeText(ZoneActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
				String vName = ZoneActivity.this.getIntent().getStringExtra("vName");
				if(vName == null) {
					ZoneActivity.this.finish();
				} else {
					Intent intent = new Intent(ZoneActivity.this, CityViewActivity.class);
					intent.putExtra(CityViewActivity.WHATTYPE, CityViewActivity.VIEW);
					intent.putExtra(CityViewActivity.CITYNAME, region);
					intent.putExtra(CityViewActivity.VIEWPINYIN, viewPinYin);
					intent.putExtra(CityViewActivity.VIEWNAME, vName);
					startActivity(intent);
					ZoneActivity.this.finish();
				}
			} else {
				Toast.makeText(ZoneActivity.this, "发布失败", Toast.LENGTH_SHORT).show();
			}
		};
	};
	
	private GridView gview;
	private TextView tv_publishmood;
	private EditText et_content;
	private String viewPinYin;
	private String region;
	public static ArrayList<String> picPaths = new ArrayList<String>();
	private static MyAdapter myAdapter;
	
	private boolean isPublishing;
	
	
	public static void addPicPaths(ArrayList<String> selectedImagePaths) {
		for(String imagePath : selectedImagePaths) {
			if(!picPaths.contains(imagePath)) {
				picPaths.add(imagePath);
			}
		}
		myAdapter.notifyDataSetChanged();
	}
	
	public static void removePicPath(String picPath) {
		picPaths.remove(picPath);
		myAdapter.notifyDataSetChanged();
	}
	
	public static void addPicPath(String picPath) {
		if(!picPaths.contains(picPath)) {
			picPaths.add(picPath);
			myAdapter.notifyDataSetChanged();
		}
	}
	
	public static boolean isInside(String picPath) {
		return picPaths.contains(picPath);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		isPublishing = false;
		
		viewPinYin = getIntent().getStringExtra("view");
		region = getIntent().getStringExtra("region");
		
		setContentView(R.layout.zone);
		tv_publishmood = (TextView) findViewById(R.id.id_publish_mood);
		et_content = (EditText) findViewById(R.id.ed_content);
		
		picPaths.clear();
		picPaths.add("drable");
		
		gview = (GridView) findViewById(R.id.gview);
		myAdapter = new MyAdapter();
		gview.setAdapter(myAdapter);
		
		tv_publishmood.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(isPublishing) {
					Toast.makeText(ZoneActivity.this, "正在发布，请稍等", Toast.LENGTH_LONG).show();
					return;
				}
				isPublishing = true;
				System.out.println("???????????????????????????????");
				final String content = et_content.getText().toString();
				if((content.length() > 0 || picPaths.size() > 1) && !content.contains("&&") && !content.contains("%%") && !content.contains("##")) {
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							String message = "publishMood&&" + content + "&&" + viewPinYin + "&&";
							for(int i = 1; i < picPaths.size(); i++) {
								String fileName = new File(picPaths.get(i)).getName();
								message += fileName + "%%";
							}
							message += "&&" + region + "&&0";
							System.out.println("PPPPPPPPPP" + "message:" + message);
							new ClientThread().println(message);
							try {
								String recvMessage = new ClientThread().readLine();
								System.out.println("RRRRRRRRRRRRR" + "recvMessage:" + recvMessage);
								String [] aMessage = recvMessage.split("&&");
								if(aMessage[0].equals("success")) {
									handler.sendEmptyMessage(0x100);
									ArrayList<String> picPathsTemp = new ArrayList<String>(picPaths);
									picPathsTemp.remove(0);
									new PictureSender(picPathsTemp, ClientThread.getUserName(), ClientThread.getUserPsd(), aMessage[1]).run();
								} else {
									handler.sendEmptyMessage(0x200);
								}
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} finally {
								isPublishing = false;
							}
						}
					}).start();
				} else {
					Toast.makeText(ZoneActivity.this, "文字内容不能包含\"&&\",\"%%\"和\"##\"", Toast.LENGTH_SHORT).show();
					System.out.println("EEERRRORROROROROOROROORO");
				}
				
			}
		});
		
	}
	
	class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return picPaths.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			final ImageView imageView; 
            if (convertView == null) {
            	imageView = new ImageView(getApplicationContext());
                imageView.setLayoutParams(new GridView.LayoutParams(200, 200));//设置ImageView对象布局 
                imageView.setAdjustViewBounds(false);//设置边界对齐 
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);//设置刻度的类型 
  //              imageView.setPadding(5, 5, 5, 5);//设置间距 
            }  
            else { 
                imageView = (ImageView) convertView; 
            }
            if(position == 0) {
            	imageView.setImageResource(R.drawable.add);
            } else {
                ImageLoader.getInstance(3, Type.LIFO).loadImage(picPaths.get(position), imageView);
            }
            imageView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(position==0){
						imageView.setBackgroundResource(R.drawable.image_add_press);
						Toast.makeText(getApplicationContext(), "hahah", 100).show();
						Intent intent = new Intent(ZoneActivity.this,WeixinActivity.class);
						intent.putExtra(WeixinActivity.WHATACTIVITY, WeixinActivity.ZONEACTIVIY);
						startActivity(intent);
					}else{
						ArrayList<String> picPathsTemp = new ArrayList<String>(picPaths);
						picPathsTemp.remove(0);
						Intent intent = new Intent(ZoneActivity.this,ImagePagerActivity.class);
						intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position - 1);
						intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, picPathsTemp);
						intent.putExtra(ImagePagerActivity.WHATACTIVITY, ImagePagerActivity.ZONEACTIVIY);
						startActivity(intent);
					}
					
				}
			});
            return imageView; 
		} 
	}

}
