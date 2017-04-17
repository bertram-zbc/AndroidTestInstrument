package com.bertram.z3;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.bertram.model.Widget;

public class z3Run {

	public static void startRun(File file, ArrayList<Widget> widgetArray) {
		
		System.err.println("start run "+file.getAbsolutePath());
		
		File z3File = new File("z3-4.5.0-x86-win\\bin\\z3.exe");
		String cmd = "\""+z3File.getAbsolutePath()+"\" -smt2 \""+file.getAbsolutePath()+"\"";
		System.out.println(cmd);
		Runtime run = Runtime.getRuntime();
		try {
			//启动执行
			Process p = run.exec(cmd);
			//获取控制台信息
			BufferedInputStream in = new BufferedInputStream(p.getInputStream());
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String lineStr = reader.readLine();
			if(lineStr.equals("sat")){
				//有解
				while( (lineStr = reader.readLine()) != null ){
					//System.out.println(lineStr);
					for(int i=0;i<widgetArray.size();i++){
						if(lineStr.contains(widgetArray.get(i).name)){
							String value = reader.readLine().trim();
							value = value.substring(0, value.length()-1);
							widgetArray.get(i).inputValue = value;
						}
					}
				}
			}
			
			for(int i=0;i<widgetArray.size();i++){
				widgetArray.get(i).print();
			}
			
		} catch (IOException e) {
			System.out.println("warning: z3 run "+file.getAbsolutePath()+" error!");
			e.printStackTrace();
		}
		
	}

}
