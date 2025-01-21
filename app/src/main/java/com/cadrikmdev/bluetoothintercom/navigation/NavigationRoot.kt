package com.cadrikmdev.bluetoothintercom.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.cadrikmdev.bluetoothintercom.screens.classic.client.BluetoothClassicClientScreenRoot
import com.cadrikmdev.bluetoothintercom.screens.classic.client.navigation.BluetoothClassicClientRoute
import com.cadrikmdev.bluetoothintercom.screens.classic.server.BluetoothClassicServerScreenRoot
import com.cadrikmdev.bluetoothintercom.screens.classic.server.navigation.BluetoothClassicServerRoute
import com.cadrikmdev.bluetoothintercom.screens.home.HomeScreenRoot
import com.cadrikmdev.bluetoothintercom.screens.home.navigation.HomeScreenRoute
import com.cadrikmdev.permissions.presentation.navigation.PermissionsNavigation
import com.cadrikmdev.permissions.presentation.navigation.permissionsGraph


@Composable
fun NavigationRoot(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = MainNavigation
    ) {
        mainGraph(
            navController = navController,
        )
        permissionsGraph(
            navController = navController,
            goBackRoute = HomeScreenRoute
        )
    }
}

private fun NavGraphBuilder.mainGraph(
    navController: NavHostController,
) {
    navigation<MainNavigation>(
        startDestination = HomeScreenRoute
    ) {
        composable<HomeScreenRoute> {
            HomeScreenRoot(
                onResolvePermissionClick = {
                    navController.navigate(PermissionsNavigation)
                },
                onBluetoothClassicClientClick = {
                    navController.navigate(BluetoothClassicClientRoute)
                },
                onBluetoothClassicServerClick = {
                    navController.navigate(BluetoothClassicServerRoute)
                },
                onBluetoothLeClientClick = {

                },
                onBluetoothLeServerClick = {

                }
            )
        }
        composable<BluetoothClassicClientRoute> {
            BluetoothClassicClientScreenRoot(
                onBackClick = {
                    navController.navigate(HomeScreenRoute) {
                        popUpTo(BluetoothClassicClientRoute) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable<BluetoothClassicServerRoute> {
            BluetoothClassicServerScreenRoot(
                onBackClick = {
                    navController.navigate(HomeScreenRoute) {
                        popUpTo(BluetoothClassicServerRoute) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}
