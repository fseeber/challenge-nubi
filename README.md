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

<<<<<<< HEAD
#### Request Body (JSON)
```json
=======
## Response (JSON)
>>>>>>> 98d325d93aa6d2dc705cfc140fa8b19570f42f60
{
  "baseCurrency": "USD",
  "targetCurrency": "EUR",
  "amount": 100.0,
  "convertedAmount": 84.32,
  "timestamp": "2024-12-29 12:00:00 PM"
}
<<<<<<< HEAD
####2. /liveRates (GET)
Obtiene las tasas de cambio actuales para diversas monedas.

####Respuesta (JSON)
```json
Copiar código
{
  "base": "USD",
  "rates": {
    "EUR": 0.84325,
    "GBP": 0.73855
  }
}
####Configuración
Obten tu clave API en exchangerate.host y reemplaza en el archivo LiveResponseDemo.java:


####Cómo ejecutar
Clona el repositorio y ejecuta el proyecto con Maven:

```bash
=======

### Configuración
Obten tu clave API en exchangerate.host y reemplaza en el archivo LiveResponseDemo.java:

### Cómo ejecutar
Clona el repositorio y ejecuta el proyecto con Maven:

>>>>>>> 98d325d93aa6d2dc705cfc140fa8b19570f42f60
mvn spring-boot:run

Realiza solicitudes a la API usando Postman o similar.

<<<<<<< HEAD
####Ejemplo con Postman
=======
### Ejemplop Postman
>>>>>>> 98d325d93aa6d2dc705cfc140fa8b19570f42f60
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
