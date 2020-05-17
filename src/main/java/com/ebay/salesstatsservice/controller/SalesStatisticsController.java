package com.ebay.salesstatsservice.controller;

import com.ebay.salesstatsservice.mapper.SalesStatisticsMapper;
import com.ebay.salesstatsservice.model.SalesRequest;
import com.ebay.salesstatsservice.model.SalesStatisticsResponse;
import com.ebay.salesstatsservice.service.SalesStatisticsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
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
    public Mono<Void> createSale(@Validated @ModelAttribute SalesRequest salesRequest) {
        return salesStatisticsService.feed(salesRequest.getSales_amount());
    }

    @GetMapping("/statistics")
    public Mono<SalesStatisticsResponse> getStatistics() {
        return salesStatisticsService.prepareSummary().map(SalesStatisticsMapper::makeSalesStatisticsResponse);
    }
}
