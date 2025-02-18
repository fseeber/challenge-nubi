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
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;

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

    /**
     * Convierte una cantidad de una moneda base a una moneda objetivo.
     * 
     * @param baseCurrency La moneda base (ej: "USD").
     * @param targetCurrency La moneda objetivo (ej: "EUR").
     * @param amount El monto a convertir.
     * @return El resultado de la conversión en un formato legible.
     * @throws IOException En caso de error de I/O.
     * @throws JSONException En caso de error al procesar el JSON.
     * @throws InvalidCurrencyException Si alguna de las monedas no es válida.
     * @throws InvalidAmountException Si el monto no es válido.
     * @throws ApiTimeoutException Si la API externa excede el tiempo de espera.
     */
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

            String url = String.format("%s%s?access_key=%s", baseUrl, endpoint, accessKey);
            logger.debug("URL de la API generada: {}", url);

            String response = restTemplate.getForObject(url, String.class);
            JSONObject exchangeRates = new JSONObject(response);

            Date timeStampDate = new Date((long) (exchangeRates.getLong("timestamp") * 1000));
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a");
            String formattedDate = dateFormat.format(timeStampDate);

            double conversionRate = exchangeRates.getJSONObject("quotes").getDouble(baseCurrency + targetCurrency);
            double convertedAmount = amount * conversionRate;
            String result = String.format("%.2f %s = %.2f %s (Fecha: %s)", amount, baseCurrency, convertedAmount, targetCurrency, formattedDate);

            logger.info("Conversión exitosa: {}", result);
            return result;
        
        } catch (HttpStatusCodeException e) {  
            logger.error("Error HTTP al obtener datos de la API externa: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("Error HTTP: " + e.getStatusCode());
        } catch (JSONException e) {
            logger.error("Error al procesar la respuesta JSON de la API: {}", e.getMessage(), e);
            throw new RuntimeException("Error al procesar el JSON: " + e.getMessage());
        } catch (ResourceAccessException  e) {
            logger.error("Error: La API externa no respondió a tiempo", e);
            throw new ApiTimeoutException("La API externa no respondió a tiempo.");
        } catch (Exception e) {
            logger.error("Error inesperado al realizar la conversión: {}", e.getMessage(), e);
            throw new RuntimeException("Error desconocido: " + e.getMessage());
        }
    }

    private boolean isValidCurrency(String currency) {
        List<String> validCurrencies = List.of("USD", "EUR", "ARS");
        return validCurrencies.contains(currency);
    }
}