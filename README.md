# TP WEB II - API REST con Spring Boot y DummyJSON

Proyecto desarrollado para la materia **Web II**. Consiste en la implementación de una API RESTful 
construida con **Spring Boot 4 / Java 25** que integra el catálogo público de [DummyJSON]
(https://dummyjson.com/) y administra una colección de **Favoritos** y **Listas** persistida en 
PostgreSQL mediante Spring Data JPA y Flyway bajo Arquitectura Hexagonal.

---

## 🛠️ Tecnologías y Requisitos Previos

- **Java 25** (o superior)
- **Maven 3.x** (o mediante el wrapper incluido `./mvnw`)
- **Spring Boot 4.1.1**
- **PostgreSQL** (instalación local)
- **Flyway** (migraciones de base de datos)
- **Springdoc-OpenAPI** (Swagger UI)
- **Jakarta Bean Validation**

---

## 🗄️ Configuración de PostgreSQL

1. Accedé al cliente `psql` o PGAdmin en tu instalación local de PostgreSQL.
2. Creá la base de datos necesaria:
   ```sql
   CREATE DATABASE favoritos_db;