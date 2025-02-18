package com.nubi.challenge.currency_converter.service;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.nubi.challenge.currency_converter.exception.ApiTimeoutException;
import com.nubi.challenge.currency_converter.exception.InvalidAmountException;
import com.nubi.challenge.currency_converter.exception.InvalidCurrencyException;

@Service
public class CurrentServiceImpl implements CurrentService {

    private static final Logger logger = LoggerFactory.getLogger(CurrentServiceImpl.class);

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
//TODO javadoc
    public String convertCurrency(String baseCurrency, String targetCurrency, double amount) throws IOException, JSONException {
        logger.info("Iniciando conversión de moneda desde {} a {} con un monto de {}", baseCurrency, targetCurrency, amount);

        try {
            if (!isValidCurrency(baseCurrency) || !isValidCurrency(targetCurrency)) {
                logger.warn("Moneda no válida: {} o {}", baseCurrency, targetCurrency);
                throw new InvalidCurrencyException("Moneda no válida: " + baseCurrency + " o " + targetCurrency);
            }
            
            if (amount <= 0) {
                logger.warn("Monto inválido: {}", amount);
                throw new InvalidAmountException("La cantidad debe ser mayor a cero: " + amount);
            }

            String url = String.format("%s%s?access_key=%s", baseUrl, "live", accessKey);
            logger.debug("URL de la API generada: {}", url);

            String response = restTemplate.getForObject(url, String.class);
            JSONObject exchangeRates = new JSONObject(response);

            Date timeStampDate = new Date((long) (exchangeRates.getLong("timestamp") * 1000));
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a");
            String formattedDate = dateFormat.format(timeStampDate);
            
            logger.debug("Tasa de cambio obtenida: {} a {}", baseCurrency + targetCurrency, exchangeRates.getJSONObject("quotes").getDouble(baseCurrency + targetCurrency));

            double convertedAmount = amount * exchangeRates.getJSONObject("quotes").getDouble(baseCurrency + targetCurrency);
            String result = String.format("%.2f %s = %.2f %s (Fecha: %s)", amount, baseCurrency, convertedAmount, targetCurrency, formattedDate);

            logger.info("Conversión exitosa: {}", result);

            return result;
        
        } catch (Exception e) {
            if (e.getCause() instanceof java.net.SocketTimeoutException) {
                logger.error("Error: La API externa no respondió a tiempo", e);
                throw new ApiTimeoutException("La API externa no respondió a tiempo.");
            }

            logger.error("Error inesperado al realizar la conversión: {}", e.getMessage(), e);
            throw new RuntimeException("Error desconocido: " + e.getMessage());
        }
    }    

    private boolean isValidCurrency(String currency) {
        List<String> validCurrencies = List.of("USD", "EUR", "ARS");
        return validCurrencies.contains(currency);
    }
}
