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
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class Fragment3 extends Fragment {
	
	private TextView jump;
	
	private TextView tvSure;
	
	private TextView seekAge;
	
	private SeekBar mSeekBar;
	
	private int SeekBarLength;
	
	private int getProgress;
	
	private View view;
	private int screenWidth;//屏幕长度
	private float scale;//分辨率
	
	private TextMoveLayout textMoveLayout;
	 
    private ViewGroup.LayoutParams layoutParams;
	
	private OnCallBackListener mListener;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		view = inflater.inflate(R.layout.fragment3, container, false);
		
		screenWidth = this.getActivity().getWindowManager().getDefaultDisplay().getWidth();
		scale = this.getActivity().getResources().getDisplayMetrics().density;
		getProgress=30;
		SeekBarLength = screenWidth - (int)( 60 * scale + 0.5f) ;
		
		/**
		 * 设置textview样式
		 */
		seekAge = new TextView(this.getActivity());
        seekAge.setBackgroundResource(R.drawable.seekage);
        seekAge.setTextColor(Color.parseColor("#ffffff"));
        seekAge.setText("40");
        seekAge.setTextSize(18);
        layoutParams = new ViewGroup.LayoutParams(screenWidth, 50);
        textMoveLayout = (TextMoveLayout) view.findViewById(R.id.textLayout);
        textMoveLayout.addView(seekAge, layoutParams);
        seekAge.layout(screenWidth/2-50, 0, screenWidth/2+50, 100);
		seekAge.getPaint().setFakeBoldText(true);
		seekAge.setGravity(Gravity.CENTER_HORIZONTAL);
		
		jump = (TextView) view.findViewById(R.id.textView1);
		jump.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				String s = "2&&30";
				mListener.onCallBack(s);
			}
		});
		
//		seekAge = (TextView) view.findViewById(R.id.seekage);
		
		mSeekBar = (SeekBar) view.findViewById(R.id.seekBar1);
		mSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				
				getProgress = mSeekBar.getProgress()+10;
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				seekAge.layout((int)(30*scale+0.5f+(SeekBarLength/60)*progress-50), 0, (int)(30*scale+0.5f+(SeekBarLength/60)*progress+50) , 100);
				seekAge.setText(Integer.toString(progress+10));
				
			}
		});
		
		tvSure = (TextView) view.findViewById(R.id.textView4);
		tvSure.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				String s = "2&"+Integer.toString(getProgress)+"&"+getProgress;
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