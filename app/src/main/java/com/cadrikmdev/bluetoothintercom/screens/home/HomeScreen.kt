package com.cadrikmdev.bluetoothintercom.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
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
    viewModel: HomeScreenViewModel = koinViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    HomeScreen(
        state = state,
        onResolvePermissionClick = onResolvePermissionClick,
        onAction = viewModel::onAction,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun HomeScreen(
    state: HomeScreenState,
    onResolvePermissionClick: () -> Unit,
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
                Text(
                    text = UiText.StringResource(R.string.bluetooth_le).asString()
                )
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
                onAction = {},
                onEvent = {},
            )
        }
    }
}