package org.dda.waveformeditor.ui.screens.file_list

import org.dda.waveformeditor.di.AppComponent
import org.dda.waveformeditor.di.AppScope
import org.dda.waveformeditor.extra.AppContext
import org.dda.waveformeditor.ui.common.ViewModelScopeHolder
import org.dda.waveformeditor.ui.tools.FileResolverImpl
import scout.scope


class FileListScope(
    appScope: AppScope
) : ViewModelScopeHolder<FileListViewModelImpl> {
    constructor(appComponent: AppComponent) : this(appComponent.scopeHolder)

    override val scope = scope("fileListScope") {
        dependsOn(appScope.scope)

        factory<FileListViewModelImpl> {
            FileListViewModelImpl(
                fileResolver = FileResolverImpl(
                    appContext = getProvider<AppContext>().get()
                )
            )
        }
    }
}