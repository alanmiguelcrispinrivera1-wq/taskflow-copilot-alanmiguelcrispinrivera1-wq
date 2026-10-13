# Arquitectura de TaskFlow

Esta guía rápida ayuda a un desarrollador nuevo a entender la estructura, las reglas de negocio, el recorrido de una petición y cómo se organizan los tests.

## Capas y paquetes

- Controladores (entrada HTTP): `src/main/java/com/taskflow/controller`
  - Ej.: `src/main/java/com/taskflow/controller/TaskController.java` (`TaskController`), `src/main/java/com/taskflow/controller/ProjectController.java` (`ProjectController`).
  - Reciben DTOs (`record`), validan con `@Valid` y construyen respuestas DTO.

- Servicio / Casos de uso: `src/main/java/com/taskflow/service`
  - Ej.: `src/main/java/com/taskflow/service/TaskService.java` (`TaskService`), `ProjectService`.
  - Orquestan operaciones, llaman a mappers y repositorios; traducen excepciones de dominio cuando es necesario.

- Repositorios (persistencia): `src/main/java/com/taskflow/repository`
  - Ej.: `src/main/java/com/taskflow/repository/TaskRepository.java` (`TaskRepository`).
  - Spring Data JPA: entidades -> tablas.

- Modelo / Dominio: `src/main/java/com/taskflow/model`
  - Ej.: `src/main/java/com/taskflow/model/Task.java` (`Task`), `Project`, `User`.
  - Aquí viven las invariantes y las reglas de negocio encapsuladas en métodos y fábricas.

- DTOs y mappers: `src/main/java/com/taskflow/dto` y `src/main/java/com/taskflow/mapper`
  - Ej.: `src/main/java/com/taskflow/mapper/TaskMapper.java` (`TaskMapper`).
  - Los controladores solo exponen DTOs; los mappers traducen entre DTO y dominio.

- Security / Config: `src/main/java/com/taskflow/config` y `src/main/java/com/taskflow/security`
  - `src/main/java/com/taskflow/config/SecurityConfig.java` (`SecurityConfig`)
  - `src/main/java/com/taskflow/security/JwtAuthenticationFilter.java` (`JwtAuthenticationFilter`)
  - `src/main/java/com/taskflow/security/JwtService.java` (`JwtService`)

- Advice / Manejo de errores: `src/main/java/com/taskflow/advice`
  - `src/main/java/com/taskflow/advice/GlobalExceptionHandler.java` (`GlobalExceptionHandler`)

- Inicialización de datos (seed): `src/main/java/com/taskflow/config/DataSeeder.java` (`DataSeeder`).

## Recorrido de POST /projects/{projectId}/tasks (paso a paso)

1. Petición HTTP: el cliente hace `POST /projects/{projectId}/tasks` con un `TaskRequest` JSON.
   - Controlador: `src/main/java/com/taskflow/controller/TaskController.java` -> método `createTask`.
   - `@Valid` en el `@RequestBody` activa Bean Validation; fallos -> `MethodArgumentNotValidException` -> 400 (advice).

2. Validación del proyecto existente:
   - `TaskController` invoca `projectService.buscarPorId(projectId)` para asegurar que el proyecto existe.
   - Si no existe: `ProjectNotFoundException` -> `GlobalExceptionHandler` devuelve 404.

3. Construcción de la entidad:
   - `TaskController` llama a `taskService.crear(request, projectId)`.
   - `TaskService.crear` usa `TaskMapper.aEntidadNueva(request, projectId)` (`src/main/java/com/taskflow/mapper/TaskMapper.java`).
   - `TaskMapper.aEntidadNueva` delega en la fábrica de dominio `Task.crear(...)` (`src/main/java/com/taskflow/model/Task.java`) para producir una entidad nueva con `status = TODO` y aplicar reglas (por ejemplo: "dueDate no puede estar en el pasado al crear").

4. Reglas de negocio y excepciones:
   - Las reglas estrictas y las invariantes están en la entidad `Task` (métodos/fábricas/`setStatus`).
   - Si la fábrica o el setter violan una regla, lanzan `TaskValidationException` (checked) — sube hasta el controller y lo maneja `GlobalExceptionHandler` que convierte en 400.
   - Para cambios de estado, `TaskService` traduce la excepción de validación a `TaskStateException` para que `GlobalExceptionHandler` devuelva 422 cuando corresponde (operación sintácticamente correcta pero prohibida por el estado del dominio).

5. Persistencia:
   - `TaskService` persiste la entidad con `TaskRepository.save(...)` (`src/main/java/com/taskflow/repository/TaskRepository.java`).
   - La base de datos es gestionada por Spring Data JPA (H2 en perfil `h2` de desarrollo).

6. Respuesta HTTP:
   - `TaskController` construye la URI de lectura `/tasks/{id}` y responde `201 Created` con header `Location` y el `TaskResponse` (mapper `TaskMapper.aResponse`).

