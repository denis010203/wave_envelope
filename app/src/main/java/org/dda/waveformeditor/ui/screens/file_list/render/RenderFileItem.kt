package org.dda.waveformeditor.ui.screens.file_list.render

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.dda.waveformeditor.ui.screens.file_list.entities.WaveFileUi
import org.dda.waveformeditor.ui.theme.RenderConst
import org.dda.waveformeditor.ui.theme.WaveformEditorTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RenderFileItem(
    modifier: Modifier = Modifier,
    file: WaveFileUi,
    onSelectFile: (file: WaveFileUi) -> Unit
) {
    Card(
        onClick = {
            onSelectFile(file)
        },
        modifier = modifier
            .height(IntrinsicSize.Min)
    ) {
        Row(
            modifier = Modifier
                .padding(RenderConst.smallCardContentPadding)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        modifier = Modifier.padding(end = 5.dp),
                        text = "File name:",
                        style = MaterialTheme.typography.labelLarge,
                    )
                    Text(
                        text = file.displayName,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                    )
                }
                Row(
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        modifier = Modifier.padding(end = 5.dp),
                        text = "File path:",
                        style = MaterialTheme.typography.labelLarge,
                    )
                    Text(
                        text = file.displayPath,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun RenderStockItemPreview() {
    WaveformEditorTheme {
        RenderFileItem(
            modifier = Modifier.fillMaxWidth(),
            file = WaveFileUi(
                path = Uri.parse(""),
                displayName = "displayName",
                displayPath = "displayPath",
            ),
        ) {

        }
    }
}
