package com.test.baidumap;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.testmap.logic.Trip;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MyTripActivity extends Activity implements OnItemClickListener {
	
	private ListView listview;
	private SharedPreferences sharedPref;
	private SharedPreferences username;
	private int tripNum=0;
	private ArrayList<Trip> trips = new ArrayList<Trip>();
	private int []pic = {R.drawable.pic_back1,R.drawable.pic_back2,R.drawable.pic_back3,R.drawable.pic_back4
			,R.drawable.pic_back5,R.drawable.pic_back6};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mytrip);
		
		username = this.getSharedPreferences("Pref01", MODE_PRIVATE);
		String s = new String();
		s = username.getString("username", "share");
		
		sharedPref = this.getSharedPreferences(s+"tripNum", MODE_PRIVATE);
		tripNum = sharedPref.getInt("tripNum", 0);
		System.out.println("MyTrip+tripNum="+tripNum);
		
		for(int i=tripNum-1;i>=0;i--){
			sharedPref = this.getSharedPreferences(s+String.valueOf(i), MODE_PRIVATE);
			System.out.println(s+String.valueOf(i));
			Trip temp = new Trip();
			temp.setCity(sharedPref.getString("city", "未知城市"));
			System.out.println(sharedPref.getString("city", "未知城市"));
			temp.setTime(sharedPref.getString("time", ""));
			temp.setRoute(sharedPref.getString("route", ""));
			temp.setSharedId(s+String.valueOf(i));
			trips.add(temp);
		}

		listview = (ListView) findViewById(R.id.listview);
		listview.setAdapter(new MyListAdapter());
		listview.setOnItemClickListener(this);
		
	}
	
	class MyListAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return tripNum;
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
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null){				
				LayoutInflater inflater = (LayoutInflater) MyTripActivity.this
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.item_trip_city, null);
			}
			
			SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy.MM.dd");
			Date nowDate = new Date(System.currentTimeMillis());
			Date toDate = null;
			try {
				toDate = dataFormat.parse(trips.get(position).getTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int datenum = (int) ((toDate.getTime() - nowDate.getTime())/(86400000));
			if(datenum<0){
				datenum=0;
			}
			
			TextView texttime = (TextView) convertView.findViewById(R.id.texttime);
			TextView textday = (TextView) convertView.findViewById(R.id.textday);
			TextView textcity = (TextView) convertView.findViewById(R.id.textcity);
			ImageView back = (ImageView) convertView.findViewById(R.id.imgback);
			texttime.setText(trips.get(position).getTime());
			textday.setText("距离出发还有"+datenum+"天");
			textcity.setText(trips.get(position).getCity());
			int i = position%6;
			back.setImageResource(pic[i]);
			return convertView;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
		Intent intent  = new Intent(MyTripActivity.this,MyTripDetailActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("sharedid", trips.get(position).getSharedId());
		intent.putExtras(bundle);
		startActivity(intent);
		
	}
	
}
