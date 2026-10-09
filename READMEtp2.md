# Arquitectura Hexagonal: Migración de Memoria a JPA

### Componentes Modificados y Nuevos
* **Eliminados/Desactivados**:
  * `FavoritoRepositoryImpl` (o `InMemoryFavoritoRepository`): Implementación anterior en 
  memoria.
* **Nuevos Componentes de Infraestructura**:
  * `FavoritoEntity`: Entidad JPA mutable mapeada a la tabla `favoritos`.
  * `FavoritoJpaRepository`: Repositorio Spring Data JPA.
  * `FavoritoRepositoryAdapter`: Adaptador de persistencia real.

### Componentes Inalterados
* **Dominio**: Modelo `Favorito` (record inmutable).
* **Puerto**: Interfaz `FavoritoRepository`.
* **Capa de Aplicación**: `FavoritoService`.
* **Capa de Presentación / Web**: `FavoritoController` y DTOs (`FavoritoRequestDto`, 
`FavoritoResponseDto`).

---

### Justificación Técnica

Esta separación fue posible gracias a la **Arquitectura Hexagonal (Ports & Adapters)** y
al principio de **Inversión de Dependencias (DIP - SOLID)**:

1. **El Puerto como Contrato**: `FavoritoRepository` es una interfaz definida en el 
dominio que establece el contrato que la aplicación necesita para persistir datos.
2. **Adaptadores Intercambiables**: Tanto la versión inicial en memoria como la nueva 
implementación `FavoritoRepositoryAdapter` son detalles de infraestructura que implementan 
dicho puerto.
3. **Desacoplamiento**: `FavoritoService` depende exclusivamente del puerto 
(`FavoritoRepository`) y no de una implementación concreta. Al cambiar el almacenamiento 
de memoria a PostgreSQL con JPA, solo se reemplazó el adaptador de infraestructura sin 
modificar la lógica de negocio ni la API REST.


### Punto 6

## Evolución del Esquema de Base de Datos (Flyway)

Las migraciones aplicadas en entornos de producción o versiones previas son inmutables.
 Modificar scripts de migración antiguos (`V1`, `V2`, `V3`) rompería el historial de hash de Flyway 
 (checksum) y fallaría en bases de datos existentes. Por este motivo, la incorporación de restricciones 
 como `NOT NULL` sobre columnas existentes con datos nulos se resuelve agregando una nueva migración 
 incremental (`V4`), que garantiza la retrocompatibilidad y la consistencia de los datos en orden 
 secuencial.

### Punto 7 
## Transacciones y Propiedades ACID

Al eliminar o reorganizar listas mediante la operación `/mover-favoritos`, se ejecutan dos escrituras 
críticas: la reasignación masiva de favoritos a la lista destino y la eliminación física de la lista 
origen.


Si se eliminara la anotación `@Transactional` y el sistema sufriera un fallo (ej. caída de red o error 
de BD) justo después de reasignar los favoritos pero antes de borrar la lista origen:

* **Violación de Atomicidad (A de ACID):** La operación quedaría "a medias". Los favoritos habrían 
cambiado de lista, pero la lista origen continuaría existiendo en la base de datos en lugar de haberse 
borrado por completo.

* **Inconsistencia de Datos:** El usuario vería la lista origen todavía creada pero vacía, generando un 
estado inconsistente e inesperado respecto al resultado atómico acordado en las reglas de negocio.