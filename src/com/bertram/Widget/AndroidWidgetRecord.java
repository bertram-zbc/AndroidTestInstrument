/**
 * 获取控件相关信息的实例和一些方法
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
		//一个实例，关于下面一些方法的调用的实例
		
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
	
	//通过读文件获取控件的resource-id信息
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
					
					//生成resource-id, 格式如com.test.baidumap:id/textlogin
					String resource_id = packageName+":id/"+widgetId.split("\\.")[2];
					//System.out.println(resource_id);
					
					//将resource-id存到对应控件中
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

	//记录一个类中出现的所有控件
	public static ArrayList<Widget>  recordWidget(SootClass c) {
		
		ArrayList<Widget> widgetArray = new ArrayList<Widget>();
		
		//获取要记录的控件类型
		ArrayList<String> widgetType = Widget.getWidgetTypeArray();
		
		for(SootMethod m: c.getMethods()){
			//查找每一个方法
			Body b = m.retrieveActiveBody();
			
			for(Unit u : b.getUnits()){
				//遍历一个方法中的每一行代码
				//System.out.println("[body]"+u.toString());
				
				for(int i=0; i<widgetType.size();i++){
					
					if(u.toString().contains(widgetType.get(i))){
						//存在控件，提取控件信息并储存
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
							//如果不在数组中，则加入数组
							if(!isExistInWidgetArray(widgetArray,w)){
								widgetArray.add(w);
							}
						}
						
					}
				}
			}
		}
		
		//获取对应文件的路径
		String filePath = AppClassFilePath.getPath(c.toString());
		//记录resource-id信息
		getWidgetResourceId(filePath,widgetArray,c.getPackageName());
		return widgetArray;
	}

	//打印控件信息
	public static void printWidget(ArrayList<Widget> widgetArray) {
		for(int i=0;i<widgetArray.size();i++){
			System.out.println(widgetArray.get(i).name);
		}	
	}

	//判断控件是否已经被记录在数组中
	private static boolean isExistInWidgetArray(ArrayList<Widget> widgetArray, Widget w) {
		
		for(int i=0;i<widgetArray.size();i++){
			if(widgetArray.get(i).name.equals(w.name)){
				return true;
			}
		}
		return false;
	}


}
