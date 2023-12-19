package org.dda.waveformeditor.ui.screens.file_list

import android.net.Uri
import androidx.core.net.toFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.dda.waveformeditor.ui.common.BaseViewModel
import org.dda.waveformeditor.ui.screens.file_list.entities.WaveFileUi
import org.dda.waveformeditor.ui.tools.FileResolver

interface FileListViewModel {

    val stateFlow: StateFlow<FileListState>

    fun onUiEvent(uiEvent: UiEvent)

    sealed interface UiEvent {
        data class OnAddToList(val uri: Uri) : UiEvent
    }

}


class FileListViewModelImpl(
    val fileResolver: FileResolver,
    scope: CoroutineScope? = null,
) : BaseViewModel(scope), FileListViewModel {

    private val stateFlowInternal = MutableStateFlow<FileListState>(FileListState.ListFiles())

    override val stateFlow: StateFlow<FileListState>
        get() = stateFlowInternal.asStateFlow()

    override fun onUiEvent(uiEvent: FileListViewModel.UiEvent) {
        when (uiEvent) {
            is FileListViewModel.UiEvent.OnAddToList -> {
                coroutineScope.launch {
                    stateFlowInternal.update { state ->
                        state.onAddFileToList(
                            file = uiEvent.toWaveFileUi()
                        )
                    }
                }
            }
        }
    }

    private suspend fun FileListViewModel.UiEvent.OnAddToList.toWaveFileUi(): WaveFileUi {
        return WaveFileUi(
            path = uri,
            displayName = fileResolver.getName(uri) ?: uri.toString(),
            displayPath = runCatching { uri.toFile().toString() }.getOrElse { uri.toString() }
        )
    }
}