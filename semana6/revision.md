# Copilot CLI Session

> [!NOTE]
> - **Session ID:** `3a01b4d5-19b6-47b0-b052-396c06161d7c`  
> - **Started:** 10/17/2026, 6:16:11 PM  
> - **Duration:** 37s  
> - **Exported:** 10/17/2026, 6:16:49 PM  

---

<sub>3s</sub>

### User

Revisa semana6/proyecto-final.diff contra la especificación specs/search.md.

---

<sub>12s</sub>

### Copilot

Reading both the diff and the specification to compare changes against requirements. Running parallel reads of semana6/proyecto-final.diff and specs/search.md to gather needed context.

---

<sub>12s</sub>

### `view`

**view the file at C:\Users\alanc\taskflow-copilot-alanmiguelcrispinrivera1-wq\semana6\proyecto-final.diff.**

C:\Users\alanc\taskflow-copilot-alanmiguelcrispinrivera1-wq\semana6\proyecto-final.diff

<details>
<summary>186 lines</summary>

```diff
diff --git a/src/main/java/com/taskflow/controller/TaskController.java b/src/main/java/com/taskflow/controller/TaskController.java
index 827024e..1d58f1b 100644
--- a/src/main/java/com/taskflow/controller/TaskController.java
+++ b/src/main/java/com/taskflow/controller/TaskController.java
@@ -87,6 +87,14 @@ public class TaskController {
         return taskService.sinResponsable().stream().map(TaskMapper::aResponse).toList();
     }
 
+    @Operation(summary = "Busca tareas por título",
+            description = "Busca tareas cuyo título contiene el parámetro 'q' (case-insensitive). q es obligatorio.")
+    @GetMapping("/tasks/search")
+    public List<TaskResponse> searchTasks(@RequestParam(name = "q", required = false) String q)
+            throws TaskValidationException {
+        return taskService.buscarPorTitulo(q).stream().map(TaskMapper::aResponse).toList();
+    }
+
     /** GET /tasks/{id} — 200 con TaskResponse, o 404 uniforme (orElseThrow -> advice). */
     @Operation(summary = "Obtiene una tarea por id",
             description = "200 con el TaskResponse; 404 uniforme si el id no existe.")
diff --git a/src/main/java/com/taskflow/service/TaskService.java b/src/main/java/com/taskflow/service/TaskService.java
index 8df7beb..017dae1 100644
--- a/src/main/java/com/taskflow/service/TaskService.java
+++ b/src/main/java/com/taskflow/service/TaskService.java
@@ -122,6 +122,20 @@ public class TaskService {
                 .toList();
     }
 
+    /**
+     * Busca tareas cuyo título contiene q (case-insensitive).
+     * q se recorta con strip() antes de buscar; si es nulo o vacío tras recortar, lanza
+     * TaskValidationException con el mensaje requerido por la especificación.
+     */
+    public List<Task> buscarPorTitulo(String q) throws TaskValidationException {
+        if (q == null || q.strip().isEmpty()) {
+            throw new TaskValidationException("El parámetro 'q' es obligatorio.");
+        }
+        String trimmed = q.strip();
+        List<Task> encontrados = repository.findByTitleContainingIgnoreCase(trimmed);
+        return encontrados.stream().sorted(TaskOrders.POR_TITULO).toList();
+    }
+
     /** Busca una tarea por id: delega en el repositorio y deja subir el Optional TAL CUAL. */
     public Optional<Task> buscarPorId(Long id) {
         return repository.findById(id);
diff --git a/src/test/java/com/taskflow/slice/BusquedaTareasControllerTest.java b/src/test/java/com/taskflow/slice/BusquedaTareasControllerTest.java
new file mode 100644
index 0000000..bd9024c
--- /dev/null
+++ b/src/test/java/com/taskflow/slice/BusquedaTareasControllerTest.java
@@ -0,0 +1,74 @@
+package com.taskflow.slice;
+
+import com.taskflow.controller.TaskController;
+import com.taskflow.exception.TaskValidationException;
+import com.taskflow.model.Task;
+import com.taskflow.model.TaskStatus;
+import com.taskflow.security.JwtAuthenticationFilter;
+import com.taskflow.service.ProjectService;
+import com.taskflow.service.TaskService;
+import org.junit.jupiter.api.Test;
+import org.springframework.beans.factory.annotation.Autowired;
+import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
+import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
+import org.springframework.http.MediaType;
+import org.springframework.test.context.bean.override.mockito.MockitoBean;
+import org.springframework.test.web.servlet.MockMvc;
+
+import java.util.List;
+import java.util.Optional;
+
+import static org.mockito.Mockito.when;
+import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
+import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
+import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
+
+@WebMvcTest(TaskController.class)
+@AutoConfigureMockMvc(addFilters = false)
+class BusquedaTareasControllerTest {
+
+    @Autowired
+    private MockMvc mockMvc;
+
+    @MockitoBean
+    private TaskService taskService;
+
+    @MockitoBean
+    private ProjectService projectService;
+
+    @MockitoBean
+    private JwtAuthenticationFilter jwtAuthenticationFilter;
+
+    @Test
+    void getSearch_conQuery_retorna200YLista() throws Exception {
+        when(taskService.buscarPorTitulo("api")).thenReturn(List.of(
+                tarea(9L, "Documentar la API con Swagger", TaskStatus.TODO),
+                tarea(5L, "Optimizar consultas de la API", TaskStatus.TODO)
+        ));
+
+        mockMvc.perform(get("/tasks/search?q=api").accept(MediaType.APPLICATION_JSON))
+                .andExpect(status().isOk())
+                .andExpect(jsonPath("$.length()").value(2))
+                .andExpect(jsonPath("$[0].id").value(9))
+                .andExpect(jsonPath("$[0].title").value("Documentar la API con Swagger"))
+                .andExpect(jsonPath("$[1].id").value(5))
+                .andExpect(jsonPath("$[1].title").value("Optimizar consultas de la API"));
+    }
+
+    @Test
+    void getSearch_sinQuery_servicioLanzaTaskValidationException_devuelve400() throws Exception {
+        when(taskService.buscarPorTitulo(null)).thenThrow(new TaskValidationException("El parámetro 'q' es obligatorio."));
+
+        mockMvc.perform(get("/tasks/search").accept(MediaType.APPLICATION_JSON))
+                .andExpect(status().isBadRequest())
+                .andExpect(jsonPath("$.message").value("El parámetro 'q' es obligatorio."));
+    }
+
+    private Task tarea(Long id, String title, TaskStatus status) {
+        try {
+            return new Task(id, title, "desc", status, com.taskflow.model.Priority.MED, 1L, 1L, null);
+        } catch (Exception e) {
+            throw new IllegalStateException(e);
+        }
+    }
+}
diff --git a/src/test/java/com/taskflow/unit/BusquedaTareasServiceTest.java b/src/test/java/com/taskflow/unit/BusquedaTareasServiceTest.java
new file mode 100644
index 0000000..95fcab1
--- /dev/null
+++ b/src/test/java/com/taskflow/unit/BusquedaTareasServiceTest.java
@@ -0,0 +1,58 @@
+package com.taskflow.unit;
+
+import com.taskflow.exception.TaskValidationException;
+import com.taskflow.model.Priority;
+import com.taskflow.model.Task;
+import com.taskflow.model.TaskStatus;
+import com.taskflow.repository.TaskRepository;
+import com.taskflow.service.TaskService;
+import org.junit.jupiter.api.Test;
+import org.junit.jupiter.api.extension.ExtendWith;
+import org.mockito.InjectMocks;
+import org.mockito.Mock;
+import org.mockito.junit.jupiter.MockitoExtension;
+
+import java.time.LocalDate;
+import java.util.List;
+
+import static org.junit.jupiter.api.Assertions.assertEquals;
+import static org.junit.jupiter.api.Assertions.assertThrows;
+import static org.mockito.Mockito.never;
+import static org.mockito.Mockito.verify;
+import static org.mockito.Mockito.when;
+
+@ExtendWith(MockitoExtension.class)
+class BusquedaTareasServiceTest {
+
+    @Mock
+    private TaskRepository taskRepository;
+
+    @InjectMocks
+    private TaskService taskService;
+
+    @Test
+    void buscarPorTitulo_ordenaAlfabeticamente_y_verificaTrim() throws Exception {
+        // repositorio devuelve en orden no alfabético
+        Task t1 = new Task(5L, "Optimizar consultas de la API", "d", TaskStatus.TODO, Priority.MED, 1L, 1L, null);
+        Task t2 = new Task(9L, "Documentar la API con Swagger", "d", TaskStatus.TODO, Priority.MED, 1L, 1L, null);
+        when(taskRepository.findByTitleContainingIgnoreCase("api")).thenReturn(List.of(t1, t2));
+
+        List<Task> resultado = taskService.buscarPorTitulo("  api ");
+
+        // verifica que se llamó con la versión recortada
+        verify(taskRepository).findByTitleContainingIgnoreCase("api");
+
+        // resultado ordenado por título: Documentar... antes de Optimizar...
+        assertEquals(2, resultado.size());
+        assertEquals(9L, resultado.get(0).getId());
+        assertEquals(5L, resultado.get(1).getId());
+    }
+
+    @Test
+    void buscarPorTitulo_nullOBlank_lanzaTaskValidationException_y_repositorioNoSeLlama() throws Exception {
+        assertThrows(TaskValidationException.class, () -> taskService.buscarPorTitulo(null));
+        assertThrows(TaskValidationException.class, () -> taskService.buscarPorTitulo("   "));
+
+        verify(taskRepository, never()).findByTitleContainingIgnoreCase("   ");
+    }
+}
```

