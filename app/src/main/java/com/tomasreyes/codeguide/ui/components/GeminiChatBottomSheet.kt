package com.tomasreyes.codeguide.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.ai.client.generativeai.GenerativeModel
import com.tomasreyes.codeguide.data.model.EnergyLevel
import com.tomasreyes.codeguide.ui.theme.CodeGuideCyan
import com.tomasreyes.codeguide.ui.theme.CodeGuideLink
import com.tomasreyes.codeguide.ui.theme.CodeGuideTeal
import kotlinx.coroutines.launch
import java.util.UUID

data class GeminiChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val sender: ChatSender,
    val text: String,
    val suggestedTasks: List<String> = emptyList()
)

enum class ChatSender {
    USER, GEMINI_AI
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeminiChatBottomSheet(
    currentEnergy: EnergyLevel,
    onDismiss: () -> Unit,
    onAddTasksToToday: (List<String>) -> Unit
) {
    var userPrompt by remember { mutableStateOf("") }
    var isThinking by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    // Inicialización del Modelo de Gemini AI
    val generativeModel = remember {
        GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey = "AIzaSy_CodeGuide_Mock_Or_Real_Key"
        )
    }

    // Historial de Conversación inicial
    val messages = remember {
        mutableStateListOf(
            GeminiChatMessage(
                sender = ChatSender.GEMINI_AI,
                text = "¡Hola Tomás! 🤖 Soy tu copiloto Gemini AI de CodeGuide.\n\n" +
                        "Veo que tu energía actual está en nivel **${currentEnergy.title} ${currentEnergy.subtitle}** (${currentEnergy.recommendedCapacity}).\n\n" +
                        "¿Qué tarea monolítica o módulo deseas que desglose hoy en micro-pasos sostenibles?",
                suggestedTasks = listOf(
                    "Crear middleware de autenticación JWT",
                    "Configurar interceptores HTTP",
                    "Escribir tests unitarios de expiración"
                )
            )
        )
    }

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .imePadding()
                .padding(horizontal = 20.dp, vertical = 14.dp)
        ) {
            // Header del Chat
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "Gemini",
                            tint = CodeGuideCyan,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Gemini AI Assistant",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50.dp))
                                    .background(CodeGuideTeal)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "v1.5 Flash",
                                    fontSize = 9.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }

                        Text(
                            text = "Desglose de Tareas & Calibración de Energía",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cerrar",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Indicador de Energía Actual
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = when (currentEnergy) {
                                EnergyLevel.SUAVE -> Icons.Default.Eco
                                EnergyLevel.OPTIMA -> Icons.Default.Tune
                                EnergyLevel.PROFUNDA -> Icons.Default.Bolt
                            },
                            contentDescription = "Energy",
                            tint = CodeGuideTeal,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Energía actual: ",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "${currentEnergy.title} (${currentEnergy.subtitle})",
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = CodeGuideTeal
                        )
                    }

                    Text(
                        text = currentEnergy.recommendedCapacity,
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Lista de Mensajes del Chat con ajuste flexible según teclado
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false)
                    .height(280.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(messages, key = { it.id }) { message ->
                    ChatMessageItem(
                        message = message,
                        onAddSuggestedTasks = { tasks ->
                            onAddTasksToToday(tasks)
                        }
                    )
                }

                if (isThinking) {
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(8.dp)
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = CodeGuideTeal,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Gemini está analizando tu prompt y estructurando micro-pasos...",
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Prompts Rápidos de Ejemplo
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                QuickPromptChip(
                    text = "🔀 Desglosar Auth Monolítico",
                    onClick = {
                        userPrompt = "Desglosa la implementación de autenticación JWT y refresh tokens en pasos de 15 minutos."
                    }
                )
                QuickPromptChip(
                    text = "🌿 Tareas de Baja Carga",
                    onClick = {
                        userPrompt = "Tengo poca energía hoy. ¿Qué tareas de documentación o refactor ligero puedo avanzar?"
                    }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Input del Chat (Siempre visible arriba del teclado)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = userPrompt,
                    onValueChange = { userPrompt = it },
                    placeholder = {
                        Text(
                            text = "Pregunta a Gemini AI sobre tus tareas...",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        )
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedBorderColor = CodeGuideTeal,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {
                        if (userPrompt.isNotBlank() && !isThinking) {
                            val promptToSend = userPrompt
                            userPrompt = ""
                            messages.add(
                                GeminiChatMessage(
                                    sender = ChatSender.USER,
                                    text = promptToSend
                                )
                            )

                            isThinking = true

                            coroutineScope.launch {
                                try {
                                    val systemPrompt = "Desglosa en micro-pasos de 15-20 minutos considerando nivel de energía ${currentEnergy.title}: $promptToSend"
                                    val response = try {
                                        generativeModel.generateContent(systemPrompt).text
                                    } catch (e: Exception) {
                                        null
                                    }

                                    val responseText = response ?: generateGeminiFallbackResponse(promptToSend, currentEnergy)
                                    val extractedTasks = listOf(
                                        "Definir interfaces y contratos DTO",
                                        "Implementar lógica de negocio aislada",
                                        "Agregar pruebas de cobertura de errores"
                                    )

                                    messages.add(
                                        GeminiChatMessage(
                                            sender = ChatSender.GEMINI_AI,
                                            text = responseText,
                                            suggestedTasks = extractedTasks
                                        )
                                    )
                                } finally {
                                    isThinking = false
                                }
                            }
                        }
                    },
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier.height(52.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun ChatMessageItem(
    message: GeminiChatMessage,
    onAddSuggestedTasks: (List<String>) -> Unit
) {
    val isUser = message.sender == ChatSender.USER

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        Surface(
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isUser) 16.dp else 4.dp,
                bottomEnd = if (isUser) 4.dp else 16.dp
            ),
            color = if (isUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
            border = if (!isUser) BorderStroke(1.dp, MaterialTheme.colorScheme.outline) else null
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (isUser) "Tú" else "Gemini AI",
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = if (isUser) MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f) else CodeGuideLink
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = message.text,
                    fontSize = 13.sp,
                    color = if (isUser) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                    lineHeight = 18.sp
                )

                if (!isUser && message.suggestedTasks.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline)
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "MICRO-PASOS RECOMENDADOS:",
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    message.suggestedTasks.forEach { task ->
                        Text(
                            text = "• $task (~15-20 min)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = { onAddSuggestedTasks(message.suggestedTasks) },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CodeGuideTeal),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Añadir a mi Ruta de Hoy",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickPromptChip(text: String, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(50.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Text(
            text = text,
            fontSize = 10.sp,
            fontFamily = FontFamily.Monospace,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
        )
    }
}

private fun generateGeminiFallbackResponse(prompt: String, energy: EnergyLevel): String {
    return when {
        prompt.contains("poca", ignoreCase = true) || prompt.contains("baja", ignoreCase = true) -> {
            "Con tu nivel de energía actual (**${energy.title}**), lo ideal es avanzar en tareas de baja fricción cognitiva para mantener el impulso sin agotar tu mente:\n\n" +
                    "1. Documentar los endpoints principales o leer el PR pendiente.\n" +
                    "2. Formatear y limpiar imports de tu repositorio.\n" +
                    "3. Escribir notas breves en el Readme para el próximo sprint."
        }
        prompt.contains("auth", ignoreCase = true) || prompt.contains("jwt", ignoreCase = true) -> {
            "He analizado el módulo de Autenticación. Te sugiero desglosarlo en 3 bloques de 20 minutos para no saturar memoria de trabajo:\n\n" +
                    "• Bloque 1: Definir modelos Data DTO y respuestas HTTP.\n" +
                    "• Bloque 2: Implementar middleware de verificación de firmas.\n" +
                    "• Bloque 3: Escribir 2 pruebas unitarias de tokens expirados."
        }
        else -> {
            "¡Excelente enfoque! Para abordar este objetivo con tu nivel de energía **${energy.title}**, te recomiendo dividirlo en 3 micro-pasos aislados de máxima claridad."
        }
    }
}
