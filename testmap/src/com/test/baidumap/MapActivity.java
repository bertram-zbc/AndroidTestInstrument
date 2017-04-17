package com.test.baidumap;

import java.util.List;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.overlayutil.OverlayManager;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.overlayutil.TransitRouteOverlay;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.testmap.logic.Mid;
import com.testmap.logic.Pointf;
import com.testmap.view.ArcMenu;
import com.testmap.view.ArcMenu.OnMenuItemClickListener;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MapActivity extends Activity {
	
	private MapView mapview;
	private BaiduMap mymap;
	private PoiSearch mPoiSearch;
	private BDLocation mLocation=null;//我的位置
	private UiSettings mUiSettings;
	
	//卫星菜单控件
	private ArcMenu mArcmenu;
	//缩放控件自定义相关
	private ImageButton zoomInBtn;
	private ImageButton zoomOutBtn;
	
	
	//定位相关
	private LocationClient mLocationClient;
	private MyLocationListener mLocationListener;
	private boolean isFirstIn = true;
	private Context context;
	private Button btnback;
	//private Button btnpoi;
	
	RouteLine route;
	
	private String routeLine = null;
	String []view = null;
	
	//路线规划
	OverlayManager routeOverlay;
	RoutePlanSearch mSearch;
	
	//poi搜索
		private RelativeLayout relaSearch;
		private EditText editSearch;
		private ImageButton imgSearch;
		
		//导航
		private RelativeLayout relaGuide;
		private EditText editStart;
		private EditText editEnd;
		private ImageButton imgGuide;
		private String cityName;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);		
		//在使用SDK各组件之前初始化context信息，传入ApplicationContext  
        //注意该方法要再setContentView方法之前实现  
        SDKInitializer.initialize(getApplicationContext()); 
		setContentView(R.layout.activity_main);

		initView();
		initLocation();	
		initback();
		initPoi();
		initZoom();
		routeplan();
		
		//poi搜索布局绑定
				relaSearch = (RelativeLayout) findViewById(R.id.rela_search);
				editSearch = (EditText) findViewById(R.id.editsearch);
				imgSearch = (ImageButton) findViewById(R.id.imgsearch);
						
				imgSearch.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						String dest = editSearch.getText().toString();
						PoiCitySearchOption citySearchOption = new PoiCitySearchOption();
						citySearchOption.city(cityName).keyword(dest).pageCapacity(10);
						mPoiSearch.searchInCity(citySearchOption);
					}
				});
				
				
				//导航布局绑定
				relaGuide = (RelativeLayout) findViewById(R.id.rela_guide);
				editStart = (EditText) findViewById(R.id.editstart);
				editEnd = (EditText) findViewById(R.id.editend);
				imgGuide = (ImageButton) findViewById(R.id.imgguide);
				imgGuide.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						mymap.clear();
						String start = editStart.getText().toString();
						String end = editEnd.getText().toString();
						PlanNode stNode;
						PlanNode enNode;
						if(start.equals("")){
							//默认从我的位置开始
							LatLng myplace = new LatLng(mLocation.getLatitude(),mLocation.getLongitude());
							stNode = PlanNode.withLocation(myplace);
						}else{
							stNode = PlanNode.withCityNameAndPlaceName(cityName, start);
						}				
						enNode = PlanNode.withCityNameAndPlaceName(cityName, end);
						mSearch.drivingSearch((new DrivingRoutePlanOption()).from(stNode).to(enNode));
					}
				});
				
				mymap.setOnMapClickListener(new OnMapClickListener() {
					//地图点击事件
					@Override
					public boolean onMapPoiClick(MapPoi arg0) {
						// TODO Auto-generated method stub
						return false;
					}
					
					@Override
					public void onMapClick(LatLng arg0) {
						if(mArcmenu.isOpen()==true){
				        	 mArcmenu.toggleMenu(200);
				         }
						//点击地图使得覆盖物消失
						mymap.hideInfoWindow();
						//搜索布局消失
						relaSearch.setVisibility(View.GONE);
						relaGuide.setVisibility(View.GONE);
					}
				});
				

		mArcmenu = (ArcMenu) findViewById(R.id.id_menu);
		initEvent();
		
