package com.ebay.salesstatsservice.domainobject;

public class SalesStatistics {

    private long orderCount;
    private double totalSalesAmount;

    public void increment(double newOrder) {
        totalSalesAmount += newOrder;
        orderCount++;
    }

    public void decrement(double expiredOrder) {
        totalSalesAmount -= expiredOrder;
        orderCount--;
    }

    public long getOrderCount() {
        return orderCount;
    }

    public double getTotalSalesAmount() {
        return totalSalesAmount;
    }
}
