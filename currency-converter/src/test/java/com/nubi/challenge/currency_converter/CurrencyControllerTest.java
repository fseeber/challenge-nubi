package com.nubi.challenge.currency_converter;

import com.nubi.challenge.currency_converter.exception.ApiTimeoutException;
import com.nubi.challenge.currency_converter.exception.InvalidAmountException;
import com.nubi.challenge.currency_converter.service.CurrentService;
import com.nubi.challenge.currency_converter.controller.CurrencyController;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CurrencyController.class)
class CurrencyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CurrentService currentService;

    @Test
    void testConvertCurrencySuccess() throws Exception {
        Mockito.when(currentService.convertCurrency("USD", "EUR", 100.0))
               .thenReturn("200.00 EUR");

        mockMvc.perform(post("/convert")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"baseCurrency\":\"USD\",\"targetCurrency\":\"EUR\",\"amount\":100}"))
                .andExpect(status().isOk())
                .andExpect(content().string("200.00 EUR"));
    }

    @Test
    void testConvertCurrencyInvalidAmount() throws Exception {
        Mockito.when(currentService.convertCurrency(Mockito.anyString(), Mockito.anyString(), Mockito.eq(-10.0)))
               .thenThrow(new InvalidAmountException("La cantidad debe ser mayor a cero."));

        mockMvc.perform(post("/convert")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"baseCurrency\":\"USD\",\"targetCurrency\":\"EUR\",\"amount\":-10}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("La cantidad debe ser mayor a cero."));
    }

}