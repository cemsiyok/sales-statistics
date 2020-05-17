package com.ebay.salesstatsservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class SalesRequest {

    @NotNull
    private double salesAmount;

    public SalesRequest(double salesAmount) {
        this.salesAmount = salesAmount;
    }

    public SalesRequest() {
    }

    public double getSalesAmount() {
        return salesAmount;
    }

    @JsonProperty("sales_amount")
    public void setSalesAmount(double salesAmount) {
        this.salesAmount = salesAmount;
    }
}
