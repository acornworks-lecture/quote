package com.acornworks.quote.data;

import com.acornworks.quote.objects.SpotData;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@ExtendWith(MockitoExtension.class)
class YahooFinanceDataTest {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private YahooFinanceData yahooFinanceData;

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
    void testGetPrice() throws IOException {
        final JsonNode payloadNode = readPayload("payloads/audkrw.json");
        ReflectionTestUtils.setField(yahooFinanceData, "baseUrl", "https://localhost:60081/%s");

        Mockito.when(restTemplate.getForObject(
                ArgumentMatchers.anyString(),
                Mockito.eq(JsonNode.class))).thenReturn(payloadNode);

        final SpotData spotData = yahooFinanceData.getPrice("AUDKRW=X");

        Assertions.assertEquals(931.43, spotData.getPrice().doubleValue());
        Assertions.assertEquals("AUDKRW=X", spotData.getTicker());
        Assertions.assertNotNull(spotData.getTimestamp());
    }

}
