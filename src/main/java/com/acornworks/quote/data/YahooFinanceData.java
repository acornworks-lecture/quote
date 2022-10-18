package com.acornworks.quote.data;

import com.acornworks.quote.objects.SpotData;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Calendar;

@Component
public class YahooFinanceData {
    private Logger logger = LoggerFactory.getLogger(YahooFinanceData.class);
    private String baseUrl;
    private RestTemplate restTemplate;

    public YahooFinanceData(RestTemplate restTemplate, @Value("${quote.yahoo.format}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public SpotData getPrice(String symbol) throws NullPointerException {
        logger.info("Get price from Yahoo for {}", symbol);
        final String url = String.format(baseUrl, symbol);
        logger.info("Calling URL: {}", url);

        final JsonNode rootNode = restTemplate.getForObject(url, JsonNode.class);
        final JsonNode resultNode = rootNode.get("optionChain").get("result").get(0);

        final BigDecimal price = BigDecimal.valueOf(resultNode.get("quote").get("regularMarketPrice").asDouble());
        final Calendar timestamp = Calendar.getInstance();

        return new SpotData(symbol, price, timestamp);
    }
}
