package com.bertram.model;

import java.util.ArrayList;

//�ؼ���Ķ��壬���ڼ�¼�ؼ���һЩ��Ϣ
public class Widget {
	public String type; //�ؼ�����
	public String name; //�ؼ��ڴ����еı�����
	public String resource_id; //�ؼ���resource-id
	public String inputValue; //�ؼ�����ֵ
	public Widget(String type, String name){
		this.type = type;
		this.name = name;
		this.resource_id = "";
		this.inputValue = "";
	}
	
	public Widget(String type, String name, String id) {
		// TODO Auto-generated constructor stub
		this.resource_id = id;
		this.type = type;
		this.name = name;
		this.inputValue = "";
	}
	
	public void print(){
		System.err.println(this.name + "\t" + this.inputValue);
	}
	
	
	
	//Ҫ���Ե����пؼ�����
	static public ArrayList<String> getWidgetTypeArray(){
		ArrayList<String> widgetType = new ArrayList<String>();
		widgetType.add("android.widget.TextView");
		widgetType.add("android.widget.EditText");
		widgetType.add("android.widget.Button");
		widgetType.add("android.widget.ImageButton");
		widgetType.add("android.widget.RadioButton");
		widgetType.add("android.widget.RadioGroup");
		widgetType.add("android.widget.CheckButton");
		widgetType.add("android.widget.ImageView");
		widgetType.add("android.widget.ProgressBar");
		return widgetType;
	}
	
}
