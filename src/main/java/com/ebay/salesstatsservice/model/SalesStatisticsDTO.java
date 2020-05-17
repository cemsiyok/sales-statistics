package com.ebay.salesstatsservice.model;

public class SalesStatisticsDTO {

    private long count;
    private double total;

    public SalesStatisticsDTO(long count, double total) {
        this.count = count;
        this.total = total;
    }

    public long getCount() {
        return count;
    }

    public double getTotal() {
        return total;
    }
}
