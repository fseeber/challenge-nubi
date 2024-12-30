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

## Request Body (JSON)
{
  "baseCurrency": "USD",
  "targetCurrency": "EUR",
  "amount": 100.0
}

## Response (JSON)
{
  "baseCurrency": "USD",
  "targetCurrency": "EUR",
  "amount": 100.0,
  "convertedAmount": 84.32,
  "timestamp": "2024-12-29 12:00:00 PM"
}

#### Configuración
Obten tu clave API en exchangerate.host y reemplaza en el archivo LiveResponseDemo.java:


#### Cómo ejecutar
Clona el repositorio y ejecuta el proyecto con Maven:

```bash
mvn spring-boot:run

Realiza solicitudes a la API usando Postman o similar.

#### Ejemplop Postman
1. Convertir divisas
Método: POST
URL: http://localhost:8080/convertCurrency
Cuerpo (JSON):
```json
{
  "baseCurrency": "USD",
  "targetCurrency": "EUR",
  "amount": 100.0
}
