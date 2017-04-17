/**
 * 将约束写入stm2文件的相关方法
 * 2017/4/16
 */
package com.bertram.Constraints;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import com.bertram.model.LogicConstraints;
import com.bertram.model.LogicType;
import com.bertram.model.Widget;

public class WriteToFile {

	/**
	 * 生成逻辑约束文件
	 * @param lcons 约束数组
	 * @param className 类名，用来确定文件路径
	 */
	public static void writeLogic(ArrayList<LogicConstraints> lcons,
			String filePath) {
		
		//判断文件夹是否存在，不存在则创建
		createFolder(filePath); 
		
		//计算所有测试用例的个数
		int bits = lcons.size();
		double times = Math.pow(2, bits);
		for(int i=0;i<times;i++){
			//得到一个实例
			char[] cases = getCase(bits,i);
			String fileName = filePath+i+".stm2";
			//System.out.println(fileName);
			//创建文件
			if(createFile(fileName)){
				//写入文件
				writeLogicToStm2File(fileName,cases,lcons);
			}
		}
		
	}

	/**
	 * 写stm2格式文件
	 * @param fileName
	 * @param cases
	 * @param lcons
	 */
	private static void writeLogicToStm2File(String fileName, char[] cases,
			ArrayList<LogicConstraints> lcons) {
		String information = ";"+fileName;
		try {
			FileWriter writer = new FileWriter(fileName, true);
			//写入基本信息
			writer.write(information);
			writer.write("\n");
			
			//写入定义
			ArrayList<Widget> existWidgets = getExistWidgets(lcons);
			for(int i=0;i<existWidgets.size();i++){
				String s = "(declare-const "+existWidgets.get(i).name+" String)";
				writer.write(s);
				writer.write("\n");
			}
			writer.write("(push)");
			writer.write("\n");
			
			//写入断言
			for(int i=0;i<lcons.size();i++){
				if(cases[i]=='0'){
					//相同的情况
					LogicConstraints lc = lcons.get(i);
					if(lc.type == LogicType.WIDGET_STRING){
						Widget w = (Widget) lc.param1;
						String s = (String) lc.param2;
						writer.write("(assert (= "+w.name+" "+s+"))");
						writer.write("\n");
					}
					if(lc.type == LogicType.STRING_WIDGET){
						String s = (String) lc.param1;
						Widget w = (Widget) lc.param2;
						writer.write("(assert (= "+w.name+" "+s+"))");
						writer.write("\n");
					}
					if(lc.type == LogicType.WIDGET_WIDGET){
						Widget w1 = (Widget) lc.param1;
						Widget w2 = (Widget) lc.param2;
						writer.write("(assert (= "+w1.name+" "+w2.name+"))");
						writer.write("\n");
					}
				}else{
					//不相同的情况
					LogicConstraints lc = lcons.get(i);
					if(lc.type == LogicType.WIDGET_STRING){
						Widget w = (Widget) lc.param1;
						String s = (String) lc.param2;
						writer.write("(assert (not (= "+w.name+" "+s+")))");
						writer.write("\n");
					}
					if(lc.type == LogicType.STRING_WIDGET){
						String s = (String) lc.param1;
						Widget w = (Widget) lc.param2;
						writer.write("(assert (not (= "+w.name+" "+s+")))");
						writer.write("\n");
					}
					if(lc.type == LogicType.WIDGET_WIDGET){
						Widget w1 = (Widget) lc.param1;
						Widget w2 = (Widget) lc.param2;
						writer.write("(assert (not (= "+w1.name+" "+w2.name+")))");
						writer.write("\n");
					}
				}
			}
			
			writer.write("(check-sat)\n");
			writer.write("(get-model)\n");
			writer.write("(pop)\n");
			writer.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}

	//获取lcons中的所有控件
	private static ArrayList<Widget> getExistWidgets(
			ArrayList<LogicConstraints> lcons) {
		
		ArrayList<Widget> widgets = new ArrayList<Widget>();
		
		for(int i=0;i<lcons.size();i++){
			LogicConstraints lc = lcons.get(i);
			if(lc.type == LogicType.WIDGET_STRING){
				Widget w = (Widget) lc.param1;
				if(!isContainWidget(w,widgets)){
					widgets.add(w);
				}
			}
			if(lc.type == LogicType.STRING_WIDGET){
				Widget w = (Widget) lc.param2;
				if(!isContainWidget(w,widgets)){
					widgets.add(w);
				}
			}
			if(lc.type == LogicType.WIDGET_WIDGET){
				Widget w1 = (Widget) lc.param1;
				if(!isContainWidget(w1,widgets)){
					widgets.add(w1);
				}
				Widget w2 = (Widget) lc.param2;
				if(!isContainWidget(w2,widgets)){
					widgets.add(w2);
				}
			}
		}
		return widgets;
	}

	private static boolean isContainWidget(Widget w, ArrayList<Widget> widgets) {
		for(int i=0;i<widgets.size();i++){
			if(w.name.equals(widgets.get(i).name)){
				return true;
			}
		}
		return false;
	}

	//创建文件操作
	private static boolean createFile(String fileName) {
		try {
			File file = new File(fileName);
			if(!file.exists()){
				file.createNewFile();
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	//计算case实例，用数组表示，1表示相同，0表示不同
	private static char[] getCase(int bits, int num) {
		char[] result = new char[bits];
		String s = Integer.toBinaryString(num);
		char[] c = s.toCharArray();
		for(int i=bits-1;i>=0;i--){
			if(i>bits-1-c.length){
				result[i]=c[c.length-bits+i];
			}else{
				result[i]='0';
			}
		}
		return result;
	}
	


	//创建文件夹
	private static void createFolder(String filePath) {
		File path = new File(filePath);
		if(!path.exists()){
			path.mkdirs();
		}		
	}

}
