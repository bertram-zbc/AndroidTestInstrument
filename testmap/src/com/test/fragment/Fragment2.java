package com.test.fragment;

import com.test.baidumap.R;
import com.test.fragment.Fragment1.OnCallBackListener;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.ImageView;
import android.widget.TextView;

public class Fragment2 extends Fragment {
	
	private TextView jump;
	private ImageView imageFemale;
	private ImageView imageMale;
	private OnCallBackListener mListener;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment2, container, false);
		
		jump = (TextView) view.findViewById(R.id.textView1);
		jump.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				String s = "1&&0";
				mListener.onCallBack(s);
			}
		});

		imageFemale = (ImageView) view.findViewById(R.id.id_preview);
		imageFemale.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				String s = "1&Å®&1";
				mListener.onCallBack(s);
			}
		});
		
		imageMale = (ImageView) view.findViewById(R.id.imageView2);
		imageMale.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				String s = "1&ÄÐ&0";
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

}