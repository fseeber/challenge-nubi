package com.nubi.challenge.currency_converter.service;

public interface CurrentService {

    String convertCurrency(String baseCurrency, String targetCurrency, double amount) throws Exception;

}
