package com.acornworks.quote.calculations;

import com.acornworks.quote.data.YahooFinanceData;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.io.*;

@ExtendWith(MockitoExtension.class)
class RatioCalculationTest {
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private YahooFinanceData yahooFinanceData;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private JsonNode readPayload(String resourcePath) throws IOException {
        String jsonStr = "";

        try(FileReader fr = new FileReader(getClass().getClassLoader().getResource(resourcePath).getFile())) {
            try(BufferedReader br = new BufferedReader(fr)) {
                String line = null;
                StringBuffer sb = new StringBuffer();

                while((line = br.readLine()) != null) {
                    sb.append(line + System.lineSeparator());
                }

                jsonStr = sb.toString();
            }
        }

        final JsonNode payloadNode = objectMapper.readTree(jsonStr);

        return payloadNode;
    }

    @Test
    void testRatio() throws IOException {
        final JsonNode payloadNode = readPayload("payloads/audkrw.json");

        ReflectionTestUtils.setField(yahooFinanceData, "baseUrl", "https://localhost:60081/%s");

        Mockito.when(restTemplate.getForObject(
                ArgumentMatchers.anyString(),
                Mockito.eq(JsonNode.class))).thenReturn(payloadNode);

        RatioCalculation ratioCalculation = new RatioCalculation(yahooFinanceData);
        double ratio = ratioCalculation.getRatio("AUDKRW=X");

        Assertions.assertEquals(0.001073618, ratio);
    }

}
