package com.test.baidumap;

import java.util.ArrayList;

import com.test.baidumap.R;
import com.testmap.logic.TripDetailSpot;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MyTripDetailActivity extends Activity {
	
	private ListView listview;
	
	private SharedPreferences sharedPref;
	private String viewLine;
	private String city;
	private int days=0;
	private MyAdapter adapter = new MyAdapter();
	private int itemNum=0;//记录listview项的数目
	
	private TextView tv_mark_for_view;//为景点评分的按钮
	private TextView tv_back;
	
	private ArrayList<TripDetailSpot>spots = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mytripdetail);
		
		tv_mark_for_view = (TextView) findViewById(R.id.id_tv_markforviews);
		tv_back = (TextView) findViewById(R.id.tvback);
		
		
		String s = this.getIntent().getExtras().getString("sharedid");
		System.out.println(s);
		sharedPref = this.getSharedPreferences(s, MODE_PRIVATE);	
		viewLine = sharedPref.getString("route", "");
		city = sharedPref.getString("city", "");
		
		System.out.println(viewLine);
		
		String []views = viewLine.split("&&");
		
		TripDetailSpot hotel = new TripDetailSpot();
		hotel.setName("酒店");
		hotel.setDay("1");
		hotel.setIsFirstOfDay(1);
		hotel.setIsHotel(1);
		spots.add(hotel);
		
		for(int i=0;i<views.length;i++){
			TripDetailSpot temp = new TripDetailSpot();
			temp.setName(views[i].split("%%")[0]);
			temp.setDay(views[i].split("%%")[7]);
			temp.setIsHotel(0);
			if( i>0 && (!views[i].split("%%")[7].equals(views[i-1].split("%%")[7])) ){
				TripDetailSpot newhotel = new TripDetailSpot();
				newhotel.setName("酒店");
				newhotel.setDay(views[i].split("%%")[7]);
				newhotel.setIsFirstOfDay(1);
				newhotel.setIsHotel(1);
				spots.add(newhotel);
			}else{
				temp.setIsFirstOfDay(0);
			}
			spots.add(temp);
		}
		spots.add(hotel);
		
		System.out.println(spots.size());
		
		days = Integer.parseInt(views[views.length-1].split("%%")[7]);		
		itemNum = days+1+views.length;

		listview = (ListView) findViewById(R.id.listview);
		listview.setAdapter(adapter);
		
		
		tv_mark_for_view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MyTripDetailActivity.this, SpotListHistoryActivity.class);
				intent.putExtra("viewLine", viewLine);
				intent.putExtra("city", city);
				MyTripDetailActivity.this.startActivity(intent);
			}
		});
		
		tv_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
			
	}
	
	
	public class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return itemNum;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if(position<itemNum-1){
				System.out.println("position="+position);
				LayoutInflater inflater = (LayoutInflater) MyTripDetailActivity.this
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.item_mytripdetail, null);
				
				TextView tvday = (TextView) convertView.findViewById(R.id.textday);
				ImageView imgday = (ImageView) convertView.findViewById(R.id.imgday);
				ImageView imgspot = (ImageView) convertView.findViewById(R.id.imgspot);
				ImageView imghotel = (ImageView) convertView.findViewById(R.id.imghotel);
				TextView tvname = (TextView) convertView.findViewById(R.id.textname);
				TextView tvadd = (TextView) convertView.findViewById(R.id.textadd);
				RelativeLayout rela = (RelativeLayout) convertView.findViewById(R.id.rela_trip);
				
				final TripDetailSpot current = spots.get(position);
				if(current.getIsHotel()==1){
					tvday.setText("Day "+current.getDay());
					imgday.setVisibility(View.VISIBLE);
					imghotel.setVisibility(View.VISIBLE);
					imgspot.setVisibility(View.INVISIBLE);
					tvname.setText("酒店");
					tvadd.setText("查找"+spots.get(position+1).getName()+"附近的酒店");
					tvadd.setTextColor(Color.parseColor("#2fa5c2"));
					tvadd.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent intent = new Intent(MyTripDetailActivity.this,MyTripHotelActivity.class);
							Bundle bundle = new Bundle();
							bundle.putString("city", city);
							bundle.putString("place", spots.get(position+1).getName());
							intent.putExtras(bundle);
							startActivity(intent);
						}
					});
					
					rela.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// 酒店项的点击事件
							Toast.makeText(MyTripDetailActivity.this, "to do hotel", 1010).show();
						}
					});
					
				}else{
					tvday.setVisibility(View.INVISIBLE);
					imgday.setVisibility(View.INVISIBLE);
					imghotel.setVisibility(View.INVISIBLE);
					imgspot.setVisibility(View.VISIBLE);
					tvname.setText(current.getName());
					
					if(spots.get(position+1).getIsHotel()==1){
						tvadd.setVisibility(View.VISIBLE);
						tvadd.setText("查找"+current.getName()+"附近的酒店");
						tvadd.setTextColor(Color.parseColor("#2fa5c2"));
						tvadd.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								Intent intent = new Intent(MyTripDetailActivity.this,MyTripHotelActivity.class);
								Bundle bundle = new Bundle();
								bundle.putString("city", city);
								bundle.putString("place", current.getName());
								intent.putExtras(bundle);
								startActivity(intent);
							}
						});
						
					}else{
						tvadd.setVisibility(View.VISIBLE);
						tvadd.setText("从"+current.getName()+"到"+spots.get(position+1).getName());
						tvadd.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								Toast.makeText(getApplicationContext(), "text", 100).show();
							}
						});
					}
					
					rela.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Toast.makeText(MyTripDetailActivity.this, "to do spot", 1010).show();
						}
					});
					
				}
				
				
			}else{
				LayoutInflater inflater = (LayoutInflater) MyTripDetailActivity.this
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.item_mytripdetail, null);
				
				TextView tvday = (TextView) convertView.findViewById(R.id.textday);
				ImageView imgday = (ImageView) convertView.findViewById(R.id.imgday);
				ImageView imgspot = (ImageView) convertView.findViewById(R.id.imgspot);
				ImageView imghotel = (ImageView) convertView.findViewById(R.id.imghotel);
				TextView tvname = (TextView) convertView.findViewById(R.id.textname);
				TextView tvadd = (TextView) convertView.findViewById(R.id.textadd);
				RelativeLayout rela = (RelativeLayout) convertView.findViewById(R.id.rela_trip);
				tvday.setText(" End  ");
				tvadd.setText("行程结束");
				
			}
			
			
			return convertView;
		}
		
	}
}
