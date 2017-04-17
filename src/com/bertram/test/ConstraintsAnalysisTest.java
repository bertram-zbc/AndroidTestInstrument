package com.bertram.test;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import com.bertram.Constraints.Logic;
import com.bertram.Constraints.WriteToFile;
import com.bertram.Widget.AndroidWidgetRecord;
import com.bertram.model.LogicConstraints;
import com.bertram.model.Widget;
import com.bertram.z3.z3Run;

import soot.Body;
import soot.BodyTransformer;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.toolkits.graph.DirectedGraph;
import soot.util.cfgcmd.CFGGraphType;
import soot.util.cfgcmd.CFGToDotGraph;
import soot.util.dot.DotGraph;

public class ConstraintsAnalysisTest extends BodyTransformer {

	private CFGGraphType graphtype = CFGGraphType.getGraphType("BriefUnitGraph");

	@Override
	protected void internalTransform(Body arg0, String arg1,
			Map<String, String> arg2) {

		for(SootClass c : Scene.v().getApplicationClasses()){
			if(c.toString().contains("RegisterActivity")&&!c.toString().contains("$")){
				ArrayList<Widget> widgetArray = AndroidWidgetRecord.recordWidget(c);
				for(SootMethod sMethod : c.getMethods()){
					if(sMethod.toString().contains("onClick")){
						Body b = sMethod.retrieveActiveBody();
						ArrayList<LogicConstraints> lcons =  Logic.getConstraints(graphtype, b, widgetArray);
						
						//生成的约束写入文件
						String className = c.toString();
						String methodName = sMethod.toString();
						//处理methodName，去掉特殊字符，不然无法创建路径
						int index = methodName.indexOf(":");
						methodName = methodName.substring(index+1, methodName.length()-1);
						String filePath = "./Constraints/"+className+"/"+methodName+"/";
						WriteToFile.writeLogic(lcons,filePath);
						
						File file = new File(filePath);
						File[] fileList = file.listFiles();
						for(int i=0;i<fileList.length;i++){
							z3Run.startRun(fileList[i],widgetArray);
						}
						
						
						
						//print_CFG(b);//打印CFG图
					}
				}
			}
		}
		
	}

	private void print_CFG(Body body) {
		
		CFGGraphType graphtype = CFGGraphType.getGraphType("BriefUnitGraph");
		DirectedGraph<Unit> graph = graphtype.buildGraph(body);
		System.out.println(graph);
		
		//CFGToDotGraph drawer = new CFGToDotGraph();
		//DotGraph canvas = graphtype.drawGraph(drawer, graph, body);
		
		
		
//		Iterator<Unit> i = graph.iterator();
//		while(i.hasNext()){
//			Unit u = i.next(); 
//			System.out.println(u);
//			if(u.toString().equals("$i3 = i0 + i1")){
//				System.err.println(graph.getPredsOf(u));//打印前驱节点
//			}
//		}
	}

}
