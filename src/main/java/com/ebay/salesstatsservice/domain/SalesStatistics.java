package com.ebay.salesstatsservice.domain;

import com.ebay.salesstatsservice.model.SalesStatisticsDTO;

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

    public SalesStatisticsDTO getSummary() {
        long localCount;
        double localTotal;
        out:
        {
            for (int i = 0; i < OPTIMISTIC_SPIN; i++) {
                long stamp = sl.tryOptimisticRead();
                localCount = orderCount;
                localTotal = totalSalesAmount;
                if (sl.validate(stamp)) {
                    break out;
                }
            }
            long stamp = sl.readLock();
            try {
                localCount = orderCount;
                localTotal = totalSalesAmount;
            } finally {
                sl.unlockRead(stamp);
            }
        }
        return new SalesStatisticsDTO(localCount, localTotal);
    }
}