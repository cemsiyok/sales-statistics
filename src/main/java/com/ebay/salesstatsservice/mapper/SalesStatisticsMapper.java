package com.ebay.salesstatsservice.mapper;

import com.ebay.salesstatsservice.model.SalesStatisticsDTO;
import com.ebay.salesstatsservice.model.SalesStatisticsResponse;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class SalesStatisticsMapper {

    private static final String ZERO = "0.00";
    private static final int SCALE = 2;

    public static SalesStatisticsResponse makeSalesStatisticsResponse(SalesStatisticsDTO salesStatisticsDTO) {
        if (salesStatisticsDTO.getCount() == 0L) {
            return new SalesStatisticsResponse(ZERO, ZERO);
        }
        var totalSalesAmount = BigDecimal.valueOf(salesStatisticsDTO.getTotal());
        return new SalesStatisticsResponse(
                totalSalesAmount.setScale(SCALE, RoundingMode.HALF_UP).toString(),
                totalSalesAmount.divide(BigDecimal.valueOf(salesStatisticsDTO.getCount()), SCALE, RoundingMode.HALF_UP).toString()
        );
    }
}
