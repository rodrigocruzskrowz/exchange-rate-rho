package com.example.exchange_rate_chal.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ConversionResponse {
    private String from;
    private String to;
    private Double amount;
    private Double result;
}
