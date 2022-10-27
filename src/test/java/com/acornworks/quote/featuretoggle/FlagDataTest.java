package com.acornworks.quote.featuretoggle;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Assertions;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class FlagDataTest {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private FlagData flagData;

    @Test
    void testIsShowNewFeature() throws Exception {
        ReflectionTestUtils.setField(flagData, "serverUrl", "http://localhost:64080");
        ReflectionTestUtils.setField(flagData, "newFeatureKey", "NewFeature");   

        JsonNode payloadNode = objectMapper.readTree("{\"enabled\": true}");

        Mockito.when(restTemplate.getForObject(ArgumentMatchers.anyString(), Mockito.eq(JsonNode.class)))
        .thenReturn(payloadNode);

        boolean result = flagData.isShowNewFeature();

        Assertions.assertTrue(result);

        Mockito.when(restTemplate.getForObject(ArgumentMatchers.anyString(), Mockito.eq(JsonNode.class)))
        .thenReturn(null);

        result = flagData.isShowNewFeature();

        Assertions.assertFalse(result);

        payloadNode = objectMapper.readTree("{\"dummy\": true}");

        Mockito.when(restTemplate.getForObject(ArgumentMatchers.anyString(), Mockito.eq(JsonNode.class)))
        .thenReturn(payloadNode);

        result = flagData.isShowNewFeature();

        Assertions.assertFalse(result);
    }

    
}
