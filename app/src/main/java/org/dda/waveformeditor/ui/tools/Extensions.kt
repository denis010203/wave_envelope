package org.dda.waveformeditor.ui.tools

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import java.io.File

fun Uri.getFileName(
    context: Context
): String? = when (scheme) {
    ContentResolver.SCHEME_CONTENT -> {
        runCatching {
            context.contentResolver.query(
                /* uri = */ this,
                /* projection = */ null,
                /* selection = */ null,
                /* selectionArgs = */ null,
                /* sortOrder = */ null
            )?.use { cursor ->
                cursor.moveToFirst()
                cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME).let(cursor::getString)
            }
        }.getOrNull()
    }

    else -> path?.let(::File)?.name
}