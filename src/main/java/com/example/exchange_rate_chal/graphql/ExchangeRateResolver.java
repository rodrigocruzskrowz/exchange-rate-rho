package com.example.exchange_rate_chal.graphql;

import com.example.exchange_rate_chal.model.dto.ConversionResponse;
import com.example.exchange_rate_chal.model.dto.RateResponse;
import com.example.exchange_rate_chal.service.ExchangeRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ExchangeRateResolver {
    private final ExchangeRateService service;

    @QueryMapping
    public RateResponse getRate(@Argument String from, @Argument String to) {
        return service.getRate(from, to);
    }

    @QueryMapping
    public List<RateResponse> getAllRates(@Argument String from) {
        return service.getAllRates(from);
    }

    @QueryMapping
    public ConversionResponse convert(@Argument String from, @Argument String to, @Argument double amount) {
        return service.convert(from, to, amount);
    }

    @QueryMapping
    public List<ConversionResponse> convertSuppliedCurrencies(@Argument String from, @Argument List<String> targets,
            @Argument double amount) {
        return service.convertSuppliedCurrencies(from, targets, amount);
    }

}
