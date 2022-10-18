package com.acornworks.quote.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Calendar;

@Getter
@AllArgsConstructor
public class SpotData {
    private String ticker;
    private BigDecimal price;
    private Calendar timestamp;
}
