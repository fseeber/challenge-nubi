package com.nubi.challenge.currency_converter.service;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.nubi.challenge.currency_converter.exception.ApiTimeoutException;
import com.nubi.challenge.currency_converter.exception.InvalidAmountException;
import com.nubi.challenge.currency_converter.exception.InvalidCurrencyException;

@Service
public class CurrentServiceImpl implements CurrentService {

    @Value("${api.access.key}")
    private String accessKey;

    @Value("${api.base.url}")
    private String baseUrl;

    @Value("${api.endpoint}")
    private String endpoint;
    
    private final RestTemplate restTemplate;

    public CurrentServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String convertCurrency(String baseCurrency, String targetCurrency, double amount) throws IOException, JSONException {

        try {
            if (!isValidCurrency(baseCurrency) || !isValidCurrency(targetCurrency)) {
                throw new InvalidCurrencyException("Moneda no válida: " + baseCurrency + " o " + targetCurrency);
            }
            if (amount <= 0) {
                throw new InvalidAmountException("La cantidad debe ser mayor a cero: " + amount);
            }
            
            String url = String.format("%s%s?access_key=%s", baseUrl, "live", accessKey);
            String response = restTemplate.getForObject(url, String.class);
            JSONObject exchangeRates = new JSONObject(response);
    
            Date timeStampDate = new Date((long) (exchangeRates.getLong("timestamp") * 1000));
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a");
            String formattedDate = dateFormat.format(timeStampDate);
    
            System.out.println(amount+": " + exchangeRates.getString("source") + " in "+targetCurrency+" : " + exchangeRates.getJSONObject("quotes").getDouble(baseCurrency+targetCurrency) + " (Date: " + formattedDate + ")");
            System.out.println("\n");
    
            return String.format("%.2f %s = %s %s (Date: %s)", amount, baseCurrency, targetCurrency, amount*(exchangeRates.getJSONObject("quotes").getDouble(baseCurrency+targetCurrency)), formattedDate);
        
        } catch (Exception e) {
            if (e.getCause() instanceof java.net.SocketTimeoutException) {
                throw new ApiTimeoutException("La API externa no respondió a tiempo.");
            }
            throw new RuntimeException("Error desconocido: " + e.getMessage());
        }
    }    

    private boolean isValidCurrency(String currency) {
        // Validar si la moneda está en una lista permitida
        List<String> validCurrencies = List.of("USD", "EUR", "ARS");
        return validCurrencies.contains(currency);
    }
}