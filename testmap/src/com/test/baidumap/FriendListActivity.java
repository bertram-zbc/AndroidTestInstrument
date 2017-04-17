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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.test.fragment.PersonFragment;
import com.testmap.logic.ClientThread;
import com.testmap.logic.RefreshListView;
import com.testmap.logic.RefreshListView.ILoadMoreDataListener;
import com.testmap.logic.RefreshListView.IRefreshListener;
import com.testmap.view.CircleImageView;

public class FriendListActivity extends Activity{
	private ArrayList<User> friendList = new ArrayList<User>();
	private MyFriendListAdapter adapter;
	private RefreshListView listView;
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if(msg.what == 0x1) {
				Toast.makeText(FriendListActivity.this, "服务器失联，请重试", Toast.LENGTH_SHORT).show();
			} else if(msg.what == 0x2) {
				String [] users = (String[]) msg.obj;
				friendList.clear();
				for(int i = 0; i < users.length; i++) {
					String [] oneUser = users[i].split("&&");
					User user = new User(oneUser[0], oneUser[1], oneUser[2]);
					friendList.add(user);
				}
				adapter.notifyDataSetChanged();
				
			} else if(msg.what == 0x3) {
				listView.refreshComplete();
			} else if(msg.what == 0x4) {
				Toast.makeText(FriendListActivity.this, "你还没有好友", Toast.LENGTH_SHORT).show();
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friend);
		listView = (RefreshListView) findViewById(R.id.id_listview);
		adapter = new MyFriendListAdapter();
		listView.setAdapter(adapter);
		listView.setInterface(new OnRefreshListener(), new OnloadMoreListener());
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String author = friendList.get(position - 1).uname;
				if(author.equals(ClientThread.getUserName())) {
					Intent intent = new Intent(FriendListActivity.this,MyPersonalActivity.class);
					FriendListActivity.this.startActivity(intent);
				} else {
//					Intent intent = new Intent(FriendListActivity.this,PersonActivity.class);
//					intent.putExtra(PersonActivity.WHO, author);
//					FriendListActivity.this.startActivity(intent);
					showDialog(author);
				}
				
			}

			private void showDialog(String s) {
				// TODO Auto-generated method stub
				FragmentTransaction ft = getFragmentManager().beginTransaction();
				DialogFragment newFragment = new PersonFragment();
				Bundle args = new Bundle();
				args.putString("username", s);
				newFragment.setArguments(args);
				newFragment.show(ft, "dialog");
			}
			
		});
		getData();
	}
	
	
	private void getData() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				new ClientThread().println("getMyFriends");
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


	private class MyFriendListAdapter extends BaseAdapter {
		
		public MyFriendListAdapter() {
			// TODO Auto-generated constructor stub
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return friendList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return friendList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if(convertView == null) {
				convertView = View.inflate(FriendListActivity.this, R.layout.friend_gridview, null);
				holder = new ViewHolder();
				holder.headImageView = (CircleImageView) convertView.findViewById(R.id.id_cirleimageview);
				holder.tt_uname = (TextView) convertView.findViewById(R.id.id_author);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			ImageLoader.getInstance().displayImage(ClientThread.getPicIp() + friendList.get(position).uname + "/head/" + friendList.get(position).headPic, holder.headImageView);
			holder.tt_uname.setText(friendList.get(position).uname);

			return convertView;
		}
		
		private class ViewHolder {
			CircleImageView headImageView;
			TextView tt_uname;
		}
		
	}
	
	private class User {
		String uname;
		String headPic;
		String uid;
		
		User(String uid, String uname, String headPic) {
			this.uid = uid;
			this.uname = uname;
			this.headPic = headPic;
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
