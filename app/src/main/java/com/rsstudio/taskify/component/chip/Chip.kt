package com.rsstudio.taskify.component.chip

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rsstudio.taskify.ui.theme.Purple40
import com.rsstudio.taskify.ui.theme.label


@Composable
fun Chip(
    text: String,
    selected: Boolean = false,
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(
                if (selected) {
                    Purple40
                } else {
                    Color.Transparent
                }
            )
            .border(
                width = if (selected) 0.dp else 1.dp,
                color = if (selected) Color.Transparent else Color.LightGray,
                shape = CircleShape
            )
            .clickable {
                onClick(text)
            }
            .padding(
                horizontal = 12.dp,
                vertical = 12.dp
            ),
    ) {

        Text(
            text = text,
            style = MaterialTheme.typography.label.copy(
                fontSize = 14.sp,
                color = if (selected) Color.White else Color.Black,
            )
        )
    }
}


@Preview(showBackground = true)
@Composable
fun ChipPreview() {
    Chip(
        text = "Daily",
        selected = true,
        onClick = {}
    )
}
