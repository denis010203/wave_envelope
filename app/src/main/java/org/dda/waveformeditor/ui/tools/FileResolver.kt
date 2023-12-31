package org.dda.waveformeditor.ui.tools

import android.net.Uri
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.dda.waveformeditor.extra.AppContext
import java.io.OutputStream

interface FileResolver {

    suspend fun getName(path: Uri): String?

    suspend fun readFile(path: Uri): String

    suspend fun openFile(path: Uri): OutputStream
}

class FileResolverImpl(
    private val appContext: AppContext,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) : FileResolver {

    override suspend fun getName(path: Uri): String? = doBackground {
        path.getFileName(appContext.value)
    }

    override suspend fun readFile(path: Uri): String = doBackground {
        appContext.value.contentResolver.openInputStream(path)?.use { input ->
            input.bufferedReader().readText()
        } ?: throw IllegalStateException("The provider recently crashed")
    }

    override suspend fun openFile(path: Uri): OutputStream = doBackground {
        appContext.value.contentResolver.openOutputStream(path)
            ?: throw IllegalStateException("The provider recently crashed")
    }

    private suspend fun <T> doBackground(block: suspend CoroutineScope.() -> T): T {
        return withContext(dispatcher, block)
    }

}