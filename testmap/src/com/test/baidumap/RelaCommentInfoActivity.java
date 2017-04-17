package com.test.baidumap;


import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.test.fragment.PersonFragment;
import com.testmap.logic.ClientThread;
import com.testmap.logic.Comment;
import com.testmap.logic.ListItemAdapter;
import com.testmap.logic.RefreshListView;
import com.testmap.logic.RefreshListView.ILoadMoreDataListener;
import com.testmap.logic.RefreshListView.IRefreshListener;

public class RelaCommentInfoActivity extends Activity{
	private ArrayList<Comment> cList = new ArrayList<Comment>();
	private MyRelaCommentsAdapter adapter;
	private RefreshListView listView;
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if(msg.what == 0x1) {
				Toast.makeText(RelaCommentInfoActivity.this, "服务器失联，请重试", Toast.LENGTH_SHORT).show();
			} else if(msg.what == 0x2) {
				String [] aComments = (String[]) msg.obj;
				if(aComments == null || aComments.length < 1) {
					return;
				}
				cList.clear();
				for(int i = 0; i < aComments.length; i++) {
					String [] oneComment = aComments[i].split("&&");
					if(oneComment.length < 9)
						return;
					Comment comment = new Comment(oneComment[0], oneComment[1], oneComment[2], oneComment[3], oneComment[4], oneComment[5], oneComment[6], oneComment[7], oneComment[8]);
					cList.add(comment);
				}
				adapter.notifyDataSetChanged();
				
			} else if(msg.what == 0x3) {
				listView.refreshComplete();
			} else if(msg.what == 0x4) {
				Toast.makeText(RelaCommentInfoActivity.this, "你还没有好友", Toast.LENGTH_SHORT).show();
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.relacomment);
		listView = (RefreshListView) findViewById(R.id.id_listview);
		adapter = new MyRelaCommentsAdapter();
		listView.setAdapter(adapter);
		listView.setInterface(new OnRefreshListener(), new OnloadMoreListener());
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(RelaCommentInfoActivity.this,OneMoodActivity.class);
				intent.putExtra(OneMoodActivity.WHATTYPE, OneMoodActivity.ONEMOOD);
				intent.putExtra(OneMoodActivity.MID, cList.get(position - 1).getMid());
				RelaCommentInfoActivity.this.startActivity(intent);
				
			}
			
		});
		getData();
	}
	
	
	private void getData() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				new ClientThread().println("getRelaComment");
				String recvString;
				try {
					recvString = new ClientThread().readLine();
					if(recvString == null) {
						handler.sendEmptyMessage(0x1);
						return;
					} else if(recvString.equals("nothing")) {
						handler.sendEmptyMessage(0x4);
						return;
					} else {
						Message msg = new Message();
						msg.obj = recvString.split("##");
						msg.what = 0x2;
						handler.sendMessage(msg);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}).start();
	}


	private class MyRelaCommentsAdapter extends BaseAdapter {
		
		MyRelaCommentsAdapter() {
			
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return cList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return cList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if(convertView == null) {
				convertView = View.inflate(RelaCommentInfoActivity.this, R.layout.comments_gridview, null);
				holder = new ViewHolder();
				holder.tt_author = (TextView) convertView.findViewById(R.id.id_author);
				holder.tt_type = (TextView) convertView.findViewById(R.id.id_type);
				holder.tt_to_who = (TextView) convertView.findViewById(R.id.id_to_who);
				holder.tt_content = (TextView) convertView.findViewById(R.id.id_content);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			Comment comment = cList.get(position);
			holder.tt_author.setText(comment.getAuthor().equals(ClientThread.getUserName()) ? "我" : comment.getAuthor());
			if(comment.getType() == 0) {
				holder.tt_type.setText("评论");
				holder.tt_to_who.setText("");
			} else {
				holder.tt_type.setText("回复");
				holder.tt_to_who.setText(comment.getReplyUname().equals(ClientThread.getUserName()) ? "我" : comment.getReplyUname());
			}
			holder.tt_content.setText(comment.getContent());
			
			holder.tt_author.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					String author = cList.get(position).getAuthor();
					if(author.equals(ClientThread.getUserName())) {
						Intent intent = new Intent(RelaCommentInfoActivity.this,MyPersonalActivity.class);
						RelaCommentInfoActivity.this.startActivity(intent);
					} else {
						FragmentTransaction ft = getFragmentManager().beginTransaction();
						DialogFragment newFragment = new PersonFragment();
						Bundle args = new Bundle();
						args.putString("username", author);
						newFragment.setArguments(args);
						newFragment.show(ft, "dialog");
					}
					
				}
			});
			holder.tt_to_who.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					String author = cList.get(position).getReplyUname();
					if(author.equals(ClientThread.getUserName())) {
						Intent intent = new Intent(RelaCommentInfoActivity.this,MyPersonalActivity.class);
						RelaCommentInfoActivity.this.startActivity(intent);
					} else {
//						Intent intent = new Intent(RelaCommentInfoActivity.this,PersonActivity.class);
//						intent.putExtra(PersonActivity.WHO, author);
//						RelaCommentInfoActivity.this.startActivity(intent);
						FragmentTransaction ft = getFragmentManager().beginTransaction();
						DialogFragment newFragment = new PersonFragment();
						Bundle args = new Bundle();
						args.putString("username", author);
						newFragment.setArguments(args);
						newFragment.show(ft, "dialog");
					}
					
				}
			});
			return convertView;
		}
		
		private class ViewHolder {
			private TextView tt_author;
			private TextView tt_type;
			private TextView tt_to_who;
			private TextView tt_content;
		}
		
	}
	
	
	private class OnloadMoreListener implements ILoadMoreDataListener {

		@Override
		public void loadMoreData(View view, int lastItem) {
			// TODO Auto-generated method stub
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
	
	private class OnRefreshListener implements IRefreshListener {

		@Override
		public void onRefresh() {
			getData();
			handler.sendEmptyMessage(0x3);
		}
		
	}
}
