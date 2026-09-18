# Documentación Interactiva de la API - Swagger UI / OpenAPI

Esta API cuenta con documentación interactiva generada mediante **Springdoc-OpenAPI**, la cual expone todos los endpoints implementados, detallando los parámetros de entrada, esquemas DTO y códigos de respuesta HTTP.

---

## 🔗 Acceso a la Interfaz

Para navegar la documentación interactiva con la aplicación en ejecución:
* **Swagger UI:** `http://localhost:8080/swagger-ui/index.html`
* **Especificación OpenAPI (JSON):** `http://localhost:8080/v3/api-docs`

---

## 📌 Grupos de Endpoints Documentados

### 1. Catálogo de Productos (Integración DummyJSON)
Controlador encargado de consultar el catálogo expuesto por la API pública externa.

* **`GET /api/productos`**
  * **Descripción:** Obtiene la lista completa de productos mapeados al DTO interno (`ProductoDto`).
  * **Respuesta de Éxito:** `200 OK` (Devuelve arreglo JSON de productos).
* **`GET /api/productos/{id}`**
  * **Descripción:** Busca y devuelve el detalle de un producto específico según su ID.
  * **Respuestas:** `200 OK` (Producto encontrado) | `404 Not Found` (Producto inexistente).

---

### 2. Gestión de Favoritos (Persistencia en Memoria)
Controlador encargado de realizar las operaciones CRUD sobre la colección local en memoria RAM.

* **`POST /api/favoritos`**
  * **Descripción:** Registra un nuevo producto favorito en la colección.
  * **Validaciones:** `@NotNull` en `productoId`, `@NotBlank` y `@Size(max=100)` en `notaPersonal`.
  * **Respuestas:** `201 Created` | `400 Bad Request` (Error de validación).
* **`GET /api/favoritos`**
  * **Descripción:** Devuelve el listado completo de favoritos guardados en memoria.
  * **Respuesta:** `200 OK`.
* **`GET /api/favoritos/{id}`**
  * **Descripción:** Obtiene los detalles de un favorito por su ID.
  * **Respuestas:** `200 OK` | `404 Not Found`.
* **`PUT /api/favoritos/{id}`**
  * **Descripción:** Modifica los datos de un favorito existente.
  * **Respuestas:** `200 OK` | `400 Bad Request` | `404 Not Found`.
* **`DELETE /api/favoritos/{id}`**
  * **Descripción:** Remueve un favorito de la colección.
  * **Respuestas:** `204 No Content` | `404 Not Found`.

---

## 🛡️ Manejo Unificado de Respuestas de Error

Todos los endpoints responden bajo una estructura JSON uniforme ante fallos:

* **`400 Bad Request`**: Emitido al violar las reglas de Bean Validation. Retorna un mapa con el campo fallido y su motivo.
* **`404 Not Found`**: Capturado mediante `RecursoNoEncontradoException` al intentar acceder/modificar un elemento inexistente.
* **`500 Internal Server Error`**: Capturado ante fallos globales o interrupciones en la conexión con DummyJSON.
