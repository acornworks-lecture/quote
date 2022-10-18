package com.acornworks.quote.calculations;

import com.acornworks.quote.data.YahooFinanceData;
import com.acornworks.quote.objects.SpotData;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.MathContext;

@Component
public class RatioCalculation {
    private YahooFinanceData yahooFinanceData;

    public RatioCalculation(YahooFinanceData yahooFinanceData) {
        this.yahooFinanceData = yahooFinanceData;
    }

    public double getRatio(final String ticker) throws NullPointerException {
        final SpotData spotData = yahooFinanceData.getPrice(ticker);

        return BigDecimal.ONE.divide(spotData.getPrice(), MathContext.DECIMAL32).doubleValue();
    }
}
