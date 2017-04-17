package com.test.baidumap;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.test.fragment.PersonFragment;
import com.testmap.logic.ClientThread;
import com.testmap.logic.RefreshListView;
import com.testmap.logic.RefreshListView.ILoadMoreDataListener;
import com.testmap.logic.RefreshListView.IRefreshListener;
import com.testmap.logic.WebInfo;

public class WebInfoActivity extends Activity{
	private ArrayList<WebInfo> wList = new ArrayList<WebInfo>();
	private MyWebinfoListAdapter adapter;
	private RefreshListView listView;
	private EditText et_webinfo;
	private Button btn_publish_webinfo;
	private TextView type;
	private String replyUname;
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if(msg.what == 0x1) {
				Toast.makeText(WebInfoActivity.this, "服务器失联，请重试", Toast.LENGTH_SHORT).show();
			} else if(msg.what == 0x2) {
				String [] aWebinfos = (String[]) msg.obj;
				wList.clear();
				for(int i = 0; i < aWebinfos.length; i++) {
					String [] oneWebInfo = aWebinfos[i].split("&&");
					if(oneWebInfo.length < 6) {
						return;
					}
					WebInfo webinfo = new WebInfo(oneWebInfo[0], oneWebInfo[1], oneWebInfo[2], oneWebInfo[3], oneWebInfo[4], oneWebInfo[5]);
					wList.add(webinfo);
				}
				adapter.notifyDataSetChanged();
				
			} else if(msg.what == 0x3) {
				listView.refreshComplete();
			} else if(msg.what == 0x4) {
				Toast.makeText(WebInfoActivity.this, "你还没有站内信消息", Toast.LENGTH_SHORT).show();
			} else if(msg.what == 0x5) {
				Toast.makeText(WebInfoActivity.this, (String) msg.obj, Toast.LENGTH_SHORT).show();
				et_webinfo.setVisibility(View.GONE);
				btn_publish_webinfo.setVisibility(View.GONE);
				type.setVisibility(View.GONE);
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview);
		listView = (RefreshListView) findViewById(R.id.id_listview);
		et_webinfo = (EditText) findViewById(R.id.id_et_webinfo);
		btn_publish_webinfo = (Button) findViewById(R.id.id_btn_publish_webinfo);
		type = (TextView) findViewById(R.id.id_type);
		adapter = new MyWebinfoListAdapter();
		listView.setAdapter(adapter);
		listView.setInterface(new OnRefreshListener(), new OnloadMoreListener());
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				WebInfo webinfo = wList.get(position - 1);
				replyUname = webinfo.getAuthor();
				type.setText("回复" + replyUname + "：");
				et_webinfo.setText("");
				type.setVisibility(View.VISIBLE);
				et_webinfo.setVisibility(View.VISIBLE);
				btn_publish_webinfo.setVisibility(View.VISIBLE);
				et_webinfo.requestFocus();
				InputMethodManager imm = (InputMethodManager) WebInfoActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.showSoftInput(et_webinfo, InputMethodManager.RESULT_SHOWN);
				imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
						InputMethodManager.HIDE_IMPLICIT_ONLY);
				
			}
			
		});
		btn_publish_webinfo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				InputMethodManager imm = (InputMethodManager) WebInfoActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(et_webinfo.getWindowToken(), 0);
				String content = et_webinfo.getText().toString();
				if(content.contains("&&") || content.contains("%%") || content.contains("##")) {
					Toast.makeText(WebInfoActivity.this, "文字内容不能包含\"&&\",\"%%\"和\"##\"", Toast.LENGTH_SHORT).show();
					return;
				}
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						new ClientThread().println("publishWebInfo&&" + replyUname + "&&" + et_webinfo.getText().toString() + "&&0");
						try {
							String recvString = new ClientThread().readLine();
							Message msg = new Message();
							msg.obj = recvString;
							msg.what = 0x5;
							handler.sendMessage(msg);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						getData();
					}
				}).start();
			}
		});
		getData();
	}
	
	
	private void getData() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				new ClientThread().println("getRelaWebInfo");
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


	private class MyWebinfoListAdapter extends BaseAdapter {
		
		public MyWebinfoListAdapter() {
			// TODO Auto-generated constructor stub
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return wList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return wList.get(position);
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
				convertView = View.inflate(WebInfoActivity.this, R.layout.webinfo_gridview, null);
				holder = new ViewHolder();
				holder.tt_author = (TextView) convertView.findViewById(R.id.id_author);
				holder.tt_type = (TextView) convertView.findViewById(R.id.id_type);
				holder.tt_to_who = (TextView) convertView.findViewById(R.id.id_to_who);
				holder.tt_content = (TextView) convertView.findViewById(R.id.id_content);
				holder.tt_say = (TextView) convertView.findViewById(R.id.id_say);
				holder.tt_publishtime = (TextView) convertView.findViewById(R.id.id_publish_time);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			WebInfo webinfo = wList.get(position);
			holder.tt_author.setText(webinfo.getAuthor().equals(ClientThread.getUserName()) ? "我" : webinfo.getAuthor());
			holder.tt_publishtime.setText(webinfo.getPublishTime().replaceAll("：", ":"));
			holder.tt_to_who.setText(webinfo.getToWho().equals(ClientThread.getUserName()) ? "我" : webinfo.getToWho());
			if(webinfo.getType() == 0) {
				holder.tt_type.setText("对");
				holder.tt_say.setText("说：");
				holder.tt_content.setText(webinfo.getContent());
			} else {
				holder.tt_type.setText("已经加");
				holder.tt_say.setText("");
				holder.tt_content.setText("为好友，(可点击TA的名字加TA为好友)");
			}
			
			
			holder.tt_author.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					String author = wList.get(position).getAuthor();
					if(author.equals(ClientThread.getUserName())) {
						Intent intent = new Intent(WebInfoActivity.this,MyPersonalActivity.class);
						WebInfoActivity.this.startActivity(intent);
					} else {
//						Intent intent = new Intent(WebInfoActivity.this,PersonActivity.class);
//						intent.putExtra(PersonActivity.WHO, author);
//						WebInfoActivity.this.startActivity(intent);
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
					String author = wList.get(position).getToWho();
					if(author.equals(ClientThread.getUserName())) {
						Intent intent = new Intent(WebInfoActivity.this,MyPersonalActivity.class);
						WebInfoActivity.this.startActivity(intent);
					} else {
//						Intent intent = new Intent(WebInfoActivity.this,PersonActivity.class);
//						intent.putExtra(PersonActivity.WHO, author);
//						WebInfoActivity.this.startActivity(intent);
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
			private TextView tt_say;
			private TextView tt_publishtime;
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

