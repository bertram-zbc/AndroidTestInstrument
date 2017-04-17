package com.example.imagepager;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.test.baidumap.R;

import android.app.Application;

public class MyApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder() //
				.showImageForEmptyUri(R.drawable.user) //
				.showImageOnFail(R.drawable.user) //
				.cacheInMemory(true) //
				.cacheOnDisk(true) //
				.build();//
		ImageLoaderConfiguration config = new ImageLoaderConfiguration//
		.Builder(getApplicationContext())//
				.defaultDisplayImageOptions(defaultOptions)//
				.diskCacheSize(50 * 1024 * 1024)
				.diskCacheFileCount(100)// ª∫¥Ê“ª∞Ÿ’≈Õº∆¨
				.writeDebugLogs()//
				.build();//
		ImageLoader.getInstance().init(config);
	}
}
