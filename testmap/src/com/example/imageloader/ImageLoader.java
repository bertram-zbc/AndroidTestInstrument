package com.example.imageloader;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.util.DisplayMetrics;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

/**
 * 图片加载类
 * @author Hzq
 *
 */
public class ImageLoader {
	private static ImageLoader mInstance;
	
	/**
	 * 图片缓存的核心对象
	 */
	private LruCache<String, Bitmap> mLruCache;
	/**
	 * 线程池
	 */
	private ExecutorService mThreadPool;
	private static final int DEAFULT_THREAD_COUNT = 1;
	/**
	 * 队列的调度方式
	 */
	private static Type mType = Type.LIFO;
	/**
	 * 任务队列
	 */
	private LinkedList<Runnable> mTaskQueue;
	/**
	 * 后台轮询线程
	 */
	private Thread mPoolThread;
	private Handler mPoolThreadHandler;
	
	/**
	 * UI线程中的Handler
	 */
	private Handler mUIHandler;
	
	private Semaphore mSemaphorePoolTheadHandler = new Semaphore(0);
	
	private Semaphore mSemaphoreThreadPool;
	
	private class ImageSize {
		public int width;
		public int height;
	}
	protected ImageSize getImageViewSize(ImageView imageView) {
		ImageSize imageSize = new ImageSize();
		DisplayMetrics displayMetrics = imageView.getContext().getResources().getDisplayMetrics();
		
		LayoutParams lp = imageView.getLayoutParams();
		int width = imageView.getWidth();
		//int width = (lp.width == LayoutParams.WRAP_CONTENT ? 0 : imageView.getWidth());
		
		if(width <= 0) {
			width = lp.width;//获取imageView在layout中声明的宽度
		}
		if(width <= 0) {
			width = getImageViewFieldValue(imageView, "mMaxWidth");//检查最大值
		}
		
		if(width <= 0) {
			width = displayMetrics.widthPixels;
		}
		
		int height = imageView.getHeight();
		//int width = (lp.width == LayoutParams.WRAP_CONTENT ? 0 : imageView.getWidth());
		
		if(height <= 0) {
			height = lp.width;//获取imageView在layout中声明的宽度
		}
		if(height <= 0) {
			height = getImageViewFieldValue(imageView, "mMaxHeight");//检查最大值
		}
		
		if(height <= 0) {
			height = displayMetrics.heightPixels;
		}
		
		imageSize.width = width;
		imageSize.height = height;
		return imageSize;
	}
	/**
	 * 通过反射获取imageView的某个属性值
	 * @param object
	 * @param fieldName
	 * @return
	 */
	private static int getImageViewFieldValue(Object object, String fieldName) {
		int value = 0;
		try {
			Field field = ImageView.class.getDeclaredField(fieldName);
			field.setAccessible(true);
			
			int fieldValue = field.getInt(object);
			if(fieldValue > 0 && fieldValue < Integer.MAX_VALUE) {
				value = fieldValue;
			}
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return value;
	}
	
	public enum Type {
		FIFO, LIFO;
	}
	
	private ImageLoader(int threadCount, Type type) {
		init(threadCount, type);
	}
	/**
	 * 初始化
	 * @return
	 */
	private void init(int threadCount, Type type) {
		//后台轮询线程
		mPoolThread = new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				mPoolThreadHandler = new Handler() {
					@Override
					public void handleMessage(Message msg) {
						//线程池取出一个任务进行执行
						mThreadPool.execute(getTask());
						
						try {
							mSemaphoreThreadPool.acquire();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				};
				//释放信号量
				mSemaphorePoolTheadHandler.release();
				Looper.loop();
			}
		};
		
		mPoolThread.start();
		//获取我们应用的最大可用内存
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		int cacheMemory = maxMemory / 8;
		
		mLruCache = new LruCache<String, Bitmap>(cacheMemory) {
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getRowBytes() * value.getHeight();
			};
		};
		
		//创建线程池
		mThreadPool = Executors.newFixedThreadPool(threadCount);
		mTaskQueue = new LinkedList<Runnable>();
		mType = type;
		
		mSemaphoreThreadPool = new Semaphore(threadCount);
	}
	/**
	 * 从任务队列取出一个方法
	 * @return
	 */
	private Runnable getTask() {
		if(mType == Type.FIFO) {
			return mTaskQueue.removeFirst();
		} else if(mType == Type.LIFO) {
			return mTaskQueue.removeLast();
		}
		return null;
	}
	public static ImageLoader getInstance() {
		if(mInstance == null) {
			synchronized (ImageLoader.class) {
				if(mInstance == null) {
					mInstance = new ImageLoader(DEAFULT_THREAD_COUNT, mType);
				}
			}
		}
		return mInstance;
	}
	public static ImageLoader getInstance(int threadCount, Type type) {
		if(mInstance == null) {
			synchronized (ImageLoader.class) {
				if(mInstance == null) {
					mInstance = new ImageLoader(threadCount, type);
				}
			}
		}
		return mInstance;
	}
	/**
	 * 根据path为imageView设置图片
	 * @param path
	 * @param imageView
	 */
	public void loadImage(final String path, final ImageView imageView) {
		imageView.setTag(path);
		
		if(mUIHandler == null) {
			mUIHandler = new Handler() {
				public void handleMessage(Message msg) {
					//获取得到的图片，为imageView回调设置图片
					ImageBeanHolder holder = (ImageBeanHolder) msg.obj;
					Bitmap bm = holder.bitmap;
					ImageView imageView = holder.imageView;
					String path = holder.path;
					//将path与getTag存储路径进行比较
					if(imageView.getTag().toString().equals(path)) {
						imageView.setImageBitmap(bm);
					}
				};
			};
		}
		//根据path在缓存中获取bitmap
		Bitmap bm = getBitmapFromLruCache(path);
		
		if(bm != null) {
			refreshBitmap(path, imageView, bm);
		} else {
			
			addTasks(new Runnable() {
				
				@Override
				public void run() {
					//加载图片
					//图片的压缩
					//1.获得图片需要显示的大小
					ImageSize imageSize = getImageViewSize(imageView);
					//2.压缩图片
					Bitmap bitmap = decodeSampledBitmapFromPath(path, imageSize.width, imageSize.height);
					//3.把图片加入到缓存
					addBitmapToLruCache(path, bitmap);
					refreshBitmap(path, imageView, bitmap);
					
					mSemaphoreThreadPool.release();
				}
			});
		}
	}
	private void refreshBitmap(final String path, final ImageView imageView, Bitmap bm) {
		Message message = Message.obtain();
		ImageBeanHolder holder = new ImageBeanHolder();
		holder.bitmap = bm;
		holder.imageView = imageView;
		holder.path = path;
		message.obj = holder;
		mUIHandler.sendMessage(message);
	}
	protected void addBitmapToLruCache(String path, Bitmap bm) {
		if(getBitmapFromLruCache(path) == null) {
			if(bm != null) {
				mLruCache.put(path, bm);
			}
		}
		
	}
	protected Bitmap decodeSampledBitmapFromPath(String path, int width, int height) {
		//获得图片的宽和高，不加到内存
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		
		options.inSampleSize = caculateInSampleSize(options, width, height);
		
		//使用或得到的InSampleSize再次解析图片
		options.inJustDecodeBounds = false;
		Bitmap bitmap = BitmapFactory.decodeFile(path, options);
		return bitmap;
	}
	
