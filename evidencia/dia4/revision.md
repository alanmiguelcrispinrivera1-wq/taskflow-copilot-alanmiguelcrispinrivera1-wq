# Copilot CLI Session

> [!NOTE]
> - **Session ID:** `fba9e931-8089-4c1f-a18c-f19c8a96e6f3`  
> - **Started:** 10/16/2026, 1:32:52 PM  
> - **Duration:** 51s  
> - **Exported:** 10/16/2026, 1:33:43 PM  

---

<sub>2s</sub>

### User

Revisa evidencia/dia4/summary.diff contra la especificación specs/summary.md.

---

<sub>5s</sub>

### Copilot

Opening the diff and the specification to compare them side-by-side and produce the reviewer report. Running parallel file reads for evidence/dia4/summary.diff and specs/summary.md.

---

<sub>6s</sub>

### `view` — Failed

**view the file at C:\Users\alanc\taskflow-copilot-alanmiguelcrispinrivera1-wq\evidence\dia4\summary.diff.**

C:\Users\alanc\taskflow-copilot-alanmiguelcrispinrivera1-wq\evidence\dia4\summary.diff

```
Path does not exist
```


---

<sub>6s</sub>

### `view`

**view the file at C:\Users\alanc\taskflow-copilot-alanmiguelcrispinrivera1-wq\specs\summary.md.**

C:\Users\alanc\taskflow-copilot-alanmiguelcrispinrivera1-wq\specs\summary.md

<details>
<summary>52 lines</summary>

````
## Qué se pide

Un endpoint nuevo, `GET /projects/{id}/summary`, que resume un proyecto: **cuántas tareas tiene en
cada estado y cuántas están vencidas**. Sirve para pintar un tablero sin descargar todas las tareas del
proyecto. Pide el token de `POST /auth/login`, como todo lo que no es público.

## Respuesta 200

```json
{
  "projectId": 2,
  "projectName": "App Móvil",
  "totalTasks": 4,
  "byStatus": { "TODO": 1, "IN_PROGRESS": 2, "DONE": 1 },
  "overdue": 1
}
```

| Campo | Tipo | Qué es |
|---|---|---|
| `projectId` | número | el `id` del proyecto pedido |
| `projectName` | texto | el `name` del proyecto |
| `totalTasks` | número | cuántas tareas tiene el proyecto |
| `byStatus` | objeto | una clave por cada valor de `TaskStatus` (`TODO`, `IN_PROGRESS`, `DONE`), **siempre las tres**, aunque valgan 0 |
| `overdue` | número | cuántas tareas del proyecto cumplen `Task.estaVencida()` |

## Reglas

1. **Vencida** es exactamente lo que ya dice `Task.estaVencida()`: tiene fecha límite, la fecha ya pasó y
   el estado no es `DONE`. Se reutiliza ese método; no se reescribe la regla en el servicio.
2. Un proyecto **sin tareas** responde 200 con `totalTasks` 0, las tres claves de `byStatus` en 0 y `overdue` 0.
3. Un proyecto **que no existe** responde **404** con el `ErrorResponse` uniforme de siempre
   (`ProjectNotFoundException`, igual que `GET /projects/{id}`).
4. **Sin token** responde **401**, como todo lo que no es público.
5. Cualquier usuario autenticado puede pedir el resumen de cualquier proyecto (igual que `GET /projects/{id}/tasks`).

## Resultado esperado con la semilla del perfil `h2`

| Petición | `totalTasks` | `byStatus` | `overdue` |
|---|---|---|---|
| `GET /projects/1/summary` | 5 | `TODO` 3 · `IN_PROGRESS` 1 · `DONE` 1 | 0 (la tarea 2 tiene fecha pasada, pero está `DONE`) |
| `GET /projects/2/summary` | 4 | `TODO` 1 · `IN_PROGRESS` 2 · `DONE` 1 | 1 (la tarea 7) |
| `GET /projects/3/summary` | 0 | `TODO` 0 · `IN_PROGRESS` 0 · `DONE` 0 | 0 |
| `GET /projects/99/summary` | — | — | 404 |

Si antes de probar creaste tareas en un proyecto (por la UI o con el servidor MCP), `totalTasks` y
`TODO` de ese proyecto suben en la misma cantidad. Al reiniciar la app con el perfil `h2` vuelven a
ser los de la tabla.

## Cómo se construye (convenciones de TaskFlow)

- DTO de salida: un `record` en `com.taskflow.dto`, `ProjectSummaryResponse`.
- La lógica de conteo va en el servicio (`ProjectService`), no en el controller.
- El controller solo traduce HTTP: busca el proyecto (404 si no existe) y delega.
- Tests:
  - **unit** del servicio con Mockito: un proyecto con tareas en los tres estados y una vencida; un proyecto sin tareas.
  - **slice** `@WebMvcTest` del controller: 200 con el JSON de arriba y 404 con un proyecto inexistente.
- `mvn -q test` en verde al terminar, sin modificar tests existentes.

## Criterios de aceptación

- [ ] `GET /projects/2/summary` con el token de `ana` devuelve el JSON del ejemplo.
- [ ] `GET /projects/3/summary` devuelve ceros en los cinco números.
- [ ] `GET /projects/99/summary` devuelve 404.
- [ ] `mvn -q test` en verde: los tests de antes más los nuevos.
````

</details>


---

<sub>10s</sub>

### Copilot

Listar los directorios evidence y evidence\dia4 para localizar summary.diff y abrir su contenido junto a la especificación. Ejecutando dos lecturas en paralelo: evidence y evidence\dia4.

---

<sub>10s</sub>

### `view` — Failed

