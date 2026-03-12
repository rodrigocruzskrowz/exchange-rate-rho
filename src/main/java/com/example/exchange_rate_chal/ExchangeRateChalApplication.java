package com.example.exchange_rate_chal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ExchangeRateChalApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExchangeRateChalApplication.class, args);
	}

}
