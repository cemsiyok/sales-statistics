package com.ebay.salesstatsservice.configuration;

import com.ebay.salesstatsservice.reactor.SchedulerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class ReactorConfig {

    @Bean
    public SchedulerFactory schedulerFactory() {
        return new SchedulerFactory(reactorExecutor());
    }

    public Executor reactorExecutor() {
        var executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("reactor-executer-");
        executor.initialize();
        return executor;
    }
}
