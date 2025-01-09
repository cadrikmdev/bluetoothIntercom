package com.cadrikmdev.bluetoothintercom.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.currentStateAsState
import com.cadrikmdev.bluetoothintercom.R
import com.cadrikmdev.bluetoothintercom.screens.home.state.HomeScreenState
import com.cadrikmdev.core.presentation.designsystem.BaseTheme
import com.cadrikmdev.core.presentation.designsystem.components.BaseActionButton
import com.cadrikmdev.core.presentation.designsystem.components.BaseScaffold
import com.cadrikmdev.core.presentation.ui.UiText
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreenRoot(
    onResolvePermissionClick: () -> Unit,
    onBluetoothClassicServerClick: () -> Unit,
    onBluetoothClassicClientClick: () -> Unit,
    onBluetoothLeServerClick: () -> Unit,
    onBluetoothLeClientClick: () -> Unit,
    viewModel: HomeScreenViewModel = koinViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    HomeScreen(
        state = state,
        onResolvePermissionClick = onResolvePermissionClick,
        onBluetoothClassicServerClick = onBluetoothClassicServerClick,
        onBluetoothClassicClientClick = onBluetoothClassicClientClick,
        onBluetoothLeServerClick = onBluetoothLeServerClick,
        onBluetoothLeClientClick = onBluetoothLeClientClick,
        onAction = viewModel::onAction,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun HomeScreen(
    state: HomeScreenState,
    onResolvePermissionClick: () -> Unit,
    onBluetoothClassicServerClick: () -> Unit,
    onBluetoothClassicClientClick: () -> Unit,
    onBluetoothLeServerClick: () -> Unit,
    onBluetoothLeClientClick: () -> Unit,
    modifier: Modifier = Modifier,
    onAction: (HomeScreenAction) -> Unit,
    onEvent: (HomeScreenEvent) -> Unit
    ) {

    val context = LocalContext.current

    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleState by lifecycleOwner.lifecycle.currentStateAsState()
    LaunchedEffect(lifecycleState) {
        when (lifecycleState) {
            Lifecycle.State.RESUMED -> {
                onEvent(HomeScreenEvent.OnResumed)
            }
            Lifecycle.State.DESTROYED,
            Lifecycle.State.INITIALIZED,
            Lifecycle.State.CREATED,
            Lifecycle.State.STARTED -> { // nothing to do }
            }
        }
    }

    BaseTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding)
            ) {
                if (!state.allPermissionsGranted) {
                    Text(
                        text = UiText.StringResource(R.string.permissions).asString()
                    )
                    BaseActionButton(
                        text = UiText.StringResource(R.string.resolve_missing_permissions)
                            .asString(),
                        isLoading = false,
                    ) {
                        onResolvePermissionClick()
                    }
                }
                Text(
                    text = UiText.StringResource(R.string.bluetooth_classic).asString()
                )
                Row()
                {
                    BaseActionButton(
                        text = UiText.StringResource(R.string.client)
                            .asString(),
                        isLoading = false,
                        modifier = Modifier.weight(1f)
                    ) {
                        onBluetoothClassicClientClick()
                    }
                    Spacer(
                        Modifier.width(16.dp)
                    )
                    BaseActionButton(
                        text = UiText.StringResource(R.string.server)
                            .asString(),
                        isLoading = false,
                        modifier = Modifier.weight(1f)
                    ) {
                        onBluetoothClassicServerClick()
                    }
                }
                Text(
                    text = UiText.StringResource(R.string.bluetooth_le).asString()
                )
                Row()
                {
                    BaseActionButton(
                        text = UiText.StringResource(R.string.client)
                            .asString(),
                        isLoading = false,
                        modifier = Modifier.weight(1f)
                    ) {
                        onBluetoothLeClientClick()
                    }
                    Spacer(
                        Modifier.width(16.dp)
                    )
                    BaseActionButton(
                        text = UiText.StringResource(R.string.server)
                            .asString(),
                        isLoading = false,
                        modifier = Modifier.weight(1f)
                    ) {
                        onBluetoothLeServerClick()
                    }
                }
            }
        }
    }
}


@Composable
@PreviewScreenSizes
fun HomeScreenPreview() {
    BaseTheme {
        BaseScaffold {
            HomeScreen(
                state = HomeScreenState(),
                onResolvePermissionClick = {},
                onBluetoothClassicServerClick = {},
                onBluetoothClassicClientClick = {},
                onBluetoothLeServerClick = {},
                onBluetoothLeClientClick = {},
                onAction = {},
                onEvent = {},
            )
        }
    }
}