//		routeplan();
		
		
	}
			
	
	private void routeplan() {
		// 路线规划方法
		mSearch = RoutePlanSearch.newInstance();
		OnGetRoutePlanResultListener routeplanResultListener = new OnGetRoutePlanResultListener() {
			
			@Override
			public void onGetWalkingRouteResult(WalkingRouteResult result) {
				 if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			            Toast.makeText(MapActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
			        }
			        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			            //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
			            //result.getSuggestAddrInfo()
			            return;
			        }
			        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			            route = result.getRouteLines().get(0);
			            WalkingRouteOverlay overlay = new WalkingRouteOverlay(mymap);
			            mymap.setOnMarkerClickListener(overlay);
			            routeOverlay = overlay;
			            overlay.setData(result.getRouteLines().get(0));
			            overlay.addToMap();
			            overlay.zoomToSpan();
			        }
				
			}
			
			@Override
			public void onGetTransitRouteResult(TransitRouteResult result) {
				if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
		            Toast.makeText(MapActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
		        }
		        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
		            //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
		            //result.getSuggestAddrInfo()
		            return;
		        }
		        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
		            route = result.getRouteLines().get(0);
		            TransitRouteOverlay overlay = new TransitRouteOverlay(mymap);
		            mymap.setOnMarkerClickListener(overlay);
		            routeOverlay = overlay;
		            overlay.setData(result.getRouteLines().get(0));
		            overlay.addToMap();
		            overlay.zoomToSpan();
		        }
				
			}
			
			@Override
			public void onGetDrivingRouteResult(DrivingRouteResult result) {
				if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
		            Toast.makeText(MapActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
		        }
		        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
		            //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
		            //result.getSuggestAddrInfo()
		            return;
		        }
		        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
		            route = result.getRouteLines().get(0);
		            DrivingRouteOverlay overlay = new DrivingRouteOverlay(mymap);
		            routeOverlay = overlay;
		            mymap.setOnMarkerClickListener(overlay);
		            overlay.setData(result.getRouteLines().get(0));
		            overlay.addToMap();
		            overlay.zoomToSpan();
		        }
				
			}
		};
		mSearch.setOnGetRoutePlanResultListener(routeplanResultListener);
	}

	//自定义缩放控件
	private void initZoom() {
		mapview.showZoomControls(false);//隐藏缩放控件
		zoomInBtn = (ImageButton) findViewById(R.id.zoomin);
		zoomOutBtn = (ImageButton) findViewById(R.id.zoomout);
		zoomInBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				float zoomLevel = mymap.getMapStatus().zoom;
				if(zoomLevel<=18){
					mymap.setMapStatus(MapStatusUpdateFactory.zoomIn());
					zoomOutBtn.setEnabled(true);
				}else{
					Toast.makeText(MapActivity.this, "已经放至最大！", Toast.LENGTH_SHORT).show();
					zoomInBtn.setEnabled(false);
				}
			}
		});
		zoomOutBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				float zoomLevel = mymap.getMapStatus().zoom;
				if(zoomLevel>4){
					mymap.setMapStatus(MapStatusUpdateFactory.zoomOut());
					zoomInBtn.setEnabled(true);
				}else{
					zoomOutBtn.setEnabled(false);
					Toast.makeText(MapActivity.this, "已经缩至最小！", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
	}

	private void initEvent() {
		mapview.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int touchEvent = event.getAction();  
		        switch (touchEvent) {
		        case MotionEvent.ACTION_DOWN:
		        case MotionEvent.ACTION_UP:
		        case MotionEvent.ACTION_MOVE:
		         if(mArcmenu.isOpen()==true){
		        	 mArcmenu.toggleMenu(300);
		         }break;
		        default:  
		            break;  
		        }
				return false;
			}
		});
		
		//卫星菜单点击处理事件
		mArcmenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public void onClick(View view, int pos) {
				switch (pos) {
				//从上到下递增，最上为1
				case 1:
					Toast.makeText(MapActivity.this, "正在搜索附近酒店", Toast.LENGTH_SHORT).show();
					PoiNearbySearchOption nearBySearchOption = new PoiNearbySearchOption();
					nearBySearchOption.location(new LatLng(mLocation.getLatitude(), mLocation.getLongitude()));
					nearBySearchOption.keyword("酒店");
					nearBySearchOption.radius(10000);
					nearBySearchOption.pageCapacity(10);//检索出的对象的个数
					mPoiSearch.searchNearby(nearBySearchOption);
					break;
				case 2:
					Toast.makeText(MapActivity.this, "正在搜索附近购物中心", Toast.LENGTH_SHORT).show();
					PoiNearbySearchOption nearBySearchOption2 = new PoiNearbySearchOption();
					nearBySearchOption2.location(new LatLng(mLocation.getLatitude(), mLocation.getLongitude()));
					nearBySearchOption2.keyword("超市");
					nearBySearchOption2.radius(10000);
					nearBySearchOption2.pageCapacity(10);//检索出的对象的个数
					mPoiSearch.searchNearby(nearBySearchOption2);
					break;
				
				case 3:
					relaGuide.setVisibility(View.GONE);
					relaSearch.setVisibility(View.VISIBLE);
//					editSearch.setFocusable(true);
//					editSearch.requestFocus();
					break;
					
				case 4:
					relaGuide.setVisibility(View.VISIBLE);
					editEnd.setFocusable(true);
					editEnd.requestFocus();
					editStart.setText("");
					editEnd.setText("");
					break;
					
				default:
					break;
				}
				
			}
		});
	}



