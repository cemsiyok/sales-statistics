package com.ebay.salesstatsservice.service;

import com.ebay.salesstatsservice.domain.SalesStatistics;
import com.ebay.salesstatsservice.reactor.SchedulerFactory;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import javafx.util.Pair;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class DefaultSalesStatisticsService implements SalesStatisticsService {

    private static final int DURATION_IN_SECOND = 60;

    private final SalesStatistics salesStatistics;
    private final AtomicLong idCounter;
    private final Cache<String, Double> cache;
    private final SchedulerFactory schedulerFactory;

    public DefaultSalesStatisticsService(SchedulerFactory schedulerFactory) {
        this.salesStatistics = new SalesStatistics();
        this.schedulerFactory = schedulerFactory;
        this.idCounter = new AtomicLong();
        RemovalListener<String, Double> removalListener = it -> {
            salesStatistics.decrement(it.getValue());
        };
        this.cache = CacheBuilder.newBuilder()
                .expireAfterWrite(DURATION_IN_SECOND, TimeUnit.SECONDS)
                .removalListener(removalListener)
                .build();
    }

    @Override
    public Mono<Void> feed(Mono<Double> salesAmount) {
        return salesAmount.flatMap(it -> {
            salesStatistics.increment(it);
            cache.put(createID(), it);
            return Mono.empty();
        }).subscribeOn(schedulerFactory.parallel()).then();
    }

    @Override
    public Mono<Pair<Long, Double>> prepareSummary() {
        return Mono.fromCallable(salesStatistics::getView).subscribeOn(schedulerFactory.parallel());
    }

    /*
     If your cache is high-throughput, then you don't have to worry about
     performing cache maintenance to clean up expired entries and the like.
     */
    @Scheduled(initialDelay = 60000, fixedDelay = 1000)
    public void cleanUp() {
        cache.cleanUp();
    }

    private String createID() {
        return String.valueOf(idCounter.incrementAndGet());
    }

}