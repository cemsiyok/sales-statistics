package com.ebay.salesstatsservice.service;

import javafx.util.Pair;
import reactor.core.publisher.Mono;

public interface SalesStatisticsService {

    Mono<Void> feed(Mono<Double> salesAmount);

    Mono<Pair<Long, Double>> prepareSummary();
}