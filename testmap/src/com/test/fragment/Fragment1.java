package com.test.fragment;

import com.test.baidumap.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class Fragment1 extends Fragment {
	
	private TextView textView;
	private OnCallBackListener mListener;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment1, container, false);
		
		textView = (TextView) view.findViewById(R.id.textView3);
		textView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				String s = "0&begin&begin";
				mListener.onCallBack(s);
				
			}
		});
		return view;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mListener = (OnCallBackListener) activity;
	}
	
	
	public interface OnCallBackListener{
		public void onCallBack(String str);
	}

}
