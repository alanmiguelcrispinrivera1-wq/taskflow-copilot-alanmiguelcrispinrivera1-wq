package com.taskflow.unit;

import com.taskflow.exception.TaskValidationException;
import com.taskflow.model.Priority;
import com.taskflow.model.Task;
import com.taskflow.model.TaskStatus;
import com.taskflow.repository.TaskRepository;
import com.taskflow.service.TaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BusquedaTareasServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    void buscarPorTitulo_ordenaAlfabeticamente_y_verificaTrim() throws Exception {
        // repositorio devuelve en orden no alfabético
        Task t1 = new Task(5L, "Optimizar consultas de la API", "d", TaskStatus.TODO, Priority.MED, 1L, 1L, null);
        Task t2 = new Task(9L, "Documentar la API con Swagger", "d", TaskStatus.TODO, Priority.MED, 1L, 1L, null);
        when(taskRepository.findByTitleContainingIgnoreCase("api")).thenReturn(List.of(t1, t2));

        List<Task> resultado = taskService.buscarPorTitulo("  api ");

        // verifica que se llamó con la versión recortada
        verify(taskRepository).findByTitleContainingIgnoreCase("api");

        // resultado ordenado por título: Documentar... antes de Optimizar...
        assertEquals(2, resultado.size());
        assertEquals(9L, resultado.get(0).getId());
        assertEquals(5L, resultado.get(1).getId());
    }

    @Test
    void buscarPorTitulo_nullOBlank_lanzaTaskValidationException_y_repositorioNoSeLlama() throws Exception {
        assertThrows(TaskValidationException.class, () -> taskService.buscarPorTitulo(null));
        assertThrows(TaskValidationException.class, () -> taskService.buscarPorTitulo("   "));

        verify(taskRepository, never()).findByTitleContainingIgnoreCase("   ");
    }
}
