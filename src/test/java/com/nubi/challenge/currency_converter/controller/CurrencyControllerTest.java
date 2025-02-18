package com.nubi.challenge.currency_converter.controller;

import com.nubi.challenge.currency_converter.exception.ApiTimeoutException;
import com.nubi.challenge.currency_converter.exception.InvalidAmountException;
import com.nubi.challenge.currency_converter.exception.InvalidCurrencyException;
import com.nubi.challenge.currency_converter.service.CurrentService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(CurrencyController.class)
@AutoConfigureMockMvc(addFilters = false) // Desactiva los filtros de seguridad
class CurrencyControllerTest {

       @Autowired
       private MockMvc mockMvc;

       @MockBean
       private CurrentService currentService;

       @Test
       void testConvertCurrencySuccess() throws Exception {
              // Mock del servicio para un caso exitoso
              Mockito.when(currentService.convertCurrency("USD", "EUR", 100.0))
                     .thenReturn("200.00 EUR");

              // Realiza la solicitud y verifica la respuesta
              mockMvc.perform(post("/convert")
                     .contentType(MediaType.APPLICATION_JSON)
                     .content("{\"baseCurrency\":\"USD\",\"targetCurrency\":\"EUR\",\"amount\":100}"))
                     .andDo(print())
                     .andExpect(status().isOk())
                     .andExpect(content().string("200.00 EUR"));
       }

       @Test
       void testConvertCurrencyInvalidAmount() throws Exception {
              // Mock del servicio para lanzar una excepción de cantidad inválida
              Mockito.when(currentService.convertCurrency(Mockito.anyString(), Mockito.anyString(), Mockito.eq(-10.0)))
                     .thenThrow(new InvalidAmountException("La cantidad debe ser mayor a cero."));

              // Realiza la solicitud y verifica el manejo de la excepción
              mockMvc.perform(post("/convert")
                     .contentType(MediaType.APPLICATION_JSON)
                     .content("{\"baseCurrency\":\"USD\",\"targetCurrency\":\"EUR\",\"amount\":-10}"))
                     .andDo(print())
                     .andExpect(status().isBadRequest())
                     .andExpect(content().string("La cantidad debe ser mayor a cero."));
       }

       @Test
       void testConvertCurrencyInvalidCurrency() throws Exception {
              // Mock del servicio para lanzar una excepción de moneda inválida
              Mockito.when(currentService.convertCurrency("XYZ", "EUR", 100.0))
                     .thenThrow(new InvalidCurrencyException("La moneda XYZ no es válida."));

              // Realiza la solicitud y verifica el manejo de la excepción
              mockMvc.perform(post("/convert")
                     .contentType(MediaType.APPLICATION_JSON)
                     .content("{\"baseCurrency\":\"XYZ\",\"targetCurrency\":\"EUR\",\"amount\":100}"))
                     .andDo(print())
                     .andExpect(status().isBadRequest())
                     .andExpect(content().string("La moneda XYZ no es válida."));
       }

       @Test
       void testConvertCurrencyApiTimeout() throws Exception {
       Mockito.when(currentService.convertCurrency("USD", "EUR", 100.0))
              .thenThrow(new ApiTimeoutException("Error de tiempo de espera: La API externa no respondió a tiempo."));

       mockMvc.perform(post("/convert")
              .contentType(MediaType.APPLICATION_JSON)
              .content("{\"baseCurrency\":\"USD\",\"targetCurrency\":\"EUR\",\"amount\":100}"))
              .andDo(print())
              .andExpect(status().isGatewayTimeout())  // Verificamos el 504
              .andExpect(content().string("Error de tiempo de espera: La API externa no respondió a tiempo."));
       }

       @Test
       void testConvertCurrencyInternalError() throws Exception {
              // Mock del servicio para lanzar una excepción genérica
              Mockito.when(currentService.convertCurrency("USD", "EUR", 100.0))
                     .thenThrow(new RuntimeException("Error inesperado."));

              // Realiza la solicitud y verifica el manejo del error interno
              mockMvc.perform(post("/convert")
                     .contentType(MediaType.APPLICATION_JSON)
                     .content("{\"baseCurrency\":\"USD\",\"targetCurrency\":\"EUR\",\"amount\":100}"))
                     .andDo(print())
                     .andExpect(status().isInternalServerError())
                     .andExpect(content().string("Error interno del servidor."));
       }
}
