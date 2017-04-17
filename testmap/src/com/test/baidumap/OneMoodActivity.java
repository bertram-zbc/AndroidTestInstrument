package com.test.baidumap;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.testmap.logic.ClientThread;
import com.testmap.logic.Comment;
import com.testmap.logic.DetailZoneAdapter;
import com.testmap.logic.ListItemAdapter;
import com.testmap.logic.Mood;
import com.testmap.logic.RefreshListView;
import com.testmap.logic.RefreshListView.ILoadMoreDataListener;
import com.testmap.logic.RefreshListView.IRefreshListener;

public class OneMoodActivity extends Activity{
	private final int REFRESH = 0x1;
	private final int LOADMORE = 0x2;
	private final int REFRESHCOMMENT = 0x3;
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
	
	private int type;
	private int mid;
	private String sendMessage;
	private String title;
	
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
				System.out.println("listlistlsitlsitllslitllsit");
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
				Toast.makeText(OneMoodActivity.this, "服务器已断开，请稍候重试", Toast.LENGTH_SHORT).show();
			}
		};
	};
	
	
	//动态listview
	private ArrayList<Mood> moodsList = new ArrayList<Mood>();
	private RefreshListView listview;
	private DetailZoneAdapter myListAdapter;
	
	private TextView tv_title;

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.onezone);
		
		tv_title = (TextView) findViewById(R.id.id_title);
		
		listview = (RefreshListView) findViewById(R.id.listview);
		
		
		type = getIntent().getIntExtra(WHATTYPE, PEOPLE);
		if(type == ONEMOOD) {
			mid = getIntent().getIntExtra(MID, -1);
			title = "详细信息";
			sendMessage = "getOneMood&&" + mid + "&&";
			
			
		}else if(type == OTHERS) {
			title = getIntent().getStringExtra(TITLE);
			sendMessage = getIntent().getStringExtra(SENDMESSAGE);
			
		}
		
		
		tv_title.setText(title);
			
		
		myListAdapter = new DetailZoneAdapter(this, moodsList);
		listview.setAdapter(myListAdapter);

		listview.setInterface(new OnRefreshListener(), new OnLoadMoreListener());
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
	
}