package com.example.imageloader;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.imageloader.ImageLoader.Type;
import com.test.baidumap.R;

public class ImageAdapter extends BaseAdapter {
	public static ArrayList<String> mSelectedImg = new ArrayList<String>();
	
	private String mDirPath;
	private List<String> mImgPaths;
	private LayoutInflater mInflater;
	
	private int mScreenWidth;
	
	public ImageAdapter(Context context, List<String> mDatas, String dirPath) {
		this.mDirPath = dirPath;
		this.mImgPaths = mDatas;
		this.mInflater = LayoutInflater.from(context);
		
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		mScreenWidth = outMetrics.widthPixels;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mImgPaths.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mImgPaths.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		
		if(convertView == null) {
			convertView = mInflater.inflate(R.layout.imageloader_item_gridview, parent, false);
			
			viewHolder = new ViewHolder();
			viewHolder.mImg = (ImageView) convertView.findViewById(R.id.id_item_image);
			viewHolder.mSelect = (ImageButton) convertView.findViewById(R.id.id_item_select);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		//閲嶇疆鐘舵�
		//viewHolder.mImg.setImageResource(R.drawable.before);
		viewHolder.mSelect.setImageResource(R.drawable.to_select);
		viewHolder.mSelect.setVisibility(View.INVISIBLE);
		viewHolder.mImg.setColorFilter(null);
		
		viewHolder.mImg.setMaxWidth(mScreenWidth / 3);
		
		ImageLoader.getInstance(3, Type.LIFO).loadImage(mDirPath + "/" + mImgPaths.get(position), viewHolder.mImg);
		
		final String filePath = mDirPath + "/" + mImgPaths.get(position);
		viewHolder.mImg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(mSelectedImg.contains(filePath)) {//宸茬粡琚�鎷�
					mSelectedImg.remove(filePath);
					viewHolder.mImg.setColorFilter(null);
//					viewHolder.mSelect.setImageResource(R.drawable.to_select);
					viewHolder.mSelect.setVisibility(View.INVISIBLE);
				} else {//鏈閫夋嫨
					mSelectedImg.add(filePath);
					viewHolder.mImg.setColorFilter(Color.parseColor("#77000000"));
//					viewHolder.mSelect.setImageResource(R.drawable.chosen);
					viewHolder.mSelect.setVisibility(View.VISIBLE);
				}
				notifyDataSetChanged();
				
			}
		});
		
		if(mSelectedImg.contains(filePath)) {
			viewHolder.mSelect.setVisibility(View.VISIBLE);
			viewHolder.mImg.setColorFilter(Color.parseColor("#77000000"));
			viewHolder.mSelect.setImageResource(R.drawable.to_select);
		}
		
		
		return convertView;
	}
	
	private class ViewHolder {
		ImageView mImg;
		ImageButton mSelect;
	}
	
}