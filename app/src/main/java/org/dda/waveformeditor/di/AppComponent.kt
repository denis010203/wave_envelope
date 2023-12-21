package org.dda.waveformeditor.di

import org.dda.waveformeditor.domain.di.useDomainBeans
import org.dda.waveformeditor.extra.AppContext
import org.dda.waveformeditor.useAndroidAppBeans
import scout.Scope
import scout.scope


class AppComponent(
    appScope: AppScope,
) : BaseComponent<AppScope>(appScope)

class AppScope(
    appContext: AppContext
) : ScopeHolder {
    override val scope: Scope = scope("globalScope") {
        useAndroidAppBeans(appContext)
        useDomainBeans()
    }
}