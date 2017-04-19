package com.bertram.UIAutomator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.bertram.model.Widget;

public class AutomatorTestCase {
	
	static String projectPath = "e:/android analysis/UIAutomator/src/com/bertram/test/";
	static String packageName = "com.bertram.test";

	public static void createFile(String componentName,
			ArrayList<Widget> finalArray, Widget clickWidget) {
		
		String className = getClassName(componentName);
		String fileName = projectPath + className + ".java";
		createFile(fileName);
		//写文件
		try {
			FileWriter fileWriter = new FileWriter(fileName, false);
			fileWriter.write("package "+packageName+";\n");
			fileWriter.write("import com.android.uiautomator.core.UiObject;\n");
			fileWriter.write("import com.android.uiautomator.core.UiObjectNotFoundException;\n");
			fileWriter.write("import com.android.uiautomator.core.UiSelector;\n");
			fileWriter.write("import com.android.uiautomator.testrunner.UiAutomatorTestCase;\n");
			fileWriter.write("public class "+className+" extends UiAutomatorTestCase{\n");
			
			String setup = "@Override\n"+
			"protected void setUp() throws Exception {\n"+
			"super.setUp();\n"+
			"StringBuffer sBuffer = new StringBuffer();\n"+
			"sBuffer.append(\"am start -n \");\n"+
			"sBuffer.append(\""+componentName+"\");\n"+
			"try {\n"+
			"Process process = Runtime.getRuntime().exec(sBuffer.toString());\n"+
			"process.waitFor();\n"+
			"} catch (Exception e) {\n"+
			"e.printStackTrace();\n"+
			"}\n"+
			"}\n";
			fileWriter.write(setup);
			
			fileWriter.write("public void testStart() throws UiObjectNotFoundException{\n");
			for(int i=0;i<finalArray.size();i++){
				String s1 = "UiObject u"+i+" = new UiObject(new UiSelector().resourceId(\""+finalArray.get(i).resource_id+"\"));\n";
				fileWriter.write(s1);
				String s2 = "u"+i+".setText("+finalArray.get(i).inputValue+");\n";
				fileWriter.write(s2);
			}
			
			fileWriter.write("UiObject button = new UiObject(new UiSelector().resourceId(\""+clickWidget.resource_id+"\"));\n");
			fileWriter.write("button.clickAndWaitForNewWindow();");
			fileWriter.write("}\n");
			fileWriter.write("}\n");
			
			fileWriter.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	//创建文件
	private static void createFile(String fileName) {
		try {
			File file = new File(fileName);
			if(!file.exists()){
				file.createNewFile();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//生成文件名
	private static String getClassName(String componentName) {
		String[] s = componentName.split("/.");
		return s[s.length-1];
	}
}
