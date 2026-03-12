package com.example.exchange_rate_chal.client;

import com.example.exchange_rate_chal.model.dto.ConvertCurrencyResponse;
import com.example.exchange_rate_chal.model.dto.ExchangeRateResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class ExchangeRateClient {
    private final RestClient restClient;
    private final String accessKey;

    private static final Logger logger = LoggerFactory.getLogger(ExchangeRateClient.class);

    public ExchangeRateClient(
            RestClient.Builder builder,
            @Value("${spring.exchangerate.base-url}") String baseUrl,
            @Value("${spring.exchangerate.access-key}") String accessKey) {

        this.restClient = builder.baseUrl(baseUrl).build();
        this.accessKey = accessKey;
    }

    public ExchangeRateResponse getRates(String source, String currencies) {

        logger.info("::: Requesting exchange rates from http://exchangerate.host/live with source: {} and currencies: {}", source, currencies);

        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/live")
                        .queryParam("access_key", accessKey)
                        .queryParam("source", source)
                        .queryParamIfPresent("currencies",
                                java.util.Optional.ofNullable(currencies))
                        .build())
                .retrieve()
                .body(ExchangeRateResponse.class);
    }

    public ConvertCurrencyResponse convert(String from, String to, double amount) {

        logger.info("::: Requesting currency conversion from http://exchangerate.host/convert with from: {}, to: {}, amount: {}", from, to, amount);

        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/convert")
                        .queryParam("access_key", accessKey)
                        .queryParam("from", from)
                        .queryParam("to", to)
                        .queryParam("amount", amount)
                        .build())
                .retrieve()
                .body(ConvertCurrencyResponse.class);
    }

}
