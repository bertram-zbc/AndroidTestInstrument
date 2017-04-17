package com.test.fragment;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;

public class MyFragmentPagerAdapter extends FragmentStatePagerAdapter{
	
	private List<Fragment>fragList;

	public MyFragmentPagerAdapter(FragmentManager fm,List<Fragment>fragList) {
		super(fm);
		this.fragList = fragList;
	}

	@Override
	public Fragment getItem(int position) {
		Fragment page = null;
		 if (fragList.size() > position) {
	            page = fragList.get(position);
	            if (page != null) {
	                return page;
	            }
	        }

	        while (position>=fragList.size()) {
	        	fragList.add(null);
	        }
	        fragList.set(position, page);
	        return page;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return fragList.size();
	}

}
