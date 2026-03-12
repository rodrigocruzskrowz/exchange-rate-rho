package com.example.exchange_rate_chal.model.dto;

import lombok.Data;

@Data
public class ConvertCurrencyResponse {
    private boolean success;
    private String terms;
    private String privacy;

    private QueryInfo query;
    private Info info;

    private Double result;

    @Data
    public static class QueryInfo {
        private String from;
        private String to;
        private Double amount;
    }

    @Data
    public static class Info {
        private Long timestamp;
        private Double rate;
    }
}
