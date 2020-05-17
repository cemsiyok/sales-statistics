package com.ebay.salesstatsservice.controller;

import com.ebay.salesstatsservice.service.DefaultSalesStatisticsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = SalesStatisticsController.class)
@Import(DefaultSalesStatisticsService.class)
class SalesStatisticsControllerTest {

    @Autowired
    private WebTestClient webClient;

    @Test
    public void it_should_feed_with_new_statistics() {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("sales_amount", "2");

        webClient.post()
                .uri("/api/v1/sales")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .exchange()
                .expectStatus()
                .isAccepted();
    }

    @Test
    public void it_should_return_statistics_json() {
        webClient.get()
                .uri("/api/v1/statistics")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("total_sales_amount").isEqualTo("0.00")
                .jsonPath("average_amount_per_order").isEqualTo("0.00");
    }
}