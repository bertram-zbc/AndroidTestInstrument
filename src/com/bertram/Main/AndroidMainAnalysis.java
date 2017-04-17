/**
 * 项目主分析方法
 */
package com.bertram.Main;

import java.util.ArrayList;
import java.util.Map;

import com.bertram.Constraints.Sequence;
import com.bertram.Widget.AndroidWidgetRecord;
import com.bertram.Widget.WidgetsOnClick;
import com.bertram.model.Widget;

import soot.Body;
import soot.BodyTransformer;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.util.cfgcmd.CFGGraphType;

public class AndroidMainAnalysis extends BodyTransformer {

	
	private CFGGraphType graphtype;
	
	@Override
	protected void internalTransform(Body arg0, String arg1,
			Map<String, String> arg2) {
		
		//初始化的一些设置
		initialize();
		
		//遍历apk中所有class
		for(SootClass c : Scene.v().getApplicationClasses()){
			if(c.toString().contains("RegisterActivity")&&!c.toString().contains("$")){
				
				//获取class中的所有控件信息
				ArrayList<Widget> widgetArray = AndroidWidgetRecord.recordWidget(c);
				//遍历class中的所有方法
				for(SootMethod sMethod : c.getMethods()){
					
					//找到onCreate方法，获取所有调用setOnClickListener的控件
					if(sMethod.toString().contains("onCreate")){
						Body b = sMethod.retrieveActiveBody();
						WidgetsOnClick.getWidget(graphtype, b, widgetArray);
					}
					
					//获取顺序约束
					if(sMethod.toString().contains("onClick")){
						Body b = sMethod.retrieveActiveBody();
						Sequence.getConstraints(graphtype, b, widgetArray);
					}
					
				}
				
			}
		}
		
	}

	private void initialize() {
		// 设置配置信息
		graphtype = CFGGraphType.getGraphType("BriefUnitGraph");
		
	}

}
