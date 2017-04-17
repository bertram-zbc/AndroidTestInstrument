/**
 * ����SootClass�ҵ�ԭ�����ж�Ӧclass���ļ�·��
 * 2017.4.11
 */
package com.bertram.model;

public class AppClassFilePath {

	static String appFile = "testmap/src/";
	
	public static String getPath(String sootClass) {
		//��"."���ָ����Ļ���Ҫ"\\."���У���Ϊ"."��ת���
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
