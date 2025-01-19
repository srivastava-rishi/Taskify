package com.rsstudio.taskify.component.dropdown

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rsstudio.taskify.ui.common.extension.crop
import com.rsstudio.taskify.ui.theme.ht2


@Composable
fun DropDown(
    openMenu: Boolean,
    menuItems: List<MenuActionItem> = emptyList(),
    offset: DpOffset = DpOffset(x = (-8.dp), y = (0.dp)),
    onTitleBarActionClick: ((MenuActionItem) -> Unit)? = null,
    onDismissMenu: () -> Unit
) {
    MaterialTheme(
        shapes = MaterialTheme.shapes.copy(
            extraSmall = RoundedCornerShape(8.dp)
        )
    ) {
        DropdownMenu(
            modifier = Modifier
                .background(color = Color.White)
                .crop(vertical = 8.dp, horizontal = 4.dp),
            expanded = openMenu,
            onDismissRequest = {
                onDismissMenu()
            },
            offset = offset
        ) {
            menuItems.forEach {
                DropdownMenuItem(
                    modifier = Modifier.crop(vertical = 6.dp),
                    text = {
                        Text(
                            text = it.title,
                            style = MaterialTheme.typography.ht2.copy(
                                fontSize = 12.sp
                            )
                        )
                    },
                    interactionSource = MutableInteractionSource(),
                    onClick = {
                        onTitleBarActionClick?.invoke(it)
                    })

            }
        }
    }
}

data class MenuActionItem(
    val id: String,
    val title: String
)

