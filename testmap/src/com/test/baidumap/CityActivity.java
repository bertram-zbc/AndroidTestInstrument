package com.test.baidumap;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.testmap.logic.ClientThread;
import com.testmap.logic.Comment;
import com.testmap.logic.GetWeather;
import com.testmap.logic.ListItemAdapter;
import com.testmap.logic.Mood;
import com.testmap.logic.RefreshListView;
import com.testmap.logic.RefreshListView.ILoadMoreDataListener;
import com.testmap.logic.RefreshListView.IRefreshListener;
import com.testmap.logic.Weather;

public class CityActivity extends Activity{
	private final int REFRESH = 0x1;
	private final int LOADMORE = 0x2;
	private final int REFRESHCOMMENT = 0x3;
	private final int REFRESHCOMPLETE = 0x4;
	private final int NETEXCEPTION = 0x5;
	public static final String WHATTYPE = "type";
	public static final int CITY = 0x1;
	public static final int PEOPLE = 0x2;
	public static final int ONEMOOD = 0x3;
	public static final int VIEW = 0x4;
	public static final int OTHERS = 0x5;
	public static final String UNAME = "uname";
	public static final String MID = "mid";
	public static final String CITYNAME = "city_name";
	public static final String VIEWPINYIN = "view_pinyin";
	public static final String VIEWNAME = "view_name";
	public static final String TITLE = "title";
	public static final String SENDMESSAGE = "send_message";
	
	private String uname;
	private int mid;
	private String cityName="南京"; 
	private String vPinYin = "。";
	private String vName;
	private String title;
	
	private String sendMessage;
	
	private Handler handler = new Handler() {
		private boolean havingMood(ArrayList<Mood> mList, Mood mood) {
			for(Mood moodTemp : mList) {
				if(moodTemp.getMid() == mood.getMid()) {
					return true;
				}
			}
			return false;
		}
		private Mood getMood(ArrayList<Mood> mList, int mid) {
			for(int i = 0; i < mList.size(); i++) {
				if(mList.get(i).getMid() == mid)
					return mList.get(i);
			}
			return null;
		}
		public void handleMessage(android.os.Message msg) {
			if(msg.what == REFRESH) {
				moodsList.clear();
				String [] moods = (String[]) msg.obj;
				for(int i = 0; i < moods.length; i++) {
					String [] oneMood = moods[i].split("&&");
					Mood mood = new Mood(oneMood[0], oneMood[1], oneMood[2], oneMood[3], oneMood[4], oneMood[5], oneMood[6], oneMood[7], oneMood[8], oneMood[9], oneMood[10]);
					moodsList.add(mood);
				}
				myListAdapter.notifyDataSetChanged();
				listview.refreshComplete();
			} else if(msg.what == LOADMORE) {
				String [] moods = (String[]) msg.obj;
				for(int i = 0; i < moods.length; i++) {
					String [] oneMood = moods[i].split("&&");
					Mood mood = new Mood(oneMood[0], oneMood[1], oneMood[2], oneMood[3], oneMood[4], oneMood[5], oneMood[6], oneMood[7], oneMood[8], oneMood[9], oneMood[10]);
					if(!havingMood(moodsList, mood)) {
						moodsList.add(mood);
					}
				}
				myListAdapter.notifyDataSetChanged();
			} else if(msg.what == REFRESHCOMMENT) {
				String [] aComments = (String[]) msg.obj;
				if(aComments == null || aComments.length < 1) {
					return;
				}
				System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa= = =" + aComments[0]);
				String [] oneComment = aComments[0].split("&&");
				if(oneComment.length < 9)
					return;
				System.out.println("comentjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj===" + oneComment[2]);
				Comment comment = new Comment(oneComment[0], oneComment[1], oneComment[2], oneComment[3], oneComment[4], oneComment[5], oneComment[6], oneComment[7], oneComment[8]);
				Mood mood = getMood(moodsList, comment.getMid());
				mood.cList.clear();
				mood.cList.add(comment);
				for(int k = 1; k < aComments.length; k++) {
					oneComment = aComments[k].split("&&");
					comment = new Comment(oneComment[0], oneComment[1], oneComment[2], oneComment[3], oneComment[4], oneComment[5], oneComment[6], oneComment[7], oneComment[8]);
					mood.cList.add(comment);
				}
				myListAdapter.notifyDataSetChanged();
			} else if(msg.what == REFRESHCOMPLETE) {
				listview.refreshComplete();
			} else if(msg.what == NETEXCEPTION) {
				Toast.makeText(CityActivity.this, "服务器已断开，请稍候重试", Toast.LENGTH_SHORT).show();
			}
		};
	};
	
