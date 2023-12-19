package org.dda.waveformeditor.ui.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.CreationExtras
import scout.Component
import scout.Scope

abstract class ViewModelComponent<V : BaseViewModel>(
    scope: Scope
) : Component(scope) {

    abstract fun provideViewModel(): V

    open fun provideViewModel(extras: CreationExtras): V {
        return provideViewModel()
    }

}

class ComponentViewModelFactory<VM : BaseViewModel>(
    private val component: ViewModelComponent<VM>
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

inline fun <reified VM : BaseViewModel> ViewModelStoreOwner.viewModelComponent(
    componentProducer: () -> ViewModelComponent<VM>
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