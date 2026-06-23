# CLAUDE.md

Este archivo proporciona orientación a Claude Code (claude.ai/code) al trabajar con el código de este repositorio.

## Comandos

```bash
# Compilar e instalar
mvn clean install

# Ejecutar la aplicación
mvn spring-boot:run

# Ejecutar con un perfil de filtro (redundancy o undersampling)
mvn spring-boot:run -Dspring-boot.run.profiles=redundancy
mvn spring-boot:run -Dspring-boot.run.profiles=undersampling

# Ejecutar todas las pruebas
mvn test

# Ejecutar una clase de prueba específica
mvn test -Dtest=BlueprintsSmokeTest

# Construir imagen Docker
docker build -t blueprints-api .
docker run -p 8080:8080 blueprints-api
```

## Arquitectura

El proyecto es una REST API con Spring Boot 3.3.x (Java 21) que sigue una arquitectura estrictamente por capas:

**Flujo:** `Controller → Service → Persistence`, con un `Filter` aplicado en la capa de servicio únicamente al obtener un blueprint individual.

### Puntos de extensión

**Agregar un backend de persistencia (ej. PostgreSQL):** Implementar `BlueprintPersistence` y anotar con `@Repository`. Spring lo inyectará en `BlueprintsServices` si es el único bean `@Repository`, o usar `@Primary` para reemplazar `InMemoryBlueprintPersistence`.

**Agregar un filtro:** Implementar `BlueprintsFilter` y anotar con `@Component @Profile("nombre-perfil")`. Los filtros solo se aplican en `getBlueprint(author, name)` — no en las consultas de lista. `IdentityFilter` es el predeterminado (sin perfil activo). `RedundancyFilter` elimina puntos consecutivos duplicados; `UndersamplingFilter` conserva uno de cada dos puntos (índices pares). Solo un filtro puede estar activo a la vez mediante perfiles de Spring.

### Modelo de dominio

- `Blueprint` — identificado por clave compuesta `(author, name)`. `equals`/`hashCode` basados únicamente en estos dos campos. La lista de puntos es inmutable externamente; se muta mediante `addPoint`.
- `Point` — record de Java con accesores `x()` e `y()`.

### REST API (ruta base: `/api/v1/blueprints`)

Todas las respuestas usan el wrapper `ApiResponse<T>` con campos `code`, `message` y `data`.

| Método | Ruta | Códigos HTTP |
|--------|------|-------------|
| GET | `/api/v1/blueprints` | 200 |
| GET | `/api/v1/blueprints/{author}` | 200, 404 |
| GET | `/api/v1/blueprints/{author}/{bpname}` | 200, 404 |
| POST | `/api/v1/blueprints` | 201, 400 (validación), 403 (duplicado) |
| PUT | `/api/v1/blueprints/{author}/{bpname}/points` | 202, 404 |

El cuerpo del POST usa el record `NewBlueprintRequest` definido dentro del controlador. El cuerpo del PUT es un objeto `Point` individual. Los errores de validación (`@NotBlank`) son capturados por `@ExceptionHandler(MethodArgumentNotValidException.class)` y retornan `400`.

### OpenAPI / Swagger

Disponible en `http://localhost:8080/swagger-ui.html` y `http://localhost:8080/v3/api-docs` con la aplicación en ejecución. Configurado en `OpenApiConfig` y `application.properties` (la estrategia `ant_path_matcher` es requerida para compatibilidad con springdoc).
