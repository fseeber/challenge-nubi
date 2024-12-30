# challenge-nubi

# Currency Conversion API

Este proyecto es una API que permite convertir divisas utilizando datos de cambio en tiempo real de `exchangerate.host`.

## Requisitos

- Java 11 o superior
- Maven
- Clave de API de `exchangerate.host`

## Endpoints

### 1. **`/convertCurrency`** (POST)
Convierte una cantidad de una moneda base a una moneda destino.

#### Request Body (JSON)
```json
{
  "baseCurrency": "USD",
  "targetCurrency": "EUR",
  "amount": 100.0
}
Respuesta (JSON)
json
Copiar código
{
  "baseCurrency": "USD",
  "targetCurrency": "EUR",
  "amount": 100.0,
  "convertedAmount": 84.32,
  "timestamp": "2024-12-29 12:00:00 PM"
}
2. /liveRates (GET)
Obtiene las tasas de cambio actuales para diversas monedas.

Respuesta (JSON)
json
Copiar código
{
  "base": "USD",
  "rates": {
    "EUR": 0.84325,
    "GBP": 0.73855
  }
}
Configuración
Obten tu clave API en exchangerate.host y reemplaza en el archivo LiveResponseDemo.java:

java
Copiar código
public static final String ACCESS_KEY = "tu_clave_de_acceso";
Dependencias: Usa el siguiente fragmento en tu pom.xml:

xml
Copiar código
<dependency>
    <groupId>org.apache.httpcomponents</groupId>
    <artifactId>httpclient</artifactId>
    <version>4.5.13</version>
</dependency>
<dependency>
    <groupId>org.json</groupId>
    <artifactId>json</artifactId>
    <version>20210307</version>
</dependency>
Cómo ejecutar
Clona el repositorio y ejecuta el proyecto con Maven:

bash
Copiar código
mvn spring-boot:run
Realiza solicitudes a la API usando Postman o similar.

Ejemplo con Postman
1. Convertir divisas
Método: POST
URL: http://localhost:8080/convertCurrency
Cuerpo (JSON):
json
Copiar código
{
  "baseCurrency": "USD",
  "targetCurrency": "EUR",
  "amount": 100.0
}
