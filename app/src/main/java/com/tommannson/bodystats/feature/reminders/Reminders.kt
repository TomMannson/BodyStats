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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.tommannson.bodystats.feature.Screen
import com.tommannson.bodystats.feature.configuration.Events
import com.tommannson.bodystats.feature.configuration.widgets.TopBar
import com.tommannson.bodystats.infrastructure.ReminderDefinition
import com.tommannson.bodystats.model.reminding.ReminderType
import com.tommannson.bodystats.model.reminding.text
import com.tommannson.bodystats.ui.controls.Progress
import com.tommannson.bodystats.ui.theme.veryLightGray
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogButtons
import com.vanpra.composematerialdialogs.MaterialDialogScope
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.flow.collect
import java.time.LocalTime
import java.time.temporal.ChronoUnit

@Composable
fun Reminders(navController: NavController) {
    val scafoldState = rememberScaffoldState();

    val vm = hiltViewModel<RemindersListViewModel>()
    val state by vm.state.collectAsState()

    Scaffold(
        topBar = { TopBar(navController, title = "Powiadomienia") },
        scaffoldState = scafoldState
    ) {

        Column() {
            ReminderItem(
                "Ważenie",
                getReminder(state.groupedDefinitions, ReminderType.Weight),
                enabled = hasReminderEnabled(state.groupedDefinitions, ReminderType.Weight),
                onTap = {
                    navController.navigate(Screen.ReminderConfigScreen.routeWithParam(ReminderType.Weight))
                },
                onToggle = {
                    vm.toggle(ReminderType.Weight, it)
                }
            )
            ReminderItem(
                "Kontrola wymiarów",
                getReminder(state.groupedDefinitions, ReminderType.Measurements),
                enabled = hasReminderEnabled(state.groupedDefinitions, ReminderType.Measurements),
                onTap = {
                    navController.navigate(Screen.ReminderConfigScreen.routeWithParam(ReminderType.Measurements))
                },
                onToggle = {
                    vm.toggle(ReminderType.Measurements, it)
                }

            )
            ReminderItem(
                "Skład ciała",
                getReminder(state.groupedDefinitions, ReminderType.Composition),
                enabled = hasReminderEnabled(state.groupedDefinitions, ReminderType.Composition),
                onTap = {
                    navController.navigate(Screen.ReminderConfigScreen.routeWithParam(ReminderType.Composition))
                },
                onToggle = {
                    vm.toggle(ReminderType.Composition, it)
                }
            )
        }
    }
}

fun hasReminderEnabled(
    map: Map<String, List<ReminderDefinition>>,
    type: ReminderType
): Boolean {
    val loadedColection = map.getOrDefault(type.name, listOf())
    if (loadedColection.isEmpty()) {
        return false
    } else {
        return loadedColection.first().enabled
    }
}

fun getReminder(
    map: Map<String, List<ReminderDefinition>>,
    type: ReminderType
): ReminderDefinition? {
    val loadedColection = map.getOrDefault(type.name, listOf())
    if (loadedColection.isEmpty()) {
        return null
    } else {
        return loadedColection.firstOrNull()
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ReminderItem(
    title: String,
    data: ReminderDefinition?,
    enabled: Boolean,
    onTap: () -> Unit,
    onToggle: (Boolean) -> Unit
) {
    val secondaryText: @Composable (() -> Unit)? = if (data != null) {
        {
            val text = "Co ${data.timeBetweenReminders} tygodnie o ${data.timeOfReminder}"
            Text(text, style = MaterialTheme.typography.subtitle1)
        }
    } else {
        null
    }

    ListItem(
        modifier = Modifier.clickable(onClick = onTap),
        text = { Text(title) },
        secondaryText = secondaryText,
        trailing = {
            Switch(
                checked = enabled,
                onCheckedChange = {
                    onToggle(!enabled)
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colors.primary,
                    uncheckedThumbColor = Color.LightGray
                )
            )
        }
    )
}


@Composable
fun ReminderConfig(navController: NavController, reminderType: ReminderType) {
    val scafoldState = rememberScaffoldState();

    val vm = hiltViewModel<RemindersListViewModel>()
    val state by vm.state.collectAsState()

    LaunchedEffect(key1 = vm) {
        vm.actions.collect {
            when (it) {
                is Actions.Success -> navController.popBackStack()
            }
        }
    }



    Scaffold(
        topBar = { TopBar(navController) },
        scaffoldState = scafoldState
    ) {

        when (state) {
            null -> Progress()
            else -> LoadedData(reminderType, vm, state.groupedDefinitions)
        }
    }
}


@Composable
fun LoadedData(
    reminderType: ReminderType,
    vm: RemindersListViewModel,
    map: Map<String, List<ReminderDefinition>>
) {
    val list = map.getOrDefault(reminderType.name, listOf())
    val data = list.firstOrNull()

    var daySelection by remember(data) {
        mutableStateOf(data?.days?.split("_")?.map { it.toInt() } ?: listOf())
    }
    var number by remember(data) { mutableStateOf(data?.timeBetweenReminders ?: 1) }
    var time by remember(data) {
        val secondsOfDay = data?.timeOfReminder?.toSecondOfDay() ?: LocalTime.now()
            .truncatedTo(ChronoUnit.MINUTES).toSecondOfDay()
        mutableStateOf(
            LocalTime.ofSecondOfDay(secondsOfDay.toLong())
        )
    }


    CompositionLocalProvider(
//        LocalTextStyle provides LocalTextStyle.current.copy(fontWeight = FontWeight.Bold),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(reminderType.text)
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {


                Text("Powtarzaj co")
                Spacer(modifier = Modifier.width(8.dp))
                NumberSelector(
                    number,
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

            DaySelector(selectedDays = daySelection,
                onSelectionChanged = {
                    daySelection = it
                }
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {


                Text("Wybierz czas: ")
                val ctx = LocalContext.current
                Spacer(modifier = Modifier.width(8.dp))
                TimePicker(time,
                    onTimeSelected = {
                        time = it
                        Toast.makeText(ctx, "$it", Toast.LENGTH_SHORT).show()
                    })
            }

            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {
                    val longTime = time.toSecondOfDay().toLong()
                    vm.setReminder(
                        reminderType,
                        number,
                        daySelection,
                        org.threeten.bp.LocalTime.ofSecondOfDay(longTime)
                    )
                },
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
    DaySelector() {}
}

@Composable
fun DaySelector(
    modifier: Modifier = Modifier,
    selectedDays: List<Int> = listOf(),
    onSelectionChanged: (List<Int>) -> Unit
) {

    val days = arrayOf("Pn", "Wt", "Śr", "Cz", "Pt", "So", "Nd")
    var map by remember(selectedDays) {
        val mapOfDays = mutableMapOf<Int, Boolean>()
        for (day in selectedDays) {
            mapOfDays[day] = true
        }
        mutableStateOf(mapOfDays)
    }

    Row(modifier = modifier) {
        for (index in 1..days.size) {

            val color = if (map.getOrDefault(index, false)) {
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
                        val newSelection = map
                            .filter { it.value == true }
                            .toList()
                            .map { it.first }
                        onSelectionChanged(newSelection)
                    }
            ) {
                Text(
                    days[index - 1], modifier = Modifier
                        .fillMaxSize()
                        .wrapContentHeight(),
                    style = LocalTextStyle.current.copy(
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp
                    )
                )
            }
        }
    }
}

@Composable
fun TimePicker(initialTime: LocalTime, onTimeSelected: (LocalTime) -> Unit) {

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

