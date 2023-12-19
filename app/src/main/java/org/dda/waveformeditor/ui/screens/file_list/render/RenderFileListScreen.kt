package org.dda.waveformeditor.ui.screens.file_list.render

import android.net.Uri
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.dda.waveformeditor.ui.navigation.NavActions
import org.dda.waveformeditor.ui.screens.file_list.FileListState
import org.dda.waveformeditor.ui.screens.file_list.FileListViewModel
import org.dda.waveformeditor.ui.screens.file_list.entities.WaveFileUi

@Composable
fun RenderFileListScreen(
    viewModel: FileListViewModel,
    navActions: NavActions,
) {

    val state = viewModel.stateFlow.collectAsStateWithLifecycle()

    val onAddToList: (uri: Uri) -> Unit = remember {
        { uri -> viewModel.onUiEvent(FileListViewModel.UiEvent.OnAddToList(uri)) }
    }

    val onSelectFile: (file: WaveFileUi) -> Unit = remember {
        { file -> navActions.onSelectFile(file) }
    }


    Scaffold(
        content = { paddingValues ->
            when (val lclState = state.value) {
                is FileListState.ListFiles -> RenderAddedFilesList(
                    modifier = Modifier.padding(paddingValues),
                    files = lclState.files,
                    onSelectFile = onSelectFile,
                )
            }
        },
        floatingActionButton = {
            AddFileFab(onAddToList = onAddToList)
        }
    )

}