	/**
	 * 根据需求的宽和高以及图片实际的宽和高计算SampleSize
	 * @param runnable
	 */
	private int caculateInSampleSize(Options options, int reqwidth, int reqheight) {
		int width = options.outWidth;
		int height = options.outHeight;
		
		int inSampleSize = 1;
		
		if(width > reqwidth || height > reqheight) {
			int widthRadio = Math.round(width * 1.0f / reqwidth);
			int heightRadio = Math.round(height * 1.0f / reqheight);
			
			inSampleSize = Math.max(widthRadio, heightRadio);
		}
		
		return inSampleSize;
	}
	
	private synchronized void addTasks(Runnable runnable) {
		mTaskQueue.add(runnable);
		//if(mPoolThreadHandler == null)wait
		try {
			if(mPoolThreadHandler == null)
				mSemaphorePoolTheadHandler.acquire();
		} catch (InterruptedException e) {
			
		}
		mPoolThreadHandler.sendEmptyMessage(0x110);
	}
	
	/**
	 * 根据path在缓存中获取bitmap
	 * @param key
	 * @return
	 */
	private Bitmap getBitmapFromLruCache(String key) {
		return mLruCache.get(key);
	}
	
	private class ImageBeanHolder {
		Bitmap bitmap;
		ImageView imageView;
		String path;
	}
}
