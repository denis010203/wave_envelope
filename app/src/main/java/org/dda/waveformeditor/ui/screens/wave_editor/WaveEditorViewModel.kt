package org.dda.waveformeditor.ui.screens.wave_editor

import android.net.Uri
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.dda.waveformeditor.domain.WaveDataFormat
import org.dda.waveformeditor.ui.common.BaseViewModel
import org.dda.waveformeditor.ui.tools.FileResolver


interface WaveEditorViewModel {

    val stateFlow: StateFlow<WaveEditorState>

    fun onUiEvent(uiEvent: UiEvent)

    sealed interface UiEvent {
        data class OnSelectWave(
            val startIndex: Int,
            val endIndex: Int,
        ) : UiEvent

        data class OnSwitchDebug(
            val isEnable: Boolean
        ) : UiEvent
    }

}

class WaveEditorViewModelImpl(
    fileUri: Uri,
    val fileResolver: FileResolver,
    val waveDataFormat: WaveDataFormat,
    scope: CoroutineScope? = null,
) : BaseViewModel(scope), WaveEditorViewModel {

    private val stateFlowInternal = MutableStateFlow<WaveEditorState>(WaveEditorState.Init(fileUri))

    override val stateFlow: StateFlow<WaveEditorState>
        get() = stateFlowInternal.asStateFlow()

    override fun onUiEvent(uiEvent: WaveEditorViewModel.UiEvent) {
        when (uiEvent) {
            is WaveEditorViewModel.UiEvent.OnSelectWave -> stateFlowInternal.update { state ->
                state.onSelectWave(uiEvent.startIndex, uiEvent.endIndex)
            }

            is WaveEditorViewModel.UiEvent.OnSwitchDebug -> stateFlowInternal.update { state ->
                state.onSwitchDebug(uiEvent.isEnable)
            }
        }
    }

    init {
        coroutineScope.launch {
            val name = fileResolver.getName(fileUri)
            stateFlowInternal.update { state ->
                state.onReadFileName(name)
            }
        }

        coroutineScope.launch {
            runCatching {
                val fileContent = fileResolver.readFile(fileUri)
                val waveData = waveDataFormat.parse(
                    fileData = fileContent,
                    validateValues = true
                ).getOrThrow()
                stateFlowInternal.update { state ->
                    state.onReadFileContent(waveData)
                }
            }.onFailure { thr ->
                stateFlowInternal.update { state ->
                    state.onFileReadFail(thr)
                }
            }
        }
    }
}