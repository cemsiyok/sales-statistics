package com.ebay.salesstatsservice.service;

import com.ebay.salesstatsservice.model.SalesStatisticsDTO;
import com.ebay.salesstatsservice.properties.ConfigProperties;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.time.Duration;

class DefaultSalesStatisticsServiceTest {

    private final DefaultSalesStatisticsService salesStatisticsService;

    private Integer ttlInMillis = 10_000;

    {
        ConfigProperties configProperties = new ConfigProperties();
        configProperties.setTimeInSecond(Duration.ofMillis(ttlInMillis));
        salesStatisticsService = new DefaultSalesStatisticsService(configProperties);
    }

    @Test
    void it_should_return_sales_statistics_snapshot() {
        // given
        double salesAmount = 10.11d;
        double expectedTotalAmount = 0d;
        long orderCount = 4_000;
        for (int i = 0; i < orderCount; i++) {
            StepVerifier.create(salesStatisticsService.feed(salesAmount))
                    .expectComplete()
                    .verify();
            expectedTotalAmount += salesAmount;
        }
        // when
        StepVerifier.create(salesStatisticsService.prepareSummary())

                // then
                .expectNext(new SalesStatisticsDTO(orderCount, Math.round(expectedTotalAmount)))
                .expectComplete()
                .verify();
    }

    @Test
    void it_should_return_sales_statistics_snapshot_when_all_tuples_expired() throws InterruptedException {
        // given
        double salesAmount = 10.11d;
        long orderCount = 1000;
        for (int i = 0; i < orderCount; i++) {
            StepVerifier.create(salesStatisticsService.feed(salesAmount))
                    .expectComplete()
                    .verify();
        }
        Thread.sleep(ttlInMillis);
        salesStatisticsService.cleanUp();

        // when
        StepVerifier.create(salesStatisticsService.prepareSummary())

                // then
                .expectNext(new SalesStatisticsDTO(0L, 0d))
                .expectComplete()
                .verify();
    }
}