/**
 * 生成CFG图，并将CFG图写入dot文件形式的实例
 * 处理对象是apk中的RegisterActivity
 */
package com.bertram.CFG;

import java.util.Map;

import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.PhaseOptions;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.options.Options;
import soot.toolkits.graph.DirectedGraph;
import soot.util.cfgcmd.CFGGraphType;
import soot.util.cfgcmd.CFGToDotGraph;
import soot.util.dot.DotGraph;

public class CFGAnalysis extends BodyTransformer {
	
	private CFGGraphType graphtype;
	private CFGToDotGraph drawer;

	@Override
	protected void internalTransform(Body body, String phase,
			Map<String, String> options) {
		//body是所有方法
		//phase是设置的标签,这里是"jtp.AndroidCFG"
		
		initialize(options);
		
		for(SootClass c : Scene.v().getApplicationClasses()){
			if(c.toString().contains("RegisterActivity")&&!c.toString().contains("$")){
				for(SootMethod sMethod : c.getMethods()){
					System.err.println(sMethod.toString());
					Body b = sMethod.retrieveActiveBody();
					print_CFG(graphtype, b);					
				}
			}
			
		}
		
	}

	private void initialize(Map<String, String> options) {
		if (drawer == null) {
			drawer = new CFGToDotGraph();
			drawer.setBriefLabels(PhaseOptions.getBoolean(options, "brief"));
			drawer.setOnePage(!PhaseOptions.getBoolean(options, "multipages"));
			drawer.setUnexceptionalControlFlowAttr("color", "black");
			drawer.setExceptionalControlFlowAttr("color", "red");
			drawer.setExceptionEdgeAttr("color", "lightgray");
			drawer.setShowExceptions(Options.v().show_exception_dests());
			
			graphtype = CFGGraphType.getGraphType("BriefUnitGraph");
		}
	}
	
	/**
	 * 生成CFG图并写入文件
	 * @param graphtype
	 * @param body
	 */
	protected void print_CFG( CFGGraphType graphtype, Body body) {
		DirectedGraph<Unit> graph = graphtype.buildGraph(body);
		//System.out.println(graph);
		DotGraph canvas = graphtype.drawGraph(drawer, graph, body);
		//GenerateFlow gen = new GenerateFlow((UnitGraph) graph);
		
		
		
		String methodname = body.getMethod().getSubSignature();
		String classname = body.getMethod().getDeclaringClass().getName().replaceAll("\\$", "\\.");
		//获取输出路径
		String filename = soot.SourceLocator.v().getOutputDir();
		if (filename.length() > 0) {
			filename = filename + java.io.File.separator;
		}
		
		//设置文件路径和文件名格式
		//filename = filename + classname + " " + methodname.replace(java.io.File.separatorChar, '.') + DotGraph.DOT_EXTENSION;
		filename = filename + classname + "_"+ methodname.replace(java.io.File.separatorChar, '.') + DotGraph.DOT_EXTENSION;
		G.v().out.println("Generate dot file in " + filename);
		G.v().out.println("classname:"+classname);
		canvas.plot(filename);
	}
	

}
