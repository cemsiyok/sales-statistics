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
        // given
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("sales_amount", "2");
        String uri = "/api/v1/sales";

        // when
        webClient.post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .exchange()

                // then
                .expectStatus()
                .isAccepted()
                .expectBody()
                .isEmpty();
    }

    @Test
    public void it_return_bad_request_when_sales_amount_not_provided() {
        // Given
        String uri = "/api/v1/sales";

        // when
        webClient.post()
                .uri("/api/v1/sales")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(new LinkedMultiValueMap<>()))
                .exchange()

                // then
                .expectStatus()
                .isBadRequest()
                .expectBody()
                .jsonPath("$.status").isEqualTo("400")
                .jsonPath("$.error").isEqualTo("Bad Request");
    }

    @Test
    public void it_should_return_statistics_json() {
        // given
        String uri = "/api/v1/statistics";

        // when
        webClient.get()
                .uri(uri)
                .exchange()

                // then
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.total_sales_amount").isEqualTo("0.00")
                .jsonPath("$.average_amount_per_order").isEqualTo("0.00");
    }
}