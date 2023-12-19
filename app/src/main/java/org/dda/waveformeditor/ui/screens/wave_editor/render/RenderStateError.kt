package org.dda.waveformeditor.ui.screens.wave_editor.render

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.dda.waveformeditor.ui.screens.wave_editor.WaveEditorState

@Composable
fun RenderStateError(
    modifier: Modifier,
    state: WaveEditorState.Error,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            modifier = Modifier.padding(end = 5.dp),
            text = state.errorMessage,
            style = MaterialTheme.typography.headlineMedium,
        )
    }

}