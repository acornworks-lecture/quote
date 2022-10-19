package com.acornworks.quote.controllers;

import com.acornworks.quote.calculations.RatioCalculation;
import com.acornworks.quote.data.YahooFinanceData;
import com.acornworks.quote.featuretoggle.FlagData;
import com.acornworks.quote.objects.SpotData;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.Calendar;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = QuoteController.class)
public class QuoteControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private YahooFinanceData yahooFinanceData;

    @MockBean
    private FlagData flagData;

    @MockBean
    private RatioCalculation ratioCalculation;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testGetPrice() throws Exception {
        final Calendar calendar = Calendar.getInstance();

        Mockito.when(yahooFinanceData.getPrice(Mockito.anyString()))
                .thenReturn(new SpotData("AUDKRW=X", new BigDecimal("900.00"), calendar));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/quote/AUDKRW=X")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        Assertions.assertEquals(result.getResponse().getStatus(), 200);

        final JsonNode resultNode = objectMapper.readTree(result.getResponse().getContentAsString());

        Assertions.assertEquals(resultNode.get("ticker").asText(), "AUDKRW=X");
        Assertions.assertEquals(resultNode.get("price").asDouble(), 900.0d);
        Assertions.assertNotNull(resultNode.get("timestamp"));
    }

    @Test
    void testGetRatio() throws Exception {
        Mockito.when(ratioCalculation.getRatio(Mockito.anyString())).thenReturn(0.01d);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/quote/ratio/AUDKRW=X")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        Assertions.assertEquals(result.getResponse().getStatus(), 200);

        final JsonNode resultNode = objectMapper.readTree(result.getResponse().getContentAsString());

        Assertions.assertEquals(resultNode.get("ratio").asDouble(), 0.01d);
    }
}
