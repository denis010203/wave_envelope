package org.dda.waveformeditor.di

import android.net.Uri
import io.mockk.mockk
import org.dda.waveformeditor.MainDispatcherRule
import org.dda.waveformeditor.extra.AppContext
import org.dda.waveformeditor.ui.common.createViewModelComponent
import org.dda.waveformeditor.ui.screens.file_list.FileListScope
import org.dda.waveformeditor.ui.screens.file_list.FileListViewModelImpl
import org.dda.waveformeditor.ui.screens.wave_editor.WaveEditorScope
import org.dda.waveformeditor.ui.screens.wave_editor.WaveEditorViewModelImpl
import org.junit.Rule
import org.junit.Test
import scout.validator.Validator
import scout.validator.tools.component.ComponentProducer


class DependencyGraphTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()


    private fun createAppComponent() = AppComponent(
        appScope = AppScope(
            appContext = AppContext(mockk())
        )
    )

    @Test
    fun `Test GlobalComponent graph`() {
        Validator.configure()
            .withConsistencyCheck()
            .validate(ComponentProducer.just(createAppComponent()))
    }

    @Test
    fun `Test FileListComponent graph`() {
        val fileListComponent = createViewModelComponent<FileListViewModelImpl, FileListScope>(
            FileListScope(
                appComponent = createAppComponent()
            )
        )
        Validator.configure()
            .withConsistencyCheck()
            .validate(ComponentProducer.just(fileListComponent))
    }

    @Test
    fun `Test WaveEditorComponent graph`() {
        val waveEditorComponent = createViewModelComponent<WaveEditorViewModelImpl, WaveEditorScope>(
            scopeHolder = WaveEditorScope(
                appScope = createAppComponent().scopeHolder,
                fileUri = mockk<Uri>(relaxed = true)
            )
        )
        Validator.configure()
            .withConsistencyCheck()
            .validate(ComponentProducer.just(waveEditorComponent))
    }
}