**view the file at C:\Users\alanc\taskflow-copilot-alanmiguelcrispinrivera1-wq\evidence.**

C:\Users\alanc\taskflow-copilot-alanmiguelcrispinrivera1-wq\evidence

```
Path does not exist
```


---

<sub>10s</sub>

### `view` — Failed

**view the file at C:\Users\alanc\taskflow-copilot-alanmiguelcrispinrivera1-wq\evidence\dia4.**

C:\Users\alanc\taskflow-copilot-alanmiguelcrispinrivera1-wq\evidence\dia4

```
Path does not exist
```


---

<sub>16s</sub>

### Copilot

Leyendo el árbol del repo y el archivo de especificación para localizar evidence/dia4/summary.diff. Ejecutando lecturas paralelas de la raíz, el directorio specs y specs/summary.md.

---

<sub>16s</sub>

### `view`

**view the file at C:\Users\alanc\taskflow-copilot-alanmiguelcrispinrivera1-wq.**

C:\Users\alanc\taskflow-copilot-alanmiguelcrispinrivera1-wq

<details>
<summary>22 lines</summary>

```
.classpath
.dockerignore
.env.example
.git
.gitattributes
.github
.gitignore
.playwright-mcp
.project
.settings
Dockerfile
README.md
docker-compose.yml
docs
evidencia
issues
pom.xml
postman
specs
src
target
taskflow-mcp
```

</details>


---

<sub>16s</sub>

### `view`

**view the file at C:\Users\alanc\taskflow-copilot-alanmiguelcrispinrivera1-wq\specs.**

C:\Users\alanc\taskflow-copilot-alanmiguelcrispinrivera1-wq\specs

```
overdue.md
summary.md
unassigned.md
```


---

<sub>16s</sub>

### `view`

**view the file at C:\Users\alanc\taskflow-copilot-alanmiguelcrispinrivera1-wq\specs\summary.md.**

C:\Users\alanc\taskflow-copilot-alanmiguelcrispinrivera1-wq\specs\summary.md

<details>
<summary>52 lines</summary>

````
## Qué se pide

Un endpoint nuevo, `GET /projects/{id}/summary`, que resume un proyecto: **cuántas tareas tiene en
cada estado y cuántas están vencidas**. Sirve para pintar un tablero sin descargar todas las tareas del
proyecto. Pide el token de `POST /auth/login`, como todo lo que no es público.

## Respuesta 200

```json
{
  "projectId": 2,
  "projectName": "App Móvil",
  "totalTasks": 4,
  "byStatus": { "TODO": 1, "IN_PROGRESS": 2, "DONE": 1 },
  "overdue": 1
}
```

| Campo | Tipo | Qué es |
|---|---|---|
| `projectId` | número | el `id` del proyecto pedido |
| `projectName` | texto | el `name` del proyecto |
| `totalTasks` | número | cuántas tareas tiene el proyecto |
| `byStatus` | objeto | una clave por cada valor de `TaskStatus` (`TODO`, `IN_PROGRESS`, `DONE`), **siempre las tres**, aunque valgan 0 |
| `overdue` | número | cuántas tareas del proyecto cumplen `Task.estaVencida()` |

## Reglas

1. **Vencida** es exactamente lo que ya dice `Task.estaVencida()`: tiene fecha límite, la fecha ya pasó y
   el estado no es `DONE`. Se reutiliza ese método; no se reescribe la regla en el servicio.
2. Un proyecto **sin tareas** responde 200 con `totalTasks` 0, las tres claves de `byStatus` en 0 y `overdue` 0.
3. Un proyecto **que no existe** responde **404** con el `ErrorResponse` uniforme de siempre
   (`ProjectNotFoundException`, igual que `GET /projects/{id}`).
4. **Sin token** responde **401**, como todo lo que no es público.
5. Cualquier usuario autenticado puede pedir el resumen de cualquier proyecto (igual que `GET /projects/{id}/tasks`).

## Resultado esperado con la semilla del perfil `h2`

| Petición | `totalTasks` | `byStatus` | `overdue` |
|---|---|---|---|
| `GET /projects/1/summary` | 5 | `TODO` 3 · `IN_PROGRESS` 1 · `DONE` 1 | 0 (la tarea 2 tiene fecha pasada, pero está `DONE`) |
| `GET /projects/2/summary` | 4 | `TODO` 1 · `IN_PROGRESS` 2 · `DONE` 1 | 1 (la tarea 7) |
| `GET /projects/3/summary` | 0 | `TODO` 0 · `IN_PROGRESS` 0 · `DONE` 0 | 0 |
| `GET /projects/99/summary` | — | — | 404 |

Si antes de probar creaste tareas en un proyecto (por la UI o con el servidor MCP), `totalTasks` y
`TODO` de ese proyecto suben en la misma cantidad. Al reiniciar la app con el perfil `h2` vuelven a
ser los de la tabla.

## Cómo se construye (convenciones de TaskFlow)

- DTO de salida: un `record` en `com.taskflow.dto`, `ProjectSummaryResponse`.
- La lógica de conteo va en el servicio (`ProjectService`), no en el controller.
- El controller solo traduce HTTP: busca el proyecto (404 si no existe) y delega.
- Tests:
  - **unit** del servicio con Mockito: un proyecto con tareas en los tres estados y una vencida; un proyecto sin tareas.
  - **slice** `@WebMvcTest` del controller: 200 con el JSON de arriba y 404 con un proyecto inexistente.
- `mvn -q test` en verde al terminar, sin modificar tests existentes.

## Criterios de aceptación

