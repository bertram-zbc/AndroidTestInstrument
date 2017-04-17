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
import android.widget.TextView;

public class Fragment5 extends Fragment {
	
	private TextView jump;
	
	private TextView tvStudent;
	
	private TextView tvStaff;
	
	private TextView tvServant;
	
	private TextView tvWorker;
	
	private TextView tvNojob;
	
	private TextView tvIndividual;

	private TextView tvOther;
	
	private String send;
	
	private OnCallBackListener mListener;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment5, container, false);
		
		jump = (TextView) view.findViewById(R.id.textView1);
		jump.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				String s = "4&&0";
				mListener.onCallBack(s);
			}
		});
		
		tvStudent = (TextView) view.findViewById(R.id.textView4);
		tvStudent.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				String s = "4&ѧ��&1";
				mListener.onCallBack(s);
			}
		});
		
		tvStaff = (TextView) view.findViewById(R.id.textView5);
		tvStaff.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				String s = "4&����/��˾ְԱ&3";
				mListener.onCallBack(s);
			}
		});
		
		tvServant = (TextView) view.findViewById(R.id.textView6);
		tvServant.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				String s = "4&����Ա/��ҵ��λ&4";
				mListener.onCallBack(s);
			}
		});
		
		tvWorker = (TextView) view.findViewById(R.id.textView7);
		tvWorker.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				String s = "4&����/������ҵ��Ա&5";
				mListener.onCallBack(s);
			}
		});
		
		tvNojob = (TextView) view.findViewById(R.id.textView8);
		tvNojob.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				String s = "4&��ҵ/����/ʧҵ/�¸�&6";
				mListener.onCallBack(s);
			}
		});
		
		tvIndividual = (TextView) view.findViewById(R.id.textView9);
		tvIndividual.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				String s = "4&����ְҵ/����/˽Ӫ��ҵ��&2";
				mListener.onCallBack(s);
			}
		});
		
		tvOther = (TextView) view.findViewById(R.id.textView10);
		tvOther.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				String s = "4&����&7";
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