</details>


---

<sub>12s</sub>

### `view`

**view the file at C:\Users\alanc\taskflow-copilot-alanmiguelcrispinrivera1-wq\specs\search.md.**

C:\Users\alanc\taskflow-copilot-alanmiguelcrispinrivera1-wq\specs\search.md

<details>
<summary>69 lines</summary>

````
# Especificación — `GET /tasks/search?q=`

## Qué

Un endpoint que busca tareas por su título, en todos los proyectos, para encontrar una tarea sin
recorrer la lista completa. «api» encuentra «Documentar la API con Swagger».

```
GET /tasks/search?q=api
Authorization: ******
```

## Reglas

1. Coincide toda tarea cuyo `title` **contiene** `q`, sin distinguir mayúsculas de minúsculas. La
   consulta ya existe en el repositorio: `TaskRepository.findByTitleContainingIgnoreCase(String)`.
   Úsala; no escribas SQL, `@Query` ni un filtro sobre `findAll()`.
2. Antes de buscar, `q` se recorta con `strip()`: `"  api "` busca `"api"`.
3. `q` es obligatorio. Si falta, o si después de recortarlo queda vacío, responde **`400`** con el
   `ErrorResponse` uniforme de siempre y el mensaje `El parámetro 'q' es obligatorio.`. Para eso el
   servicio lanza `TaskValidationException`, que el `GlobalExceptionHandler` ya convierte en 400.
