package org.dda.waveformeditor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import org.dda.waveformeditor.ui.navigation.NavScreens
import org.dda.waveformeditor.ui.theme.WaveformEditorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WaveformEditorTheme {
                NavScreens {
                    (application as WaveApp).appComponent
                }
            }
        }
    }
}