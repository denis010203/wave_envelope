package org.dda.waveformeditor.ui.screens.wave_editor

import android.net.Uri
import org.dda.waveformeditor.di.globalScope
import org.dda.waveformeditor.extra.AppContext
import org.dda.waveformeditor.ui.common.ViewModelComponent
import org.dda.waveformeditor.ui.tools.FileResolverImpl
import scout.scope


private val waveEditorScope = scope("waveEditorScope") {
    dependsOn(globalScope)
}

class WaveEditorComponent(
    private val fileUri: Uri
) : ViewModelComponent<WaveEditorViewModelImpl>(waveEditorScope) {
    override fun provideViewModel(): WaveEditorViewModelImpl {
        return WaveEditorViewModelImpl(
            fileUri = fileUri,
            fileResolver = FileResolverImpl(
                appContext = getProvider<AppContext>().get()
            )
        )
    }
}