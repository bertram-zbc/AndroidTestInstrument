package com.test.fragment;

import com.test.baidumap.R;
import com.test.fragment.Fragment1.OnCallBackListener;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.TextView;

public class Fragment9 extends Fragment {
	
	private TextView tv1;
	private TextView tv2;
	private TextView tv3;
	private TextView tv4;
	private TextView tv5;
	private TextView tv6;
	
	Animation anim = null;
	private OnCallBackListener mListener;
	
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				tv1.setVisibility(View.VISIBLE);
				tv1.startAnimation(anim);
				break;
			case 2:
				tv2.setVisibility(View.VISIBLE);
				tv2.startAnimation(anim);
				break;
			case 3:
				tv3.setVisibility(View.VISIBLE);
				tv3.startAnimation(anim);
				break;				
			case 4:
				tv4.setVisibility(View.VISIBLE);
				tv4.startAnimation(anim);
				break;				
			case 5:
				tv5.setVisibility(View.VISIBLE);
				tv5.startAnimation(anim);
				break;
			case 6:
				tv6.setVisibility(View.VISIBLE);
				tv6.startAnimation(anim);
				break;
			default:
				break;
			}
		};
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment9, container, false);
		
		tv1 = (TextView) view.findViewById(R.id.textView1);
		tv2 = (TextView) view.findViewById(R.id.textView2);
		tv3 = (TextView) view.findViewById(R.id.textView3);
		tv4 = (TextView) view.findViewById(R.id.textView4);
		tv5 = (TextView) view.findViewById(R.id.textView5);
		tv6 = (TextView) view.findViewById(R.id.textView6);
		
		anim = AnimationUtils.loadAnimation(this.getActivity(), R.anim.text_flow_in);
		
//		TimerTask task1 = new TimerTask() {
//			
//			@Override
//			public void run() {
//				
//			}
//		};
//		
//		Timer timer1 = new Timer();
//		timer1.schedule(task1, 10000);
//		tv1.setVisibility(View.VISIBLE);
//		tv1.startAnimation(anim);
//
//		TimerTask task2 = new TimerTask() {
//			
//			@Override
//			public void run() {
//				
//			}
//		};
//		
//		Timer timer2 = new Timer();
//		timer2.schedule(task2, 20000);
//		tv2.setVisibility(View.VISIBLE);
//		tv2.startAnimation(anim);
//		
//		
//		TimerTask task3 = new TimerTask() {
//			
//			@Override
//			public void run() {
//				
//			}
//		};
//		
//		Timer timer3 = new Timer();
//		timer3.schedule(task3, 30000);
//		tv3.setVisibility(View.VISIBLE);
//		tv3.startAnimation(anim);
//		
//		TimerTask task4 = new TimerTask() {
//			
//			@Override
//			public void run() {
//				
//			}
//		};
//		
//		Timer timer4 = new Timer();
//		timer4.schedule(task4, 40000);
//		tv4.setVisibility(View.VISIBLE);
//		tv4.startAnimation(anim);
//		
//		TimerTask task5 = new TimerTask() {
//			
//			@Override
//			public void run() {
//				
//			}
//		};
//		
//		Timer timer5 = new Timer();
//		timer5.schedule(task5, 50000);
//		tv5.setVisibility(View.VISIBLE);
//		tv5.startAnimation(anim);
//		
//		TimerTask task6 = new TimerTask() {
//			
//			@Override
//			public void run() {
//				
//			}
//		};
//		
//		Timer timer6 = new Timer();
//		timer6.schedule(task6, 60000);
//		tv6.setVisibility(View.VISIBLE);
//		tv6.startAnimation(anim);
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				handler.sendEmptyMessage(1);
			}
		}).start();
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				handler.sendEmptyMessage(2);
			}
		}).start();

		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Thread.sleep(4500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				handler.sendEmptyMessage(3);
			}
		}).start();

		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Thread.sleep(6000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				handler.sendEmptyMessage(4);
			}
		}).start();

		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Thread.sleep(7500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				handler.sendEmptyMessage(5);
			}
		}).start();

		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				handler.sendEmptyMessage(6);
			}
		}).start();


		
		tv6.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String s = "8&end&end";
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