- [ ] `GET /projects/2/summary` con el token de `ana` devuelve el JSON del ejemplo.
- [ ] `GET /projects/3/summary` devuelve ceros en los cinco números.
- [ ] `GET /projects/99/summary` devuelve 404.
- [ ] `mvn -q test` en verde: los tests de antes más los nuevos.
````

</details>


---

<sub>21s</sub>

### Copilot

Abrir el diff generado en evidencia/dia4/summary.diff para leer los cambios propuestos y comparar con la especificación specs/summary.md. Ejecutando la lectura del diff.

---

<sub>21s</sub>

### `view`

**view the file at C:\Users\alanc\taskflow-copilot-alanmiguelcrispinrivera1-wq\evidencia\dia4\summary.diff.**

C:\Users\alanc\taskflow-copilot-alanmiguelcrispinrivera1-wq\evidencia\dia4\summary.diff

<details>
<summary>213 lines</summary>

```diff
diff --git a/src/main/java/com/taskflow/controller/ProjectController.java b/src/main/java/com/taskflow/controller/ProjectController.java
index bef609e..5ccf749 100644
--- a/src/main/java/com/taskflow/controller/ProjectController.java
+++ b/src/main/java/com/taskflow/controller/ProjectController.java
@@ -86,6 +86,19 @@ public class ProjectController {
         return tareas.stream().map(TaskMapper::aResponse).toList();
     }
 
+    /**
+     * GET /projects/{id}/summary — resumen: total, conteo por estado (todas las claves) y vencidas.
+     * 404 si el proyecto no existe; delega la lógica al service.
+     */
+    @Operation(summary = "Resumen del proyecto",
+            description = "Devuelve total de tareas, conteos por estado (TODO, IN_PROGRESS, DONE) y cuántas están vencidas.")
+    @GetMapping("/projects/{id}/summary")
+    public com.taskflow.dto.ProjectSummaryResponse getResumen(@PathVariable("id") Long id) {
+        Project proyecto = projectService.buscarPorId(id)
+                .orElseThrow(() -> new ProjectNotFoundException(id));
+        return projectService.resumen(proyecto);
+    }
+
     /**
      * POST /projects — 201 + Location a /projects/{id}. @Valid dispara Bean Validation (400 si falla).
      * El owner sale del JWT — el Authentication (inyectado por Spring Security) trae el username
diff --git a/src/main/java/com/taskflow/dto/ProjectSummaryResponse.java b/src/main/java/com/taskflow/dto/ProjectSummaryResponse.java
new file mode 100644
index 0000000..326237f
--- /dev/null
+++ b/src/main/java/com/taskflow/dto/ProjectSummaryResponse.java
@@ -0,0 +1,10 @@
+package com.taskflow.dto;
+
+import java.util.Map;
+
+/**
+ * DTO de salida para GET /projects/{id}/summary — resumen del proyecto: conteos por estado y vencidas.
+ */
+public record ProjectSummaryResponse(Long projectId, String projectName, int totalTasks,
+                                     Map<String, Integer> byStatus, int overdue) {
+}
diff --git a/src/main/java/com/taskflow/service/ProjectService.java b/src/main/java/com/taskflow/service/ProjectService.java
index b3e9d0f..c87c190 100644
--- a/src/main/java/com/taskflow/service/ProjectService.java
+++ b/src/main/java/com/taskflow/service/ProjectService.java
@@ -60,6 +60,28 @@ public class ProjectService {
         return taskRepository.findByProjectId(projectId);
     }
 
+    /**
+     * Genera un resumen del proyecto: total de tareas, conteos por estado (TODO, IN_PROGRESS, DONE)
+     * y número de tareas vencidas (según Task.estaVencida()). Devuelve siempre las tres claves en el map.
+     */
+    public com.taskflow.dto.ProjectSummaryResponse resumen(Project project) {
+        List<Task> tareas = taskRepository.findByProjectId(project.getId());
+        int total = tareas.size();
+        java.util.Map<String, Integer> byStatus = new java.util.LinkedHashMap<>();
+        byStatus.put("TODO", 0);
+        byStatus.put("IN_PROGRESS", 0);
+        byStatus.put("DONE", 0);
+        int overdue = 0;
+        for (Task t : tareas) {
+            String key = t.getStatus().name();
+            byStatus.put(key, byStatus.getOrDefault(key, 0) + 1);
+            if (t.estaVencida()) {
+                overdue++;
+            }
+        }
+        return new com.taskflow.dto.ProjectSummaryResponse(project.getId(), project.getName(), total, byStatus, overdue);
+    }
+
     /**
      * Crea un proyecto (POST): el request trae name y description; el ownerId NO es una constante
      * — se resuelve del USERNAME AUTENTICADO (el que puso el JWT en el
diff --git a/src/test/java/com/taskflow/slice/ProjectControllerSummaryTest.java b/src/test/java/com/taskflow/slice/ProjectControllerSummaryTest.java
new file mode 100644
index 0000000..965403c
--- /dev/null
+++ b/src/test/java/com/taskflow/slice/ProjectControllerSummaryTest.java
@@ -0,0 +1,63 @@
+package com.taskflow.slice;
+
+import com.taskflow.controller.ProjectController;
+import com.taskflow.dto.ProjectSummaryResponse;
+import com.taskflow.model.Project;
+import com.taskflow.service.ProjectService;
+import com.taskflow.security.JwtAuthenticationFilter;
+import org.junit.jupiter.api.Test;
+import org.mockito.ArgumentMatchers;
+import org.springframework.beans.factory.annotation.Autowired;
+import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
+import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
+import org.springframework.boot.test.mock.mockito.MockBean;
+import org.springframework.http.MediaType;
+import org.springframework.test.web.servlet.MockMvc;
+import java.time.LocalDate;
+import java.util.Map;
+import java.util.Optional;
+
+import static org.mockito.Mockito.when;
+import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
+import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
+import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
+
+@WebMvcTest(ProjectController.class)
+@AutoConfigureMockMvc(addFilters = false)
+class ProjectControllerSummaryTest {
+
+    @Autowired
+    MockMvc mockMvc;
+
+    @MockBean
+    ProjectService projectService;
+
+    @MockBean
+    JwtAuthenticationFilter jwtAuthenticationFilter;
+
+    @Test
+    void getResumen_proyectoExistente_devuelve200yJson() throws Exception {
+        Project proyecto = new Project(2L, "App Móvil", "", 1L, LocalDate.now());
+        ProjectSummaryResponse resp = new ProjectSummaryResponse(2L, "App Móvil", 4,
+                Map.of("TODO", 1, "IN_PROGRESS", 2, "DONE", 1), 1);
+        when(projectService.buscarPorId(2L)).thenReturn(Optional.of(proyecto));
+        when(projectService.resumen(ArgumentMatchers.any(Project.class))).thenReturn(resp);
+
+        mockMvc.perform(get("/projects/2/summary").accept(MediaType.APPLICATION_JSON))
+                .andExpect(status().isOk())
+                .andExpect(jsonPath("$.projectId").value(2))
+                .andExpect(jsonPath("$.projectName").value("App Móvil"))
+                .andExpect(jsonPath("$.totalTasks").value(4))
+                .andExpect(jsonPath("$.byStatus.TODO").value(1))
+                .andExpect(jsonPath("$.byStatus.IN_PROGRESS").value(2))
+                .andExpect(jsonPath("$.byStatus.DONE").value(1))
+                .andExpect(jsonPath("$.overdue").value(1));
+    }
+
+    @Test
+    void getResumen_proyectoInexistente_devuelve404() throws Exception {
+        when(projectService.buscarPorId(99L)).thenReturn(Optional.empty());
+        mockMvc.perform(get("/projects/99/summary").accept(MediaType.APPLICATION_JSON))
+                .andExpect(status().isNotFound());
+    }
+}
diff --git a/src/test/java/com/taskflow/unit/ProjectServiceSummaryTest.java b/src/test/java/com/taskflow/unit/ProjectServiceSummaryTest.java
new file mode 100644
index 0000000..3a748e7
--- /dev/null
+++ b/src/test/java/com/taskflow/unit/ProjectServiceSummaryTest.java
@@ -0,0 +1,67 @@
+package com.taskflow.unit;
+
+import com.taskflow.dto.ProjectSummaryResponse;
+import com.taskflow.model.Priority;
+import com.taskflow.model.Project;
+import com.taskflow.model.Task;
+import com.taskflow.model.TaskStatus;
+import com.taskflow.repository.ProjectRepository;
+import com.taskflow.repository.TaskRepository;
+import com.taskflow.repository.UserRepository;
+import com.taskflow.service.ProjectService;
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
+import static org.mockito.Mockito.when;
+
+@ExtendWith(MockitoExtension.class)
+class ProjectServiceSummaryTest {
+
+    @Mock
+    ProjectRepository projectRepository;
+    @Mock
+    TaskRepository taskRepository;
+    @Mock
+    UserRepository userRepository;
+
+    @InjectMocks
+    ProjectService projectService;
+
+    @Test
+    void resumen_proyectoConTareas_calculaConteos() throws Exception {
+        Project proyecto = new Project(2L, "App Móvil", "desc", 1L, LocalDate.now().minusDays(10));
+        Task t1 = new Task(10L, "T01", "", TaskStatus.TODO, Priority.LOW, 2L, null, null);
+        Task t2 = new Task(11L, "T02", "", TaskStatus.IN_PROGRESS, Priority.MED, 2L, null, LocalDate.now().minusDays(1));
+        Task t3 = new Task(12L, "T03", "", TaskStatus.DONE, Priority.HIGH, 2L, null, LocalDate.now().minusDays(5));
+        List<Task> tareas = List.of(t1, t2, t3);
+        when(taskRepository.findByProjectId(2L)).thenReturn(tareas);
+
+        ProjectSummaryResponse resumen = projectService.resumen(proyecto);
+
+        assertEquals(3, resumen.totalTasks());
+        assertEquals(1, resumen.byStatus().get("TODO").intValue());
+        assertEquals(1, resumen.byStatus().get("IN_PROGRESS").intValue());
+        assertEquals(1, resumen.byStatus().get("DONE").intValue());
+        assertEquals(1, resumen.overdue());
+    }
+
+    @Test
+    void resumen_proyectoSinTareas_devuelveCeros() throws Exception {
+        Project proyecto = new Project(3L, "Vacio", "desc", 1L, LocalDate.now());
+        when(taskRepository.findByProjectId(3L)).thenReturn(List.of());
+
+        ProjectSummaryResponse resumen = projectService.resumen(proyecto);
+        assertEquals(0, resumen.totalTasks());
+        assertEquals(0, resumen.byStatus().get("TODO").intValue());
+        assertEquals(0, resumen.byStatus().get("IN_PROGRESS").intValue());
+        assertEquals(0, resumen.byStatus().get("DONE").intValue());
+        assertEquals(0, resumen.overdue());
+    }
+}
```

