package com.bertram.test;

import java.io.File;

public class Test1 {

	/**
	 * ����������
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
		aboutFile();
		
		
	}

}