4. Orden: alfabético por título, con el comparador que ya existe, `TaskOrders.POR_TITULO`.
5. Respuesta: `200` con una lista de `TaskResponse` (el mismo DTO que `GET /tasks`, con el mapper que
   ya existe, `TaskMapper.aResponse`). Si nada coincide, `200` con `[]`.
6. Seguridad: igual que el resto de `/tasks` (sin token → `401`). No cambies `SecurityConfig`.

## Dónde

- **Sin DTO ni mapper nuevos**: la respuesta es una lista del `TaskResponse` que ya existe.
- Método nuevo en `TaskService`:
  `public List<Task> buscarPorTitulo(String q) throws TaskValidationException`.
- Endpoint nuevo en `TaskController`: `@GetMapping("/tasks/search")`, con
  `@RequestParam(name = "q", required = false) String q`. El `required = false` es a propósito: así
  el «falta `q`» llega al servicio y sale con el mismo 400 uniforme que el «`q` vacío».
- Hoy `GET /tasks/search` responde `400` con «El parámetro 'id' tiene un valor ilegible»: lo atrapa
  `GET /tasks/{id}`. Con el endpoint nuevo, Spring prefiere la ruta literal `/tasks/search` sin
  importar en qué orden declares los métodos. No escribas un comentario que diga lo contrario.

> **Por qué no se reutiliza `ReportService.buscarPorTitulo`**, que hace casi lo mismo: habría que
> inyectar `ReportService` en `TaskController`, y el slice `TaskControllerTest` (que no puedes tocar)
> dejaría de arrancar. Medido: sus 10 tests fallan con `No qualifying bean of type
> 'com.taskflow.service.ReportService'`. Es la regla «no agregues dependencias nuevas al constructor
> de un controller que ya existe» de la skill `crear-endpoint-taskflow`.

