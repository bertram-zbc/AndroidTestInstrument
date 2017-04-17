/**
 * 获取一个CFG中的逻辑约束条件
 * 2017/4/7
 * 
 * 主要是以if条件语句为入口
 * 目前分析的约束只有equal
 */
package com.bertram.Constraints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import soot.Body;
import soot.Unit;
import soot.toolkits.graph.DirectedGraph;
import soot.util.cfgcmd.CFGGraphType;

import com.bertram.model.LogicConstraints;
import com.bertram.model.LogicType;
import com.bertram.model.Widget;

public class Logic {
	
	static ArrayList<LogicConstraints> consArray = new ArrayList<LogicConstraints>();

	public static ArrayList<LogicConstraints> getConstraints(CFGGraphType graphtype, Body b,
			ArrayList<Widget> widgetArray) {
		DirectedGraph<Unit> graph = graphtype.buildGraph(b);
		
		//遍历图中所有节点
		Iterator<Unit> units = graph.iterator();
		while(units.hasNext()){
			Unit u = units.next();
			String uStr = u.toString();
			//判断是否是if语句
			if(uStr.startsWith("if")){
				//分析if语句并提取出判断的变量
				//System.err.println(uStr);
				Recall(u,graph,widgetArray);
			}
		}
//		for(int i=0;i<consArray.size();i++){
//			consArray.get(i).print();
//		}
		return consArray;
		
	}

	//根据当前if语句的unit，回溯graph找到判断条件
	private static void Recall(Unit u, DirectedGraph<Unit> graph, ArrayList<Widget> widgetArray) {
		//提取if中的临时变量
		String factor = u.toString().split(" ")[1];
		List<Unit> pres = graph.getPredsOf(u);
		for(int i=0;i<pres.size();i++){
			if(pres.get(i).toString().startsWith(factor)){
				//找到需要的unit
				//System.out.println(pres.get(i).toString());
				dealWithUnit(pres.get(i), graph, widgetArray);
			}else{
				Recall(pres.get(i), graph, widgetArray);
			}
		}
		
	}

	/**
	 * 处理判断条件的unit，提取出其中的逻辑关系
	 * 例如：$z0 = virtualinvoke $r5.<java.lang.String: boolean equals(java.lang.Object)>("")
	 * 需要提取的内容：
	 * @param widgetArray 
	 * @param:$r5
	 * @func:java.lang.String: boolean equals(java.lang.Object)
	 * @param:""
	 */
	private static void dealWithUnit(Unit unit, DirectedGraph<Unit> graph, ArrayList<Widget> widgetArray) {
		
		String u = unit.toString().trim();
		int index = u.indexOf("invoke");
		String key = u.substring(index+7,u.length());
		
		//获取第一个变量
		index = key.indexOf(".");
		String param1 = key.substring(0, index);
		//System.out.println(param1);
		//找到变量对应的控件
		Widget w1 = findWidget(param1,unit,graph,widgetArray);
		
		//获取函数
		int startIndex = key.indexOf("<");
		int endIndex = key.indexOf(">");
		String func = key.substring(startIndex+1, endIndex);
		//TODO 添加其他逻辑判断
		
		
		//获取第二个参数
		index = key.indexOf(">");
		String temp = key.substring(index+1);
		startIndex = temp.indexOf("(");
		endIndex = temp.indexOf(")");
		String param2 = temp.substring(startIndex+1, endIndex);
		//System.out.println(param2);
		if(param2.startsWith("$")){
			Widget w2 = findWidget(param2, unit, graph, widgetArray);
			if(w1!=null && w2!=null){
				LogicConstraints lc = new LogicConstraints(w1, w2);
				if(!isContainLogicConstraints(lc)){
					consArray.add(lc);
				}
			}
		}else{
			LogicConstraints lc = new LogicConstraints(w1, param2);
			if(!isContainLogicConstraints(lc)){
				consArray.add(lc);
			}
		}
		
	}

	//判断是否已经存在当前约束
	private static boolean isContainLogicConstraints(LogicConstraints lc) {
		for(int i=0;i<consArray.size();i++){
			if(lc.type == consArray.get(i).type){
				if(lc.type == LogicType.WIDGET_WIDGET){
					//两个控件之间的约束
					Widget w1 = (Widget) lc.param1;
					Widget w2 = (Widget) lc.param2;
					Widget w3 = (Widget) consArray.get(i).param1;
					Widget w4 = (Widget) consArray.get(i).param2;
					if(w1.name.equals(w3.name) && w2.name.equals(w4.name) ){
						return true;
					}
				}else if(lc.type == LogicType.WIDGET_STRING){
					Widget w1 = (Widget) lc.param1;
					String s1 = (String) lc.param2;
					Widget w2 = (Widget) consArray.get(i).param1;
					String s2 = (String) consArray.get(i).param2;
					if(w1.name.equals(w2.name) && s1.equals(s2)){
						return true;
					}
				}else if(lc.type == LogicType.STRING_WIDGET){
					String s1 = (String) lc.param1;
					Widget w1 = (Widget) lc.param2;
					String s2 = (String) consArray.get(i).param1;
					Widget w2 = (Widget) consArray.get(i).param2;
					if(s1.equals(s2) && w1.name.equals(w2.name)){
						return true;
					}
				}
			}
		}
		return false;
	}

	//回溯查找出对应的控件
	private static Widget findWidget(String param, Unit unit, DirectedGraph<Unit> graph,
			ArrayList<Widget> widgetArray) {
		
		List<Unit> pres = graph.getPredsOf(unit);
		for(int i=0;i<pres.size();i++){
			String u = pres.get(i).toString().trim();
			if(u.startsWith(param)){
				Widget w = isContainWidget(pres.get(i),widgetArray);
				if(w!=null){
					//找到对应控件
					//System.err.println(pres.get(i).toString()+"\t"+w.name+"\t"+w.resource_id);
					return w;
				}else{
					//没找到，继续回溯
					int index = u.indexOf("invoke");
					String key = u.substring(index+7,u.length());
					index = key.indexOf(".");
					String param1 = key.substring(0, index);
					return findWidget(param1, pres.get(i), graph, widgetArray);
				}
			}else{
				return findWidget(param, pres.get(i), graph, widgetArray);
			}
		}
		return null;
	}

	//判断一个unit中是否包含控件
	private static Widget isContainWidget(Unit unit,
			ArrayList<Widget> widgetArray) {
		
		String u = unit.toString();
		for(int i=0;i<widgetArray.size();i++){
			if(u.contains(widgetArray.get(i).name)){
				return widgetArray.get(i);
			}
		}
		
		return null;
	}

	

}
