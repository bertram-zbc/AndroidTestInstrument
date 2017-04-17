package com.example.imagepager;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.imageloader.WeixinActivity;
import com.test.baidumap.R;
import com.test.baidumap.ZoneActivity;

/**
 * 图片查看器
 */
public class ImagePagerActivity extends FragmentActivity {
	private static final String STATE_POSITION = "STATE_POSITION";
	public static final String EXTRA_IMAGE_INDEX = "image_index"; 
	public static final String EXTRA_IMAGE_URLS = "image_urls";
	public final static int ZONEACTIVIY = 0x1;
	public final static int WEIXINACTIVITY = 0x2;
	public final static String WHATACTIVITY = "whatactivity";

	private HackyViewPager mPager;
	private int pagerPosition;
	private TextView indicator;
	private ImageButton select;
	private ArrayList<String> urls;
	private boolean isSelected;
	int activity;

	@Override 
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.imagepager_detail_pager);
		
		activity = ImagePagerActivity.this.getIntent().getIntExtra(WHATACTIVITY, 0);

		pagerPosition = getIntent().getIntExtra(EXTRA_IMAGE_INDEX, 0);
		urls = getIntent().getStringArrayListExtra(EXTRA_IMAGE_URLS);

		mPager = (HackyViewPager) findViewById(R.id.pager);
		ImagePagerAdapter mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), urls);
		mPager.setAdapter(mAdapter);
		indicator = (TextView) findViewById(R.id.indicator);
		
		select = (ImageButton) findViewById(R.id.id_select);
		
		if(urls == null || urls.size() < 1) {
			ImagePagerActivity.this.finish();
			return;
		}
		CharSequence text = getString(R.string.viewpager_indicator, 1, mPager.getAdapter().getCount());
		indicator.setText(text);
		// 更新下标
		mPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageSelected(int arg0) {
				CharSequence text = getString(R.string.viewpager_indicator, arg0 + 1, mPager.getAdapter().getCount());
				indicator.setText(text);
				pagerPosition = arg0;
				if(activity == WEIXINACTIVITY) {
					if(WeixinActivity.isInside(urls.get(pagerPosition))) {
						isSelected = true;
						select.setImageResource(R.drawable.image_chosen);
					} else {
						isSelected = false;
						select.setImageResource(R.drawable.image_to_choose);
					}
				} else if(activity == ZONEACTIVIY) {
					if(ZoneActivity.isInside(urls.get(pagerPosition))) {
						isSelected = true;
						select.setImageResource(R.drawable.image_chosen);
					} else {
						isSelected = false;
						select.setImageResource(R.drawable.image_to_choose);
					}
				}
			}

		});
		if (savedInstanceState != null) {
			pagerPosition = savedInstanceState.getInt(STATE_POSITION);
		}
		
		if(activity == WEIXINACTIVITY) {
			if(WeixinActivity.isInside(urls.get(pagerPosition))) {
				isSelected = true;
				select.setImageResource(R.drawable.image_chosen);
			} else {
				isSelected = false;
				select.setImageResource(R.drawable.image_to_choose);
			}
		} else if(activity == ZONEACTIVIY) {
			if(ZoneActivity.isInside(urls.get(pagerPosition))) {
				isSelected = true;
				select.setImageResource(R.drawable.image_chosen);
			} else {
				isSelected = false;
				select.setImageResource(R.drawable.image_to_choose);
			}
		} else {
			select.setVisibility(View.GONE);
		}

		mPager.setCurrentItem(pagerPosition);
		
		
		select.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(isSelected) {
					if(activity == WEIXINACTIVITY && pagerPosition >= 0) {
						WeixinActivity.remove(urls.get(pagerPosition));
					} else if(activity == ZONEACTIVIY && pagerPosition >= 0) {
						ZoneActivity.removePicPath(urls.get(pagerPosition));
					}
					select.setImageResource(R.drawable.image_to_choose);
					isSelected = false;
				} else {
					if(activity == WEIXINACTIVITY && pagerPosition >= 0) {
						WeixinActivity.add(urls.get(pagerPosition));
					} else if(activity == ZONEACTIVIY && pagerPosition >= 0) {
						ZoneActivity.addPicPath(urls.get(pagerPosition));
					}
					select.setImageResource(R.drawable.image_chosen);
					isSelected = true;
				}
				
			}
		});
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(STATE_POSITION, mPager.getCurrentItem());
	}

	private class ImagePagerAdapter extends FragmentStatePagerAdapter {

		public ArrayList<String> fileList;

		public ImagePagerAdapter(FragmentManager fm, ArrayList<String> fileList) {
			super(fm);
			this.fileList = fileList;
		}

		@Override
		public int getCount() {
			return fileList == null ? 0 : fileList.size();
		}

		@Override
		public Fragment getItem(int position) {
			String url = fileList.get(position);
			return ImageDetailFragment.newInstance(url);
		}

	}
}

