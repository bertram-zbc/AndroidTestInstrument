package com.bertram.z3;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.bertram.model.Widget;

public class z3Run {

	public static ArrayList<Widget> startRun(File file, ArrayList<Widget> widgetArray) {
		
		System.out.println("start run "+file.getAbsolutePath());
		
		
		File z3File = new File("z3-4.5.0-x86-win\\bin\\z3.exe");
		String cmd = "\""+z3File.getAbsolutePath()+"\" -smt2 \""+file.getAbsolutePath()+"\"";
		System.out.println(cmd);
		Runtime run = Runtime.getRuntime();
		try {
			//����ִ��
			Process p = run.exec(cmd);
			//��ȡ����̨��Ϣ
			BufferedInputStream in = new BufferedInputStream(p.getInputStream());
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String lineStr = reader.readLine();
			if(lineStr.equals("sat")){
				//�н�
				ArrayList<Widget> result = new ArrayList<Widget>();
				while( (lineStr = reader.readLine()) != null ){
					//System.out.println(lineStr);
					for(int i=0;i<widgetArray.size();i++){
						if(lineStr.contains(widgetArray.get(i).name)){
							String value = reader.readLine().trim();
							value = value.substring(0, value.length()-1);
							//widgetArray.get(i).inputValue = value;
							Widget w = new Widget(widgetArray.get(i));
							w.inputValue = value;
							result.add(w);
						}
					}
				}
				return result;
			}
			
//			for(int i=0;i<widgetArray.size();i++){
//				widgetArray.get(i).print();
//			}
		} catch (IOException e) {
			System.out.println("warning: z3 run "+file.getAbsolutePath()+" error!");
			e.printStackTrace();
		}
		return null;
		
	}

}
