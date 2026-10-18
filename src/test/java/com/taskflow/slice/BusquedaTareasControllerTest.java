package com.taskflow.slice;

import com.taskflow.controller.TaskController;
import com.taskflow.exception.TaskValidationException;
import com.taskflow.model.Task;
import com.taskflow.model.TaskStatus;
import com.taskflow.security.JwtAuthenticationFilter;
import com.taskflow.service.ProjectService;
import com.taskflow.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
@AutoConfigureMockMvc(addFilters = false)
class BusquedaTareasControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @MockitoBean
    private ProjectService projectService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    void getSearch_conQuery_retorna200YLista() throws Exception {
        when(taskService.buscarPorTitulo("api")).thenReturn(List.of(
                tarea(9L, "Documentar la API con Swagger", TaskStatus.TODO),
                tarea(5L, "Optimizar consultas de la API", TaskStatus.TODO)
        ));

        mockMvc.perform(get("/tasks/search?q=api").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(9))
                .andExpect(jsonPath("$[0].title").value("Documentar la API con Swagger"))
                .andExpect(jsonPath("$[1].id").value(5))
                .andExpect(jsonPath("$[1].title").value("Optimizar consultas de la API"));
    }

    @Test
    void getSearch_sinQuery_servicioLanzaTaskValidationException_devuelve400() throws Exception {
        when(taskService.buscarPorTitulo(null)).thenThrow(new TaskValidationException("El parámetro 'q' es obligatorio."));

        mockMvc.perform(get("/tasks/search").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("El parámetro 'q' es obligatorio."));
    }

    private Task tarea(Long id, String title, TaskStatus status) {
        try {
            return new Task(id, title, "desc", status, com.taskflow.model.Priority.MED, 1L, 1L, null);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
