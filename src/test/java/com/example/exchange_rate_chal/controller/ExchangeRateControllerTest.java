package com.example.exchange_rate_chal.controller;

import com.example.exchange_rate_chal.model.dto.ConversionResponse;
import com.example.exchange_rate_chal.model.dto.RateResponse;
import com.example.exchange_rate_chal.security.AuthTokenFilter;
import com.example.exchange_rate_chal.service.ExchangeRateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ExchangeRateController.class,
            excludeFilters = @ComponentScan.Filter(
                    type = FilterType.ASSIGNABLE_TYPE,
                    classes = AuthTokenFilter.class
            ))
public class ExchangeRateControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ExchangeRateService service;

    @Test
    void getRate_whenCurrencyIsValid_thenReturnsSuccessfully() throws Exception {
        RateResponse mockRate = RateResponse.builder()
                .from("EUR")
                .to("USD")
                .rate(1.14)
                .timestamp(1773324845L)
                .build();

        when(service.getRate("EUR", "USD")).thenReturn(mockRate);

        mockMvc.perform(get("/api/rates/EUR/USD"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.from").value("EUR"))
                .andExpect(jsonPath("$.to").value("USD"))
                .andExpect(jsonPath("$.rate").value(1.14))
                .andExpect(jsonPath("$.timestamp").value(1773324845L));
    }

    @Test
    void getAllRates_whenCurrenciesAreValid_thenReturnsListSuccessfully() throws Exception {
        List<RateResponse> mockRates = List.of(
                RateResponse.builder().from("EUR").to("USD").rate(1.14).timestamp(1773324845L).build(),
                RateResponse.builder().from("EUR").to("GBP").rate(0.86).timestamp(1773324845L).build()
        );

        when(service.getAllRates("EUR")).thenReturn(mockRates);

        mockMvc.perform(get("/api/rates/EUR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].from").value("EUR"))
                .andExpect(jsonPath("$[0].to").value("USD"))
                .andExpect(jsonPath("$[1].to").value("GBP"));
    }

    @Test
    void convert_whenAmountIsValid_thenReturnsSuccessfully() throws Exception {
        ConversionResponse mockConversion = ConversionResponse.builder()
                .from("EUR")
                .to("USD")
                .amount(100.0)
                .rate(1.14)
                .result(114.0)
                .build();

        when(service.convert("EUR", "USD", 100.0)).thenReturn(mockConversion);

        mockMvc.perform(get("/api/convert/EUR/USD")
                        .param("amount", "100.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.from").value("EUR"))
                .andExpect(jsonPath("$.to").value("USD"))
                .andExpect(jsonPath("$.amount").value(100.0))
                .andExpect(jsonPath("$.rate").value(1.14))
                .andExpect(jsonPath("$.result").value(114.0));
    }

    @Test
    void convertSuppliedCurrencies_whenCurrenciesAreProvided_thenReturnListSuccessfully() throws Exception {
        List<ConversionResponse> mockConversions = List.of(
                ConversionResponse.builder().from("EUR").to("USD").amount(100.0).rate(1.14).result(114.0).build(),
                ConversionResponse.builder().from("EUR").to("GBP").amount(100.0).rate(0.86).result(86.0).build()
        );

        when(service.convertSuppliedCurrencies("EUR", List.of("USD", "GBP"), 100.0))
                .thenReturn(mockConversions);

        mockMvc.perform(get("/api/convert/EUR/currencies")
                        .param("amount", "100.0")
                        .param("targets", "USD", "GBP"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].to").value("USD"))
                .andExpect(jsonPath("$[1].to").value("GBP"));
    }

    @Test
    void convert_whenAmountIsNotValid_thenReturnsError() throws Exception {
        mockMvc.perform(get("/api/convert/EUR/USD"))
                .andExpect(status().isBadRequest());
    }
}
