# Proyecto final · Semana 6 · GitHub Copilot

**Nota**: Los ejercicios resueltos que abarcan desde el tema 7 hasta el tema 12 se encuentran en `Ejercicios-Java`

**Alumno:** `Alan Miguel Crispin Rivera` · **Usuario de GitHub:** `alanmiguelcrispinrivera1-wq`

## 1. Qué construí

| | Feature | Especificación |
|---|---|---|
| [ x ] | `GET /tasks/search?q=` — buscar tareas por título | [`specs/search.md`](../specs/search.md) |

## 2. El pull request

- **URL del PR (mergeado):** `https://github.com/alanmiguelcrispinrivera1-wq/taskflow-copilot-alanmiguelcrispinrivera1-wq/pull/5`
- **Commit del merge en `main`:** `1a01f39 (HEAD -> main, origin/main) Merge pull request #5 from alanmiguelcrispinrivera1-wq/feature/search`
- **Comentarios de Copilot code review:** `1`

## 3. Cómo lo hice

| Paso | Qué hice | Evidencia |
|---|---|---|
| Rama y spec | Con el comando`git switch -c feature/search` creé una nueva rama llamada feature/search y me cambie a ella inmediatamente en un solo paso, después copié la spec a `specs/search.md` | Realicé el commit `4e298d6`: `spec: search (proyecto final) |
| Implementación | Ejecuté `copilot -p "/crear-endpoint-taskflow …"` con `gpt-5-mini`, dando permisos de escritura y ejecución para que pudiese emplear comandos de Maven | En el commit `2b485b3` tenemos la parte referente a `Skill "crear-endpoint-taskflow" loaded successfully` que se puede corroborar en `semana6/sesion-implementacion.md` |
| Revisión | Ejecuté la parte de la revision con ayuda del agente `revisor` sobre `semana6/proyecto-final.diff`, no realizo modificaciones, unicamente brindo algunas sugerencias que considero no `bloqueantes` | `semana6/revision.md` finalmente, se nos dio el `Veredicto: APROBADO` |
| Tests | Ejecuté `mvn test` y las 14 pruebas salieron en verde | `Tests run: 81, Failures: 0, Errors: 0, Skipped: 0` |
| Comprobación REST | `verificar.ps1` con `casos-search.ps1` | sección 5 de este documento |
| Code review | En mi caso le solicité la revisión a Copilot en el PR #5 antes de realizar el merge | En la pestaña Conversation del PR me coloco `Changes recommended` indicando que "La prueba de entradas inválidas no garantiza la ausencia de consultas al repositorio exigida por la especificación.", por lo que me dio a entender, es una recomendacion a implementar y lo catalogo como una issue pendiente. |

## 4. Qué hizo el agente y qué corregí yo

| # | Qué hizo mal el agente (archivo) | Quién lo detectó | Cómo quedó corregido |
|---|---|---|---|
| 1 | **Hallazgo falso / no aplicado.** El revisor pidió quitar `throws TaskValidationException` de la firma del nuevo endpoint en `TaskController.java` por considerarlo innecesario (lo maneja `GlobalExceptionHandler`). | revisor (`semana6/revision.md`, sugerencia #1) | No lo cambié. Es una sugerencia de estilo, no una regla de la spec ni de la checklist. El comportamiento es el correcto: `verificar.ps1` confirma que `q` vacío o con espacios responde 400 con el mensaje de la spec (14/14 OK). `git status --porcelain src` quedó limpio, así que no hubo commit de corrección. |
| 2 | **Hallazgo falso (para JUnit).** El revisor marcó como "casos sin test" la lista vacía (`q=zzz` → `[]`), la búsqueda sin distinguir mayúsculas (`q=API`) y el 401 sin token, en `BusquedaTareasControllerTest.java` / `BusquedaTareasServiceTest.java`. | revisor (`semana6/revision.md`, sugerencia #2) | La sección "Tests que deben existir" de `specs/search.md` no los exige. El propio revisor lo reconoce y dio veredicto APROBADO. Esos tres casos quedaron cubiertos a nivel REST con `casos-search.ps1` en `verificar.ps1`, que da 14/14 OK. Commit `100b24a` (`test: casos REST de search en verificar.ps1`). |
| 3 | La sesión exportada `semana6/sesion-implementacion.md` captura la salida de `mvn test`, que puede incluir la línea `Using generated security password` de Spring Security. | yo (paso de la checklist antes de subir la sesión) | La filtré a mano con `Where-Object { $_ -notmatch 'generated security password' }`. Luego verifiqué con `Select-String` que no quedaran `security password`, `Bearer ey`, `ghp_`, `gho_`, `github_pat_` ni `AKIA` (sin coincidencias), antes del commit `2b485b3`. |

**Lo que el agente hizo bien a la primera:** no tuve que tocar ninguno de los 4 archivos que generó: `TaskService.java` (+14), `TaskController.java` (+8), `BusquedaTareasServiceTest.java` y `BusquedaTareasControllerTest.java`. En una sola corrida (4.74 créditos, ~3 min) pasaron `mvn test` (81 tests, 0 fallos), la revisión (APROBADO) y los 6 casos de `/tasks/search` en `verificar.ps1`, antes y después del merge (PR #5, `1a01f39`).

No ejecuté prompts de corrección en PF-4 ni PF-6 ya Copilot code review no solicitó cambios que fuesen extremadamente necesarios, unicamente dio una recomendacion pero nada absolutamente necesario.

## 5. Comprobaciones REST

```text
Empaquetando con Maven (mvn -q package -DskipTests), tarda unos segundos...
App arrancando (PID 49360). Esperando a que /info responda...
App lista en 12 s.
[OK]    GET /tasks/overdue devuelve solo la tarea 7
[OK]    GET /tasks/unassigned devuelve las tareas 4 y 6
[OK]    GET /projects/1/summary
[OK]    GET /projects/2/summary
[OK]    GET /projects/3/summary
[OK]    GET /projects/99/summary responde 404
[OK]    GET /projects/1/summary sin token responde 401
[OK]    GET /tasks/search?q=api devuelve 9 y 5, en ese orden
[OK]    GET /tasks/search?q=API devuelve lo mismo (no distingue mayúsculas)
[OK]    GET /tasks/search?q=zzz devuelve 200 con []
[OK]    GET /tasks/search?q=(espacios) responde 400 con el mensaje de la spec
[OK]    GET /tasks/search sin q responde 400 con el mensaje de la spec
[OK]    GET /tasks/search sin token responde 401
App detenida (PID 49360).
[OK]    App apagada: el puerto 8080 ya no responde
RESULTADO: 14/14 OK
```

## 6. Créditos de la semana

| Qué | AI credits |
|---|---|
| Usados en octubre según github.com (incluye semanas anteriores si usaste Copilot antes) | `411.32` |
| Implementación con la skill (`AI Credits` del PF-2) | `4.74` |
| Revisión del `revisor` (`AI Credits` del PF-3) | `0.86` |
| Correcciones del PF-4 y del PF-6, si hubo (`AI Credits`) | `0` |