</details>


---

<sub>28s</sub>

### Copilot

Reading current ProjectController.java, ProjectService.java, and Project model to verify usage and context. Running three parallel reads.

---

<sub>28s</sub>

### `view`

**view the file at C:\Users\alanc\taskflow-copilot-alanmiguelcrispinrivera1-wq\src\main\java\com\taskflow\controller\ProjectController.java.**

C:\Users\alanc\taskflow-copilot-alanmiguelcrispinrivera1-wq\src\main\java\com\taskflow\controller\ProjectController.java

<details>
<summary>135 lines</summary>

```
package com.taskflow.controller;

import com.taskflow.dto.ProjectRequest;
import com.taskflow.dto.ProjectResponse;
import com.taskflow.dto.TaskResponse;
import com.taskflow.exception.ProjectNotFoundException;
import com.taskflow.mapper.ProjectMapper;
import com.taskflow.mapper.TaskMapper;
import com.taskflow.model.Project;
import com.taskflow.model.Task;
import com.taskflow.model.TaskStatus;
import com.taskflow.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * ProjectController — la puerta HTTP de los proyectos: CRUD completo, en DTOs
 * (ProjectRequest / ProjectResponse); las tareas de un proyecto salen como TaskResponse.
 *
 * Repetición deliberada del patrón de TaskController: mismos gestos (201 + Location, @Valid, 404 vía
 * orElseThrow -> advice). Con el lado Project queda completa la tabla de endpoints de la API (menos /auth/*).
 */
@RestController
@Tag(name = "Projects", description = "CRUD de proyectos y sus tareas. Borrar un proyecto arrastra sus tareas (cascada).")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    /** GET /projects — todos los proyectos como ProjectResponse. */
    @Operation(summary = "Lista todos los proyectos",
            description = "Devuelve todos los proyectos como ProjectResponse (lista vacía si no hay ninguno).")
    @GetMapping("/projects")
    public List<ProjectResponse> getProjects() {
        return projectService.listar().stream()
                .map(ProjectMapper::aResponse)
                .toList();
    }

    /** GET /projects/{id} — 200 con ProjectResponse; 404 uniforme si no existe. */
    @Operation(summary = "Obtiene un proyecto por id",
            description = "200 con el ProjectResponse; 404 uniforme si el id no existe.")
    @GetMapping("/projects/{id}")
    public ProjectResponse getProject(@PathVariable("id") Long id) {
        Project proyecto = projectService.buscarPorId(id)
                .orElseThrow(() -> new ProjectNotFoundException(id));
        return ProjectMapper.aResponse(proyecto);
    }

    /**
     * GET /projects/{id}/tasks — las tareas de un proyecto como TaskResponse. Distinción clave:
     * proyecto inexistente -> 404 (orElseThrow); proyecto sin tareas -> 200 con []. El filtro
     * ?status= es opcional (mismo enum que /tasks). Delega en ProjectService.tareasDe.
     */
    @Operation(summary = "Lista las tareas de un proyecto",
            description = "Tareas del proyecto como TaskResponse; 404 si el proyecto no existe, 200 con [] si no tiene tareas. Filtro opcional ?status=.")
    @GetMapping("/projects/{id}/tasks")
    public List<TaskResponse> getTareasDeProyecto(
            @PathVariable("id") Long id,
            @RequestParam(name = "status", required = false) TaskStatus status) {   // status: opcional
        projectService.buscarPorId(id)
                .orElseThrow(() -> new ProjectNotFoundException(id));
        List<Task> tareas = projectService.tareasDe(id);
        if (status != null) {                          // filtro opcional por estado
            tareas = tareas.stream().filter(t -> t.getStatus() == status).toList();
        }
        return tareas.stream().map(TaskMapper::aResponse).toList();
    }

    /**
     * GET /projects/{id}/summary — resumen: total, conteo por estado (todas las claves) y vencidas.
     * 404 si el proyecto no existe; delega la lógica al service.
     */
    @Operation(summary = "Resumen del proyecto",
            description = "Devuelve total de tareas, conteos por estado (TODO, IN_PROGRESS, DONE) y cuántas están vencidas.")
    @GetMapping("/projects/{id}/summary")
    public com.taskflow.dto.ProjectSummaryResponse getResumen(@PathVariable("id") Long id) {
        Project proyecto = projectService.buscarPorId(id)
                .orElseThrow(() -> new ProjectNotFoundException(id));
        return projectService.resumen(proyecto);
    }

    /**
     * POST /projects — 201 + Location a /projects/{id}. @Valid dispara Bean Validation (400 si falla).
     * El owner sale del JWT — el Authentication (inyectado por Spring Security) trae el username
     * del token; el service lo resuelve a ownerId. El dueño es QUIEN crea, no una constante.
     */
    @Operation(summary = "Crea un proyecto",
            description = "El owner se toma del usuario autenticado (el username del JWT).")
    @PostMapping("/projects")
    public ResponseEntity<ProjectResponse> createProject(@Valid @RequestBody ProjectRequest request,
                                                         Authentication authentication) {
        Project creado = projectService.crear(request, authentication.getName());
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/projects/{id}")
                .buildAndExpand(creado.getId())
                .toUri();
        return ResponseEntity.created(location).body(ProjectMapper.aResponse(creado));
    }

    /** PUT /projects/{id} — reemplazo COMPLETO. 200 con ProjectResponse; 404 si no existe. */
    @Operation(summary = "Reemplaza un proyecto",
            description = "Conserva id, owner y createdAt; name y description vienen del cuerpo.")
    @PutMapping("/projects/{id}")
    public ProjectResponse updateProject(@PathVariable("id") Long id,
                                         @Valid @RequestBody ProjectRequest request) {
        Project actualizado = projectService.reemplazar(id, request);
        return ProjectMapper.aResponse(actualizado);
    }

    /**
     * DELETE /projects/{id} — 204 No Content; 404 si no existe. Borra en cascada las tareas del
     * proyecto (regla "no Task sin Project").
     *
     * La regla de negocio, aplicada: @PreAuthorize se evalúa ANTES del método: pasa si el
     * usuario es ADMIN o es el owner del proyecto (bean @projectSecurity, data-driven). Un USER que no
     * es owner -> 403 (la API SÍ sabe quién es, por eso 403 y no 401). Proyecto inexistente ->
     * esOwner devuelve true a propósito para que hable el 404 del servicio.
     */
    @Operation(summary = "Borra un proyecto y sus tareas",
            description = "Solo ADMIN o el owner. Un USER no-owner recibe 403.")
    @PreAuthorize("hasRole('ADMIN') or @projectSecurity.esOwner(#id, authentication.name)")
    @DeleteMapping("/projects/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable("id") Long id) {
        projectService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
```

