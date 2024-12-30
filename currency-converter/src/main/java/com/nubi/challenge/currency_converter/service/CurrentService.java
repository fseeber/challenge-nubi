package com.nubi.challenge.currency_converter.service;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class CurrentService {

    // Access key and URL for API
    private static final String ACCESS_KEY = "b290a93160e4c6d9a8e09bc24b830acc";
    private static final String BASE_URL = "http://api.exchangerate.host/";
    private static final String ENDPOINT = "live";

    private final CloseableHttpClient httpClient;

    public CurrentService() {
        this.httpClient = HttpClients.createDefault();
    }

    // Método para obtener la tasa de cambio
    public String convertCurrency(String baseCurrency, String targetCurrency, double amount) throws IOException, JSONException {

        // Build the request URL
        HttpGet get = new HttpGet(BASE_URL + ENDPOINT + "?access_key=" + ACCESS_KEY);
        CloseableHttpResponse response = httpClient.execute(get);
        HttpEntity entity = response.getEntity();

        // Parse the JSON response
        JSONObject exchangeRates = new JSONObject(EntityUtils.toString(entity));

        // Get the exchange rate and calculate the result
        //double rate = exchangeRates.getJSONObject("rates").getDouble(targetCurrency);

        // Formatting the timestamp
        Date timeStampDate = new Date((long) (exchangeRates.getLong("timestamp") * 1000));
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a");
        String formattedDate = dateFormat.format(timeStampDate);

        System.out.println(amount+": " + exchangeRates.getString("source") + " in "+targetCurrency+" : " + exchangeRates.getJSONObject("quotes").getDouble(baseCurrency+targetCurrency) + " (Date: " + formattedDate + ")");
        System.out.println("\n");
        response.close();
        //double result = rate * amount;

        // Return the result as a string
        return String.format("%.2f %s = %s (Date: %s)", amount, baseCurrency, targetCurrency, formattedDate);
    }

    public void closeClient() throws IOException {
        httpClient.close();
    }
}