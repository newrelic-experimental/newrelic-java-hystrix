package com.nr.instrumentation.hystrix.javanica;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.newrelic.agent.bridge.AgentBridge;
import com.newrelic.api.agent.NewRelic;

public class Utils {

	static List<String> instrumented = new ArrayList<String>();
	
	public static boolean isInstrumented(Method method) {
		Class<?> clazz = method.getDeclaringClass();
		String classname = clazz.getName();
		String name = method.getName();
		return instrumented.contains(classname+"-"+name);
	}
	
	public static void instrument(Method method) {
		NewRelic.getAgent().getLogger().log(Level.FINE, "attempting to instrument method: {0}", method);
		AgentBridge.instrumentation.instrument(method, "Custom/HystrixCommand/Fallback/");
		Class<?> clazz = method.getDeclaringClass();
		String classname = clazz.getName();
		String name = method.getName();
		boolean b = instrumented.add(classname+"-"+name);
		if(b) {
			NewRelic.getAgent().getLogger().log(Level.FINE, "instrumented "+classname+"-"+name);
		} else {
			NewRelic.getAgent().getLogger().log(Level.FINE, "failed to instrument "+classname+"-"+name);
		}
	}
	
}
