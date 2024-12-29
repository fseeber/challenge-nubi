package com.nubi.challenge.currency_converter.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/log-test")
public class LogTestController {

    private static final Logger logger = LoggerFactory.getLogger(LogTestController.class);

    @GetMapping
    public String testLogging() {
        logger.info("INFO: Log de prueba ejecutado correctamente.");
        logger.debug("DEBUG: Este es un mensaje de debug.");
        logger.error("ERROR: Simulación de un error para prueba.");
        return "Los logs fueron generados. Verifica en Kibana o en los archivos de logs.";
    }
}