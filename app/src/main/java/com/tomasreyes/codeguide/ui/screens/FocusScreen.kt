package com.tomasreyes.codeguide.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Laptop
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
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
 * Pantalla: "Enfoque" (Temporizador Pomodoro / Sesión de Flujo sin Interrupciones)
 * Maquetación stateless basada en el diseño de CodeGuide.
 */
@Composable
fun FocusScreen(
    onNavigateToToday: () -> Unit = {},
    onNavigateToProjects: () -> Unit = {},
    onNavigateToMetrics: () -> Unit = {}
) {
    Scaffold(
        bottomBar = {
            CodeGuideBottomNavigationBar(
                selectedTab = "Enfoque",
                onTabSelected = { tab ->
                    when (tab) {
                        "Hoy" -> onNavigateToToday()
                        "Proyectos" -> onNavigateToProjects()
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
                // 1. Encabezado Superior (Logo + ENFOQUE + Racha + Avatar)
                HeaderBarFocus()

                // 2. Sesión de Flujo
                FlowSessionHeaderCard()

                // 3. Nivel de Energía & Mood Selector
                EnergyAndMoodCard()

                // 4. Temporizador de Enfoque Circular ("08:16")
                CircularTimerCard()

                // 5. Tarjeta de Micro-tarea Activa
                ActiveMicroTaskCard()

                // 6. Botones de Control Principal (Pausar / Completar / Micro-break)
                FocusControlButtons()

                // 7. Audio de Acompañamiento
                AmbientAudioCard()

                // 8. Cita de Arquitectura Mental
                MentalArchitectureQuoteCard()

                // 9. Consejos de Ritmo Sostenible
                SustainableRhythmCard()

                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

// 1. Encabezado Superior Focus
@Composable
private fun HeaderBarFocus() {
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
                    text = "ENFOQUE",
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

// 2. Tarjeta: Sesión de Flujo Header
@Composable
private fun FlowSessionHeaderCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = CodeGuideSurface,
        border = BorderStroke(1.dp, CodeGuideBorder)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(CodeGuidePasskeyBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Psychology,
                        contentDescription = "Brain",
                        tint = CodeGuideLink,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text(
                        text = "SESIÓN DE FLUJO",
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = CodeGuideTextSecondary
                    )
                    Text(
                        text = "Modo Enfoque ...",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = CodeGuideNavy
                    )
                }
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .background(CodeGuidePasskeyBg)
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(CodeGuideTeal)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Sprint #4",
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

// 3. Tarjeta: Nivel de Energía & Mood
@Composable
private fun EnergyAndMoodCard() {
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
                    text = "NIVEL DE ENERGÍA & MOOD",
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
                        text = "25 min estándar",
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        color = CodeGuideLink
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "¿Cómo te sientes para abordar este bloque?",
                fontSize = 12.sp,
                color = CodeGuideTextSecondary
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Modo 1: Zen / Flow (Seleccionado)
                MoodCard(
                    modifier = Modifier.weight(1f),
                    emoji = "🧘",
                    title = "Zen / Flow",
                    duration = "25 min",
                    isSelected = true
                )

                // Modo 2: Energético
                MoodCard(
                    modifier = Modifier.weight(1f),
                    emoji = "⚡",
                    title = "Energético",
                    duration = "45 min",
                    isSelected = false
                )

                // Modo 3: Ligero / Fatiga
                MoodCard(
                    modifier = Modifier.weight(1f),
                    emoji = "☕",
                    title = "Ligero / Fatiga",
                    duration = "15 min",
                    isSelected = false
                )
            }
        }
    }
}

@Composable
private fun MoodCard(
    modifier: Modifier = Modifier,
    emoji: String,
    title: String,
    duration: String,
    isSelected: Boolean
) {
    Surface(
        modifier = modifier.height(86.dp),
        shape = RoundedCornerShape(14.dp),
        color = if (isSelected) CodeGuidePasskeyBg else CodeGuideSurface,
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) CodeGuideNavy else CodeGuideBorder
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = emoji, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = CodeGuideNavy
            )
            Text(
                text = duration,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                color = CodeGuideTextSecondary
            )
        }
    }
}

// 4. Tarjeta: Temporizador Circular
@Composable
private fun CircularTimerCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = CodeGuideSurface,
        border = BorderStroke(1.dp, CodeGuideBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(200.dp)
            ) {
                // Anillo de progreso circular Canvas
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val strokeWidth = 14.dp.toPx()
                    // Fondo del anillo
                    drawCircle(
                        color = Color(0xFFE2E8F0),
                        style = Stroke(width = strokeWidth)
                    )
                    // Arco del anillo activo (~45% transcurrido)
                    drawArc(
                        brush = Brush.sweepGradient(
                            colors = listOf(
                                CodeGuideTeal,
                                CodeGuideCyan,
                                CodeGuideTeal
                            )
                        ),
                        startAngle = -90f,
                        sweepAngle = 160f,
                        useCenter = false,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "TIEMPO RESTANTE",
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = CodeGuideTextSecondary,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "08:16",
                        fontSize = 38.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif,
                        color = CodeGuideNavy
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50.dp))
                            .background(CodeGuidePasskeyBg)
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Bloque activo",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = CodeGuideLink
                        )
                    }
                }
            }
        }
    }
}

