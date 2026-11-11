package com.tomasreyes.codeguide

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tomasreyes.codeguide.ui.navigation.CodeGuideNavigation
import com.tomasreyes.codeguide.ui.theme.CodeGuideTheme
import com.tomasreyes.codeguide.ui.viewmodel.CodeGuideViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val codeGuideViewModel: CodeGuideViewModel = viewModel()
            val uiState by codeGuideViewModel.uiState.collectAsState()

            CodeGuideTheme(darkTheme = uiState.isDarkMode) {
                CodeGuideNavigation(codeGuideViewModel = codeGuideViewModel)
            }
        }
    }
}
