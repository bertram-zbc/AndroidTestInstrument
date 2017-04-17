package com.test.baidumap;

import android.app.ActivityGroup;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;
import android.widget.TextView;

public class TabActivity extends ActivityGroup {

	private TabHost mTabHost;
	
	private ImageButton img0;
	private ImageButton img1;
	private ImageButton img2;
	
	private TextView text0;
	private TextView text1;
	private TextView text2;
	
	private View tab0;
	private View tab1;
	private View tab2;
//	private View currentView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabhost);
		initTabs();
	}

	private void initTabs() {
//		 mTabHost = (TabHost) findViewById(R.id.tabhost);
//		    mTabHost.setup(this.getLocalActivityManager());
//		    // 添加日志列表的tab,注意下面的setContent中的代码.是这个需求实现的关键
//		    mTabHost.addTab(mTabHost.newTabSpec("tab_homepage")
//		      .setIndicator("首页",getResources().getDrawable(R.drawable.homepage_1))
//		      .setContent(new Intent(this, HomepageActivity.class)));
//		 
//		    // 添加应用设置的tab,注意下面的setContent中的代码.是这个需求实现的关键
//		    mTabHost.addTab(mTabHost.newTabSpec("tab_map")
//		      .setIndicator("地图",getResources().getDrawable(R.drawable.map_1))
//		      .setContent(new Intent(this, MapActivity.class)));
//		    
//		    mTabHost.addTab(mTabHost.newTabSpec("tab_trip")
//				      .setIndicator("行程",getResources().getDrawable(R.drawable.trip_1))
//				      .setContent(new Intent(this, TripActivity.class)));
//		    
		    tab0 = (View)LayoutInflater.from(this).inflate(R.layout.tab, null);
		    text0 = (TextView) tab0.findViewById(R.id.tab_tv);
		    text0.setText("首页");
		    text0.setTextColor(Color.parseColor("#2fa5c2"));
		    img0 = (ImageButton) tab0.findViewById(R.id.tab_img);
		    img0.setImageResource(R.drawable.homepage_1);
		    
		    tab1 = (View)LayoutInflater.from(this).inflate(R.layout.tab, null);
		    text1 = (TextView) tab1.findViewById(R.id.tab_tv);
		    text1.setText("热门推荐");
		    text1.setTextColor(Color.parseColor("#686868"));
		    img1 = (ImageButton) tab1.findViewById(R.id.tab_img);
		    img1.setImageResource(R.drawable.map);
		    
		    tab2 = (View)LayoutInflater.from(this).inflate(R.layout.tab, null);
		    text2 = (TextView) tab2.findViewById(R.id.tab_tv);
		    text2.setText("我");
		    text2.setTextColor(Color.parseColor("#686868"));
		    img2 = (ImageButton) tab2.findViewById(R.id.tab_img);
		    img2.setImageResource(R.drawable.my);
		    
			mTabHost = (TabHost) findViewById(R.id.tabhost);
		    mTabHost.setup(this.getLocalActivityManager());
 		    
		    mTabHost.addTab(mTabHost.newTabSpec("tab_homepage")
		    		.setIndicator(tab0)
		    		.setContent(new Intent(this, HomepageActivity.class)));
		    mTabHost.addTab(mTabHost.newTabSpec("tab_map")
		    		.setIndicator(tab1)
		    		.setContent(new Intent(this, RecommandActivity.class)));
		    mTabHost.addTab(mTabHost.newTabSpec("tab_my")
		    		.setIndicator(tab2)
		    		.setContent(new Intent(this, MyActivity.class)));
		    
		    mTabHost.setOnTabChangedListener(new OnTabChangeListener() {
				
				@Override
				public void onTabChanged(String tabId) {
					if("tab_homepage".equals(tabId)){
						reset();
						text0.setTextColor(Color.parseColor("#2fa5c2"));
						img0.setImageResource(R.drawable.homepage_1);
//						currentView = tab0;
					}
					else if("tab_map".equals(tabId) /* && currentView.equals(tab0) */ ){
						reset();
						text1.setTextColor(Color.parseColor("#2fa5c2"));
						img1.setImageResource(R.drawable.map_1);
						
						
//						mTabHost.getCurrentView().setAnimation(inFromRightAnimation());
//						currentView = tab1;
					}
//					else if("tab_map".equals(tabId) /* && currentView.equals(tab2) */){
//						reset();
//						System.out.println("22222222222222222222222222222222222222222222");
//						text1.setTextColor(Color.parseColor("#2fa5c2"));
//						img1.setImageResource(R.drawable.map_1);
//						mTabHost.getCurrentView().setAnimation(outToRightAnimation());
//						currentView = tab1;
//					}
					else if("tab_my".equals(tabId)){
						reset();
			    		text2.setTextColor(Color.parseColor("#2fa5c2"));
			    		img2.setImageResource(R.drawable.my_1);
//			    		currentView = tab2;
					}
					
				}
			});
		    
		    mTabHost.setCurrentTab(0);
//		    currentView = tab0;
		     
	}

//	protected Animation inFromRightAnimation() {
//		 Animation inFromRight = new TranslateAnimation(
//		           Animation.RELATIVE_TO_PARENT, +1.0f,
//		           Animation.RELATIVE_TO_PARENT, 0.0f,
//		           Animation.RELATIVE_TO_PARENT, 0.0f,
//		           Animation.RELATIVE_TO_PARENT, 0.0f);
//		   inFromRight.setDuration(240);
//		   inFromRight.setInterpolator(new AccelerateInterpolator());
//		   return inFromRight;
//	}
//
//	protected Animation outToRightAnimation() {
//		Animation outtoLeft = new TranslateAnimation(
//		           Animation.RELATIVE_TO_PARENT, -1.0f,
//		           Animation.RELATIVE_TO_PARENT, 0.0f,
//		           Animation.RELATIVE_TO_PARENT, 0.0f,
//		           Animation.RELATIVE_TO_PARENT, 0.0f);
//		   outtoLeft.setDuration(240);
//		   outtoLeft.setInterpolator(new AccelerateInterpolator());
//		   return outtoLeft;
//	}

	protected void reset() {
		
		img0.setImageResource(R.drawable.homepage);
		img1.setImageResource(R.drawable.map);
		img2.setImageResource(R.drawable.my);
		text0.setTextColor(Color.parseColor("#686868"));
		text1.setTextColor(Color.parseColor("#686868"));
		text2.setTextColor(Color.parseColor("#686868"));
		
	}

}
