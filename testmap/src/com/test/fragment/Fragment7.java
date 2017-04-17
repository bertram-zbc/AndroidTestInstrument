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

public class Fragment7 extends Fragment {
	
	private TextView jump;
	
	private TextView tvArch;
	private int numArch=0;
	
	private TextView tvRiver;
	private int numRiver=0;
	
	private TextView tvNature;
	private int numNature=0;
	
	private TextView tvCountry;
	private int numCountry=0;
	
	private TextView tvThemePark;
	private int numThemePark=0;
	
	private TextView tvSea;
	private int numSea=0;
	
	private TextView tvOutsport;
	private int numOutsport=0;
	
	private TextView tvRelax;
	private int numRelax=0;
	
	private TextView tvShop;
	private int numShop=0;
	
	private TextView tvSure;
	
	private OnCallBackListener mListener;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment7, container, false);

		jump = (TextView) view.findViewById(R.id.textView1);
		jump.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				String s = "6&&000000000";
				mListener.onCallBack(s);
			}
		});
		
		
		tvShop = (TextView) view.findViewById(R.id.textView13);
		tvShop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				numShop++;
				if(numShop%2==1){
					tvShop.setTextColor(Color.parseColor("#70a7ba"));
				}
				else{
					tvShop.setTextColor(Color.parseColor("#bbbbbb"));
				}
			}
		});
		
		tvArch = (TextView) view.findViewById(R.id.textView4);
		tvArch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				numArch++;
				if(numArch%2==1){
					tvArch.setTextColor(Color.parseColor("#70a7ba"));
				}
				else{
					tvArch.setTextColor(Color.parseColor("#bbbbbb"));
				}
			}
		});
		
		tvRiver = (TextView) view.findViewById(R.id.textView5);
		tvRiver.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				numRiver++;
				if(numRiver%2==1){
					tvRiver.setTextColor(Color.parseColor("#70a7ba"));
				}
				else{
					tvRiver.setTextColor(Color.parseColor("#bbbbbb"));
				}
			}
		});
		
		tvNature = (TextView) view.findViewById(R.id.textView6);
		tvNature.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				numNature++;
				if(numNature%2==1){
					tvNature.setTextColor(Color.parseColor("#70a7ba"));
				}
				else{
					tvNature.setTextColor(Color.parseColor("#bbbbbb"));
				}
			}
		});
		
		tvCountry = (TextView) view.findViewById(R.id.textView7);
		tvCountry.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				numCountry++;
				if(numCountry%2==1){
					tvCountry.setTextColor(Color.parseColor("#70a7ba"));
				}
				else{
					tvCountry.setTextColor(Color.parseColor("#bbbbbb"));
				}
			}
		});
		
		tvThemePark = (TextView) view.findViewById(R.id.textView8);
		tvThemePark.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				numThemePark++;
				if(numThemePark%2==1){
					tvThemePark.setTextColor(Color.parseColor("#70a7ba"));
				}
				else{
					tvThemePark.setTextColor(Color.parseColor("#bbbbbb"));
				}
			}
		});
		
		tvSea = (TextView) view.findViewById(R.id.textView9);
		tvSea.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				numSea++;
				if(numSea%2==1){
					tvSea.setTextColor(Color.parseColor("#70a7ba"));
				}
				else{
					tvSea.setTextColor(Color.parseColor("#bbbbbb"));
				}
			}
		});
		
		tvOutsport = (TextView) view.findViewById(R.id.textView10);
		tvOutsport.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				numOutsport++;
				if(numOutsport%2==1){
					tvOutsport.setTextColor(Color.parseColor("#70a7ba"));
				}
				else{
					tvOutsport.setTextColor(Color.parseColor("#bbbbbb"));
				}
			}
		});
		
		tvRelax = (TextView) view.findViewById(R.id.textView11);
		tvRelax.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				numRelax++;
				if(numRelax%2==1){
					tvRelax.setTextColor(Color.parseColor("#70a7ba"));
				}
				else{
					tvRelax.setTextColor(Color.parseColor("#bbbbbb"));
				}
			}
		});
		
		tvSure = (TextView) view.findViewById(R.id.textView12);
		tvSure.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String s = "6&";
				String str="";
				if(numArch%2==1){
					s=s+"古迹建筑%";
					str=str+"1";
				}else{
					str=str+"0";
				}
				if(numRiver%2==1){
					s=s+"沿河风光%";
					str=str+"1";
				}else{
					str=str+"0";
				}
				if(numNature%2==1){
					s=s+"自然山水%";
					str=str+"1";
				}else{
					str=str+"0";
				}
				if(numCountry%2==1){
					s=s+"古村古镇%";
					str=str+"1";
				}else{
					str=str+"0";
				}
				if(numThemePark%2==1){
					s=s+"主题公园%";
					str=str+"1";
				}else{
					str=str+"0";
				}
				if(numSea%2==1){
					s=s+"海边风景%";
					str=str+"1";
				}else{
					str=str+"0";
				}
				if(numShop%2==1){
					s=s+"购物中心%";
					str=str+"1";
				}else{
					str=str+"0";
				}
				if(numOutsport%2==1){
					s=s+"户外运动%";
					str=str+"1";
				}else{
					str=str+"0";
				}
				if(numRelax%2==1){
					s=s+"休闲放松%";
					str=str+"1";
				}else{
					str=str+"0";
				}
				s=s+"&"+str;
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
