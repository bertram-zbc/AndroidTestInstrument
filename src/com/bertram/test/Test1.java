package com.bertram.test;

import java.io.File;
import java.util.ArrayList;

class Name{
	String name;
	int id;
	public Name(String n , int i) {
		name = n;
		id  = i;
	}
}

public class Test1 {

	/**
	 * 二进制运算
	 */
	public static void binCal(){
		int bits = 5;
		char[] re = new char[bits];
		
		System.out.println(Math.pow(2, 5));
		for(int i=0;i<32;i++){
			String s = Integer.toBinaryString(i);
			char[] c = s.toCharArray();
			for(int j=bits-1;j>=0;j--){
				if(j>bits-1-c.length){
					re[j]=c[c.length-bits+j];
				}else{
					re[j]='0';
				}
			}
			System.out.println(re);
		}
	}
	
	public static void aboutFile(){
		File file = new File("./Constraints/com.test.baidumap.RegisterActivity/ void onClick(android.view.View)/");
		File[] fileList = file.listFiles();
		for(int i=0;i<fileList.length;i++){
			System.out.println(fileList[i]);
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String s1 = "com.test.baidumap.RegisterActivity";
		String s2 = "com.test.baidumap";
		String []s = s1.split(s2);
		System.out.println(s.length);
		System.out.println(s[0]+"\t"+s[1]);
		System.out.println(s2+"/"+s[1]);
		
		
		
	}

}
