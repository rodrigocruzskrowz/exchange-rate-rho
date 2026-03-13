package com.example.exchange_rate_chal.service;

import com.example.exchange_rate_chal.client.ExchangeRateClient;
import com.example.exchange_rate_chal.model.dto.ConversionResponse;
import com.example.exchange_rate_chal.model.dto.ExchangeRateResponse;
import com.example.exchange_rate_chal.model.dto.RateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExchangeRateService {
    private final ExchangeRateClient client;

    @Cacheable(value = "rates", key = "#from + '-' + #to")
    public RateResponse getRate(String from, String to) {
        ExchangeRateResponse response = client.getRates(from, to);

        String key = from.toUpperCase() + to.toUpperCase();
        Double rate = response.getQuotes().get(key);

        return RateResponse.builder()
                .from(from.toUpperCase())
                .to(to.toUpperCase())
                .rate(rate)
                .timestamp(response.getTimestamp())
                .build();
    }

    @Cacheable(value = "rates", key = "#from")
    public List<RateResponse> getAllRates(String from) {
        ExchangeRateResponse response = client.getRates(from, null);

        return response.getQuotes().entrySet().stream()
                .map(entry -> RateResponse.builder()
                        .from(from.toUpperCase())
                        .to(entry.getKey().substring(3))
                        .rate(entry.getValue())
                        .timestamp(response.getTimestamp())
                        .build())
                .toList();
    }

    @Cacheable(value = "conversions", key = "#from + '-' + #to + '-' + #amount")
    public ConversionResponse convert(String from, String to, double amount) {
        var response = client.convert(from, to, amount);

        Double rate = response.getResult() / amount;

        return ConversionResponse.builder()
                .from(from.toUpperCase())
                .to(to.toUpperCase())
                .amount(amount)
                .rate(rate)
                .result(response.getResult())
                .build();
    }

    @Cacheable(value = "conversions", key = "#from + '-' + #suppliedCurrencies.toString() + '-' + #amount")
    public List<ConversionResponse> convertSuppliedCurrencies(String from, List<String> suppliedCurrencies, double amount) {
        String currencies = String.join(",", suppliedCurrencies);

        ExchangeRateResponse response = client.getRates(from, currencies);
        return response.getQuotes().entrySet().stream()
                .map(entry -> {
                    String to = entry.getKey().substring(3);
                    Double rate = entry.getValue();

                    return ConversionResponse.builder()
                            .from(from.toUpperCase())
                            .to(to)
                            .amount(amount)
                            .rate(rate)
                            .result(amount * rate)
                            .build();
                })
                .toList();
    }
}
