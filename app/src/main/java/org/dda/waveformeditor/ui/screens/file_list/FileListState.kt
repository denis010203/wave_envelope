package org.dda.waveformeditor.ui.screens.file_list

import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.toImmutableSet
import org.dda.waveformeditor.ui.screens.file_list.entities.WaveFileUi

sealed interface FileListState {

    fun onAddFileToList(file: WaveFileUi): FileListState

    data class ListFiles(
        val files: ImmutableSet<WaveFileUi> = emptySet<WaveFileUi>().toImmutableSet()
    ) : FileListState {
        override fun onAddFileToList(file: WaveFileUi): FileListState {
            return copy(
                files = files.plus(file).toImmutableSet()
            )
        }
    }

}