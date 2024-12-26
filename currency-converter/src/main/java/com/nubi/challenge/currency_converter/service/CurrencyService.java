package com.nubi.challenge.currency_converter.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.nubi.challenge.currency_converter.model.ConversionRequest;

@Service
public class CurrencyService {

    //@Value("${currency.api.url}")
    //private String apiUrl;
    
    // essential URL structure is built using constants
    public static final String ACCESS_KEY = "b290a93160e4c6d9a8e09bc24b830acc";
    public static final String BASE_URL = "http://api.exchangerate.host/";
    public static final String ENDPOINT = "live";

    private final RestTemplate restTemplate;

    public CurrencyService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public double convertCurrency(ConversionRequest request) {
        String url = String.format("%s?base=%s&symbols=%s", BASE_URL + ENDPOINT + "?access_key=" + ACCESS_KEY, request.getFromCurrency(), request.getToCurrency());

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        String responseBody = response.getBody();

        // Lógica para parsear el JSON y obtener la tasa de cambio
        String exchangeRateString = responseBody.split(request.getToCurrency() + "\":")[1].split("}")[0];
        double exchangeRate = Double.parseDouble(exchangeRateString);

        return request.getAmount() * exchangeRate;
    }
}