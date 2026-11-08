package com.tomasreyes.codeguide.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tomasreyes.codeguide.ui.screens.ForgotPasswordScreen
import com.tomasreyes.codeguide.ui.screens.LoginScreen
import com.tomasreyes.codeguide.ui.screens.ProjectsScreen
import com.tomasreyes.codeguide.ui.screens.RegisterScreen
import com.tomasreyes.codeguide.ui.screens.TodayScreen

sealed class CodeGuideScreen(val route: String) {
    object Login : CodeGuideScreen("login")
    object Register : CodeGuideScreen("register")
    object ForgotPassword : CodeGuideScreen("forgot_password")
    object Home : CodeGuideScreen("home")
    object Projects : CodeGuideScreen("projects")
}

@Composable
fun CodeGuideNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = CodeGuideScreen.Login.route
    ) {
        composable(CodeGuideScreen.Login.route) {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate(CodeGuideScreen.Register.route)
                },
                onNavigateToForgotPassword = {
                    navController.navigate(CodeGuideScreen.ForgotPassword.route)
                },
                onLoginSuccess = {
                    navController.navigate(CodeGuideScreen.Home.route) {
                        popUpTo(CodeGuideScreen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(CodeGuideScreen.Register.route) {
            RegisterScreen(
                onNavigateToLogin = {
                    navController.popBackStack()
                },
                onRegisterSuccess = {
                    navController.navigate(CodeGuideScreen.Home.route) {
                        popUpTo(CodeGuideScreen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(CodeGuideScreen.ForgotPassword.route) {
            ForgotPasswordScreen(
                onNavigateToLogin = {
                    navController.popBackStack()
                },
                onResetRequested = {
                    navController.popBackStack()
                }
            )
        }

        composable(CodeGuideScreen.Home.route) {
            TodayScreen(
                onNavigateToProjects = {
                    navController.navigate(CodeGuideScreen.Projects.route) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(CodeGuideScreen.Projects.route) {
            ProjectsScreen(
                onNavigateToToday = {
                    navController.navigate(CodeGuideScreen.Home.route) {
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}
