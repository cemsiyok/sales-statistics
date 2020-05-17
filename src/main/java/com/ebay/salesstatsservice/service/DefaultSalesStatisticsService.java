package com.ebay.salesstatsservice.service;

import com.ebay.salesstatsservice.mapper.SalesStatisticsMapper;
import com.ebay.salesstatsservice.model.SalesStatisticsResponse;
import com.ebay.salesstatsservice.reactor.SchedulerFactory;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class DefaultSalesStatisticsService implements SalesStatisticsService {

    private static final int DURATION_IN_SECOND = 60;

    private long orderCount;
    private double totalSalesAmount;
    private final AtomicLong idCounter;
    private final Lock lock;
    private final Cache<String, Double> cache;
    private final SchedulerFactory schedulerFactory;

    public DefaultSalesStatisticsService(SchedulerFactory schedulerFactory) {
        this.schedulerFactory = schedulerFactory;
        this.idCounter = new AtomicLong();
        this.lock = new ReentrantLock(true);
        RemovalListener<String, Double> removalListener = it -> {
            try {
                lock.lock();
                orderCount--;
                totalSalesAmount -= it.getValue();
            } finally {
                lock.unlock();
            }
        };
        this.cache = CacheBuilder.newBuilder()
                .expireAfterWrite(DURATION_IN_SECOND, TimeUnit.SECONDS)
                .removalListener(removalListener)
                .build();
    }

    @Override
    public Mono<Void> feed(Mono<Double> salesAmount) {
        return salesAmount.flatMap(it -> {
            try {
                lock.lock();
                orderCount++;
                totalSalesAmount += it;
                cache.put(createID(), it);
            } finally {
                lock.unlock();
            }
            return Mono.empty();
        }).subscribeOn(schedulerFactory.parallel()).then();
    }

    public Mono<SalesStatisticsResponse> prepareSummary() {
        return Mono.fromCallable(() -> {
            try {
                lock.lock();
                return SalesStatisticsMapper.makeSalesStatisticsResponse(totalSalesAmount, orderCount);
            } finally {
                lock.unlock();
            }
        }).subscribeOn(schedulerFactory.parallel());
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