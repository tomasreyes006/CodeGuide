package com.tomasreyes.codeguide.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tomasreyes.codeguide.ui.components.CodeGuideBottomNavigationBar
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
import com.tomasreyes.codeguide.ui.theme.CodeGuideTextSecondary
import com.tomasreyes.codeguide.ui.theme.CodeGuideTheme
import com.tomasreyes.codeguide.ui.viewmodel.CodeGuideViewModel

/**
 * Pantalla: "Métricas" (Estadísticas Dinámicas con ViewModel)
 */
@Composable
fun MetricsScreen(
    viewModel: CodeGuideViewModel = viewModel(),
    onNavigateToToday: () -> Unit = {},
    onNavigateToProjects: () -> Unit = {},
    onNavigateToFocus: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedPeriod by remember { mutableStateOf("Esta semana") }

    Scaffold(
        bottomBar = {
            CodeGuideBottomNavigationBar(
                selectedTab = "Métricas",
                onTabSelected = { tab ->
                    when (tab) {
                        "Hoy" -> onNavigateToToday()
                        "Proyectos" -> onNavigateToProjects()
                        "Enfoque" -> onNavigateToFocus()
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
                HeaderBarMetrics(streakDays = uiState.streakDays)

                TitleAndPeriodSelector(
                    selectedPeriod = selectedPeriod,
                    onPeriodSelected = { selectedPeriod = it }
                )

                MentalClarityIndexCard(clarityIndex = uiState.clarityIndex)

                CircadianRhythmCard(totalFocusHours = (uiState.totalFocusMinutes / 60f))

                MicroBlocksAndHabitsSection(
                    completedCount = uiState.completedDailyTasksCount,
                    totalCount = uiState.totalDailyTasksCount,
                    progressPercentage = uiState.dailyProgressPercentage,
                    streakDays = uiState.streakDays
                )

                MentalHealthCopilotCard()

                ProjectLoadCard()

                QualityOverVolumeCard()

                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun HeaderBarMetrics(streakDays: Int) {
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
                    text = "MÉTRICAS",
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
                        text = "${streakDays}d",
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

@Composable
private fun TitleAndPeriodSelector(
    selectedPeriod: String,
    onPeriodSelected: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Métricas & Bienestar",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = CodeGuideNavy
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Rendimiento cognitivo y prevención de agotamiento",
                    fontSize = 12.sp,
                    color = CodeGuideTextSecondary
                )
            }

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = CodeGuidePasskeyBg,
                border = BorderStroke(1.dp, CodeGuideBorder),
                modifier = Modifier.size(38.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = CodeGuideNavy,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("Esta semana", "Sprint actual", "Mes (Jun)").forEach { period ->
                val isSelected = period == selectedPeriod
                Surface(
                    onClick = { onPeriodSelected(period) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    color = if (isSelected) CodeGuideSurface else CodeGuidePasskeyBg,
                    border = if (isSelected) BorderStroke(1.5.dp, CodeGuideBorder) else null
                ) {
                    Box(
                        modifier = Modifier.padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = period,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) CodeGuideNavy else CodeGuideTextSecondary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MentalClarityIndexCard(clarityIndex: Int) {
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
                Text(
                    text = "ÍNDICE DE CLARIDAD MENTAL",
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = CodeGuideTextSecondary
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(CodeGuidePasskeyBg)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Riesgo fatiga: Muy bajo",
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        color = CodeGuideTeal
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "$clarityIndex",
                        fontSize = 34.sp,
                        fontWeight = FontWeight.Bold,
                        color = CodeGuideNavy
                    )
                    Text(
                        text = " /100",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = CodeGuideTextSecondary,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }

                Text(
                    text = "+4% vs. sprint anterior",
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    color = CodeGuideTextSecondary
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(CodeGuideTeal)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Flujo Sostenible",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = CodeGuideTeal
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = CodeGuidePasskeyBg
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = "Bolt",
                            tint = CodeGuideLink,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Sobrecarga cognitiva mitigada",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = CodeGuideNavy
                        )
                    }

                    Text(
                        text = "94%",
                        fontSize = 13.sp,
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
private fun CircadianRhythmCard(totalFocusHours: Float) {
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
                Text(
                    text = "Ritmo Circadiano & Foco",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = CodeGuideNavy
                )

                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(CodeGuidePasskeyBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Analytics,
                        contentDescription = "Analytics",
                        tint = CodeGuideLink,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Text(
                text = "Ventana óptima para escribir arquitectura y lógica pura",
                fontSize = 12.sp,
                color = CodeGuideTextSecondary
            )

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = CodeGuidePasskeyBg
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CheckCircleOutline,
                            contentDescription = "Check",
                            tint = CodeGuideLink,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Horario Pico\nIdentificado",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = CodeGuideNavy,
                            lineHeight = 14.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.White)
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "10:00 AM - 12:30 PM",
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = CodeGuideNavy
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val width = size.width
                    val height = size.height

                    val zonePath = Path().apply {
                        moveTo(width * 0.22f, height)
                        lineTo(width * 0.22f, height * 0.1f)
                        lineTo(width * 0.45f, height * 0.1f)
                        lineTo(width * 0.45f, height)
                        close()
                    }
                    drawPath(
                        path = zonePath,
                        color = Color(0xFFEDF5FD)
                    )

                    val wavePath = Path().apply {
                        moveTo(0f, height * 0.75f)
                        cubicTo(
                            width * 0.15f, height * 0.65f,
                            width * 0.22f, height * 0.1f,
                            width * 0.32f, height * 0.12f
                        )
                        cubicTo(
                            width * 0.45f, height * 0.15f,
                            width * 0.52f, height * 0.85f,
                            width * 0.60f, height * 0.75f
                        )
                        cubicTo(
                            width * 0.75f, height * 0.5f,
                            width * 0.88f, height * 0.6f,
                            width, height * 0.7f
                        )
                    }

                    drawPath(
                        path = wavePath,
                        color = Color(0xFF0284C7),
                        style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                    )

                    drawCircle(
                        color = CodeGuideTeal,
                        radius = 5.dp.toPx(),
                        center = Offset(width * 0.32f, height * 0.12f)
                    )
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(start = 70.dp, top = 4.dp)
                        .clip(RoundedCornerShape(50.dp))
                        .background(CodeGuidePasskeyBg)
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "ZONA FLOW",
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = CodeGuideLink
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "08:00", fontSize = 10.sp, fontFamily = FontFamily.Monospace, color = CodeGuideTextSecondary)
                Text(text = "11:00 (Pico)", fontSize = 10.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, color = CodeGuideNavy)
                Text(text = "14:30 (Dip)", fontSize = 10.sp, fontFamily = FontFamily.Monospace, color = CodeGuideTextSecondary)
                Text(text = "17:00", fontSize = 10.sp, fontFamily = FontFamily.Monospace, color = CodeGuideTextSecondary)
                Text(text = "20:00", fontSize = 10.sp, fontFamily = FontFamily.Monospace, color = CodeGuideTextSecondary)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CircadianSummaryChip(
                    modifier = Modifier.weight(1f),
                    title = "Deep Work",
                    value = String.format("%.1f h", totalFocusHours),
                    tag = "Alto foco",
                    isTeal = false
                )

                CircadianSummaryChip(
                    modifier = Modifier.weight(1f),
                    title = "Táctico / Docs",
                    value = "6.2 h",
                    tag = "Baja carga",
                    isTeal = false
                )

                CircadianSummaryChip(
                    modifier = Modifier.weight(1f),
                    title = "Micro-breaks",
                    value = "18 pausas",
                    tag = "Cumplido",
                    isTeal = true
                )
            }
        }
    }
}

@Composable
private fun CircadianSummaryChip(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    tag: String,
    isTeal: Boolean
) {
    Surface(
        modifier = modifier.height(78.dp),
        shape = RoundedCornerShape(12.dp),
        color = CodeGuidePasskeyBg
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = title, fontSize = 10.sp, color = CodeGuideTextSecondary)
                Text(text = value, fontSize = 13.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, color = CodeGuideNavy)
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(if (isTeal) CodeGuideBadgeBg else Color.White)
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = tag,
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = if (isTeal) CodeGuideTeal else CodeGuideLink
                )
            }
        }
    }
}

@Composable
private fun MicroBlocksAndHabitsSection(
    completedCount: Int,
    totalCount: Int,
    progressPercentage: Int,
    streakDays: Int
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Micro-bloques & Hábitos",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = CodeGuideNavy
            )

            Text(
                text = "Semana 24",
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                color = CodeGuideLink
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            HabitMetricItem(
                icon = Icons.Default.CheckCircle,
                title = "Micro-tareas logradas",
                score = "$progressPercentage%",
                subtitle = "$completedCount de $totalCount completadas hoy",
                tag = "ÓPTIMO",
                isTealTag = true
            )

            HabitMetricItem(
                icon = Icons.Default.Timer,
                title = "Tiempo por bloque de código",
                score = "24 min",
                subtitle = "Ventana recomendada: 20–30 min",
                tag = "SALUDABLE",
                isTealTag = false
            )

            HabitMetricItem(
                icon = Icons.Default.LocalFireDepartment,
                title = "Racha en ritmo óptimo",
                score = "$streakDays d",
                subtitle = "Días sin picos de fatiga aguda",
                tag = "RÉCORD PERSONAL",
                isTealTag = false
            )
        }
    }
}

@Composable
private fun HabitMetricItem(
    icon: ImageVector,
    title: String,
    score: String,
    subtitle: String,
    tag: String,
    isTealTag: Boolean
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = CodeGuideSurface,
        border = BorderStroke(1.dp, CodeGuideBorder)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(CodeGuidePasskeyBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = CodeGuideNavy,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = CodeGuideNavy
                    )
                    Text(
                        text = score,
                        fontSize = 18.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = CodeGuideNavy
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = subtitle,
                        fontSize = 11.sp,
                        color = CodeGuideTextSecondary
                    )
                    Text(
                        text = tag,
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = if (isTealTag) CodeGuideTeal else CodeGuideLink
                    )
                }
            }
        }
    }
}

