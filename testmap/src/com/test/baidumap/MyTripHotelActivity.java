package com.test.baidumap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.testmap.logic.Hotel;

public class MyTripHotelActivity extends Activity implements OnItemClickListener,OnScrollListener {
	
	private SimpleAdapter adapter;
	
	private View moreView;
	private ListView listview;
	private EditText editplace;
	private ImageButton btn;
	private static ArrayList<Hotel> hotels = new ArrayList<Hotel>();
	 
	private static ArrayList<Map<String,Object>> dataList = new ArrayList<Map<String,Object>>();
	
	private GeoCoder mSearch;
	
	private double lat,lng;
	
	private PoiSearch mPoiSearch;
	
	private PoiResult poiResult;
	
	private int lastItem;
	
	private int pageNum=0;
	
	private Spinner spinner;
	private EditText cityName;
	private int radius = 1000;
//	Handler handler = new Handler(){
//		public void handleMessage(android.os.Message msg) {
//			switch (msg.what) {
//			case 0x1:
//				
//				break;
//
//			default:
//				break;
//			}
//		};
//	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext()); 
		setContentView(R.layout.hotel);

		System.out.println("ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ");
		System.out.println(hotels.size());
		System.out.println(dataList.size());

		
		nameToGrocode();
		initPoi();

//		PoiNearbySearchOption nearBySearchOption = new PoiNearbySearchOption();
//		nearBySearchOption.location(new LatLng(32.047738, 118.791579));
//		nearBySearchOption.keyword("酒店");
//		nearBySearchOption.radius(3000);
//		nearBySearchOption.pageCapacity(10);//检索出的对象的个数
//		mPoiSearch.searchNearby(nearBySearchOption);
		
//		PoiCitySearchOption citySearchOption = new PoiCitySearchOption();
//		citySearchOption.city("保定").keyword("正润生态园").pageCapacity(5);
//		mPoiSearch.searchInCity(citySearchOption);
//		
		
