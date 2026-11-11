package com.tomasreyes.codeguide.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tomasreyes.codeguide.ui.screens.FocusScreen
import com.tomasreyes.codeguide.ui.screens.ForgotPasswordScreen
import com.tomasreyes.codeguide.ui.screens.LoginScreen
import com.tomasreyes.codeguide.ui.screens.MetricsScreen
import com.tomasreyes.codeguide.ui.screens.ProfileScreen
import com.tomasreyes.codeguide.ui.screens.ProjectsScreen
import com.tomasreyes.codeguide.ui.screens.RegisterScreen
import com.tomasreyes.codeguide.ui.screens.TodayScreen
import com.tomasreyes.codeguide.ui.viewmodel.CodeGuideViewModel

sealed class CodeGuideScreen(val route: String) {
    object Login : CodeGuideScreen("login")
    object Register : CodeGuideScreen("register")
    object ForgotPassword : CodeGuideScreen("forgot_password")
    object Home : CodeGuideScreen("home")
    object Projects : CodeGuideScreen("projects")
    object Focus : CodeGuideScreen("focus")
    object Metrics : CodeGuideScreen("metrics")
    object Profile : CodeGuideScreen("profile")
}

@Composable
fun CodeGuideNavigation(
    navController: NavHostController = rememberNavController(),
    codeGuideViewModel: CodeGuideViewModel = viewModel()
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
                viewModel = codeGuideViewModel,
                onNavigateToProjects = {
                    navController.navigate(CodeGuideScreen.Projects.route) { launchSingleTop = true }
                },
                onNavigateToFocus = {
                    navController.navigate(CodeGuideScreen.Focus.route) { launchSingleTop = true }
                },
                onNavigateToMetrics = {
                    navController.navigate(CodeGuideScreen.Metrics.route) { launchSingleTop = true }
                },
                onNavigateToProfile = {
                    navController.navigate(CodeGuideScreen.Profile.route)
                }
            )
        }

        composable(CodeGuideScreen.Projects.route) {
            ProjectsScreen(
                viewModel = codeGuideViewModel,
                onNavigateToToday = {
                    navController.navigate(CodeGuideScreen.Home.route) { launchSingleTop = true }
                },
                onNavigateToFocus = {
                    navController.navigate(CodeGuideScreen.Focus.route) { launchSingleTop = true }
                },
                onNavigateToMetrics = {
                    navController.navigate(CodeGuideScreen.Metrics.route) { launchSingleTop = true }
                },
                onNavigateToProfile = {
                    navController.navigate(CodeGuideScreen.Profile.route)
                }
            )
        }

        composable(CodeGuideScreen.Focus.route) {
            FocusScreen(
                viewModel = codeGuideViewModel,
                onNavigateToToday = {
                    navController.navigate(CodeGuideScreen.Home.route) { launchSingleTop = true }
                },
                onNavigateToProjects = {
                    navController.navigate(CodeGuideScreen.Projects.route) { launchSingleTop = true }
                },
                onNavigateToMetrics = {
                    navController.navigate(CodeGuideScreen.Metrics.route) { launchSingleTop = true }
                },
                onNavigateToProfile = {
                    navController.navigate(CodeGuideScreen.Profile.route)
                }
            )
        }

        composable(CodeGuideScreen.Metrics.route) {
            MetricsScreen(
                viewModel = codeGuideViewModel,
                onNavigateToToday = {
                    navController.navigate(CodeGuideScreen.Home.route) { launchSingleTop = true }
                },
                onNavigateToProjects = {
                    navController.navigate(CodeGuideScreen.Projects.route) { launchSingleTop = true }
                },
                onNavigateToFocus = {
                    navController.navigate(CodeGuideScreen.Focus.route) { launchSingleTop = true }
                },
                onNavigateToProfile = {
                    navController.navigate(CodeGuideScreen.Profile.route)
                }
            )
        }

        composable(CodeGuideScreen.Profile.route) {
            ProfileScreen(
                codeGuideViewModel = codeGuideViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onLogout = {
                    navController.navigate(CodeGuideScreen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}