	//天气的可缩放listview
	private Weather[]weathers = new Weather[7];	
	private ExpandableListView exList;
//	private MyAdapter adapter;
	
	//动态listview
	private ArrayList<Mood> moodsList = new ArrayList<Mood>();
	private RefreshListView listview;
	private ListItemAdapter myListAdapter;
	
	private TextView tvAdd;
		

	int numlike=0;
	int numweather=0;

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.city);
		cityName = this.getIntent().getExtras().getString("CityName");
		title = cityName;
		sendMessage = "getMoodsOfRegionSortByTime&&" + cityName + "&&";
		
		
		for(int i=0;i<7;i++){
			weathers[i]=new Weather();
			weathers[i].setDate("暂无数据");
			weathers[i].setTemperature("");
			weathers[i].setWeather("");
		}
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(GetWeather.getWeather(cityName)==null){
					for(int i=0;i<7;i++){
						weathers[i].setDate("暂无数据");
						weathers[i].setTemperature("");
						weathers[i].setWeather("");
					}
				}else{
					weathers = GetWeather.getWeather(cityName);
				}
			}
		}).start();
				
		
//		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy/MM/dd");
//		date[0] = sDateFormat.format(new java.util.Date());
//		System.out.println("1111111111111111111111111111111"+date[0]);
		
		tvAdd = (TextView) findViewById(R.id.textadd);
		
		tvAdd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(CityActivity.this,ZoneActivity.class);
				intent.putExtra("view", vPinYin);
				intent.putExtra("region", cityName);
				startActivity(intent);				
			}
		});

			
			
			
		
		exList = (ExpandableListView) findViewById(R.id.exlist);
		exList.setAdapter(new MyAdapter());
		exList.setGroupIndicator(null);
		exList.setDivider(null);
		exList.setOnGroupClickListener(new OnGroupClickListener() {
			
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				return true;
			}
		});
		
		

		
		
//		if(weathers != null) {
//			adapter = new ExampleAdapter(this);
//			exList = (AnimatedExpandableListView) findViewById(R.id.exlist);
//			exList.setAdapter(adapter);
//			exList.setOnGroupClickListener(new OnGroupClickListener() {
//
//	            @Override
//	            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
//	                return true;
//	            }
//	            
//	        });
//		}
//		else{
//			System.out.println("###################################");
//		}
//		
		
