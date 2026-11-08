package com.tomasreyes.codeguide.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tomasreyes.codeguide.ui.theme.CodeGuideNavy
import com.tomasreyes.codeguide.ui.theme.CodeGuidePasskeyBg
import com.tomasreyes.codeguide.ui.theme.CodeGuideSurface
import com.tomasreyes.codeguide.ui.theme.CodeGuideTextSecondary

@Composable
fun CodeGuideBottomNavigationBar(
    selectedTab: String,
    onTabSelected: (String) -> Unit
) {
    NavigationBar(
        containerColor = CodeGuideSurface,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = selectedTab == "Hoy",
            onClick = { onTabSelected("Hoy") },
            icon = {
                Icon(
                    imageVector = Icons.Default.LightMode,
                    contentDescription = "Hoy"
                )
            },
            label = { Text(text = "Hoy", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = CodeGuideNavy,
                selectedTextColor = CodeGuideNavy,
                indicatorColor = CodeGuidePasskeyBg,
                unselectedIconColor = CodeGuideTextSecondary,
                unselectedTextColor = CodeGuideTextSecondary
            )
        )

        NavigationBarItem(
            selected = selectedTab == "Proyectos",
            onClick = { onTabSelected("Proyectos") },
            icon = {
                Icon(
                    imageVector = Icons.Default.Folder,
                    contentDescription = "Proyectos"
                )
            },
            label = { Text(text = "Proyectos", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = CodeGuideNavy,
                selectedTextColor = CodeGuideNavy,
                indicatorColor = CodeGuidePasskeyBg,
                unselectedIconColor = CodeGuideTextSecondary,
                unselectedTextColor = CodeGuideTextSecondary
            )
        )

        NavigationBarItem(
            selected = selectedTab == "Enfoque",
            onClick = { onTabSelected("Enfoque") },
            icon = {
                Icon(
                    imageVector = Icons.Default.Timer,
                    contentDescription = "Enfoque"
                )
            },
            label = { Text(text = "Enfoque", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = CodeGuideNavy,
                selectedTextColor = CodeGuideNavy,
                indicatorColor = CodeGuidePasskeyBg,
                unselectedIconColor = CodeGuideTextSecondary,
                unselectedTextColor = CodeGuideTextSecondary
            )
        )

        NavigationBarItem(
            selected = selectedTab == "Métricas",
            onClick = { onTabSelected("Métricas") },
            icon = {
                Icon(
                    imageVector = Icons.Default.BarChart,
                    contentDescription = "Métricas"
                )
            },
            label = { Text(text = "Métricas", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = CodeGuideNavy,
                selectedTextColor = CodeGuideNavy,
                indicatorColor = CodeGuidePasskeyBg,
                unselectedIconColor = CodeGuideTextSecondary,
                unselectedTextColor = CodeGuideTextSecondary
            )
        )
    }
}
