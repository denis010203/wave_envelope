package org.dda.waveformeditor.domain.di

import org.dda.waveformeditor.domain.WaveDataFormat
import org.dda.waveformeditor.domain.WaveDataFormatImpl
import scout.definition.Registry

fun Registry.useDomainBeans() {
    factory<WaveDataFormat> {
        WaveDataFormatImpl()
    }
}