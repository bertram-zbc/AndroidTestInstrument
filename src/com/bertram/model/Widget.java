package com.bertram.model;

import java.util.ArrayList;

//控件类的定义，用于记录控件的一些信息
public class Widget {
	public String type; //控件类型
	public String name; //控件在代码中的变量名
	public String resource_id; //控件的resource-id
	public String inputValue; //控件输入值
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
	
	
	
	//要测试的所有控件类型
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
