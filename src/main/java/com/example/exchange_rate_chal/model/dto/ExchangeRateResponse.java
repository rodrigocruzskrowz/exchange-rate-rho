package com.example.exchange_rate_chal.model.dto;

import lombok.Data;
import java.util.Map;

@Data
public class ExchangeRateResponse {
    private boolean success;
    private String terms;
    private String privacy;
    private Long timestamp;
    private String source;
    private Map<String, Double> quotes;
}
