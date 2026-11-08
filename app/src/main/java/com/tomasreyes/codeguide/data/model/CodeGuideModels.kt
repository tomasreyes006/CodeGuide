package com.tomasreyes.codeguide.data.model

import java.util.UUID

enum class ProjectStatus(val label: String) {
    EN_CURSO("En curso"),
    PAUSADO("Pausados"),
    COMPLETADO("Completados")
}

data class ProjectItem(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val techStack: String,
    val status: ProjectStatus = ProjectStatus.EN_CURSO,
    val completedTasks: Int = 0,
    val totalTasks: Int = 10,
    val phaseOrBadge: String = "Fase 1",
    val nextTaskTitle: String = "",
    val nextTaskTag: String = "",
    val nextTaskPriority: String = "Prioridad Media",
    val branchName: String = "main",
    val syncTimeAgo: String = "Sync reciente"
) {
    val progressPercentage: Int
        get() = if (totalTasks == 0) 0 else ((completedTasks.toFloat() / totalTasks) * 100).toInt()
}

data class MicroTask(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String? = null,
    val durationMinutes: Int = 20,
    val tags: List<String> = emptyList(),
    var isCompleted: Boolean = false
)

enum class EnergyLevel(val title: String, val subtitle: String, val recommendedCapacity: String) {
    SUAVE("Suave", "Docs & Fixes", "2 micro-tareas (~1h 30m)"),
    OPTIMA("Óptima", "Features STD", "3 micro-tareas (~2h 15m)"),
    PROFUNDA("Profunda", "Arquitectura", "2 micro-tareas (~3h 00m)")
}

enum class FocusMood(val title: String, val minutes: Int, val emoji: String) {
    ZEN("Zen / Flow", 25, "🧘"),
    ENERGETICO("Energético", 45, "⚡"),
    LIGERO("Ligero / Fatiga", 15, "☕")
}

enum class AmbientAudio(val label: String) {
    LOFI("Lofi Coding"),
    RAIN("Lluvia suave"),
    SILENCE("Silencio")
}