Resumen de la cadena: `TaskController` -> `ProjectService`(check) -> `TaskService.crear` -> `TaskMapper` -> `Task.crear` (dominio) -> `TaskRepository.save` -> respuesta 201.

## Dónde viven las reglas de negocio

- Reglas de invariantes y transiciones de estado: dentro del dominio, principalmente en `src/main/java/com/taskflow/model/Task.java` (`Task`).
  - Ejemplo: la fábrica `Task.crear(...)` impone que `dueDate` no esté en el pasado al crear.
  - `Task.setStatus(...)` valida transiciones (p.ej. no pasar a `DONE` sin `assignee`).

- Orquestación, validaciones cruzadas y traducción de errores: en `src/main/java/com/taskflow/service/TaskService.java` (`TaskService`).
  - No duplicar reglas de dominio aquí: usar los métodos del modelo.

- Validación de existencia de otros agregados (por ejemplo proyecto existe): en los controllers o en el service que tiene sentido para la operación (aquí `TaskController` delega en `ProjectService` antes de crear).

- Traducción a códigos HTTP: `src/main/java/com/taskflow/advice/GlobalExceptionHandler.java`.

## Seguridad con JWT (cómo funciona)

- Configuración global: `src/main/java/com/taskflow/config/SecurityConfig.java` (`SecurityConfig`).
  - Habilita `@EnableMethodSecurity` para `@PreAuthorize` (p.ej. regla owner del `DELETE /projects/{id}`).
  - Define rutas públicas: `/auth/**`, `/info`, Swagger y la consola H2; el resto requiere token.
  - Política stateless: `SessionCreationPolicy.STATELESS`.

- Generación y verificación de tokens:
  - `src/main/java/com/taskflow/security/JwtService.java` (`JwtService`) genera y valida tokens (jjwt). No meter datos sensibles en claims.

- Filtro de autenticación:
  - `src/main/java/com/taskflow/security/JwtAuthenticationFilter.java` (`JwtAuthenticationFilter`) examina el header `Authorization: Bearer <token>` en cada request.
  - Si el token es válido coloca una `Authentication` en el `SecurityContext`; si es inválido o expirado responde 401 INMEDIATAMENTE (el `GlobalExceptionHandler` no intercepta excepciones lanzadas antes del DispatcherServlet).

- Roles y autorización:
  - Los roles vienen del `UserDetails` (tabla `users`). `@PreAuthorize` y `AccessDeniedHandler` aplican las políticas finas (ej.: `@PreAuthorize("hasRole('ADMIN') or @projectSecurity.esOwner(#id, authentication.name)")` en `ProjectController`).

## Organización de los tests

- Tests unitarios (rápidos, sin Spring): `src/test/java/com/taskflow/unit`
  - Por ejemplo `src/test/java/com/taskflow/unit/TaskServiceTest.java`.
  - Uso: JUnit + Mockito; prueban lógica de servicios y comportamiento del dominio aislado.

- Tests de slice (capa): `src/test/java/com/taskflow/slice`
  - Ej.: `TaskControllerTest`, `ProjectControllerTest`, `TaskRepositoryTest`.
  - Usan `@WebMvcTest` o `@DataJpaTest` para probar una capa con soporte limitado de Spring.

- Tests de integración (lentos, con contexto completo): `src/test/java/com/taskflow/integration`
  - Ej.: `TaskflowApiApplicationTests`, `FlujoCompletoE2ETest`, `SecurityRulesTest`.
  - Algunos usan Testcontainers (marcados `*IT.java`) — por defecto no se ejecutan con `mvn test` salvo que actives `-Ddocker.tests=true`.

- Data de prueba y semilla:
  - `src/main/java/com/taskflow/config/DataSeeder.java` siembra usuarios (`ana`, `luis`, `admin`) y proyectos/tareas en H2 para el perfil `h2`.

- Ejecutar la suite localmente:
  - Comando habitual: `mvn -q test` (ejecuta los unit + slice configurados; evita los IT que requieren Docker a menos que se pida).

## Buenas prácticas y recomendaciones rápidas

- Reusar las reglas del dominio (`Task`) desde los servicios y mappers: no duplicarlas.
- Los controladores solo deben traducir HTTP <-> DTO; la lógica va en `service` o `model`.
- Manejar excepciones conocidas en `GlobalExceptionHandler`; no crear un handler genérico para `Exception`.
- No exponer entidades JPA en las APIs: siempre `DTO` + `TaskMapper` (`src/main/java/com/taskflow/mapper/TaskMapper.java`).

---

Si necesitas, puedo añadir un diagrama simple del flujo o un checklist de onboarding (qué ejecutar localmente y en qué orden).
Las fechas límite se validan en `Task.crear(...)` (`src/main/java/com/taskflow/model/Task.java`).
