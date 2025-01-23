package com.cadrikmdev.bluetoothintercom.screens.classic.client

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.currentStateAsState
import com.cadrikmdev.bluetoothintercom.R
import com.cadrikmdev.bluetoothintercom.screens.classic.client.state.BluetoothClassicClientScreenState
import com.cadrikmdev.bluetoothintercom.ui.ManagedDeviceListItem
import com.cadrikmdev.core.presentation.designsystem.BaseTheme
import com.cadrikmdev.core.presentation.designsystem.components.BaseOutlinedActionButton
import com.cadrikmdev.intercom.domain.client.TrackingDevice
import org.koin.androidx.compose.koinViewModel

@Composable
fun BluetoothClassicClientScreenRoot(
    onBackClick: () -> Unit,
    viewModel: BluetoothClassicClientScreenViewModel = koinViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    BluetoothClassicClientScreen(
        state = state,
        onBackClick = onBackClick,
        onAction = viewModel::onAction,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun BluetoothClassicClientScreen(
    state: BluetoothClassicClientScreenState,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    onAction: (BluetoothClassicClientScreenAction) -> Unit,
    onEvent: (BluetoothClassicClientScreenEvent) -> Unit
) {

    val context = LocalContext.current

    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleState by lifecycleOwner.lifecycle.currentStateAsState()
    LaunchedEffect(lifecycleState) {
        when (lifecycleState) {
            Lifecycle.State.RESUMED -> {
                onEvent(BluetoothClassicClientScreenEvent.OnResumed)
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
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.Top
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
                            onAction(BluetoothClassicClientScreenAction.OnOpenBluetoothSettingsClick)
                        }
                    }
                }
                if (state.devices.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(text = stringResource(id = R.string.please_connect_signal_tracking_device))
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                    ) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth(),
                            contentPadding = PaddingValues(8.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                        ) {
                            items(
                                items = state.devices,
                                key = {
                                    it.address
                                }
                            ) {
                                ManagedDeviceListItem(
                                    trackingDeviceUi = it,
                                    onStopClick = { address ->
                                        onAction(
                                            BluetoothClassicClientScreenAction.SendMessageToDevice(
                                                address,
                                                "stop"
                                            )
                                        )
                                    },
                                    onStartClick = { address ->
                                        onAction(
                                            BluetoothClassicClientScreenAction.SendMessageToDevice(
                                                address,
                                                "start"
                                            )
                                        )
                                    },
                                    onConnectClick = { address ->
                                        onAction(
                                            BluetoothClassicClientScreenAction.ConnectToDeviceClicked(
                                                address
                                            )
                                        )
                                    },
                                    onDisconnectClick = { address ->
                                        onAction(
                                            BluetoothClassicClientScreenAction.DisconnectToDeviceClicked(
                                                address
                                            )
                                        )
                                    },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun previewBluetoothClassicClientScreen() {
    BluetoothClassicClientScreen(
      state = BluetoothClassicClientScreenState(
          isBluetoothAdapterEnabled = true,
          listOf(
            TrackingDevice(name = "Pixel 5", connected = true, updateTimestamp = System.currentTimeMillis(), address = "00:00:00:00:00:00:00")
          ),
    ),
        onAction = {},
        onEvent = {},
        onBackClick = {},
    )
}