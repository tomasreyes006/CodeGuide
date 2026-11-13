package com.tomasreyes.codeguide.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tomasreyes.codeguide.ui.theme.CodeGuideCyan
import com.tomasreyes.codeguide.ui.theme.CodeGuideNavy
import com.tomasreyes.codeguide.ui.theme.CodeGuideNavyLight
import com.tomasreyes.codeguide.ui.theme.CodeGuideTeal

/**
 * Logotipo Vectorial de CodeGuide
 */
@Composable
fun CodeGuideLogo(
    modifier: Modifier = Modifier,
    iconSize: Dp = 64.dp,
    showText: Boolean = true
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.size(iconSize),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height

                val bgBrush = Brush.linearGradient(
                    colors = listOf(
                        CodeGuideNavy,
                        CodeGuideNavyLight
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(w, h)
                )

                drawRoundRect(
                    brush = bgBrush,
                    cornerRadius = CornerRadius(w * 0.28f, h * 0.28f),
                    size = Size(w, h)
                )

                val strokeW = w * 0.08f

                // Left Bracket '<'
                val leftPath = Path().apply {
                    moveTo(w * 0.38f, h * 0.32f)
                    lineTo(w * 0.22f, h * 0.50f)
                    lineTo(w * 0.38f, h * 0.68f)
                }

                // Right Bracket '>'
                val rightPath = Path().apply {
                    moveTo(w * 0.62f, h * 0.32f)
                    lineTo(w * 0.78f, h * 0.50f)
                    lineTo(w * 0.62f, h * 0.68f)
                }

                drawPath(
                    path = leftPath,
                    color = Color.White,
                    style = Stroke(width = strokeW, cap = StrokeCap.Round)
                )

                drawPath(
                    path = rightPath,
                    color = Color.White,
                    style = Stroke(width = strokeW, cap = StrokeCap.Round)
                )

                val nodeCenterY = h * 0.42f

                val circuitPath = Path().apply {
                    moveTo(w * 0.50f, h * 0.74f)
                    lineTo(w * 0.50f, nodeCenterY)
                }

                drawPath(
                    path = circuitPath,
                    brush = Brush.verticalGradient(
                        colors = listOf(CodeGuideCyan, CodeGuideTeal)
                    ),
                    style = Stroke(width = strokeW * 0.9f, cap = StrokeCap.Round)
                )

                drawCircle(
                    color = CodeGuideCyan,
                    radius = strokeW * 0.95f,
                    center = Offset(w * 0.50f, nodeCenterY)
                )
            }
        }

        if (showText) {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "CodeGuide",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                color = MaterialTheme.colorScheme.onSurface,
                letterSpacing = (-0.5).sp
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "LEARN. CODE. MASTER.",
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                letterSpacing = 1.8.sp
            )
        }
    }
}