</details>


---

<sub>28s</sub>

### `view`

**view the file at C:\Users\alanc\taskflow-copilot-alanmiguelcrispinrivera1-wq\src\main\java\com\taskflow\service\ProjectService.java.**

C:\Users\alanc\taskflow-copilot-alanmiguelcrispinrivera1-wq\src\main\java\com\taskflow\service\ProjectService.java

<details>
<summary>112 lines</summary>

```
package com.taskflow.service;

import com.taskflow.dto.ProjectRequest;
import com.taskflow.exception.ProjectNotFoundException;
import com.taskflow.model.Project;
import com.taskflow.model.Task;
import com.taskflow.model.User;
import com.taskflow.repository.ProjectRepository;
import com.taskflow.repository.TaskRepository;
import com.taskflow.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * ProjectService — la capa de negocio del lado Project. Cero HTTP aquí (el "no existe" se traduce con
 * orElseThrow -> ProjectNotFoundException; el advice le pone el 404).
 *
 * Dos decisiones que se ven aquí:
 *   - Project guarda un ownerId (Long), no un objeto User, así que 'crear' y 'reemplazar' manejan
 *     ese Long. 'crear' construye con id null y la BD asigna el id (IDENTITY).
 *   - 'tareasDe' usa la derived query taskRepository.findByProjectId(id) en vez de filtrar findAll()
 *     con un stream: el filtro lo hace la BD.
 */
@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;   // para resolver el owner desde el username del JWT

    public ProjectService(ProjectRepository projectRepository, TaskRepository taskRepository,
                          UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    /** Todos los proyectos. */
    public List<Project> listar() {
        return projectRepository.findAll();
    }

    /** Un proyecto por id (el Optional sube tal cual: el controller decide 404 vs 200). */
    public Optional<Project> buscarPorId(Long id) {
        return projectRepository.findById(id);
    }

    /**
     * Las tareas de un proyecto, con la derived query taskRepository.findByProjectId(projectId):
     * el WHERE project_id = ? lo hace
     * la BD (viajan solo las filas del proyecto). "no existe el proyecto" (404) lo decide el
     * controller con buscarPorId; ESTE método, si el proyecto existe pero no tiene tareas, devuelve
     * [] (-> 200 con []). No son lo mismo.
     */
    public List<Task> tareasDe(Long projectId) {
        return taskRepository.findByProjectId(projectId);
    }

    /**
     * Genera un resumen del proyecto: total de tareas, conteos por estado (TODO, IN_PROGRESS, DONE)
     * y número de tareas vencidas (según Task.estaVencida()). Devuelve siempre las tres claves en el map.
     */
    public com.taskflow.dto.ProjectSummaryResponse resumen(Project project) {
        List<Task> tareas = taskRepository.findByProjectId(project.getId());
        int total = tareas.size();
        java.util.Map<String, Integer> byStatus = new java.util.LinkedHashMap<>();
        byStatus.put("TODO", 0);
        byStatus.put("IN_PROGRESS", 0);
        byStatus.put("DONE", 0);
        int overdue = 0;
        for (Task t : tareas) {
            String key = t.getStatus().name();
            byStatus.put(key, byStatus.getOrDefault(key, 0) + 1);
            if (t.estaVencida()) {
                overdue++;
            }
        }
        return new com.taskflow.dto.ProjectSummaryResponse(project.getId(), project.getName(), total, byStatus, overdue);
    }

    /**
     * Crea un proyecto (POST): el request trae name y description; el ownerId NO es una constante
     * — se resuelve del USERNAME AUTENTICADO (el que puso el JWT en el
     * Authentication). El dueño no lo decide el cliente ni una semilla: es QUIEN llama. createdAt = hoy;
     * id == null -> la BD lo asigna (IDENTITY).
     */
    public Project crear(ProjectRequest request, String ownerUsername) {
        User owner = userRepository.findByUsername(ownerUsername)
                .orElseThrow(() -> new UsernameNotFoundException(ownerUsername));
        Project nuevo = new Project(null, request.name(), request.description(),
                owner.getId(), LocalDate.now());
        return projectRepository.save(nuevo);
    }

    /**
     * Reemplazo COMPLETO (PUT): existe -> reconstruye con el MISMO id, conservando ownerId y createdAt
     * (el cliente no los toca); name y description vienen del request. save con id presente = update.
     * No existe -> 404.
     */
    public Project reemplazar(Long id, ProjectRequest request) {
        Project actual = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException(id));
        Project reemplazo = new Project(actual.getId(), request.name(), request.description(),
                actual.getOwnerId(), actual.getCreatedAt());
        return projectRepository.save(reemplazo);
    }

    /**
     * Elimina un proyecto (DELETE) con CASCADA MANUAL: borra primero sus tareas (regla "no Task sin
     * Project" — y además la FK task.project_id lo EXIGE: borrar el proyecto con tareas colgando daría
     * una violación de integridad) y luego el proyecto. No existe -> 404. La regla owner/ADMIN no
     * vive aquí: la aplica el @PreAuthorize del controller antes de llegar a este método.
     */
    public void eliminar(Long id) {
        projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException(id));
        tareasDe(id).forEach(t -> taskRepository.deleteById(t.getId()));   // cascada manual (la FK obliga el orden)
        projectRepository.deleteById(id);
    }
}
```

