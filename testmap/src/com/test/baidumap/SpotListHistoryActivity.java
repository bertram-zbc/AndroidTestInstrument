package com.test.baidumap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.testmap.logic.ClientThread;

public class SpotListHistoryActivity extends Activity implements OnScrollListener {
	
	  
    private List<Map<String,Object>> dataList = new ArrayList<Map<String,Object>>();  
    //list的表项计数i从0开始
    private Handler handler = new Handler() {  
        public void handleMessage(android.os.Message msg) { 
        	if(msg.what == 0x1) {
        		if(mAdapter!=null){ 
                	//动态添加list表项
                	getData();
                    mAdapter.notifyDataSetChanged();  
                }  
        	} else if(msg.what == 0x2) {
        		Toast.makeText(SpotListHistoryActivity.this, "评分成功", Toast.LENGTH_SHORT).show();
        	}
        };  
    };  
    
    private String spotline = null;
    private ListView mListView;  
  
    private View moreView;  
    private MySimpleAdapter mAdapter; 
    private TextView btn_tvBack;
    
    private int lastItem;
    
    private String city;
    

	@Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.loadlistview);    

        findView();
        
        spotline = SpotListHistoryActivity.this.getIntent().getExtras().getString("viewLine");
        city = SpotListHistoryActivity.this.getIntent().getExtras().getString("city");
        if(city == null) {
        	city = "南京";
        }
        new Thread(new Runnable() {
        	@Override
        	public void run() { 
					handler.sendEmptyMessage(0x1);
        	}
        }).start();
  

    }  
    
    private class MySimpleAdapter extends SimpleAdapter {
    	private ImageView image_urlPic;
    	private Button btn_mark;
    	private RatingBar rb_mark;
    	private Button btn_comment;
    	private int isMarkingPosition;
    	
		public MySimpleAdapter(Context context,
				List<? extends Map<String, ?>> data, int resource,
				String[] from, int[] to) {
			super(context, data, resource, from, to);
			isMarkingPosition = -1;
		}
		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			convertView = super.getView(position, convertView, parent);
			image_urlPic = (ImageView) convertView.findViewById(R.id.image);
			ImageLoader.getInstance().displayImage((String) dataList.get(position).get("picUrl"), image_urlPic);
			btn_mark = (Button) convertView.findViewById(R.id.id_btn_mark);
			btn_comment = (Button) convertView.findViewById(R.id.id_btn_comment);
			rb_mark = (RatingBar) convertView.findViewById(R.id.id_mark);
			if(isMarkingPosition != position) {
				rb_mark.setVisibility(View.GONE);
				btn_mark.setText("评分");
			} else {
				rb_mark.setVisibility(View.VISIBLE);
				btn_mark.setText("确认评分");
			}
			btn_mark.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(isMarkingPosition == position) {
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							new ClientThread().println("mark&&" + dataList.get(position).get("vPinYin") + "&&" + rb_mark.getRating());
							try {
								String recvString = new ClientThread().readLine();
								if(recvString.equals("success")) {
									handler.sendEmptyMessage(0x2);
								}
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}).start();
						isMarkingPosition = -1;
						btn_mark.setText("评分");
						mAdapter.notifyDataSetChanged();
					} else {
						isMarkingPosition = position;
						rb_mark.setVisibility(View.VISIBLE);
						btn_mark.setText("确认评分");
						mAdapter.notifyDataSetChanged();
					}
					
				}
			});
			btn_comment.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(SpotListHistoryActivity.this,ZoneActivity.class);
					String vPinYin = (String) dataList.get(position).get("vPinYin");
					String vName = (String) dataList.get(position).get("Name");
					intent.putExtra("view", vPinYin);
					intent.putExtra("region", city);
					intent.putExtra("vName", vName);
					startActivity(intent);
					
				}
			});
			convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(SpotListHistoryActivity.this,SpotActivity.class);
					String vPinYin = (String) dataList.get(position).get("vPinYin");
					intent.putExtra(SpotActivity.VIEWPINYIN, vPinYin);
					startActivity(intent);
				}
			});
			
			return convertView;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return dataList.size();
		}
    	
    }
  
    private void findView() {  
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());  
        moreView = inflater.inflate(R.layout.footer_layout, null);  
        moreView.findViewById(R.id.load_layout).setVisibility(View.GONE);
        mListView = (ListView) findViewById(R.id.listview);    
        mListView.addFooterView(moreView);
     
        mAdapter =  new MySimpleAdapter(this, dataList, R.layout.item_mark, //
        		new String[]{"Image","Name","Message", "openTime"}, 
				new int[]{R.id.image,R.id.view_name,R.id.view_message, R.id.view_opentime});
        
        mAdapter.setViewBinder(new ViewBinder() {  
            
            @Override  
            public boolean setViewValue( View view, Object data, String textRepresentation) {    
                 if((view instanceof ImageView) && (data instanceof Bitmap)) {    
                        ImageView imageView = (ImageView) view;    
                        Bitmap bmp = (Bitmap) data;    
                        imageView.setImageBitmap(bmp); 
                        return true;    
                    }    
                return false;  
            }  
        });  
        
        mListView.setAdapter(mAdapter);
        mListView.setOnScrollListener(this);
        btn_tvBack = (TextView) findViewById(R.id.tvback);
        btn_tvBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
        

    }  
    
    
    private List<? extends Map<String, ?>> getData() {
    	String []spots = spotline.split("&&");
    	for (int i = 0; i < spots.length; i++) {
         	String[] view = spots[i].split("%%");	
         	Map<String,Object>map = new HashMap<String, Object>();
         	map.put("Image", R.drawable.imageloader);
 			map.put("Name", view[0]);
 			map.put("Message", view[3]);
 			map.put("picUrl", view[5]);
 			map.put("vPinYin", view[6].replace("http://lvyou.baidu.com/", ""));
 			map.put("openTime", view[4]);
         	dataList.add(map);
         }
		return dataList;
	}

 
  
    public void onScroll(AbsListView view, int firstVisibleItem,  
            int visibleItemCount, int totalItemCount) {  
  
        lastItem = firstVisibleItem + visibleItemCount - 1;  
  
    }  
  
    public void onScrollStateChanged(AbsListView view, int scrollState) {    
        if (lastItem == mAdapter.getCount()  
                && scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
            //loadMoreData();  
        }  
    }  
  

}
