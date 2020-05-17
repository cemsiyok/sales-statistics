package com.ebay.salesstatsservice.controller;

import com.ebay.salesstatsservice.model.SalesStatisticsResponse;
import com.ebay.salesstatsservice.service.SalesStatisticsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1")
public class SalesStatisticsController {

    private final SalesStatisticsService salesStatisticsService;

    public SalesStatisticsController(SalesStatisticsService salesStatisticsService) {
        this.salesStatisticsService = salesStatisticsService;
    }

    @PostMapping(value = "/sales", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Mono<Void> createSale(ServerWebExchange exchange) {
        Mono<Double> salesAmount = exchange.getFormData().flatMap(formData -> {
            String parameterValue = formData.getFirst("sales_amount");
            return Mono.just(Double.parseDouble(parameterValue));
        });
        return salesStatisticsService.feed(salesAmount);
    }

    @GetMapping("/statistics")
    public Mono<SalesStatisticsResponse> getStatistics() {
        return salesStatisticsService.prepareSummary();
    }
}
