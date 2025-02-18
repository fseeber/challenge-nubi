package com.nubi.challenge.currency_converter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.json.JSONException;
import org.json.JSONObject;

public class LiveResponseDemo {

    public static final String ACCESS_KEY = "b290a93160e4c6d9a8e09bc24b830acc";
    public static final String BASE_URL = "http://api.exchangerate.host/";
    public static final String ENDPOINT = "live";
    
    static RestTemplate restTemplate = new RestTemplate();

    static Set<String> validCurrencies = new HashSet<String>();

    static {
        validCurrencies.add("USD");
        validCurrencies.add("EUR");
        validCurrencies.add("ARS");
        validCurrencies.add("GBP");
        validCurrencies.add("JPY");
    }

    public void sendLiveRequest() {
        String url = BASE_URL + ENDPOINT + "?access_key=" + ACCESS_KEY;
        System.out.println("URL de la solicitud: " + url); 

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            JSONObject exchangeRates = new JSONObject(response.getBody());

            System.out.println("Live Currency Exchange Rates");

            String baseCurrency;
            while (true) {
                System.out.print("Ingrese la moneda base (ejemplo: USD): ");
                baseCurrency = reader.readLine().trim().toUpperCase();
                if (validCurrencies.contains(baseCurrency)) {
                    break;  
                } else {
                    System.out.println("Moneda base no válida. Por favor ingrese una moneda válida como: USD, EUR, ARS.");
                }
            }

            String targetCurrency;
            while (true) {
                System.out.print("Ingrese la moneda destino (ejemplo: EUR): ");
                targetCurrency = reader.readLine().trim().toUpperCase();
                if (validCurrencies.contains(targetCurrency)) {
                    break;  
                } else {
                    System.out.println("Moneda destino no válida. Por favor ingrese una moneda válida como: USD, EUR, ARS.");
                }
            }

            double amount = 0;
            while (true) {
                try {
                    System.out.print("Ingrese la cantidad a convertir: ");
                    amount = Double.parseDouble(reader.readLine().trim());
                    if (amount > 0) {
                        break;  
                    } else {
                        System.out.println("La cantidad debe ser mayor a cero.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Cantidad inválida, por favor ingrese un número válido.");
                }
            }

            Date timeStampDate = new Date((long) (exchangeRates.getLong("timestamp") * 1000));
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a");
            String formattedDate = dateFormat.format(timeStampDate);

            double conversionRate = exchangeRates.getJSONObject("quotes").getDouble(baseCurrency + targetCurrency);
            System.out.println(amount + ": " + exchangeRates.getString("source") + " en " + targetCurrency + " : "
                    + conversionRate + " (Fecha: " + formattedDate + ")");
            System.out.println("\n");

        } catch (IOException | JSONException e) {
            System.out.println("Error al procesar la solicitud: " + e.getMessage());
        }
    }

    public static void main(String[] args) throws IOException {
        LiveResponseDemo demo = new LiveResponseDemo();
        demo.sendLiveRequest();
    }
}
