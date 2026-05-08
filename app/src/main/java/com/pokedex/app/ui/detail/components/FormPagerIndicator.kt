package com.pokedex.app.ui.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pokedex.app.ui.theme.PokeRed

@Composable
fun FormPagerIndicator(pageCount: Int, current: Int, modifier: Modifier = Modifier) {
    if (pageCount <= 1) return
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "${current + 1} / $pageCount",
            fontSize = 11.sp,
            color = Color.Gray
        )
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            repeat(pageCount) { idx ->
                Box(
                    modifier = Modifier
                        .size(if (idx == current) 9.dp else 7.dp)
                        .clip(CircleShape)
                        .background(if (idx == current) PokeRed else Color.LightGray)
                )
            }
        }
    }
}
