package com.cadrikmdev.bluetoothintercom.screens.classic.server

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.currentStateAsState
import com.cadrikmdev.bluetoothintercom.R
import com.cadrikmdev.bluetoothintercom.screens.classic.client.BluetoothClassicClientScreenAction
import com.cadrikmdev.bluetoothintercom.screens.classic.server.state.BluetoothClassicServerScreenState
import com.cadrikmdev.core.presentation.designsystem.BaseTheme
import com.cadrikmdev.core.presentation.designsystem.components.BaseActionButton
import com.cadrikmdev.core.presentation.designsystem.components.BaseOutlinedActionButton
import com.cadrikmdev.intercom.domain.server.ConnectionState
import org.koin.androidx.compose.koinViewModel

@Composable
fun BluetoothClassicServerScreenRoot(
    onBackClick: () -> Unit,
    viewModel: BluetoothClassicServerScreenViewModel = koinViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    BluetoothClassicServerScreen(
        state = state,
        onBackClick = onBackClick,
        onAction = viewModel::onAction,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun BluetoothClassicServerScreen(
    state: BluetoothClassicServerScreenState,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    onAction: (BluetoothClassicServerScreenAction) -> Unit,
    onEvent: (BluetoothClassicServerScreenEvent) -> Unit
) {

    val context = LocalContext.current

    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleState by lifecycleOwner.lifecycle.currentStateAsState()
    LaunchedEffect(lifecycleState) {
        when (lifecycleState) {
            Lifecycle.State.RESUMED -> {
                onEvent(BluetoothClassicServerScreenEvent.OnResumed)
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
                if (!state.isBluetoothAdapterEnabled) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(text = stringResource(id = R.string.bluetoothAdapterDisabled))
                        BaseOutlinedActionButton(
                            modifier = Modifier.padding(start = 16.dp),
                            text = stringResource(id = R.string.enable),
                            isLoading = false
                        ) {
                            onAction(BluetoothClassicServerScreenAction.OnOpenBluetoothSettingsClick)
                        }
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    BaseActionButton(
                        text = stringResource(id = R.string.start),
                        modifier = Modifier.weight(1f),
                        enabled = (!state.isServerRunning),
                        isLoading = false
                    ) {
                        onAction(BluetoothClassicServerScreenAction.StartActionClicked)
                    }
                    BaseActionButton(
                        text = stringResource(id = R.string.stop),
                        modifier = Modifier.weight(1f),
                        enabled = (state.isServerRunning),
                        isLoading = false
                    ) {
                        onAction(BluetoothClassicServerScreenAction.StopActionClicked)
                    }
                }
                Text(
                    text = state.intercomStatus.toString()
                )

            }
        }
    }
}