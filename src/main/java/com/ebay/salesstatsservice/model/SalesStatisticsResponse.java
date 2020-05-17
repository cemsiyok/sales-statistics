package com.ebay.salesstatsservice.model;

public class SalesStatisticsResponse {

    private String totalSalesAmount;
    private String averageAmountPerOrder;

    public SalesStatisticsResponse(String totalSalesAmount, String averageAmountPerOrder) {
        this.totalSalesAmount = totalSalesAmount;
        this.averageAmountPerOrder = averageAmountPerOrder;
    }

    public String getTotalSalesAmount() {
        return totalSalesAmount;
    }

    public void setTotalSalesAmount(String totalSalesAmount) {
        this.totalSalesAmount = totalSalesAmount;
    }

    public String getAverageAmountPerOrder() {
        return averageAmountPerOrder;
    }

    public void setAverageAmountPerOrder(String averageAmountPerOrder) {
        this.averageAmountPerOrder = averageAmountPerOrder;
    }
}
