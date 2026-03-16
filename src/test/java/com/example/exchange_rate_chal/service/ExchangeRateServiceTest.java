package com.example.exchange_rate_chal.service;

import com.example.exchange_rate_chal.client.ExchangeRateClient;
import com.example.exchange_rate_chal.model.dto.ConversionResponse;
import com.example.exchange_rate_chal.model.dto.ConvertCurrencyResponse;
import com.example.exchange_rate_chal.model.dto.ExchangeRateResponse;
import com.example.exchange_rate_chal.model.dto.RateResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExchangeRateServiceTest {
    @Mock
    private ExchangeRateClient client;

    @InjectMocks
    private ExchangeRateService service;

    private ExchangeRateResponse mockExternalRateResponse;
    private ConvertCurrencyResponse mockExternalConversionResponse;

    @BeforeEach
    void setUp() {
        mockExternalRateResponse = new ExchangeRateResponse();
        mockExternalRateResponse.setSuccess(true);
        mockExternalRateResponse.setSource("EUR");
        mockExternalRateResponse.setTimestamp(1773324845L);
        mockExternalRateResponse.setQuotes(new HashMap<>(Map.of(
                "EURUSD", 1.14,
                "EURGBP", 0.86,
                "EURJPY", 182.41
        )));

        ConvertCurrencyResponse.QueryInfo query = new ConvertCurrencyResponse.QueryInfo();
        query.setFrom("EUR");
        query.setTo("USD");
        query.setAmount(100.0);

        ConvertCurrencyResponse.Info info = new ConvertCurrencyResponse.Info();
        info.setRate(1.14);

        mockExternalConversionResponse = new ConvertCurrencyResponse();
        mockExternalConversionResponse.setSuccess(true);
        mockExternalConversionResponse.setResult(114.0);
        mockExternalConversionResponse.setInfo(info);
        mockExternalConversionResponse.setQuery(query);
    }

    @Test
    void getRate_whenCurrenciesAreValid_thenReturnsExchangeRate() {
        when(client.getRates("EUR", "USD")).thenReturn(mockExternalRateResponse);

        RateResponse result = service.getRate("EUR", "USD");

        assertThat(result.getFrom()).isEqualTo("EUR");
        assertThat(result.getTo()).isEqualTo("USD");
        assertThat(result.getRate()).isEqualTo(1.14);
        assertThat(result.getTimestamp()).isEqualTo(1773324845L);

        verify(client, times(1)).getRates("EUR", "USD");
    }

    @Test
    void getRate_whenCurrenciesAreLowercase_thenConvertsToUppercase() {
        when(client.getRates("eur", "usd")).thenReturn(mockExternalRateResponse);

        RateResponse result = service.getRate("eur", "usd");

        assertThat(result.getFrom()).isEqualTo("EUR");
        assertThat(result.getTo()).isEqualTo("USD");
    }

    @Test
    void getAllRates_whenCurrencyIsValid_thenReturnsExchangeRateList() {
        when(client.getRates("EUR", null)).thenReturn(mockExternalRateResponse);

        List<RateResponse> result = service.getAllRates("EUR");

        assertThat(result).hasSize(3);
        assertThat(result).extracting(RateResponse::getFrom)
                .containsOnly("EUR");
        assertThat(result).extracting(RateResponse::getTo)
                .containsExactlyInAnyOrder("USD", "GBP", "JPY");

        verify(client, times(1)).getRates("EUR", null);
    }

    @Test
    void convert_whenAmountIsValid_thenReturnsConversionResult() {
        when(client.convert("EUR", "USD", 100.0)).thenReturn(mockExternalConversionResponse);

        ConversionResponse result = service.convert("EUR", "USD", 100.0);

        assertThat(result.getFrom()).isEqualTo("EUR");
        assertThat(result.getTo()).isEqualTo("USD");
        assertThat(result.getAmount()).isEqualTo(100.0);
        assertThat(result.getRate()).isEqualTo(1.14);
        assertThat(result.getResult()).isEqualTo(114.0);

        verify(client, times(1)).convert("EUR", "USD", 100.0);
    }

    @Test
    void convertSuppliedCurrencies_whenCurrenciesAreSupplied_thenReturnsList() {
        when(client.getRates("EUR", "USD,GBP,JPY")).thenReturn(mockExternalRateResponse);

        List<ConversionResponse> result = service.convertSuppliedCurrencies("EUR", List.of("USD", "GBP", "JPY"), 100.0);

        assertThat(result).hasSize(3);
        assertThat(result).extracting(ConversionResponse::getFrom)
                .containsOnly("EUR");
        assertThat(result).extracting(ConversionResponse::getTo)
                .containsExactlyInAnyOrder("USD", "GBP", "JPY");

        verify(client, times(1)).getRates("EUR", "USD,GBP,JPY");
    }

    @Test
    void convertSuppliedCurrencies_whenCurrenciesAreSupplied_thenReturnsConversionResults() {
        when(client.getRates("EUR", "USD,GBP")).thenReturn(mockExternalRateResponse);

        List<ConversionResponse> result = service.convertSuppliedCurrencies(
                "EUR",
                List.of("USD", "GBP"),
                100.0
        );

        ConversionResponse eurConversion = result.stream()
                .filter(c -> c.getTo().equals("USD"))
                .findFirst()
                .orElseThrow();

        assertThat(eurConversion.getResult())
                .isEqualTo(eurConversion.getAmount() * eurConversion.getRate());
    }
}
