package com.tomasreyes.codeguide.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tomasreyes.codeguide.ui.theme.CodeGuideCyan
import com.tomasreyes.codeguide.ui.theme.CodeGuideNavy
import com.tomasreyes.codeguide.ui.theme.CodeGuideTealBright
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun CodeGuideLogo(
    modifier: Modifier = Modifier,
    iconSize: Dp = 68.dp,
    showText: Boolean = true
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logo Vector Icon
        Canvas(modifier = Modifier.size(iconSize)) {
            val width = size.width
            val height = size.height
            val center = Offset(width / 2f, height / 2f)
            val radius = width * 0.42f

            val gradientBrush = Brush.sweepGradient(
                colors = listOf(
                    CodeGuideTealBright,
                    CodeGuideCyan,
                    Color(0xFF2563EB),
                    CodeGuideTealBright
                ),
                center = center
            )

            // Outer Circle Ring
            drawCircle(
                brush = gradientBrush,
                radius = radius,
                center = center,
                style = Stroke(width = width * 0.05f)
            )

            // Central Hexagon Node
            val hexPath = Path()
            val hexRadius = width * 0.16f
            for (i in 0 until 6) {
                val angleRad = (i * 60 - 30) * PI / 180.0
                val x = center.x + hexRadius * cos(angleRad).toFloat()
                val y = center.y + hexRadius * sin(angleRad).toFloat()
                if (i == 0) hexPath.moveTo(x, y) else hexPath.lineTo(x, y)
            }
            hexPath.close()

            drawPath(
                path = hexPath,
                brush = gradientBrush
            )

            // Outer Nodes (Top, Right, Bottom, Left)
            val nodeAngles = listOf(-90f, 0f, 90f, 180f)
            nodeAngles.forEach { angle ->
                val rad = angle * PI / 180.0
                val nx = center.x + radius * cos(rad).toFloat()
                val ny = center.y + radius * sin(rad).toFloat()

                // Line from center to node
                drawLine(
                    brush = gradientBrush,
                    start = center,
                    end = Offset(nx, ny),
                    strokeWidth = width * 0.035f
                )

                // Node circle
                drawCircle(
                    color = Color.White,
                    radius = width * 0.07f,
                    center = Offset(nx, ny)
                )
                drawCircle(
                    brush = gradientBrush,
                    radius = width * 0.07f,
                    center = Offset(nx, ny),
                    style = Stroke(width = width * 0.03f)
                )
            }
        }

        if (showText) {
            Spacer(modifier = Modifier.height(8.dp))

            // Main Brand Title
            Text(
                text = "CodeGuide",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                color = CodeGuideNavy,
                letterSpacing = (-0.5).sp
            )

            Spacer(modifier = Modifier.height(2.dp))

            // Tagline
            Text(
                text = "LEARN. CODE. MASTER.",
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = CodeGuideNavy.copy(alpha = 0.8f),
                letterSpacing = 1.8.sp
            )
        }
    }
}
