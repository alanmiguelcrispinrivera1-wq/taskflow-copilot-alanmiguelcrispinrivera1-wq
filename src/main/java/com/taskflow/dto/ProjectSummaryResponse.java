package com.taskflow.dto;

import java.util.Map;

/**
 * DTO de salida para GET /projects/{id}/summary — resumen del proyecto: conteos por estado y vencidas.
 */
public record ProjectSummaryResponse(Long projectId, String projectName, int totalTasks,
                                     Map<String, Integer> byStatus, int overdue) {
}
