package com.example.exchange_rate_chal.controller;

import com.example.exchange_rate_chal.model.dto.ConversionResponse;
import com.example.exchange_rate_chal.model.dto.RateResponse;
import com.example.exchange_rate_chal.service.ExchangeRateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ExchangeRateController {
    private final ExchangeRateService service;

    @Operation(
            summary = "Get exchange rate",
            description = "Retrieve the exchange rate from a source currency to a target currency."
    )
    @GetMapping("/rates/{from}/{to}")
    public ResponseEntity<RateResponse> getRate(
            @Parameter(description = "Source currency", example = "EUR")
            @PathVariable String from,
            @Parameter(description = "Target currency", example = "USD")
            @PathVariable String to) {

        return ResponseEntity.ok(service.getRate(from, to));
    }

    @Operation(
            summary = "Get all exchange rates",
            description = "Retrieve exchange rates from a source currency to all available target currencies."
    )
    @GetMapping("/rates/{from}")
    public ResponseEntity<List<RateResponse>> getAllRates(
            @Parameter(description = "Source currency", example = "EUR")
            @PathVariable String from) {

        return ResponseEntity.ok(service.getAllRates(from));
    }

    @Operation(
            summary = "Convert currency",
            description = "Convert a specified amount from a source currency to a target currency."
    )
    @GetMapping("/convert/{from}/{to}")
    public ResponseEntity<ConversionResponse> convert(
            @Parameter(description = "Source currency", example = "EUR")
            @PathVariable String from,
            @Parameter(description = "Target currency", example = "USD")
            @PathVariable String to,
            @Parameter(description = "Value", example = "100.0")
            @RequestParam double amount) {

        return ResponseEntity.ok(service.convert(from, to, amount));
    }

    @Operation(
            summary = "Convert multiple currencies",
            description = "Convert a specified amount from a source currency to multiple target currencies in a single request."
    )
    @GetMapping("/convert/{from}/currencies")
    public ResponseEntity<List<ConversionResponse>> convertSuppliedCurrencies(
            @Parameter(description = "Source currency", example = "EUR")
            @PathVariable String from,
            @Parameter(description = "Value", example = "100.0")
            @RequestParam double amount,
            @Parameter(description = "Lista de moedas de destino", example = "USD,GBP,JPY")
            @RequestParam List<String> targets) {

        return ResponseEntity.ok(service.convertSuppliedCurrencies(from, targets, amount));
    }
}