		editplace = (EditText) findViewById(R.id.editplace);
		System.out.println("XXXXXXXXXXXXXX"+editplace.getText().toString());
		btn = (ImageButton) findViewById(R.id.imgsearch);
		cityName = (EditText) findViewById(R.id.editcity);
		String s1 = this.getIntent().getExtras().getString("city");
		cityName.setText(s1);
		String s2 = this.getIntent().getExtras().getString("place");
		editplace.setText(s2);
		
		
		spinner = (Spinner) findViewById(R.id.spinnerradius);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				switch (position) {
				case 0:
					radius=1000;
					break;
				case 1:
					radius=3000;
				case 2:
					radius=5000;
				case 3:
					radius=10000;
				default:
					radius=3000;
					break;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		
		
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(android.view.View v) {
				// TODO Auto-generated method stub
				dataList.clear();
				hotels.clear();
				final String s = editplace.getText().toString();
				final String city = cityName.getText().toString();
				System.out.println(s+" "+city+" "+radius);
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						mSearch.geocode(new GeoCodeOption().city(city).address(s));
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						PoiNearbySearchOption nearBySearchOption = new PoiNearbySearchOption();
						nearBySearchOption.location(new LatLng(lat, lng));
						nearBySearchOption.keyword("酒店");
						nearBySearchOption.radius(radius);
						nearBySearchOption.pageNum(pageNum);//检索出的对象的个数
						mPoiSearch.searchNearby(nearBySearchOption);
						pageNum++;
					}
				}).start();
			}
		});
		
		listview = (ListView) findViewById(R.id.listView1);
		
		//dataList = new ArrayList<Map<String,Object>>();
		adapter = new SimpleAdapter(this, dataList, R.layout.item_hotel, 
				new String[]{"name","tel","add"} , new int[]{R.id.hotelname,R.id.hoteltel,R.id.hoteladd});
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(this);
		
		
		
		LayoutInflater inflater = LayoutInflater.from(getApplicationContext());  
        moreView = inflater.inflate(R.layout.footer_layout, null);  
        moreView.findViewById(R.id.load_layout).setVisibility(View.GONE);
        listview.addFooterView(moreView);
        listview.setOnScrollListener(this);
        
        
        System.out.println("YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY");
		System.out.println(hotels.size());
		System.out.println(dataList.size());
	}

	private void initPoi() {
		//通过POI对周边检索
		mPoiSearch = PoiSearch.newInstance();
		OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener() {
			
			@Override
			public void onGetPoiResult(PoiResult result) {
				if (result == null
						|| result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
					Toast.makeText(MyTripHotelActivity.this, "未找到结果", Toast.LENGTH_LONG)
					.show();
					return;
				}
				if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {

					// 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
					String strInfo = "在";
					for (CityInfo cityInfo : result.getSuggestCityList()) {
						strInfo += cityInfo.city;
						strInfo += ",";
					}
					strInfo += "找到结果";
					Toast.makeText(MyTripHotelActivity.this, strInfo, Toast.LENGTH_LONG)
							.show();
				}
				poiResult = result;
				System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+poiResult.getAllPoi().size());
				
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						for(int i=0;i<poiResult.getAllPoi().size();i++){
							System.out.println(i+poiResult.getAllPoi().get(i).name+poiResult.getAllPoi().get(i).uid);
							mPoiSearch.searchPoiDetail(new PoiDetailSearchOption().poiUid(poiResult.getAllPoi().get(i).uid));
							try {
								Thread.sleep(500);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}).start();
				
			}
			
			@Override
			public void onGetPoiDetailResult(PoiDetailResult result) {
								
				if (result.error != SearchResult.ERRORNO.NO_ERROR) {
					Toast.makeText(MyTripHotelActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT)
							.show();
				} 
				else {
					System.out.println( result.getName() + ": " + result.getAddress());
					Hotel temp = new Hotel();
					temp.setName(result.getName());
					temp.setAddress(result.getAddress());
					temp.setLatLng(result.getLocation());
					temp.setLink(result.getDetailUrl());
					temp.setPrice(result.getPrice());
					temp.setTelephone(result.getTelephone());
					hotels.add(temp);	
					//修改dataList并更新listview
					Map<String,Object>map = new HashMap<String, Object>();
					map.put("name", temp.getName());
					map.put("tel", "电话："+temp.getTelephone());
					map.put("add", "地址："+temp.getAddress());
					dataList.add(map);
					adapter.notifyDataSetChanged();
				}
			}
		};
		mPoiSearch.setOnGetPoiSearchResultListener(poiListener);
	}	
	

	private void nameToGrocode() {
		// TODO Auto-generated method stub
		mSearch = GeoCoder.newInstance();
		OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {
			
			@Override
			public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
				if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {  
		            //没有检索到结果  
		        }  
		        //获取地理编码结果  
				
			}
			
			@Override
			public void onGetGeoCodeResult(GeoCodeResult result) {
				if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {  
		            //没有检索到结果  
					Toast.makeText(MyTripHotelActivity.this, "暂无搜索信息", Toast.LENGTH_SHORT).show();
					return;
		        }  
		        //获取地理编码结果  
				lat = result.getLocation().latitude;
				lng = result.getLocation().longitude;
				System.out.println("!!!!!!!!!!!!!!!!!"+result.getLocation().latitude+"  "+result.getLocation().longitude);
				System.out.println("##################"+lat+"  "+lng);
			}
		};
		mSearch.setOnGetGeoCodeResultListener(listener);
	}

//	private List<? extends Map<String, ?>> getData() {
//		System.out.println("#############################"+hotels.size());
//		for(int i=0;i<hotels.size();i++){
//			System.out.println(i);
//			Map<String,Object>map = new HashMap<String, Object>();
//			map.put("name", hotels.get(i).getName());
//			map.put("tel", hotels.get(i).getTelephone());
//			map.put("add", hotels.get(i).getAddress());
//			dataList.add(map);
//		}
//		return dataList;
//	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		String url = hotels.get(position).getLink();
		System.out.println( hotels.get(position).getName()+ "  " +url);
		WebViewActivity.setUrl(url);
		Intent intent = new Intent(MyTripHotelActivity.this,WebViewActivity.class);
		startActivity(intent);
	}

	public void onScroll(AbsListView view, int firstVisibleItem,  
            int visibleItemCount, int totalItemCount) {  
		
        lastItem = firstVisibleItem + visibleItemCount - 1;  
  
    }  
  
    public void onScrollStateChanged(AbsListView view, int scrollState) {    
        if (lastItem == adapter.getCount()  
                && scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
            loadMoreData();  
        }  
    }

	private void loadMoreData() {
		// TODO Auto-generated method stub	
		moreView.findViewById(R.id.load_layout).setVisibility(View.VISIBLE); 
		PoiNearbySearchOption nearBySearchOption = new PoiNearbySearchOption();
		nearBySearchOption.location(new LatLng(lat, lng));
		nearBySearchOption.keyword("酒店");
		nearBySearchOption.radius(3000);
		nearBySearchOption.pageNum(pageNum);//检索出的对象的个数
		mPoiSearch.searchNearby(nearBySearchOption);
		pageNum++;
	}  
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
		System.out.println(dataList.size());
		System.out.println(hotels.size());
	}

}
