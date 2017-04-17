package com.testmap.logic;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap.Config;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.imagepager.ImagePagerActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.test.baidumap.CityActivity;
import com.test.baidumap.MyPersonalActivity;
import com.test.baidumap.R;
import com.test.baidumap.WebInfoActivity;
import com.test.fragment.PersonFragment;
import com.testmap.view.CircleImageView;


public class ListItemAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<Mood> moodsList;
	private int clickPosition;
	private int clickSoftInputPosition;
	private InputMethodManager imm;
	
	
	private Handler handler = new Handler() {
		private Mood getMood(ArrayList<Mood> mList, int mid) {
			for(int i = 0; i < mList.size(); i++) {
				if(mList.get(i).getMid() == mid)
					return mList.get(i);
			}
			return null;
		}
		public void handleMessage(android.os.Message msg) {
			if(msg.what == 0x1) {
				Toast.makeText(mContext, "评论成功", Toast.LENGTH_SHORT).show();
				Comment comment = (Comment) msg.obj;
				Mood mood = getMood(moodsList, comment.getMid());
				if(mood != null) {
					if(comment.getType() == 0) {
						mood.cList.add(comment);
					} else if(comment.getType() == 1) {
						int cIndex;
						for(cIndex = 0; cIndex < mood.cList.size(); cIndex++) {
							if(mood.cList.get(cIndex).getCid() == comment.getpCid()) {
								break;
							}
						}
						for(cIndex++; cIndex < mood.cList.size(); cIndex++) {
							if(mood.cList.get(cIndex).getpCid() != comment.getpCid()) {
								break;
							}
						}
						mood.cList.add(cIndex, comment);
					} else {
						Toast.makeText(mContext, "no,error", Toast.LENGTH_SHORT);
					}
					
					mood.adapter.notifyDataSetChanged();
				}
			} else if(msg.what == 0x2) {
				Toast.makeText(mContext, (String) msg.obj, Toast.LENGTH_SHORT).show();
			} else if(msg.what == 0x3) {
				notifyDataSetChanged();
			}
		};
	};
	
	
	public ListItemAdapter(Context ctx, ArrayList<Mood> moodsList) {
		this.mContext = ctx;
		this.moodsList = moodsList;
		imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
		clickPosition = -1;
		clickSoftInputPosition = -1;
	}

	@Override
	public int getCount() {
		return moodsList == null ? 0 : moodsList.size();
	}

	@Override
	public Object getItem(int position) {
		return moodsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.item4, null);
			holder.img_head = (CircleImageView) convertView.findViewById(R.id.circle_head);
			holder.name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.content = (TextView) convertView.findViewById(R.id.tv_content);
			holder.gridview = (NoScrollGridView) convertView.findViewById(R.id.gridview);
			holder.zannum = (TextView) convertView.findViewById(R.id.zannum);
			holder.image_zan = (ImageView) convertView.findViewById(R.id.image_zan);
			holder.image_comment = (ImageView) convertView.findViewById(R.id.image_comment);
			holder.comments_listview = (NoScrollListview) convertView.findViewById(R.id.id_comments_listview);
			holder.et_comment = (EditText) convertView.findViewById(R.id.id_et_comment);
			holder.tt_publishtime = (TextView) convertView.findViewById(R.id.id_publishtime);
			holder.btn_publish = (ImageButton) convertView.findViewById(R.id.id_btn_publish);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final Mood mood = moodsList.get(position);
		holder.name.setText(mood.getAuthor());
		holder.content.setText(mood.getContent());
		holder.zannum.setText(mood.getzNum() + "");
		holder.tt_publishtime.setText(mood.getPublishTime().replaceAll("：", ":"));
		holder.et_comment.requestFocus();
		if(clickSoftInputPosition != position) {
			holder.btn_publish.setVisibility(View.GONE);
			holder.et_comment.setVisibility(View.GONE);
		} else {
			holder.btn_publish.setVisibility(View.VISIBLE);
			holder.et_comment.setVisibility(View.VISIBLE);
		}
		holder.comments_listview.setVisibility(View.GONE);
		if(mood.cList != null && mood.cList.size() > 0)
			holder.comments_listview.setVisibility(View.VISIBLE);
		if(mood.adapter == null) {
			mood.adapter = new MyCommentsAdapter(mContext, mood.cList);
		}
		if(mood.isZan()) {
			holder.image_zan.setImageResource(R.drawable.zan_1);
		} else {
			holder.image_zan.setImageResource(R.drawable.zan);
		}
		if(clickPosition == -1 && clickSoftInputPosition == position) {
			holder.image_comment.setImageResource(R.drawable.comment_1);
		} else {
			holder.image_comment.setImageResource(R.drawable.comment);
		}
		holder.comments_listview.setAdapter(mood.adapter);
		
		// 使用ImageLoader加载网络图片
		DisplayImageOptions options = new DisplayImageOptions.Builder()//
				.showImageOnLoading(R.drawable.ic_launcher) // 加载中显示的默认图片
				.showImageOnFail(R.drawable.ic_launcher) // 设置加载失败的默认图片
				.cacheInMemory(true)  // 内存缓存
				.cacheOnDisk(true) // sdcard缓存
				.bitmapConfig(Config.RGB_565)// 设置最低配置
				.build();//
		ImageLoader.getInstance().displayImage(mood.getHeadPic(), holder.img_head, options);
		holder.img_head.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String author = moodsList.get(position).getAuthor();
				if(author.equals(ClientThread.getUserName())) {
					Intent intent = new Intent(mContext,MyPersonalActivity.class);
					mContext.startActivity(intent);
				} else {
//					Intent intent = new Intent(mContext,PersonActivity.class);
//					intent.putExtra(PersonActivity.WHO, author);
//					mContext.startActivity(intent);
					showDialog(author);
				}
			}
			
			private void showDialog(String s) {
				// TODO Auto-generated method stub
				FragmentTransaction ft = ((Activity) mContext).getFragmentManager().beginTransaction();
				DialogFragment newFragment = new PersonFragment();
				Bundle args = new Bundle();
				args.putString("username", s);
				newFragment.setArguments(args);
				newFragment.show(ft, "dialog");
			}

		});
		
		
		final ArrayList<String> imageUrls = mood.getAlPicUrls();
		if (imageUrls == null || imageUrls.size() == 0) { // 没有图片资源就隐藏GridView
			holder.gridview.setVisibility(View.GONE);
		} else {
			holder.gridview.setVisibility(View.VISIBLE);
			holder.gridview.setAdapter(new NoScrollGridAdapter(mContext, imageUrls));
		}
		// 点击回帖九宫格，查看大图
		holder.gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				imageBrower(position, imageUrls);
			}
		});
		final EditText commentEditText = holder.et_comment;
		final ImageButton btn_OK = holder.btn_publish;
		final ImageView image_zan = holder.image_zan;
		//点赞/撤销赞
		holder.image_zan.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mood.isZan()) {
					image_zan.setImageResource(R.drawable.zan);
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							new ClientThread().println("deleZan&&" + mood.getMid());
							try {
								
								String recvString = new ClientThread().readLine();
								Message msg = new Message();
								msg.obj = "取消赞";
								msg.what = 0x2;
								if(recvString.equals("success")) {
									mood.setzNum(mood.getzNum() - 1);
									mood.setZan(false);
									handler.sendEmptyMessage(0x3);
								} else {
									msg.obj = "取消赞";
								}
								handler.sendMessage(msg);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
						}
					}).start();
				} else {
					image_zan.setImageResource(R.drawable.zan_1);
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							new ClientThread().println("dianZan&&" + mood.getMid() + "&&" + "0");
							try {
								String recvString = new ClientThread().readLine();
								Message msg = new Message();
								msg.obj = "已赞";
								msg.what = 0x2;
								if(recvString.equals("success")) {
									mood.setzNum(mood.getzNum() + 1);
									mood.setZan(true);
									handler.sendEmptyMessage(0x3);
								} else {
									msg.obj = "赞失败";
								}
								handler.sendMessage(msg);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
						}
					}).start();
				}
				
			}
		});
		//评论
		holder.image_comment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				clickPosition = -1;
				clickSoftInputPosition = position;
				commentEditText.setText("");
				commentEditText.setHint("评论：");
				commentEditText.setVisibility(View.VISIBLE);
				btn_OK.setVisibility(View.VISIBLE);
				commentEditText.requestFocus();
				imm.showSoftInput(commentEditText, InputMethodManager.RESULT_SHOWN);
				imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
						InputMethodManager.HIDE_IMPLICIT_ONLY);
			}
		});
		final int position2 = position;
		holder.comments_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				clickPosition = position;
				clickSoftInputPosition = position2;
				commentEditText.setText("");
				commentEditText.setHint("回复" + mood.cList.get(clickPosition).getAuthor() + "：");
				commentEditText.setVisibility(View.VISIBLE);
				btn_OK.setVisibility(View.VISIBLE);
				commentEditText.findFocus();
				commentEditText.requestFocus();
				imm.showSoftInput(commentEditText, InputMethodManager.RESULT_SHOWN);
				imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
						InputMethodManager.HIDE_IMPLICIT_ONLY);
				
			}
		});
		holder.btn_publish.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				imm.hideSoftInputFromWindow(commentEditText.getWindowToken(), 0);
				clickSoftInputPosition = -1;
				
				String content = commentEditText.getText().toString();
				if(content.contains("&&") || content.contains("%%") || content.contains("##")) {
					Toast.makeText(mContext, "文字内容不能包含\"&&\",\"%%\"和\"##\"", Toast.LENGTH_SHORT).show();
					return;
				}
				
				
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						String content = commentEditText.getText().toString();
						int cType = 0;
						int replyCid = 0;
						int pCid = 0;
						String replyUname = "。";
						if(clickPosition >= 0) {
							cType = 1;
							Comment clickComment = moodsList.get(position).cList.get(clickPosition);
							replyCid = clickComment.getCid();
							replyUname = clickComment.getAuthor();
							pCid = clickComment.getpCid();
							if(pCid == 0) {
								pCid = replyCid;
							}
						}
						new ClientThread().println("publishComment&&" + content + "&&"
								+ moodsList.get(position).getMid() + "&&" + cType + "&&"
								+ replyCid + "&&" + pCid);
						try {
							String recvString = new ClientThread().readLine();
							String [] recvArray = recvString.split("&&");
							if(recvArray[0].equals("success")) {
								Message msg = new Message();
								msg.what = 0x1;
								msg.obj = new Comment(recvArray[1], ClientThread.getUserName(), moodsList.get(position).getMid()+"", content, recvArray[2], cType + "", replyCid + "", pCid + "", replyUname);
								handler.sendMessage(msg);
							} else {
								Message msg = new Message();
								msg.what = 0x2;
								msg.obj = "评论失败";
								handler.sendMessage(msg);
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
				}).start();
				
			}
		});
		
		
		return convertView;
	}

	/**
	 * 打开图片查看器
	 * 
	 * @param position
	 * @param urls2
	 */
	protected void imageBrower(int position, ArrayList<String> urls2) {
		Intent intent = new Intent(mContext, ImagePagerActivity.class);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
		mContext.startActivity(intent);
	}

	/**
	 * listview组件复用，防止“卡顿”
	 * 
	 * @author Administrator
	 * 
	 */
	class ViewHolder {
		private CircleImageView img_head;
		private TextView name;
		private TextView content;
		private NoScrollGridView gridview;
		private TextView zannum;
		private ImageView image_zan;
		private ImageView image_comment;
		private NoScrollListview comments_listview;
		private EditText et_comment;
		private TextView tt_publishtime;
		private ImageButton btn_publish;
	}
	
	public class MyCommentsAdapter extends BaseAdapter {
		private Context mContext;
		private ArrayList<Comment> cList;
		
		
		private class CommentViewHolder {
			private TextView tt_author;
			private TextView tt_type;
			private TextView tt_to_who;
			private TextView tt_content;
		}
		
		
		
		public MyCommentsAdapter(Context ctx, ArrayList<Comment> cList) {
			this.mContext = ctx;
			this.cList = cList;
		}
		
		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			CommentViewHolder holder = null;
			if(convertView == null) {
				holder = new CommentViewHolder();
				convertView = View.inflate(mContext, R.layout.comments_gridview, null);
				holder.tt_author = (TextView) convertView.findViewById(R.id.id_author);
				holder.tt_type = (TextView) convertView.findViewById(R.id.id_type);
				holder.tt_to_who = (TextView) convertView.findViewById(R.id.id_to_who);
				holder.tt_content = (TextView) convertView.findViewById(R.id.id_content);
				convertView.setTag(holder);
			} else {
				holder = (CommentViewHolder)convertView.getTag();
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
						Intent intent = new Intent(ListItemAdapter.this.mContext,MyPersonalActivity.class);
						ListItemAdapter.this.mContext.startActivity(intent);
					} else {
						showDialog(author);
					}
					
				}
				
				private void showDialog(String s) {
					// TODO Auto-generated method stub
					FragmentTransaction ft = ((Activity) mContext).getFragmentManager().beginTransaction();
					DialogFragment newFragment = new PersonFragment();
					Bundle args = new Bundle();
					args.putString("username", s);
					newFragment.setArguments(args);
					newFragment.show(ft, "dialog");
				}
			});
			holder.tt_to_who.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					String author = cList.get(position).getReplyUname();
					if(author.equals(ClientThread.getUserName())) {
						Intent intent = new Intent(ListItemAdapter.this.mContext,MyPersonalActivity.class);
						ListItemAdapter.this.mContext.startActivity(intent);
					} else {
						showDialog(author);
					}
					
				}
				private void showDialog(String s) {
					// TODO Auto-generated method stub
					FragmentTransaction ft = ((Activity) mContext).getFragmentManager().beginTransaction();
					DialogFragment newFragment = new PersonFragment();
					Bundle args = new Bundle();
					args.putString("username", s);
					newFragment.setArguments(args);
					newFragment.show(ft, "dialog");
				}
			});
			return convertView;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return cList == null ? 0 : cList.size();
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
		
	}
	
}
