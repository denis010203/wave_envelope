package org.dda.waveformeditor.di

import android.net.Uri
import io.mockk.mockk
import org.dda.waveformeditor.MainDispatcherRule
import org.dda.waveformeditor.ui.screens.file_list.FileListComponent
import org.dda.waveformeditor.ui.screens.wave_editor.WaveEditorComponent
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import scout.validator.Validator
import scout.validator.tools.component.ComponentProducer


class DependencyGraphTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()


    @Test
    fun `Test GlobalComponent graph`() {
        Validator.configure()
            .withConsistencyCheck()
            .validate(ComponentProducer.just(GlobalComponent))
    }

    @Ignore("Need to fix useAndroidAppBeans")
    @Test
    fun `Test FileListComponent graph`() {
        Validator.configure()
            .withConsistencyCheck()
            .validate(ComponentProducer.just(FileListComponent()))
    }

    @Ignore("Need to fix useAndroidAppBeans")
    @Test
    fun `Test WaveEditorComponent graph`() {
        Validator.configure()
            .withConsistencyCheck()
            .validate(ComponentProducer.just(WaveEditorComponent(mockk<Uri>(relaxed = true))))
    }
}