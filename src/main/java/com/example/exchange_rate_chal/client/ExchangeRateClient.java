package com.example.exchange_rate_chal.client;

import com.example.exchange_rate_chal.model.dto.ConvertCurrencyResponse;
import com.example.exchange_rate_chal.model.dto.ExchangeRateResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class ExchangeRateClient {
    private final RestClient restClient;
    private final String accessKey;

    public ExchangeRateClient(
            RestClient.Builder builder,
            @Value("${spring.exchangerate.base-url}") String baseUrl,
            @Value("${spring.exchangerate.access-key}") String accessKey) {

        this.restClient = builder.baseUrl(baseUrl).build();
        this.accessKey = accessKey;
    }

    public ExchangeRateResponse getRates(String source, String currencies) {
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
