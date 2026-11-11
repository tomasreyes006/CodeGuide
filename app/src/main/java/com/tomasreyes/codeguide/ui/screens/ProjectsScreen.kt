package com.tomasreyes.codeguide.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tomasreyes.codeguide.data.model.ProjectItem
import com.tomasreyes.codeguide.data.model.ProjectStatus
import com.tomasreyes.codeguide.ui.components.CodeGuideBottomNavigationBar
import com.tomasreyes.codeguide.ui.components.CodeGuideLogo
import com.tomasreyes.codeguide.ui.theme.CodeGuideBackground
import com.tomasreyes.codeguide.ui.theme.CodeGuideBorder
import com.tomasreyes.codeguide.ui.theme.CodeGuideLink
import com.tomasreyes.codeguide.ui.theme.CodeGuideNavy
import com.tomasreyes.codeguide.ui.theme.CodeGuidePasskeyBg
import com.tomasreyes.codeguide.ui.theme.CodeGuideSurface
import com.tomasreyes.codeguide.ui.theme.CodeGuideTeal
import com.tomasreyes.codeguide.ui.theme.CodeGuideTextPrimary
import com.tomasreyes.codeguide.ui.theme.CodeGuideTextSecondary
import com.tomasreyes.codeguide.ui.viewmodel.CodeGuideViewModel

