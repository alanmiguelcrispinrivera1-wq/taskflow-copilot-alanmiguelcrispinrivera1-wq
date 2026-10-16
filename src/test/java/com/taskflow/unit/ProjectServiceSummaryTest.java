package com.taskflow.unit;

import com.taskflow.dto.ProjectSummaryResponse;
import com.taskflow.model.Priority;
import com.taskflow.model.Project;
import com.taskflow.model.Task;
import com.taskflow.model.TaskStatus;
import com.taskflow.repository.ProjectRepository;
import com.taskflow.repository.TaskRepository;
import com.taskflow.repository.UserRepository;
import com.taskflow.service.ProjectService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProjectServiceSummaryTest {

    @Mock
    ProjectRepository projectRepository;
    @Mock
    TaskRepository taskRepository;
    @Mock
    UserRepository userRepository;

    @InjectMocks
    ProjectService projectService;

    @Test
    void resumen_proyectoConTareas_calculaConteos() throws Exception {
        Project proyecto = new Project(2L, "App Móvil", "desc", 1L, LocalDate.now().minusDays(10));
        Task t1 = new Task(10L, "T01", "", TaskStatus.TODO, Priority.LOW, 2L, null, null);
        Task t2 = new Task(11L, "T02", "", TaskStatus.IN_PROGRESS, Priority.MED, 2L, null, LocalDate.now().minusDays(1));
        Task t3 = new Task(12L, "T03", "", TaskStatus.DONE, Priority.HIGH, 2L, null, LocalDate.now().minusDays(5));
        List<Task> tareas = List.of(t1, t2, t3);
        when(taskRepository.findByProjectId(2L)).thenReturn(tareas);

        ProjectSummaryResponse resumen = projectService.resumen(proyecto);

        assertEquals(3, resumen.totalTasks());
        assertEquals(1, resumen.byStatus().get("TODO").intValue());
        assertEquals(1, resumen.byStatus().get("IN_PROGRESS").intValue());
        assertEquals(1, resumen.byStatus().get("DONE").intValue());
        assertEquals(1, resumen.overdue());
    }

    @Test
    void resumen_proyectoSinTareas_devuelveCeros() throws Exception {
        Project proyecto = new Project(3L, "Vacio", "desc", 1L, LocalDate.now());
        when(taskRepository.findByProjectId(3L)).thenReturn(List.of());

        ProjectSummaryResponse resumen = projectService.resumen(proyecto);
        assertEquals(0, resumen.totalTasks());
        assertEquals(0, resumen.byStatus().get("TODO").intValue());
        assertEquals(0, resumen.byStatus().get("IN_PROGRESS").intValue());
        assertEquals(0, resumen.byStatus().get("DONE").intValue());
        assertEquals(0, resumen.overdue());
    }
}
