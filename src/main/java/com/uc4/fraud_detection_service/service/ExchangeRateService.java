package com.uc4.fraud_detection_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class ExchangeRateService {

    private final WebClient webClient;

    @Autowired
    public ExchangeRateService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.exchangeratesapi.io").build();
    }

    @Cacheable("exchangeRates")
    public Map<String, Double> getExchangeRates() {
        return fetchExchangeRates();
    }

    @Scheduled(fixedRate = 3600000) // Refresh every hour
    public void refreshExchangeRates() {
        fetchExchangeRates();
    }

    private Map<String, Double> fetchExchangeRates() {
        return this.webClient.get()
                .uri("/latest")
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }
}
