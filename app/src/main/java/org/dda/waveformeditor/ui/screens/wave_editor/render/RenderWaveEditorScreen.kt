package org.dda.waveformeditor.ui.screens.wave_editor.render

import android.net.Uri
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.dda.waveformeditor.ui.navigation.NavActions
import org.dda.waveformeditor.ui.screens.wave_editor.WaveEditorState
import org.dda.waveformeditor.ui.screens.wave_editor.WaveEditorViewModel
import org.dda.waveformeditor.ui.theme.RenderConst
import org.dda.waveformeditor.ui.tools.observeAsOneTimeEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RenderWaveEditorScreen(
    viewModel: WaveEditorViewModel,
    navActions: NavActions,
) {

    val state = viewModel.stateFlow.collectAsStateWithLifecycle()


    val snackBarHostState = remember { SnackbarHostState() }

    viewModel.oneTimeMessageFlow.observeAsOneTimeEvent { event ->
        when (event) {
            is WaveEditorViewModel.OneTimeMessage.Text -> {
                snackBarHostState
                    .showSnackbar(
                        message = event.text,
                        actionLabel = "Action",
                        duration = SnackbarDuration.Long
                    )
            }
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                navigationIcon = {
                    IconButton(
                        onClick = { navActions.onBackPress() }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                title = {
                    Text(state.value.title)
                },
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        }
    ) { paddings ->
        when (val stateLcl = state.value) {
            is WaveEditorState.Init -> RenderStateInit(
                modifier = Modifier.padding(paddings),
                state = stateLcl
            )

            is WaveEditorState.Loaded -> {
                val onSelectWave: (startIndex: Int, endIndex: Int) -> Unit = remember(viewModel) {
                    { startIndex: Int, endIndex: Int ->
                        viewModel.onUiEvent(
                            WaveEditorViewModel.UiEvent.OnSelectWave(
                                startIndex = startIndex,
                                endIndex = endIndex
                            )
                        )
                    }
                }
                val onSwitchDebug: (isEnable: Boolean) -> Unit = remember(viewModel) {
                    { isEnable ->
                        viewModel.onUiEvent(
                            WaveEditorViewModel.UiEvent.OnSwitchDebug(
                                isEnable = isEnable
                            )
                        )
                    }
                }
                val onSelectExportFile: (uri: Uri) -> Unit = remember(viewModel) {
                    { uri: Uri ->
                        viewModel.onUiEvent(
                            WaveEditorViewModel.UiEvent.OnSelectExportFile(
                                uri = uri
                            )
                        )
                    }
                }

                RenderStateLoaded(
                    modifier = Modifier
                        .padding(paddings)
                        .padding(horizontal = RenderConst.paddingPageEdge),
                    state = stateLcl,
                    onSelect = onSelectWave,
                    onSwitchDebug = onSwitchDebug,
                    onSelectExportFile = onSelectExportFile,
                )
            }

            is WaveEditorState.Error -> RenderStateError(
                modifier = Modifier
                    .padding(paddings)
                    .padding(horizontal = RenderConst.paddingPageEdge)
                    .fillMaxSize(),
                state = stateLcl
            )
        }
    }
}