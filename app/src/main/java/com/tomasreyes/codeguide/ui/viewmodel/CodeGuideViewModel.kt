package com.tomasreyes.codeguide.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tomasreyes.codeguide.data.local.CodeGuideJsonManager
import com.tomasreyes.codeguide.data.model.AmbientAudio
import com.tomasreyes.codeguide.data.model.EnergyLevel
import com.tomasreyes.codeguide.data.model.FocusMood
import com.tomasreyes.codeguide.data.model.MicroTask
import com.tomasreyes.codeguide.data.model.ProjectItem
import com.tomasreyes.codeguide.data.model.ProjectStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.min

data class CodeGuideUiState(
    // State 1: Hoy / Daily Tasks
    val dailyTasks: List<MicroTask> = listOf(
        MicroTask(
            id = "1",
            title = "Revisar PR #142: Schemas Zod",
            durationMinutes = 15,
            tags = listOf("Fácil • Code Review"),
            isCompleted = true
        ),
        MicroTask(
            id = "2",
            title = "Actualizar variables en Dockerfile",
            durationMinutes = 20,
            tags = listOf("Fácil • DevOps"),
            isCompleted = true
        ),
        MicroTask(
            id = "3",
            title = "Crear handler de Refresh Token",
            description = "Rotación de tokens y almacenamiento en HTTP-Only...",
            durationMinutes = 35,
            tags = listOf("Moderado • Backend", "Auth Project"),
            isCompleted = false
        ),
        MicroTask(
            id = "4",
            title = "Redactar Readme de integración OpenAPI",
            description = "Ejemplos de llamadas cURL para el equipo de...",
            durationMinutes = 25,
            tags = listOf("Baja Carga • Documentación"),
            isCompleted = false
        )
    ),
    val energyLevel: EnergyLevel = EnergyLevel.OPTIMA,

    // State 2: Proyectos
    val projectsList: List<ProjectItem> = listOf(
        ProjectItem(
            id = "p1",
            title = "E-Commerce Microservices",
            techStack = "Go • Kafka • PostgreSQL",
            status = ProjectStatus.EN_CURSO,
            completedTasks = 18,
            totalTasks = 24,
            phaseOrBadge = "2 módulos listos",
            nextTaskTitle = "Optimizar queries en Postgres y pooling de conexiones",
            nextTaskTag = "Arquitectura · Alto foco",
            nextTaskPriority = "Prioridad Alta • ~30m",
            syncTimeAgo = "Sync hace 12m"
        ),
        ProjectItem(
            id = "p2",
            title = "App Móvil Flutter",
            techStack = "Dart • Riverpod • Clean Arch",
            status = ProjectStatus.EN_CURSO,
            completedTasks = 6,
            totalTasks = 15,
            phaseOrBadge = "Fase 2 de 4",
            nextTaskTitle = "Crear componentes atómicos de botón y tarjeta base",
            nextTaskTag = "UI Kit · Modo Flow Ligero",
            nextTaskPriority = "Baja Energía • ~20m",
            branchName = "feature/ds-tokens"
        ),
        ProjectItem(
            id = "p3",
            title = "CLI Tool de Automatización",
            techStack = "Rust • Clap • GitHub Actions",
            status = ProjectStatus.EN_CURSO,
            completedTasks = 18,
            totalTasks = 20,
            phaseOrBadge = "☑ Casi listo",
            nextTaskTitle = "Generar binarios multi-arquitectura y release v1.0",
            nextTaskTag = "Despliegue CI/CD",
            nextTaskPriority = "Revisión • ~15m"
        )
    ),
    val projectFilter: ProjectStatus = ProjectStatus.EN_CURSO,
    val searchQuery: String = "",

    // State 3: Temporizador de Enfoque
    val focusMood: FocusMood = FocusMood.ZEN,
    val timerSecondsRemaining: Int = 25 * 60,
    val isTimerRunning: Boolean = false,
    val activeAudio: AmbientAudio = AmbientAudio.LOFI,
    val activeFocusTaskTitle: String = "Definir middleware de validación JWT",

    // State 4: Métricas & Stats
    val completedBlocksCount: Int = 14,
    val totalFocusMinutes: Int = 360,
    val clarityIndex: Int = 88,
    val streakDays: Int = 5,

    // State 5: Configuración / Modo Oscuro
    val isDarkMode: Boolean = false
) {
    val completedDailyTasksCount: Int
        get() = dailyTasks.count { it.isCompleted }

    val totalDailyTasksCount: Int
        get() = dailyTasks.size

    val dailyProgressPercentage: Int
        get() = if (totalDailyTasksCount == 0) 0 else ((completedDailyTasksCount.toFloat() / totalDailyTasksCount) * 100).toInt()

    val formattedTimerText: String
        get() {
            val minutes = timerSecondsRemaining / 60
            val seconds = timerSecondsRemaining % 60
            return String.format("%02d:%02d", minutes, seconds)
        }
}

class CodeGuideViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(CodeGuideUiState())
    val uiState: StateFlow<CodeGuideUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null

    init {
        // Cargar Base de Datos Local al iniciar la aplicación
        val savedState = CodeGuideJsonManager.loadFromDatabase(application)
        if (savedState != null) {
            _uiState.value = savedState
        }
    }

    private fun persistState() {
        viewModelScope.launch(Dispatchers.IO) {
            CodeGuideJsonManager.saveToDatabase(getApplication(), _uiState.value)
        }
    }

    // --- ACCIONES DE HOY / TAREAS DIARIAS ---
    fun toggleDailyTask(taskId: String) {
        val updatedTasks = _uiState.value.dailyTasks.map { task ->
            if (task.id == taskId) {
                task.copy(isCompleted = !task.isCompleted)
            } else task
        }
        _uiState.value = _uiState.value.copy(dailyTasks = updatedTasks)
        persistState()
    }

    fun addDailyTask(title: String, description: String?, durationMinutes: Int, tagText: String) {
        if (title.isBlank()) return
        val newTask = MicroTask(
            title = title.trim(),
            description = description?.takeIf { it.isNotBlank() },
            durationMinutes = durationMinutes,
            tags = if (tagText.isBlank()) listOf("Personalizado") else tagText.split(",").map { it.trim() },
            isCompleted = false
        )
        _uiState.value = _uiState.value.copy(
            dailyTasks = _uiState.value.dailyTasks + newTask
        )
        persistState()
    }

    fun setEnergyLevel(level: EnergyLevel) {
        _uiState.value = _uiState.value.copy(energyLevel = level)
        persistState()
    }

    // --- ACCIONES DE PROYECTOS ---
    fun setProjectFilter(status: ProjectStatus) {
        _uiState.value = _uiState.value.copy(projectFilter = status)
    }

    fun setSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun createProject(title: String, techStack: String, repoUrl: String) {
        if (title.isBlank()) return
        val newProject = ProjectItem(
            title = title.trim(),
            techStack = if (techStack.isBlank()) "Kotlin • Android" else techStack.trim(),
            status = ProjectStatus.EN_CURSO,
            completedTasks = 1,
            totalTasks = 10,
            phaseOrBadge = "Fase Inicial 1 de 5",
            nextTaskTitle = "Configurar arquitectura base y repositorios",
            nextTaskTag = "DevOps · Setup",
            nextTaskPriority = "Alta Prioridad • ~25m"
        )
        _uiState.value = _uiState.value.copy(
            projectsList = _uiState.value.projectsList + newProject
        )
        persistState()
    }

    fun deleteProject(projectId: String) {
        val updatedProjects = _uiState.value.projectsList.filter { it.id != projectId }
        _uiState.value = _uiState.value.copy(projectsList = updatedProjects)
        persistState()
    }

    fun toggleDarkMode() {
        _uiState.value = _uiState.value.copy(isDarkMode = !_uiState.value.isDarkMode)
        persistState()
    }

    // --- ACCIONES DE ENFOQUE / TIMER ---
    fun selectFocusMood(mood: FocusMood) {
        pauseTimer()
        _uiState.value = _uiState.value.copy(
            focusMood = mood,
            timerSecondsRemaining = mood.minutes * 60,
            isTimerRunning = false
        )
    }

    fun startOrPauseTimer() {
        if (_uiState.value.isTimerRunning) {
            pauseTimer()
        } else {
            startTimer()
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        _uiState.value = _uiState.value.copy(isTimerRunning = true)
        timerJob = viewModelScope.launch {
            while (_uiState.value.timerSecondsRemaining > 0 && _uiState.value.isTimerRunning) {
                delay(1000)
                _uiState.value = _uiState.value.copy(
                    timerSecondsRemaining = _uiState.value.timerSecondsRemaining - 1
                )
            }
            if (_uiState.value.timerSecondsRemaining <= 0) {
                completeFocusBlock()
            }
        }
    }

    fun pauseTimer() {
        timerJob?.cancel()
        _uiState.value = _uiState.value.copy(isTimerRunning = false)
    }

    fun completeFocusBlock() {
        pauseTimer()
        _uiState.value = _uiState.value.copy(
            completedBlocksCount = _uiState.value.completedBlocksCount + 1,
            totalFocusMinutes = _uiState.value.totalFocusMinutes + _uiState.value.focusMood.minutes,
            timerSecondsRemaining = _uiState.value.focusMood.minutes * 60,
            clarityIndex = min(100, _uiState.value.clarityIndex + 1)
        )
        persistState()
    }

    fun startBreak(minutes: Int = 5) {
        pauseTimer()
        _uiState.value = _uiState.value.copy(
            timerSecondsRemaining = minutes * 60
        )
        startTimer()
    }

    fun selectAmbientAudio(audio: AmbientAudio) {
        _uiState.value = _uiState.value.copy(activeAudio = audio)
    }
}
