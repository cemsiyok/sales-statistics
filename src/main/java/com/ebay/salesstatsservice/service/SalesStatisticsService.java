package com.ebay.salesstatsservice.service;

import com.ebay.salesstatsservice.model.SalesStatisticsDTO;
import reactor.core.publisher.Mono;

public interface SalesStatisticsService {

    Mono<Void> feed(Double salesAmount);

    Mono<SalesStatisticsDTO> prepareSummary();
}