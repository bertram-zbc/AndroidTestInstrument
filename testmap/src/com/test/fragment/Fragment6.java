package com.test.fragment;

import com.test.baidumap.R;
import com.test.fragment.Fragment1.OnCallBackListener;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class Fragment6 extends Fragment {
	
	private TextView jump;
	
	private TextView tvCountry;
	
	private TextView tvCounty;
	
	private TextView tvCity;
	
	private TextView tvBeiShangGuang;
	
	private TextView tvProv;
	
	private TextView tvOverSea;
	
	private TextView tvOther;
	
	private OnCallBackListener mListener;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment6, container, false);
		
		jump = (TextView) view.findViewById(R.id.textView1);
		jump.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				String s = "5&&0";
				mListener.onCallBack(s);
			}
		});
		
		tvCountry = (TextView) view.findViewById(R.id.textView4);
		tvCountry.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				String s = "5&乡镇农村&1";
				mListener.onCallBack(s);
			}
		});
		
		tvCounty = (TextView) view.findViewById(R.id.textView5);
		tvCounty.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				String s = "5&县或县级市&2";
				mListener.onCallBack(s);
			}
		});
		
		tvCity = (TextView) view.findViewById(R.id.textView6);
		tvCity.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				String s = "5&地级市&3";
				mListener.onCallBack(s);
			}
		});
		
		tvBeiShangGuang = (TextView) view.findViewById(R.id.textView7);
		tvBeiShangGuang.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				String s = "5&北京/上海/广州/深圳&5";
				mListener.onCallBack(s);
			}
		});
		
		tvProv = (TextView) view.findViewById(R.id.textView8);
		tvProv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				String s = "5&其他省会城市&4";
				mListener.onCallBack(s);
			}
		});
		
		tvOverSea = (TextView) view.findViewById(R.id.textView9);
		tvOverSea.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				String s = "5&海外&6";
				mListener.onCallBack(s);
			}
		});
		
		tvOther = (TextView) view.findViewById(R.id.textView10);
		tvOther.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				String s = "5&其他&6";
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