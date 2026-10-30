# API Web II

API REST desarrollada con Java 21 y Spring Boot 3. Provee autenticación con JWT, gestión de usuarios, listas y favoritos, y consulta de productos mediante DummyJSON. PostgreSQL almacena usuarios, listas y favoritos; Flyway administra el esquema.

## Requisitos

- JDK 21.
- PostgreSQL en ejecución.
- Docker, opcional, para ejecutar la aplicación en un contenedor.

El proyecto Maven está dentro de `api-blank/`. El wrapper incluido (`mvnw`) descarga y utiliza la versión de Maven definida por el proyecto; no hace falta instalar Maven por separado. La primera compilación requiere conexión a Internet para descargar Maven y las dependencias.

## Configuración de PostgreSQL

La aplicación local espera por defecto una base llamada `webii_tp2`, el usuario `webii_tp2` y PostgreSQL en `localhost:5432`. Crear el usuario y la base de datos en PostgreSQL. Reemplazar la contraseña de ejemplo por una contraseña propia:

```sql
CREATE ROLE webii_tp2 WITH LOGIN PASSWORD 'elegir-una-contrasena';
CREATE DATABASE webii_tp2 OWNER webii_tp2;
```

Configurar la contraseña en el entorno de la terminal desde la que se iniciará la aplicación:

```sh
export SPRING_DATASOURCE_PASSWORD='la-misma-contrasena'
```

Spring Boot usa esa variable para sobrescribir la contraseña de conexión. También se pueden configurar otros valores mediante variables de entorno:

| Variable | Valor local predeterminado | Uso |
| --- | --- | --- |
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://localhost:5432/webii_tp2` | URL JDBC de PostgreSQL |
| `SPRING_DATASOURCE_USERNAME` | `webii_tp2` | Usuario de PostgreSQL |
| `SPRING_DATASOURCE_PASSWORD` | Sin valor documentado | Contraseña de PostgreSQL |
| `JWT_SECRET` | Valor de desarrollo configurado por defecto | Clave de firma JWT; en producción debe ser secreta y tener al menos 32 caracteres |
| `JWT_EXPIRATION` | `86400000` | Vigencia del token, en milisegundos |
| `SERVER_PORT` | `8080` | Puerto HTTP |

Las migraciones de Flyway se aplican al iniciar la aplicación. No crear manualmente las tablas administradas por Flyway.

## Iniciar la aplicación

Desde la raíz del repositorio:

```sh
cd api-blank
export SPRING_DATASOURCE_PASSWORD='la-misma-contrasena'
./mvnw spring-boot:run
```

La aplicación queda disponible en `http://localhost:8080`. Para detenerla, presionar `Ctrl+C` en la terminal.

## Documentación y comprobación rápida

Con la aplicación iniciada:

- Swagger UI: <http://localhost:8080/swagger-ui/index.html>
- Especificación OpenAPI: <http://localhost:8080/v3/api-docs>
- Estado de la aplicación: <http://localhost:8080/health>

`GET /health` debe responder `200 OK` sin token. Los productos y las rutas de autenticación son públicos. Las rutas de listas y favoritos requieren un JWT Bearer.

Registrar un usuario de prueba y obtener un token:

```sh
curl -X POST http://localhost:8080/api/auth/registro \
  -H 'Content-Type: application/json' \
  -d '{"email":"usuario@example.com","password":"cambiar-esta-clave"}'

curl -X POST http://localhost:8080/api/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"email":"usuario@example.com","password":"cambiar-esta-clave"}'
```

La respuesta de inicio de sesión contiene el JWT en el campo `token`. Usarlo para consultar una ruta protegida:

```sh
curl http://localhost:8080/api/listas \
  -H 'Authorization: Bearer <TOKEN>'
```

Sin el encabezado `Authorization`, las rutas protegidas deben responder `401 Unauthorized`.

## Ejecutar pruebas

