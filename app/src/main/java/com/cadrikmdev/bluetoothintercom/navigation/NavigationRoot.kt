package com.cadrikmdev.bluetoothintercom.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.cadrikmdev.bluetoothintercom.screens.home.HomeScreenRoot
import com.cadrikmdev.permissions.presentation.screen.permissions.PermissionsScreen
import com.cadrikmdev.permissions.presentation.util.openAppSettings


@Composable
fun NavigationRoot(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = "main"
    ) {
        mainGraph(
            navController = navController,
        )
        permissionsGraph(
            navController = navController,
        )
    }
}

private fun NavGraphBuilder.mainGraph(
    navController: NavHostController,
) {
    navigation(
        startDestination = "main_screen",
        route = "main"
    ) {
        composable("main_screen") {
            HomeScreenRoot(
                onResolvePermissionClick = {
                    navController.navigate("permissions")
                }
            )
        }
    }
}

private fun NavGraphBuilder.permissionsGraph(
    navController: NavHostController,
) {
    navigation(
        startDestination = "permissions_screen",
        route = "permissions"
    ) {
        composable("permissions_screen") {
            val context = LocalContext.current
            PermissionsScreen(
                onBackPressed = {
                    navController.navigate("main") {
                        popUpTo("permissions") {
                            inclusive = true
                        }
                    }
                },
                openAppSettings = {
                    context.openAppSettings()
                }
            )
        }
    }
}