package com.ebay.salesstatsservice.model;

import javax.validation.constraints.NotNull;

public class SalesRequest {

    @NotNull
    private Double sales_amount;

    public Double getSales_amount() {
        return sales_amount;
    }

    public void setSales_amount(Double sales_amount) {
        this.sales_amount = sales_amount;
    }
}
