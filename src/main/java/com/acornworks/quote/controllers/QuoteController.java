package com.acornworks.quote.controllers;

import com.acornworks.quote.calculations.RatioCalculation;
import com.acornworks.quote.data.YahooFinanceData;
import com.acornworks.quote.featuretoggle.FlagData;
import com.acornworks.quote.objects.SpotData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/quote")
public class QuoteController {
    @Autowired
    private YahooFinanceData yahooFinanceData;

    @Autowired
    private FlagData flagData;

    @Autowired
    private RatioCalculation ratioCalculation;

    @CrossOrigin
    @GetMapping(value = "/{symbol}", produces = "application/json")
    public SpotData getPrice(@PathVariable("symbol") String symbol) {
        return yahooFinanceData.getPrice(symbol);
    }

    @CrossOrigin
    @GetMapping(value = "/ratio/{symbol}", produces = "application/json")
    public Map<String, Double> getRatio(@PathVariable("symbol") String symbol) {
        final Map<String, Double> rtnMap = new HashMap<>();
        final double ratio = ratioCalculation.getRatio(symbol);

        rtnMap.put("ratio", ratio);

        return rtnMap;
    }

    @GetMapping(value = "/environment", produces = "application/json")
    public Map<String, String> getEnvironment() {
        final Map<String, String> rtnMap = new HashMap<>();

        rtnMap.put("ENVIRONMENT", System.getenv().containsKey("ENV_COLOR") ? System.getenv("ENV_COLOR").toUpperCase() : "BLUE");

        return rtnMap;
    }

    @GetMapping(value = "/engine", produces = "application/json")
    public Map<String, String> getEngineKey() {
        final Map<String, String> rtnMap = new HashMap<>();

        // FIXME Refactor after STORY-001 release
        if (flagData.isShowNewFeature()) {
            rtnMap.put("EngineKey", "NewKey0001");
        } else {
            rtnMap.put("EngineKey", "OldKey0001");
        }

        return rtnMap;
    }
}
