package com.nubi.challenge.currency_converter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nubi.challenge.currency_converter.exception.ApiTimeoutException;
import com.nubi.challenge.currency_converter.exception.InvalidAmountException;
import com.nubi.challenge.currency_converter.exception.InvalidCurrencyException;
import com.nubi.challenge.currency_converter.model.CurrencyConversionRequest;
import com.nubi.challenge.currency_converter.service.CurrentService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/convert")
@Api(value = "Convert", description = "Endpoint para conversión de monedas")
public class CurrencyController {

    @Autowired
    private CurrentService currencyService;
 
    @ApiOperation(value = "Convertir monedas USD/EUR, USD/ARS", notes = "Este endpoint genera una tasa de cambio a partir de dos monedas x ej: USD/ARS, USD/EUR")
    @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Conversión realizada exitosamente"),
    @ApiResponse(code = 400, message = "Solicitud inválida"),
    @ApiResponse(code = 500, message = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<String> convertCurrency(@RequestBody CurrencyConversionRequest request) {
        try {
            String conversionResult = currencyService.convertCurrency(request.getBaseCurrency(), request.getTargetCurrency(), request.getAmount());
            return ResponseEntity.ok(conversionResult);
        }catch (InvalidAmountException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }catch (InvalidCurrencyException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }catch (ApiTimeoutException e) {
            return ResponseEntity.status(504).body(e.getMessage()); 
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error en la conversión: " + e.getMessage());
        }
    }
}