package com.netflix.hystrix.strategy.concurrency;

import java.util.concurrent.TimeUnit;

import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;

import rx.Subscription;
import rx.functions.Action0;

@Weave
public class HystrixContextScheduler {

	@Weave
	private static class HystrixContextSchedulerWorker {
		
		@Trace
		 public Subscription schedule(Action0 action, long delayTime, TimeUnit unit) {
			 return Weaver.callOriginal();
		 }
		
		@Trace
		public Subscription schedule(Action0 action) {
			 return Weaver.callOriginal();
		}
	}
}
