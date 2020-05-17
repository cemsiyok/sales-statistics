package com.ebay.salesstatsservice.reactor;

import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.Executor;

public class SchedulerFactory {

    private final Executor reactorExecutor;

    public SchedulerFactory(Executor reactorExecutor) {
        this.reactorExecutor = reactorExecutor;
    }

    public Scheduler parallel() {
        return Schedulers.fromExecutor(reactorExecutor);
    }
}
