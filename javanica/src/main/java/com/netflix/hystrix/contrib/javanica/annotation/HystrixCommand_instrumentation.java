package com.netflix.hystrix.contrib.javanica.annotation;

import java.lang.reflect.Method;
import java.util.logging.Level;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.WeaveIntoAllMethods;
import com.newrelic.api.agent.weaver.WeaveWithAnnotation;
import com.newrelic.api.agent.weaver.Weaver;
import com.nr.instrumentation.hystrix.javanica.Utils;

public class HystrixCommand_instrumentation {


	@WeaveWithAnnotation(annotationClasses= {"com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand"},type=MatchType.Interface)
	@WeaveIntoAllMethods
	@Trace(dispatcher=true)
	private static void instrumentation() {
		HystrixCommand hystrixCommand = Weaver.getMethodAnnotation(HystrixCommand.class);
		
		if (hystrixCommand != null) {
			String cmdKey = hystrixCommand.commandKey();
			String grpKey = hystrixCommand.groupKey();
			String fallback = hystrixCommand.fallbackMethod();
			if (grpKey != null && !grpKey.isEmpty()) {
				if (cmdKey != null && !cmdKey.isEmpty()) {
					NewRelic.getAgent().getTracedMethod().setMetricName("Custom", "HystrixCommand", grpKey, cmdKey);
				}
			} else if (cmdKey != null && !cmdKey.isEmpty()) {
				NewRelic.getAgent().getTracedMethod().setMetricName("Custom", "HystrixCommand", cmdKey);
			}
			if(fallback != null && !fallback.isEmpty()) {
				StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
				StackTraceElement first = stacktrace[1];
				String classname = first.getClassName();
				NewRelic.getAgent().getLogger().log(Level.FINE, "attempting to find method: {0} in class: {1}",fallback,classname);
				try {
					Class<?> clazz = Class.forName(classname);
					Method[] methods = clazz.getMethods();
					for(Method method : methods) {
						String methodName = method.getName();
						if(methodName.equals(fallback)) {
							if(!Utils.isInstrumented(method)) {
								Utils.instrument(method);
								break;
							}
						}
					}
				} catch (ClassNotFoundException e) {
					NewRelic.getAgent().getLogger().log(Level.FINE, "failed to find class: {0}",classname);
				}
				
			}
			NewRelic.getAgent().getLogger().log(Level.FINE,"call to Hystrix annotated method, grpKey: {0}, cmdKey: {1}, fallback: {2}", grpKey, cmdKey, 
					fallback);
		} else {
			NewRelic.getAgent().getLogger().log(Level.FINE,"call to Hystrix annotated method, did not find HystrixCommand");
		}
		NewRelic.getAgent().getLogger().log(Level.FINE,"call to Hystrix annotated method produced metric name: {0}",NewRelic.getAgent().getTracedMethod().getMetricName());
	}
}
