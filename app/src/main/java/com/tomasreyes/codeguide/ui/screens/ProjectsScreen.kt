package com.tomasreyes.codeguide.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Laptop
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.Warning
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tomasreyes.codeguide.ui.components.CodeGuideLogo
import com.tomasreyes.codeguide.ui.theme.CodeGuideBackground
import com.tomasreyes.codeguide.ui.theme.CodeGuideBadgeBg
import com.tomasreyes.codeguide.ui.theme.CodeGuideBorder
import com.tomasreyes.codeguide.ui.theme.CodeGuideCyan
import com.tomasreyes.codeguide.ui.theme.CodeGuideLink
import com.tomasreyes.codeguide.ui.theme.CodeGuideNavy
import com.tomasreyes.codeguide.ui.theme.CodeGuidePasskeyBg
import com.tomasreyes.codeguide.ui.theme.CodeGuideSurface
import com.tomasreyes.codeguide.ui.theme.CodeGuideTeal
import com.tomasreyes.codeguide.ui.theme.CodeGuideTextPrimary
import com.tomasreyes.codeguide.ui.theme.CodeGuideTextSecondary
import com.tomasreyes.codeguide.ui.theme.CodeGuideTheme

/**
 * Pantalla: "Proyectos" (Gestión y Seguimiento de Proyectos en CodeGuide)
 * Maquetación limpia para el seguimiento de proyectos activos, métricas cognitivas y creación de nuevos proyectos.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectsScreen(
    onNavigateToToday: () -> Unit = {},
    onNavigateToFocus: () -> Unit = {},
    onNavigateToMetrics: () -> Unit = {}
) {
    var showCreateModal by remember { mutableStateOf(false) }

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
                // 1. Encabezado Superior (Logo + PROYECTOS + Racha + Avatar)
                HeaderBarProjects()

                // 2. Metría de Carga Cognitiva
                CognitiveLoadMetricCard()

                // 3. Barra de Búsqueda y Filtros
                SearchAndFilterSection()

                // 4. Lista de Proyectos Activos
                // Proyecto 1: E-Commerce Microservices
                ECommerceProjectCard()

                // Proyecto 2: App Móvil Flutter
                FlutterAppProjectCard()

                // Proyecto 3: CLI Tool de Automatización
                CliToolProjectCard()

                // 5. Botón de Nuevo Proyecto / Sincronizar Repo
                NewProjectButton(onClick = { showCreateModal = true })

                Spacer(modifier = Modifier.height(12.dp))
            }

            // Modal / Bottom Sheet para crear un nuevo proyecto
            if (showCreateModal) {
                CreateProjectBottomSheet(onDismiss = { showCreateModal = false })
            }
        }
    }
}

// 1. Encabezado de la Pantalla Proyectos
@Composable
private fun HeaderBarProjects() {
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

            Box(contentAlignment = Alignment.BottomEnd) {
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

// 2. Metría de Carga Cognitiva
@Composable
private fun CognitiveLoadMetricCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = CodeGuideSurface,
        border = BorderStroke(1.dp, CodeGuideBorder)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Header Row: Dot + Tag + Score Pill
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

            // Main Metric Row: Title/Subtitle + Circular Indicator
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

                // Circular Progress indicator (65%)
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

            Spacer(modifier = Modifier.height(14.dp))

            // 3 Summary Chips: Profundo, Táctico, Descanso
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MetricSummaryChip(
                    modifier = Modifier.weight(1f),
                    label = "Profundo",
                    value = "3.5 hrs",
                    isHighlight = false
                )

                MetricSummaryChip(
                    modifier = Modifier.weight(1f),
                    label = "Táctico",
                    value = "2.0 hrs",
                    isHighlight = false
                )

                MetricSummaryChip(
                    modifier = Modifier.weight(1f),
                    label = "Descanso",
                    value = "Regenerado",
                    isHighlight = true
                )
            }
        }
    }
}

@Composable
private fun MetricSummaryChip(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    isHighlight: Boolean
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = CodeGuidePasskeyBg
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = label,
                fontSize = 10.sp,
                color = CodeGuideTextSecondary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                color = if (isHighlight) CodeGuideTeal else CodeGuideNavy
            )
        }
    }
}

// 3. Sección de Búsqueda y Filtros
@Composable
private fun SearchAndFilterSection() {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        // Campo de búsqueda
        OutlinedTextField(
            value = "",
            onValueChange = {},
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
                focusedContainerColor = CodeGuideSurface,
                unfocusedContainerColor = CodeGuideSurface,
                focusedBorderColor = CodeGuideTeal,
                unfocusedBorderColor = CodeGuideBorder
            ),
            singleLine = true
        )

        // Chips de estado de filtro
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // En curso (3) - Seleccionado
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .background(CodeGuideNavy)
                    .padding(horizontal = 14.dp, vertical = 7.dp)
            ) {
                Text(
                    text = "En curso (3)",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            // Pausados (1)
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .background(CodeGuidePasskeyBg)
                    .border(1.dp, CodeGuideBorder, RoundedCornerShape(50.dp))
                    .padding(horizontal = 14.dp, vertical = 7.dp)
            ) {
                Text(
                    text = "Pausados (1)",
                    fontSize = 12.sp,
                    color = CodeGuideNavy
                )
            }

            // Completados (8)
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .background(CodeGuidePasskeyBg)
                    .border(1.dp, CodeGuideBorder, RoundedCornerShape(50.dp))
                    .padding(horizontal = 14.dp, vertical = 7.dp)
            ) {
                Text(
                    text = "Completados (8)",
                    fontSize = 12.sp,
                    color = CodeGuideNavy
                )
            }
        }
    }
}

// 4. Proyecto 1: E-Commerce Microservices
@Composable
private fun ECommerceProjectCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = CodeGuideSurface,
        border = BorderStroke(1.dp, CodeGuideBorder)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Header del Proyecto: Ícono + Nombre + Pill Módulos
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(CodeGuidePasskeyBg),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Dns,
                            contentDescription = "Database",
                            tint = CodeGuideNavy,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = "E-Commerce ...",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = CodeGuideNavy
                        )
                        Text(
                            text = "Go • Kafka • PostgreSQL",
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            color = CodeGuideTextSecondary
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(CodeGuidePasskeyBg)
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = "2 módulos listos",
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        color = CodeGuideLink
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Progreso de ejecucion
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
                    text = "18/24 micro-tareas (75%)",
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = CodeGuideLink
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            LinearProgressIndicator(
                progress = { 0.75f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = CodeGuideLink,
                trackColor = CodeGuideBorder
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Subtarjeta: Próxima Micro-tarea (Priority Red/Orange Tint)
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = Color(0xFFFFF1F2)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "❗", fontSize = 12.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "PRÓXIMA MICRO-TAREA",
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFE11D48)
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50.dp))
                                .background(Color(0xFFFFE4E6))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "Prioridad Alta • ~30m",
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFE11D48)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Optimizar queries en Postgres y pooling de conexiones",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = CodeGuideNavy,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = "Loc",
                                tint = CodeGuideTextSecondary,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Arquitectura · Alto foco",
                                fontSize = 11.sp,
                                color = CodeGuideTextSecondary
                            )
                        }

                        Button(
                            onClick = { },
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

            Spacer(modifier = Modifier.height(12.dp))

            // Footer del Proyecto: Colaboradores + Sync
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CollaboratorAvatar(text = "PG", bg = CodeGuideNavy)
                    CollaboratorAvatar(text = "KR", bg = Color(0xFF1E293B))
                    CollaboratorAvatar(text = "+2", bg = CodeGuidePasskeyBg, textColor = CodeGuideNavy)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = "Sync",
                        tint = CodeGuideTextSecondary,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Sync hace 12m",
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        color = CodeGuideTextSecondary
                    )
                }
            }
        }
    }
}

@Composable
private fun CollaboratorAvatar(text: String, bg: Color, textColor: Color = Color.White) {
    Box(
        modifier = Modifier
            .size(24.dp)
            .clip(CircleShape)
            .background(bg)
            .border(1.dp, Color.White, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 9.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}

// 5. Proyecto 2: App Móvil Flutter
@Composable
private fun FlutterAppProjectCard() {
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
                            .size(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(CodeGuidePasskeyBg),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Smartphone,
                            contentDescription = "Mobile",
                            tint = CodeGuideNavy,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = "App Móvil Flutter ...",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = CodeGuideNavy
                        )
                        Text(
                            text = "Dart • Riverpod • Clean Arch",
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            color = CodeGuideTextSecondary
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(CodeGuidePasskeyBg)
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = "Fase 2 de 4",
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        color = CodeGuideNavy
                    )
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
                    text = "6/15 micro-tareas (40%)",
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = CodeGuideLink
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            LinearProgressIndicator(
                progress = { 0.40f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = CodeGuideNavy,
                trackColor = CodeGuideBorder
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Subtarjeta: Paso Ideal para Empezar
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Eco,
                                contentDescription = "Leaf",
                                tint = CodeGuideTeal,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "PASO IDEAL PARA EMPEZAR",
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                color = CodeGuideTeal
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50.dp))
                                .background(Color.White)
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "Baja Energía • ~20m",
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace,
                                color = CodeGuideNavy
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Crear componentes atómicos de botón y tarjeta base",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = CodeGuideNavy,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Explore,
                                contentDescription = "UI Kit",
                                tint = CodeGuideTextSecondary,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "UI Kit · Modo Flow Ligero",
                                fontSize = 11.sp,
                                color = CodeGuideTextSecondary
                            )
                        }

                        Button(
                            onClick = { },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            border = BorderStroke(1.dp, CodeGuideBorder),
                            contentPadding = PaddingValues(
                                horizontal = 12.dp,
                                vertical = 4.dp
                            )
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Explorar",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CodeGuideNavy
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = "Go",
                                    tint = CodeGuideNavy,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Folder,
                        contentDescription = "Folder",
                        tint = CodeGuideTextSecondary,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "5 pantallas pendientes",
                        fontSize = 11.sp,
                        color = CodeGuideTextSecondary
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Code,
                        contentDescription = "Branch",
                        tint = CodeGuideTextSecondary,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Rama: feature/ds-tokens",
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        color = CodeGuideTextSecondary
                    )
                }
            }
        }
    }
}

// 6. Proyecto 3: CLI Tool de Automatización
@Composable
private fun CliToolProjectCard() {
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
                            .size(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(CodeGuidePasskeyBg),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Terminal,
                            contentDescription = "Terminal",
                            tint = CodeGuideNavy,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = "CLI Tool de Auto...",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = CodeGuideNavy
                        )
                        Text(
                            text = "Rust • Clap • GitHub Actions",
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            color = CodeGuideTextSecondary
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(CodeGuideBadgeBg)
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = "☑ Casi listo",
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = CodeGuideTeal
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Sprint final",
                    fontSize = 12.sp,
                    color = CodeGuideTextSecondary
                )
                Text(
                    text = "18/20 micro-tareas (90%)",
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = CodeGuideTeal
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            LinearProgressIndicator(
                progress = { 0.90f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = CodeGuideTeal,
                trackColor = CodeGuideBorder
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Subtarjeta: Cierre de Versión
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Check",
                                tint = CodeGuideNavy,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "CIERRE DE VERSIÓN",
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                color = CodeGuideNavy
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50.dp))
                                .background(Color.White)
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "Revisión • ~15m",
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace,
                                color = CodeGuideNavy
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Generar binarios multi-arquitectura y release v1.0",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = CodeGuideNavy,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.RocketLaunch,
                                contentDescription = "Rocket",
                                tint = CodeGuideTeal,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Despliegue CI/CD",
                                fontSize = 11.sp,
                                color = CodeGuideTextSecondary
                            )
                        }

                        Button(
                            onClick = { },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = CodeGuideBadgeBg),
                            contentPadding = PaddingValues(
                                horizontal = 12.dp,
                                vertical = 4.dp
                            )
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Publicar",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CodeGuideTeal
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Check",
                                    tint = CodeGuideTeal,
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

// 7. Botón Nuevo Proyecto / Sincronizar Repo
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

// 8. Bottom Sheet Maquetación para crear un Nuevo Proyecto
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateProjectBottomSheet(onDismiss: () -> Unit) {
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
            // Header modal
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

            // Campo Nombre
            Text(
                text = "Nombre del Proyecto",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = CodeGuideTextPrimary
            )
            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("Ej: Microservicio de Pagos", fontSize = 14.sp) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CodeGuideTeal,
                    unfocusedBorderColor = CodeGuideBorder
                ),
                singleLine = true
            )

            // Campo Stack Tecnológico
            Text(
                text = "Stack Tecnológico (Lenguaje, DB, Framework)",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = CodeGuideTextPrimary
            )
            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("Ej: Kotlin • Spring Boot • Postgres", fontSize = 14.sp) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CodeGuideTeal,
                    unfocusedBorderColor = CodeGuideBorder
                ),
                singleLine = true
            )

            // Campo Repo Git
            Text(
                text = "URL Repositorio Git (Opcional)",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = CodeGuideTextPrimary
            )
            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("https://github.com/empresa/repo.git", fontSize = 14.sp) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CodeGuideTeal,
                    unfocusedBorderColor = CodeGuideBorder
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Botón Crear Proyecto
            Button(
                onClick = onDismiss,
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

@Preview(showBackground = true)
@Composable
fun ProjectsScreenPreview() {
    CodeGuideTheme {
        ProjectsScreen()
    }
}
