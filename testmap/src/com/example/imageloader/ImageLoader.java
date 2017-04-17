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
 * ͼƬ������
 * @author Hzq
 *
 */
public class ImageLoader {
	private static ImageLoader mInstance;
	
	/**
	 * ͼƬ����ĺ��Ķ���
	 */
	private LruCache<String, Bitmap> mLruCache;
	/**
	 * �̳߳�
	 */
	private ExecutorService mThreadPool;
	private static final int DEAFULT_THREAD_COUNT = 1;
	/**
	 * ���еĵ��ȷ�ʽ
	 */
	private static Type mType = Type.LIFO;
	/**
	 * �������
	 */
	private LinkedList<Runnable> mTaskQueue;
	/**
	 * ��̨��ѯ�߳�
	 */
	private Thread mPoolThread;
	private Handler mPoolThreadHandler;
	
	/**
	 * UI�߳��е�Handler
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
			width = lp.width;//��ȡimageView��layout�������Ŀ��
		}
		if(width <= 0) {
			width = getImageViewFieldValue(imageView, "mMaxWidth");//������ֵ
		}
		
		if(width <= 0) {
			width = displayMetrics.widthPixels;
		}
		
		int height = imageView.getHeight();
		//int width = (lp.width == LayoutParams.WRAP_CONTENT ? 0 : imageView.getWidth());
		
		if(height <= 0) {
			height = lp.width;//��ȡimageView��layout�������Ŀ��
		}
		if(height <= 0) {
			height = getImageViewFieldValue(imageView, "mMaxHeight");//������ֵ
		}
		
		if(height <= 0) {
			height = displayMetrics.heightPixels;
		}
		
		imageSize.width = width;
		imageSize.height = height;
		return imageSize;
	}
	/**
	 * ͨ�������ȡimageView��ĳ������ֵ
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
	 * ��ʼ��
	 * @return
	 */
	private void init(int threadCount, Type type) {
		//��̨��ѯ�߳�
		mPoolThread = new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				mPoolThreadHandler = new Handler() {
					@Override
					public void handleMessage(Message msg) {
						//�̳߳�ȡ��һ���������ִ��
						mThreadPool.execute(getTask());
						
						try {
							mSemaphoreThreadPool.acquire();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				};
				//�ͷ��ź���
				mSemaphorePoolTheadHandler.release();
				Looper.loop();
			}
		};
		
		mPoolThread.start();
		//��ȡ����Ӧ�õ��������ڴ�
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		int cacheMemory = maxMemory / 8;
		
		mLruCache = new LruCache<String, Bitmap>(cacheMemory) {
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getRowBytes() * value.getHeight();
			};
		};
		
		//�����̳߳�
		mThreadPool = Executors.newFixedThreadPool(threadCount);
		mTaskQueue = new LinkedList<Runnable>();
		mType = type;
		
		mSemaphoreThreadPool = new Semaphore(threadCount);
	}
	/**
	 * ���������ȡ��һ������
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
	 * ����pathΪimageView����ͼƬ
	 * @param path
	 * @param imageView
	 */
	public void loadImage(final String path, final ImageView imageView) {
		imageView.setTag(path);
		
		if(mUIHandler == null) {
			mUIHandler = new Handler() {
				public void handleMessage(Message msg) {
					//��ȡ�õ���ͼƬ��ΪimageView�ص�����ͼƬ
					ImageBeanHolder holder = (ImageBeanHolder) msg.obj;
					Bitmap bm = holder.bitmap;
					ImageView imageView = holder.imageView;
					String path = holder.path;
					//��path��getTag�洢·�����бȽ�
					if(imageView.getTag().toString().equals(path)) {
						imageView.setImageBitmap(bm);
					}
				};
			};
		}
		//����path�ڻ����л�ȡbitmap
		Bitmap bm = getBitmapFromLruCache(path);
		
		if(bm != null) {
			refreshBitmap(path, imageView, bm);
		} else {
			
			addTasks(new Runnable() {
				
				@Override
				public void run() {
					//����ͼƬ
					//ͼƬ��ѹ��
					//1.���ͼƬ��Ҫ��ʾ�Ĵ�С
					ImageSize imageSize = getImageViewSize(imageView);
					//2.ѹ��ͼƬ
					Bitmap bitmap = decodeSampledBitmapFromPath(path, imageSize.width, imageSize.height);
					//3.��ͼƬ���뵽����
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
		//���ͼƬ�Ŀ�͸ߣ����ӵ��ڴ�
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		
		options.inSampleSize = caculateInSampleSize(options, width, height);
		
		//ʹ�û�õ���InSampleSize�ٴν���ͼƬ
		options.inJustDecodeBounds = false;
		Bitmap bitmap = BitmapFactory.decodeFile(path, options);
		return bitmap;
	}
	
	/**
	 * ��������Ŀ�͸��Լ�ͼƬʵ�ʵĿ�͸߼���SampleSize
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
	 * ����path�ڻ����л�ȡbitmap
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