## Tests que deben existir al terminar

Los dos en **clases nuevas** (las que ya existen no se tocan):

- **Unit** `src/test/java/com/taskflow/unit/BusquedaTareasServiceTest.java`, con
  `@Mock TaskRepository` e `@InjectMocks TaskService`:
  - el repositorio devuelve dos tareas en orden **no** alfabético y el servicio las regresa ordenadas
    por título (el test falla si quitas el orden);
  - `"  api "` llama al repositorio con `"api"` (`verify`);
  - `null` y `"   "` lanzan `TaskValidationException` y el repositorio **nunca** se llama.
- **Slice** `src/test/java/com/taskflow/slice/BusquedaTareasControllerTest.java`, con
  `@WebMvcTest(TaskController.class)`, `@AutoConfigureMockMvc(addFilters = false)` y `@MockitoBean`
  de `TaskService`, `ProjectService` (el constructor de `TaskController` recibe los dos) y
  `JwtAuthenticationFilter`:
  - `GET /tasks/search?q=api` → `200` y el JSON trae los ids y títulos que devolvió el servicio, en
    ese orden;
  - `GET /tasks/search` sin `q`, con el servicio lanzando `TaskValidationException` → `400` y
    `$.message` = `El parámetro 'q' es obligatorio.`.

## Resultado esperado con la semilla

Con la app arrancada con el perfil `h2` y el token de `ana`:

| Petición | Resultado |
|---|---|
| `GET /tasks/search?q=api` | `200`, ids `[9, 5]`: «Documentar la API con Swagger» y «Optimizar consultas de la API», en ese orden |
| `GET /tasks/search?q=API` | lo mismo: `[9, 5]` |
| `GET /tasks/search?q=zzz` | `200` con `[]` |
| `GET /tasks/search?q=%20%20` | `400` con `message` = `El parámetro 'q' es obligatorio.` |
| `GET /tasks/search` | `400` con el mismo mensaje |
| `GET /tasks/search?q=api` sin token | `401` |

## Restricciones para el agente

- No modifiques, borres ni desactives ningún test existente.
- No toques archivos fuera de `TaskService.java`, `TaskController.java` y las dos clases de test
  nuevas.
- Al terminar, `mvn -q test` tiene que pasar completo.

El pull request del proyecto final lleva además `semana6/`, `.github/skills/verificar-taskflow/casos-search.ps1`
y una línea nueva en `verificar.ps1`. Esos archivos los agregas tú en el proceso (PF-2 a PF-7 de la guía),
no el agente: no cuentan contra esta restricción.
````

</details>


---

<sub>37s</sub>

### Copilot

Resumen breve: leído el diff y la especificación. Implementación y tests nuevos cumplen la especificación; solo hay pequeñas sugerencias no bloqueantes.

| # | Gravedad | Archivo:línea | Punto checklist | Qué pasa | Qué harías |
|---|---|---:|---|---|---|
| 1 | Sugerencia | src/main/java/com/taskflow/controller/TaskController.java | Convenciones | El método declara "throws TaskValidationException". No es incorrecto, pero la declaración es innecesaria (excepción manejada por GlobalExceptionHandler). | Opcional: quitar "throws" para simplificar la firma. |
| 2 | Sugerencia | src/test/java/com/taskflow/slice/BusquedaTareasControllerTest.java | Tests que prueban algo | Los tests slice y unit verifican comportamiento requerido, pero no cubren casos adicionales del spec (ver abajo). | Añadir tests opcionales para resultados vacíos, case-insensitivity y 401 (seguridad). |

Casos sin test (no obligatorios por la sección "Tests que deben existir", pero mencionados en la especificación):
- GET /tasks/search?q=zzz → 200 con [] (no hay test que verifique lista vacía).
- GET /tasks/search?q=API (mayúsculas) → case-insensitive (no hay test explícito).
- GET /tasks/search?q=... sin token → 401 (slice desactiva filtros; no hay test de seguridad).

Veredicto: APROBADO

Si se desea, aplicar la sugerencia (quitar "throws") y agregar los tests opcionales mencionados.

---

<sub>Generated by [GitHub Copilot CLI](https://github.com/features/copilot/cli)</sub>
