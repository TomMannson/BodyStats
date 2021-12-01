package com.tommannson.bodystats.feature.reminders

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.tommannson.bodystats.feature.configuration.widgets.TopBar
import com.tommannson.bodystats.ui.controls.Progress
import com.tommannson.bodystats.ui.theme.veryLightGray
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogButtons
import com.vanpra.composematerialdialogs.MaterialDialogScope
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalTime
import java.time.temporal.ChronoUnit

@Composable
fun Reminders(navController: NavController) {
    val scafoldState = rememberScaffoldState();

    Scaffold(
        topBar = { TopBar(navController) },
        scaffoldState = scafoldState
    ) {

        var state: String? = "null"

        when (state) {
            null -> Progress()
            else -> LoadedData()

        }
    }
}


@Composable
fun LoadedData() {

    CompositionLocalProvider(
//        LocalTextStyle provides LocalTextStyle.current.copy(fontWeight = FontWeight.Bold),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Powiadomienie pomiaru Wagi")
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                var number by remember { mutableStateOf(1) }

                Text("Powtarzaj co")
                Spacer(modifier = Modifier.width(8.dp))
                NumberSelector(number,
                    onValueChange = { number = it },
                    modifier = Modifier
                        .background(MaterialTheme.colors.veryLightGray)
                        .padding(4.dp)
                    )
                Spacer(modifier = Modifier.width(8.dp))
                Text("tygodni")

            }
            Spacer(modifier = Modifier.height(16.dp))
            Text("Powtarzaj w")
            DaySelector()

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                var time by remember {
                    mutableStateOf(
                        LocalTime.now().truncatedTo(ChronoUnit.HOURS)
                    )
                }

                Text("Wybierz czas: ")
                val ctx = LocalContext.current
                Spacer(modifier = Modifier.width(8.dp))
                TimePicker(time, onTimeSelected = {
                    time = it
                    Toast.makeText(ctx, "$it", Toast.LENGTH_SHORT).show()
                })
            }

            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(text = "Zapisz")
            }
        }
    }
}

@Preview
@Composable
fun PreviewDaySelector() {
    DaySelector()
}

@Composable
fun DaySelector(modifier: Modifier = Modifier) {

    val days = arrayOf("Pn", "Wt", "Śr", "Cz", "Pt", "So", "Nd")
    var map by remember {
        mutableStateOf(mapOf<Int, Boolean>())
    }

    Row(modifier = modifier) {
        for (index in 0 until days.size) {

            val color = if(map.getOrDefault(index, false)){
                MaterialTheme.colors.primary
            } else {
                MaterialTheme.colors.veryLightGray
            }

            Surface(
                shape = CircleShape,
                color = color,
                modifier = Modifier
                    .padding(4.dp)
                    .size(32.dp)
                    .clip(CircleShape)
                    .clickable {

                        val copy = map.getOrDefault(index, false)
                        val copyMap = map.toMutableMap()
                        copyMap[index] = !copy
                        map = copyMap
                    }
            ) {
                Text(days[index], modifier = Modifier
                    .fillMaxSize()
                    .wrapContentHeight(),
                    style = LocalTextStyle.current.copy(textAlign = TextAlign.Center, fontSize = 12.sp)
                )
            }
        }
    }
}

@Composable
fun TimePicker(initialTime: LocalTime, onTimeSelected: (LocalTime) -> Unit ) {

    var timeSelected by remember {
        mutableStateOf(initialTime)
    }

    DialogAndShowButton(
        buttonText = initialTime.toString(),
        buttons = {
            positiveButton(
                text = "Ok",
                onClick = {
                    onTimeSelected(timeSelected)
                }
            )
            negativeButton(
                text = "Cancel",
                onClick = {}
            )
        },
        content = {

            val context = LocalContext.current

            timepicker(
                initialTime = timeSelected,
                is24HourClock = true
            ) {
                timeSelected = it
            }
        }
    )
}

@Composable
fun DialogAndShowButton(
    buttonText: String,
    buttons: @Composable MaterialDialogButtons.() -> Unit = {},
    content: @Composable MaterialDialogScope.() -> Unit
) {
    val dialogState = rememberMaterialDialogState()

    MaterialDialog(dialogState = dialogState, buttons = buttons) {
        content()
    }

    TextButton(
        onClick = { dialogState.show() },
    ) {
        Text(
            buttonText,
            modifier = Modifier
                .background(MaterialTheme.colors.veryLightGray)
                .padding(4.dp)
                .wrapContentSize(Alignment.Center),
        )
    }
}