/**
 * Pantalla: "Proyectos" (CRUD Funcional con ViewModel)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectsScreen(
    viewModel: CodeGuideViewModel = viewModel(),
    onNavigateToToday: () -> Unit = {},
    onNavigateToFocus: () -> Unit = {},
    onNavigateToMetrics: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    var showCreateModal by remember { mutableStateOf(false) }
    var projectToDelete by remember { mutableStateOf<ProjectItem?>(null) }

    val filteredProjects = uiState.projectsList.filter { project ->
        val matchesFilter = project.status == uiState.projectFilter
        val matchesQuery = uiState.searchQuery.isBlank() ||
                project.title.contains(uiState.searchQuery, ignoreCase = true) ||
                project.techStack.contains(uiState.searchQuery, ignoreCase = true)
        matchesFilter && matchesQuery
    }

    Scaffold(
        bottomBar = {
            CodeGuideBottomNavigationBar(
                selectedTab = "Proyectos",
                onTabSelected = { tab ->
                    when (tab) {
                        "Hoy" -> onNavigateToToday()
                        "Enfoque" -> onNavigateToFocus()
                        "Métricas" -> onNavigateToMetrics()
                    }
                }
            )
        },
        containerColor = CodeGuideBackground
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            CodeGuideBackground,
                            Color(0xFFEBF5FB),
                            CodeGuideBackground
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                // 1. Header
                HeaderBarProjects(onProfileClick = onNavigateToProfile)

                // 2. Metría
                CognitiveLoadMetricCard()

                // 3. Búsqueda y Filtros
                SearchAndFilterSection(
                    searchQuery = uiState.searchQuery,
                    onSearchQueryChange = { query -> viewModel.setSearchQuery(query) },
                    selectedStatus = uiState.projectFilter,
                    onStatusSelected = { status -> viewModel.setProjectFilter(status) }
                )

                // 4. Lista Dinámica de Proyectos
                if (filteredProjects.isEmpty()) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        color = CodeGuideSurface,
                        border = BorderStroke(1.dp, CodeGuideBorder)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "No hay proyectos en la categoría '${uiState.projectFilter.label}'",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = CodeGuideNavy
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "¡Crea un nuevo proyecto con el botón inferior para comenzar!",
                                fontSize = 12.sp,
                                color = CodeGuideTextSecondary
                            )
                        }
                    }
                } else {
                    filteredProjects.forEach { project ->
                        GenericProjectCard(
                            project = project,
                            onStartTask = onNavigateToFocus,
                            onDelete = { projectToDelete = project }
                        )
                    }
                }

                // 5. Botón de Nuevo Proyecto
                NewProjectButton(onClick = { showCreateModal = true })

                Spacer(modifier = Modifier.height(12.dp))
            }

            if (showCreateModal) {
                CreateProjectBottomSheet(
                    onDismiss = { showCreateModal = false },
                    onCreateProject = { title, stack, repo ->
                        viewModel.createProject(title, stack, repo)
                        showCreateModal = false
                    }
                )
            }

            // Diálogo de Confirmación de Borrado CRUD
            projectToDelete?.let { targetProject ->
                AlertDialog(
                    onDismissRequest = { projectToDelete = null },
                    title = {
                        Text(text = "Eliminar Proyecto", fontWeight = FontWeight.Bold, color = CodeGuideNavy)
                    },
                    text = {
                        Text(
                            text = "¿Estás seguro de que deseas eliminar el proyecto '${targetProject.title}'? Esta acción no se puede deshacer.",
                            fontSize = 13.sp,
                            color = CodeGuideTextSecondary
                        )
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                viewModel.deleteProject(targetProject.id)
                                projectToDelete = null
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE11D48))
                        ) {
                            Text("Eliminar", color = Color.White)
                        }
                    },
                    dismissButton = {
                        Button(
                            onClick = { projectToDelete = null },
                            colors = ButtonDefaults.buttonColors(containerColor = CodeGuideBorder)
                        ) {
                            Text("Cancelar", color = CodeGuideNavy)
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun HeaderBarProjects(onProfileClick: () -> Unit = {}) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            CodeGuideLogo(iconSize = 34.dp, showText = false)
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = "CodeGuide",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = CodeGuideNavy
                )
                Text(
                    text = "PROYECTOS",
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = CodeGuideTextSecondary,
                    letterSpacing = 1.sp
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(50.dp),
                color = CodeGuidePasskeyBg,
                border = BorderStroke(1.dp, CodeGuideBorder)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "🔥", fontSize = 13.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "5d",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = CodeGuideNavy
                    )
                }
            }

            Box(
                contentAlignment = Alignment.BottomEnd,
                modifier = Modifier.clickable { onProfileClick() }
            ) {
                Surface(
                    shape = CircleShape,
                    color = CodeGuideNavy,
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "img",
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(CodeGuideTeal)
                        .border(1.5.dp, Color.White, CircleShape)
                )
            }
        }
    }
}

@Composable
private fun CognitiveLoadMetricCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = CodeGuideSurface,
        border = BorderStroke(1.dp, CodeGuideBorder)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(CodeGuideTeal)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "METRÍA DE CARGA COGNITIVA",
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = CodeGuideTextSecondary,
                        letterSpacing = 0.5.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(CodeGuidePasskeyBg)
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = "65%  •  Saludable",
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = CodeGuideNavy
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Capacidad Óptima",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = CodeGuideNavy
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "3 ramas activas sin saturación mental. Flujo estable.",
                        fontSize = 12.sp,
                        color = CodeGuideTextSecondary,
                        lineHeight = 16.sp
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(52.dp)
                ) {
                    CircularProgressIndicator(
                        progress = { 0.65f },
                        modifier = Modifier.fillMaxSize(),
                        color = CodeGuideNavy,
                        strokeWidth = 5.dp,
                        trackColor = CodeGuideBorder
                    )
                    Text(
                        text = "65%",
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = CodeGuideNavy
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchAndFilterSection(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    selectedStatus: ProjectStatus,
    onStatusSelected: (ProjectStatus) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            placeholder = {
                Text(
                    text = "Buscar módulos, microservicios, stack...",
                    fontSize = 13.sp,
                    color = CodeGuideTextSecondary.copy(alpha = 0.6f)
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = CodeGuideTextSecondary,
                    modifier = Modifier.size(18.dp)
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = "Filter",
                    tint = CodeGuideNavy,
                    modifier = Modifier.size(18.dp)
                )
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = CodeGuideTextPrimary,
                unfocusedTextColor = CodeGuideTextPrimary,
                focusedContainerColor = CodeGuideSurface,
                unfocusedContainerColor = CodeGuideSurface,
                focusedBorderColor = CodeGuideTeal,
                unfocusedBorderColor = CodeGuideBorder
            ),
            singleLine = true
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProjectStatus.entries.forEach { status ->
                val isSelected = status == selectedStatus
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(if (isSelected) CodeGuideNavy else CodeGuidePasskeyBg)
                        .border(
                            1.dp,
                            if (isSelected) CodeGuideNavy else CodeGuideBorder,
                            RoundedCornerShape(50.dp)
                        )
                        .clickable { onStatusSelected(status) }
                        .padding(horizontal = 14.dp, vertical = 7.dp)
                ) {
                    Text(
                        text = status.label,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) Color.White else CodeGuideNavy
                    )
                }
            }
        }
    }
}

@Composable
private fun GenericProjectCard(
    project: ProjectItem,
    onStartTask: () -> Unit,
    onDelete: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = CodeGuideSurface,
        border = BorderStroke(1.dp, CodeGuideBorder)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(CodeGuidePasskeyBg),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Dns,
                            contentDescription = "Repo",
                            tint = CodeGuideNavy,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = project.title,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = CodeGuideNavy
                        )
                        Text(
                            text = project.techStack,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            color = CodeGuideTextSecondary
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50.dp))
                            .background(CodeGuidePasskeyBg)
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = project.phaseOrBadge,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            color = CodeGuideLink
                        )
                    }

                    IconButton(onClick = onDelete) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Borrar Proyecto",
                            tint = Color(0xFFE11D48),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Progreso de ejecución",
                    fontSize = 12.sp,
                    color = CodeGuideTextSecondary
                )
                Text(
                    text = "${project.completedTasks}/${project.totalTasks} micro-tareas (${project.progressPercentage}%)",
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = CodeGuideLink
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            LinearProgressIndicator(
                progress = { project.completedTasks.toFloat() / project.totalTasks },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = CodeGuideNavy,
                trackColor = CodeGuideBorder
            )

            if (project.nextTaskTitle.isNotBlank()) {
                Spacer(modifier = Modifier.height(14.dp))

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    color = CodeGuidePasskeyBg
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "PRÓXIMO PASO",
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                color = CodeGuideNavy
                            )

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50.dp))
                                    .background(Color.White)
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = project.nextTaskPriority,
                                    fontSize = 10.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = CodeGuideNavy
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = project.nextTaskTitle,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = CodeGuideNavy
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = project.nextTaskTag,
                                fontSize = 11.sp,
                                color = CodeGuideTextSecondary
                            )

                            Button(
                                onClick = onStartTask,
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = CodeGuideNavy),
                                contentPadding = PaddingValues(
                                    horizontal = 12.dp,
                                    vertical = 4.dp
                                )
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Iniciar",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                        contentDescription = "Go",
                                        tint = Color.White,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NewProjectButton(onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp),
        shape = RoundedCornerShape(16.dp),
        color = CodeGuideNavy
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 18.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.AddCircleOutline,
                    contentDescription = "Add",
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Nuevo Proyecto / Sincronizar Repo",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White.copy(alpha = 0.15f))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "Git / Jira",
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateProjectBottomSheet(
    onDismiss: () -> Unit,
    onCreateProject: (String, String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var techStack by remember { mutableStateOf("") }
    var repoUrl by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = CodeGuideSurface,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Nuevo Proyecto",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = CodeGuideNavy
                )
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = CodeGuideTextSecondary
                    )
                }
            }

            Text(
                text = "Define tu nuevo objetivo o conecta un repositorio remoto para desglosar tus tareas automáticas.",
                fontSize = 13.sp,
                color = CodeGuideTextSecondary
            )

            Text(
                text = "Nombre del Proyecto",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = CodeGuideTextPrimary
            )
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                placeholder = { Text("Ej: Microservicio de Pagos", fontSize = 14.sp) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = CodeGuideTextPrimary,
                    unfocusedTextColor = CodeGuideTextPrimary,
                    focusedBorderColor = CodeGuideTeal,
                    unfocusedBorderColor = CodeGuideBorder
                ),
                singleLine = true
            )

            Text(
                text = "Stack Tecnológico (Lenguaje, DB, Framework)",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = CodeGuideTextPrimary
            )
            OutlinedTextField(
                value = techStack,
                onValueChange = { techStack = it },
                placeholder = { Text("Ej: Kotlin • Spring Boot • Postgres", fontSize = 14.sp) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = CodeGuideTextPrimary,
                    unfocusedTextColor = CodeGuideTextPrimary,
                    focusedBorderColor = CodeGuideTeal,
                    unfocusedBorderColor = CodeGuideBorder
                ),
                singleLine = true
            )

            Text(
                text = "URL Repositorio Git (Opcional)",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = CodeGuideTextPrimary
            )
            OutlinedTextField(
                value = repoUrl,
                onValueChange = { repoUrl = it },
                placeholder = { Text("https://github.com/empresa/repo.git", fontSize = 14.sp) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = CodeGuideTextPrimary,
                    unfocusedTextColor = CodeGuideTextPrimary,
                    focusedBorderColor = CodeGuideTeal,
                    unfocusedBorderColor = CodeGuideBorder
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onCreateProject(title, techStack, repoUrl)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CodeGuideNavy)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AddCircleOutline,
                        contentDescription = "Create",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Crear e Iniciar Desglose",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
