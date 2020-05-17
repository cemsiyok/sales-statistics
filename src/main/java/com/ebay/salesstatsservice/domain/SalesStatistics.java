package com.ebay.salesstatsservice.domain;

import javafx.util.Pair;

import java.util.concurrent.locks.StampedLock;

public class SalesStatistics {
    private static final int OPTIMISTIC_SPIN = 5;
    private final StampedLock sl = new StampedLock();
    private long orderCount;
    private double totalSalesAmount;

    public void increment(double newOrder) {
        long stamp = sl.writeLock();
        try {
            totalSalesAmount += newOrder;
            orderCount++;
        } finally {
            sl.unlockWrite(stamp);
        }
    }

    public void decrement(double expiredOrder) {
        long stamp = sl.writeLock();
        try {
            totalSalesAmount -= expiredOrder;
            orderCount--;
        } finally {
            sl.unlockWrite(stamp);
        }
    }

    public long getOrderCount() {
        return orderCount;
    }

    public double getTotalSalesAmount() {
        return totalSalesAmount;
    }

    public Pair<Long, Double> getSummary() {
        long localCount;
        double localTotal;
        out:
        // try a few times to do an optimistic read
        {
            for (int i = 0; i < OPTIMISTIC_SPIN; i++) {
                long stamp = sl.tryOptimisticRead();
                localCount = orderCount;
                localTotal = totalSalesAmount;
                if (sl.validate(stamp)) {
                    break out;
                }
            }
            // pessimistic read
            long stamp = sl.readLock();
            try {
                localCount = orderCount;
                localTotal = totalSalesAmount;
            } finally {
                sl.unlockRead(stamp);
            }
        }
        return new Pair<>(localCount, localTotal);
    }

}