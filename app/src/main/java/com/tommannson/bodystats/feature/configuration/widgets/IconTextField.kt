package com.tommannson.bodystats.feature.configuration.widgets

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.tommannson.bodystats.R

@Composable
fun IconTextField(value: String,
                  iconPainter: Painter,
                  label: String,
                  keyboardOptions: KeyboardOptions,
                  keyboardActions: KeyboardActions? = null,
                  onValueChange: (String) -> Unit,) {

    val focusManager = LocalFocusManager.current
    val localKeyboardActions = keyboardActions ?: KeyboardActions(
        onNext = {
            focusManager.moveFocus(FocusDirection.Down)
        }
    )


    Row {
        Box(modifier = Modifier.height(72.dp).width(54.dp)) {
            Icon(
                modifier = Modifier.align(Alignment.Center),
                painter = iconPainter,
                contentDescription = null
            )
        }
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().padding(end = 16.dp),
            label = { Text(label) },
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            keyboardOptions = keyboardOptions,
            keyboardActions = localKeyboardActions
        )
    }
}