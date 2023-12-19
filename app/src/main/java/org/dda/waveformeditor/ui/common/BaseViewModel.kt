package org.dda.waveformeditor.ui.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope

abstract class BaseViewModel(
    coroutineScope: CoroutineScope? = null
) : ViewModel() {
    protected val coroutineScope: CoroutineScope by lazy {
        coroutineScope ?: viewModelScope
    }
}