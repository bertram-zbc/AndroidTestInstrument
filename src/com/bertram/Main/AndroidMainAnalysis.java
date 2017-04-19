/**
 * 项目主分析方法
 */
package com.bertram.Main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import com.bertram.Constraints.Logic;
import com.bertram.Constraints.Sequence;
import com.bertram.Constraints.WriteToFile;
import com.bertram.UIAutomator.AutomatorTestCase;
import com.bertram.Widget.AndroidWidgetRecord;
import com.bertram.Widget.WidgetsOnClick;
import com.bertram.model.LogicConstraints;
import com.bertram.model.Widget;
import com.bertram.z3.z3Run;

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
			if(c.toString().contains("ConditionActivity")&&!c.toString().contains("$")){
				
				//获取class中的所有控件信息
				ArrayList<Widget> widgetArray = AndroidWidgetRecord.recordWidget(c);
				//获取调用onClick方法的控件
				ArrayList<Widget> widgetOnClick = null;
				
				//先遍历class中的方法找到oncreate对widgetOnClick赋值，因为后面的操作得确保widgetOnClick已经先得到值
				for(SootMethod sMethod:c.getMethods()){
					//找到onCreate方法，获取所有调用setOnClickListener的控件
//					System.out.println(c.toString());
//					System.out.println(c.getPackageName());
//					System.out.println(c.getName());
					if(sMethod.toString().contains("onCreate")){
						Body b = sMethod.retrieveActiveBody();
						widgetOnClick =  WidgetsOnClick.getWidget(graphtype, b, widgetArray);
					}
				}
				
				//再遍历class中的所有方法
				for(SootMethod sMethod : c.getMethods()){
					
					//获取约束并生成测试用例
					if(sMethod.toString().contains("onClick")){
						Body b = sMethod.retrieveActiveBody();
						
						//获取顺序约束
						ArrayList<ArrayList<Widget>> scons = Sequence.getConstraints(graphtype, b, widgetArray);
						
						//获取逻辑约束
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
							ArrayList<Widget> z3ResultArray = z3Run.startRun(fileList[i],widgetArray);
							if(z3ResultArray!=null){
								
								for(int j=0;j<scons.size();j++){
									ArrayList<Widget> finalArray = scons.get(j);
									dealWith(finalArray,z3ResultArray);
									for(int k=0;k<widgetOnClick.size();k++){
										Widget clickWidget = widgetOnClick.get(k);
										
										//输出测试
										System.err.println("======================================");
										for(int l=0;l<finalArray.size();l++){
											System.out.print(finalArray.get(l).name+":"+finalArray.get(l).inputValue+"\t");
										}
										System.out.println(clickWidget.name);
										System.err.println("======================================");
										
										//TODO UIAutomator脚本生成
										String componentName = c.getPackageName()+"/"+c.toString().split(c.getPackageName())[1];
										AutomatorTestCase.createFile(componentName,finalArray,clickWidget);
										runPython();//运行测试脚本
										
									}
								}
								
							}
							
							
							
						}
					}
					
				}
				
			}
		}
		
	}

	//运行Python脚本
	private void runPython() {
		System.out.println("start running command.py...");
		String cmd = "python command.py";
		try {
			Process process = Runtime.getRuntime().exec(cmd);
			process.waitFor();
			//Thread.currentThread().sleep(10000);
			System.out.println("finish running command.py");
		} catch (Exception e) {
			System.err.println("运行command.py错误!!");
		} 
		
	}

	/**
	 * 将控件的输入值赋值到顺序约束的数组中
	 * @param finalArray 记录的是顺序约束
	 * @param widgetArray
	 */
	private void dealWith(ArrayList<Widget> finalArray,
			ArrayList<Widget> widgetArray) {
		for(int i=0;i<finalArray.size();i++){
			for(int j=0;j<widgetArray.size();j++){
				if(finalArray.get(i).name.equals(widgetArray.get(j).name)){
					finalArray.get(i).inputValue = widgetArray.get(j).inputValue;
				}
			}
		}	
	}

	private void initialize() {
		// 设置配置信息
		graphtype = CFGGraphType.getGraphType("BriefUnitGraph");
		
	}

}
