package org.dda.waveformeditor.ui.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.dda.waveformeditor.di.AppComponent
import org.dda.waveformeditor.ui.common.viewModelComponent
import org.dda.waveformeditor.ui.screens.file_list.FileListScope
import org.dda.waveformeditor.ui.screens.file_list.render.RenderFileListScreen
import org.dda.waveformeditor.ui.screens.wave_editor.WaveEditorScope
import org.dda.waveformeditor.ui.screens.wave_editor.render.RenderWaveEditorScreen

@Composable
fun NavScreens(
    getAppComponent: () -> AppComponent
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = NavDestinations.fileList.route
    ) {
        composable(route = NavDestinations.fileList.route) { backStackEntry ->
            val stockListViewModel by backStackEntry.viewModelComponent(
                scopeHolder = FileListScope(getAppComponent())
            )
            RenderFileListScreen(
                viewModel = stockListViewModel,
                navActions = navActions(navController)
            )
        }

        composable(route = NavDestinations.waveEditor.route) { backStackEntry ->
            val waveEditorViewModel by backStackEntry.viewModelComponent(
                scopeHolder = WaveEditorScope(
                    appScope = getAppComponent().scopeHolder,
                    fileUri = requireNotNull(
                        NavDestinations.waveEditor.getArgumentString(backStackEntry)
                    ).let { stringArg ->
                        Uri.parse(stringArg)
                    }
                )
            )
            RenderWaveEditorScreen(
                viewModel = waveEditorViewModel,
                navActions = navActions(navController)
            )
        }
    }
}