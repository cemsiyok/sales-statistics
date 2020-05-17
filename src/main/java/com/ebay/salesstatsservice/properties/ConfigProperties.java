package com.ebay.salesstatsservice.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DurationUnit;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@ConfigurationProperties(prefix = "cache")
public class ConfigProperties {

    @DurationUnit(ChronoUnit.SECONDS)
    private Duration timeInSecond;

    public Duration getTimeInSecond() {
        return timeInSecond;
    }

    public void setTimeInSecond(Duration timeInSecond) {
        this.timeInSecond = timeInSecond;
    }
}
