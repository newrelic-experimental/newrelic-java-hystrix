package com.netflix.hystrix.contrib.javanica.command;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;

@Weave(type=MatchType.Interface)
public abstract class CommandAction {

	@Trace(dispatcher=true)
	public Object execute(ExecutionType executionType) {
		NewRelic.getAgent().getTracedMethod().setMetricName("Custom","Hystrix","CommandAction",getClass().getSimpleName(),"execute",getActionName());
		return Weaver.callOriginal();
	}
	
	@Trace(dispatcher=true)
	public Object executeWithArgs(ExecutionType executionType, Object[] args) {
		NewRelic.getAgent().getTracedMethod().setMetricName("Custom","Hystrix","CommandAction",getClass().getSimpleName(),"executeWithArgs",getActionName());
		return Weaver.callOriginal();
	}
	
	public abstract String getActionName();
}