@Composable
private fun MentalHealthCopilotCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = CodeGuideNavy
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Psychology,
                    contentDescription = "Copilot",
                    tint = CodeGuideCyan,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "COPILOTO DE SALUD MENTAL",
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Patrón de Fatiga en Tardes",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Los jueves después de las 3:30 PM tu retención en debug baja un 30%. Te recomendamos agendar revisiones de PRs o refactorización ligera en lugar de resolver arquitectura core.",
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.8f),
                lineHeight = 17.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = { },
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = "Schedule",
                            tint = CodeGuideNavy,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Ajustar descansos guiados",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = CodeGuideNavy
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White.copy(alpha = 0.15f),
                    modifier = Modifier.size(44.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Check",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProjectLoadCard() {
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
                Text(
                    text = "Carga por Proyecto",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = CodeGuideNavy
                )

                Icon(
                    imageVector = Icons.Default.Public,
                    contentDescription = "Globe",
                    tint = CodeGuideTextSecondary,
                    modifier = Modifier.size(18.dp)
                )
            }

            Text(
                text = "Esfuerzo mental ponderado según lenguaje y alcance",
                fontSize = 12.sp,
                color = CodeGuideTextSecondary
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(CodeGuideNavy))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "E-Commerce Microservices (Go)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = CodeGuideNavy
                    )
                }

                Text(
                    text = "72% (Moderada)",
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    color = CodeGuideTextSecondary
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            LinearProgressIndicator(
                progress = { 0.72f },
                modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                color = CodeGuideNavy,
                trackColor = CodeGuideBorder
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(CodeGuideTeal))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "App Móvil Multiplataforma (Flutter)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = CodeGuideNavy
                    )
                }

                Text(
                    text = "28% (Ligera)",
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    color = CodeGuideTextSecondary
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            LinearProgressIndicator(
                progress = { 0.28f },
                modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                color = CodeGuideTeal,
                trackColor = CodeGuideBorder
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = "Shield",
                    tint = CodeGuideLink,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Sin detección de deuda de sueño o sobrecarga consecutiva.",
                    fontSize = 11.sp,
                    color = CodeGuideTextSecondary,
                    lineHeight = 15.sp
                )
            }
        }
    }
}

@Composable
private fun QualityOverVolumeCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = CodeGuideSurface,
        border = BorderStroke(1.dp, CodeGuideBorder)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(CodeGuidePasskeyBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Spa,
                    contentDescription = "Sprout",
                    tint = CodeGuideTeal,
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = "Calidad sobre volumen",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = CodeGuideNavy
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "El código limpio nace de mentes despejadas. 5 días constantes valen más que un sprint con desveladas.",
                    fontSize = 12.sp,
                    color = CodeGuideTextSecondary,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MetricsScreenPreview() {
    CodeGuideTheme {
        MetricsScreen()
    }
}