//		adapter = new ExampleAdapter(this);
//		exList = (AnimatedExpandableListView) findViewById(R.id.exlist);
//		exList.setAdapter(adapter);
//		exList.setOnGroupClickListener(new OnGroupClickListener() {
//
//            @Override
//            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
//                return true;
//            }
//            
//        });
		
		listview = (RefreshListView) findViewById(R.id.listview);
		
		
		LayoutInflater inflater = LayoutInflater.from(getApplicationContext());  
        
        myListAdapter = new ListItemAdapter(this, moodsList);
		listview.setAdapter(myListAdapter);

		listview.setInterface(new OnRefreshListener(), new OnLoadMoreListener());
	}
	
	@Override
	protected void onStart() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				getData(0, 10, REFRESH);
			}
		}).start();
		super.onStart();
	}
	
	
	private boolean getData(final int start, final int end, final int msgWhat) {
		new ClientThread().println(sendMessage + start + "&&" + end);
		try {
			String message = new ClientThread().readLine();
			if(message == null) {
				handler.sendEmptyMessage(NETEXCEPTION);
				return false;
			}
			if(message.equals("nothing")) {
				handler.sendEmptyMessage(REFRESHCOMPLETE);
				return false;
			}
			String [] moods = message.split("##");
			Message msg = new Message();
			msg.obj = moods;
			msg.what = msgWhat;
			handler.sendMessage(msg);
			for(int i = 0; i < moods.length; i++) {
				String [] oneMood = moods[i].split("&&");
				System.out.println("mood = " + moods[i]);
				new ClientThread().println("getCommentOfMid&&" + oneMood[0]);
				String recvString = new ClientThread().readLine();
				if(recvString == null) {
					handler.sendEmptyMessage(NETEXCEPTION);
					return false;
				}
				if(recvString.equals("nothing")) {
					continue;
				}
				String [] aComments = recvString.split("##");
				Message cMsg = new Message();
				cMsg.obj = aComments;
				cMsg.what = REFRESHCOMMENT;
				handler.sendMessage(cMsg);
			}
			if(moods.length < 10) {
				return false;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}


	class MyAdapter extends BaseExpandableListAdapter{

		//获取父item下的子item数目，这里直接返回7
		@Override
		public int getChildrenCount(int groupPosition) {
			return 7;
		}

		//得到父item相关数据，即城市名
		@Override
		public Object getGroup(int groupPosition) {
			return cityName;
		}

		//得到子item需要相关联的数据，这里是对应的天气
		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return weathers[childPosition];
		}

		@Override
		public long getGroupId(int groupPosition) {
			return 0;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		//设置父item布局
		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			if(convertView == null){
    		if(groupPosition==1){
    			LayoutInflater inflater = (LayoutInflater) CityActivity.this
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.item_expand_null, null);
				return convertView;
    		}
			LayoutInflater inflater = (LayoutInflater) CityActivity.this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.item_expand_main, null);
		}
		TextView city = (TextView) convertView.findViewById(R.id.textcity);
		city.setText(title);
		
		final ImageView like = (ImageView) convertView.findViewById(R.id.like);
		like.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				numlike++;
				if(numlike%2==1){
					like.setImageResource(R.drawable.like1);
					Toast.makeText(CityActivity.this, "已加入“我想去”列表", Toast.LENGTH_SHORT).show();
				}else{
					like.setImageResource(R.drawable.like);
					Toast.makeText(CityActivity.this, "已取消收藏", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		final ImageView weather = (ImageView) convertView.findViewById(R.id.weather);
		weather.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				numweather++;
				if(numweather%2==1){
					weather.setImageResource(R.drawable.weather1);
					exList.expandGroup(0);
				}else{
					weather.setImageResource(R.drawable.weather);
					exList.collapseGroup(0);
				}

				
			}
		});
        
        return convertView;
    }
		//设置子item布局
		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {

        	if(convertView == null){				
				LayoutInflater inflater = (LayoutInflater) CityActivity.this
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.item_expand_child, null);
			}
			TextView tvDate = (TextView) convertView.findViewById(R.id.date);
			tvDate.setText("加载中...");
			ImageView picWeather = (ImageView) convertView.findViewById(R.id.weather_pic);
			picWeather.setVisibility(View.INVISIBLE);
			TextView tvTem = (TextView) convertView.findViewById(R.id.temperature);
			tvTem.setText("加载中...");
			
			if(weathers[childPosition].getDate()!=null){
				picWeather.setVisibility(View.VISIBLE);
				if(childPosition==0){
					tvDate.setText(weathers[childPosition].getDate()+"(今天)");
				}else{
					tvDate.setText(weathers[childPosition].getDate());
				}
			
				if(weathers[childPosition].getWeather().equals("晴")){
					picWeather.setImageResource(R.drawable.weather_qing);
				}else if(weathers[childPosition].getWeather().equals("多云")){
					picWeather.setImageResource(R.drawable.weather_duoyun);
				}else if(weathers[childPosition].getWeather().equals("小雨")||weathers[childPosition].getWeather().equals("中雨")
						||weathers[childPosition].getWeather().equals("小到中雨")){
					picWeather.setImageResource(R.drawable.weather_xiaodaozhongyu);
				}else if(weathers[childPosition].getWeather().equals("大雨")||weathers[childPosition].getWeather().equals("暴雨")
						||weathers[childPosition].getWeather().equals("大暴雨")||weathers[childPosition].getWeather().equals("特暴雨")
						||weathers[childPosition].getWeather().equals("大到暴雨")||weathers[childPosition].getWeather().equals("暴雨到大暴雨")
						||weathers[childPosition].getWeather().equals("大暴雨到特大暴雨")){
					picWeather.setImageResource(R.drawable.weather_dadaobaoyu);
				}else if(weathers[childPosition].getWeather().equals("阵雪")||weathers[childPosition].getWeather().equals("小雪")
						||weathers[childPosition].getWeather().equals("中雪")||weathers[childPosition].getWeather().equals("大雪")
						||weathers[childPosition].getWeather().equals("暴雪")||weathers[childPosition].getWeather().equals("小到中雪")
						||weathers[childPosition].getWeather().equals("中到大雪")||weathers[childPosition].getWeather().equals("大到暴雪")){
					picWeather.setImageResource(R.drawable.weather_xue);
				}else if(weathers[childPosition].getWeather().equals("")){
					picWeather.setVisibility(View.INVISIBLE);
				}else{
					picWeather.setImageResource(R.drawable.weather_yin);
				}
			
				if(weathers[childPosition].getTemperature().equals("")){
					tvTem.setText("暂无数据");
				}else{
					tvTem.setText(weathers[childPosition].getTemperature()+ " ℃");
				}
			}
            return convertView;
        }
		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return false;
		}

		@Override
		public int getGroupCount() {
			return 1;
		}
		
	}
	