// 5. Tarjeta: Micro-tarea Activa
@Composable
private fun ActiveMicroTaskCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = CodeGuidePasskeyBg,
        border = BorderStroke(1.dp, CodeGuideBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(CodeGuideSurface),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Code,
                            contentDescription = "Task",
                            tint = CodeGuideNavy,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "MICRO-TAREA 2 DE 4",
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = CodeGuideNavy
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(Color.White)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "~12m est.",
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        color = CodeGuideTeal
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Definir middleware de validación JWT",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = CodeGuideNavy
            )

            Spacer(modifier = Modifier.height(10.dp))

            LinearProgressIndicator(
                progress = { 0.45f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = CodeGuideNavy,
                trackColor = CodeGuideBorder
            )
        }
    }
}

// 6. Botones de Control Principal
@Composable
private fun FocusControlButtons() {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Botón Pausar
            Button(
                onClick = { },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CodeGuideNavy)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Pause,
                        contentDescription = "Pause",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Pausar",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            // Botón Completar
            Button(
                onClick = { },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDBEAFE))
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Complete",
                        tint = CodeGuideNavy,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Completar",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = CodeGuideNavy
                    )
                }
            }
        }

        // Botón Micro-break
        OutlinedButton(
            onClick = { },
            modifier = Modifier
                .fillMaxWidth()
                .height(46.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(containerColor = CodeGuideSurface),
            border = BorderStroke(1.dp, CodeGuideBorder)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Coffee,
                    contentDescription = "Coffee",
                    tint = CodeGuideTeal,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Tomar micro-break de 5 min",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = CodeGuideNavy
                )
            }
        }
    }
}

// 7. Tarjeta: Audio de Acompañamiento
@Composable
private fun AmbientAudioCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = CodeGuideSurface,
        border = BorderStroke(1.dp, CodeGuideBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Headphones,
                        contentDescription = "Audio",
                        tint = CodeGuideNavy,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "AUDIO DE ACOMPAÑAMIENTO",
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = CodeGuideTextSecondary
                    )
                }

                Text(
                    text = "Lofi Coding",
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = CodeGuideNavy
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AudioOptionChip(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.GraphicEq,
                    text = "Lofi Coding",
                    isSelected = true
                )

                AudioOptionChip(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.WaterDrop,
                    text = "Lluvia suave",
                    isSelected = false
                )

                AudioOptionChip(
                    modifier = Modifier.weight(1f),
                    icon = Icons.AutoMirrored.Filled.VolumeOff,
                    text = "Silencio",
                    isSelected = false
                )
            }
        }
    }
}

@Composable
private fun AudioOptionChip(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    text: String,
    isSelected: Boolean
) {
    Surface(
        modifier = modifier.height(44.dp),
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) CodeGuidePasskeyBg else CodeGuideSurface,
        border = BorderStroke(
            width = if (isSelected) 1.5.dp else 1.dp,
            color = if (isSelected) CodeGuideNavy else CodeGuideBorder
        )
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = if (isSelected) CodeGuideNavy else CodeGuideTextSecondary,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = text,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = CodeGuideNavy
            )
        }
    }
}

// 8. Cita de Arquitectura Mental
@Composable
private fun MentalArchitectureQuoteCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = CodeGuidePasskeyBg
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Check",
                tint = CodeGuideLink,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = "\"Un commit a la vez. Sin prisa pero sin pausas.\"",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = CodeGuideNavy
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Arquitectura Mental · CodeGuide Flow",
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    color = CodeGuideTextSecondary
                )
            }
        }
    }
}

// 9. Consejos de Ritmo Sostenible
@Composable
private fun SustainableRhythmCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = CodeGuideSurface,
        border = BorderStroke(1.dp, CodeGuideBorder)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(CodeGuidePasskeyBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Laptop,
                    contentDescription = "Desk",
                    tint = CodeGuideNavy,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Eco,
                        contentDescription = "Leaf",
                        tint = CodeGuideTeal,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "RITMO SOSTENIBLE",
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = CodeGuideTeal
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Mantener pausas cortas previene la fatiga en módulos complejos.",
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
fun FocusScreenPreview() {
    CodeGuideTheme {
        FocusScreen()
    }
}
