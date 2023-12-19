package org.dda.waveformeditor.ui.screens.file_list

import org.dda.waveformeditor.di.globalScope
import org.dda.waveformeditor.extra.AppContext
import org.dda.waveformeditor.ui.common.ViewModelComponent
import org.dda.waveformeditor.ui.tools.FileResolverImpl
import scout.scope


private val fileListScope = scope("fileListScope") {
    dependsOn(globalScope)
}

class FileListComponent : ViewModelComponent<FileListViewModelImpl>(fileListScope) {
    override fun provideViewModel(): FileListViewModelImpl {
        return FileListViewModelImpl(
            fileResolver = FileResolverImpl(
                appContext = getProvider<AppContext>().get()
            )
        )
    }
}