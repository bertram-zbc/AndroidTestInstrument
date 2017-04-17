package com.example.sendpicture;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.testmap.logic.ClientThread;
import com.testmap.logic.ImageComputeSampleSize;


public class PictureSender extends Thread{
	private ArrayList<String> selectedImgPath = null;
	private String uname;
	private String psd;
	private String publishTime;
	public PictureSender(ArrayList<String> mSelectedImg, String uname, String psd, String publishTime) {
		selectedImgPath = mSelectedImg;
		this.uname = uname;
		this.psd = psd;
		this.publishTime = publishTime;
	}
	@Override
	public void run() {
		try {
			for(String filePath : selectedImgPath) {
				System.out.println("filePath:" + filePath);
				Socket socket = new Socket(ClientThread.getIpAddr(),8192);
				System.out.println("���ӷ������ɹ�");
		        File file = new File(filePath);
		        
		        BitmapFactory.Options option = new BitmapFactory.Options();
				option.inJustDecodeBounds = true;
	            BitmapFactory.decodeFile(filePath, option);
	            option.inSampleSize = ImageComputeSampleSize.computeSampleSize(option, -1, 512*1024);
	            option.inJustDecodeBounds = false;
	            Bitmap bm = BitmapFactory.decodeFile(filePath, option);
	            ByteArrayOutputStream baos = new ByteArrayOutputStream();
	            bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
	            InputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		        
		        DataInputStream fis = new DataInputStream(new BufferedInputStream(isBm));
		        
		        DataOutputStream ps = new DataOutputStream(socket.getOutputStream());
		        //���ļ��������ȴ����ͻ��ˡ�����Ҫ������������ƽ̨�������������Ĵ�������Ҫ�ӹ���������Բμ�Think In Java 4th�����ֳɵĴ��롣
		        ps.writeUTF(uname + "&&" + psd + "&&" + publishTime + "&&" + file.getName());
		        ps.flush();
		        ps.writeLong((long) file.length());
		        ps.flush();
		        
		        int bufferSize = 8192;
		        byte[] buf = new byte[bufferSize];
		        while(true) {
		       	 int read = 0;
		            if (fis != null) {
		                read = fis.read(buf);
		            }

		            if (read == -1) {
		                break;
		            }
		            ps.write(buf, 0, read);
		        }
		        ps.flush();
		        // ע��ر�socket����Ŷ����Ȼ�ͻ��˻�ȴ�server�����ݹ�����
		        // ֱ��socket��ʱ���������ݲ�������         
		        fis.close();
		        socket.close();                
		        System.out.println("�ļ��������");
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		
	}
	
}