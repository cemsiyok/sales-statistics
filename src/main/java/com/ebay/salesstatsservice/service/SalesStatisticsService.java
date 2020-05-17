package com.ebay.salesstatsservice.service;

import com.ebay.salesstatsservice.domainobject.SalesStatistics;
import reactor.core.publisher.Mono;

public interface SalesStatisticsService {

    Mono<Void> feed(Mono<Double> salesAmount);

    Mono<SalesStatistics> prepareSummary();
}