package com.nubi.challenge.currency_converter.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Api;

@RestController
@RequestMapping("/api/log-test")
@Api(value = "Log Test", description = "Endpoint para probar generación de logs")
public class LogTestController {

    private static final Logger logger = LoggerFactory.getLogger(LogTestController.class);

    @ApiOperation(value = "Generar logs de prueba", notes = "Este endpoint genera logs en diferentes niveles: INFO, DEBUG, ERROR.")
    @GetMapping
    public String testLogging() {
        logger.info("INFO: Log de prueba ejecutado correctamente.");
        logger.debug("DEBUG: Este es un mensaje de debug.");
        logger.error("ERROR: Simulación de un error para prueba.");
        return "Los logs fueron generados. Verifica en Kibana o en los archivos de logs.";
    }
}