package org.dda.waveformeditor.ui.screens.wave_editor

import android.net.Uri
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.dda.waveformeditor.domain.WaveDataFormat
import org.dda.waveformeditor.ui.common.BaseViewModel
import org.dda.waveformeditor.ui.tools.FileResolver


interface WaveEditorViewModel {

    val stateFlow: StateFlow<WaveEditorState>

    val oneTimeMessageFlow: Flow<OneTimeMessage>

    fun onUiEvent(uiEvent: UiEvent)

    sealed interface UiEvent {
        data class OnSelectWave(
            val startIndex: Int,
            val endIndex: Int,
        ) : UiEvent

        data class OnSwitchDebug(
            val isEnable: Boolean
        ) : UiEvent

        class OnSelectExportFile(
            val uri: Uri
        ) : UiEvent
    }

    sealed interface OneTimeMessage {
        data class Text(
            val text: String
        ) : OneTimeMessage
    }

}

class WaveEditorViewModelImpl(
    fileUri: Uri,
    val fileResolver: FileResolver,
    val waveDataFormat: WaveDataFormat,
    scope: CoroutineScope? = null,
) : BaseViewModel(scope), WaveEditorViewModel {

    private val stateFlowInternal = MutableStateFlow<WaveEditorState>(WaveEditorState.Init(fileUri))

    private val oneTimeMessageChannel = Channel<WaveEditorViewModel.OneTimeMessage>()

    override val stateFlow: StateFlow<WaveEditorState>
        get() = stateFlowInternal.asStateFlow()

    override val oneTimeMessageFlow: Flow<WaveEditorViewModel.OneTimeMessage>
        get() = oneTimeMessageChannel.receiveAsFlow()

    override fun onUiEvent(uiEvent: WaveEditorViewModel.UiEvent) {
        when (uiEvent) {
            is WaveEditorViewModel.UiEvent.OnSelectWave -> stateFlowInternal.update { state ->
                state.onSelectWave(uiEvent.startIndex, uiEvent.endIndex)
            }

            is WaveEditorViewModel.UiEvent.OnSwitchDebug -> stateFlowInternal.update { state ->
                state.onSwitchDebug(uiEvent.isEnable)
            }

            is WaveEditorViewModel.UiEvent.OnSelectExportFile -> {
                coroutineScope.launch {
                    val message = onSelectExportFile(
                        uri = uiEvent.uri
                    )
                    oneTimeMessageChannel.send(
                        WaveEditorViewModel.OneTimeMessage.Text(
                            message
                        )
                    )
                }
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


    suspend fun onSelectExportFile(uri: Uri): String {
        return when (val state = stateFlowInternal.value) {
            is WaveEditorState.Init, is WaveEditorState.Error -> "Wrong state"
            is WaveEditorState.Loaded -> {
                runCatching {
                    val fileName = fileResolver.getName(uri)
                    fileResolver.openFile(uri).use { outputStream ->
                        waveDataFormat.save(
                            data = state.waveEditData.waveData,
                            selection = state.waveEditData.selectionStartPairIndex..state.waveEditData.selectionEndPairIndex,
                            output = outputStream,
                        )
                    }
                    "Saved to $fileName"
                }.getOrElse { thr ->
                    "Something was wrong: ${thr.message}"
                }
            }
        }
    }
}