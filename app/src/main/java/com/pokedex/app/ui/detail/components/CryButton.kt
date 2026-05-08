package com.pokedex.app.ui.detail.components

import android.media.MediaPlayer
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CryButton(cryUrl: String?, modifier: Modifier = Modifier) {
    if (cryUrl.isNullOrBlank()) return

    val player = remember { MediaPlayer() }

    DisposableEffect(Unit) {
        onDispose {
            runCatching {
                if (player.isPlaying) player.stop()
                player.reset()
                player.release()
            }
        }
    }

    OutlinedButton(
        onClick = {
            runCatching {
                player.reset()
                player.setDataSource(cryUrl)
                player.setOnPreparedListener { it.start() }
                player.prepareAsync()
            }
        },
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            Icon(Icons.Default.PlayArrow, contentDescription = null)
            Text("Écouter le cri", fontSize = 13.sp)
        }
    }
}
