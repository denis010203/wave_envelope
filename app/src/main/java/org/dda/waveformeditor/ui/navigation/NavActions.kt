package org.dda.waveformeditor.ui.navigation

import androidx.compose.runtime.Immutable
import androidx.navigation.NavHostController
import org.dda.waveformeditor.ui.screens.file_list.entities.WaveFileUi

interface NavActions {
    fun onBackPress()
    fun onSelectFile(file: WaveFileUi)
}

fun navActions(navController: NavHostController) = NavActionsImpl(navController)

@Immutable
class NavActionsImpl(
    private val navController: NavHostController
) : NavActions {

    override fun onBackPress() {
        navController.popBackStack()
    }

    override fun onSelectFile(file: WaveFileUi) {
        navController.navigate(NavDestinations.waveEditor, file.path.toString())
    }
}