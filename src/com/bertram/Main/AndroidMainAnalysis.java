/**
 * ��Ŀ����������
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
		
		//��ʼ����һЩ����
		initialize();
		
		//����apk������class
		for(SootClass c : Scene.v().getApplicationClasses()){
			if(c.toString().contains("ConditionActivity")&&!c.toString().contains("$")){
				
				//��ȡclass�е����пؼ���Ϣ
				ArrayList<Widget> widgetArray = AndroidWidgetRecord.recordWidget(c);
				//��ȡ����onClick�����Ŀؼ�
				ArrayList<Widget> widgetOnClick = null;
				
				//�ȱ���class�еķ����ҵ�oncreate��widgetOnClick��ֵ����Ϊ����Ĳ�����ȷ��widgetOnClick�Ѿ��ȵõ�ֵ
				for(SootMethod sMethod:c.getMethods()){
					//�ҵ�onCreate��������ȡ���е���setOnClickListener�Ŀؼ�
//					System.out.println(c.toString());
//					System.out.println(c.getPackageName());
//					System.out.println(c.getName());
					if(sMethod.toString().contains("onCreate")){
						Body b = sMethod.retrieveActiveBody();
						widgetOnClick =  WidgetsOnClick.getWidget(graphtype, b, widgetArray);
					}
				}
				
				//�ٱ���class�е����з���
				for(SootMethod sMethod : c.getMethods()){
					
					//��ȡԼ�������ɲ�������
					if(sMethod.toString().contains("onClick")){
						Body b = sMethod.retrieveActiveBody();
						
						//��ȡ˳��Լ��
						ArrayList<ArrayList<Widget>> scons = Sequence.getConstraints(graphtype, b, widgetArray);
						
						//��ȡ�߼�Լ��
						ArrayList<LogicConstraints> lcons =  Logic.getConstraints(graphtype, b, widgetArray);
						
						//���ɵ�Լ��д���ļ�
						String className = c.toString();
						String methodName = sMethod.toString();
						//����methodName��ȥ�������ַ�����Ȼ�޷�����·��
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
										
										//�������
										System.err.println("======================================");
										for(int l=0;l<finalArray.size();l++){
											System.out.print(finalArray.get(l).name+":"+finalArray.get(l).inputValue+"\t");
										}
										System.out.println(clickWidget.name);
										System.err.println("======================================");
										
										//TODO UIAutomator�ű�����
										String componentName = c.getPackageName()+"/"+c.toString().split(c.getPackageName())[1];
										AutomatorTestCase.createFile(componentName,finalArray,clickWidget);
										runPython();//���в��Խű�
										
									}
								}
								
							}
							
							
							
						}
					}
					
				}
				
			}
		}
		
	}

	//����Python�ű�
	private void runPython() {
		System.out.println("start running command.py...");
		String cmd = "python command.py";
		try {
			Process process = Runtime.getRuntime().exec(cmd);
			process.waitFor();
			//Thread.currentThread().sleep(10000);
			System.out.println("finish running command.py");
		} catch (Exception e) {
			System.err.println("����command.py����!!");
		} 
		
	}

	/**
	 * ���ؼ�������ֵ��ֵ��˳��Լ����������
	 * @param finalArray ��¼����˳��Լ��
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
		// ����������Ϣ
		graphtype = CFGGraphType.getGraphType("BriefUnitGraph");
		
	}

}
