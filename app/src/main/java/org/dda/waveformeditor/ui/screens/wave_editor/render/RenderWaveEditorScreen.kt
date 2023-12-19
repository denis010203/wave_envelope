package org.dda.waveformeditor.ui.screens.wave_editor.render

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RenderWaveEditorScreen(
    viewModel: WaveEditorViewModel,
    navActions: NavActions,
) {

    val state = viewModel.stateFlow.collectAsStateWithLifecycle()

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

                RenderStateLoaded(
                    modifier = Modifier
                        .padding(paddings)
                        .padding(horizontal = RenderConst.paddingPageEdge),
                    state = stateLcl,
                    onSelect = onSelectWave,
                    onSwitchDebug = onSwitchDebug,
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