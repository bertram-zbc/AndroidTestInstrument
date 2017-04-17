package com.bertram.CallGraph;

import java.util.Iterator;
import java.util.Map;

import soot.Body;
import soot.BodyTransformer;
import soot.MethodOrMethodContext;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.jimple.toolkits.callgraph.CHATransformer;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Targets;

public class CallGraphAnalysis extends BodyTransformer {

	@Override
	protected void internalTransform(Body arg0, String arg1,
			Map<String, String> arg2) {
		// TODO Auto-generated method stub
		
		CHATransformer.v().transform();
		
		CallGraph cg = Scene.v().getCallGraph();
		SootClass mainClass = Scene.v().getSootClass("com.test.baidumap.RegisterActivity");
		System.out.println(mainClass.getMethods());
//		SootMethod sm = mainClass.getMethodByName("onClick");
//		Iterator targets = new Targets(cg.edgesOutOf(sm));
//		while(targets.hasNext()){
//            SootMethod tgt = (SootMethod)targets.next();
//            System.out.println(sm+" might call "+tgt);
//        }
	}

}
