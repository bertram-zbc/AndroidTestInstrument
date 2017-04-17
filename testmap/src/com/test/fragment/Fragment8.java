package com.test.fragment;

import com.test.baidumap.R;
import com.test.fragment.Fragment1.OnCallBackListener;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class Fragment8 extends Fragment {
	
	private TextView jump;
	
	private TextView tvSport;
	private int numSport=0;
	
	private TextView tvQuiet;
	private int numQuiet=0;
	
	private TextView tvNoisy;
	private int numNoisy=0;
	
	private TextView tvHistory;
	private int numHistory=0;
	
	private TextView tvSnack;
	private int numSnack=0;
	
	private TextView tvCultrue;
	private int numCultrue=0;
	
	private TextView tvSure;
	
	private OnCallBackListener mListener;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment8, container, false);
		
		jump = (TextView) view.findViewById(R.id.textView1);
		jump.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				String s = "7&&0000";
				mListener.onCallBack(s);
			}
		});
		
		tvSport = (TextView) view.findViewById(R.id.textView4);
		tvSport.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				numSport++;
				if(numSport%2==1){
					tvSport.setTextColor(Color.parseColor("#ef6771"));
				}
				else{
					tvSport.setTextColor(Color.parseColor("#bbbbbb"));
				}
			}
		});
		
		tvQuiet = (TextView) view.findViewById(R.id.textView5);
		tvQuiet.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				numQuiet++;
				if(numQuiet%2==1){
					tvQuiet.setTextColor(Color.parseColor("#ef6771"));
				}
				else{
					tvQuiet.setTextColor(Color.parseColor("#bbbbbb"));
				}
			}
		});
		
		tvNoisy = (TextView) view.findViewById(R.id.textView6);
		tvNoisy.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				numNoisy++;
				if(numNoisy%2==1){
					tvNoisy.setTextColor(Color.parseColor("#ef6771"));
				}
				else{
					tvNoisy.setTextColor(Color.parseColor("#bbbbbb"));
				}
			}
		});
		
		tvHistory = (TextView) view.findViewById(R.id.textView7);
		tvHistory.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				numHistory++;
				if(numHistory%2==1){
					tvHistory.setTextColor(Color.parseColor("#ef6771"));
				}
				else{
					tvHistory.setTextColor(Color.parseColor("#bbbbbb"));
				}
			}
		});
		
		tvSnack = (TextView) view.findViewById(R.id.textView8);
		tvSnack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				numSnack++;
				if(numSnack%2==1){
					tvSnack.setTextColor(Color.parseColor("#ef6771"));
				}
				else{
					tvSnack.setTextColor(Color.parseColor("#bbbbbb"));
				}
			}
		});
		
		tvCultrue = (TextView) view.findViewById(R.id.textView9);
		tvCultrue.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				numCultrue++;
				if(numCultrue%2==1){
					tvCultrue.setTextColor(Color.parseColor("#ef6771"));
				}
				else{
					tvCultrue.setTextColor(Color.parseColor("#bbbbbb"));
				}
			}
		});
		
		
		tvSure = (TextView) view.findViewById(R.id.textView11);
		tvSure.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String s = "7&";
				String str="";
				if(numSport%2==1){
					s=s+"运动%";
					str=str+"1";
				}else{
					str=str+"0";
				}
				if(numQuiet%2==1){
					s=s+"安静%";
					str=str+"1";
				}else{
					str=str+"0";
				}
				if(numNoisy%2==1){
					s=s+"热闹%";
				}
				if(numHistory%2==1){
					s=s+"历史%";
					str=str+"1";
				}else{
					str=str+"0";
				}
				if(numSnack%2==1){
					s=s+"小吃%";
					str=str+"1";
				}else{
					str=str+"0";
				}
				if(numCultrue%2==1){
					s=s+"文化%";
					str=str+"1";
				}else{
					str=str+"0";
				}
//				if(numSport%2==0&&numQuiet%2==0&&numNoisy%2==0&&numHistory%2==0&&numSnack%2==0&&numCultrue%2==0){
//					s=s+" &"+str+"&";
//				}else{
					s=s+"&"+str;
//				}
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
