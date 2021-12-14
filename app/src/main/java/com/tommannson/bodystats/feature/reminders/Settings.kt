package com.tommannson.bodystats.feature.reminders

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.tommannson.bodystats.R
import com.tommannson.bodystats.feature.MainActivity
import com.tommannson.bodystats.feature.Screen
import com.tommannson.bodystats.feature.configuration.widgets.TopBar
import com.tommannson.bodystats.infrastructure.ReminderDefinition
import com.tommannson.bodystats.model.reminding.ReminderType

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Settings(navController: NavController) {
    val scafoldState = rememberScaffoldState();

    val vm = hiltViewModel<RemindersListViewModel>()
    val state by vm.state.collectAsState()

    val context = LocalContext.current

    Scaffold(
        topBar = { TopBar(navController, title = "Ustawienia") },
        scaffoldState = scafoldState
    ) {

        var showCustomDialogWithResult by remember { mutableStateOf(false) }

        Column() {
            Text("Zaplanuj powiadomienia", modifier = Modifier.padding(8.dp))
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


            Text("Opcje importu i exportu", modifier = Modifier.padding(8.dp))
            ListItem(modifier = Modifier.clickable {
                showCustomDialogWithResult = true

            }, icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_file_upload_24),
                    contentDescription = null
                )
            }) {
                Text("Import")
            }
            ListItem(
                modifier = Modifier.clickable {
                    saveBackup(context)
                },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_file_upload_24),
                        contentDescription = null
                    )
                }) {
                Text("Export")
            }
        }




        if (showCustomDialogWithResult) {
            AlertDialog(
                properties = DialogProperties(
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true,
                ),
                onDismissRequest = {
                    showCustomDialogWithResult = false
                },
                title = {
                    Text("Operacja usunie cały dotychczasowy postęp. \nCzy chcesz kontynuować?")
                },
                confirmButton = {
                    TextButton(onClick = {
                        showCustomDialogWithResult = false
                        loadBackup(context)
                    }) {
                        Text("Ok")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showCustomDialogWithResult = false
                    }) {
                        Text("Anuluj")
                    }
                }
            )
        }
    }
}

fun saveBackup(ctx: Context) {
    val screen = ctx as MainActivity
    screen.saveExportLocation()
}

fun loadBackup(ctx: Context) {
    val screen = ctx as MainActivity
    screen.loadExportedDataPicker()
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