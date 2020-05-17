package com.ebay.salesstatsservice.service;

import com.ebay.salesstatsservice.model.SalesStatisticsResponse;
import reactor.core.publisher.Mono;

public interface SalesStatisticsService {

    Mono<Void> feed(Mono<Double> salesAmount);

    Mono<SalesStatisticsResponse> prepareSummary();
}