package org.dda.waveformeditor.ui.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.CreationExtras
import org.dda.waveformeditor.di.BaseComponent
import org.dda.waveformeditor.di.ScopeHolder

interface ViewModelScopeHolder<VM : BaseViewModel> : ScopeHolder

abstract class ViewModelComponent<VM : BaseViewModel, SH : ViewModelScopeHolder<VM>>(
    scopeHolder: SH
) : BaseComponent<SH>(scopeHolder) {

    abstract fun provideViewModel(): VM

    open fun provideViewModel(extras: CreationExtras): VM {
        return provideViewModel()
    }

}

inline fun <reified VM : BaseViewModel, reified SH : ViewModelScopeHolder<VM>> createViewModelComponent(
    scopeHolder: SH
): ViewModelComponent<VM, SH> {
    return object : ViewModelComponent<VM, SH>(scopeHolder) {
        override fun provideViewModel(): VM {
            return get<VM>()
        }
    }
}

class ComponentViewModelFactory<VM : BaseViewModel, SH : ViewModelScopeHolder<VM>>(
    private val component: ViewModelComponent<VM, SH>
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return component.provideViewModel() as T
    }

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        @Suppress("UNCHECKED_CAST")
        return component.provideViewModel(extras) as T
    }
}

inline fun <reified VM : BaseViewModel, SH : ViewModelScopeHolder<VM>> ViewModelStoreOwner.viewModelComponent(
    componentProducer: () -> ViewModelComponent<VM, SH>
): Lazy<VM> {

    val factory = ComponentViewModelFactory(
        component = componentProducer()
    )

    return ViewModelLazy(
        viewModelClass = VM::class,
        storeProducer = { viewModelStore },
        factoryProducer = { factory },
    )
}

inline fun <reified VM : BaseViewModel, reified SH : ViewModelScopeHolder<VM>> ViewModelStoreOwner.viewModelComponent(
    scopeHolder: SH
): Lazy<VM> {
    return viewModelComponent<VM, SH> {
        createViewModelComponent<VM, SH>(scopeHolder)
    }
}