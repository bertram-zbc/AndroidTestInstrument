package com.example.imageloader;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.imageloader.ListImageDirProupWindow.OnDirSelectedListener;
import com.example.imagepager.ImagePagerActivity;
import com.example.sendpicture.PictureSender;

import android.content.SharedPreferences.Editor;
import com.test.baidumap.R;
import com.test.baidumap.ZoneActivity;
import com.testmap.logic.ClientThread;




public class WeixinActivity extends ActionBarActivity {
	public final static int ZONEACTIVIY = 0x1;
	public final static int PERSONACTIVIY = 0x2;
	public final static String WHATACTIVITY = "whatactivity";
	
	private GridView mGridView;
	private List<String> mImgs;
	private static ImageAdapter mImageAdapter;
	
	private RelativeLayout mBottomLy;
	private TextView mDirName;
	private ImageView mImageOk;
	private ImageView mImagePreView;
	
	private File mCurrentDir;
	private int mMaxCount;
	
	private List<FolderBean> mFolderBeans = new ArrayList<FolderBean>();
	
	private ProgressDialog mProgressDialog;
	
	private ListImageDirProupWindow mDirProupWindow;
	
	private SharedPreferences username;
	private SharedPreferences user_headpic;
	private Editor editor;
	
	private FilenameFilter imageFilenameFilter = new FilenameFilter() {
		
		@Override
		public boolean accept(File dir, String filename) {
			if(filename.endsWith(".jpg") || filename.endsWith(".jpeg") || filename.endsWith("png")) {
				return true;
			}
			return false;
		}
	};
	
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if(msg.what == 0x110) {
				mProgressDialog.dismiss();
				//绑定数据到View中
				data2View();
				
				initDirPopupWindow();
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.imageloader_main);
		ImageAdapter.mSelectedImg.clear();//销毁选择的项
		initView();
		initDatas();
		initEvent();
		username = this.getSharedPreferences("Pref01", MODE_PRIVATE);
		String s = new String();
		s = username.getString("username", "none");
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+s);
		user_headpic = this.getSharedPreferences(s, MODE_PRIVATE);
		editor = user_headpic.edit();
	}


	protected void initDirPopupWindow() {
		mDirProupWindow = new ListImageDirProupWindow(this, mFolderBeans);
		
		mDirProupWindow.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				lightOn();
			}
		});
		
		mDirProupWindow.setOnDirSelectedListener(new OnDirSelectedListener() {
			
			@Override
			public void onSelected(FolderBean folderBean) {
				mCurrentDir = new File(folderBean.getDir());
				
				mImgs = Arrays.asList(mCurrentDir.list(imageFilenameFilter));
				
				mImageAdapter = new ImageAdapter(WeixinActivity.this, mImgs, mCurrentDir.getAbsolutePath());
				mGridView.setAdapter(mImageAdapter);
				
				mDirName.setText(folderBean.getName());
				mDirProupWindow.dismiss();
				
			}
		});
	}

	


	protected void data2View() {
		// TODO Auto-generated method stub
		if(mCurrentDir == null) {
			Toast.makeText(this, "未扫描到任何图片", Toast.LENGTH_SHORT).show();
			return;
		}
		mImgs = Arrays.asList(mCurrentDir.list(imageFilenameFilter));
		mImageAdapter = new ImageAdapter(this, mImgs, mCurrentDir.getAbsolutePath());
		mGridView.setAdapter(mImageAdapter);
		
		mDirName.setText(mCurrentDir.getName());
	}


	private void initEvent() {
		mBottomLy.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mDirProupWindow.setAnimationStyle(R.style.AppTheme);
				mDirProupWindow.showAsDropDown(mBottomLy, 0, 0);
				
				lightOff();
			}
		});
		mImageOk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int activity = WeixinActivity.this.getIntent().getIntExtra(WHATACTIVITY, 0);
				if(activity == ZONEACTIVIY) {
					ZoneActivity.addPicPaths(ImageAdapter.mSelectedImg);
					WeixinActivity.this.finish();
				} else if(activity == PERSONACTIVIY) {
					if(ImageAdapter.mSelectedImg.size() < 1) {
						Toast.makeText(WeixinActivity.this, "请至少选择一张图片", Toast.LENGTH_SHORT).show();
						return;
					}
//					PersonActivity.changeHeadPic(ImageAdapter.mSelectedImg.get(0));
					editor.putString("头像", ImageAdapter.mSelectedImg.get(0));
					editor.commit();
					System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+ImageAdapter.mSelectedImg.get(0));
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							ArrayList<String> pics = new ArrayList<String>();
							pics.add(ImageAdapter.mSelectedImg.get(0));
							new PictureSender(pics, ClientThread.getUserName(), ClientThread.getUserPsd(), "head").run();
							String headPicName = new File(ImageAdapter.mSelectedImg.get(0)).getName();
							new ClientThread().println("updateHeadPic&&" + headPicName);
							
						}
					}).start();
					WeixinActivity.this.finish();
				}
			}
		});
		mImagePreView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(ImageAdapter.mSelectedImg.size() < 1) {
					Toast.makeText(WeixinActivity.this, "请至少选择一张图片", Toast.LENGTH_SHORT).show();
					return;
				}
				Intent intent = new Intent(WeixinActivity.this,ImagePagerActivity.class);
				intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, 0);
				intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, ImageAdapter.mSelectedImg);
				intent.putExtra(ImagePagerActivity.WHATACTIVITY, ImagePagerActivity.WEIXINACTIVITY);
				startActivity(intent);
			}
		});
		
	}
	
	/**
	 * 内容区域变亮
	 */
	protected void lightOn() {
		// TODO Auto-generated method stub
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = 1.0f;
		getWindow().setAttributes(lp);
	}
	
	/**
	 * 内容区域变暗
	 */
	protected void lightOff() {
		// TODO Auto-generated method stub
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = .3f;
		getWindow().setAttributes(lp);
	}


	/**
	 * 利用ContentProvider扫描手机中的所有图片
	 */
	private void initDatas() {
		if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			Toast.makeText(this, "当前存储卡不可用！", Toast.LENGTH_SHORT).show();
			return;
		}
		
		mProgressDialog = ProgressDialog.show(this, null, "正在加载...");
		new Thread() {
			public void run() {
				Uri mImgUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				ContentResolver cr = WeixinActivity.this.getContentResolver();
				
				Cursor cursor = cr.query(mImgUri, null, MediaStore.Images.Media.MIME_TYPE + " = ? or " + MediaStore.Images.Media.MIME_TYPE + " = ? ", new String[] {"image/jpeg", "image/png" }, MediaStore.Images.Media.DATE_MODIFIED);
				
				Set<String> mDirPaths = new HashSet<String>();
				
				while(cursor.moveToNext()) {
					String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
					File parentFile = new File(path).getParentFile();
					if(parentFile == null) {
						continue;
					}
					String dirPath = parentFile.getAbsolutePath();
					
					FolderBean folderBean = null;
					
					if(mDirPaths.contains(dirPath)) {
						continue;
					} else {
						mDirPaths.add(dirPath);
						folderBean = new FolderBean();
						folderBean.setDir(dirPath);
						folderBean.setFirstImgPath(path);
						
						
					}
					
					if(parentFile.list() == null) {
						continue;
					}
					
					int picSize = parentFile.list(imageFilenameFilter).length;
					folderBean.setCount(picSize);
					mFolderBeans.add(folderBean);
					
					if(picSize > mMaxCount) {
						mMaxCount = picSize;
						mCurrentDir = parentFile;
					}
					
				}
				
				cursor.close();
				//通知Handler扫描完成
				mHandler.sendEmptyMessage(0x110);
				
			};
		}.start();
		
	}


	private void initView() {
		// TODO Auto-generated method stub
		mGridView = (GridView) findViewById(R.id.id_gridView);
		mBottomLy = (RelativeLayout) findViewById(R.id.id_bottom_ly);
		mDirName = (TextView) findViewById(R.id.id_dir_name);
		mImageOk = (ImageView) findViewById(R.id.id_chooseOk);
		mImagePreView = (ImageView) findViewById(R.id.id_preview);
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public static void remove(String picUrl) {
		ImageAdapter.mSelectedImg.remove(picUrl);
		mImageAdapter.notifyDataSetChanged();
	}
	public static void add(String picUrl) {
		if(!ImageAdapter.mSelectedImg.contains(picUrl)) {
			ImageAdapter.mSelectedImg.add(picUrl);
			mImageAdapter.notifyDataSetChanged();
		}
	}
	public static boolean isInside(String picUrl) {
		return ImageAdapter.mSelectedImg.contains(picUrl);
	}
	
	
}