//	public boolean onTouchEvent(MotionEvent event) {  
//        int touchEvent = event.getAction();  
//
//        switch (touchEvent) {
//        case MotionEvent.ACTION_DOWN:
//        case MotionEvent.ACTION_UP:
//        case MotionEvent.ACTION_MOVE:
//         if(mArcmenu.isOpen()==true){
//        	 mArcmenu.toggleMenu(300);
//         }break;
//        default:  
//            break;  
//        }  
//        return true;  
//    }



	private void initPoi() {
		//通过POI对周边检索
		//btnpoi = (Button) findViewById(R.id.btnpoi);
		mPoiSearch = PoiSearch.newInstance();
		OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener() {
			
			@Override
			public void onGetPoiResult(PoiResult result) {
				if (result == null
						|| result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
					Toast.makeText(MapActivity.this, "未找到结果", Toast.LENGTH_LONG)
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
					Toast.makeText(MapActivity.this, strInfo, Toast.LENGTH_LONG)
							.show();
				}
				mymap.clear();
				//添加动画效果，移动到某一个景点poi
				List<PoiInfo> pois = result.getAllPoi();
				PoiInfo tour = pois.get(0);
				Pointf[] pointfs = new Pointf[pois.size()];
				for(int i=0; i<pois.size();i++){
					pointfs[i] = new Pointf((float)pois.get(i).location.latitude, (float)pois.get(i).location.longitude);
				}
				Mid mid = new Mid(pointfs);
				Pointf pf = mid.getPoint(); 
				LatLng latlng = new LatLng(pf.getX(),pf.getY());
				MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latlng);
				mymap.animateMapStatus(msu);
				
				
				PoiOverlay overlay = new MyPoiOverlay(mymap);
				mymap.setOnMarkerClickListener(overlay);
				overlay.setData(result);
				overlay.addToMap();
				overlay.zoomToSpan();
			}
			
			@Override
			public void onGetPoiDetailResult(PoiDetailResult result) {
				
				if (result.error != SearchResult.ERRORNO.NO_ERROR) {
					Toast.makeText(MapActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT)
							.show();
				} 
				else {
					Toast.makeText(MapActivity.this, result.getName() + ": " + result.getAddress(), Toast.LENGTH_LONG)
					.show();
					//这里还需要添加更多功能
				}
			}
		};
		mPoiSearch.setOnGetPoiSearchResultListener(poiListener);
//		btnpoi.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
				//周边检索
				//mymap.clear();
//				PoiNearbySearchOption nearBySearchOption = new PoiNearbySearchOption();
//				nearBySearchOption.location(new LatLng(mLocation.getLatitude(), mLocation.getLongitude()));
//				nearBySearchOption.keyword("酒店");
//				nearBySearchOption.radius(10000);
//				nearBySearchOption.pageCapacity(10);//检索出的对象的个数
//				mPoiSearch.searchNearby(nearBySearchOption);
				
				//城市景点检索
