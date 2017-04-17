package com.bertram.model;

public class LogicConstraints {
	
	public int type = -1;
	public Object param1 = new Object();
	public Object param2 = new Object();
	
	public LogicConstraints(Widget w1, Widget w2) {
		type = LogicType.WIDGET_WIDGET;
		param1 = w1;
		param2 = w2;
	}
	
	public LogicConstraints(Widget w, String s) {
		type = LogicType.WIDGET_STRING;
		param1 = w;
		param2 = s;
	}
	
	public LogicConstraints(String s, Widget w) {
		type = LogicType.STRING_WIDGET;
		param1 = s;
		param2 = w;
	}
	
	public void print() {
		System.out.println(param1+"\t"+param2);

	}

}

