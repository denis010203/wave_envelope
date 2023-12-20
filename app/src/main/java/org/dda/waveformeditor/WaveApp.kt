package org.dda.waveformeditor

import android.app.Application
import org.dda.waveformeditor.di.AppComponent
import org.dda.waveformeditor.di.AppScope
import org.dda.waveformeditor.extra.AppContext
import scout.definition.Registry

class WaveApp : Application() {

    val appComponent by lazy {
        AppComponent(
            appScope = AppScope(
                appContext = AppContext(this)
            )
        )
    }

}

fun Registry.useAndroidAppBeans(appContext: AppContext) {
    singleton<AppContext> {
        appContext
    }
}