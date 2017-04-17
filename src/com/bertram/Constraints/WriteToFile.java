/**
 * ��Լ��д��stm2�ļ�����ط���
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
	 * �����߼�Լ���ļ�
	 * @param lcons Լ������
	 * @param className ����������ȷ���ļ�·��
	 */
	public static void writeLogic(ArrayList<LogicConstraints> lcons,
			String filePath) {
		
		//�ж��ļ����Ƿ���ڣ��������򴴽�
		createFolder(filePath); 
		
		//�������в��������ĸ���
		int bits = lcons.size();
		double times = Math.pow(2, bits);
		for(int i=0;i<times;i++){
			//�õ�һ��ʵ��
			char[] cases = getCase(bits,i);
			String fileName = filePath+i+".stm2";
			//System.out.println(fileName);
			//�����ļ�
			if(createFile(fileName)){
				//д���ļ�
				writeLogicToStm2File(fileName,cases,lcons);
			}
		}
		
	}

	/**
	 * дstm2��ʽ�ļ�
	 * @param fileName
	 * @param cases
	 * @param lcons
	 */
	private static void writeLogicToStm2File(String fileName, char[] cases,
			ArrayList<LogicConstraints> lcons) {
		String information = ";"+fileName;
		try {
			FileWriter writer = new FileWriter(fileName, true);
			//д�������Ϣ
			writer.write(information);
			writer.write("\n");
			
			//д�붨��
			ArrayList<Widget> existWidgets = getExistWidgets(lcons);
			for(int i=0;i<existWidgets.size();i++){
				String s = "(declare-const "+existWidgets.get(i).name+" String)";
				writer.write(s);
				writer.write("\n");
			}
			writer.write("(push)");
			writer.write("\n");
			
			//д�����
			for(int i=0;i<lcons.size();i++){
				if(cases[i]=='0'){
					//��ͬ�����
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
					//����ͬ�����
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

	//��ȡlcons�е����пؼ�
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

	//�����ļ�����
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

	//����caseʵ�����������ʾ��1��ʾ��ͬ��0��ʾ��ͬ
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
	


	//�����ļ���
	private static void createFolder(String filePath) {
		File path = new File(filePath);
		if(!path.exists()){
			path.mkdirs();
		}		
	}

}
