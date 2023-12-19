package org.dda.waveformeditor.di

import org.dda.waveformeditor.useAndroidAppBeans
import scout.Component
import scout.scope


object GlobalComponent : Component(globalScope)

val globalScope = scope("globalScope") {
    useAndroidAppBeans()
}