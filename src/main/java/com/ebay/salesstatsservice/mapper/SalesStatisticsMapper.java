package com.ebay.salesstatsservice.mapper;

import com.ebay.salesstatsservice.domainobject.SalesStatistics;
import com.ebay.salesstatsservice.model.SalesStatisticsResponse;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class SalesStatisticsMapper {

    private static final String ZERO = "0.00";
    private static final int SCALE = 2;

    public static SalesStatisticsResponse makeSalesStatisticsResponse(SalesStatistics salesStatistics) {
        if (salesStatistics.getOrderCount() == 0L) {
            return new SalesStatisticsResponse(ZERO, ZERO);
        }
        var totalSalesAmount = BigDecimal.valueOf(salesStatistics.getTotalSalesAmount());
        return new SalesStatisticsResponse(
                totalSalesAmount.setScale(SCALE, RoundingMode.HALF_UP).toString(),
                totalSalesAmount.divide(BigDecimal.valueOf(salesStatistics.getOrderCount()), SCALE, RoundingMode.HALF_UP).toString()
        );
    }
}
