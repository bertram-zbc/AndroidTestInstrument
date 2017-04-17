package com.test.fragment;

import java.util.Calendar;

import com.test.baidumap.R;
import com.test.fragment.Fragment1.OnCallBackListener;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView.FindListener;
import android.widget.DatePicker;

public class DatePickerFragment extends DialogFragment {

	private OnCallBackListener mListener;
	private String s;
	private int year,month,day;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		final Calendar c = Calendar.getInstance();  
        year = c.get(Calendar.YEAR);  
        month = c.get(Calendar.MONTH);  
        day = c.get(Calendar.DAY_OF_MONTH); 
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());  
        LayoutInflater inflater = getActivity().getLayoutInflater();  
        View view = inflater.inflate(R.layout.datepicker, null); 
        final DatePicker datePicker = (DatePicker) view.findViewById(R.id.datepicker);
        builder.setView(view).setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int id) {
				// TODO Auto-generated method stub
				int month = datePicker.getMonth()+1;
				String s = datePicker.getYear()+"."+ month +"."+datePicker.getDayOfMonth();
				//String s = year+"."+month+"."+day;
				System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!"+s);
				mListener.onCallBack(s);
			}
		}).setNegativeButton("取消", null);  
        return builder.create();   
	}
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		mListener = (OnCallBackListener) activity;
	}

}
