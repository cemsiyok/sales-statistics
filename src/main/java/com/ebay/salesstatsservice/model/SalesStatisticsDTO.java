package com.ebay.salesstatsservice.model;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SalesStatisticsDTO that = (SalesStatisticsDTO) o;
        return count == that.count &&
                Double.compare(that.total, total) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(count, total);
    }

    @Override
    public String toString() {
        return "SalesStatisticsDTO{" +
                "count=" + count +
                ", total=" + total +
                '}';
    }
}
