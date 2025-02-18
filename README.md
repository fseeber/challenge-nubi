# Currency Converter API

## Descripción
La API de conversión de divisas permite convertir montos entre diferentes monedas, como USD, EUR y ARS. La aplicación recibe una solicitud con las monedas base y objetivo junto con el monto a convertir y devuelve el resultado de la conversión.

### Endpoints disponibles
- **POST /convert**
    - **Descripción**: Convierte el monto de una moneda base a una moneda objetivo.
    - **Parámetros de entrada (body)**:
      ```json
      {
        "baseCurrency": "USD",
        "targetCurrency": "EUR",
        "amount": 100
      }
      ```
    - **Respuestas**:
        - **200 OK**: Conversión exitosa.
        - **400 Bad Request**: Si el monto es inválido o las monedas no son válidas.
        - **500 Internal Server Error**: Si ocurre un error inesperado en el servidor.
        - **504 Gateway Timeout**: Si la API externa no responde a tiempo.

## Tecnologías utilizadas
- **Spring Boot**: Framework para el desarrollo de aplicaciones Java.
- **Log4j2**: Sistema de logging utilizado para la captura de logs en los niveles `INFO`, `WARN`, `DEBUG`, `ERROR`.
- **Swagger**: Documentación de la API y pruebas interactivas de los endpoints.

## Requisitos
- **Java 17**: Necesario para ejecutar la aplicación.
- **Maven**: Herramienta de construcción para gestionar dependencias y ejecutar la aplicación.

## Instalación y ejecución

### 1. Clonar el repositorio
Clona este repositorio a tu máquina local:
```bash
git clone https://github.com/tu_usuario/currency-converter.git
```
### 2. Compilar y ejecutar
Desde la raíz del proyecto, ejecuta el siguiente comando para compilar y ejecutar la aplicación:
```bash
git clone https://github.com/tu_usuario/currency-converter.git
```
### 3. Acceder a la API
Una vez que la aplicación esté en funcionamiento, puedes acceder a la API en la URL:
```bash
http://localhost:8080/convert
```
## Configuración
**Archivos de configuración**:
application.properties o application.yml:
api.access.key: La clave de acceso para la API externa de conversión de divisas.
api.base.url: La URL base de la API externa.
api.endpoint: El endpoint de la API externa utilizado para obtener las tasas de cambio.
```bash
Ejemplo de configuración en application.properties:

api.access.key=tu_clave_de_acceso
api.base.url=https://api.exchangerate-api.com/v4
api.endpoint=latest
```
