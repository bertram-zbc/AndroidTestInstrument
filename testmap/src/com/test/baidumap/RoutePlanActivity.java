package com.test.baidumap;

import java.util.ArrayList;
import java.util.List;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.UiSettings;
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
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.testmap.logic.MapInfo;
import com.testmap.logic.Mid;
import com.testmap.logic.Pointf;
import com.testmap.view.ArcMenu;
import com.testmap.view.ArcMenu.OnMenuItemClickListener;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RoutePlanActivity extends Activity {
	
	private MapView mapview;
	private BaiduMap mymap;
	private PoiSearch mPoiSearch;
	private BDLocation mLocation=null;//�ҵ�λ��
	private UiSettings mUiSettings;
	
	private RelativeLayout relativeLayout;
	private Animation anim = null;
	//���ǲ˵��ؼ�
	private ArcMenu mArcmenu;
	//���ſؼ��Զ������
	private ImageButton zoomInBtn;
	private ImageButton zoomOutBtn;
	
	private String cityName;
	
	
	//��λ���
	private LocationClient mLocationClient;
	private MyLocationListener mLocationListener;
	private Context context;
	private Button btnback;
	//private Button btnpoi;
	
	//poi����
	private RelativeLayout relaSearch;
	private EditText editSearch;
	private ImageButton imgSearch;
	
	//����
	private RelativeLayout relaGuide;
	private EditText editStart;
	private EditText editEnd;
	private ImageButton imgGuide;
	
	private String routeLine = null;
	String []view = null;
	
	private int []pupple = {R.drawable.pupple1,R.drawable.pupple2,R.drawable.pupple3,
			R.drawable.pupple4,R.drawable.pupple5,R.drawable.pupple6,R.drawable.pupple7,
			R.drawable.pupple8,R.drawable.pupple9,R.drawable.pupple10,R.drawable.pupple11,
			R.drawable.pupple12,R.drawable.pupple13,R.drawable.pupple14,R.drawable.pupple15,
			R.drawable.pupple16,R.drawable.pupple17,R.drawable.pupple18,R.drawable.pupple19,
			R.drawable.pupple20,};
	
	//·�߹滮
	OverlayManager routeOverlay;
	RoutePlanSearch mSearch;
	RouteLine route;
	
	//���������
	//private RelativeLayout mapPop;
	private List<MapInfo>infos = new ArrayList<MapInfo>();
	//private BitmapDescriptor mMarker;
	//private int startnum;//��¼�滮���ǵڼ������
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(android.os.Message msg) {
			if(msg.what < 0xff) {
				int i = msg.what - 1;
				String []item1=view[i].split("%%");
				String []item2=view[i+1].split("%%");
				System.out.println(Double.valueOf(item1[1])+"     "+Double.valueOf(item1[2]));
				LatLng start = new LatLng(Double.parseDouble(item1[1]), Double.parseDouble(item1[2]));
				System.out.println(Double.parseDouble(item2[1])+"     "+Double.parseDouble(item2[2]));
				LatLng end = new LatLng(Double.parseDouble(item2[1]), Double.parseDouble(item2[2]));
				PlanNode stNode = PlanNode.withLocation(start);
				PlanNode enNode = PlanNode.withLocation(end);
//				PlanNode stNode = PlanNode.withCityNameAndPlaceName("��Ϫ", item1[0]);
//				PlanNode enNode = PlanNode.withCityNameAndPlaceName("��Ϫ", item2[0]);
				mSearch.walkingSearch((new WalkingRoutePlanOption()).from(stNode).to(enNode));
			} else if(msg.what == 0x100) {
				relativeLayout.startAnimation(anim);
				relativeLayout.setVisibility(View.GONE);
			}
			
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);		
		//��ʹ��SDK�����֮ǰ��ʼ��context��Ϣ������ApplicationContext  
        //ע��÷���Ҫ��setContentView����֮ǰʵ��  
        SDKInitializer.initialize(getApplicationContext()); 
		setContentView(R.layout.activity_main);
		
		anim = AnimationUtils.loadAnimation(this, R.anim.page_disappear);
		
		relativeLayout = (RelativeLayout) findViewById(R.id.pagelode);
		relativeLayout.setVisibility(View.VISIBLE);
		initView();
		initLocation();	
		initback();
		initPoi();
		initZoom();
		routeplan();
		
		//poi�������ְ�
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
		
		
		//�������ְ�
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
					//Ĭ�ϴ��ҵ�λ�ÿ�ʼ
					LatLng myplace = new LatLng(mLocation.getLatitude(),mLocation.getLongitude());
					stNode = PlanNode.withLocation(myplace);
				}else{
					stNode = PlanNode.withCityNameAndPlaceName(cityName, start);
				}				
				enNode = PlanNode.withCityNameAndPlaceName(cityName, end);
				mSearch.drivingSearch((new DrivingRoutePlanOption()).from(stNode).to(enNode));
			}
		});
		
		
		
		//���������¼�
		mymap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
			
			@Override
			public boolean onMarkerClick(Marker marker) {
				System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!");
				Bundle extraInfo = marker.getExtraInfo();
				MapInfo info = (MapInfo) extraInfo.getSerializable("info");
				if(info==null){
					return false;
				}
				LatLng latLng = marker.getPosition();
				InfoWindow infoWindow = new InfoWindow(getInfoWindow(marker,info),latLng,-77);
//				Point p = mymap.getProjection().toScreenLocation(latLng);
//				p.y -= 47;
//				LatLng ll = mymap.getProjection().fromScreenLocation(p);
				mymap.showInfoWindow(infoWindow);	
				System.out.println("55555555555555555555555555555555");
				return false;
			}
		});
		
		
		
		mymap.setOnMapClickListener(new OnMapClickListener() {
			//��ͼ����¼�
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
				//�����ͼʹ�ø�������ʧ
				mymap.hideInfoWindow();
				//����������ʧ
				relaSearch.setVisibility(View.GONE);
				relaGuide.setVisibility(View.GONE);
			}
		});
		
		mArcmenu = (ArcMenu) findViewById(R.id.id_menu);
		initEvent();
		
		routeLine = this.getIntent().getExtras().getString("viewline");
		//routeLine="���Է%%41.102544123783%%123.01374589879%%70.00Ԫ%%11~3�£�08:00~16:30  4~10�£�08:00~17:00%%http://hiphotos.baidu.com/lvpics/pic/item/eaf81a4c510fd9f998529af5202dd42a2934a4e1.jpg%%http://lvyou.baidu.com/yufoyuan&&���ɽ%%41.102815407836%%123.02088494734%%%%%%http://hiphotos.baidu.com/lvpics/pic/item/29381f30e924b899f1cd8fef6f061d950b7bf670.jpg%%http://lvyou.baidu.com/yufoshan&&";
		System.out.println(routeLine);
		if(routeLine != null){
			//��ȡinfos����
			getInfos();
			//routeplan();
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					for (int i = 0; i < view.length - 1; i++) {
						//startnum=i;
						Message message = new Message();
						message.what = i + 1;
						handler.sendMessage(message);
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					Message msg = new Message();
					msg.what = 0x100;
					handler.sendMessage(msg);
				}
			}).start();
			
			System.out.println("111111111111111111111111111111111111");
			addOverlays(infos);	
			MapStatusUpdate msu =MapStatusUpdateFactory.zoomTo(15.0f);
			mymap.setMapStatus(msu);
		}
		
	}
	
	protected View getInfoWindow(Marker marker,MapInfo info) {
		//����infowindow����
		System.out.println("44444444444444444444444444444");
		RelativeLayout infoView;
		LayoutInflater inflater = (LayoutInflater) RoutePlanActivity.this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		infoView = (RelativeLayout) inflater.inflate(R.layout.mappop, null);
		TextView tvName = (TextView) infoView.findViewById(R.id.name);
		tvName.setText(info.getName());
		ImageView img = (ImageView) infoView.findViewById(R.id.pic);
		ImageLoader.getInstance().displayImage(info.getUrl(), img);
		
		
		return infoView;		
	}

	private void getInfos() {
		view = routeLine.split("&&");
		for(int i=0;i<view.length;i++){
			String[]item = view[i].split("%%");
			MapInfo newinfo = new MapInfo(item[0], item[5], Double.valueOf(item[1]), Double.valueOf(item[2]));
			infos.add(newinfo);
		}
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"+infos.size());
		
	}

	/**
	 * ��Ӹ����﷽��
	 */
	private void addOverlays(List<MapInfo>infos){
		mymap.clear();
		LatLng latLng = null;
		BitmapDescriptor bitmarker = null;
		Marker marker;
		OverlayOptions options;
		for(MapInfo info: infos){
			int i=infos.indexOf(info);
			bitmarker = BitmapDescriptorFactory.fromResource(pupple[i]);
			latLng = new LatLng(info.getLatitude(), info.getLongitude());
			options = new MarkerOptions().position(latLng).icon(bitmarker).zIndex(99);
			marker = (Marker) mymap.addOverlay(options);
			Bundle arg0 = new Bundle();
			arg0.putSerializable("info", info);
			marker.setExtraInfo(arg0);
		}
		System.out.println("2222222222222222222222222222222");
		MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
		mymap.setMapStatus(msu);
		
	}
	
	private void routeplan() {
		// ·�߹滮����
		mSearch = RoutePlanSearch.newInstance();
		OnGetRoutePlanResultListener routeplanResultListener = new OnGetRoutePlanResultListener() {
			
			
			@Override
			public void onGetWalkingRouteResult(WalkingRouteResult result) {
				 if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			            Toast.makeText(RoutePlanActivity.this, "��Ǹ��δ�ҵ����", Toast.LENGTH_SHORT).show();
					 return;
			        }
			        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			            //���յ��;�����ַ����壬ͨ�����½ӿڻ�ȡ�����ѯ��Ϣ
			            //result.getSuggestAddrInfo()
			            return;
			        }
			        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			            route = result.getRouteLines().get(0);
			            MyWalkingRouteOverlay overlay = new MyWalkingRouteOverlay(mymap);
			           // mymap.setOnMarkerClickListener(overlay);
			            routeOverlay = overlay;
			            overlay.setData(result.getRouteLines().get(0));
			            overlay.addToMap();
			            overlay.zoomToSpan();
			        }
				
			}
			
			@Override
			public void onGetTransitRouteResult(TransitRouteResult result) {
				if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
		            //Toast.makeText(RoutePlanActivity.this, "��Ǹ��δ�ҵ����", Toast.LENGTH_SHORT).show();
					return;
		        }
		        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
		            //���յ��;�����ַ����壬ͨ�����½ӿڻ�ȡ�����ѯ��Ϣ
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
		            Toast.makeText(RoutePlanActivity.this, "��Ǹ��δ�ҵ����", Toast.LENGTH_SHORT).show();
					return;
		        }
		        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
		            //���յ��;�����ַ����壬ͨ�����½ӿڻ�ȡ�����ѯ��Ϣ
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

	//�Զ������ſؼ�
	private void initZoom() {
		mapview.showZoomControls(false);//�������ſؼ�
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
					Toast.makeText(RoutePlanActivity.this, "�Ѿ��������", Toast.LENGTH_SHORT).show();
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
					Toast.makeText(RoutePlanActivity.this, "�Ѿ�������С��", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
	}

	private void initEvent() {
//		mapview.setOnTouchListener(new OnTouchListener() {
//			
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				int touchEvent = event.getAction();  
//		        switch (touchEvent) {
//		        case MotionEvent.ACTION_DOWN:
//		        case MotionEvent.ACTION_UP:
//		        case MotionEvent.ACTION_MOVE:
//		         if(mArcmenu.isOpen()==true){
//		        	 mArcmenu.toggleMenu(300);
//		         }break;
//		        default:  
//		            break;  
//		        }
//				return false;
//			}
//		});
		
		//���ǲ˵���������¼�
		mArcmenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public void onClick(View view, int pos) {
				switch (pos) {
				//���ϵ��µ���������Ϊ1
				case 1:
					Toast.makeText(RoutePlanActivity.this, "�������������Ƶ�", Toast.LENGTH_SHORT).show();
					PoiNearbySearchOption nearBySearchOption = new PoiNearbySearchOption();
					nearBySearchOption.location(new LatLng(mLocation.getLatitude(), mLocation.getLongitude()));
					nearBySearchOption.keyword("�Ƶ�");
					nearBySearchOption.radius(10000);
					nearBySearchOption.pageCapacity(10);//�������Ķ���ĸ���
					mPoiSearch.searchNearby(nearBySearchOption);
					break;
				case 2:
					Toast.makeText(RoutePlanActivity.this, "��������������������", Toast.LENGTH_SHORT).show();
					PoiNearbySearchOption nearBySearchOption2 = new PoiNearbySearchOption();
					nearBySearchOption2.location(new LatLng(mLocation.getLatitude(), mLocation.getLongitude()));
					nearBySearchOption2.keyword("����");
					nearBySearchOption2.radius(10000);
					nearBySearchOption2.pageCapacity(10);//�������Ķ���ĸ���
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

	private void initPoi() {
		//ͨ��POI���ܱ߼���
		//btnpoi = (Button) findViewById(R.id.btnpoi);
		mPoiSearch = PoiSearch.newInstance();
		OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener() {
			
			@Override
			public void onGetPoiResult(PoiResult result) {
				if (result == null
						|| result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
					Toast.makeText(RoutePlanActivity.this, "δ�ҵ����", Toast.LENGTH_LONG)
					.show();
					return;
				}
				if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {

					// ������ؼ����ڱ���û���ҵ����������������ҵ�ʱ�����ذ����ùؼ�����Ϣ�ĳ����б�
					String strInfo = "��";
					for (CityInfo cityInfo : result.getSuggestCityList()) {
						strInfo += cityInfo.city;
						strInfo += ",";
					}
					strInfo += "�ҵ����";
					Toast.makeText(RoutePlanActivity.this, strInfo, Toast.LENGTH_LONG)
							.show();
				}
				mymap.clear();
				//��Ӷ���Ч�����ƶ���ĳһ������poi
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
					Toast.makeText(RoutePlanActivity.this, "��Ǹ��δ�ҵ����", Toast.LENGTH_SHORT)
							.show();
				} 
				else {
					Toast.makeText(RoutePlanActivity.this, result.getName() + ": " + result.getAddress(), Toast.LENGTH_LONG)
					.show();
					//���ﻹ��Ҫ��Ӹ��๦��
				}
			}
		};
		mPoiSearch.setOnGetPoiSearchResultListener(poiListener);
//		btnpoi.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				//�ܱ߼���
//				//mymap.clear();
//				PoiNearbySearchOption nearBySearchOption = new PoiNearbySearchOption();
//				nearBySearchOption.location(new LatLng(mLocation.getLatitude(), mLocation.getLongitude()));
//				nearBySearchOption.keyword("�Ƶ�");
//				nearBySearchOption.radius(10000);
//				nearBySearchOption.pageCapacity(10);//�������Ķ���ĸ���
//				mPoiSearch.searchNearby(nearBySearchOption);
				
				//���о������
//				PoiCitySearchOption citySearchOption = new PoiCitySearchOption();
//				citySearchOption.city("�Ͼ�").keyword("��ɽ��").pageCapacity(5);
//				mPoiSearch.searchInCity(citySearchOption);
//			}
//		});
	}	
	
	
	private class MyPoiOverlay extends PoiOverlay {

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
		// �����ť�ص��ҵ�λ��
		btnback = (Button) findViewById(R.id.btnback);
		btnback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				LatLng latlng = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
				MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latlng);
				mymap.animateMapStatus(msu);
				Toast.makeText(context,"��ǰλ�ã�"+ mLocation.getAddrStr(), Toast.LENGTH_SHORT).show();
				
				
			}
		});
	}

	private void initLocation() {
		// ��ʼ����λ�ķ���
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
	 * �Ƿ�����ƽ������
	 * 
	 * @param v
	 */
	public void setScrollEnable(View v) {
		mUiSettings.setScrollGesturesEnabled(((CheckBox) v).isChecked());
	}

	/**
	 * �Ƿ�������ת����
	 * 
	 * @param v
	 */
	public void setRotateEnable(View v) {
		mUiSettings.setRotateGesturesEnabled(((CheckBox) v).isChecked());
	}

	/**
	 * �Ƿ����ø�������
	 * 
	 * @param v
	 */
	public void setOverlookEnable(View v) {
		mUiSettings.setOverlookingGesturesEnabled(((CheckBox) v).isChecked());
	}

	/**
	 * �Ƿ�����ָ����ͼ��
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
		
		//������λ
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
		
		//�رն�λ
		mymap.setMyLocationEnabled(false);
		mLocationClient.stop();
	}
	
	private class MyLocationListener implements BDLocationListener
	{
		
		@Override
		public void onReceiveLocation(BDLocation location) {
			// ��λ�ɹ���ص�����
			MyLocationData data = new MyLocationData.Builder()//
									.accuracy(location.getRadius())//
									.latitude(location.getLatitude())//
									.longitude(location.getLongitude()).build();
			
			mymap.setMyLocationData(data);
			
			//ÿ�ζ�λ�ɹ�����¾�γ�����ݲ�������mLocation��
			mLocation = location;
			cityName = location.getCity();
			System.out.println("%%%%%%%%%%%%%%%%%%%%%"+cityName);
		}
		
	} 
	
	
	
	
	/**
	 * ��д���й滮������
	 *
	 */
	private class MyWalkingRouteOverlay extends WalkingRouteOverlay{

		public MyWalkingRouteOverlay(BaiduMap arg0) {
			super(arg0);
		}
		
		//��д�������ͼƬ������Ϊ��
		@Override
		public BitmapDescriptor getStartMarker() {
//			if(startnum<routeLine.split("&&").length-1){
//				return BitmapDescriptorFactory.fromResource(pupple[startnum]);
//			}else{
//				return null;
//			}
			return BitmapDescriptorFactory.fromResource(R.drawable.blank);
		}
		
		//��д���յ�����ͼƬ������Ϊ��
		@Override
		public BitmapDescriptor getTerminalMarker() {
//			if(startnum==routeLine.split("&&").length-2){
//				return BitmapDescriptorFactory.fromResource(pupple[startnum+1]);
//			}else{
//				return null;
//			}
			return BitmapDescriptorFactory.fromResource(R.drawable.blank);
		}
	}
	
}
