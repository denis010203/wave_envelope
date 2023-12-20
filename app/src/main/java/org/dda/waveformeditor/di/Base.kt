package org.dda.waveformeditor.di

import scout.Component
import scout.Scope

interface ScopeHolder {
    val scope: Scope
}

abstract class BaseComponent<SH : ScopeHolder>(
    val scopeHolder: SH
) : Component(scopeHolder.scope)