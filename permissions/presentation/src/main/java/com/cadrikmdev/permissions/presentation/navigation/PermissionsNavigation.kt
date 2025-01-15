package com.cadrikmdev.permissions.presentation.navigation

import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.cadrikmdev.permissions.presentation.screen.permissions.PermissionsScreen
import com.cadrikmdev.permissions.presentation.screen.permissions.navigation.PermissionsScreenRoute
import com.cadrikmdev.permissions.presentation.util.openAppSettings
import kotlinx.serialization.Serializable
import androidx.navigation.compose.composable

@Serializable
data object PermissionsNavigation


fun NavGraphBuilder.permissionsGraph(
    navController: NavHostController,
    goBackRoute: Any
) {
    navigation<PermissionsNavigation>(
        startDestination = PermissionsScreenRoute,
    ) {
        composable<PermissionsScreenRoute> {
            val context = LocalContext.current
            PermissionsScreen(
                onBackPressed = {
                    navController.navigate(goBackRoute) {
                        popUpTo(PermissionsScreenRoute) {
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