</details>


---

<sub>28s</sub>

### `view`

**view the file at C:\Users\alanc\taskflow-copilot-alanmiguelcrispinrivera1-wq\src\main\java\com\taskflow\model\Task.java.**

C:\Users\alanc\taskflow-copilot-alanmiguelcrispinrivera1-wq\src\main\java\com\taskflow\model\Task.java

<details>
<summary>225 lines</summary>

```
package com.taskflow.model;

import com.taskflow.exception.TaskValidationException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Task — entidad central del dominio TaskFlow (persistida con JPA).
 *
 * Es una clase de dominio normal (8 campos, validaciones, comportamiento): NO hay un "modelo de
 * BD" aparte. Lo que la hace entidad, todo por anotación:
 *   - @Entity + @Table(name = "tasks"): esta clase mapea a la tabla TASKS.
 *   - @Id + @GeneratedValue(IDENTITY): el id lo asigna la BD (columna autoincremental). Nada de
 *     secuencia manual en Java: el id lo pone la BD y nadie más.
 *   - @Enumerated(STRING) en status y priority: se guardan como TEXTO ('TODO'), NUNCA como ORDINAL
 *     (0,1,2): reordenar o insertar un enum corrompería datos históricos. El orden LOW,MED,HIGH
 *     es contrato del SORT (compareTo), no del almacenamiento.
 *   - relación @ManyToOne Task->Project (patrón "asociación de solo lectura"): la COLUMNA la
 *     manda el escalar 'projectId' (por eso Task.crear, TaskService, mappers y DTOs hablan de projectId);
 *     el objeto 'project' solo NAVEGA. Frase ancla: "la columna manda; el objeto navega".
 *
 * Tensión JPA vs validaciones: JPA rehidrata
 * por reflexión con un constructor sin args protegido, SIN pasar por las validaciones. Está bien:
 * Task.crear(...) sigue siendo el ÚNICO camino de CREACIÓN de negocio; JPA solo REHIDRATA datos que
 * ya existían (la distinción crear/rehidratar).
 *
 * OJO: las columnas persistentes NO pueden ser 'final' (JPA las escribe por reflexión tras el no-arg);
 * por eso title/description/projectId/dueDate no son 'final'. La inmutabilidad
 * de negocio la sigue garantizando la ausencia de setters públicos para esos campos.
 */
@Entity
@Table(name = "tasks")
public class Task implements Comparable<Task> {

    // Reglas de longitud del título (regla de negocio).
    private static final int TITULO_MIN = 3;
    private static final int TITULO_MAX = 120;

    // id nace null; lo asigna la BD (IDENTITY). Ver setId (uso interno de JPA/repo).
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String title;

    private String description;                 // por convención -> columna DESCRIPTION

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status;                  // texto 'TODO'/'IN_PROGRESS'/'DONE' (nunca 0/1/2)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority;                  // texto 'LOW'/'MED'/'HIGH'

    // La COLUMNA project_id la posee este escalar (insertable/updatable por default). Sigue siendo
    // el dueño: Task.crear, el mapper y el servicio hablan con projectId, no con el objeto.
    @Column(name = "project_id", nullable = false)
    private Long projectId;

    // assigneeId se queda Long a secas (juicio explícito): es nullable, NUNCA navegamos al User
    // completo al leer tareas, y las derived queries operan igual. No toda FK necesita ser objeto:
    // mapea objeto cuando NAVEGAS, deja el id cuando solo REFERENCIAS.
    @Column(name = "assignee_id")
    private Long assigneeId;

    private LocalDate dueDate;                  // por convención -> columna DUE_DATE (camelCase->snake_case)

    /**
     * Asociación de SOLO LECTURA hacia el proyecto. Comparte la MISMA columna project_id con
     * el escalar de arriba, pero marcada insertable=false/updatable=false: no escribe la columna (el
     * escalar lo hace), solo permite NAVEGAR (task.getProject().getName()). LAZY: no se carga hasta
     * que se toca — y fuera de una sesión abierta, tocarla lanza LazyInitializationException (por eso
     * la API responde con DTOs, nunca con la entidad).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", insertable = false, updatable = false)
    private Project project;

    /**
     * Constructor NO-ARG protegido: EXCLUSIVO de JPA (rehidratación por reflexión). Nunca es un camino
     * de creación de negocio. Su existencia es obligatoria para cualquier @Entity: sin él, al arrancar
     * Hibernate lanza "No default constructor for entity: com.taskflow.model.Task".
     */
    protected Task() {
        // solo para JPA: rehidratación, nunca creación de negocio
    }

    /**
     * Constructor de REHIDRATACIÓN: reconstruye una tarea que YA existía (semilla del DataSeeder,
     * datos releídos). Valida las INVARIANTES que siempre deben cumplirse (título y proyecto), pero
     * NO la regla temporal de dueDate: una tarea vencida es un dato válido que hay que poder releer.
     */
    public Task(Long id, String title, String description, TaskStatus status,
                Priority priority, Long projectId, Long assigneeId, LocalDate dueDate)
            throws TaskValidationException {
        // Invariante 1: título obligatorio, 3-120 chars.
        if (title == null || title.isBlank()) {
            throw new TaskValidationException("El título es obligatorio (no puede ir vacío).");
        }
        if (title.length() < TITULO_MIN || title.length() > TITULO_MAX) {
            throw new TaskValidationException(
                    "El título debe tener entre " + TITULO_MIN + " y " + TITULO_MAX
                            + " caracteres; recibí " + title.length() + ": \"" + title + "\".");
        }
        // Invariante 2: una tarea no puede existir sin proyecto (regla de negocio).
        if (projectId == null) {
            throw new TaskValidationException(
                    "Una tarea no puede existir sin proyecto (projectId == null).");
        }
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.projectId = projectId;
        this.assigneeId = assigneeId;
        this.dueDate = dueDate;
    }

    /**
     * Factory de CREACIÓN de negocio: añade la regla temporal (dueDate no en el pasado) y delega en
     * el constructor. id nace null, status nace TODO. La usan el mapper y el servicio tal cual: que
     * Task sea @Entity no cambia esta firma.
     */
    public static Task crear(String title, String description, Priority priority,
                             LocalDate dueDate, Long projectId, Long assigneeId)
            throws TaskValidationException {
        if (dueDate != null && dueDate.isBefore(LocalDate.now())) {
            throw new TaskValidationException(
                    "La fecha límite no puede estar en el pasado: " + dueDate + ".");
        }
        return new Task(null, title, description, TaskStatus.TODO, priority, projectId, assigneeId, dueDate);
    }

    // ---- Comportamiento: vive CON los datos ----

    /** true si tiene fecha, ya pasó, y la tarea aún no está terminada. */
    public boolean estaVencida() {
        return dueDate != null && dueDate.isBefore(LocalDate.now()) && status != TaskStatus.DONE;
    }

    // ---- Getters ----

    public Long getId() {
        return id;
    }

    /** Uso interno (JPA/repo) para el id autoincremental. Nadie más lo llama a mano. */
    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public Priority getPriority() {
        return priority;
    }

    public Long getProjectId() {
        return projectId;
    }

    public Long getAssigneeId() {
        return assigneeId;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    /**
     * Navega al proyecto asociado (asociación de solo lectura). Fuera de una sesión JPA abierta puede
     * lanzar LazyInitializationException (por eso la API expone DTOs, no la entidad). En el runtime
     * nadie del camino principal lo usa; existe para NAVEGAR cuando haga falta.
     */
    public Project getProject() {
        return project;
    }

    // ---- Setters SOLO donde el dominio muta ----

    /**
     * Cambia el estado aplicando la regla de negocio: no se puede pasar a DONE una tarea sin
     * responsable (assigneeId). La regla vive AQUÍ, no en el servicio ni en el controller.
     */
    public void setStatus(TaskStatus status) throws TaskValidationException {
        if (status == TaskStatus.DONE && assigneeId == null) {
            throw new TaskValidationException(
                    "No se puede marcar como " + TaskStatus.DONE
                            + " una tarea sin responsable: \"" + title + "\".");
        }
        this.status = status;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public void setAssigneeId(Long assigneeId) {
        this.assigneeId = assigneeId;
    }

    // ---- Identidad de ENTIDAD por id (literalmente lo que hace JPA) ----

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Task otra = (Task) o;
        return Objects.equals(id, otra.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // ---- Orden natural por prioridad (Comparable) ----

    @Override
    public int compareTo(Task otra) {
        return this.priority.compareTo(otra.priority);
    }

    @Override
    public String toString() {
        return "Task{id=" + id
                + ", title='" + title + '\''
                + ", status=" + status
                + ", priority=" + priority
                + ", assigneeId=" + (assigneeId == null ? "sin asignar" : assigneeId)
                + ", dueDate=" + (dueDate == null ? "sin fecha" : dueDate)
                + (estaVencida() ? " *VENCIDA*" : "")
                + '}';
    }
}
```