//				PoiCitySearchOption citySearchOption = new PoiCitySearchOption();
//				citySearchOption.city("保定").keyword("正润生态园").pageCapacity(5);
//				mPoiSearch.searchInCity(citySearchOption);
//			}
//		});
	}	
	
	
	public class MyPoiOverlay extends PoiOverlay {

		public MyPoiOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}

		@Override
		public boolean onPoiClick(int index) {
			super.onPoiClick(index);
			PoiInfo poi = getPoiResult().getAllPoi().get(index);
			// if (poi.hasCaterDetails) {
				mPoiSearch.searchPoiDetail((new PoiDetailSearchOption())
						.poiUid(poi.uid));
			// }
			return true;
		}
	}

	private void initback() {
		// 点击按钮回到我的位置
		btnback = (Button) findViewById(R.id.btnback);
		btnback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				LatLng latlng = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
				MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latlng);
				mymap.animateMapStatus(msu);
				Toast.makeText(context,"当前位置："+ mLocation.getAddrStr(), Toast.LENGTH_SHORT).show();
				
				
			}
		});
	}

	private void initLocation() {
		// 初始化定位的方法
		mLocationClient = new LocationClient(this);
		mLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mLocationListener);
		
		
		LocationClientOption option = new LocationClientOption();
		option.setCoorType("bd09ll");
		option.setIsNeedAddress(true);
		option.setOpenGps(true);
		option.setScanSpan(3000);
		mLocationClient.setLocOption(option);
		this.context = this;
	}

	private void initView() {
		
		mapview = (MapView) findViewById(R.id.id_bmapView);
		mymap = mapview.getMap();
		MapStatusUpdate msu =MapStatusUpdateFactory.zoomTo(15.0f);
		mymap.setMapStatus(msu);
	}
	
	public void setZoomEnable(View v) {
		mUiSettings.setZoomGesturesEnabled(((CheckBox) v).isChecked());
	}

	/**
	 * 是否启用平移手势
	 * 
	 * @param v
	 */
	public void setScrollEnable(View v) {
		mUiSettings.setScrollGesturesEnabled(((CheckBox) v).isChecked());
	}

	/**
	 * 是否启用旋转手势
	 * 
	 * @param v
	 */
	public void setRotateEnable(View v) {
		mUiSettings.setRotateGesturesEnabled(((CheckBox) v).isChecked());
	}

	/**
	 * 是否启用俯视手势
	 * 
	 * @param v
	 */
	public void setOverlookEnable(View v) {
		mUiSettings.setOverlookingGesturesEnabled(((CheckBox) v).isChecked());
	}

	/**
	 * 是否启用指南针图层
	 * 
	 * @param v
	 */
	public void setCompassEnable(View v) {
		mUiSettings.setCompassEnabled(((CheckBox) v).isChecked());
	}
	
	@Override
	protected void onDestroy() {
		
		super.onDestroy();
		mapview.onDestroy();
	}
	
	@Override
	protected void onResume() {
		
		super.onResume();
		mapview.onResume();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
		//开启定位
		mymap.setMyLocationEnabled(true);
		if(!mLocationClient.isStarted()){
			mLocationClient.start();
		}
	}
	
	
	
	@Override
	protected void onPause() {
		
		super.onPause();
		mapview.onResume();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		
		//关闭定位
		mymap.setMyLocationEnabled(false);
		mLocationClient.stop();
	}
	
	private class MyLocationListener implements BDLocationListener
	{
		
		@Override
		public void onReceiveLocation(BDLocation location) {
			// 定位成功后回调数据
			MyLocationData data = new MyLocationData.Builder()//
									.accuracy(location.getRadius())//
									.latitude(location.getLatitude())//
									.longitude(location.getLongitude()).build();
			
			mymap.setMyLocationData(data);
			
			//每次定位成功后更新经纬度数据并保存在mLocation中
			mLocation = location;
			cityName = location.getCity();
			
			if(isFirstIn)
			{
				//用户第一次登入定位时将用户的位置移到屏幕中心
				LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
				MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latlng);
				mymap.animateMapStatus(msu);
				
				isFirstIn = false;
				//提示当前位置
				Toast.makeText(context,"当前位置："+ location.getAddrStr(), Toast.LENGTH_LONG).show();
			}
		}
		
	}
	
}
