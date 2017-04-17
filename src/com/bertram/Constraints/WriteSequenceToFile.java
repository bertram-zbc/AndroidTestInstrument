/*
 * 写入顺序约束到文件中
 * 不过发现目前暂时好像没必要再用求解器求解顺序约束
 * 2017/4/6
 */
package com.bertram.Constraints;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.bertram.model.Widget;

public class WriteSequenceToFile {

	/**
	 * @param args
	 */
	static ArrayList<ArrayList<Widget>> recordArr = new ArrayList<ArrayList<Widget>>();
	static String methodName = "test1";
	static String className = "class";
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		init();
		writeConstraints(recordArr, className, methodName);
	}


	//将约束写入文件
	public static void writeConstraints(
			ArrayList<ArrayList<Widget>> array, String cname,
			String mname) {
		// TODO Auto-generated method stub
		
		String filePath = "./TestWriteConstraints/"+cname+"/"+mname;
		createFolder(filePath);//判断文件夹是否存在，不存在则创建
		
		for(int i=0;i<array.size();i++){
			String fileName = filePath+"/"+i+".stm2";
			createFile(fileName);
			String begin = ";"+filePath;
			//appendContentToFile(fileName,begin+"\n");
			
			try {
				FileWriter writer = new FileWriter(fileName,true);
				writer.write(begin);
				ArrayList<Widget> widget = array.get(i);
				for(int j=0;j<widget.size();j++){
				
				}
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
		}
		
		
	}


	
	private static void appendContentToFile(String fileName, String content) {
		//添加内容到文件尾部
		try {
			FileWriter writer = new FileWriter(fileName,true);
			writer.write(content);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	private static void createFile(String fileName) {
		// 创 建文件操作
		try {
			File file = new File(fileName);
			if(!file.exists()){
				file.createNewFile();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private static void createFolder(String filePath) {
		// 创建文件夹方法
		File path = new File(filePath);
		if(!path.exists()){
			path.mkdirs();
		}
	}


	private static void init() {
		// TODO Auto-generated method stub
		ArrayList<Widget> widget = new ArrayList<Widget>();
		Widget w1 = new Widget("android.widget.EditText", "userName");
		Widget w2 = new Widget("android.widget.EditText", "password1");
		Widget w3 = new Widget("android.widget.EditText","password2");
		widget.add(w1);
		widget.add(w2);
		widget.add(w3);
		recordArr.add(widget);
	}
	

}
