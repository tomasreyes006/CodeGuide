package com.tomasreyes.codeguide

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.tomasreyes.codeguide.ui.navigation.CodeGuideNavigation
import com.tomasreyes.codeguide.ui.theme.CodeGuideTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CodeGuideTheme {
                CodeGuideNavigation()
            }
        }
    }
}
