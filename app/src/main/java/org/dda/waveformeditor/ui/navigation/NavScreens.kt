package org.dda.waveformeditor.ui.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.dda.waveformeditor.ui.common.viewModelComponent
import org.dda.waveformeditor.ui.screens.file_list.FileListComponent
import org.dda.waveformeditor.ui.screens.file_list.FileListViewModel
import org.dda.waveformeditor.ui.screens.file_list.render.RenderFileListScreen
import org.dda.waveformeditor.ui.screens.wave_editor.WaveEditorComponent
import org.dda.waveformeditor.ui.screens.wave_editor.WaveEditorViewModel
import org.dda.waveformeditor.ui.screens.wave_editor.render.RenderWaveEditorScreen

@Composable
fun NavScreens() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = NavDestinations.fileList.route
    ) {
        composable(route = NavDestinations.fileList.route) { backStackEntry ->
            val stockListViewModel: FileListViewModel by backStackEntry.viewModelComponent {
                FileListComponent()
            }
            RenderFileListScreen(
                viewModel = stockListViewModel,
                navActions = navActions(navController)
            )
        }

        composable(route = NavDestinations.waveEditor.route) { backStackEntry ->
            val waveEditorViewModel: WaveEditorViewModel by backStackEntry.viewModelComponent {
                WaveEditorComponent(
                    fileUri = requireNotNull(
                        NavDestinations.waveEditor.getArgumentString(backStackEntry)
                    ).let { stringArg ->
                        Uri.parse(stringArg)
                    }
                )
            }
            RenderWaveEditorScreen(
                viewModel = waveEditorViewModel,
                navActions = navActions(navController)
            )
        }
    }
}