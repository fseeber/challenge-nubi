package com.nubi.challenge.currency_converter.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;

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
    
    private static final Logger logger = LoggerFactory.getLogger(CurrencyController.class);

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
            logger.debug("Resultado de conversión: {}", conversionResult);
            return ResponseEntity.ok(conversionResult);
        } catch (InvalidAmountException e) {
            logger.warn("Monto inválido proporcionado: {}", request.getAmount(), e);
            return ResponseEntity.status(400).body(e.getMessage());  
        } catch (InvalidCurrencyException e) {
            logger.warn("Moneda inválida: {} o {}", request.getBaseCurrency(), request.getTargetCurrency(), e);
            return ResponseEntity.status(400).body(e.getMessage());  
        } catch (ApiTimeoutException e) {
            logger.error("Tiempo de espera agotado al intentar obtener la tasa de cambio", e); 
            return ResponseEntity.status(504).body("Error de tiempo de espera: La API externa no respondió a tiempo.");
        } catch (HttpStatusCodeException e) {
            if (e.getStatusCode().is4xxClientError()) {
                logger.error("Solicitud inválida (400): {}", e.getResponseBodyAsString());
                return ResponseEntity.status(400).body("Solicitud inválida. Detalles: " + e.getResponseBodyAsString());  
            }
            logger.error("Error HTTP al obtener datos de la API externa: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            return ResponseEntity.status(500).body("Error al procesar la conversión.");
        } catch (Exception e) {
            logger.error("Error inesperado al procesar la conversión: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("Error interno del servidor.");
        }
    }
}