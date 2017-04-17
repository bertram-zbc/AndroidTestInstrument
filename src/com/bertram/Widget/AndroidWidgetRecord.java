/**
 * ��ȡ�ؼ������Ϣ��ʵ����һЩ����
 */
package com.bertram.Widget;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import com.bertram.model.AppClassFilePath;
import com.bertram.model.Widget;

import soot.Body;
import soot.BodyTransformer;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;

public class AndroidWidgetRecord extends BodyTransformer {
	
	@Override
	protected void internalTransform(Body arg0, String arg1,
			Map<String, String> arg2) {
		//һ��ʵ������������һЩ�����ĵ��õ�ʵ��
		
		for(SootClass c: Scene.v().getApplicationClasses()){
			//System.out.println(c);
			if(c.toString().contains("ConditionActivity")&&!c.toString().contains("$")){
				ArrayList<Widget> widgetArray = recordWidget(c);
				
				System.err.println("<<<<<<<<<<<<<<<<<<<");
				for(int i=0;i<widgetArray.size();i++){
					System.out.println(widgetArray.get(i).name+"\t"+widgetArray.get(i).resource_id);
				}
				System.err.println(">>>>>>>>>>>>>>>>>>>");
				
			}

		}
			
	}
	
	//ͨ�����ļ���ȡ�ؼ���resource-id��Ϣ
	private static void getWidgetResourceId(String filePath,
			ArrayList<Widget> widgetArray, String packageName) {
		File file = new File(filePath);
		BufferedReader reader = null;
		
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			while((tempString = reader.readLine())!=null){
				if(tempString.contains("findViewById")){
					//tempString.trim();
					int index = tempString.indexOf("findViewById");
					String subStr = tempString.substring(index+12, tempString.length());
					int start = subStr.indexOf("(");
					int end = subStr.indexOf(")");
					String widgetId = subStr.substring(start+1, end);
					
					//����resource-id, ��ʽ��com.test.baidumap:id/textlogin
					String resource_id = packageName+":id/"+widgetId.split("\\.")[2];
					//System.out.println(resource_id);
					
					//��resource-id�浽��Ӧ�ؼ���
					for(int i=0;i<widgetArray.size();i++){
						if(tempString.contains(widgetArray.get(i).name)){
							widgetArray.get(i).resource_id = resource_id;
						}
					}
				}
			}
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("warning:"+filePath+" not found!");
		} catch (IOException e) {
			System.err.println("read file error:"+filePath);
		}
		
	}

	//��¼һ�����г��ֵ����пؼ�
	public static ArrayList<Widget>  recordWidget(SootClass c) {
		
		ArrayList<Widget> widgetArray = new ArrayList<Widget>();
		
		//��ȡҪ��¼�Ŀؼ�����
		ArrayList<String> widgetType = Widget.getWidgetTypeArray();
		
		for(SootMethod m: c.getMethods()){
			//����ÿһ������
			Body b = m.retrieveActiveBody();
			
			for(Unit u : b.getUnits()){
				//����һ�������е�ÿһ�д���
				//System.out.println("[body]"+u.toString());
				
				for(int i=0; i<widgetType.size();i++){
					
					if(u.toString().contains(widgetType.get(i))){
						//���ڿؼ�����ȡ�ؼ���Ϣ������
						int index = u.toString().indexOf(widgetType.get(i));
						int x = index + widgetType.get(i).length();
						char widgetName[] = new char[100];
						if(u.toString().charAt(x)==' '){
							for(int j=x+1;u.toString().charAt(j)!=' '
									&&u.toString().charAt(j)!='>';j++){
								widgetName[j-x-1] = u.toString().charAt(j);
							}
							String name = String.valueOf(widgetName);
							Widget w = new Widget(widgetType.get(i).trim(), name.trim());
							//������������У����������
							if(!isExistInWidgetArray(widgetArray,w)){
								widgetArray.add(w);
							}
						}
						
					}
				}
			}
		}
		
		//��ȡ��Ӧ�ļ���·��
		String filePath = AppClassFilePath.getPath(c.toString());
		//��¼resource-id��Ϣ
		getWidgetResourceId(filePath,widgetArray,c.getPackageName());
		return widgetArray;
	}

	//��ӡ�ؼ���Ϣ
	public static void printWidget(ArrayList<Widget> widgetArray) {
		for(int i=0;i<widgetArray.size();i++){
			System.out.println(widgetArray.get(i).name);
		}	
	}

	//�жϿؼ��Ƿ��Ѿ�����¼��������
	private static boolean isExistInWidgetArray(ArrayList<Widget> widgetArray, Widget w) {
		
		for(int i=0;i<widgetArray.size();i++){
			if(widgetArray.get(i).name.equals(w.name)){
				return true;
			}
		}
		return false;
	}


}
