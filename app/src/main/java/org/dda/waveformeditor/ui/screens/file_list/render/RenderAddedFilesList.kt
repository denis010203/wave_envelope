package org.dda.waveformeditor.ui.screens.file_list.render

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableCollection
import org.dda.waveformeditor.ui.screens.file_list.entities.WaveFileUi
import org.dda.waveformeditor.ui.theme.RenderConst


@Composable
fun RenderAddedFilesList(
    modifier: Modifier,
    files: ImmutableCollection<WaveFileUi>,
    onSelectFile: (file: WaveFileUi) -> Unit
) {

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(
            vertical = 10.dp,
            horizontal = RenderConst.paddingPageEdge,
        )
    ) {
        files.forEach { file ->
            item(
                key = file.key
            ) {
                RenderFileItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(),
                    file = file,
                    onSelectFile = onSelectFile
                )

                Divider(
                    color = Color.Transparent,
                    thickness = RenderConst.listItemPadding
                )
            }
        }
    }
}


@Composable
fun AddFileFab(
    onAddToList: (uri: Uri) -> Unit
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri != null) {
            onAddToList(uri)
        }
    }
    FloatingActionButton(
        onClick = { launcher.launch(arrayOf("text/plain")) }
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 10.dp),
            text = "Add file",
        )
    }

}