</details>


---

<sub>51s</sub>

### Copilot

Resumen breve: leída la especificación specs/summary.md y el diff evidencia/dia4/summary.diff; los cambios implementan correctamente el endpoint GET /projects/{id}/summary, la lógica en el service, el DTO record y los tests unit/slice pedidos. No hay fallos que bloqueen el merge.

| # | Gravedad | Archivo:línea | Punto de la checklist | Qué pasa | Qué harías |
|---|---|---|---|---|---|
| 1 | Sugerencia | src/main/java/com/taskflow/dto/ProjectSummaryResponse.java:1-10 | Contrato / Tipos | El DTO usa Map\<String,Integer> para byStatus (cumple contrato JSON) | Considerar EnumMap\<TaskStatus,Integer> o Map\<TaskStatus,Integer> para mayor tipo-seguridad y evitar errores por strings, sin cambiar la representación JSON si se serializa por name(). |
| 2 | Sugerencia | src/test/java/com/taskflow/slice/ProjectControllerSummaryTest.java:1-63 | Tests existentes / Seguridad | El slice test desactiva filtros (@AutoConfigureMockMvc(addFilters = false)) y no verifica 401/autorization | Opcional: añadir un test de integración/smoke que verifique 401 cuando falte token (la spec exige 401 en runtime), aunque no es obligatorio en el slice según la spec. |

Casos sin test:
- Ninguno: hay unit tests del service para proyecto con tareas (incl. vencida) y sin tareas; hay slice tests para 200 (JSON) y 404.

Veredicto: APROBADO.

---

<sub>Generated by [GitHub Copilot CLI](https://github.com/features/copilot/cli)</sub>
