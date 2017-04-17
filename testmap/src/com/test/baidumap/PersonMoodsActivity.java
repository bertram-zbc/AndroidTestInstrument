package com.test.baidumap;

import java.io.IOException;
import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

import com.example.imageloader.ImageLoader.Type;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.testmap.logic.ClientThread;
import com.testmap.logic.Mood;
import com.testmap.view.CircleImageView;

public class PersonMoodsActivity extends Activity implements OnScrollListener{
	private final int REFRESH = 0x1;
	private final int LOADMORE = 0x2;
	private final int REFRESHCOMPLETE = 0x4;
	private final int NETEXCEPTION = 0x5;
	public static final String WHATTYPE = "type";
	public static final int PEOPLE = 0x2;
	public static final int ONEMOOD = 0x3;
	public static final int OTHERS = 0x5;
	public static final String UNAME = "uname";
	public static final String MID = "mid";
	public static final String TITLE = "title";
	public static final String SENDMESSAGE = "send_message";
	public static final String HEADPIC = "headPic";
	public static String headPicUrl;
	
	private String uname;
	private String sendMessage;
	private int lastItem;

	//动态listview
	private ArrayList<Mood> moodsList = new ArrayList<Mood>();
	private ListView listview;
	private MyAdapter adapter = new MyAdapter();
	private View moreView;
	
	private Handler handler = new Handler() {
		private boolean havingMood(ArrayList<Mood> mList, Mood mood) {
			for(Mood moodTemp : mList) {
				if(moodTemp.getMid() == mood.getMid()) {
					return true;
				}
			}
			return false;
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
				System.out.println(moodsList.size());
				adapter.notifyDataSetChanged();
				//listview.setAdapter(adapter);
				System.out.println("listlistlsitlsitllslitllsit");
			} else if(msg.what == LOADMORE) {
				String [] moods = (String[]) msg.obj;
				for(int i = 0; i < moods.length; i++) {
					String [] oneMood = moods[i].split("&&");
					Mood mood = new Mood(oneMood[0], oneMood[1], oneMood[2], oneMood[3], oneMood[4], oneMood[5], oneMood[6], oneMood[7], oneMood[8], oneMood[9], oneMood[10]);
					if(!havingMood(moodsList, mood)) {
						moodsList.add(mood);
					}
				}
				adapter.notifyDataSetChanged();
			} else if(msg.what == REFRESHCOMPLETE) {
//				TextView loadMore = (TextView) findViewById(R.id.tv_load);
//            	loadMore.setText("没有更多信息了");  
//            	ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
//            	progressBar.setVisibility(View.GONE);  
			} else if(msg.what == NETEXCEPTION) {
				Toast.makeText(PersonMoodsActivity.this, "服务器已断开，请稍候重试", Toast.LENGTH_SHORT).show();
			}
		};
	};

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myzone);

		listview = (ListView) findViewById(R.id.listview);
		
		uname = getIntent().getStringExtra(UNAME);
		headPicUrl = getIntent().getStringExtra(HEADPIC);
		sendMessage = "getMoodsOfWho&&" + uname + "&&";
		
		LayoutInflater inflater = LayoutInflater.from(getApplicationContext());  
        moreView = inflater.inflate(R.layout.footer_layout, null);  
        moreView.findViewById(R.id.load_layout).setVisibility(View.GONE);
        listview.addFooterView(moreView);
        
        listview.setAdapter(adapter);
        listview.setOnScrollListener(this);
        
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				getData(0, 10, REFRESH);
			}
		}).start();

	}
	
	
	
	
	private boolean getData(final int start, final int end, final int msgWhat) {
		new ClientThread().println(sendMessage + start + "&&" + end);
		try {
			String message = new ClientThread().readLine();
			System.out.println(message);
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
//			for(int i = 0; i < moods.length; i++) {
//				String [] oneMood = moods[i].split("&&");
//				System.out.println("mood = " + moods[i]);
//				new ClientThread().println("getCommentOfMid&&" + oneMood[0]);
//				String recvString = new ClientThread().readLine();
//				if(recvString == null) {
//					handler.sendEmptyMessage(NETEXCEPTION);
//					return false;
//				}
//				if(recvString.equals("nothing")) {
//					continue;
//				}
//				String [] aComments = recvString.split("##");
//				Message cMsg = new Message();
//				cMsg.obj = aComments;
//				cMsg.what = REFRESHCOMMENT;
//				handler.sendMessage(cMsg);
//			}
			if(moods.length < 10) {
				return false;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
	
	
	private class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return moodsList.size()+1;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(position==0){
				LayoutInflater inflater = (LayoutInflater) PersonMoodsActivity.this
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.item_myzone_head, null);
				
				TextView tvname = (TextView) convertView.findViewById(R.id.textusername);
				CircleImageView head = (CircleImageView) convertView.findViewById(R.id.cirleimageview);
				tvname.setText(uname);
				ImageLoader.getInstance().displayImage(headPicUrl, head);
			}else{
				LayoutInflater inflater = (LayoutInflater) PersonMoodsActivity.this
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.item_myzone, null);
				
				TextView tvtime = (TextView) convertView.findViewById(R.id.texttime);
				ImageView img = (ImageView) convertView.findViewById(R.id.zone_img);
				TextView tvcity = (TextView) convertView.findViewById(R.id.textcityname);
				TextView tvcontent = (TextView) convertView.findViewById(R.id.textcontent);
				RelativeLayout relazone = (RelativeLayout) convertView.findViewById(R.id.rela_zone);
				final Mood temp = moodsList.get(position-1);
				tvtime.setText(temp.getPublishTime().split(" ")[0]);
				if(temp.getAlPicUrls().size()==0){
					img.setVisibility(View.GONE);
				}else{
					ImageLoader.getInstance().displayImage(temp.getAlPicUrls().get(0), img);
				}				
				tvcity.setText(temp.getRegion());
				tvcontent.setText(temp.getContent());
				
				relazone.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(PersonMoodsActivity.this,OneMoodActivity.class);
						intent.putExtra(OneMoodActivity.WHATTYPE, OneMoodActivity.ONEMOOD);
						intent.putExtra(OneMoodActivity.MID, temp.getMid());
						PersonMoodsActivity.this.startActivity(intent);
					}
				});
			}
			return convertView;
		}

		
	}



	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		 if (lastItem == adapter.getCount()  
	                && scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
	            loadMoreData();  
	        }  
	}




	private void loadMoreData() {
		// TODO Auto-generated method stub
		if(lastItem == adapter.getCount() + 1) {
			moreView.findViewById(R.id.load_layout).setVisibility(View.VISIBLE);
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




	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		lastItem = firstVisibleItem + visibleItemCount - 1;  
	}	
}
