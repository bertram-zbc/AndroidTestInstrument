package com.test.fragment;

import com.test.baidumap.R;
import com.test.fragment.Fragment1.OnCallBackListener;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class Fragment4 extends Fragment {
	
	private TextView jump;
	
	private TextView tvSure;
	
	private TextView seekMoney;
	
	private SeekBar mSeekBar;
	
	private int SeekBarLength;
	
	private String money = new String();
	
	private View view;
	private int screenWidth;//屏幕长度
	private float scale;//分辨率
	
	private String send;
	
	private TextMoveLayout textMoveLayout;
	 
    private ViewGroup.LayoutParams layoutParams;
	
	private OnCallBackListener mListener;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		view = inflater.inflate(R.layout.fragment4, container, false);

		screenWidth = this.getActivity().getWindowManager().getDefaultDisplay().getWidth();
		scale = this.getActivity().getResources().getDisplayMetrics().density;
		SeekBarLength = screenWidth - (int)( 60 * scale + 0.5f) ;
		
		seekMoney = new TextView(this.getActivity());
        seekMoney.setBackgroundResource(R.drawable.seekmoney);
        seekMoney.setTextColor(Color.parseColor("#ffffff"));
        seekMoney.setText("5000");
        seekMoney.setTextSize(15);
        layoutParams = new ViewGroup.LayoutParams(screenWidth, 50);
        textMoveLayout = (TextMoveLayout) view.findViewById(R.id.textLayout);
        textMoveLayout.addView(seekMoney, layoutParams);
        seekMoney.layout(screenWidth/2-100, 0, screenWidth/2+100, 100);
		seekMoney.getPaint().setFakeBoldText(true);
		seekMoney.setGravity(Gravity.CENTER_HORIZONTAL);
		
		money = "4000-4999元";
		send="5";
		
		jump = (TextView) view.findViewById(R.id.textView1);
		jump.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				String s = "3&&4";
				mListener.onCallBack(s);
			}
		});
		
		mSeekBar = (SeekBar) view.findViewById(R.id.seekBar1);
		mSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				int seekProgress = mSeekBar.getProgress();  
				if(seekProgress<=7){
					mSeekBar.setProgress(0);
					money="1000元以下";
					send="1";
				}else if(seekProgress>7 && seekProgress<=21){
					mSeekBar.setProgress(14);
					money="1000-1999元";
					send="2";
				}else if(seekProgress>21 && seekProgress<=35){
					mSeekBar.setProgress(28);
					money="2000-2999元";
					send="3";
				}else if(seekProgress>35 && seekProgress<=49){
					mSeekBar.setProgress(42);
					money="3000-3999元";
					send="4";
				}else if(seekProgress>49 && seekProgress<=63){
					mSeekBar.setProgress(56);
					money="4000-4999元";
					send="5";
				}else if(seekProgress>63 && seekProgress<=77){
					mSeekBar.setProgress(70);
					money="5000-7999元";
					send="6";
				}else if(seekProgress>77 && seekProgress<=91){
					mSeekBar.setProgress(84);
					money="8000-11999元";
					send="7";
				}else if(seekProgress>91){
					mSeekBar.setProgress(100);
					money="12000元以上";
					send="8";
				}

			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				if(progress<=7){
					seekMoney.layout((int)(30*scale+0.5f+(SeekBarLength/100)*progress-60), 0, (int)(30*scale+0.5f+(SeekBarLength/100)*progress+140) , 100);
				}else if(progress>91){
					seekMoney.layout((int)(30*scale+0.5f+(SeekBarLength/100)*progress-140), 0, (int)(30*scale+0.5f+(SeekBarLength/100)*progress+60) , 100);
				}else{
					seekMoney.layout((int)(30*scale+0.5f+(SeekBarLength/100)*progress-100), 0, (int)(30*scale+0.5f+(SeekBarLength/100)*progress+100) , 100);
				}
				if(progress<=7){
					seekMoney.setText("1k-");
				}else if(progress>7 && progress<=21){
					seekMoney.setText("1k-2k");
				}else if(progress>21 && progress<=35){
					seekMoney.setText("2k-3k");
				}else if(progress>35 && progress<=49){
					seekMoney.setText("3k-4k");
				}else if(progress>49 && progress<=63){
					seekMoney.setText("4k-5k");
				}else if(progress>63 && progress<=77){
					seekMoney.setText("5k-8k");
				}else if(progress>77 && progress<=91){
					seekMoney.setText("8k-12k");
				}else if(progress>91){
					seekMoney.setText("12k+");
				}
								
			}
		});

		tvSure = (TextView) view.findViewById(R.id.textView4);
		tvSure.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				String s = "3&"+money+"&"+send;
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