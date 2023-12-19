package org.dda.waveformeditor.ui.screens.file_list.entities

import android.net.Uri

data class WaveFileUi(
    val path: Uri,
    val displayName: String,
    val displayPath: String = path.toString(),
) {
    val key: String = displayPath
}