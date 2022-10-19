package com.acornworks.quote.featuretoggle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;

@Component
public class FlagData {
    private Logger logger = LoggerFactory.getLogger(FlagData.class);
    private RestTemplate restTemplate;
    private String serverUrl;
    private String newFeatureKey;

    public FlagData(RestTemplate restTemplate, 
        @Value("${featuretoggle.server}") String serverUrl,
        @Value("${featuretoggle.key.new}") String newFeatureKey) {
        this.restTemplate = restTemplate;
        this.serverUrl = serverUrl;
        this.newFeatureKey = newFeatureKey;

        logger.info("FlipT URL: {}", serverUrl);
    }

    public boolean isShowNewFeature() {
        final String callUrl = String.format("%s/api/v1/flags/%s", serverUrl, newFeatureKey);

        logger.info("Toogle Call URL: {}", callUrl);

        final JsonNode rootNode = restTemplate.getForObject(callUrl, JsonNode.class);

        if (rootNode == null) {
            return false;
        }

        final JsonNode enabledNode = rootNode.get("enabled");

        return enabledNode == null ? false : enabledNode.asBoolean();
    }
}
