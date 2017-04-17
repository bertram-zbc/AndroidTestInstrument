/**
 * ��Ŀ����������
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
		
		//��ʼ����һЩ����
		initialize();
		
		//����apk������class
		for(SootClass c : Scene.v().getApplicationClasses()){
			if(c.toString().contains("RegisterActivity")&&!c.toString().contains("$")){
				
				//��ȡclass�е����пؼ���Ϣ
				ArrayList<Widget> widgetArray = AndroidWidgetRecord.recordWidget(c);
				//����class�е����з���
				for(SootMethod sMethod : c.getMethods()){
					
					//�ҵ�onCreate��������ȡ���е���setOnClickListener�Ŀؼ�
					if(sMethod.toString().contains("onCreate")){
						Body b = sMethod.retrieveActiveBody();
						WidgetsOnClick.getWidget(graphtype, b, widgetArray);
					}
					
					//��ȡ˳��Լ��
					if(sMethod.toString().contains("onClick")){
						Body b = sMethod.retrieveActiveBody();
						Sequence.getConstraints(graphtype, b, widgetArray);
					}
					
				}
				
			}
		}
		
	}

	private void initialize() {
		// ����������Ϣ
		graphtype = CFGGraphType.getGraphType("BriefUnitGraph");
		
	}

}
