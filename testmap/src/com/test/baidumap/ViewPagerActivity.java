package com.test.baidumap;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.test.fragment.*;
import com.test.fragment.Fragment1.OnCallBackListener;
import com.testmap.logic.ClientThread;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.animation.AccelerateInterpolator;

public class ViewPagerActivity extends FragmentActivity implements OnCallBackListener{
	
	SharedPreferences usermessage;
	SharedPreferences username;
	Editor editor;
	
	private List<Fragment>fragList;
	private ViewPager viewPager;
	private String[] string = new String[9];
	String []send = {"0", "10", "0", "0", "0", "0", "0", "0"};//性别&&年龄&&收入&&职业&&家乡&&景点类型&&爱好
	MyFragmentPagerAdapter fragPageAdapter;
	
	private String message;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewpager);
		
		viewPager = (ViewPager)findViewById(R.id.viewpager);
		viewPager.setOffscreenPageLimit(1);
		try {
            	Field field = ViewPager.class.getDeclaredField("mScroller");
            	field.setAccessible(true);
            	FixedSpeedScroller scroller = new FixedSpeedScroller(viewPager.getContext(),
            									new AccelerateInterpolator());
            	field.set(viewPager, scroller);
            	scroller.setmDuration(800);
        	} catch (Exception e) {
        		System.out.println("sth wrong");
        	}
		
		
		fragList = new ArrayList<Fragment>();
		fragList.add(new Fragment1());
		fragList.add(new Fragment2());
		fragList.add(new Fragment3());
		fragList.add(new Fragment4());
		fragList.add(new Fragment5());
		fragList.add(new Fragment6());
		fragList.add(new Fragment7());
		fragList.add(new Fragment8());
		fragList.add(new Fragment9());
		
		fragPageAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragList);
		viewPager.setAdapter(fragPageAdapter);
		
	}

	@Override
	public void onCallBack(String str) {
		System.out.println("****************************************");
		System.out.println(str);
		String []temp = str.split("&");
		System.out.println(temp[0]+"  "+temp[1]+" "+temp[2]);
		int tempPage = Integer.parseInt(temp[0]);
		System.out.println(tempPage);
		string[tempPage]=temp[1];
		if(tempPage>=1&&tempPage<=7){
			System.out.println("++++++++++++"+tempPage+"+"+temp[2]);
			send[tempPage-1]=temp[2];
		}
		if(tempPage<8){
			viewPager.setCurrentItem(++tempPage);
		}else{
			message = "registInfo&&";
			for(int i=0;i<7;i++){//性别&&年龄&&收入&&职业&&家乡&&景点类型&&爱好
				message += send[i] + "&&";
				System.out.println("*********************"+send[i]);
			}
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					new ClientThread().println(message);
					try {
						String recvString = new ClientThread().readLine();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).start();
			username = this.getSharedPreferences("Pref01", MODE_PRIVATE);
			String s = new String();
			s = username.getString("username", "share");
			
			usermessage = this.getSharedPreferences(s, MODE_PRIVATE);
			editor = usermessage.edit();
			editor.putInt("isMessage", 1);
			editor.putString("性别", string[1]);
			editor.putString("年龄", string[2]);
			editor.putString("收入", string[3]);
			editor.putString("职业", string[4]);
			editor.putString("家乡", string[5]);
			editor.putString("景点类型", string[6]);
			editor.putString("爱好", string[7]);
			editor.commit();
			Intent intent = new Intent(ViewPagerActivity.this,ConditionActivity.class);
			startActivity(intent);
			finish();
		}
		
	}

}
