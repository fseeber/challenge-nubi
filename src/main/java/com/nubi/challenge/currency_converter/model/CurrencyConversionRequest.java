package com.nubi.challenge.currency_converter.model;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CurrencyConversionRequest {
    private double amount;
    private String baseCurrency;
    private String targetCurrency;
}