package com.nubi.challenge.currency_converter.controller;

import com.nubi.challenge.currency_converter.service.CurrentService;
import com.nubi.challenge.currency_converter.model.CurrencyConversionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/convert")
public class CurrencyController {

    @Autowired
    private CurrentService currencyService;

    // Endpoint para convertir divisas con POST
    @PostMapping
    public ResponseEntity<String> convertCurrency(@RequestBody CurrencyConversionRequest request) {
        try {
            // Llamamos al servicio que realiza la conversión
            String conversionResult = currencyService.convertCurrency(request.getBaseCurrency(), request.getTargetCurrency(), request.getAmount());
            // Devolvemos el resultado de la conversión
            return ResponseEntity.ok(conversionResult);
        } catch (Exception e) {
            // En caso de error, retornamos un mensaje adecuado
            return ResponseEntity.status(500).body("Error en la conversión: " + e.getMessage());
        }
    }
}