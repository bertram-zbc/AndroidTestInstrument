/**
 * 根据SootClass找到原代码中对应class的文件路径
 * 2017.4.11
 */
package com.bertram.model;

public class AppClassFilePath {

	static String appFile = "testmap/src/";
	
	public static String getPath(String sootClass) {
		//用"."做分隔符的话需要"\\."才行，因为"."是转义符
		String[] tempStrings = sootClass.split("\\.");
		//System.out.println(tempStrings.length);
		String path = new String(appFile);
		for(int i=0;i<tempStrings.length;i++){
			if(i!=tempStrings.length-1){
				path = path + tempStrings[i] + "/";
			}else{
				path = path + tempStrings[i] + ".java";
			}
		}
		//System.out.println(path);
		return path;
	}
	
	
	public static void main(String[] args) {
		String sootClass = "com.test.baidumap.RegisterActivity";
		getPath(sootClass);
	}

}
