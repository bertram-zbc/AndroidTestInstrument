/**
 * 获取onCreate中调用setOnClickListener的所有控件
 * 
 */
package com.bertram.Widget;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.bertram.model.Widget;

import soot.Body;
import soot.Unit;
import soot.toolkits.graph.DirectedGraph;
import soot.util.cfgcmd.CFGGraphType;

public class WidgetsOnClick {

	static ArrayList<Widget> widgetOnClick = new ArrayList<Widget>();
	
	public static ArrayList<Widget> getWidget(CFGGraphType graphtype, Body b,
			ArrayList<Widget> widgetArray) {
		DirectedGraph<Unit> graph = graphtype.buildGraph(b);
		
		Iterator<Unit> i = graph.iterator();
		while(i.hasNext()){
			Unit u = i.next(); 
			if(u.toString().contains("setOnClickListener")){
				Recall(graph,u,widgetArray);
			}
		}
		//System.out.println(widgetOnClick);
		return widgetOnClick;
		
	}

	//回溯找到控件
	private static void Recall(DirectedGraph<Unit> graph, Unit u,
			ArrayList<Widget> widgetArray) {
		
		List<Unit> pres = graph.getPredsOf(u);
		for(int i=0;i<pres.size();i++){
			Widget w = FindWidget(pres.get(i),widgetArray);
			if( w != null ){
				if(!isExistWidget(w,widgetOnClick) ){
					System.err.println(FindWidget(pres.get(i),widgetArray).name);
					widgetOnClick.add(w);
				}	
			}else{
				Recall(graph, pres.get(i), widgetArray);
			}
		}
		
	}

	//判断控件是否已经在array中存在
	private static boolean isExistWidget(Widget w,
			ArrayList<Widget> array) {
		for(int i=0;i<array.size();i++){
			if(array.get(i).name.equals(w.name)){
				return true;
			}
		}
		return false;
	}

	//查找当前句中是否存在控件
	private static Widget FindWidget(Unit u, ArrayList<Widget> widgetArray) {
		for(int i=0;i<widgetArray.size();i++){
			if(u.toString().contains(widgetArray.get(i).name.toString())){
				return widgetArray.get(i);
			}
		}
		return null;
	}

	

}
