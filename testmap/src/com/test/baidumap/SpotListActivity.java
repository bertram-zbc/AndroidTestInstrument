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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.testmap.logic.ClientThread;

public class SpotListActivity extends Activity implements OnScrollListener {
	
	private static final int LOAD_DATA_FINISH = 0x10000;  
	
	private String city;
	  
    private List<Map<String,Object>> dataList = new ArrayList<Map<String,Object>>();  
    //list的表项计数i从0开始
    private Handler handler = new Handler() {  
        public void handleMessage(android.os.Message msg) { 
        	if(msg.what == LOAD_DATA_FINISH) {
        		if(mAdapter!=null){ 
                	//动态添加list表项
                	times++;
                	getData();
                    mAdapter.notifyDataSetChanged();  
                }  
        	}
        };  
    };  
    
    private int times;//记录刷新次数
    private String spotline = null;
    private String sendedMsg = null;
    private ListView mListView;  
  
    private View moreView;  
    private MySimpleAdapter mAdapter; 
    private TextView btnOk;
    private TextView btn_tvBack;
    
    private int lastItem;
    
    
    private List<Integer> selectedIndex = new ArrayList<Integer>();

	@Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.loadlistview);    
        
        times = 0;
        findView();
        
        spotline = SpotListActivity.this.getIntent().getExtras().getString("viewLine");
        city = SpotListActivity.this.getIntent().getStringExtra("city");
        if(city == null) {
        	city = "南京";
        }
        int viewNum = spotline.split("&&").length;
        for(int i = 0; i < viewNum; i++)
        	selectedIndex.add(i);
        new Thread(new Runnable() {
        	@Override
        	public void run() {         	
        		
				new ClientThread().println("refference&&"+times); 
            	try {
					spotline += new ClientThread().readLine();
					System.out.println(spotline);
					handler.sendEmptyMessage(LOAD_DATA_FINISH);
				} catch (IOException e) {
					e.printStackTrace();
				}  
                
				
        	}
        }).start();
  

    }  
    
    private class MySimpleAdapter extends SimpleAdapter {
    	private ImageView image_to_choose;
    	private ImageView image_urlPic;
    	
		public MySimpleAdapter(Context context,
				List<? extends Map<String, ?>> data, int resource,
				String[] from, int[] to) {
			super(context, data, resource, from, to);		
		}
		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			convertView = super.getView(position, convertView, parent);
			image_to_choose = (ImageView) convertView.findViewById(R.id.id_to_choose);
			image_urlPic = (ImageView) convertView.findViewById(R.id.image);
			image_to_choose.setImageResource(R.drawable.to_choose);
			ImageLoader.getInstance().displayImage((String) dataList.get(position).get("picUrl"), image_urlPic);
			image_to_choose.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(selectedIndex.contains(position)) {
						selectedIndex.remove(new Integer(position));
						image_to_choose.setImageResource(R.drawable.to_choose);
					} else {
						selectedIndex.add(position);
						image_to_choose.setImageResource(R.drawable.chosen);
					}
					notifyDataSetChanged();
				}
			});
			
			if(selectedIndex.contains(position)) {
				image_to_choose.setImageResource(R.drawable.chosen);
			}
			
			convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(SpotListActivity.this,SpotActivity.class);
					String vPinYin = (String) dataList.get(position).get("vPinYin");
					intent.putExtra(SpotActivity.VIEWPINYIN, vPinYin);
					startActivity(intent);
				}
			});
			
			return convertView;
		}
    	
    }
  
    private void findView() {  
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());  
        moreView = inflater.inflate(R.layout.footer_layout, null);  
        moreView.findViewById(R.id.load_layout).setVisibility(View.GONE);
        mListView = (ListView) findViewById(R.id.listview);    
        mListView.addFooterView(moreView);
     
        mAdapter =  new MySimpleAdapter(this, dataList, R.layout.item3, //
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
        btnOk = (TextView) findViewById(R.id.id_ok);
        btnOk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(selectedIndex.size() > 0) {
					sendedMsg = "querySort";
					for(int index : selectedIndex) {
						sendedMsg += "&&" + index;
					}
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							
							new ClientThread().println(sendedMsg);
							String msgRcv;
							try {
								msgRcv = new ClientThread().readLine();
								RouteActivity.finishLast();
								Intent intent = new Intent();
								intent.setClass(SpotListActivity.this, RouteActivity.class);
								Bundle bundle = new Bundle();
								bundle.putString("viewLine", msgRcv);
								intent.putExtras(bundle);
								intent.putExtra("city", city);
//								startActivityForResult(intent, 0);
								startActivity(intent);
								finish();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
						}
					}).start();
					
				}
				
			}
		});
        

    }  
    
    
    private List<? extends Map<String, ?>> getData() {
    	if(times!=0){
    	String []spots = spotline.split("&&");
    	for (int i = 0; i < spots.length; i++) {
         	String[] view = spots[i].split("%%");	
         	Map<String,Object>map = new HashMap<String, Object>();
//         	if(bList.get(i+bLnum-spots.length)==null){
//         		map.put("Image", R.drawable.ic_launcher);
//         	}else{
//         		imageView.setImageBitmap(bList.get(i+bLnum-spots.length));
//         		map.put("Image", imageView);
//        	}
//         	imageView.setImageBitmap(bList.get(i+bLnum-spots.length));
//         	map.put("Image", bList.get(i+bLnum-spots.length));
         	//map.put("Image", bList.get(i+bLnum-spots.length));
         	map.put("Image", R.drawable.imageloader);
 			map.put("Name", view[0]);
 			map.put("Message", view[3]);
 			map.put("picUrl", view[5]);
 			map.put("vPinYin", view[6].replace("http://lvyou.baidu.com/", ""));
 			map.put("openTime", view[4]);
         	dataList.add(map);
         }
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
            loadMoreData();  
        }  
    }  
    
    private void loadMoreData() {  
    	moreView.findViewById(R.id.load_layout).setVisibility(View.VISIBLE); 

        new Thread(){             
            public void run() {             
                if (spotline.split("&&").length >= 10) { 
                	new ClientThread().println("refference&&"+times); 
                	try {
						spotline = new ClientThread().readLine();
						if(spotline.equals("nothing")) {
							handler.postDelayed(new Runnable() {  
		                          
		                        @Override  
		                        public void run() {                             
		                        	TextView loadMore = (TextView) findViewById(R.id.tv_load);
		                        	loadMore.setText("没有更多信息了");  
		                        	ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
		                        	progressBar.setVisibility(View.GONE);  
		                              
		                        }  
		                    }, 1000);
							return;
						}
						handler.sendEmptyMessage(LOAD_DATA_FINISH);
						
					} catch (IOException e) {
						e.printStackTrace();
					}  
                      
                }else{ //加载到最后一页，没有数据可以加载 
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
                  
            };  
        }.start();  
        
    }  
  

}

