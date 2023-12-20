package org.dda.waveformeditor.ui.screens.wave_editor

import android.net.Uri
import org.dda.waveformeditor.di.AppScope
import org.dda.waveformeditor.extra.AppContext
import org.dda.waveformeditor.ui.common.ViewModelScopeHolder
import org.dda.waveformeditor.ui.tools.FileResolverImpl
import scout.scope


class WaveEditorScope(
    appScope: AppScope,
    fileUri: Uri
) : ViewModelScopeHolder<WaveEditorViewModelImpl> {

    override val scope = scope("waveEditorScope") {
        dependsOn(appScope.scope)

        factory<WaveEditorViewModelImpl> {
            WaveEditorViewModelImpl(
                fileUri = fileUri,
                fileResolver = FileResolverImpl(
                    appContext = getProvider<AppContext>().get()
                )
            )
        }
    }
}