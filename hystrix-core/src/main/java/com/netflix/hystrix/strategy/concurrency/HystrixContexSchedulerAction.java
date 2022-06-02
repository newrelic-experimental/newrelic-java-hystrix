package com.netflix.hystrix.strategy.concurrency;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Token;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.NewField;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;

import rx.functions.Action0;

@Weave
public class HystrixContexSchedulerAction {

	@NewField
	private Token token = null;
	
	public HystrixContexSchedulerAction(final HystrixConcurrencyStrategy concurrencyStrategy, Action0 action) {
		token = NewRelic.getAgent().getTransaction().getToken();
	}
	
	@Trace(async=true)
    public void call() {
    	if(token != null) {
    		token.linkAndExpire();
    		token = null;
    	}
    	Weaver.callOriginal();
    }

}
