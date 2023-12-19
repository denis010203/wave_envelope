package org.dda.waveformeditor.ui.screens.wave_editor

import android.net.Uri
import org.dda.waveformeditor.domain.entities.WaveEditData
import org.dda.waveformeditor.domain.entities.isValidSelection
import org.dda.waveformeditor.domain.entities.parseToWaveData

sealed interface WaveEditorState {

    fun onReadFileName(name: String?): WaveEditorState {
        return when (this) {
            is Init -> copy(title = name ?: "")
            is Loaded -> copy(title = name ?: "")
            is Error -> copy(title = name ?: "")
        }
    }

    suspend fun onReadFileContent(fileData: String): WaveEditorState {
        return fileData.parseToWaveData().map { waveData ->
            when (this) {
                is Init, is Error -> Loaded(
                    uri = uri,
                    title = title,
                    fileData = fileData,
                    waveEditData = WaveEditData(waveData)
                )

                is Loaded -> copy(
                    fileData = fileData,
                    waveEditData = WaveEditData(waveData)
                )
            }
        }.getOrElse { thr ->
            onFileReadFileContent(thr)
        }
    }

    fun onFileReadFileContent(thr: Throwable): WaveEditorState {
        return Error(
            uri = uri,
            title = title,
            errorMessage = thr.message ?: "Unknown message"
        )
    }

    fun onSelectWave(startIndex: Int, endIndex: Int): WaveEditorState {
        return when (this) {
            is Error, is Init -> this
            is Loaded -> {
                if (waveEditData.waveData.isValidSelection(startIndex, endIndex)) {
                    copy(
                        waveEditData = waveEditData.copy(
                            selectionStartPairIndex = startIndex,
                            selectionEndPairIndex = endIndex,
                        )
                    )
                } else {
                    this
                }
            }
        }
    }

    fun onSwitchDebug(enable: Boolean): WaveEditorState {
        return when (this) {
            is Error, is Init -> this
            is Loaded -> {
                copy(showDebug = enable)
            }
        }
    }

    /////////////

    val uri: Uri
    val title: String


    data class Init(
        override val uri: Uri,
        override val title: String = ""
    ) : WaveEditorState

    data class Loaded(
        override val uri: Uri,
        override val title: String = "",
        val fileData: String,
        val showDebug: Boolean = false,
        val waveEditData: WaveEditData,
    ) : WaveEditorState

    data class Error(
        override val uri: Uri,
        override val title: String = "",
        val errorMessage: String,
    ) : WaveEditorState
}