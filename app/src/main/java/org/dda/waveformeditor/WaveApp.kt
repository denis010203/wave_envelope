package org.dda.waveformeditor

import android.app.Application
import org.dda.waveformeditor.extra.AppContext
import scout.definition.Registry

class WaveApp : Application() {
    companion object {
        var appContext: AppContext? = null
    }

    override fun onCreate() {
        super.onCreate()
        appContext = AppContext(this)
    }
}

fun Registry.useAndroidAppBeans() {
    reusable<AppContext> {
        requireNotNull(WaveApp.appContext)
    }
}