package com.ebay.salesstatsservice.service;

import com.ebay.salesstatsservice.domain.SalesStatistics;
import com.ebay.salesstatsservice.model.SalesStatisticsDTO;
import com.ebay.salesstatsservice.properties.ConfigProperties;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.atomic.AtomicLong;

@Service
public class DefaultSalesStatisticsService implements SalesStatisticsService {

    private final ConfigProperties configProperties;
    private final SalesStatistics salesStatistics;
    private final AtomicLong idCounter;
    private final Cache<String, Double> cache;

    public DefaultSalesStatisticsService(ConfigProperties configProperties) {
        this.configProperties = configProperties;
        this.salesStatistics = new SalesStatistics();
        this.idCounter = new AtomicLong();
        RemovalListener<String, Double> removalListener = it -> salesStatistics.decrement(it.getValue());
        this.cache = CacheBuilder.newBuilder()
                .expireAfterWrite(configProperties.getTimeInSecond())
                .removalListener(removalListener)
                .build();
    }

    @Override
    public Mono<Void> feed(Double salesAmount) {
        return Mono.fromRunnable(() -> {
            cache.put(createID(), salesAmount);
            salesStatistics.increment(salesAmount);
        }).subscribeOn(Schedulers.parallel()).then();
    }

    @Override
    public Mono<SalesStatisticsDTO> prepareSummary() {
        return Mono.fromCallable(salesStatistics::getSummary)
                .subscribeOn(Schedulers.parallel());
    }

    /*
     If our cache is high-throughput, then we don't have to worry about
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