//	private class ExampleAdapter extends AnimatedExpandableListAdapter {
//		private LayoutInflater inflater;        
//        public ExampleAdapter(Context context) {
//             inflater = LayoutInflater.from(context);
//        }
//
//        @Override
//        public Object getChild(int groupPosition, int childPosition) {
//			return weathers[childPosition];
//		}
//
//        @Override
//        public long getChildId(int groupPosition, int childPosition) {
//            return childPosition;
//        }
//        
//        @Override
//        public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
//
//        	if(convertView == null){				
//				LayoutInflater inflater = (LayoutInflater) CityActivity.this
//						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//				convertView = inflater.inflate(R.layout.item_expand_child, null);
//			}
//			TextView tvDate = (TextView) convertView.findViewById(R.id.date);
//			tvDate.setText("加载中...");
//			ImageView picWeather = (ImageView) convertView.findViewById(R.id.weather_pic);
//			picWeather.setVisibility(View.INVISIBLE);
//			TextView tvTem = (TextView) convertView.findViewById(R.id.temperature);
//			tvTem.setText("加载中...");
//			
//			if(weathers[childPosition].getDate()!=null){
//				picWeather.setVisibility(View.VISIBLE);
//				if(childPosition==0){
//					tvDate.setText(weathers[childPosition].getDate()+"(今天)");
//				}else{
//					tvDate.setText(weathers[childPosition].getDate());
//				}
//			
//				if(weathers[childPosition].getWeather().equals("晴")){
//					picWeather.setImageResource(R.drawable.weather_qing);
//				}else if(weathers[childPosition].getWeather().equals("多云")){
//					picWeather.setImageResource(R.drawable.weather_duoyun);
//				}else if(weathers[childPosition].getWeather().equals("小雨")||weathers[childPosition].getWeather().equals("中雨")
//						||weathers[childPosition].getWeather().equals("小到中雨")){
//					picWeather.setImageResource(R.drawable.weather_xiaodaozhongyu);
//				}else if(weathers[childPosition].getWeather().equals("大雨")||weathers[childPosition].getWeather().equals("暴雨")
//						||weathers[childPosition].getWeather().equals("大暴雨")||weathers[childPosition].getWeather().equals("特暴雨")
//						||weathers[childPosition].getWeather().equals("大到暴雨")||weathers[childPosition].getWeather().equals("暴雨到大暴雨")
//						||weathers[childPosition].getWeather().equals("大暴雨到特大暴雨")){
//					picWeather.setImageResource(R.drawable.weather_dadaobaoyu);
//				}else if(weathers[childPosition].getWeather().equals("阵雪")||weathers[childPosition].getWeather().equals("小雪")
//						||weathers[childPosition].getWeather().equals("中雪")||weathers[childPosition].getWeather().equals("大雪")
//						||weathers[childPosition].getWeather().equals("暴雪")||weathers[childPosition].getWeather().equals("小到中雪")
//						||weathers[childPosition].getWeather().equals("中到大雪")||weathers[childPosition].getWeather().equals("大到暴雪")){
//					picWeather.setImageResource(R.drawable.weather_xue);
//				}else if(weathers[childPosition].getWeather().equals("")){
//					picWeather.setVisibility(View.INVISIBLE);
//				}else{
//					picWeather.setImageResource(R.drawable.weather_yin);
//				}
//			
//				if(weathers[childPosition].getTemperature().equals("")){
//					tvTem.setText("暂无数据");
//				}else{
//					tvTem.setText(weathers[childPosition].getTemperature()+ " ℃");
//				}
//			}
//            return convertView;
//        }
//
//        @Override
//        public int getRealChildrenCount(int groupPosition) {
//            return 7;
//        }
//
//        @Override
//        public Object getGroup(int groupPosition) {
//			return cityName;
//		}
//
//        @Override
//        public int getGroupCount() {
//            return 1;
//        }
//
//        @Override
//        public long getGroupId(int groupPosition) {
//            return groupPosition;
//        }
//
//        @Override
//        public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
//        	if(convertView == null){
////        		if(groupPosition==1){
////        			LayoutInflater inflater = (LayoutInflater) CityActivity.this
////    						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
////    				convertView = inflater.inflate(R.layout.item_expand_null, null);
////    				return convertView;
////        		}
//				LayoutInflater inflater = (LayoutInflater) CityActivity.this
//						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//				convertView = inflater.inflate(R.layout.item_expand_main, null);
//			}
//			TextView city = (TextView) convertView.findViewById(R.id.textcity);
//			city.setText(cityName);
//			
//			final ImageView like = (ImageView) convertView.findViewById(R.id.like);
//			like.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					numlike++;
//					if(numlike%2==1){
//						like.setImageResource(R.drawable.like1);
//						Toast.makeText(CityActivity.this, "已加入“我想去”列表", Toast.LENGTH_SHORT).show();
//					}else{
//						like.setImageResource(R.drawable.like);
//						Toast.makeText(CityActivity.this, "已取消收藏", Toast.LENGTH_SHORT).show();
//					}
//				}
//			});
//			
//			final ImageView weather = (ImageView) convertView.findViewById(R.id.weather);
//			weather.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					numweather++;
//					if(numweather%2==1){
//						weather.setImageResource(R.drawable.weather1);
//						exList.expandGroupWithAnimation(0);
//					}else{
//						weather.setImageResource(R.drawable.weather);
//						exList.collapseGroupWithAnimation(0);
//					}
//
//					
//				}
//			});
//            
//            return convertView;
//        }
//
//        @Override
//        public boolean hasStableIds() {
//            return true;
//        }
//
//        @Override
//        public boolean isChildSelectable(int arg0, int arg1) {
//            return false;
//        }
//        
//    }
//
//	
	
	private class OnLoadMoreListener implements ILoadMoreDataListener {

		@Override
		public void loadMoreData(View view, int lastItem) {
			if(lastItem == myListAdapter.getCount() + 1) {
				view.findViewById(R.id.load_layout).setVisibility(View.VISIBLE);
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						if(!getData(moodsList.size(), moodsList.size() + 10, LOADMORE)) {
							handler.postDelayed(new Runnable() {  
		                          
		                        @Override  
		                        public void run() {                             
		                        	TextView loadMore = (TextView) findViewById(R.id.tv_load);
		                        	loadMore.setText("没有更多信息了");  
		                        	ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
		                        	progressBar.setVisibility(View.GONE);  
		                              
		                        }  
		                    }, 1000);
						}
						
					}
				}).start();
			}
			
			
		}
		
	}
	
	private class OnRefreshListener implements IRefreshListener {

		@Override
		public void onRefresh() {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					getData(0, 10,  REFRESH);
					
				}
			}).start();
		}
		
	}
	
//	@Override
//	protected void onStart() {
//		// TODO Auto-generated method stub
//		super.onStart();
//		myListAdapter.notifyDataSetChanged();
//		
//	}
	
}
