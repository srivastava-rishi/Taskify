package com.rsstudio.taskify.component.textfield

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rsstudio.taskify.ui.theme.ht2
import com.rsstudio.taskify.ui.theme.lightBlack
import com.rsstudio.taskify.ui.theme.subTitle


@Composable
fun OutlinedTextFieldWithLabel(
    modifier: Modifier = Modifier,
    value: TextFieldValue = TextFieldValue(),
    label: String? = null,
    onTextChange: (TextFieldValue) -> Unit
) {

    Column {
        label?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.ht2.copy(fontSize = 14.sp, color = lightBlack)
            )
            Spacer(modifier = Modifier.size(12.dp))
        }
        Box(
            modifier = modifier
                .border(
                    width = 1.dp,
                    color = Color.LightGray,
                    shape = RoundedCornerShape(20.dp)
                )
                .fillMaxWidth()
                .height(80.dp),
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = value,
                onValueChange = {
                    onTextChange(it)
                },
                textStyle = MaterialTheme.typography.subTitle,
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                ),
                keyboardOptions = KeyboardOptions.Default
            )
        }
    }


}

@Preview(showBackground = true)
@Composable
fun OutlinedTextFieldPreview() {
    OutlinedTextFieldWithLabel(
        label = "Title",
        onTextChange = {}
    )
}