Las pruebas de integración usan el perfil `test` y la base PostgreSQL `favoritos_db_test` en `localhost:5432`, configurada en `api-blank/src/test/resources/application-test.properties`. Crear esa base antes de ejecutar las pruebas:

```sql
CREATE DATABASE favoritos_db_test OWNER postgres;
```

Configurar la contraseña del usuario `postgres` que utiliza la configuración de pruebas:

```sh
export SPRING_DATASOURCE_PASSWORD='contrasena-del-usuario-postgres'
```

Desde la raíz del repositorio:

```sh
cd api-blank
./mvnw test
```

Las pruebas de integración eliminan los datos de las tablas `favoritos`, `listas` y `usuarios` de `favoritos_db_test` antes de preparar sus escenarios. Usar una base exclusiva para pruebas; nunca apuntar el perfil de test a datos que se quieran conservar.

## Endpoints principales

| Método | Ruta | Acceso | Descripción |
| --- | --- | --- | --- |
| `POST` | `/api/auth/registro` | Público | Registrar usuario |
| `POST` | `/api/auth/login` | Público | Iniciar sesión y recibir un JWT |
| `GET` | `/api/productos` | Público | Consultar el catálogo de DummyJSON |
| `GET` | `/api/productos/{id}` | Público | Consultar un producto |
| `GET`, `POST` | `/api/listas` | JWT | Listar o crear listas |
| `GET`, `DELETE` | `/api/listas/{id}` | JWT | Consultar o eliminar una lista vacía |
| `GET` | `/api/listas/{id}/favoritos` | JWT | Consultar favoritos de una lista |
| `POST` | `/api/listas/{origenId}/mover-favoritos` | JWT | Mover favoritos a otra lista y eliminar la lista de origen |
| `GET`, `POST` | `/api/favorito` | JWT | Listar o crear favoritos |
| `GET`, `PUT`, `DELETE` | `/api/favorito/{id}` | JWT | Consultar, actualizar o eliminar un favorito |
| `GET` | `/health` | Público | Comprobar que la aplicación responde |

Swagger contiene el detalle de los parámetros, cuerpos de solicitud y respuestas. El acceso a listas y favoritos está limitado al usuario autenticado; los usuarios con rol `ADMIN` pueden acceder a todas las listas.

## Ejecutar con Docker

Construir la imagen desde la raíz del repositorio:

```sh
docker build -t api-web-ii ./api-blank
```

Si PostgreSQL se ejecuta en el equipo anfitrión con Docker Desktop:

```sh
docker run --rm -p 8080:8080 \
  -e SPRING_DATASOURCE_URL='jdbc:postgresql://host.docker.internal:5432/webii_tp2' \
  -e SPRING_DATASOURCE_USERNAME='webii_tp2' \
  -e SPRING_DATASOURCE_PASSWORD='la-misma-contrasena' \
  -e JWT_SECRET='reemplazar-por-una-clave-secreta-de-al-menos-32-caracteres' \
  api-web-ii
```

Si PostgreSQL también corre en Docker, ambos contenedores deben compartir una red Docker y la URL JDBC debe usar como host el nombre del contenedor de PostgreSQL, no `localhost`.

## Despliegue

El servicio publicado está en <https://web-2-cho5.onrender.com>. Swagger UI está en <https://web-2-cho5.onrender.com/swagger-ui/index.html> y la especificación en <https://web-2-cho5.onrender.com/v3/api-docs>.

Para un despliegue propio, configurar las variables `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD` y `JWT_SECRET` en el proveedor. La base PostgreSQL debe ser accesible desde el servicio, y `JWT_SECRET` debe ser un secreto privado, no la clave predeterminada de desarrollo. Flyway ejecuta las migraciones al iniciar.

El Dockerfile está en `api-blank/`; usar ese directorio como contexto de construcción. El servicio debe publicar el puerto HTTP `8080` o configurar `SERVER_PORT` según el puerto que le asigne el proveedor.
