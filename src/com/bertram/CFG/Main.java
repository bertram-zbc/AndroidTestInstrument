/**
 * 运行soot调用CFGAnalysis方法
 */
package com.bertram.CFG;

import java.util.Collections;

import soot.PackManager;
import soot.Scene;
import soot.Transform;
import soot.options.Options;

public class Main {

	private static boolean SOOT_INITIALIZED=false;
	private final static String androidJAR="./lib/android.jar";
	private final static String appApk="./自由行.apk";
	public static void initialiseSoot()
	{
		if(SOOT_INITIALIZED)
			return;
		
		//设置soot运行结果输出路径
		Options.v().set_output_dir("./RegisterActivity");
		
		Options.v().set_allow_phantom_refs(true);
		Options.v().set_prepend_classpath(true);
		Options.v().set_validate(true);
		Options.v().set_output_format(Options.output_format_dex);
		Options.v().set_process_dir(Collections.singletonList(appApk));
		Options.v().set_force_android_jar(androidJAR);
		Options.v().set_src_prec(Options.src_prec_apk);
		Options.v().set_soot_classpath(androidJAR);
		Scene.v().loadNecessaryClasses();
		SOOT_INITIALIZED=true;
	}
	public static void main(String[] args)
	{
		initialiseSoot();
		
		PackManager.v().getPack("jtp").add(new Transform("jtp.AndroidCFG", new CFGAnalysis()));
		PackManager.v().runPacks();

		PackManager.v().writeOutput();
	}

}
