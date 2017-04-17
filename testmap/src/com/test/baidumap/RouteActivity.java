package com.test.baidumap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.test.fragment.DatePickerFragment;
import com.test.fragment.Fragment1.OnCallBackListener;




import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class RouteActivity extends Activity implements OnItemClickListener,OnCallBackListener {

	public int tripNum = 0;
	public static RouteActivity instance = null;
	private ListView listview;
	private mSimpleAdapter adapter;
	private List<Map<String,Object>> dataList;
	private String viewLine = null;//获取到的路线
	String route = "";//记录重置路线
	private String [] view = null;
	
	//控件
	private TextView tvRoute;
	private TextView tvBack;
	private ImageButton imgMap;
	private TextView tvEdit;
	private ImageButton imgBegin;
		
	private SharedPreferences trip;
	private SharedPreferences username;
	private Editor editor;
	private String city;
	
	public static void finishLast() {
		if(instance != null) {
			instance.finish();
			instance = null;
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.routelistview);
		finishLast();
		instance=this;
		viewLine = this.getIntent().getExtras().getString("viewLine");
		
		city = this.getIntent().getStringExtra("city");
		if(city == null) {
			city = "南京";
		}
		
		System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAA"+city);
		System.out.println(viewLine);
		view = viewLine.split("&&");
		
		tvRoute = (TextView) findViewById(R.id.tv_route);
		tvBack = (TextView) findViewById(R.id.tvback);
		tvEdit = (TextView) findViewById(R.id.tvedit);
		imgMap = (ImageButton) findViewById(R.id.img_map);
		listview = (ListView) findViewById(R.id.listView);
		dataList = new ArrayList<Map<String,Object>>();
		adapter = new mSimpleAdapter(this, getData(), R.layout.item2, //
				new String[]{"Number","Title","OpenTime","Price","Telephone","Guideway"}, 
				new int[]{R.id.text1,R.id.title,R.id.opentime,R.id.price,R.id.telephone,R.id.guideway});
		
		listview.setOnItemClickListener(this);
		listview.setAdapter(adapter);
		
		for(int i=0;i<view.length;i++){
			route = route + view[i].split("%%")[0];
			if(i != view.length-1){
				route = route + "->";
			}
		}
		tvRoute.setText(route);
		
		tvBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(RouteActivity.this, ConditionActivity.class);
				setResult(2, intent);
				finish();				
			}
		});
		
		tvEdit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(RouteActivity.this, SpotListActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("viewLine", viewLine);
				intent.putExtras(bundle);
				intent.putExtra("city", city);
				startActivity(intent);			
			}
		});
		
		imgMap.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent();
				intent.setClass(RouteActivity.this, RoutePlanActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("viewline", viewLine);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		
		imgBegin = (ImageButton) findViewById(R.id.beginbutton);
		imgBegin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(RouteActivity.this, "选择出发日期", Toast.LENGTH_SHORT).show();
				showDatePickerDialog();
			}
		});
		
	}
	
	public void showDatePickerDialog(){  
	    DatePickerFragment datePicker = new DatePickerFragment();  
	    datePicker.show(getFragmentManager(), "datePicker"); 
	} 
	
	
	private List<Map<String,Object>> getData(){
		for(int i=0;i<view.length;i++){
			Map<String,Object>map = new HashMap<String, Object>();
			if(i==0){
				map.put("Number", "景点一");
			}
			else if(i==1){
				map.put("Number", "景点二");
			}
			else if(i==2){
				map.put("Number", "景点三");
			}
			else if(i==3){
				map.put("Number", "景点四");
			}
			else if(i==4){
				map.put("Number", "景点五");
			}
			else if(i==5){
				map.put("Number", "景点六");
			}
			else if(i==6){
				map.put("Number", "景点七");
			}
			else if(i==7){
				map.put("Number", "景点八");
			}
			else if(i==8){
				map.put("Number", "景点九");
			}
			else if(i==9){
				map.put("Number", "景点十");
			}
			else if(i==10){
				map.put("Number", "景点十一");
			}
			else if(i==11){
				map.put("Number", "景点十二");
			}
			String []spots = view[i].split("%%");
			map.put("Title", spots[0]);
			if(spots[4].equals("")){
				map.put("OpenTime", "开放时间：全天");
			}else{
				map.put("OpenTime", "开放时间："+spots[4]);
			}
			if(spots[3].equals("")){
				map.put("Price", "门票价格：免费");
			}else{
				map.put("Price", "门票价格："+spots[3]);
			}
			
			map.put("Telephone", "联系电话：见详细信息");
			map.put("vPinYin", spots[6].replace("http://lvyou.baidu.com/", ""));
			if(i < view.length - 1){
				map.put("Guideway", "从"+view[i].split("%%")[0]+"前往"+view[i+1].split("%%")[0]+" >");
			}else{
				map.put("Guideway", "");
			}
			dataList.add(map);
		}		
		return dataList;
	}
	
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// 获取点击ListView item中的内容信息
		//String text = listview.getItemAtPosition(position) + "";
		// 弹出Toast信息显示点击位置和内容
//		Toast.makeText(RouteActivity.this,
//				"position=" + position , Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(RouteActivity.this,SpotActivity.class);
		String vPinYin = (String) dataList.get(position).get("vPinYin");
		intent.putExtra(SpotActivity.VIEWPINYIN, vPinYin);
		startActivity(intent);
	}
	
	public class mSimpleAdapter extends SimpleAdapter{

		public mSimpleAdapter(Context context,
				List<? extends Map<String, ?>> data, int resource,
				String[] from, int[] to) {
			super(context, data, resource, from, to);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = super.getView(position, convertView, parent);
			TextView tvGuide = (TextView) convertView.findViewById(R.id.guideway);
			if(position==view.length-1){
				tvGuide.setVisibility(View.VISIBLE);
			}
			tvGuide.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Toast.makeText(RouteActivity.this, "guide", 100).show();
					
				}
			});
			
			return convertView;
		}	
	}

	@Override
	public void onCallBack(String str) {
		// TODO Auto-generated method stub
		System.out.println(str);
		//点击日期确定按钮，文件储存相关信息并跳转到新的页面
		
		username = this.getSharedPreferences("Pref01", MODE_PRIVATE);
		String s = new String();
		s = username.getString("username", "share");
		
		trip = this.getSharedPreferences(s+"tripNum", MODE_PRIVATE);
		tripNum = trip.getInt("tripNum", 0);
		
		
		trip = this.getSharedPreferences(s+String.valueOf(tripNum), MODE_PRIVATE);
		editor = trip.edit();
		editor.putString("city", city);
		editor.putString("time", str);
		editor.putString("route", viewLine);
		editor.commit();
		editor.clear();
		
		tripNum++;
		System.out.println("tripNum="+tripNum);

		trip = this.getSharedPreferences(s+"tripNum", MODE_PRIVATE);
		editor = trip.edit();
		editor.putInt("tripNum", tripNum);
		editor.commit();
		System.out.println(s+"tripNum");
		Intent intent = new Intent(RouteActivity.this,MyTripActivity.class);
		startActivity(intent);
		finish();
		ConditionActivity.instance.finish();
	}

}
